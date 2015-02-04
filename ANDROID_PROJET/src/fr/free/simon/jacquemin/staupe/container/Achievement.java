package fr.free.simon.jacquemin.staupe.container;

import android.content.Context;

import fr.free.simon.jacquemin.staupe.data_sets.StatsSet;

/**
 * Created by Simon on 21/01/2015.
 */
public class Achievement {
    public enum EAchievementSign{
        SUPERIOR,
        INFERIOR,
    }

    public String title = "";
    public String desc = "";
    public StatsSet.EStats stat = null;
    public EAchievementSign sign = null;
    public int statNb = 0;

    public Achievement(String title, String desc, StatsSet.EStats stat, EAchievementSign sign, int statNb) {
        this.title = title;
        this.desc = desc;
        this.stat = stat;
        this.sign = sign;
        this.statNb = statNb;
    }

    public boolean isUnlocked(){
        // Todo Manage unlocked Achievement
        return true;
    }

    public int getPercent(Context context){
        int percent = 0;

        if(stat == null || sign == null)
            return percent;

        if(sign == EAchievementSign.SUPERIOR){
            if(StatsSet.getStats(context).get(stat) > statNb)
                percent = 100;
            else
                percent = StatsSet.getStats(context).get(stat) / statNb * 100;
        } else if(sign == EAchievementSign.INFERIOR){
            if(StatsSet.getStats(context).get(stat) < statNb)
                percent = 100;
            else
                percent = (StatsSet.getStats(context).get(stat) - statNb) / statNb * 100;
        }

        return percent;
    }

}
