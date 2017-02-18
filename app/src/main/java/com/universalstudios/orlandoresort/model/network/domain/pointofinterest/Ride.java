package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author Steven Byle
 */
@Parcel
public class Ride extends PointOfInterest implements AccessibilityOptionsProvider {

	// Ride type values
	public static final String RIDE_TYPE_THRILL = "Thrill";
	public static final String RIDE_TYPE_VIDEO_3D_4D = "Video3D4D";
	public static final String RIDE_TYPE_WATER = "Water";
	public static final String RIDE_TYPE_KID_FRIENDLY = "KidFriendly";
	public static final String RIDE_SECONDARY_TYPE_WATER_SUPER_THRILL = "WaterSuperThrill";
	public static final String RIDE_SECONDARY_TYPE_WATER_GROUP_THRILL = "WaterGroupThrill";
	public static final String RIDE_SECONDARY_TYPE_WATER_FAMILY_THRILL = "WaterFamilyFun";
	public static final String RIDE_SECONDARY_TYPE_WATER_EXTRAS = "WaterExtras";

	// Ride wait time special status values
	public static final int RIDE_WAIT_TIME_STATUS_OUT_OF_OPERATING_HOURS = -1;
	public static final int RIDE_WAIT_TIME_STATUS_CLOSED_TEMP = -2;
	public static final int RIDE_WAIT_TIME_STATUS_CLOSED_LONG_TERM = -3;
	public static final int RIDE_WAIT_TIME_STATUS_CLOSED_INCLEMENT_WEATHER = -4;
	public static final int RIDE_WAIT_TIME_STATUS_CLOSED_FOR_CAPACITY = -5;
	public static final int RIDE_WAIT_TIME_STATUS_CLOSED_INSIDE_OF_OPERATING_HOURS = -6;
	public static final int RIDE_WAIT_TIME_STATUS_NOT_AVAILABLE = -50;

	@SerializedName("MinHeightInInches")
	Integer minHeightInInches;

	@SerializedName("MaxHeightInInches")
	Integer maxHeightInInches;

	@SerializedName("ExpressPassAccepted")
	Boolean expressPassAccepted;

	@SerializedName("WaitTime")
	Integer waitTime;

	@SerializedName("TopSpeedInMph")
	Integer topSpeedInMph;

	@SerializedName("PeakHeightInFeet")
	Integer peakHeightInFeet;

	@SerializedName("AdaDescription")
	String adaDescription;

	@SerializedName("MinRecommendedAge")
	Integer minRecommendedAge;

	@SerializedName("MaxRecommendedAge")
	Integer maxRecommendedAge;

	@SerializedName("HasSingleRiderLine")
	Boolean hasSingleRiderLine;

	@SerializedName("HasChildSwap")
	Boolean hasChildSwap;

	@SerializedName("AccessibilityOptions")
	List<String> accessibilityOptions;

	@SerializedName("RideTypes")
	List<String> rideTypes;

	@SerializedName("FunFact")
	String funFact;

	@SerializedName("OpensAt")
	String opensAt;
	
	@SerializedName("HasNominalFee")
	Boolean hasNominalFee;
	
    /**
     * Returns true if rideTypes contains the passed secondary sub-type, false otherwise.
     * 
     * @param secondarySubType
     * @return
     */
    public boolean hasSecondarySubType(String secondarySubType) {
        if (rideTypes != null && rideTypes.size() > 1 && rideTypes.contains(secondarySubType)) {
            return true;
        }

        return false;
    }

	@Override
	public long getSubTypeFlags() {
		long subTypeFlags = 0l;

		if (rideTypes != null) {
			for (String rideType : rideTypes) {
				if (rideType.equalsIgnoreCase(RIDE_TYPE_THRILL)) {
					subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_THRILL;
				}
				else if (rideType.equalsIgnoreCase(RIDE_TYPE_KID_FRIENDLY)) {
					subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_KID_FRIENDLY;
				}
				else if (rideType.equalsIgnoreCase(RIDE_TYPE_VIDEO_3D_4D)) {
					subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_VIDEO_3D_4D;
				}
				else if (rideType.equalsIgnoreCase(RIDE_TYPE_WATER)) {
					subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_WATER;
				} 
				else if (rideType.equalsIgnoreCase(RIDE_SECONDARY_TYPE_WATER_SUPER_THRILL)) {
				    subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_WATER_SUPER_THRILL;
				} 
				else if (rideType.equalsIgnoreCase(RIDE_SECONDARY_TYPE_WATER_GROUP_THRILL)) {
				    subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_WATER_GROUP_THRILL;
				}
				else if (rideType.equalsIgnoreCase(RIDE_SECONDARY_TYPE_WATER_FAMILY_THRILL)) {
                    subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_WATER_FAMILY_THRILL;
                }
				else if (rideType.equalsIgnoreCase(RIDE_SECONDARY_TYPE_WATER_EXTRAS)) {
                    subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_WATER_EXTRAS;
                }
			}
		}
		return super.getSubTypeFlags() | subTypeFlags;
	}
	
