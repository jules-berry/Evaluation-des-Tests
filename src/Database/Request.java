package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import Analyse.KeyStrokeSet;
import Exception.BadLoginException;
import KeystrokeMeasuring.KeyStroke;

import java.sql.PreparedStatement;

import Main.Main;
import Main.Account;

public class Request {

	public static ResultSet getLogin(int i, Connection conn) {

		String request = "SELECT Login,masterPassword,passwordLength FROM Compte "
				+ "WHERE CompteSystem_Login = ? and domainHash = ?;";

		PreparedStatement st;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(request);
			st.setString(1, String.valueOf(Main.currentSystemAccount.getLogin().hashCode()));
			st.setString(2, String.valueOf(i));
			rs = st.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rs;
	}

	public static LinkedList<KeyStrokeSet> getLastSuccessfulEntries(Account account, Connection conn) {
		int loginHash = account.getLoginHash();
		int domainHash = account.getDomainHash();
		String sysLoginHash = String.valueOf(account.getSysAccount().getSysLoginHash());

		String request = "Select * From Touche Where Touche.Entree_Index in (SELECT Entree.Index from Entree"
				+ " Where Entree.Session_index in (Select Session.index From Session"
				+ " Where Session.sucess = 1 and Session.Compte_Index in (Select Compte.Index From Compte"
				+ " Where Compte.Login = ? and Compte.domainHash = ? and Compte.CompteSystem_Login = ?))"
				+ " Order by Entree.Index DESC);";

		ResultSet res = null;
		LinkedList<KeyStrokeSet> sets = new LinkedList<KeyStrokeSet>();
		int[] indexes = new int[50];

		try {
			PreparedStatement entriesStatement = conn.prepareStatement(request);
			entriesStatement.setInt(1, loginHash);
			entriesStatement.setInt(2, domainHash);
			entriesStatement.setString(3, sysLoginHash);
			res = entriesStatement.executeQuery();
			int prevIndex = -1;
			LinkedList<KeyStroke> set = new LinkedList<KeyStroke>();

			while (res.next() && sets.size() <= 50) {
				if (res.getInt("Entree_Index") != prevIndex) {
					if (set!=null && set.size()>0){
						System.out.print("#");
						sets.add(new KeyStrokeSet(set));
					}
					set = new LinkedList<KeyStroke>();
					prevIndex = res.getInt("Entree_Index");
				}
				ArrayList<String> encryptedValues = new ArrayList<String>(8);
				for (int i = 1; i < 16; i++) {
					encryptedValues.add(res.getString(i));
					// System.out.println("String : " + res.getString(i));
				}
				// System.out.println("values" + values.size());
				set.add(new KeyStroke(encryptedValues, account));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print("\n");
		return sets;
	}

	public static ArrayList<ArrayList<String>> getTouchesForEntry(int entryIndex, Connection conn) {
		String request = "Select * From Touche Where Touche.Entree_Index = ?;";

		ResultSet res = null;
		// System.out.println("test for entree : " + entryIndex);

		ArrayList<ArrayList<String>> keys = new ArrayList<ArrayList<String>>(16);

		try {
			PreparedStatement entriesStatement = conn.prepareStatement(request);
			entriesStatement.setInt(1, entryIndex);
			res = entriesStatement.executeQuery();
			while (res.next()) {
				ArrayList<String> values = new ArrayList<String>(16);
				for (int i = 1; i < 16; i++) {
					values.add(res.getString(i));
					// System.out.println("String : " + res.getString(i));
				}
				// System.out.println("values" + values.size());
				keys.add(values);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("keys before return : " + keys.size());
		return keys;

	}

	public static String getPasswordForSystemAccount(String login, Connection conn) throws BadLoginException {

		login = String.valueOf(login.hashCode());
		System.out.println(login);

		String password = "";

		String request = "SELECT Password FROM CompteSystem WHERE Login = ? LIMIT 1;";

		PreparedStatement statement = null;

		ResultSet rs = null;

		try {
			statement = conn.prepareStatement(request);
			statement.setString(1, login);
			rs = statement.executeQuery();
			rs.next();
			password = rs.getString(1);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new BadLoginException();
		}
		return password;
	}

	public static String getEncryptedPassword(Account account, Connection conn) throws BadLoginException {
		int loginHash = account.getLoginHash();
		int domainHash = account.getDomainHash();

		String request = "SELECT masterPassword FROM Compte WHERE Login = ? AND domainHash = ?;";

		PreparedStatement statement = null;

		ResultSet rs = null;

		try {
			statement = conn.prepareStatement(request);
			statement.setInt(1, loginHash);
			statement.setInt(2, domainHash);
			rs = statement.executeQuery();
			rs.first();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			return rs.getString("masterPassword");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BadLoginException();
		}
	}

	public static boolean checkIfAccountExists(Account account, Connection conn) {
		int sysLoginHash = account.getSysAccount().getSysLoginHash();
		int loginHash = account.getLoginHash();
		int domainHash = account.getDomainHash();

		String query = "Select Compte.domainHash From Compte Where Compte.CompteSystem_Login = ? and Compte.Login = ?;";

		try {
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, String.valueOf(sysLoginHash));
			st.setInt(2, loginHash);

			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) == domainHash) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}