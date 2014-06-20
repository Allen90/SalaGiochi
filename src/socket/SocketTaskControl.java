package socket;

import interfacciaDB.ConnessioneDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
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
import start.UtentiLoggati;
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
	private UtentiLoggati l;

	public SocketTaskControl(Socket client) throws IOException{
		this.client = client;
		db = ConnessioneDB.getInstance();
		tc = new TaskController();
		continua = true;
		reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		writer = new PrintWriter(client.getOutputStream(), true);
		l = UtentiLoggati.getIstance();
	}
	@Override
	public void run(){
		while(continua){
			try {

				while(!reader.ready()){
					Thread.sleep(500);
				}

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
					try {
						registra(r.getUsername(),r.getPassword(),r.getConfPass(),r.getNome(),r.getCognome());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				case "VINTOTOMBOLA":{
					System.out.println("ricevuto dal client richiesta di vincita tombola");
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
				case "LOGOUT":{
					termina();
					break;
				}
				case "GIOCOTOMBOLA":{
					int numCartelle = Decoder.serverGiocoTombola(stringaClient);
					giocoTombola(numCartelle);
					break;
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
	
	public void chiudi(){
		continua = false;
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
		continua = tc.termina(utente.getUsername());
	}

	public void login(String username,String password) throws EccezioneClassificaVuota{
		boolean valido = db.controlloUtente(username,password);
		int posizione = 0;
		boolean ok = false;
		
		for(int i=0;i< l.getLoggati().size();i++){
			if(l.getLoggati().get(i).equals(username)){
				ok = false;
			}
		}
		
		if(valido && ok){
			db.aggiornaUltimoLogin(username);
			l.addLoggato(utente.getUsername());
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
		writer.println(Encoder.serverLogin(utente, posizione, valido));
	}

	public void registra(String username,String password,String passwordConf,String nome,String cognome) throws EccezioneClassificaVuota, EccezioneUtente, ParseException{
		boolean valido;
		int posizione = 0;

		try {
			db.getUtente(username);
			valido = false;
		} catch (EccezioneUtente e) {
			if(password.equals(passwordConf)){
				
				Date ultimaVisita = new Date();
				try {
					utente = new Utente(nome,cognome,username,password,0,ultimaVisita);
				}catch(EccezioneUtente e1) {
					e.printStackTrace();
				}
				db.addUtente(utente);
				utente = db.getUtente(username);
				l.addLoggato(utente.getUsername());
				Utente test = db.getUtente(username);
				ArrayList<Utente> classifica = db.getClassifica(false);
				for(int i=0; i<classifica.size();i++)
					if(classifica.get(i).getUsername().equals(username))
						posizione = i;

				valido = true;
			}else{
				valido = false;
			}
		}
		
		writer.println(Encoder.serverRegistra(utente, posizione, valido));
	}

	public void rolla() throws EccezioneUtente{
		Rollata r = tc.rolla(utente.getUsername());
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
		writer.println(s);
	}



}


