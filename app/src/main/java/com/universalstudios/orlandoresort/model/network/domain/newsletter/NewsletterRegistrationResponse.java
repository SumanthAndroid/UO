package com.universalstudios.orlandoresort.model.network.domain.newsletter;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * @author Steven Byle
 */
@Parcel
public class NewsletterRegistrationResponse extends NetworkResponse {

    @SerializedName("Message")
    String errorMessage;

    @SerializedName("ModelState")
    NewsletterRegistrationErrorState errorState;

    public String getErrorMessage() {
        return errorMessage;
    }

    public NewsletterRegistrationErrorState getErrorState() {
        return errorState;
    }
}