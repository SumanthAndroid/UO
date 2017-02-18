package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.shippinginfo;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import retrofit.RetrofitError;

/**
 * Created by bhargavi on 1/27/16.
 */
@Parcel
public class CartShippingInfoResponse extends NetworkResponse {

    @SerializedName("orderId")
    String orderId;

    RetrofitError retrofitError;

    public String getOrderId() {
        return orderId;
    }

    public RetrofitError getRetrofitError() {
        return retrofitError;
    }
}
