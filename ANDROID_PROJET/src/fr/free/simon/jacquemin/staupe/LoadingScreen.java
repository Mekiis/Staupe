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
import java.util.concurrent.TimeUnit;

import fr.free.simon.jacquemin.staupe.SGM.SGMActivity;
import fr.free.simon.jacquemin.staupe.SGM.UnlockLevel;
import fr.free.simon.jacquemin.staupe.container.Level;
import fr.free.simon.jacquemin.staupe.container.data.EData;
import fr.free.simon.jacquemin.staupe.utils.ReadLevelFile;
import io.brothers.sgm.SGMADisplayableStat;
import io.brothers.sgm.SGMStatManager;
import io.brothers.sgm.Unlockable.Conditions.SGMCValue;
import io.brothers.sgm.Unlockable.Conditions.SGMCOr;
import io.brothers.sgm.Unlockable.SGMAchievement;
import io.brothers.sgm.Unlockable.SGMAchievementManager;
import io.brothers.sgm.Unlockable.SGMUnlockManager;
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

        // Create Achievement
        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "J_1",
            "Jardinier en herbe",
            "Obtenir 1 étoile sur au moins 1 niveau",
            new SGMCOr(
                    new SGMCValue(EData.STATS_LEVEL_ONE_STAR.toString(), 1),
                    new SGMCValue(EData.STATS_LEVEL_TWO_STAR.toString(), 1),
                    new SGMCValue(EData.STATS_LEVEL_THREE_STAR.toString(), 1)
            ),
            false)
        );

        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "J_2",
            "Jardinier du dimanche",
            "Obtenir 2 étoiles sur au moins 2 niveaux",
            new SGMCOr(
                    new SGMCValue(EData.STATS_LEVEL_TWO_STAR.toString(), 2),
                    new SGMCValue(EData.STATS_LEVEL_THREE_STAR.toString(), 2)
            ),
            false)
        );

        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "J_3",
            "Jardinier expert",
            "Obtenir 3 étoile sur au moins 3 niveaux",
            new SGMCValue(EData.STATS_LEVEL_THREE_STAR.toString(), 3),
            false)
        );

        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "C_1",
            "Début du défi",
            "Finir au moins 5 niveaux différents avec 3 étoiles",
            new SGMCValue(EData.STATS_LEVEL_THREE_STAR.toString(), 5),
            false)
        );

        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "C_2",
            "La routine",
            "Finir au moins 15 niveaux différents avec 3 étoiles",
            new SGMCValue(EData.STATS_LEVEL_THREE_STAR.toString(), 15),
            false)
        );

        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "C_3",
            "Interminable mission",
            "Finir au moins 20 niveaux différents avec 3 étoiles",
            new SGMCValue(EData.STATS_LEVEL_THREE_STAR.toString(), 20),
            false)
        );

        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "S_1",
            "Etoile naissante",
            "Obtenir au moins 10 étoiles",
            new SGMCValue(EData.STATS_ALL_STARS.toString(), 10),
            false)
        );

        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "S_2",
            "Etoile filante",
            "Obtenir au moins 25 étoiles",
            new SGMCValue(EData.STATS_ALL_STARS.toString(), 25),
            false)
        );

        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "S_3",
            "Etoile brillante",
            "Obtenir au moins 50 étoiles",
            new SGMCValue(EData.STATS_ALL_STARS.toString(), 50),
            false)
        );

        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "B_1",
            "Formation",
            "Empecher 5 taupes de sortir",
            new SGMCValue(EData.STATS_ALL_UNIQUE_MAUL.toString(), 5),
            false)
        );

        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "B_2",
            "Blocus",
            "Empecher 20 taupes de sortir",
            new SGMCValue(EData.STATS_ALL_UNIQUE_MAUL.toString(), 20),
            false)
        );

        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "B_3",
            "Stratège",
            "Empecher 40 taupes de sortir",
            new SGMCValue(EData.STATS_ALL_UNIQUE_MAUL.toString(), 40),
            false)
        );

        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "R_INIT",
            "Challenger",
            "Réinitilializer au moins 1 fois les étoiles",
            new SGMCValue(EData.STATS_NB_REINITIALIZE.toString(), 1),
            false)
        );

        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "I_FLY",
            "Tapette",
            "Ecrasez au moins 20 mouches",
            new SGMCValue(EData.STATS_NB_FLY_KILL.toString(), 20),
            false)
        );

        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "I_KILL_1",
            "Nettoyeur",
            "Ecrasez au moins 1 insecte",
            new SGMCValue(EData.STATS_NB_INSECT_KILL.toString(), 1),
            false)
        );

        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "I_KILL_2",
            "Insecticide",
            "Ecrasez au moins 10 insectes",
            new SGMCValue(EData.STATS_NB_INSECT_KILL.toString(), 10),
            false)
        );

        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "I_KILL_3",
            "Ecrabouillator",
            "Ecrasez au moins 50 insecte",
            new SGMCValue(EData.STATS_NB_INSECT_KILL.toString(), 50),
            false)
        );

        SGMAchievementManager.getInstance().addAchievement(new SGMAchievement(
            "F",
            "Avoir la main verte",
            "Finir tous les niveaux avoir 3 étoiles",
            new SGMCValue(EData.STATS_LEVEL_THREE_STAR.toString(), 22),
            false)
        );


        // Create stats
        SGMStatManager.getInstance().addStatDisplayable(new SGMADisplayableStat(
                "INFESTATION",
                getString(R.string.stats_ratioMaulPerLevel),
                ""
        ) {
            @Override
            public float getValue(String userId) {
                float nbMaul = SGMStatManager.getInstance().getStatValueForUser(userId, EData.STATS_ALL_UNIQUE_MAUL.toString());
                float nbLevelWin = SGMStatManager.getInstance().getStatValueForUser(userId, EData.STATS_NB_GAMES_WIN.toString());
                return (nbLevelWin > 0f ? nbMaul / nbLevelWin * 1f : 0f);
            }

            @Override
            public String getValueFormat(String userId) {
                return String.format("%.0f", getValue(userId));
            }
        });
        SGMStatManager.getInstance().addStatDisplayable(new SGMADisplayableStat(
                "MARK",
                getString(R.string.stats_ratioWinLose),
                ""
        ) {
            @Override
            public float getValue(String userId) {
                float nbLevelWin = SGMStatManager.getInstance().getStatValueForUser(userId, EData.STATS_NB_GAMES_WIN.toString());
                float nbLevelLose = SGMStatManager.getInstance().getStatValueForUser(userId, EData.STATS_NB_GAMES_LOST.toString());
                return ((nbLevelWin + nbLevelLose) > 0f ? (nbLevelWin / (nbLevelWin + nbLevelLose)) * 10f : 5f);
            }

            @Override
            public String getValueFormat(String userId) {
                return String.format("%.1f", getValue(userId)) + "/10.0";
            }
        });
        SGMStatManager.getInstance().addStatDisplayable(new SGMADisplayableStat(
                "WIN",
                getString(R.string.stats_nbLevelWin),
                ""
        ) {
            @Override
            public float getValue(String userId) {
                float nbLevelWin = SGMStatManager.getInstance().getStatValueForUser(userId, EData.STATS_NB_GAMES_WIN.toString());
                return nbLevelWin;
            }

            @Override
            public String getValueFormat(String userId) {
                return String.format("%.0f", getValue(userId));
            }
        });
        SGMStatManager.getInstance().addStatDisplayable(new SGMADisplayableStat(
                "MAUL",
                getString(R.string.stats_nbUniqueMaul),
                ""
        ) {
            @Override
            public float getValue(String userId) {
                float nbMaul = SGMStatManager.getInstance().getStatValueForUser(userId, EData.STATS_ALL_UNIQUE_MAUL.toString());
                return nbMaul;
            }

            @Override
            public String getValueFormat(String userId) {
                return String.format("%.0f", getValue(userId));
            }
        });
        SGMStatManager.getInstance().addStatDisplayable(new SGMADisplayableStat(
                "RATIO_INSECT",
                getString(R.string.stats_ratioInsectKill),
                ""
        ) {
            @Override
            public float getValue(String userId) {
                float nbInsectKill = SGMStatManager.getInstance().getStatValueForUser(userId, EData.STATS_NB_INSECT_KILL.toString());
                float nbInsectNotKill = SGMStatManager.getInstance().getStatValueForUser(userId, EData.STATS_NB_INSECT_NOT_KILL.toString());
                return (nbInsectKill + nbInsectNotKill > 0f ? (nbInsectKill / (nbInsectKill + nbInsectNotKill)) * 10f : 0f);
            }

            @Override
            public String getValueFormat(String userId) {
                return String.format("%.1f", getValue(userId)) + "/10.0";
            }
        });
        SGMStatManager.getInstance().addStatDisplayable(new SGMADisplayableStat(
                "TIME_GAME",
                getString(R.string.stats_timeOnGame),
                ""
        ) {
            @Override
            public float getValue(String userId) {
                long dateInstallation = (long) SGMStatManager.getInstance().getStatValueForUser(userId, EData.STATS_DATE_INSTALLATION.toString());
                return (float) dateToLong(now()) - dateInstallation;
            }

            @Override
            public String getValueFormat(String userId) {
                long seconds = (long) getValue(userId) / 1000; // your date
                long day = TimeUnit.SECONDS.toDays(seconds);
                long hours = TimeUnit.SECONDS.toHours(seconds);
                long minute = TimeUnit.SECONDS.toMinutes(seconds);
                long second = TimeUnit.SECONDS.toSeconds(seconds);
                long month = (long) Math.ceil(day / 30);
                long year = (long) Math.ceil(day / 365);
                if (year > 0) {
                    return "Voyageur du temps";
                } else if (month > 0) {
                    return "Sage";
                } else if (day > 0) {
                    return "Herboriste";
                } else if (hours > 0) {
                    return "Jardinier";
                } else if (minute > 0) {
                    return "Jeune pousse";
                } else {
                    return "Graine";
                }
            }
        });

        SGMUser user = SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID);
        if(SGMUserManager.getInstance().getUser(SGMGameManager.USER_ID) == null)
            user = new SGMUser(getApplicationContext(), SGMGameManager.USER_ID, true);

        if(!SGMStatManager.getInstance().isStatExistForUser(user.id, EData.STATS_DATE_INSTALLATION.toString())){
            SGMStatManager.getInstance().setValueForStat(user.id, EData.STATS_DATE_INSTALLATION.toString(), (float) dateToLong(now()));
        }

        ReadLevelFile f = new ReadLevelFile();
        ArrayList<Level> allLevels = f.buildLevel(getApplicationContext(),
                "lvl.txt");
        for (Level level : allLevels){
            UnlockLevel unlockLevel = new UnlockLevel(level, new SGMCValue(EData.STATS_ALL_STARS.toString(), level.lock));
            SGMUnlockManager.getInstance().addUnlocked(unlockLevel);
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