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
public class InteractiveMasterExperience extends GsonObject {

    public enum ExperienceType{
        Audio,
        Reward
    }

    @SerializedName("Id")
    Long Id;

    @SerializedName("ExperienceName")
    String experienceName;

    @SerializedName("ExperienceType")
    String experienceType;

    @SerializedName("Triggers")
    List<Trigger> triggers;

    @SerializedName("AudioExperiences")
    List<AudioExperience> audioExperiences;

    @SerializedName("RewardExperience")
    RewardExperience rewardExperience;

    @SerializedName("DisplayView")
    ExperienceView displayView;

    public Long getId() {
        return Id;
    }

    public String getExperienceName() {
        return experienceName;
    }

    public String getExperienceType() {
        return experienceType;
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public List<AudioExperience> getAudioExperiences() {
        return audioExperiences;
    }

    public RewardExperience getRewardExperience() {
        return rewardExperience;
    }

    public ExperienceView getDisplayView() {
        return displayView;
    }
}
