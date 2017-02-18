package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * 
 * @author acampbell
 *
 */
@Parcel
public class EventActivity extends GsonObject {

    @SerializedName("MblDisplayName")
    String displayName;

    @SerializedName("MblShortDescription")
    String shortDescription;

    @SerializedName("ThumbnailImage")
    String thumbnailImageUrl;

    @SerializedName("Url")
    String urlEndpoint;

    @SerializedName("Id")
    Long id;

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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((shortDescription == null) ? 0 : shortDescription.hashCode());
        result = prime * result + ((thumbnailImageUrl == null) ? 0 : thumbnailImageUrl.hashCode());
        result = prime * result + ((urlEndpoint == null) ? 0 : urlEndpoint.hashCode());
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
        EventActivity other = (EventActivity) obj;
        if (displayName == null) {
            if (other.displayName != null) {
                return false;
            }
        } else if (!displayName.equals(other.displayName)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (shortDescription == null) {
            if (other.shortDescription != null) {
                return false;
            }
        } else if (!shortDescription.equals(other.shortDescription)) {
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
        return true;
    }

}
