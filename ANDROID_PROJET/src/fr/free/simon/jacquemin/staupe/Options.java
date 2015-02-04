package fr.free.simon.jacquemin.staupe;

import fr.free.simon.jacquemin.staupe.SGM.SGMActivity;
import fr.free.simon.jacquemin.staupe.data_sets.StatsSet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
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

        ((Button) findViewById(R.id.options_btn_reinitialise_stars)).setTypeface(font);
        ((Button) findViewById(R.id.options_btn_reinitialise_achievement)).setTypeface(font);
        ((Switch) findViewById(R.id.options_cb_activate_animations_IG)).setTypeface(font);
        ((Switch) findViewById(R.id.options_cb_activate_animations_menu)).setTypeface(font);
        ((TextView) findViewById(R.id.options_title)).setTypeface(font);

        ((Switch) findViewById(R.id.options_cb_activate_animations_IG)).setOnCheckedChangeListener(new OnChangeListener());
        ((Switch) findViewById(R.id.options_cb_activate_animations_menu)).setOnCheckedChangeListener(new OnChangeListener());

        String value = "1";

        value = getPref(SGMGameManager.FILE_OPTIONS, SGMGameManager.OPTION_ANIM_IG, "1");
        ((Switch) findViewById(R.id.options_cb_activate_animations_IG)).setChecked(value == "1" ? true : false);

        value = getPref(SGMGameManager.FILE_OPTIONS, SGMGameManager.OPTION_ANIM_MENU, "1");
        ((Switch) findViewById(R.id.options_cb_activate_animations_menu)).setChecked(value == "1" ? true : false);


    }

    public class OnChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String key = "";
            String value = "0";

            if(isChecked)
                value = "1";
            else
                value = "0";

            if(buttonView.getId() == R.id.options_cb_activate_animations_IG)
                key = SGMGameManager.OPTION_ANIM_IG;

            if(buttonView.getId() == R.id.options_cb_activate_animations_menu)
                key = SGMGameManager.OPTION_ANIM_MENU;

            setPref(SGMGameManager.FILE_OPTIONS, key, value);
        }
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
		case R.id.options_btn_reinitialise_stars:
            confirmReinitialiseStars();
			break;
		case R.id.options_btn_reinitialise_achievement:
			break;
		}
	}

    @Override
    public String getNameActivity() {
        return "Options";
    }

    public void confirmReinitialiseStars() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 2. Chain together various setter methods to set the dialog
        // characteristics
        builder.setTitle(R.string.dlg_starsReinitialiseTitleConfirm);
        builder.setMessage(R.string.dlg_starsReinitialiseConfirm);

        // 3. Add the buttons
        builder.setPositiveButton(R.string.dlg_starsReinitialiseConfirmYes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Yes button
                        reinitialiseStars();
                    }
                });

        builder.setNegativeButton(R.string.dlg_starsReinitialiseConfirmNo,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        // 4. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void reinitialiseStars() {
        getSharedPreferences(SGMGameManager.FILE_LEVELS, 0).edit().clear().commit();
        StatsSet.setStat(this, StatsSet.EStats.STATS_ALL_STARS, 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 2. Chain together various setter methods to set the dialog
        // characteristics
        builder.setTitle(R.string.dlg_starsReinitialiseTitle);
        builder.setMessage(R.string.dlg_starsReinitialise);

        // 3. Add the buttons
        builder.setNeutralButton(R.string.dlg_starsReinitialiseOk,
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
}
