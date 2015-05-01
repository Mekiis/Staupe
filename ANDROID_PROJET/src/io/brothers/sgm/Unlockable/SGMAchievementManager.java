package io.brothers.sgm.Unlockable;

import java.util.ArrayList;
import java.util.List;

import io.brothers.sgm.SGMStatManager;
import io.brothers.sgm.User.SGMUser;

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

    public void majAchievementForData(String key, SGMUser user){
        for(SGMAchievement achievement : achievements){
            boolean needToCheck = achievement.isKeyNeeded(key);

            if(needToCheck){
                boolean isNeverDone = true;
                if(SGMStatManager.getInstance().isStatExistForUser(user, achievement.getId() + KEY_ACHIEVEMENT_ALREADY_DONE)
                        && SGMStatManager.getInstance().getStatValueForUser(user, achievement.getId() + KEY_ACHIEVEMENT_ALREADY_DONE) > 0
                        && !achievement.isRepeatable())
                    isNeverDone = false;

                boolean isUnlocked = (isNeverDone && achievement.isUnlocked(user) ? true : false);

                if (isUnlocked){
                    if(user.getSGMAchievementEventListener() != null){
                        user.getSGMAchievementEventListener().unlock(achievement);
                    }
                    SGMStatManager.getInstance().addOneForInternalStat(user, achievement.getId() + KEY_ACHIEVEMENT_COUNT);
                    SGMStatManager.getInstance().addOneForInternalStat(user, achievement.getId() + KEY_ACHIEVEMENT_ALREADY_DONE);
                }
            }
        }
    }

    public void resetRepeatabilityForAllAchievements(SGMUser user){
        for(SGMAchievement achievement : achievements){
            if(SGMStatManager.getInstance().isStatExistForUser(user, achievement.getId() + KEY_ACHIEVEMENT_ALREADY_DONE))
                SGMStatManager.getInstance().setValueForInternalStat(user, achievement.getId() + KEY_ACHIEVEMENT_ALREADY_DONE, 0);
        }
    }

    public void resetAchievement(String id, SGMUser user){
        if(SGMStatManager.getInstance().isStatExistForUser(user, id + KEY_ACHIEVEMENT_ALREADY_DONE))
            SGMStatManager.getInstance().setValueForInternalStat(user, id + KEY_ACHIEVEMENT_ALREADY_DONE, 0);
        if(SGMStatManager.getInstance().isStatExistForUser(user, id + KEY_ACHIEVEMENT_COUNT))
            SGMStatManager.getInstance().setValueForInternalStat(user, id + KEY_ACHIEVEMENT_COUNT, 0);
    }

    public void resetAllAchievements(SGMUser user){
        for(SGMAchievement achievement : achievements){
            resetAchievement(achievement.getId(), user);
        }
    }

    public boolean isAchievementComplete(SGMUser user, String id){
        boolean isComplete = false;

        for (SGMAchievement achievement : achievements){
            if(achievement.getId() == id)
                isComplete = achievement.isUnlocked(user);
        }

        return isComplete;
    }

    public float getAchievementCompletionPercent(SGMUser user, String id){
        float completionPercent = 0f;

        for (SGMAchievement achievement : achievements){
            if(achievement.getId() == id)
                completionPercent = achievement.getCompletionPercent(user);
        }

        return completionPercent;
    }

    public void setAchievements(List<SGMAchievement> achievements){
        if(achievements != null)
            this.achievements = achievements;
        else
            this.achievements = new ArrayList<>();
    }

    public void addAchievement(SGMAchievement achievement){
        if(this.achievements == null)
            this.achievements = new ArrayList<>();

        this.achievements.add(achievement);
    }

    public List<SGMAchievement> getAchievements() {
        return achievements;
    }
}
