package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.controller.userinterface.addons.AddOnsState;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tyler Ritchie on 10/13/16.
 */
@Parcel
public class PriceInventoryEventAvailabilityInvEvent extends GsonObject {

    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @SerializedName("startDate")
    String startDate;

    @SerializedName("endDate")
    String endDate;

    @SerializedName("showEventKey")
    String showEventKey;

    @SerializedName("eventName")
    String eventName;

    @SerializedName("eventId")
    String eventId;

    @SerializedName("showType")
    String showType;

    @SerializedName("resourceId")
    String resourceId;

    @SerializedName("available")
    String available;

    @SerializedName("showTime")
    String showTime;

    @SerializedName("showDate")
    String showDate;

    @SerializedName("totalCapacity")
    String totalCapacity;

    @SerializedName("partNumber")
    String partNumber;

    public String getAvailable() {
        return available;
    }

    public Date getEndDate() {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        try {
            return df.parse(endDate);
        }
        catch (ParseException e) {
            return null;
        }
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getResourceId() {
        return resourceId;
    }

    public Date getShowDate() {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        try {
            return df.parse(showDate);
        }
        catch (ParseException e) {
            return null;
        }
    }

    public String getShowEventKey() {
        return showEventKey;
    }

    public String getShowTime() {
        return showTime;
    }

    public String getShowType() {
        return showType;
    }

    public Date getStartDate() {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        try {
            return df.parse(startDate);
        }
        catch (ParseException e) {
            return null;
        }
    }

    public String getTotalCapacity() {
        return totalCapacity;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    @Nullable
    public String getStartTime() {
        DateFormat df = new SimpleDateFormat(AddOnsState.TIME_FORMAT, Locale.US);
        Date date = getStartDate();
        if (date != null) {
            return df.format(date);
        } else {
            return null;
        }
    }
}
