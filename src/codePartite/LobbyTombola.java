package codePartite;

import java.util.ArrayList;

import model.Client;
import model.GiocatoreTombola;
import model.Utente;

public class LobbyTombola {
	private static LobbyTombola l;
	private ArrayList<GiocatoreTombola> giocatori;
	private LobbyTombola(){
		giocatori = new ArrayList<GiocatoreTombola>();
	}
	
	public static LobbyTombola getIstance(){
		if(l == null)
			l = new LobbyTombola();
		return l;
	}
	
	public int numUtentiTombola(){
		return giocatori.size();
	}
	
	public void svuotaLobbyTombola(){
		for(int i = 0;i < giocatori.size();i++)
			giocatori.remove(i);
	}
	
	public void addUserLobbyTomb(GiocatoreTombola g){
		giocatori.add(g);
	}
	
}
