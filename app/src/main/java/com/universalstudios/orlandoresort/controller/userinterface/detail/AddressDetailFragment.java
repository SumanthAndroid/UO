/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import java.net.URLEncoder;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.venue.StreetAddress;
import com.universalstudios.orlandoresort.model.network.domain.venue.Venue;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.view.fonts.TextView;

/**
 * 
 * 
 * @author Steven Byle
 */
public class AddressDetailFragment extends DatabaseQueryFragment implements OnClickListener, OnLongClickListener {
	private static final String TAG = AddressDetailFragment.class.getSimpleName();

	private String mFormattedAddressWithNewLines, mFormattedAddressWithSpaces;
	private double mLatitude, mLongitude;
	private View mFragmentView;
	private ViewGroup mDirectionsButtonLayout;
	private TextView mVenueNameText, mAddressText;
	private String mPoiName;

	public static AddressDetailFragment newInstance(DatabaseQuery databaseQuery) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: databaseQuery = " + databaseQuery);
		}

		// Create a new fragment instance
		AddressDetailFragment fragment = new AddressDetailFragment();

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
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onAttach");
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
		}
		// Otherwise, set incoming parameters
		else {
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		mFragmentView = inflater.inflate(R.layout.fragment_detail_address, container, false);

		// Setup Views
		mVenueNameText = (TextView) mFragmentView.findViewById(R.id.fragment_detail_address_venue_name_text);
		mAddressText = (TextView) mFragmentView.findViewById(R.id.fragment_detail_address_text);
		mDirectionsButtonLayout = (ViewGroup) mFragmentView.findViewById(R.id.fragment_detail_address_directions_button_layout);

		mDirectionsButtonLayout.setOnClickListener(this);
		mAddressText.setOnLongClickListener(this);

		// Hide the fragment to start, until its data loads
		mFragmentView.setVisibility(View.INVISIBLE);
		return mFragmentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onActivityCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
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
			case R.id.fragment_detail_address_directions_button_layout:

				// View the map, starting from the POI lat/lon, and find the venue address
				String uriString = "geo:" + mLatitude + "," + mLongitude;
				try {
					uriString += "?q=" + URLEncoder.encode(mFormattedAddressWithNewLines, "UTF-8");
					if (BuildConfig.DEBUG) {
						Log.d(TAG, uriString);
					}

					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(uriString));
					if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
						// Tag the event
						AnalyticsUtils.trackEvent(
								mPoiName,
								AnalyticsUtils.EVENT_NAME_GET_RESORT_DIRECTIONS,
								null, null);

						// Start the share activity/picker
						startActivity(intent);
					}
					else {
						// Do nothing, this case should never happen
					}
				}
				catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "onClick: exception trying to get directions");
					}

					// Log the exception to crittercism
					Crittercism.logHandledException(e);
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
	public boolean onLongClick(View v) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLongClick");
		}

		switch (v.getId()) {
			case R.id.fragment_detail_address_text:
				String address = (String) mAddressText.getText();

				// Copy the address to the clipboard
				ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText(getString(R.string.detail_address_clipboard_label), address);
				clipboard.setPrimaryClip(clip);

				UserInterfaceUtils.showToastFromForeground(getString(R.string.detail_address_copied_to_clipboard_toast_message),
						Toast.LENGTH_SHORT, getActivity());

				return true;
			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onClick: unknown view long clicked");
				}
				return false;
		}
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

				boolean hasValidAddress = false;

				// Get venue object
				if (data != null && data.moveToFirst()) {
					String poiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
					Integer poiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
					mPoiName = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME));
					String venueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
					String venueName = data.getString(data.getColumnIndex(VenuesTable.COL_ALIAS_DISPLAY_NAME));

					mVenueNameText.setText(venueName != null ? venueName : "");

					PointOfInterest poi = PointOfInterest.fromJson(poiObjectJson, poiTypeId);
					Venue venue = GsonObject.fromJson(venueObjectJson, Venue.class);
					if (poi != null && venue != null) {
						mLatitude = poi.getLatitude();
						mLongitude = poi.getLongitude();

						StreetAddress streetAddress = venue.getStreetAddress();
						if (streetAddress != null) {
							mFormattedAddressWithNewLines = streetAddress.getFormattedAddress(false);
							mFormattedAddressWithSpaces = streetAddress.getFormattedAddress(true);
							if (mFormattedAddressWithNewLines != null) {
								mAddressText.setText(mFormattedAddressWithNewLines);
								hasValidAddress = true;
							}
						}
					}
				}

				mFragmentView.setVisibility(hasValidAddress ? View.VISIBLE : View.GONE);
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
				break;
			default:
				break;
		}
	}
}
