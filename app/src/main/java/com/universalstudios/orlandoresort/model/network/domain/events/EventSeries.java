/**
 * 
 */
package com.universalstudios.orlandoresort.model.network.domain.events;

import android.util.Log;

import com.crittercism.app.Crittercism;
import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Event;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.EventDate;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author acampbell
 *
 */
@Parcel
public class EventSeries extends GsonObject {

    public static final int FLAG_SHOW_IN_NAV = 2;

    private static final String TAG = EventSeries.class.getSimpleName();

    @SerializedName("MblDisplayName")
    String displayName;

    @SerializedName("MblShortDescription")
    String shortDescription;

    @SerializedName("Dates")
    List<EventDate> eventDates;

    @SerializedName("DatePlaceHolder")
    String datePlaceholder;

    @SerializedName("Events")
    List<Event> events;

    @SerializedName("Attractions")
    List<PointOfInterest> attractions;

    @SerializedName("RequiresAnnualPass")
    Boolean requiresAnualPass;

    @SerializedName("RequiresSeparateTicket")
    Boolean requireSeparateTicket;

    @SerializedName("Disclaimer")
    String disclaimer;

    @SerializedName("BuyNowUrl")
    String buyNowUrl;

    @SerializedName("EventListDisplayName")
    String eventListDisplayName;

    @SerializedName("AttractionListDisplayName")
    String attractionListDisplayName;

    @SerializedName("VenueId")
    Long venueId;

    @SerializedName("DetailImages")
    List<String> detailImageUrls;

    @SerializedName("ListImage")
    String listImageUrl;

    @SerializedName("ThumbnailImage")
    String thumbnailImageUrl;

    @SerializedName("Url")
    String urlEndpoint;

    @SerializedName("Id")
    Long id;

    @SerializedName("DisplayInNavigation")
    boolean displayInNavigation;

    @SerializedName("NavigationIcon")
    String navigationIconLink;

    @SerializedName("HeroImage")
    String heroImageUrl;

    @SerializedName("TicketedEventDetails")
    String ticketedEventDetails;
    
    @SerializedName("HideStickyHeader")
    Boolean hideStickyHeader;

    @SerializedName("PhotoFrameExperiences")
    List<Long> photoFrameExperienceIds;

    @SerializedName("MapTileSetId")
    Long mapTilesId;

    // FIELD NOT RETURNED FROM SERVICE, ONLY USED FOR TIMELINE SORTING
    int sortOrder;

