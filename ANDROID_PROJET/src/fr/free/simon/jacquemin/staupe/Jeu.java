package fr.free.simon.jacquemin.staupe;

import java.util.ArrayList;
import java.util.Random;

import fr.free.simon.jacquemin.staupe.Accueil.LauncherInsect;
import fr.free.simon.jacquemin.staupe.utils.SGMMath;
import fr.free.simon.jacquemin.staupe.utils.SGMTimer;
import fr.free.simon.jacquemin.staupe.utils.UtilsGrille;
import fr.free.simon.jacquemin.staupe.utils.UtilsLevel;
import fr.free.simon.jacquemin.staupe.utils.UtilsReadLevelFile;
import fr.free.simon.jacquemin.staupe.utils.UtilsTaupe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Jeu extends SGMScreenInterface {
	private UtilsLevel levelActuel;
	private UtilsGrille grilleActuelle;
	private UtilsTaupe taupeActuelle;
	private ArrayList<UtilsLevel> allLevel;
	private int nbStars = 0;

	private int[] arr = null;
	private GridLayout grille;
	
	// Animation Insects
	private Context ctx = null;
	private Activity activity = null;
	private ImageView img = null;
	private LauncherInsect taskInsect = null;
	private SGMTimer timerInsect = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jeu);
		
		nameActivity = "Jeu";
		init();
		
		decodeLevel();
		if (levelActuel != null) {
			nbStars = Integer.parseInt(getPref(SGMGameManager.FILE_LEVELS,
					SGMGameManager.STARS + levelActuel.id, "0"));
		}
		
		img = (ImageView) findViewById(R.id.anim_test);
		activity = this;
		ctx = getApplicationContext();
		
		taskInsect = new LauncherInsect();
		timerInsect = new SGMTimer();
		timerInsect.execute((float) SGMMath.randInt(5, 7), false, taskInsect);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		int count = 0;
		for (int i = 0; i < this.grilleActuelle.getGrille().length; i++) {
			count += this.grilleActuelle.getGrille()[i].length;
		}
		int[] arr = new int[count];
		for (int i = 0; i < this.grilleActuelle.getGrille().length; i++) {
			for (int j = 0; j < this.grilleActuelle.getGrille()[i].length; j++) {
				arr[i * this.grilleActuelle.getGrille()[i].length + j] = this.grilleActuelle
						.getGrille()[i][j].getState();
			}
		}
		outState.putIntArray("TAB", arr);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		if (!savedInstanceState.containsKey("TAB")) {
			return;
		}

		Log.d("Simon", "Restaure instance");
		arr = savedInstanceState.getIntArray("TAB");
		grille = (GridLayout) findViewById(R.id.grid);
		grille.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						int count = 0;
						for (int i = 0; i < grilleActuelle.getGrille().length; i++) {
							for (int j = 0; j < grilleActuelle.getGrille()[i].length; j++) {
								grilleActuelle.getCase(i, j).setState(
										arr[count]);
								count++;
							}
						}

						// unregister listener (this is important)
						grille.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});

	}

	@Override
	protected void init() {
		super.init();
		
		displayNbBonusShowTaupe();

		((TextView) findViewById(R.id.tv_Level)).setTypeface(font);
		((TextView) findViewById(R.id.tv_maulShape)).setTypeface(font);
		((Button) findViewById(R.id.btn_back_game)).setTypeface(font);
		((Button) findViewById(R.id.btn_check_game)).setTypeface(font);
	}

	public void decodeLevel() {
		int levelID = -1;
		Intent intent = getIntent();
		if (intent != null) {
			// On prends l'ID du level
			levelID = intent.getIntExtra(SGMGameManager.LEVEL, -1);
		} else {
			// Si problème avec l'intent, on fait un back
			endActivity("Return");
		}
		// On charge tous les niveau
		allLevel = new UtilsReadLevelFile().buildLevel(getApplicationContext(),
				"lvl.txt");
		// On recherche si l'ID corresponds à un niveau
		for (int i = 0; i < allLevel.size(); i++) {
			if (allLevel.get(i).id == levelID) {
				// Si on le trouve, on créé le jeu
				levelActuel = allLevel.get(i);
				createGame(levelActuel);
				return;
			}
		}
		// Si on le trouve pas, on fait un back
		endActivity("Return");
	}

	private void createGame(UtilsLevel l) {
		int hauteur = l.height;
		int largeur = l.width;
		grilleActuelle = new UtilsGrille(hauteur, largeur, this,
				getApplicationContext());
		GridLayout grille = (GridLayout) findViewById(R.id.grid);
		RelativeLayout rlAnim = (RelativeLayout) findViewById(R.id.game_rl_anim);
		grille.removeAllViews();
		grille.setColumnCount(largeur);

		int A = 0;
		int B = 0;

		if (Configuration.ORIENTATION_LANDSCAPE == getResources()
				.getConfiguration().orientation) {
			A = metrics.widthPixels / (largeur * 2 + 2);
			B = metrics.heightPixels / (hauteur + 1);
		} else {
			A = metrics.widthPixels / (largeur + 1);
			B = metrics.heightPixels / (hauteur * 2 + 2);
		}

		int size = (A < B) ? (A) : (B);

		for (int i = 0; i < hauteur; i++) {
			for (int j = 0; j < largeur; j++) {
				grilleActuelle.getCaseArchive(i, j).setState(
						l.rep.get(i).get(j));
				grilleActuelle.getCase(i, j).setState(l.rep.get(i).get(j));
				grilleActuelle.getCase(i, j).setImgBtn(
						new ImageButton(getApplicationContext()),
						grilleActuelle, rlAnim);
				grilleActuelle.getCase(i, j).getImgBtn()
						.setLayoutParams(new LayoutParams(size, size));
				grille.addView(grilleActuelle.getCase(i, j).getImgBtn());
			}
		}
		taupeActuelle = convertTaupeFromLevel(l);
		displayTaupe(taupeActuelle, largeur, hauteur);

		((TextView) findViewById(R.id.tv_Level)).setText(l.name);
	}

	public UtilsTaupe convertTaupeFromLevel(UtilsLevel level) {
		UtilsTaupe t = new UtilsTaupe();
		int[][] f = new int[level.heightTaupe][level.widthTaupe];
		for (int i = 0; i < level.heightTaupe; i++) {
			for (int j = 0; j < level.widthTaupe; j++) {
				f[i][j] = level.taupe.get(i).get(j);
			}
		}
		t.setForme(f, true);

		return t;
	}

	public void displayTaupe(UtilsTaupe taupe, int largeur, int hauteur) {
		GridLayout grilleTaupe = (GridLayout) findViewById(R.id.gridTaupe);
		grilleTaupe.removeAllViews();

		int A = 0;
		int B = 0;

		if (Configuration.ORIENTATION_LANDSCAPE == getResources()
				.getConfiguration().orientation) {
			A = metrics.widthPixels / (2 * largeur + 2);
			B = metrics.heightPixels / (hauteur + 2);
		} else {
			A = metrics.widthPixels / (largeur + 1);
			B = metrics.heightPixels / (2 * hauteur + 2);
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
					img.setBackgroundResource(getResources().getIdentifier(
							"case_taupe" + SGMMath.randInt(1, 5), "drawable",
							getApplicationContext().getPackageName()));
				}
				grilleTaupe.addView(img);
			}
		}
	}
	
	public class LauncherInsect implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			new Insecte().execute(img, activity, ctx,
					metrics.heightPixels,
					-500f, metrics.widthPixels + 1000f);
			
			timerInsect = new SGMTimer();
			timerInsect.execute((float) SGMMath.randInt(5, 7), false, taskInsect);
		}
	}

	@Override
	public void actionClick(View v) {
		resetTimerInsect();
		
		switch (v.getId()) {
		case R.id.btn_back_game:
			endActivity("Return");
			break;
		case R.id.btn_check_game:
			verify();
			break;
		}
	}

	public void actionTurn(View v) {
		resetTimerInsect();
		
		int[][] f = this.taupeActuelle.rot90Hor();
		this.taupeActuelle.setForme(f, true);
		displayTaupe(this.taupeActuelle, levelActuel.width, levelActuel.height);

		grille = (GridLayout) findViewById(R.id.grid);
		grille.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						for (int i = 0; i < grilleActuelle.getGrille().length; i++) {
							for (int j = 0; j < grilleActuelle.getGrille()[i].length; j++) {
								grilleActuelle.getCase(i, j).initAnim();
							}
						}

						// unregister listener (this is important)
						grille.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	}

	public void actionReset(View v) {
		resetTimerInsect();
		
		for (int i = 0; i < this.grilleActuelle.getGrille().length; i++) {
			for (int j = 0; j < this.grilleActuelle.getGrille()[i].length; j++) {
				this.grilleActuelle.getCase(i, j).setState(
						this.grilleActuelle.getCaseArchive(i, j).getState());
			}
		}
	}

	public void actionBest(View v) {
		resetTimerInsect();
	}

	public void actionBonusShowTaupe(View v) {
		resetTimerInsect();
		
		int nbBonus = Integer.parseInt(getPref(SGMGameManager.FILE_BONUS,
				SGMGameManager.BONUS_AFFICHE_TAUPE_NB,
				Integer.toString(SGMGameManager.BONUS_AFFICHE_TAUPE_DEFAULT)));
		if (nbBonus > 0) {
			this.grilleActuelle.clearTaupe();
			if (this.grilleActuelle.displayXTaupe(this.taupeActuelle, 1) == 0) {
				Toast.makeText(getApplicationContext(),
						R.string.msg_game_bonus_no_maule, Toast.LENGTH_SHORT)
						.show();
			} else {
				setPref(SGMGameManager.FILE_BONUS,
						SGMGameManager.BONUS_AFFICHE_TAUPE_NB,
						Integer.toString(nbBonus - 1));
				displayNbBonusShowTaupe();
			}
		} else {
			// TODO Message no bonus
		}

	}

	private void resetTimerInsect(){
		if(timerInsect != null)
			timerInsect.resetTimer();
	}
	
	private void displayNbBonusShowTaupe() {
		((TextView) findViewById(R.id.game_btn_bonus_show_taupe_nb))
				.setText(getPref(
						SGMGameManager.FILE_BONUS,
						SGMGameManager.BONUS_AFFICHE_TAUPE_NB,
						Integer.toString(SGMGameManager.BONUS_AFFICHE_TAUPE_DEFAULT)));
	}

	public void verify() {
		boolean verif = grilleActuelle.verifGrille(taupeActuelle);
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (!verif) {
			// lose
			// Statistique : Nb de partie perdue
			int nbGameLost = Integer.parseInt(getPref(
					SGMGameManager.FILE_STATS,
					SGMGameManager.STATS_NB_GAMES_LOST, "0"));
			setPref(SGMGameManager.FILE_STATS,
					SGMGameManager.STATS_NB_GAMES_LOST,
					Integer.toString(nbGameLost + 1));

			// 2. Chain together various setter methods to set the dialog
			// characteristics
			builder.setMessage(R.string.dlg_loseMsg);
			builder.setTitle(R.string.dlg_loseTitle);

			// 3. Add the buttons
			builder.setNegativeButton(R.string.dlg_lose_quit,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User clicked Continue button
							endActivity("Return");
						}
					});
			builder.setNeutralButton(R.string.dlg_lose_continue,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog

						}
					});
			builder.setPositiveButton(R.string.dlg_lose_retry,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User clicked Continue button
							for (int i = 0; i < grilleActuelle.getGrille().length; i++) {
								for (int j = 0; j < grilleActuelle.getGrille()[i].length; j++) {
									grilleActuelle.getCase(i, j).setState(
											grilleActuelle.getCaseArchive(i, j)
													.getState());
								}
							}
						}
					});
		} else {
			// win
			// 2. Chain together various setter methods to set the dialog
			int nbStarThisRound = checkNbStars(
					grilleActuelle.findBestSolution(taupeActuelle),
					grilleActuelle.countNbMine(grilleActuelle.getGrille(), 2));

			// Statistique : Nb de partie gagnée
			int nbGameWin = Integer.parseInt(getPref(SGMGameManager.FILE_STATS,
					SGMGameManager.STATS_NB_GAMES_WIN, "0"));
			setPref(SGMGameManager.FILE_STATS,
					SGMGameManager.STATS_NB_GAMES_WIN,
					Integer.toString(nbGameWin + 1));
			// Statistique : Nb de mines
			int nbMineTot = Integer.parseInt(getPref(SGMGameManager.FILE_STATS,
					SGMGameManager.STATS_ALL_MINES, "0"));
			setPref(SGMGameManager.FILE_STATS,
					SGMGameManager.STATS_ALL_MINES,
					Integer.toString(nbMineTot
							+ grilleActuelle.countNbMine(
									grilleActuelle.getGrille(), 2)));
			// Statistique : Nb de taupe éliminée
			int nbTaupeUniqueTot = Integer.parseInt(getPref(
					SGMGameManager.FILE_STATS,
					SGMGameManager.STATS_ALL_UNIQUE_MAUL, "0"));
			setPref(SGMGameManager.FILE_STATS,
					SGMGameManager.STATS_ALL_UNIQUE_MAUL,
					Integer.toString(nbTaupeUniqueTot + taupeActuelle.getNb()));
			// Statistique : Nb de taupe éliminée
			int nbTaupeGroupeTot = Integer.parseInt(getPref(
					SGMGameManager.FILE_STATS,
					SGMGameManager.STATS_ALL_GROUP_MAUL, "0"));
			setPref(SGMGameManager.FILE_STATS,
					SGMGameManager.STATS_ALL_GROUP_MAUL,
					Integer.toString(nbTaupeGroupeTot + 1));

			String msgWinNbBonusShowTaupe = "";
			int nbBonus = Integer
					.parseInt(getPref(
							SGMGameManager.FILE_BONUS,
							SGMGameManager.BONUS_AFFICHE_TAUPE_NB,
							Integer.toString(SGMGameManager.BONUS_AFFICHE_TAUPE_DEFAULT)));

			if (nbBonus >= SGMGameManager.BONUS_AFFICHE_TAUPE_DEFAULT) {
				msgWinNbBonusShowTaupe = " "
						+ getString(R.string.msg_game_bonus_no_gain_max);
			} else if (nbStarThisRound > nbStars) {
				nbBonus += nbStarThisRound - nbStars;
				if (nbBonus > SGMGameManager.BONUS_AFFICHE_TAUPE_DEFAULT) {
					nbBonus = SGMGameManager.BONUS_AFFICHE_TAUPE_DEFAULT;
				}
				setPref(SGMGameManager.FILE_BONUS,
						SGMGameManager.BONUS_AFFICHE_TAUPE_NB,
						Integer.toString(nbBonus));
				msgWinNbBonusShowTaupe = " "
						+ getString(R.string.msg_game_bonus_gain_beat_part1)
						+ " " + (nbStarThisRound - nbStars) + " "
						+ getString(R.string.msg_game_bonus_gain_part2);
			} else if (nbStarThisRound == 3) {
				nbBonus += 1;
				if (nbBonus > SGMGameManager.BONUS_AFFICHE_TAUPE_DEFAULT) {
					nbBonus = SGMGameManager.BONUS_AFFICHE_TAUPE_DEFAULT;
				}
				setPref(SGMGameManager.FILE_BONUS,
						SGMGameManager.BONUS_AFFICHE_TAUPE_NB,
						Integer.toString(nbBonus));
				msgWinNbBonusShowTaupe = " "
						+ getString(R.string.msg_game_bonus_gain_max_part1)
						+ " " + 1 + " "
						+ getString(R.string.msg_game_bonus_gain_part2);

			}
			displayNbBonusShowTaupe();

			if (nbStarThisRound > nbStars) {
				// Statistique : Nb d'étoile
				int nbStarsTot = Integer.parseInt(getPref(
						SGMGameManager.FILE_STATS,
						SGMGameManager.STATS_ALL_STARS, "0"));
				setPref(SGMGameManager.FILE_STATS,
						SGMGameManager.STATS_ALL_STARS,
						Integer.toString(nbStarsTot - nbStars + nbStarThisRound));

				nbStars = nbStarThisRound;
				// Save : Stars pour ce niveau
				setPref(SGMGameManager.FILE_LEVELS, SGMGameManager.STARS
						+ levelActuel.id, Integer.toString(nbStarThisRound));

			}

			// characteristics
			builder.setMessage(getString(R.string.dlg_winMsg)
					+ msgWinNbBonusShowTaupe);
			builder.setTitle(R.string.dlg_winTitle);
			Bitmap bmOn = BitmapFactory.decodeResource(getResources(),
					R.drawable.star_on);
			Bitmap bmOff = BitmapFactory.decodeResource(getResources(),
					R.drawable.star_off);
			ArrayList<Bitmap> a = new ArrayList<Bitmap>();
			for (int j = 0; j < nbStarThisRound; j++) {
				a.add(bmOn);
			}
			for (int j = nbStarThisRound; j < 3; j++) {
				a.add(bmOff);
			}
			Drawable image = new BitmapDrawable(getResources(),
					combineImageIntoOne(a));
			builder.setIcon(image);

			// 3. Add the buttons
			builder.setPositiveButton(R.string.dlg_win_return,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User clicked OK button
							endActivity("Return");
						}
					});
			builder.setNegativeButton(R.string.dlg_win_continue,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog

						}
					});

		}
		// 4. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	// Combine Multi Image Into One
	private Bitmap combineImageIntoOne(ArrayList<Bitmap> bitmap) {
		int w = 0, h = 0;
		for (int i = 0; i < bitmap.size(); i++) {
			if (i < bitmap.size() - 1) {
				h = bitmap.get(i).getHeight() > bitmap.get(i + 1).getHeight() ? bitmap
						.get(i).getHeight() : bitmap.get(i + 1).getHeight();
			}
			w += bitmap.get(i).getWidth();
		}

		Bitmap temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(temp);
		int left = 0;
		for (int i = 0; i < bitmap.size(); i++) {
			left = (i == 0 ? 0 : left + bitmap.get(i).getWidth());
			canvas.drawBitmap(bitmap.get(i), left, 0f, null);
		}
		return temp;
	}

	private int checkNbStars(int nbMineMinimum, int nbMineActual) {
		int nbStarsThisRound = 0;
		
		if (nbMineActual <= nbMineMinimum) {
			nbStarsThisRound = 3;
		} else if (nbMineActual <= Math.round(nbMineMinimum
				+ (25.0 / 100.0 * nbMineMinimum))) {
			nbStarsThisRound = 2;
		} else if (nbMineActual <= Math.round(nbMineMinimum
				+ (50.0 / 100.0 * nbMineMinimum))) {
			nbStarsThisRound = 1;
		} else {
			nbStarsThisRound = 0;
		}

		return nbStarsThisRound;
	}
}