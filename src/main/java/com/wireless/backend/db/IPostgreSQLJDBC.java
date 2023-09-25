package com.wireless.backend.db;

import java.sql.Connection;

public interface IPostgreSQLJDBC {
	Connection createConnection() throws Exception;
}
