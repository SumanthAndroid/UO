package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/17/16.
 * Edited by Tyler Ritchie on 11/15/16.
 */
public class UserContactPermissions extends GsonObject {

    @SerializedName("Direct Mail")
    private boolean directMail;

    @SerializedName("Email Permission")
    private boolean emailPermission;

    @SerializedName("Targeted Social Advertising")
    private boolean targetedSocialAdvertising;

    @SerializedName("Text Message")
    private boolean textMessage;

    public boolean isDirectMail() {
        return directMail;
    }

    public void setDirectMail(boolean directMail) {
        this.directMail = directMail;
    }

    public boolean isEmailPermission() {
        return emailPermission;
    }

    public void setEmailPermission(boolean emailPermission) {
        this.emailPermission = emailPermission;
    }

    public boolean isTargetedSocialAdvertising() {
        return targetedSocialAdvertising;
    }

    public void setTargetedSocialAdvertising(boolean targetedSocialAdvertising) {
        this.targetedSocialAdvertising = targetedSocialAdvertising;
    }

    public boolean isTextMessage() {
        return textMessage;
    }

    public void setTextMessage(boolean textMessage) {
        this.textMessage = textMessage;
    }
}
