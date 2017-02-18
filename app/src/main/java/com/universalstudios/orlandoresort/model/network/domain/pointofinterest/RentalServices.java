package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by GOKHAN on 6/6/2016.
 */
@Parcel
public class RentalServices extends PointOfInterest {
    public static String RENTAL_TYPE_ECVS = "ECVs";
    public static String RENTAL_TYPE_STROLLERS = "Strollers";
    public static String RENTAL_TYPE_WHEELCHAIRS = "Wheelchairs";

    @SerializedName("RentalTypes")
    List<String> rentalTypes;

    @SerializedName("Category")
    String category;

    @SerializedName("Prices")
    List<String> rentalPrices;

    @SerializedName("TermsAndConditions")
    String termsConditions;

    @Override
    public long getSubTypeFlags() {
        long subTypeFlags = 0l;

        if (rentalTypes != null && !rentalTypes.isEmpty()) {
            if (rentalTypes.contains(RENTAL_TYPE_ECVS)) {
                subTypeFlags |= UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RENTAL_TYPE_ECV;
            }
            if (rentalTypes.contains(RENTAL_TYPE_STROLLERS)) {
                subTypeFlags |= UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RENTAL_TYPE_STROLLER;
            }
            if (rentalTypes.contains(RENTAL_TYPE_WHEELCHAIRS)) {
                subTypeFlags |= UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RENTAL_TYPE_WHEELCHAIR;
            }
        }
        return super.getSubTypeFlags() | subTypeFlags;
    }

    public List<String> getRentalTypes() {
        return rentalTypes;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getRentalPrices() {
        return rentalPrices;
    }

    public String getTermsConditions() {
        return termsConditions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        RentalServices that = (RentalServices) o;

        if (rentalTypes != null ? !rentalTypes.equals(that.rentalTypes) : that.rentalTypes != null) {
            return false;
        }
        if (category != null ? !category.equals(that.category) : that.category != null) {
            return false;
        }
        if (rentalPrices != null ? !rentalPrices.equals(that.rentalPrices) : that.rentalPrices != null) {
            return false;
        }
        return termsConditions != null ? termsConditions.equals(that.termsConditions) : that.termsConditions == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (rentalTypes != null ? rentalTypes.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (rentalPrices != null ? rentalPrices.hashCode() : 0);
        result = 31 * result + (termsConditions != null ? termsConditions.hashCode() : 0);
        return result;
    }
}
