package com.universalstudios.orlandoresort.model.network.domain.linkentitlements;

import android.support.annotation.StringDef;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Tyler Ritchie on 1/23/17.
 */
@Parcel
public class LinkEntitlementsResult extends GsonObject {

    @SerializedName("mediaStatus")
    String mediaStatus;

    public String getMediaStatus() {
        return mediaStatus;
    }
}
