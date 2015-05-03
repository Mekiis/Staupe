package io.brothers.sgm;

import java.util.ArrayList;
import java.util.List;

import io.brothers.sgm.Unlockable.SGMAchievementManager;
import io.brothers.sgm.Unlockable.SGMUnlockManager;
import io.brothers.sgm.User.SGMUser;
import io.brothers.sgm.User.SGMUserManager;

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
     * @param userId
     * @param key
     */
    public void addOneForStat(String userId, String key){
        addValueForStat(userId, key, 1);
    }

    /**
     * This function is used to add a value for a stat. It notify the UnlockManager and the AchievementManager for the SGMUser
     * @param userId
     * @param key
     * @param value
     */
    public void addValueForStat(String userId, String key, float value){
        SGMUser user = SGMUserManager.getInstance().getUser(userId);
        if(user == null)
            return;

        if(user.getAllSavedData().data.containsKey(key)){
            setValueForStat(userId, key, user.getAllSavedData().data.get(key) + value);
        } else {
            setValueForStat(userId, key, value);
        }
    }

    /**
     * This function is used to set a value for a stat. It notify the UnlockManager and the AchievementManager for the SGMUser
     * @param userId
     * @param key
     * @param value
     */
    public void setValueForStat(String userId, String key, float value) {
        setValueForInternalStat(userId, key, value);

        SGMUnlockManager.getInstance().majUnlockForData(key, userId);
        SGMAchievementManager.getInstance().majAchievementForData(key, userId);
    }

    /**
     * This function is used to add 1 for a stat and don't notify UnlockManager and AchievementManager
     * @param userId
     * @param key
     */
    public void addOneForInternalStat(String userId, String key){
        addValueForInternalStat(userId, key, 1);
    }

    /**
     * This function is used to add a value for a stat and don't notify UnlockManager and AchievementManager
     * @param userId
     * @param key
     * @param value
     */
    public void addValueForInternalStat(String userId, String key, float value){
        SGMUser user = SGMUserManager.getInstance().getUser(userId);
        if(user == null)
            return;

        if(user.getAllSavedData().data.containsKey(key)){
            setValueForInternalStat(userId, key, user.getAllSavedData().data.get(key) + value);
        } else {
            setValueForInternalStat(userId, key, value);
        }
    }

    /**
     * This function is used to set a value for a stat and don't notify UnlockManager and AchievementManager
     * @param userId
     * @param key
     * @param value
     */
    public void setValueForInternalStat(String userId, String key, float value) {
        SGMUser user = SGMUserManager.getInstance().getUser(userId);
        if(user == null)
            return;

        user.getAllSavedData().data.put(key, value);

        if(user.isAutoSave())
            user.save(user.getContext());
    }

    public boolean isStatExistForUser(String userId, String key){
        boolean statExist = false;

        SGMUser user = SGMUserManager.getInstance().getUser(userId);
        if(user == null)
            return statExist;

        if(user.getAllSavedData().data.containsKey(key))
            statExist = true;

        for (SGMADisplayableStat stat : statsDisplayable){
            if(stat.id.equals(key))
                statExist = true;
        }

        return statExist;
    }

    public float getStatValueForUser(String userId, String key){
        float value = 0.0f;

        SGMUser user = SGMUserManager.getInstance().getUser(userId);
        if(user == null)
            return value;

        if(user.getAllSavedData().data.containsKey(key))
            value = user.getAllSavedData().data.get(key);

        for (SGMADisplayableStat stat : statsDisplayable){
            if(stat.id.equals(key))
                value = stat.getValue(userId);
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
