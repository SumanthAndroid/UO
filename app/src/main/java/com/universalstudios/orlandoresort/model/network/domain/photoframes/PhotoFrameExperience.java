package com.universalstudios.orlandoresort.model.network.domain.photoframes;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 6/19/16.
 * Class: PhotoFrameExperience
 * Class Description: Class containing information for the photo frame experience
 */
@Parcel
public class PhotoFrameExperience extends GsonObject {
    public static final String TAG = PhotoFrameExperience.class.getSimpleName();

    @SerializedName("PhotoFrameExperienceId")
    long photoFrameExperienceId;

    @SerializedName("MobileShortDescription")
    String shortDescription;

    @SerializedName("MobileLongDescription")
    String longDiscription;

    @SerializedName("PhotoFrames")
    List<PhotoFrame> photoFrames;

    @SerializedName("Url")
    String url;

    @SerializedName("Id")
    long id;

    @SerializedName("ExernalIds")
    long[] externalIds;

    public long getPhotoFrameExperienceId() {
        return photoFrameExperienceId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDiscription() {
        return longDiscription;
    }

    public List<PhotoFrame> getPhotoFrames() {
        return photoFrames;
    }

    public String getUrl() {
        return url;
    }

    public long getId() {
        return id;
    }

    public long[] getExternalIds() {
        return externalIds;
    }
}
