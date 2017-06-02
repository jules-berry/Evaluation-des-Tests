package Analyse;

import java.util.Iterator;
import java.util.LinkedList;

import KeystrokeMeasuring.KeyStroke;

public class GaussTest {

	private static final double gaussianCoef = 3; // si 1 niveau confiance de
													// 67%, si 2 niveau de
													// confiance 95%, si 3
													// niveau de confiance 99%
	private static final int nbParams = 15;

	protected static double[][] getAvgMatrix(LinkedList<KeyStrokeSet> sets) {
		System.out.println("gauss test"+sets.size());
		// On definit la matrice des moyennes pour chaque parametre de chaque
		// touche
		double[][] avgMatrix = new double[sets.getFirst().getSet().size()][nbParams];

		Iterator<KeyStrokeSet> setsIter = sets.iterator();

		// On calcule la moyenne de chaque parametre
		while (setsIter.hasNext()) {

			LinkedList<KeyStroke> strokes = setsIter.next().getSet();
			Iterator<KeyStroke> strokesIter = strokes.iterator();
			int keyIndex = 0;

			while (strokesIter.hasNext()) {
				KeyStroke curr = strokesIter.next();
				double[] values = curr.getValues();
				for (int i = 0; i < values.length; i++) {
					if (i < avgMatrix[keyIndex].length)
						avgMatrix[keyIndex][i] += (values[i] / (sets.size()));
				}
				keyIndex++;

			}

			System.out.println("End of sets iteration");

		}

		return avgMatrix;

	}

	protected static double[][] getStandardDeviationMatrix(LinkedList<KeyStrokeSet> sets, double[][] avgMatrix) {
		double[][] standardDeviationMatrix = new double[sets.getFirst().getSet().size()][nbParams];

		// On reinitialise l'iterateur de sets
		Iterator<KeyStrokeSet> setsIter = sets.iterator();

		// On calcule la matrice des variances
		while (setsIter.hasNext()) {

			LinkedList<KeyStroke> strokes = setsIter.next().getSet();
			Iterator<KeyStroke> strokesIter = strokes.iterator();
			int keyIndex = 0;

			while (strokesIter.hasNext()) {

				double[] values = strokesIter.next().getValues();
				for (int i = 0; i < values.length; i++)
					standardDeviationMatrix[keyIndex][i] += Math.pow(values[i] - avgMatrix[keyIndex][i], 2)
							/ (sets.size());
				keyIndex++;

			}

		}

		// L'ecart-type est la racine carree de la variance
		for (int i = 0; i < standardDeviationMatrix.length; i++)
			for (int j = 0; j < standardDeviationMatrix[i].length; j++)
				standardDeviationMatrix[i][j] = Math.sqrt(standardDeviationMatrix[i][j]);

		return standardDeviationMatrix;

	}

	public static double getGaussiancoef() {
		return gaussianCoef;
	}

	public static int getNbparams() {
		return nbParams;
	}

}
