package fr.free.simon.jacquemin.staupe.SGM;

import android.app.Activity;

import fr.free.simon.jacquemin.staupe.SGMGameManager;

/**
 * Created by Simon on 22/11/2014.
 */
public abstract class SGMStatisticsManager extends SGMPreferenceManager {

    public void addStatistics(String a_key, String a_default, int a_value){
        int previousValue = Integer.parseInt(getPref(SGMGameManager.FILE_STATS,a_key, a_default));
        setPref(SGMGameManager.FILE_STATS,a_key,Integer.toString(previousValue + a_value));
    }

    public void setStatistics(String a_key, int a_value){
        setPref(SGMGameManager.FILE_STATS,a_key,Integer.toString(a_value));
    }
}
