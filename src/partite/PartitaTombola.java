package partite;

import java.util.ArrayList;

import tombola.GiocatoreTombola;
import tombola.SituazioneTombola;
import tombola.Tabella;
import tombola.Tabellone;



public class PartitaTombola implements Runnable{

	private ArrayList<GiocatoreTombola> giocatori = null;
	private Tabellone tabellone = null;
	private InfoPartitaTombola ipt;
	private boolean[] vincite;
	private int numPartita;

	public PartitaTombola(ArrayList<GiocatoreTombola> giocatori, int numPartita){
		this.numPartita = numPartita;
		tabellone = new Tabellone();
		this.giocatori = giocatori;
		ipt = InfoPartitaTombola.getInstance();
		for(int i = 0; i < 5;i++)
			vincite[i] = true;
		for(int i = 0; i< giocatori.size();i++){
			SituazioneTombola s = new SituazioneTombola(tabellone,giocatori.get(i),vincite,numPartita);
			ipt.addUtente(s);
		}
	}

	public void setVittoria(int tipoVittoria){
		vincite[tipoVittoria] = false;
		for(int i = 0; i< giocatori.size();i++){
			ipt.getUtente(giocatori.get(i).getUtente().getUsername()).aggiornaSituazione(tabellone, giocatori.get(i), vincite);
		}
	}

	public ArrayList<GiocatoreTombola> getGiocatori(){
		return giocatori;
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
				for(int j=0;j<giocatori.get(i).getNCartelle();j++)
					giocatori.get(i).controllaEstratto(estratto,j);
			}


		}

	}
}
