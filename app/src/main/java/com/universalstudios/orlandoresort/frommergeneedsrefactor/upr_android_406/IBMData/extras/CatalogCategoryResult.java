package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 10/13/16.
 */
@Parcel
public class CatalogCategoryResult extends GsonObject {

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
