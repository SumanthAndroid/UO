package com.universalstudios.orlandoresort.controller.userinterface.explore.list;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crittercism.app.Crittercism;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.events.EventUtils;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.explore.PoiViewHolder;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions.FilterSort;
import com.universalstudios.orlandoresort.controller.userinterface.pois.PoiUtils;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Dining;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Event;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.EventDate;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Gateway;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Lockers;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Ride;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Shop;
import com.universalstudios.orlandoresort.model.network.domain.venue.VenueHours;
import com.universalstudios.orlandoresort.model.network.image.ImageUtils;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.request.RequestQueryParams;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventTimesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.ShowTimesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenueLandsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;
import com.universalstudios.orlandoresort.view.buttons.FavoriteToggleButton;
import com.universalstudios.orlandoresort.view.stickylistheaders.StickyListHeadersAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 
 * 
 * @author Steven Byle
 */
public class ExploreListCursorAdapter extends CursorAdapter implements StickyListHeadersAdapter, OnClickListener {
	private static final String TAG = ExploreListCursorAdapter.class.getSimpleName();

	private static SparseIntArray sPoiHeaderStringResIdMap;
	private final ExploreType mExploreType;
	private final String mImageSizeParam;
	private final UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
	private final Picasso mPicasso;
	private OnExploreListChildClickListener mOnExploreListChildClickListener;
	private FilterSort mFilterSort;
	private static final double METER_IN_FEET = 3.28084;
	private boolean hideStickyHeader = false;
	private Resources resources;
	
	private enum WaitTimes {
	    ZERO(0, R.string.poi_list_header_sort_wait_times_unknown),
	    LESS_5(5, R.string.poi_list_header_sort_wait_times_5_or_less),
	    LESS_15(15, R.string.poi_list_header_sort_wait_times_15_or_less),
	    LESS_30(30, R.string.poi_list_header_sort_wait_times_30_or_less),
	    LESS_45(45, R.string.poi_list_header_sort_wait_times_45_or_less),
	    LESS_60(60, R.string.poi_list_header_sort_wait_times_60_or_less),
	    LESS_75(75, R.string.poi_list_header_sort_wait_times_75_or_less),
	    LESS_90(90, R.string.poi_list_header_sort_wait_times_90_or_less),
	    MORE_90(Integer.MAX_VALUE, R.string.poi_list_header_sort_wait_times_90_or_more);
	    
	    private final int value;
	    private final int stringId;
        private WaitTimes(int value, int stringId) {
            this.value = value;
            this.stringId = stringId;
        }
        public int getValue() {
            return value;
        }
        public int getStringId() {
            return stringId;
        }
	}
	
	private enum Distances {
	    VERY_CLOSE(100, R.string.poi_list_header_sort_distance_100_or_less),
	    VERY_NEAR(300, R.string.poi_list_header_sort_distance_300_or_less),
	    FAR(500, R.string.poi_list_header_sort_distance_500_or_less),
	    VERY_FAR(Integer.MAX_VALUE, R.string.poi_list_header_sort_distance_500_or_more);
	    
	    private final int value;
        private final int stringId;
        private Distances(int value, int stringId) {
            this.value = value;
            this.stringId = stringId;
        }
        public int getValue() {
            return value;
        }
        public int getStringId() {
            return stringId;
        }
	}
	
	private enum ShowTimes {
	    LESS_15(15, R.string.poi_list_header_sort_show_times_15_or_less),
	    LESS_30(30, R.string.poi_list_header_sort_show_times_30_or_less),
	    LESS_45(45, R.string.poi_list_header_sort_show_times_45_or_less),
	    LESS_60(60, R.string.poi_list_header_sort_show_times_60_or_less),
	    LESS_90(90, R.string.poi_list_header_sort_show_times_90_or_less),
	    LESS_120(120, R.string.poi_list_header_sort_show_times_120_or_less),
	    MORE_120(Integer.MAX_VALUE, R.string.poi_list_header_sort_show_times_120_or_more);
	    
	    private final int value;
        private final int stringId;
        ShowTimes(int value, int stringId) {
            this.value = value;
            this.stringId = stringId;
        }
        public int getValue() {
            return value;
        }
        public int getStringId() {
            return stringId;
        }
	}

