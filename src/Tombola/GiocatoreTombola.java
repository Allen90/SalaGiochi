package Tombola;

import java.util.ArrayList;

public class GiocatoreTombola {
	
	private ArrayList<Tabella> cartelle = null;
	private Client client = null;
	
	public GiocatoreTombola(ArrayList<Tabella> cartelle, Client client){
		this.client = client;
		this.cartelle = new ArrayList<Tabella>(cartelle);
	}
	
	
}
