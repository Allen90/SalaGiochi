package interfacciaDB;

import java.sql.*;

public class ConnessioneDB {

	private static Connection con = null;  
    private static ConnessioneDB dbc = null;
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String PATH="jdbc:derby:DBSalagiochi";
	private static final String USER="user";
	private static final String PWD="pass";
	
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
    		System.out.println("statement creato con la connessione");
    	}catch(SQLException e){
    		System.out.println("errore\n");
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
}
