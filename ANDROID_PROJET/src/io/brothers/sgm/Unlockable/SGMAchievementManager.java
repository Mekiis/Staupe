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
    private static final String KEY_ACHIEVEMENT_ALREADY_DONE = "ACH_ARDY_DONE";
    private static final String KEY_ACHIEVEMENT_COUNT = "ACH_COUNT";

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
            boolean needToCheck = false;
            for (SGMCondition condition : achievement.conditions){
                if(condition.key == key)
                    needToCheck = true;
            }

            if(needToCheck){
                boolean conditionsValidated = true;
                if(SGMStatManager.getInstance().isStatExistForUser(user, achievement.getId() + KEY_ACHIEVEMENT_ALREADY_DONE)
                        && SGMStatManager.getInstance().getStatValueForUser(user, achievement.getId() + KEY_ACHIEVEMENT_ALREADY_DONE) > 0
                        && !achievement.isRepeatable())
                    conditionsValidated = false;

                for (SGMCondition condition : achievement.conditions){
                    if( !SGMStatManager.getInstance().isStatExistForUser(user, condition.key) ||
                         SGMStatManager.getInstance().getStatValueForUser(user, condition.key) < condition.value){
                        conditionsValidated = false;
                    }
                }

                if (conditionsValidated){
                    if(user.getSGMAchievementEventListener() != null){
                        user.getSGMAchievementEventListener().unlock(achievement);
                        SGMStatManager.getInstance().addOneForStat(user, achievement.getId() + KEY_ACHIEVEMENT_COUNT);
                        SGMStatManager.getInstance().addOneForStat(user, achievement.getId() + KEY_ACHIEVEMENT_ALREADY_DONE);
                    }
                }
            }
        }
    }

    public void resetRepeatabilityForAllAchievements(SGMUser user){
        for(SGMAchievement achievement : achievements){
            if(SGMStatManager.getInstance().isStatExistForUser(user, achievement.getId() + KEY_ACHIEVEMENT_ALREADY_DONE)
                    && SGMStatManager.getInstance().getStatValueForUser(user, achievement.getId() + KEY_ACHIEVEMENT_ALREADY_DONE) > 0)
                SGMStatManager.getInstance().setStatDataForUser(user, achievement.getId() + KEY_ACHIEVEMENT_ALREADY_DONE, 0);
        }
    }

    public boolean isAchievementComplete(SGMUser user, SGMAchievement achievement){
        return getAchievementCompletionPercent(user, achievement) >= 100f;
    }

    public float getAchievementCompletionPercent(SGMUser user, SGMAchievement achievement){
        float max = 0f, actual = 0f;

        // Todo Debug computation of percentage
        for (SGMCondition condition : achievement.conditions){
            if( !SGMStatManager.getInstance().isStatExistForUser(user, condition.key) ||
                    SGMStatManager.getInstance().getStatValueForUser(user, condition.key) < condition.value){
                max += condition.value - condition.base;
                actual += SGMStatManager.getInstance().getStatValueForUser(user, condition.key) - condition.base;
            }
        }

        return actual*100f/max;
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
