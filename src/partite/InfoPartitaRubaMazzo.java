package partite;

import java.util.ArrayList;

public class InfoPartitaRubaMazzo {
	private static InfoPartitaRubaMazzo iprm;
	private ArrayList<SituazioneRubaMazzo> info;
	
	private InfoPartitaRubaMazzo(){
		info = new ArrayList<SituazioneRubaMazzo>();
	}
	
	public static InfoPartitaRubaMazzo  getInstance(){
		if(iprm == null)
			iprm = new InfoPartitaRubaMazzo();
		return iprm; 
	}
	
	public SituazioneRubaMazzo getUtente(String username){
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
	
	public void addUtente(SituazioneRubaMazzo utente){
		info.add(utente);
	}
}
