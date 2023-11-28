package it.betacom.operation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfGenerator {

	private static final Logger logger = LogManager.getLogger(PdfGenerator.class);
	
	public static void stampaStatistiche(Connection connection) {
		Document document = new Document();
		
		try {
			PdfWriter.getInstance(document, new FileOutputStream("./statistiche/statistiche.pdf"));
			document.open();
			
			aggiungiContenuto(document, connection);
		} catch (FileNotFoundException | DocumentException e) {
			logger.error("Errore durante la creazione del documento PDF", e);
		} finally {
			document.close();
			try {
				if(connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch(SQLException e) {
				logger.error("Errore durante la chiusura della connessione", e);
			}
		}
	}

	private static void aggiungiContenuto(Document document, Connection connection) {
		try {
			Statement stm = connection.createStatement();
			ResultSet resultSet = stm.executeQuery("SELECT * FROM partecipanti ORDER BY id DESC");
			
			while(resultSet.next()) {
				document.add(new Paragraph("Id: " + resultSet.getInt("id")));
				document.add(new Paragraph("Nome: " + resultSet.getString("nome")));
				document.add(new Paragraph("Sede: " + resultSet.getString("sede")));
				
				aggiungiInformazioni(document, connection, resultSet.getInt("id"));
			}
			logger.info("Documento generato correttamente");
		} catch (SQLException e) {
			logger.error("Errore Sql durante l'aggiunta del contenuto al documento", e);
		} catch (DocumentException e) {
			logger.error("Errore del documento durante l'aggiunta del contenuto al documento", e);
		}
		
	}

	private static void aggiungiInformazioni(Document document, Connection connection, int idPartecipante) {
		try {
			Statement stm = connection.createStatement();
			ResultSet resultSet = stm.executeQuery("SELECT COUNT(*) AS estrazioni, " +
													"MAX(dataEstrazione) AS ultima_estrazione " +
													"FROM estrazioni WHERE idPartecipante =" + idPartecipante);
			
			while(resultSet.next()) {
				document.add(new Paragraph("Numero di volte estratto: " + resultSet.getInt("estrazioni")));
				document.add(new Paragraph("Ultima estrazione: " + resultSet.getString("ultima_estrazione")));
				document.add(new Paragraph("\n"));
			}
		} catch (SQLException e) {
			
			logger.error("Errore Sql durante l'aggiunta delle informazioni al documento", e);
		} catch (DocumentException e) {
			logger.error("Errore del documento durante l'aggiunta delle informazioni al documento", e);
		}
		
	}
}
