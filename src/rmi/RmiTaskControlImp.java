package rmi;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import slotMachine.Slot;
import Tombola.GiocatoreTombola;
import model.RmiServer;
import model.RmiTaskControl;
import model.Rollata;
import model.Tabelllone;
import model.Tavolo;
import model.Utent;
import model.Utente;

public class RmiTaskControlImp extends UnicastRemoteObject implements RmiTaskControl,Runnable{
	private Utente utente;
	private Boolean continua;
	private GiocatoreTombola gt;
	public RmiTaskControlImp(Utente utente){
		this.utente = utente;
		continua = true;
	}
	
	@Override
	public void run() {
		while(continua){
			
		}
		
	}
	
	public void termina(){
		continua = false;
	}
	
	@Override
	public Rollata rolla() {
		Rollata r = new Rollata();
		
		if(db.getUtente(utente.getUsername()).getCrediti() < 1)
			return null;
		else {
			
			Slot s = new Slot();
			int[] comb = s.calcolaCombinazione();
			int premio = s.getPremio(true);
			db.aggiornaCrediti(premio,1,utente.getUsername());
			
			r.setComb(comb);
			r.setPremio(premio);
			r.setCrediti(utente.getCrediti());
			return r;
		}
		
	}

	@Override
	public GiocatoreTombola aggTombola() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tavolo aggRubaMazzo() {
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
