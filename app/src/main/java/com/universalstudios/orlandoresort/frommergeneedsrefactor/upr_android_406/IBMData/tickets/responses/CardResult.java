package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/14/16.
 * Class: SubmitPurchaseResponse
 * Class Description: Response class for submitting purchase data
 */
@Parcel
public class CardResult extends GsonObject {
    public static final String TAG = "CardResult";

    @SerializedName("accountLastFour")
    String accountLastFour;

    @SerializedName("amount")
    BigDecimal amount;

    @SerializedName("currencyCode")
    String currencyCode;

    @SerializedName("paymentType")
    String paymentType;

    @SerializedName("orderId")
    String orderId;

    @SerializedName("masterOrderNumber")
    String masterOrderNumber;

    @SerializedName("authorizationNumber")
    String authorizationNumber;

    @SerializedName("responseDate")
    String responseDate;

    @SerializedName("status")
    String status;

    @SerializedName("statusDescription")
    String statusDescription;

    public String getAccountLastFour() {
        return accountLastFour;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getMasterOrderNumber() {
        return masterOrderNumber;
    }

    public String getAuthorizationNumber() {
        return authorizationNumber;
    }

    public String getResponseDate() {
        return responseDate;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }
}
