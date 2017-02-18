package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 10/19/16.
 */
@Parcel
public class CommerceProperty extends GsonObject {

    @SerializedName("WCSKEYPARKS")
    String keyParksAmount;

    @SerializedName("WCSKEYPERCHILD")
    String keyPerChild;

    @SerializedName("WCSKEYPARKNAME")
    String keyParkNames;

    @SerializedName("WCSKEYDAYS")
    String keyDays;

    public String getKeyDays() {
        return keyDays;
    }

    public String getKeyParkNames() {
        return keyParkNames;
    }

    public String getKeyParksAmount() {
        return keyParksAmount;
    }

    public String getKeyPerChild() {
        return keyPerChild;
    }
}
