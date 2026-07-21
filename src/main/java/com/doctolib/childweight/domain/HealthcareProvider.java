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
    private String facilityId; //Hospital Name

    protected HealthcareProvider() {}

    public HealthcareProvider(String name, String facilityId) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty");
        if (facilityId == null || facilityId.isBlank()) throw new IllegalArgumentException("Facility ID cannot be empty");
        this.name = name.trim();
        this.facilityId = facilityId.trim();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getFacilityId() { return facilityId; }
}