package start;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import lobby.ThreadLobbyRubaMazzo;
import lobby.ThreadLobbyTombola;
import rmi.RmiServerImp;
import socket.SocketServer;

public class StartServerGUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					Thread t1,t2,t3,t4,t5;
					SocketServer ssocket = new SocketServer();
					RmiServerImp srmi = new RmiServerImp();
					AggiornaCrediti ag = new AggiornaCrediti();
					ThreadLobbyTombola lt = ThreadLobbyTombola.getIstance();
					ThreadLobbyRubaMazzo lrm = ThreadLobbyRubaMazzo.getInstance();

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

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JButton stopServer = new JButton("Stop Server");
		stopServer.setBounds(88, 183, 270, 25);
		stopServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(stopServer);

		JLabel lblServerSalaGiochi = new JLabel("Server Sala Giochi");
		lblServerSalaGiochi.setVerticalAlignment(SwingConstants.BOTTOM);
		lblServerSalaGiochi.setBounds(142, 59, 153, 15);
		frame.getContentPane().add(lblServerSalaGiochi);
	}
}