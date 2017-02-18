package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Error object that gets return for a login failure
 * @author tjudkins
 * @since 10/26//16
 */
@Parcel
public class LoginErrorResult extends GsonObject {
    public static final String TAG = LoginErrorResult.class.getSimpleName();

    private static final String ACCOUNT_LOCKED_MESSAGE = "Account locked";

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public boolean isAccountLocked() {
        return ACCOUNT_LOCKED_MESSAGE.equals(message);
    }
}
