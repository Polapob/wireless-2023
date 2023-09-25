package com.wireless.backend.repositories;

import com.wireless.backend.models.SoilMoisture;
import com.wireless.backend.models.WaterPumpEvent;

public interface IWaterPumpRepository {
	public SoilMoisture createSoilMoisture(SoilMoisture soilMoisture) throws Exception;
	public WaterPumpEvent createWaterPumpEvent(WaterPumpEvent waterPumpEvent) throws Exception;
	public WaterPumpEvent getLatestWaterPumpEvent() throws Exception;
}
