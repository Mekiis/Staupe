package fr.free.simon.jacquemin.staupe.container;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.free.simon.jacquemin.staupe.R;
import fr.free.simon.jacquemin.staupe.SGM.SGMActivity;
import fr.free.simon.jacquemin.staupe.container.data.EData;
import io.brothers.sgm.Tools.SGMMath;
import fr.free.simon.jacquemin.staupe.SGMGameManager;
import fr.free.simon.jacquemin.staupe.utils.ReadLevelFile;
import io.brothers.sgm.User.SGMUserManager;

public class Game extends SGMActivity{
    private static Level actualLevel;
    private static Grid actualGrid;
    private static Maul actualMaul;
    private static ArrayList<Level> allLevel;

    private static int[] arr = null;

    private static GridLayout UIgridLevelContainer = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        init();

        decodeLevel();

        UIgridLevelContainer = (GridLayout) findViewById(R.id.game_grid_level);
    }

    public void setArray(String key, int[] array)
    {
        for(int i = 0; i < array.length; i++){
            setPref(SGMGameManager.FILE_LEVELS, key +"_"+ actualLevel.id+"_"+i, Integer.toString(array[i]));
        }
    }

    public int[] constructArrayFromGame(){
        int count = 0;
        for (int i = 0; i < actualGrid.getGrille().length; i++) {
            count += actualGrid.getGrille()[i].length;
        }
        int[] arr = new int[count];
        for (int i = 0; i < actualGrid.getGrille().length; i++) {
            for (int j = 0; j < actualGrid.getGrille()[i].length; j++) {
                arr[i * actualGrid.getGrille()[i].length + j] = actualGrid
                        .getCase(i, j).getState();
            }
        }

        return arr;
    }

    public int[] getArray(String key)
    {
        List<Integer> array = new ArrayList<Integer>();
        int[] arr = new int[0];
        int count = 0;
        for (int i = 0; i < actualGrid.getGrille().length; i++) {
            count += actualGrid.getGrille()[i].length;
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
        UIgridLevelContainer.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if(arr.length == 0)
                            return;

                        for (int i = 0; i < actualGrid.getGrille().length; i++) {
                            for (int j = 0; j < actualGrid.getGrille()[i].length; j++) {
                                actualGrid.getCase(i, j).setState(
                                        arr[i*actualGrid.getGrille()[i].length+j]);
                            }
                        }

                        // unregister listener (this is important)
                        UIgridLevelContainer.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });

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
        endActivity("Back");
    }

    private void createGame(Level l) {
        int levelHeight = l.height;
        int levelWidth = l.width;
        actualGrid = new Grid(levelHeight, levelWidth, this,
                getApplicationContext());
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
                actualGrid.getCaseArchive(i, j).setState(
                        l.rep.get(i).get(j));
                actualGrid.getCase(i, j).setState(l.rep.get(i).get(j));
                actualGrid.getCase(i, j).setImgBtn(
                        new ImageButton(getApplicationContext()),
                        actualGrid, rlAnim);
                actualGrid.getCase(i, j).getImgBtn()
                        .setLayoutParams(new ViewGroup.LayoutParams(size, size));
                grille.addView(actualGrid.getCase(i, j).getImgBtn());
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

        int size = (A < B) ? (A) : (B);

        int height = 0;

        grilleTaupe.setColumnCount(m.getWidth());
        for (int i = 0; i < m.getHeight(); i++) {
            for (int j = 0; j < m.getWidth(); j++) {
                ImageView img = new ImageView(getApplicationContext());
                img.setLayoutParams(new ViewGroup.LayoutParams(size, size));
                if (m.getShape()[i][j] == 0) {
                    img.setBackgroundResource(R.drawable.case_none);
                } else {
                    img.setBackgroundResource(getResources().getIdentifier(
                            "case_taupe" + SGMMath.randInt(1, 5), "drawable",
                            getApplicationContext().getPackageName()));
                }
                grilleTaupe.addView(img);

                GridLayout.LayoutParams lp =
                        (GridLayout.LayoutParams) img.getLayoutParams();
                height = lp.topMargin + lp.bottomMargin;
            }
        }


        int caseSize = (m.getHeight() > m.getWidth()) ? (m.getHeight()) : (m.getWidth());
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llTaupe.getLayoutParams();
        params.height = size * (caseSize + height) ; // In dp
        llTaupe.setLayoutParams(params);
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
    }

    public void actionTurn(View v) {
        int[][] f = actualMaul.rot90Hor();
        actualMaul.setShape(f, true);
        displayTaupe(actualMaul, actualLevel.width, actualLevel.height);

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
        for (int i = 0; i < actualGrid.getGrille().length; i++) {
            for (int j = 0; j < actualGrid.getGrille()[i].length; j++) {
                actualGrid.getCase(i, j).setState(
                        actualGrid.getCaseArchive(i, j).getState());
            }
        }
    }

    public void actionBest(View v) {
        if((arr = getArray(SGMGameManager.LVL_BEST_STATE)).length > 0){
            for (int i = 0; i < actualGrid.getGrille().length; i++) {
                for (int j = 0; j < actualGrid.getGrille()[i].length; j++) {
                    actualGrid.getCase(i, j).setState(
                            arr[i*actualGrid.getGrille()[i].length+j]);
                }
            }
        }
    }

    public void actionHintMaul(View v) {
        int nbBonus = Integer.parseInt(getPref(SGMGameManager.FILE_BONUS,
                SGMGameManager.BONUS_DISPLAY_MAUL_NB,
                Integer.toString(SGMGameManager.BONUS_DISPLAY_MAUL_DEFAULT)));
        if (nbBonus > 0) {
            actualGrid.clearTaupe();
            if (actualGrid.displayXTaupe(actualMaul, 1) == 0)
                Toast.makeText(getApplicationContext(),
                        R.string.msg_game_bonus_no_maule, Toast.LENGTH_SHORT)
                        .show();
            else {
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

    public static Drawable GetImage(Context c, String ImageName) {
        return c.getResources().getDrawable(c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName()));
    }

    private void verify() {
        boolean isGridComplete = actualGrid.verifGrille(actualMaul);
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (!isGridComplete) {
            // LOSE
            // Statistics : Nb of games lose
            SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID).addData(EData.STATS_NB_GAMES_LOST.toString());

            // 2. Chain together various setter methods to set the dialog
            // characteristics
            builder.setMessage(R.string.dlg_loseMsg);
            builder.setTitle(R.string.dlg_loseTitle);

            // 3. Add the buttons
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
                        SGMGameManager.LVL_STARS + actualLevel.id, "0"));
            }
            // 2. Chain together various setter methods to set the dialog
            int nbStarThisRound = checkNbStars(
                    actualGrid.findBestSolution(actualMaul),
                    actualGrid.countNbMine(actualGrid.getGrille(), 2));

            // Statistics : Nb of games win
            SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID).addData(EData.STATS_NB_GAMES_WIN.toString());
            // Statistics : Nb of mines
            SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID).addData(EData.STATS_ALL_MINES.toString(), actualGrid.countNbMine(actualGrid.getGrille(), 2));
            // Statistics : Nb of maul blocked
            SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID).addData(EData.STATS_ALL_UNIQUE_MAUL.toString(), actualMaul.getWeight());

            String msgWinNbBonusShowTaupe = constructMsgHint(nbStarThisRound, nbStars);
            displayNbHint();

            // New best score
            if (nbStarThisRound > nbStars) {
                // Statistics : Nb of stars
                SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID).addData(EData.STATS_ALL_STARS.toString(), nbStars - nbStarThisRound);

                // Save : Stars for this level
                setPref(SGMGameManager.FILE_LEVELS, SGMGameManager.LVL_STARS + actualLevel.id, Integer.toString(nbStarThisRound));

                setArray(SGMGameManager.LVL_BEST_STATE, constructArrayFromGame());
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
                            for (int i = 0; i < actualGrid.getGrille().length; i++) {
                                for (int j = 0; j < actualGrid.getGrille()[i].length; j++) {
                                    actualGrid.getCase(i, j).setState(actualGrid.getCaseArchive(i, j).getState());
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
        // 4. Get the AlertDialog from create()
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
        int nbStarsThisRound;

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

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public String getNameActivity() {
        return null;
    }
}
