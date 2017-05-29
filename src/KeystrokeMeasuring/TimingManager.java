package KeystrokeMeasuring;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPasswordField;

import Arduino.PressionManager;
import Main.Account;
import Main.Main;
import Main.PasswordTry;

public class TimingManager implements KeyListener {
	//Params de compte
	private Account account;
	private final JPasswordField pf;
	
	private final PressionManager pm;
	private Thread pressureThread;
	
	private boolean arduinoConnected = true;
		
	//Tableau de toutes les touches (modifiers ou caracteres)
	ArrayList<KeyStrokeListener> strokes;
	
	//Tableau des caracteres avec tous leurs parametres associes (temps, pression, modifers)
	ArrayList<KeyStroke> keyStrokes;
		
	//Params de mofiers
	boolean lShift=false, rShift=false, lCtrl=false, rCtrl=false, lAlt=false, rAlt=false, capsLock=false;
	Toolkit t;
	
	public TimingManager(Account account, JPasswordField pf){	
		this.account = account;
		this.pf=pf;
		strokes = new ArrayList<KeyStrokeListener>(2*account.getPasswordAsString().length());
		keyStrokes = new ArrayList<KeyStroke>(account.getPasswordAsString().length());
		t=Toolkit.getDefaultToolkit();
		pm=new PressionManager(this);
		pressureThread = new Thread(pm);
		pressureThread.start();
	}
	
