package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.BillingAddress;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Created by IBM_ADMIN on 6/20/2016.
 */
public class Errors extends GsonObject {

    @SerializedName("errors")
    private List<BillingAddressError> errors = new ArrayList<BillingAddressError>();

    /**
     *
     * @return
     * The errors
     */
    public List<BillingAddressError> getErrors() {
        return errors;
    }

    /**
     *
     * @param errors
     * The errors
     */
    public void setErrors(List<BillingAddressError> errors) {
        this.errors = errors;
    }

}
