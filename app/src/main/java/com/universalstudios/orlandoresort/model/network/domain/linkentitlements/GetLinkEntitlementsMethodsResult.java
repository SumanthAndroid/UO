package com.universalstudios.orlandoresort.model.network.domain.linkentitlements;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 1/23/17.
 */
@Parcel
public class GetLinkEntitlementsMethodsResult extends GsonObject {
    @SerializedName("name")
    boolean name;
    @SerializedName("salesProgramId")
    boolean salesProgramId;
    @SerializedName("mediaStatus")
    String mediaStatus;

    public boolean isName() {
        return name;
    }

    public boolean isSalesProgramId() {
        return salesProgramId;
    }

    public String getMediaStatus() {
        return mediaStatus;
    }
}
