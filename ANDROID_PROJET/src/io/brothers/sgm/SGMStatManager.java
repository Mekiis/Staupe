package io.brothers.sgm;

import java.util.ArrayList;
import java.util.List;

import io.brothers.sgm.Unlockable.SGMAchievementManager;
import io.brothers.sgm.Unlockable.SGMUnlockManager;
import io.brothers.sgm.User.SGMUser;

/**
 * Created by SJ on 25/03/2015.
 */
public class SGMStatManager {
    private List<SGMADisplayableStat> statsDisplayable = new ArrayList<>();
    private static SGMStatManager instance = null;

    public static SGMStatManager getInstance(){
        if(instance == null)
            instance = new SGMStatManager();

        return instance;
    }

    /**
     * This function is used to add 1 for a stat. It notify the UnlockManager and the AchievementManager for the SGMUser
     * @param user
     * @param key
     */
    public void addOneForStat(SGMUser user, String key){
        addValueForStat(user, key, 1);
    }

    /**
     * This function is used to add a value for a stat. It notify the UnlockManager and the AchievementManager for the SGMUser
     * @param user
     * @param key
     * @param value
     */
    public void addValueForStat(SGMUser user, String key, float value){
        if(user.getAllSavedData().data.containsKey(key)){
            setValueForStat(user, key, user.getAllSavedData().data.get(key) + value);
        } else {
            setValueForStat(user, key, value);
        }
    }

    /**
     * This function is used to set a value for a stat. It notify the UnlockManager and the AchievementManager for the SGMUser
     * @param user
     * @param key
     * @param value
     */
    public void setValueForStat(SGMUser user, String key, float value) {
        user.getAllSavedData().data.put(key, value);

        if(user.isAutoSave())
            user.save(user.getContext());

        SGMUnlockManager.getInstance().majUnlockForData(key, user);
        SGMAchievementManager.getInstance().majAchievementForData(key, user);
    }

    /**
     * This function is used to add 1 for a stat and don't notify UnlockManager and AchievementManager
     * @param user
     * @param key
     */
    public void addOneForInternalStat(SGMUser user, String key){
        addValueForInternalStat(user, key, 1);
    }

    /**
     * This function is used to add a value for a stat and don't notify UnlockManager and AchievementManager
     * @param user
     * @param key
     * @param value
     */
    public void addValueForInternalStat(SGMUser user, String key, float value){
        if(user.getAllSavedData().data.containsKey(key)){
            setValueForInternalStat(user, key, user.getAllSavedData().data.get(key) + value);
        } else {
            setValueForInternalStat(user, key, value);
        }
    }

    /**
     * This function is used to set a value for a stat and don't notify UnlockManager and AchievementManager
     * @param user
     * @param key
     * @param value
     */
    public void setValueForInternalStat(SGMUser user, String key, float value) {
        user.getAllSavedData().data.put(key, value);

        if(user.isAutoSave())
            user.save(user.getContext());
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

    /**
     * Replace the current list with a new list of SGMDisplayableStat for the StatManager
     * @param statsDisplayable The new list of SGMDisplayableStat. If <i>null</i>, a new empty list is instantiate to replace the old one
     */
    public void setStatsDisplayable(List<SGMADisplayableStat> statsDisplayable){
        if(statsDisplayable != null)
            this.statsDisplayable = statsDisplayable;
        else
            this.statsDisplayable = new ArrayList<>();
    }

    /**
     * Add a SGMDisplayableStat to the current list for the StatManager
     * @param stat The SGMDisplayableStat to add. If previous list is <i>null</i>, a new one is instantiate
     */
    public void addStatDisplayable(SGMADisplayableStat stat){
        if(this.statsDisplayable == null)
            this.statsDisplayable = new ArrayList<>();

        this.statsDisplayable.add(stat);
    }

    /**
     * Get the complete list of all SGMDisplayableStat that the SGMStatManager contain
     * @return A list of SGMDisplayableStat. <b>Cannot be <i>null<i/></b>
     */
    public List<SGMADisplayableStat> getStatsDisplayable() {
        return statsDisplayable;
    }
}
