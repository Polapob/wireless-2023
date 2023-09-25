package com.wireless.backend.models;

import java.util.Date;
import java.util.UUID;

public class SoilMoisture {
	private UUID id;
	private Date createAt;
	private int moisture;
	private Double moisturePercentage;
	
	public SoilMoisture(UUID id, Date createAt, int moisture, Double moisturePercentage) {
		this.id = id;
		this.createAt = createAt;
		this.moisture = moisture;
		this.moisturePercentage = moisturePercentage;
	}

	public UUID getId() {
		return id;
	}

	public int getMoisture() {
		return moisture;
	}

	public Double getMoisturePercentage() {
		return moisturePercentage;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setMoisture(int moisture) {
		this.moisture = moisture;
	}

	public void setMoisturePercentage(Double moisturePercentage) {
		this.moisturePercentage = moisturePercentage;
	}
	
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	@Override
	public String toString() {
		return "SoilMoisture [id=" + id + ", measureAt=" + createAt + ", moisture=" + moisture
				+ ", moisturePercentage=" + moisturePercentage + "]";
	}

}
