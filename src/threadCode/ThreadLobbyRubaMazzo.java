package threadCode;

import java.util.ArrayList;

import model.Utente;
import partite.PartitaRubaMazzo;
import rubamazzo.GiocatoreRubamazzo;
import codePartite.LobbyRubaMazzo;

public class ThreadLobbyRubaMazzo implements Runnable{
	private ArrayList<Utente> utenti;
	private PartitaRubaMazzo task;
	private static ThreadLobbyRubaMazzo lrm;
	private ArrayList<PartitaRubaMazzo> partite;

	private ThreadLobbyRubaMazzo(){
		utenti = new ArrayList<Utente>();
		partite = new ArrayList<PartitaRubaMazzo>();
	}

	public ThreadLobbyRubaMazzo getInstance(){
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
		boolean ok;
		int indicePartita;
			for(int j = 0; j < partite.get(numPartita).getGiocatori().size();j++)
				if(partite.get(numPartita).getGiocatori().get(j).getUtente().getUsername().equals(username)){
					
					int m = m.getTipoMossa();
					switch(m){
					case 0: {partite.get(i).getTavolo().daGiocatoreABanco(m.getCartaGiocata(),username);ok=true;break;}
					case 1:{ try{
						partite.get(i).getTavolo().daBancoAGiocatore(m.getCartaGiocata(),m.getCarteBersagio.get(0),username);
					}
					catch(EccezioneRubamazzo e){
						ok = false;
					}
					break;
					}
					case 2:{ try{
						partite.get(i).getTavolo().daBancoAGiocatore(m.getCartaGiocata(),m.getCarteBersagio(),username);
					}
					catch(EccezioneRubamazzo e){
						ok = false;
					}
					break;
					}
					case 3: { try{
						partite.get(i).getTavolo().daGiocatoreAGiocatore(m.getCartaGiocata,m.getGiocatoreBersaglio,username);
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
					partite.add(task);
					svuotaLobbyRubaMazzo();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

}
