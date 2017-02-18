package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author Steven Byle
 */
@Parcel
public class Entertainment extends PointOfInterest implements HoursProvider, PricesProvider {

	// Entertainment type values
	public static final String ENTERTAINMENT_TYPE_NIGHT_SPOTS = "NightSpots";
	public static final String ENTERTAINMENT_TYPE_EXPERIENCES = "Experiences";

	@SerializedName("PartyPassAccepted")
	Boolean partyPassAccepted;

	@SerializedName("MinPrice")
	Double minPrice;

	@SerializedName("MaxPrice")
	Double maxPrice;

	@SerializedName("GeneralOpenTime")
	String generalOpenTime;

	@SerializedName("GeneralCloseTime")
	String generalCloseTime;

	@SerializedName("DailyHours")
	DailyHours dailyHours;

	@SerializedName("NightlifeTypes")
	List<String> entertainmentTypes;

	@Override
	public long getSubTypeFlags() {
		long subTypeFlags = 0l;

		if (entertainmentTypes != null) {
			for (String entertainmentType : entertainmentTypes) {
				if (entertainmentType.equalsIgnoreCase(ENTERTAINMENT_TYPE_NIGHT_SPOTS)) {
					subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_ENTERTAINMENT_TYPE_NIGHT_SPOTS;
				} else if (entertainmentType.equalsIgnoreCase(ENTERTAINMENT_TYPE_EXPERIENCES)) {
					subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_ENTERTAINMENT_TYPE_EXPERIENCES;
				}
			}
		}
		return super.getSubTypeFlags() | subTypeFlags;
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
	 * @return the partyPassAccepted
	 */
	public Boolean getPartyPassAccepted() {
		return partyPassAccepted;
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

	public List<String> getEntertainmentTypes() {
		return entertainmentTypes;
	}

	/* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dailyHours == null) ? 0 : dailyHours.hashCode());
		result = prime * result + ((generalCloseTime == null) ? 0 : generalCloseTime.hashCode());
		result = prime * result + ((generalOpenTime == null) ? 0 : generalOpenTime.hashCode());
		result = prime * result + ((maxPrice == null) ? 0 : maxPrice.hashCode());
		result = prime * result + ((minPrice == null) ? 0 : minPrice.hashCode());
		result = prime * result + ((partyPassAccepted == null) ? 0 : partyPassAccepted.hashCode());
		result = prime * result + ((entertainmentTypes == null) ? 0 : entertainmentTypes.hashCode());
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
		Entertainment other = (Entertainment) obj;
		if (dailyHours == null) {
			if (other.dailyHours != null) {
				return false;
			}
		}
		else if (!dailyHours.equals(other.dailyHours)) {
			return false;
		}
		if (generalCloseTime == null) {
			if (other.generalCloseTime != null) {
				return false;
			}
		}
		else if (!generalCloseTime.equals(other.generalCloseTime)) {
			return false;
		}
		if (generalOpenTime == null) {
			if (other.generalOpenTime != null) {
				return false;
			}
		}
		else if (!generalOpenTime.equals(other.generalOpenTime)) {
			return false;
		}
		if (maxPrice == null) {
			if (other.maxPrice != null) {
				return false;
			}
		}
		else if (!maxPrice.equals(other.maxPrice)) {
			return false;
		}
		if (minPrice == null) {
			if (other.minPrice != null) {
				return false;
			}
		}
		else if (!minPrice.equals(other.minPrice)) {
			return false;
		}
		if (partyPassAccepted == null) {
			if (other.partyPassAccepted != null) {
				return false;
			}
		}
		else if (!partyPassAccepted.equals(other.partyPassAccepted)) {
			return false;
		}
		if (entertainmentTypes == null) {
			if (other.entertainmentTypes != null) {
				return false;
			}
		} else if (!entertainmentTypes.equals(other.entertainmentTypes)) {
			return false;
		}
		return true;
	}
}
