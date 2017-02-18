package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 10/12/16.
 */
@Parcel
public class ProductSummaryBreadCrumbTrailEntry extends GsonObject {

    @SerializedName("value")
    String value;

    @SerializedName("label")
    String label;

    @SerializedName("type")
    String type;

    public String getLabel() {
        return label;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
