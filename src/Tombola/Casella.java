package Tombola;

public class Casella {

	private int numero;
	private boolean estratto;
	
	public Casella (){
		numero = -1;
		estratto = false;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public boolean isEstratto() {
		return estratto;
	}

	public void setEstratto(boolean estratto) {
		this.estratto = estratto;
	}
	
	
}
