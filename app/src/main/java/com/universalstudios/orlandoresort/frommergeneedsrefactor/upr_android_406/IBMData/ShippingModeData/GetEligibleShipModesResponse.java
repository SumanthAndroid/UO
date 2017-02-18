package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ShippingModeData;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by IBM_ADMIN on 2/6/2016.
 */
@Parcel
public class GetEligibleShipModesResponse extends NetworkResponse {

    @SerializedName("deliveryOptions")
    List<DeliveryOption> deliveryOptions;

    /**
     * @return The deliveryOptions
     */
    public List<DeliveryOption> getDeliveryOptions() {
        return deliveryOptions;
    }

}
