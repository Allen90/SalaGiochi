package socket;

import java.io.*;
import java.net.*;

public class SocketServer implements Runnable{
	private static final int port = 4445;

	@Override
	public void run() {
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
			while(true){
				clientSocket = serverSocket.accept();
				SocketTaskControl task = new SocketTaskControl(clientSocket);
				Thread t = new Thread(task);
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
