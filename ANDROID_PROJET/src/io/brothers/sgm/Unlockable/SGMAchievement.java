package io.brothers.sgm.Unlockable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 25/03/2015.
 */
public class SGMAchievement {
    List<SGMCondition> conditions;
    boolean isRepeatable = false;
    String id = "";

    public SGMAchievement(String id, List<SGMCondition> conditions, boolean isRepeatable){
        if(conditions != null)
            this.conditions = conditions;
        else
            this.conditions = new ArrayList<>();

        this.isRepeatable = isRepeatable;
        this.id = id;
    }
}
