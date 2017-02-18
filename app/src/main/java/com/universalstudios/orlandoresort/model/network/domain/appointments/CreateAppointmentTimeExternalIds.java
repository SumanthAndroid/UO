package com.universalstudios.orlandoresort.model.network.domain.appointments;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 *
 * Created by GOKHAN on 7/26/2016.
 */
public class CreateAppointmentTimeExternalIds extends GsonObject {

    @SerializedName("Tridion")
    private String tridion;

    public String getTridion() {return tridion;}
    public void setTridion(String tridion) {this.tridion = tridion;}

}
