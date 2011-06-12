

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Qualification {

	static String DEFAULT_IN = "src/input2.txt";
	static String DEFAULT_OUT = "src/output2.txt";
	
	BufferedReader reader;
	BufferedWriter writer;
	
	public Qualification(String in, String out) throws IOException {
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(in)));
		writer = new BufferedWriter(new FileWriter(out));
	}
	
	
	/*
	 * Les variables sont r�utilis�es et d�clar�e dans le scope classe
	 * pour pouvoir �tre utilis�e dans les fonctions helper de classe
	 * cf. writeList
	 */
	int[] liste = new int[64];
	int N;
	boolean[] moved = new boolean[64];
	StringBuffer out;
	
	public void solveAll() throws NumberFormatException, IOException {

		while((N = Integer.valueOf(reader.readLine()))!=0) {
			
			/*
			 * Chargement des donn�es, et calcul de la moyenne � obtenir,
			 * On utilise la congruence de moyenne pour ne travailler qu'avec
			 * des valeurs enti�re.
			 */
			String[] in = reader.readLine().split(" ");
			reader.readLine();
			int moyenne = 0;
			for(int i=0; i<N; i++) {
				int j = Integer.valueOf(in[i]);
				moyenne += j;
				liste[i] = j;
			}
			
			if(moyenne % N != 0) {
				writer.write("-1\n\n");
				continue;
			}
			
			moyenne /= N;
			
			int k = 0; // Est utilis� pour afficher le num�ro de l'�tape
			boolean solved = false;
			
			out = new StringBuffer();
			
			/*
			 * Tant que le probl�me n'est pas r�solu, on it�re.
			 */
			while(!solved) {
				solved = true;
				writeList(k++);
				
				int diff = 0;
				
				/*
				 * Lecture et travail de gauche � droite, on retient si le i�me �l�ment �
				 * envoy� son unit� ou non dans moved[]
				 */
				for(int i=0; i<N; i++) {
					moved[i] = false;
				}
				
				for(int i=0; i<N-1; i++) {
					
					/*
					 * diff stoke le diff�rentiel entre le "poids" du bloc parcourru � gauche
					 * et le "poids" qu'il devrait avoir. Cela permet de d�cider entre les 
					 * deux cas ci apr�s.
					 */
					diff += liste[i] - moyenne;
					
					if(liste[i]!=moyenne) solved = false;
				
					/*
					 * Si le bloc de gauche est trop "l�ger" on rajoute de la masse en la prenant � droite
					 * (ce qui est toujours autoris�, car l'�l�ment � droite n'a jamais �t� touch� pdt cette
					 * it�ration.
					 */
					if(diff < 0 && liste[i+1]>0) {
						liste[i]++;
						liste[i+1]--;
						diff++;
						moved[i+1] = true;
						continue;
					}
					
					/*
					 * Dans le cas on l'on doit envoyer de la masse � droite, on v�rifie d'abords que l'�l�ment
					 * courant n'a pas c�d� son unit� de poids.
					 */
					if(diff > 0 && liste[i]>0 && !moved[i]) {
						liste[i]--;
						liste[i+1]++;
						diff--;
						continue;
					}
				}
			}
			writer.write(--k+"\n");
			writer.write(out.toString());
			writer.write("\n");
		}
		writer.close();
		
	}
	
	void writeList(int k) throws IOException {
		out.append(k).append(" : (").append(liste[0]);
		for(int i=1;i<N;i++) {
			out.append(", ").append(liste[i]);
		}
		out.append(")\n");
	}
	
	
	public static void main(String args[]) throws IOException {
		new Qualification(DEFAULT_IN, DEFAULT_OUT).solveAll();
	}
	
}
