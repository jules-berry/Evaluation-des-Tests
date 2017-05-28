package KeystrokeMeasuring;

import java.util.ArrayList;
import java.util.Arrays;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;

import Encryption.Encryption;
import Main.Account;

public class KeyStroke extends Key {
	
	private double pressure;
	private int modifierSequence;
	private char c;
	private Modifier shift, ctrl, alt, capsLock;
	private KeyStroke next;
	
	public KeyStroke(char c, long timeUp, long timeDown){
		super(timeUp, timeDown);
		this.setC(c);
		this.setNext(null);
	}
	
	public KeyStroke(ArrayList<String> encryptedValues, Account account) throws EncryptionOperationNotPossibleException {
		
		super(Encryption.decryptLong(encryptedValues.get(0), account.getPasswordAsString()), Encryption.decryptLong(encryptedValues.get(0), account.getPasswordAsString()));
	
		setPressure(Encryption.decryptValue(encryptedValues.get(2), account.getPasswordAsString()));
		setModifierSequence(Encryption.decryptInt(encryptedValues.get(3), account.getPasswordAsString()));
		long tempDown=Encryption.decryptLong(encryptedValues.get(5), account.getPasswordAsString());
		if(tempDown>=0){
			setShift(new Modifier(Encryption.decryptLong(encryptedValues.get(4), account.getPasswordAsString()), tempDown,Encryption.decryptInt(encryptedValues.get(6), account.getPasswordAsString())));
		} else setShift(null);
		tempDown=Encryption.decryptLong(encryptedValues.get(8), account.getPasswordAsString());
		if(tempDown>=0){
			setCtrl(new Modifier(Encryption.decryptLong(encryptedValues.get(7), account.getPasswordAsString()), tempDown,Encryption.decryptInt(encryptedValues.get(9), account.getPasswordAsString())));
		} else setCtrl(null);
		tempDown=Encryption.decryptLong(encryptedValues.get(11), account.getPasswordAsString());
		if(tempDown>=0){
			setAlt(new Modifier(Encryption.decryptLong(encryptedValues.get(10), account.getPasswordAsString()), tempDown,Encryption.decryptInt(encryptedValues.get(12), account.getPasswordAsString())));
		} else setAlt(null);
		tempDown=Encryption.decryptLong(encryptedValues.get(14), account.getPasswordAsString());
		if(tempDown>=0){
			setCapsLock(new Modifier(Encryption.decryptLong(encryptedValues.get(13), account.getPasswordAsString()), tempDown));
		} else setCapsLock(null);

	}
	
	/**
	 * Calcule la norme 1 du vecteur
	 * @return la norme 1
	 */
	public double getNorme1(){
		double norme1 = 0;
		double[] values = getValues();
		for(double v: values)
			norme1 += Math.abs(v);
		return norme1;
	}
	
	/**
	 * Retourne le produit scalaire entre ce vecteur et le vecteur de reference
	 * @param ref Le vecteur de reference
	 * @return le produit scalaire
	 */
	public double getScalarProduct(KeyStroke ref){
		double sP = 0;
		double[] values = getValues();
		double[] refValues = ref.getValues();
		for(int i=0; i<values.length; i++)
			sP += values[i]*refValues[i];
		return sP;
	}
	
	/**
	 * Calcule la norme euclidienne du vecteur a partir du produit scalaire canonique
	 * @return la norme 2
	 */
	public double getNorme2(){
		return Math.sqrt(this.getScalarProduct(this));
	}
	
	@Override
	public long getReleasePressTimes(){
		if(next!=null)
			return this.next.getTimeDown() - this.getTimeUp();
		else return 0;
	}
		
	/**
	 * Retourne les caracteres correspondant aux touches passes en parametre
	 * @param keyStrokes les touches
	 * @return le tableau de caracteres
	 */
	public static char[] getChars(ArrayList<KeyStroke> keyStrokes){
		char[] chars = new char[keyStrokes.size()];
		for(int i=0; i<chars.length; i++){
			chars[i]=keyStrokes.get(i).getC();
		}
		return chars;	
	}
	
