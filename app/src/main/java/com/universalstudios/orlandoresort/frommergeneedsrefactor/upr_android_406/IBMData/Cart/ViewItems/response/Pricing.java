package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IBM_ADMIN on 1/25/2016.
 */
@Parcel
public class Pricing extends GsonObject {

    @SerializedName("discount")
    BigDecimal discount;

    @SerializedName("fee")
    BigDecimal fee;

    @SerializedName("listPrice")
    BigDecimal listPrice;

    @SerializedName("currency")
    String currency;

    @SerializedName("shippingCharge")
    BigDecimal shippingCharge;

    @SerializedName("unitPrice")
    BigDecimal unitPrice;

    @SerializedName("adjustments")
    List<Adjustment> adjustments;

    @SerializedName("offerId")
    String offerId;

    @SerializedName("adjustmentTotal")
    BigDecimal adjustmentTotal;

    @SerializedName("orderItemPrice")
    BigDecimal orderItemPrice;

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

    @SerializedName("priceSavings")
    BigDecimal priceSavings;

    @SerializedName("totalFee")
    BigDecimal totalFee;

    /**
     * @return The currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @return The shippingCharge
     */
    public BigDecimal getShippingCharge() {
        return shippingCharge;
    }

    /**
     * @return The unitPrice
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * @return The adjustments
     */
    public List<Adjustment> getAdjustments() {
        return adjustments;
    }

    /**
     * @return The offerId
     */
    public String getOfferId() {
        return offerId;
    }

    /**
     * @return The adjustmentTotal
     */
    public BigDecimal getAdjustmentTotal() {
        return adjustmentTotal;
    }

    /**
     * @return The orderItemPrice
     */
    public BigDecimal getOrderItemPrice() {
        return orderItemPrice;
    }

    /**
     * @return The grandTotal
     */
    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    /**
     * @return The taxes
     */
    public List<Taxes> getTaxes() {
        return taxes;
    }

    /**
     * @return The shippingTax
     */
    public BigDecimal getShippingTax() {
        return shippingTax;
    }

    /**
     * @return The totalProductPrice
     */
    public BigDecimal getTotalProductPrice() {
        return totalProductPrice;
    }


    /**
     * @return The salesTax
     */
    public BigDecimal getSalesTax() {
        return salesTax;
    }

    public BigDecimal getPriceSavings() {
        return priceSavings;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public BigDecimal getListPrice() {
        return listPrice;
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
