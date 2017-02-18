package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Comparator;

/**
 * 
 * @author acampbell
 *
 */
@Parcel
public class EventDate extends GsonObject {

    @SerializedName("StartDate")
    String startDate;

    @SerializedName("EndDate")
    String endDate;

    @SerializedName("StartDateUnix")
    Long startDateUnix;

    @SerializedName("EndDateUnix")
    Long endDateUnix;

    @ParcelConstructor
    public EventDate(Long startDateUnix, Long endDateUnix) {
        super();
        this.startDateUnix = startDateUnix;
        this.endDateUnix = endDateUnix;
    }

    public static class EventDateAscendingComparator implements Comparator<EventDate> {

        @Override
        public int compare(EventDate lhs, EventDate rhs) {
            if (lhs.getStartDateUnix() != null && rhs.getStartDateUnix() != null) {
                return (int) (lhs.getStartDateUnix() - rhs.getStartDateUnix());
            }
            return 0;
        }
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Long getEndDateUnix() {
        return endDateUnix;
    }

    public void setEndDateUnix(Long endDateUnix) {
        this.endDateUnix = endDateUnix;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Long getStartDateUnix() {
        return startDateUnix;
    }

    public void setStartDateUnix(Long startDateUnix) {
        this.startDateUnix = startDateUnix;
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
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + ((endDateUnix == null) ? 0 : endDateUnix.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((startDateUnix == null) ? 0 : startDateUnix.hashCode());
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
        EventDate other = (EventDate) obj;
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
        if (startDate == null) {
            if (other.startDate != null) {
                return false;
            }
        } else if (!startDate.equals(other.startDate)) {
            return false;
        }
        if (startDateUnix == null) {
            if (other.startDateUnix != null) {
                return false;
            }
        } else if (!startDateUnix.equals(other.startDateUnix)) {
            return false;
        }
        return true;
    }

}
