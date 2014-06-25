package lobby;

import interfacciaDB.ConnessioneDB;

import java.util.ArrayList;

import eccezioni.EccezioneUtente;
import partite.PartitaTombola;
import tombola.GiocatoreTombola;

/**
 * thread che gestisce la coda di persone che vogliono giocare a tombola, una volta che 
 * sono presenti almeno 2 utenti, aspetta ancora 20 secondi e poi crea il thread che gestira' la partita con
 * gli utenti attualmente in coda.
 * La classe si occupa anche di gestire le richieste di vittoria degli utenti, aggiornando la partita 
 * in cui il giocatore che ha chiamato la vittoria è presente 
 * @author fritz
 *
 */

public class ThreadLobbyTombola implements Runnable{
	private static ThreadLobbyTombola lt;
	private PartitaTombola task;
	private ArrayList<PartitaTombola> pt; 
	private ArrayList<GiocatoreTombola> giocatori;
	private boolean continua;
	private ConnessioneDB db;

	private ThreadLobbyTombola(){
		db = ConnessioneDB.getInstance();
		giocatori = new ArrayList<GiocatoreTombola>();
		pt = new ArrayList<PartitaTombola>();

	}

	public static ThreadLobbyTombola getIstance(){
		if(lt == null)
			lt = new ThreadLobbyTombola();
		return lt;
	}

	public int numUtentiLobby(){
		return giocatori.size();
	}

	public ArrayList<GiocatoreTombola> getUtenti(){
		return giocatori;
	}

	public void svuotaLobbyTombola(){
		giocatori.removeAll(giocatori);
	}

	public void addUserLobbyTomb(GiocatoreTombola g){
		giocatori.add(g);
	}

	public boolean aggiornaVincite(String username,int numPartita,int tipoVittoria, int indiceCartella,int indiceRiga) throws EccezioneUtente{
		boolean ok = false;
		System.out.println("ricevuto vinto tombola da: "+username);
		ok = pt.get(numPartita).setVittoria(tipoVittoria-1);
		for(int i = 0;i< pt.get(numPartita).getGiocatori().size();i++){
			if(pt.get(numPartita).getGiocatori().get(i).getUtente().getUsername().equals(username))
				pt.get(numPartita).getGiocatori().get(i).getCartelle().get(indiceCartella).rigaVinta(indiceRiga);
		}
		int premio = 0;
		switch(tipoVittoria){
		case 1:{premio = pt.get(numPartita).getPremioAmbo();break;}
		case 2:{premio = pt.get(numPartita).getPremioTerna();break;}
		case 3:{premio = pt.get(numPartita).getPremioQuaterna();break;}
		case 4:{premio = pt.get(numPartita).getPremioCinquina();break;}
		case 5:{premio = pt.get(numPartita).getPremioTombola();break;}
		}
		db.aggiornaCrediti(premio, 0, username);
		System.out.println("rispondo al client con: "+ok);
		return ok;
	}

	
	public void chiudi(){
		continua = false;
	}
	
	public void run(){
		continua = true;
		int n = numUtentiLobby();
		while(continua){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.out.println("utenti presenti:" + numUtentiLobby());
			if(n > 1){
				System.out.println("qui in lobby con piu' di una persona");
				//				try {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("sto per creare la partita");
				task = new PartitaTombola(getUtenti(),pt.size());
				Thread t = new Thread(task);
				pt.add(task);
				t.start();
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				svuotaLobbyTombola();
				//				} catch (InterruptedException e) {
				//					// TODO Auto-generated catch block
				//					e.printStackTrace();
				//				}
			}
			n = numUtentiLobby();
		}
	}

}
