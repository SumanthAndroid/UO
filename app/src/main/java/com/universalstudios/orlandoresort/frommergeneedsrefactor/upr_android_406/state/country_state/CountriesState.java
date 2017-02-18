package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.country_state;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Countries.Country;

/**
 * Note: Because this object is persisted, it should not be changed, but can be added to.
 *
 * @author Jack Hughes on 9/28/2016
 */
public class CountriesState extends GsonObject {

    @SerializedName("countries")
    private ArrayList<Country> countries = new ArrayList<>();

    /**
     * Method to get the list of Countries.
     *
     * @return an {@link ArrayList<Country>} list of Countries
     */
    public ArrayList<Country> getCountries() {
        return countries;
    }
}