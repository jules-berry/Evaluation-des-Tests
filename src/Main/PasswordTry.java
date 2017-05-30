package Main;

import java.util.ArrayList;
import java.util.LinkedList;

import Analyse.KeyStrokeSet;
import KeystrokeMeasuring.KeyStroke;

public class PasswordTry {
	
	private ArrayList <KeyStroke> keys;
	private boolean success;
	private String userId;
	private String domain;
	private String password;
	

	@SuppressWarnings("unchecked")
	public PasswordTry(ArrayList <KeyStroke> keys){
		this.keys=new ArrayList<KeyStroke>(keys);
	}
	
	
	public KeyStrokeSet toKeyStrokeSet(){
		LinkedList l = new LinkedList(keys);
		return new KeyStrokeSet( l);
	}
	public PasswordTry(ArrayList <KeyStroke> keys, boolean success){

		this.keys=new ArrayList<KeyStroke>(keys);
	}
	
	public ArrayList<KeyStroke> getKeys() {
		return keys;
	}

	public void setKeys(ArrayList<KeyStroke> keys) {
		this.keys = keys;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}