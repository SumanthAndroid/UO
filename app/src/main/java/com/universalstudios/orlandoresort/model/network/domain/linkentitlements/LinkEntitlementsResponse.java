package com.universalstudios.orlandoresort.model.network.domain.linkentitlements;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 1/23/17.
 */
@Parcel
public class LinkEntitlementsResponse extends NetworkResponse {
    @SerializedName("result")
    LinkEntitlementsResult result;

    public LinkEntitlementsResult getResult() {
        return result;
    }
}
