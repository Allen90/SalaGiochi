package interfacciaDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import start.UtentiLoggati;
import userModel.Utente;
import eccezioni.EccezioneClassificaVuota;
import eccezioni.EccezioneUtente;

public class ConnessioneDB {

	private static Connection con = null;
	private static UtentiLoggati loggati = null;
    private static ConnessioneDB dbc = null;
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String PATH="jdbc:derby:DBSalagiochi";
	private static final String USER="user";
	private static final String PWD="pass";
	
	private static final int INC_CREDITI_ONLINE = 20;
	private static final int INC_CREDITI_OFFLINE = 5;
	
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
    		getUtente(utente.getUsername());
    		ok = false;
    	}catch(EccezioneUtente eu){
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
    	}
    	try {
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
    	closeDB();
    	
    	return ok;
    }
    
    public Utente getUtente(String username) throws EccezioneUtente{
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

		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeDB();
    	if(utente == null)
    		throw new EccezioneUtente("Utente non trovato : " + username);
    	else return utente;
    }
    
    public int controlloUtente(String username, String password){
    	Utente utente = null;
    	
    	try{
    		utente = getUtente(username);
    	}catch(EccezioneUtente e){
    		e.printStackTrace();
    		return 1;
    	}
    	
    	if(utente.getPassword().equals(password))
    		return 0;
    	else return 2;
    }
    
    public boolean aggiornaCrediti(int premio, int spesa, String username) throws EccezioneUtente{
    	ConnessioneDB cdb = null;
    	PreparedStatement ps = null;
    	boolean ok = true;
    	Utente utente = null;
    	
    	try{
    		cdb = ConnessioneDB.getInstance();
    		utente = getUtente(username);
    		ps = cdb.getPStatement(StatementsDB.aggiornaCrediti);
    		ps.setInt(1, utente.getCrediti()+premio-spesa);
    		ps.setInt(1, utente.getCrediti_giornalieri()+premio-spesa);
    		ps.setString(3, username);
    		ps.execute();
    	}catch(SQLException e){
    		e.printStackTrace();
    		ok = false;
    	}catch(EccezioneUtente e){
    		throw new EccezioneUtente("Utente non trovato : " + username);
    	}
    	
    	try {
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
    	closeDB();
    	
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

		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
		closeDB();
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

		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeDB();
    	if(classifica == null)
    		throw new EccezioneClassificaVuota("Classifica vuota! Nessun utente registrato!");
    	else return classifica;
    }
    
    public int getPosizioneGlobale(String username) throws EccezioneUtente{
    	ConnessioneDB cdb = null;
    	Statement st = null;
    	ResultSet rs = null;
    	int posizione = 0;
    	boolean ok = false;
    	
    	try{
    		cdb = ConnessioneDB.getInstance();
    		st = cdb.getStatement();
    		rs = st.executeQuery(StatementsDB.getClassifica);
    		
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	try{
			for(posizione = 1; rs.next() || rs.getString("userid").equalsIgnoreCase(username); posizione ++)
				if(rs.getString("userid").equals(username)) ok = true;
				
    	}catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	try {
    		rs.close();
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeDB();
		
    	if(!ok)
    		throw new EccezioneUtente("Utente non trovato : " + username);
    	else return posizione;
    }
    
    public boolean aggiornaPeriodico() throws EccezioneClassificaVuota{
    	loggati = UtentiLoggati.getIstance();
    	ConnessioneDB cdb = null;
    	PreparedStatement ps = null;
    	ArrayList<Utente> utentiDB = null;
    	boolean[] isOnline = null;
    	boolean ok = true;
    			
    	try{
    		cdb = ConnessioneDB.getInstance();
    		utentiDB = getClassifica(false);
    		isOnline = new boolean[utentiDB.size()];
        	for(int i = 0;i < utentiDB.size(); i++){
        		isOnline[i] = false;
        		for(int j = 0; j < loggati.getLoggati().size(); j++)
        			if(loggati.presente(utentiDB.get(i).getUsername()))
        				isOnline[i] = true;
        	}
        	
        	ps = cdb.getPStatement(StatementsDB.aggiornaCreditiPeriodico);
        	for(int i = 0; i < utentiDB.size(); i++){
        		ps.setString(2, utentiDB.get(i).getUsername());
        		if(isOnline[i])
        			ps.setInt(1, utentiDB.get(i).getCrediti()+INC_CREDITI_ONLINE);
        		else
        			ps.setInt(1, utentiDB.get(i).getCrediti()+INC_CREDITI_OFFLINE);
        		ps.execute();
        	}
    	}catch(EccezioneClassificaVuota e){
    		ok = false;
    		throw new EccezioneClassificaVuota("Classifica vuota! Nessun utente registrato!");
    	}catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
    	
    	try {
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
		closeDB();
		
    	return ok;
    }
    
    public boolean aggiornaUltimoLogin(String username){
    	ConnessioneDB cdb = null;
    	PreparedStatement ps = null;
    	boolean ok = true;
    	
    	long dataAttualeLong = new Date().getTime();
    	java.sql.Date dataAttuale = new java.sql.Date(dataAttualeLong);
    	
    	try{
    		cdb = ConnessioneDB.getInstance();
    		ps = cdb.getPStatement(StatementsDB.setUltimoLogin);
    		ps.setDate(1, dataAttuale);
    		ps.setString(2, username);
    		ps.execute();
    	}catch(SQLException e){
    		e.printStackTrace();
    		ok = false;
    	}
    	
    	try {
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
		closeDB();
    	return ok;
    }
    
    public Date getUltimoLogin(String username) throws EccezioneUtente{
    	ConnessioneDB cdb = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	Long dataAttualeSQL = null;
    	
    	try{
    		cdb = ConnessioneDB.getInstance();
    		ps = cdb.getPStatement(StatementsDB.getUltimoLogin);
    		ps.setString(1, username);
    		rs = ps.executeQuery();
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	try {
			while(rs.next()){
				dataAttualeSQL = rs.getDate("ultimo_login").getTime(); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	try {
    		rs.close();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeDB();
		
		if(dataAttualeSQL == null)
			throw new EccezioneUtente("Utente non trovato : " + username);
		else return new Date(dataAttualeSQL);
    }
}




