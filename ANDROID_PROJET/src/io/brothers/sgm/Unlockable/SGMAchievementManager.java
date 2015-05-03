package io.brothers.sgm.Unlockable;

import java.util.ArrayList;
import java.util.List;

import io.brothers.sgm.SGMStatManager;
import io.brothers.sgm.User.SGMUser;
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

    public void majAchievementForData(String userId, String key){
        SGMUser user = SGMUserManager.getInstance().getUser(userId);
        if(user == null)
            return;

        for(SGMAchievement achievement : achievements){
            boolean needToCheck = achievement.isKeyNeeded(key);

            if(needToCheck){
                boolean isNeverDone = true;
                if(SGMStatManager.getInstance().isStatExistForUser(userId, achievement.getId() + KEY_ACHIEVEMENT_ALREADY_DONE)
                        && SGMStatManager.getInstance().getStatValueForUser(userId, achievement.getId() + KEY_ACHIEVEMENT_ALREADY_DONE) > 0
                        && !achievement.isRepeatable())
                    isNeverDone = false;

                boolean isUnlocked = (isNeverDone && achievement.isUnlocked(userId));

                if (isUnlocked){
                    if(user.getSGMAchievementEventListener() != null){
                        user.getSGMAchievementEventListener().unlock(achievement);
                    }
                    SGMStatManager.getInstance().addOneForInternalStat(userId, achievement.getId() + KEY_ACHIEVEMENT_COUNT);
                    SGMStatManager.getInstance().addOneForInternalStat(userId, achievement.getId() + KEY_ACHIEVEMENT_ALREADY_DONE);
                }
            }
        }
    }

    public void resetRepeatabilityForAllAchievements(String userId){
        for(SGMAchievement achievement : achievements){
            if(SGMStatManager.getInstance().isStatExistForUser(userId, achievement.getId() + KEY_ACHIEVEMENT_ALREADY_DONE))
                SGMStatManager.getInstance().setValueForInternalStat(userId, achievement.getId() + KEY_ACHIEVEMENT_ALREADY_DONE, 0);
        }
    }

    public void resetAchievement(String achievementId, String userId){
        if(SGMStatManager.getInstance().isStatExistForUser(userId, achievementId + KEY_ACHIEVEMENT_ALREADY_DONE))
            SGMStatManager.getInstance().setValueForInternalStat(userId, achievementId + KEY_ACHIEVEMENT_ALREADY_DONE, 0);
        if(SGMStatManager.getInstance().isStatExistForUser(userId, achievementId + KEY_ACHIEVEMENT_COUNT))
            SGMStatManager.getInstance().setValueForInternalStat(userId, achievementId + KEY_ACHIEVEMENT_COUNT, 0);
    }

    public void resetAllAchievements(String userId){
        for(SGMAchievement achievement : achievements){
            resetAchievement(achievement.getId(), userId);
        }
    }

    public boolean isAchievementComplete(String achievementId, String userId){
        boolean isComplete = false;

        for (SGMAchievement achievement : achievements){
            if(achievement.getId().equals(achievementId))
                isComplete = achievement.isUnlocked(userId);
        }

        return isComplete;
    }

    public float getAchievementCompletionPercent(String achievementId, String userId){
        float completionPercent = 0f;

        for (SGMAchievement achievement : achievements){
            if(achievement.getId().equals(achievementId))
                completionPercent = achievement.getCompletionPercent(userId);
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
