package io.brothers.sgm.Unlockable;

/**
 * Created by Simon on 17/03/2015.
 */
public class SGMCondition {
    String key = "";
    int value = 0;
    int base = 0;

    public SGMCondition(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public SGMCondition(String key, int value, int base) {
        this.key = key;
        this.value = value;
        this.base = 0;
    }
}
