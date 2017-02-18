package com.universalstudios.orlandoresort.model.network.domain.appointments;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by GOKHAN on 7/19/2016.
 */
@Parcel
public class GetAppointmentTimesResponse extends NetworkResponse {

    @SerializedName("Results")
    List<AppointmentTimes> AppointmentTimesResults;

    @SerializedName("TotalCount")
    int totalCount;

    @SerializedName("Pages")
    int pages;

    @SerializedName("PreviousPage")
    String previousPage;

    @SerializedName("NextPage")
    String nextPage;

    public List<AppointmentTimes> getAppointmentTimesResults() {
        return AppointmentTimesResults;
    }

    public void setAppointmentTimesResults(List<AppointmentTimes> appointmentTimesResults) {
        AppointmentTimesResults = appointmentTimesResults;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }
}
