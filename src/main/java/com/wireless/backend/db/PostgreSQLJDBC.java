package com.wireless.backend.db;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PostgreSQLJDBC implements IPostgreSQLJDBC {
	@Value("${spring.datasource.url}")
	private String connectionString;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;

	public Connection createConnection() throws Exception {
		Connection c = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(connectionString, username, password);
			return c;
		} catch (Exception e) {
			throw e;
		}
	}
}