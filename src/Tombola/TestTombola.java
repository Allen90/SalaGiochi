package Tombola;

import java.util.Scanner;

public class TestTombola {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		Tabellone t = new Tabellone();
		Tabella tab = new Tabella(t);
		t.stampa();
		s.nextLine();
		
		//---------test estrazione da tabellone ----------------
//		for(int i=0;i<4;i++){
//			System.out.println("estrazione "+(i+1));
//			System.out.println("estratto --------> " + t.estrai());
//			System.out.println("-----------------------------------------------");
//			t.stampa();
//			String line = s.nextLine();
//		}
		
		//--------------test caricamento tabella --------------------
//		for(int i=0; i<10; i++)
//			for(int j=0; j<3; j++){
//				System.out.println("estrazione "+(i+1));
//				System.out.println("estratto --------> " + t.estraiDaDecina(i));
//				System.out.println("-----------------------------------------------");
//				t.stampa();
//				String line = s.nextLine();
//			}

		tab.stampa();
	}
}
