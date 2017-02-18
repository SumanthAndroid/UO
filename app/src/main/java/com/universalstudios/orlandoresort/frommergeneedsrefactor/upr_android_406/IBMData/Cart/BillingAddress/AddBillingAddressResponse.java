package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.BillingAddress;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Created by jamesblack on 5/25/16.
 */
@Parcel
public class AddBillingAddressResponse extends NetworkResponse {

    @SerializedName("addressId")
    String addressId;

    @SerializedName("userId")
    String userId;

    @SerializedName("resourceId")
    String resourceId;

    @SerializedName("resourceName ")
    String resourceName;

    /**
     *
     * @return
     * The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @return
     * The resourceId
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     *
     * @return
     * The resourceName
     */
    public String getResourceName() {
        return resourceName;
    }

    public String getAddressId() {
        return addressId;
    }

}
