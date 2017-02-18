/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.explore.map;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crittercism.app.Crittercism;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.detail.DetailUtils;
import com.universalstudios.orlandoresort.controller.userinterface.events.EventUtils;
import com.universalstudios.orlandoresort.controller.userinterface.explore.PoiViewHolder;
import com.universalstudios.orlandoresort.controller.userinterface.pois.PoiUtils;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Dining;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Event;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Gateway;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Lockers;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.RentalServices;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Ride;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Shop;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Show;
import com.universalstudios.orlandoresort.model.network.domain.venue.Venue;
import com.universalstudios.orlandoresort.model.network.domain.venue.VenueLand;
import com.universalstudios.orlandoresort.model.network.image.ImageUtils;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.request.RequestQueryParams;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;
import com.universalstudios.orlandoresort.view.buttons.FavoriteToggleButton;
import com.universalstudios.orlandoresort.view.map.MapWrapperLayout;

import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * 
 * 
 * @author Steven Byle
 */
public class ExploreMapInfoWindowAdapter implements InfoWindowAdapter {
	private static final String TAG = ExploreMapInfoWindowAdapter.class.getSimpleName();

	private Activity mActivity;
	private MapWrapperLayout mMapWrapperLayout;
	private final View mInfoWindow;
	private final View mInfoWindowBackgroundLayout;
	private final PoiViewHolder mPoiViewHolder;
	private final OnInfoWindowTouchListener mOnInfoWindowGuideMeTouchListener;
	private final OnInfoWindowTouchListener mOnInfoWindowFavoriteTouchListener;
	private final OnInfoWindowTouchListener mOnInfoWindowTouchListener;
	private OnInfoWindowChildViewClickListener mOnInfoWindowChildViewClickListener;
	private SparseArray<PointOfInterestMarker> mPoiMarkerSparseArray;
	private final String mImageSizeParam;
	private final UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
	private final Picasso mPicasso;
	private String mLatestListImageUrl;
	private boolean mGetInfoWindowHasReturned;

	public ExploreMapInfoWindowAdapter(Activity activity, MapWrapperLayout mapWrapperLayout,
			SparseArray<PointOfInterestMarker> poiMarkerSparseArray,
			OnInfoWindowChildViewClickListener onInfoWindowChildViewClickListener) {

		mActivity = activity;
		Context appContext = mActivity.getApplicationContext();
		mMapWrapperLayout = mapWrapperLayout;
		mPoiMarkerSparseArray = poiMarkerSparseArray;
		mOnInfoWindowChildViewClickListener = onInfoWindowChildViewClickListener;

		// Create the image downloader to get the images
		mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
				CacheUtils.POI_IMAGE_DISK_CACHE_NAME,
				CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
				CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);
		mImageSizeParam = ImageUtils.getPoiImageSizeString(appContext.getResources().getInteger(R.integer.poi_list_image_dpi_shift));

		mPicasso = new Picasso.Builder(appContext)
		.loggingEnabled(UniversalOrlandoImageDownloader.SHOW_DEBUG)
		.downloader(mUniversalOrlandoImageDownloader)
		.build();
		setLatestListImageUrl("");
		mGetInfoWindowHasReturned = false;

		LayoutInflater inflater = LayoutInflater.from(appContext);
		mInfoWindow = inflater.inflate(R.layout.map_poi_window, null, false);
		mInfoWindowBackgroundLayout = mInfoWindow.findViewById(R.id.map_poi_window_layout);

