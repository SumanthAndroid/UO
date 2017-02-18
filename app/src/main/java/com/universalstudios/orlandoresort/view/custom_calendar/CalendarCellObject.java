package com.universalstudios.orlandoresort.view.custom_calendar;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 7/22/16.
 * Class: CalendarCellObject
 * Class Description: CalendarCellObject for custom calendar
 *
 * Edited by Tyler Ritchie
 */
public class CalendarCellObject {
    public static final String TAG = "CalendarCellObject";
    public int day;
    public boolean isOutOfMonth;
    public boolean isSelected;
    public CalendarCellSeasonState computedSeasonState;
    public CalendarCellSeasonState twoParkSeasonState;
    public CalendarCellSeasonState uvbSeasonState;

    public CalendarCellObject(int day, boolean isOutOfMonth) {
        this.day = day;
        this.isOutOfMonth = isOutOfMonth;
    }
}
