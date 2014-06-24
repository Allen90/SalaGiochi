package start;

import java.util.ArrayList;


/**
 * singleton che contiene tutti gli username loggati, viene utilizzato per l'aggiornamento periodico
 * @author fritz
 *
 */

public class UtentiLoggati {
	private static UtentiLoggati l;
	private ArrayList<String> loggati;

	private UtentiLoggati(){
		loggati = new ArrayList<String>();
	}

	public static UtentiLoggati getIstance(){
		if( l == null){
			l = new UtentiLoggati();
		}

		return l;
	}

	public ArrayList<String> getLoggati(){
		return loggati;
	}

	public void addLoggato(String utente){
		loggati.add(utente);
	}

	public boolean presente(String user){
		for(String s : loggati){
			if(user.equals(s))
				return true;
		}
		return false;
	}

	public void rimuovi(String user){
		for(int i = 0; i < loggati.size();i++){
			if(loggati.get(i).equals(user))
				loggati.remove(i);
		}
	}
}
