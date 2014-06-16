package socket;

import interfacciaDB.ConnessioneDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import eccezioni.EccezioneClassificaVuota;
import eccezioni.EccezioneUtente;
import encodec.Decoder;
import encodec.Encoder;
import rubamazzo.Mossa;
import rubamazzo.MossaSocket;
import rubamazzo.SituazioneRubamazzo;
import slot.Rollata;
import taskController.TaskController;
import tombola.SituazioneTombola;
import tombola.Vincita;
import userModel.EntryClassifica;
import userModel.Login;
import userModel.Registrazione;
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
	private String stringaClient;

	public SocketTaskControl(Socket client) throws IOException{
		this.client = client;
		db = ConnessioneDB.getInstance();
		tc = new TaskController();
		continua = true;
		reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		writer = new PrintWriter(client.getOutputStream(), true);
	}
	@Override
	public void run(){
		System.out.println("creato thread per client:" + client);
		while(continua){
			try {

				while(!reader.ready()){
					Thread.sleep(500);
				}

				System.out.println("sto per leggere stringa client");

				stringaClient = reader.readLine();
				String azione = Decoder.getTipoAzione(stringaClient);

				switch(azione){
				case "LOGIN":{
					Login l = Decoder.serverLogin(stringaClient);
					login(l.getUsername(),l.getPassword());
					break;
				}
				case "REGISTRA":{
					Registrazione r = Decoder.serverRegistra(stringaClient);
					registra(r.getUsername(),r.getPassword(),r.getConfPass(),r.getNome(),r.getCognome());
					break;
				}
				case "VINTOTOMBOLA":{
					Vincita v = Decoder.serverVincitaTombola(stringaClient);
					vintoTombola(v.getNumPartita(),v.getTipoVincita(),v.getIndiceCartella(),v.getIndiceRiga());
					break;
				}
				case "MOSSARUBAMAZZO":{
					MossaSocket m = Decoder.serverMossarubamazzo(stringaClient);
					mossaRubamazzo(m.getMossa(),m.getNumPartita());
					break;
				}
				case "ROLLA":{
					rolla();
					break;
				}
				case "AGGTOMBOLA":{
					aggTombola();
					break;
				}
				case "AGGCLASS":{
					aggClass(false);
					aggClass(true);
					break;
				}
				case "AGGRUBAMAZZO":{
					aggRubamazzo();
					break;
				}
				case "TERMINA":{
					termina();
					break;
				}
				case "GIOCOTOMBOLA":{
					int numCartelle = Decoder.serverGiocoTombola(stringaClient);
					giocoTombola(numCartelle);
				}
				case "GIOCORUBAMAZZO":{
					giocoRubaMazzo();
				}
				}
			} catch (IOException | EccezioneUtente | EccezioneClassificaVuota e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}

	public void giocoRubaMazzo() throws EccezioneUtente{
		boolean valido;
		valido = tc.giocoRubamazzo(utente);
		String s = Encoder.serverGiocoRubamazzo(valido,utente.getCrediti());
		writer.println(s);
	}


	public void giocoTombola(int numCartelle) throws EccezioneUtente{
		boolean valido;
		valido = tc.giocoTombola(utente, numCartelle);
		String s = Encoder.serverGiocoTombola(valido,utente.getCrediti());
		writer.println(s);
	}

	public void termina(){
		continua = tc.termina();
	}

	public void login(String username,String password) throws EccezioneClassificaVuota{
		System.out.println("sto controllando utente");
		boolean valido = db.controlloUtente(username,password);
		int posizione = 0;

		if(valido){
			db.aggiornaUltimoLogin(username);
			try {
				utente = db.getUtente(username);
				System.out.println("Data letta da db"+ db.getUltimoLogin(username));
				System.out.println(utente.getUltimaVisita());
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
		System.out.println(valido);
		writer.println(Encoder.serverLogin(utente, posizione, valido));
	}

	public void registra(String username,String password,String passwordConf,String nome,String cognome) throws EccezioneClassificaVuota, EccezioneUtente{
		boolean valido;
		int posizione = 0;

		try {
			db.getUtente(username);
			valido = false;
		} catch (EccezioneUtente e) {
			if(password.equals(passwordConf)){
				
				Date ultimaVisita = new Date();
				System.out.println(ultimaVisita);
				try {
					utente = new Utente(nome,cognome,username,password,0,ultimaVisita);
				}catch(EccezioneUtente e1) {
					e.printStackTrace();
				}
				System.out.println("Data letta da db"+ db.getUltimoLogin(username));
				db.addUtente(utente);
				System.out.println("qui dopo aggiunta");
				Utente test = db.getUtente(username);
				System.out.println(test.getUltimaVisita());
				ArrayList<Utente> classifica = db.getClassifica(false);
				for(int i=0; i<classifica.size();i++)
					if(classifica.get(i).getUsername().equals(username))
						posizione = i;

				valido = true;
			}else{
				valido = false;
			}
		}
		System.out.println(Encoder.serverRegistra(utente, posizione, valido));
		writer.println(Encoder.serverRegistra(utente, posizione, valido));
	}

	public void rolla() throws EccezioneUtente{
		Rollata r = tc.rolla(utente);
		String s = Encoder.serverRolla(r);
		writer.println(s);
	}

	public void mossaRubamazzo(Mossa m, int numPartita){
		boolean ok;
		ok = tc.mossaRubaMazzo(utente, m, numPartita);
		writer.println(Encoder.serverMossaRubamazzo(ok));
	}

	public void vintoTombola(int numPartita,int tipoVittoria,int indiceCartella, int indiceRiga){
		boolean valido = tc.vintoTombola(utente, numPartita, tipoVittoria, indiceCartella, indiceRiga);
		String s = Encoder.serverResponseVintoTombola(valido);
		writer.println(s);
	}

	public void aggTombola(){
		SituazioneTombola st = tc.aggTombola(utente);
		String s = Encoder.serverAggiornaTombola(st);
		writer.println(s);
	}

	public void aggRubamazzo(){
		SituazioneRubamazzo st = tc.aggRubamazzo(utente);
		String s = Encoder.serverAggiornaRubamazzo(st);
		writer.println(s);
	}

	public void aggClass(boolean giorn) throws EccezioneClassificaVuota{
		ArrayList<EntryClassifica> classifica = tc.aggClass(giorn);
		String s = Encoder.serverClassifica(classifica);
		System.out.println("qui in server prima dell'invio");
		System.out.println(s);
		writer.println(s);
	}



}


