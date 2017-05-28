package Analyse;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;

import Database.ConnectionBD;
import Database.Request;
import Exception.BadLoginException;
import KeystrokeMeasuring.KeyStroke;
import Main.Account;
import Warnings.SimpleWarning;

public class DistanceTest {
	
	//TODO régler les valeurs des seuils
	private static final double euclidianRatioThreshold = 0.37;
	private static final double manhattanRatioThreshold = 0.1;
	
	//TODO fusionner login,domain et password dans une instace ce compte
	public static boolean test(KeyStrokeSet testSet, Account account,FileWriter fw) throws BadLoginException{
		try{
			LinkedList <KeyStrokeSet> sets = new LinkedList(Main.Main.sets);
			
			double[][] euclidianDistances = buildEuclidianDistances(testSet,sets);
			double[][] manhattanDistances = buildManhattanDistances(testSet,sets);
			double[] avgEuclidianDistance  = new double [euclidianDistances.length];
			double[] avgManhattanDistance = new double [manhattanDistances.length];
			double[] avgEuclidianDistanceRatio = new double [euclidianDistances.length];
			double[] avgManhattanDistanceRatio = new double [manhattanDistances.length];
			double avgEuclidianRatio = 0;
			double avgManhatanRatio = 0;
			int i;
			for(i=0; i<sets.size();i++){
				avgEuclidianDistance[i] = 0;
				avgManhattanDistance[i] = 0;
				for(int j=0; j<testSet.getSet().size();j++ ){
	
					if(testSet.getSet().get(j).getNorme2()!=0 && testSet.getSet().get(j).getNorme1()!=0 && j<euclidianDistances[i].length){
						avgEuclidianDistance[i]+=euclidianDistances[i][j];
						avgManhattanDistance[i]+=manhattanDistances[i][j];
						avgEuclidianDistanceRatio[i]+=euclidianDistances[i][j]/(testSet.getSet().get(j).getNorme2()+sets.get(i).getSet().get(j).getNorme2());
						avgManhattanDistanceRatio[i]+=manhattanDistances[i][j]/(testSet.getSet().get(j).getNorme1()+sets.get(i).getSet().get(j).getNorme1());
					}
				
				}
				avgEuclidianDistanceRatio[i] /= euclidianDistances[i].length;
				avgManhattanDistance[i]/=manhattanDistances[i].length;
				avgEuclidianDistance[i]/=euclidianDistances[i].length;
				avgManhattanDistance[i]/=manhattanDistances[i].length;
				//System.out.println("AvgEuclidianDistance : " + avgEuclidianDistance[i]);
				//System.out.println("AvgManhattanDistance : " + avgManhattanDistance[i]);
				
				avgEuclidianRatio+=avgEuclidianDistanceRatio[i];
				avgManhatanRatio+=avgManhattanDistanceRatio[i];
	
			}
			avgEuclidianRatio /=(euclidianDistances.length);
			avgManhatanRatio/=(euclidianDistances.length);
			try {
				fw.write(String.valueOf(avgEuclidianRatio) + ",");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return(avgEuclidianRatio<euclidianRatioThreshold );
		}catch (EncryptionOperationNotPossibleException e){
			throw new BadLoginException();
		}
	}
	
	
	
	//TODO convertir en ArrayList pour gagner du temps à l'exécution?
	private static double[][] buildEuclidianDistances(KeyStrokeSet testSet,LinkedList <KeyStrokeSet> sets){
		//System.out.println("size : " + sets.get(0).getSet().size() );
		double[][] distances = new double[sets.size()][];
		for(int i=0;i<distances.length;i++){
			distances[i] = new double[sets.get(i).getSet().size()];
			for(int j=0; j<distances[i].length; j++){
				if(j<testSet.getSet().size()){
					distances[i][j] = testSet.getSet().get(j).euclidianDistance(sets.get(i).getSet().get(j));
				}else{
					System.out.println(testSet.getSet().size());
					distances[i][j] = 0;
				}
			}
		}
		return distances;
	}
	
	//TODO convertir en ArrayList pour gagner du temps à l'exécution?
	private static double[][] buildManhattanDistances(KeyStrokeSet testSet,LinkedList <KeyStrokeSet> sets){
		double[][] distances = new double[sets.size()][];
		for(int i=0;i<distances.length;i++){
			distances[i] = new double[sets.get(i).getSet().size()];
			for(int j=0; j<distances[i].length; j++){
				if(j<testSet.getSet().size()){
					distances[i][j] = testSet.getSet().get(j).manhattanDistance(sets.get(i).getSet().get(j));
				}else{
					distances[i][j] = 0;
				}

			}
		}
		return distances;
	}


}