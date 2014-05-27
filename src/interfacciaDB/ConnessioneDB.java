package interfacciaDB;

import java.sql.*;
import java.util.ArrayList;

import eccezioni.EccezioneClassificaVuota;
import eccezioni.EccezioneUtente;
import eccezioni.EccezioneUtenteNonTrovato;
import model.Utente;

public class ConnessioneDB {

	private static Connection con = null;  
    private static ConnessioneDB dbc = null;
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String PATH="jdbc:derby:DBSalagiochi";
	private static final String USER="user";
	private static final String PWD="pass";
	private static String DBErr_1 = "Errore nel caricamento del Database!";
	private static String DBErr_2 = "Errore nella modifica del Database!";
	
	private ConnessioneDB() {  
          
        try {                
            Class.forName(DRIVER).newInstance();   
            System.out.println("connessione...");
            con = DriverManager.getConnection(PATH, USER, PWD);  
            System.out.println("connessione creata");
        }            
        catch(Exception e) {  
            System.err.println(e.getMessage());
        }            
    }
	
    public static ConnessioneDB getInstance() {            
        if(dbc == null){                
            dbc = new ConnessioneDB();                
        }            
        return dbc;            
    }
    
    public Statement getStatement(){
    	Statement st = null;
    	try{
    		st = con.createStatement();
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	return st;
    }
    
    public PreparedStatement getPStatement(String s){
    	PreparedStatement ps = null;
    	try{    		
    		ps = con.prepareStatement(s);
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	return ps;
    }
    
    public void closeDB(){
    	try{
    		con.close();
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    }
    
    public boolean addUtente(Utente utente){
    	ConnessioneDB cdb = null;
    	PreparedStatement ps = null;
    	boolean ok = true;
    	try{
    		cdb = ConnessioneDB.getInstance();
    		ps = cdb.getPStatement(StatementsDB.aggiungiUtente);
    		ps.setString(1, utente.getUsername());
    		ps.setString(2, utente.getPassword());
    		ps.setString(3, utente.getNome());
    		ps.setString(4, utente.getCognome());
    		ps.execute();
    	}catch(SQLException e){
    		e.printStackTrace();
    		ok = false;
    	}
    	try {
			ps.close();
			closeDB();
		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
    	return ok;
    }
    
    private Utente getUtente(String username) throws EccezioneUtenteNonTrovato{
    	ConnessioneDB cdb = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	Utente utente = null;
    
    	try {
        	cdb = getInstance();
        	ps = cdb.getPStatement(StatementsDB.getUtente);
			ps.setString(1, username);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}


    	try {
			while(rs.next()){
				utente = new Utente(rs.getString("nome"), rs.getString("cognome"),
						rs.getString("userid"), rs.getString("password"), rs.getInt("crediti"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (EccezioneUtente e) {
			e.printStackTrace();
		}

    	try {
    		rs.close();
			ps.close();
			closeDB();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	if(utente == null)
    		throw new EccezioneUtenteNonTrovato("Utente non trovato : " + username);
    	else return utente;
    }
    
    public boolean controlloUtente(String username, String password){
    	Utente utente = null;
    	
    	try{
    		utente = getUtente(username);
    	}catch(EccezioneUtenteNonTrovato e){
    		e.printStackTrace();
    		return false;
    	}
    	
    	if(utente.getPassword().equals(password))
    		return true;
    	else return false;
    }
    
    public boolean aggiornaCrediti(int premio, String username){
    	ConnessioneDB cdb = null;
    	PreparedStatement ps = null;
    	boolean ok = true;
    	
    	try{
    		cdb = ConnessioneDB.getInstance();
    		ps = cdb.getPStatement(StatementsDB.aggiornaCrediti);
    		ps.setInt(1, premio);
    		ps.setString(2, username);
    		ps.execute();
    	}catch(SQLException e){
    		e.printStackTrace();
    		ok = false;
    	}
    	
    	try {
			ps.close();
			closeDB();
		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
    	return ok;
    }
    
    public boolean resetCreditiG(String username){
    	ConnessioneDB cdb = null;
    	PreparedStatement ps = null;
    	boolean ok = true;
    	
    	try{
    		cdb = ConnessioneDB.getInstance();
    		ps = cdb.getPStatement(StatementsDB.resetCreditiGiornalieri);
    		ps.setString(1, username);
    		ps.execute();
    	}catch(SQLException e){
    		e.printStackTrace();
    		ok = false;
    	}
    	
    	try {
			ps.close();
			closeDB();
		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
    	return ok;
    }
    
    public ArrayList<Utente> getClassifica(boolean isGiornaliera) throws EccezioneClassificaVuota{
    	ArrayList<Utente> classifica = new ArrayList<Utente>();
    	ConnessioneDB cdb = null;
    	Statement st = null;
    	ResultSet rs = null;
    	
    	try{
    		cdb = ConnessioneDB.getInstance();
    		st = cdb.getStatement();
    		if(isGiornaliera)
    			rs = st.executeQuery(StatementsDB.getClassificaGiornaliera);
    		else rs = st.executeQuery(StatementsDB.getClassifica);
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	try{
			while(rs.next()){
				classifica.add(new Utente(rs.getString("nome"), rs.getString("cognome"),
						rs.getString("userid"), rs.getString("password"), rs.getInt("crediti")));
			}
    	}catch (SQLException e) {
			e.printStackTrace();
		}catch (EccezioneUtente e) {
			e.printStackTrace();
		}

    	try {
    		rs.close();
			st.close();
			closeDB();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	if(classifica == null)
    		throw new EccezioneClassificaVuota("Classifica vuota! Nessun utente registrato!");
    	else return classifica;
    }
    
}

















