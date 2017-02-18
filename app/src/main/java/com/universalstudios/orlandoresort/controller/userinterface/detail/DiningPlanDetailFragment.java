/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreActivity;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;

/**
 * 
 * 
 * @author Steven Byle
 */
public class DiningPlanDetailFragment extends Fragment implements OnClickListener {
	private static final String TAG = DiningPlanDetailFragment.class.getSimpleName();
	
	public static DiningPlanDetailFragment newInstance() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance");
		}

		// Create a new fragment instance
		DiningPlanDetailFragment fragment = new DiningPlanDetailFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		// Add parameters to the argument bundle
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
			// Track the page view
			AnalyticsUtils.trackPageView(
					AnalyticsUtils.CONTENT_GROUP_PLANNING,
					AnalyticsUtils.CONTENT_FOCUS_GUEST_SERVICES,
					AnalyticsUtils.CONTENT_SUB_1_FAQ,
					AnalyticsUtils.CONTENT_SUB_2_DINING_PLANS_INFO,
					AnalyticsUtils.PROPERTY_NAME_PARKS,
					null, null);
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
		View fragmentView = inflater.inflate(R.layout.fragment_detail_dining_plan, container, false);
		fragmentView.findViewById(R.id.fragment_detail_dining_plan_universal_dining_plan).setOnClickListener(this);
		fragmentView.findViewById(R.id.fragment_detail_dining_plan_quick_service_plan).setOnClickListener(this);
		
		return fragmentView;
	}
	
	@Override
    public void onClick(View v) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onClick");
        }

        switch (v.getId()) {
            case R.id.fragment_detail_dining_plan_universal_dining_plan: {
                FilterOptions filterOptions = new FilterOptions(null, ExploreType.UNIVERSAL_DINING);
                filterOptions.setDiningPlans(Arrays.asList(String
                        .valueOf(PointsOfInterestTable.VAL_OPTION_FLAG_DINING_UNIVERSAL_DINING_PLAN)));
                Bundle args = ExploreActivity.newInstanceBundle(R.string.dining_plan_participating_locations,
                        ExploreType.UNIVERSAL_DINING, filterOptions);
                startActivity(new Intent(getActivity(), ExploreActivity.class).putExtras(args));
                break;
            }
            case R.id.fragment_detail_dining_plan_quick_service_plan: {
                FilterOptions filterOptions = new FilterOptions(null,
                        ExploreType.UNIVERSAL_DINING_QUICK_SERVICE);
                filterOptions
                        .setDiningPlans(Arrays.asList(String
                                .valueOf(PointsOfInterestTable.VAL_OPTION_FLAG_DINING_UNIVERSAL_DINING_PLAN_QUICK_SERVICE)));
                Bundle args = ExploreActivity.newInstanceBundle(R.string.dining_plan_participating_locations,
                        ExploreType.UNIVERSAL_DINING_QUICK_SERVICE);
                startActivity(new Intent(getActivity(), ExploreActivity.class).putExtras(args));
                break;
            }
        }
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

}
