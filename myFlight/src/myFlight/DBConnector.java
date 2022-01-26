package myFlight;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DBConnector {

	public static Connection getConnection() throws SQLException {

		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/myFlight", "root",
				"123Demo");

		return conn;
	}

}