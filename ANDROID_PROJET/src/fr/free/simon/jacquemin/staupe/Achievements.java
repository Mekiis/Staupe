package fr.free.simon.jacquemin.staupe;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

import fr.free.simon.jacquemin.staupe.SGM.SGMActivity;
import fr.free.simon.jacquemin.staupe.gui.achievements.AchievementsAdapter;
import fr.free.simon.jacquemin.staupe.gui.achievements.AchievementsItem;
import io.brothers.sgm.Unlockable.SGMAchievement;
import io.brothers.sgm.Unlockable.SGMAchievementManager;
import io.brothers.sgm.User.SGMUser;
import io.brothers.sgm.User.SGMUserManager;

public class Achievements extends SGMActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.achievements);
		init();
	}

    @Override
    protected void init() {
        super.init();

        displayAchievements();
    }

    private void displayAchievements() {
        AchievementsItem[] items = new AchievementsItem[SGMAchievementManager.getInstance().getAchievements().size()];

        int i = 0;
        for (SGMAchievement achievement : SGMAchievementManager.getInstance().getAchievements()){
            SGMUser user = SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID);
            if(SGMAchievementManager.getInstance().isAchievementComplete(user, achievement))
                items[i] = new AchievementsItem(achievement.getId(), achievement.getName(), achievement.getDesc());
            else
                items[i] = new AchievementsItem(achievement.getId(), achievement.getName(), achievement.getDesc(), SGMAchievementManager.getInstance().getAchievementCompletionPercent(user, achievement));
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

        // Create a new ListView, set the adapter and item click listener
        ListView listViewItems = new ListView(this);
        listViewItems.setAdapter(new AchievementsAdapter(this, R.layout.achivements_item, items, measuredWidth));

        // Add the list to the main layout
        ((LinearLayout) findViewById(R.id.achievements_ll_display_stats)).addView(listViewItems);
    }

    // 2.0 and above
	@Override
	public void onBackPressed() {
		endActivity("Back");
	}

	// Before 2.0
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			endActivity("Back");
			return true;
		}
		return false;
	}

    @Override
	public void actionClick(View v) {
		switch (v.getId()) {
		case R.id.achievements_btn_back:
			endActivity("Back");
			break;
		}
	}

    @Override
    public String getNameActivity() {
        return "Achievements";
    }
}
