package com.universalstudios.orlandoresort.controller.userinterface.checkout;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.universalstudios.orlandoresort.model.pricing.DisplayPricingModel;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Data object for payment summary on order confirmation screen.
 *
 * @author tjudkins
 * @since 10/11/16
 */

@Parcel
public class PaymentSummaryInfo extends BaseObservable {

    @Bindable
    final String creditCardNumber;
    @Bindable
    final String creditCardType;
    @Bindable
    final String totalCharged;
    @Bindable
    final DisplayPricingModel pricingModel;

    @ParcelConstructor
    public PaymentSummaryInfo(String creditCardNumber, String creditCardType, String totalCharged, DisplayPricingModel pricingModel) {
        this.creditCardNumber = creditCardNumber;
        this.creditCardType = creditCardType;
        this.totalCharged = totalCharged;
        this.pricingModel = pricingModel;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public DisplayPricingModel getPricingModel() {
        return pricingModel;
    }

    public String getTotalCharged() {
        return totalCharged;
    }
}
