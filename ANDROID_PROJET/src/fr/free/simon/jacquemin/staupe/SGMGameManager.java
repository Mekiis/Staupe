package fr.free.simon.jacquemin.staupe;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import fr.free.simon.jacquemin.staupe.SGM.SGMStatisticsManager;

public class SGMGameManager extends SGMStatisticsManager {
    private static SGMGameManager mInstance = null;


	public static final String RESPOND_NAME = "RESPOND_NAME";
	
	public static final int CODE_RETOUR = 0;
	public static final int RESULT_OK = 1;
	public static final int RESULT_CANCELED = -1;

	public static final String START_ACTION = "SA";

	public static final String LEVEL = "LEVEL_";
	public static final String WORLD = "WORLD_";
	
	public static final String FILE_LEVELS = "F_LEVELS";
	public static final String STARS = "L_STARS_LEVEL_";
	public static final String STATE = "L_LAST_STATE_";
	
	public static final String FILE_BONUS = "F_BONUS";
	public static final String BONUS_AFFICHE_TAUPE_NB = "B_A_T";
	public static final int BONUS_AFFICHE_TAUPE_DEFAULT = 10;

	public static final String FILE_STATS = "F_STATS";
	public static final String STATS_ALL_STARS = "S_ALL_STARS";
	public static final String STATS_ALL_MINES = "S_ALL_MINES";
	public static final String STATS_ALL_UNIQUE_MAUL = "S_ALL_UNIQUE_TAUPE";
	public static final String STATS_NB_GAMES_LOST = "S_NB_GAMES_LOST";
	public static final String STATS_NB_GAMES_WIN = "S_NB_GAMES_WIN";
    public static final String STATS_NB_INSECT_KILL = "S_NB_INSECT_KILL";
    public static final String STATS_NB_INSECT_NOT_KILL = "S_NB_INSECT_NOT_KILL";
    public static final String STATS_DATE_INSTALLATION = "S_DATE_INSTALLATION";

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public int[] listWorld = { 1, 1, 1 };
	public String[] listNom = { "pot_fleur_", "tondeuse_", "grenouille_" };
	public int[] listDuration = { 25, 18, 39 };
	public double[] listRatio = { 1.46, 1.46, 1.46 };
	
	public String[] listInsectNom = { "mouche_", "abeille_", "libellule_", "papillon_" };
	public int[] listInsectDurationLife = { 27, 30, 30, 72 };
	public int[] listInsectDurationDeath = { 7, 7, 7, 7 };
	
	public ArrayList<AnimationDrawable> listAnimation = new ArrayList<AnimationDrawable>();
	
	private int world = -1;
    private int level = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Lancement de l'home uniquement si la demande viens du splashscreen
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

		if (requestCode == CODE_RETOUR) {

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

//		if (nom.equalsIgnoreCase("Choix") == true
//				|| nom.equalsIgnoreCase("AccueilPlay") == true
//				|| nom.equalsIgnoreCase("LevelReturn") == true) {
//			Intent intent = new Intent(this, SelectWorld.class);
//			startActivityForResult(intent, CODE_RETOUR);
//		} else
		if (nom.equalsIgnoreCase("Loading") == true) {
			Intent intent = new Intent(this, LoadingScreen.class);
			startActivityForResult(intent, CODE_RETOUR);
		} else if (nom.equalsIgnoreCase("JeuReturn") == true
				|| nom.equalsIgnoreCase("WorldChoose") == true
				|| nom.equalsIgnoreCase("Choix") == true
				|| nom.equalsIgnoreCase("AccueilPlay") == true) {
			Intent intent = new Intent(this, SelectLevel.class);
			intent.putExtra(SGMGameManager.LEVEL, level);
			intent.putExtra(SGMGameManager.WORLD, world);
			startActivityForResult(intent, CODE_RETOUR);
		} else if (nom.equalsIgnoreCase("Game") == true
				|| nom.equalsIgnoreCase("LevelOk") == true) {
			Intent intent = new Intent(this, Game.class);
			intent.putExtra(SGMGameManager.LEVEL, level);
			startActivityForResult(intent, CODE_RETOUR);
		} else if (nom.equalsIgnoreCase("Home") == true
				|| nom.equalsIgnoreCase("StatsBack") == true
				|| nom.equalsIgnoreCase("StoryBack") == true
				|| nom.equalsIgnoreCase("WorldReturn") == true
				|| nom.equalsIgnoreCase("OptionsBack") == true
				|| nom.equalsIgnoreCase("TutorialBack") == true
				|| nom.equalsIgnoreCase("LevelReturn") == true
				|| nom.equalsIgnoreCase("LoadingOk") == true) {
			Intent intent = new Intent(this, Home.class);
			startActivityForResult(intent, CODE_RETOUR);
		} else if (nom.equalsIgnoreCase("AccueilRules") == true) {
			Intent intent = new Intent(this, Story.class);
			startActivityForResult(intent, CODE_RETOUR);
		} else if (nom.equalsIgnoreCase("AccueilStats") == true) {
			Intent intent = new Intent(this, Stats.class);
			startActivityForResult(intent, CODE_RETOUR);
		} else if (nom.equalsIgnoreCase("AccueilTutorial") == true) {
			Intent intent = new Intent(this, Tutorial.class);
			startActivityForResult(intent, CODE_RETOUR);
		}
	}

    public static SGMGameManager instance(){
        if(mInstance == null)
            mInstance = new SGMGameManager();

        return mInstance;
    }
}