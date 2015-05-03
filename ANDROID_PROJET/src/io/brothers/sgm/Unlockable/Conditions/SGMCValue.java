package io.brothers.sgm.Unlockable.Conditions;

import io.brothers.sgm.SGMStatManager;
import io.brothers.sgm.User.SGMUser;
import io.brothers.sgm.User.SGMUserManager;

/**
 * Created by Simon on 22/04/2015.
 */
public class SGMCValue extends SGMAConditionElement {
    private String key = "";
    private float value = 0f;

    @Override
    public boolean compute(String userId) {
        SGMUser user = SGMUserManager.getInstance().getUser(userId);

        return user != null && SGMStatManager.getInstance().getStatValueForUser(userId, key) >= value;
    }

    @Override
    public float getPercentCompletion(String userId) {
        return SGMStatManager.getInstance().getStatValueForUser(userId, key) * 100f / value;
    }

    public SGMCValue(String key, float value){
        this.key = key;
        this.value = value;
    }
}
