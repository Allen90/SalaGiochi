package partite;

import java.util.ArrayList;

import Tombola.GiocatoreTombola;
import Tombola.Tabellone;

public class partitaTombola implements Runnable{

	ArrayList<GiocatoreTombola> giocatori = null;
	Tabellone tabellone = null;
	
	public partitaTombola(ArrayList<GiocatoreTombola> giocatori){
		tabellone = new Tabellone();
		this.giocatori = giocatori;
		for(GiocatoreTombola giocatore: giocatori)
			giocatore.aggiornaTabellone(tabellone);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
