package codePartite;
import java.util.ArrayList;

import model.Client;
import model.Utente;
public class LobbyRubaMazzo {
	private ArrayList<Client> client;
	private static LobbyRubaMazzo l;
	
	private LobbyRubaMazzo(){
		client = new ArrayList<Client>();
	}
	
	public static LobbyRubaMazzo getInstance(){
		if(l == null)
			l = new LobbyRubaMazzo();
		return l;
	}
	
	public ArrayList<Client> getLobbyRubaMazzo(){
		return client;
	}
	
	public void svuotaLobbyRubaMazzo(){
		for(int i = 0;i< client.size();i++)
			client.remove(i);
	}
	
	public void addUserLobbyRubaMazzo(Client c){
		client.add(c);
	}
}
