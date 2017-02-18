/**
 *
 */
package com.universalstudios.orlandoresort.controller.userinterface.home;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crittercism.app.Crittercism;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.LoaderUtils;
import com.universalstudios.orlandoresort.controller.userinterface.detail.DetailUtils;
import com.universalstudios.orlandoresort.controller.userinterface.events.EventSeriesDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.explore.PoiViewHolder;
import com.universalstudios.orlandoresort.controller.userinterface.home.FeaturedItem.Type;
import com.universalstudios.orlandoresort.controller.userinterface.image.PicassoProvider;
import com.universalstudios.orlandoresort.controller.userinterface.news.NewsDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.pois.PoiUtils;
import com.universalstudios.orlandoresort.model.network.domain.events.EventSeries;
import com.universalstudios.orlandoresort.model.network.domain.news.News;
import com.universalstudios.orlandoresort.model.network.domain.offers.Offer;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Ride;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Show;
import com.universalstudios.orlandoresort.model.network.domain.venue.Venue;
import com.universalstudios.orlandoresort.model.network.image.ImageUtils;
import com.universalstudios.orlandoresort.model.network.request.RequestQueryParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventSeriesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.NewsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OffersTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenueLandsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 *
 * @author Steven Byle
 */
public class FeaturedItemFragment extends DatabaseQueryFragment implements OnClickListener {
	private static final String TAG = FeaturedItemFragment.class.getSimpleName();

	private static final String KEY_LOADER_ARG_DATABASE_QUERY_JSON = "KEY_LOADER_ARG_DATABASE_QUERY_JSON";
	private static final String KEY_ARG_FEATURED_ITEM_ID = "KEY_ARG_FEATURED_ITEM_ID";
	private static final String KEY_ARG_FEATURED_ITEM_TYPE = "KEY_ARG_FEATURED_ITEM_TYPE";

	// The loader's unique id. Loader ids are specific to the Activity or
	// Fragment in which they reside.
	private static final int LOADER_ID_PARK_NEWS_ITEM = LoaderUtils.LOADER_ID_FEATURED_ITEM_FRAGMENT;
	private static final int LOADER_ID_EVENT_SERIES_ITEM = LoaderUtils.LOADER_ID_FEATURED_ITEM_FRAGMENT_2;
	private static final int LOADER_ID_OFFER_ITEM = LoaderUtils.LOADER_ID_FEATURED_ITEM_FRAGMENT_3;

	private String mImageSizeParam;
	private PicassoProvider mParentPicassoProvider;
	private TextView mVenueNameText;
	private TextView mPoiNameText;
	private ImageView mDetailImage;
	private ImageView mDetailImageNoImage;
	private View mImageSelector;
	private String mVenueObjectJson;
	private String mPoiObjectJson;
	private PointOfInterest mPoi;
	private Venue mVenue;
	private Integer mPoiTypeId;
	private PoiViewHolder mPoiViewHolder;
	private FeaturedItem mFeaturedItem;
	private Long mFeaturedItemId;
	private News mNews;
	private Offer mOffer;
	private EventSeries mEventSeries;
	private String mEventSeriesObjectJson;

