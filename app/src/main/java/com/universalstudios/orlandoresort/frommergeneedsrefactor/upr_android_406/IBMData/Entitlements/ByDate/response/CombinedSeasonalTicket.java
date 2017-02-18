package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Entitlements.ByDate.response;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Class to represent the JSON value of a combined seasonal ticket, used by {@link
 * GetTicketsByDateResponse}
 * <p/>
 * Created by Jack Hughes on 9/19/16.
 */
@Parcel
public class CombinedSeasonalTicket extends GsonObject {

    private static final String UVB_STR = "uvb";
    private static final String UVB_TWO_PARK = "2p";
    private static final String SPACE = " ";

    @SerializedName("computedSeason")
    String computedSeason;

    @SerializedName("detail")
    ArrayList<SeasonalTicketDetail> detail;

    /**
     * Method to get the computed season for a particular date. This takes into account all of the
     * {@link SeasonalTicketDetail} items inside of {@link #detail}, and summing up their seasons.
     *
     * @return a String for the computed (combined) season, (i.e. "mixed" for multiple seasons, or
     * if all season are the same inside the details, that value (i.e "value", "peak", or "regular")
     */
    public String getComputedSeason() {
        return computedSeason;
    }

    /**
     * Method to get the list of {@link SeasonalTicketDetail} for the combined seasonal ticket.
     *
     * @return an {@link ArrayList} of {@link SeasonalTicketDetail} for the seasonal ticket details
     */
    public ArrayList<SeasonalTicketDetail> getDetail() {
        return detail;
    }

    public String getUvbSeason() {
        return getSeasonTicketDetailSeason(UVB_STR);
    }

    public String getTwoParkSeason() {
        return getSeasonTicketDetailSeason(UVB_TWO_PARK);
    }

    /**
     * Loops through the list of {@link SeasonalTicketDetail}, looking at the season
     * which takes on a value from the service such as "Value UVB" and "Peak 2P".
     * Returns the first piece of the string (such as "Value" or "Peak"), matching the season type
     * against the "UVB" and "2P" portion.
     *
     * @param seasonParkType The given season type to match against
     * @return The season value returned
     */
    private String getSeasonTicketDetailSeason(String seasonParkType) {
        if (detail != null) {
            for (SeasonalTicketDetail seasonalTicketDetail : detail) {
                String seasonText = seasonalTicketDetail.getSeason();
                if (seasonText != null) {
                    // Check if the season text contains the seasonParkType
                    if (seasonText.toLowerCase().contains(seasonParkType)) {
                        String[] splitSeasonStr = seasonText.split(SPACE, 2);
                        // Capture the first word, splitting on space (i.e. "Value", "Peak")
                        if (splitSeasonStr.length > 0) {
                            return splitSeasonStr[0];
                        }
                    }
                }
            }
        }
        return computedSeason;
    }
}
