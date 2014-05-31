package Tombola;

import java.util.ArrayList;

import model.Utente;

public class GiocatoreTombola {
	
	private ArrayList<Tabella> cartelle = null;
	private Utente utente = null;
	private Tabellone tabellone = null;
	
	
	public GiocatoreTombola(ArrayList<Tabella> cartelle, Utente giocatore ){
		this.utente = giocatore;
		this.cartelle = new ArrayList<Tabella>(cartelle);
	}
	
	public void aggiornaTabellone(Tabellone t){
		tabellone = t;
	}
	
	public Utente getUtente(){
		return utente;
	}
	
	
}
