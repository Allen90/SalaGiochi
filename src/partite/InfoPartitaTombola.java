package partite;
import java.util.*;

/**
 * singleton che contiene tutte le informazioni delle partite tombola degli utenti che attualmente 
 * stanno partecipando in una di esse
 */

import tombola.SituazioneTombola;
public class InfoPartitaTombola {
	
	private static InfoPartitaTombola ipt;
	private ArrayList<SituazioneTombola> info;
	
	private InfoPartitaTombola(){
		info = new ArrayList<SituazioneTombola>();
	}
	
	public static InfoPartitaTombola  getInstance(){
		if(ipt == null)
			ipt = new InfoPartitaTombola();
		return ipt; 
	}
	
	public SituazioneTombola getUtente(String username){
		for(int i = 0;i<info.size();i++)
			if(info.get(i).getUsername().equals(username))
				return info.get(i);
		return null;
	}
	
	public void rimuovi(String username){
		for(int i = 0;i<info.size();i++)
			if(info.get(i).getUsername().equals(username))
				info.remove(i);
	}
	
	public void addUtente(SituazioneTombola utente){
		info.add(utente);
	}
}
