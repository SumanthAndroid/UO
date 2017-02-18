package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * @author Tyler Ritchie on 11/18/16.
 */
@Parcel
public class CommunicationPreferencePermissions extends GsonObject {

    @SerializedName("Direct Mail")
    boolean directMail;

    @SerializedName("Email Permission")
    boolean emailPermission;

    @SerializedName("Targeted Social Advertising")
    boolean targetedSocialAdvertising;

    @SerializedName("Text Message")
    boolean textMessage;

    public boolean isDirectMail() {
        return directMail;
    }

    public boolean isEmailPermission() {
        return emailPermission;
    }

    public boolean isTargetedSocialAdvertising() {
        return targetedSocialAdvertising;
    }

    public boolean isTextMessage() {
        return textMessage;
    }

}
