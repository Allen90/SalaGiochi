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
	private ArrayList<Utente> utenti;
	private TavoloRubamazzo trm;
	private ArrayList<GiocatoreRubamazzo> giocatori;
	private int numPartita;
	private boolean mossaFinita;
	private int turno;
	private ConnessioneDB db;

	public PartitaRubaMazzo(ArrayList<Utente> utenti,int numPartita){
		this.numPartita = numPartita;
		this.giocatori = giocatori;
		for(int i = 0; i< giocatori.size();i++){
			GiocatoreRubamazzo u = new GiocatoreRubamazzo(utenti.get(i));
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
		if(turno>giocatori.size())
			turno = 0;
		for(int i=0;i<giocatori.size();i++){
			iprm.getUtente(giocatori.get(i).getUtente().getUsername()).aggiornaSituazione(giocatori.get(i), trm);
			iprm.getUtente(giocatori.get(i).getUtente().getUsername()).setAbilitato(false);
			if( i == turno)
				iprm.getUtente(giocatori.get(i).getUtente().getUsername()).setAbilitato(true);
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

		while(!trm.isFinita()){
			while(!mossaFinita()){
			}
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
