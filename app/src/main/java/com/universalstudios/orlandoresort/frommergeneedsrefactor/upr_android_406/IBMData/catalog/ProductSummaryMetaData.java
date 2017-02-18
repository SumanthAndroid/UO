package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 10/12/16.
 */
@Parcel
public class ProductSummaryMetaData extends GsonObject {

    @SerializedName("metaKey")
    String metaKey;

    @SerializedName("metaData")
    String metaData;

    public String getMetaData() {
        return metaData;
    }

    public String getMetaKey() {
        return metaKey;
    }
}
