package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Created by Tyler Ritchie on 10/12/16.
 */

public class CategoryMetaData extends GsonObject {

    @SerializedName("metaKey")
    private String metaKey;

    @SerializedName("metaData")
    private String metaData;

    public String getMetaData() {
        return metaData;
    }

    public String getMetaKey() {
        return metaKey;
    }
}