		mPoiViewHolder = new PoiViewHolder(mInfoWindow);
		mPoiViewHolder.rootLayout = (ViewGroup) mInfoWindow.findViewById(R.id.map_poi_window_root_container);
		mPoiViewHolder.listImage = (ImageView) mInfoWindow.findViewById(R.id.map_poi_window_image);
		mPoiViewHolder.listImageNoImage = (ImageView) mInfoWindow.findViewById(R.id.map_poi_window_no_image_logo);
		mPoiViewHolder.displayNameText = (TextView) mInfoWindow.findViewById(R.id.map_poi_window_display_name_text);
		mPoiViewHolder.venueNameText = (TextView) mInfoWindow.findViewById(R.id.map_poi_window_venue_name_text);
		mPoiViewHolder.extraInfoText = (TextView) mInfoWindow.findViewById(R.id.map_poi_window_extra_info_text);
		mPoiViewHolder.guideMeLayout = (LinearLayout) mInfoWindow.findViewById(R.id.map_poi_window_guide_me_button_layout);
		mPoiViewHolder.guideMeDivider = mInfoWindow.findViewById(R.id.map_poi_window_guide_me_button_divider);
		mPoiViewHolder.favoriteToggleButton = (FavoriteToggleButton) mInfoWindow.findViewById(R.id.map_poi_window_favorite_toggle_button);
		mPoiViewHolder.circleBadgeRootLayout = (LinearLayout) mInfoWindow.findViewById(R.id.poi_item_circle_badge_root_container);
		mPoiViewHolder.waitTimeLayout = (LinearLayout) mInfoWindow.findViewById(R.id.poi_item_circle_badge_wait_time_layout);
		mPoiViewHolder.waitTimeMinNumText = (TextView) mInfoWindow.findViewById(R.id.poi_item_circle_badge_wait_time_num_text);
		mPoiViewHolder.showTimeLayout = (FrameLayout) mInfoWindow.findViewById(R.id.poi_item_circle_badge_show_time_layout);
		mPoiViewHolder.showTimeBackgroundGray = mInfoWindow.findViewById(R.id.poi_item_circle_badge_show_time_background_gray);
		mPoiViewHolder.showTimeBackgroundBlue = mInfoWindow.findViewById(R.id.poi_item_circle_badge_show_time_background_blue);
		mPoiViewHolder.showTimeStartsTimeText = (TextView) mInfoWindow.findViewById(R.id.poi_item_circle_badge_show_time_starts_time_text);
		mPoiViewHolder.showTimeStartsText = (TextView) mInfoWindow.findViewById(R.id.poi_item_circle_badge_show_time_starts_text);
		mPoiViewHolder.showTimeOpensText = (TextView) mInfoWindow.findViewById(R.id.poi_item_circle_badge_show_time_opens_text);
		mPoiViewHolder.showTimeAmPmText = (TextView) mInfoWindow.findViewById(R.id.poi_item_circle_badge_show_time_am_pm_text);
		mPoiViewHolder.closedLayout = (LinearLayout) mInfoWindow.findViewById(R.id.poi_item_circle_badge_closed_layout);
		mPoiViewHolder.closedText = (TextView) mInfoWindow.findViewById(R.id.poi_item_circle_badge_closed_text);
		mPoiViewHolder.closedWeatherText = (TextView) mInfoWindow.findViewById(R.id.poi_item_circle_badge_weather_text);
		mPoiViewHolder.closedTemporaryText = (TextView) mInfoWindow.findViewById(R.id.poi_item_circle_badge_temporary_text);
		mPoiViewHolder.closedCapacityText = (TextView) mInfoWindow.findViewById(R.id.poi_item_circle_badge_closed_capacity_text);

		// onTouch listener for guide me button
		mOnInfoWindowGuideMeTouchListener = new OnInfoWindowTouchListener(mPoiViewHolder.guideMeLayout, mOnInfoWindowChildViewClickListener);
		mPoiViewHolder.guideMeLayout.setOnTouchListener(mOnInfoWindowGuideMeTouchListener);

		// onTouch listener for favorite button
		mOnInfoWindowFavoriteTouchListener = new OnInfoWindowTouchListener(mPoiViewHolder.favoriteToggleButton, mOnInfoWindowChildViewClickListener);
		mPoiViewHolder.favoriteToggleButton.setOnTouchListener(mOnInfoWindowFavoriteTouchListener);

