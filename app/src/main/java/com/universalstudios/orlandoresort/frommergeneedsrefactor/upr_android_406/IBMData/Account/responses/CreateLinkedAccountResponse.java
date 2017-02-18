package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 10/24/16.
 */
@Parcel
public class CreateLinkedAccountResponse extends NetworkResponse {

    @SerializedName("guestProfile")
    GuestProfile guestProfile;

    @SerializedName("guestId")
    String guestId;

    @SerializedName("ldapId")
    String ldapId;

    @SerializedName("mdmId")
    String mdmId;

    @SerializedName("userId")
    String userId;

    public String getGuestId() {
        return guestId;
    }

    public GuestProfile getGuestProfile() {
        return guestProfile;
    }

    public String getLdapId() {
        return ldapId;
    }

    public String getMdmId() {
        return mdmId;
    }

    public String getUserId() {
        return userId;
    }
}
