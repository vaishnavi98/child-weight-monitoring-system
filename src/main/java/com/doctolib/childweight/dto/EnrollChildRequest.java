package com.doctolib.childweight.dto;

import java.time.LocalDate;

import com.doctolib.childweight.domain.Child.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record EnrollChildRequest(

		@NotBlank(message = "Firstname should not be blank") String firstName,

		@NotBlank(message = "Lastname should not be blank") String lastName,

		@NotNull(message = "DOB should not be null") @PastOrPresent(message = "Date Of Birth cannot be in the future") LocalDate dateOfBirth,

		@NotNull(message = "Gender should not be null") Gender gender,
		
		@NotNull(message = "Provider ID is required") Long providerId) {
}
