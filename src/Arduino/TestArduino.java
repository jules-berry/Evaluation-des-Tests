package Arduino;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import jssc.SerialPortException;

public class TestArduino 
{
	
	private ArrayList<Mesure> tabTriee;
    
    public TestArduino(){
    	
    	final Console console = new Console();
                
        console.log( "DEBUT du programme TestArduino !.." );
        
        String port = null;
        
        do {
        
            console.log( "RECHERCHE d'un port disponible..." );
            port = ArduinoUsbChannel.getOneComPort();
            
            if (port == null) {
                console.log( "Aucun port disponible!" );
                console.log( "Nouvel essai dans 5s" );
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    // Ignorer l'Exception
                }
            }

        } while (port == null);
        
        //port = "COM3";
        
        console.println("Connection au Port " + port);
        try {

            final ArduinoUsbChannel vcpChannel = new ArduinoUsbChannel(port);

            Thread readingThread = new Thread(new Runnable() {

                @Override
				public void run() {
             
                	ArrayList<Mesure> tabMesures= new ArrayList<Mesure>(); //mesures non-traitées
                    
                    int rang=0;
                    BufferedReader vcpInput = new BufferedReader(new InputStreamReader(vcpChannel.getReader()));
                    String line;
                    
                    try {
                    	
                    	//TODO gerer l'arret de la boucle
                        while ((line = vcpInput.readLine()) != null && rang <100) {
                           insertionTab (line, tabMesures , rang);
                           rang++ ; 
                           console.println("Data from Arduino: " + line);  
                        }
                        triTab(tabMesures);
                        afficherTabTriee(); 
                        

                    } catch (IOException ex) {
                        ex.printStackTrace(System.err);
                    }
                    
                }
            });
            
            readingThread.start();
            
            vcpChannel.open();
            
            boolean exit = false;
            
            while (!exit) {
            
                String line = console.readLine("Envoyer une ligne (ou 'fin') > ");
            
                if (line.length() == 0) {
                    continue;
                }
                
                if ("fin".equals(line)) {
                    exit = true;
                    continue;
                }
                
                vcpChannel.getWriter().write(line.getBytes("UTF-8"));
                vcpChannel.getWriter().write('\n');
            
            }
            
            vcpChannel.close();
            
            readingThread.interrupt();
            try {
                readingThread.join(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace(System.err);
            }

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        } catch (SerialPortException ex) {
            ex.printStackTrace(System.err);
        }
        
        
        
    }


    public void insertionTab(String s, ArrayList<Mesure> tab,int rang){
        
    	int nbretiret=0;
        char ident='t';
        
        String pression="0";
        String compteur="0";
        
        for (int i = 0; i < s.length(); i++) {
        	
        	if(s.charAt(i)=='_')
            	nbretiret++;
            else if(nbretiret==0)
            	ident=s.charAt(i);
            if(nbretiret==1)
            	pression=pression+s.charAt(i);
            if(nbretiret==2)
            	compteur=compteur+s.charAt(i);
            
       }
        
      int cmpt=Integer.parseInt(compteur);
      double p=Double.parseDouble(pression);
      
      tab.add(rang, new Mesure(cmpt,p,ident));
    
    }
    
    
    public void triTab(ArrayList<Mesure> t){
        
    	ArrayList<Mesure> m = new ArrayList<Mesure>(); 
        
    	int cmpt=t.get(0).compt;
        double pres=t.get(0).pression;
        char ident=t.get(0).id;
        
        for(int i=0;i<t.size();i++){
            
        	if(t.get(i).compt==cmpt && t.get(i).id==ident && t.get(i).pression>=pres)
            	pres=t.get(i).pression;
            else{
                m.add(new Mesure(cmpt,pres,ident));
                ident=t.get(i).id;
                pres=t.get(i).pression;
                cmpt=t.get(i).compt;
            }
        
        }
        
        m.add(new Mesure(cmpt,pres,ident));
        
        setTabTriee(m);
    
    }
    
    
    public void afficherTabTriee (){
        
    	for (Mesure m : getTabTriee())
            System.out.println(m);
    
    }


	public ArrayList<Mesure> getTabTriee() {
		return tabTriee;
	}


	public void setTabTriee(ArrayList<Mesure> tabTriee) {
		this.tabTriee = tabTriee;
	}
}