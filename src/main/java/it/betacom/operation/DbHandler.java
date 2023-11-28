package it.betacom.operation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbHandler {
	
	private static final String jdbc_url = "jdbc:mysql://localhost:3306/esEstrazione";
	private static final String username = "root";
	private static final String password = "rootroot";
	
	private static Connection connection;
	
	private DbHandler() {
		// Costruttore privato per impedire la creazione di istanze multiple
	}
	
	public static Connection getConnection() throws SQLException {
		if(connection == null || connection.isClosed()) {
			connection = DriverManager.getConnection(jdbc_url, username, password);
		}
		return connection;
	}
	
	public static void closeConnection() throws SQLException {
		if(connection != null && !connection.isClosed()) {
			connection.close();
		}
	}
}
