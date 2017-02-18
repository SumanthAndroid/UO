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
public class Reward extends GsonObject {

    @SerializedName("Id")
    Long id;

    @SerializedName("ExperienceRequired")
    Long experienceRequired;

    @SerializedName("VideoUrl")
    String videoUrl;

    @SerializedName("RewardImages")
    List<String> rewardImages;

    @SerializedName("MblDisplayName")
    String mblDisplayName;

    @SerializedName("HiddenUntilUnlock")
    Boolean hiddenUntilUnlock;

    public Long getId() {
        return id;
    }

    public Long getExperienceRequired() {
        return experienceRequired;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public List<String> getRewardImages() {
        return rewardImages;
    }

    public String getMblDisplayName() {
        return mblDisplayName;
    }

    public Boolean getHiddenUntilUnlock() {
        return hiddenUntilUnlock;
    }
}
