package partite;

import interfacciaDB.ConnessioneDB;

import java.util.ArrayList;

import eccezioni.EccezioneUtente;
import rubamazzo.GiocatoreRubamazzo;
import rubamazzo.SituazioneRubamazzo;
import rubamazzo.TavoloRubamazzo;
import userModel.Utente;

public class PartitaRubaMazzo implements Runnable{
	private InfoPartitaRubaMazzo iprm;
	private TavoloRubamazzo trm;
	private ArrayList<GiocatoreRubamazzo> giocatori;
	private boolean mossaFinita;
	private int turno;
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
		for(int i=0;i<giocatori.size();i++){
			SituazioneRubamazzo s = new SituazioneRubamazzo(trm, giocatori.get(i),numPartita);
			if( i == 0)
				s.setAbilitato(true);
			iprm.addUtente(s);
		}
		db = ConnessioneDB.getInstance();
	}

	public void aggiornaTurno(){
		turno++;
		System.out.println("STO PER AGGIORNARE IL TURNO");
		if(turno>giocatori.size()-1){
			turno = 0;
			System.out.println("TURNO AGGIORNATO :" + turno);
			trm.daiCarte(giocatori);
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
			System.out.println("mossa finita, aggiorno il turno");
			aggiornaTurno();
		}
		ArrayList<String> vincitori = trm.getVincitore();
		int premio = trm.getPremio();
		boolean vincitore = false;
		for(int i = 0;i<giocatori.size();i++){
			for(int j=0;j<vincitori.size();j++)
				if(giocatori.get(i).getUtente().getUsername().equals(vincitori.get(j))){
					try {
						db.aggiornaCrediti(premio, 1, giocatori.get(i).getUtente().getUsername());
					} catch (EccezioneUtente e) {
						e.printStackTrace();
					}
					vincitore = true;
				}
			if(!vincitore)
				try {
					db.aggiornaCrediti(0, 20, giocatori.get(i).getUtente().getUsername());
				} catch (EccezioneUtente e) {
					e.printStackTrace();
				}
		}

	}

}
