package KeystrokeMeasuring;

import java.awt.event.KeyEvent;

public class CharacterListener extends KeyStrokeListener{
	private boolean shift;
	private boolean ctrl;
	private boolean alt;
	private boolean altGraph;
	private boolean capsLock;
	private int modifiersCounter;

	public CharacterListener(long downTime, KeyEvent e, boolean capsLock){
		super(downTime, e);
		this.setShift(e.isShiftDown());
		this.setCtrl(e.isControlDown());
		this.setAlt(e.isAltDown());
		this.setAltGraph(e.isAltGraphDown());
		this.setCapsLock(capsLock);
		this.setModifiersCounter(0);
	}
	
	public boolean isShift() {
		return shift;
	}

	public void setShift(boolean shift) {
		this.shift = shift;
		if(shift)
			modifiersCounter++;
	}

	public boolean isCtrl() {
		return ctrl;
	}

	public void setCtrl(boolean ctrl) {
		this.ctrl = ctrl;
		if(ctrl)
			modifiersCounter++;
	}

	public boolean isAlt() {
		return alt;
	}

	public void setAlt(boolean alt) {
		this.alt = alt;
		if(alt)
			modifiersCounter++;
	}

	public boolean isCapsLock() {
		return capsLock;
	}

	public void setCapsLock(boolean capsLock) {
		this.capsLock = capsLock;
		if(capsLock)
			modifiersCounter++;
	}

	public int getModifiersCounter() {
		return modifiersCounter;
	}

	public void setModifiersCounter(int modifiersCounter) {
		this.modifiersCounter = modifiersCounter;
	}

	public boolean isAltGraph() {
		return altGraph;
	}

	public void setAltGraph(boolean altGraph) {
		this.altGraph = altGraph;
		if(altGraph)
			modifiersCounter++;
	}

}