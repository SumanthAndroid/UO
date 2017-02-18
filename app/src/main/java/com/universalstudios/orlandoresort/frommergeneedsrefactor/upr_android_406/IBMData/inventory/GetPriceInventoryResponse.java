package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 10/13/16.
 */

@Parcel
public class GetPriceInventoryResponse extends NetworkResponse {

    public static final String MAP_DATE_FORMAT = "yyyy-MM-dd";

    // Within the response JSON, "eventAvailability" was designated as an array containing generated
    // keys, those keys containing values with their own arrays
    @SerializedName("eventAvailability")
    List<Map<String, PriceInventoryEventAvailabilityDate>> priceInventoryEventAvailabilities;


    /**
     * This accessor takes each item in the priceInventoryEventAvailabilities list, insert an object layer to nest
     * the Map of String to PriceInventoryEventAvailabilityDate for readability/usability's sake.
     *
     * @return The List of PriceInventoryEventAvailabilityDate, each element containing the Map
     */
    public List<Map<String, PriceInventoryEventAvailabilityDate>> getPriceInventoryEventAvailabilities() {
        return priceInventoryEventAvailabilities;
    }

    public List<PriceInventoryEventAvailabilityDate> getInventoryForDate(Date selectedDate) {
        return getInventoryForDate(dateToString(selectedDate));
    }

    public List<PriceInventoryEventAvailabilityDate> getInventoryForDate(String selectedDate) {
        if (selectedDate == null) {
            return null;
        }

        List<Map<String, PriceInventoryEventAvailabilityDate>> list = getPriceInventoryEventAvailabilities();
        List<PriceInventoryEventAvailabilityDate> availabilityDates = new ArrayList<>();
        for (Map<String, PriceInventoryEventAvailabilityDate> dateMap : list) {
            PriceInventoryEventAvailabilityDate date = dateMap.get(selectedDate);
            if (date != null) {
                availabilityDates.add(dateMap.get(selectedDate));
            }
        }
        return availabilityDates;
    }

    public Map<String, Map<String, PriceInventoryEventAvailabilityDate>> getPartNumberInventoryMap() {
        List<Map<String, PriceInventoryEventAvailabilityDate>> inventory = getPriceInventoryEventAvailabilities();
        Map<String, Map<String, PriceInventoryEventAvailabilityDate>> partNumberInventoryMap = new HashMap<>();
        if (inventory != null) {
            for (Map<String, PriceInventoryEventAvailabilityDate> availabilityDateMap : inventory) {
                if (availabilityDateMap != null) {
                    for (String dateKey : availabilityDateMap.keySet()) {
                        PriceInventoryEventAvailabilityDate date = availabilityDateMap.get(dateKey);
                        if (date != null) {
                            List<PriceInventoryEventAvailabilityInvEvent> events = date.getPriceInventoryEventAvailabilityInvEvents();
                            if (events != null) {
                                for (PriceInventoryEventAvailabilityInvEvent event : events) {
                                    String partNumber = event.getPartNumber();
                                    Map<String, PriceInventoryEventAvailabilityDate> map = partNumberInventoryMap.get(partNumber);
                                    if (map == null) {
                                        map = new HashMap<>();
                                    }
                                    map.put(dateKey, date);
                                    partNumberInventoryMap.put(partNumber, map);
                                }
                            }
                        }
                    }
                }
            }
        }
        return partNumberInventoryMap;
    }

    /**
     * get a list of unique show times available for the given date
     *
     * @return
     */
    public List<String> getShowTimesForDate(Date selectedDate) {
        return getShowTimesForDate(dateToString(selectedDate));
    }

    /**
     * @param selectedDate
     * @return
     */
    public List<String> getShowTimesForDate(String selectedDate) {
        List<PriceInventoryEventAvailabilityDate> dates = getInventoryForDate(selectedDate);

        //use set because it is unique
        Set<String> temp = new HashSet<>();
        if (dates != null) {
            for (PriceInventoryEventAvailabilityDate date : dates) {
                if (date != null) {
                    List<PriceInventoryEventAvailabilityInvEvent> events = date.getPriceInventoryEventAvailabilityInvEvents();
                    if (events != null) {
                        for (PriceInventoryEventAvailabilityInvEvent event : events) {
                            if (event != null) {
                                String time = null;
                                // Shows have a showTime while other events only have a start date
                                if (!TextUtils.isEmpty(event.getShowTime())) {
                                    time = event.getShowTime();
                                } else if (event.getStartDate() != null) {
                                    time = event.getStartTime();
                                }
                                if (!TextUtils.isEmpty(time)) {
                                    temp.add(time);
                                }
                            }
                        }
                    }
                }
            }
        }
        List<String> result = new ArrayList<>();
        result.addAll(temp);
        Collections.sort(result);
        return result;
    }

    @Nullable
    public PriceInventoryEventAvailabilityInvEvent getEventForDateTimeAndPartNumber(Date date, String parNumber) {
        if (date != null && !TextUtils.isEmpty(parNumber) && priceInventoryEventAvailabilities != null) {
            String dateString = dateToString(date);
            for (Map<String, PriceInventoryEventAvailabilityDate> dates : priceInventoryEventAvailabilities) {
                if (dates != null) {
                    PriceInventoryEventAvailabilityDate priceInventoryEventAvailabilityDate = dates.get(dateString);
                    if (priceInventoryEventAvailabilityDate !=  null) {
                        PriceInventoryEventAvailabilityInvEvent invEvent = priceInventoryEventAvailabilityDate.getEventForDate(date);
                        if (invEvent != null && parNumber.equals(invEvent.getPartNumber())) {
                            return invEvent;
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * @param date
     * @return
     */
    private String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(MAP_DATE_FORMAT);
        return sdf.format(date);
    }
}
