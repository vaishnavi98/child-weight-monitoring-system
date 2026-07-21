package com.doctolib.childweight.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doctolib.childweight.domain.Child;
import com.doctolib.childweight.dto.EnrollChildRequest;
import com.doctolib.childweight.dto.RecordWeightRequest;
import com.doctolib.childweight.dto.WeightMeasurementResponse;
import com.doctolib.childweight.service.ChildService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/children")
public class ChildController {

	private final ChildService childService;

	public ChildController(ChildService childService) {
		this.childService = childService;
	}

	@PostMapping
	public ResponseEntity<Child> enrollChild(@Valid @RequestBody EnrollChildRequest enrollReq) {
		Child enrollChild = childService.enrollChild(enrollReq);

		return ResponseEntity.status(HttpStatus.CREATED).body(enrollChild);

	}

	@PostMapping("api/{id}/weight")
	public ResponseEntity<Void> recordWeight(@PathVariable("id") Long childId,
			@Valid @RequestBody RecordWeightRequest req) {
		childService.recordWeight(childId, req);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}/weights")
	public ResponseEntity<Page<WeightMeasurementResponse>> getWeightHistory(
			@PathVariable("id") Long childId,
			@PathVariable("id") Long providerId,
			@PageableDefault(size = 10) Pageable page) 
	{
		Page<WeightMeasurementResponse> history = childService.getChildWeightHistory(childId, providerId, page);
		return ResponseEntity.ok(history);
	}

}
