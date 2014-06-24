package socket;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 *  thread che si mette in attesa di connessioni da parte del client, ad ogni client connesso viene generato un thread 
 *  che lo identificherà e gestirà le sue richieste
 * @author fritz
 *
 */

public class SocketServer implements Runnable{
	private static final int port = 4445;
	private boolean continua;
	private ArrayList<SocketTaskControl> arrayController;
	
	public void chiudi(){
		for(int i = 0; i< arrayController.size();i++)
			arrayController.get(i).chiudi();
		continua = false;
	}
	
	public void run() {
		arrayController = new ArrayList<SocketTaskControl>();
		continua = true;
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Non posso fare listen sulla porta: "+port);
			System.exit(1);
		}

		System.out.println("Mi metto in ascolto");
		try {
			while(continua){
				
				clientSocket = serverSocket.accept();
				System.out.println("nuova connessione client");
				System.out.println(""+clientSocket);
				System.out.println("creo thread per il client connesso");
				SocketTaskControl task = new SocketTaskControl(clientSocket);
				Thread t = new Thread(task);
				arrayController.add(task);
				t.start();
			}

		} catch (IOException e) {
			System.err.println("Accept fallita.");
			System.exit(1);
		}finally{
			if(!serverSocket.isClosed()){
				try { serverSocket.close();	} catch (IOException e) {e.printStackTrace();}
			}
		}
		System.out.println("Server terminato");

	}
}
