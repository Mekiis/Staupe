package fr.free.simon.jacquemin.staupe;

import java.util.ArrayList;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

import io.brothers.sgm.Unlockable.SGMAchievement;
import io.brothers.sgm.Unlockable.SGMAchievementManager;
import io.brothers.sgm.User.SGMUser;
import io.brothers.sgm.User.SGMUserManager;

public class SGMGameManager extends FragmentActivity implements SGMAchievementManager.SGMAchievementEventListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
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

    // Connection
    private static boolean mResolvingError = false;
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String DIALOG_ERROR = "dialog_error";
    private static String lastTryConnectUserId = "";
    private static final String STATE_RESOLVING_ERROR = "resolving_error";

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

        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);

	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
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

        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                SGMUser user = SGMUserManager.getInstance().getUser(lastTryConnectUserId);
                if(user == null)
                    return;

                // Make sure the app is not already connected or attempting to connect
                if (!user.getApiClient().isConnecting() &&
                        !user.getApiClient().isConnected()) {
                    connectUser(lastTryConnectUserId);
                }
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

    public void connectUser(String userId){
        SGMUser user = SGMUserManager.getInstance().getUser(userId);

        if(user == null)
            return;

        lastTryConnectUserId = userId;

        if (!mResolvingError) {
            user.connectToGooglePlay(getApplicationContext(), this, this);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {

    }

    @Override
    public void onConnectionSuspended(int cause) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.

            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    // The rest of this code is all about building the error dialog

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((SGMGameManager)getActivity()).onDialogDismissed();
        }
    }

    @Override
    public void achievementsSynchronized(SGMAchievementManager.EWaySynchronize way, boolean isSynchronized) {

    }
}