package io.brothers.sgm;

import java.util.ArrayList;
import java.util.List;

import io.brothers.sgm.Unlockable.SGMUnlockManager;
import io.brothers.sgm.User.SGMUser;

/**
 * Created by Simon on 25/03/2015.
 */
public class SGMStatManager {
    private List<SGMAStat> statsCustom = new ArrayList<>();
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

        SGMUnlockManager.getInstance().majUnlockForData(key, user.id);
    }

    public void setStatDataForUser(SGMUser user, String key, float value) {
        user.getAllSavedData().data.put(key, value);
    }

    public boolean isStatExistForUser(SGMUser user, String key){
        boolean statExist = false;
        if(user.getAllSavedData().data.containsKey(key))
            statExist = true;

        for (SGMAStat stat : statsCustom){
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

        for (SGMAStat stat : statsCustom){
            if(stat.id == key)
                value = stat.getValue(user);
        }

        return value;
    }

    public void setStatsCustom(List<SGMAStat> statsCustom){
        if(statsCustom != null)
            this.statsCustom = statsCustom;
        else
            this.statsCustom = new ArrayList<>();
    }

    public void addStatCustom(SGMAStat stat){
        if(this.statsCustom == null)
            this.statsCustom = new ArrayList<>();

        this.statsCustom.add(stat);
    }

    public List<SGMAStat> getStatsCustom() {
        return statsCustom;
    }
}
