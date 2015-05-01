package io.brothers.sgm.Unlockable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.brothers.sgm.SGMStatManager;
import io.brothers.sgm.User.SGMUser;
import io.brothers.sgm.User.SGMUserManager;

/**
 * Created by Simon on 19/03/2015.
 */
public class SGMUnlockManager {
    public static final String KEY_UNLOCK_COUNT = "KEY_ULCK_COUNT";

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
            boolean needToCheck = unlock.isKeyNeeded(key);

            if(needToCheck){
                boolean isNeverDone = true;
                if(SGMStatManager.getInstance().isStatExistForUser(user, unlock.getId() + KEY_UNLOCK_COUNT)
                        && SGMStatManager.getInstance().getStatValueForUser(user, unlock.getId() + KEY_UNLOCK_COUNT) > 0)
                    isNeverDone = false;

                boolean isUnlocked = (isNeverDone && unlock.isUnlocked(user) ? true : false);

                if (isUnlocked){
                    if(SGMUserManager.getInstance().getUser(user.id).getSGMUnlockEventListener() != null){
                        SGMUserManager.getInstance().getUser(user.id).getSGMUnlockEventListener().unlock(unlock);
                    }
                    SGMStatManager.getInstance().addOneForInternalStat(user, unlock.getId() + KEY_UNLOCK_COUNT);
                }
            }
        }
    }

    public <T extends SGMUnlock> List<T> getAllUnlockOf(Class<T> type, SGMUser user){
        List<T> list = new LinkedList<>();

        for (SGMUnlock unlock : unlocked){
            if(unlock.getClass() == type){
                list.add((T) unlock);
            }
        }

        return list;
    }

    public <T extends SGMUnlock> List<T> getAllUnlockUnlockedOf(Class<T> type, SGMUser user){
        List<T> list = new LinkedList<>();

        for (SGMUnlock unlock : unlocked){
            if(unlock.getClass() == type && unlock.isUnlocked(user)){
                list.add((T) unlock);
            }
        }

        return list;
    }

    public void resetUnlocked(String id, SGMUser user){
        if(SGMStatManager.getInstance().isStatExistForUser(user, id + KEY_UNLOCK_COUNT))
            SGMStatManager.getInstance().setValueForInternalStat(user, id + KEY_UNLOCK_COUNT, 0);
    }

    public void resetAllUnlocked(SGMUser user){
        for (SGMUnlock unlock : unlocked){
            resetUnlocked(unlock.getId(), user);
        }
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
