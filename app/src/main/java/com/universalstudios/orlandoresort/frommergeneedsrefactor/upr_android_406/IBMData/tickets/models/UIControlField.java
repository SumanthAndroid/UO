package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: UIControlField
 * Class Description: TODO: ALWAYS FILL OUT
 */
@Parcel
public class UIControlField extends GsonObject {

    @SerializedName("serviceIdentifierRelatedType")
    String serviceIdentifierRelatedType;

    @SerializedName("isDefault")
    boolean isDefault;

    @SerializedName("serviceIdentifier")
    String serviceIdentifier;

    @SerializedName("displayValue")
    String displayValue;

    public String getServiceIdentifierRelatedType() {
        return serviceIdentifierRelatedType;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getServiceIdentifier() {
        return serviceIdentifier;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    // FIXME This field really shouldn't be set in this response object.
    // Field was being directly set before introduction of getters/setters
    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }
}
