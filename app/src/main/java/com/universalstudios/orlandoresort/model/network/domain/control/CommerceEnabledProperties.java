package com.universalstudios.orlandoresort.model.network.domain.control;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * @author Steven Byle
 */
@Parcel
public class CommerceEnabledProperties extends NetworkResponse {

    @SerializedName("appValid")
    Boolean appValidForCommerce;

    @SerializedName("message")
    String message;

    public Boolean getAppValidForCommerce() {
        return appValidForCommerce;
    }

    public String getMessage() {
        return message;
    }
}