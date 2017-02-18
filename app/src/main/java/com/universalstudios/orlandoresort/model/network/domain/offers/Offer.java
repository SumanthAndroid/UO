package com.universalstudios.orlandoresort.model.network.domain.offers;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.Comparator;
import java.util.List;

/**
 * @author acampbell
 *
 */
@Parcel
public class Offer extends PointOfInterest {

    private static final String TAG = Offer.class.getSimpleName();

    public static class AscendingComparator implements Comparator<Offer> {

        @Override
        public int compare(Offer lhs, Offer rhs) {
            if (lhs != null && rhs != null && lhs.getDisplayName() != null && rhs.getDisplayName() != null) {
                return lhs.getDisplayName().compareTo(rhs.getDisplayName());
            }
            return 0;
        }

    }

    public static final String OFFER_TYPE_FOOD_AND_BEVERAGE = "FoodAndBeverage";
    public static final String OFFER_TYPE_MERCHANDISE = "Merchandise";
    public static final String OFFER_TYPE_LOUNGE = "Lounge";
    public static final String OFFER_TYPE_OTHER = "Other";

    @SerializedName("EndDate")
    String endDate;

    @SerializedName("EndDateUnix")
    Long endDateUnix;

    @SerializedName("RequiresCouponCode")
    Boolean requiresCouponCode;

    @SerializedName("TermsAndConditions")
    String termsAndConditions;

    @SerializedName("Exclusions")
    String exclusions;

    @SerializedName("OfferType")
    String offerType;

    @SerializedName("AssociatedPointsOfInterest")
    List<PointOfInterest> associatedPointsOfInterest;

    @SerializedName("LocationHours")
    String locationHours;

    @SerializedName("LocationDays")
    String locationDays;

    @SerializedName("CouponCodeUrl")
    String couponCodeUrl;

    @SerializedName("OfferAdditionalDetails")
    String offerAdditionalDetails;

    @SerializedName("ShowOfferDetailPage")
    Boolean showOfferDetailPage;

    public static boolean areOffersEqual(Offer offer, String offerObjectJson) {
        try {
            Offer offerFromJson = GsonObject.fromJson(offerObjectJson, Offer.class);
            return offer.equals(offerFromJson);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "areOffersEqual: exception trying to compare Offers", e);
            }
        }

        return false;
    }

    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @return the endDateUnix
     */
    public Long getEndDateUnix() {
        return endDateUnix;
    }

    /**
     * @return the requiresCouponCode
     */
    public Boolean getRequiresCouponCode() {
        return requiresCouponCode;
    }

    /**
     * @return the termsAndConditions
     */
    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    /**
     * @return the exclusions
     */
    public String getExclusions() {
        return exclusions;
    }

    /**
     * @return the offerType
     */
    public String getOfferType() {
        return offerType;
    }

    /**
     * @return the associatedPointsOfInterest
     */
    public List<PointOfInterest> getAssociatedPointsOfInterest() {
        return associatedPointsOfInterest;
    }

    /**
     * @return the locationHours
     */
    public String getLocationHours() {
        return locationHours;
    }

    /**
     * @return the locationDays
     */
    public String getLocationDays() {
        return locationDays;
    }
    /**
     * @return the locationDays
     */
    public String getOfferAdditionalDetails() {
        return offerAdditionalDetails;
    }

    /**
     * @return the locationDays
     */
    public Boolean getShowOfferDetailPage() {
        return showOfferDetailPage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((associatedPointsOfInterest == null) ? 0 : associatedPointsOfInterest.hashCode());
        result = prime * result + ((couponCodeUrl == null) ? 0 : couponCodeUrl.hashCode());
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + ((endDateUnix == null) ? 0 : endDateUnix.hashCode());
        result = prime * result + ((exclusions == null) ? 0 : exclusions.hashCode());
        result = prime * result + ((locationDays == null) ? 0 : locationDays.hashCode());
        result = prime * result + ((locationHours == null) ? 0 : locationHours.hashCode());
        result = prime * result + ((offerType == null) ? 0 : offerType.hashCode());
        result = prime * result + ((requiresCouponCode == null) ? 0 : requiresCouponCode.hashCode());
        result = prime * result + ((termsAndConditions == null) ? 0 : termsAndConditions.hashCode());
        result = prime * result + ((offerAdditionalDetails == null) ? 0 : offerAdditionalDetails.hashCode());
        result = prime * result + ((showOfferDetailPage == null) ? 0 : showOfferDetailPage.hashCode());
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
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Offer other = (Offer) obj;
        if (associatedPointsOfInterest == null) {
            if (other.associatedPointsOfInterest != null) {
                return false;
            }
        } else if (!associatedPointsOfInterest.equals(other.associatedPointsOfInterest)) {
            return false;
        }
        if (couponCodeUrl == null) {
            if (other.couponCodeUrl != null) {
                return false;
            }
        } else if (!couponCodeUrl.equals(other.couponCodeUrl)) {
            return false;
        }
        if (endDate == null) {
            if (other.endDate != null) {
                return false;
            }
        } else if (!endDate.equals(other.endDate)) {
            return false;
        }
        if (endDateUnix == null) {
            if (other.endDateUnix != null) {
                return false;
            }
        } else if (!endDateUnix.equals(other.endDateUnix)) {
            return false;
        }
        if (exclusions == null) {
            if (other.exclusions != null) {
                return false;
            }
        } else if (!exclusions.equals(other.exclusions)) {
            return false;
        }
        if (locationDays == null) {
            if (other.locationDays != null) {
                return false;
            }
        } else if (!locationDays.equals(other.locationDays)) {
            return false;
        }
        if (locationHours == null) {
            if (other.locationHours != null) {
                return false;
            }
        } else if (!locationHours.equals(other.locationHours)) {
            return false;
        }
        if (offerType == null) {
            if (other.offerType != null) {
                return false;
            }
        } else if (!offerType.equals(other.offerType)) {
            return false;
        }
        if (requiresCouponCode == null) {
            if (other.requiresCouponCode != null) {
                return false;
            }
        } else if (!requiresCouponCode.equals(other.requiresCouponCode)) {
            return false;
        }
        if (termsAndConditions == null) {
            if (other.termsAndConditions != null) {
                return false;
            }
        } else if (!termsAndConditions.equals(other.termsAndConditions)) {
            return false;
        }
        if(offerAdditionalDetails == null) {
            if(other.offerAdditionalDetails != null) {
                return false;
            }
        } else if(!offerAdditionalDetails.equals(other.offerAdditionalDetails)) {
            return false;
        }
        if(showOfferDetailPage == null) {
            if(other.offerAdditionalDetails != null) {
                return false;
            }
        } else if(!showOfferDetailPage.equals(other.showOfferDetailPage)) {
            return false;
        }
        return true;
    }

}
