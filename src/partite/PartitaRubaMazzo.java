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
	private int numPartita;
	private boolean mossaFinita;
	public PartitaRubaMazzo(ArrayList<Utente> utenti,int numPartita){
		this.numPartita = numPartita;
		this.giocatori = giocatori;
		for(int i = 0; i< giocatori.size();i++){
			GiocatoreRubamazzo u = new GiocatoreRubamazzo(utenti.get(i));
			giocatori.add(u);
		}
		trm = new TavoloRubamazzo(giocatori);
		iprm = InfoPartitaRubaMazzo.getInstance();
		
		for(int i=0;i<giocatori.size();i++){
			SituazioneRubamazzo s = new SituazioneRubamazzo(giocatori.get(i),numPartita);
			if( i == 0)
				s.setAbilitato(true);
			iprm.addUtente(s);
		}

	}
	
	
	
	public ArrayList<GiocatoreRubamazzo> getGiocatori(){
		return giocatori;
	}
 	
	public TavoloRubamazzo getTavolo(){
		return trm;
	}
	
	public void setMossaFinita(boolean finita){
		mossaFinita = finita;
	}
	
	public boolean mossaFinita()
	{
		return mossaFinita;
	}
	
	public void run() {
		
		while(!trm.isFinito()){
			while(!mossaFinita()){
				
			}
			
		}
		
	}

}
