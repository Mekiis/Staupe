package io.brothers.sgm.Unlockable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 25/03/2015.
 */
public class SGMAchievement {
    List<SGMCondition> conditions;
    private boolean isRepeatable = false;
    private String id = "";
    private String name = "";
    private String desc = "";

    public SGMAchievement(String id, String name, String desc, List<SGMCondition> conditions, boolean isRepeatable){
        if(conditions != null)
            this.conditions = conditions;
        else
            this.conditions = new ArrayList<>();

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
}