	/**
	 * Calcule la distance euclidienne entre ce vecteur et une reference
	 * @param k le vecteur de reference
	 * @return la distance euclidienne
	 */
	public double euclidianDistance (KeyStroke k){
		
		double result = 0;
		double[] values = getValues();
		double[] refValues = k.getValues();
		for(int i=0; i<values.length; i++)
			result += Math.pow(refValues[i]-values[i], 2);
		return Math.sqrt(result);

	}
	
	/**
	 * Calcule la distance de manhattan entre ce vecteur et une reference
	 * @param k le vecteur de reference
	 * @return la distance de manhattan
	 */
	public double manhattanDistance(KeyStroke k){
		double result = 0;
		double values[] = getValues();
		double refValues[] = k.getValues();
		for(int i=0; i<values.length; i++)
			result += Math.abs(refValues[i]-values[i]);
		return result;
	}	
	
	/**
	 * Retourne la similarite cosinus entre cette touche et une touche de reference
	 * @param ref La touche de reference
	 * @return La similarite cosinus
	 */
	public double getCosineSimilarity(KeyStroke ref){
		return this.getScalarProduct(ref) / (this.getNorme2()*ref.getNorme2());
	}
	
	/**
	 * Retourne un tableau avec les valeurs numeriques necessaires a l'exploitation
	 * @return le taleau des valeurs
	 */
	public double[] getValues(){
		double[] values = new double[14];
		Arrays.fill(values, 0.0);
		values[0] = getReleasePressTimes();
		values[1] = getPressReleaseTimes();
		values[2] = getPressure();
		if(getShift() != null){
			values[3] = getShift().getReleasePressTimes();
			values[4] = getShift().getPressReleaseTimes();
			values[5] = getShift().getLocation();
		}
		if(getCtrl() != null){
			values[6] = getCtrl().getReleasePressTimes();
			values[7] = getCtrl().getPressReleaseTimes();
			values[8] = getCtrl().getLocation();
		}
		if(getAlt() != null){
			values[3] = getAlt().getReleasePressTimes();
			values[4] = getAlt().getPressReleaseTimes();
			values[5] = getAlt().getLocation();
		}
		if(getCapsLock() != null){
			values[3] = getCapsLock().getReleasePressTimes();
			values[4] = getCapsLock().getPressReleaseTimes();
			values[5] = getCapsLock().getLocation();
		}
		return values;
	}
	
	@Override
	public ArrayList<String> getEncryptedValues(String p){
		ArrayList<String> encryptedValues = super.getEncryptedValues(p);
		encryptedValues.add(Encryption.encryptValue(pressure,p));
		encryptedValues.add(Encryption.encryptInt(modifierSequence,p));
		if(shift!=null)
			encryptedValues.addAll(shift.getEncryptedValues(p));
		if(ctrl!=null)
			encryptedValues.addAll(ctrl.getEncryptedValues(p));
		if(alt!=null)
			encryptedValues.addAll(alt.getEncryptedValues(p));
		if(capsLock!=null)
			encryptedValues.addAll(capsLock.getEncryptedValues(p));
		return encryptedValues;
	}
	
	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public int getModifierSequence() {
		return modifierSequence;
	}

	public void setModifierSequence(int modifierSequence) {
		this.modifierSequence = modifierSequence;
	}

	public char getC() {
		return c;
	}

	public void setC(char c) {
		this.c = c;
	}
	public Modifier getShift() {
		return shift;
	}

	public void setShift(Modifier shift) {
		this.shift = shift;
		if(shift!=null)
			this.shift.setAssociatedKeyStroke(this);
	}

	public Modifier getCtrl() {
		return ctrl;
	}

	public void setCtrl(Modifier ctrl) {
		this.ctrl = ctrl;
		if(ctrl!=null)
			this.ctrl.setAssociatedKeyStroke(this);
	}

	public Modifier getAlt() {
		return alt;
	}

	public void setAlt(Modifier alt) {
		this.alt = alt;
		if(alt!=null)
			this.alt.setAssociatedKeyStroke(this);
	}

	public Modifier getCapsLock() {
		return capsLock;
	}

	public void setCapsLock(Modifier capsLock) {
		this.capsLock = capsLock;
		if(capsLock!=null)
			this.capsLock.setAssociatedKeyStroke(this);
	}

	public KeyStroke getNext() {
		return next;
	}

	public void setNext(KeyStroke next) {
		this.next = next;
	}	
}
