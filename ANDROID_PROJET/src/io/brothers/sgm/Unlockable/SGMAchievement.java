package io.brothers.sgm.Unlockable;

import io.brothers.sgm.Unlockable.Conditions.SGMAConditionElement;

/**
 * Created by Simon on 25/03/2015.
 */
public class SGMAchievement {
    SGMAConditionElement conditions;
    private boolean isRepeatable = false;
    private String id = "";
    private String name = "";
    private String desc = "";

    public SGMAchievement(String id, String name, String desc, SGMAConditionElement conditions, boolean isRepeatable){
        this.conditions = conditions;
        this.isRepeatable = isRepeatable;
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public boolean isRepeatable() {
        return isRepeatable;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
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
