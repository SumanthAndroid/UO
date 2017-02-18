package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 10/12/16.
 */

@Parcel
public class ProductDetailsAttributesValuesExtended extends GsonObject {

    @SerializedName("key")
    String key;

    @SerializedName("value")
    String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
