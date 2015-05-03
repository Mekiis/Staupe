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

    public void majUnlockForData(String key, String userId){
        SGMUser user = SGMUserManager.getInstance().getUser(userId);
        if(user == null)
            return;

        for(SGMUnlock unlock : unlocked){
            boolean needToCheck = unlock.isKeyNeeded(key);

            if(needToCheck){
                boolean isNeverDone = true;
                if(SGMStatManager.getInstance().isStatExistForUser(userId, unlock.getId() + KEY_UNLOCK_COUNT)
                        && SGMStatManager.getInstance().getStatValueForUser(userId, unlock.getId() + KEY_UNLOCK_COUNT) > 0)
                    isNeverDone = false;

                boolean isUnlocked = (isNeverDone && unlock.isUnlocked(userId));

                if (isUnlocked){
                    if(user.getSGMUnlockEventListener() != null){
                        user.getSGMUnlockEventListener().unlock(unlock);
                    }
                    SGMStatManager.getInstance().addOneForInternalStat(userId, unlock.getId() + KEY_UNLOCK_COUNT);
                }
            }
        }
    }

    public <T extends SGMUnlock> List<T> getAllUnlockOf(Class<T> type){
        List<T> list = new LinkedList<>();

        for (SGMUnlock unlock : unlocked){
            if(unlock.getClass() == type){
                list.add((T) unlock);
            }
        }

        return list;
    }

    public <T extends SGMUnlock> List<T> getAllUnlockUnlockedOf(Class<T> type, String userId){
        List<T> list = getAllUnlockOf(type);

        for (T ele : list){
            if(!ele.isUnlocked(userId))
                list.remove(ele);
        }

        return list;
    }

    public void resetUnlocked(String id, String userId){

        if(SGMStatManager.getInstance().isStatExistForUser(userId, id + KEY_UNLOCK_COUNT))
            SGMStatManager.getInstance().setValueForInternalStat(userId, id + KEY_UNLOCK_COUNT, 0);
    }

    public void resetAllUnlocked(String userId){
        for (SGMUnlock unlock : unlocked){
            resetUnlocked(unlock.getId(), userId);
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
