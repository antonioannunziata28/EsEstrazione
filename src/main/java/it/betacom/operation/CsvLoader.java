package it.betacom.operation;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CsvLoader {
	
	private static final Logger logger = LogManager.getLogger(CsvLoader.class);
	
	public static void caricaDati(Connection connection, String filePath) {
		try {
//			Statement stm = connection.createStatement();
//			stm.executeUpdate("DELETE FROM partecipanti");
//			stm.executeUpdate("LOAD DATA INFILE './esercizioPartecipanti.csv'" +
//                    " INTO TABLE partecipanti" +
//                    " FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' IGNORE 1 LINES");
			
			//Creo uno scanner per leggere il file CSV
			Scanner scanner = new Scanner(new File(filePath));
			
			//Salto la prima riga se contiene l'intestazione
			if(scanner.hasNextLine()) {
				scanner.nextLine();
			}
			
			//Preparo l'inserimento
			String insertQuery = "INSERT INTO partecipanti (nome, sede) VALUES (?,?)";
			try {
				PreparedStatement pStm = connection.prepareStatement(insertQuery);
				
				//Itero attraverso le righe del file 
				while(scanner.hasNextLine()) {
					String line = scanner.nextLine();
					String[] fields = line.split(";");
					
					//Mi assicuro che la riga ha almeno due campi
					if(fields.length >= 2) {
						String nome = fields[0];
						String sede = fields[1];
						
						//Impostiamo i paramentri nella queri di inserimento 
						pStm.setString(1, nome);
						pStm.setString(2, sede);
						
						//Eseguiamo la query
						pStm.executeUpdate();
					}
				}
				logger.info("Database inizializzato con successo.");
				scanner.close();
			} catch (SQLException e) {
				logger.error("Errore nell'inizializzare il db " + e);
			}
			
		} catch (FileNotFoundException e) {
			logger.error("File partecipanti non trovato, verifica il path " + e);
		}
	}
}
