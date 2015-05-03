package io.brothers.sgm.Unlockable.Conditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 22/04/2015.
 */
public class SGMCOr extends SGMAConditionElement {
    List<SGMAConditionElement> conditions = null;

    @Override
    public boolean compute(String userId) {
        boolean isComplete = false;

        for (SGMAConditionElement condition : conditions)
            if(condition.compute(userId)) isComplete = true;

        return isComplete;
    }

    @Override
    public float getPercentCompletion(String userId) {
        List<Float> completionValues = new ArrayList<>();
        float completionValue = 0f;

        for (SGMAConditionElement condition : conditions)
            completionValues.add(condition.getPercentCompletion(userId));

        for (Float value : completionValues)
            completionValue = (value > completionValue ? value : completionValue);

        return completionValue;
    }

    public SGMCOr(SGMAConditionElement... elements){
        this.conditions = new ArrayList<>();

        if(elements != null){
            for (SGMAConditionElement element : elements)
                this.conditions.add(element);
        }
    }
}
