package start;

import java.rmi.RemoteException;

import rmi.RmiServerImp;
import socket.SocketServer;


public class serverStart {

	public static void main(String[] args) throws RemoteException{
		// thread socket acc
		// thread rmi 
		// thread update
		SocketServer taskSocket = new SocketServer();
		RmiServerImp taskRmi = new RmiServerImp();
		Thread tsock = new Thread(taskSocket);
		//Thread trmi = new Thread(taskRmi);
		tsock.start();
	}
}
