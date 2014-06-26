package start;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import userModel.Utente;
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
		Date d = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int day = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
		while(continua){
		try {
			Thread.sleep(3600000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date d2 = new Date();
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(d2);
		int day2 = cal2.get(Calendar.DAY_OF_MONTH);
		if(day != day2 ){
			ArrayList<Utente> utenti;
			try {
				utenti = db.getClassifica(true);
				for(int i = 0;i< utenti.size();i++)
					db.resetCreditiG(utenti.get(i).getUsername());
			} catch (EccezioneClassificaVuota e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		try {
			db.aggiornaPeriodico(1);
		} catch (EccezioneClassificaVuota e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}

}
