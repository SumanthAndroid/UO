package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 1/16/17.
 */
@Parcel
public class CreateAccountResponseProfile extends GsonObject {

    @SerializedName("guestProfile")
    GuestProfile guestProfile;

    public GuestProfile getGuestProfile() {
        return guestProfile;
    }
}