package io.brothers.sgm.Unlockable;

import java.util.ArrayList;
import java.util.List;

import io.brothers.sgm.Unlockable.Conditions.SGMAConditionElement;
import io.brothers.sgm.User.SGMUser;

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

    public boolean isUnlocked(SGMUser user){
        // Todo compute the result of the condition to check if it's complete or not
        return true;
    }
}
