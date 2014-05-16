package socket;
import java.io.*;
import java.net.*;
public class SocketAccept implements Runnable{
	private Socket clientSocket;
	public SocketAccept(Socket clientSocket) {
		super();
		this.clientSocket = clientSocket;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
