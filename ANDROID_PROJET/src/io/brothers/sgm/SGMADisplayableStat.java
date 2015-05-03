package io.brothers.sgm;

/**
 * Created by Simon on 27/03/2015.
 */
public abstract class SGMADisplayableStat {
    public String id = "";
    public String name = "";
    public String desc = "";

    protected abstract float getValue(String userId);

    public String getValueFormat(String userId){
        return Float.toString(getValue(userId));
    }

    protected SGMADisplayableStat(String id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }
}
