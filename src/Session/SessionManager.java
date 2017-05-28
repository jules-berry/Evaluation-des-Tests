package Session;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Analyse.CosineTest;
import Analyse.DistanceTest;
import Analyse.GaussTest;
import Analyse.KeyStrokeSet;
import Database.Insert;
import Exception.BadLoginException;
import Main.Main;

/*
 * Objet permettant la gestion des sessions durant l'l'execution du programme  
 */

public class SessionManager {
	
	private Session currentSession; // session en cours
	private ArrayList <Session>prevSessions; // liste des sessions précédantes
	
	public SessionManager() throws IOException{
		prevSessions = new ArrayList<Session>();
		newSession(0);
	}
	
	// termine une session et l'ajoute à la liste des sessions terminées
	public void endCurrentSession(int k) throws IOException{
		currentSession.setRunning(false);
		//le succès de la session est défini à partir du succès de la dernière tentative
		if(currentSession.getPasswordTries().size()>0){
			currentSession.setSuccess(currentSession.getPasswordTries().get(currentSession.getPasswordTries().size()-1).isSuccess());
			FileWriter fw = new FileWriter (new File(Main.currentSystemAccount.getLogin()+"-test.csv"));
			FileWriter result = new FileWriter(new File(Main.currentSystemAccount.getLogin() + "-resultats.csv"));
			for(int i=0; i<currentSession.getPasswordTries().size();i++){
				KeyStrokeSet sk = currentSession.getPasswordTries().get(i).toKeyStrokeSet();
				
				for(int j=0; j<currentSession.getPasswordTries().get(i).getKeys().size();j++){
					double[] values = currentSession.getPasswordTries().get(i).getKeys().get(j).getValues();
					fw.write(i+",");
					fw.write(j+",");
					for(int n=0; n<values.length;n++){
						fw.write(values[n]+",");
					}
					fw.write("\n");
				}
				try {
					result.write(String.valueOf(DistanceTest.test(currentSession.getPasswordTries().get(i).toKeyStrokeSet(), currentSession.getAccount(),result,k))+",");
					result.write(String.valueOf(CosineTest.test(currentSession.getPasswordTries().get(i).toKeyStrokeSet(), currentSession.getAccount(),result,k))+",");
					result.write(String.valueOf(GaussTest.test(currentSession.getPasswordTries().get(i).toKeyStrokeSet(), currentSession.getAccount(),k))+",");

					result.write("\n");
				} catch (BadLoginException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			fw.flush();
			fw.close();
			result.flush();
			result.close();
		}

		prevSessions.add(new Session(currentSession,this));
		System.out.println("Ending current session : " + currentSession.isSuccess());

		
	}
	
	// créé une nouvelle session en terminant la précédante si elle existe.
	public void newSession(int n) throws IOException{
		if(currentSession!=null){
			endCurrentSession(n);
		}
		System.out.println("New Session");
		currentSession = new Session(this);
		//currentSession.getTimeUpdater().start();
	}
	
	public Session getCurrentSession() {
		return currentSession;
	}

	public void setCurrentSession(Session currentSession) {
		this.currentSession = currentSession;
	}

	public ArrayList<Session> getPrevSessions() {
		return prevSessions;
	}

	public void setPrevSessions(ArrayList<Session> prevSessions) {
		this.prevSessions = prevSessions;
	}

}
