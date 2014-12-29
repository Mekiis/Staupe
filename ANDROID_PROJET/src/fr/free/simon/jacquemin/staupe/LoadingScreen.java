package fr.free.simon.jacquemin.staupe;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

public class LoadingScreen extends Activity {	
	private ImageView loadingBg = null;
	private float angle = 0f;

	private Handler myHandler;
	private Runnable myRunnable = new Runnable() {
		@Override
		public void run() {
			// Code to execute periodically
			float newAngle = (angle+1.5f)%360f;
			rotate(angle, newAngle);
			angle = newAngle;
			myHandler.postDelayed(this,20);
	    }
	};
	
	private void rotate(float degreeInitial, float degree) {
	    RotateAnimation rotateAnim = new RotateAnimation(degreeInitial, degree,
	            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
	            RotateAnimation.RELATIVE_TO_SELF, 0.5f);

	    rotateAnim.setDuration(0);
	    rotateAnim.setFillAfter(true);
	    loadingBg.startAnimation(rotateAnim);
	}
	
	/** Chargement de l'Activity */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		
		loadingBg = (ImageView) findViewById(R.id.loading_bg);
		
		myHandler = new Handler();
	    myHandler.postDelayed(myRunnable,10);
		
		new LoadingImages(getPackageName(), getResources()).execute();
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    if(myHandler != null)
	        myHandler.removeCallbacks(myRunnable); // On arrete le callback
	}
	
	private class LoadingImages extends AsyncTask<Void, Integer, Void> {

		private String packageName = "";
		private Resources resources = null;
		
		public LoadingImages(String packageName, Resources resources){
			this.packageName = packageName;
			this.resources = resources;
		}
		
        @Override
        protected Void doInBackground(Void... params) {
        	if(SGMGameManager.listAnimation.size() > 0 )
        		return null;
        	
        	for (int i = 0; i < SGMGameManager.listNom.length; i++) {
				SGMGameManager.listAnimation.add(loadAnim(
						SGMGameManager.listDuration[i],
						SGMGameManager.listNom[i]));
				publishProgress(i, SGMGameManager.listNom.length+SGMGameManager.listInsectNom.length*2);
			}

			for (int i = 0; i < SGMGameManager.listInsectNom.length; i++) {
				SGMGameManager.listAnimation.add(loadAnim(
						SGMGameManager.listInsectDurationLife[i],
						SGMGameManager.listInsectNom[i]));
				publishProgress(SGMGameManager.listNom.length+i*2, SGMGameManager.listNom.length+SGMGameManager.listInsectNom.length*2);
				SGMGameManager.listAnimation.add(loadAnim(
						SGMGameManager.listInsectDurationDeath[i],
						"sprotch_" + SGMGameManager.listInsectNom[i]));
				publishProgress(SGMGameManager.listNom.length+i*2+1, SGMGameManager.listNom.length+SGMGameManager.listInsectNom.length*2);
			}
		
            return null;
        }
        
        @Override
        protected void onProgressUpdate(Integer... values){
        	super.onProgressUpdate(values);        	
        }
        
        @Override 
        protected void onPostExecute(Void result) {
        	super.onPostExecute(result);
        	
        	endActivity("Ok");
        }
        
        private AnimationDrawable loadAnim(int nbFrame, String name) {
			AnimationDrawable container = new AnimationDrawable();
			container.setOneShot(true);
			container.stop();

			for (int i = 0; i < nbFrame; i++) {
				String nameLocal = name + (i + 1);
				int globeId = resources.getIdentifier(nameLocal,
						"drawable", packageName);
				
				Log.d("Staupe", "Size : "+resources.getDrawable(globeId).getMinimumHeight());

				container.addFrame(resources.getDrawable(globeId), 40);
			}

			return container;
		}
        
    }
	
	public void endActivity(String msg) {
		// Get the instance of the Intent
		Intent intent = getIntent();

		// Add the name of the Activity
		intent.putExtra(SGMGameManager.RESPOND_NAME, "Loading" + msg);
		
		// Return a RESULT_OK as a result of the activity
		setResult(SGMGameManager.RESULT_OK, intent);

		// Finish the current activity
		finish();
	}
}