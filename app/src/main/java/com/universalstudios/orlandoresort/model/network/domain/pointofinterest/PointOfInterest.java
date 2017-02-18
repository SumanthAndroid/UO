/**
 * 
 */
package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import android.util.Log;

import com.crittercism.app.Crittercism;
import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;

import org.parceler.Parcel;

import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
@Parcel
public class PointOfInterest extends GsonObject {
	private static final String TAG = PointOfInterest.class.getSimpleName();

	@SerializedName("MblDisplayName")
	String displayName;

	@SerializedName("LandId")
	Long landId;

	@SerializedName("VenueId")
	Long venueId;

	@SerializedName("MblLongDescription")
	String longDescription;

	@SerializedName("MblShortDescription")
	String shortDescription;

	@SerializedName("Latitude")
	Double latitude;

	@SerializedName("Longitude")
	Double longitude;

	@SerializedName("Url")
	String urlEndpoint;

	@SerializedName("Id")
	Long id;

	@SerializedName("ListImage")
	String listImageUrl;

	@SerializedName("ThumbnailImage")
	String thumbnailImageUrl;

	@SerializedName("DetailImages")
	List<String> detailImageUrls;

	@SerializedName("IsRoutable")
	Boolean isRoutable;

	@SerializedName("SiteUrl")
	String siteUrl;

	@SerializedName("SocialSharingText")
	String socialSharingText;
	
	@SerializedName("Tags")
	List<String> tags;
	
	@SerializedName("OfferIds")
	List<Long> offerIds;

	@SerializedName("InteractiveExperiences")
	List<Long> interactiveExperiences;

	@SerializedName("PhotoFrameExperiences")
	List<Long> photoFrameExperienceIds;

	@SerializedName("VideoImage")
	String videoImageUrl;

	@SerializedName("VideoURL")
	String videoUrl;

	@SerializedName("QueueImage")
	String queueImage;

	// ************ NOT PARSED, ADDED FIELDS ************
	// USED FOR FAVORITES
	@SerializedName("IsFavorite")
	Boolean isFavorite;

	// USED FOR TIMELINE SORTING
	@SerializedName("sortOrder")
	int sortOrder;

	// USED BY EVENT MAP
	@SerializedName("groupedPoiIds")
	List<Long> groupedPoiIds;

	// USED BY EVENT MAP
	@SerializedName("venueObjectJson")
	String venueObjectJson;

	// USED BY EVENT MAP
	@SerializedName("poiTypeId")
	Integer poiTypeId;


	public static boolean arePoisEqual(PointOfInterest poi, int poiTypeId, String poiObjectJson) {

		try {
			switch (poiTypeId) {
				case PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE:
					Ride rideFromJson = (Ride) fromJson(poiObjectJson, poiTypeId);
					return ((Ride) poi).equals(rideFromJson);
				case PointsOfInterestTable.VAL_POI_TYPE_ID_DINING:
					Dining diningFromJson = (Dining) fromJson(poiObjectJson, poiTypeId);
					return ((Dining) poi).equals(diningFromJson);
				case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW:
					Show showFromJson = (Show) fromJson(poiObjectJson, poiTypeId);
					return ((Show) poi).equals(showFromJson);
				case PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE:
					Parade paradeFromJson = (Parade) fromJson(poiObjectJson, poiTypeId);
					return ((Parade) poi).equals(paradeFromJson);
				case PointsOfInterestTable.VAL_POI_TYPE_ID_RESTROOM:
					Restroom restroomFromJson = (Restroom) fromJson(poiObjectJson, poiTypeId);
					return ((Restroom) poi).equals(restroomFromJson);
				case PointsOfInterestTable.VAL_POI_TYPE_ID_LOCKERS:
					Lockers lockersFromJson = (Lockers) fromJson(poiObjectJson, poiTypeId);
					return ((Lockers) poi).equals(lockersFromJson);
				case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP:
					Shop shopFromJson = (Shop) fromJson(poiObjectJson, poiTypeId);
					return ((Shop) poi).equals(shopFromJson);
				case PointsOfInterestTable.VAL_POI_TYPE_ID_RENTALS:
					RentalServices rentalServicesFromJson = (RentalServices) fromJson(poiObjectJson, poiTypeId);
					return ((RentalServices) poi).equals(rentalServicesFromJson);
				case PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT:
					Entertainment entertainmentFromJson = (Entertainment) fromJson(poiObjectJson, poiTypeId);
					return ((Entertainment) poi).equals(entertainmentFromJson);
				case PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL:
					Hotel hotelFromJson = (Hotel) fromJson(poiObjectJson, poiTypeId);
					return ((Hotel) poi).equals(hotelFromJson);
				case PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT:
				    Event eventFronJson = (Event) fromJson(poiObjectJson, poiTypeId);
				    return ((Event) poi).equals(eventFronJson);
				case PointsOfInterestTable.VAL_POI_TYPE_ID_GATEWAY:
					Gateway gatewayFromJson = (Gateway) fromJson(poiObjectJson, poiTypeId);
					return ((Gateway) poi).equals(gatewayFromJson);
				default:
					PointOfInterest poiFromJson = GsonObject.fromJson(poiObjectJson, PointOfInterest.class);
					return poi.equals(poiFromJson);
			}
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "arePoisEqual: exception trying to compare POIs", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
			return false;
		}
	}

