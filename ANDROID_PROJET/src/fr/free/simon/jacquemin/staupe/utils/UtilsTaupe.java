package fr.free.simon.jacquemin.staupe.utils;

import android.util.Log;

public class UtilsTaupe {
	private int[][] forme;
	private int[][] formeOriginelle;
	private int largeur;
	private int hauteur;

	public UtilsTaupe() {
		largeur = 0;
		hauteur = 0;
	}

	public void setFormeAle(int l, int h, boolean completeForme) {
		largeur = l;
		hauteur = h;

		setFormeVide();

		for (int i = 0; i < largeur; i++) {
			for (int j = 0; j < hauteur; j++) {
				forme[j][i] = SGMMath.randInt(0, 1);
			}
		}
		
		formeOriginelle = forme;
		
		if(completeForme){
			forme = completeForme(forme);
			calculTailleForme();
		}
	}

	public int getNb() {
		int nb = 0;

		for (int i = 0; i < largeur; i++) {
			for (int j = 0; j < hauteur; j++) {
				if (forme[j][i] == 1) {
					nb++;
				}
			}
		}

		return nb;
	}

	public void setForme(int[][] f, boolean completeForme) {
		forme = f;
		formeOriginelle = f;
		calculTailleForme();
		
		if(completeForme){
			forme = completeForme(forme);
			calculTailleForme();
		}
	}
	
	public void setOriginaleTaupe(boolean a_value){
		if(a_value){
			forme = formeOriginelle;
			calculTailleForme();
		} else {
			forme = completeForme(forme);
			calculTailleForme();
		}
	}
	
	
	public int[][] completeForme(int[][] f){
		if(f.length == 0){
			return null;
		}
		int maxTaille = Math.max(f.length, f[0].length);
		
		int[][] formeCarre = new int[maxTaille][maxTaille];
		for(int i = 0; i < f.length; i++){
			for(int j = 0; j < f[i].length; j++){
				if(i > hauteur || j > largeur){
					formeCarre[i][j] = 0;
				} else {
					formeCarre[i][j] = f[i][j];
				}
				
			}
		}
		
		return formeCarre;
	}

	public int[][] getForme() {
		return forme;
	}

	public int getLargeur() {
		return largeur;
	}

	public int getHauteur() {
		return hauteur;
	}

	private void setFormeVide() {
		forme = new int[hauteur][largeur];
		for (int i = 0; i < largeur; i++) {
			for (int j = 0; j < hauteur; j++) {
				forme[j][i] = 0;
			}
		}
	}

	private void calculTailleForme() {
		hauteur = forme.length;
		largeur = 0;
		if (hauteur != 0) {
			largeur = forme[0].length;
		}
	}

	public int getCase(int hauteur, int largeur) {
		return forme[hauteur][largeur];
	}

	public int[][] rot90Hor() {
		int[][] rotate = new int[forme[0].length][forme.length];

		for (int i = 0; i < forme[0].length; i++) {
			for (int j = 0; j < forme.length; j++) {
				rotate[i][forme.length - 1 - j] = forme[j][i];
			}
		}

		return rotate;
	}
}