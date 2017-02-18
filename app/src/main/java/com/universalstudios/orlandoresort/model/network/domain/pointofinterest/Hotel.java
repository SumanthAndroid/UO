package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author Steven Byle
 */
@Parcel
public class Hotel extends PointOfInterest {

	// Hotel perk values
	public static final String HOTEL_PERK_FREE_EXPRESS_PASS = "FreeExpressPass";
	public static final String HOTEL_PERK_EARLY_ADMISSION = "EarlyParkAdmission";

	@SerializedName("HotelPerks")
	List<String> hotelPerks;

	@SerializedName("ReservationsPhoneNumber")
	String reservationsPhoneNumber;

    @SerializedName("ReservationCopy")
    String reservationCopy;
	
	@SerializedName("BenefitsInfo")
	List<BenefitsInfo> benefitsInfo;

	public boolean hasPerk(String perk) {
		if (hotelPerks == null || perk == null) {
			return false;
		}
		return hotelPerks.contains(perk);
	}

	public List<String> getHotelPerks() {
		return hotelPerks;
	}

	/**
	 * @return the reservationsPhoneNumber
	 */
	public String getReservationsPhoneNumber() {
		return reservationsPhoneNumber;
	}

    /**
     *
     * @return reservationCopy
     */
    public String getReservationCopy() {
        return reservationCopy;
    }

    /**
     * @return the benefitsInfo
     */
    public List<BenefitsInfo> getBenefitsInfo() {
        return benefitsInfo;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((benefitsInfo == null) ? 0 : benefitsInfo.hashCode());
        result = prime * result + ((hotelPerks == null) ? 0 : hotelPerks.hashCode());
        result = prime * result + ((reservationsPhoneNumber == null) ? 0 : reservationsPhoneNumber.hashCode());
        result = prime * result + ((reservationCopy == null) ? 0 : reservationCopy.hashCode());
        return result;
    }

	/* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Hotel other = (Hotel) obj;
        if (benefitsInfo == null) {
            if (other.benefitsInfo != null)
                return false;
        }
        else if (!benefitsInfo.equals(other.benefitsInfo))
            return false;
        if (hotelPerks == null) {
            if (other.hotelPerks != null)
                return false;
        }
        else if (!hotelPerks.equals(other.hotelPerks))
            return false;
        if (reservationsPhoneNumber == null) {
            if (other.reservationsPhoneNumber != null)
                return false;
        }
        else if (!reservationsPhoneNumber.equals(other.reservationsPhoneNumber))
            return false;
        if (reservationCopy == null) {
            if (other.reservationCopy != null)
                return false;
        }
        else if (!reservationCopy.equals(other.reservationCopy))
            return false;
        return true;
    }

}
