package start;

import interfacciaDB.ConnessioneDB;

public class AggiornaCrediti implements Runnable{
	private ConnessioneDB db;
	private UtentiLoggati ul;

	public AggiornaCrediti(){
		db = ConnessioneDB.getInstance();
		ul = UtentiLoggati.getIstance();
	}
	public void run() {
		while(true){
		try {
			Thread.sleep(3600000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db.aggiornaCrediti(ul.getLoggati());
		}
		
	}

}
