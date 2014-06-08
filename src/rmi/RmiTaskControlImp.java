package rmi;

import interfacciaDB.ConnessioneDB;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import partite.InfoPartitaRubaMazzo;
import partite.InfoPartitaTombola;
import eccezioni.EccezioneClassificaVuota;
import eccezioni.EccezioneUtente;
import rubamazzo.Mossa;
import rubamazzo.SituazioneRubamazzo;
import rubamazzo.TavoloRubamazzo;
import slot.Rollata;
import slot.Slot;
import taskController.TaskController;
import tombola.GiocatoreTombola;
import tombola.SituazioneTombola;
import tombola.Tabella;
import lobby.ThreadLobbyRubaMazzo;
import lobby.ThreadLobbyTombola;
import model.RmiServer;
import model.RmiTaskControl;
import model.Utente;

public class RmiTaskControlImp extends UnicastRemoteObject implements RmiTaskControl,Runnable{
	private Utente utente;
	private Boolean continua;
	private TaskController tc;
	
	
	public RmiTaskControlImp(Utente utente) throws RemoteException{
		this.utente = utente;
		continua = true;
		tc = new TaskController();
	}
	
	@Override
	public void run() {
		while(continua){
			
		}
		
	}

	@Override
	public Rollata rolla() {
		return tc.rolla(utente);
	}

	@Override
	public SituazioneTombola aggTombola() {
		return tc.aggTombola(utente);
	}

	@Override
	public SituazioneRubamazzo aggRubaMazzo() {
		return tc.aggRubamazzo(utente);
	}

	@Override
	public ArrayList<Utente> aggClass() throws EccezioneClassificaVuota {
		return tc.aggClass(utente);
		
	}

	@Override
	public ArrayList<Utente> aggClassGiorn() throws EccezioneClassificaVuota {
		return tc.aggClassGiorn(utente);
	}

	@Override
	public void termina() {
		continua = tc.termina();
	}
	
	public void giocoTombola(int numCartelle){
		tc.giocoTombola(utente, numCartelle);
	}
	
	public void giocoRubamazzo(){
		tc.giocoRubamazzo(utente);
	}
	
	public boolean mossaRubamazzo(Mossa m,int numPartita){
		return tc.mossaRubaMazzo(utente, m, numPartita);
	}
	
	public boolean vintoTombola(int numPartita,int tipoVittoria,int indiceCartella, int indiceRiga){
		return tc.vintoTombola(utente, numPartita, tipoVittoria, indiceCartella, indiceRiga);
	}
	
	

}
