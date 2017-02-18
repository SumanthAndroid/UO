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
public class RewardExperience extends GsonObject {

    @SerializedName("Id")
    Long id;

    @SerializedName("MblDisplayName")
    String mblDisplayName;

    @SerializedName("MblShortDescription")
    String mblShortDescription;

    @SerializedName("Rewards")
    List<Reward> rewards;

    public Long getId() {
        return id;
    }

    public String getMblDisplayName() {
        return mblDisplayName;
    }

    public String getMblShortDescription() {
        return mblShortDescription;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

}
