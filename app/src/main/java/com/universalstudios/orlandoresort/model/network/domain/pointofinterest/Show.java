package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;

import org.parceler.Parcel;

import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
@Parcel
public class Show extends PointOfInterest implements AccessibilityOptionsProvider {

	// Show type values
	public static final String SHOW_TYPE_PARADE = "Parade";
	public static final String SHOW_TYPE_ACTION = "Action";
	public static final String SHOW_TYPE_MUSIC = "Music";
	public static final String SHOW_TYPE_COMEDY = "Comedy";
	public static final String SHOW_TYPE_CHARACTER = "Character";

	@SerializedName("StartTimes")
	List<String> startTimes;

	@SerializedName("ExpressPassAccepted")
	Boolean expressPassAccepted;

	@SerializedName("ShowTypes")
	List<String> showTypes;

	@SerializedName("AccessibilityOptions")
	List<String> accessibilityOptions;

	@SerializedName("ReservationsPhoneNumber")
	String reservationsPhoneNumber;

	@SerializedName("WaitTime")
	Integer waitTime;

	@SerializedName("EndTimes")
	List<String> endTimes;

	@Override
	public long getSubTypeFlags() {
		long subTypeFlags = 0l;

		if (showTypes != null) {
			for (String showType : showTypes) {
				if (showType.equalsIgnoreCase(SHOW_TYPE_PARADE)) {
					subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_PARADE;
				}
				else if (showType.equalsIgnoreCase(SHOW_TYPE_ACTION)) {
					subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_ACTION;
				}
				else if (showType.equalsIgnoreCase(SHOW_TYPE_MUSIC)) {
					subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_MUSIC;
				}
				else if (showType.equalsIgnoreCase(SHOW_TYPE_COMEDY)) {
					subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_COMEDY;
				}
				else if (showType.equalsIgnoreCase(SHOW_TYPE_CHARACTER)) {
					subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_CHARACTER;
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
	 * @return the startTimes
	 */
	public List<String> getStartTimes() {
		return startTimes;
	}

	/**
	 * @return the expressPassAccepted
	 */
	public Boolean getExpressPassAccepted() {
		return expressPassAccepted;
	}

	/**
	 * @return the showTypes
	 */
	public List<String> getShowTypes() {
		return showTypes;
	}

	/**
	 * @return the accessibilityOptions
	 */
	public List<String> getAccessibilityOptions() {
		return accessibilityOptions;
	}

	/**
	 * @return the reservationsPhoneNumber
	 */
	public String getReservationsPhoneNumber() {
		return reservationsPhoneNumber;
	}

	/**
	 * @return the waitTime
	 */
	public Integer getWaitTime() {
		return waitTime;
	}

	/**
	 * @return the endTimes
	 */
	public List<String> getEndTimes() {
		return endTimes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((accessibilityOptions == null) ? 0 : accessibilityOptions.hashCode());
		result = prime * result + ((endTimes == null) ? 0 : endTimes.hashCode());
		result = prime * result + ((expressPassAccepted == null) ? 0 : expressPassAccepted.hashCode());
		result = prime * result + ((reservationsPhoneNumber == null) ? 0 : reservationsPhoneNumber.hashCode());
		result = prime * result + ((showTypes == null) ? 0 : showTypes.hashCode());
		result = prime * result + ((startTimes == null) ? 0 : startTimes.hashCode());
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
		Show other = (Show) obj;
		if (accessibilityOptions == null) {
			if (other.accessibilityOptions != null) {
				return false;
			}
		}
		else if (!accessibilityOptions.equals(other.accessibilityOptions)) {
			return false;
		}
		if (endTimes == null) {
			if (other.endTimes != null) {
				return false;
			}
		}
		else if (!endTimes.equals(other.endTimes)) {
			return false;
		}
		if (expressPassAccepted == null) {
			if (other.expressPassAccepted != null) {
				return false;
			}
		}
		else if (!expressPassAccepted.equals(other.expressPassAccepted)) {
			return false;
		}
		if (reservationsPhoneNumber == null) {
			if (other.reservationsPhoneNumber != null) {
				return false;
			}
		}
		else if (!reservationsPhoneNumber.equals(other.reservationsPhoneNumber)) {
			return false;
		}
		if (showTypes == null) {
			if (other.showTypes != null) {
				return false;
			}
		}
		else if (!showTypes.equals(other.showTypes)) {
			return false;
		}
		if (startTimes == null) {
			if (other.startTimes != null) {
				return false;
			}
		}
		else if (!startTimes.equals(other.startTimes)) {
			return false;
		}
		if (waitTime == null) {
			if (other.waitTime != null) {
				return false;
			}
		}
		else if (!waitTime.equals(other.waitTime)) {
			return false;
		}
		return true;
	}

}
