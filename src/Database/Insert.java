package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import Main.Account;
import Main.Main;
import javax.swing.JOptionPane;
import Session.Session;
import Encryption.Encryption;


// C'est moche mais bon ça marche...

public class Insert {

	
	public static void addCompte(Account account,int passwordLength,Connection conn){
		
		System.out.println("Ajout d'un compte");
		String ePassword = Encryption.encryptPassword(account.getPasswordAsString());
		int  eLogin = account.getLoginHash();
		int hDomain = account.getDomainHash();
		String ePasswordLength = Encryption.encryptInt(passwordLength, account.getPasswordAsString());
		
		
        
       
       String compte = "INSERT INTO Compte (Login,masterPassword,domainHash,passwordLength,CompteSystem_Login) "
       		+ "values (?,?,?,?,?);";
       
       
       try {
    	   Statement begin = conn.createStatement();
			begin.executeQuery("Start Transaction;");
			PreparedStatement compteStatement = conn.prepareStatement(compte);
			compteStatement.setString(1, String.valueOf(eLogin));
			compteStatement.setString(2, ePassword);
			compteStatement.setString(3, String.valueOf(hDomain));
			compteStatement.setString(4, ePasswordLength);
			compteStatement.setString(5, String.valueOf(account.getSysAccount().getSysLoginHash()));
			compteStatement.executeUpdate();
			System.out.println("Compte ajouté");
			Statement commit = conn.createStatement();
	        commit.executeQuery("Commit;");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Erreur lors de l'ajout du compte");
		}
 
       
       
	}
		
	
	
	public static  void addSession(Session s,Connection conn){
		
		System.out.println("Ajout d'un nouvelle session");
		ResultSet res = null;
		
		// on recupere le compte associe a la session
		
		int userId = s.getAccount().getLoginHash();
		
		int domain = s.getAccount().getDomainHash();
		
		String account = "SELECT Compte.Index FROM Compte WHERE Login = \""+userId+
				"\" and domainHash = " + domain+";"; 
				
		
		String session = "INSERT INTO Session (Compte_Index,sucess) values(?,?); ";
		
		String sessionIndex = "SELECT max(Session.index) FROM Session;";
		
		String entree = "INSERT INTO Entree (Session_index,Local,try) values (?,?,?)";
		
		String entreeIndex = "SELECT max(Entree.Index) From Entree where Session_index = ?;";
		
		String touche = "INSERT INTO Touche (Entree_Index,timeUp,timeDown,pressure,modifierSequence,"
				+ "shiftUp,shiftDown,shiftLocation,ctrlUp,ctrlDown,ctrlLocation,altUp,altDown,"
				+ "altLocation,capslockUp,capsLockDown) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?),";
		String toucheValues = "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		
		
		
                
        Statement accountStatement;
		try {
			accountStatement = conn.createStatement();
			Statement begin = conn.createStatement();
			begin.executeQuery("Start Transaction;");
			 res =accountStatement.executeQuery(account);
		        
		        int accountId = 0;
		        while (res.next()){
		        	accountId = res.getInt(1);
		        }
		        
		        PreparedStatement sessionStatement = conn.prepareStatement(session);
		        sessionStatement.setInt(1,accountId);
		        sessionStatement.setBoolean(2,s.isSuccess());
				sessionStatement.executeUpdate();
				
				Statement sessionIndexStatement = conn.createStatement();
				res = sessionIndexStatement.executeQuery(sessionIndex);
				int sessionId = 0;
				while(res.next()){
					sessionId = res.getInt("max(Session.index)");
				}
				System.out.println("Ajout des donnéees");
				for (int i=0; i<s.getPasswordTries().size();i++){
					System.out.println("Ajout entree " + i+" pour la session " +sessionId );
					PreparedStatement entreeStatement = conn.prepareStatement(entree);
					entreeStatement.setInt(1,sessionId);
					entreeStatement.setString(2,s.getLocal());
					entreeStatement.setInt(3,i);
					entreeStatement.executeUpdate();
					
					PreparedStatement entreeIndexStatement = conn.prepareStatement(entreeIndex);
					entreeIndexStatement.setInt(1, sessionId);
					res = entreeIndexStatement.executeQuery();
					int entreeId=0;
					while(res.next()){
						entreeId = res.getInt("max(Entree.Index)");
					}
					
					String allTouche = touche + String.join(",", Collections.nCopies(s.getPasswordTries().get(i).getKeys().size()-1, toucheValues))+";";
					PreparedStatement toucheStatement = conn.prepareStatement(allTouche);
					System.out.println("Size " + s.getPasswordTries().get(i).getKeys().size());

					for(int j=0; j<s.getPasswordTries().get(i).getKeys().size();j++){
						//System.out.println("Ajout de la touche " + j + " pour l'entree " + entreeId);
						ArrayList<String>encryptedValues = s.getPasswordTries().get(i).getKeys().get(j).getEncryptedValues(s.getAccount().getPasswordAsString());
						toucheStatement.setInt(j*16+1,entreeId);
						
						//TODO moddifier avec un iterator 
						int k=0;
						for (k=0; k<encryptedValues.size();k++){
							
							toucheStatement.setString(j*16 +k+2,encryptedValues.get(k));
						}
						
						while(k+2<17){
							toucheStatement.setString(j*16+k+2, Encryption.encryptText("NULL", s.getAccount().getPasswordAsString()));
							k++;
						}

					}
					System.out.println(toucheStatement.executeUpdate());

				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        System.out.println("Session ajoutée");
        Statement commit;
		try {
			commit = conn.createStatement();
	        commit.executeQuery("Commit;");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
       
	}
public static String addCompteSystem(String identifiant, String password,Connection conn){ 
		
		PreparedStatement insertAccountSystem = null;
		Statement ps = null;
		ResultSet res = null;
		boolean existsYet = false;
		identifiant = String.valueOf(identifiant.hashCode());
		password = Encryption.encryptPassword(password);

		String getLogin = "SELECT Login FROM CompteSystem";
        
        try { //on verifie que la cle (login) n existe pas deja
        	Statement begin = conn.createStatement();
    		begin.executeQuery("Start Transaction;");
			ps = conn.createStatement();
			res =ps.executeQuery(getLogin);
			while(res.next()){
				existsYet  = res.getString("Login").equals(identifiant);   //true si identifant existe deja dans la BD
				if(existsYet==true){
					break;
				}
			}
			
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        
        if(!(existsYet)){

	        
	        String insertCompteSystem = "INSERT INTO CompteSystem(Login,Password) VALUES (?,?);";
	        
	        try {
	        	if (existsYet==false){					//cree le compte si l identifiant n existe pas deja
	        		insertAccountSystem = conn.prepareStatement(insertCompteSystem);  
	        		insertAccountSystem.setString(1, identifiant);
	        		insertAccountSystem.setString(2, password);
	        	}
	        	else{
	        		JOptionPane.showMessageDialog(null, "This id is already used, try again");  //sinon affiche un message d erreur
	        	}
			} catch (SQLException e1) {
				System.err.println("Could not create prepared statements");
	
				e1.printStackTrace();
				System.exit(0);
	
			}
	        System.out.println("Statements ready...");
	
	        try {
	        	insertAccountSystem.executeUpdate();
	        	Statement commit = conn.createStatement();
		        commit.executeQuery("Commit;");
				
			} catch (SQLException e1) {
				System.err.println("Could not execute updates");
				e1.printStackTrace();
				System.exit(0);
	
			}
	        System.out.println("Done...");
	        return "Compte ajouté";
        }
        return "Une erreur s'est produite";

	}

	
}