package io.brothers.sgm.Unlockable;

import java.util.ArrayList;
import java.util.List;

import io.brothers.sgm.SGMStatManager;
import io.brothers.sgm.User.SGMUser;
import io.brothers.sgm.User.SGMUserManager;

/**
 * Created by Simon on 19/03/2015.
 */
public class SGMUnlockManager {
    private static final String KEY_UNLOCK_ALREADY_DONE = "KEY_UNLOCK_ALREADY_DONE";

    public interface SGMUnlockEventListener {
        public void unlock(SGMUnlock unlocked);
    }

    private List<SGMUnlock> unlocked = new ArrayList<>();
    private static SGMUnlockManager instance = null;

    public static SGMUnlockManager getInstance(){
        if(instance == null)
            instance = new SGMUnlockManager();

        return instance;
    }

    public void majUnlockForData(String key, SGMUser user){
        for(SGMUnlock unlock : unlocked){
            boolean needToCheck = false;
            for (SGMCondition condition : unlock.conditions){
                if(condition.key == key)
                    needToCheck = true;
            }

            if(needToCheck){
                boolean conditionsValidated = true;
                if(SGMStatManager.getInstance().isStatExistForUser(user, unlock.getId() + KEY_UNLOCK_ALREADY_DONE)
                        && SGMStatManager.getInstance().getStatValueForUser(user, unlock.getId() + KEY_UNLOCK_ALREADY_DONE) > 0)
                    conditionsValidated = false;

                for (SGMCondition condition : unlock.conditions){
                    if( !SGMStatManager.getInstance().isStatExistForUser(SGMUserManager.getInstance().getUser(user.id), key) ||
                            SGMStatManager.getInstance().getStatValueForUser(SGMUserManager.getInstance().getUser(user.id), key) < condition.value){
                        conditionsValidated = false;
                    }
                }

                if (conditionsValidated){
                    if(SGMUserManager.getInstance().getUser(user.id).getSGMUnlockEventListener() != null){
                        SGMUserManager.getInstance().getUser(user.id).getSGMUnlockEventListener().unlock(unlock);
                        SGMStatManager.getInstance().addOneForStat(user, unlock.getId() + KEY_UNLOCK_ALREADY_DONE);
                    }

                }
            }
        }
    }

    public void resetUnlocked(String id, SGMUser user){
        if(SGMStatManager.getInstance().isStatExistForUser(user, id + KEY_UNLOCK_ALREADY_DONE))
            SGMStatManager.getInstance().setStatDataForUser(user, id + KEY_UNLOCK_ALREADY_DONE, 0);
    }

    public void resetAllUnlocked(){

    }

    public void addUnlocked(SGMUnlock unlock){
        if(this.unlocked == null)
            this.unlocked = new ArrayList<>();

        this.unlocked.add(unlock);
    }

    public void setUnlocked(List<SGMUnlock> unlocked){
        if(unlocked != null)
            this.unlocked = unlocked;
        else
            this.unlocked = new ArrayList<>();
    }

    public List<SGMUnlock> getUnlocked() {
        return unlocked;
    }
}
