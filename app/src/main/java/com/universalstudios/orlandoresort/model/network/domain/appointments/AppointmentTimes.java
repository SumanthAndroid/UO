package com.universalstudios.orlandoresort.model.network.domain.appointments;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by GOKHAN on 7/19/2016.
 */
@Parcel
public class AppointmentTimes extends GsonObject {

    @SerializedName("Date")
    String date;

    @SerializedName("StartTime")
    String startTime;

    @SerializedName("EndTime")
    String endTime;

    @SerializedName("QueueId")
    int queueId;

    @SerializedName("Capacity")
    int capacity;

    @SerializedName("UEPAllowance")
    int uepAllowance;

    @SerializedName("NoShowAllowance")
    int noShowAllowance;

    @SerializedName("StandByAllowance")
    int standByAllowance;

    @SerializedName("UsedCapacity")
    int usedCapacity;

    @SerializedName("Url")
    String url;

    @SerializedName("Id")
    long appointmentTimeId;

    @SerializedName("ExternalIds")
    int externalIds;

    // FIELD NOT RETURNED FROM SERVICE
    @SerializedName("QueueEntityType")
    String queueEntityType;

    @SerializedName("TicketAppointmentId")
    long ticketAppointmentId;

    @SerializedName("TicketDisplayName")
    String ticketDisplayName;
    

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return getFormattedTime(startTime);
    }

    public String getEndTime() {
       return getFormattedTime(endTime);
    }

    public int getQueueId() {
        return queueId;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getUepAllowance() {
        return uepAllowance;
    }

    public int getNoShowAllowance() {
        return noShowAllowance;
    }

    public int getStandByAllowance() {
        return standByAllowance;
    }

    public int getUsedCapacity() {
        return usedCapacity;
    }

    public String getUrl() {
        return url;
    }

    public long getId() {
        return appointmentTimeId;
    }

    public int getExternalIds() {
        return externalIds;
    }

    public long getTicketAppointmentId() {return ticketAppointmentId;}
    public void setTicketAppointmentId(long ticketAppointmentId) {this.ticketAppointmentId = ticketAppointmentId;}

    public String getQueueEntityType() {return queueEntityType;}
    public void setQueueEntityType(String queueEntityType) {this.queueEntityType = queueEntityType;}

    public String getTicketDisplayName() {return ticketDisplayName;}
    public void setTicketDisplayName(String ticketDisplayName) {this.ticketDisplayName = ticketDisplayName;}

    private String getFormattedTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sszzzz");
        Date date;

        SimpleDateFormat sdfOutTime = new SimpleDateFormat("h:mm a", Locale.US);
        sdfOutTime.setTimeZone(DateTimeUtils.getParkTimeZone());
        try {
            date = format.parse(time);
            return sdfOutTime.format(date);
        } catch(ParseException e) {
            if(BuildConfig.DEBUG) {
                Log.e("AppointmentTimes", "parse exception", e);
            }
        }
        return "";
    }
}