	@Override
	public long getOptionFlags() {
	    long optionFlags = 0l;
	    
	    if(accessibilityOptions != null) {
	        for(String option : accessibilityOptions) {
	            if(option.equalsIgnoreCase(ACCESSIBILITY_OPTION_ASSISTIVE_LISTENING)) {
	                optionFlags |= PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_ASSISTIVE_LISTENING;
	            } else if(option.equalsIgnoreCase(ACCESSIBILITY_OPTION_STANDARD_WHEELCHAIR)) {
                    optionFlags |= PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_STANDARD_WHEELCHAIR;
                } else if(option.equalsIgnoreCase(ACCESSIBILITY_OPTION_CLOSED_CAPTION)) {
                    optionFlags |= PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_CLOSED_CAPTION;
                } else if(option.equalsIgnoreCase(ACCESSIBILITY_OPTION_SIGN_LANGUAGE)) {
                    optionFlags |= PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_SIGN_LANGUAGE;
                } else if(option.equalsIgnoreCase(ACCESSIBILITY_OPTION_PARENTAL_DISCRETION_ADVISED)) {
                    optionFlags |= PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_PARENTAL_DISCRETION_ADVISED;
                } else if(option.equalsIgnoreCase(ACCESSIBILITY_OPTION_WHEELCHAIR_MUST_TRANSFER)) {
                    optionFlags |= PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_WHEELCHAIR_MUST_TRANSFER;
                } else if(option.equalsIgnoreCase(ACCESSIBILITY_OPTION_ANY_WHEELCHAIR)) {
                    optionFlags |= PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_ANY_WHEELCHAIR;
                } else if(option.equalsIgnoreCase(ACCESSIBILITY_OPTION_STATIONARY_SEATING)) {
                    optionFlags |= PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_STATIONARY_SEATING;
                } else if(option.equalsIgnoreCase(ACCESSIBILITY_OPTION_EXTRA_INFO)) {
                    optionFlags |= PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_EXTRA_INFO;
                } else if(option.equalsIgnoreCase(ACCESSIBILITY_OPTION_NO_UNATTENDED_CHILDREN)) {
                    optionFlags |= PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_NO_UNATTENDED_CHILDREN;
                } else if(option.equalsIgnoreCase(ACCESSIBILITY_OPTION_LIFE_VEST_REQUIRED)) {
                    optionFlags |= PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_LIFE_VEST_REQUIRED;
                }
	        }
	    }
	    if(expressPassAccepted != null && expressPassAccepted.booleanValue()) {
	        optionFlags |= PointsOfInterestTable.VAL_OPTION_FLAG_EXPRESS_PASS_ACCEPTED;
	    }
	    if(hasSingleRiderLine != null && hasSingleRiderLine.booleanValue()) {
	        optionFlags |= PointsOfInterestTable.VAL_OPTION_FLAG_RIDE_SINGLE_RIDER_LINE;
	    }
	    if(hasChildSwap != null && hasChildSwap.booleanValue()) {
	        optionFlags |= PointsOfInterestTable.VAL_OPTION_FLAG_RIDE_CHILD_SWAP;
	    }
	    
	    return super.getOptionFlags() | optionFlags;
	}

	@Override
	public boolean hasAccessibilityOption(String accessibilityOption) {

		if (accessibilityOptions == null || accessibilityOption == null) {
			return false;
		}
		return accessibilityOptions.contains(accessibilityOption);
	}

	/**
	 * @return the hasChildSwap
	 */
	public Boolean getHasChildSwap() {
		return hasChildSwap;
	}

	/**
	 * @return the funFact
	 */
	public String getFunFact() {
		return funFact;
	}

	/**
	 * @return the minHeightInInches
	 */
	public Integer getMinHeightInInches() {
		return minHeightInInches;
	}

	/**
	 * @return the maxHeightInInches
	 */
	public Integer getMaxHeightInInches() {
		return maxHeightInInches;
	}

	/**
	 * @return the expressPassAccepted
	 */
	public Boolean getExpressPassAccepted() {
		return expressPassAccepted;
	}

	/**
	 * @return the waitTime
	 */
	public Integer getWaitTime() {
		return waitTime;
	}

	/**
	 * @return the topSpeedInMph
	 */
	public Integer getTopSpeedInMph() {
		return topSpeedInMph;
	}

	/**
	 * @return the peakHeightInFeet
	 */
	public Integer getPeakHeightInFeet() {
		return peakHeightInFeet;
	}

	/**
	 * @return the adaDescription
	 */
	public String getAdaDescription() {
		return adaDescription;
	}

	/**
	 * @return the minRecommendedAge
	 */
	public Integer getMinRecommendedAge() {
		return minRecommendedAge;
	}

	/**
	 * @return the maxRecommendedAge
	 */
	public Integer getMaxRecommendedAge() {
		return maxRecommendedAge;
	}

	/**
	 * @return the hasSingleRiderLine
	 */
	public Boolean getHasSingleRiderLine() {
		return hasSingleRiderLine;
	}

	/**
	 * @return the rideTypes
	 */
	public List<String> getRideTypes() {
		return rideTypes;
	}

