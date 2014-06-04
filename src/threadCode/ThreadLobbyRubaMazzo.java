package threadCode;

import partite.PartitaRubaMazzo;
import codePartite.LobbyRubaMazzo;

public class ThreadLobbyRubaMazzo implements Runnable{
	private LobbyRubaMazzo rm;
	PartitaRubaMazzo task;
	public ThreadLobbyRubaMazzo(){
		rm = LobbyRubaMazzo.getInstance();
	}
	public void run(){
		while(true){
			if(rm.numUtentiLobby() > 1){
				try {
					Thread.sleep(30000);
					task = new PartitaRubaMazzo(rm.getLobbyRubaMazzo());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		}
			
	}

}
