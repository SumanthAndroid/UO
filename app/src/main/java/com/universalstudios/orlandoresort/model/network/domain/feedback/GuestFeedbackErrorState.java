package com.universalstudios.orlandoresort.model.network.domain.feedback;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author Steven Byle
 */
@Parcel
public class GuestFeedbackErrorState extends GsonObject {

    @SerializedName("guestFeedback.Subject")
    List<String> subjectErrors;

    @SerializedName("guestFeedback.Rating")
    List<String> ratingErrors;

    @SerializedName("guestFeedback.Message")
    List<String> messageErrors;

    @SerializedName("guestFeedback.Name")
    List<String> nameErrors;

    @SerializedName("guestFeedback.Email")
    List<String> emailErrors;

    @SerializedName("guestFeedback.Phone")
    List<String> phoneErrors;

    public List<String> getEmailErrors() {
        return emailErrors;
    }

    public List<String> getMessageErrors() {
        return messageErrors;
    }

    public List<String> getNameErrors() {
        return nameErrors;
    }

    public List<String> getPhoneErrors() {
        return phoneErrors;
    }

    public List<String> getRatingErrors() {
        return ratingErrors;
    }

    public List<String> getSubjectErrors() {
        return subjectErrors;
    }
}