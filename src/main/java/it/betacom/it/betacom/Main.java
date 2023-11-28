package it.betacom.it.betacom;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.betacom.operation.CsvLoader;
import it.betacom.operation.DbHandler;
import it.betacom.operation.DbManager;
import it.betacom.operation.Estrazione;
import it.betacom.operation.PdfGenerator;

public class Main {
	
	private static final Logger logger = LogManager.getLogger(Main.class);
	
	    public static void main( String[] args ){
	    	Connection connection = null;
			try {
				connection = DbHandler.getConnection();
				DbManager.inizializzaDb();
				CsvLoader.caricaDati(connection, "./esercizioPartecipanti.csv");
				
				Scanner scanner = new Scanner(System.in);
				
				while(true) {
					System.out.println("1. Estrai un partecipante a caso");
					System.out.println("2. Stampa statistica su file Pdf");
					System.out.println("3. Rinizializza processo");
					System.out.println("4. Esci");
					
					int scelta = scanner.nextInt();
					
					switch(scelta) {
					
					case 1:
						Estrazione.estraiPartecipante(connection);
						break;
					
					case 2: 
						PdfGenerator.stampaStatistiche(connection);
						break;
					
					case 3:
						DbManager.rinizializzaProcesso(connection);
						break;
					
					case 4:
						scanner.close();
						System.exit(0);
						break;
					
					default:
						System.out.println("Scelta non valida, riprova");
					}
				}
			} catch (SQLException e1) {
				logger.error("Errore durante l'esecuzione del programma", e1);
			} finally {
				if(connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						logger.error("Errore nella chiusura della connessione", e);
					}
				}
			}
		}
	}