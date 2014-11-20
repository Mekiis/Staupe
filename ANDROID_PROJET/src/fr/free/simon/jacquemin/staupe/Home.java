package fr.free.simon.jacquemin.staupe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import fr.free.simon.jacquemin.staupe.SGM.SGMScreenInterface;
import fr.free.simon.jacquemin.staupe.insects.LauncherInsect;

public class Home extends SGMScreenInterface {
	private static ImageView UIimageViewInsectContainer = null;
	private static LauncherInsect insectLauncher = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		this.behaviorQuitButton = 1;
		init();

        UIimageViewInsectContainer = (ImageView) findViewById(R.id.anim_test);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
        insectLauncher.stop();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
        insectLauncher = new fr.free.simon.jacquemin.staupe.insects.LauncherInsect(5, 7, UIimageViewInsectContainer, this, metrics);
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
		case R.id.btn_home_stats:
			endActivity("Stats");
		case R.id.btn_home_rules:
			endActivity("Rules");
		case R.id.btn_home_tutorial:
			endActivity("Tutorial");
		case R.id.btn_home_options:
			endActivity("Options");
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
	}

}
