package com.universalstudios.orlandoresort.model.network.domain.interactiveexperience;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * @author jamestimberlake
 * @created 4/25/16.
 */
@Parcel
public class Beacon extends GsonObject {

    public enum BeaconType {
        EDDYSTONE,
        IBEACON;
    }

    @SerializedName("Uuid")
    String uuid;

    @SerializedName("MajorId")
    Long majorId;

    @SerializedName("MinorId")
    Long minorId;

    @SerializedName("BeaconName")
    String beaconName;

    @SerializedName("BeaconType")
    String beaconType;

    @SerializedName("Url")
    String url;

    @SerializedName("Id")
    Long Id;

    public long triggerId;

    public Long getId() {
        return Id;
    }

    public String getUuid() {
        return uuid;
    }

    public Long getMajorId() {
        return majorId;
    }

    public Long getMinorId() {
        return minorId;
    }

    public String getBeaconName() {
        return beaconName;
    }

    public String getBeaconType() {
        return beaconType;
    }

    public String getUrl() {
        return url;
    }

}
