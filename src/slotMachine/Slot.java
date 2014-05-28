package slotMachine;

public class Slot {

	private Combinazione c = null;
	private Jackpot j = null;
	
	public Slot(){
		c = new Combinazione();
		j = Jackpot.getInstance();
	}
	
	public String calcolaCombinazione(){		
		int[] combinazione = c.calcola();
		String output = null; 
		
		for(int i = 0; i < 3; i++)
			output += combinazione[i]+"#";
		return output;		
	}
	
	public int getPremio(boolean reset){
		int premio = 0;
		if(c.calcolaPremio() == 100){
			if(j.getJackpot() > 100){
				premio = j.getJackpot();
				if(reset) j.resetJackpot();
			}
		}
		else 
			premio = c.calcolaPremio();
		return premio;
	}
	
	public String getStringaPremio(){
		String output = null;
		if(getPremio(false) >= 0){
			if(getPremio(false) >= 100)
				output += "JACKPOT#";
			else output += "VINTO#";
		}
		else output += "PERSO#";
		output += getPremio(false)+"#";
		return output;
	}
}
