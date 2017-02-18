package com.universalstudios.orlandoresort.model.network.domain.photoframes;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 6/13/16.
 * Class: PhotoFrame
 * Class Description: Containing Object class for photo frames
 */
@Parcel
public class PhotoFrame extends GsonObject {

    @SerializedName("PhotoFrameDisplayName")
    String displayName;

    @SerializedName("PhotoFrameShortDescription")
    String shortDescription;

    @SerializedName("PhotoFrameLongDescription")
    String longDescription;

    @SerializedName("PhotoFrameDetailImage")
    String detailImageUrl;

    @SerializedName("PhotoFrameThumbnailImage")
    String thumbnailUrl;

    @SerializedName("Url")
    String url;

    @SerializedName("Id")
    long id;

    @SerializedName("ExternalIds")
    long[] externalIds;

    public String getUrl() {
        return url;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getDetailImageUrl() {
        return detailImageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
