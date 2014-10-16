package fr.free.simon.jacquemin.staupe.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

public class UtilsGrille {
	private int largeur;
	private int hauteur;
	private UtilsCase[][] grille;
	private UtilsCase[][] grilleArchive;
	private Activity parent;
	private Context ctx;

	public UtilsGrille(int hauteur, int largeur, Activity parent, Context ctx) {
		this.largeur = largeur;
		this.hauteur = hauteur;
		this.parent = parent;
		this.ctx = ctx;
		
		setGrille(new UtilsCase[this.hauteur][this.largeur]);
		grilleArchive = new UtilsCase[this.hauteur][this.largeur];
		
		for (int i = 0; i < this.hauteur; i++) {
			for (int j = 0; j < this.largeur; j++) {
				grilleArchive[i][j] =  new UtilsCase(1, this.parent, this.ctx);
				getGrille()[i][j] = new UtilsCase(1, this.parent, this.ctx);
			}
		}
	}

	public UtilsCase getCase(int hauteur, int largeur) {
		return getGrille()[hauteur][largeur];
	}
	
	public UtilsCase getCaseArchive(int hauteur, int largeur) {
		return grilleArchive[hauteur][largeur];
	}
	
	public int displayXTaupe(UtilsTaupe t, int nbTaupeToDisplay) {
		int nbTaupeCanFit = 0;
		ArrayList<Integer> listTaupe = new ArrayList<Integer>();
		
		t.setOriginaleTaupe(true);
		
		// Compter le nombre de taupe encore possible
		for (int i = 0; i < hauteur; i++) {
			for (int j = 0; j < largeur; j++) {
				// Pour chaque case de la grille
				for (int r = 0; r < 4; r++) {
					if (taupeCanFit(t, i, j, getGrille(), hauteur, largeur) == true) {
						nbTaupeCanFit++;
					}
					// Rotation de la taupe
					t.setForme(t.rot90Hor(), false);
				}
			}
		}
		
		if(nbTaupeCanFit == 0){
			return 0;
		}
		
		// Choisir les taupes à afficher
		if(nbTaupeToDisplay >= nbTaupeCanFit){
			for(int i = 0; i < nbTaupeCanFit; i++){
				listTaupe.add(i);
			}
		} else {
			List<Integer> l = new ArrayList<Integer>();
			for(int i = 0; i < nbTaupeCanFit; i++){
				l.add(i);
			}
			l = pickRandom(l, nbTaupeToDisplay);
			for(int i = 0; i < l.size(); i++){
				listTaupe.add(l.get(i));
			}
		}
		
		// Afficher les taupes
		int idTaupe = 0;
		for (int i = 0; i < hauteur; i++) {
			for (int j = 0; j < largeur; j++) {
				// Pour chaque case de la grille
				for (int r = 0; r < 4; r++) {
					if (taupeCanFit(t, i, j, getGrille(), hauteur, largeur) == true) {
						
						if(listTaupe.contains(idTaupe)){
							for (int iTaupe = 0; iTaupe < t.getHauteur(); iTaupe++) {
								for (int jTaupe = 0; jTaupe < t.getLargeur(); jTaupe++) {
									if(t.getCase(iTaupe, jTaupe) == 1){
										getGrille()[i + iTaupe][j + jTaupe].setState(5);
									}
									
								}
							}
						}
						idTaupe++;
					}
					// Rotation de la taupe
					t.setForme(t.rot90Hor(), false);
				}
			}
		}
		
		t.setOriginaleTaupe(false);
		
		return Math.min(nbTaupeCanFit, nbTaupeToDisplay);
	}
	
	public void clearTaupe(){
		for (int i = 0; i < hauteur; i++) {
			for (int j = 0; j < largeur; j++) {
				if(getGrille()[i][j].getState() == 5){
					getGrille()[i][j].setState(1);
				}
			}
		}
	}
	
	public List<Integer> pickRandom(List<Integer> array, int number) {
	    Collections.shuffle(array);
	    array = array.subList(0, number);
	    Collections.sort(array);
	    return array;
	}

