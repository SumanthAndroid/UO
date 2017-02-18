package com.universalstudios.orlandoresort.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Utility that allows the application to determine if content should be shown
 * after a specified date.
 * TODO: Remove after January 1st, 2017
 * Created by Nicholas Hanna on 11/10/2016.
 */
public final class WnWKillSwitchDateUtil {
    /**
     * Indicates if the specified date is before the 'kill' date
     * @return true if the date is before the 'kill' date
     */
    public static boolean shouldDisplayContent() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2016);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        Date endDate = cal.getTime();
        return Calendar.getInstance().getTime().before(endDate);
    }
}
