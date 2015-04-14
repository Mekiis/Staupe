package fr.free.simon.jacquemin.staupe;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import fr.free.simon.jacquemin.staupe.SGM.SGMActivity;
import fr.free.simon.jacquemin.staupe.container.data.EData;
import io.brothers.sgm.SGMAStat;
import io.brothers.sgm.SGMStatManager;
import io.brothers.sgm.Unlockable.SGMAchievement;
import io.brothers.sgm.Unlockable.SGMAchievementManager;
import io.brothers.sgm.Unlockable.SGMCondition;
import io.brothers.sgm.User.SGMUser;
import io.brothers.sgm.User.SGMUserManager;

public class LoadingScreen extends SGMActivity {
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

        // Todo Resolve the bug that player stats is not correctly registered

        // Create Achievement
        // Todo Create the achievements
        /*
        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
                "TEST",
                "Test",
                "Ceci est un test",
                new ArrayList<SGMCondition>(Arrays.asList(
                        new SGMCondition(EData.PLAYED.toString(), 6),
                        new SGMCondition("MARK", 0)
                )), false));

        */
        // Create stats
        SGMStatManager.getInstance().addStatCustom(new SGMAStat(
                "INFESTATION",
                getString(R.string.stats_ratioMaulPerLevel),
                ""
        ) {
            @Override
            public float getValue(SGMUser user) {
                float nbMaul = SGMStatManager.getInstance().getStatValueForUser(user, EData.STATS_ALL_UNIQUE_MAUL.toString());
                float nbLevelWin = SGMStatManager.getInstance().getStatValueForUser(user, EData.STATS_NB_GAMES_WIN.toString());
                return (nbLevelWin > 0f ? nbMaul/nbLevelWin*1f : 0f);
            }

            @Override
            public String getValueFormat(SGMUser user) {
                return String.format("%.0f", getValue(user));
            }
        });
        SGMStatManager.getInstance().addStatCustom(new SGMAStat(
                "MARK",
                getString(R.string.stats_ratioWinLose),
                ""
        ) {
            @Override
            public float getValue(SGMUser user) {
                float nbLevelWin = SGMStatManager.getInstance().getStatValueForUser(user, EData.STATS_NB_GAMES_WIN.toString());
                float nbLevelLose = SGMStatManager.getInstance().getStatValueForUser(user, EData.STATS_NB_GAMES_LOST.toString());
                return ((nbLevelWin + nbLevelLose) > 0f ? (nbLevelWin / (nbLevelWin + nbLevelLose)) * 10f : 5f);
            }

            @Override
            public String getValueFormat(SGMUser user) {
                return String.format("%.1f", getValue(user))+"/10.0";
            }
        });
        SGMStatManager.getInstance().addStatCustom(new SGMAStat(
                "WIN",
                getString(R.string.stats_nbLevelWin),
                ""
        ) {
            @Override
            public float getValue(SGMUser user) {
                float nbLevelWin = SGMStatManager.getInstance().getStatValueForUser(user, EData.STATS_NB_GAMES_WIN.toString());
                return nbLevelWin;
            }

            @Override
            public String getValueFormat(SGMUser user) {
                return String.format("%.0f", getValue(user));
            }
        });
        SGMStatManager.getInstance().addStatCustom(new SGMAStat(
                "MAUL",
                getString(R.string.stats_nbUniqueMaul),
                ""
        ) {
            @Override
            public float getValue(SGMUser user) {
                float nbMaul = SGMStatManager.getInstance().getStatValueForUser(user, EData.STATS_ALL_UNIQUE_MAUL.toString());
                return nbMaul;
            }

            @Override
            public String getValueFormat(SGMUser user) {
                return String.format("%.0f", getValue(user));
            }
        });
        SGMStatManager.getInstance().addStatCustom(new SGMAStat(
                "RATIO_INSECT",
                getString(R.string.stats_ratioInsectKill),
                ""
        ) {
            @Override
            public float getValue(SGMUser user) {
                float nbInsectKill = SGMStatManager.getInstance().getStatValueForUser(user, EData.STATS_NB_INSECT_KILL.toString());
                float nbInsectNotKill = SGMStatManager.getInstance().getStatValueForUser(user, EData.STATS_NB_INSECT_NOT_KILL.toString());
                return (nbInsectKill + nbInsectNotKill > 0f ? (nbInsectKill / (nbInsectKill + nbInsectNotKill)) * 10f : 0f);
            }

            @Override
            public String getValueFormat(SGMUser user) {
                return String.format("%.1f", getValue(user))+"/10.0";
            }
        });
        SGMStatManager.getInstance().addStatCustom(new SGMAStat(
                "TIME_GAME",
                getString(R.string.stats_timeOnGame),
                ""
        ) {
            @Override
            public float getValue(SGMUser user) {
                long dateInstallation = (long) SGMStatManager.getInstance().getStatValueForUser(user, EData.STATS_DATE_INSTALLATION.toString());
                return (float) dateToLong(now()) - dateInstallation;
            }

            @Override
            public String getValueFormat(SGMUser user) {
                Date date = new Date((long) getValue(user)); // your date
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int hours = cal.get(Calendar.HOUR_OF_DAY);
                int minutes = cal.get(Calendar.MINUTE);
                if(year > 0){
                    return "Voyageur du temps";
                } else if(month > 0){
                    return "Sage";
                } else if(day > 0){
                    return "Herboriste";
                } else if(hours > 0){
                    return "Jardinier";
                } else if(minutes > 0){
                    return "Jeune pousse";
                } else {
                    return "Graine";
                }
            }
        });

        SGMUser user = SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID);
        if(SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID) == null)
            user = new SGMUser(getApplicationContext(), SGMGameManager.USER_ID, true);

        if(SGMStatManager.getInstance().isStatExistForUser(user, EData.STATS_DATE_INSTALLATION.toString())){
            SGMStatManager.getInstance().setStatDataForUser(user, EData.STATS_DATE_INSTALLATION.toString(), (float) dateToLong(now()));
        }
		
		new LoadingImages(getPackageName(), getResources()).execute();
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    if(myHandler != null)
	        myHandler.removeCallbacks(myRunnable); // We stop callbacks
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

    @Override
    public String getNameActivity() {
        return "Loading";
    }
}