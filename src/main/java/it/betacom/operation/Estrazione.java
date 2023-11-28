package it.betacom.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Estrazione {
	
	private static final Logger logger = LogManager.getLogger(Estrazione.class);
	
	public static void estraiPartecipante(Connection connection) {
		try {
			Statement stm = connection.createStatement();
			int partecipanteId = getPartecipanteCasualeId(stm);
			
			if(partecipanteId != -1) {
				logger.info("Partecipante estratto con successo");
				showDatiPartecipante(connection, partecipanteId);
				saveEstrazione(connection, partecipanteId);
			} else {
				logger.warn("Nessun partecipante disponibile per l'estrazione");
			}
		} catch (SQLException e) {
			logger.error("Errore Sql durante l'estrazione del partecipante", e);
		}
	}
	

	private static int getPartecipanteCasualeId(Statement stm) throws SQLException {
		
		ResultSet resultSet = stm.executeQuery("SELECT id FROM partecipanti ORDER BY RAND() LIMIT 1");
		
		if(resultSet.next()) {
			return resultSet.getInt("id");
		}
		
		return -1;
	}
	
	private static void showDatiPartecipante(Connection connection, int partecipanteId) {
		
		try {
			Statement stm = connection.createStatement();
			ResultSet resultSet = stm.executeQuery("SELECT * FROM partecipanti WHERE id = " + partecipanteId);
			
			while(resultSet.next()) {
				System.out.println("ID: " + resultSet.getInt("id"));
				System.out.println("Nome: " + resultSet.getString("nome"));
				System.out.println("Sede: " + resultSet.getString("sede"));
			}
		} catch (SQLException e) {
			logger.error("Errore durante la visualizzazione dei dati del partecipante", e);
		}
		
	}
	
	
	private static void saveEstrazione(Connection connection, int partecipanteId) {
		try {
			// Incrementa il valore della colonna 'estrazioni' per il partecipante estratto
			PreparedStatement updateStm = connection.prepareStatement("UPDATE partecipanti SET estrazioni = estrazioni + 1 " +  
																"WHERE id = ?");
			
			updateStm.setInt(1, partecipanteId);
			updateStm.executeUpdate();
			
			//Salviamo l'estrazione nella tabella estrazioni
			PreparedStatement insertStm = connection.prepareStatement("INSERT INTO estrazioni (idPartecipante) VALUES (?)");
			insertStm.setInt(1, partecipanteId);
			insertStm.executeUpdate();
			logger.info("Estrazione salvata con successo");
		} catch (SQLException e) {
			logger.error("Errore Sql durante il salvataggio dell'estrazione", e);
		}
	}
	
}

















