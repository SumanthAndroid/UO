
package com.universalstudios.orlandoresort.controller.userinterface.help;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreActivity;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

/**
 * 
 * 
 * @author Steven Byle
 */
public class AboutWifiFragment extends Fragment implements OnClickListener, ActionBarTitleProvider {
	private static final String TAG = AboutWifiFragment.class.getSimpleName();

	private static final String DEFAULT_IN_PARK_WIFI_NETWORK_NAME = "XFINITYWiFi";

	private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";
	private static final String VIEW_TAG_PHONE_NUMBER = "VIEW_TAG_PHONE_NUMBER";
	private static final String VIEW_TAG_EMAIL_ADDRESS = "VIEW_TAG_EMAIL_ADDRESS";
	private static final String VIEW_TAG_SHOW_LOCATIONS = "VIEW_TAG_SHOW_LOCATIONS";

	private int mCalculatedImageHeightDp;
	private int mActionBarTitleResId;
	private DrawerStateProvider mParentDrawerStateProvider;
	private View mHeroImage;
	private ViewGroup mStepsLayout;

	public static AboutWifiFragment newInstance(int actionBarTitleResId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: actionBarTitleResId = " + actionBarTitleResId);
		}

		// Create a new fragment instance
		AboutWifiFragment fragment = new AboutWifiFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		args.putInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID, actionBarTitleResId);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onAttach");
		}

		// Check if parent fragment (if there is one) implements the interface
		Fragment parentFragment = getParentFragment();
		if (parentFragment != null && parentFragment instanceof DrawerStateProvider) {
			mParentDrawerStateProvider = (DrawerStateProvider) parentFragment;
		}
		// Otherwise, check if parent activity implements the interface
		else if (activity != null && activity instanceof DrawerStateProvider) {
			mParentDrawerStateProvider = (DrawerStateProvider) activity;
		}
		// If neither implements the interface, log a warning
		else if (mParentDrawerStateProvider == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement DrawerStateProvider");
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
			mActionBarTitleResId = -1;
		}
		// Otherwise, set incoming parameters
		else {
			mActionBarTitleResId = args.getInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			// Track the page view
			AnalyticsUtils.trackPageView(
					AnalyticsUtils.CONTENT_GROUP_PLANNING,
					AnalyticsUtils.CONTENT_FOCUS_GUEST_SERVICES,
					AnalyticsUtils.CONTENT_SUB_1_FAQ,
					null,
					AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
					null, null);
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
		}

		// Get the smallest (portrait) width in dp
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		float widthDp = displayMetrics.widthPixels / displayMetrics.density;
		float heightDp = displayMetrics.heightPixels / displayMetrics.density;
		float smallestWidthDp = Math.min(widthDp, heightDp);

		// Compute the height based on image aspect ratio 1080x760 @ 480dpi
		mCalculatedImageHeightDp = (int) Math.round(smallestWidthDp * (760.0 / 1080.0));

		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			parentActivity.getActionBar().setTitle(mActionBarTitleResId);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Set the action bar title, if the drawer isn't open
		if (mParentDrawerStateProvider != null && !mParentDrawerStateProvider.isDrawerOpenAtAll()) {
			Activity parentActivity = getActivity();
			if (parentActivity != null) {
				parentActivity.getActionBar().setTitle(provideTitle());
			}
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_about_wifi, container, false);

		// Setup Views
		mHeroImage = fragmentView.findViewById(R.id.fragment_about_wifi_hero_image);
		mStepsLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_about_wifi_steps_layout);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise, restore state
		else {
		}

		// Set image height based on detail image aspect ratio
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int calculatedImageHeightPx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				mCalculatedImageHeightDp, displayMetrics));
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, calculatedImageHeightPx);
		mHeroImage.setLayoutParams(layoutParams);

		UniversalAppState uoState = UniversalAppStateManager.getInstance();
		String wifiName = uoState.getInParkWifiNetworkName();
		if (wifiName == null) {
			wifiName = DEFAULT_IN_PARK_WIFI_NETWORK_NAME;
		}

		// Remove any views in the layout
		mStepsLayout.removeAllViews();

		View itemView;
		itemView = createItemView(mStepsLayout, R.string.about_wifi_item_1);
		mStepsLayout.addView(itemView);
		itemView = createItemView(mStepsLayout, getString(R.string.about_wifi_item_2, wifiName));
		mStepsLayout.addView(itemView);
		itemView = createItemView(mStepsLayout, getString(R.string.about_wifi_item_3));
		mStepsLayout.addView(itemView);
		itemView = createItemView(mStepsLayout, R.string.about_wifi_item_4);
		mStepsLayout.addView(itemView);


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

		Object tag = v.getTag();
		if (tag != null && tag instanceof String) {
			if (tag.equals(VIEW_TAG_PHONE_NUMBER)) {
				// Send the user to the dialer with the phone number filled in
				Intent intent = (Intent) v.getTag(R.id.key_view_tag_detail_feature_phone_number);
				if (intent != null && intent.resolveActivity(getActivity().getPackageManager()) != null) {
					startActivity(intent);
				}
			}
			else if (tag.equals(VIEW_TAG_EMAIL_ADDRESS)) {
				// Send the user to the dialer with the phone number filled in
				Intent intent = (Intent) v.getTag(R.id.key_view_tag_detail_feature_email_address);
				if (intent != null && intent.resolveActivity(getActivity().getPackageManager()) != null) {
					startActivity(intent);
				}
			}
			else if (tag.equals(VIEW_TAG_SHOW_LOCATIONS)) {
				Bundle bundle = ExploreActivity.newInstanceBundle(R.string.drawer_item_guest_services_locations, ExploreType.GUEST_SERVICES_BOOTHS);
				Intent guestServicesLocationsIntent = new Intent(v.getContext(), ExploreActivity.class).putExtras(bundle);
				startActivity(guestServicesLocationsIntent);
			}
		}
	}

	@Override
	public String provideTitle() {
		return getString(mActionBarTitleResId);
	}

	private static View createItemView(ViewGroup parentLayout, Integer primaryTextStringResId) {
		Context context = parentLayout.getContext();
		return createItemView(parentLayout, context.getString(primaryTextStringResId));
	}

	private static View createItemView(ViewGroup parentLayout, String primaryTextString) {
		LayoutInflater inflater = LayoutInflater.from(parentLayout.getContext());

		ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.list_wifi_step_item, parentLayout, false);
		TextView primaryText = (TextView) itemView.findViewById(R.id.list_wifi_step_item_primary_text);
		TextView numberText = (TextView) itemView.findViewById(R.id.list_wifi_step_item_number_text);

		if (primaryTextString != null) {
			primaryText.setText(primaryTextString);
		}
		primaryText.setVisibility(primaryTextString != null ? View.VISIBLE : View.GONE);

		int stepNum = parentLayout.getChildCount() + 1;
		numberText.setText("" + stepNum);

		return itemView;
	}
}
