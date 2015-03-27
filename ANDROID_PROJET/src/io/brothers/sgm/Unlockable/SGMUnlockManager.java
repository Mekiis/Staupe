package io.brothers.sgm.Unlockable;

import java.util.ArrayList;
import java.util.List;

import io.brothers.sgm.User.SGMUserManager;

/**
 * Created by Simon on 19/03/2015.
 */
public class SGMUnlockManager {
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

    public void majUnlockForData(String key, String userId){
        for(SGMUnlock unlock : unlocked){
            boolean needToCheck = false;
            for (SGMCondition condition : unlock.conditions){
                if(condition.key == key)
                    needToCheck = true;
            }

            if(needToCheck){
                boolean conditionsValidated = true;
                for (SGMCondition condition : unlock.conditions){
                    if( !SGMUserManager.getInstance().getUser(userId).getAllSavedData().data.containsKey(key) ||
                        SGMUserManager.getInstance().getUser(userId).getAllSavedData().data.get(key) < condition.value){
                        conditionsValidated = false;
                    }
                }

                if (conditionsValidated){
                    if(SGMUserManager.getInstance().getUser(userId).getSGMUnlockEventListener() != null)
                        SGMUserManager.getInstance().getUser(userId).getSGMUnlockEventListener().unlock(unlock);
                }
            }
        }
    }

    public void setUnlocked(List<SGMUnlock> unlocked){
        if(unlocked != null)
            this.unlocked = unlocked;
        else
            this.unlocked = new ArrayList<>();
    }
}
