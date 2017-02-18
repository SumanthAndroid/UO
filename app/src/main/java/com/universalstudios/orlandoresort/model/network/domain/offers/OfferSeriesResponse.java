package com.universalstudios.orlandoresort.model.network.domain.offers;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Wrapper response object for offer series
 */
@Parcel
public class OfferSeriesResponse extends NetworkResponse {

    /**
     * This is injected in {@link GetVendorOffersRequest}
     */
    OfferSeries offerSeries;

    @ParcelConstructor
    public OfferSeriesResponse(OfferSeries offerSeries) {
        this.offerSeries = offerSeries;
    }

    public OfferSeries getOfferSeries() {
        return offerSeries;
    }
}
