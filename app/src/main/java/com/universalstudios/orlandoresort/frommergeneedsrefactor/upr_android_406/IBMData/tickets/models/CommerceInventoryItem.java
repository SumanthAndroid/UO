package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: CommerceInventoryItem
 * Class Description: TODO: ALWAYS FILL OUT
 */
@Parcel
public class CommerceInventoryItem extends GsonObject {
    public static final String TAG = CommerceInventoryItem.class.getSimpleName();

    @SerializedName("status")
    Integer status;

    @SerializedName("available")
    Integer quantitiy;

    @SerializedName("eventId")
    Integer eventId;

    @SerializedName("resourceId")
    Integer resourceId;

    public Integer getStatus() {
        return status;
    }

    public Integer getQuantitiy() {
        return quantitiy;
    }

    public Integer getEventId() {
        return eventId;
    }

    public Integer getResourceId() {
        return resourceId;
    }

}
