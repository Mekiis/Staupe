package fr.free.simon.jacquemin.staupe.SGM;

import fr.free.simon.jacquemin.staupe.container.Level;
import fr.free.simon.jacquemin.staupe.container.data.EData;
import io.brothers.sgm.SGMStatManager;
import io.brothers.sgm.Unlockable.Conditions.SGMAConditionElement;
import io.brothers.sgm.Unlockable.SGMUnlock;
import io.brothers.sgm.User.SGMUser;

/**
 * Created by Simon on 27/04/2015.
 */
public class UnlockLevel extends SGMUnlock {
    private Level level = null;

    public UnlockLevel(Level level, SGMAConditionElement conditions) {
        super(Integer.toString(level.id), conditions);

        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    public int getStarToReach(SGMUser user){
        return this.level.lock - (int) SGMStatManager.getInstance().getStatValueForUser(user, EData.STATS_ALL_STARS.toString());
    }

    @Override
    public boolean isUnlocked(SGMUser user) {
        return this.level.lock <= (int) SGMStatManager.getInstance().getStatValueForUser(user, EData.STATS_ALL_STARS.toString());
    }
}
