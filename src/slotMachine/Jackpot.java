package slotMachine;

public class Jackpot {

	private static Jackpot j = null;
	private int deposito;

	private Jackpot(){
		deposito = 0;
	}
	
	public static Jackpot getInstance(){
		if(j == null){                
			j = new Jackpot();                
		}            
		return j;
	}
	
	public int getJackpot(){
		return deposito;
	}
	
	public int incJackpot(){
		deposito += 1;
		return deposito;
	}
	
	public void resetJackpot(){
		deposito = 0;
	}
}