	public ExploreListCursorAdapter(Context context, Cursor c, ExploreType exploreType,
			OnExploreListChildClickListener onExploreListChildClickListener, boolean hideStickyHeader) {
		super(context, c, 0);

		mExploreType = exploreType;
		mOnExploreListChildClickListener = onExploreListChildClickListener;

		// Create the image downloader to get the images
		mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
				CacheUtils.POI_IMAGE_DISK_CACHE_NAME,
				CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
				CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);

		mPicasso = new Picasso.Builder(context)
		.loggingEnabled(UniversalOrlandoImageDownloader.SHOW_DEBUG)
		.downloader(mUniversalOrlandoImageDownloader)
		.build();

		mImageSizeParam = ImageUtils.getPoiImageSizeString(context.getResources().getInteger(R.integer.poi_list_image_dpi_shift));
		
		this.hideStickyHeader = hideStickyHeader;
		resources = context.getResources();
		
		
		// Set FilterSort
        getFilterSortFromCursor(c);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View row = inflater.inflate(R.layout.list_poi_item, parent, false);
		
        // Set timeline background if this is for the event timeline
        if (mExploreType == ExploreType.EVENTS  || mExploreType == ExploreType.UPCOMING_EVENTS) {
            row.setBackgroundResource(R.drawable.waypoint);
        }

		PoiViewHolder holder = new PoiViewHolder(row);
		holder.displayNameText = (TextView) row.findViewById(R.id.list_poi_item_display_name_text);
		holder.venueNameText = (TextView) row.findViewById(R.id.list_poi_item_venue_name_text);
		holder.extraInfoText = (TextView) row.findViewById(R.id.list_poi_item_extra_info_text);
		holder.listImage = (ImageView) row.findViewById(R.id.list_poi_item_image);
		holder.listImageNoImage = (ImageView) row.findViewById(R.id.list_poi_item_no_image_logo);
		holder.guideMeLayout = (LinearLayout) row.findViewById(R.id.list_poi_item_guide_me_button_layout);
		holder.guideMeDivider = row.findViewById(R.id.list_poi_item_guide_me_button_divider);
		holder.favoriteToggleButton = (FavoriteToggleButton) row.findViewById(R.id.list_poi_item_favorite_toggle_button);
		holder.locateButton = (Button) row.findViewById(R.id.list_poi_item_locate_button);
		holder.circleBadgeRootLayout = (LinearLayout) row.findViewById(R.id.poi_item_circle_badge_root_container);
		holder.waitTimeLayout = (LinearLayout) row.findViewById(R.id.poi_item_circle_badge_wait_time_layout);
		holder.waitTimeMinNumText = (TextView) row.findViewById(R.id.poi_item_circle_badge_wait_time_num_text);
		holder.showTimeLayout = (FrameLayout) row.findViewById(R.id.poi_item_circle_badge_show_time_layout);
		holder.showTimeBackgroundGray = row.findViewById(R.id.poi_item_circle_badge_show_time_background_gray);
		holder.showTimeBackgroundBlue = row.findViewById(R.id.poi_item_circle_badge_show_time_background_blue);
		holder.showTimeStartsTimeText = (TextView) row.findViewById(R.id.poi_item_circle_badge_show_time_starts_time_text);
		holder.showTimeStartsText = (TextView) row.findViewById(R.id.poi_item_circle_badge_show_time_starts_text);
		holder.showTimeOpensText = (TextView) row.findViewById(R.id.poi_item_circle_badge_show_time_opens_text);
		holder.showTimeAmPmText = (TextView) row.findViewById(R.id.poi_item_circle_badge_show_time_am_pm_text);
		holder.closedLayout = (LinearLayout) row.findViewById(R.id.poi_item_circle_badge_closed_layout);
		holder.closedText = (TextView) row.findViewById(R.id.poi_item_circle_badge_closed_text);
		holder.closedWeatherText = (TextView) row.findViewById(R.id.poi_item_circle_badge_weather_text);
		holder.closedTemporaryText = (TextView) row.findViewById(R.id.poi_item_circle_badge_temporary_text);
		holder.closedCapacityText = (TextView) row.findViewById(R.id.poi_item_circle_badge_closed_capacity_text);

		// Listen for guide me clicks
		holder.guideMeLayout.setOnClickListener(this);
		holder.favoriteToggleButton.setOnClickListener(this);
		holder.locateButton.setOnClickListener(this);

		if (BuildConfigUtils.isLocationFlavorHollywood()) {
			holder.locateButton.setVisibility(View.VISIBLE);
		} else {
			holder.locateButton.setVisibility(View.GONE);
		}

