package fr.free.simon.jacquemin.staupe.SGM;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fr.free.simon.jacquemin.staupe.SGMGameManager;

public abstract class SGMActivity extends SGMPreferenceManager {
	protected static DisplayMetrics metrics = new DisplayMetrics();
	protected static Typeface font = null;
	protected Intent intent = getIntent();
	protected int behaviorQuitButton = 0;

    private static String nameActivity = "Generic";

	protected void init() {
		// Measure the metrics of the screen
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		// Get the font used for all the text displayed
		font = Typeface.createFromAsset(getAssets(),
				"fonts/Barthowheel Regular.ttf");
	}

	// 2.0 and above
	@Override
	abstract public void onBackPressed();

	// Before 2.0
	@Override
	abstract public boolean onKeyDown(int keyCode, KeyEvent event);

	public void actionClick(View v) {
		// Need to be override to react to the click
	}

	protected void endActivity(String msg) {
		// Get the instance of the Intent
		intent = getIntent();

		// Add the name of the Activity
		intent.putExtra(SGMGameManager.RESPOND_NAME, getNameActivity() + msg);
		
		// Return a RESULT_OK as a result of the activity
		setResult(SGMGameManager.RESULT_OK, intent);

		// Finish the current activity
		finish();
	}

    public String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(SGMGameManager.DATE_FORMAT);
        return sdf.format(cal.getTime());
    }

    public long dateToLong(String date){
        SimpleDateFormat f = new SimpleDateFormat(SGMGameManager.DATE_FORMAT);
        Date d = null;
        long milliseconds = 0;
        try {
            d = f.parse(date);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            Log.e(this.getClass().getName(), "Error - While parsing the date convert to a long millisecond value");
        }
        return milliseconds;
    }

    abstract public String getNameActivity();

    protected ImageView createImageView(RelativeLayout root){
        ImageView imageView = new ImageView(this);
        RelativeLayout.LayoutParams vp =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(vp);
        root.addView(imageView);
        return imageView;
    }
}
