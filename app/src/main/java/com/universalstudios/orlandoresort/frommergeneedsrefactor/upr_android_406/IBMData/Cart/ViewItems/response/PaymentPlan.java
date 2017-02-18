package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;

/**
 * @author acampbell
 */
@Parcel
public class PaymentPlan extends GsonObject {

    @SerializedName("recurringPaymentAmount")
    BigDecimal recurringPaymentAmount;

    @SerializedName("currency")
    String currency;

    @SerializedName("financedAmount")
    BigDecimal financedAmount;

    @SerializedName("downPayment")
    BigDecimal downPayment;

    @SerializedName("firstPaymentDate")
    String firstPaymentDate;

    @SerializedName("recurringPaymentCount")
    Integer recurringPaymentCount;

    @SerializedName("paymentPlanId")
    String paymentPlanId;

    @SerializedName("contractId")
    String contractId;

    public BigDecimal getRecurringPaymentAmount() {
        return recurringPaymentAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getFinancedAmount() {
        return financedAmount;
    }

    public BigDecimal getDownPayment() {
        return downPayment;
    }

    public String getFirstPaymentDate() {
        return firstPaymentDate;
    }

    public Integer getRecurringPaymentCount() {
        return recurringPaymentCount;
    }

    public String getPaymentPlanId() {
        return paymentPlanId;
    }

    public String getContractId() {
        return contractId;
    }
}