package Analyse;

import java.util.Iterator;
import java.util.LinkedList;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;

import Exception.BadLoginException;
import KeystrokeMeasuring.KeyStroke;
import Main.Account;

public class SimpleGaussTest {
	
	private static final double gaussianCoef = 3; // si 1 niveau confiance de
	// 67%, si 2 niveau de
	// confiance 95%, si 3
	// niveau de confiance 99%
	
	public static boolean test(KeyStrokeSet testSet, Account account,int k) throws BadLoginException {

		try {

			boolean isTheSamePerson = true;

			LinkedList<KeyStrokeSet> sets = Main.Main.setList.get(k);

			double[][] avgMatrix = GaussTest.getAvgMatrix(sets);
			double[][] sdMatrix = GaussTest.getStandardDeviationMatrix(sets, avgMatrix);

			Iterator<KeyStroke> keyIter = testSet.getSet().iterator();
			int keyIndex = 0;

			while (isTheSamePerson && keyIter.hasNext()) {

				double[] values = keyIter.next().getValues();
				int i = 0;

				while (i < GaussTest.getNbparams() && isTheSamePerson) {

					double min = avgMatrix[keyIndex][i] - gaussianCoef * sdMatrix[keyIndex][i];
					double max = avgMatrix[keyIndex][i] + gaussianCoef * sdMatrix[keyIndex][i];
					if (values[i] < min || values[i] > max)
						isTheSamePerson = false;
					i++;

				}

				keyIndex++;

			}

			return isTheSamePerson;

		} catch (EncryptionOperationNotPossibleException e) {
			throw new BadLoginException();
		}

	}

}