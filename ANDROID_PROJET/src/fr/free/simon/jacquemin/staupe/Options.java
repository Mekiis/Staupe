package fr.free.simon.jacquemin.staupe;

import fr.free.simon.jacquemin.staupe.SGM.SGMActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class Options extends SGMActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		init();
	}

    @Override
    protected void init() {
        super.init();

        ((Button) findViewById(R.id.options_btn_reinitialise_stats)).setTypeface(font);
        ((Button) findViewById(R.id.options_btn_reinitialise_achievement)).setTypeface(font);
        ((CheckBox) findViewById(R.id.options_cb_activate_animations)).setTypeface(font);
        ((CheckBox) findViewById(R.id.options_cb_activate_optional_animations)).setTypeface(font);
        ((TextView) findViewById(R.id.options_title)).setTypeface(font);
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
		case R.id.options_btn_back:
			endActivity("Back");
			break;
		case R.id.options_btn_reinitialise_stats:
			reinitialiseStats();
			break;
		case R.id.options_btn_reinitialise_achievement:
			break;
		}
	}

    @Override
    public String getNameActivity() {
        return "Options";
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
}
