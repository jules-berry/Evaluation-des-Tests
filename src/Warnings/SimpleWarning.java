package Warnings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;


@SuppressWarnings("serial")
public class SimpleWarning extends JFrame implements ActionListener{
	

	private SpringLayout layout;
	
	public SimpleWarning (String message){
		setSize(300,200);
		setLocation(300,300);
		setTitle("Error");
		
		layout = new SpringLayout();
		
		JPanel mainPane = new JPanel();
		mainPane.setLayout(layout);
		
		JLabel txt = new JLabel(message);
		mainPane.add(txt);
		
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, txt, 0, SpringLayout.HORIZONTAL_CENTER, mainPane);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, txt, -30, SpringLayout.VERTICAL_CENTER, mainPane);
		
		JButton okButton = new JButton("Ok");
		mainPane.add(okButton);
		
		okButton.addActionListener(this);
		
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, okButton, 0, SpringLayout.HORIZONTAL_CENTER, mainPane);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, okButton, 30, SpringLayout.VERTICAL_CENTER, mainPane);
		
		
		okButton.requestFocus();
		okButton.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
					setVisible(false);
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		setContentPane(mainPane);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		this.setVisible(false);
	}
}
