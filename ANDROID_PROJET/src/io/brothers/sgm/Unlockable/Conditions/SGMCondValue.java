package io.brothers.sgm.Unlockable.Conditions;

/**
 * Created by Simon on 22/04/2015.
 */
public class SGMCondValue extends SGMAConditionElement {
    private String key = "";
    private float value = 0f;

    @Override
    public void interpret() {

    }

    public SGMCondValue(String key, float value){
        this.key = key;
        this.value = value;
    }
}
