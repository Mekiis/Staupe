package io.brothers.sgm.Unlockable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iem on 13/01/15.
 */
public class SGMUnlock {
    private String id = "";
    List<SGMCondition> conditions;

    public SGMUnlock(String id, List<SGMCondition> conditions){
        this.id = id;

        if(conditions != null)
            this.conditions = conditions;
        else
            this.conditions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }
}
