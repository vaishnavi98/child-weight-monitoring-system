package com.doctolib.childweight.dto;

import java.time.LocalDateTime;

import com.doctolib.childweight.domain.WeightMeasurement.AuditStatus;

public record WeightMeasurementResponse(

		double weight, String unit, Long providerId, LocalDateTime recordedAt, AuditStatus status

) {
	public WeightMeasurementResponse {
		if (unit == null || unit.isBlank()) {
			unit = "kg";
		}
	}
}
