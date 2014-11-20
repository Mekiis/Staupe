package fr.free.simon.jacquemin.staupe.insects;

import android.app.Activity;
import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import fr.free.simon.jacquemin.staupe.SGM.utils.SGMMath;
import fr.free.simon.jacquemin.staupe.SGM.utils.SGMTimer;

/**
 * Created by Simon on 17/11/2014.
 */
public class LauncherInsect{
    private RunnerInsect insectTask = null;
    private SGMTimer insectTimer = null;

    private static int timeMin = 1;
    private static int timeMax = 2;
    private static ImageView UIimageViewInsectContainer = null;
    private static Activity activity = null;
    private static DisplayMetrics metrics = null;

    public LauncherInsect(int timeMin, int timeMax, ImageView UIimageViewInsectContainer, Activity activity, DisplayMetrics metrics){
        this.timeMin = timeMin;
        this.timeMax = timeMax;
        this.UIimageViewInsectContainer = UIimageViewInsectContainer;
        this.activity = activity;
        this.metrics = metrics;
    }

    public void run(){
        insectTask = new RunnerInsect();
        insectTimer = new SGMTimer();
        insectTimer.execute((float) SGMMath.randInt(timeMin, timeMax), false, insectTask);
    }


    private class RunnerInsect implements Runnable{

        @Override
        public void run() {
            new Insecte().execute(UIimageViewInsectContainer, activity, activity.getApplicationContext(),
                    metrics.heightPixels,
                    -500f, metrics.widthPixels + 1000f);

            insectTimer = new SGMTimer();
            insectTimer.execute((float) SGMMath.randInt(timeMin, timeMax), false, insectTask);
        }
    }

    public void resetTime(){
        insectTimer.resetTimer();
    }

    public void stop(){
        insectTimer.stop();
    }
}
