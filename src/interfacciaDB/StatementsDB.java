package interfacciaDB;

public class StatementsDB {

	static String aggiornaCrediti = // OK
			"UPDATE utenti"
			+ "SET crediti = ?, crediti_giornalieri = ?"
			+ "WHERE userid = ?";
	
	static String aggiornaCreditiPeriodico = // OK
			"UPDATE utenti"
			+ "SET crediti = ?"
			+ "WHERE userid = ?";
	
	static String resetCreditiGiornalieri = // OK
			"UPDATE utenti"
			+ "SET crediti_giornalieri = 0"
			+ "WHERE userid = ?";
	
	static String aggiungiUtente = // OK
			"INSERT INTO utenti (userid, pass, nome, cognome)"
			+ "VALUES (?,?,?,?)";
	
	static String getUtente = // OK
			"SELECT *"
			+ "FROM utenti"
			+ "WHERE userid = ?";
	
	static String getClassifica = // OK
			"SELECT userid, nome, cognome, crediti"
			+ "FROM utenti"
			+ "ORDER BY crediti DESC";
	
	static String getClassificaGiornaliera = // OK
			"SELECT userid, nome, cognome, crediti_giornalieri"
			+ "FROM utenti"
			+ "ORDER BY crediti_giornalieri DESC";
	
	static String getUltimoLogin = //TODO
			"SELECT ultimo_login"
			+ "FROM utenti"
			+ "WHERE userid = ?";
	
	static String setUltimoLogin = //TODO
			"UPDATE utenti"
			+ "SET ultimo_login = ?"
			+ "WHERE userid = ?";
}
