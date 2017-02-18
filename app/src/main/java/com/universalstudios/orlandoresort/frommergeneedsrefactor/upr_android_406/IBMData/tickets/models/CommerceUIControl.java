package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: CommerceUIControl
 * Class Description: TODO: ALWAYS FILL OUT
 */
@Parcel
public class CommerceUIControl extends GsonObject {

    private static final String CONTROL_TYPE_ROLLING_AVAILIBILITY = "RollingAvailability";

    @SerializedName("numberOfSelections")
    int numberOfSelections;

    @SerializedName("controlType")
    String controlType;

    @SerializedName("fields")
    List<UIControlField> fields;

    @SerializedName("primaryControl")
    boolean primaryControl;

    public boolean isRollingAvailability() {
        return CONTROL_TYPE_ROLLING_AVAILIBILITY.equalsIgnoreCase(controlType);
    }

    public int getNumberOfSelections() {
        return numberOfSelections;
    }

    public String getControlType() {
        return controlType;
    }

    public List<UIControlField> getFields() {
        return fields;
    }

    public boolean isPrimaryControl() {
        return primaryControl;
    }
}
