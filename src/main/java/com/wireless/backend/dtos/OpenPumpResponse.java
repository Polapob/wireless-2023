package com.wireless.backend.dtos;

import java.util.UUID;

public class OpenPumpResponse {
	public boolean isOpen;
	public UUID pumpEventId;
	
	public OpenPumpResponse(boolean isOpen, UUID pumpEventId) {
		this.isOpen = isOpen;
		this.pumpEventId = pumpEventId;
	}
}
