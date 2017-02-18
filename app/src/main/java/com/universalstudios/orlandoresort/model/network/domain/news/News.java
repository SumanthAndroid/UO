package com.universalstudios.orlandoresort.model.network.domain.news;

import com.crittercism.app.Crittercism;
import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 
 * 
 * @author Steven Byle
 */
@Parcel
public class News extends GsonObject {

	@SerializedName("CreatedDate")
    String createdDate;

	@SerializedName("LastUpdatedDate")
    String lastUpdatedDate;

	@SerializedName("StartDate")
    String startDate;

	@SerializedName("ExpirationDate")
    String expirationDate;

	@SerializedName("Heading")
    String messageHeading;

	@SerializedName("Body")
    String messageBody;

	@SerializedName("DetailImage")
    String detailImageUrl;

	@SerializedName("Url")
    String urlEndpoint;

	@SerializedName("Id")
    Long id;
	
	@SerializedName("LinkId")
    Long linkId;
	
	@SerializedName("LinkDestination")
    String linkDestination;

    @SerializedName("AndroidLink")
    String androidLink;

    @SerializedName("IsExternalLink")
    Boolean isExternalLink;

	// ************ NOT PARSED, ADDED FIELDS ************

	@SerializedName("StartDateInMillis")
    Long startDateInMillis;

	@SerializedName("ExpirationDateInMillis")
    Long expirationDateInMillis;

	@SerializedName("LastUpdatedDateInMillis")
    Long lastUpdatedDateInMillis;

	@SerializedName("CreatedDateInMillis")
    Long createdDateInMillis;

	@SerializedName("HasBeenRead")
    Boolean hasBeenRead;

	/**
	 * 
	 */
	public void setDatesInMillis() {
		SimpleDateFormat sdfInFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);

		if (startDate != null) {
			try {
				startDateInMillis = sdfInFormat.parse(startDate).getTime();
			}
			catch (ParseException e) {
				startDateInMillis = null;

				// Log the exception to crittercism
				Crittercism.logHandledException(e);
			}
		}

		if (expirationDate != null) {
			try {
				expirationDateInMillis = sdfInFormat.parse(expirationDate).getTime();
			}
			catch (ParseException e) {
				expirationDateInMillis = null;

				// Log the exception to crittercism
				Crittercism.logHandledException(e);
			}
		}

		if (lastUpdatedDate != null) {
			try {
				lastUpdatedDateInMillis = sdfInFormat.parse(lastUpdatedDate).getTime();
			}
			catch (ParseException e) {
				lastUpdatedDateInMillis = null;

				// Log the exception to crittercism
				Crittercism.logHandledException(e);
			}
		}

