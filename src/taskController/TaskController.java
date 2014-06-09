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
	
	
	/*TODO controllo costo cartelle
	 * 
	 * il client ha il tasto "giocatombola" abilitato o meno in base al costo di una tabella
	 * il controllo del numero tabelle massime viene fato lato server 
	 * che mander� "KO#troppetabelle" (o simile) in caso di costo troppo alto
	 */
	public void giocoTombola(Utente utente, int numCartelle){
		ArrayList<Tabella> cartelle = new ArrayList<Tabella>();
		for(int i = 0; i< numCartelle;i++){
			Tabella c = new Tabella();
			cartelle.add(c);
		}
			
		gt = new GiocatoreTombola(cartelle,utente);
		lt.addUserLobbyTomb(gt);
	}
	
	public void giocoRubamazzo(Utente utente){
		lrm.addUserLobbyRubaMazzo(utente);
	}
	
	public Rollata rolla(Utente utente) {
		Rollata r = null;
		
		try {
			if(db.getUtente(utente.getUsername()).getCrediti() > 1) {
				r = new Rollata(true);				
				s = new Slot();
				int[] comb = s.calcolaCombinazione();
				int premio = s.getPremio(true);
				db.aggiornaCrediti(premio,1,utente.getUsername());
				r.setComb(comb);
				r.setPremio(premio);
				r.setCrediti(utente.getCrediti());
				r.setVincita(s.getStringaPremio());
			}
		}catch (EccezioneUtente e) {
			r = new Rollata(false);
			r.setCrediti(utente.getCrediti());
		}
		return r;
	}

	public ArrayList<Utente> aggClass(Utente utente) throws EccezioneClassificaVuota {
		ArrayList<Utente> classifica = db.getClassifica(true);
		return classifica;
	}

	public ArrayList<Utente> aggClassGiorn(Utente utente) throws EccezioneClassificaVuota {
		ArrayList<Utente> classifica = db.getClassifica(true);
		return classifica;
	}

}