package org.quantum.dao;

import java.sql.DriverManager;
import java.sql.SQLException;

public class UserDao {

	public boolean delete(Integer userId) throws SQLException {
		try (var connection = DriverManager.getConnection("url", "username", "password")) {
			return true;
		}
	}
}