	public TimingManager(JPasswordField pf){	
		System.out.println(Thread.currentThread());

		this.pf=pf;
		strokes = new ArrayList<KeyStrokeListener>(16);
		keyStrokes = new ArrayList<KeyStroke>(16);
		t=Toolkit.getDefaultToolkit();
		pm=new PressionManager(this);
		pressureThread = new Thread(pm);
		pressureThread.start();
	}
	
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
			pm.setEnd(true);
			pressureThread.interrupt();
			int j;
			int modifiersCount;
			int tempLocation=0;
			int[] modifiersOrder = new int[4];
			for(int i=0; i<strokes.size(); i++){
				if(strokes.get(i) instanceof CharacterListener){
					int modifiersAdded=0;
					boolean shiftNotAdded=true, ctrlNotAdded=true, altNotAdded=true, altGraphNotAdded=true, capsNotAdded=true;
					modifiersCount = ((CharacterListener)strokes.get(i)).getModifiersCounter();
					Arrays.fill(modifiersOrder, 0);
					keyStrokes.add(new KeyStroke(strokes.get(i).getE().getKeyChar(),strokes.get(i).getUpTime(),strokes.get(i).getDownTime()));
					//TODO add pressure
					//keyStrokes.get(keyStrokes.size()-1).setPressure(getPressure());
					CharacterListener cListener = (CharacterListener)strokes.get(i);
					if(modifiersCount>0){
						j=i;
						while( (j>0) 
								&& (modifiersAdded<modifiersCount) 
								&& ((cListener.isShift()&&shiftNotAdded) 
									|| (cListener.isCtrl()&&ctrlNotAdded)
									|| (cListener.isAlt()&&altNotAdded)
									|| (cListener.isAltGraph()&&altGraphNotAdded)
									|| (cListener.isCapsLock()&&capsNotAdded))){
							do{
								j--;
							}
							while(!(strokes.get(j) instanceof ModifierListener));
							
							if(strokes.get(j).getE().getKeyLocation()==KeyEvent.KEY_LOCATION_LEFT)
								tempLocation=-1;
							else if(strokes.get(j).getE().getKeyLocation()==KeyEvent.KEY_LOCATION_RIGHT)
								tempLocation=1;
							
							int keyCode = strokes.get(j).getE().getKeyCode();
							if(keyCode==KeyEvent.VK_SHIFT && cListener.isShift() && shiftNotAdded){
								keyStrokes.get(keyStrokes.size()-1).setShift(new Modifier(strokes.get(j).getUpTime(),strokes.get(j).getDownTime(),tempLocation));
								modifiersOrder[modifiersCount-modifiersAdded]=1;
								shiftNotAdded=false;
								System.out.println("shift added");
							} else if(keyCode==KeyEvent.VK_CONTROL && cListener.isCtrl() && ctrlNotAdded ){
								keyStrokes.get(keyStrokes.size()-1).setCtrl(new Modifier(strokes.get(j).getUpTime(),strokes.get(j).getDownTime(),tempLocation));
								modifiersOrder[modifiersCount-modifiersAdded]=2;
								ctrlNotAdded=false;
							} else if(keyCode==KeyEvent.VK_ALT && cListener.isAlt() && altNotAdded ){
								keyStrokes.get(keyStrokes.size()-1).setAlt(new Modifier(strokes.get(j).getUpTime(),strokes.get(j).getDownTime(),tempLocation));
								modifiersOrder[modifiersCount-modifiersAdded]=3;
								altNotAdded=false;
							} else if(keyCode==KeyEvent.VK_ALT_GRAPH && cListener.isAltGraph() && altGraphNotAdded){
								keyStrokes.get(keyStrokes.size()-1).setAlt(new Modifier(strokes.get(j).getUpTime(),strokes.get(j).getDownTime(),1));
								modifiersOrder[modifiersCount-modifiersAdded]=3;
								altGraphNotAdded=false;
							} else if(keyCode==KeyEvent.VK_CAPS_LOCK && cListener.isCapsLock() && capsNotAdded){
								keyStrokes.get(keyStrokes.size()-1).setCapsLock(new Modifier(strokes.get(j).getUpTime(), strokes.get(j).getDownTime()));
								modifiersOrder[modifiersCount-modifiersAdded]=4;								
								capsNotAdded=false;
							}
							modifiersAdded++;
							shiftNotAdded = true;
							ctrlNotAdded = true;
							altNotAdded=true;
							capsNotAdded = true;
						}							
					}
					keyStrokes.get(keyStrokes.size()-1).setModifierSequence(ModifierSequence.getSequence(modifiersOrder));
				}
			} 
				if(new String(pf.getPassword()).equals(account.getPasswordAsString()))
				Main.sessionManager.getCurrentSession().addPasswordTry(new PasswordTry(keyStrokes));

			
			if(pm!=null && arduinoConnected){
				ArrayList<Double> d = new ArrayList<Double> (pm.getTabTriee());
				System.out.println(d.size());
				for(int i=0; i<account.getPasswordAsString().length(); i++){
					double test =d.get(i);
					keyStrokes.get(i).setPressure(test);
				}
				pm.setEnd(false);
			}
				
			
			
		}else if(arg0.getKeyCode() == KeyEvent.VK_SHIFT || arg0.getKeyCode() == KeyEvent.VK_CAPS_LOCK || arg0.getKeyCode() ==  KeyEvent.VK_ALT || arg0.getKeyCode() == KeyEvent.VK_ALT_GRAPH || arg0.getKeyCode() == KeyEvent.VK_CONTROL ){
			synchronized(this){
				this.notifyAll();
			}
			strokes.add(new ModifierListener(System.nanoTime(),arg0));
			pf.addKeyListener(strokes.get(strokes.size()-1));
		}else if (arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE ||arg0.getKeyCode() == KeyEvent.VK_DELETE ){
			pm.setEnd(true);
			strokes.clear();
			keyStrokes.clear();
		}
		else{ // si ce n'est pas la touche entre, on prend en comte le caractere
			pm.setEnd(false);
			synchronized(this){
				this.notifyAll();
			}
			strokes.add(new CharacterListener(System.nanoTime(),arg0,t.getLockingKeyState(KeyEvent.VK_CAPS_LOCK)));
			pf.addKeyListener(strokes.get(strokes.size()-1));
		}
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	



	public ArrayList<KeyStrokeListener> getStrokes() {
		return strokes;
	}

	public void setStrokes(ArrayList<KeyStrokeListener> strokes) {
		this.strokes = strokes;
	}

	public ArrayList<KeyStroke> getKeyStrokes() {
		return keyStrokes;
	}

	public void setKeyStrokes(ArrayList<KeyStroke> keyStrokes) {
		this.keyStrokes = keyStrokes;
	}

	public boolean isArduinoConnected() {
		return arduinoConnected;
	}

	public void setArduinoConnected(boolean arduinoConnected) {
		this.arduinoConnected = arduinoConnected;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
}