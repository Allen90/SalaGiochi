package socket;

import interfacciaDB.ConnessioneDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import codePartite.LobbyRubaMazzo;
import codePartite.LobbyTombola;
import eccezioni.EccezioneClassificaVuota;
import eccezioni.EccezioneUtente;
import slotMachine.Slot;
import tombola.GiocatoreTombola;
import tombola.Tabella;
import model.Client;
import model.Utente;

public class SocketTaskControl implements Runnable{
	private Socket client;
	private PrintWriter writer = null;
	private BufferedReader reader = null;
	private ConnessioneDB db;
	private Utente u;
	private boolean continua;
	private GiocatoreTombola gt;
	private LobbyTombola lt;
	private LobbyRubaMazzo lrm;
	public SocketTaskControl(Socket client){
		this.client = client;
		db = ConnessioneDB.getInstance();
		lt = LobbyTombola.getIstance();
		lrm = LobbyRubaMazzo.getInstance();
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
				case "TERMINA":{
					termina();
					break;
				}
				case "GIOCOTOMBOLA":{
					int numCartella = Integer.parseInt(reader.readLine());
					giocoTombola(numCartella);
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
	
	public void giocoRubaMazzo(){
		lrm.addUserLobbyRubaMazzo(u);
	}
	
	public void giocoTombola(int numCartelle){
		ArrayList<Tabella> cartelle = new ArrayList<Tabella>();
		for(int i = 0; i< numCartelle;i++){
			Tabella c = new Tabella();
			cartelle.add(c);
		}
			
		gt = new GiocatoreTombola(cartelle,u);
		lt.addUserLobbyTomb(gt);
	}
	
	public void termina(){
		continua = false;
	}

	public void login(String username,String password) throws EccezioneClassificaVuota, EccezioneUtente{
		int controllo = db.controlloUtente(username,password);

		switch(controllo){
		case 0:{
			
			Utente u = db.getUtente(username);
			int posizione = 0;
			ArrayList<Utente> classifica = db.getClassifica(false);
			for(int i=0; i<classifica.size();i++)
				if(classifica.get(i).getUsername().equals(username))
					posizione = i;
			writer.println("OK#"+u.getNome()+"#"+u.getCognome()+"#"+u.getCrediti()+"#"+u.getUltimaVisita()+"#"+posizione);
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
		try {
			if(db.getUtente(u.getUsername()) == null && password.equals(passwordConf)){
				Utente u = new Utente(nome,cognome,username,password,0);
				Boolean ok = db.addUtente(u);
				if(ok)
					writer.println("OK");
				else writer.println("OK#servererr");
			}
		} catch (EccezioneUtente e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void rolla() throws EccezioneUtente{
		if(db.getUtente(u.getUsername()).getCrediti() < 1)
			writer.println("KO#NOCREDITI#"+db.getUtente(u.getUsername()).getCrediti());
		else {
			Slot s = new Slot();
			String comb = s.calcolaCombinazioneToString();
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


