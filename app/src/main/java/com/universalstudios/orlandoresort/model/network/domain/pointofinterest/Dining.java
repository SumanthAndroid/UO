package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author Steven Byle
 */
@Parcel
public class Dining extends PointOfInterest implements HoursProvider, PricesProvider {

    // Dining service values
    public static final String DINING_SERVICE_BREAKFAST = "Breakfast";
    public static final String DINING_SERVICE_LUNCH = "Lunch";
    public static final String DINING_SERVICE_DINNER = "Dinner";
    public static final String DINING_SERVICE_CHARACTER_DINING = "CharacterDining";
    public static final String DINING_SERVICE_SNACK = "Snack";

    // Dining type values
    public static final String DINING_TYPE_FINE_DINING = "FineDining";
    public static final String DINING_TYPE_CASUAL_DINING = "CasualDining";
    public static final String DINING_TYPE_QUICK_SERVICE = "QuickService";
    public static final String DINING_TYPE_SNACKS = "Snacks";

    @SerializedName("QuickServeParticipant")
    Boolean quickServeParticipant;

    @SerializedName("DiningPlanAccepted")
    Boolean diningPlanAccepted;

    @SerializedName("DiningServices")
    List<String> diningServices;

    @SerializedName("DiningTypes")
    List<String> diningTypes;

    @SerializedName("MinPrice")
    Double minPrice;

    @SerializedName("MaxPrice")
    Double maxPrice;

    @SerializedName("HasHealthyOptions")
    Boolean hasHealthyOptions;

    @SerializedName("PhoneNumber")
    String phoneNumber;

    @Deprecated
    @SerializedName("GeneralOpenTime")
    String generalOpenTime;

    @Deprecated
    @SerializedName("GeneralCloseTime")
    String generalCloseTime;

    @Deprecated
    @SerializedName("MenuUrl")
    String menuUrl;

    @SerializedName("ReservationsSuggested")
    Boolean areReservationsSuggested;

    @SerializedName("DailyHours")
    DailyHours dailyHours;

    @SerializedName("MenuImages")
    List<String> menuImages;

    @SerializedName("HasCocaColaFreestyleMachine")
    Boolean hasCocaColaFreestyleMachine;

    @Override
    public long getSubTypeFlags() {
        long subTypeFlags = 0l;

        if (diningTypes != null) {
            for (String diningType : diningTypes) {
                if (diningType.equalsIgnoreCase(DINING_TYPE_FINE_DINING)) {
                    subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_FINE_DINING;
                } else if (diningType.equalsIgnoreCase(DINING_TYPE_CASUAL_DINING)) {
                    subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_CASUAL_DINING;
                } else if (diningType.equalsIgnoreCase(DINING_TYPE_QUICK_SERVICE)) {
                    subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_QUICK_SERVICE;
                } else if (diningType.equalsIgnoreCase(DINING_TYPE_SNACKS)) {
                    subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_SNACKS;
                }
            }
        }
        if (diningServices != null) {
            for (String diningService : diningServices) {
                if (diningService.equalsIgnoreCase(DINING_SERVICE_BREAKFAST)) {
                    subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_SERVICE_BREAKFAST;
                } else if (diningService.equalsIgnoreCase(DINING_SERVICE_LUNCH)) {
                    subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_SERVICE_LUNCH;
                } else if (diningService.equalsIgnoreCase(DINING_SERVICE_DINNER)) {
                    subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_SERVICE_DINNER;
                } else if (diningService.equalsIgnoreCase(DINING_SERVICE_CHARACTER_DINING)) {
                    subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_SERVICE_CHARACTER_DINING;
                } else if (diningService.equalsIgnoreCase(DINING_SERVICE_SNACK)) {
                    subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_SERVICE_SNACK;
                }
            }
        }

        if (hasCocaColaFreestyleMachine)  {
            subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINNING_COCA_COLA_FREESTYLE;
        }

        return super.getSubTypeFlags() | subTypeFlags;
    }

    @Override
    public long getOptionFlags() {
        long optionFlags = 0l;
        if (diningPlanAccepted != null && diningPlanAccepted.booleanValue()) {
            optionFlags |= PointsOfInterestTable.VAL_OPTION_FLAG_DINING_UNIVERSAL_DINING_PLAN;
        }
        if (quickServeParticipant != null && quickServeParticipant.booleanValue()) {
            optionFlags |= PointsOfInterestTable.VAL_OPTION_FLAG_DINING_UNIVERSAL_DINING_PLAN_QUICK_SERVICE;
        }
        if (hasHealthyOptions != null && hasHealthyOptions.booleanValue()) {
            optionFlags |= PointsOfInterestTable.VAL_OPTION_FLAG_DINING_VEGETARIAN_HEALTHY_OPTIONS;
        }

        return super.getOptionFlags() | optionFlags;
    }

    @Override
    public String getOpenTime() {
        return getGeneralOpenTime();
    }

    @Override
    public String getCloseTime() {
        return getGeneralCloseTime();
    }

    @Override
    public DailyHours getDailyHours() {
        return dailyHours;
    }

    @Override
    public Double getMinimumPrice() {
        return getMinPrice();
    }

    @Override
    public Double getMaximumPrice() {
        return getMaxPrice();
    }

    /**
     * @return the quickServeParticipant
     */
    public Boolean getQuickServeParticipant() {
        return quickServeParticipant;
    }

    /**
     * @return the diningPlanAccepted
     */
    public Boolean getDiningPlanAccepted() {
        return diningPlanAccepted;
    }

    /**
     * @return the diningServices
     */
    public List<String> getDiningServices() {
        return diningServices;
    }

