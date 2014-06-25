package start;

import java.util.Calendar;
import java.util.Date;

import eccezioni.EccezioneClassificaVuota;
import interfacciaDB.ConnessioneDB;

/**
 * classe che gestisce l'aggiornamento periodico dei crediti degli utenti loggati e non
 * @author fritz
 *
 */

public class AggiornaCrediti implements Runnable{
	
	private ConnessioneDB db;
	private boolean continua;
	public AggiornaCrediti(){
		db = ConnessioneDB.getInstance();
		continua = true;
	}
	
	public void chiudi(){
		continua = false;
	}
	
	public void run() {
//		Date d = new Date();
		while(continua){
		try {
			Thread.sleep(3600000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Date d2 = new Date();
//		Calendar c = Calendar.
		try {
			db.aggiornaPeriodico(1);
		} catch (EccezioneClassificaVuota e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}

}