	/**
	 * Create a specific point of interest child class object from JSON, based
	 * on the POI type id.
	 * 
	 * @param poiObjectJson
	 * @param poiTypeId
	 * @return
	 */
	public static PointOfInterest fromJson(String poiObjectJson, Integer poiTypeId) {
		if (poiObjectJson == null || poiTypeId == null) {
			return null;
		}

		switch (poiTypeId) {
			case PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE:
				return GsonObject.fromJson(poiObjectJson, Ride.class);
			case PointsOfInterestTable.VAL_POI_TYPE_ID_DINING:
				return GsonObject.fromJson(poiObjectJson, Dining.class);
			case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW:
				return GsonObject.fromJson(poiObjectJson, Show.class);
			case PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE:
				return GsonObject.fromJson(poiObjectJson, Parade.class);
			case PointsOfInterestTable.VAL_POI_TYPE_ID_RESTROOM:
				return GsonObject.fromJson(poiObjectJson, Restroom.class);
			case PointsOfInterestTable.VAL_POI_TYPE_ID_LOCKERS:
				return GsonObject.fromJson(poiObjectJson, Lockers.class);
			case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP:
				return GsonObject.fromJson(poiObjectJson, Shop.class);
			case PointsOfInterestTable.VAL_POI_TYPE_ID_RENTALS:
				return GsonObject.fromJson(poiObjectJson, RentalServices.class);
			case PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT:
				return GsonObject.fromJson(poiObjectJson, Entertainment.class);
			case PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL:
				return GsonObject.fromJson(poiObjectJson, Hotel.class);
			case PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT:
			    return GsonObject.fromJson(poiObjectJson, Event.class);
			case PointsOfInterestTable.VAL_POI_TYPE_ID_GATEWAY:
				return GsonObject.fromJson(poiObjectJson, Gateway.class);
			default:
				return GsonObject.fromJson(poiObjectJson, PointOfInterest.class);
		}
	}

	public boolean isSubType(long subTypeFlag) {
		return isSubType(getSubTypeFlags(), subTypeFlag);
	}

	public static boolean isSubType(long poiSubTypeFlags, long subTypeFlag) {
		return ((poiSubTypeFlags & subTypeFlag) > 0);
	}

	/**
	 * @return bitwise flags set for POI sub type fields
	 */
	public long getSubTypeFlags() {
		return 0l;
	}
	
    /**
     * Overridden in {@link Show}, {@link Ride}, and {@link Dining}
     * @return bitwise flags set for POI options
     */
    public long getOptionFlags() {
        return 0l;
    }

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @return the landId
	 */
	public Long getLandId() {
		return landId;
	}

