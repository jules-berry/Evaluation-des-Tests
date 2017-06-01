package Analyse;

import java.util.Iterator;
import java.util.LinkedList;

import KeystrokeMeasuring.KeyStroke;

public class GaussNormalizer {

	private double[][] avgMatrix;
	private double[][] sdMatrix;
	private LinkedList<KeyStrokeSet> sets;

	public GaussNormalizer(LinkedList<KeyStrokeSet> bruteSets) {

		sets = new LinkedList<KeyStrokeSet>(bruteSets);

		avgMatrix = GaussTest.getAvgMatrix(sets);
		sdMatrix = GaussTest.getStandardDeviationMatrix(sets, avgMatrix);

	}

	protected KeyStrokeSet normalizeKeyStrokeSet(KeyStrokeSet notNorm) {
		LinkedList<KeyStroke> normSet = new LinkedList<KeyStroke>();
		Iterator<KeyStroke> keyIter = notNorm.getSet().iterator();
		int keyIndex = 0;

		while (keyIter.hasNext()) {
			double[] tempValues = keyIter.next().getValues();
			double[] tempNorm = new double[GaussTest.getNbparams()];
			for (int i = 0; i < tempValues.length; i++) {
				if (sdMatrix[keyIndex][i] != 0 && (i != 5 || i != 8 || i != 11 || i != 14)) {
					tempNorm[i] = Math.abs(tempValues[i] - avgMatrix[keyIndex][i]) / sdMatrix[keyIndex][i];
				} else
					tempNorm[i] = tempValues[i];
				normSet.add(new KeyStroke(tempNorm));
			}
			keyIndex++;
		}

		return new KeyStrokeSet(normSet);

	}

	protected LinkedList<KeyStrokeSet> getNormalizedSets() {
		
		LinkedList<KeyStrokeSet> normSets = new LinkedList<KeyStrokeSet>();
		Iterator<KeyStrokeSet> setsIter = sets.iterator();
		
		while (setsIter.hasNext()) {
			normSets.add(normalizeKeyStrokeSet(setsIter.next()));
		}
		
		return new LinkedList<KeyStrokeSet>(normSets);
	}

}
