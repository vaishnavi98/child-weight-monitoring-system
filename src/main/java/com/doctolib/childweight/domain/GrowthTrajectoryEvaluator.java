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

		long daysBetween = ChronoUnit.DAYS.between(prev.getRecordedAt(), lat.getRecordedAt());

		if (daysBetween <= 0)
			return 0.0;

		double weightdiff = (lat.getWeightInKg() - prev.getWeightInKg()) * 1000.0;

		double velocity =  weightdiff / daysBetween;
		
		return velocity;
	}

	// Check if any significant weight loss for the child is occurred.
	public boolean hasweightdrop(Child child) {
		Optional<WeightMeasurement> latestWeight = child.getLatestWeight();
		Optional<WeightMeasurement> prevWeight = child.getPreviousWeight();

		if (latestWeight.isEmpty() || prevWeight.isEmpty())
			return false;

		WeightMeasurement lat = latestWeight.get();
		WeightMeasurement prev = prevWeight.get();

		long daysBetween = ChronoUnit.DAYS.between(prev.getRecordedAt(), lat.getRecordedAt());

		if (daysBetween <= 30 && prev.getWeightInKg() > 0) {
			double hasWeightDrop = (prev.getWeightInKg() - lat.getWeightInKg()) / prev.getWeightInKg();
			return hasWeightDrop >= 0.05;
		}

		return false;

	}

}
