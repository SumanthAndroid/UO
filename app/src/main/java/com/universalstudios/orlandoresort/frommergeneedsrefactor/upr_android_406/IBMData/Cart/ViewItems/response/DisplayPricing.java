package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author jamestimberlake
 * @created 1/2/17.
 */
@Parcel
public class DisplayPricing extends GsonObject {

    @SerializedName("totalDownPayment")
    BigDecimal totalDownPayment;

    @SerializedName("shippingCharge")
    BigDecimal shippingCharge;

    @SerializedName("totalDownPaymentTax")
    BigDecimal totalDownPaymentTax;

    @SerializedName("totalCharged")
    BigDecimal totalCharged;

    @SerializedName("totalFinanced")
    BigDecimal totalFinanced;

    @SerializedName("currency")
    String currency;

    @SerializedName("totalPriceSavings")
    BigDecimal totalPriceSavings;

    @SerializedName("adjustmentTotal")
    BigDecimal adjustmentTotal;

    @SerializedName("grandTotal")
    BigDecimal grandTotal;

    @SerializedName("taxes")
    List<Taxes> taxes;

    @SerializedName("shippingTax")
    BigDecimal shippingTax;

    @SerializedName("totalProductPrice")
    BigDecimal totalProductPrice;

    @SerializedName("salesTax")
    BigDecimal salesTax;

    @SerializedName("paymentPlans")
    List<PaymentPlan> paymentPlans;

    @SerializedName("adjustments")
    List<Adjustment> adjustments;

    public BigDecimal getTotalDownPayment() {
        return totalDownPayment;
    }

    public BigDecimal getShippingCharge() {
        return shippingCharge;
    }

    public BigDecimal getTotalDownPaymentTax() {
        return totalDownPaymentTax;
    }

    public BigDecimal getTotalCharged() {
        return totalCharged;
    }

    public BigDecimal getTotalFinanced() {
        return totalFinanced;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getTotalPriceSavings() { return totalPriceSavings; }

    public BigDecimal getAdjustmentTotal() {
        return adjustmentTotal;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public List<Taxes> getTaxes() {
        return taxes;
    }

    public BigDecimal getShippingTax() {
        return shippingTax;
    }

    public BigDecimal getTotalProductPrice() {
        return totalProductPrice;
    }

    public BigDecimal getSalesTax() {
        return salesTax;
    }

    public List<PaymentPlan> getPaymentPlans() {
        return paymentPlans;
    }

    public List<Adjustment> getAdjustments() {
        return adjustments;
    }

    @NonNull
    public BigDecimal getPaymentPlanTotalPaymentAmount() {
        BigDecimal totalPaymentAmount = BigDecimal.ZERO;
        if (paymentPlans != null) {
            for (PaymentPlan paymentPlan : paymentPlans) {
                if (paymentPlan != null && paymentPlan.getRecurringPaymentAmount() != null) {
                    totalPaymentAmount = totalPaymentAmount.add(paymentPlan.getRecurringPaymentAmount());
                }
            }
        }
        return totalPaymentAmount;
    }

    @Nullable
    public PaymentPlan getFirstPaymentPlan() {
        if (paymentPlans != null) {
            for (PaymentPlan paymentPlan : paymentPlans) {
                if (paymentPlan != null) {
                    return paymentPlan;
                }
            }
        }
        return null;
    }

    /**
     * Return the first adjustment, there should only be one type but may be multiple entries
     */
    @Nullable
    public Adjustment getFirstAdjustment() {
        Adjustment firstAdjustment = null;
        if (adjustments != null && !adjustments.isEmpty()) {
            for (Adjustment adjustment : adjustments) {
                if (adjustment != null) {
                    firstAdjustment = adjustment;
                    break;
                }
            }
        }
        return firstAdjustment;
    }
}
