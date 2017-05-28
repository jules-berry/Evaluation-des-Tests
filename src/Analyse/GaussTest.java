package Analyse;

import java.util.Iterator;
import java.util.LinkedList;

import KeystrokeMeasuring.KeyStroke;
import Main.Account;

public class GaussTest {
	
	private static final double gaussianCoef = 3; //si 1 niveau confiance de 67%, si 2 niveau de confiance 95%, si 3 niveau de confiance 99% 
	private static final int nbParams = 15;
	
	public static boolean test(KeyStrokeSet testSet, Account account){
		
		boolean isTheSamePerson = true;
		
		LinkedList<KeyStrokeSet> sets = new LinkedList(Main.Main.sets);
		
		double[][] avgMatrix = getAvgMatrix(sets);
		double[][] sdMatrix = getStandardDeviationMatrix(sets,avgMatrix);
		
		Iterator<KeyStroke> keyIter = testSet.getSet().iterator();
		int keyIndex = 0;
		
		while(isTheSamePerson && keyIter.hasNext()){
			
			double[] values = keyIter.next().getValues();
			int i=0;
			
			while(i<nbParams && isTheSamePerson){
			
				double min = avgMatrix[keyIndex][i] - gaussianCoef*sdMatrix[keyIndex][i];
				double max = avgMatrix[keyIndex][i] + gaussianCoef*sdMatrix[keyIndex][i];
				System.out.println(min+"|"+max+"|"+values[i]);
				if(values[i]<min || values[i]>max)
					isTheSamePerson = false;
				i++;
					
			}
			
			keyIndex++;
			
		}
				
		return isTheSamePerson;
		
	}
	
	private static double[][] getAvgMatrix(LinkedList<KeyStrokeSet> sets){
		
		//On definit la matrice des moyennes pour chaque parametre de chaque touche
		double[][] avgMatrix = new double[sets.getFirst().getSet().size()][nbParams];
					
		Iterator<KeyStrokeSet> setsIter = sets.iterator();
				
		//On calcule la moyenne de chaque parametre
		while(setsIter.hasNext()){
			
			LinkedList<KeyStroke> strokes = setsIter.next().getSet();
			Iterator<KeyStroke> strokesIter = strokes.iterator();
			int keyIndex = 0;
			
			while(strokesIter.hasNext()){
				KeyStroke curr = strokesIter.next();
				double[] values = curr.getValues();
				for(int i=0; i<values.length; i++){
					//System.out.println(keyIndex + "|" + i + "|" + values[i]);
					avgMatrix[keyIndex][i] += (values[i] / ((double)sets.size()));}
				keyIndex++;	
			
			}
			
			System.out.println("End of sets iteration");
		
		}
		
		return avgMatrix;
		
	}
	
	public static double[][] getStandardDeviationMatrix(LinkedList<KeyStrokeSet> sets, double[][] avgMatrix){
		double[][] standardDeviationMatrix = new double[sets.getFirst().getSet().size()][nbParams];
		
		//On reinitialise l'iterateur de sets
		Iterator<KeyStrokeSet>setsIter = sets.iterator();
				
		//On calcule la matrice des variances
		while(setsIter.hasNext()){
			
			LinkedList<KeyStroke> strokes = setsIter.next().getSet();
			Iterator<KeyStroke> strokesIter = strokes.iterator();
			int keyIndex = 0;
					
			while(strokesIter.hasNext()){
					
				double[] values = strokesIter.next().getValues();
				for(int i=0; i<values.length; i++)					
					standardDeviationMatrix[keyIndex][i] += Math.pow(values[i]-avgMatrix[keyIndex][i],2) / ((double)sets.size());
				keyIndex++;	
					
			}
				
		}
		
		//L'ecart-type est la racine carree de la variance
		for(int i=0; i<standardDeviationMatrix.length; i++)
			for(int j=0; j<standardDeviationMatrix[i].length; j++)
				standardDeviationMatrix[i][j] = Math.sqrt(standardDeviationMatrix[i][j]);
				
		return standardDeviationMatrix;
		
	}

}