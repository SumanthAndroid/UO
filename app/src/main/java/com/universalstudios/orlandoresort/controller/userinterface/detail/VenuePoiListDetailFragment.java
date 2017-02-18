/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;

/**
 * 
 * 
 * @author Steven Byle
 */
public class VenuePoiListDetailFragment extends DatabaseQueryFragment implements OnClickListener {
	private static final String TAG = VenuePoiListDetailFragment.class.getSimpleName();

	private static final String VIEW_TAG_WAIT_TIME_ALERT = "VIEW_TAG_WAIT_TIME_ALERT";
	private static final String VIEW_TAG_EXPRESS_PASS = "VIEW_TAG_EXPRESS_PASS";
	private static final String VIEW_TAG_SINGLE_RIDER = "VIEW_TAG_SINGLE_RIDER";
	private static final String VIEW_TAG_CHILD_SWAP = "VIEW_TAG_CHILD_SWAP";
	private static final String VIEW_TAG_DINING_PLAN = "VIEW_TAG_DINING_PLAN";
	private static final String VIEW_TAG_PHONE_NUMBER = "VIEW_TAG_PHONE_NUMBER";
	private static final String VIEW_TAG_VIEW_MENU = "VIEW_TAG_VIEW_MENU";
	private static final String VIEW_TAG_PRICES = "VIEW_TAG_PRICES";
	private static final String VIEW_TAG_PARTY_PASS = "VIEW_TAG_PARTY_PASS";

	private View mFragmentView;
	private LinearLayout mVenuePoiListLayout;

	public static VenuePoiListDetailFragment newInstance(DatabaseQuery databaseQuery) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: databaseQuery = " + databaseQuery);
		}

		// Create a new fragment instance
		VenuePoiListDetailFragment fragment = new VenuePoiListDetailFragment();

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
		mFragmentView = inflater.inflate(R.layout.fragment_detail_venue_poi_list, container, false);

		// Setup Views
		mVenuePoiListLayout = (LinearLayout) mFragmentView.findViewById(R.id.fragment_detail_venue_poi_list_layout);

		// Default to hide
		mFragmentView.setVisibility(View.GONE);

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

		// If a POI is clicked, open its detail page
		Object tag = v.getTag();
		if (tag != null && tag instanceof Bundle) {
			Bundle detailActivityBundle = (Bundle) tag;
			startActivity(new Intent(getActivity(), DetailActivity.class).putExtras(detailActivityBundle));
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

				// Clear the feature options and load new ones
				mVenuePoiListLayout.removeAllViews();

				// Go through each new POI
				if (data != null && data.moveToFirst()) {
					do {
						String poiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
						Integer poiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
						String venueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));

						PointOfInterest poi = PointOfInterest.fromJson(poiObjectJson, poiTypeId);

						if (poi != null) {
							String displayName = poi.getDisplayName();

							Bundle detailActivityBundle = DetailUtils.getDetailPageBundle(venueObjectJson, poiObjectJson, poiTypeId, null);
							if (displayName != null && detailActivityBundle != null) {
								View poiView = createPoiItemView(mVenuePoiListLayout, displayName, mVenuePoiListLayout.getChildCount() > 0, detailActivityBundle);
								mVenuePoiListLayout.addView(poiView);
							}
						}
					} while (data.moveToNext());
				}

				mFragmentView.setVisibility(mVenuePoiListLayout.getChildCount() > 0 ?
						View.VISIBLE : View.GONE);
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

	private View createPoiItemView(ViewGroup parentViewGroup, String primaryTextString, boolean showDivider, Bundle detailActivityBundle) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());

		ViewGroup poiView = (ViewGroup) inflater.inflate(R.layout.list_feature_item, parentViewGroup, false);
		ImageView iconImage = (ImageView) poiView.findViewById(R.id.list_feature_item_icon_image);
		TextView primaryText = (TextView) poiView.findViewById(R.id.list_feature_item_primary_text);
		TextView primarySubText = (TextView) poiView.findViewById(R.id.list_feature_item_primary_sub_text);
		TextView secondaryText = (TextView) poiView.findViewById(R.id.list_feature_item_secondary_text);
		View divider = poiView.findViewById(R.id.list_feature_item_top_divider);

		// Always hide the icon and secondary text
		iconImage.setVisibility(View.GONE);
		secondaryText.setVisibility(View.GONE);

		if (primaryTextString != null) {
			primaryText.setText(primaryTextString);
		}
		primaryText.setVisibility(primaryTextString != null ? View.VISIBLE : View.GONE);
		primarySubText.setVisibility(View.GONE);

		divider.setVisibility(showDivider ? View.VISIBLE : View.GONE);

		// Set the tag to reference back to clicks
		poiView.setClickable(true);
		poiView.setOnClickListener(this);
		poiView.setTag(detailActivityBundle);
		return poiView;
	}
}
