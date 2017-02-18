package com.universalstudios.orlandoresort.model.network.domain.offers;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * @author acampbell
 *
 */
@Parcel
public class GetOfferCouponResponse extends NetworkResponse {

    @SerializedName("CouponShortDescription")
    String shortDescription;

    @SerializedName("CouponCode")
    String couponCode;

    /**
     * @return the shortDescription
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * @return the couponCode
     */
    public String getCouponCode() {
        return couponCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((couponCode == null) ? 0 : couponCode.hashCode());
        result = prime * result + ((shortDescription == null) ? 0 : shortDescription.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GetOfferCouponResponse other = (GetOfferCouponResponse) obj;
        if (couponCode == null) {
            if (other.couponCode != null) {
                return false;
            }
        } else if (!couponCode.equals(other.couponCode)) {
            return false;
        }
        if (shortDescription == null) {
            if (other.shortDescription != null) {
                return false;
            }
        } else if (!shortDescription.equals(other.shortDescription)) {
            return false;
        }
        return true;
    }
}