		// onTouch listener for info window itself
		mOnInfoWindowTouchListener = new OnInfoWindowTouchListener(mPoiViewHolder.rootLayout, mOnInfoWindowChildViewClickListener);
		mPoiViewHolder.rootLayout.setOnTouchListener(mOnInfoWindowTouchListener);
	}

	@Override
	public View getInfoWindow(final Marker marker) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getInfoWindow: marker.getSnippet() = " + marker.getSnippet());
		}

		mGetInfoWindowHasReturned = false;

		// Default the window state in case there is an issue loading
		mPoiViewHolder.displayNameText.setText("");
		mPoiViewHolder.venueNameText.setText("");
		mPoiViewHolder.listImage.setVisibility(View.GONE);
		mPoiViewHolder.listImageNoImage.setVisibility(View.VISIBLE);
		mPoiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);
		mPoiViewHolder.guideMeDivider.setVisibility(View.GONE);
		mPoiViewHolder.guideMeLayout.setVisibility(View.GONE);
		mPoiViewHolder.favoriteToggleButton.setVisibility(View.GONE);
		mPoiViewHolder.extraInfoText.setVisibility(View.GONE);

		// Hash code of the POI is passed in the marker snippet
		int poiHashCode = -1;
		try {
			poiHashCode = Integer.parseInt(marker.getSnippet());
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "getInfoWindow: exception trying to get info window for marker, poi hash code = " + marker.getSnippet());
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}

		if (mPoiMarkerSparseArray == null) {
			mGetInfoWindowHasReturned = true;
			return mInfoWindow;
		}

		// Pull out the POI marker from the map
		PointOfInterestMarker poiMarker = mPoiMarkerSparseArray.get(poiHashCode);
		if (poiMarker == null) {
			mGetInfoWindowHasReturned = true;
			return mInfoWindow;
		}

		// Pull out the POI to bind to the info window
		PointOfInterest poi = poiMarker.getPointOfInterest();
		Venue venue = poiMarker.getVenue();
		if (poi == null || venue == null) {
			mGetInfoWindowHasReturned = true;
			return mInfoWindow;
		}

		String displayName = poi.getDisplayName();
		String venueName = venue.getDisplayName();
		Integer poiTypeId = poiMarker.getPoiTypeId();

		// Enable clicking only if the POI has a detail page
		mInfoWindowBackgroundLayout.setBackgroundResource(DetailUtils.hasDetailPage(poiTypeId) ?
				R.drawable.state_list_explore_map_poi_background : R.drawable.map_poi_window_background);

		Context appContext = mMapWrapperLayout.getContext().getApplicationContext();
		if (appContext == null) {
			mGetInfoWindowHasReturned = true;
			return mInfoWindow;
		}

		// Special header for hotels and water parks
		if (poiTypeId != null) {
			if (poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL) {
				venueName = appContext.getString(R.string.poi_list_item_venue_name_hotel);
			}
			else if (poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_WATERPARK) {
				venueName = appContext.getString(R.string.poi_list_item_venue_name_waterpark);
			}
		}

		// Special header format for Universal Studios Hollywood
		VenueLand venueLand = poiMarker.getVenueLand();
		Long venueId = venue.getId();
		if (venueId != null && venueLand != null && venueId == VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD) {
			String venueLandName = venueLand.getDisplayName();
			if (!TextUtils.isEmpty(venueName) && !TextUtils.isEmpty(venueLandName)) {
				if (mActivity != null) {
					venueName = mActivity.getString(R.string.poi_venue_land_label_format, venueName, venueLandName);
				}
			}
		}

		// Bind the POI to the info window
		mPoiViewHolder.displayNameText.setText(displayName != null ? displayName : "");
		mPoiViewHolder.venueNameText.setText(venueName != null ? venueName.toUpperCase(Locale.US) : "");
		mPoiViewHolder.venueNameText.setVisibility(venueName != null ? View.VISIBLE : View.GONE);

		// Assume there is no image to start
		mPoiViewHolder.listImage.setVisibility(View.GONE);
		mPoiViewHolder.listImageNoImage.setVisibility(View.VISIBLE);

		// Load the map image
		String listImageUrl = poi.getListImageUrl();
		if (listImageUrl != null && !listImageUrl.isEmpty()) {
			Uri listImageUri = null;
			try {
				listImageUri = Uri.parse(listImageUrl);
			}
			catch (Exception e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "getInfoWindow: invalid image URL: " + listImageUri, e);
				}

				// Log the exception to crittercism
				Crittercism.logHandledException(e);
			}

			if (listImageUri != null) {
				Uri imageUriToLoad = listImageUri.buildUpon()
						.appendQueryParameter(RequestQueryParams.Keys.IMAGE_SIZE, mImageSizeParam)
						.build();

				if (BuildConfig.DEBUG) {
					Log.d(TAG, "getInfoWindow: imageUriToLoad = " + imageUriToLoad);
				}

				mPicasso.load(imageUriToLoad).into(mPoiViewHolder.listImage, new MapImageCallback(
						listImageUrl, marker, mPoiViewHolder, this, mActivity));
			}
		}

		// Show / hide the guide me button
		boolean isInPark = UniversalAppStateManager.getInstance().isInResortGeofence();
		boolean isRoutable = poi.getIsRoutable() != null ? poi.getIsRoutable() : false;
		PoiUtils.updateGuideMeButton(isInPark, isRoutable, mPoiViewHolder);

		// Update the wait time / show time badge
		if (poi instanceof Ride) {
			Ride ride = (Ride) poi;
			PoiUtils.updatePoiCircleBadgeForRide(ride.getWaitTime(), ride.getOpensAt(), venue.getHours(), mPoiViewHolder);
		}
		// Covers shows and parades (sub class of shows)
		else if (poi instanceof Show) {
			Show show = (Show) poi;

			// First try to show a wait time, if it exists
			PoiUtils.updatePoiCircleBadgeForRide(show.getWaitTime(), null, venue.getHours(), mPoiViewHolder);

			// If the badge was hidden, try showing a show time instead
			if (mPoiViewHolder.circleBadgeRootLayout.getVisibility() == View.GONE) {
				PoiUtils.updatePoiCircleBadgeForShow(show.getStartTimes(), mPoiViewHolder, appContext);
			}
		}
		else if (poi instanceof Event) {
		    Event event = (Event) poi;
		    if (event.getWaitTime() == null) {
		        EventUtils.updatePoiCircleBadgeForEvent(event.getEventDates(), mPoiViewHolder, false);
		    } else {
		        PoiUtils.updatePoiCircleBadgeForRide(event.getWaitTime(), null, null, mPoiViewHolder);
		    }
		    
		    // Hide circle badge if event has passed
		    if (mPoiViewHolder.showTimeBackgroundGray.getVisibility() == View.VISIBLE) {
				//TODO this functionality was removed in order to show event wait times
				//with no event dates. we need to revisit event logic
		        //mPoiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);
		    }
		}
		else {
			mPoiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);
		}

		// Update extra line for certain POI types
		int extraInfoStringResId = -1;
		String extraInfoString = null;

		if (poi instanceof Dining) {
			Dining dining = (Dining) poi;
			Boolean hasHealthyOptions = dining.getHasHealthyOptions();
			if (hasHealthyOptions != null && hasHealthyOptions.booleanValue()) {
				extraInfoStringResId = R.string.poi_list_item_dining_has_healthy_options_info;
			}
		}
		else if (poi instanceof Lockers) {
			Lockers lockers = (Lockers) poi;
			if (lockers.getIsFree()) {
				extraInfoStringResId = R.string.poi_list_item_lockers_ride_info;
			}
		}
		else if (poi instanceof Shop) {
			Shop shop = (Shop) poi;
			Boolean sellsExpressPass = shop.getSellsExpressPass();
			Boolean hasPackagePickup = shop.getHasPackagePickup();
			if (sellsExpressPass != null && sellsExpressPass.booleanValue()) {
				extraInfoStringResId = R.string.poi_list_item_shop_sells_express_pass_info;
			} else if (hasPackagePickup != null && hasPackagePickup.booleanValue()) {
				extraInfoStringResId = R.string.poi_list_item_shop_has_package_pickup_info;
			}
		}

		//Subtitle for rentals
		else if (poi instanceof RentalServices) {
			extraInfoStringResId = R.string.advanced_filter_rental_type_extra_info;
		}
		else if (poi instanceof Gateway) {
			Gateway gateway = (Gateway) poi;
			extraInfoString = gateway.getGatewayType();
		}

		// Set the extra text, or hide it
		if (extraInfoStringResId != -1 || extraInfoString != null) {
			if(extraInfoStringResId != -1) {
				mPoiViewHolder.extraInfoText.setText(extraInfoStringResId);
			} else{
				mPoiViewHolder.extraInfoText.setText(extraInfoString);
			}
			mPoiViewHolder.extraInfoText.setVisibility(View.VISIBLE);
		}
		else {
			mPoiViewHolder.extraInfoText.setVisibility(View.GONE);
		}

		if (poiTypeId != null && PoiUtils.isFavoriteEnabled(poiTypeId)) {
			boolean isFavorite = poi.getIsFavorite() != null && poi.getIsFavorite().booleanValue();
			mPoiViewHolder.favoriteToggleButton.setChecked(isFavorite);
			mPoiViewHolder.favoriteToggleButton.setVisibility(View.VISIBLE);
		}
		else {
			mPoiViewHolder.favoriteToggleButton.setVisibility(View.GONE);
		}

		// Tie together touch locations and markers
		mOnInfoWindowGuideMeTouchListener.setMarker(marker);
		mOnInfoWindowFavoriteTouchListener.setMarker(marker);
		mOnInfoWindowTouchListener.setMarker(marker);

		if (mMapWrapperLayout == null) {
			mGetInfoWindowHasReturned = true;
			return mInfoWindow;
		}

		// We must call this to set the current marker and infoWindow references
		// to the MapWrapperLayout
		mMapWrapperLayout.setMarkerWithInfoWindow(marker, mInfoWindow);

		mGetInfoWindowHasReturned = true;
		return mInfoWindow;
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	public void destroy() {
		if (mPicasso != null) {
			mPicasso.shutdown();
		}
		if (mUniversalOrlandoImageDownloader != null) {
			mUniversalOrlandoImageDownloader.destroy();
		}

		mActivity = null;
		mMapWrapperLayout = null;
		mPoiMarkerSparseArray = null;
		mOnInfoWindowChildViewClickListener = null;
	}

	private synchronized String getLatestListImageUrl() {
		return mLatestListImageUrl;
	}

	private synchronized void setLatestListImageUrl(String latestListImageUrl) {
		mLatestListImageUrl = latestListImageUrl;
	}

	// Private class using weak references to prevent leaking a context
	private static final class MapImageCallback implements Callback {
		private final String mlistImageUrl;
		private final WeakReference<Marker> mMarker;
		private final WeakReference<PoiViewHolder> mPoiViewHolder;
		private final WeakReference<ExploreMapInfoWindowAdapter> mExploreMapInfoWindowAdapter;
		private final WeakReference<Activity> mActivity;

		public MapImageCallback(String listImageUrl, Marker marker, PoiViewHolder poiViewHolder,
				ExploreMapInfoWindowAdapter exploreMapInfoWindowAdapter, Activity activity) {
			mlistImageUrl = listImageUrl;
			mMarker = new WeakReference<Marker>(marker);
			mPoiViewHolder = new WeakReference<PoiViewHolder>(poiViewHolder);
			mExploreMapInfoWindowAdapter = new WeakReference<ExploreMapInfoWindowAdapter>(exploreMapInfoWindowAdapter);
			mActivity = new WeakReference<Activity>(activity);
		}

		@Override
		public void onSuccess() {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "onSuccess: map image load success");
			}

			PoiViewHolder holder = mPoiViewHolder.get();
			if (holder != null) {
				if (holder.listImage != null) {
					holder.listImage.setVisibility(View.VISIBLE);
				}
				if (holder.listImageNoImage != null) {
					holder.listImageNoImage.setVisibility(View.GONE);
				}

				final Marker marker = mMarker.get();
				final ExploreMapInfoWindowAdapter exploreMapInfoWindowAdapter = mExploreMapInfoWindowAdapter.get();
				final Activity activity = mActivity.get();

				// Check to see the references are still alive
				if (marker != null && exploreMapInfoWindowAdapter != null && activity != null) {

					// Refresh the info window only if the image wasn't loaded last refresh (prevent infinite recursion)
					if (!mlistImageUrl.equals(exploreMapInfoWindowAdapter.getLatestListImageUrl())
							&& exploreMapInfoWindowAdapter.mGetInfoWindowHasReturned) {

						MarkerInfoWindowRefreshThread thread = new MarkerInfoWindowRefreshThread(mlistImageUrl,
								marker, exploreMapInfoWindowAdapter, activity);
						thread.start();
					}
				}
			}
		}

		@Override
		public void onError() {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "onError: map image load error");
			}

			PoiViewHolder holder = mPoiViewHolder.get();
			if (holder != null) {
				if (holder.listImage != null) {
					holder.listImage.setVisibility(View.GONE);
				}
				if (holder.listImageNoImage != null) {
					holder.listImageNoImage.setVisibility(View.VISIBLE);
				}

				// Leave the image unloaded, don't refresh the window
			}
		}
	}

	private static final class MarkerInfoWindowRefreshThread extends Thread {
		private final String mlistImageUrl;
		private final WeakReference<Marker> mMarker;
		private final WeakReference<ExploreMapInfoWindowAdapter> mExploreMapInfoWindowAdapter;
		private final WeakReference<Activity> mActivity;

		public MarkerInfoWindowRefreshThread(String listImageUrl, Marker marker,
				ExploreMapInfoWindowAdapter exploreMapInfoWindowAdapter, Activity activity) {
			mlistImageUrl = listImageUrl;
			mMarker = new WeakReference<Marker>(marker);
			mExploreMapInfoWindowAdapter = new WeakReference<ExploreMapInfoWindowAdapter>(exploreMapInfoWindowAdapter);
			mActivity = new WeakReference<Activity>(activity);
		}

		@Override
		public void run() {
			// Wait for the pin to center
			try {
				Thread.sleep(ExploreMapFragment.MAP_CENTER_ANIMATION_DURATION_IN_MS * 3 / 5);
			}
			catch (InterruptedException e) {
			}

			final Marker marker = mMarker.get();
			final ExploreMapInfoWindowAdapter exploreMapInfoWindowAdapter = mExploreMapInfoWindowAdapter.get();
			final Activity activity = mActivity.get();

			// Check to see the references are still alive
			if (marker != null && exploreMapInfoWindowAdapter != null && activity != null) {

				// Then refresh the info window
				MarkerInfoWindowRefreshRunnable runnable = new MarkerInfoWindowRefreshRunnable(mlistImageUrl,
						marker, exploreMapInfoWindowAdapter);
				activity.runOnUiThread(runnable);
			}
		}
	}

	private static final class MarkerInfoWindowRefreshRunnable implements Runnable {
		private final String mlistImageUrl;
		private final WeakReference<Marker> mMarker;
		private final WeakReference<ExploreMapInfoWindowAdapter> mExploreMapInfoWindowAdapter;

		public MarkerInfoWindowRefreshRunnable(String listImageUrl, Marker marker,
				ExploreMapInfoWindowAdapter exploreMapInfoWindowAdapter) {
			mlistImageUrl = listImageUrl;
			mMarker = new WeakReference<Marker>(marker);
			mExploreMapInfoWindowAdapter = new WeakReference<ExploreMapInfoWindowAdapter>(exploreMapInfoWindowAdapter);
		}

		@Override
		public void run() {
			final Marker marker = mMarker.get();
			final ExploreMapInfoWindowAdapter exploreMapInfoWindowAdapter = mExploreMapInfoWindowAdapter.get();

			// Check to see the references are still alive, and the marker is still open
			if (marker != null && marker.isInfoWindowShown() && exploreMapInfoWindowAdapter != null) {
				exploreMapInfoWindowAdapter.setLatestListImageUrl(mlistImageUrl);
				marker.showInfoWindow();
			}
		}
	}
}
