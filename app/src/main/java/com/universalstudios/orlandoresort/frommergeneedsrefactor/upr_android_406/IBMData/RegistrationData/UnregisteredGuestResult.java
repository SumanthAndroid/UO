package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.RegistrationData;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by IBM_ADMIN on 4/6/2016.
 */
@Parcel
public class UnregisteredGuestResult extends GsonObject {

    @SerializedName("guestProfile")
    UnregisteredGuestProfile unregisteredGuestProfile;

    public UnregisteredGuestProfile getUnregisteredGuestProfile() {
        return unregisteredGuestProfile;
    }

}
