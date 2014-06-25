package rmi;



import interfacciaDB.ConnessioneDB;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import eccezioni.EccezioneClassificaVuota;
import eccezioni.EccezioneUtente;
import rmiServer.RmiTaskControl;
import rubamazzo.Mossa;
import rubamazzo.SituazioneRubamazzo;
import slot.Rollata;
import taskController.TaskController;
import tombola.SituazioneTombola;
import userModel.EntryClassifica;
import userModel.InfoHome;
import userModel.Utente;


public class RmiTaskControlImp extends UnicastRemoteObject implements RmiTaskControl,Runnable{

	private static final long serialVersionUID = 1L;
	private Utente utente;
	private Boolean continua;
	private TaskController tc;
	private ConnessioneDB db;
	
	
	public RmiTaskControlImp(Utente utente) throws RemoteException{
		this.utente = utente;
		continua = true;
		tc = new TaskController();
		db = ConnessioneDB.getInstance();
	}
	
	@Override
	public void run() {
		while(continua){
			
		}
		
	}
	
	public void chiudi(){
		continua = false;
	}

	@Override
	public Rollata rolla() throws EccezioneUtente {
		return tc.rolla(utente.getUsername());
	}

	@Override
	public SituazioneTombola aggTombola() {
		System.out.println("ricevuto richiesta di aggiornamento tombola");
		return tc.aggTombola(utente);
	}

	@Override
	public SituazioneRubamazzo aggRubaMazzo() {
		return tc.aggRubamazzo(utente);
	}

	@Override
	public ArrayList<EntryClassifica> aggClass() throws EccezioneClassificaVuota {
		return tc.aggClass(false);
		
	}

	@Override
	public ArrayList<EntryClassifica> aggClassGiorn() throws EccezioneClassificaVuota {
		return tc.aggClass(true);
	}


	
	public boolean giocoTombola(int numCartelle) throws EccezioneUtente{
		return tc.giocoTombola(utente, numCartelle);
		
	}
	
	public boolean giocoRubamazzo() throws EccezioneUtente{
		return tc.giocoRubamazzo(utente);
	}
	
	public boolean mossaRubamazzo(Mossa m,int numPartita){
		return tc.mossaRubaMazzo(utente, m, numPartita);
	}
	
	public boolean vintoTombola(int numPartita,int tipoVittoria,int indiceCartella, int indiceRiga) throws EccezioneUtente{
		return tc.vintoTombola(utente, numPartita, tipoVittoria, indiceCartella, indiceRiga);
	}

	
	public InfoHome getInfoHome() throws EccezioneUtente{
		utente = db.getUtente(utente.getUsername());
		int posizione = tc.getPosizione(utente.getUsername());
		InfoHome ih = new InfoHome(utente.getNome(),utente.getCognome(),utente.getCrediti(),posizione, utente.getUltimaVisita());
		return ih;
	}

	
	public boolean logout() throws EccezioneUtente, RemoteException {
		System.out.println("prima del logout "+continua);
		continua = tc.termina(utente.getUsername());
		System.out.println("dopo il logout "+continua);
		return continua;
	}


	

}
