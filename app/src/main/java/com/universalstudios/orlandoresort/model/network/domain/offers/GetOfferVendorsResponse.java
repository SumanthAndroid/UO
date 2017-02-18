package com.universalstudios.orlandoresort.model.network.domain.offers;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author acampbell
 *
 */
@Parcel
public class GetOfferVendorsResponse extends NetworkResponse {

    @SerializedName("Vendors")
    List<String> vendors;

    /**
     * @return the vendors
     */
    public List<String> getVendors() {
        return vendors;
    }

}
