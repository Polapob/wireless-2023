package com.wireless.backend.controllers;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.util.concurrent.AtomicDouble;
import com.wireless.backend.dtos.OpenPumpRequest;
import com.wireless.backend.dtos.OpenPumpResponse;
import com.wireless.backend.exceptions.BadRequestException;
import com.wireless.backend.models.SoilMoisture;
import com.wireless.backend.models.WaterPumpEvent;
import com.wireless.backend.repositories.IWaterPumpRepository;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;

@RestController
@RequestMapping("/waterpump")
public class WaterPumpController {
	private IWaterPumpRepository waterPumpRepository;
	private AtomicDouble gaugeMoisture;
	private Counter countWaterPumpRuns;
	private Counter countMoistureBelowThreshould;

	@Autowired
	public WaterPumpController(IWaterPumpRepository waterPumpRepository) {
		this.waterPumpRepository = waterPumpRepository;
		this.gaugeMoisture = Metrics.gauge("soil.moisture", new AtomicDouble(0)); 
		this.countWaterPumpRuns = Metrics.counter("water.pump.run");
		this.countMoistureBelowThreshould = Metrics.counter("soil.moisture.below.");
	}

	@PostMapping("/openPump")
	public OpenPumpResponse openPump(@RequestBody OpenPumpRequest request) throws Exception {
		int moisture = request.moisture;

		if (moisture < 0 || moisture > 1023) {
			throw new BadRequestException("moisture value is invalid");
		}

		double moisturePercentage = 100 - (moisture * 100 / 1023);
		SoilMoisture soilMoisture = new SoilMoisture(UUID.randomUUID(), new Date(), moisture, moisturePercentage);
		
		this.gaugeMoisture.set(moisturePercentage);

		waterPumpRepository.createSoilMoisture(soilMoisture);

		OpenPumpResponse response = new OpenPumpResponse(false, null);

		if (moisturePercentage > 40) {
			return response;
		}
		
		countMoistureBelowThreshould.increment();

		var currentEvent = waterPumpRepository.getLatestWaterPumpEvent();

		if (currentEvent.getCreateAt() == null) {
			var event = waterPumpRepository
					.createWaterPumpEvent(new WaterPumpEvent(UUID.randomUUID(), new Date(), soilMoisture.getId()));
			countWaterPumpRuns.increment();
			return new OpenPumpResponse(true, event.getId());
		}

		var hourInMilliSecond = 1000 * 60 * 60;
		var sixHour = 6;
		var hourDiff = (soilMoisture.getCreateAt().getTime() - currentEvent.getCreateAt().getTime())
				/ hourInMilliSecond;

		if (hourDiff > sixHour) {
			var event = waterPumpRepository
					.createWaterPumpEvent(new WaterPumpEvent(UUID.randomUUID(), new Date(), soilMoisture.getId()));
			countWaterPumpRuns.increment();
			return new OpenPumpResponse(true, event.getId());
		}

		return response;
	};

}
