package start;
import java.util.*;

import rmi.RmiServer;
import socket.SocketServer;


public class serverStart {

	public static void main(String[] args){
		// thread socket acc
		// thread rmi 
		// thread update
		SocketServer taskSocket = new SocketServer();
		RmiServer taskRmi = new RmiServer();
		Thread tsock = new Thread(taskSocket);
		//Thread trmi = new Thread(taskRmi);
		tsock.start();
	}
}
