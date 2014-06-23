package interfacciaDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import start.UtentiLoggati;
import userModel.Utente;
import eccezioni.EccezioneClassificaVuota;
import eccezioni.EccezioneUtente;

public class ConnessioneDB {

	private static Connection con = null;
	private static UtentiLoggati loggati = null;
    private static ConnessioneDB dbc = null;
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String PATH="jdbc:derby:DBSalagiochi;create=true";
	private static final String USER="user";
	private static final String PWD="pass";
	
	private static final int INC_CREDITI_ONLINE = 20;
	private static final int INC_CREDITI_OFFLINE = 5;
	
	private ConnessioneDB() {  
          
        try {                
            Class.forName(DRIVER).newInstance();  
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
    
    public boolean addUtente(Utente utente) throws ParseException{
    	ConnessioneDB cdb = null;
    	PreparedStatement ps = null;
    	boolean ok = true;
		//DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy",Locale.ENGLISH);
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
//		String s = dateFormat.format(utente.getUltimaVisita());
//		java.sql.Date data = dateFormat.parse(s);
    	try{
    		getUtente(utente.getUsername());
    		ok = false;
    	}catch(EccezioneUtente eu){
    		try{
    			cdb = ConnessioneDB.getInstance();
    	        System.out.println("connessione...");
    	        try {
    				con = DriverManager.getConnection(PATH, USER, PWD);
    			} catch (SQLException e1) {
    				e1.printStackTrace();
    			}
    	        System.out.println("connessione stabilita");
        		ps = cdb.getPStatement(StatementsDB.aggiungiUtente);
        		ps.setString(1, utente.getUsername());
        		ps.setString(2, utente.getPassword());
        		ps.setString(3, utente.getNome());
        		ps.setString(4, utente.getCognome());
        		ps.setDate(5, utente.getUltimaVisitaSQL());
        		//ps.setDate(5, data);
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
    	ConnessioneDB cdb = ConnessioneDB.getInstance();
        System.out.println("connessione...");
        try {
			con = DriverManager.getConnection(PATH, USER, PWD);
	        System.out.println("connessione stabilita");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}  
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	Utente utente = null;
    
    	try {
        	ps = cdb.getPStatement(StatementsDB.getUtente);
			ps.setString(1, username);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			
		}


    	try {
			while(rs.next()){
				utente = new Utente(rs.getString("nome").trim(), rs.getString("cognome").trim(),
						rs.getString("userid").trim(), rs.getString("password").trim(), rs.getInt("crediti"), rs.getDate("ULTIMO_LOGIN"));
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
    
    
    
    public boolean controlloUtente(String username, String password){    	
    	try{
    		Utente utente = getUtente(username);
    		System.out.println(utente.getUsername()+" "+utente.getPassword());
    		if(utente.getPassword().equals(password))
    			return true;
    		else
    			return false;
    	}catch(EccezioneUtente e){
    		return false;
    	}
    }
    
    public boolean aggiornaCrediti(int premio, int spesa, String username) throws EccezioneUtente{
    	ConnessioneDB cdb = ConnessioneDB.getInstance();
        
    	PreparedStatement ps = null;
    	boolean ok = true;
    	Utente utente = null;
    	
    	try{
    		utente = getUtente(username);
    		System.out.println("utente recuperato durante l'aggiorna crediti" + utente.getUsername() + " " +utente.getCrediti());
    		System.out.println("connessione...");
            try {
    			con = DriverManager.getConnection(PATH, USER, PWD);
    			System.out.println("connessione stabilita");
    		} catch (SQLException e1) {
    			e1.printStackTrace();
    		}  
    		ps = cdb.getPStatement(StatementsDB.aggiornaCrediti);
    		ps.setInt(1, utente.getCrediti()+premio-spesa);
    		ps.setInt(2, utente.getCrediti_giornalieri()+premio-spesa);
    		ps.setString(3, username);
    		ps.execute();
    		System.out.println("eseguito query aggiornamento");
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
    	ConnessioneDB cdb = ConnessioneDB.getInstance();
        System.out.println("connessione...");
        try {
			con = DriverManager.getConnection(PATH, USER, PWD);
			System.out.println("connessione stabilita");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}  
    	PreparedStatement ps = null;
    	boolean ok = true;
    	
    	try{
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
    	ConnessioneDB cdb = ConnessioneDB.getInstance();
        System.out.println("connessione...");
        try {
			con = DriverManager.getConnection(PATH, USER, PWD);
			System.out.println("connessione stabilita");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}  
    	Statement st = null;
    	ResultSet rs = null;
    	
    	try{
    		st = cdb.getStatement();
    		if(isGiornaliera)
    			rs = st.executeQuery(StatementsDB.getClassificaGiornaliera);
    		else rs = st.executeQuery(StatementsDB.getClassifica);
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	try{
			while(rs.next()){
				classifica.add(new Utente(rs.getString("nome").trim(), rs.getString("cognome").trim(),
						rs.getString("userid").trim(), rs.getString("password").trim(), rs.getInt(5)));
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
    	ConnessioneDB cdb = ConnessioneDB.getInstance();
        System.out.println("connessione...");
        try {
			con = DriverManager.getConnection(PATH, USER, PWD);
			System.out.println("connessione stabilita");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}  
    	Statement st = null;
    	ResultSet rs = null;
    	int posizione = 0;
    	boolean ok = false;
    	
    	try{
    		st = cdb.getStatement();
    		rs = st.executeQuery(StatementsDB.getClassifica);
    		
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	try{
			for(posizione = 1; rs.next(); posizione ++){
				if(rs.getString("userid").trim().equalsIgnoreCase(username)){
					ok = true;
					break;
				}
			}
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
    
    public boolean aggiornaPeriodico(int ore) throws EccezioneClassificaVuota{
    	loggati = UtentiLoggati.getIstance();
    	ConnessioneDB cdb = ConnessioneDB.getInstance();
    	PreparedStatement ps = null;
    	ArrayList<Utente> utentiDB = null;
    	boolean[] isOnline = null;
    	boolean ok = true;
    			
    	try{
    		utentiDB = getClassifica(false);
    		isOnline = new boolean[utentiDB.size()];
        	for(int i = 0;i < utentiDB.size(); i++){
        		isOnline[i] = false;
        		for(int j = 0; j < loggati.getLoggati().size(); j++)
        			if(loggati.presente(utentiDB.get(i).getUsername()))
        				isOnline[i] = true;
        	}
        	
        	System.out.println("connessione...");
            try {
    			con = DriverManager.getConnection(PATH, USER, PWD);
    			System.out.println("connessione stabilita");
    		} catch (SQLException e1) {
    			e1.printStackTrace();
    		}  
        	
        	ps = cdb.getPStatement(StatementsDB.aggiornaCreditiPeriodico);
        	for(int i = 0; i < utentiDB.size(); i++){
        		ps.setString(2, utentiDB.get(i).getUsername());
        		if(isOnline[i])
        			ps.setInt(1, utentiDB.get(i).getCrediti()+(INC_CREDITI_ONLINE * ore));
        		else
        			ps.setInt(1, utentiDB.get(i).getCrediti()+(INC_CREDITI_OFFLINE * ore));
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
    	ConnessioneDB cdb = ConnessioneDB.getInstance();
        System.out.println("connessione...");
        try {
			con = DriverManager.getConnection(PATH, USER, PWD);
			System.out.println("connessione stabilita");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}  
    	PreparedStatement ps = null;
    	boolean ok = true;
    	
    	long dataAttualeLong = new Date().getTime();
    	java.sql.Date dataAttuale = new java.sql.Date(dataAttualeLong);
    	
    	try{
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
    	ConnessioneDB cdb = ConnessioneDB.getInstance();
        System.out.println("connessione...");
        try {
			con = DriverManager.getConnection(PATH, USER, PWD);
			System.out.println("connessione stabilita");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}  
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	Long dataAttualeSQL = null;
    	
    	try{
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




