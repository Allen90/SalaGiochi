package codePartite;
import java.util.ArrayList;

import model.Client;
import model.Utente;
public class LobbyRubaMazzo {
	private ArrayList<Utente> client;
	private static LobbyRubaMazzo l;
	
	private LobbyRubaMazzo(){
		client = new ArrayList<Utente>();
	}
	
	public static LobbyRubaMazzo getInstance(){
		if(l == null)
			l = new LobbyRubaMazzo();
		return l;
	}
	
	public ArrayList<Utente> getLobbyRubaMazzo(){
		return client;
	}
	
	public void svuotaLobbyRubaMazzo(){
		for(int i = 0;i< client.size();i++)
			client.remove(i);
	}
	
	public void addUserLobbyRubaMazzo(Utente c){
		client.add(c);
	}
}