    /**
     * @return the diningTypes
     */
    public List<String> getDiningTypes() {
        return diningTypes;
    }

    /**
     * @return the hasHealthyOptions
     */
    public Boolean getHasHealthyOptions() {
        return hasHealthyOptions;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return the generalOpenTime
     */
    public String getGeneralOpenTime() {
        return generalOpenTime;
    }

    /**
     * @return the generalCloseTime
     */
    public String getGeneralCloseTime() {
        return generalCloseTime;
    }

    /**
     * @return the menuUrl
     */
    public String getMenuUrl() {
        return menuUrl;
    }

    /**
     * @return the minPrice
     */
    public Double getMinPrice() {
        return minPrice;
    }

    /**
     * @return the maxPrice
     */
    public Double getMaxPrice() {
        return maxPrice;
    }

    /**
     * @return the areReservationsSuggested
     */
    public Boolean getAreReservationsSuggested() {
        return areReservationsSuggested;
    }

    /**
     * @return the menuImages
     */
    public List<String> getMenuImages() {
        return menuImages;
    }

    /**
     * @return the hasCocaColaFreestyleMachine
     */
    public Boolean getHasCocaColaFreestyleMachine() {
        return hasCocaColaFreestyleMachine;
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
                + ((areReservationsSuggested == null) ? 0 : areReservationsSuggested.hashCode());
        result = prime * result + ((dailyHours == null) ? 0 : dailyHours.hashCode());
        result = prime * result + ((diningPlanAccepted == null) ? 0 : diningPlanAccepted.hashCode());
        result = prime * result + ((diningServices == null) ? 0 : diningServices.hashCode());
        result = prime * result + ((diningTypes == null) ? 0 : diningTypes.hashCode());
        result = prime * result + ((generalCloseTime == null) ? 0 : generalCloseTime.hashCode());
        result = prime * result + ((generalOpenTime == null) ? 0 : generalOpenTime.hashCode());
        result = prime * result + ((hasHealthyOptions == null) ? 0 : hasHealthyOptions.hashCode());
        result = prime * result + ((maxPrice == null) ? 0 : maxPrice.hashCode());
        result = prime * result + ((menuImages == null) ? 0 : menuImages.hashCode());
        result = prime * result + ((menuUrl == null) ? 0 : menuUrl.hashCode());
        result = prime * result + ((minPrice == null) ? 0 : minPrice.hashCode());
        result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
        result = prime * result + ((quickServeParticipant == null) ? 0 : quickServeParticipant.hashCode());
        result = prime * result + ((hasCocaColaFreestyleMachine == null) ? 0 : hasCocaColaFreestyleMachine.hashCode());
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
        Dining other = (Dining) obj;
        if (areReservationsSuggested == null) {
            if (other.areReservationsSuggested != null) {
                return false;
            }
        } else if (!areReservationsSuggested.equals(other.areReservationsSuggested)) {
            return false;
        }
        if (dailyHours == null) {
            if (other.dailyHours != null) {
                return false;
            }
        } else if (!dailyHours.equals(other.dailyHours)) {
            return false;
        }
        if (diningPlanAccepted == null) {
            if (other.diningPlanAccepted != null) {
                return false;
            }
        } else if (!diningPlanAccepted.equals(other.diningPlanAccepted)) {
            return false;
        }
        if (diningServices == null) {
            if (other.diningServices != null) {
                return false;
            }
        } else if (!diningServices.equals(other.diningServices)) {
            return false;
        }
        if (diningTypes == null) {
            if (other.diningTypes != null) {
                return false;
            }
        } else if (!diningTypes.equals(other.diningTypes)) {
            return false;
        }
        if (generalCloseTime == null) {
            if (other.generalCloseTime != null) {
                return false;
            }
        } else if (!generalCloseTime.equals(other.generalCloseTime)) {
            return false;
        }
        if (generalOpenTime == null) {
            if (other.generalOpenTime != null) {
                return false;
            }
        } else if (!generalOpenTime.equals(other.generalOpenTime)) {
            return false;
        }
        if (hasHealthyOptions == null) {
            if (other.hasHealthyOptions != null) {
                return false;
            }
        } else if (!hasHealthyOptions.equals(other.hasHealthyOptions)) {
            return false;
        }
        if (maxPrice == null) {
            if (other.maxPrice != null) {
                return false;
            }
        } else if (!maxPrice.equals(other.maxPrice)) {
            return false;
        }
        if (menuImages == null) {
            if (other.menuImages != null) {
                return false;
            }
        } else if (!menuImages.equals(other.menuImages)) {
            return false;
        }
        if (menuUrl == null) {
            if (other.menuUrl != null) {
                return false;
            }
        } else if (!menuUrl.equals(other.menuUrl)) {
            return false;
        }
        if (minPrice == null) {
            if (other.minPrice != null) {
                return false;
            }
        } else if (!minPrice.equals(other.minPrice)) {
            return false;
        }
        if (phoneNumber == null) {
            if (other.phoneNumber != null) {
                return false;
            }
        } else if (!phoneNumber.equals(other.phoneNumber)) {
            return false;
        }
        if (quickServeParticipant == null) {
            if (other.quickServeParticipant != null) {
                return false;
            }
        } else if (!quickServeParticipant.equals(other.quickServeParticipant)) {
            return false;
        }
        if (hasCocaColaFreestyleMachine == null) {
            if (other.hasCocaColaFreestyleMachine != null) {
                return false;
            }
        } else if (!hasCocaColaFreestyleMachine.equals(other.hasCocaColaFreestyleMachine)) {
            return false;
        }
        return true;
    }

}
