package fr.free.simon.jacquemin.staupe;

import fr.free.simon.jacquemin.staupe.stats.StatsAdapter;
import fr.free.simon.jacquemin.staupe.stats.StatsItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Stats extends SGMScreenInterface {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats);

		init();
		affichage();
	}

	@Override
	public void actionClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back_stats:
			endActivity("Back");
			break;
		}
	}

    @Override
    public String getNameActivity() {
        return "Stats";
    }

    public void reinitialiseStats() {
		getSharedPreferences(SGMGameManager.FILE_LEVELS, 0).edit().clear().commit();
		setPref(SGMGameManager.FILE_STATS, SGMGameManager.STATS_ALL_STARS, "0");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// win
		// 2. Chain together various setter methods to set the dialog
		// characteristics
		builder.setMessage(R.string.dlg_statsReinitialise);
		builder.setTitle(R.string.dlg_statsReinitialiseTitle);

		// 3. Add the buttons
		builder.setPositiveButton(R.string.dlg_statsReinitialiseOk,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User clicked OK button
						endActivity("Back");
					}
				});
		
		// 4. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public void init() {
		super.init();
		
		((Button) findViewById(R.id.btn_back_stats)).setTypeface(font);
	}

	private void affichage() {
		StatsItem[] items = new StatsItem[6];

		// Get all the stats
		items[0] = new StatsItem(Integer.parseInt(getPref(SGMGameManager.FILE_STATS,
				SGMGameManager.STATS_NB_GAMES_WIN, "0")),
				getString(R.string.stats_gameWin));
		items[1] = new StatsItem(Integer.parseInt(getPref(SGMGameManager.FILE_STATS,
				SGMGameManager.STATS_NB_GAMES_LOST, "0")),
				getString(R.string.stats_gameLost));
		items[2] = new StatsItem(Integer.parseInt(getPref(SGMGameManager.FILE_STATS,
				SGMGameManager.STATS_ALL_STARS, "0")),
				getString(R.string.stats_allStars));
		items[3] = new StatsItem(Integer.parseInt(getPref(SGMGameManager.FILE_STATS,
				SGMGameManager.STATS_ALL_GROUP_MAUL, "0")),
				getString(R.string.stats_groupMaul));
		items[4] = new StatsItem(Integer.parseInt(getPref(SGMGameManager.FILE_STATS,
				SGMGameManager.STATS_ALL_UNIQUE_MAUL, "0")),
				getString(R.string.stats_uniqueMaul));
		items[5] = new StatsItem(Integer.parseInt(getPref(SGMGameManager.FILE_STATS,
				SGMGameManager.STATS_ALL_MINES, "0")),
				getString(R.string.stats_allMine));

		// Get the size of the screen
		int Measuredwidth = 0;
		Point size = new Point();
		WindowManager w = getWindowManager();
		// Check for outdate version
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			w.getDefaultDisplay().getSize(size);
			Measuredwidth = size.x;
		} else {
			Display d = w.getDefaultDisplay();
			Measuredwidth = d.getWidth();
		}

		// Create an adapter instance
		StatsAdapter adapter = new StatsAdapter(this, R.layout.stats_item,
				items, Measuredwidth);

		// Create a new ListView, set the adapter and item click listener
		ListView listViewItems = new ListView(this);
		listViewItems.setAdapter(adapter);

		// Add the list to the main layout
		LinearLayout layout = (LinearLayout) findViewById(R.id.lay_stats);
		layout.addView(listViewItems);
	}
}
