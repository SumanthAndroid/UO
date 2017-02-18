package com.universalstudios.orlandoresort.model.network.datetime;

import android.text.format.DateFormat;

import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;

import java.util.TimeZone;

/**
 *
 */
public class DateTimeUtils {

    public static final String TIME_ZONE_US_EASTERN = "US/Eastern";
    public static final String TIME_ZONE_US_PACIFIC = "US/Pacific";
    public static final String TIME_ZONE_US_GMT = "GMT";

    public static TimeZone getParkTimeZone() {
        return TimeZone.getTimeZone(getParkTimezoneString());
    }

    public static TimeZone getEasternTimeZone() {
        return TimeZone.getTimeZone(TIME_ZONE_US_EASTERN);
    }

    public static TimeZone getPacificTimeZone() {
        return TimeZone.getTimeZone(TIME_ZONE_US_PACIFIC);
    }

    public static TimeZone getGmtTimeZone() {
        return TimeZone.getTimeZone(TIME_ZONE_US_GMT);
    }

    public static String getParkTimezoneString() {
        // Hollywood is PT
        if (BuildConfigUtils.isLocationFlavorHollywood()) {
            return TIME_ZONE_US_PACIFIC;
        }
        // Orlando is ET
        else {
            return TIME_ZONE_US_EASTERN;
        }
    }

    public static boolean is24HourFormat() {
        return DateFormat.is24HourFormat(UniversalOrlandoApplication.getAppContext());
    }
}
