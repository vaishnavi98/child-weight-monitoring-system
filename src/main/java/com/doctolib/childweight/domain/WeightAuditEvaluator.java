package com.doctolib.childweight.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.doctolib.childweight.domain.WeightMeasurement.AuditStatus;

@Component
public class WeightAuditEvaluator {

	private static final double MAX_DEVIATION_THRESHOLD = 0.20;// 20%
	private static final long THRESHOLD_DAYS = 30;

	public AuditStatus evaluteAuditStatus(Child child, double newWeight) {
		Optional<WeightMeasurement> latest = child.getLatestWeight(); // It forces to acknowledge that the weight might not exist

		if (latest.isEmpty())
			return AuditStatus.VERIFIED;

		if (latest.isPresent()) {
			WeightMeasurement latestweight = latest.get(); // If it's empty throws a NoSuchElementException
			long daysSinceLastRecord = ChronoUnit.DAYS.between(latestweight.getRecordedAt(), LocalDateTime.now());

			if (daysSinceLastRecord <= THRESHOLD_DAYS) {
				double prev = latestweight.getWeightInKg();
				double deviation = Math.abs(newWeight - prev) / prev;

				if (deviation > MAX_DEVIATION_THRESHOLD) {
					return AuditStatus.PENDING_VERIFICATION;
				}
			}
		}

		return AuditStatus.VERIFIED;
	}
}