	public static FeaturedItemFragment newInstance(FeaturedItem featuredItem) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: featuredItem = " + featuredItem.getId());
		}

		// Create a new fragment instance
		FeaturedItemFragment fragment = new FeaturedItemFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		// Add parameters to the argument bundle
		DatabaseQuery databaseQuery = DatabaseQueryUtils.getDetailDatabaseQuery(featuredItem.getId());
		args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
		args.putLong(KEY_ARG_FEATURED_ITEM_ID, featuredItem.getId());
		args.putSerializable(KEY_ARG_FEATURED_ITEM_TYPE, featuredItem.getType());

		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Check if parent fragment (if there is one) implements the interface
		Fragment parentFragment = getParentFragment();
		if (parentFragment != null && parentFragment instanceof PicassoProvider) {
			mParentPicassoProvider = (PicassoProvider) parentFragment;
		}
		// Otherwise, check if parent activity implements the interface
		else if (activity != null && activity instanceof PicassoProvider) {
			mParentPicassoProvider = (PicassoProvider) activity;
		}
		// If neither implements the image selection callback, warn that
		// selections are being missed
		else if (mParentPicassoProvider == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement PicassoProvider");
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Default parameters
		Bundle args = getArguments();
		if (args == null) {
			mFeaturedItemId = null;
			mFeaturedItem = null;
		}
		// Otherwise, set incoming parameters
		else {
			mFeaturedItemId = args.getLong(KEY_ARG_FEATURED_ITEM_ID);
			mFeaturedItem = new FeaturedItem(mFeaturedItemId, (FeaturedItem.Type)args.getSerializable(KEY_ARG_FEATURED_ITEM_TYPE));
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
		}

		mImageSizeParam = ImageUtils.getPoiImageSizeString(getResources().getInteger(R.integer.poi_detail_image_dpi_shift));
		mPoi = null;
		mVenue = null;
		mNews = null;
		mOffer = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		View fragmentView = inflater.inflate(R.layout.fragment_featured_poi, container, false);

		// Setup Views
		mDetailImage = (ImageView) fragmentView.findViewById(R.id.fragment_featured_poi_image);
		mDetailImageNoImage = (ImageView) fragmentView.findViewById(R.id.fragment_featured_poi_image_no_image_logo);
		mVenueNameText = (TextView) fragmentView.findViewById(R.id.fragment_featured_poi_venue_name_text);
		mPoiNameText = (TextView) fragmentView.findViewById(R.id.fragment_featured_poi_poi_name_text);
		mImageSelector = fragmentView.findViewById(R.id.fragment_featured_poi_image_selector);

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

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise, restore state
		else {
		}

		// Assume there is no image and no text
		mDetailImage.setVisibility(View.GONE);
		mDetailImageNoImage.setVisibility(View.VISIBLE);
		mVenueNameText.setVisibility(View.GONE);
		mPoiNameText.setVisibility(View.GONE);
		mPoiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);

		// Assume the POI can't be clicked until it loads
		mImageSelector.setOnClickListener(this);
		mImageSelector.setClickable(false);

		return fragmentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
	}

	@Override
	public void onActivityCreated( Bundle savedInstanceState ) {
		super.onActivityCreated(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onActivityCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Create loader to look for a park news item with the same ID
		if (mFeaturedItem != null) {
			Bundle loaderArgs = new Bundle();
			DatabaseQuery databaseQuery;
			if (mFeaturedItem.getType().equals(FeaturedItem.Type.NEWS)) {
				databaseQuery = DatabaseQueryUtils.getNewsDatabaseQuery(mFeaturedItemId);
				loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
				LoaderUtils.initFragmentLoaderWithHandler(this, savedInstanceState, LOADER_ID_PARK_NEWS_ITEM, loaderArgs);
			} else if (mFeaturedItem.getType().equals(FeaturedItem.Type.POI)) {
				// Don't worry about this, it was already requested.
			} else if (mFeaturedItem.getType().equals(FeaturedItem.Type.EVENT_SERIES)) {
				databaseQuery = DatabaseQueryUtils.getEventSeriesDetailDatabaseQuery(mFeaturedItemId);
				loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
				LoaderUtils.initFragmentLoaderWithHandler(this, savedInstanceState, LOADER_ID_EVENT_SERIES_ITEM, loaderArgs);
			} else if (mFeaturedItem.getType().equals(Type.OFFER)) {
				List<Long> offerIds = new ArrayList<>();
				offerIds.add(mFeaturedItemId);
				databaseQuery = DatabaseQueryUtils.getOffersById(offerIds);
				loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
				LoaderUtils.initFragmentLoaderWithHandler(this, savedInstanceState, LOADER_ID_OFFER_ITEM, loaderArgs);
			} else {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "onActivityCreated: could not get correct databaseQuery!");
				}
			}
		}
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewStateRestored: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStart");
		}
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
	}

	@Override
	public void onStop() {
		super.onStop();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStop");
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroyView");
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroy");
		}
	}

	@Override
	public void onClick(View v) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onClick");
		}

		switch (v.getId()) {
			case R.id.fragment_featured_poi_image_selector:
				if (mPoiObjectJson != null && mPoiTypeId != null && mVenueObjectJson != null) {
					DetailUtils.openDetailPage(v.getContext(), mVenueObjectJson, mPoiObjectJson, mPoiTypeId, true, null);
				}
				else if (mNews != null) {
					Activity parentActivity = getActivity();
					Long newsId = mNews.getId();
					if (parentActivity != null && newsId != null) {
						parentActivity.startActivity(new Intent(parentActivity, NewsDetailActivity.class)
								.putExtras(NewsDetailActivity.newInstanceBundle(newsId)));
					}
				}
				else if (mOffer != null) {
					Activity parentActivity = getActivity();
					Long offerId = mOffer.getId();
					if (parentActivity != null && offerId != null) {
						DetailUtils.openOfferDetailPage(parentActivity, offerId);
					}
				}
				else if (mEventSeries != null && mEventSeriesObjectJson != null) {
					Activity parentActivity = getActivity();
					if (parentActivity != null) {
						Bundle eventSeriesDetailBundle = EventSeriesDetailActivity.newInstanceBundle(mEventSeriesObjectJson);
						if (mEventSeriesObjectJson != null) {
							parentActivity.startActivity(
									new Intent(parentActivity, EventSeriesDetailActivity.class).putExtras(eventSeriesDetailBundle));
							if (BuildConfig.DEBUG) {
								Log.v(TAG, "openEventSeriesDetailPage: loading event series detail page");
							}
						}
					}
				}
				break;
			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onClick: unknown view clicked");
				}
				break;
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreateLoader: id = " + id);
		}

		switch (id) {
			case LOADER_ID_PARK_NEWS_ITEM:
			case LOADER_ID_EVENT_SERIES_ITEM:
			case LOADER_ID_OFFER_ITEM:
				String databaseQueryJson = args.getString(KEY_LOADER_ARG_DATABASE_QUERY_JSON);
				DatabaseQuery databaseQuery = GsonObject.fromJson(databaseQueryJson, DatabaseQuery.class);
				return LoaderUtils.createCursorLoader(databaseQuery);
			default:
				// Otherwise, let the parent class handle it
				return super.onCreateLoader(id, args);
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoadFinished");
		}

		switch (loader.getId()) {
			case LOADER_ID_EVENT_SERIES_ITEM:
				// Pull out data from the news item
				if (data != null && data.moveToFirst()) {
					mEventSeriesObjectJson = data.getString(data.getColumnIndex(EventSeriesTable.COL_EVENT_SERIES_OBJECT_JSON));
					mVenueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
					mEventSeries = GsonObject.fromJson(mEventSeriesObjectJson, EventSeries.class);
					mVenue = GsonObject.fromJson(mVenueObjectJson, Venue.class);

					String eventSeriesName = mEventSeries != null ? mEventSeries.getDisplayName() : "";
					String venueName = mVenue != null ? mVenue.getDisplayName() : "";

					// Update circle badge
					mPoiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);

					// Get the image URL
					List<String> imageUrls = new ArrayList<String>();
					if (mEventSeries != null && mEventSeries.getDetailImageUrls() != null && !mEventSeries.getDetailImageUrls()
							.isEmpty()) {
						imageUrls.addAll(mEventSeries.getDetailImageUrls());

						// Bind the data to the views
						bindViews(venueName, eventSeriesName, imageUrls);

						// Allow the POI to be clicked only if it has all the data
						mImageSelector.setClickable(mEventSeries != null);
					}
				}
				break;
			case LOADER_ID_OFFER_ITEM:
				// Pull out data from the offer item
				if (data != null && data.moveToFirst()) {
					String offerObjectJson = data.getString(data.getColumnIndex(OffersTable.COL_OFFER_OBJECT_JSON));
					mOffer = GsonObject.fromJson(offerObjectJson, Offer.class);
					mVenueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
					mVenue = GsonObject.fromJson(mVenueObjectJson, Venue.class);

					// Update circle badge
					mPoiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);

					String poiName = mOffer.getDisplayName();
					String venueName = mVenue != null ? mVenue.getDisplayName() : "";

					// Bind the data to the views
					bindViews(poiName, venueName, mOffer.getDetailImageUrls());

					// Allow the POI to be clicked only if it has all the data
					mImageSelector.setClickable(mOffer != null);
				}
				break;
			case LOADER_ID_PARK_NEWS_ITEM:
				// Pull out data from the news item
				if (data != null && data.moveToFirst()) {
					String newObjectJson = data.getString(data.getColumnIndex(NewsTable.COL_NEWS_OBJECT_JSON));
					mNews = GsonObject.fromJson(newObjectJson, News.class);

					// Update circle badge
					mPoiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);

					// Get the image URL
					List<String> imageUrls = new ArrayList<String>();
					imageUrls.add(mNews.getDetailImageUrl());

					// Bind the data to the views
					bindViews(null, mNews.getMessageHeading(), imageUrls);

					// Allow the POI to be clicked only if it has all the data
					mImageSelector.setClickable(mNews != null);
				}
				break;
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
					mPoiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
					mPoiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
					mVenueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
					long venueId = data.getLong(data.getColumnIndex(VenuesTable.COL_VENUE_ID));
					String venueLandName = data.getString(data.getColumnIndex(VenueLandsTable.COL_ALIAS_DISPLAY_NAME));

					mPoi = PointOfInterest.fromJson(mPoiObjectJson, mPoiTypeId);
					mVenue = GsonObject.fromJson(mVenueObjectJson, Venue.class);

					String poiName = mPoi.getDisplayName();
					String venueName = mVenue != null ? mVenue.getDisplayName() : "";

					// Special header for hotels and water parks
					if (mPoiTypeId != null) {
						if (mPoiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL) {
							venueName = getString(R.string.poi_list_item_venue_name_hotel);
						}
						else if (mPoiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_WATERPARK) {
							venueName = getString(R.string.poi_list_item_venue_name_waterpark);
						}
					}

					// Special header format for Universal Studios Hollywood
					if (venueId == VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD) {
						if (!TextUtils.isEmpty(venueName) && !TextUtils.isEmpty(venueLandName)) {
							venueName = mVenueNameText.getContext().getString(R.string.poi_venue_land_label_format, venueName, venueLandName);
						}
					}

					// Update circle badge
					updateCircleBadgeViews(mPoi, mVenue);

					// Bind the data to the views
					bindViews(venueName, poiName, mPoi.getDetailImageUrls());

					// Allow the POI to be clicked only if it has all the data
					mImageSelector.setClickable(mPoiObjectJson != null && mPoiTypeId != null && mVenueObjectJson != null);
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
			case LOADER_ID_PARK_NEWS_ITEM:
				// Data is not available anymore, delete reference
				break;
			case LOADER_ID_DATABASE_QUERY:
				// Data is not available anymore, delete reference
				break;
			default:
				break;
		}
	}

	private void bindViews(String titleText, String bodyText, List<String> imageUrls) {
		// Bind the POI data
		mVenueNameText.setText(titleText != null ? titleText.toUpperCase(Locale.US) : "");
		mVenueNameText.setVisibility(titleText != null ?  View.VISIBLE : View.GONE);
		mPoiNameText.setText(bodyText != null ? bodyText : "");
		mPoiNameText.setVisibility(bodyText != null ? View.VISIBLE : View.GONE);

		// Set content descriptions for ADA
		mVenueNameText.setContentDescription(titleText != null ? titleText : "");
		mPoiNameText.setContentDescription(bodyText != null ? bodyText : "");

		int circleBadgeVisibility = mPoiViewHolder.circleBadgeRootLayout.getVisibility();

		// Try to find all valid image URIs
		List<Uri> validImageUris = new ArrayList<Uri>();
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

		// Try to get the first valid detail image URI, and then load it
		Uri imageUriToLoad = validImageUris.size() > 0 ? validImageUris.get(0) : null;
		if (imageUriToLoad != null && mParentPicassoProvider != null) {

			imageUriToLoad = imageUriToLoad.buildUpon()
					.appendQueryParameter(RequestQueryParams.Keys.IMAGE_SIZE, mImageSizeParam)
					.build();

			if (BuildConfig.DEBUG) {
				Log.d(TAG, "onLoadFinished: imageUriToLoad = " + imageUriToLoad);
			}

			Picasso picasso = mParentPicassoProvider.providePicasso();
			if (picasso != null) {
				// Hide the text and badge until the image loads
				mVenueNameText.setVisibility(View.GONE);
				mPoiNameText.setVisibility(View.GONE);
				mPoiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);

				picasso.load(imageUriToLoad).into(mDetailImage, new DetailImageCallback(mDetailImage, mDetailImageNoImage,
						mVenueNameText, mPoiNameText, mPoiViewHolder.circleBadgeRootLayout, circleBadgeVisibility));
			}
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
		else {
			mPoiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);
		}
	}

	// Private static class using weak references to prevent leaking a context
	private static final class DetailImageCallback implements Callback {
		private final WeakReference<ImageView> mImage;
		private final WeakReference<ImageView> mNoImage;
		private final WeakReference<TextView> mVenueNameText;
		private final WeakReference<TextView> mPoiNameText;
		private final WeakReference<View> mCircleBadgeView;
		private final int mCircleBadgeVisibility;

		public DetailImageCallback(ImageView image, ImageView noImage, TextView venueNameText,
								   TextView poiNameText, View circleBadgeView, int circleBadgeVisibility) {
			mImage = new WeakReference<ImageView>(image);
			mNoImage = new WeakReference<ImageView>(noImage);
			mVenueNameText = new WeakReference<TextView>(venueNameText);
			mPoiNameText = new WeakReference<TextView>(poiNameText);
			mCircleBadgeView = new WeakReference<View>(circleBadgeView);
			mCircleBadgeVisibility = circleBadgeVisibility;
		}

		@Override
		public void onSuccess() {
			ImageView image = mImage.get();
			ImageView noImage = mNoImage.get();

			if (image != null) {
				image.setVisibility(View.VISIBLE);
			}
			if (noImage != null) {
				noImage.setVisibility(View.GONE);
			}

			// Update text and circle badge
			updateTextAndCircleBadge();
		}

		@Override
		public void onError() {
			ImageView image = mImage.get();
			ImageView noImage = mNoImage.get();

			if (image != null) {
				image.setVisibility(View.GONE);
			}
			if (noImage != null) {
				noImage.setVisibility(View.VISIBLE);
			}

			// Update text and circle badge
			updateTextAndCircleBadge();
		}

		public void updateTextAndCircleBadge() {
			TextView venueNameText = mVenueNameText.get();
			TextView poiNameText = mPoiNameText.get();
			View circleBadgeView = mCircleBadgeView.get();

			// Update text
			if (venueNameText != null) {
				venueNameText.setVisibility(venueNameText.getText().length() > 0 ? View.VISIBLE : View.GONE);
			}
			if (poiNameText != null) {
				poiNameText.setVisibility(poiNameText.getText().length() > 0 ? View.VISIBLE : View.GONE);
			}
			if (circleBadgeView != null) {
				circleBadgeView.setVisibility(mCircleBadgeVisibility);
			}
		}
	}
}