		if (createdDate != null) {
			try {
				createdDateInMillis = sdfInFormat.parse(createdDate).getTime();
			}
			catch (ParseException e) {
				createdDateInMillis = null;

				// Log the exception to crittercism
				Crittercism.logHandledException(e);
			}
		}
	}

	/**
	 * @param hasBeenRead the hasBeenRead to set
	 */
	public void setHasBeenRead(Boolean hasBeenRead) {
        // FIXME This variable shouldn't be set from an outside Util method
		this.hasBeenRead = hasBeenRead;
	}

	/**
	 * @return the lastUpdatedDateInMillis
	 */
	public Long getLastUpdatedDateInMillis() {
		return lastUpdatedDateInMillis;
	}

	/**
	 * @return the createdDateInMillis
	 */
	public Long getCreatedDateInMillis() {
		return createdDateInMillis;
	}

	/**
	 * @return the hasBeenRead
	 */
	public Boolean getHasBeenRead() {
		return hasBeenRead;
	}

	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @return the lastUpdatedDate
	 */
	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @return the expirationDate
	 */
	public String getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @return the messageHeading
	 */
	public String getMessageHeading() {
		return messageHeading;
	}

	/**
	 * @return the messageBody
	 */
	public String getMessageBody() {
		return messageBody;
	}

	/**
	 * @return the detailImageUrl
	 */
	public String getDetailImageUrl() {
		return detailImageUrl;
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
	 * @return the startDateInMillis
	 */
	public Long getStartDateInMillis() {
		return startDateInMillis;
	}

	/**
	 * @return the expirationDateInMillis
	 */
	public Long getExpirationDateInMillis() {
		return expirationDateInMillis;
	}

    /**
     * @return the linkId
     */
    public Long getLinkId() {
        return linkId;
    }

    /**
     * @return the linkDestination
     */
    public String getLinkDestination() {
        return linkDestination;
    }

    /**
     * @return the androidLink
     */
    public String getAndroidLink() {
        return androidLink;
    }

    /**
     * @return the isExternalLink
     */
    public Boolean getIsExternalLink() {
        return isExternalLink;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
        result = prime * result + ((createdDateInMillis == null) ? 0 : createdDateInMillis.hashCode());
        result = prime * result + ((detailImageUrl == null) ? 0 : detailImageUrl.hashCode());
        result = prime * result + ((expirationDate == null) ? 0 : expirationDate.hashCode());
        result = prime * result + ((expirationDateInMillis == null) ? 0 : expirationDateInMillis.hashCode());
        result = prime * result + ((hasBeenRead == null) ? 0 : hasBeenRead.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((lastUpdatedDate == null) ? 0 : lastUpdatedDate.hashCode());
        result = prime * result + ((lastUpdatedDateInMillis == null) ? 0 : lastUpdatedDateInMillis.hashCode());
        result = prime * result + ((linkDestination == null) ? 0 : linkDestination.hashCode());
        result = prime * result + ((linkId == null) ? 0 : linkId.hashCode());
        result = prime * result + ((messageBody == null) ? 0 : messageBody.hashCode());
        result = prime * result + ((messageHeading == null) ? 0 : messageHeading.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((startDateInMillis == null) ? 0 : startDateInMillis.hashCode());
        result = prime * result + ((urlEndpoint == null) ? 0 : urlEndpoint.hashCode());
        result = prime * result + ((isExternalLink == null) ? 0 : isExternalLink.hashCode());
        result = prime * result + ((androidLink == null) ? 0 : androidLink.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        // By default, ignore any transient state when making comparisons for
        // syncing
        return equals(obj, true);
    }

	/**
	 * @param obj
	 * @param ignoreTransientFields
	 * @return
	 */
	public boolean equals(Object obj, boolean ignoreTransientFields) {
	    if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        News other = (News) obj;
        if (createdDate == null) {
            if (other.createdDate != null) {
                return false;
            }
        } else if (!createdDate.equals(other.createdDate)) {
            return false;
        }
        if (createdDateInMillis == null) {
            if (other.createdDateInMillis != null) {
                return false;
            }
        } else if (!createdDateInMillis.equals(other.createdDateInMillis)) {
            return false;
        }
        if (detailImageUrl == null) {
            if (other.detailImageUrl != null) {
                return false;
            }
        } else if (!detailImageUrl.equals(other.detailImageUrl)) {
            return false;
        }
        if (expirationDate == null) {
            if (other.expirationDate != null) {
                return false;
            }
        } else if (!expirationDate.equals(other.expirationDate)) {
            return false;
        }
        if (expirationDateInMillis == null) {
            if (other.expirationDateInMillis != null) {
                return false;
            }
        } else if (!expirationDateInMillis.equals(other.expirationDateInMillis)) {
            return false;
        }
        // Ignore has been read in some comparisons
        if (!ignoreTransientFields) {
            if (hasBeenRead == null) {
                if (other.hasBeenRead != null) {
                    return false;
                }
            } else if (!hasBeenRead.equals(other.hasBeenRead)) {
                return false;
            }
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (lastUpdatedDate == null) {
            if (other.lastUpdatedDate != null) {
                return false;
            }
        } else if (!lastUpdatedDate.equals(other.lastUpdatedDate)) {
            return false;
        }
        if (lastUpdatedDateInMillis == null) {
            if (other.lastUpdatedDateInMillis != null) {
                return false;
            }
        } else if (!lastUpdatedDateInMillis.equals(other.lastUpdatedDateInMillis)) {
            return false;
        }
        if (linkDestination == null) {
            if (other.linkDestination != null) {
                return false;
            }
        } else if (!linkDestination.equals(other.linkDestination)) {
            return false;
        }
        if (linkId == null) {
            if (other.linkId != null) {
                return false;
            }
        } else if (!linkId.equals(other.linkId)) {
            return false;
        }
        if (messageBody == null) {
            if (other.messageBody != null) {
                return false;
            }
        } else if (!messageBody.equals(other.messageBody)) {
            return false;
        }
        if (messageHeading == null) {
            if (other.messageHeading != null) {
                return false;
            }
        } else if (!messageHeading.equals(other.messageHeading)) {
            return false;
        }
        if (startDate == null) {
            if (other.startDate != null) {
                return false;
            }
        } else if (!startDate.equals(other.startDate)) {
            return false;
        }
        if (startDateInMillis == null) {
            if (other.startDateInMillis != null) {
                return false;
            }
        } else if (!startDateInMillis.equals(other.startDateInMillis)) {
            return false;
        }
        if (urlEndpoint == null) {
            if (other.urlEndpoint != null) {
                return false;
            }
        } else if (!urlEndpoint.equals(other.urlEndpoint)) {
            return false;
        }
        if(isExternalLink == null) {
            if(other.isExternalLink != null) {
                return false;
            }
        } else if(!isExternalLink.equals(other.isExternalLink)) {
            return false;
        }
        if(androidLink == null) {
            if(other.androidLink != null) {
                return false;
            }
        } else if(!androidLink.equals(other.androidLink)) {
            return false;
        }
        return true;
	}

}
