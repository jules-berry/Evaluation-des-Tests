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
		
		public KeyStrokeSet(LinkedList<KeyStroke> set){
			this.setSet(new LinkedList<KeyStroke>(set));
			Iterator<KeyStroke> itr = set.iterator();
			KeyStroke cur=itr.next(); //l'iterator commence avant le premier element
			KeyStroke next;
			while (itr.hasNext()){
				next = itr.next();
				cur.setNext(next);
				cur=next;
			}
		}
	
	public static LinkedList<KeyStrokeSet> buildReferenceSet(Account account){
		Connection conn = Main.Main.conn;
		LinkedList <KeyStrokeSet> sets = new LinkedList<KeyStrokeSet>();
		int[] refIndexes = Request.getLastSuccessfulEntries(account,conn);
		
			for (int k =0; k<refIndexes.length;k++){
				LinkedList <KeyStroke> keys = new LinkedList <KeyStroke>();
				ArrayList <ArrayList<String>> keysForEntry = Request.getTouchesForEntry(refIndexes[k],conn);
				//System.out.println("keys :" + keysForEntry.size());
				for(int j=0;j<keysForEntry.size();j++){
					
					keys.add(new KeyStroke(new ArrayList<String>(keysForEntry.get(j)),account));
					if(keys.size()>1){
						keys.get(keys.size()-2).setNext(keys.get(keys.size()-1));
					}
				}
				if(keys.size()>0)
				sets.add(new KeyStrokeSet (keys));
				System.out.print("#");
			}
			//System.out.println("sets" + sets.size());
			System.out.println("\n");
			return sets;
	}

	public LinkedList<KeyStroke> getSet() {
		return set;
	}

	public void setSet(LinkedList<KeyStroke> set) {
		this.set = new LinkedList<KeyStroke>(set);
	}

}