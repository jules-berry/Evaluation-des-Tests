package Analyse;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import Exception.BadLoginException;
import KeystrokeMeasuring.KeyStroke;
import Main.Account;

public class NormalizedGaussTest {

	public static boolean test(KeyStrokeSet testSet,int k,FileWriter fw) throws BadLoginException {

		LinkedList<KeyStrokeSet> sets = new LinkedList<KeyStrokeSet> (Main.Main.setList.get(k));

		double[][] avgMatrix = GaussTest.getAvgMatrix(sets);
		double[][] sdMatrix = GaussTest.getStandardDeviationMatrix(sets, avgMatrix);

		Iterator<KeyStroke> keyIter = testSet.getSet().iterator();
		int keyIndex = 0;

		double normValue = 0.0;

		int nbParamsNotNull = 0;

		while (keyIter.hasNext()) {
			double[] tempValues = keyIter.next().getValues();
			for (int i = 0; i < tempValues.length; i++) {
				if (sdMatrix[keyIndex][i] != 0) {
					normValue += Math.abs(tempValues[i] - avgMatrix[keyIndex][i]) / sdMatrix[keyIndex][i];
					nbParamsNotNull++;
				} else {
					if (tempValues[i] != avgMatrix[keyIndex][i])
						return false;
				}
			}
			keyIndex++;
		}

		double normValueThreshold = avgMatrix.length * nbParamsNotNull;

		System.out.println("value: " + normValue + "|threshold: " + normValueThreshold);
		try {
			fw.write(normValue +",");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (normValue <= normValueThreshold)
			return true;
		else
			return false;

	}

	
	
}