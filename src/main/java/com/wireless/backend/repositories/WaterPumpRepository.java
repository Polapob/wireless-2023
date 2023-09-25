package com.wireless.backend.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wireless.backend.db.IPostgreSQLJDBC;
import com.wireless.backend.models.SoilMoisture;
import com.wireless.backend.models.WaterPumpEvent;

@Repository
public class WaterPumpRepository implements IWaterPumpRepository {
	private IPostgreSQLJDBC db;

	@Autowired
	public WaterPumpRepository(IPostgreSQLJDBC db) {
		super();
		this.db = db;
	}

	public SoilMoisture createSoilMoisture(SoilMoisture soilMoisture) throws Exception {
		var sql = "INSERT INTO SOIL_MOISTURE(id,moisture,moisture_percentage,create_at) VALUES(?,?,?,?)";
		PreparedStatement stmt = createPrepareStatement(sql);
		stmt.setString(1, soilMoisture.getId().toString());
		stmt.setInt(2, soilMoisture.getMoisture());
		stmt.setDouble(3, soilMoisture.getMoisturePercentage());
		stmt.setTimestamp(4, new Timestamp(soilMoisture.getCreateAt().getTime()));
		stmt.executeUpdate();

		return soilMoisture;
	}

	public WaterPumpEvent createWaterPumpEvent(WaterPumpEvent waterPumpEvent) throws Exception {
		var sql = "INSERT INTO WATER_PUMP_EVENT(id,moisture_id,create_at) VALUES(?,?,?)";
		PreparedStatement stmt = createPrepareStatement(sql);
		stmt.setString(1, waterPumpEvent.getId().toString());
		stmt.setString(2, waterPumpEvent.getMoistureId().toString());
		stmt.setTimestamp(3, new Timestamp(waterPumpEvent.getCreateAt().getTime()));
		stmt.executeUpdate();
		return waterPumpEvent;
	}

	private PreparedStatement createPrepareStatement(String sql) throws Exception, SQLException {
		Connection connection = db.createConnection();
		PreparedStatement stmt = connection.prepareStatement(sql);
		return stmt;
	}

	@Override
	public WaterPumpEvent getLatestWaterPumpEvent() throws Exception {
		try {
			var sql = "SELECT id,moisture_id,create_at FROM WATER_PUMP_EVENT ORDER BY create_at DESC LIMIT 1";
			Connection connection = db.createConnection();
			var result = connection.createStatement().executeQuery(sql);
			
			if (result == null) {
				return new WaterPumpEvent(null, null, null);
			}
			if (result.next()) {
				var waterPumpEvent = new WaterPumpEvent(UUID.fromString(result.getString("id")), new Date(result.getTimestamp("create_at").getTime()),
						UUID.fromString(result.getString("moisture_id")));
				return waterPumpEvent;	
			}
			return new WaterPumpEvent(null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new WaterPumpEvent(null, null, null);
		}
	}

}
