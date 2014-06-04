package rmi;

import interfacciaDB.ConnessioneDB;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import codePartite.LobbyRubaMazzo;
import codePartite.LobbyTombola;
import eccezioni.EccezioneUtente;
import rubamazzo.TavoloRubamazzo;
import slot.Rollata;
import slotMachine.Slot;
import tombola.GiocatoreTombola;
import tombola.Tabella;
import model.RmiServer;
import model.RmiTaskControl;
import model.Utente;

public class RmiTaskControlImp extends UnicastRemoteObject implements RmiTaskControl,Runnable{
	private Utente utente;
	private Boolean continua;
	private GiocatoreTombola gt;
	private ConnessioneDB db;
	private LobbyTombola lt;
	private LobbyRubaMazzo lrb;
	public RmiTaskControlImp(Utente utente) throws RemoteException{
		this.utente = utente;
		continua = true;
		db = ConnessioneDB.getInstance();
		lt = LobbyTombola.getIstance();
		lrb = LobbyRubaMazzo.getInstance();
	}
	
	@Override
	public void run() {
		while(continua){
			
		}
		
	}
	
	public void giocoTombola(int numCartelle){
		ArrayList<Tabella> cartelle = new ArrayList<Tabella>();
		for(int i = 0; i< numCartelle;i++){
			Tabella c = new Tabella();
			cartelle.add(c);
		}
			
		gt = new GiocatoreTombola(cartelle,utente);
		lt.addUserLobbyTomb(gt);
	}
	
	
	public void giocoRubaMazzo(){
		lrb.addUserLobbyRubaMazzo(utente);
	}
	
	public void termina(){
		continua = false;
	}
	
	@Override
	public Rollata rolla() {
		Rollata r = new Rollata();
		
		try {
			if(db.getUtente(utente.getUsername()).getCrediti() > 1) {

				Slot s = new Slot();
				int[] comb = s.calcolaCombinazione();
				int premio = s.getPremio(true);
				db.aggiornaCrediti(premio,1,utente.getUsername());
				
				r.setComb(comb);
				r.setPremio(premio);
				r.setCrediti(utente.getCrediti());
				
			}
		} catch (EccezioneUtente e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return r;
	}

	@Override
	public GiocatoreTombola aggTombola() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TavoloRubamazzo aggRubaMazzo() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Utente> aggClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Utente> aggClassGiorn() {
		// TODO Auto-generated method stub
		return null;
	}

}
