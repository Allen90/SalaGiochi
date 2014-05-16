package slotMachine;

import java.util.Random;

public class Combinazione {

	private static final int DIM_SLOT = 3;
	private static final int DIM_RULLO = 5;
	
	private int valori[] = new int[DIM_SLOT]; 
	private Random gen = new Random();
	private String output = null;
	
	public Combinazione(){
		for(int i = 0; i < DIM_SLOT; i++)
			valori[i] = 0;
	}
	
	public void ricalcola(){
		int n = 0;
		if(gen.nextInt(5)+1 == 5){
			n = gen.nextInt(4)+1;
			for(int i = 0; i < DIM_SLOT; i++)
				valori[i] = n;
		}else 
			for(int i = 0; i < DIM_SLOT; i++){
				valori[i] = gen.nextInt(DIM_RULLO)+1;
				System.out.println(valori[i]);
			}
		
	}
	
	protected void setValori(int[] valori){
		for(int i = 0; i < DIM_SLOT; i++){
			this.valori[i] = valori[i];
		}
	}

	public String getValori() {
		output = new String();
		for ( int i = 0; i < DIM_SLOT; i++)
			output += valori[i];
		return output;
	}
}
