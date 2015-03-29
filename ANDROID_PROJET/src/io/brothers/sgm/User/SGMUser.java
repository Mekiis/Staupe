package io.brothers.sgm.User;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import io.brothers.sgm.Unlockable.SGMAchievementManager;
import io.brothers.sgm.Unlockable.SGMUnlockManager;

/**
 * Created by Simon on 16/03/2015.
 */
public class SGMUser {
    public static final String KEY_USERS_FILE = "USERS_FILES";
    public static final String KEY_USER_ID = "USER_ID_";

    public String id;
    private SGMSavedData savedData;
    private boolean autoSave;
    private Context context;

    private SGMUnlockManager.SGMUnlockEventListener SGMUnlockEventListener = null;
    private SGMAchievementManager.SGMAchievementEventListener SGMAchievementEventListener = null;

    public SGMUser(Context context, String id, boolean autoSave){
        this.context = context;
        this.id = id;
        this.savedData = load(context, this.id);
        this.autoSave = autoSave;

        SGMUserManager.getInstance().registerUser(this);
    }

    public SGMUser(Context context, String id, boolean autoSave, SGMUnlockManager.SGMUnlockEventListener SGMUnlockEventListener){
        this.context = context;
        this.id = id;
        this.savedData = load(context, this.id);
        this.autoSave = autoSave;
        this.SGMUnlockEventListener = SGMUnlockEventListener;

        SGMUserManager.getInstance().registerUser(this);
    }

    public SGMUser(Context context, String id, boolean autoSave, SGMAchievementManager.SGMAchievementEventListener SGMAchievementEventListener){
        this.context = context;
        this.id = id;
        this.savedData = load(context, this.id);
        this.autoSave = autoSave;
        this.SGMAchievementEventListener = SGMAchievementEventListener;

        SGMUserManager.getInstance().registerUser(this);
    }

    public SGMUser(Context context, String id, boolean autoSave, SGMUnlockManager.SGMUnlockEventListener SGMUnlockEventListener, SGMAchievementManager.SGMAchievementEventListener SGMAchievementEventListener){
        this.context = context;
        this.id = id;
        this.savedData = load(context, this.id);
        this.autoSave = autoSave;
        this.SGMUnlockEventListener = SGMUnlockEventListener;
        this.SGMAchievementEventListener = SGMAchievementEventListener;

        SGMUserManager.getInstance().registerUser(this);
    }

    public void save(Context context) {
        Gson gson = new Gson();
        String data = gson.toJson(savedData);
        setPref(context, KEY_USERS_FILE, KEY_USER_ID + this.id, data);
    }

    private static SGMSavedData load(Context context, String id){
        String data = getPref(context, KEY_USERS_FILE, KEY_USER_ID + id, "");
        Gson gson = new Gson();
        if(!data.equalsIgnoreCase(""))
            return gson.fromJson(data, SGMSavedData.class);
        else
            return new SGMSavedData();
    }


    public void setPref(Context context, String file, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(file, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPref(Context context, String file, String key, String defaultValue) {
        String s = key;
        SharedPreferences preferences = context.getSharedPreferences(file, 0);
        return preferences.getString(s, defaultValue);
    }

    public SGMUnlockManager.SGMUnlockEventListener getSGMUnlockEventListener(){
        return SGMUnlockEventListener;
    }

    public SGMAchievementManager.SGMAchievementEventListener getSGMAchievementEventListener(){
        return SGMAchievementEventListener;
    }

    public SGMSavedData getAllSavedData() {
        return savedData;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }
}
