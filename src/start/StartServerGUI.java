package start;



// aggiornare tombola con aggiornamento crediti e calcolo premi

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import lobby.ThreadLobbyRubaMazzo;
import lobby.ThreadLobbyTombola;
import rmi.RmiServerImp;
import socket.SocketServer;

public class StartServerGUI {

	private static JFrame frame;
	private static SocketServer ssocket;
	private static RmiServerImp srmi;
	private static AggiornaCrediti ag;
	private static ThreadLobbyTombola lt;
	private static ThreadLobbyRubaMazzo lrm;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Thread t1,t2,t3,t4,t5;
					ssocket = new SocketServer();
					srmi = new RmiServerImp();
					ag = new AggiornaCrediti();
					lt = ThreadLobbyTombola.getIstance();
					lrm = ThreadLobbyRubaMazzo.getInstance();

					t1 = new Thread(ssocket);
					t2 = new Thread(srmi);
					t3 = new Thread(ag);
					t4 = new Thread(lt);
					t5 = new Thread(lrm);
					t5.start();
					t4.start();
					t3.start();
					t2.start();
					t1.start();
					StartServerGUI window = new StartServerGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StartServerGUI() {
		initialize();
	}
	
	
	public void chiusuraServer(){
		
		ssocket.chiudi();
		srmi.chiudi();
		ag.chiudi();
		lt.chiudi();
		lrm.chiudi();
		
		Date d = new Date();
		try {
			FileOutputStream file = new FileOutputStream("file.txt");
			PrintStream Output = new PrintStream(file);
			Output.println(d);
			

		} catch (IOException e1) {
			System.out.println("Errore: " + e1);
		}
		frame.dispose();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		this.addWindowListener(new WindowAdapter() {
			   public void windowClosing(WindowEvent evt) {
			    chiusuraServer();
			   }
			 });
		JButton stopServer = new JButton("Stop Server");
		stopServer.setBounds(88, 183, 270, 25);
		stopServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chiusuraServer();
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(stopServer);

		JLabel lblServerSalaGiochi = new JLabel("Server Sala Giochi");
		lblServerSalaGiochi.setVerticalAlignment(SwingConstants.BOTTOM);
		lblServerSalaGiochi.setBounds(142, 59, 153, 15);
		frame.getContentPane().add(lblServerSalaGiochi);
	}

	private void addWindowListener(WindowAdapter windowAdapter) {
		// TODO Auto-generated method stub
		
	}
}
