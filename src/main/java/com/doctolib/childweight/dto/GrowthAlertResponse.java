package com.doctolib.childweight.dto;

import java.time.LocalDateTime;

public record GrowthAlertResponse(Long childId, boolean hasAlert, double latestVelocity, boolean hasWeightDrop,
		String alertMessage, LocalDateTime evaluatedAt

) {

	//If true
	public static GrowthAlertResponse alert(Long childId, double velocity, boolean severeDrop, String msg) {
		return new GrowthAlertResponse(childId, true, velocity, severeDrop, msg, LocalDateTime.now());
	}

	//If everything is normal
	public static GrowthAlertResponse noAlert(Long childId, double velocity) {
		return new GrowthAlertResponse(childId, false, velocity, false, "Growth trajectory is within normal range.",
				LocalDateTime.now());
	}

}
