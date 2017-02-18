package com.universalstudios.orlandoresort.view.custom_calendar;

import android.util.Log;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.universalstudios.orlandoresort.BuildConfig;

/**
 * Reversible enum for keeping custom calendar cells' state.
 *
 * Created by Tyler Ritchie on 9/21/16.
 */
public enum CalendarCellSeasonState {
    VALUE("Value"),
    REGULAR("Regular"),
    PEAK("Peak"),
    MIXED("Mixed"),
    NONE("None");

    private static final String TAG = CalendarCellSeasonState.class.getSimpleName();
    private static final Map<String, CalendarCellSeasonState> stringToEnumMap;
    private String keyString;

    static {
        stringToEnumMap = new HashMap<>();
        for (CalendarCellSeasonState state : EnumSet.allOf(CalendarCellSeasonState.class)) {
            stringToEnumMap.put(state.getKeyString(), state);
        }
    }

    CalendarCellSeasonState(String keyString) {
        this.keyString = keyString;
    }

    public String getKeyString() {
        return keyString;
    }

    public static CalendarCellSeasonState fromSeasonValue(String value) {
        CalendarCellSeasonState ret = stringToEnumMap.get(value);
        if (ret == null) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Could not map CalendarCellSeasonState enum to value");
            }
        }
        return ret;
    }
}
