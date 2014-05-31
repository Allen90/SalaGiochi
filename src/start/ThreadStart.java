package start;

import java.net.ServerSocket;

import rmi.RmiServerImp;

public class ThreadStart implements Runnable{
	
	@Override
	public void run() {
		ServerSocket ssocket = new ServerSocket();
		RmiServerImp srmi = new RmiServerImp();
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
