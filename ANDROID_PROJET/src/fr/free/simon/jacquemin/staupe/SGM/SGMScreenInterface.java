package fr.free.simon.jacquemin.staupe.SGM;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;

import fr.free.simon.jacquemin.staupe.SGMGameManager;

public abstract class SGMScreenInterface extends Activity {
	protected static DisplayMetrics metrics = new DisplayMetrics();
	protected static Typeface font = null;
	protected Intent intent = getIntent();
	protected static int behaviorQuitButton = 0;

    private static String nameActivity = "Generic";

	protected void init() {
		// Measure the metrics of the screen
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		// Get the font used for all the text displayed
		font = Typeface.createFromAsset(getAssets(),
				"fonts/Barthowheel Regular.ttf");
	}

	// 2.0 and above
	@Override
	public void onBackPressed() {
		switch(behaviorQuitButton){
		case 0:
			endActivity("Back");
			break;
		case 1:
			moveTaskToBack(true);
			break;
		}
		
	}

	// Before 2.0
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch(behaviorQuitButton){
			case 0:
				endActivity("Back");
				break;
			case 1:
				moveTaskToBack(true);
				break;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void actionClick(View v) {
		// Need to be override to react to the click
	}

	protected void endActivity(String msg) {
		// Get the instance of the Intent
		intent = getIntent();

		// Add the name of the Activity
		intent.putExtra(SGMGameManager.RESPOND_NAME, getNameActivity() + msg);
		
		// Return a RESULT_OK as a result of the activity
		setResult(SGMGameManager.RESULT_OK, intent);

		// Finish the current activity
		finish();
	}
	
	protected String getPref(String file, String key, String defaulValue) {
		String s = key;
		SharedPreferences preferences = getSharedPreferences(file, 0);
		return preferences.getString(s, defaulValue);
	}
	
	protected void setPref(String file, String key, String value) {
		SharedPreferences preferences = getSharedPreferences(file, 0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

    abstract public String getNameActivity();
}
