package start;

public class StartServer {
	public void main(){
		ThreadStart s = new ThreadStart();
		Thread t = new Thread(s);
		t.start();
	}
}