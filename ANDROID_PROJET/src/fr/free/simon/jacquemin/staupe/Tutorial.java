package fr.free.simon.jacquemin.staupe;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fr.free.simon.jacquemin.staupe.utils.SGMMath;
import fr.free.simon.jacquemin.staupe.utils.UtilsGrille;
import fr.free.simon.jacquemin.staupe.utils.UtilsLevel;
import fr.free.simon.jacquemin.staupe.utils.UtilsTaupe;

public class Tutorial extends SGMScreenInterface {
	UtilsTaupe taupe;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);
		init();
		UtilsLevel l = new UtilsLevel();
		l.height = 2;
		l.width = 2;
		ArrayList<ArrayList<Integer>> niveau = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i < 2; i++){
			niveau.add(new ArrayList<Integer>());
			for(int j = 0; j < 2; j++){
				niveau.get(i).add(1);
			}
		}
		l.rep = niveau;
		displayLevel(l, (GridLayout) findViewById(R.id.tuto_grille_p1_jeu));
		
		taupe = new UtilsTaupe();
		int i[][] = {{1,0},{1,0}};
		taupe.setForme(i, true);
		displayTaupe(taupe, (GridLayout) findViewById(R.id.tuto_grille_p2_taupe));
	}
	
	private void displayLevel(UtilsLevel l, GridLayout grille) {
		int hauteur = l.height;
		int largeur = l.width;
		UtilsGrille grilleActuelle = new UtilsGrille(hauteur, largeur, this,
				getApplicationContext());
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.tuto_rl_anim);
		grille.removeAllViews();
		grille.setColumnCount(largeur);

		int A = 0;
		int B = 0;

		if (Configuration.ORIENTATION_LANDSCAPE == getResources()
				.getConfiguration().orientation) {
			A = metrics.widthPixels / (largeur * 4 + 2);
			B = metrics.heightPixels / (hauteur * 2 + 2);
		} else {
			A = metrics.widthPixels / (largeur * 2 + 1);
			B = metrics.heightPixels / (hauteur * 4 + 2);
		}

		int size = (A < B) ? (A) : (B);

		for (int i = 0; i < hauteur; i++) {
			for (int j = 0; j < largeur; j++) {
				grilleActuelle.getCaseArchive(i, j).setState(
						l.rep.get(i).get(j));
				grilleActuelle.getCase(i, j).setState(l.rep.get(i).get(j));
				grilleActuelle.getCase(i, j).setImgBtn(
						new ImageButton(getApplicationContext()),
						grilleActuelle, rl);
				grilleActuelle.getCase(i, j).getImgBtn()
						.setLayoutParams(new LayoutParams(size, size));
				grille.addView(grilleActuelle.getCase(i, j).getImgBtn());
			}
		}
	}
	
	public void displayTaupe(UtilsTaupe taupe, GridLayout grilleTaupe) {
		grilleTaupe.removeAllViews();

		int A = 0;
		int B = 0;

		if (Configuration.ORIENTATION_LANDSCAPE == getResources()
				.getConfiguration().orientation) {
			A = metrics.widthPixels / (4 * taupe.getLargeur() + 2);
			B = metrics.heightPixels / (2 * taupe.getHauteur() + 2);
		} else {
			A = metrics.widthPixels / (2 * taupe.getLargeur() + 1);
			B = metrics.heightPixels / (4 * taupe.getHauteur() + 2);
		}

		int size = (A < B) ? (A) : (B);

		grilleTaupe.setColumnCount(taupe.getLargeur());
		for (int i = 0; i < taupe.getHauteur(); i++) {
			for (int j = 0; j < taupe.getLargeur(); j++) {
				ImageView img = new ImageView(getApplicationContext());
				img.setLayoutParams(new LayoutParams(size, size));
				if (taupe.getForme()[i][j] == 0) {
					img.setBackgroundResource(R.drawable.case_none);
				} else {
					img.setBackgroundResource(getResources().getIdentifier("case_taupe"+SGMMath.randInt(1, 5), "drawable", getApplicationContext().getPackageName()));
				}
				grilleTaupe.addView(img);
			}
		}
	}
	
	public UtilsTaupe actionTurn(View v, GridLayout grille, UtilsTaupe taupe) {
		int[][] f = taupe.rot90Hor();
		taupe.setForme(f, true);
		displayTaupe(taupe, grille);
		return taupe;
	}

	@Override
	public void actionClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back_tutorial:
			endActivity("Back");
			break;
		case R.id.tuto_grille_p2_taupe:
			taupe = actionTurn(v, (GridLayout) findViewById(R.id.tuto_grille_p2_taupe), taupe);
			break;
		}
	}

    @Override
    public String getNameActivity() {
        return "Tutorial";
    }

    protected void init() {
		super.init();

		((TextView) findViewById(R.id.tuto_msg_p0_global)).setTypeface(font);
		((TextView) findViewById(R.id.tuto_msg_p1_jeu)).setTypeface(font);
		((TextView) findViewById(R.id.tuto_msg_p2_taupe)).setTypeface(font);
		((TextView) findViewById(R.id.tuto_msg_p3_bonus)).setTypeface(font);
		((TextView) findViewById(R.id.tuto_msg_p4_level)).setTypeface(font);
		((TextView) findViewById(R.id.tuto_msg_p5_level)).setTypeface(font);
		((TextView) findViewById(R.id.tuto_msg_p6)).setTypeface(font);
		((Button) findViewById(R.id.btn_back_tutorial)).setTypeface(font);
	}

}
