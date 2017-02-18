package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Countries;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by jamesblack on 6/13/16.
 */
@Parcel
public class Country extends GsonObject implements Comparable<Country> {

    public static final String COUNTRY_CODE_US = "US";
    public static final String COUNTRY_NAME_UNITED_STATES = "United States";

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
    public int compareTo(Country country) {
        return displayName.compareTo(country.getDisplayName());
    }
}