package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.domain.photoframes.PhotoFrame;
import com.universalstudios.orlandoresort.model.network.domain.photoframes.PhotoFrameExperience;

import org.parceler.Parcel;

import java.util.List;

/**
 * 
 * @author acampbell
 *
 */
@Parcel
public class Event extends PointOfInterest {

    public static final int FLAG_SHOW_IN_TODAY_VIEW = 2;

    @SerializedName("Dates")
    List<EventDate> eventDates;

    @SerializedName("RequiresAnnualPass")
    Boolean requiresAnualPass;

    @SerializedName("RequiresSeparateTicket")
    Boolean requireSeparateTicket;

    @SerializedName("Location")
    String location;

    @SerializedName("Activities")
    List<EventActivity> eventActivities;

    @SerializedName("BuyNowUrl")
    String buyNowUrl;

    @SerializedName("EventSeriesIds")
    List<Long> eventSeriesIds;

    @SerializedName("WaitTime")
    Integer waitTime;

    @SerializedName("DisplayInEventList")
    Boolean displayInEventList;

    @SerializedName("TicketedEventDetails")
    String ticketedEventDetails;

    @SerializedName("TBD")//TODO:
    List<PhotoFrameExperience> photoFrameExperiences;

    // ************ NOT PARSED, ADDED FIELDS ************
    // USED BY EVENT MAP
    List<Long> groupedEventIds;


    /**
     * @return the eventDates
     */
    public List<EventDate> getEventDates() {
        return eventDates;
    }

    /**
     * @return the eventSeriesIds
     */
    public List<Long> getEventSeriesIds() {
        return eventSeriesIds;
    }

    /**
     * @return the requiresAnualPass
     */
    public Boolean getRequiresAnualPass() {
        return requiresAnualPass;
    }

    /**
     * @return the requireSeparateTicket
     */
    public Boolean getRequireSeparateTicket() {
        return requireSeparateTicket;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return true if should show in display list
     */
    public boolean shouldShowInDisplayList() {
        return displayInEventList;
    }

    /**
     * @return the eventActivities
     */
    public List<EventActivity> getEventActivities() {
        return eventActivities;
    }

    /**
     * @return the buyNowUrl
     */
    public String getBuyNowUrl() {
        return buyNowUrl;
    }

    /**
     * @return List of {@link PhotoFrame}
     */
    public List<PhotoFrameExperience> getPhotoFrameExperiences() {
        return photoFrameExperiences;
    }

    /**
     * @return the groupedEventIds
     */
    public List<Long> getGroupedEventIds() {
        return groupedEventIds;
    }

    /**
     * @param groupedEventIds
     *            the groupedEventIds to set
     */
    public void setGroupedEventIds(List<Long> groupedEventIds) {
        this.groupedEventIds = groupedEventIds;
    }

    /**
     * @return the waitTime
     */
    public Integer getWaitTime() {
        return waitTime;
    }

    /**
     * @return the ticketedEventDetails
     */
    public String getTicketedEventDetails() {
        return ticketedEventDetails;
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
        result = prime * result + ((buyNowUrl == null) ? 0 : buyNowUrl.hashCode());
        result = prime * result + ((eventActivities == null) ? 0 : eventActivities.hashCode());
        result = prime * result + ((eventDates == null) ? 0 : eventDates.hashCode());
        result = prime * result + ((eventSeriesIds == null) ? 0 : eventSeriesIds.hashCode());
        result = prime * result + ((groupedEventIds == null) ? 0 : groupedEventIds.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((requireSeparateTicket == null) ? 0 : requireSeparateTicket.hashCode());
        result = prime * result + ((requiresAnualPass == null) ? 0 : requiresAnualPass.hashCode());
        result = prime * result + ((ticketedEventDetails == null) ? 0 : ticketedEventDetails.hashCode());
        result = prime * result + ((waitTime == null) ? 0 : waitTime.hashCode());
        result = prime * result + ((displayInEventList == null) ? 0 : displayInEventList.hashCode());
        result = prime * result + ((photoFrameExperiences == null) ? 0 : photoFrameExperiences.hashCode());
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
        Event other = (Event) obj;
        if (buyNowUrl == null) {
            if (other.buyNowUrl != null) {
                return false;
            }
        } else if (!buyNowUrl.equals(other.buyNowUrl)) {
            return false;
        }
        if (eventActivities == null) {
            if (other.eventActivities != null) {
                return false;
            }
        } else if (!eventActivities.equals(other.eventActivities)) {
            return false;
        }
        if (eventDates == null) {
            if (other.eventDates != null) {
                return false;
            }
        } else if (!eventDates.equals(other.eventDates)) {
            return false;
        }
        if (eventSeriesIds == null) {
            if (other.eventSeriesIds != null) {
                return false;
            }
        } else if (!eventSeriesIds.equals(other.eventSeriesIds)) {
            return false;
        }
        if (groupedEventIds == null) {
            if (other.groupedEventIds != null) {
                return false;
            }
        } else if (!groupedEventIds.equals(other.groupedEventIds)) {
            return false;
        }
        if (location == null) {
            if (other.location != null) {
                return false;
            }
        } else if (!location.equals(other.location)) {
            return false;
        }
        if (requireSeparateTicket == null) {
            if (other.requireSeparateTicket != null) {
                return false;
            }
        } else if (!requireSeparateTicket.equals(other.requireSeparateTicket)) {
            return false;
        }
        if (requiresAnualPass == null) {
            if (other.requiresAnualPass != null) {
                return false;
            }
        } else if (!requiresAnualPass.equals(other.requiresAnualPass)) {
            return false;
        }
        if (ticketedEventDetails == null) {
            if (other.ticketedEventDetails != null) {
                return false;
            }
        } else if (!ticketedEventDetails.equals(other.ticketedEventDetails)) {
            return false;
        }
        if (waitTime == null) {
            if (other.waitTime != null) {
                return false;
            }
        } else if (!waitTime.equals(other.waitTime)) {
            return false;
        }
        if (displayInEventList == null) {
            if (other.displayInEventList != null) {
                return false;
            }
        } else if (!displayInEventList.equals(other.displayInEventList)) {
            return false;
        }
        if (photoFrameExperiences == null) {
            if (other.photoFrameExperiences != null) {
                return false;
            }
        } else if (!photoFrameExperiences.equals(other.photoFrameExperiences)) {
            return false;
        }
        return true;
    }

}
