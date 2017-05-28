package Main;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.Iterator;
import java.util.LinkedList;

import Analyse.KeyStrokeSet;
import Database.ConnectionBD;
import GUI.MenuGUI;
import KeystrokeMeasuring.KeyStroke;
import Session.SessionManager;


public class Main {
	public static SessionManager sessionManager;
	public static SystemAccount currentSystemAccount;
	public static Connection conn;
	public static LinkedList <KeyStrokeSet>sets;
	public static String[] noms = {"Youssef","Idriss","Safia","Margot","Kaloyan","Jules"};

	
	public static void main(String[] args) throws InterruptedException{
		conn = ConnectionBD.connect();
		try {
			sessionManager =new SessionManager();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sessionManager.getCurrentSession().setAccount(new Account("test-1","test","password"));
		MenuGUI mg = new MenuGUI(); 


		System.out.println("création des csv de référence");
		for(int i =0; i<noms.length;i++){
			try {
				FileWriter fw = new FileWriter(new File(noms[i]+"-Reference.csv"));
				currentSystemAccount= new SystemAccount(noms[i]);
				sets = new LinkedList<KeyStrokeSet>(KeyStrokeSet.buildReferenceSet(new Account ("test-1","test","password")));
				if(sets.size()>0){
					Iterator <KeyStrokeSet>itr = sets.iterator();
					KeyStrokeSet cur = itr.next();
					int t=1;
					while(itr.hasNext()){
						
						for(int n=0;n<cur.getSet().size();n++){
							KeyStroke s = cur.getSet().get(n);
							double[] values = s.getValues();
							fw.write(noms[i]+","+t+","+n+",");
							for(int v=0; v<values.length;v++){
								fw.write(values[v]+",");
							}
							fw.write("\n");
							System.out.print("#");
							
						}
						t++;
						System.out.print("\n");
					
						itr.next();
					}
				}
				fw.flush();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mg.initBdGui(new Account ("test-1","test","password"), 0);
	//	GUI initGui = new GUI(); //initialisation de l'interface
		//@SuppressWarnings("unused")
		//SyncUtil sync =new SyncUtil();
		//sync.start();
	}
	//permet de tester si deux mots de passe correspondent
	public static boolean passwordMatch(char[] p1, char[] p2){
		if (p1.length == p2.length){ //on test d'abbord les longueurs pour gagner du temps
			//on test ensuite chaque caracere separement
			for (int i=0; i<p1.length;i++){
				if (p1[i]!=p2[i]){
					return false;
				}
			}
			return true;
		}else{
			return false;
		}
	}
	

	

}
