package partite;

import java.util.ArrayList;

import tombola.GiocatoreTombola;
import tombola.Tabella;
import tombola.Tabellone;



public class PartitaTombola implements Runnable{

	ArrayList<GiocatoreTombola> giocatori = null;
	Tabellone tabellone = null;

	public PartitaTombola(ArrayList<GiocatoreTombola> giocatori){
		tabellone = new Tabellone();
		this.giocatori = giocatori;
		for(GiocatoreTombola giocatore: giocatori)
			giocatore.aggiornaTabellone(tabellone);
	}

	@Override
	public void run() {
		
		while(!tabellone.terminato()){
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int estratto = tabellone.estrai();
			for(int i=0;i<giocatori.size();i++){
				ArrayList<Tabella> t = giocatori.get(i).getCartelle();
				for(int j=0;j<t.size();j++)
					t.get(i).setEstratto(estratto);
			}
			
		}
		
	}
}
