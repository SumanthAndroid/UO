package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Entitlements.ByDate.response;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Class to represent the JSON value of the seasonal ticket details, used by {@link
 * CombinedSeasonalTicket} Created by Jack Hughes on 9/19/16.
 */
@Parcel
public class SeasonalTicketDetail extends GsonObject {

    @SerializedName("parkName")
    String parkName;

    @SerializedName("numParks")
    String numParks;

    @SerializedName("season")
    String season;

    /**
     * Method to get the park name.
     *
     * @return a String for the name of the park, (i.e. "Volcano Bay")
     */
    public String getParkName() {
        return parkName;
    }

    /**
     * Method to get the number of parks.
     *
     * @return a String for the number of Parks. *NOTE* Should be a Integer coming back?
     */
    public String getNumParks() {
        return numParks;
    }

    /**
     * Method to get the season for the park.
     *
     * @return a String for the park's season, (i.e "peak")
     */
    public String getSeason() {
        return season;
    }
}
