package start;

import java.net.ServerSocket;
import java.rmi.RemoteException;

import rmi.RmiServerImp;
import socket.SocketServer;

public class ThreadStart implements Runnable{
	
	@Override
	public void run() {
		SocketServer ssocket = new SocketServer();
		RmiServerImp srmi = null;
		try {
			srmi = new RmiServerImp();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AggiornaCrediti ag = new AggiornaCrediti();
		Thread t1,t2,t3;
		t1 = new Thread(ssocket);
		t2 = new Thread(srmi);
		t3 = new Thread(ag);
		t3.start();
		t2.start();
		t1.start();
	}

}
