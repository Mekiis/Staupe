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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import fr.free.simon.jacquemin.staupe.SGM.SGMActivity;
import fr.free.simon.jacquemin.staupe.SGM.UnlockLevel;
import fr.free.simon.jacquemin.staupe.container.data.EData;
import io.brothers.sgm.SGMStatManager;
import io.brothers.sgm.Tools.SGMMath;
import fr.free.simon.jacquemin.staupe.container.Grid;
import fr.free.simon.jacquemin.staupe.container.Level;
import fr.free.simon.jacquemin.staupe.insects.LauncherInsect;
import fr.free.simon.jacquemin.staupe.container.Maul;
import io.brothers.sgm.Unlockable.SGMUnlockManager;
import io.brothers.sgm.User.SGMUserManager;

public class InGame extends SGMActivity implements View.OnTouchListener, Grid.GridEventListener {
	private static Level actualLevel;
	private static Grid actualGrid;
	private static Maul actualMaul;

	private static int[] arr = null;

	private static GridLayout UIGridLevelContainer = null;
    private static Button UIResetGrid = null;
    private static ImageView UIImageViewInsectContainer = null;

    // Insects
    private static LauncherInsect insectLauncher = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

        decodeLevel();

		init();
	}

    @Override
    protected void onPause() {
        super.onPause();
        setArray(SGMGameManager.LVL_LAST_STATE, constructArrayFromGame());
        insectLauncher.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if((arr = getArray(SGMGameManager.LVL_LAST_STATE)).length > 0){
            constructGameFromArray();
        }

        String value = "1";
        value = getPref(SGMGameManager.FILE_OPTIONS, SGMGameManager.OPTION_ANIM_IG, value);
        insectLauncher = new fr.free.simon.jacquemin.staupe.insects.LauncherInsect(5, 7, UIImageViewInsectContainer, this, metrics, value.equals("1") );
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

	public void setArray(String key, int[] array)
	{
		for(int i = 0; i < array.length; i++){
			setPref(SGMGameManager.FILE_LEVELS, key +"_"+ actualLevel.id+"_"+i, Integer.toString(array[i]));
		}
	}

	public int[] constructArrayFromGame(){
		int count = 0;
		for (int i = 0; i < actualGrid.getGrid().length; i++) {
			count += actualGrid.getGrid()[i].length;
		}
		int[] arr = new int[count];
		for (int i = 0; i < actualGrid.getGrid().length; i++) {
			for (int j = 0; j < actualGrid.getGrid()[i].length; j++) {
				arr[i * actualGrid.getGrid()[i].length + j] = actualGrid
						.getTile(i, j).getState();
			}
		}
		
		return arr;
	}

	public int[] getArray(String key)
	{
		List<Integer> array = new ArrayList<>();
        int[] arr = new int[0];
        int count = 0;
        for (int i = 0; i < actualGrid.getGrid().length; i++) {
            count += actualGrid.getGrid()[i].length;
        }
		for(int i = 0; i < count; i++){
			array.add(Integer.parseInt(getPref(SGMGameManager.FILE_LEVELS, key +"_"+ actualLevel.id+"_"+i, "-1")));
            if(array.get(i) == -1)
                return arr;
		}

		arr = new int[array.size()];
		for(int i = 0; i < array.size(); i++){
			arr[i] = array.get(i);
		}
		
		return arr;
	}

	public void constructGameFromArray(){
		UIGridLevelContainer.getViewTreeObserver().addOnGlobalLayoutListener(
            new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    if(arr.length == 0)
                        return;

                    for (int i = 0; i < actualGrid.getGrid().length; i++) {
                        for (int j = 0; j < actualGrid.getGrid()[i].length; j++) {
                            actualGrid.getTile(i, j).setState(
                                    arr[i*actualGrid.getGrid()[i].length+j]);
                        }
                    }

                    // unregister listener (this is important)
                    UIGridLevelContainer.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(this);
                }
            });
	
	}

	@Override
	protected void init() {
		super.init();
		
		displayNbHint();

        UIImageViewInsectContainer = createImageView((RelativeLayout) findViewById(R.id.game_root));
        UIGridLevelContainer = (GridLayout) findViewById(R.id.game_grid_level);
        UIResetGrid = (Button) findViewById(R.id.game_btn_reset);

		((TextView) findViewById(R.id.game_tv_level_name)).setTypeface(font);
		((TextView) findViewById(R.id.game_tv_maul_shape_title)).setTypeface(font);

        if((arr = getArray(SGMGameManager.LVL_BEST_STATE)).length > 0)
            findViewById(R.id.game_btn_best).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.game_btn_best).setVisibility(View.INVISIBLE);

        findViewById(R.id.game_root).setOnTouchListener(this);
        findViewById(R.id.game_sub_root).setOnTouchListener(this);

        if(actualGrid.getNbTileBlocked() > 0)
            UIResetGrid.setVisibility(View.VISIBLE);
        else
            UIResetGrid.setVisibility(View.GONE);
	}

    @Override
    public void onBackPressed() {
        endActivity("Back");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            endActivity("Back");
            return true;
        }
        return false;
    }

    private void decodeLevel() {
		int levelID = -1;
		Intent intent = getIntent();
		if (intent != null) {
			// On prends l'ID du level
			levelID = intent.getIntExtra(SGMGameManager.LEVEL, -1);
		} else {
			// Si probl�me avec l'intent, on fait un back
			endActivity("Back");
		}

        List<Level> allLevel = new ArrayList<>();
        for (UnlockLevel level : SGMUnlockManager.getInstance().getAllUnlockOf(UnlockLevel.class, SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID)))
            allLevel.add(level.getLevel());

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
		endActivity("Back");
	}

	private void createGame(Level l) {
		int levelHeight = l.height;
		int levelWidth = l.width;
		actualGrid = new Grid(levelHeight, levelWidth, this,
				getApplicationContext(), this);
		GridLayout grille = (GridLayout) findViewById(R.id.game_grid_level);
		RelativeLayout rlAnim = (RelativeLayout) findViewById(R.id.game_rl_anim);
		grille.removeAllViews();
		grille.setColumnCount(levelWidth);

		int A, B;

		if (Configuration.ORIENTATION_LANDSCAPE == getResources()
				.getConfiguration().orientation) {
			A = metrics.widthPixels / (levelWidth * 2 + 2);
			B = metrics.heightPixels / (levelHeight + 1);
		} else {
			A = metrics.widthPixels / (levelWidth + 1);
			B = metrics.heightPixels / (levelHeight * 2 + 2);
		}

		int size = (A < B) ? (A) : (B);

		for (int i = 0; i < levelHeight; i++) {
			for (int j = 0; j < levelWidth; j++) {
				actualGrid.getTileOrigin(i, j).setState(
						l.rep.get(i).get(j));
				actualGrid.getTile(i, j).setState(l.rep.get(i).get(j));
				actualGrid.getTile(i, j).setImgBtn(
						new ImageButton(getApplicationContext()),
                        actualGrid, rlAnim);
				actualGrid.getTile(i, j).getImgBtn()
						.setLayoutParams(new LayoutParams(size, size));
				grille.addView(actualGrid.getTile(i, j).getImgBtn());
			}
		}
		actualMaul = convertTaupeFromLevel(l);
		displayTaupe(actualMaul, levelWidth, levelHeight);

		((TextView) findViewById(R.id.game_tv_level_name)).setText(l.name);
	}

	private Maul convertTaupeFromLevel(Level l) {
		Maul t = new Maul();
		int[][] f = new int[l.heightTaupe][l.widthTaupe];
		for (int i = 0; i < l.heightTaupe; i++) {
			for (int j = 0; j < l.widthTaupe; j++) {
				f[i][j] = l.taupe.get(i).get(j);
			}
		}
		t.setShape(f, true);

		return t;
	}

	private void displayTaupe(Maul m, int maulWidth, int maulHeight) {
		GridLayout grilleTaupe = (GridLayout) findViewById(R.id.game_grid_maul);
        LinearLayout llTaupe = (LinearLayout) findViewById(R.id.game_ll_maul);
		grilleTaupe.removeAllViews();

		int A, B;
		if (Configuration.ORIENTATION_LANDSCAPE == getResources()
				.getConfiguration().orientation) {
			A = metrics.widthPixels / (2 * maulWidth + 2);
			B = metrics.heightPixels / (maulHeight + 1);
		} else {
			A = metrics.widthPixels / (maulWidth + 1);
			B = metrics.heightPixels / (2 * maulHeight + 2);
		}
		int tileSize = (A < B) ? (A) : (B);

        int margeSize = 0;
        int nbMaxTile = (m.getHeight() > m.getWidth()) ? (m.getHeight()) : (m.getWidth());

		grilleTaupe.setColumnCount(m.getWidth());
		for (int i = 0; i < m.getHeight(); i++) {
			for (int j = 0; j < m.getWidth(); j++) {
				ImageView img = new ImageView(getApplicationContext());
				img.setLayoutParams(new LayoutParams(tileSize, tileSize));
				if (m.getShape()[i][j] == 0) {
					img.setBackgroundResource(R.drawable.case_none);
				} else {
					img.setBackgroundResource(getResources().getIdentifier(
							"case_taupe" + SGMMath.randInt(1, 5), "drawable",
							getApplicationContext().getPackageName()));
				}
				grilleTaupe.addView(img);

                GridLayout.LayoutParams lp = (GridLayout.LayoutParams) img.getLayoutParams();
                margeSize = lp.topMargin + lp.bottomMargin;
			}
		}

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llTaupe.getLayoutParams();
        int sizeInDp = (tileSize + margeSize) * nbMaxTile;
        params.height =  sizeInDp;
        params.width = sizeInDp;
        llTaupe.setLayoutParams(params);
	}

    @Override
    public String getNameActivity() {
        return "Game";
    }

    @Override
	public void actionClick(View v) {
		switch (v.getId()) {
            case R.id.game_btn_back:
                endActivity("Back");
                break;
            case R.id.game_btn_check:
                verify();
                break;
		}

        clickOnScreen();
	}

    public void actionTurn90Hor(View v) {
		int[][] f = actualMaul.rot90Hor();
		actualMaul.setShape(f, true);
		displayTaupe(actualMaul, actualLevel.width, actualLevel.height);

		UIGridLevelContainer = (GridLayout) findViewById(R.id.game_grid_level);
		UIGridLevelContainer.getViewTreeObserver().addOnGlobalLayoutListener(
            new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    for (int i = 0; i < actualGrid.getGrid().length; i++) {
                        for (int j = 0; j < actualGrid.getGrid()[i].length; j++) {
                            actualGrid.getTile(i, j).initAnim();
                        }
                    }

                    // unregister listener (this is important)
                    UIGridLevelContainer.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(this);
                }
            });

        clickOnScreen();
	}

    public void actionTurn90AntiHor(View v) {
        int[][] f = actualMaul.rot90Hor();
        actualMaul.setShape(f, true);
        f = actualMaul.rot90Hor();
        actualMaul.setShape(f, true);
        f = actualMaul.rot90Hor();
        actualMaul.setShape(f, true);
        displayTaupe(actualMaul, actualLevel.width, actualLevel.height);

        UIGridLevelContainer = (GridLayout) findViewById(R.id.game_grid_level);
        UIGridLevelContainer.getViewTreeObserver().addOnGlobalLayoutListener(
            new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    for (int i = 0; i < actualGrid.getGrid().length; i++) {
                        for (int j = 0; j < actualGrid.getGrid()[i].length; j++) {
                            actualGrid.getTile(i, j).initAnim();
                        }
                    }

                    // unregister listener (this is important)
                    UIGridLevelContainer.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(this);
                }
            });

        clickOnScreen();
    }

	public void actionReset(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.msg_game_reset_title);
        builder.setMessage(String.format(getResources().getString(R.string.msg_game_reset_body), actualGrid.findBestSolution(actualMaul)));

        builder.setPositiveButton(R.string.msg_game_reset_ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for (int i = 0; i < actualGrid.getGrid().length; i++) {
                            for (int j = 0; j < actualGrid.getGrid()[i].length; j++) {
                                actualGrid.getTile(i, j).setState(
                                        actualGrid.getTileOrigin(i, j).getState());
                            }
                        }
                    }
                });
        builder.setNegativeButton(R.string.msg_game_reset_back,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

        clickOnScreen();
	}

	public void actionBest(View v) {
        if((arr = getArray(SGMGameManager.LVL_BEST_STATE)).length > 0){
            for (int i = 0; i < actualGrid.getGrid().length; i++) {
                for (int j = 0; j < actualGrid.getGrid()[i].length; j++) {
                    actualGrid.getTile(i, j).setState(
                            arr[i*actualGrid.getGrid()[i].length+j]);
                }
            }
        }

        clickOnScreen();
	}

	public void actionHintMaul(View v) {
		resetTimerInsect();
		
		int nbBonus = Integer.parseInt(getPref(SGMGameManager.FILE_BONUS,
				SGMGameManager.BONUS_DISPLAY_MAUL_NB,
				Integer.toString(SGMGameManager.BONUS_DISPLAY_MAUL_DEFAULT)));
		if (nbBonus > 0) {
			actualGrid.clearMaul();
			if (actualGrid.displayXTaupe(actualMaul, 1) == 0) {
				Toast.makeText(getApplicationContext(),
						R.string.msg_game_bonus_no_maule, Toast.LENGTH_SHORT)
						.show();
			} else {
                setPref(SGMGameManager.FILE_BONUS,
						SGMGameManager.BONUS_DISPLAY_MAUL_NB,
						Integer.toString(nbBonus - 1));
				displayNbHint();
			}
		} else {
            Toast.makeText(getApplicationContext(),
                    R.string.msg_game_bonus_no_avaible, Toast.LENGTH_SHORT)
                    .show();
		}

	}

    public void actionHintStars(View v){
        int nbBonus = Integer.parseInt(getPref(SGMGameManager.FILE_BONUS,
                SGMGameManager.BONUS_DISPLAY_MAUL_NB,
                Integer.toString(SGMGameManager.BONUS_DISPLAY_MAUL_DEFAULT)));
        if (nbBonus > 0) {
            setPref(SGMGameManager.FILE_BONUS,
                    SGMGameManager.BONUS_DISPLAY_MAUL_NB,
                    Integer.toString(nbBonus - 1));
            displayNbHint();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.msg_game_hint_stars_title);
            builder.setMessage(String.format(getResources().getString(R.string.msg_game_hint_stars_body), actualGrid.findBestSolution(actualMaul)));

            // 3. Add the buttons
            builder.setNeutralButton(R.string.msg_game_hint_stars_ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog

                        }
                    });
            // 4. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.msg_game_bonus_no_avaible, Toast.LENGTH_SHORT)
                    .show();
        }
    }

	private void displayNbHint() {
        int nbHint = Integer.parseInt(getPref(
                SGMGameManager.FILE_BONUS,
                SGMGameManager.BONUS_DISPLAY_MAUL_NB,
                Integer.toString(SGMGameManager.BONUS_DISPLAY_MAUL_DEFAULT)));
		((ImageView) findViewById(R.id.game_btn_bonus_show_taupe_nb)).setImageDrawable(GetImage(getApplicationContext(), "number_"+nbHint));
	}

    private Drawable GetImage(Context c, String ImageName) {
        return c.getResources().getDrawable(c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName()));
    }

	private void verify() {
		boolean isGridComplete = actualGrid.verifyGrid(actualMaul);
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (!isGridComplete) {
			/* RESULT : LOSE */
			/* STATISTICS */
            // Statistics : Nb of games lose
            SGMStatManager.getInstance().addValueForStat(SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID), EData.STATS_NB_GAMES_LOST.toString(), 1);

			/* DISPLAY*/
			builder.setMessage(R.string.dlg_loseMsg);
			builder.setTitle(R.string.dlg_loseTitle);

			builder.setNegativeButton(R.string.dlg_lose_quit,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User clicked Continue button
							endActivity("Back");
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
							for (int i = 0; i < actualGrid.getGrid().length; i++) {
								for (int j = 0; j < actualGrid.getGrid()[i].length; j++) {
									actualGrid.getTile(i, j).setState(
											actualGrid.getTileOrigin(i, j)
													.getState());
								}
							}

                            if(actualGrid.getNbTileBlocked() > 0)
                                UIResetGrid.setVisibility(View.VISIBLE);
                            else
                                UIResetGrid.setVisibility(View.GONE);
						}
					});
		} else {
			/* RESULT : WIN */
            /* STATISTICS */
            int nbStars = 0;
            if (actualLevel != null) {
                nbStars = Integer.parseInt(getPref(SGMGameManager.FILE_LEVELS,
                        SGMGameManager.LVL_STARS + actualLevel.id, "0"));
            }
			int nbStarThisRound = checkNbStars(
                    actualGrid.findBestSolution(actualMaul),
                    actualGrid.countNbMine(actualGrid.getGrid(), 2));

			// Statistics : Nb of games win
            SGMStatManager.getInstance().addOneForStat(SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID), EData.STATS_NB_GAMES_WIN.toString());
			// Statistics : Nb of mines
            SGMStatManager.getInstance().addValueForStat(SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID), EData.STATS_ALL_MINES.toString(), actualGrid.countNbMine(actualGrid.getGrid(), 2));
			// Statistics : Nb of maul blocked
            SGMStatManager.getInstance().addValueForStat(SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID), EData.STATS_ALL_UNIQUE_MAUL.toString(), actualMaul.getWeight());

            /* BEST SCORE */
            int[] best = getArray(SGMGameManager.LVL_BEST_STATE);
            int countPreviousBestScoreTileBlocked = -1;
            if(best.length > 0) {
                countPreviousBestScoreTileBlocked = 0;
                for (int t : best) {
                    if(t == 2)
                        countPreviousBestScoreTileBlocked++;
                }
            }
            if(nbStarThisRound >= nbStars && (countPreviousBestScoreTileBlocked == -1 || actualGrid.getNbTileBlocked() <= countPreviousBestScoreTileBlocked)){
                setArray(SGMGameManager.LVL_BEST_STATE, constructArrayFromGame());
            }

			if (nbStarThisRound > nbStars) {
				// Statistics : Nb of stars
                SGMStatManager.getInstance().addValueForStat(SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID), EData.STATS_ALL_STARS.toString(), nbStarThisRound - nbStars);
                // Save : Stars for this level
                setPref(SGMGameManager.FILE_LEVELS, SGMGameManager.LVL_STARS + actualLevel.id, Integer.toString(nbStarThisRound));
            }

			/* DISPLAY */
            String msgWinNbBonusShowTaupe = constructMsgHint(nbStarThisRound, nbStars);
            displayNbHint();

			builder.setMessage(getString(R.string.dlg_winMsg)
					+ msgWinNbBonusShowTaupe);
			builder.setTitle(R.string.dlg_winTitle);
			Bitmap bmOn = BitmapFactory.decodeResource(getResources(),
					R.drawable.star_on);
			Bitmap bmOff = BitmapFactory.decodeResource(getResources(),
					R.drawable.star_off);
			ArrayList<Bitmap> a = new ArrayList<>();
			for (int j = 0; j < nbStarThisRound; j++) {
				a.add(bmOn);
			}
			for (int j = nbStarThisRound; j < 3; j++) {
				a.add(bmOff);
			}
			Drawable image = new BitmapDrawable(getResources(),
					combineImageIntoOne(a));
			builder.setIcon(image);

			builder.setPositiveButton(R.string.dlg_win_return,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User clicked OK button
                            for (int i = 0; i < actualGrid.getGrid().length; i++) {
                                for (int j = 0; j < actualGrid.getGrid()[i].length; j++) {
                                    actualGrid.getTile(i, j).setState(actualGrid.getTileOrigin(i, j).getState());
                                }
                            }
							endActivity("Back");
						}
					});
			builder.setNegativeButton(R.string.dlg_win_continue,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog

						}
					});

		}
		AlertDialog dialog = builder.create();
		dialog.show();
	}

    private String constructMsgHint(int nbStarInLevelThisRound, int nbStarsInLevel){
        String msg = "";

        int nbBonus = Integer
                .parseInt(getPref(
                        SGMGameManager.FILE_BONUS,
                        SGMGameManager.BONUS_DISPLAY_MAUL_NB,
                        Integer.toString(SGMGameManager.BONUS_DISPLAY_MAUL_DEFAULT)));

        if (nbBonus >= SGMGameManager.BONUS_DISPLAY_MAUL_DEFAULT) {
            msg = " "
                    + getString(R.string.msg_game_bonus_no_gain_max);
        } else if (nbStarInLevelThisRound > nbStarsInLevel) {
            nbBonus += nbStarInLevelThisRound - nbStarsInLevel;
            if (nbBonus > SGMGameManager.BONUS_DISPLAY_MAUL_DEFAULT) {
                nbBonus = SGMGameManager.BONUS_DISPLAY_MAUL_DEFAULT;
            }
            setPref(SGMGameManager.FILE_BONUS,
                    SGMGameManager.BONUS_DISPLAY_MAUL_NB,
                    Integer.toString(nbBonus));
            msg = " "
                    + getString(R.string.msg_game_bonus_gain_beat_part1)
                    + " " + (nbStarInLevelThisRound - nbStarsInLevel) + " "
                    + getString(R.string.msg_game_bonus_gain_part2);
        } else if (nbStarInLevelThisRound == 3) {
            nbBonus += 1;
            if (nbBonus > SGMGameManager.BONUS_DISPLAY_MAUL_DEFAULT) {
                nbBonus = SGMGameManager.BONUS_DISPLAY_MAUL_DEFAULT;
            }
            setPref(SGMGameManager.FILE_BONUS,
                    SGMGameManager.BONUS_DISPLAY_MAUL_NB,
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

    private void clickOnScreen(){
        this.resetTimerInsect();

        if(actualGrid.getNbTileBlocked() > 0)
            UIResetGrid.setVisibility(View.VISIBLE);
        else
            UIResetGrid.setVisibility(View.GONE);
    }

    private void resetTimerInsect(){
        if(insectLauncher != null)
            insectLauncher.resetTime();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        clickOnScreen();

        return false;
    }

    @Override
    public void onTileClick() {
        actualGrid.clearMaul();

        clickOnScreen();
    }
}