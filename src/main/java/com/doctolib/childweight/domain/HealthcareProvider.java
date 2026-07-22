package com.doctolib.childweight.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "healthcare_providers")
public class HealthcareProvider {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "facility_id", nullable = false)
	private String facilityId; // Hospital Name

	@Column(name = "professional_id", unique = true, nullable = false)
	private String professionalId;

	@Column(name = "passwordHash", nullable = false)
	private String passwordHash;

	protected HealthcareProvider() {
	}

	public HealthcareProvider(String name, String facilityId, String professionalId, String passwordHash) {
		if (name == null || name.isBlank())
			throw new IllegalArgumentException("Name cannot be empty");
		if (facilityId == null || facilityId.isBlank())
			throw new IllegalArgumentException("Facility ID cannot be empty");
		if (professionalId == null || professionalId.isBlank())
			throw new IllegalArgumentException("Professional ID cannot be empty");
		if (passwordHash == null || passwordHash.isBlank())
			throw new IllegalArgumentException("Password cannot be empty");
		this.name = name.trim();
		this.facilityId = facilityId.trim();
		this.professionalId = professionalId;
		this.passwordHash = passwordHash;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public String getProfessionalId() {
		return professionalId;
	}

	public String getPasswordHash() {
		return passwordHash;
	}
}