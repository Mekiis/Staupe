package fr.free.simon.jacquemin.staupe;

import java.util.Timer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import fr.free.simon.jacquemin.staupe.utils.SGMMath;
import fr.free.simon.jacquemin.staupe.utils.SGMTimer;

public class Accueil extends SGMScreenInterface {
	private Context ctx = null;
	private Activity activity = null;
	private ImageView img = null;
	private LauncherInsect taskInsect = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accueil);
		this.nameActivity = "Accueil";
		this.behaviorQuitButton = 1;
		init();

		img = (ImageView) findViewById(R.id.anim_test);
		activity = this;
		ctx = getApplicationContext();

		taskInsect = new LauncherInsect();
		new SGMTimer().execute((float) SGMMath.randInt(5, 7), false, taskInsect);
		
	}
	
	public class LauncherInsect implements Runnable{

		@Override
		public void run() {
			new Insecte().execute(img, activity, ctx,
					metrics.heightPixels,
					-500f, metrics.widthPixels + 1000f);
			
			new SGMTimer().execute((float) SGMMath.randInt(5, 7), false, taskInsect);
		}
	}
	
	@Override
	protected void onPause() {		
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
	}
	
	@Override
	protected void endActivity(String msg) {
		
		super.endActivity(msg);
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
