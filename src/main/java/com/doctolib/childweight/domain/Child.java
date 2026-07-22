package com.doctolib.childweight.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.doctolib.childweight.domain.WeightMeasurement.AuditStatus;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "children")
public class Child {
	
	public enum Gender{
		MALE,
		FEMALE
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "date_of_birth", nullable = false)
	private LocalDateTime dateOfBirth;

    @Enumerated(EnumType.STRING)
	@Column(name = "gender", nullable = false)
	private Gender gender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "enrolling_provider_id", nullable = false)
	private HealthcareProvider enrollingProvider;

	@ElementCollection
	@CollectionTable(name = "child_weight_measurements", joinColumns = @JoinColumn(name = "child_id"))
	@OrderBy("recordedAt ASC")
	private List<WeightMeasurement> weights = new ArrayList<>();

	protected Child() {
		// This is necessary for JPA default constructor
	}

	public Child(String firstName, String lastName, LocalDateTime dateOfBirth, Gender gender, HealthcareProvider enrollingProvider) {
		if (firstName == null || firstName.isBlank())
			throw new IllegalArgumentException("First name cannot be empty");
		if (lastName == null || lastName.isBlank())
			throw new IllegalArgumentException("Last name cannot be empty");
		if (dateOfBirth == null)
			throw new IllegalArgumentException("Date of birth is required");
		if (gender == null)
            throw new IllegalArgumentException("Gender is required");
		if (enrollingProvider == null)
			throw new IllegalArgumentException("Enrolling provider is required");

		this.firstName = firstName.trim();
		this.lastName = lastName.trim();
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.enrollingProvider = enrollingProvider;
	}

	public void addWeight(double weight, Long recordingProviderId, AuditStatus status) {
		this.weights.add(new WeightMeasurement(weight, recordingProviderId, status));
	}

	public String getEnrollingFacilityId() {
		return this.enrollingProvider != null ? this.enrollingProvider.getFacilityId() : null;
	}

	public Optional<WeightMeasurement> getLatestWeight() {
		return weights.isEmpty() ? Optional.empty() : Optional.of(weights.get(weights.size() - 1));
	}

	public Optional<WeightMeasurement> getPreviousWeight() {
		return weights.size() < 2 ? Optional.empty() : Optional.of(weights.get(weights.size() - 2));
	}

	public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public HealthcareProvider getEnrollingProvider() {
        return enrollingProvider;
    }

    public List<WeightMeasurement> getWeights() {
        return weights;
    }
}