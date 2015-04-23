package io.brothers.sgm;

import io.brothers.sgm.User.SGMUser;

/**
 * Created by Simon on 27/03/2015.
 */
public abstract class SGMADisplayableStat {
    public String id = "";
    public String name = "";
    public String desc = "";

    protected abstract float getValue(SGMUser user);

    public String getValueFormat(SGMUser user){
        return Float.toString(getValue(user));
    }

    protected SGMADisplayableStat(String id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }
}
