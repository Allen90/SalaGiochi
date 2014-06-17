package rmi;

import interfacciaDB.ConnessioneDB;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import eccezioni.EccezioneUtente;
import rmiServer.RmiServer;
import rmiServer.RmiTaskControl;
import start.UtentiLoggati;
import userModel.Utente;

public class RmiServerImp extends UnicastRemoteObject implements RmiServer,Runnable{

	private static final long serialVersionUID = 1L;
	private static String url = "rmi://127.0.0.1/server";
	private static String host = "127.0.0.1";
	private Thread serverThread;
	private ConnessioneDB db;
	private UtentiLoggati l;
	private Object lock;
	private boolean continua;

	public RmiServerImp() throws RemoteException{
		db = ConnessioneDB.getInstance();
		l = UtentiLoggati.getIstance();
		lock = null;
	}
	
	public void setFinito(){
		continua = false;
	}

	public  void run() {
		continua = true;
		try {
			// Creo il SecurityManager
			if (System.getSecurityManager() == null)
				System.setSecurityManager(new SecurityManager());

			// Nuova istanza del server
			RmiServer stub = new RmiServerImp();

			// Bind dell'oggetto remoro nel registry
			Registry registry = LocateRegistry.getRegistry(host);
			System.out.println("qui");
			registry.rebind(url, stub);
			System.out.println("qui");
			System.out.println("Modalità RMI avviata.");
			//Attendo il comando di spegimento del Server.
			while (continua){		
				
			}
			// Effettuo l'unbind della classe remota
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

		boolean valido = db.controlloUtente(username,password);

		if(valido){
			Utente user = db.getUtente(username);
			System.out.println("data login" + user.getUltimaVisita());
			server = new RmiTaskControlImp(user);
			serverThread = new Thread(server);
			serverThread.start();

//			synchronized (lock) {
				//Aggiungo il client ai client connessi
				l.addLoggato(user.getUsername());
//			}
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
			//				synchronized (lock) {
			//					//Aggiungo il client ai client connessi
			//					l.addLoggato(u.getUsername());
			//				}
			u = db.getUtente(username);
			server = new RmiTaskControlImp(u);
			serverThread = new Thread(server);
			serverThread.start();
			return server;
		}
		return null;
	}

}

