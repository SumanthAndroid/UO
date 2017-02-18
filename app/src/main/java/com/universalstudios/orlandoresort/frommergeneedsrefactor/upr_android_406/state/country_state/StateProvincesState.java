package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.country_state;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.State.ProvState;

/**
 * Note: Because this object is persisted, it should not be changed, but can be added to.
 *
 * @author Jack Hughes on 9/28/2016
 */
public class StateProvincesState extends GsonObject {

    @SerializedName("stateProvinces")
    ArrayList<ProvState> stateProvinces = new ArrayList<>();

    /**
     * Method to get the list of State/Provinces.
     *
     * @return an {@link ArrayList<ProvState>} list of State/Provinces
     */
    public ArrayList<ProvState> getStateProvinces() {
        return stateProvinces;
    }
}