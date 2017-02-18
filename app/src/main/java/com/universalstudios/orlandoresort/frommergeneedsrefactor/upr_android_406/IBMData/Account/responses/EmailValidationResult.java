package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Email Verification information for successful or failed verification. Used by the {@link
 * ValidateEmailAddressResponse}
 * <p>
 * Created by Jack Hughes on 10/4/16.
 */
@Parcel
public class EmailValidationResult extends GsonObject {

    @SerializedName("email")
    String email;

    @SerializedName("marketingId")
    String marketingId;

    @SerializedName("verboseOutput")
    String verboseOutput;

    @SerializedName("certainty")
    String certainty;

    @SerializedName("corrections")
    ArrayList<String> corrections;

    @SerializedName("verificationStatus")
    String verificationStatus;

    /**
     * Method to get the email address that was originally sent to be validated. (i.e.
     * "test@gmailcom")
     *
     * @return the String containing the email address that was originally sent to be validated
     */
    public String getEmail() {
        return email;
    }

    /**
     * Method to get the marketing Id that was originally sent with the email address to be
     * validated. (i.e "12345")
     *
     * @return the String containing the marketing Id that was originally sent with the email
     * address to be validated
     */
    public String getMarketingId() {
        return marketingId;
    }

    /**
     * Method to get the verbose output for the validation results. This is the reason for failure
     * (or success). (i.e "syntaxFailure" or "verified")
     *
     * @return the String containing the verbose output, aka the reason for failure/success
     */
    public String getVerboseOutput() {
        return verboseOutput;
    }

    /**
     * Method to get the certainty for the validation of the email address. For invalid email
     * addresses, this may look like "undeliverable". For verified email addresses, this may look
     * like "verified".
     *
     * @return the String containing the certainty of the validation
     */
    public String getCertainty() {
        return certainty;
    }

    /**
     * Method to get the possible corrections for for an invalid email address. This field does not
     * return with a verified email address.
     *
     * @return an {@link ArrayList<String>} containing possible corrections for the invalid email
     * address
     */
    public ArrayList<String> getCorrections() {
        return corrections;
    }

    /**
     * Method to get the verification status of the email validation attempt. (i.e. "Hardfail" or
     * "Ok")
     *
     * @return the String containing the verification status of the email validation attempt
     */
    public String getVerificationStatus() {
        return verificationStatus;
    }
}
