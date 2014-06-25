package partite;

import interfacciaDB.ConnessioneDB;

import java.util.ArrayList;

import eccezioni.EccezioneUtente;
import rubamazzo.GiocatoreRubamazzo;
import rubamazzo.SituazioneRubamazzo;
import rubamazzo.TavoloRubamazzo;
import userModel.Utente;
/**
 * thread che gestisce una singola partita, una volta lanciato, aspetta il termine del turnodi ogni utente,
 * aggiornando man mano le informazioni in essa.
 * @author fritz
 *
 */
public class PartitaRubaMazzo implements Runnable{
	private InfoPartitaRubaMazzo iprm;
	private TavoloRubamazzo trm;
	private ArrayList<GiocatoreRubamazzo> giocatori;
	private boolean mossaFinita;
	private int turno, giro;
	private ConnessioneDB db;

	public PartitaRubaMazzo(ArrayList<Utente> utenti,int numPartita){
		
		ArrayList<Utente> temp = new ArrayList<>();
		giocatori = new ArrayList<>();
		temp.addAll(utenti);
		for(int i = 0; i< temp.size();i++){
			GiocatoreRubamazzo u = new GiocatoreRubamazzo(temp.get(i));
			giocatori.add(u);
		}
		trm = new TavoloRubamazzo(giocatori);
		iprm = InfoPartitaRubaMazzo.getInstance();
		turno = 0;
		giro = 0;
		for(int i=0;i<giocatori.size();i++){
			SituazioneRubamazzo s = new SituazioneRubamazzo(trm, giocatori.get(i),numPartita);
			if( i == 0)
				s.setAbilitato(true);
			iprm.addUtente(s);
		}
		db = ConnessioneDB.getInstance();
	}
	/**
	 * aggiorna il tuo assegnandolo al giocatore successivo, se il giro è finito
	 * distribuisce le 3 carte della mano
	 */
	public void aggiornaTurno(){
		turno++;
		if(turno>giocatori.size()-1){
			turno = 0;
			giro ++;
			if(giro >= 3){
				giro = 0;
				trm.daiCarteInizio(giocatori);
			}
		}
			
		for(int i=0;i<giocatori.size();i++){
			iprm.getUtente(giocatori.get(i).getUtente().getUsername()).aggiornaSituazione(giocatori.get(i), trm);
			iprm.getUtente(giocatori.get(i).getUtente().getUsername()).setAbilitato(false);
			if( i == turno)
				iprm.getUtente(giocatori.get(i).getUtente().getUsername()).setAbilitato(true);
		}
		mossaFinita = false;
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
		while(!trm.isFinita()){
			while(!mossaFinita()){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			aggiornaTurno();
		}
		ArrayList<String> vincitori = trm.getVincitore();
		int premio = trm.getPremio();
		for(int i = 0;i<giocatori.size();i++){
			for(int j=0;j<vincitori.size();j++)
				if(giocatori.get(i).getUtente().getUsername().equals(vincitori.get(j))){
					try {
						db.aggiornaCrediti(premio, 0, giocatori.get(i).getUtente().getUsername());
					} catch (EccezioneUtente e) {
						e.printStackTrace();
					}
				}
			
		}
		// rimuovo tutte le situazioni partita della sessione ormai conclusa
		for(int i= 0; i< giocatori.size();i++){
			iprm.rimuovi(giocatori.get(i).getUtente().getUsername());
		}

	}

}
