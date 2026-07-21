package com.doctolib.childweight.dto;

public record ChildGrowthSummaryResponse(
		Long childId, 
		String firstname, 
		String lastname, 
		double latestweight,
		double growthVelocity, 
		boolean requiresdoctorReview

) {}
