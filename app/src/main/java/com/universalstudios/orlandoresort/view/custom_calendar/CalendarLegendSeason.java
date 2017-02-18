package com.universalstudios.orlandoresort.view.custom_calendar;

/**
 * Calendar legend season section for custom calendar legend
 *
 * Created by Tyler Ritchie on 9/23/16.
 */
public class CalendarLegendSeason {
    private String itemHeader;
    private String itemDescription;
    private int colorValue;

    public CalendarLegendSeason(String itemHeader, String itemDescription, int colorValue) {
        this.itemHeader = itemHeader;
        this.itemDescription = itemDescription;
        this.colorValue = colorValue;
    }

    public String getItemHeader() {
        return itemHeader;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public int getColorValue() {
        return colorValue;
    }

}
