package io.brothers.sgm.Tools;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fr.free.simon.jacquemin.staupe.SGMGameManager;

/**
 * Created by Simon on 22/11/2014.
 */
public class SGMDate {

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(SGMGameManager.DATE_FORMAT);
        return sdf.format(cal.getTime());
    }

    public static long dateToLong(String date){
        SimpleDateFormat f = new SimpleDateFormat(SGMGameManager.DATE_FORMAT);
        Date d = null;
        long milliseconds = 0;
        try {
            d = f.parse(date);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            Log.e("SGMDate", "Error - While parsing the date convert to a long millisecond value");
        }
        return milliseconds;
    }
}
