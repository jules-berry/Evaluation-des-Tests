package Analyse;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import Main.Account;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import Exception.BadLoginException;

public class DistanceTest {

	// TODO régler les valeurs des seuils
	private static final double euclidianRatioThreshold = 0.5;
	private static final double manhattanRatioThreshold = 0.1;

	// TODO fusionner login,domain et password dans une instace ce compte
	public static boolean test(KeyStrokeSet bruteTestSet,int k, FileWriter fw) throws BadLoginException {
		try {

			LinkedList<KeyStrokeSet> bruteSets = new LinkedList<KeyStrokeSet> (Main.Main.setList.get(k));
			GaussNormalizer gn = new GaussNormalizer(bruteSets);
			LinkedList<KeyStrokeSet> sets = gn.getNormalizedSets();
			KeyStrokeSet testSet = gn.normalizeKeyStrokeSet(bruteTestSet);
			double[][] euclidianDistances = buildEuclidianDistances(testSet, sets);
			double[][] manhattanDistances = buildManhattanDistances(testSet, sets);
			double[] avgEuclidianDistance = new double[euclidianDistances.length];
			double[] avgManhattanDistance = new double[manhattanDistances.length];
			double[] avgEuclidianDistanceRatio = new double[euclidianDistances.length];
			double[] avgManhattanDistanceRatio = new double[manhattanDistances.length];
			double avgEuclidianRatio = 0;
			double avgManhatanRatio = 0;
			int i;
			for (i = 0; i < euclidianDistances.length; i++) {
				avgEuclidianDistance[i] = 0;
				avgManhattanDistance[i] = 0;
				for (int j = 0; j < testSet.getSet().size(); j++) {
					if (testSet.getSet().get(j).getNorme2() != 0 && testSet.getSet().get(j).getNorme1() != 0
							&& j < testSet.getSet().size() && i < euclidianDistances.length
							&& j < euclidianDistances[i].length) {
						avgEuclidianDistance[i] += euclidianDistances[i][j];
						avgManhattanDistance[i] += manhattanDistances[i][j];
						avgEuclidianDistanceRatio[i] += euclidianDistances[i][j]
								/ (testSet.getSet().get(j).getNorme2() + sets.get(i).getSet().get(j).getNorme2());
						avgManhattanDistanceRatio[i] += manhattanDistances[i][j]
								/ (testSet.getSet().get(j).getNorme1() + sets.get(i).getSet().get(j).getNorme1());
					}

				}
				avgEuclidianDistanceRatio[i] /= euclidianDistances[i].length;
				avgManhattanDistance[i] /= manhattanDistances[i].length;
				avgEuclidianDistance[i] /= euclidianDistances[i].length;
				avgManhattanDistance[i] /= manhattanDistances[i].length;

				avgEuclidianRatio += avgEuclidianDistanceRatio[i];
				avgManhatanRatio += avgManhattanDistanceRatio[i];

			}
			avgEuclidianRatio /= (euclidianDistances.length);
			avgManhatanRatio /= (euclidianDistances.length);
			System.out.println("avgRE : " + avgEuclidianRatio + " avgRM : " + avgManhatanRatio);
			try {
				fw.write(avgEuclidianRatio + ",");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return (avgEuclidianRatio < euclidianRatioThreshold);
		} catch (EncryptionOperationNotPossibleException e) {
			throw new BadLoginException();
		}
	}

	
	// TODO convertir en ArrayList pour gagner du temps à l'exécution?
	private static double[][] buildEuclidianDistances(KeyStrokeSet testSet, LinkedList<KeyStrokeSet> sets) {
		// System.out.println("size : " + sets.get(0).getSet().size() );
		double[][] distances = new double[sets.size()][];
		for (int i = 0; i < distances.length; i++) {
			distances[i] = new double[sets.get(i).getSet().size()];
			for (int j = 0; j < distances[i].length; j++) {
				if (j < testSet.getSet().size()) {
					distances[i][j] = testSet.getSet().get(j).euclidianDistance(sets.get(i).getSet().get(j));
				} else {
					System.out.println(testSet.getSet().size());
					distances[i][j] = 0;
				}
			}
		}
		return distances;
	}

	// TODO convertir en ArrayList pour gagner du temps à l'exécution?
	private static double[][] buildManhattanDistances(KeyStrokeSet testSet, LinkedList<KeyStrokeSet> sets) {
		double[][] distances = new double[sets.size()][];
		for (int i = 0; i < distances.length; i++) {
			distances[i] = new double[sets.get(i).getSet().size()];
			for (int j = 0; j < distances[i].length; j++) {
				if (j < testSet.getSet().size()) {
					distances[i][j] = testSet.getSet().get(j).manhattanDistance(sets.get(i).getSet().get(j));
				} else {
					distances[i][j] = 0;
				}

			}
		}
		return distances;
	}

}