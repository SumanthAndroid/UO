package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.ValidateEmailAddressRequest;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Network response for the {@link ValidateEmailAddressRequest}
 * request.
 * <p>
 * Created by Jack Hughes on 10/4/16.
 */

@Parcel
public class ValidateEmailAddressResponse extends NetworkResponse {

    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("result")
    EmailValidationResult result;

    @SerializedName("message")
    String message;

    /**
     * Method to get the HTTP Status Code for the response.
     *
     * @return an int for the HTTP Status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Method to get the the {@link EmailValidationResult} for the the email that was sent to get
     * validated.
     *
     * @return the {@link EmailValidationResult} object for the email validation results
     */
    public EmailValidationResult getResult() {
        return result;
    }

    /**
     * Method to get any addition messages about the network response.
     *
     * @return the String holding any addition messages about the network response
     */
    public String getMessage() {
        return message;
    }
}
