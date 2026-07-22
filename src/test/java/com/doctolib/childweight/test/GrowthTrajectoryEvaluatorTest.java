package com.doctolib.childweight.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.doctolib.childweight.domain.Child;
import com.doctolib.childweight.domain.GrowthTrajectoryEvaluator;
import com.doctolib.childweight.domain.WeightMeasurement;

@ExtendWith(MockitoExtension.class)
public class GrowthTrajectoryEvaluatorTest {
	
	@Mock
	private Child child;
	
	private GrowthTrajectoryEvaluator evaluator;
	
	@BeforeEach
	void setUp() {
		evaluator = new GrowthTrajectoryEvaluator();
	}
	
	@Test
	void calculateVelocityTest() {
		LocalDateTime now = LocalDateTime.now();
		
		WeightMeasurement prev = new WeightMeasurement();
		prev.setWeightInKg(4.0);
		prev.setRecordedAt(now.minusDays(10));
		
		WeightMeasurement latest = new WeightMeasurement();
		latest.setWeightInKg(4.5);
		latest.setRecordedAt(now);
		
		when(child.getLatestWeight()).thenReturn(Optional.of(latest));
        when(child.getPreviousWeight()).thenReturn(Optional.of(prev));
        
        double velocity = evaluator.calculateLatestVelocityGramsPerDay(child);
        
        assertEquals(50.0, velocity, 0.001);
		
	}

}
