package taskController;

import interfacciaDB.ConnessioneDB;

import java.util.ArrayList;

import partite.InfoPartitaRubaMazzo;
import partite.InfoPartitaTombola;
import lobby.ThreadLobbyRubaMazzo;
import lobby.ThreadLobbyTombola;
import rubamazzo.Mossa;
import rubamazzo.SituazioneRubamazzo;
import slot.Rollata;
import slot.Slot;
import tombola.GiocatoreTombola;
import tombola.SituazioneTombola;
import tombola.Tabella;
import userModel.EntryClassifica;
import userModel.Utente;
import eccezioni.EccezioneClassificaVuota;
import eccezioni.EccezioneUtente;

public class TaskController {
	private GiocatoreTombola gt;
	private ConnessioneDB db;
	private ThreadLobbyTombola lt;
	private ThreadLobbyRubaMazzo lrm;
	private Slot s;
	private InfoPartitaTombola ipt;
	private InfoPartitaRubaMazzo iprm;


	public TaskController(){
		db = ConnessioneDB.getInstance();
		lt = ThreadLobbyTombola.getIstance();
		lrm = ThreadLobbyRubaMazzo.getInstance();
		ipt = InfoPartitaTombola.getInstance();
		iprm = InfoPartitaRubaMazzo.getInstance();
	}

	public boolean termina(){
		return false;
	}


	public boolean mossaRubaMazzo(Utente utente ,Mossa m, int numPartita){
		return lrm.controllaMossa(utente.getUsername(), numPartita, m);
	}

	public boolean vintoTombola(Utente utente,int numPartita,int tipoVittoria, int indiceCartella,int indiceRiga){
		return lt.aggiornaVincite(utente.getUsername(), numPartita, tipoVittoria, indiceCartella, indiceRiga);
	}


	public SituazioneTombola aggTombola(Utente utente){
		return ipt.getUtente(utente.getUsername());
	}

	public SituazioneRubamazzo aggRubamazzo(Utente utente){
		return iprm.getUtente(utente.getUsername());
	}


	public boolean giocoTombola(Utente utente, int numCartelle) throws EccezioneUtente{
		boolean ok;
		if(db.getUtente(utente.getUsername()).getCrediti()<100)
			ok = false;
		else{
			ok = true;
			ArrayList<Tabella> cartelle = new ArrayList<Tabella>();
			for(int i = 0; i< numCartelle;i++){
				
				Tabella c = new Tabella();
				cartelle.add(c);

			}
			gt = new GiocatoreTombola(cartelle,utente);

			lt.addUserLobbyTomb(gt);
			System.out.println("Giocatore aggiunto nella lobby");
			System.out.println("giocatori nella lobby"+lt.numUtentiLobby());
		}
		return ok;
	}

	public boolean giocoRubamazzo(Utente utente) throws EccezioneUtente{
		boolean ok;
		if(db.getUtente(utente.getUsername()).getCrediti()<100)
			ok = false;
		else{
			ok = true;
		lrm.addUserLobbyRubaMazzo(utente);
		}
		return ok;
	}

	public Rollata rolla(String username) throws EccezioneUtente {
		Rollata r = null;
		int premio  = 0;
		Utente utente = db.getUtente(username);
		try {
			if(utente.getCrediti() > 1) {

				r = new Rollata(true);

				s = new Slot();
				int[] comb = s.calcolaCombinazione();
				premio = s.getPremio(true);
				db.aggiornaCrediti(premio,1,utente.getUsername());

				r.setComb(comb);
				r.setPremio(premio);
				r.setCrediti(utente.getCrediti());

			}
		} catch (EccezioneUtente e) {
			r = new Rollata(false);
		}
		return r;

	}

	public ArrayList<EntryClassifica> aggClass(boolean giorn) throws EccezioneClassificaVuota {
		ArrayList<Utente> temp =  db.getClassifica(giorn);
		ArrayList<EntryClassifica> classifica = new ArrayList<EntryClassifica>();
		for(Utente u : temp){
			classifica.add(new EntryClassifica(u,giorn));
		}
		return classifica;
	}
	
	public int getPosizione(String username) throws EccezioneUtente{
		return db.getPosizioneGlobale(username);
		
	}

}
