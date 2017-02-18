package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Created by Tyler Ritchie on 11/16/16.
 */
public class GuestPreferenceSeasons extends GsonObject {

    @SerializedName("Spring Travel")
    private List<GuestPreferenceValue<Boolean>> springTravel;

    @SerializedName("Summer Travel")
    private List<GuestPreferenceValue<Boolean>> summerTravel;

    @SerializedName("Fall Travel")
    private List<GuestPreferenceValue<Boolean>> fallTravel;

    @SerializedName("Winter Travel")
    private List<GuestPreferenceValue<Boolean>> winterTravel;

    public List<GuestPreferenceValue<Boolean>> getFallTravel() {
        return fallTravel;
    }

    public List<GuestPreferenceValue<Boolean>> getSpringTravel() {
        return springTravel;
    }

    public List<GuestPreferenceValue<Boolean>> getSummerTravel() {
        return summerTravel;
    }

    public List<GuestPreferenceValue<Boolean>> getWinterTravel() {
        return winterTravel;
    }
}
