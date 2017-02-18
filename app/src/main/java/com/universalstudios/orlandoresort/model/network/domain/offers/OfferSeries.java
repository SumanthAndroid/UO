package com.universalstudios.orlandoresort.model.network.domain.offers;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author acampbell
 *
 */
@Parcel
public class OfferSeries extends PointOfInterest {

    private static final String TAG = OfferSeries.class.getSimpleName();

    @SerializedName("Offers")
    List<Offer> offers;

    @SerializedName("ApplyButtonUrl")
    String applyButtonUrl;

    @SerializedName("ApplyButtonText")
    String applyButtonText;

    public static boolean areOfferSeriesEqual(OfferSeries offerSeries, String offerSeriesObjectJson) {
        try {
            OfferSeries offerSeriesFromJson = GsonObject.fromJson(offerSeriesObjectJson, OfferSeries.class);
            return offerSeries.equals(offerSeriesFromJson);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "areOfferSeriesEqual: exception trying to compare Offer series", e);
            }
        }

        return false;
    }

    /**
     * @return the offers
     */
    public List<Offer> getOffers() {
        return offers;
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
        result = prime * result + ((offers == null) ? 0 : offers.hashCode());
        result = prime * result + ((applyButtonUrl == null) ? 0 : applyButtonUrl.hashCode());
        result = prime * result + ((applyButtonText == null) ? 0 : applyButtonText.hashCode());
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
        OfferSeries other = (OfferSeries) obj;
        if (offers == null) {
            if (other.offers != null) {
                return false;
            }
        } else if (!offers.equals(other.offers)) {
            return false;
        }
        if (other.getApplyButtonText() != null && !other.getApplyButtonText().equals(this.getApplyButtonText())) {
          return false;
        }
        if (other.getApplyButtonUrl() != null && !other.getApplyButtonUrl().equals(this.getApplyButtonUrl())) {
          return false;
        }
        return true;
    }

    
    public static String getTag() {
      return TAG;
    }

    
    public String getApplyButtonText() {
      return applyButtonText;
    }

    
    public String getApplyButtonUrl() {
      return applyButtonUrl;
    }

}
