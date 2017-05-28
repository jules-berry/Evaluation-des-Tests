package Main;

public class Account {

	private String login;
	private String domain;
	private String password;
	private int loginHash;
	private int domainHash;
	private SystemAccount sysAccount;
	
	public Account (String login,String domain, String password){
		this.setLogin(login);
		this.setDomain(domain);
		this.setPassword(password);
		this.setLoginHash(login.hashCode());
		this.setDomainHash(domain.hashCode());
		this.setSysAccount(Main.currentSystemAccount);
	}
	
	public Account (String login,String domain, char[] password){
		this.setLogin(login);
		this.setDomain(domain);
		this.setPassword(new String (password));
		this.setLoginHash(login.hashCode());
		this.setDomainHash(domain.hashCode());
		this.setSysAccount(Main.currentSystemAccount);
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPasswordAsString() {
		return password;
	}
	
	public char[] getPasswordAsArray(){
		return password.toCharArray();
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getLoginHash() {
		return loginHash;
	}

	public void setLoginHash(int loginHash) {
		this.loginHash = loginHash;
	}

	public int getDomainHash() {
		return domainHash;
	}

	public void setDomainHash(int domainHash) {
		this.domainHash = domainHash;
	}

	public SystemAccount getSysAccount() {
		return sysAccount;
	}

	public void setSysAccount(SystemAccount sysAccount) {
		this.sysAccount = sysAccount;
	}
	
	
	
}
