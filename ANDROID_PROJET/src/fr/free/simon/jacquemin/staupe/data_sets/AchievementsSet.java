package fr.free.simon.jacquemin.staupe.data_sets;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.free.simon.jacquemin.staupe.container.Achievement;

/**
 * Created by Simon on 21/01/2015.
 */
public class AchievementsSet {
    private static List<Achievement> achievements;

    public static List<Achievement> getAchievements(Context context){
        if(achievements == null){
            // Todo Create all achievements
            achievements = new LinkedList<Achievement>();
            achievements.add(new Achievement(
                    "Insectator",
                    "Kill at least 6 insect",
                    StatsSet.EStats.STATS_NB_INSECT_KILL,
                    Achievement.EAchievementSign.SUPERIOR,
                    6));
        }

        return achievements;
    }

    public static List<Achievement> getUnlockAchievements(Context context){
        List<Achievement> achievementsUnlocked = new LinkedList<Achievement>();

        for (Achievement a : getAchievements(context)){
            if(a.isUnlocked())
                achievementsUnlocked.add(a);
        }

        return achievementsUnlocked;
    }

    public static Map<Achievement, Integer> getLockAchievements(Context context){
        Map<Achievement, Integer> achievementsLocked = new LinkedHashMap<Achievement, Integer>();

        for (Achievement a : getAchievements(context)){
            if(!a.isUnlocked())
                achievementsLocked.put(a, a.getPercent(context));
        }

        return achievementsLocked;
    }
}
