/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.events.EventSeriesDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreActivity;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.offers.OfferDetailActivity;
import com.universalstudios.orlandoresort.model.network.domain.events.EventSeries;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Event;
import com.universalstudios.orlandoresort.model.network.domain.venue.Venue;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;

import java.util.Arrays;

/**
 * 
 * 
 * @author Steven Byle
 */
public class DetailUtils {
    private static final String TAG = DetailUtils.class.getSimpleName();

	public static boolean hasDetailPage(Integer poiTypeId) {
		if (poiTypeId == null) {
			return false;
		}

		switch (poiTypeId) {
			case PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_DINING:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_WATERPARK:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_LOCKERS:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_RENTALS:
				return true;
			default:
				return false;
		}
	}

	public static Bundle getDetailPageBundle(String venueObjectJson, String poiObjectJson, Integer poiTypeId, Long mapTileId) {
		if (venueObjectJson == null || poiObjectJson == null || poiTypeId == null) {
			return null;
		}

		DetailType detailType = null;
		switch (poiTypeId) {
			case PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE:
				detailType = DetailType.RIDE;
				break;
			case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE:
				detailType = DetailType.SHOW;
				break;
			case PointsOfInterestTable.VAL_POI_TYPE_ID_DINING:
				detailType = DetailType.DINING;
				break;
			case PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL:
				detailType = DetailType.HOTEL;
				break;
			case PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT:
				detailType = DetailType.ENTERTAINMENT;
				break;
			case PointsOfInterestTable.VAL_POI_TYPE_ID_WATERPARK:
				detailType = DetailType.PARK;
				break;
			case PointsOfInterestTable.VAL_POI_TYPE_ID_LOCKERS:
				detailType = DetailType.LOCKER;
				break;
			case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP:
				detailType = DetailType.SHOPPING;
				break;
			case PointsOfInterestTable.VAL_POI_TYPE_ID_RENTALS:
				detailType = DetailType.RENTAL_SERVICES;
				break;
			case PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT:
			    detailType = DetailType.EVENT;
			    break;
			case PointsOfInterestTable.VAL_POI_TYPE_ID_GATEWAY:
				detailType = DetailType.GATEWAY;
				break;
			case PointsOfInterestTable.VAL_POI_TYPE_ID_GEN_LOCATION:
				detailType = DetailType.GENERAL_LOCATION;
				break;
			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "getDetailPageBundle: can't create bundle for poiTypeId = " + poiTypeId);
				}
				return null;
		}
		return DetailActivity.newInstanceBundle(detailType, venueObjectJson, poiObjectJson, poiTypeId, mapTileId);
	}
	
    public static boolean openEventSeriesDetailPage(Context context, String venueObjectJson,
			String eventSeriesObjectJson, Long mapTileId) {
        if (eventSeriesObjectJson == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "openEventSeriesDetailPage: eventSeriesObjectJson is null, can't open detail page");
            }
            return false;
        }

        EventSeries eventSeries = GsonObject.fromJson(eventSeriesObjectJson, EventSeries.class);
        // If event series has one event load event detail page
        if (eventSeries.getEvents() != null && eventSeries.getEvents().size() == 1) {
            if (TextUtils.isEmpty(venueObjectJson)) {
                venueObjectJson = Venue.DEFAULT_EVENT_VENUE.toJson();
            }
            Event event = eventSeries.getEvents().get(0);
            if (BuildConfig.DEBUG) {
                Log.v(TAG, "openEventSeriesDetailPage: loading event detail page");
            }
            return openDetailPage(context, venueObjectJson, event.toJson(),
                    PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT, false, mapTileId);
        } else {
            Bundle eventSeriesDetailBundle = EventSeriesDetailActivity
                    .newInstanceBundle(eventSeriesObjectJson);
            if (eventSeriesDetailBundle != null) {
                context.startActivity(new Intent(context, EventSeriesDetailActivity.class)
                        .putExtras(eventSeriesDetailBundle));
                if (BuildConfig.DEBUG) {
                    Log.v(TAG, "openEventSeriesDetailPage: loading event series detail page");
                }
                return true;
            } else {
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "openEventSeriesDetailPage: can't open detail page for event series = "
                            + eventSeriesObjectJson);
                }
            }
        }

        return false;
    }
    
    public static boolean openOfferDetailPage(Context context, long offerId) {
        Bundle detailActivityBundle = OfferDetailActivity.newInstanceBundle(
                DatabaseQueryUtils.getOffersById(Arrays.asList(offerId)), offerId);
        if (detailActivityBundle != null) {
            context.startActivity(new Intent(context, OfferDetailActivity.class)
                    .putExtras(detailActivityBundle));
            return true;
        } else {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "openOfferDetailPage: can't open detail page for offerId = " + offerId);
            }
        }

        return false;
    }

	public static boolean openDetailPage(Context context, String venueObjectJson, String poiObjectJson, Integer poiTypeId,
			boolean useExplorePageIfNoDetail) {
		return openDetailPage(context, venueObjectJson, poiObjectJson, poiTypeId, useExplorePageIfNoDetail, null);
	}

	public static boolean openDetailPage(Context context, String venueObjectJson, String poiObjectJson, Integer poiTypeId,
			boolean useExplorePageIfNoDetail, Long mapTileId) {
		if (venueObjectJson == null || poiObjectJson == null || poiTypeId == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "openDetailPage: venueObjectJson or poiObjectJson or poiTypeId is null, can't open detail page");
			}
			return false;
		}

		// Try to open a detail activity
		Bundle detailActivityBundle = getDetailPageBundle(venueObjectJson, poiObjectJson, poiTypeId, mapTileId);
		if (detailActivityBundle != null) {
			context.startActivity(new Intent(context, DetailActivity.class).putExtras(detailActivityBundle));
			return true;
		}
		else {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "openDetailPage: can't open detail page for poiTypeId = " + poiTypeId
						+ " trying to open an explore map/list page instead");
			}
		}

		// Otherwise, try to open an explore map/list page for the POI
		if (useExplorePageIfNoDetail) {
			switch (poiTypeId) {
				case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP:
					context.startActivity(new Intent(context, ExploreActivity.class)
					.putExtras(ExploreActivity.newInstanceBundle(
							R.string.drawer_item_shopping,
							ExploreType.SHOPPING,
							poiObjectJson)));
					return true;
				case PointsOfInterestTable.VAL_POI_TYPE_ID_RESTROOM:
					context.startActivity(new Intent(context, ExploreActivity.class)
					.putExtras(ExploreActivity.newInstanceBundle(
							R.string.drawer_item_restrooms,
							ExploreType.RESTROOMS,
							poiObjectJson)));
					return true;
				case PointsOfInterestTable.VAL_POI_TYPE_ID_LOCKERS:
					context.startActivity(new Intent(context, ExploreActivity.class)
					.putExtras(ExploreActivity.newInstanceBundle(
							R.string.drawer_item_lockers,
							ExploreType.LOCKERS,
							poiObjectJson)));
					return true;
				case PointsOfInterestTable.VAL_POI_TYPE_ID_RENTALS:
					context.startActivity(new Intent(context, ExploreActivity.class)
							.putExtras(ExploreActivity.newInstanceBundle(
									R.string.drawer_item_rental_services,
									ExploreType.RENTAL_SERVICES,
									poiObjectJson)));
					return true;
				case PointsOfInterestTable.VAL_POI_TYPE_ID_GUEST_SERVICES_BOOTH:
					context.startActivity(new Intent(context, ExploreActivity.class)
					.putExtras(ExploreActivity.newInstanceBundle(
							R.string.drawer_item_guest_services_locations,
							ExploreType.GUEST_SERVICES_BOOTHS,
							poiObjectJson)));
					return true;
				case PointsOfInterestTable.VAL_POI_TYPE_ID_ATM:
					context.startActivity(new Intent(context, ExploreActivity.class)
					.putExtras(ExploreActivity.newInstanceBundle(
							R.string.drawer_item_atms,
							ExploreType.ATMS,
							poiObjectJson)));
					return true;
				case PointsOfInterestTable.VAL_POI_TYPE_ID_CHARGING_STATION:
					context.startActivity(new Intent(context, ExploreActivity.class)
					.putExtras(ExploreActivity.newInstanceBundle(
							R.string.drawer_item_charging_stations,
							ExploreType.CHARGING_STATIONS,
							poiObjectJson)));

					return true;
				case PointsOfInterestTable.VAL_POI_TYPE_ID_LOST_AND_FOUND_STATION:
					context.startActivity(new Intent(context, ExploreActivity.class)
					.putExtras(ExploreActivity.newInstanceBundle(
							R.string.drawer_item_lost_and_found,
							ExploreType.LOST_AND_FOUND,
							poiObjectJson)));
					return true;
				case PointsOfInterestTable.VAL_POI_TYPE_ID_FIRST_AID_STATION:
					context.startActivity(new Intent(context, ExploreActivity.class)
					.putExtras(ExploreActivity.newInstanceBundle(
							R.string.drawer_item_first_aid,
							ExploreType.FIRST_AID,
							poiObjectJson)));
					return true;
				case PointsOfInterestTable.VAL_POI_TYPE_ID_SERVICE_ANIMAL_REST_AREA:
					context.startActivity(new Intent(context, ExploreActivity.class)
					.putExtras(ExploreActivity.newInstanceBundle(
							R.string.drawer_item_service_animal_rest_areas,
							ExploreType.SERVICE_ANIMAL_REST_AREAS,
							poiObjectJson)));
					return true;
				case PointsOfInterestTable.VAL_POI_TYPE_ID_SMOKING_AREA:
					context.startActivity(new Intent(context, ExploreActivity.class)
					.putExtras(ExploreActivity.newInstanceBundle(
							R.string.drawer_item_smoking_areas,
							ExploreType.SMOKING_AREAS,
							poiObjectJson)));
					return true;
				case PointsOfInterestTable.VAL_POI_TYPE_ID_GATEWAY:
					context.startActivity(new Intent(context, ExploreActivity.class)
							.putExtras(ExploreActivity.newInstanceBundle(
									R.string.drawer_item_gateways,
									ExploreType.GATEWAYS,
									poiObjectJson)));
					return true;
				case PointsOfInterestTable.VAL_POI_TYPE_ID_GEN_LOCATION:
					context.startActivity(new Intent(context, ExploreActivity.class)
							.putExtras(ExploreActivity.newInstanceBundle(
									R.string.drawer_item_general_locations,
									ExploreType.GENERAL_LOCATIONS,
									poiObjectJson)));
					return true;
				default:
					if (BuildConfig.DEBUG) {
						Log.i(TAG, "openDetailPage: can't open explore page for poiTypeId = " + poiTypeId);
					}
			}
		}

		return false;
	}

}
