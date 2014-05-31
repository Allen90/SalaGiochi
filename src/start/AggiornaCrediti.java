package start;

import interfacciaDB.ConnessioneDB;

public class AggiornaCrediti implements Runnable{
	private ConnessioneDB db;

	public AggiornaCrediti(){
		db = ConnessioneDB.getInstance();
	}
	public void run() {
		while(true){
		try {
			
			Thread.sleep(3600000);
			//aggiorna crediti online
			//aggiorna crediti offline
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}

}
