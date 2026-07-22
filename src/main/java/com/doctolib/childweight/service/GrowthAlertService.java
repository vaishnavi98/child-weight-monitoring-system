package com.doctolib.childweight.service;

import org.springframework.stereotype.Service;

import com.doctolib.childweight.domain.Child;
import com.doctolib.childweight.domain.GrowthTrajectoryEvaluator;
import com.doctolib.childweight.dto.GrowthAlertResponse;
import com.doctolib.childweight.exception.ChildNotFoundException;
import com.doctolib.childweight.repository.ChildRepository;

@Service
public class GrowthAlertService {

	private final ChildRepository childRepository;
	private final GrowthTrajectoryEvaluator trajectoryEvaluator;
	
	public GrowthAlertService(ChildRepository childRepo, GrowthTrajectoryEvaluator gte) {
		this.childRepository =  childRepo;
		this.trajectoryEvaluator = gte;
	}
	
	public GrowthAlertResponse evaluateChildGrowth(Long childId, Long requestingProviderId) {
		Child child = childRepository.findById(childId).orElseThrow(() -> new ChildNotFoundException(childId));
		
		// 2. Security Check: Validate requesting provider owns this child record
        if (!child.getEnrollingProvider().getId().equals(requestingProviderId)) {
            throw new IllegalArgumentException("Access Denied: Provider does not match the enrolling provider.");
        }
		
		double velocity = trajectoryEvaluator.calculateLatestVelocityGramsPerDay(child);
		boolean severeDrop = trajectoryEvaluator.hasweightdrop(child);
		
		if(severeDrop) {
			return GrowthAlertResponse.alert(childId, velocity, severeDrop, "Warning: Severe weight drop of 5% or more within 30 days.");
		}
		
		if (velocity < 10.0) {
            return GrowthAlertResponse.alert(childId, velocity, severeDrop, "Warning: Growth velocity is below 10g/day threshold.");
        }
		
		return GrowthAlertResponse.noAlert(childId, velocity);
	}
}
