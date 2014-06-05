package threadCode;

import partite.PartitaTombola;
import codePartite.LobbyTombola;

public class ThreadLobbyTombola implements Runnable{
	private LobbyTombola lt;
	private PartitaTombola pt;
	public ThreadLobbyTombola(){
		lt = LobbyTombola.getIstance();
	}
	public void run(){
		while(true){
			if(lt.numUtentiLobby() >1){
				try {
					Thread.sleep(30000);
					pt = new PartitaTombola(lt.getUtenti());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
