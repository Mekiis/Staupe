package io.brothers.sgm.Unlockable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iem on 13/01/15.
 */
public class SGMUnlock {
    List<SGMCondition> conditions;

    public SGMUnlock(List<SGMCondition> conditions){
        if(conditions != null)
            this.conditions = conditions;
        else
            this.conditions = new ArrayList<>();
    }
}
