/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.crittercism.app.Crittercism;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.events.EventUtils;
import com.universalstudios.orlandoresort.controller.userinterface.explore.PoiViewHolder;
import com.universalstudios.orlandoresort.controller.userinterface.image.ImagePagerUtils;
import com.universalstudios.orlandoresort.controller.userinterface.image.ImagePagerUtils.PagerDotColor;
import com.universalstudios.orlandoresort.controller.userinterface.image.PicassoProvider;
import com.universalstudios.orlandoresort.controller.userinterface.pois.PoiUtils;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Event;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Ride;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Show;
import com.universalstudios.orlandoresort.model.network.domain.venue.Venue;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenueLandsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.view.buttons.FavoriteToggleButton;
import com.universalstudios.orlandoresort.view.fonts.TextView;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager.TransitionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 
 * 
 * @author Steven Byle
 */
public class ImagesAndTitleDetailFragment extends DatabaseQueryFragment implements OnPageChangeListener, PicassoProvider, OnCheckedChangeListener {
	private static final String TAG = ImagesAndTitleDetailFragment.class.getSimpleName();

	private static final String KEY_STATE_CUR_VIEWPAGER_TAB = "KEY_STATE_CUR_VIEWPAGER_TAB";

	private int mCurrentViewPagerTab;
	private int mCalculatedImageHeightDp;
	private TextView mVenueNameText;
	private TextView mPoiNameText;
	private TextView mPoiDescText;
	private RelativeLayout mViewPagerContainer;
	private JazzyViewPager mViewPager;
	private LinearLayout mPagerDotContainer;
	private View mBottomGradient;
	private FavoriteToggleButton mFavoriteToggleButton;
	private PointOfInterest mPoi;
	private Venue mVenue;
	private ImageDetailFragmentPagerAdapter mImageDetailFragmentPagerAdapter;
	private UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
	private Picasso mPicasso;
	private PoiViewHolder mPoiViewHolder;
	private boolean mHasLoadedData;

