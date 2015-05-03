package io.brothers.sgm.Unlockable;

import io.brothers.sgm.Unlockable.Conditions.SGMAConditionElement;

/**
 * Created by iem on 13/01/15.
 */
public class SGMUnlock {
    private String id = "";
    SGMAConditionElement conditions;

    public SGMUnlock(String id, SGMAConditionElement conditions){
        this.id = id;
        this.conditions = conditions;
    }

    public String getId() {
        return id;
    }

    public boolean isKeyNeeded(String key){
        // Todo check if the key is needed in the conditions
        return true;
    }

    public boolean isUnlocked(String userId){
        return conditions.compute(userId);
    }

    public float getCompletionPercent(String userId){
        return conditions.getPercentCompletion(userId);
    }
}
