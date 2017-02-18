package com.universalstudios.orlandoresort.model.network.domain.appointments.queues;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by GOKHAN on 7/12/2016.
 */
@Parcel
public class GetQueuesByPageResponse extends NetworkResponse {

    @SerializedName("Results")
    List<QueuesResult> results;
    
    @SerializedName("TotalCount")
    int totalCount;
    
    @SerializedName("Pages")
    int page;
    
    @SerializedName("PreviousPage")
    String previousPage;
    
    @SerializedName("NextPage")
    String nextPage;

    public List<QueuesResult> getResults() {
        return results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPage() {
        return page;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public String getNextPage() {
        return nextPage;
    }
    
}
