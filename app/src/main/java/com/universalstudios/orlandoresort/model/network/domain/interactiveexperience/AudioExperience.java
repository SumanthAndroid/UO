package com.universalstudios.orlandoresort.model.network.domain.interactiveexperience;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author jamestimberlake
 * @created 4/25/16.
 */
@Parcel
public class AudioExperience extends GsonObject {

    @SerializedName("Id")
    Long id;

    @SerializedName("AudioContentId")
    Long audioContentId;

    @SerializedName("MblDisplayName")
    String mblDisplayName;

    //This is used on MasterExperiences
    //Server should really return the same for both audio and master
    @SerializedName("DisplayView")
    ExperienceView experienceView;

    //This is for display on AudioExpeiences
    @SerializedName("OptionalDisplayView")
    ExperienceView optionalDisplayView;

    @SerializedName("ClosedCaptioningAvailable")
    boolean mHasClosedCaption;

    @SerializedName("ClosedCaptioningForced")
    boolean mShouldForceClosedCaption;

    @SerializedName("ClosedCaptioningDefaultOn")
    boolean mIsClosedCaptionDefaultOn;

    @SerializedName("AudioTimestamps")
    List<AudioTimestamp> audioTimestamps;

    public boolean hasClosedCaption() {
        return mHasClosedCaption;
    }

    public boolean shouldForceShowClosedCaption() {
        return mShouldForceClosedCaption;
    }

    public boolean shouldDefaultClosedCaptionOn() {
        return mIsClosedCaptionDefaultOn;
    }

    public Long getId() {
        return id;
    }

    public Long getAudioContentId() {
        return audioContentId;
    }

    public String getMblDisplayName() {
        return mblDisplayName;
    }

    public ExperienceView getExperienceView() {
        return experienceView;
    }

    public List<AudioTimestamp> getAudioTimestamps() {
        return audioTimestamps;
    }

    public ExperienceView getOptionalDisplayView() {
        return optionalDisplayView;
    }
}
