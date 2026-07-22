package com.doctolib.childweight.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doctolib.childweight.dto.GrowthAlertResponse;
import com.doctolib.childweight.service.GrowthAlertService;

//import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/children")
//@RequiredArgsConstructor
public class GrowthAlertController {

	private final GrowthAlertService growthAlertService;

	public GrowthAlertController(GrowthAlertService gas) {
		this.growthAlertService = gas;
	}

	@GetMapping("/{childId}/growth-alert")
	public ResponseEntity<GrowthAlertResponse> evaluateGrowthAlert(@PathVariable Long childId,
			@RequestHeader("X-Provider-Id") Long requestingProviderId) {

		// Delegate to service (provider ID check ensures multi-tenant access control)
		GrowthAlertResponse response = growthAlertService.evaluateChildGrowth(childId, requestingProviderId);

		return ResponseEntity.ok(response);
	}
}
