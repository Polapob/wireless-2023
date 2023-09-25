package com.wireless.backend.models;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class WaterPumpEvent {
	private UUID id;
	private Date createAt;
	private UUID moistureId;
	
	

	public WaterPumpEvent(UUID id, Date createAt, UUID moistureId) {
		this.id = id;
		this.createAt = createAt;
		this.moistureId = moistureId;
	}

	public UUID getId() {
		return id;
	}

	public UUID getMoistureId() {
		return moistureId;
	}
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setMoistureId(UUID moistureId) {
		this.moistureId = moistureId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WaterPumpEvent other = (WaterPumpEvent) obj;
		return Objects.equals(createAt, other.createAt) && Objects.equals(id, other.id)
				&& Objects.equals(moistureId, other.moistureId);
	}

	@Override
	public String toString() {
		return "WaterPumpEvent [id=" + id + ", openAt=" + createAt + ", moistureId=" + moistureId + "]";
	}
}
