package lobby;

import java.util.ArrayList;

import partite.PartitaTombola;
import tombola.GiocatoreTombola;

public class ThreadLobbyTombola implements Runnable{
	private static ThreadLobbyTombola lt;
	private PartitaTombola task;
	private ArrayList<PartitaTombola> pt; 
	private ArrayList<GiocatoreTombola> giocatori;


	private ThreadLobbyTombola(){
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

	public boolean aggiornaVincite(String username,int numPartita,int tipoVittoria, int indiceCartella,int indiceRiga){
		boolean ok = false;
		System.out.println("ricevuto vinto tombola da: "+username);
		ok = pt.get(numPartita).setVittoria(tipoVittoria-1);
		for(int i = 0;i< pt.get(numPartita).getGiocatori().size();i++){
			if(pt.get(numPartita).getGiocatori().get(i).getUtente().getUsername().equals(username))
				pt.get(numPartita).getGiocatori().get(i).getCartelle().get(indiceCartella).rigaVinta(indiceRiga);
		}
		System.out.println("rispondo al client con: "+ok);
		return ok;
	}

	public void run(){
		int n = numUtentiLobby();
		while(true){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.out.println("utenti presenti:" + numUtentiLobby());
			if(n > 0){
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
