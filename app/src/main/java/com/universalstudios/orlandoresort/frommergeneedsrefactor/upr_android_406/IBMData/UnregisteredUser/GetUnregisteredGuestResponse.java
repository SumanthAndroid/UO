package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.UnregisteredUser;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.RegistrationData.UnregisteredGuestResult;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Created by IBM_ADMIN on 5/4/2016.
 */
@Parcel
public class GetUnregisteredGuestResponse extends NetworkResponse {

    @SerializedName("statusCode")
    Integer statusCode;

    @SerializedName("result")
    UnregisteredGuestResult unregisteredGuestResult;

    /**
     *
     * @return
     * The statusCode
     */
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     *
     * @return
     * The result
     */
    public UnregisteredGuestResult getUnregisteredGuestResult() {
        return unregisteredGuestResult;
    }
}
