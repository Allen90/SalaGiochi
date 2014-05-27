package codePartite;

import java.util.ArrayList;

import model.Client;
import model.Utente;

public class LobbyTombola {
	private static LobbyTombola l;
	private ArrayList<Client> client;
	private LobbyTombola(){
		client = new ArrayList<Client>();
	}
	
	public LobbyTombola getIstance(){
		if(l == null)
			l = new LobbyTombola();
		return l;
	}
	
	public int numUtentiTombola(){
		return client.size();
	}
	
	public void svuotaLobbyTombola(){
		for(int i = 0;i < client.size();i++)
			client.remove(i);
	}
	
	public void addUserLobbyTomb(Client c,int i){
		GiocatoreTombola g = new GiocatoreTombola(c,i);
		lobbyTombola.add(g);
	}
	
}
