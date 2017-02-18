/**
 * 
 */
package com.universalstudios.orlandoresort.model.network.domain.events;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author acampbell
 *
 */
@Parcel
public class GetEventSeriesResponse extends NetworkResponse {

    @SerializedName("Results")
    List<EventSeries> eventSeries;

    /**
     * @return the eventSeries
     */
    public List<EventSeries> getEventSeries() {
        return eventSeries;
    }

}
