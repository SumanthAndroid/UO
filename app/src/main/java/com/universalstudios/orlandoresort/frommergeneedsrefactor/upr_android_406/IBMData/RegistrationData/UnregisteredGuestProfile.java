package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.RegistrationData;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 10/25/16.
 */
@Parcel
public class UnregisteredGuestProfile extends GsonObject {

    @SerializedName("createdDate")
    String createdDate;

    @SerializedName("docType")
    String docType;

    @SerializedName("guestId")
    String guestId;

    @SerializedName("id")
    String id;

    public String getCreatedDate() {
        return createdDate;
    }

    public String getDocType() {
        return docType;
    }

    public String getGuestId() {
        return guestId;
    }

    public String getId() {
        return id;
    }

}
