package io.brothers.sgm.Unlockable.Conditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 22/04/2015.
 */
public class SGMCondAnd extends SGMAConditionElement {
    List<SGMAConditionElement> conditions = null;

    @Override
    public void interpret() {

    }

    public SGMCondAnd(SGMAConditionElement... elements){
        this.conditions = new ArrayList<>();

        if(elements != null){
            for (SGMAConditionElement element : elements)
                this.conditions.add(element);
        }
    }
}
