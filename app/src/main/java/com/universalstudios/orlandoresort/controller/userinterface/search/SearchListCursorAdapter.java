package com.universalstudios.orlandoresort.controller.userinterface.search;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crittercism.app.Crittercism;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.image.ImageUtils;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.request.RequestQueryParams;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenueLandsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.view.stickylistheaders.StickyListHeadersAdapter;

import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * 
 * 
 * @author Steven Byle
 */
public class SearchListCursorAdapter extends CursorAdapter implements StickyListHeadersAdapter {
	private static final String TAG = SearchListCursorAdapter.class.getSimpleName();

	private static SparseIntArray sPoiHeaderStringResIdMap;
	private final String mImageSizeParam;
	private final UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
	private final Picasso mPicasso;

	public SearchListCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor, 0);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "SearchListCursorAdapter constructor");
		}

		// Create the image downloader to get the images
		mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
				CacheUtils.POI_IMAGE_DISK_CACHE_NAME,
				CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
				CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);
		mImageSizeParam = ImageUtils.getPoiImageSizeString(context.getResources().getInteger(R.integer.poi_search_image_dpi_shift));

		mPicasso = new Picasso.Builder(context)
		.loggingEnabled(UniversalOrlandoImageDownloader.SHOW_DEBUG)
		.downloader(mUniversalOrlandoImageDownloader)
		.build();
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View row = inflater.inflate(R.layout.list_poi_search_item, parent, false);

		SearchRowViewHolder holder = new SearchRowViewHolder();
		holder.displayNameText = (TextView) row.findViewById(R.id.list_poi_search_item_display_name_text);
		holder.venueNameText = (TextView) row.findViewById(R.id.list_poi_search_item_venue_name_text);
		holder.thumbnailImage = (ImageView) row.findViewById(R.id.list_poi_search_item_image);
		holder.thumbnailImageNoImage = (ImageView) row.findViewById(R.id.list_poi_search_item_no_image_logo);

		row.setTag(holder);

		return row;
	}

	@Override
	public void bindView(View row, final Context context, Cursor cursor) {

		// Get data from database row
		int poiTypeId = cursor.getInt(cursor.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
		String displayName = cursor.getString(cursor.getColumnIndex(PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME));
		String thumbnailImageUrl = cursor.getString(cursor.getColumnIndex(PointsOfInterestTable.COL_THUMBNAIL_IMAGE_URL));
		String venueName = cursor.getString(cursor.getColumnIndex(VenuesTable.COL_ALIAS_DISPLAY_NAME));
		long venueId = cursor.getLong(cursor.getColumnIndex(VenuesTable.COL_VENUE_ID));
		String venueLandName = cursor.getString(cursor.getColumnIndex(VenueLandsTable.COL_ALIAS_DISPLAY_NAME));

		// Special header for hotels and water parks
		if (poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL) {
			venueName = context.getString(R.string.poi_list_item_venue_name_hotel);
		} else if (poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_WATERPARK) {
			venueName = context.getString(R.string.poi_list_item_venue_name_waterpark);
		}

		// Special header format for Universal Studios Hollywood
		if (venueId == VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD) {
			if (!TextUtils.isEmpty(venueName) && !TextUtils.isEmpty(venueLandName)) {
				venueName = context.getString(R.string.poi_venue_land_label_format, venueName, venueLandName);
			}
		}

		// Apply data to view
		final SearchRowViewHolder holder = (SearchRowViewHolder) row.getTag();

		// Set the text
		holder.displayNameText.setText(displayName != null ? displayName : "");
		holder.venueNameText.setText(venueName != null ? venueName.toUpperCase(Locale.US) : "");

		// Assume there is no image to start
		holder.thumbnailImage.setVisibility(View.GONE);
		holder.thumbnailImageNoImage.setVisibility(View.VISIBLE);

		// Load the search image
		if (thumbnailImageUrl != null && !thumbnailImageUrl.isEmpty()) {

			Uri thumbnailImageUri = null;
			try {
				thumbnailImageUri = Uri.parse(thumbnailImageUrl);
			}
			catch (Exception e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "bindView: invalid image URL: " + thumbnailImageUri, e);
				}

				// Log the exception to crittercism
				Crittercism.logHandledException(e);
			}

			if (thumbnailImageUri != null) {
				Uri imageUriToLoad = thumbnailImageUri.buildUpon()
						.appendQueryParameter(RequestQueryParams.Keys.IMAGE_SIZE, mImageSizeParam)
						.build();

				if (BuildConfig.DEBUG) {
					Log.d(TAG, "bindView: imageUriToLoad = " + imageUriToLoad);
				}

				mPicasso.load(imageUriToLoad).into(holder.thumbnailImage, new SearchImageCallback(holder));
			}
		}
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

		// Get the proper header title, fall back to amenities if there isn't a specific one
		int poiHeaderStringResId = getPoiHeaderStringResId(poiTypeId);
		holder.headerTitleText.setText(poiHeaderStringResId);

		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		Cursor cursor = (Cursor) getItem(position);
		Integer poiTypeId = cursor.getInt(cursor.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));

		// Get the proper header title, fall back to amenities if there isn't a specific one
		return getPoiHeaderStringResId(poiTypeId);
	}

	public void destroy() {
		if (mUniversalOrlandoImageDownloader != null) {
			mUniversalOrlandoImageDownloader.destroy();
		}
		if (mPicasso != null) {
			mPicasso.shutdown();
		}
	}

	private static int getPoiHeaderStringResId(int poiTypeId) {
		if (sPoiHeaderStringResIdMap == null) {
			sPoiHeaderStringResIdMap = new SparseIntArray();

			// Hollywood groups rides/shows as attractions
			if (BuildConfigUtils.isLocationFlavorHollywood()) {
				sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE, R.string.poi_list_header_attractions);
				sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW, R.string.poi_list_header_attractions);
				sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE, R.string.poi_list_header_attractions);
			}
			// Orlando uses standard dividers
			else {
				sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE, R.string.poi_list_header_rides);
				sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW, R.string.poi_list_header_shows);
				sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE, R.string.poi_list_header_shows);
			}
			sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_DINING, R.string.poi_list_header_dining);
			sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP, R.string.poi_list_header_shopping);
			sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT, R.string.poi_list_header_entertainment);
			sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL, R.string.poi_list_header_hotels);
			sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_WATERPARK, R.string.poi_list_header_waterparks);
			sPoiHeaderStringResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT, R.string.poi_list_header_events);
		}

		return sPoiHeaderStringResIdMap.get(poiTypeId, R.string.poi_list_header_guest_amenities);
	}

	private static class HeaderViewHolder {
		TextView headerTitleText;
	}

	private static class SearchRowViewHolder {
		TextView displayNameText;
		TextView venueNameText;
		ImageView thumbnailImage;
		ImageView thumbnailImageNoImage;
	}

	// Private static class using weak references to prevent leaking a context
	private static class SearchImageCallback implements Callback {
		private final WeakReference<SearchRowViewHolder> mSearchRowViewHolder;

		public SearchImageCallback(SearchRowViewHolder searchRowViewHolder) {
			mSearchRowViewHolder = new WeakReference<SearchRowViewHolder>(searchRowViewHolder);
		}

		@Override
		public void onSuccess() {
			SearchRowViewHolder holder = mSearchRowViewHolder.get();
			if (holder != null) {
				if (holder.thumbnailImage != null) {
					holder.thumbnailImage.setVisibility(View.VISIBLE);
				}
				if (holder.thumbnailImageNoImage != null) {
					holder.thumbnailImageNoImage.setVisibility(View.GONE);
				}
			}
		}

		@Override
		public void onError() {
			SearchRowViewHolder holder = mSearchRowViewHolder.get();
			if (holder != null) {
				if (holder.thumbnailImage != null) {
					holder.thumbnailImage.setVisibility(View.GONE);
				}
				if (holder.thumbnailImageNoImage != null) {
					holder.thumbnailImageNoImage.setVisibility(View.VISIBLE);
				}
			}
		}
	}


}
