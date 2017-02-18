/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.web.WebViewActivity;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Lockers;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.RentalServices;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
public class LockerPricingDetailFragment extends DatabaseQueryFragment {
	private static final String TAG = LockerPricingDetailFragment.class.getSimpleName();
	private static final String KEY_ARG_DETAIL_TYPE = "DETAIL_TYPE";

	private View mFragmentView;
	private TextView mPricingText;
	private TextView mPricingTextVisitLocation;
	private TextView mTermsConditionText;
	private LinearLayout mTermsConditionsLayout;
	private LinearLayout mPricingRootLayout;
	private String rentalTermsCondition = "rentalTermsCondition";

	public static LockerPricingDetailFragment newInstance(DatabaseQuery databaseQuery) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: databaseQuery = " + databaseQuery);
		}
		// Create a new fragment instance
		LockerPricingDetailFragment fragment = new LockerPricingDetailFragment();

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
		mFragmentView = inflater.inflate(R.layout.fragment_detail_locker_pricing, container, false);

		// Setup Views
		mPricingText = (TextView) mFragmentView.findViewById(R.id.fragment_detail_locker_pricing_text);
		mPricingRootLayout = (LinearLayout) mFragmentView.findViewById(R.id.fragment_detail_list_pricing_root);
		mTermsConditionText = (TextView) mFragmentView.findViewById(R.id.fragment_detail_term_condition_text);
		mTermsConditionsLayout = (LinearLayout) mFragmentView.findViewById(R.id.rental_terms_conditions_layout);
		mPricingTextVisitLocation = (TextView) mFragmentView.findViewById(R.id.fragment_detail_locker_pricing_text_visit_location);

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

				// Read the single POI
				boolean hasPrices = false;
				boolean hasTermsAndConditions = false;

				if (data != null && data.moveToFirst()) {
					Integer poiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
					String poiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
					PointOfInterest poi = PointOfInterest.fromJson(poiObjectJson, poiTypeId);

					if (poi != null && poi instanceof Lockers) {
						StringBuilder lockerPricesStringBuilder = new StringBuilder();
						List<String> lockerPrices = ((Lockers) poi).getLockerPrices();
						if (lockerPrices != null) {
							for (String lockerPrice : lockerPrices) {
								if (lockerPrice != null && !lockerPrice.isEmpty()) {
									lockerPricesStringBuilder.append(lockerPrice).append("\n");
								}
							}
						}

						if (lockerPricesStringBuilder.length() > 0) {
							hasPrices = true;
							mPricingText.setText(lockerPricesStringBuilder.toString().trim());
						}
					} else if (poi != null && poi instanceof RentalServices) {
						StringBuilder rentalPricesStringBuilder = new StringBuilder();
						List<String> rentalPrices = ((RentalServices) poi).getRentalPrices();
						final String termsAndConditionsUrl = ((RentalServices) poi).getTermsConditions();
						if (rentalPrices != null && !rentalPrices.isEmpty()) {
							for (String rentalPrice : rentalPrices) {
								rentalPricesStringBuilder.append(rentalPrice).append("\n");
							}
						} else {
							mPricingTextVisitLocation.setText(R.string.detail_rental_visit_for_pricing);
						}
						if (rentalPricesStringBuilder.length() > 0) {
							hasPrices = true;
							mPricingText.setText(rentalPricesStringBuilder.toString().trim());
						}

						if (!TextUtils.isEmpty(termsAndConditionsUrl)) {
							hasTermsAndConditions = true;
							mTermsConditionsLayout.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(termsAndConditionsUrl));
									PackageManager manager = getActivity().getPackageManager();
									List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
									if (infos.size() > 0) {
										startActivity(intent);
									} else {
										Toast.makeText(getActivity(), "No browser app available", Toast.LENGTH_LONG).show();
									}
								}
							});
						}
					}
				}

				// Show the proper text based on data
				mPricingText.setVisibility(hasPrices ? View.VISIBLE : View.GONE);
				mPricingTextVisitLocation.setVisibility(hasPrices ? View.GONE : View.VISIBLE);
				mTermsConditionsLayout.setVisibility(hasTermsAndConditions ? View.VISIBLE : View.GONE);
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
