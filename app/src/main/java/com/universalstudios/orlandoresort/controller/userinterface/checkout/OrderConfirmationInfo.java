package com.universalstudios.orlandoresort.controller.userinterface.checkout;

import android.databinding.BaseObservable;

import org.parceler.Parcel;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.offers.GetPersonalizationOffersRequest;

/**
 * Data object for order confirmation screen.
 *
 * @author tjudkins
 * @since 10/13/16
 */

@Parcel
public class OrderConfirmationInfo extends BaseObservable {

    public String confirmationNumber;
    public String firstName;
    public String lastName;
    public String emailAddress;
    public @GetPersonalizationOffersRequest.GeoLocationType String geoLocation;
    public PaymentSummaryInfo paymentSummaryInfo;

    public OrderConfirmationInfo() {
    }


}
