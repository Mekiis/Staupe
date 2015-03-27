package fr.free.simon.jacquemin.staupe;

import fr.free.simon.jacquemin.staupe.SGM.SGMActivity;
import fr.free.simon.jacquemin.staupe.container.data.EData;
import fr.free.simon.jacquemin.staupe.stats.StatsAdapter;
import fr.free.simon.jacquemin.staupe.stats.StatsItem;
import io.brothers.sgm.User.SGMUserManager;

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
		displayStats();
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

	private void displayStats() {
		StatsItem[] items = new StatsItem[6];

		// Get all the stats
        int nbMaul = SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID).getSavedData(EData.STATS_ALL_UNIQUE_MAUL.toString());
        int nbLevelWin = SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID).getSavedData(EData.STATS_NB_GAMES_WIN.toString());
        int nbLevelLose = SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID).getSavedData(EData.STATS_NB_GAMES_LOST.toString());
        int nbInsectKill = SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID).getSavedData(EData.STATS_NB_INSECT_KILL.toString());
        int nbInsectNotKill = SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID).getSavedData(EData.STATS_NB_INSECT_NOT_KILL.toString());
        long dateInstallation = SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID).getSavedData(EData.STATS_DATE_INSTALLATION.toString());

        // Todo Refactor code stats on SGM
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
		int measuredWidth = 0;
		Point size = new Point();
		WindowManager w = getWindowManager();
		// Check for out of date version
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			w.getDefaultDisplay().getSize(size);
			measuredWidth = size.x;
		} else {
			Display d = w.getDefaultDisplay();
			measuredWidth = d.getWidth();
		}

		// Create an adapter instance
		StatsAdapter adapter = new StatsAdapter(this, R.layout.stats_item,
				items, measuredWidth);

		// Create a new ListView, set the adapter and item click listener
		ListView listViewItems = new ListView(this);
		listViewItems.setAdapter(adapter);

		// Add the list to the main layout
		LinearLayout layout = (LinearLayout) findViewById(R.id.stats_ll_display_stats);
		layout.addView(listViewItems);
	}
}
