package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.NumberUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 10/12/16.
 */
@Parcel
public class ProductSummaryFacetEntry extends GsonObject {

    @SerializedName("label")
    String label;

    @SerializedName("count")
    String count;

    @SerializedName("entryValue")
    String entryValue;

    public Integer getCount() {
        return NumberUtils.toInteger(count);
    }

    public String getEntryValue() {
        return entryValue;
    }

    public String getLabel() {
        return label;
    }
}
