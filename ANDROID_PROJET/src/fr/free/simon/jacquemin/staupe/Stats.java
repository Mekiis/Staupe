package fr.free.simon.jacquemin.staupe;

import fr.free.simon.jacquemin.staupe.SGM.SGMActivity;
import fr.free.simon.jacquemin.staupe.data_sets.StatsSet;
import fr.free.simon.jacquemin.staupe.stats.StatsAdapter;
import fr.free.simon.jacquemin.staupe.stats.StatsItem;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.text.SimpleDateFormat;

public class Stats extends SGMActivity {

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

	public void init() {
		super.init();
		
		((Button) findViewById(R.id.stats_btn_back)).setTypeface(font);
	}

	private void displayStatistiques() {
		StatsItem[] items = new StatsItem[6];

		// Get all the stats
        int nbMaul = StatsSet.getStats(this).get(StatsSet.EStats.STATS_ALL_UNIQUE_MAUL);
        int nbLevelWin = StatsSet.getStats(this).get(StatsSet.EStats.STATS_NB_GAMES_WIN);
        int nbLevelLose = StatsSet.getStats(this).get(StatsSet.EStats.STATS_NB_GAMES_LOST);
        int nbInsectKill = StatsSet.getStats(this).get(StatsSet.EStats.STATS_NB_INSECT_KILL);
        int nbInsectNotKill = StatsSet.getStats(this).get(StatsSet.EStats.STATS_NB_INSECT_NOT_KILL);
        long dateInstallation = StatsSet.getStats(this).get(StatsSet.EStats.STATS_DATE_INSTALLATION);

		items[0] = new StatsItem((nbLevelWin > 0f ? Float.toString(nbMaul/nbLevelWin*1f) : "0"),
				getString(R.string.stats_ratioMaulPerLevel));
		items[1] = new StatsItem(((nbLevelWin + nbLevelLose) > 0f ? Float.toString((nbLevelWin / (nbLevelWin + nbLevelLose))*10f) : "0"),
				getString(R.string.stats_ratioWinLose));
		items[2] = new StatsItem(Integer.toString(nbLevelWin),
				getString(R.string.stats_nbLevelWin));
		items[3] = new StatsItem(Integer.toString(nbMaul),
				getString(R.string.stats_nbUniqueMaul));
		items[4] = new StatsItem((nbInsectKill + nbInsectNotKill > 0f ? Float.toString((nbInsectKill / (nbInsectKill + nbInsectNotKill))*10f) : "0"),
				getString(R.string.stats_ratioInsectKill));
        SimpleDateFormat sdf = new SimpleDateFormat(SGMGameManager.DATE_FORMAT);
        items[5] = new StatsItem(sdf.format(dateToLong(now()) - dateInstallation),
                getString(R.string.stats_timeOnGame));

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
