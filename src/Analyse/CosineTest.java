package Analyse;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;

import Exception.BadLoginException;
import KeystrokeMeasuring.KeyStroke;
import Main.Account;

public class CosineTest {

	private static final double cosineSimilarityThreshold = 0.75;

	public static boolean test(KeyStrokeSet bruteTestSet,int k, FileWriter fw) throws BadLoginException {
		try {

			LinkedList<KeyStrokeSet> bruteSets = new LinkedList<KeyStrokeSet> (Main.Main.setList.get(k));
			GaussNormalizer gn = new GaussNormalizer(bruteSets);
			LinkedList<KeyStrokeSet> sets = gn.getNormalizedSets();
			KeyStrokeSet testSet = gn.normalizeKeyStrokeSet(bruteTestSet);
			Iterator<KeyStrokeSet> setsIterator = sets.iterator();
			double meanCosineSimilarity = 0.0;

			// On definit la matrice de similarite avec les similarites cosinus
			// de chaque touche de chaque entree, on calcule aussi les
			// similarites moyennes pour chaque entree
			while (setsIterator.hasNext()) {

				LinkedList<KeyStroke> temp = setsIterator.next().getSet();

				if (temp.size() == testSet.getSet().size()) {

					Iterator<KeyStroke> tempIterator = temp.iterator();
					Iterator<KeyStroke> testIterator = testSet.getSet().iterator();

					double somme = 0.0;

					while (tempIterator.hasNext()) {

						KeyStroke test = testIterator.next();
						KeyStroke ref = tempIterator.next();
						somme += test.getCosineSimilarity(ref);
						System.out.println(somme);

					}
					double cosineSimilarity = somme / temp.size();
					meanCosineSimilarity += cosineSimilarity / sets.size();

				} else
					return false; // si pas la bonne taille, le mot de passe est
									// forcemment faux
			}
			System.out.println("Similarity: " + meanCosineSimilarity);
			try {
				fw.write(meanCosineSimilarity + ",");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return meanCosineSimilarity > cosineSimilarityThreshold;
			
		} catch (EncryptionOperationNotPossibleException e) {
			throw new BadLoginException();
		}
	}
	
	

}
