package fr.free.simon.jacquemin.staupe.data_sets;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.free.simon.jacquemin.staupe.SGMGameManager;
import fr.free.simon.jacquemin.staupe.container.Achievement;

/**
 * Created by Simon on 21/01/2015.
 */
public class StatsSet {
    public enum EStats{
        STATS_ALL_UNIQUE_MAUL,
        STATS_NB_GAMES_WIN,
        STATS_NB_GAMES_LOST,
        STATS_NB_INSECT_KILL,
        STATS_NB_INSECT_NOT_KILL,
        STATS_DATE_INSTALLATION,
        STATS_ALL_MINES,
        STATS_ALL_STARS
    }

    private static Map<EStats, Integer> stats = null;

    public static Map<EStats, Integer> getStats(Context context){
        if(stats == null){
            String allStats = getPref(context, SGMGameManager.FILE_STATS, SGMGameManager.FILE_ALL_STATS, "");

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            Type type = new TypeToken<Map<EStats, Integer>>(){}.getType();
            stats = new HashMap<EStats, Integer>();
            stats = gson.fromJson(allStats, type);
        }

        return stats;
    }

    public static EStats getStatFromString(Context context, String statName){
        EStats stat = null;

        for (EStats s : getStats(context).keySet()){
            if(s.toString().compareToIgnoreCase(statName) == 0)
                stat = s;
        }

        return stat;
    }

    public static void setStat(Context context, EStats stat, int valueStat){
        getStats(context).put(stat, valueStat);

        saveStat(context);
    }

    public static void addStat(Context context, EStats stat, int valueAddToStat){
        if(!getStats(context).containsKey(stat))
            getStats(context).put(stat, valueAddToStat);
        else
            getStats(context).put(stat, getStats(context).get(stat));

        saveStat(context);
    }

    public static void saveStat(Context context){
        Gson gson = new Gson();
        String userJson = gson.toJson(getStats(context));
        setPref(context, SGMGameManager.FILE_STATS, SGMGameManager.FILE_ALL_STATS, userJson);
    }

    public static void setPref(Context context, String file, String key, String value) {
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
}
