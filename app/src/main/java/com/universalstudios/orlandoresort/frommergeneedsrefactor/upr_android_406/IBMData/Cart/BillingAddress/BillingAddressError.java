package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.BillingAddress;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Created by IBM_ADMIN on 6/20/2016.
 */
public class BillingAddressError extends GsonObject {

    @SerializedName("errorCode")
    private String errorCode;

    @SerializedName("errorKey")
    private String errorKey;

    @SerializedName("errorMessage")
    private String errorMessage;

    /**
     *
     * @return
     * The errorCode
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     *
     * @param errorCode
     * The errorCode
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     *
     * @return
     * The errorKey
     */
    public String getErrorKey() {
        return errorKey;
    }

    /**
     *
     * @param errorKey
     * The errorKey
     */
    public void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }

    /**
     *
     * @return
     * The errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     *
     * @param errorMessage
     * The errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
