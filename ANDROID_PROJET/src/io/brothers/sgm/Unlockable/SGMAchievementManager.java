package io.brothers.sgm.Unlockable;

import java.util.ArrayList;
import java.util.List;

import io.brothers.sgm.User.SGMUserManager;

/**
 * Created by Simon on 25/03/2015.
 */
public class SGMAchievementManager {
    public static final String KEY_ACHIEVEMENT_ALREADY_DONE = "ACH_ARDY_DONE";
    public static final String KEY_ACHIEVEMENT_COUNT = "ACH_COUNT";

    public interface SGMAchievementEventListener {
        public void unlock(SGMAchievement achievement);
    }

    private List<SGMAchievement> achievements = new ArrayList<>();
    private static SGMAchievementManager instance = null;

    public static SGMAchievementManager getInstance(){
        if(instance == null)
            instance = new SGMAchievementManager();

        return instance;
    }

    public void majAchievementForData(String key, String userId){
        for(SGMAchievement achievement : achievements){
            boolean needToCheck = false;
            for (SGMCondition condition : achievement.conditions){
                if(condition.key == key)
                    needToCheck = true;
            }

            if(needToCheck){
                boolean conditionsValidated = true;
                if(SGMUserManager.getInstance().getUser(userId).getAllSavedData().data.containsKey(achievement.id + KEY_ACHIEVEMENT_ALREADY_DONE)
                        && SGMUserManager.getInstance().getUser(userId).getSavedData(achievement.id + KEY_ACHIEVEMENT_ALREADY_DONE) > 0
                        && !achievement.isRepeatable)
                    conditionsValidated = false;

                for (SGMCondition condition : achievement.conditions){
                    if( !SGMUserManager.getInstance().getUser(userId).getAllSavedData().data.containsKey(key) ||
                            SGMUserManager.getInstance().getUser(userId).getSavedData(key) < condition.value){
                        conditionsValidated = false;
                    }
                }

                if (conditionsValidated){
                    if(SGMUserManager.getInstance().getUser(userId).getSGMAchievementEventListener() != null){
                        SGMUserManager.getInstance().getUser(userId).getSGMAchievementEventListener().unlock(achievement);
                        SGMUserManager.getInstance().getUser(userId).addData(achievement.id + KEY_ACHIEVEMENT_COUNT);
                        SGMUserManager.getInstance().getUser(userId).addData(achievement.id + KEY_ACHIEVEMENT_ALREADY_DONE, 1);
                    }
                }
            }
        }
    }

    public void resetAchievement(String userId){
        for(SGMAchievement achievement : achievements){
            if(SGMUserManager.getInstance().getUser(userId).getAllSavedData().data.containsKey(achievement.id + KEY_ACHIEVEMENT_ALREADY_DONE)
                    && SGMUserManager.getInstance().getUser(userId).getSavedData(achievement.id + KEY_ACHIEVEMENT_ALREADY_DONE) > 0)
                SGMUserManager.getInstance().getUser(userId).setSavedData(achievement.id+ KEY_ACHIEVEMENT_ALREADY_DONE, 0);
        }
    }

    public void setUnlocked(List<SGMAchievement> achievements){
        if(achievements != null)
            this.achievements = achievements;
        else
            this.achievements = new ArrayList<>();
    }

    public void addUnlocked(SGMAchievement achievement){
        if(this.achievements == null)
            this.achievements = new ArrayList<>();

        this.achievements.add(achievement);
    }
}
