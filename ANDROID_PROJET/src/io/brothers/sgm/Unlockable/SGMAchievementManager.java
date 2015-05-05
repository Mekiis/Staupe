package io.brothers.sgm.Unlockable;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fr.free.simon.jacquemin.staupe.AchievementsActivity;
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

        public void achievementsSynchronized(EWaySynchronize way, boolean isSynchronized);
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

    public enum EWaySynchronize{
        SrcGooglePlay,
        SrcApp,
        SrcGooglePlayAndApp
    }

    //http://gamedev.stackexchange.com/questions/77621/how-can-you-check-your-users-unlocked-achievements-google-play-game-services
    //http://stackoverflow.com/questions/23848014/google-play-game-services-unlock-achievement-store-unlock-in-game-or-call-unlo/23853222#23853222
    //https://developer.android.com/reference/com/google/android/gms/games/achievement/Achievements.LoadAchievementsResult.html
    //https://github.com/playgameservices/android-basic-samples/blob/master/FAQ.txt

    // https://developers.google.com/games/services/training/signin

    //https://developer.android.com/google/auth/api-client.html
    /**
     * Synchronize achievements between App and GooglePlay with an asynchronous function.
     * An event <i>achievementsSynchronized</i> is call when the process is complete.
     * @param way The way to synchronize data :<br/>
     *            <b>SrcGooglePlay</b> - Get the state on the play store and set data locally<br/>
     *            <b>SrcApp</b> - Get the state locally and set data on the GooglePlay<br/>
     *            <b>SrcGooglePlayAndApp</b> - Get the best state for each achievements and set data on the GooglePlay and locally<br/>
     * @param userId The id of the user
     */
    public void synchronizeWithGooglePlay(EWaySynchronize way, String userId){
        //new Synchronize(way, userId).start();
        SGMUser user = SGMUserManager.getInstance().getUser(userId);
        if(user != null && user.getApiClient() != null)
            Games.Achievements.load(user.getApiClient(), false).setResultCallback(new Synchronize(way, userId));
    }

    private class Synchronize implements ResultCallback<Achievements.LoadAchievementsResult> {
        SGMUser user = null;
        EWaySynchronize way;

        public Synchronize(EWaySynchronize way, String userId){
            user = SGMUserManager.getInstance().getUser(userId);
            this.way = way;
        }

        @Override
        public void onResult(Achievements.LoadAchievementsResult loadAchievementsResult) {
            Achievement ach;
            AchievementBuffer aBuffer = loadAchievementsResult.getAchievements();
            Iterator<Achievement> aIterator = aBuffer.iterator();

            while (aIterator.hasNext()) {
                ach = aIterator.next();
                if ("The Achievement Id you are checking".equals(ach.getAchievementId())) {
                    if (ach.getState() == Achievement.STATE_UNLOCKED) {
                        // it is unlocked
                    } else {
                        //it is not unlocked
                    }
                    aBuffer.close();
                    break;
                }
            }
        }
    }
}