	/**
	 * @return the accessibilityOptions
	 */
	public List<String> getAccessibilityOptions() {
		return accessibilityOptions;
	}

	/**
	 * @return the opensAt
	 */
	public String getOpensAt() {
		return opensAt;
	}
	
    /**
     * @return the hasNominalFee
     */
    public Boolean getHasNominalFee() {
        return hasNominalFee;
    }

	/* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((accessibilityOptions == null) ? 0 : accessibilityOptions.hashCode());
        result = prime * result + ((adaDescription == null) ? 0 : adaDescription.hashCode());
        result = prime * result + ((expressPassAccepted == null) ? 0 : expressPassAccepted.hashCode());
        result = prime * result + ((funFact == null) ? 0 : funFact.hashCode());
        result = prime * result + ((hasChildSwap == null) ? 0 : hasChildSwap.hashCode());
        result = prime * result + ((hasNominalFee == null) ? 0 : hasNominalFee.hashCode());
        result = prime * result + ((hasSingleRiderLine == null) ? 0 : hasSingleRiderLine.hashCode());
        result = prime * result + ((maxHeightInInches == null) ? 0 : maxHeightInInches.hashCode());
        result = prime * result + ((maxRecommendedAge == null) ? 0 : maxRecommendedAge.hashCode());
        result = prime * result + ((minHeightInInches == null) ? 0 : minHeightInInches.hashCode());
        result = prime * result + ((minRecommendedAge == null) ? 0 : minRecommendedAge.hashCode());
        result = prime * result + ((opensAt == null) ? 0 : opensAt.hashCode());
        result = prime * result + ((peakHeightInFeet == null) ? 0 : peakHeightInFeet.hashCode());
        result = prime * result + ((rideTypes == null) ? 0 : rideTypes.hashCode());
        result = prime * result + ((topSpeedInMph == null) ? 0 : topSpeedInMph.hashCode());
        result = prime * result + ((waitTime == null) ? 0 : waitTime.hashCode());
        return result;
    }

	/* (non-Javadoc)
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
        Ride other = (Ride) obj;
        if (accessibilityOptions == null) {
            if (other.accessibilityOptions != null) {
                return false;
            }
        } else if (!accessibilityOptions.equals(other.accessibilityOptions)) {
            return false;
        }
        if (adaDescription == null) {
            if (other.adaDescription != null) {
                return false;
            }
        } else if (!adaDescription.equals(other.adaDescription)) {
            return false;
        }
        if (expressPassAccepted == null) {
            if (other.expressPassAccepted != null) {
                return false;
            }
        } else if (!expressPassAccepted.equals(other.expressPassAccepted)) {
            return false;
        }
        if (funFact == null) {
            if (other.funFact != null) {
                return false;
            }
        } else if (!funFact.equals(other.funFact)) {
            return false;
        }
        if (hasChildSwap == null) {
            if (other.hasChildSwap != null) {
                return false;
            }
        } else if (!hasChildSwap.equals(other.hasChildSwap)) {
            return false;
        }
        if (hasNominalFee == null) {
            if (other.hasNominalFee != null) {
                return false;
            }
        } else if (!hasNominalFee.equals(other.hasNominalFee)) {
            return false;
        }
        if (hasSingleRiderLine == null) {
            if (other.hasSingleRiderLine != null) {
                return false;
            }
        } else if (!hasSingleRiderLine.equals(other.hasSingleRiderLine)) {
            return false;
        }
        if (maxHeightInInches == null) {
            if (other.maxHeightInInches != null) {
                return false;
            }
        } else if (!maxHeightInInches.equals(other.maxHeightInInches)) {
            return false;
        }
        if (maxRecommendedAge == null) {
            if (other.maxRecommendedAge != null) {
                return false;
            }
        } else if (!maxRecommendedAge.equals(other.maxRecommendedAge)) {
            return false;
        }
        if (minHeightInInches == null) {
            if (other.minHeightInInches != null) {
                return false;
            }
        } else if (!minHeightInInches.equals(other.minHeightInInches)) {
            return false;
        }
        if (minRecommendedAge == null) {
            if (other.minRecommendedAge != null) {
                return false;
            }
        } else if (!minRecommendedAge.equals(other.minRecommendedAge)) {
            return false;
        }
        if (opensAt == null) {
            if (other.opensAt != null) {
                return false;
            }
        } else if (!opensAt.equals(other.opensAt)) {
            return false;
        }
        if (peakHeightInFeet == null) {
            if (other.peakHeightInFeet != null) {
                return false;
            }
        } else if (!peakHeightInFeet.equals(other.peakHeightInFeet)) {
            return false;
        }
        if (rideTypes == null) {
            if (other.rideTypes != null) {
                return false;
            }
        } else if (!rideTypes.equals(other.rideTypes)) {
            return false;
        }
        if (topSpeedInMph == null) {
            if (other.topSpeedInMph != null) {
                return false;
            }
        } else if (!topSpeedInMph.equals(other.topSpeedInMph)) {
            return false;
        }
        if (waitTime == null) {
            if (other.waitTime != null) {
                return false;
            }
        } else if (!waitTime.equals(other.waitTime)) {
            return false;
        }
        return true;
    }
}
