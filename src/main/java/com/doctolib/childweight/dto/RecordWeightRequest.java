package com.doctolib.childweight.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecordWeightRequest (
		
		@NotNull(message = "Weight value is required")
		@Positive(message = "Weight must be positive")
		Double weight,
		
		@NotNull(message = "Provider Id is required")
		Long providerId
		
		) {}
