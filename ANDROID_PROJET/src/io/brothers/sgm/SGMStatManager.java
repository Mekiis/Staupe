package io.brothers.sgm;

import java.util.ArrayList;
import java.util.List;

import io.brothers.sgm.Unlockable.SGMAchievementManager;
import io.brothers.sgm.Unlockable.SGMUnlockManager;
import io.brothers.sgm.User.SGMUser;

/**
 * Created by Simon on 25/03/2015.
 */
public class SGMStatManager {
    private List<SGMADisplayableStat> statsDisplayable = new ArrayList<>();
    private static SGMStatManager instance = null;

    public static SGMStatManager getInstance(){
        if(instance == null)
            instance = new SGMStatManager();

        return instance;
    }

    public void addOneForStat(SGMUser user, String key){
        addValueForStat(user, key, 1);
    }

    public void addValueForStat(SGMUser user, String key, float value){
        if(user.getAllSavedData().data.containsKey(key)){
            user.getAllSavedData().data.put(key, user.getAllSavedData().data.get(key) + value);
        } else {
            user.getAllSavedData().data.put(key, value);
        }

        if(user.isAutoSave())
            user.save(user.getContext());

        SGMUnlockManager.getInstance().majUnlockForData(key, user);
        SGMAchievementManager.getInstance().majAchievementForData(key, user);
    }

    public void setStatDataForUser(SGMUser user, String key, float value) {
        user.getAllSavedData().data.put(key, value);

        if(user.isAutoSave())
            user.save(user.getContext());

        SGMUnlockManager.getInstance().majUnlockForData(key, user);
        SGMAchievementManager.getInstance().majAchievementForData(key, user);
    }

    public boolean isStatExistForUser(SGMUser user, String key){
        boolean statExist = false;
        if(user.getAllSavedData().data.containsKey(key))
            statExist = true;

        for (SGMADisplayableStat stat : statsDisplayable){
            if(stat.id == key)
                statExist = true;
        }

        return statExist;
    }

    public float getStatValueForUser(SGMUser user, String key){
        float value = 0.0f;

        if(user.getAllSavedData().data.containsKey(key))
        {
            value = user.getAllSavedData().data.get(key);
        }

        for (SGMADisplayableStat stat : statsDisplayable){
            if(stat.id == key)
                value = stat.getValue(user);
        }

        return value;
    }

    public void setStatsDisplayable(List<SGMADisplayableStat> statsDisplayable){
        if(statsDisplayable != null)
            this.statsDisplayable = statsDisplayable;
        else
            this.statsDisplayable = new ArrayList<>();
    }

    public void addStatDisplayable(SGMADisplayableStat stat){
        if(this.statsDisplayable == null)
            this.statsDisplayable = new ArrayList<>();

        this.statsDisplayable.add(stat);
    }

    public List<SGMADisplayableStat> getStatsDisplayable() {
        return statsDisplayable;
    }
}
