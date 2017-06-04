package Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;

import Analyse.CosineTest;
import Analyse.DistanceTest;
import Analyse.Entree;
import Analyse.KeyStrokeSet;
import Analyse.NormalizedGaussTest;
import Analyse.SimpleGaussTest;
import Database.ConnectionBD;
import GUI.MenuGUI;
import KeystrokeMeasuring.KeyStroke;
import Session.SessionManager;

public class Main {
	public static SessionManager sessionManager;
	public static SystemAccount currentSystemAccount;
	public static Connection conn;
	public static LinkedList<KeyStrokeSet> sets;
	public static String[] noms = { "Youssef", "Idriss", "Safia", "Margot", "Kaloyan", "Jules" };
	public static ArrayList<LinkedList<KeyStrokeSet>> setList = new ArrayList<LinkedList<KeyStrokeSet>>(6);
	public static ArrayList<KeyStrokeSet> allEntries = new ArrayList<KeyStrokeSet>(300);

	public static void main(String[] args) throws InterruptedException {
		conn = ConnectionBD.connect();
		try {
			sessionManager = new SessionManager();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sessionManager.getCurrentSession().setAccount(new Account("test-1", "test", "PASS+ord"));
		// MenuGUI mg = new MenuGUI();

		System.out.println("création des csv de référence");
		for (int i = 0; i < noms.length; i++) {
			try {
				FileWriter fw = new FileWriter(new File(noms[i] + "-Reference.csv"));
				currentSystemAccount = new SystemAccount(noms[i]);
				try {
					sets = new LinkedList<KeyStrokeSet>(
							KeyStrokeSet.buildReferenceSet(new Account("test-1", "test", "PASS+ord")));
					Iterator itr = sets.iterator();
					while (itr.hasNext()) {
						KeyStrokeSet ks = (KeyStrokeSet) itr.next();
						allEntries.add((KeyStrokeSet) ks.clone());
					}
				} catch (EncryptionOperationNotPossibleException e) {

					setList.add(new LinkedList());
					continue;
				}
				setList.add(sets);
				if (sets.size() > 0) {
					Iterator<KeyStrokeSet> itr = sets.iterator();
					KeyStrokeSet cur = itr.next();
					int t = 1;
					while (itr.hasNext()) {

						for (int n = 0; n < cur.getSet().size(); n++) {
							KeyStroke s = cur.getSet().get(n);
							double[] values = s.getValues();
							fw.write(noms[i] + "," + t + "," + n + ",");
							for (int v = 0; v < values.length; v++) {
								fw.write(values[v] + ",");
							}
							fw.write("\n");
							System.out.print("#");

						}
						t++;
						System.out.print("\n");

						cur = itr.next();
					}
				}
				fw.flush();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		int testEntry = (int) (Math.random() * allEntries.size());
		FileWriter testWriter = null;
		FileWriter entriesWriter = null;
		try {
			testWriter = new FileWriter(new File("tests.csv"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int n = 0; n < setList.size(); n++) {

			if (setList.get(n) != null && setList.get(n).size()>0) {
				try {
					testWriter.write(allEntries.get(testEntry).systemAccount + "," + noms[n] + ",");
					testWriter.write(String.valueOf(DistanceTest.test(allEntries.get(testEntry), n, testWriter)) + ",");
					testWriter.write(String.valueOf(CosineTest.test(allEntries.get(testEntry), n, testWriter)) + ",");
					testWriter.write(String.valueOf(SimpleGaussTest.test(allEntries.get(testEntry), n) + ","));
					testWriter.write(
							String.valueOf(NormalizedGaussTest.test(allEntries.get(testEntry), n, testWriter)) + ",");
					testWriter.write("\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		try {
			testWriter.flush();
			testWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mg.initBdGui(new Account ("test-1","test","PASS+ord"), 0);
		// GUI initGui = new GUI(); //initialisation de l'interface
		// @SuppressWarnings("unused")
		// SyncUtil sync =new SyncUtil();
		// sync.start();
	}

	// permet de tester si deux mots de passe correspondent
	public static boolean passwordMatch(char[] p1, char[] p2) {
		if (p1.length == p2.length) { // on test d'abbord les longueurs pour
										// gagner du temps
			// on test ensuite chaque caracere separement
			for (int i = 0; i < p1.length; i++) {
				if (p1[i] != p2[i]) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

}
