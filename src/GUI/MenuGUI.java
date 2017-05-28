package GUI;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import Main.Account;

@SuppressWarnings("serial")
public class MenuGUI extends JFrame {
	
	public JPanel mainPane;

	JPanel requestPsswdPane; // panel permettant la recuperation d'un mot de passe associe a un compte
	public SpringLayout layout; // le layout
	// deux boutons pour choisir entre creer un compte et recuperer un mot de passe

	JButton create;
	JButton request;
	BDGUI bdGui;

	
	
	public MenuGUI () {
		
		setLocation(300,300);
		setSize(500,200);
		
		layout = new SpringLayout ();
		

		
		mainPane = new JPanel();
		mainPane.setBackground(Color.blue);
		mainPane.setLayout(layout);
		//setResizable(false);
		
		
		setContentPane(mainPane);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	

	public void initBdGui(Account account,int passwordLength){
		bdGui = new BDGUI(account,passwordLength,this);
		mainPane.add(bdGui);
		layout.putConstraint(SpringLayout.WEST, bdGui, 0, SpringLayout.WEST, mainPane);
		layout.putConstraint(SpringLayout.EAST, bdGui, 0, SpringLayout.EAST, mainPane);
		layout.putConstraint(SpringLayout.SOUTH, bdGui, 0, SpringLayout.SOUTH, mainPane);
		layout.putConstraint(SpringLayout.NORTH, bdGui, 0, SpringLayout.NORTH, mainPane);
		bdGui.setVisible(true);
		
	}
	
	public void hideBdGui(){
		bdGui.setVisible(false);
	}
	
	
	


	public JPanel getMainPane() {
		return mainPane;
	}

	public void setMainPane(JPanel mainPane) {
		this.mainPane = mainPane;
	}



	public JPanel getRequestPsswdPane() {
		return requestPsswdPane;
	}

	public void setRequestPsswdPane(JPanel requestPsswdPane) {
		this.requestPsswdPane = requestPsswdPane;
	}

	public SpringLayout getLayout() {
		return layout;
	}

	public void setLayout(SpringLayout layout) {
		this.layout = layout;
	}


	public JButton getCreate() {
		return create;
	}

	public void setCreate(JButton create) {
		this.create = create;
	}

	public JButton getRequest() {
		return request;
	}

	public void setRequest(JButton request) {
		this.request = request;
	}

	public BDGUI getBdGui() {
		return bdGui;
	}

	public void setBdGui(BDGUI bdGui) {
		this.bdGui = bdGui;
	}


	
	

	
}
