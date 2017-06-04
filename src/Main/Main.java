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

		FileWriter testWriter = null;
		FileWriter entriesWriter = null;
		double DistanceTruePositive = 0;
		double DistanceTrueNegative = 0;
		double DistanceFalsePositive = 0;
		double DistanceFalseNegative = 0;
		
		double CosTruePositive = 0;
		double CosTrueNegative = 0;
		double CosFalsePositive = 0;
		double CosFalseNegative = 0;
		
		double SGTruePositive = 0;
		double SGTrueNegative = 0;
		double SGFalsePositive = 0;
		double SGFalseNegative = 0;
		
		double NGTruePositive = 0;
		double NGTrueNegative = 0;
		double NGFalsePositive = 0;
		double NGFalseNegative = 0;
		boolean testResult;
		try {
			testWriter = new FileWriter(new File("tests.csv"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int nbTries = 15;
		for (int k = 1; k <= nbTries; k++) {
			try {
				testWriter.write("essai num " + k + "/" + nbTries + ",\n");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (int i = 0; i < noms.length; i++) {

				if (setList.get(i) != null && setList.get(i).size() > 0) {

					int testEntry = (int) (Math.random() * setList.get(i).size());
					for (int n = 0; n < setList.size(); n++) {

						if (setList.get(n) != null && setList.get(n).size() > 0) {
							try {
								testWriter.write(noms[i] + "," + noms[n] + ",");
								// test des distances
								testResult = DistanceTest.test(setList.get(i).get(testEntry), n, testWriter);
								if(noms[i].equals(noms[n])){
									if(testResult){
										DistanceTruePositive++;
									}else{
										DistanceFalseNegative++;
									}
								}else{
									if(testResult){
										DistanceFalsePositive++;
									}else{
										DistanceTrueNegative++;
									}
								}
								testWriter.write(String.valueOf(testResult) + ",");
								// test cosinus
								testResult = CosineTest.test(setList.get(i).get(testEntry), n, testWriter);
								if(noms[i].equals(noms[n])){
									if(testResult){
										CosTruePositive++;
									}else{
										CosFalseNegative++;
									}
								}else{
									if(testResult){
										CosFalsePositive++;
									}else{
										CosTrueNegative++;
									}
								}
								testWriter.write(String.valueOf(testResult) + ",");
								// gauss simple
								testResult = SimpleGaussTest.test(setList.get(i).get(testEntry), n);
								if(noms[i].equals(noms[n])){
									if(testResult){
										SGTruePositive++;
									}else{
										SGFalseNegative++;
									}
								}else{
									if(testResult){
										SGFalsePositive++;
									}else{
										SGTrueNegative++;
									}
								}
								testWriter.write(String.valueOf(testResult) + ",");
								// gauss norlalisé
								testResult = NormalizedGaussTest.test(setList.get(i).get(testEntry), n, testWriter);
								if(noms[i].equals(noms[n])){
									if(testResult){
										NGTruePositive++;
									}else{
										NGFalseNegative++;
									}
								}else{
									if(testResult){
										NGFalsePositive++;
									}else{
										NGTrueNegative++;
									}
								}
								testWriter.write(String.valueOf(testResult) + ",");
								testWriter.write("\n");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
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
		double DtruePositiveRate = DistanceTruePositive/(DistanceTruePositive+DistanceFalsePositive);
		double DfalsePositiveRate = DistanceFalsePositive/(DistanceTruePositive+DistanceFalsePositive);
		double DtrueNegativeRate = DistanceTrueNegative/(DistanceTrueNegative+DistanceFalseNegative);
		double DfalseNegativeRate = DistanceFalseNegative/(DistanceTrueNegative+DistanceFalseNegative);
		
		double CtruePositiveRate = CosTruePositive/(CosTruePositive+CosFalsePositive);
		double CfalsePositiveRate = CosFalsePositive/(CosFalsePositive+CosTruePositive);
		double CtrueNegativeRate = CosTrueNegative/(CosTrueNegative+CosFalseNegative);
		double CfalseNegativeRate = CosFalseNegative/(CosTrueNegative+CosFalseNegative);
		
		double SGtruePositiveRate = SGTruePositive/(SGTruePositive+SGFalsePositive);
		double SGfalsePositiveRate = SGFalsePositive/(SGTruePositive+SGFalsePositive);
		double SGtrueNegativeRate = SGTrueNegative/(SGTrueNegative+SGFalseNegative);
		double SGfalseNegativeRate = SGFalseNegative/(SGTrueNegative+SGFalseNegative);
		
		double NGtruePositiveRate = NGTruePositive/(NGTruePositive+NGFalsePositive);
		double NGfalsePositiveRate = NGFalsePositive/(NGTruePositive+NGFalsePositive);
		double NGtrueNegativeRate = NGTrueNegative/(NGTrueNegative+NGFalseNegative);
		double NGfalseNegativeRate = NGFalseNegative/(NGTrueNegative+NGFalseNegative);
		
		try {
			FileWriter stats = new FileWriter (new File ("stats.csv"));
			stats.write("Distance,\n");
			stats.write("STATS,Positif,Négatif,\n");
			stats.write("Vrai,"+DtruePositiveRate*100+"%,"+DtrueNegativeRate*100+"%,\n");
			stats.write("Faux,"+DfalsePositiveRate*100+"%,"+DfalseNegativeRate*100+"%,\n,\n");
			
			stats.write("Cosinus,\n");
			stats.write("STATS,Positif,Négatif,\n");
			stats.write("Vrai,"+CtruePositiveRate*100+"%,"+CtrueNegativeRate*100+"%,\n");
			stats.write("Faux,"+CfalsePositiveRate*100+"%,"+CfalseNegativeRate*100+"%,\n,\n");
			
			stats.write("Gauss Simple,\n");
			stats.write("STATS,Positif,Négatif,\n");
			stats.write("Vrai,"+SGtruePositiveRate*100+"%,"+SGtrueNegativeRate*100+"%,\n");
			stats.write("Faux,"+SGfalsePositiveRate*100+"%,"+SGfalseNegativeRate*100+"%,\n,\n");
			
			stats.write("Gauss Normalisé,\n");
			stats.write("STATS,Positif,Négatif,\n");
			stats.write("Vrai,"+NGtruePositiveRate*100+"%,"+NGtrueNegativeRate*100+"%,\n");
			stats.write("Faux,"+NGfalsePositiveRate*100+"%,"+NGfalseNegativeRate*100+"%,\n,\n");
			stats.flush();
			stats.close();

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
