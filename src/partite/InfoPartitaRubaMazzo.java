package partite;

import java.util.ArrayList;

import rubamazzo.SituazioneRubamazzo;

/**
 *  singleton che contiene tutte le informazioni delle partite rubamazzo di ogni utente
 *  che attualmente stanno partecipando in una di esse
 * @author fritz
 *
 */

public class InfoPartitaRubaMazzo {
	private static InfoPartitaRubaMazzo iprm;
	private ArrayList<SituazioneRubamazzo> info;
	
	private InfoPartitaRubaMazzo(){
		info = new ArrayList<SituazioneRubamazzo>();
	}
	
	public static InfoPartitaRubaMazzo  getInstance(){
		if(iprm == null)
			iprm = new InfoPartitaRubaMazzo();
		return iprm; 
	}
	
	public SituazioneRubamazzo getUtente(String username){
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
	
	public void addUtente(SituazioneRubamazzo utente){
		info.add(utente);
	}
	
	public int numSituazioni(){
		return info.size();
	}
}
