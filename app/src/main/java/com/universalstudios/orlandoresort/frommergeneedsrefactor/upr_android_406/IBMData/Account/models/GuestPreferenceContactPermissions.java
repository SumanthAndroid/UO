package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Created by Tyler Ritchie on 11/16/16.
 */
public class GuestPreferenceContactPermissions extends GsonObject {

    @SerializedName("Email Permission")
    private List<GuestPreferenceValue<Boolean>> emailPermission;

    @SerializedName("Text Message")
    private List<GuestPreferenceValue<Boolean>> textMessage;

    @SerializedName("Direct Mail")
    private List<GuestPreferenceValue<Boolean>> directMail;

    @SerializedName("Targeted Social Advertising")
    private List<GuestPreferenceValue<Boolean>> targetedSocialAdvertising;

    public List<GuestPreferenceValue<Boolean>> getDirectMail() {
        return directMail;
    }

    public List<GuestPreferenceValue<Boolean>> getEmailPermission() {
        return emailPermission;
    }

    public List<GuestPreferenceValue<Boolean>> getTargetedSocialAdvertising() {
        return targetedSocialAdvertising;
    }

    public List<GuestPreferenceValue<Boolean>> getTextMessage() {
        return textMessage;
    }
}
