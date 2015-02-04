package fr.free.simon.jacquemin.staupe.SGM;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Simon on 22/11/2014.
 */
public abstract class SGMPreferenceManager extends Activity {

    public String getPref(String file, String key, String defaultValue) {
        String s = key;
        SharedPreferences preferences = getSharedPreferences(file, 0);
        return preferences.getString(s, defaultValue);
    }

    public void setPref(String file, String key, String value) {
        SharedPreferences preferences = getSharedPreferences(file, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
