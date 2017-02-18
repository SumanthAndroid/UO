package com.universalstudios.orlandoresort.model.network.domain.appointments;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Created by GOKHAN on 8/3/2016.
 */
@Parcel
public class DeleteCreatedAppointmentTicketResponse extends NetworkResponse {

    @SerializedName("Status")
    int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
