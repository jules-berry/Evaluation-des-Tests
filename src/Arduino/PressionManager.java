package Arduino;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import KeystrokeMeasuring.TimingManager;
import jssc.SerialPortException;

public class PressionManager implements Runnable {

	private ArrayList<Double> tabTriee;
	private LinkedList<Mesure> tabMesures;
	private ArduinoUsbChannel vcpChannel;
	private final TimingManager tm;
	private boolean stop;
	private boolean end;
	private boolean triee;
	private boolean wait;
	private boolean connected;

	public PressionManager(TimingManager tm) {

		setStop(false);
		setEnd(false);
		setTriee(false);
		setWait(true);
		
		this.tm = tm;

		String port = null;

		// Recherche du port de l'Arduino

		System.err.println("RECHERCHE d'un port disponible...");
		port = ArduinoUsbChannel.getOneComPort();
		//TODO mettre la methode de recherche de port dans TimingManager
		if (port != null) {
			try {
				vcpChannel = new ArduinoUsbChannel(port);
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		} else {
			System.out.println("Impossible de se connecter au module de mesure de pressions, poursuite du programme "
					+ "sans mesure de pressions");
			tm.setArduinoConnected(false);
			this.setEnd(true);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException ex) {
			}
		}
	}

	@Override
	public void run() {
		
		BufferedReader vcpInput = null;
		
		try {
			vcpChannel.open();
		} catch (SerialPortException | IOException e1) {
			e1.printStackTrace(System.err);
			
		}

		tabMesures = new LinkedList<Mesure>(); // mesures
												// brutes de
												// pression

		vcpInput = new BufferedReader(new InputStreamReader(vcpChannel.getReader()));

		while (!stop && tm.isArduinoConnected()) {

			try {

				// Attend le debut de la lecture des donnees par le clavier
				System.err.println("Waiting TimingManager!");
				while (wait) {
					synchronized (this) {
						try {
							wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				System.err.println("Done waiting TimingManager!");

				setTriee(false);

				System.err.println("Entree boucle de lecture des pressions");
				
				while (!end) {
					try{
						String line;
	
						if ((line = vcpInput.readLine()) != null) {
							insertionTab(line);
							System.out.println("Data from Arduino: " + line);
						}
				}catch(InterruptedIOException e){}

				}

				System.err.println("Sortie boucle de lecture des pressions");

				triTab();

				setWait(true);

			} catch (IOException e) {
				e.printStackTrace(System.err);
			}

		}

		try {
			vcpInput.close();
			vcpChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
		}

	}

	public boolean isWait() {
		return wait;
	}

	public void setWait(boolean wait) {
		this.wait = wait;
	}

	public void insertionTab(String s) {

		String[] temp = s.split("_");

		if (temp.length == 3) {

			char ident = temp[0].charAt(0);
			double p = Double.parseDouble(temp[1]);
			int cmpt = Integer.parseInt(temp[2]);

			tabMesures.add(new Mesure(cmpt, p, ident));

			System.err.println(s + " inseree");

		}
	}

	public void triTab() {

		System.err.println("Debut du tri des mesures");

		Iterator<Mesure> mesuresIter = tabMesures.iterator();
		LinkedList<Double> m = new LinkedList<Double>();

		int cmpt = tabMesures.getFirst().compt;
		double pres = tabMesures.getFirst().pression;
		char ident = tabMesures.getFirst().id;

		while (mesuresIter.hasNext()) {
			Mesure tempMesure = mesuresIter.next();
			if (tempMesure.compt == cmpt && tempMesure.id == ident) {
				if (tempMesure.pression >= pres) {
					pres = tempMesure.pression;
				}
			} else {
				m.add(pres);
				ident = tempMesure.id;
				pres = tempMesure.pression;
				cmpt = tempMesure.compt;
			}
		}

		m.add(pres);

		setTabTriee(new ArrayList<Double>(m));

		System.err.println("Tableau des mesures trie");

		setTriee(true);

		tm.resume();
		
		tabMesures.clear();

	}

	public void afficherTabTriee() {

		for (Double m : tabTriee)
			System.out.println(m);

	}

	public void close() {
		setStop(true);
	}

	public synchronized void resume() {
		setWait(false);
		notify();
	}

	public ArrayList<Double> getTabTriee() {
		return tabTriee;
	}

	public void setTabTriee(ArrayList<Double> tabTriee) {
		this.tabTriee = tabTriee;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public boolean isTriee() {
		return triee;
	}

	public void setTriee(boolean triee) {
		this.triee = triee;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	
}