	public static ImagesAndTitleDetailFragment newInstance(DatabaseQuery databaseQuery) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: databaseQuery = " + databaseQuery);
		}

		// Create a new fragment instance
		ImagesAndTitleDetailFragment fragment = new ImagesAndTitleDetailFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		if (databaseQuery != null) {
			args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
		}
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Get the smallest (portrait) width in dp
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		float widthDp = displayMetrics.widthPixels / displayMetrics.density;
		float heightDp = displayMetrics.heightPixels / displayMetrics.density;
		float smallestWidthDp = Math.min(widthDp, heightDp);

		// Compute the height based on image aspect ratio 1080x760 @ 480dpi
		mCalculatedImageHeightDp = (int) Math.round(smallestWidthDp * (760.0 / 1080.0));

		// Default POI state
		mPoi = null;
		mVenue = null;
		mHasLoadedData = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_detail_images_and_title, container, false);

		// Setup Views
		mViewPagerContainer = (RelativeLayout) fragmentView.findViewById(R.id.fragment_detail_images_and_title_viewpager_container);
		mViewPager = (JazzyViewPager) fragmentView.findViewById(R.id.fragment_detail_images_and_title_viewpager);
		mPagerDotContainer = (LinearLayout) fragmentView.findViewById(R.id.fragment_detail_images_and_title_dot_layout);
		mBottomGradient = fragmentView.findViewById(R.id.fragment_detail_images_and_title_bottom_gradient);
		mVenueNameText = (TextView) fragmentView.findViewById(R.id.fragment_detail_images_and_title_venue_name_text);
		mPoiNameText = (TextView) fragmentView.findViewById(R.id.fragment_detail_images_and_title_poi_name_text);
		mPoiDescText = (TextView) fragmentView.findViewById(R.id.fragment_detail_images_and_title_poi_desc_text);
		mFavoriteToggleButton = (FavoriteToggleButton) fragmentView.findViewById(R.id.fragment_detail_images_and_title_favorite_toggle_button);

		LinearLayout circleBadgeRootLayout = (LinearLayout) fragmentView.findViewById(R.id.poi_detail_circle_badge_root_container);
		mPoiViewHolder = new PoiViewHolder(circleBadgeRootLayout);
		mPoiViewHolder.circleBadgeRootLayout = circleBadgeRootLayout;
		mPoiViewHolder.waitTimeLayout = (LinearLayout) fragmentView.findViewById(R.id.poi_detail_circle_badge_wait_time_layout);
		mPoiViewHolder.waitTimeMinNumText = (TextView) fragmentView.findViewById(R.id.poi_detail_circle_badge_wait_time_num_text);
		mPoiViewHolder.showTimeLayout = (FrameLayout) fragmentView.findViewById(R.id.poi_detail_circle_badge_show_time_layout);
		mPoiViewHolder.showTimeBackgroundGray = fragmentView.findViewById(R.id.poi_detail_circle_badge_show_time_background_gray);
		mPoiViewHolder.showTimeBackgroundBlue = fragmentView.findViewById(R.id.poi_detail_circle_badge_show_time_background_blue);
		mPoiViewHolder.showTimeStartsTimeText = (TextView) fragmentView.findViewById(R.id.poi_detail_circle_badge_show_time_starts_time_text);
		mPoiViewHolder.showTimeStartsText = (TextView) fragmentView.findViewById(R.id.poi_detail_circle_badge_show_time_starts_text);
		mPoiViewHolder.showTimeOpensText = (TextView) fragmentView.findViewById(R.id.poi_detail_circle_badge_show_time_opens_text);
		mPoiViewHolder.showTimeAmPmText = (TextView) fragmentView.findViewById(R.id.poi_detail_circle_badge_show_time_am_pm_text);
		mPoiViewHolder.closedLayout = (LinearLayout) fragmentView.findViewById(R.id.poi_detail_circle_badge_closed_layout);
		mPoiViewHolder.closedText = (TextView) fragmentView.findViewById(R.id.poi_detail_circle_badge_closed_text);
		mPoiViewHolder.closedWeatherText = (TextView) fragmentView.findViewById(R.id.poi_detail_circle_badge_weather_text);
		mPoiViewHolder.closedTemporaryText = (TextView) fragmentView.findViewById(R.id.poi_detail_circle_badge_temporary_text);
		mPoiViewHolder.closedCapacityText = (TextView) fragmentView.findViewById(R.id.poi_detail_circle_badge_closed_capacity_text);

		mVenueNameText.setVisibility(View.INVISIBLE);
		mPoiNameText.setVisibility(View.INVISIBLE);
		mPoiDescText.setVisibility(View.INVISIBLE);
		mPagerDotContainer.setVisibility(View.GONE);
		mBottomGradient.setVisibility(View.GONE);
		mPoiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);
		mFavoriteToggleButton.setVisibility(View.GONE);

		mFavoriteToggleButton.setOnCheckedChangeListener(this);

		// Set pager height based on detail image aspect ratio
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int calculatedImageHeightPx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				mCalculatedImageHeightDp, displayMetrics));
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViewPagerContainer.getLayoutParams();
		layoutParams.width = LayoutParams.MATCH_PARENT;
		layoutParams.height = calculatedImageHeightPx;
		mViewPagerContainer.setLayoutParams(layoutParams);

		mViewPager.setOnPageChangeListener(this);
		mViewPager.setTransitionEffect(TransitionEffect.Standard);
		mViewPager.setFadeEnabled(false);

		// Create the image downloader to get the images
		mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
				CacheUtils.POI_IMAGE_DISK_CACHE_NAME,
				CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
				CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);

		mPicasso = new Picasso.Builder(getActivity())
		.loggingEnabled(UniversalOrlandoImageDownloader.SHOW_DEBUG)
		.downloader(mUniversalOrlandoImageDownloader)
		.build();

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			mCurrentViewPagerTab = mViewPager.getCurrentItem();
		}
		// Otherwise, restore state
		else {
			mCurrentViewPagerTab = savedInstanceState.getInt(KEY_STATE_CUR_VIEWPAGER_TAB);
		}

		return fragmentView;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onResume");
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onPause");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onSaveInstanceState");
		}
		outState.putInt(KEY_STATE_CUR_VIEWPAGER_TAB, mCurrentViewPagerTab);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroyView");
		}

		if (mImageDetailFragmentPagerAdapter != null) {
			mImageDetailFragmentPagerAdapter.destroy();
		}
		if (mPicasso != null) {
			mPicasso.shutdown();
		}
		if (mUniversalOrlandoImageDownloader != null) {
			mUniversalOrlandoImageDownloader.destroy();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCheckedChanged: isChecked = " + isChecked);
		}

		switch (buttonView.getId()) {
			case R.id.fragment_detail_images_and_title_favorite_toggle_button:

				if (mPoi != null) {
					// Update the favorite state off the main thread
					PoiUtils.updatePoiIsFavoriteInDatabase(
							getActivity(), mPoi, isChecked, true);
				}
				break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

	@Override
	public void onPageSelected(int position) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onPageSelected: " + position);
		}

		mCurrentViewPagerTab = position;

		// Set the proper dot on, and the others off
		int pageCount = mPagerDotContainer.getChildCount();
		if (pageCount > 1) {
			for (int i = 0; i < pageCount; i++) {
				View pagerDot = mPagerDotContainer.getChildAt(i);
				if (pagerDot != null) {
					pagerDot.setBackgroundResource(ImagePagerUtils.getPagerDotResId(i == mCurrentViewPagerTab, PagerDotColor.WHITE));
				}
			}
		}
	}

	@Override
	public Picasso providePicasso() {
		return mPicasso;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoadFinished");
		}

		switch (loader.getId()) {
			case LOADER_ID_DATABASE_QUERY:
				// Let the parent class check for double loads
				if (checkIfDoubleOnLoadFinished()) {
					if (BuildConfig.DEBUG) {
						Log.i(TAG, "onLoadFinished: ignoring update due to double load");
					}
					return;
				}

				// Pull out data from the POI
				if (data != null && data.moveToFirst()) {
					String poiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
					Integer poiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
					String venueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
					boolean isFavorite = (data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_IS_FAVORITE)) != 0);
					long venueId = data.getLong(data.getColumnIndex(VenuesTable.COL_VENUE_ID));
					String venueLandName = data.getString(data.getColumnIndex(VenueLandsTable.COL_ALIAS_DISPLAY_NAME));

					mPoi = PointOfInterest.fromJson(poiObjectJson, poiTypeId);
					mVenue = GsonObject.fromJson(venueObjectJson, Venue.class);
					String poiName = mPoi.getDisplayName();
					String poiDesc = mPoi.getLongDescription();
					String venueName = mVenue != null ? mVenue.getDisplayName() : "";

					// If there is no long description, get the short description
					if (poiDesc == null || poiDesc.isEmpty()) {
						poiDesc = mPoi.getShortDescription();
					}

					// Special header for hotels and water parks
					if (poiTypeId != null) {
						if (poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL) {
							venueName = getString(R.string.poi_list_item_venue_name_hotel);
						}
						else if (poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_WATERPARK) {
							venueName = getString(R.string.poi_list_item_venue_name_waterpark);
						}
					}

					// Special header format for Universal Studios Hollywood
					if (venueId == VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD) {
						if (!TextUtils.isEmpty(venueName) && !TextUtils.isEmpty(venueLandName)) {
							venueName = mVenueNameText.getContext().getString(R.string.poi_venue_land_label_format, venueName, venueLandName);
						}
					}

					mVenueNameText.setText(venueName != null ? venueName.toUpperCase(Locale.US) : "");
					mVenueNameText.setVisibility(venueName != null ? View.VISIBLE : View.GONE);
					mPoiNameText.setText(poiName != null ? poiName : "");
					mPoiNameText.setVisibility(poiName != null ? View.VISIBLE : View.GONE);
					mPoiDescText.setText(poiDesc != null ? poiDesc : "");
					mPoiDescText.setVisibility(poiDesc != null ? View.VISIBLE : View.GONE);


					if (poiTypeId != null && PoiUtils.isFavoriteEnabled(poiTypeId)) {
						mFavoriteToggleButton.setChecked(isFavorite);
						mFavoriteToggleButton.setVisibility(View.VISIBLE);
					}
					else {
						mFavoriteToggleButton.setVisibility(View.GONE);
					}

					// Set the content description
					mFavoriteToggleButton.setContentDescriptionByPoiName(poiName != null ? poiName : "");

					// Update circle badge
					updateCircleBadgeViews(mPoi, mVenue);

					// Only load new images if the data hasn't been loaded before
					if (!mHasLoadedData) {

						// Try to load valid detail image URIs
						List<Uri> validImageUris = new ArrayList<Uri>();
						List<String> imageUrls = mPoi.getDetailImageUrls();
						if (imageUrls != null) {
							for (String imageUrl : imageUrls) {
								if (imageUrl != null) {
									// Only add URIs that can be parsed
									try {
										Uri imageUri = Uri.parse(imageUrl);
										validImageUris.add(imageUri);
									}
									catch (Exception e) {
										if (BuildConfig.DEBUG) {
											Log.e(TAG, "onLoadFinished: invalid image URL: " + imageUrl, e);
										}

										// Log the exception to crittercism
										Crittercism.logHandledException(e);
									}
								}
							}
						}


						// Create the pager adapter to bind images, if any are there
						List<Uri> imageUrisToLoad = validImageUris.size() > 0 ? validImageUris : createDefaultUriList();

						if(mPoi.getVideoUrl() == null) {
							mImageDetailFragmentPagerAdapter = new ImageDetailFragmentPagerAdapter(mViewPager,
									getChildFragmentManager(), imageUrisToLoad);
						} else {
							mImageDetailFragmentPagerAdapter = new ImageDetailFragmentPagerAdapter(mViewPager,
									getChildFragmentManager(), imageUrisToLoad, mPoi.getVideoImageUrl(), mPoi.getVideoUrl());
						}


						mViewPager.setAdapter(mImageDetailFragmentPagerAdapter);

						// Clear out the dot pager indicator and add new ones
						mPagerDotContainer.removeAllViews();
						int pageCount = mImageDetailFragmentPagerAdapter.getCount();
						if (pageCount > 1) {
							for (int i = 0; i < pageCount; i++) {
								View pagerDot = ImagePagerUtils.createPagerDotView(mPagerDotContainer, i == mCurrentViewPagerTab, PagerDotColor.WHITE);
								mPagerDotContainer.addView(pagerDot);
							}
							mPagerDotContainer.setVisibility(View.VISIBLE);
							mBottomGradient.setVisibility(View.VISIBLE);
						}
						else {
							mPagerDotContainer.setVisibility(View.GONE);
							mBottomGradient.setVisibility(View.GONE);
						}
					}

					// Track that the data has been loaded once
					mHasLoadedData = true;
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoaderReset");
		}

		switch (loader.getId()) {
			case LOADER_ID_DATABASE_QUERY:
				// Data is not available anymore, delete reference
				mHasLoadedData = false;
				break;
			default:
				break;
		}
	}

	private void updateCircleBadgeViews(PointOfInterest poi, Venue venue) {
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
				PoiUtils.updatePoiCircleBadgeForShow(show.getStartTimes(), mPoiViewHolder, getActivity());
			}
		}
		// Update the event time
		else if(poi instanceof Event) {
		    Event event = (Event) poi;
		    if (event.getWaitTime() == null) {
		        EventUtils.updatePoiCircleBadgeForEvent(event.getEventDates(), mPoiViewHolder, false);
		    } else {
		        PoiUtils.updatePoiCircleBadgeForRide(event.getWaitTime(), null, null, mPoiViewHolder);
		    }
		    // Hide badge if event passed
		    if (mPoiViewHolder.showTimeLayout.getVisibility() == View.VISIBLE) {
		        mPoiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);
		    }
		}
		else {
			mPoiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);
		}
	}

	private List<Uri> createDefaultUriList() {
		List<Uri> defaultUriList = new ArrayList<Uri>();
		defaultUriList.add(null);
		return defaultUriList;
	}
}
