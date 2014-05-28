package slotMachine;

import java.util.Random;

public class Combinazione {

	private Random gen = new Random();
	private int valori[] = null; 
	
	public Combinazione(){
		valori = new int[3];
		for(int i = 0; i < 3; i++)
			valori[i] = 0;
	}
	
	public int[] calcola(){
		for(int i = 0; i < 3; i++){
			valori[i] = gen.nextInt(5)+1;
		}
		return valori;
	}
	
	public int[] getValori(){
		return valori;
	}
	
	public int calcolaPremio(){
		int premio = 0;
		
		// controlli {01, 12, 02}
		boolean flags[] = {false, false, false}; 
		
		
		for(int i=0; i<2; i++)
			if(valori[i] == valori[i+1]) flags[i] = true;
		if(valori[0] == valori[2])
			flags[2] = false;	
		
		
		if(flags[0] == flags[1])
			if(flags[0]){
				if(valori[0] == 5) premio = 100;
				else premio = 20;
			}else{
				if(flags[2]) premio = 10;
				else premio = 0;			
			}
		else premio = 10;
		
		return premio;
	}




}
