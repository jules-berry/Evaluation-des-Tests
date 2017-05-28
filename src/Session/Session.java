package Session;

import java.awt.im.InputContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import Main.Account;
import Main.PasswordTry;

public class Session {
	
	private Date debutTime; // date de début de la session
	private Date currentTime; // temps actuel de la session
	private Date shceduledEnd; // date de la fin prévue de la session
	private TimeUpdater timeUpdater; //Thread qui met à jour le temps de la session toutes les 5 secondes.
	private boolean running; //true si la session est en cours
	private SessionManager manager; // référence au manager
	private Account account;
	private boolean success;
	private String local;
	

	private ArrayList <PasswordTry> passwordTries; // liste des essais de mot de passe ayant eu lieu durant le tamps de la session
	
	
	public Session(SessionManager manager){
		this.manager = manager;
		debutTime = new Date(System.currentTimeMillis());
		currentTime = new Date(System.currentTimeMillis());
		shceduledEnd = new Date (debutTime.getTime()+(long)(60*1000)); // fin de la session prévue 10 min après le début
		running = true;
		timeUpdater = new TimeUpdater(this);
		passwordTries = new ArrayList<PasswordTry>();
		InputContext ic = InputContext.getInstance();
		local = ic.getLocale().getLanguage()+","+ic.getLocale().getCountry();
	}
		
	
	// constructeur générant une copie d'une session existante
	public Session (Session s, SessionManager manager){
		this.manager = manager;
		debutTime = new Date (s.getDebutTime().getTime());
		currentTime = new Date (s.getCurrentTime().getTime());
		shceduledEnd = new Date (s.getShceduledEnd().getTime());
		running = s.getRunning();
		
	}
	
	// vérifie si il faut terminer la session
	public void checkEnd(){
		if(currentTime.compareTo(shceduledEnd)>=0){
			System.out.println("ending session");
			try {
				manager.newSession();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// ajoute un essai de mot de passe et termine la session si l'essai est un succes
	public void addPasswordTry(PasswordTry passwordTry){
		passwordTries.add(passwordTry);
		if(passwordTry.isSuccess()){
			try {
				manager.newSession();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Essai ajouté");
	}
	
	public void reshceduleEnd(){
		shceduledEnd = new Date(shceduledEnd.getTime() + (long)(20*1000));
		System.out.println("End of session reshceduled");
	}

	public Date getDebutTime() {
		return debutTime;
	}

	public void setDebutTime(Date debutTime) {
		this.debutTime = debutTime;
	}

	public Date getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}

	public Date getShceduledEnd() {
		return shceduledEnd;
	}

	public void setShceduledEnd(Date shceduledEnd) {
		this.shceduledEnd = shceduledEnd;
	}

	public TimeUpdater getTimeUpdater() {
		return timeUpdater;
	}

	public void setTimeUpdater(TimeUpdater timeUpdater) {
		this.timeUpdater = timeUpdater;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public boolean getRunning(){
		return running;
	}

	public ArrayList <PasswordTry> getPasswordTries() {
		return passwordTries;
	}

	public void setPasswordTries(ArrayList <PasswordTry> passwordTries) {
		this.passwordTries = passwordTries;
	}
	

	public SessionManager getManager() {
		return manager;
	}

	public void setManager(SessionManager manager) {
		this.manager = manager;
	}


	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}


	public Account getAccount() {
		return account;
	}


	public void setAccount(Account account) {
		this.account = account;
	}
}
