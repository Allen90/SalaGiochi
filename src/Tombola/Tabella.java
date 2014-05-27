package Tombola;

import java.util.Random;

public class Tabella {
	
	private int tabella[][];
	private Random r = new Random();
	private Tabellone t;

	public Tabella(){
		t.resetta();
		int estratto = 0;
		tabella = new int[3][9];
		for(int decina=0; decina<9; decina++)
			for(int j=0; j<3; j++){
				tabella[j][decina] = t.estraiDaDecina(decina);
				System.out.println("estratto numero di riga "+j+" e colonna "+decina+": --->"+tabella[j][decina]);
			}
		for(int i=0;i<3;i++)
			for(int j=0;j<4;j++){
				do{
					estratto = r.nextInt(9);
				}while(tabella[i][estratto] == 0);
				tabella[i][estratto] = 0;
			}
		t.resetta();
	}
	
	public int getNumero(int riga, int colonna){
		return tabella[riga][colonna];
	}
	
	public void stampa(){
		for(int i=0;i<3;i++){
			for(int j=0;j<9;j++){
				if(tabella[i][j] != 0)
					System.out.print(tabella[i][j]+"\t");
				else System.out.print(" \t");
			}
			System.out.println();
		}
	}
}
