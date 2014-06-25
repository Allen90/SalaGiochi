package partite;

import java.util.ArrayList;

import tombola.GiocatoreTombola;
import tombola.SituazioneTombola;
import tombola.Tabellone;

/**
 *  thread che gestisce una singola partita, rimane attivo finche' almeno un premio è disponibile
 *  e tutti i numeri non vengono estratti, ad ogni numero estratto aggiorna la situazione partita
 *  di tutti gli utenti presenti in essa
 * @author fritz
 *
 */

public class PartitaTombola implements Runnable{

	private ArrayList<GiocatoreTombola> giocatori = null;
	private Tabellone tabellone = null;
	private InfoPartitaTombola ipt;
	private boolean[] vincite;
	@SuppressWarnings("unused")
	private int numPartita;
	private boolean terminato;
	private int premioAmbo;
	private int premioTerna;
	private int premioQuaterna;
	private int premioCinquina;
	private int premioTombola;

	public PartitaTombola(ArrayList<GiocatoreTombola> giocatori, int numPartita){
		System.out.println("partita creata" + numPartita);
		terminato = false;
		this.numPartita = numPartita;
		tabellone = new Tabellone();
		this.giocatori = new ArrayList<>();
		this.giocatori.addAll(giocatori);
		ipt = InfoPartitaTombola.getInstance();
		vincite = new boolean[5];
		for(int i = 0; i < 5;i++)
			vincite[i] = true;
		System.out.println("utenti presenti:");
		for(int i = 0; i< giocatori.size();i++){
			System.out.println(giocatori.get(i).getUtente().getUsername());
			SituazioneTombola s = new SituazioneTombola(tabellone,giocatori.get(i),vincite,numPartita);
			ipt.addUtente(s);
		}
		int cartelleTotali = 0;
		for(int i = 0; i< giocatori.size();i++){
			cartelleTotali = giocatori.get(i).getNCartelle();
		}
		
		int premioTot = cartelleTotali*20;
		
		premioAmbo = premioTot*5/100;
		premioTerna = premioTot*10/100;
		premioQuaterna = premioTot*15/100;
		premioCinquina = premioTot*20/100;
		premioTombola = premioTot*50/100;
		
	}

	
	public int getPremioAmbo(){
		return premioAmbo;
	}
	
	public int getPremioTerna(){
		return premioTerna;
	}
	
	public int getPremioQuaterna(){
		return premioQuaterna;
	}
	
	public int getPremioCinquina(){
		return premioCinquina;
	}
	
	public int getPremioTombola(){
		return premioTombola;
	}
	
	/**
	 * aggiorna le vittorie disponibili nella partita, togliendo quella passata dalla lobby
	 * @param tipoVittoria
	 * @return
	 */
	public boolean setVittoria(int tipoVittoria){
		boolean ok = false;
		if(vincite[tipoVittoria] == false)
			ok = false;
		else{
			vincite[tipoVittoria] = false;
			for(int i = 0; i< giocatori.size();i++){
				ipt.getUtente(giocatori.get(i).getUtente().getUsername()).aggiornaSituazione(tabellone, giocatori.get(i), vincite);
			}
			
			ok = true;
		}
		controlloFinito();
		return ok;
		
	}

	public ArrayList<GiocatoreTombola> getGiocatori(){
		return giocatori;
	}
	
	public void controlloFinito(){
		int cont = 0;
		for(int i = 0;i< 5;i++){
			if(vincite[i] == false)
				cont++;
		}
		if(cont == 5)
			terminato = true;
	}

	@Override
	public void run() {
		
		while(!tabellone.terminato() && terminato == false){
			//System.out.println("Ho il ciclo");
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int estratto = tabellone.estrai();
			//System.out.println(estratto);
//			System.out.println(giocatori.size());
			for(int i=0;i<giocatori.size();i++){
				//System.out.println(giocatori.get(i).getNCartelle());
				for(int j=0;j<giocatori.get(i).getNCartelle();j++){
					//System.out.println("giocatore " + i + " cartella "+j);
					giocatori.get(i).controllaEstratto(estratto,j);
					//giocatori.get(i).getCartelle().get(j).stampa();
				}
				ipt.getUtente(giocatori.get(i).getUtente().getUsername()).aggiornaSituazione(tabellone, giocatori.get(i), vincite);
				System.out.println("situazione aggiornata dopo l'estrazione");
			}


		}
		System.out.println("anto bananaro");

	}
}
