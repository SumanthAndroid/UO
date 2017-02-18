package com.universalstudios.orlandoresort.model.network.domain.appointments;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 1/12/17.
 */
@Parcel
public class AppointmentTimeExternalIds extends GsonObject {

    @SerializedName("Tridion")
    String tridion;

    public String getTridion() {
        return tridion;
    }
}
