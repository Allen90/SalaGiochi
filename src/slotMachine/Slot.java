package slotMachine;

public class Slot {

	private Combinazione c = null;
	private int premio = 0;
	private Jackpot j = null;
	
	public Slot(){
		c = new Combinazione();
		j = Jackpot.getInstance();
	}
	
	public void rolla(){
		c.ricalcola();
	}
	
	public String getCombinazione(){		
		return c.getValori();		
	}
	
	public int getPremio(){
		premio = c.getPremio();
		if(premio == 100)
			if(j.getJackpot() > 100)
				premio = j.getJackpot();
		return premio-1;
	}
}
