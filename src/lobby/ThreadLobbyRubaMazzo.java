package lobby;

import java.util.ArrayList;

import partite.PartitaRubaMazzo;
import rubamazzo.Mossa;
import userModel.Utente;
import eccezioni.EccezioneRubamazzo;

public class ThreadLobbyRubaMazzo implements Runnable{
	private ArrayList<Utente> utenti;
	private PartitaRubaMazzo task;
	private static ThreadLobbyRubaMazzo lrm;
	private ArrayList<PartitaRubaMazzo> partite;
	private boolean continua;

	private ThreadLobbyRubaMazzo(){
		utenti = new ArrayList<Utente>();
		partite = new ArrayList<PartitaRubaMazzo>();
	}

	public static ThreadLobbyRubaMazzo getInstance(){
		if(lrm == null)
			lrm = new ThreadLobbyRubaMazzo();
		return lrm;
	}

	public int numUtentiLobby(){
		return utenti.size();
	}

	public ArrayList<Utente> getLobbyRubaMazzo(){
		return utenti;
	}

	public void svuotaLobbyRubaMazzo(){
		utenti.removeAll(utenti);
	}

	public void addUserLobbyRubaMazzo(Utente c){
		utenti.add(c);
	}

	public boolean controllaMossa(String username,int numPartita,Mossa m){
		System.out.println("mossa ricevuta");
		boolean ok = false;
		for(int j = 0; j < partite.get(numPartita).getGiocatori().size();j++){
			if(partite.get(numPartita).getGiocatori().get(j).getUtente().getUsername().equals(username)){

				int tipo = m.getTipoMossa();
				switch(tipo){
				case 0: {try {
					partite.get(numPartita).getTavolo().daGiocatoreABanco(m.getCartaGiocata(),username);
					ok = true;
				} catch (EccezioneRubamazzo e) {
					ok = false;
				}
				break;
				}
				case 1:{ try{
					partite.get(numPartita).getTavolo().daBancoAGiocatore(m.getCartaGiocata(),m.getCartaBersaglio(),username);
					ok = true;
				}
				catch(EccezioneRubamazzo e){
					ok = false;
				}
				break;
				}
				case 2:{ try{
					partite.get(numPartita).getTavolo().daBancoAGiocatore(m.getCartaGiocata(),m.getCarteBersaglio(),username);
					ok = true;
				}
				catch(EccezioneRubamazzo e){
					ok = false;
				}
				break;
				}
				case 3: { try{
					partite.get(numPartita).getTavolo().daGiocatoreAGiocatore(username,m.getCartaGiocata(),m.getGiocatoreBersaglio());
					ok = true;
				}
				catch(EccezioneRubamazzo e){
					ok = false;
				}
				break;
				}
				}
			}
		}
		System.out.println("LOBBY STAMPO ESITO: " + ok);
		if(ok == true){
			partite.get(numPartita).setMossaFinita(true);
		}
		return ok;
	}


	public void chiudi(){
		continua = false;
	}

	public void run(){
		continua = true;
		while(continua){
			int n = numUtentiLobby();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(n);
			if(n > 1){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				task = new PartitaRubaMazzo(getLobbyRubaMazzo(),partite.size());
				Thread t = new Thread(task);
				partite.add(task);
				t.start();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				svuotaLobbyRubaMazzo();
			}
			n = numUtentiLobby();


		}

	}

}
