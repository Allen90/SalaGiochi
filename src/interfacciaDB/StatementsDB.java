package interfacciaDB;

public class StatementsDB {

	static String aggiornaEntrambiCrediti = 
			"UPDATE utenti"
			+ "SET crediti = ?, crediti_giornalieri = ?"
			+ "WHERE userid = ?";

	static String aggiornaCrediti = 
			"UPDATE utenti"
			+ "SET crediti = ?"
			+ "WHERE userid = ?";
	
	static String aggiornaCreditiGiornalieri = 
			"UPDATE utenti"
			+ "SET crediti_giornalieri = ?"
			+ "WHERE userid = ?";
	
	static String aggiungiUtente = 
			"INSERT INTO utenti (userid, pass)"
			+ "VALUES (?,?)";
	
//	static String cercaUtente =
//			"SELECT COUNT(userid) AS presente"
//			+ "FROM utenti"
//			+ "WHERE userid = ?";
	
	static String getClassifica =
			"SELECT userid, crediti"
			+ "FROM utenti"
			+ "ORDER BY crediti DESC";
	
	static String getClassificaGiornaliera = 
			"SELECT userid, crediti_giornalieri"
			+ "FROM utenti"
			+ "ORDER BY crediti_giornalieri DESC";
	
	static String getStatUtente = 
			"SELECT partite_slot, crediti_slot, partite_tombola, vittorie_tombola, "
					+ "partite_rubamazzo, vittorie_rubamazzo"
			+ "FROM utenti"
			+ "WHERE userid = ?";
	
	static String getUltimoLogin = 
			"SELECT ultimo_login"
			+ "FROM utenti"
			+ "WHERE userid = ?";
	
	static String setUltimoLogin = 
			"UPDATE utenti"
			+ "SET ultimo_login = ?"
			+ "WHERE userid = ?";
}
