package interfacciaDB;

/**
 * classe che contiene tutti i prepared statement che verranno utlizzati 
 * nelle chiamate al database
 * @author fritz
 *
 */
public class StatementsDB {

	static String aggiornaCrediti = // OK
			"UPDATE utenti "
			+ "SET crediti = ?, crediti_giornalieri = ? "
			+ "WHERE userid = ?";
	
	static String aggiornaCreditiPeriodico = // OK
			"UPDATE utenti "
			+ "SET crediti = ? "
			+ "WHERE userid = ?";
	
	static String resetCreditiGiornalieri = // OK
			"UPDATE utenti "
			+ "SET crediti_giornalieri = 0 "
			+ "WHERE userid = ?";
	
	static String aggiungiUtente = // OK
			"INSERT INTO utenti (userid, password, nome, cognome, ultimo_login) "
			+ "VALUES (?,?,?,?,?)";
	
	static String getUtente = // OK
			"SELECT * "
			+ "FROM utenti "
			+ "WHERE USERID = ?";
	
	static String getClassifica = // OK
			"SELECT userid, password, nome, cognome, crediti "
			+ "FROM utenti "
			+ "ORDER BY crediti DESC";
	
	static String getClassificaGiornaliera = // OK
			"SELECT userid, password, nome, cognome, crediti_giornalieri "
			+ "FROM utenti "
			+ "ORDER BY crediti_giornalieri DESC";
	
	static String getUltimoLogin = //OK
			"SELECT ultimo_login "
			+ "FROM utenti "
			+ "WHERE userid = ?";
	
	static String setUltimoLogin = //OK
			"UPDATE utenti "
			+ "SET ultimo_login = ? "
			+ "WHERE userid = ?";
}
