package com.doctolib.childweight.dto;

import java.time.LocalDateTime;

import com.doctolib.childweight.domain.Child;

public record ChildResponse(Long childId, String firstName, String lastName, LocalDateTime dateOfBirth

) {

	public static ChildResponse fromEntity(Child child) {
		return new ChildResponse(child.getId(), child.getFirstName(), child.getLastName(), child.getDateOfBirth());
	}

}
