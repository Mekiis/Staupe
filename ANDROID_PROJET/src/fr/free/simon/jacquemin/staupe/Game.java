package fr.free.simon.jacquemin.staupe;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import fr.free.simon.jacquemin.staupe.SGM.SGMActivity;
import fr.free.simon.jacquemin.staupe.SGM.utils.SGMMath;
import fr.free.simon.jacquemin.staupe.container.Grid;
import fr.free.simon.jacquemin.staupe.container.Level;
import fr.free.simon.jacquemin.staupe.insects.LauncherInsect;
import fr.free.simon.jacquemin.staupe.utils.ReadLevelFile;
import fr.free.simon.jacquemin.staupe.container.Maul;

public class Game extends SGMActivity {
	private static Level actualLevel;
	private static Grid actualGrid;
	private static Maul actualMaul;
	private static ArrayList<Level> allLevel;

	private static int[] arr = null;

	private static GridLayout UIgridLevelContainer = null;
    private static ImageView UIimageViewInsectContainer = null;

    // Insects
    private static LauncherInsect insectLauncher = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

		init();
		
		decodeLevel();
		
		UIimageViewInsectContainer = createImageView();
        UIgridLevelContainer = (GridLayout) findViewById(R.id.game_grid_level);
	}

    @Override
    protected void onPause() {
        super.onPause();
        setArray(constructArrayFromGame());
        insectLauncher.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if((arr = getArray()).length > 0){
            constructGameFromArray();
        }
        insectLauncher = new fr.free.simon.jacquemin.staupe.insects.LauncherInsect(5, 7, UIimageViewInsectContainer, this, metrics);
        insectLauncher.run();
    }
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putIntArray("TAB", constructArrayFromGame());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		if (!savedInstanceState.containsKey("TAB")) {
			return;
		}

		arr = savedInstanceState.getIntArray("TAB");
		constructGameFromArray();
	}

	public void setArray(int[] array)
	{
		for(int i = 0; i < array.length; i++){
			setPref(SGMGameManager.FILE_LEVELS, SGMGameManager.STATE+"_"+ actualLevel.id+"_"+i, Integer.toString(array[i]));
		}
	}

	public int[] constructArrayFromGame(){
		int count = 0;
		for (int i = 0; i < this.actualGrid.getGrille().length; i++) {
			count += this.actualGrid.getGrille()[i].length;
		}
		int[] arr = new int[count];
		for (int i = 0; i < this.actualGrid.getGrille().length; i++) {
			for (int j = 0; j < this.actualGrid.getGrille()[i].length; j++) {
				arr[i * this.actualGrid.getGrille()[i].length + j] = this.actualGrid
						.getGrille()[i][j].getState();
			}
		}
		
		return arr;
	}

	public int[] getArray()
	{
		List<Integer> array = new ArrayList<Integer>();
        int count = 0;
        for (int i = 0; i < this.actualGrid.getGrille().length; i++) {
            count += this.actualGrid.getGrille()[i].length;
        }
		for(int i = 0; i < count; i++){
			array.add(Integer.parseInt(getPref(SGMGameManager.FILE_LEVELS, SGMGameManager.STATE+"_"+ actualLevel.id+"_"+i, "1")));
		}
		
		int[] arr = new int[array.size()];
		for(int i = 0; i < array.size(); i++){
			arr[i] = array.get(i);
		}
		
		return arr;
	}

	public void constructGameFromArray(){
		UIgridLevelContainer.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
	
					@Override
					public void onGlobalLayout() {
                        if(arr.length == 0)
                            return;

						int count = 0;
						for (int i = 0; i < actualGrid.getGrille().length; i++) {
							for (int j = 0; j < actualGrid.getGrille()[i].length; j++) {
								actualGrid.getCase(i, j).setState(
										arr[count]);
								count++;
							}
						}
	
						// unregister listener (this is important)
						UIgridLevelContainer.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	
	}

	@Override
	protected void init() {
		super.init();
		
		displayNbHint();

		((TextView) findViewById(R.id.game_tv_level_name)).setTypeface(font);
		((TextView) findViewById(R.id.game_tv_maul_shape_title)).setTypeface(font);
	}

	private void decodeLevel() {
		int levelID = -1;
		Intent intent = getIntent();
		if (intent != null) {
			// On prends l'ID du level
			levelID = intent.getIntExtra(SGMGameManager.LEVEL, -1);
		} else {
			// Si probl�me avec l'intent, on fait un back
			endActivity("Return");
		}
		// On charge tous les niveau
		allLevel = new ReadLevelFile().buildLevel(getApplicationContext(),
				"lvl.txt");
		// On recherche si l'ID corresponds � un niveau
		for (int i = 0; i < allLevel.size(); i++) {
			if (allLevel.get(i).id == levelID) {
				// Si on le trouve, on cr�� le game
				actualLevel = allLevel.get(i);
				createGame(actualLevel);
				return;
			}
		}
		// Si on le trouve pas, on fait un back
		endActivity("Return");
	}

	private void createGame(Level l) {
		int hauteur = l.height;
		int largeur = l.width;
		actualGrid = new Grid(hauteur, largeur, this,
				getApplicationContext());
		GridLayout grille = (GridLayout) findViewById(R.id.game_grid_level);
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
				actualGrid.getCaseArchive(i, j).setState(
						l.rep.get(i).get(j));
				actualGrid.getCase(i, j).setState(l.rep.get(i).get(j));
				actualGrid.getCase(i, j).setImgBtn(
						new ImageButton(getApplicationContext()),
                        actualGrid, rlAnim);
				actualGrid.getCase(i, j).getImgBtn()
						.setLayoutParams(new LayoutParams(size, size));
				grille.addView(actualGrid.getCase(i, j).getImgBtn());
			}
		}
		actualMaul = convertTaupeFromLevel(l);
		displayTaupe(actualMaul, largeur, hauteur);

		((TextView) findViewById(R.id.game_tv_level_name)).setText(l.name);
	}

	private Maul convertTaupeFromLevel(Level level) {
		Maul t = new Maul();
		int[][] f = new int[level.heightTaupe][level.widthTaupe];
		for (int i = 0; i < level.heightTaupe; i++) {
			for (int j = 0; j < level.widthTaupe; j++) {
				f[i][j] = level.taupe.get(i).get(j);
			}
		}
		t.setForme(f, true);

		return t;
	}

	private void displayTaupe(Maul taupe, int largeur, int hauteur) {
		GridLayout grilleTaupe = (GridLayout) findViewById(R.id.game_grid_maul);
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
	


	private void resetTimerInsect(){
		if(insectLauncher != null)
            insectLauncher.resetTime();
	}

	@Override
	public void actionClick(View v) {
		resetTimerInsect();
		
		switch (v.getId()) {
		case R.id.game_btn_back:
			endActivity("Return");
			break;
		case R.id.game_btn_check:
			verify();
			break;
		}
	}

    @Override
    public String getNameActivity() {
        return "Game";
    }

    public void actionTurn(View v) {
		resetTimerInsect();
		
		int[][] f = this.actualMaul.rot90Hor();
		this.actualMaul.setForme(f, true);
		displayTaupe(this.actualMaul, actualLevel.width, actualLevel.height);

		UIgridLevelContainer = (GridLayout) findViewById(R.id.game_grid_level);
		UIgridLevelContainer.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						for (int i = 0; i < actualGrid.getGrille().length; i++) {
							for (int j = 0; j < actualGrid.getGrille()[i].length; j++) {
								actualGrid.getCase(i, j).initAnim();
							}
						}

						// unregister listener (this is important)
						UIgridLevelContainer.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	}

	public void actionReset(View v) {
		resetTimerInsect();
		
		for (int i = 0; i < this.actualGrid.getGrille().length; i++) {
			for (int j = 0; j < this.actualGrid.getGrille()[i].length; j++) {
				this.actualGrid.getCase(i, j).setState(
						this.actualGrid.getCaseArchive(i, j).getState());
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
			this.actualGrid.clearTaupe();
			if (this.actualGrid.displayXTaupe(this.actualMaul, 1) == 0) {
				Toast.makeText(getApplicationContext(),
						R.string.msg_game_bonus_no_maule, Toast.LENGTH_SHORT)
						.show();
			} else {
                setPref(SGMGameManager.FILE_BONUS,
						SGMGameManager.BONUS_AFFICHE_TAUPE_NB,
						Integer.toString(nbBonus - 1));
				displayNbHint();
			}
		} else {
            Toast.makeText(getApplicationContext(),
                    R.string.msg_game_bonus_no_avaible, Toast.LENGTH_SHORT)
                    .show();
		}

	}

	private void displayNbHint() {
        int nbHint = Integer.parseInt(getPref(
                SGMGameManager.FILE_BONUS,
                SGMGameManager.BONUS_AFFICHE_TAUPE_NB,
                Integer.toString(SGMGameManager.BONUS_AFFICHE_TAUPE_DEFAULT)));
		((ImageView) findViewById(R.id.game_btn_bonus_show_taupe_nb)).setImageDrawable(GetImage(getApplicationContext(), "number_"+nbHint));
	}

    public static Drawable GetImage(Context c, String ImageName) {
        return c.getResources().getDrawable(c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName()));
    }

	private void verify() {
		boolean isGridComplete = actualGrid.verifGrille(actualMaul);
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (!isGridComplete) {
			// LOSE
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
							for (int i = 0; i < actualGrid.getGrille().length; i++) {
								for (int j = 0; j < actualGrid.getGrille()[i].length; j++) {
									actualGrid.getCase(i, j).setState(
											actualGrid.getCaseArchive(i, j)
													.getState());
								}
							}
						}
					});
		} else {
			// WIN
            int nbStars = 0;
            if (actualLevel != null) {
                nbStars = Integer.parseInt(getPref(SGMGameManager.FILE_LEVELS,
                        SGMGameManager.STARS + actualLevel.id, "0"));
            }
			// 2. Chain together various setter methods to set the dialog
			int nbStarThisRound = checkNbStars(
                    actualGrid.findBestSolution(actualMaul),
                    actualGrid.countNbMine(actualGrid.getGrille(), 2));

			// Statistics : Nb of games win
            addStatistics(SGMGameManager.STATS_NB_GAMES_WIN, "0", 1);
			// Statistics : Nb of mines
            addStatistics(SGMGameManager.STATS_ALL_MINES, "0", actualGrid.countNbMine(actualGrid.getGrille(), 2));
			// Statistics : Nb of maul blocked
            addStatistics(SGMGameManager.STATS_ALL_UNIQUE_MAUL, "0", actualMaul.getNb());

			String msgWinNbBonusShowTaupe = constructMsgHint(nbStarThisRound, nbStars);
			displayNbHint();

            // New best score
			if (nbStarThisRound > nbStars) {
				// Statistics : Nb of stars
                addStatistics(SGMGameManager.STATS_ALL_STARS, "0", - nbStars + nbStarThisRound);

				// Save : Stars for this level
                setPref(SGMGameManager.FILE_LEVELS, SGMGameManager.STARS + actualLevel.id, Integer.toString(nbStarThisRound));

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

    private String constructMsgHint(int nbStarInLevelThisRound, int nbStarsInLevel){
        String msg = "";

        int nbBonus = Integer
                .parseInt(getPref(
                        SGMGameManager.FILE_BONUS,
                        SGMGameManager.BONUS_AFFICHE_TAUPE_NB,
                        Integer.toString(SGMGameManager.BONUS_AFFICHE_TAUPE_DEFAULT)));

        if (nbBonus >= SGMGameManager.BONUS_AFFICHE_TAUPE_DEFAULT) {
            msg = " "
                    + getString(R.string.msg_game_bonus_no_gain_max);
        } else if (nbStarInLevelThisRound > nbStarsInLevel) {
            nbBonus += nbStarInLevelThisRound - nbStarsInLevel;
            if (nbBonus > SGMGameManager.BONUS_AFFICHE_TAUPE_DEFAULT) {
                nbBonus = SGMGameManager.BONUS_AFFICHE_TAUPE_DEFAULT;
            }
            setPref(SGMGameManager.FILE_BONUS,
                    SGMGameManager.BONUS_AFFICHE_TAUPE_NB,
                    Integer.toString(nbBonus));
            msg = " "
                    + getString(R.string.msg_game_bonus_gain_beat_part1)
                    + " " + (nbStarInLevelThisRound - nbStarsInLevel) + " "
                    + getString(R.string.msg_game_bonus_gain_part2);
        } else if (nbStarInLevelThisRound == 3) {
            nbBonus += 1;
            if (nbBonus > SGMGameManager.BONUS_AFFICHE_TAUPE_DEFAULT) {
                nbBonus = SGMGameManager.BONUS_AFFICHE_TAUPE_DEFAULT;
            }
            setPref(SGMGameManager.FILE_BONUS,
                    SGMGameManager.BONUS_AFFICHE_TAUPE_NB,
                    Integer.toString(nbBonus));
            msg = " "
                    + getString(R.string.msg_game_bonus_gain_max_part1)
                    + " " + 1 + " "
                    + getString(R.string.msg_game_bonus_gain_part2);

        }

        return msg;
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
}