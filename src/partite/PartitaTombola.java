package partite;

import java.util.ArrayList;

import tombola.GiocatoreTombola;
import tombola.Tabella;
import tombola.Tabellone;



public class PartitaTombola implements Runnable{

	ArrayList<GiocatoreTombola> giocatori = null;
	Tabellone tabellone = null;
	InfoPartitaTombola ipt;
	
	public PartitaTombola(ArrayList<GiocatoreTombola> giocatori){
		tabellone = new Tabellone();
		this.giocatori = giocatori;
		ipt = InfoPartitaTombola.getInstance();
		
		for(int i = 0; i< giocatori.size();i++){
			SituazioneTombola s = new SituazioneTombola(tabellone,giocatori.get(i));
			ipt.addUtente(s);
		}
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
				for(int j=0;j<giocatori.getNCartelle();j++)
					giocatori.get(i).controllaEstratto(estratto,j);
			}
			
			
		}
		
	}
}
