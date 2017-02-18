package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

@Parcel
public class CommunicationResult extends GsonObject {

    @SerializedName("preferences")
    CommunicationPreference result;

    public CommunicationPreference getResult() {
        return result;
    }

}