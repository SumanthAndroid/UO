package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkErrorResponse;

import org.parceler.Parcel;

/**
 * Error object that gets returned for a login failure
 * @author tjudkins
 * @since 10/26//16
 */
@Parcel
public class LoginUserErrorResponse extends NetworkErrorResponse {
    public static final String TAG = LoginUserErrorResponse.class.getSimpleName();

    @SerializedName("message")
    String message;

    @SerializedName("result")
    LoginErrorResult result;

    public String getMessage() {
        return message;
    }

    public LoginErrorResult getResult() {
        return result;
    }

    public boolean isAccountLocked() {
        return null != result && result.isAccountLocked();
    }
}
