package Tombola;

import java.util.Random;

public class Tabella {
	
	private Casella tabella[][];
	private Random r = new Random();
	private Tabellone t;
	private int[] vincente;

	public Tabella(){
		for(int i=0; i<3; i++)
			vincente[i] = 0;
		t = new Tabellone();
		int estratto = 0;
		tabella = new Casella[3][9];
		for(int decina=0; decina<9; decina++)
			for(int j=0; j<3; j++){
				tabella[j][decina].setNumero(t.estraiDaDecina(decina));
				System.out.println("estratto numero di riga "+j+" e colonna "+decina+": --->"+tabella[j][decina]);
			}
		
		for(int i=0;i<3;i++)
			for(int j=0;j<4;j++){
				do{
					estratto = r.nextInt(9);
				}while(tabella[i][estratto].getNumero() == 0);
				tabella[i][estratto].setNumero(0);
			}
		t.resetta();
	}
	
	public int getNumero(int riga, int colonna){
		return tabella[riga][colonna].getNumero();
	}
	
	public void setEstratto(int riga, int colonna, boolean estratto){
		tabella[riga][colonna].setEstratto(estratto);
	}
	
	public boolean isEstratto(int riga, int colonna){
		return tabella[riga][colonna].isEstratto();
	}
	
	public void stampa(){
		for(int i=0;i<3;i++){
			for(int j=0;j<9;j++){
				if(tabella[i][j].getNumero() != 0)
					System.out.print(tabella[i][j]+"\t");
				else System.out.print(" \t");
			}
			System.out.println();
		}
	}
	
	public void setEstratto(int estratto){
		for(int i = 0; i<3; i++)
			for(int j = 0; j<9; j++)
				if(getNumero(i, j) == estratto)
					setEstratto(i, j, true);
		setVincente();
	}
	
	private int statusVincita(int numeroTrue){
		if(numeroTrue < 2) return 0;
		else return numeroTrue;
	}
	
	private void setVincente(){
		int contaTrue = 0;
		for(int i = 0; i < 3; i++)
			if(vincente[i] != 1){
				for(int j = 0; j<9; j++)
					if(isEstratto(i, j))
						contaTrue++;
				vincente[i] = statusVincita(contaTrue);
				contaTrue = 0;
			}else vincente[i] = 1;
	}
	
	
	
	public int[] getVincente(){
		return vincente;
	}
}
