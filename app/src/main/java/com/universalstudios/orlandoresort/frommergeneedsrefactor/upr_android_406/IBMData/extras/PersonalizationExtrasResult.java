package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Tyler Ritchie on 10/13/16.
 */
@Parcel
public class PersonalizationExtrasResult extends NetworkResponse {

    @SerializedName("products")
    List<PersonalizationExtrasProduct> personalizationExtrasProduct;

    public List<PersonalizationExtrasProduct> getPersonalizationExtrasProduct() {
        return personalizationExtrasProduct;
    }

}