		row.setTag(holder);
		return row;
	}

	@Override
	public void bindView(View row, final Context context, Cursor cursor) {

		// Get data from database row
		Integer poiTypeId = cursor.getInt(cursor.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
		String displayName = cursor.getString(cursor.getColumnIndex(PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME));
		String poiObjectJson = cursor.getString(cursor.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
		String listImageUrl = cursor.getString(cursor.getColumnIndex(PointsOfInterestTable.COL_LIST_IMAGE_URL));
		boolean isRoutable = (cursor.getInt(cursor.getColumnIndex(PointsOfInterestTable.COL_IS_ROUTABLE)) != 0);
		boolean isFavorite = (cursor.getInt(cursor.getColumnIndex(PointsOfInterestTable.COL_IS_FAVORITE)) != 0);
		String venueName = cursor.getString(cursor.getColumnIndex(VenuesTable.COL_ALIAS_DISPLAY_NAME));
		long venueId = cursor.getLong(cursor.getColumnIndex(VenuesTable.COL_VENUE_ID));
		String venueLandName = cursor.getString(cursor.getColumnIndex(VenueLandsTable.COL_ALIAS_DISPLAY_NAME));

		// Ensure display name is not null
		displayName = displayName != null ? displayName : "";

		// Special header for hotels and water parks
		if (poiTypeId != null) {
			if (poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL) {
				venueName = context.getString(R.string.poi_list_item_venue_name_hotel);
			}
			else if (poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_WATERPARK) {
				venueName = context.getString(R.string.poi_list_item_venue_name_waterpark);
			}
		}

		// Special header format for Universal Studios Hollywood
		if (venueId == VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD) {
			if (!TextUtils.isEmpty(venueName) && !TextUtils.isEmpty(venueLandName)) {
				venueName = context.getString(R.string.poi_venue_land_label_format, venueName, venueLandName);
			}
		}

		// Apply data to view
		final PoiViewHolder holder = (PoiViewHolder) row.getTag();

		// Attach the relevant data to the guide me and favorite layouts
		holder.guideMeLayout.setTag(R.id.key_view_tag_explore_list_poi_object_json, poiObjectJson);
		holder.guideMeLayout.setTag(R.id.key_view_tag_explore_list_poi_type_id, poiTypeId);
		holder.favoriteToggleButton.setTag(R.id.key_view_tag_explore_list_poi_object_json, poiObjectJson);
		holder.favoriteToggleButton.setTag(R.id.key_view_tag_explore_list_poi_type_id, poiTypeId);
		holder.locateButton.setTag(R.id.key_view_tag_explore_list_poi_object_json, poiObjectJson);
		holder.locateButton.setTag(R.id.key_view_tag_explore_list_poi_type_id, poiTypeId);

		holder.guideMeLayout.setContentDescription(context.getString(R.string.poi_list_item_guide_me_formatted_content_description, displayName));

		// Bind the text
		holder.displayNameText.setText(displayName);
		holder.venueNameText.setText(venueName != null ? venueName.toUpperCase(Locale.US) : "");
		holder.venueNameText.setVisibility(venueName != null ? View.VISIBLE : View.GONE);

		// Show the favorite button
		if (poiTypeId != null && PoiUtils.isFavoriteEnabled(poiTypeId)) {
			holder.favoriteToggleButton.setChecked(isFavorite);
			holder.favoriteToggleButton.setVisibility(View.VISIBLE);
		}
		else {
			holder.favoriteToggleButton.setVisibility(View.GONE);
		}

		// Set the content description
		holder.favoriteToggleButton.setContentDescriptionByPoiName(displayName);

		// Assume there is no image to start
		holder.listImage.setVisibility(View.GONE);
		holder.listImageNoImage.setVisibility(View.VISIBLE);

		// Load the list image
		if (listImageUrl != null && !listImageUrl.isEmpty()) {
			Uri listImageUri = null;
			try {
				listImageUri = Uri.parse(listImageUrl);
			}
			catch (Exception e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "bindView: invalid image URL: " + listImageUri, e);
				}

				// Log the exception to crittercism
				Crittercism.logHandledException(e);
			}

			if (listImageUri != null) {
				Uri imageUriToLoad = listImageUri.buildUpon()
						.appendQueryParameter(RequestQueryParams.Keys.IMAGE_SIZE, mImageSizeParam)
						.build();

				if (BuildConfig.DEBUG) {
					Log.d(TAG, "bindView: imageUriToLoad = " + imageUriToLoad);
				}

				mPicasso.load(imageUriToLoad).into(holder.listImage, new ListImageCallback(holder));
			}
		}
		else {
			holder.listImage.setVisibility(View.GONE);
			holder.listImageNoImage.setVisibility(View.VISIBLE);
		}

		// Show / hide the guide me button
		boolean isInPark = UniversalAppStateManager.getInstance().isInResortGeofence();
		PoiUtils.updateGuideMeButton(isInPark, isRoutable, holder);


		// Update the wait time / show time badge
		int poiTypeIdInt = poiTypeId != null ? poiTypeId.intValue() : -1;
		switch (poiTypeIdInt) {
			case PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE:
				Integer waitTime = cursor.getInt(cursor.getColumnIndex(PointsOfInterestTable.COL_WAIT_TIME));
				String hoursListJson = cursor.getString(cursor.getColumnIndex(VenuesTable.COL_HOURS_LIST_JSON));

				List<VenueHours> venueHoursList = null;
				if (hoursListJson != null) {
					try {
						venueHoursList = new Gson().fromJson(hoursListJson, new TypeToken<List<VenueHours>>() {}.getType());
					}
					catch (Exception e) {
						if (BuildConfig.DEBUG) {
							Log.e(TAG, "bindView: exception trying to parse venue hours list from JSON", e);
						}

						// Log the exception to crittercism
						Crittercism.logHandledException(e);
					}
				}

				Ride ride = (Ride) PointOfInterest.fromJson(poiObjectJson, poiTypeId);
				String opensAt = ride.getOpensAt();

				PoiUtils.updatePoiCircleBadgeForRide(waitTime, opensAt, venueHoursList, holder);
				break;
			case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE:
				waitTime = cursor.getInt(cursor.getColumnIndex(PointsOfInterestTable.COL_WAIT_TIME));
				hoursListJson = cursor.getString(cursor.getColumnIndex(VenuesTable.COL_HOURS_LIST_JSON));
				String showTimesJson = cursor.getString(cursor.getColumnIndex(PointsOfInterestTable.COL_SHOW_TIMES_JSON));

				venueHoursList = null;
				if (hoursListJson != null) {
					try {
						venueHoursList = new Gson().fromJson(hoursListJson, new TypeToken<List<VenueHours>>() {}.getType());
					}
					catch (Exception e) {
						if (BuildConfig.DEBUG) {
							Log.e(TAG, "bindView: exception trying to parse venue hours list from JSON", e);
						}

						// Log the exception to crittercism
						Crittercism.logHandledException(e);
					}
				}

				List<String> showTimesList = null;
				if (showTimesJson != null) {
					try {
						showTimesList = new Gson().fromJson(showTimesJson, new TypeToken<List<String>>() {}.getType());
					}
					catch (Exception e) {
						if (BuildConfig.DEBUG) {
							Log.e(TAG, "bindView: exception trying to parse show times list from JSON", e);
						}

						// Log the exception to crittercism
						Crittercism.logHandledException(e);
					}
				}

				// First try to show a wait time, if it exists
				PoiUtils.updatePoiCircleBadgeForRide(waitTime, null, venueHoursList, holder);

				// If the badge was hidden, try showing a show time instead
				if (holder.circleBadgeRootLayout.getVisibility() == View.GONE) {
					PoiUtils.updatePoiCircleBadgeForShow(showTimesList, holder, context);
				}
				break;
			case PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT:
			    Event event = (Event) PointOfInterest.fromJson(poiObjectJson, poiTypeId);
			    // Get start date from cursor, used for timeline events
			    if (event != null) {
                    if (cursor.getColumnIndex(EventTimesTable.COL_START_DATE) != -1) {
                        Long startDateUnix = cursor.getLong(cursor.getColumnIndex(EventTimesTable.COL_START_DATE));
						if (9223372036854775806L == startDateUnix) {
							startDateUnix = 0L;
						}
                        Long endDateUnix = cursor.getLong(cursor.getColumnIndex(EventTimesTable.COL_END_DATE));
                        EventDate eventDate = new EventDate(startDateUnix, endDateUnix);
                        if (event.getWaitTime() == null) {
                            EventUtils.updatePoiCircleBadgeForEvent(Arrays.asList(eventDate), holder, mExploreType == ExploreType.FAVORITES);
                        } else {
                            PoiUtils.updatePoiCircleBadgeForRide(event.getWaitTime(), null, null, holder);
                        }
                    } 
                    // Get dates from event object used everywhere except timelines
                    else {
                        if (event.getWaitTime() == null) {
                            EventUtils.updatePoiCircleBadgeForEvent(event.getEventDates(), holder, mExploreType == ExploreType.FAVORITES);
                        } else {
                            PoiUtils.updatePoiCircleBadgeForRide(event.getWaitTime(), null, null, holder);
                        }
                    }
			    } else {
			        holder.circleBadgeRootLayout.setVisibility(View.GONE);
			    }
			    break;
			//TODO add stuff for gateway and general location
			default:
				holder.circleBadgeRootLayout.setVisibility(View.GONE);
				break;
		}

		// Update extra line for certain POI types
		int extraInfoStringResId = -1;
		String extraInfoString = null;

		switch (poiTypeIdInt) {
			case PointsOfInterestTable.VAL_POI_TYPE_ID_DINING:
				Dining dining = (Dining) PointOfInterest.fromJson(poiObjectJson, poiTypeId);
				Boolean hasHealthyOptions = dining.getHasHealthyOptions();
				if (hasHealthyOptions != null && hasHealthyOptions.booleanValue()) {
					extraInfoStringResId = R.string.poi_list_item_dining_has_healthy_options_info;
				}
				break;

			case PointsOfInterestTable.VAL_POI_TYPE_ID_LOCKERS:
				Lockers lockers = (Lockers) PointOfInterest.fromJson(poiObjectJson, poiTypeId);
				if (lockers.getIsFree()) {
					extraInfoStringResId = R.string.poi_list_item_lockers_ride_info;
				}

				break;

			case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP:
				Shop shop = (Shop) PointOfInterest.fromJson(poiObjectJson, poiTypeId);
				Boolean sellsExpressPass = shop.getSellsExpressPass();
				Boolean hasPickagePickup = shop.getHasPackagePickup();
				if (sellsExpressPass != null && sellsExpressPass.booleanValue()) {
					extraInfoStringResId = R.string.poi_list_item_shop_sells_express_pass_info;
				} else if (hasPickagePickup != null && hasPickagePickup.booleanValue()){
					extraInfoStringResId = R.string.poi_list_item_shop_has_package_pickup_info;
				}
				break;

			//Subtitle for rentals
			case PointsOfInterestTable.VAL_POI_TYPE_ID_RENTALS:
				extraInfoStringResId = R.string.advanced_filter_rental_type_extra_info;
				break;

			case PointsOfInterestTable.VAL_POI_TYPE_ID_GATEWAY:
				Gateway gateway = (Gateway) PointOfInterest.fromJson(poiObjectJson, poiTypeId);
				extraInfoString = gateway.getGatewayType();
				break;

			default:
				break;
		}

		// Set the extra text, or hide it
		if (extraInfoStringResId != -1|| extraInfoString != null) {
			if(extraInfoStringResId != -1) {
				holder.extraInfoText.setText(extraInfoStringResId);
			} else {
				holder.extraInfoText.setText(extraInfoString);
			}
			holder.extraInfoText.setVisibility(View.VISIBLE);
		}
		else {
			holder.extraInfoText.setVisibility(View.GONE);
		}

		// Build a single content description that describes all UI elements in a specific order.
		StringBuilder contentDescriptionBuilder = new StringBuilder();

		// Add the attraction header to the content description
		if (!TextUtils.isEmpty(venueName)) {
			contentDescriptionBuilder.append(venueName).append("\n");
		}
		contentDescriptionBuilder.append(displayName).append("\n");

		// Add circle badge content description
		if (holder.circleBadgeRootLayout.getVisibility() == View.VISIBLE) {
			CharSequence circleBadgeContentDescription = holder.circleBadgeRootLayout.getContentDescription();
			if (circleBadgeContentDescription != null && circleBadgeContentDescription.length() > 0) {
				contentDescriptionBuilder.append(circleBadgeContentDescription).append("\n");
			}
		}

		// Set entire row's content description.
		row.setContentDescription(contentDescriptionBuilder.toString());
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.list_poi_section_header, parent, false);

			holder = new HeaderViewHolder();
			holder.headerTitleText = (TextView) convertView.findViewById(R.id.list_poi_section_header_title_text);
			convertView.setTag(holder);
		}
		else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		Cursor cursor = (Cursor) getItem(position);
		Integer poiTypeId = cursor.getInt(cursor.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
		Long poiSubTypeFlags = cursor.getLong(cursor.getColumnIndex(PointsOfInterestTable.COL_SUB_TYPE_FLAGS));

		// Get the proper header title
		if (FilterSort.EVENT_DATE == mFilterSort) {
            holder.headerTitleText.setText(getEventDateHeader(mContext,
                    cursor.getLong(cursor.getColumnIndex(EventTimesTable.COL_START_DATE))));
		} else {
		    int poiHeaderStringResId = getPoiHeaderStringResId(mExploreType, poiTypeId, poiSubTypeFlags,
	                mFilterSort, cursor);
		    holder.headerTitleText.setText(poiHeaderStringResId);
		}

		
		LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.list_poi_section_header_root_container);

		 ViewGroup.LayoutParams lp = (LayoutParams) layout.getLayoutParams();
		 
		if(hideStickyHeader){
			lp.height = 0;	
			holder.headerTitleText.setVisibility(View.GONE);
		}else {
			lp.height = resources.getDimensionPixelSize(R.dimen.list_poi_section_header_root_container_height);
			holder.headerTitleText.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		Cursor cursor = (Cursor) getItem(position);
		Integer poiTypeId = cursor.getInt(cursor.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
		Long poiSubTypeFlags = cursor.getLong(cursor.getColumnIndex(PointsOfInterestTable.COL_SUB_TYPE_FLAGS));

		// Get the proper header title
		int poiHeaderStringResId;
		if (FilterSort.EVENT_DATE == mFilterSort) {
		    poiHeaderStringResId = getEventDateHeader(mContext,
                    cursor.getLong(cursor.getColumnIndex(EventTimesTable.COL_START_DATE))).hashCode();
		} else {
            poiHeaderStringResId = getPoiHeaderStringResId(mExploreType, poiTypeId, poiSubTypeFlags,
                    mFilterSort, cursor);
		}

		return poiHeaderStringResId;
	}

	@Override
	public void onClick(View v) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onClick");
		}

		if (mOnExploreListChildClickListener != null) {
			String poiObjectJson = (String) v.getTag(R.id.key_view_tag_explore_list_poi_object_json);
			Integer poiTypeId = (Integer) v.getTag(R.id.key_view_tag_explore_list_poi_type_id);
			if (poiObjectJson != null && poiTypeId != null) {
				mOnExploreListChildClickListener.onExploreListChildClick(v, poiObjectJson, poiTypeId);
			}
		}
	}

	public void destroy() {
		mOnExploreListChildClickListener = null;
		if (mPicasso != null) {
			mPicasso.shutdown();
		}
		if (mUniversalOrlandoImageDownloader != null) {
			mUniversalOrlandoImageDownloader.destroy();
		}
	}
	
    private static String getEventDateHeader(Context context, Long startDateUnix) {
		if (9223372036854775806L == startDateUnix) {
			startDateUnix = 0L;
		}
        EventDate eventDate = new EventDate(startDateUnix, null);
        return EventUtils.getEventHeaderDateSpan(eventDate, false,
                context.getString(R.string.event_series_dates_tba));
    }

	private static int getPoiHeaderStringResId(ExploreType exploreType, int poiTypeId, long poiSubTypeFlags) {

		switch(exploreType) {

			case RIDES:
			case WAIT_TIMES:
				// Explore by activity - ride headers
				if (PointOfInterest.isSubType(poiSubTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_THRILL)) {
					return R.string.poi_list_header_rides_thrill;
				}
				else if (PointOfInterest.isSubType(poiSubTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_WATER)) {
					return R.string.poi_list_header_rides_water;
				}
				else if (PointOfInterest.isSubType(poiSubTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_VIDEO_3D_4D)) {
					return R.string.poi_list_header_rides_3d_and_4d;
				}
				else if (PointOfInterest.isSubType(poiSubTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_KID_FRIENDLY)) {
					return R.string.poi_list_header_rides_kids;
				}
				else {
					return getPoiHeaderStringResId(poiTypeId, R.string.poi_list_header_rides);
				}

			case SHOWS:
				// Explore by activity - show headers
				if (PointOfInterest.isSubType(poiSubTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_ACTION)) {
					return R.string.poi_list_header_shows_action;
				}
				else if (PointOfInterest.isSubType(poiSubTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_COMEDY)) {
					return R.string.poi_list_header_shows_comedy;
				}
				else if (PointOfInterest.isSubType(poiSubTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_MUSIC)) {
					return R.string.poi_list_header_shows_music;
				}
				else if (PointOfInterest.isSubType(poiSubTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_PARADE)) {
					return R.string.poi_list_header_shows_parade;
				}
				else if (PointOfInterest.isSubType(poiSubTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_CHARACTER)) {
					return R.string.poi_list_header_shows_character;
				}
				else {
					return getPoiHeaderStringResId(poiTypeId, R.string.poi_list_header_shows);
				}

			case DINING:
				// Explore by activity - dining headers
				if (PointOfInterest.isSubType(poiSubTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_FINE_DINING)) {
					return R.string.poi_list_header_dining_fine;
				}
				else if (PointOfInterest.isSubType(poiSubTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_CASUAL_DINING)) {
					return R.string.poi_list_header_dining_casual;
				}
				else if (PointOfInterest.isSubType(poiSubTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_QUICK_SERVICE)) {
					return R.string.poi_list_header_dining_quick_service;
				}
				else {
					return getPoiHeaderStringResId(poiTypeId, R.string.poi_list_header_dining);
				}

			case EVENT_SERIES_ATTRACTIONS:
				if(poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_GATEWAY){
					return R.string.poi_list_header_gateways;
				}
				else if(poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_GEN_LOCATION) {
					return R.string.poi_list_header_general_locations;
				}

			case UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS:
			case UNIVERSAL_STUDIOS_HOLLYWOOD_DINING:
			case UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING:
			case FAVORITES:
				// Override rides and shows as "attractions" for USH
				if (BuildConfigUtils.isLocationFlavorHollywood()
					&& (poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE
						|| poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW
						|| poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE)) {
					return R.string.poi_list_header_attractions;
				} else {
					return getPoiHeaderStringResId(poiTypeId, R.string.poi_list_header_guest_amenities);
				}
			default:
				// Otherwise, get the header based on the POI type
				return getPoiHeaderStringResId(poiTypeId, R.string.poi_list_header_guest_amenities);
		}
	}

	private static int getPoiHeaderStringResId(int poiTypeId, int fallbackResId) {
		if (sPoiHeaderStringResIdMap == null) {
			sPoiHeaderStringResIdMap = new SparseIntArray();

			sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE, R.string.poi_list_header_rides);
			sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_DINING, R.string.poi_list_header_dining);
			sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW, R.string.poi_list_header_shows);
			sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE, R.string.poi_list_header_shows);
			sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP, R.string.poi_list_header_shopping);
			sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT, R.string.poi_list_header_entertainment);
			sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL, R.string.poi_list_header_sort_alphabetical);
			sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_WATERPARK, R.string.poi_list_header_waterparks);
			sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT, R.string.poi_list_header_events);
		}

		return sPoiHeaderStringResIdMap.get(poiTypeId, fallbackResId);
	}
	
	private static int getPoiHeaderStringResId(ExploreType exploreType, int poiTypeId, 
	        long poiSubTypeFlags, FilterSort filterSort, Cursor cursor) {
	    
	    if(filterSort == null) {
	        return getPoiHeaderStringResId(exploreType, poiTypeId, poiSubTypeFlags);
	    }
	    
	    switch (filterSort) {
	        case ALPHABETICAL:
	            return R.string.poi_list_header_sort_alphabetical;
	        case WAIT_TIMES:
	        {
	            int waitTime = cursor.getInt(cursor.getColumnIndex(PointsOfInterestTable.COL_WAIT_TIME));
	            // Ride is closed or wait time is unknown
	            if(waitTime < 0) {
	                switch (waitTime) {
	                    case Ride.RIDE_WAIT_TIME_STATUS_NOT_AVAILABLE:
                            return R.string.poi_list_header_sort_wait_times_unknown;
                        case Ride.RIDE_WAIT_TIME_STATUS_CLOSED_FOR_CAPACITY:
                            return R.string.poi_list_header_sort_wait_times_capacity;
                        case Ride.RIDE_WAIT_TIME_STATUS_CLOSED_INCLEMENT_WEATHER:
                        case Ride.RIDE_WAIT_TIME_STATUS_CLOSED_LONG_TERM:
                        case Ride.RIDE_WAIT_TIME_STATUS_CLOSED_TEMP:
                        default:
                            return R.string.poi_list_header_sort_wait_times_closed;
                    }
	            } 
	            // Loop through known wait times looking for a match
	            else {
	                for(WaitTimes waitTimes : WaitTimes.values()) {
	                    if(waitTime <= waitTimes.getValue()) {
	                        return waitTimes.getStringId();
	                    }
	                }
	                // Wait time not covered somehow
	                if(BuildConfig.DEBUG) {
	                    Log.e(TAG, "getPoiHeaderStringResId: wait time missed by enum waitTime = " + waitTime);
	                }
	                return R.string.poi_list_header_sort_wait_times_unknown;
	            }
	        }
	        case DISTANCE:
	        {
	            Location userLocation = new Location(LocationManager.GPS_PROVIDER);
	            Location poiLocation = new Location(LocationManager.GPS_PROVIDER);
	            userLocation.setLatitude(
	                    cursor.getDouble(cursor.getColumnIndex(UniversalOrlandoDatabaseTables.COL_USER_LATITUDE)));
	            userLocation.setLongitude(
                        cursor.getDouble(cursor.getColumnIndex(UniversalOrlandoDatabaseTables.COL_USER_LONGITUDE)));
	            poiLocation.setLatitude(
                        cursor.getDouble(cursor.getColumnIndex(PointsOfInterestTable.COL_LATITUDE)));
	            poiLocation.setLongitude(
                        cursor.getDouble(cursor.getColumnIndex(PointsOfInterestTable.COL_LONGITUDE)));
	            double distance = Math.abs(userLocation.distanceTo(poiLocation)) * METER_IN_FEET;
	            for(Distances distances : Distances.values()) {
	                if(distance <= distances.getValue()) {
	                    return distances.getStringId();
	                }
	            }
	            return Distances.VERY_FAR.getStringId();
	        }
	        case SHOW_TIMES:
	        {
	            String showTimeString = cursor.getString(cursor.getColumnIndex(ShowTimesTable.COL_SHOW_TIME));
	            if(showTimeString != null && showTimeString.matches(PoiUtils.REGEX_SHOW_TIME)) {
	                // Calculate minutes before next show time
	                int minutesBeforeShow = PoiUtils.getMinutesBeforeShowTime(showTimeString);
	                // Loop through known show times looking for a match
	                for(ShowTimes showTimes : ShowTimes.values()) {
	                    if(minutesBeforeShow > 0
	                            && minutesBeforeShow <= showTimes.getValue()) {
	                        if(BuildConfig.DEBUG) {
	                            Log.v(TAG, "getPoiHeaderStringResId: minutesBeforeShow = " + minutesBeforeShow
	                                    + " showTimes.value = " + showTimes.getValue());
	                        }
	                        return showTimes.getStringId();
	                    }
	                }
	            }
	            // POI has no show times or none remaining today, this includes all-day shows
	            else {
	                return R.string.poi_list_header_sort_show_times_unavailable;
	            }
	        }
            case TYPE:
            default:
                return getPoiHeaderStringResId(exploreType, poiTypeId, poiSubTypeFlags);
        }
	}
	
	@Override
	public Cursor swapCursor(Cursor newCursor) {
	    // Update sort when cursor is swapped
	    getFilterSortFromCursor(newCursor);
	    
	    return super.swapCursor(newCursor);
	}
	
	/**
	 * If the sort column is present set the FilterSort, other wise set the FilterSort to null
	 * @param cursor
	 */
	private void getFilterSortFromCursor(Cursor cursor) {
	    if(cursor != null && cursor.moveToFirst()) {
	        int index = cursor.getColumnIndex(UniversalOrlandoDatabaseTables.COL_FILTER_SORT);
	        if(index != -1) {
	            int sortOrdinal = cursor.getInt(index);
	            mFilterSort = (sortOrdinal < FilterSort.values().length) ? FilterSort.values()[sortOrdinal] : null;
	            if(BuildConfig.DEBUG) {
	                Log.v(TAG, "getFilterSortFromCursor: mFilterSort = " + mFilterSort);
	            }
	        }
	    }
	}
	
	private static class HeaderViewHolder {
		TextView headerTitleText;
	}
}
