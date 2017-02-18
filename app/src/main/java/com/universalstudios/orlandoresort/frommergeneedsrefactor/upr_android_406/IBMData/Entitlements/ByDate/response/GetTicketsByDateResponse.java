package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Entitlements.ByDate.response;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Entitlements.ByDate.request.GetTicketsByDateRequest;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Class to represent the JSON value of the response coming from the {@link
 * GetTicketsByDateRequest}
 * <p/>
 * Created by Jack Hughes on 9/19/16.
 */
@Parcel
public class GetTicketsByDateResponse extends NetworkResponse {

    @SerializedName("statusCode")
    String statusCode;

    @SerializedName("result")
    HashMap<String, CombinedSeasonalTicket> result;

    /**
     * Method to get the HTTP Status Code for the response.
     *
     * @return a String for the HTTP Status code
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * Method to get the {@link HashMap} (or associative array) of combined seasonal tickets. The
     * key is a String of the formatted date for each {@link CombinedSeasonalTicket} object.
     *
     * @return
     */
    public HashMap<String, CombinedSeasonalTicket> getResult() {
        return result;
    }

    /**
     * Static Method to get a String of the correctly formatted date (to use as the key for the
     * {@link HashMap} {@link #result}.
     *
     * @param dateToFormat
     *         the {@link Date} object to format
     *
     * @return a String of the correctly formatted Date
     */
    public static String getFormattedDateKey(Date dateToFormat) {
        // Date format: 2016-09-19
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return dateToFormat != null ? sdf.format(dateToFormat) : "";
    }
}
