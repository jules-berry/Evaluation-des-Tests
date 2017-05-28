package KeystrokeMeasuring;

public class ModifierSequence{
	
	/* Contient juste la méthode getSequence(int[]) qui permet de retourner la séquence 
	 * de modifieurs codée sous la forme d'un entier compris entre 0 et 64 (-1 en cas d'erreur) à partir 
	 * d'un tableau d'entiers de taille 4 où chaque case carrespond à l'utilisation d'un modifieur 
	 * - 1 pour majuscule
	 * - 2 pour control
	 * - 3 pour alt;
	 * - 4 pour capslock
	 * - 0 pour les emplacements non utilisées
	 * La fin d'une séquence est détectée par la présence d'un 0.
	 * L'entier qui est retourné correspond au numéro de ligne associé à la séquence dans le fichier 'liste_combinaisons.txt'.
	 */
	
	private final static int MAJ = 1;
	private final static int CTRL = 2;
	private final static int ALT = 3;
	private final static int CAPSLOCK = 4;
	private final static int NULL = 0;
	
	public static int getSequence(int[]modifieurs){
		
		switch (modifieurs[0]){
		case NULL:
			return 0;
			
			
		case MAJ :
			switch (modifieurs[1]){
			case NULL:
				return 1;
				
				
			case CTRL:
				switch (modifieurs[2]){
				case NULL:
					return 5;
					
					
				case ALT :
					switch (modifieurs[3]){
					case NULL :
						return 17;
						
					
					case CAPSLOCK :
						return 41;
					
					}
					
				case CAPSLOCK :
					switch (modifieurs[3]){
					case NULL:
						return 18;
					
					
					case ALT :
						return 42;
						
					}
					
				}
			case ALT :
				switch (modifieurs[2]){
				case NULL:
					return 6;
					
					
				case CTRL :
					switch (modifieurs[3]){
					case NULL:
						return 19;
					
						
					case CAPSLOCK :
						return 43;
					}
					
					
				case CAPSLOCK :
					switch (modifieurs[3]){
					case NULL :
						return 20;
						
						
					case CTRL :
						return 44;
					
					}
					
				}
			case CAPSLOCK :
				switch (modifieurs[2]){
				case NULL:
					return 7;
			
					
				case CTRL :
					switch (modifieurs[3]){
					case NULL :
						return 21;
					
						
					case ALT :
						return 45;
						
					}
					
				case ALT :
					switch(modifieurs[3]){
					case NULL:
						return 22;
						
					case CTRL :
						return 46;
					}
					
				}
			}
			
		case CTRL :
			switch (modifieurs[1]){
			case NULL:
				return 2;
				
			case MAJ:
				switch(modifieurs[2]){
				case NULL :
					return 8;
					
				case ALT:
					switch(modifieurs[3]){
					case NULL:
						return 23;
						
					case CAPSLOCK:
						return 47;
					}
					
				case CAPSLOCK :
					switch (modifieurs[3]){
					case NULL:
						return 24;
						
					case ALT:
						return 48;
					}
				}
			case ALT:
				switch (modifieurs[2]){
				case NULL:
					return 9;
				
				case MAJ:
					switch (modifieurs[3]){
					case NULL:
						return 25;
						
					case CAPSLOCK:
						return 49;
					}
				case CAPSLOCK:
					switch (modifieurs[3]){
					case NULL:
						return 26;
						
					case MAJ:
						return 50;
					}
					
					
				}
			case CAPSLOCK:
				switch (modifieurs[2]){
				case NULL:
					return 10;
					
				case MAJ:
					switch (modifieurs[3]){
					case NULL:
						return 27;
						
					case ALT:
						return 51; 
					}
					
				case ALT:
					switch (modifieurs[3]){
					case NULL:
						return 28;
						
					case MAJ:
						return 52;
					}
				}
			}
		case ALT:
			switch (modifieurs[1]){
			case NULL:
				return 3;
				
			case MAJ:
				switch(modifieurs[2]){
				case NULL:
					return 11;
					
				case CTRL:
					switch (modifieurs[3]){
					case NULL:
						return 29;
						
					case CAPSLOCK :
						return 53;
					}
				case CAPSLOCK :
					switch (modifieurs[3]){
					case NULL:
						return 30;
						
					case CTRL :
						return 54;
					}
				}
			case CTRL :
				switch (modifieurs[2]){
				case NULL:
					return 12;
					
				case MAJ :
					switch (modifieurs[3]){
					case NULL:
						return 31;
						
					case CAPSLOCK :
						return 55;
					}
				case CAPSLOCK :
					switch (modifieurs[3]){
					case NULL:
						return 32;
						
					case MAJ :
						return 56;
					}
				}
			case CAPSLOCK :
				switch (modifieurs[2]){
				case NULL:
					return 13;
					
				case MAJ:
					switch (modifieurs[3]){
					case NULL:
						return 33;
						
					case CTRL :
						return 57;
					}
				case CTRL :
					switch (modifieurs[3]){
					case NULL:
						return 34;
						
					case MAJ:
						return 58;
					}
				}
			}
		case CAPSLOCK :
			switch (modifieurs[1]){
			case NULL :
				return 4;
				
			case MAJ:
				switch (modifieurs[2]){
				case NULL:
					return 14;
					
				case CTRL:
					switch (modifieurs[3]){
					case NULL:
						return 35;
						
					case ALT:
						return 59;
					}
				case ALT:
					switch(modifieurs[3]){
					case NULL:
						return 36;
						
					case CTRL :
						return 60;
					}
				}
			case CTRL :
				switch (modifieurs[2]){
				case NULL:
					return 15;
					
				case MAJ:
					switch (modifieurs[3]){
					case NULL:
						return 37;
						
					case ALT:
						return 61;
					}
				case ALT:
					switch (modifieurs[3]){
					case NULL:
						return 38;
						
					case MAJ:
						return 62;
					}
				}
			case ALT :
				switch (modifieurs[2]){
				case NULL:
					return 16;
					
				case MAJ:
					switch (modifieurs[3]){
					case NULL:
						return 39;
						
					case CTRL :
						return 63;
					}
				case CTRL :
					switch (modifieurs[3]){
					case NULL:
						return 40;
					
					case MAJ :
						return 64;
					}
				}
			}
		default :
			return -1;
		}
	}
	
	
	
}

