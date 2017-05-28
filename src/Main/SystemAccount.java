package Main;

public class SystemAccount{
	
	private String login;
	private int sysLoginHash;

	
	public SystemAccount(String login){
		this.setLogin(login);
		this.setSysLoginHash(login.hashCode());
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public int getSysLoginHash() {
		return sysLoginHash;
	}

	public void setSysLoginHash(int sysLoginHash) {
		this.sysLoginHash = sysLoginHash;
	}

	
	

}
