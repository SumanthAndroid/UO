package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.State;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Created by jamesblack on 6/13/16.
 */
@Parcel
public class ProvState extends NetworkResponse implements Comparable<ProvState> {
    public static final String STATE_CODE_FLORIDA = "FL";
    public static final String STATE_CODE_GEORGIA = "GA";

    @SerializedName("type")
    String countryType;

    @SerializedName("code")
    String code;

    @SerializedName("displayName")
    String displayName;

    public String getCountryType() {
        return countryType;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    public int compareTo(ProvState provState) {
        return displayName.compareTo(provState.getDisplayName());
    }
}