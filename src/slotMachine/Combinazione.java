package slotMachine;

import java.util.Random;

public class Combinazione {

	private static final int DIM_SLOT = 3;
	private static final int DIM_RULLO = 5;
	private Random gen = new Random();
	private int valori[] = new int[DIM_SLOT]; 
	private String output = null;
	private int premio = 0;
	
	public Combinazione(){
		for(int i = 0; i < DIM_SLOT; i++)
			valori[i] = 0;
	}
	
	public void ricalcola(){
		premio = 0;

		for(int i = 0; i < DIM_SLOT; i++){
			valori[i] = gen.nextInt(DIM_RULLO)+1;
			//System.out.println(valori[i]);
		}
		premio = calcolaPremio();
	}

	private int calcolaPremio(){
		int premio = 0;
		boolean flags[] = {false, false};
		boolean jackpot = false; 
		for(int i=0; i<2; i++)
			if(valori[i] == valori[i+1]) flags[i] = true;
		
		if(flags[0] && flags[1] && valori[0] == 5) jackpot = true;
		
		if(jackpot == true) premio = 100;
		else if(flags[0] != flags[1]) premio = 10;
		else if(flags[0] == flags[1] && flags[0] == true) premio = 20;
		else premio = 0;
		return premio;
	}
	
	protected void setValori(int[] valori){
		for(int i = 0; i < DIM_SLOT; i++){
			this.valori[i] = valori[i];
		}
	}

	public String getValori() {
		output = new String();
		for ( int i = 0; i < DIM_SLOT; i++)
			output += valori[i] + "#";
		return output;
	}
	
	public int getPremio(){
		return premio;
	}
}
