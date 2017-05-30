package GUIElements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CancelButton extends JButton implements ActionListener{
	
	private JPanel prevPane;
	private JPanel currentPane;
	
	public CancelButton(JPanel prev , JPanel current){
		super("Annuler");
		prevPane = prev;
		currentPane = current;
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		currentPane.setVisible(false);
		prevPane.setVisible(true);
		
	}

}
