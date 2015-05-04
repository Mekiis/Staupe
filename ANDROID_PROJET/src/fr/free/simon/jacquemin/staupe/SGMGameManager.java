package fr.free.simon.jacquemin.staupe;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import io.brothers.sgm.Unlockable.SGMAchievement;
import io.brothers.sgm.Unlockable.SGMAchievementManager;

public class SGMGameManager extends Activity implements SGMAchievementManager.SGMAchievementEventListener {
	public static final String RESPOND_NAME = "RESPOND_NAME";

    public static final String USER_ID = "STAUPE";
	
	public static final int CALLBACK_ID = 0;
	public static final int RESULT_OK = 1;
	public static final int RESULT_CANCELED = -1;

	public static final String START_ACTION = "SA";

	public static final String LEVEL = "LEVEL_";
	public static final String WORLD = "WORLD_";
	
	public static final String FILE_LEVELS = "F_LEVELS";
	public static final String LVL_STARS = "L_STARS_LEVEL_";
	public static final String LVL_LAST_STATE = "L_LAST_STATE_";
    public static final String LVL_BEST_STATE = "L_BEST_STATE_";
	
	public static final String FILE_BONUS = "F_BONUS";
	public static final String BONUS_DISPLAY_MAUL_NB = "B_A_T";
	public static final int BONUS_DISPLAY_MAUL_DEFAULT = 10;

    public static final String FILE_OPTIONS = "F_OPTIONS";
    public static final String OPTION_ANIM_IG = "OPTION_ANIM_IG";
    public static final String OPTION_ANIM_MENU = "OPTION_ANIM_MENU";

    public static final String DATE_FORMAT = "HH:mm";

    public static int[] listWorld = { 1, 1, 1 };
	public static String[] listNom = { "pot_fleur_", "tondeuse_", "grenouille_" };
	public static int[] listDuration = { 25, 18, 39 };
	public static double[] listRatio = { 1.46, 1.46, 1.46 };
	
	public static String[] listInsectNom = { "mouche_", "abeille_", "libellule_", "papillon_" };
	public static int[] listInsectDurationLife = { 27, 30, 30, 72 };
	public static int[] listInsectDurationDeath = { 7, 7, 7, 7 };
	
	public static ArrayList<AnimationDrawable> listAnimation = new ArrayList<AnimationDrawable>();
	
	private int world = -1;
    private int level = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Launch loading if anyone asking
		Intent i = getIntent();
		if (i != null) {
			String s = i.getStringExtra(SGMGameManager.START_ACTION);
			if (s != null && s.compareToIgnoreCase("launch") == 0) {
				launchIntent("Loading");
				i.putExtra(SGMGameManager.START_ACTION, "");
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CALLBACK_ID) {

			if (resultCode == RESULT_OK) {

				if (data == null) {
					return;
				}
				String nom = data.getStringExtra(SGMGameManager.RESPOND_NAME);
				if (data.hasExtra(SGMGameManager.LEVEL)) {
					level = data.getIntExtra(SGMGameManager.LEVEL, -1);
				}
				if (data.hasExtra(SGMGameManager.WORLD)) {
					world = data.getIntExtra(SGMGameManager.WORLD, -1);
				}

				launchIntent(nom);

			} else if (resultCode == RESULT_CANCELED) {


			}
		}
	}

	public void launchIntent(String nom) {

		if (nom.equalsIgnoreCase("Loading") == true) {
			Intent intent = new Intent(this, LoadingScreen.class);
			startActivityForResult(intent, CALLBACK_ID);
		} else if (nom.equalsIgnoreCase("GameBack") == true
				|| nom.equalsIgnoreCase("WorldChoose") == true
				|| nom.equalsIgnoreCase("HomePlay") == true) {
			Intent intent = new Intent(this, SelectLevel.class);
			intent.putExtra(SGMGameManager.LEVEL, level);
			intent.putExtra(SGMGameManager.WORLD, world);
			startActivityForResult(intent, CALLBACK_ID);
		} else if (nom.equalsIgnoreCase("Game") == true
				|| nom.equalsIgnoreCase("SelectLevelOk") == true) {
			Intent intent = new Intent(this, InGame.class);
			intent.putExtra(SGMGameManager.LEVEL, level);
			startActivityForResult(intent, CALLBACK_ID);
		} else if (nom.equalsIgnoreCase("Home") == true
				|| nom.equalsIgnoreCase("StatsBack") == true
				|| nom.equalsIgnoreCase("StoryBack") == true
				|| nom.equalsIgnoreCase("WorldReturn") == true
				|| nom.equalsIgnoreCase("OptionsBack") == true
				|| nom.equalsIgnoreCase("TutorialBack") == true
				|| nom.equalsIgnoreCase("SelectLevelBack") == true
                || nom.equalsIgnoreCase("TutorialBack") == true
				|| nom.equalsIgnoreCase("LoadingOk") == true
                || nom.equalsIgnoreCase("AchievementsBack") == true) {
			Intent intent = new Intent(this, Home.class);
			startActivityForResult(intent, CALLBACK_ID);
		} else if (nom.equalsIgnoreCase("HomeRules")) {
			Intent intent = new Intent(this, Story.class);
			startActivityForResult(intent, CALLBACK_ID);
		} else if (nom.equalsIgnoreCase("HomeStats")) {
			Intent intent = new Intent(this, Stats.class);
			startActivityForResult(intent, CALLBACK_ID);
		} else if (nom.equalsIgnoreCase("HomeTutorial")) {
			Intent intent = new Intent(this, Tutorial.class);
			startActivityForResult(intent, CALLBACK_ID);
		} else if (nom.equalsIgnoreCase("HomeOptions")) {
            Intent intent = new Intent(this, Options.class);
            startActivityForResult(intent, CALLBACK_ID);
        } else if (nom.equalsIgnoreCase("HomeAchievements")) {
            Intent intent = new Intent(this, AchievementsActivity.class);
            startActivityForResult(intent, CALLBACK_ID);
        }
	}

    @Override
    public void unlock(SGMAchievement achievement) {

    }
}