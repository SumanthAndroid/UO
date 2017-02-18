package com.universalstudios.orlandoresort.model.network.domain.interactiveexperience;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author jamestimberlake
 * @created 4/28/16.
 */
@Parcel
public class AudioTimestamp extends GsonObject {

    @SerializedName("Id")
    Long id;

    @SerializedName("StartTimeInSecs")
    Float startTime;

    @SerializedName("EndTimeInSecs")
    Float endTime;

    @SerializedName("Text")
    String text;

    @SerializedName("DisplayImagesIntervalInSecs")
    Float displayImagesInterval;

    @SerializedName("DisplayImages")
    List<String> displayImages;

    @SerializedName("OverlayDisplayIntervalInSecs")
    Float overlayDisplayImagesInterval;

    @SerializedName("OverlayDisplayImages")
    List<String> overlayDisplyImages;

    public Long getId() {
        return id;
    }

    public Float getStartTime() {
        return startTime;
    }

    public Float getEndTime() {
        return endTime;
    }

    public String getText() {
        return text;
    }

    public Float getDisplayImagesInterval() {
        return displayImagesInterval;
    }

    public List<String> getDisplayImages() {
        return displayImages;
    }

    public Float getOverlayDisplayImagesInterval() {
        return overlayDisplayImagesInterval;
    }

    public List<String> getOverlayDisplyImages() {
        return overlayDisplyImages;
    }

    @Override
    public boolean equals(Object o) {
        if (null == o) {
            return false;
        }

        if (!(o instanceof AudioTimestamp)) {
            return false;
        }
        AudioTimestamp otherStamp = (AudioTimestamp) o;
        if (otherStamp.getId().equals(getId())) {
            return true;
        }

        return super.equals(o);
    }
}
