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

public class Options extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		init();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
		return super.onKeyDown(keyCode, event);
	}

	public void actionClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back_options:
			endActivity("Back");
			break;
		case R.id.btn_reinitialise_stats:
			reinitialiseStats();
			break;
		case R.id.btn_reinitialise_achievement:
			break;
		}
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
		
		Log.d("Simon", "Dialog");
		// 4. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public String getPref(String file, String key, String defaulValue) {
		String s = key;
		SharedPreferences preferences = getSharedPreferences(file, 0);
		return preferences.getString(s, defaulValue);
	}

	public void setPref(String file, String key, String value) {
		SharedPreferences preferences = getSharedPreferences(file, 0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void init() {
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/Barthowheel Regular.ttf");
		((Button) findViewById(R.id.btn_back_options)).setTypeface(tf);
		((Button) findViewById(R.id.btn_reinitialise_stats)).setTypeface(tf);
	}

	private void endActivity(String msg) {
		// Création de l'intent
		Intent IntentParent = getIntent();

		// On rajoute le nom saisie dans l'intent
		IntentParent.putExtra(SGMGameManager.RESPOND_NAME, "Options" + msg);

		// On retourne le résultat avec l'intent
		setResult(SGMGameManager.RESULT_OK, IntentParent);

		// On termine cette activité
		finish();
	}

}
