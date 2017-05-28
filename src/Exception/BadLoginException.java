package Exception;

import Warnings.SimpleWarning;

@SuppressWarnings("serial")
public class BadLoginException extends Exception{
	
	public BadLoginException(){
		new SimpleWarning("Identifiants ou mot de passe incorrecte");

	}

}
