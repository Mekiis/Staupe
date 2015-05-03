package fr.free.simon.jacquemin.staupe;

import fr.free.simon.jacquemin.staupe.SGM.SGMActivity;
import fr.free.simon.jacquemin.staupe.gui.stats.StatsAdapter;
import fr.free.simon.jacquemin.staupe.gui.stats.StatsItem;
import io.brothers.sgm.SGMADisplayableStat;
import io.brothers.sgm.SGMStatManager;
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
		StatsItem[] items = new StatsItem[SGMStatManager.getInstance().getStatsDisplayable().size()];

        int i = 0;
        for (SGMADisplayableStat stat : SGMStatManager.getInstance().getStatsDisplayable()){
            items[i] = new StatsItem(stat.id, stat.name, stat.desc, stat.getValueFormat(SGMGameManager.USER_ID));
            i++;
        }

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
