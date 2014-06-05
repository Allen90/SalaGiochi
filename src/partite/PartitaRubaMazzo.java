package partite;

import java.util.ArrayList;

import rubamazzo.GiocatoreRubamazzo;
import rubamazzo.TavoloRubamazzo;
import model.Utente;

public class PartitaRubaMazzo implements Runnable{
	private InfoPartitaRubaMazzo iprm;
	private ArrayList<Utente> utenti;
	private TavoloRubamazzo trm;
	private ArrayList<GiocatoreRubamazzo> giocatori;
	
	public PartitaRubaMazzo(ArrayList<Utente> utenti){
		this.giocatori = giocatori;
		for(int i = 0; i< giocatori.size();i++){
			GiocatoreRubamazzo u = new GiocatoreRubamazzo(utenti.get(i));
			giocatori.add(u);
		}
		trm = new TavoloRubamazzo(giocatori);
		iprm = InfoPartitaRubaMazzo.getInstance();
		for(int i=0;i<giocatori.size();i++){
			SituazioneRubamazzo s = new SituazioneRubamazzo(giocatori.get(i));
			iprm.addUtente(s);
		}
	}
	public void run() {
		
		while(!trm.isFinito()){
			
		}
		
	}

}
