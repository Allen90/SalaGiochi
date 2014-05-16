package slotMachine;

import java.util.Random;

public class Slot {

	private Combinazione c = null;

	
	public Slot(){
		c = new Combinazione();
		
	}
	
	public String getCombinazione(){
		c.ricalcola();
		Random r = new Random();
		
		return c.getValori();		
	}
	
}