	public boolean verifGrille(UtilsTaupe t) {
		// Si taupe.possibilte == true => return false
		
		t.setOriginaleTaupe(true);
		
		// Algo de vérification de la grille
		for (int i = 0; i < hauteur; i++) {
			for (int j = 0; j < largeur; j++) {
				// Pour chaque case de la grille
				boolean hasFindSolution = false;
				for (int r = 0; r < 4; r++) {
					if (taupeCanFit(t, i, j, getGrille(), hauteur, largeur) == true) {
						hasFindSolution = true;
					}
					// Rotation de la taupe
					t.setForme(t.rot90Hor(), false);
				}

				if (hasFindSolution == true) {
					return false;
				}

			}
		}
		
		t.setOriginaleTaupe(false);
		
		return true;
	}

	private boolean taupeCanFit(UtilsTaupe t, int x, int y, UtilsCase[][] g, int h, int l) {
		boolean canFit = true;
				
		for (int iTaupe = 0; iTaupe < t.getHauteur(); iTaupe++) {
			for (int jTaupe = 0; jTaupe < t.getLargeur(); jTaupe++) {
				if (x + iTaupe >= h || y + jTaupe >= l) {
					// La case actuelle de la taupe est en dehors de la grille
					canFit = false;
				} else {
					// La case actuelle de la taupe est dans la grille
					if (t.getCase(iTaupe, jTaupe) == 0) {
						// La case actuelle de la taupe est vide
					} else {
						if (g[x + iTaupe][y + jTaupe].getState() != 1 && g[x + iTaupe][y + jTaupe].getState() != 3 && g[x + iTaupe][y + jTaupe].getState() != 5) {
							canFit = false;
						}
					}
				}
			}
		}
		
		return canFit;
	}

	public int findBestSolution(UtilsTaupe t) {
		UtilsCase[][] field = new UtilsCase[hauteur][largeur];
		
		t.setOriginaleTaupe(true);
		
		// On recopie la grille initiale
		for(int i = 0; i < hauteur; i++){
			for(int j = 0; j < largeur; j++){
				field[i][j] = new UtilsCase(grilleArchive[i][j].getState(), this.parent, this.ctx);
			}
		}

		// Algo de vérification de la grille
		for (int i = 0; i < hauteur; i++) {
			for (int j = 0; j < largeur; j++) {
				// Pour chaque case de la grille
				boolean hasFindSolution = false;
				for (int r = 0; r < 4; r++) {
					if (taupeCanFit(t, i, j, field, hauteur, largeur) == true) {
						hasFindSolution = true;
					}
					// Rotation de la taupe
					t.setForme(t.rot90Hor(), false);
				}

				if (hasFindSolution == true) {
					field[i][j].setState(1);
				} else {
					field[i][j].setState(3);
				}
			}
		}
		
		for (int j = largeur - 1; j >= 0; j--) {
			for (int i = hauteur - 1; i >= 0; i--) {
				// Pour les cases contenant un 1 en partant de la case en bas à
				// droite et en remontant
				if(field[i][j].getState() == 1){
					boolean hasFindSolution = false;
					for (int r = 0; r < 4; r++) {
						if (taupeCanFit(t, i, j, field, hauteur, largeur) == true) {
							hasFindSolution = true;
						}
						// Rotation de la taupe
						t.setForme(t.rot90Hor(), false);
					}

					if (hasFindSolution == true) {
						field[i][j].setState(4);
					} else {
						field[i][j].setState(3);
					}
				}
			}
		}
		
		t.setOriginaleTaupe(false);
		
		// On compte le nombre de mine posé (case = 4)
		return countNbMine(field, 4);
	}
	
	public int countNbMine(UtilsCase[][] field, int idMine){
		int nbMine = 0;
		
		for(int i = 0; i < hauteur; i++){
			for(int j = 0; j < largeur; j++){
				if(field[i][j].getState() == idMine){
					nbMine++;
				}
			}
		}
		
		return nbMine;
	}

	public UtilsCase[][] getGrille() {
		return grille;
	}

	public void setGrille(UtilsCase[][] grille) {
		this.grille = grille;
	}
}