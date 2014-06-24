package rmi;

import interfacciaDB.ConnessioneDB;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import eccezioni.EccezioneUtente;
import rmiServer.RmiServer;
import rmiServer.RmiTaskControl;
import start.UtentiLoggati;
import userModel.Utente;
/**
 * thread che gestisce le connessioni rmi del client, una volta effettuato il login o la registrazione, viene creato un
 * thread che gestira' le seguenti richieste del client  
 * @author fritz
 *
 */
public class RmiServerImp extends UnicastRemoteObject implements RmiServer,Runnable{

	private static final long serialVersionUID = 1L;
	private static String url = "rmi://127.0.0.1/server";
	private static String host = "127.0.0.1";
	private Thread serverThread;
	private ConnessioneDB db;
	private UtentiLoggati l;
	private ArrayList<RmiTaskControlImp> arrayController;
	private boolean continua;

	public RmiServerImp() throws RemoteException{
		db = ConnessioneDB.getInstance();
		l = UtentiLoggati.getIstance();
		arrayController = new ArrayList<RmiTaskControlImp>();
	}
	
	public void chiudi(){
		for(int i = 0 ; i < arrayController.size();i++)
			arrayController.get(i).chiudi();
		continua = false;
	}

	public  void run() {
		
		continua = true;
		try {
			if (System.getSecurityManager() == null)
				System.setSecurityManager(new SecurityManager());
			RmiServer stub = new RmiServerImp();
			Registry registry = LocateRegistry.getRegistry(host);
			System.out.println("qui");
			registry.rebind(url, stub);
			System.out.println("qui");
			System.out.println("Modalità RMI avviata.");
			while (continua){		
				
			}
			registry.unbind(url);			
			System.out.println("Modalita RMI terminata.");

		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}

	public RmiTaskControl login(String username,String password) throws RemoteException, EccezioneUtente{
		System.out.println("richiesta login rmi");
		RmiTaskControlImp server = null;
		boolean ok = true;
		boolean valido = db.controlloUtente(username,password);
		
		for(int i=0;i< l.getLoggati().size();i++){
			if(l.getLoggati().get(i).equals(username)){
				ok = false;
			}
		}
		
		System.out.println(valido);
		System.out.println(ok);
		
		if(valido && ok){
			Utente user = db.getUtente(username);
			server = new RmiTaskControlImp(user);
			serverThread = new Thread(server);
			arrayController.add(server);
			l.addLoggato(user.getUsername());
			serverThread.start();
			return server;
		}
		return null;

	}

	public RmiTaskControl registra(String username, String password, String confPassword, String nome, String cognome) throws EccezioneUtente, RemoteException, ParseException{
		RmiTaskControlImp server = null;
		System.out.println("richiesta registrazione rmi");
		if (password.equals(confPassword) && !db.controlloUtente(username, confPassword)){
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
			Date ultimaVisita = new Date();
			Utente u = new Utente(nome,cognome,username,password,0,ultimaVisita);
			db.addUtente(u);
			u = db.getUtente(username);
			server = new RmiTaskControlImp(u);
			//arrayController.add(server);
			serverThread = new Thread(server);
			serverThread.start();
			return server;
		}
		return null;
	}

}

