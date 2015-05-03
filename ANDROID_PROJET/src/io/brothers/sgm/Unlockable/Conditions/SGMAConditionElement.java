package io.brothers.sgm.Unlockable.Conditions;

/**
 * Created by Simon on 22/04/2015.
 */
public abstract class SGMAConditionElement {
    public abstract boolean compute(String userId);

    public abstract float getPercentCompletion(String userId);
}
