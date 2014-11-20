package fr.free.simon.jacquemin.staupe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import fr.free.simon.jacquemin.staupe.SGM.utils.SGMTimer;

public class SplashScreen extends Activity {
	protected float _splashTime = 5f;
	private SGMTimer timerSplashScreen = null;
	
	/** Chargement de l'Activity */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		timerSplashScreen = new SGMTimer();
		timerSplashScreen.execute(_splashTime, false, new Runnable() {
			
			@Override
			public void run() {
				Intent i = new Intent(getBaseContext(),
						SGMGameManager.class);
				i.putExtra(SGMGameManager.START_ACTION, "launch");
				startActivity(i);

				// Remove activity
				finish();
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/**
		 * Si l'utilisateur fait un mouvement de haut en bas on passe �
		 * l'Activity principale
		 */
		if (event.getAction() == MotionEvent.ACTION_UP) {
			timerSplashScreen.removeTime(1f);
		}
		return true;
	}
}