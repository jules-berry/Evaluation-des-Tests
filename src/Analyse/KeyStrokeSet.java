package Analyse;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import Database.Request;
import KeystrokeMeasuring.KeyStroke;
import Main.Account;

public class KeyStrokeSet {

	private LinkedList<KeyStroke> set;
	public int index;
	public String systemAccount;

	public KeyStrokeSet(LinkedList<KeyStroke> set) {
		this.setSet(new LinkedList<KeyStroke>(set));
		Iterator<KeyStroke> itr = set.iterator();
		KeyStroke cur = itr.next(); // l'iterator commence avant le premier
									// element
		KeyStroke next;
		while (itr.hasNext()) {
			next = itr.next();
			cur.setNext(next);
			cur = next;
		}
	}

	public KeyStrokeSet(LinkedList<KeyStroke> set, int index, String account) {
		this.systemAccount = account;
		this.setSet(new LinkedList<KeyStroke>(set));
		this.index = index;
		Iterator<KeyStroke> itr = set.iterator();
		KeyStroke cur = itr.next(); // l'iterator commence avant le premier
									// element
		KeyStroke next;
		while (itr.hasNext()) {
			next = itr.next();
			cur.setNext(next);
			cur = next;
		}
	}

	public static LinkedList<KeyStrokeSet> buildReferenceSet(Account account) {
		Connection conn = Main.Main.conn;
		LinkedList<KeyStrokeSet> sets = Request.getLastSuccessfulEntries(account, conn);
		return sets;
	}

	public LinkedList<KeyStroke> getSet() {
		return set;
	}

	public void setSet(LinkedList<KeyStroke> set) {
		this.set = new LinkedList<KeyStroke>(set);
	}

	public KeyStrokeSet clone() {
		return new KeyStrokeSet(this.set, this.index, this.systemAccount);
	}

}