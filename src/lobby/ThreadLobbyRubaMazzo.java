package lobby;

import java.util.ArrayList;

import partite.PartitaRubaMazzo;
import rubamazzo.GiocatoreRubamazzo;
import rubamazzo.Mossa;
import userModel.Utente;
import eccezioni.EccezioneRubamazzo;

public class ThreadLobbyRubaMazzo implements Runnable{
	private ArrayList<Utente> utenti;
	private PartitaRubaMazzo task;
	private static ThreadLobbyRubaMazzo lrm;
	private ArrayList<PartitaRubaMazzo> partite;

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
		for(int i = 0;i< utenti.size();i++)
			utenti.remove(i);
	}

	public void addUserLobbyRubaMazzo(Utente c){
		utenti.add(c);
	}

	public boolean controllaMossa(String username,int numPartita,Mossa m){
		boolean ok = false;
		int indicePartita;
			for(int j = 0; j < partite.get(numPartita).getGiocatori().size();j++){
				if(partite.get(numPartita).getGiocatori().get(j).getUtente().getUsername().equals(username)){
					
					int tipo = m.getTipoMossa();
					switch(tipo){
					case 0: {partite.get(numPartita).getTavolo().daGiocatoreABanco(m.getCartaGiocata(),username);ok=true;break;}
					case 1:{ try{
						partite.get(numPartita).getTavolo().daBancoAGiocatore(m.getCartaGiocata(),m.getCartaBersaglio(),username);
					}
					catch(EccezioneRubamazzo e){
						ok = false;
					}
					break;
					}
					case 2:{ try{
						partite.get(numPartita).getTavolo().daBancoAGiocatore(m.getCartaGiocata(),m.getCarteBersaglio(),username);
					}
					catch(EccezioneRubamazzo e){
						ok = false;
					}
					break;
					}
					case 3: { try{
						partite.get(numPartita).getTavolo().daGiocatoreAGiocatore(username,m.getCartaGiocata(),m.getGiocatoreBersaglio());
					}
					catch(EccezioneRubamazzo e){
						ok = false;
					}
					break;
					}
					}
				}
		}
		if(ok == true)
			partite.get(numPartita).setMossaFinita(true);
		return ok;
	}

	public void run(){
		while(true){
			if(numUtentiLobby() > 1){
				try {
					Thread.sleep(30000);
					task = new PartitaRubaMazzo(getLobbyRubaMazzo(),partite.size());
					Thread t = new Thread(task);
					t.start();
					partite.add(task);
					svuotaLobbyRubaMazzo();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

}