	/**
	 * @return the longDescription
	 */
	public String getLongDescription() {
		return longDescription;
	}

	/**
	 * @return the shortDescription
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
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
	 * @return the VenueId
	 */
	public Long getVenueId() {
		return venueId;
	}

	/**
	 *
	 * @return the videoImageUrl
     */
	public String getVideoImageUrl() {
		return videoImageUrl;
	}

	/**
	 *
	 * @return the videoUrl
     */
	public String getVideoUrl() {
		return videoUrl;
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
	 * @return the detailImageUrls
	 */
	public List<String> getDetailImageUrls() {
		return detailImageUrls;
	}

	/**
	 * @return the isRoutable
	 */
	public Boolean getIsRoutable() {
		return isRoutable;
	}

	/**
	 * @param isFavorite
	 */
	public void setIsFavorite(Boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	/**
	 * @return the isFavorite
	 */
	public Boolean getIsFavorite() {
		return isFavorite;
	}

	/**
	 * @return the siteUrl
	 */
	public String getSiteUrl() {
		return siteUrl;
	}

	/**
	 * @return the socialSharingText
	 */
	public String getSocialSharingText() {
		return socialSharingText;
	}

	/**
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}
	
    /**
     * @return the offerIds
     */
    public List<Long> getOfferIds() {
        return offerIds;
    }

	public List<Long> getInteractiveExperiences() {
		return interactiveExperiences;
	}

    public List<Long> getPhotoFrameExperienceIds() {
        return photoFrameExperienceIds;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

	/**
	 * @return the groupedPoiIds
	 */
	public List<Long> getGroupedPoiIds() {
		return groupedPoiIds;
	}

	/**
	 * @param groupedPoiIds
	 *   the groupedPoiIds to set
	 */
	public void setGroupedPoiIds(List<Long> groupedPoiIds) {
		this.groupedPoiIds = groupedPoiIds;
	}

	public Integer getPoiTypeId() {
		return poiTypeId;
	}

	public void setPoiTypeId(Integer poiTypeId) {
		this.poiTypeId = poiTypeId;
	}

	public String getQueueImage() {
		return queueImage;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((detailImageUrls == null) ? 0 : detailImageUrls.hashCode());
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((isFavorite == null) ? 0 : isFavorite.hashCode());
        result = prime * result + ((isRoutable == null) ? 0 : isRoutable.hashCode());
        result = prime * result + ((landId == null) ? 0 : landId.hashCode());
        result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
        result = prime * result + ((listImageUrl == null) ? 0 : listImageUrl.hashCode());
        result = prime * result + ((longDescription == null) ? 0 : longDescription.hashCode());
        result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
        result = prime * result + ((offerIds == null) ? 0 : offerIds.hashCode());
        result = prime * result + ((shortDescription == null) ? 0 : shortDescription.hashCode());
        result = prime * result + ((siteUrl == null) ? 0 : siteUrl.hashCode());
        result = prime * result + ((socialSharingText == null) ? 0 : socialSharingText.hashCode());
        result = prime * result + ((tags == null) ? 0 : tags.hashCode());
        result = prime * result + ((thumbnailImageUrl == null) ? 0 : thumbnailImageUrl.hashCode());
        result = prime * result + ((urlEndpoint == null) ? 0 : urlEndpoint.hashCode());
        result = prime * result + ((venueId == null) ? 0 : venueId.hashCode());
		result = prime * result + ((interactiveExperiences == null) ? 0 : interactiveExperiences.hashCode());
		result = prime * result + ((photoFrameExperienceIds == null) ? 0 : photoFrameExperienceIds.hashCode());
		result = prime * result + ((videoImageUrl == null) ? 0 : videoImageUrl.hashCode());
		result = prime * result + ((videoUrl == null) ? 0 : videoUrl.hashCode());
		result = prime * result + sortOrder;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        // By default, ignore any transient state when making comparisons for
        // syncing
        return equals(obj, true);
    }

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
        PointOfInterest other = (PointOfInterest) obj;
        if (detailImageUrls == null) {
            if (other.detailImageUrls != null) {
                return false;
            }
        } else if (!detailImageUrls.equals(other.detailImageUrls)) {
            return false;
        }
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
        // Don't compare transient data in syncs
        if (!ignoreTransientFields) {
            if (isFavorite == null) {
                if (other.isFavorite != null) {
                    return false;
                }
            } else if (!isFavorite.equals(other.isFavorite)) {
                return false;
            }
        }
        if (isRoutable == null) {
            if (other.isRoutable != null) {
                return false;
            }
        } else if (!isRoutable.equals(other.isRoutable)) {
            return false;
        }
        if (landId == null) {
            if (other.landId != null) {
                return false;
            }
        } else if (!landId.equals(other.landId)) {
            return false;
        }
        if (latitude == null) {
            if (other.latitude != null) {
                return false;
            }
        } else if (!latitude.equals(other.latitude)) {
            return false;
        }
        if (listImageUrl == null) {
            if (other.listImageUrl != null) {
                return false;
            }
        } else if (!listImageUrl.equals(other.listImageUrl)) {
            return false;
        }
        if (longDescription == null) {
            if (other.longDescription != null) {
                return false;
            }
        } else if (!longDescription.equals(other.longDescription)) {
            return false;
        }
        if (longitude == null) {
            if (other.longitude != null) {
                return false;
            }
        } else if (!longitude.equals(other.longitude)) {
            return false;
        }
        if (offerIds == null) {
            if (other.offerIds != null) {
                return false;
            }
        } else if (!offerIds.equals(other.offerIds)) {
            return false;
        }
        if (shortDescription == null) {
            if (other.shortDescription != null) {
                return false;
            }
        } else if (!shortDescription.equals(other.shortDescription)) {
            return false;
        }
        if (siteUrl == null) {
            if (other.siteUrl != null) {
                return false;
            }
        } else if (!siteUrl.equals(other.siteUrl)) {
            return false;
        }
        if (socialSharingText == null) {
            if (other.socialSharingText != null) {
                return false;
            }
        } else if (!socialSharingText.equals(other.socialSharingText)) {
            return false;
        }
        if (tags == null) {
            if (other.tags != null) {
                return false;
            }
        } else if (!tags.equals(other.tags)) {
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
        if (sortOrder != other.sortOrder) {
            return false;
        }
		if (interactiveExperiences == null) {
			if (other.interactiveExperiences != null) {
				return false;
			}
		} else if (!interactiveExperiences.equals(other.interactiveExperiences)) {
			return false;
		}
		if (photoFrameExperienceIds == null) {
			if (other.photoFrameExperienceIds != null) {
				return false;
			}
		} else if (!photoFrameExperienceIds.equals(other.photoFrameExperienceIds)) {
			return false;
		}
		if (videoImageUrl == null) {
			if (other.videoImageUrl != null)
				return false;
		}
		else if (!videoImageUrl.equals(other.videoImageUrl))
			return false;
		if (videoUrl == null) {
			if (other.videoUrl != null)
				return false;
		}
		else if (!videoUrl.equals(other.videoUrl))
			return false;
		// Don't compare transient data in syncs
		if (!ignoreTransientFields) {
			if (groupedPoiIds == null) {
				if (other.groupedPoiIds != null) {
					return false;
				}
			} else if (!groupedPoiIds.equals(other.groupedPoiIds)) {
				return false;
			}
		}
		// Don't compare transient data in syncs
		if (!ignoreTransientFields) {
			if (venueObjectJson == null) {
				if (other.venueObjectJson != null) {
					return false;
				}
			} else if (!venueObjectJson.equals(other.venueObjectJson)) {
				return false;
			}
		}
		// Don't compare transient data in syncs
		if (!ignoreTransientFields) {
			if (poiTypeId == null) {
				if (other.poiTypeId != null) {
					return false;
				}
			} else if (!poiTypeId.equals(other.poiTypeId)) {
				return false;
			}
		}
        return true;
    }
}
