package Analyse;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import KeystrokeMeasuring.KeyStroke;
import Main.Account;

public class CosineTest {
	
	private static final double cosineSimilarityThreshold = 0.9;

	public static boolean test(KeyStrokeSet testSet, Account account,FileWriter fw){
		
		LinkedList<KeyStrokeSet> sets = new LinkedList<KeyStrokeSet>(Main.Main.sets);
		LinkedList<LinkedList<Double>> similarityMatrix = new LinkedList<LinkedList<Double>>();
		Iterator<KeyStrokeSet> setsIterator = sets.iterator();
		LinkedList<Double> averageSimilarity = new LinkedList<Double>();
		
		//On definit la matrice de similarite avec les similarites cosinus de chaque touche de chaque entree, on calcule aussi les similarites moyennes pour chaque entree
		while(setsIterator.hasNext()){
			LinkedList<KeyStroke> temp = new LinkedList<KeyStroke>(setsIterator.next().getSet());
			
			if(temp.size() == testSet.getSet().size()){
				
				Iterator<KeyStroke> tempIterator = temp.iterator();
				Iterator<KeyStroke> testIterator = testSet.getSet().iterator();
				LinkedList<Double> tempSimilarities = new LinkedList<Double>();
				
				double somme = 0.0;
			
				while(tempIterator.hasNext()){

					KeyStroke test = testIterator.next();
					KeyStroke ref = tempIterator.next();
					double tempCosineSimilarity = test.getCosineSimilarity(ref); 
					tempSimilarities.add(tempCosineSimilarity);
					if(!(Double.isNaN(tempCosineSimilarity)))
					somme += tempCosineSimilarity;
				
				}
				
				similarityMatrix.add(tempSimilarities);
				averageSimilarity.add(somme / temp.size());

			
			}
		
		}
		
		boolean isTheSamePerson = true;
		
		Iterator<Double> averageSimilarityIterator = averageSimilarity.iterator();
		
		while(averageSimilarityIterator.hasNext()){

			Double cur = averageSimilarityIterator.next();
			try {
				fw.write(String.valueOf(cur.doubleValue())+",");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(cur < cosineSimilarityThreshold)
				isTheSamePerson = false;
						
		}
		
		//TODO traitement des autres donnees statistiques possibles (mediane, ecart-type...)
		
		return isTheSamePerson;
				
	}
}