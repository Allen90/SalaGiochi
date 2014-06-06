package rmi;

import interfacciaDB.ConnessioneDB;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import partite.InfoPartitaRubaMazzo;
import partite.InfoPartitaTombola;
import eccezioni.EccezioneUtente;
import rubamazzo.SituazioneRubamazzo;
import rubamazzo.TavoloRubamazzo;
import slot.Rollata;
import slot.Slot;
import threadCode.ThreadLobbyRubaMazzo;
import threadCode.ThreadLobbyTombola;
import tombola.GiocatoreTombola;
import tombola.SituazioneTombola;
import tombola.Tabella;
import model.RmiServer;
import model.RmiTaskControl;
import model.Utente;

public class RmiTaskControlImp extends UnicastRemoteObject implements RmiTaskControl,Runnable{
	private Utente utente;
	private Boolean continua;
	private GiocatoreTombola gt;
	private ConnessioneDB db;
	private ThreadLobbyTombola lt;
	private ThreadLobbyRubaMazzo lrb;
	private Slot s;
	private InfoPartitaTombola ipt;
	private InfoPartitaRubaMazzo iprm;
	
	
	public RmiTaskControlImp(Utente utente) throws RemoteException{
		this.utente = utente;
		continua = true;
		db = ConnessioneDB.getInstance();
		lt = ThreadLobbyTombola.getIstance();
		lrb = ThreadLobbyRubaMazzo.getInstance();
		ipt = InfoPartitaTombola.getInstance();
		iprm = InfoPartitaRubaMazzo.getInstance();
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
	
	public SituazioneTombola aggTombola(){
		SituazioneTombola sit = ipt.getUtente(utente.getUsername());
		return sit;
	}
	
	public void giocoRubaMazzo(){
		lrb.addUserLobbyRubaMazzo(utente);
				
	}
	
	public SituazioneRubamazzo aggRubaMazzo(){
		SituazioneRubamazzo sit = iprm.getUtente(utente.getUsername());
		return sit;
	}
	
	public void termina(){
		continua = false;
	}
	
	@Override
	public Rollata rolla() {
		Rollata r = new Rollata();
		
		try {
			if(db.getUtente(utente.getUsername()).getCrediti() > 1) {

				s = new Slot();
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
