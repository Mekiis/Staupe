package fr.free.simon.jacquemin.staupe;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.free.simon.jacquemin.staupe.SGM.SGMActivity;
import fr.free.simon.jacquemin.staupe.insects.LauncherInsect;
import io.brothers.sgm.SGMStatManager;

public class Home extends SGMActivity implements View.OnTouchListener{
	private static ImageView UIImageViewInsectContainer = null;
	private static LauncherInsect insectLauncher = null;

    private static Dialog dialog = null;
    private static CheckBox checkBoxConnectAuto = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		this.behaviorQuitButton = 1;
		init();

        UIImageViewInsectContainer = createImageView((RelativeLayout) findViewById(R.id.home_root));

        if(SGMStatManager.getInstance().getStatValueForUser(SGMGameManager.USER_ID, "DISPLAY_HOME") == 0){
            dialog = new Dialog(Home.this);
            dialog.setContentView(R.layout.connect_popup);
            dialog.setTitle(R.string.connection_gps_title);

            Button dialogOkButton = (Button) dialog.findViewById(R.id.connect_popup_btn_ok);
            dialogOkButton.setTypeface(font);
            dialogOkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //SGMGameManager.this.connectUser(SGMGameManager.USER_ID);
                    dialog.dismiss();
                }
            });

            Button dialogCancelButton = (Button) dialog.findViewById(R.id.connect_popup_btn_cancel);
            dialogCancelButton.setTypeface(font);
            dialogCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });

            checkBoxConnectAuto = (CheckBox) dialog.findViewById(R.id.connect_popup_auto_connect);
            checkBoxConnectAuto.setTypeface(font);
            ((TextView) dialog.findViewById(R.id.connect_popup_text)).setTypeface(font);

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    SGMStatManager.getInstance().setValueForInternalStat(SGMGameManager.USER_ID, "CONNECT_AUTO", checkBoxConnectAuto.isChecked() == true ? 1 : 0);
                    insectLauncher.run();
                }
            });

            dialog.show();
            SGMStatManager.getInstance().addOneForInternalStat(SGMGameManager.USER_ID, "DISPLAY_HOME");
        }

	}
	
	@Override
	protected void onPause() {
		super.onPause();
        insectLauncher.stop();
	}
	
	@Override
	protected void onResume() {
		super.onResume();

        String value = "1";
        value = getPref(SGMGameManager.FILE_OPTIONS, SGMGameManager.OPTION_ANIM_MENU, value);
        insectLauncher = new fr.free.simon.jacquemin.staupe.insects.LauncherInsect(5, 7, UIImageViewInsectContainer, this, metrics, value == "1" ? true : false);

        if(dialog == null || !dialog.isShowing())
            insectLauncher.run();
	}
	
	@Override
	protected void endActivity(String msg) {
		
		super.endActivity(msg);
	}

    @Override
    public String getNameActivity() {
        return "Home";
    }

    @Override
	public void actionClick(View v) {
		switch (v.getId()) {
		case R.id.btn_home_play:
			endActivity("Play");
            break;
		case R.id.btn_home_stats:
			endActivity("Stats");
            break;
		case R.id.btn_home_rules:
			endActivity("Rules");
            break;
		case R.id.btn_home_tutorial:
			endActivity("Tutorial");
            break;
		case R.id.btn_home_options:
			endActivity("Options");
            break;
        case R.id.btn_home_achievements:
            endActivity("Achievements");
            break;
		}
	}

	protected void init() {
		super.init();

		((TextView) findViewById(R.id.tv_title_home)).setTypeface(font);
		((TextView) findViewById(R.id.tv_version)).setTypeface(font);
		((Button) findViewById(R.id.btn_home_play)).setTypeface(font);
		((Button) findViewById(R.id.btn_home_stats)).setTypeface(font);
		((Button) findViewById(R.id.btn_home_rules)).setTypeface(font);
		((Button) findViewById(R.id.btn_home_tutorial)).setTypeface(font);
		((Button) findViewById(R.id.btn_home_options)).setTypeface(font);
        ((Button) findViewById(R.id.btn_home_achievements)).setTypeface(font);

        findViewById(R.id.home_root).setOnTouchListener(this);
	}

    private void resetTimerInsect(){
        if(insectLauncher != null)
            insectLauncher.resetTime();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.resetTimerInsect();

        return false;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return false;
    }
}
