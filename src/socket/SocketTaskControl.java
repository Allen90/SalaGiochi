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
import eccezioni.EccezioneUtente;
import encodec.Encoder;
import rubamazzo.Mossa;
import rubamazzo.SituazioneRubamazzo;
import slot.Rollata;
import taskController.TaskController;
import tombola.SituazioneTombola;
import userModel.Utente;

//controllare riferimento al passaggio di utente!!!


public class SocketTaskControl implements Runnable{
	private Socket client;
	private PrintWriter writer = null;
	private BufferedReader reader = null;
	private Utente utente;
	private boolean continua;
	private TaskController tc;
	private ConnessioneDB db;

	public SocketTaskControl(Socket client){
		this.client = client;
		db = ConnessioneDB.getInstance();
		tc = new TaskController();
		continua = true;
	}
	@Override
	public void run(){
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
				case "VINTOTOMBOLA":{
					int numPartita = Integer.parseInt(st.nextToken());
					int tipoVittoria = Integer.parseInt(st.nextToken());
					int indiceCartella = Integer.parseInt(st.nextToken());
					int indiceRiga = Integer.parseInt(st.nextToken());
					vintoTombola(numPartita,tipoVittoria,indiceCartella,indiceRiga);
					break;
				}
				case "MOSSARUBAMAZZO":{
					Mossa m = null;
					int numPartita = 0;
					//decode della mossa
					mossaRubamazzo(m,numPartita);
					break;
				}
				case "ROLLA":{
					rolla();
					break;
				}
				case "AGGTOMBOLA":{
					break;
				}
				case "AGGCLASS":{
					break;
				}
				case "AGGRUBAMAZZO":{
					break;
				}
				case "TERMINA":{
					termina();
					break;
				}
				case "GIOCOTOMBOLA":{
					int numCartelle = Integer.parseInt(reader.readLine());
					giocoTombola(numCartelle);
				}
				case "GIOCORUBAMAZZO":{
					giocoRubaMazzo();
				}
				}
			} catch (IOException | InterruptedException | EccezioneUtente | EccezioneClassificaVuota e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}
	
	public void giocoRubaMazzo() throws EccezioneUtente{
		boolean valido;
		valido = tc.giocoRubamazzo(utente);
		String s = Encoder.serverGiocoRubamazzo(valido,utente.getCrediti());
		writer.print(s);
	}
	
	
	public void giocoTombola(int numCartelle) throws EccezioneUtente{
		boolean valido;
		valido = tc.giocoTombola(utente, numCartelle);
		String s = Encoder.serverGiocoTombola(valido,utente.getCrediti());
		writer.print(s);
	}
	
	public void termina(){
		continua = tc.termina();
	}

	public void login(String username,String password) throws EccezioneClassificaVuota{
		
		boolean valido = db.controlloUtente(username,password);
		int posizione = 0;
		
		if(valido){
			
			try {
				utente = db.getUtente(username);
			} catch (EccezioneUtente e) {
				e.printStackTrace();
			}
			
			ArrayList<Utente> classifica = db.getClassifica(false);
			for(int i=0; i<classifica.size();i++)
				if(classifica.get(i).getUsername().equals(username))
					posizione = i;
		}
		else{
			utente = null;
		}
		writer.print(Encoder.serverLogin(utente, posizione, valido));

	}

	public void registra(String username,String password,String passwordConf,String nome,String cognome) throws EccezioneClassificaVuota{
		boolean valido;
		int posizione = 0;
		
		try {
			db.getUtente(username);
			valido = false;
		} catch (EccezioneUtente e) {
			if(password.equals(passwordConf)){
				
				try {
					utente = new Utente(nome,cognome,username,password,0);
				}catch(EccezioneUtente e1) {
					e.printStackTrace();
				}
				
				db.addUtente(utente);
				ArrayList<Utente> classifica = db.getClassifica(false);
				for(int i=0; i<classifica.size();i++)
					if(classifica.get(i).getUsername().equals(username))
						posizione = i;
				
				valido = true;
			}else{
				valido = false;
			}
		}
		writer.print(Encoder.serverRegistra(utente, posizione, valido));
	}

	public void rolla() throws EccezioneUtente{
		Rollata r = tc.rolla(utente);
		String s = Encoder.serverRolla(r);
		writer.print(s);
	}

	public void mossaRubamazzo(Mossa m, int numPartita){
		boolean ok;
		ok = tc.mossaRubaMazzo(utente, m, numPartita);
		writer.print(Encoder.serverMossaRubamazzo(ok));
	}

	public void vintoTombola(int numPartita,int tipoVittoria,int indiceCartella, int indiceRiga){
		tc.vintoTombola(utente, numPartita, tipoVittoria, indiceCartella, indiceRiga);
	}

	public void aggTombola(){
		SituazioneTombola st = tc.aggTombola(utente);
		String s = Encoder.serverAggiornaTombola(st);
		writer.print(s);
	}

	public void aggRubamazzo(){
		SituazioneRubamazzo st = tc.aggRubamazzo(utente);
		String s = Encoder.serverAggiornaRubamazzo(st);
		writer.print(s);
	}

	public void aggClass(boolean giorn) throws EccezioneClassificaVuota{
		ArrayList<Utente> classifica = tc.aggClass(utente, giorn);
		String s = Encoder.serverClassifica(classifica, giorn);
		writer.println(s);
	}

}


