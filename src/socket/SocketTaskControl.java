package socket;

import interfacciaDB.ConnessioneDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import eccezioni.EccezioneClassificaVuota;
import slotMachine.Slot;
import model.Client;
import model.Utente;

public class SocketTaskControl implements Runnable{
	private Socket client;
	private PrintWriter writer = null;
	private BufferedReader reader = null;
	private ConnessioneDB db;
	private Utente u;
	public SocketTaskControl(Socket client){
		this.client = client;
		db = ConnessioneDB.getInstance();

	}
	@Override
	public void run(){
		Boolean continua = true;
		while(continua){
			try {
				reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				writer = new PrintWriter(client.getOutputStream(), true);
				while(!reader.ready()){
					Thread.sleep(500);
				}

				String controlloTask = reader.readLine();
				StringTokenizer st = new StringTokenizer(controlloTask,"#");
				String caseS = st.nextToken();
				switch(caseS){
				case "LOGIN":{
					String username = st.nextToken();
					String password = st.nextToken();
					login(username,password);
					break;
				}
				case "REGISTRA":{
					String username = st.nextToken();
					String password = st.nextToken();
					String passwordConf = st.nextToken();
					String nome = st.nextToken();
					String cognome = st.nextToken();
					registra(username,password,passwordConf,nome,cognome);
					break;
				}
				case "VINTO":{
					String combinazione = st.nextToken();
					vinto(combinazione);
					break;
				}
				case "MOSSA":{
					mossa();
					break;
				}
				case "ROLLA":{
					rolla();
					break;
				}
				case "AGGTAB":{
					break;
				}
				case "AGGCLASS":{
					break;
				}
				case "AGGTAV":{
					break;
				}
				}
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}

	public void login(String username,String password){
		int controllo = db.controlloUtente(username,password);

		switch(controllo){
		case 0:{
			Utente u = db.getUtente(username);
			writer.println("OK#"+client.getNome()+"#"+client.getCognome()+"#"+client.getCrediti()+"#"+client.GetUltimoLogin()+"#"+client.GetPosizionelassificaGlobale());
			break;
		}
		case 1:{
			writer.println("KO#nouser");
		}
		case 2:{
			writer.println("KO#passerr");
		}
		}

	}

	public void registra(String username,String password,String passwordConf,String nome,String cognome){
		if(db.getUtente(u.getUsername()) == null && password.equals(passwordConf)){
			Utente u = new Utente(nome,cognome,username,password,0);
			Boolean ok = db.addUtente(u);
			if(ok)
				writer.println("OK");
			else writer.println("OK#servererr");
		}
		else if(db.getUtente(username) != null)
			writer.println("KO#userdoppio");
		else writer.println("KO#passwerr");
	}

	public void rolla(){
		if(db.getUtente(u.getUsername()).getCrediti() < 1)
			writer.println("KO#NOCREDITI#"+db.getUtente(u.getUsername()).getCrediti());
		else {
			Slot s = new Slot();
			String comb = s.calcolaCombinazione();
			String premios = s.getStringaPremio();
			int premio = s.getPremio(true);
			db.aggiornaCrediti(premio,1,u.getUsername());
			writer.println("OK#"+comb+premios);
		}
	}

	public void mossa(){

	}

	public void vinto(String combinazione){



	}

	public void aggTav(){

	}

	public void aggTab(String username){

	}

	public void aggClass() throws EccezioneClassificaVuota{
		ArrayList<Utente> classifica = db.getClassifica(true);
		String s = "CLASSIFICA#"; 
		for(int i=0;i<classifica.size();i++)
			s = s + classifica.get(i).getUsername() + "#";
		writer.println(s);
	}

	public void aggClassGiorn() throws EccezioneClassificaVuota{
		ArrayList<Utente> classifica = db.getClassifica(true);
		String s = "CLASSIFICA#"; 
		for(int i=0;i<classifica.size();i++)
			s = s + classifica.get(i).getUsername() + "#";
		writer.println(s);
	}
}


