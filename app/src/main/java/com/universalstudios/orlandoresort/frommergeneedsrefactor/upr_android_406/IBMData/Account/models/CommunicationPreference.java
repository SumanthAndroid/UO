package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.Map;

@Parcel
public class CommunicationPreference extends GsonObject {

    @EmailFrequencyType.StringValue
    @SerializedName("emailFrequency")
    String emailFrequency;

    @SerializedName("contactPermissions")
    CommunicationPreferencePermissions contactPermissions;

    @SerializedName("communicationInterests")
    Map<String, String> interests;

    public CommunicationPreferencePermissions getContactPermissions() {
        return contactPermissions;
    }


    public Map<String, String> getInterests() {
        return interests;
    }
}