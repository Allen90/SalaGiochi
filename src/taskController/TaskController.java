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
import start.UtentiLoggati;
import tombola.GiocatoreTombola;
import tombola.SituazioneTombola;
import tombola.Tabella;
import userModel.EntryClassifica;
import userModel.Utente;
import eccezioni.EccezioneClassificaVuota;
import eccezioni.EccezioneUtente;


/**
 *  classe che gestisce le richieste del client comuni ai due tipi di comunicazione: rmi e socket
 * @author fritz
 *
 */


public class TaskController {
	private GiocatoreTombola gt;
	private ConnessioneDB db;
	private ThreadLobbyTombola lt;
	private ThreadLobbyRubaMazzo lrm;
	private Slot s;
	private InfoPartitaTombola ipt;
	private InfoPartitaRubaMazzo iprm;
	private UtentiLoggati l;

	public TaskController(){
		db = ConnessioneDB.getInstance();
		lt = ThreadLobbyTombola.getIstance();
		lrm = ThreadLobbyRubaMazzo.getInstance();
		ipt = InfoPartitaTombola.getInstance();
		iprm = InfoPartitaRubaMazzo.getInstance();
		l = UtentiLoggati.getIstance();
	}

	/**
	 * rimuove l'utente passato dagli utenti loggati e restuisce false, permettendo la chiusura del controoller delle richieste che l'ha chiamato 
	 * @param username
	 * @return
	 */
	public boolean termina(String username){
		
		for(int i=0;i< l.getLoggati().size();i++){
			if(l.getLoggati().get(i).equals(username)){
				l.rimuovi(username);
			}
		}
		return false;
	}
	
	/**
	 *  riceve una mossa dal controller e la invia alla lobby, restituisce la risposta ricevuta dalla lobby
	 * @param utente
	 * @param m
	 * @param numPartita
	 * @return boolean
	 */

	public boolean mossaRubaMazzo(Utente utente ,Mossa m, int numPartita){
		return lrm.controllaMossa(utente.getUsername(), numPartita, m);
	}
	
	/**
	 *  riceve dal controller una vittoria e la invia alla lobby, restituisce la risposta ricevuta dalla lobbby
	 * @param utente
	 * @param numPartita
	 * @param tipoVittoria
	 * @param indiceCartella
	 * @param indiceRiga
	 * @return boolean
	 * @throws EccezioneUtente 
	 */

	public boolean vintoTombola(Utente utente,int numPartita,int tipoVittoria, int indiceCartella,int indiceRiga) throws EccezioneUtente{
		System.out.println("sto mandando alla lobby la richiesta di vincita");
		return lt.aggiornaVincite(utente.getUsername(), numPartita, tipoVittoria, indiceCartella, indiceRiga);
	}


	/**
	 *  restituisce la situazione tombola delll'utente passato cercandolo tra tutte le situazioni rubamazzo presenti
	 * @param utente
	 * @return SituazioneTombola
	 */
	public SituazioneTombola aggTombola(Utente utente){
		return ipt.getUtente(utente.getUsername());
	}
	
	/**
	 * restituisce la situazione tombola delll'utente passato cercandolo tra tutte le situazioni rubamazzo presenti
	 * @param utente
	 * @return
	 */

	public SituazioneRubamazzo aggRubamazzo(Utente utente){
		return iprm.getUtente(utente.getUsername());
	}

	/**
	 *  invia la richiesta di giocata tombola alla lobby
	 * @param utente
	 * @param numCartelle
	 * @return
	 * @throws EccezioneUtente
	 */
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
		db.aggiornaCrediti(0, numCartelle*20, utente.getUsername());
		return ok;
		
	}
	
	/**
	 * invia la richiesta di giocata rubamazzo alla lobby
	 * @param utente
	 * @return
	 * @throws EccezioneUtente
	 */
	public boolean giocoRubamazzo(Utente utente) throws EccezioneUtente{
		boolean ok;
		if(db.getUtente(utente.getUsername()).getCrediti()<200)
			ok = false;
		else{
			ok = true;
		lrm.addUserLobbyRubaMazzo(utente);
		}
		db.aggiornaCrediti(0, 20, utente.getUsername());
		return ok;
	}
	
	
	/**
	 * genera una nuova giocata della slot machine se possibile e restituisce il risultato 
	 * @param username
	 * @return Rollata
	 * @throws EccezioneUtente
	 */
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
	
	/**
	 * crea la classifica utenti aggiornata e la restituisce al controller che l'ha richiesta
	 * @param giorn
	 * @return
	 * @throws EccezioneClassificaVuota
	 */

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
