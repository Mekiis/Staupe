package fr.free.simon.jacquemin.staupe;

import fr.free.simon.jacquemin.staupe.SGM.SGMScreenInterface;
import fr.free.simon.jacquemin.staupe.stats.StatsAdapter;
import fr.free.simon.jacquemin.staupe.stats.StatsItem;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
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
		displayStatistiques();
	}

	@Override
	public void actionClick(View v) {
		switch (v.getId()) {
		case R.id.stats_btn_back:
			endActivity("Back");
			break;
		}
	}

    @Override
    public String getNameActivity() {
        return "Stats";
    }

	public void init() {
		super.init();
		
		((Button) findViewById(R.id.stats_btn_back)).setTypeface(font);
	}

	private void displayStatistiques() {
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
		LinearLayout layout = (LinearLayout) findViewById(R.id.stats_ll_display_stats);
		layout.addView(listViewItems);
	}
}
