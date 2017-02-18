package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 1/11/17.
 */
@Parcel
public class GuestProfileResult extends GsonObject {
    @SerializedName("guestProfile")
    GuestProfile guestProfile;

    public GuestProfile getGuestProfile() {
        return guestProfile;
    }
}