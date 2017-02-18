package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Created by bhargavi on 6/5/16.
 */
@Parcel
public class TicketGroupingResponse extends NetworkResponse {

    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("result")
    TicketGroupingResult result;

    public int getStatusCode() {
        return statusCode;
    }

    public TicketGroupingResult getResult() {
        return result;
    }

    @Nullable
    public TicketGroupOrder getOrder() {
        if (result != null) {
            return result.getOrder();
        }
        return null;
    }
}
