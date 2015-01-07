package fr.free.simon.jacquemin.staupe;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fr.free.simon.jacquemin.staupe.SGM.SGMActivity;
import fr.free.simon.jacquemin.staupe.SGM.utils.SGMMath;
import fr.free.simon.jacquemin.staupe.container.Grid;
import fr.free.simon.jacquemin.staupe.container.Level;
import fr.free.simon.jacquemin.staupe.container.Maul;
import fr.free.simon.jacquemin.staupe.utils.ReadLevelFile;

public class Tutorial extends SGMActivity {
    private static Level actualLevel;
    private static Grid actualGrid;
    private static Maul actualMaul;

    private static ArrayList<Level> allLevel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);
		init();
	}


    @Override
    public void actionClick(View v) {
        switch (v.getId()) {
            case R.id.game_btn_back:
                endActivity("Back");
                break;
            case R.id.game_btn_check:
                break;
        }
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

    @Override
    public String getNameActivity() {
        return "Tutorial";
    }

    @Override
    protected void init() {
        super.init();

        ((TextView) findViewById(R.id.game_tv_level_name)).setTypeface(font);
        ((TextView) findViewById(R.id.game_tv_maul_shape_title)).setTypeface(font);
        ((Button) findViewById(R.id.game_btn_back)).setTypeface(font);
        ((Button) findViewById(R.id.game_btn_check)).setTypeface(font);
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

        int A = 0;
        int B = 0;

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

        int A = 0;
        int B = 0;

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

                ImageView imageView = img;
                GridLayout.LayoutParams lp =
                        (GridLayout.LayoutParams) imageView.getLayoutParams();
                height = lp.topMargin + lp.bottomMargin;
            }
        }


        int caseSize = (m.getHeight() > m.getWidth()) ? (m.getHeight()) : (m.getWidth());
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llTaupe.getLayoutParams();
        params.height = size * (caseSize + height) ; // In dp
        llTaupe.setLayoutParams(params);
    }

}
