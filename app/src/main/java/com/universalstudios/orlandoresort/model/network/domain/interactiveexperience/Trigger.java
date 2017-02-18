package com.universalstudios.orlandoresort.model.network.domain.interactiveexperience;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author jamestimberlake
 * @created 4/25/16.
 */
@Parcel
public class Trigger extends GsonObject {

    @SerializedName("NotificationTitle")
    String notificationTitle;

    @SerializedName("NotificationText")
    String notificationText;

    @SerializedName("ReTriggerTimeInMins")
    int reTriggertimeInMins;

    @SerializedName("Beacons")
    List<Beacon> beacons;

    @SerializedName("Id")
    long Id;

    public String getNotificationText() {
        return notificationText;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public int getReTriggertimeInMins() {
        return reTriggertimeInMins;
    }

    public List<Beacon> getBeacons() {
        return beacons;
    }
}
