package io.brothers.sgm.Unlockable.Conditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 22/04/2015.
 */
public class SGMCAnd extends SGMAConditionElement {
    List<SGMAConditionElement> conditions = null;

    @Override
    public boolean compute(String userId) {
        boolean isComplete = true;

        for (SGMAConditionElement condition : conditions)
            if(!condition.compute(userId)) isComplete = false;

        return isComplete;
    }

    @Override
    public float getPercentCompletion(String userId) {
        float completionValue = -1f;

        for (SGMAConditionElement condition : conditions)
            completionValue = (completionValue == -1 ? condition.getPercentCompletion(userId) : completionValue + condition.getPercentCompletion(userId));

        completionValue = (conditions.size() > 0 ? completionValue / conditions.size() : 0f);

        return completionValue;
    }

    public SGMCAnd(SGMAConditionElement... elements){
        this.conditions = new ArrayList<>();

        if(elements != null){
            for (SGMAConditionElement element : elements)
                this.conditions.add(element);
        }
    }
}
