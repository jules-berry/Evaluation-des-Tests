package KeystrokeMeasuring;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class KeyStrokeListener implements KeyListener {

	private KeyEvent e;
	private long downTime;
	private long upTime;
	
	public KeyStrokeListener (long downTime, KeyEvent e){
		this.setDownTime(downTime);
		this.e=e;
	}
	
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == e.getKeyCode())
			this.setUpTime(System.nanoTime());
	}

	
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

	
	public KeyEvent getE(){
		return e;
	}

	public long getDownTime() {
		return downTime;
	}

	public void setDownTime(long downTime) {
		this.downTime = downTime;
	}

	public long getUpTime() {
		return upTime;
	}

	public void setUpTime(long upTime) {
		this.upTime = upTime;
	}
	
	

}
