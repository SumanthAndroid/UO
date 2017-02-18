/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.BenefitsInfo;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Hotel;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;

import java.util.Locale;

/**
 * 
 * 
 * @author Steven Byle
 */
public class PerksDetailFragment extends DatabaseQueryFragment {
	private static final String TAG = PerksDetailFragment.class.getSimpleName();

	private View mFragmentView;
	private LinearLayout mPerksLayout;

	public static PerksDetailFragment newInstance(DatabaseQuery databaseQuery) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: databaseQuery = " + databaseQuery);
		}

		// Create a new fragment instance
		PerksDetailFragment fragment = new PerksDetailFragment();

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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		mFragmentView = inflater.inflate(R.layout.fragment_detail_perks, container, false);

		// Setup Views
		mPerksLayout = (LinearLayout) mFragmentView.findViewById(R.id.fragment_detail_perks_list_layout);

		// Default to hide
		mFragmentView.setVisibility(View.GONE);

		return mFragmentView;
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

				// Go through each new POI
				if (data != null && data.moveToFirst()) {
					String poiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
					Integer poiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));

					PointOfInterest poi = PointOfInterest.fromJson(poiObjectJson, poiTypeId);

					// Clear the perks and load new ones
					mPerksLayout.removeAllViews();

					if (poi instanceof Hotel) {
						Hotel hotel = (Hotel) poi;
						// Loop through benefits array and display known perks
						if(hotel.getBenefitsInfo() != null) {
						    for(BenefitsInfo benefitsInfo : hotel.getBenefitsInfo()) {
						        if(Hotel.HOTEL_PERK_EARLY_ADMISSION.equals(benefitsInfo.getBenefitsCategory())) {
						            View perkView = createPerksItemView(mPerksLayout,
		                                    R.drawable.ic_detail_perk_early_admission,
		                                    benefitsInfo.getTitle(),
		                                    benefitsInfo.getText());
		                            mPerksLayout.addView(perkView);
						        } else if(Hotel.HOTEL_PERK_FREE_EXPRESS_PASS.equals(benefitsInfo.getBenefitsCategory())) {
						            View perkView = createPerksItemView(mPerksLayout,
		                                    R.drawable.ic_detail_perk_free_express,
		                                    benefitsInfo.getTitle(),
                                            benefitsInfo.getText());
		                            mPerksLayout.addView(perkView);
                                }
						    }
						}
						// TODO: May remove this if paired with DB update that forces POI update anyway
						// Fall back to original logic if POI sync has not occurred yet
						else {
                            if (hotel.hasPerk(Hotel.HOTEL_PERK_EARLY_ADMISSION)) {
                                View perkView = createPerksItemView(mPerksLayout,
                                        R.drawable.ic_detail_perk_early_admission,
                                        R.string.detail_perks_early_park_admission_title,
                                        R.string.detail_perks_early_park_admission_description);
                                mPerksLayout.addView(perkView);
                            }
                            if (hotel.hasPerk(Hotel.HOTEL_PERK_FREE_EXPRESS_PASS)) {
                                View perkView = createPerksItemView(mPerksLayout,
                                        R.drawable.ic_detail_perk_free_express,
                                        R.string.detail_perks_skip_the_regular_lines_title,
                                        R.string.detail_perks_skip_the_regular_lines_description);
                                mPerksLayout.addView(perkView);
                            }
						}
					}
					mFragmentView.setVisibility(mPerksLayout.getChildCount() > 0 ?
							View.VISIBLE : View.GONE);
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
				break;
			default:
				break;
		}
	}

	private View createPerksItemView(ViewGroup parentViewGroup, Integer iconResId, Integer titleTextResId, Integer descriptionTextResId) {
		Resources r = getResources();

		String primaryText = titleTextResId != null ? r.getString(titleTextResId) : null;
		String secondaryText = descriptionTextResId != null ? r.getString(descriptionTextResId) : null;

		return createPerksItemView(parentViewGroup, iconResId, primaryText, secondaryText);
	}

	private View createPerksItemView(ViewGroup parentViewGroup, Integer iconResId, String titleTextString, String descriptionTextString) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());

		ViewGroup perkView = (ViewGroup) inflater.inflate(R.layout.perk_item, parentViewGroup, false);
		ImageView iconImage = (ImageView) perkView.findViewById(R.id.perk_item_icon);
		TextView titleText = (TextView) perkView.findViewById(R.id.perk_item_title_text);
		TextView descriptionText = (TextView) perkView.findViewById(R.id.perk_item_description_text);

		if (iconResId != null) {
			iconImage.setImageResource(iconResId);
		}
		iconImage.setVisibility(iconResId != null ? View.VISIBLE : View.GONE);

		if (titleTextString != null) {
			titleText.setText(titleTextString.toUpperCase(Locale.US));
		}
		titleText.setVisibility(titleTextString != null ? View.VISIBLE : View.GONE);

		if (descriptionTextString != null) {
			descriptionText.setText(descriptionTextString);
		}
		descriptionText.setVisibility(descriptionTextString != null ? View.VISIBLE : View.GONE);

		return perkView;
	}

}
