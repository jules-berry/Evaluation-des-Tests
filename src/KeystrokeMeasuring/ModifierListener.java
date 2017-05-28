package KeystrokeMeasuring;

import java.awt.event.KeyEvent;

public class ModifierListener extends KeyStrokeListener {
	private int location;
	
	public ModifierListener(long downTime, KeyEvent e){
		super(downTime,e);
		switch(e.getKeyLocation()){
			case KeyEvent.KEY_LOCATION_RIGHT: setLocation(1);
			break;
			case KeyEvent.KEY_LOCATION_LEFT: setLocation(-1);
			break;
		}
	}
	
	public void initLocation(){
		int keyLocation = super.getE().getKeyLocation();
		if(keyLocation == KeyEvent.KEY_LOCATION_LEFT){
			location = -1;
		}else if (keyLocation == KeyEvent.KEY_LOCATION_RIGHT){
			location = 1;
		}
	}


	public int getLocation() {
		return location;
	}


	public void setLocation(int location) {
		this.location = location;
	}
}
