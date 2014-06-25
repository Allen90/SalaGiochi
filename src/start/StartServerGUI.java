package start;


/**
 * classe che inizializza e fa partire tutti i thread per la gestione delle connessioni e dell'aggiornamento crediti
 */

import interfacciaDB.ConnessioneDB;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;

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
	private static PrintWriter writer;
	private static BufferedReader reader;
	private static ConnessioneDB db;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					db = ConnessioneDB.getInstance();
					File file = new File("src/chiusuraServer.txt");
					FileInputStream fileInStr = new FileInputStream(file); 
					DataInputStream dataIn = new DataInputStream(fileInStr); 
					long ore = dataIn.readLong();
					Date d = new Date();
					Long oreShutdown = d.getTime()/3600000 -ore/ 3600000;
					System.out.println(oreShutdown);
					db.aggiornaPeriodico(oreShutdown.intValue());
					System.out.println(ore);
					dataIn.close(); 
					fileInStr.close();
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


	public void chiusuraServer() throws IOException{

		ssocket.chiudi();
		srmi.chiudi();
		ag.chiudi();
		lt.chiudi();
		lrm.chiudi();

		Date d = new Date();
		System.out.println(""+d.getTime());
		File file = new File("src/chiusuraServer.txt");
		FileOutputStream fileOutStr = new FileOutputStream(file); 
		DataOutputStream dataOut = new DataOutputStream(fileOutStr);
		dataOut.writeLong(d.getTime());
		dataOut.close(); 
		fileOutStr.close(); 
		System.exit(0);
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
				try {
					chiusuraServer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		JButton stopServer = new JButton("Stop Server");
		stopServer.setBounds(88, 183, 270, 25);
		stopServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					chiusuraServer();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
