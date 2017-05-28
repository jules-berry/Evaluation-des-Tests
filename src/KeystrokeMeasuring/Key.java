package KeystrokeMeasuring;

import java.util.ArrayList;

import Encryption.Encryption;

public abstract class Key {
	private long timeUp, timeDown;
	
	public Key(long timeUp, long timeDown){
		this.setTimeUp(timeUp);
		this.setTimeDown(timeDown);
	}
	
	public long getTimeUp() {
		return timeUp;
	}
	
	public ArrayList<String> getEncryptedValues(String p){
		ArrayList<String> encryptedValues = new ArrayList<String>();
		encryptedValues.add(Encryption.encryptLong(timeUp, p));
		encryptedValues.add(Encryption.encryptLong(timeDown, p));
		return encryptedValues;
	}
	
	/**
	 * Methode permettant de calculer le temps d'appui d'une touche
	 * @return La difference de temps entre timeUp et timeDown
	 */
	public long getPressReleaseTimes(){
		return this.getTimeUp() - this.getTimeDown();
	}
	
	/**
	 * Methode permettant de calculer le temps entre le relachement d'une touche et l'appui de la seconde
	 * @return la difference de temps Release-Press
	 */
	public abstract long getReleasePressTimes();
	
	public void setTimeUp(long timeUp) {
		this.timeUp = timeUp;
	}
	
	public long getTimeDown() {
		return timeDown;
	}

	public void setTimeDown(long timeDown) {
		this.timeDown = timeDown;
	}
}
