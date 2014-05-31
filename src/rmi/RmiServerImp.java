package rmi;

import interfacciaDB.ConnessioneDB;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import start.UtentiLoggati;
import model.Client;
import model.ClientRMI;
import model.RmiServer;
import model.RmiTaskControl;
import model.Utente;
import model.UtenteReg;

public class RmiServerImp extends UnicastRemoteObject implements RmiServer,Runnable{

	private static final long serialVersionUID = 1L;
	private static String url = "rmi://127.0.0.1/server";
	private static String host = "127.0.0.1";
	private ClientRMI client;
	private RmiTaskControlImp server = null;
	private Thread serverThread;
	private ConnessioneDB db;
	private UtentiLoggati l;
	private Object lock;
	public RmiServerImp() throws RemoteException{
		db = ConnessioneDB.getInstance();
		l = UtentiLoggati.getIstance();
		lock = null;
	}
	public  void run() {
		try {
			// Creo il SecurityManager
			if (System.getSecurityManager() == null)
				System.setSecurityManager(new SecurityManager());

			// Nuova istanza del server
			RmiServer stub = new RmiServerImp();

			// Bind dell'oggetto remoro nel registry
			Registry registry = LocateRegistry.getRegistry(host);
			registry.rebind(url, stub);			
			System.out.println("Modalità RMI avviata.");
			//Attendo il comando di spegimento del Server.
			while (true){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {					
					e.printStackTrace();
				} 
				// Effettuo l'unbind della classe remota			
				registry.unbind(url);			
				System.out.println("Modalita RMI terminata.");
			}

		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}

	public RmiTaskControl login(ClientRMI client,Utente user){

		RmiTaskControlImp server = null;

		int controllo = db.controlloUtente(user.getUsername(),user.getPassword());

		switch(controllo){
		case 0:{

			serverThread = new Thread(server);
			serverThread.start();
			return server;
			synchronized (lock) {
				//Aggiungo il client ai client connessi
				l.addLoggato(user);			
			}
		}
		case 1:
		case 2: return null;
		}





	}

	public RmiTaskControl registra(UtenteReg u){
		RmiTaskControlImp server = null;
		if (u.getPassword().equals(u.getPasswordConf()))
			if(db.controlloUtente(u.getUsername(),u.getPassword()) == 1){
				Utente u = new Utente(u.getNome(),u.getCognome(),u.getUsername(),u.getPassword(),0);
				synchronized (lock) {
					//Aggiungo il client ai client connessi
					l.addLoggato(u);
				}
				serverThread = new Thread(server);
				serverThread.start();
			}

	}
}

