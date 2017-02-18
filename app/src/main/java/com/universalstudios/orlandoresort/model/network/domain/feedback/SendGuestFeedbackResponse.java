package com.universalstudios.orlandoresort.model.network.domain.feedback;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * @author Steven Byle
 */
@Parcel
public class SendGuestFeedbackResponse extends NetworkResponse {

    @SerializedName("Message")
    String errorMessage;

    @SerializedName("ModelState")
    GuestFeedbackErrorState errorState;

    public String getErrorMessage() {
        return errorMessage;
    }

    public GuestFeedbackErrorState getErrorState() {
        return errorState;
    }
}