package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by kbojarski on 10/25/16.
 */
@Parcel
public class CreatedTravelPartyMember extends GsonObject {

    @SerializedName("sequenceId")
    Integer sequenceId;

    public Integer getSequenceId() {
        return sequenceId;
    }
}
