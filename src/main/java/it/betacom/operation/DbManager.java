package it.betacom.operation;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DbManager {
	
	private static final Logger logger = LogManager.getLogger(DbManager.class);
	
	public static void inizializzaDb() {
		try {
			Statement stm = DbHandler.getConnection().createStatement();
			
			stm.executeUpdate("CREATE TABLE IF NOT EXISTS partecipanti (" +
			                    "id INT AUTO_INCREMENT PRIMARY KEY," +
			                    "nome VARCHAR(255) NOT NULL," +
			                    "sede VARCHAR(255) NOT NULL," +
			                    "estrazioni INT DEFAULT 0)");
			
			stm.executeUpdate("CREATE TABLE IF NOT EXISTS estrazioni (" +
			                    "id INT AUTO_INCREMENT PRIMARY KEY," +
			                    "idPartecipante INT," +
			                    "dataEstrazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
			                    "FOREIGN KEY (idPartecipante) REFERENCES partecipanti(id))");
			
			logger.info("Database inizializzato con successo");
		} catch (SQLException e) {
			logger.error("Errore durante l'inizializzazione del database", e);
		}
	}
	
	public static void rinizializzaProcesso(Connection connection) {
		try {
			Statement stm = DbHandler.getConnection().createStatement();
			stm.executeUpdate("DROP TABLE IF EXISTS estrazioni");
			stm.executeUpdate("DROP TABLE IF EXISTS partecipanti");
			
			inizializzaDb();
			logger.info("Processo rinizializzato con successo.");
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Errore durante la rinizializzazione del processo", e);
		}
	}
}

