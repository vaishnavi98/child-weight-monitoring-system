package com.doctolib.childweight.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class WeightMeasurement {
	
	public enum AuditStatus{
		VERIFIED,
		PENDING_VERIFICATION
	}

    @Column(name = "weight_in_kg", nullable = false)
    private double weightInKg;

    @Column(name = "provider_id", nullable = false)
    private Long providerId;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "audit_status", nullable = false)
    private AuditStatus status;

    public WeightMeasurement() {}

    public WeightMeasurement(double weightInKg, Long providerId, AuditStatus status) {
        if (weightInKg <= 2.0 || weightInKg >= 30.0) {
            throw new IllegalArgumentException("Weight must be between 2.0kg and 30.0kg");
        }
        if (providerId == null) {
        	throw new IllegalArgumentException("ProviderId cannot be null" + providerId);
        }
        this.weightInKg = weightInKg;
        this.providerId = providerId;
        this.recordedAt = LocalDateTime.now();
        this.status = status!= null? status : AuditStatus.VERIFIED;
    }

    public void setWeightInKg(double weightInKg) {
		this.weightInKg = weightInKg;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public void setRecordedAt(LocalDateTime recordedAt) {
		this.recordedAt = recordedAt;
	}

	public void setStatus(AuditStatus status) {
		this.status = status;
	}

	public double getWeightInKg() { return weightInKg; }
    public Long getProviderId() { return providerId; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public AuditStatus getAuditStatus() { return status; }
}