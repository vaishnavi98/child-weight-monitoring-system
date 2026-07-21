package com.doctolib.childweight.domain;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class GrowthTrajectoryEvaluator {

	
	//To calculate the weight increase or decrease based on days for the child.
	public double calculateLatestVelocityGramsPerDay(Child child) {
		Optional<WeightMeasurement> latestweight = child.getLatestWeight();
		Optional<WeightMeasurement> prevweight = child.getPreviousWeight();

		if (latestweight.isEmpty() || prevweight.isEmpty())
			return 0.0;

		WeightMeasurement lat = latestweight.get();
		WeightMeasurement prev = prevweight.get();

		long daysbetween = ChronoUnit.DAYS.between(prev.getRecordedAt(), lat.getRecordedAt());

		if (daysbetween <= 0)
			return 0.0;

		double weightdiff = (lat.getWeightInKg() - prev.getWeightInKg());

		return weightdiff / daysbetween;
	}

	// Check if any significant weight loss for the child is occured.
	public boolean hasweightdrop(Child child) {
		Optional<WeightMeasurement> latestweight = child.getLatestWeight();
		Optional<WeightMeasurement> prevweight = child.getPreviousWeight();

		if (latestweight.isEmpty() || prevweight.isEmpty())
			return false;

		WeightMeasurement lat = latestweight.get();
		WeightMeasurement prev = prevweight.get();

		long daysbetween = ChronoUnit.DAYS.between(prev.getRecordedAt(), lat.getRecordedAt());

		if (daysbetween <= 30 && prev.getWeightInKg() > 0) {
			double weightdrop = (prev.getWeightInKg() - lat.getWeightInKg()) / prev.getWeightInKg();
			return weightdrop >= 0.05;
		}

		return false;

	}

}
