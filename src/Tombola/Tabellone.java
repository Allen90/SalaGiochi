package Tombola;

import java.util.Random;

public class Tabellone { 
	
	private Casella numeri[][] = new Casella[9][10];
	Random estrattore = new Random();
	int estratti = 0;

	public Tabellone(){
		riempi();
		estratti = 0;
	}
	
	public void resetta(){
		riempi();
		estratti = 0;
	}
	
	public Casella[][] getTabellone(){
		return numeri;
	}
	
	private void riempi(){
		for(int i=0;i<9;i++)
			for(int j=0;j<10;j++)
				numeri[i][j].setNumero(i*10+j+1);
	}
	
	public int estrai(){
		int estratto = 0;
		int decine = 0, unita = 0; 
		do{
			estratto = estrattore.nextInt(90)+1;
			unita = estratto%10;
			decine = (estratto - unita)/10;
		}while(numeri[decine][unita-1].isEstratto());
		numeri[decine][unita-1].setEstratto(true);;
		estratti ++;
		return estratto;
	}	
	
	public boolean terminato(){
		if(estratti == 90)
			return true;
		else return false;
	}
	
//	public void stampa(){
//		for(int i=0;i<9;i++){
//			for(int j=0;j<10;j++)
//				System.out.print(numeri[i][j]+"\t");
//			System.out.println();
//		}
//	}
	
	public int estraiDaDecina(int decina){
		int estratto = 0;
		int unita = 0;
		do{
			unita = estrattore.nextInt(10);
			estratto = numeri[decina][unita].getNumero();
		}while(numeri[decina][unita].isEstratto());
		numeri[decina][unita].setEstratto(true);;
		return estratto;
	}
	
	

	
}
