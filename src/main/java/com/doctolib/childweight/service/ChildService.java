package com.doctolib.childweight.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doctolib.childweight.domain.Child;
import com.doctolib.childweight.domain.GrowthTrajectoryEvaluator;
import com.doctolib.childweight.domain.HealthcareProvider;
import com.doctolib.childweight.domain.WeightAuditEvaluator;
import com.doctolib.childweight.domain.WeightMeasurement;
import com.doctolib.childweight.domain.WeightMeasurement.AuditStatus;
import com.doctolib.childweight.dto.ChildGrowthSummaryResponse;
import com.doctolib.childweight.dto.EnrollChildRequest;
import com.doctolib.childweight.dto.RecordWeightRequest;
import com.doctolib.childweight.dto.WeightMeasurementResponse;
import com.doctolib.childweight.exception.ChildNotFoundException;
import com.doctolib.childweight.exception.ProviderNotFoundException;
import com.doctolib.childweight.repository.ChildRepository;
import com.doctolib.childweight.repository.HealthcareProviderRepository;

@Service
public class ChildService {

	private final ChildRepository childRepository;
	private final HealthcareProviderRepository providerRepository;
	private final GrowthTrajectoryEvaluator growthTrajectoryEvaluator;
	private final WeightAuditEvaluator auditEvaluator;

	public ChildService(ChildRepository childRepository, HealthcareProviderRepository providerRepository,
			GrowthTrajectoryEvaluator growthTrajectoryEvaluator, WeightAuditEvaluator auditEvaluator) {
		this.childRepository = childRepository;
		this.providerRepository = providerRepository;
		this.growthTrajectoryEvaluator = growthTrajectoryEvaluator;
		this.auditEvaluator = auditEvaluator;
	}

	@Transactional
	public Child enrollChild(EnrollChildRequest request) {

		boolean duplicateExists = childRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndDateOfBirth(
				request.firstName(), request.lastName(), request.dateOfBirth());
		if (duplicateExists) {
			throw new IllegalArgumentException("A child with this same record exists");
		}

		HealthcareProvider provider = providerRepository.findById(request.providerId())
				.orElseThrow(() -> new ProviderNotFoundException(request.providerId()));

		Child child = new Child(request.firstName(), request.lastName(), request.dateOfBirth(), request.gender(),
				provider);

		return childRepository.save(child);
	}

	@Transactional
	public void recordWeight(Long childId, RecordWeightRequest request) {
		Child child = childRepository.findById(childId).orElseThrow(() -> new ChildNotFoundException(childId));

		HealthcareProvider recordingProvider = providerRepository.findById(request.providerId())
				.orElseThrow(() -> new ProviderNotFoundException(request.providerId()));

		validateEnrollingProvider(child, recordingProvider.getId());

		AuditStatus status = auditEvaluator.evaluteAuditStatus(child, request.weight());

		child.addWeight(request.weight(), recordingProvider.getId(), status);
	}

	@Transactional(readOnly = true)
	public ChildGrowthSummaryResponse evaluteGrowthTrajectory(Long childId) {

		Child child = childRepository.findByIdWithWeight(childId)
				.orElseThrow(() -> new ChildNotFoundException(childId));

		WeightMeasurement latest = child.getLatestWeight()
				.orElseThrow(() -> new IllegalArgumentException("No weight records found"));

		return new ChildGrowthSummaryResponse(child.getId(), child.getFirstName(), child.getLastName(),
				latest.getWeightInKg(), growthTrajectoryEvaluator.calculateLatestVelocityGramsPerDay(child),
				growthTrajectoryEvaluator.hasweightdrop(child));
	}

	@Transactional(readOnly = true)
	public Page<WeightMeasurementResponse> getChildWeightHistory(Long childId, Long requestingProviderId,
			Pageable page) {

		Child child = childRepository.findById(childId).orElseThrow(() -> new ChildNotFoundException(childId));

		validateEnrollingProvider(child, requestingProviderId);

		// As the business requirements asked for the latest date and history to be
		// appeared first
		List<WeightMeasurementResponse> allWeights = child.getWeights().stream()
				.sorted(Comparator.comparing(WeightMeasurement::getRecordedAt).reversed())
				.map(w -> new WeightMeasurementResponse(w.getWeightInKg(), "kg", w.getProviderId(), w.getRecordedAt(),
						w.getAuditStatus()))
				.toList();

		int start = (int) page.getOffset();
		int end = Math.min(start + page.getPageSize(), allWeights.size());

		if (start > allWeights.size()) {
			return new PageImpl<>(List.of(), page, allWeights.size());
		}

		List<WeightMeasurementResponse> pagecontent = allWeights.subList(start, end);

		return new PageImpl<>(pagecontent, page, allWeights.size());
	}

	private void validateEnrollingProvider(Child child, Long requestingProviderId) {
		if (!child.getEnrollingProvider().getId().equals(requestingProviderId)) {
			throw new IllegalStateException("Access Denied: Provider does not match who enrolled for this child.");
		}
	}
	
	@Transactional
	public void deleteChild(Long childId) {
		if (!childRepository.existsById(childId)) {
			throw new ChildNotFoundException(childId);
		}
		childRepository.deleteById(childId);
	}
}