    public static boolean areEventSeriesEqual(EventSeries eventSeries, String eventSeriesJson) {
        try {
            EventSeries eventSeriesFromJson = GsonObject.fromJson(eventSeriesJson, EventSeries.class);
            return eventSeries.equals(eventSeriesFromJson);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "areEventSeriesEqual: exception trying to compare Event series", e);
            }

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        }
        return false;
    }

    public List<Long> getPhotoFrameExperienceIds() {
        return photoFrameExperienceIds;
    }

    public Long getCustomMapTileId() {
        return mapTilesId;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return the shortDescription
     */
    public String getShortDescription() {
        return shortDescription;
    }

    public boolean isDisplayInNavigation() {
        return displayInNavigation;
    }

    public String getNavigationIconLink() {
        return navigationIconLink;
    }

    /**
     * @return the eventDates
     */
    public List<EventDate> getEventDates() {
        return eventDates;
    }

    /**
     * @return the datePlaceholder
     */
    public String getDatePlaceholder() {
        return datePlaceholder;
    }

    /**
     * @return the events
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * @return the attractions
     */
    public List<PointOfInterest> getAttractions() {
        return attractions;
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
     * @return true if this should be displayed in navigation
     */
    private boolean getDisplayInNavigation() {
        return displayInNavigation;
    }

    /**
     * @return The navigation icon if application may return null
     */
    private String getNavigationIcon() {
        return navigationIconLink;
    }

    /**
     * @return the disclaimer
     */
    public String getDisclaimer() {
        return disclaimer;
    }

    /**
     * @return the buyNowUrl
     */
    public String getBuyNowUrl() {
        return buyNowUrl;
    }

    /**
     * @return the eventListDisplayName
     */
    public String getEventListDisplayName() {
        return eventListDisplayName;
    }

    /**
     * @return the attractionListDisplayName
     */
    public String getAttractionListDisplayName() {
        return attractionListDisplayName;
    }

    /**
     * @return the venueId
     */
    public Long getVenueId() {
        return venueId;
    }

    /**
     * @return the detailImageUrls
     */
    public List<String> getDetailImageUrls() {
        return detailImageUrls;
    }

    /**
     * @return the listImageUrl
     */
    public String getListImageUrl() {
        return listImageUrl;
    }

    /**
     * @return the thumbnailImageUrl
     */
    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    /**
     * @return the urlEndpoint
     */
    public String getUrlEndpoint() {
        return urlEndpoint;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the heroImageUrl
     */
    public String getHeroImageUrl() {
        return heroImageUrl;
    }

    /**
     * @return the ticketedEventDetails
     */
    public String getTicketedEventDetails() {
        return ticketedEventDetails;
    }
    
    /**
     * @return the hasStickyHeader
     */
    public Boolean getHideStickyHeader() {
      return hideStickyHeader;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
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
        result = prime * result
                + ((attractionListDisplayName == null) ? 0 : attractionListDisplayName.hashCode());
        result = prime * result + ((attractions == null) ? 0 : attractions.hashCode());
        result = prime * result + ((buyNowUrl == null) ? 0 : buyNowUrl.hashCode());
        result = prime * result + ((datePlaceholder == null) ? 0 : datePlaceholder.hashCode());
        result = prime * result + ((detailImageUrls == null) ? 0 : detailImageUrls.hashCode());
        result = prime * result + ((disclaimer == null) ? 0 : disclaimer.hashCode());
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        result = prime * result + ((eventDates == null) ? 0 : eventDates.hashCode());
        result = prime * result + ((eventListDisplayName == null) ? 0 : eventListDisplayName.hashCode());
        result = prime * result + ((events == null) ? 0 : events.hashCode());
        result = prime * result + ((heroImageUrl == null) ? 0 : heroImageUrl.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((listImageUrl == null) ? 0 : listImageUrl.hashCode());
        result = prime * result + ((requireSeparateTicket == null) ? 0 : requireSeparateTicket.hashCode());
        result = prime * result + ((requiresAnualPass == null) ? 0 : requiresAnualPass.hashCode());
        result = prime * result + ((shortDescription == null) ? 0 : shortDescription.hashCode());
        result = prime * result + sortOrder;
        result = prime * result + ((thumbnailImageUrl == null) ? 0 : thumbnailImageUrl.hashCode());
        result = prime * result + ((ticketedEventDetails == null) ? 0 : ticketedEventDetails.hashCode());
        result = prime * result + ((urlEndpoint == null) ? 0 : urlEndpoint.hashCode());
        result = prime * result + ((venueId == null) ? 0 : venueId.hashCode());
        result = prime * result + ((hideStickyHeader == null) ? 0 : hideStickyHeader.hashCode());
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
        EventSeries other = (EventSeries) obj;
        if (attractionListDisplayName == null) {
            if (other.attractionListDisplayName != null) {
                return false;
            }
        } else if (!attractionListDisplayName.equals(other.attractionListDisplayName)) {
            return false;
        }
        if (attractions == null) {
            if (other.attractions != null) {
                return false;
            }
        } else if (!attractions.equals(other.attractions)) {
            return false;
        }
        if (buyNowUrl == null) {
            if (other.buyNowUrl != null) {
                return false;
            }
        } else if (!buyNowUrl.equals(other.buyNowUrl)) {
            return false;
        }
        if (datePlaceholder == null) {
            if (other.datePlaceholder != null) {
                return false;
            }
        } else if (!datePlaceholder.equals(other.datePlaceholder)) {
            return false;
        }
        if (detailImageUrls == null) {
            if (other.detailImageUrls != null) {
                return false;
            }
        } else if (!detailImageUrls.equals(other.detailImageUrls)) {
            return false;
        }
        if (disclaimer == null) {
            if (other.disclaimer != null) {
                return false;
            }
        } else if (!disclaimer.equals(other.disclaimer)) {
            return false;
        }
        if (displayName == null) {
            if (other.displayName != null) {
                return false;
            }
        } else if (!displayName.equals(other.displayName)) {
            return false;
        }
        if (eventDates == null) {
            if (other.eventDates != null) {
                return false;
            }
        } else if (!eventDates.equals(other.eventDates)) {
            return false;
        }
        if (eventListDisplayName == null) {
            if (other.eventListDisplayName != null) {
                return false;
            }
        } else if (!eventListDisplayName.equals(other.eventListDisplayName)) {
            return false;
        }
        if (events == null) {
            if (other.events != null) {
                return false;
            }
        } else if (!events.equals(other.events)) {
            return false;
        }
        if (heroImageUrl == null) {
            if (other.heroImageUrl != null) {
                return false;
            }
        } else if (!heroImageUrl.equals(other.heroImageUrl)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (listImageUrl == null) {
            if (other.listImageUrl != null) {
                return false;
            }
        } else if (!listImageUrl.equals(other.listImageUrl)) {
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
        if (shortDescription == null) {
            if (other.shortDescription != null) {
                return false;
            }
        } else if (!shortDescription.equals(other.shortDescription)) {
            return false;
        }
        if (sortOrder != other.sortOrder) {
            return false;
        }
        if (thumbnailImageUrl == null) {
            if (other.thumbnailImageUrl != null) {
                return false;
            }
        } else if (!thumbnailImageUrl.equals(other.thumbnailImageUrl)) {
            return false;
        }
        if (urlEndpoint == null) {
            if (other.urlEndpoint != null) {
                return false;
            }
        } else if (!urlEndpoint.equals(other.urlEndpoint)) {
            return false;
        }
        if (venueId == null) {
            if (other.venueId != null) {
                return false;
            }
        } else if (!venueId.equals(other.venueId)) {
            return false;
        }
        if (ticketedEventDetails == null) {
            if (other.ticketedEventDetails != null) {
                return false;
            }
        } else if (!ticketedEventDetails.equals(other.ticketedEventDetails)) {
            return false;
        }
        if (hideStickyHeader == null) {
            if (other.hideStickyHeader != null) {
                return false;
            }
        } else if (!hideStickyHeader.equals(other.hideStickyHeader)) {
            return false;
        }
        return true;
    }

}
