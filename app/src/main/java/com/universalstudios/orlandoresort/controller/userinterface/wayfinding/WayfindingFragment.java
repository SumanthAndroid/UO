/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.wayfinding;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.DismissableDialogFragment;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.OnDialogDismissListener;
import com.universalstudios.orlandoresort.controller.userinterface.image.PicassoProvider;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.wayfinding.Path;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.state.wayfinding.OnWayfindingStateChangeListener;
import com.universalstudios.orlandoresort.model.state.wayfinding.WayfindingState;
import com.universalstudios.orlandoresort.model.state.wayfinding.WayfindingStateManager;
import com.universalstudios.orlandoresort.view.fonts.TextView;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager.TransitionEffect;
import com.universalstudios.orlandoresort.view.wayfinding.WayfindProgressView;

/**
 * 
 * 
 * @author Steven Byle
 */
public class WayfindingFragment extends Fragment implements OnPageChangeListener, PicassoProvider,
OnWayfindingStateChangeListener, OnClickListener, WayfindingStarter, OnDialogDismissListener {
	private static final String TAG = WayfindingFragment.class.getSimpleName();

	private static final String KEY_ARG_SELECTED_POI_OBJECT_JSON = "KEY_ARG_SELECTED_POI_OBJECT_JSON";
	private static final String KEY_ARG_SELECTED_POI_TYPE_ID = "KEY_ARG_SELECTED_POI_TYPE_ID";
	private static final String KEY_STATE_CUR_VIEWPAGER_TAB = "KEY_STATE_CUR_VIEWPAGER_TAB";
	private static final String KEY_STATE_IS_FAILED_GETTING_ROUTE_DIALOG_SHOWING = "KEY_STATE_IS_FAILED_GETTING_ROUTE_DIALOG_SHOWING";
	private static final String KEY_STATE_IS_NO_ROUTE_DIALOG_SHOWING = "KEY_STATE_IS_NO_ROUTE_DIALOG_SHOWING";
	private static final String KEY_STATE_HAS_SHOWN_YOU_HAVE_ARRIVED = "KEY_STATE_HAS_SHOWN_YOU_HAVE_ARRIVED";
	private static final String KEY_STATE_IS_NO_GPS_DIALOG_SHOWING = "KEY_STATE_IS_NO_GPS_DIALOG_SHOWING";
	private static final String KEY_STATE_IS_NO_LOCATION_DIALOG_SHOWING = "KEY_STATE_IS_NO_LOCATION_DIALOG_SHOWING";

	private boolean mIsFailedGettingRouteDialogShowing;
	private boolean mIsNoRouteDialogShowing;
	private boolean mHasShownYouHaveArrived;
	private boolean mIsNoGpsDialogShowing;
	private boolean mIsNoLocationDialogShowing;
	private String mSelectedPoiObjectJson;
	private int mSelectedPoiTypeId;
	private int mCurrentViewPagerTab;
	private PointOfInterest mSelectedPoi;
	private TextView mPoiNameText;
	private TextView mStepNumberText;
	private WayfindProgressView mProgressView;
	private View mCloseButton;
	private View mCurrentStepButton;
	private JazzyViewPager mViewPager;
	private ViewGroup mLoadingLayout;
	private PathFragmentPagerAdapter mPathFragmentPagerAdapter;
	private UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
	private Picasso mPicasso;
	private WayfindingStopper mParentWayfindingStopper;
	private String mCurrentRouteRequestTag;

	public static WayfindingFragment newInstance(String selectedPoiObjectJson, int selectedPoiTypeId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance");
		}

		// Create a new fragment instance
		WayfindingFragment fragment = new WayfindingFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		// Add parameters to the argument bundle
		args.putString(KEY_ARG_SELECTED_POI_OBJECT_JSON, selectedPoiObjectJson);
		args.putInt(KEY_ARG_SELECTED_POI_TYPE_ID, selectedPoiTypeId);
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
		if (parentFragment != null && parentFragment instanceof WayfindingStopper) {
			mParentWayfindingStopper = (WayfindingStopper) parentFragment;
		}
		// Otherwise, check if parent activity implements the interface
		else if (activity != null && activity instanceof WayfindingStopper) {
			mParentWayfindingStopper = (WayfindingStopper) activity;
		}
		// If neither implements the image selection callback, warn that
		// selections are being missed
		else if (mParentWayfindingStopper == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement WayfindingStopper");
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
			mSelectedPoiObjectJson = null;
			mSelectedPoiTypeId = -1;
			mSelectedPoi = null;
		}
		// Otherwise, set incoming parameters
		else {
			mSelectedPoiObjectJson = args.getString(KEY_ARG_SELECTED_POI_OBJECT_JSON);
			mSelectedPoiTypeId = args.getInt(KEY_ARG_SELECTED_POI_TYPE_ID);
			mSelectedPoi = PointOfInterest.fromJson(mSelectedPoiObjectJson, mSelectedPoiTypeId);
		}

		mCurrentRouteRequestTag = null;

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			mIsFailedGettingRouteDialogShowing = false;
			mIsNoRouteDialogShowing = false;
			mHasShownYouHaveArrived = false;
			mIsNoGpsDialogShowing = false;
			mIsNoLocationDialogShowing = false;

			// On first creation, start the wayfinding service
			startWayfinding(false);

			// Track the event
			AnalyticsUtils.trackEvent(
					mSelectedPoi.getDisplayName(),
					AnalyticsUtils.EVENT_NAME_GUIDE_ME,
					AnalyticsUtils.EVENT_NUM_GUIDE_ME,
					null);
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
			mIsFailedGettingRouteDialogShowing = savedInstanceState.getBoolean(KEY_STATE_IS_FAILED_GETTING_ROUTE_DIALOG_SHOWING);
			mIsNoRouteDialogShowing = savedInstanceState.getBoolean(KEY_STATE_IS_NO_ROUTE_DIALOG_SHOWING);
			mHasShownYouHaveArrived = savedInstanceState.getBoolean(KEY_STATE_HAS_SHOWN_YOU_HAVE_ARRIVED);
			mIsNoGpsDialogShowing = savedInstanceState.getBoolean(KEY_STATE_IS_NO_GPS_DIALOG_SHOWING);
			mIsNoLocationDialogShowing = savedInstanceState.getBoolean(KEY_STATE_IS_NO_LOCATION_DIALOG_SHOWING);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_wayfinding, container, false);

		// Setup Views
		mViewPager = (JazzyViewPager) fragmentView.findViewById(R.id.fragment_wayfinding_viewpager);
		mLoadingLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_wayfinding_loading_layout);
		mPoiNameText = (TextView) fragmentView.findViewById(R.id.fragment_wayfinding_poi_name_text);
		mStepNumberText = (TextView) fragmentView.findViewById(R.id.fragment_wayfinding_step_number_text);
		mCloseButton = fragmentView.findViewById(R.id.fragment_wayfinding_close_button);
		mCurrentStepButton = fragmentView.findViewById(R.id.fragment_wayfinding_current_step_button);
		mProgressView = (WayfindProgressView) fragmentView.findViewById(R.id.wayfindProgressView);

		// Set text
		String poiName = mSelectedPoi.getDisplayName();
		mPoiNameText.setText(poiName != null ? poiName : "");
		int stepNumber = mCurrentViewPagerTab + 1;
		mStepNumberText.setText("" + stepNumber);
		updateStepTextContentDescription(stepNumber);

		// Setup view pager
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setTransitionEffect(TransitionEffect.Standard);
		mViewPager.setFadeEnabled(false);

		mCloseButton.setOnClickListener(this);
		mCurrentStepButton.setOnClickListener(this);

		// Create the image downloader to get the images
		mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
				CacheUtils.WAYFINDING_DISK_CACHE_NAME,
				CacheUtils.WAYFINDING_DISK_CACHE_MIN_SIZE_BYTES,
				CacheUtils.WAYFINDING_DISK_CACHE_MAX_SIZE_BYTES);

		mPicasso = new Picasso.Builder(getActivity())
		.debugging(UniversalOrlandoImageDownloader.SHOW_DEBUG)
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

		WayfindingState state = WayfindingStateManager.getInstance();
		updateWayfindingButtonViews(state.getCurrentStepIndex(), mCurrentViewPagerTab);

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

		// Store whether the wayfinding page is showing
		WayfindingState state = WayfindingStateManager.getInstance();
		state.setWayfindingScreenShowing(true);

		// Refresh the views to match any state that may have changed while in the background
		updateViewsBasedOnWayfindingState();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onResume");
		}

		// Listen for state changes
		WayfindingStateManager.registerStateChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onPause");
		}

		// Stop listening for state changes
		WayfindingStateManager.unregisterStateChangeListener(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onSaveInstanceState");
		}
		outState.putInt(KEY_STATE_CUR_VIEWPAGER_TAB, mCurrentViewPagerTab);
		outState.putBoolean(KEY_STATE_IS_FAILED_GETTING_ROUTE_DIALOG_SHOWING, mIsFailedGettingRouteDialogShowing);
		outState.putBoolean(KEY_STATE_IS_NO_ROUTE_DIALOG_SHOWING, mIsNoRouteDialogShowing);
		outState.putBoolean(KEY_STATE_HAS_SHOWN_YOU_HAVE_ARRIVED, mHasShownYouHaveArrived);
		outState.putBoolean(KEY_STATE_IS_NO_GPS_DIALOG_SHOWING, mIsNoGpsDialogShowing);
		outState.putBoolean(KEY_STATE_IS_NO_LOCATION_DIALOG_SHOWING, mIsNoLocationDialogShowing);
	}

	@Override
	public void onStop() {
		super.onStop();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStop");
		}

		// Store whether the wayfinding page is not showing
		WayfindingState state = WayfindingStateManager.getInstance();
		state.setWayfindingScreenShowing(false);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroyView");
		}

		if (mPathFragmentPagerAdapter != null) {
			mPathFragmentPagerAdapter.destroy();
		}
		if (mPicasso != null) {
			mPicasso.shutdown();
		}
		if (mUniversalOrlandoImageDownloader != null) {
			mUniversalOrlandoImageDownloader.destroy();
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
			case R.id.fragment_wayfinding_close_button:
				if (mParentWayfindingStopper != null) {
					mParentWayfindingStopper.stopWayfinding(true);
				}
				break;
			case R.id.fragment_wayfinding_current_step_button:
				WayfindingState state = WayfindingStateManager.getInstance();
				mViewPager.setCurrentItem(state.getCurrentStepIndex());
				break;
			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onClick: unknown view clicked");
				}
				break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onPageSelected: " + position);
		}
		mCurrentViewPagerTab = position;
		int stepNumber = mCurrentViewPagerTab + 1;
		mStepNumberText.setText("" + stepNumber);
		updateStepTextContentDescription(stepNumber);
		mProgressView.setSelectedPosition(position);

		WayfindingState state = WayfindingStateManager.getInstance();
		updateWayfindingButtonViews(state.getCurrentStepIndex(), mCurrentViewPagerTab);
	}

	@Override
	public Picasso providePicasso() {
		return mPicasso;
	}

	@Override
	public void startWayfinding(boolean reloadRoute) {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			// Reset the wayfinding state if this is the first load
			if (!reloadRoute) {
				WayfindingStateManager.resetWayfindingState();
			}

			// Show loading state
			if (mLoadingLayout != null && mViewPager != null) {
				mLoadingLayout.setVisibility(View.VISIBLE);
				mViewPager.setVisibility(View.GONE);
			}

			Bundle wayfindingServiceBundle = WayfindingService.newInstanceBundle(mSelectedPoiObjectJson, mSelectedPoiTypeId, reloadRoute);
			parentActivity.startService(new Intent(parentActivity, WayfindingService.class).putExtras(wayfindingServiceBundle));
		}
	}


	@Override
	public void onDialogDismiss(Object dialog) {
		if (dialog instanceof FailedGettingRouteDialogFragment) {
			mIsFailedGettingRouteDialogShowing = false;
		}
		if (dialog instanceof NoRouteDialogFragment) {
			mIsNoRouteDialogShowing = false;
		}
		if (dialog instanceof NoGpsDialogFragment) {
			mIsNoGpsDialogShowing = false;
		}
		if (dialog instanceof NoLocationDialogFragment) {
			mIsNoLocationDialogShowing = false;
		}
	}

	@Override
	public void onWayfindingStateChange(WayfindingState wayfindingState) {
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "onWayfindingStateChange");
		}

		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			parentActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						updateViewsBasedOnWayfindingState();
					}
					catch (Exception e) {
						if (BuildConfig.DEBUG) {
							Log.e(TAG, "onWayfindingStateChange: exception trying to refresh UI", e);
						}

						// Log the exception to crittercism
						Crittercism.logHandledException(e);
					}
				}
			});
		}
	}

	private void updateViewsBasedOnWayfindingState() {
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "updateViewsBasedOnWayfindingState");
		}

		WayfindingState state = WayfindingStateManager.getInstance();

		// If the service has been killed, stop wayfinding
		Boolean isServiceRunning = state.getIsServiceRunning();
		if (isServiceRunning != null && !isServiceRunning.booleanValue()) {
			if (BuildConfig.DEBUG) {
				Log.i(TAG, "updateViewsBasedOnWayfindingState: wayfinding service has stopped, closing wayfinding");
			}

			if (mParentWayfindingStopper != null) {
				mParentWayfindingStopper.stopWayfinding(true);
				return;
			}
		}

		// If the route is still being waited on, show the loading screen
		if (state.getDidRouteRequestSucceed() == null) {
			mLoadingLayout.setVisibility(View.VISIBLE);
			mViewPager.setVisibility(View.GONE);
		}
		else if (state.getDidRouteRequestSucceed().booleanValue()) {
			mLoadingLayout.setVisibility(View.GONE);
			mViewPager.setVisibility(View.VISIBLE);
		}
		else if (!mIsFailedGettingRouteDialogShowing) {
			mLoadingLayout.setVisibility(View.GONE);
			mViewPager.setVisibility(View.GONE);

			// Show an error dialog if the network call failed
			mIsFailedGettingRouteDialogShowing = true;
			FragmentManager fragmentManager = getChildFragmentManager();
			FailedGettingRouteDialogFragment dialogFragment = new FailedGettingRouteDialogFragment();
			dialogFragment.show(fragmentManager, dialogFragment.getClass().getSimpleName());
		}

		// If there is a different request than we are currently showing, and it
		// completed successfully, set the adapter
		List<Path> paths = state.getPaths();
		String routeRequestTag = state.getRouteRequestTag();
		if (routeRequestTag != null && !routeRequestTag.equals(mCurrentRouteRequestTag)
				&& state.getDidRouteRequestSucceed() != null && state.getDidRouteRequestSucceed().booleanValue()
				&& paths != null) {
			mCurrentRouteRequestTag = routeRequestTag;

			// If the route is not empty, load it into the adapter
			if (paths.size() > 0) {
				mPathFragmentPagerAdapter = new PathFragmentPagerAdapter(mViewPager,
						getChildFragmentManager(), paths);
				mProgressView.setVisibility(View.VISIBLE);
				mProgressView.setIndicatorCount(paths.size());
				mViewPager.setAdapter(mPathFragmentPagerAdapter);
			}
			// Otherwise, the route is empty
			else if (!mIsNoRouteDialogShowing) {
				// Show an error dialog if the route is empty
				mIsNoRouteDialogShowing = true;
				FragmentManager fragmentManager = getChildFragmentManager();
				NoRouteDialogFragment dialogFragment = new NoRouteDialogFragment();
				dialogFragment.show(fragmentManager, dialogFragment.getClass().getSimpleName());
			}
		}

		// If the current route is the same as the one currently shown, update
		// the pager to be on the same page
		if (routeRequestTag != null && routeRequestTag.equals(mCurrentRouteRequestTag)
				&& state.getDidRouteRequestSucceed() != null && state.getDidRouteRequestSucceed().booleanValue()
				&& paths != null && paths.size() > 0) {
			int currentStepIndex = state.getCurrentStepIndex();
			if (mViewPager != null && currentStepIndex != mCurrentViewPagerTab) {
				mViewPager.setCurrentItem(currentStepIndex);
				int stepNumber = currentStepIndex + 1;
				mStepNumberText.setText("" + stepNumber);
				updateStepTextContentDescription(stepNumber);
			}

			// Update the wayfinding button state
			updateWayfindingButtonViews(currentStepIndex, mViewPager.getCurrentItem());
		}

		// If the route has finished, show the you have arrived toast only once
		if (!mHasShownYouHaveArrived && state.hasFinishedRoute()) {
			mHasShownYouHaveArrived = true;

			LayoutInflater inflater = LayoutInflater.from(getActivity());
			View toastView = inflater.inflate(R.layout.toast_wayfinding_you_have_arrived, (ViewGroup) getView(), false);

			Toast toast = new Toast(getActivity());
			toast.setView(toastView);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.show();
		}

		// If there is no location connect, show a dialog
		Boolean isLocationConnected = state.getIsLocationServicesConnected();
		if (!mIsNoLocationDialogShowing && !mIsFailedGettingRouteDialogShowing && !mIsNoRouteDialogShowing && !mIsNoGpsDialogShowing
				&& !state.hasFinishedRoute() && isLocationConnected != null && !isLocationConnected.booleanValue()) {
			mIsNoLocationDialogShowing = true;

			FragmentManager fragmentManager = getChildFragmentManager();
			NoLocationDialogFragment dialogFragment = new NoLocationDialogFragment();
			dialogFragment.show(fragmentManager, dialogFragment.getClass().getSimpleName());
		}

		// If there are no location services / GPS connection, show a dialog
		Boolean isGpsEnabled = state.getIsGpsEnabled();
		if (!mIsNoGpsDialogShowing && !mIsFailedGettingRouteDialogShowing && !mIsNoRouteDialogShowing && !mIsNoLocationDialogShowing
				&& !state.hasFinishedRoute() && isGpsEnabled != null && !isGpsEnabled.booleanValue()) {
			mIsNoGpsDialogShowing = true;

			FragmentManager fragmentManager = getChildFragmentManager();
			NoGpsDialogFragment dialogFragment = new NoGpsDialogFragment();
			dialogFragment.show(fragmentManager, dialogFragment.getClass().getSimpleName());
		}
	}

	private void updateWayfindingButtonViews(int currentStep, int currentViewpagerTab) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "updateWayfindingButtonViews: currentStep = " + currentStep
					+ " currentViewpagerTab = " + currentViewpagerTab);
		}

		boolean onCurrentStepPage = currentStep == currentViewpagerTab;
		if (mCloseButton != null && mCurrentStepButton != null) {
			mCloseButton.setVisibility(onCurrentStepPage ? View.VISIBLE : View.GONE);
			mCurrentStepButton.setVisibility(onCurrentStepPage ? View.GONE : View.VISIBLE);
		}
	}

	private void updateStepTextContentDescription(int stepNumber) {
		String contentDescription = "";
		if (stepNumber == 1) {
			contentDescription = getString(R.string.wayfinding_step_1_content_description, stepNumber);
		}
		else {
			contentDescription = getString(R.string.wayfinding_step_x_content_description, stepNumber);
		}
		mStepNumberText.setContentDescription(contentDescription);
	}

	public static class FailedGettingRouteDialogFragment extends DismissableDialogFragment implements DialogInterface.OnClickListener {
		private static final String TAG = FailedGettingRouteDialogFragment.class.getSimpleName();

		private WayfindingStarter mParentWayfindingStarter;

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);

			// Check if parent fragment (if there is one) implements the interface
			Fragment parentFragment = getParentFragment();
			if (parentFragment != null && parentFragment instanceof WayfindingStarter) {
				mParentWayfindingStarter = (WayfindingStarter) parentFragment;
			}
			// Otherwise, check if parent activity implements the interface
			else if (activity != null && activity instanceof WayfindingStarter) {
				mParentWayfindingStarter = (WayfindingStarter) activity;
			}
			// If neither implements the image selection callback, warn that
			// selections are being missed
			else if (mParentWayfindingStarter == null) {
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement WayfindingStarter");
				}
			}
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			alertDialogBuilder.setTitle(R.string.wayfinding_get_route_failed_title);
			alertDialogBuilder.setMessage(R.string.wayfinding_get_route_failed_message);
			alertDialogBuilder.setPositiveButton(R.string.wayfinding_get_route_failed_positive_button, this);
			alertDialogBuilder.setNegativeButton(R.string.wayfinding_get_route_failed_negative_button, this);

			Dialog dialog = alertDialogBuilder.create();
			dialog.setCanceledOnTouchOutside(false);
			return dialog;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					if (mParentWayfindingStarter != null) {
						mParentWayfindingStarter.startWayfinding(true);
					}
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					Activity parentActivity = getActivity();
					if (parentActivity != null) {
						parentActivity.finish();
					}
					break;
			}
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			super.onCancel(dialog);

			// Simulate a cancel click
			onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
		}
	}

	public static class NoRouteDialogFragment extends DismissableDialogFragment implements DialogInterface.OnClickListener {
		private static final String TAG = FailedGettingRouteDialogFragment.class.getSimpleName();

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			alertDialogBuilder.setTitle(R.string.wayfinding_no_route_title);
			alertDialogBuilder.setMessage(R.string.wayfinding_no_route_message);
			alertDialogBuilder.setPositiveButton(R.string.wayfinding_no_route_positive_button, this);

			Dialog dialog = alertDialogBuilder.create();
			dialog.setCanceledOnTouchOutside(false);
			return dialog;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Activity parentActivity = getActivity();
					if (parentActivity != null) {
						parentActivity.finish();
					}
					break;
			}
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			super.onCancel(dialog);

			// Simulate an ok click
			onClick(dialog, DialogInterface.BUTTON_POSITIVE);
		}
	}

	public static class NoGpsDialogFragment extends DismissableDialogFragment implements DialogInterface.OnClickListener {
		private static final String TAG = FailedGettingRouteDialogFragment.class.getSimpleName();

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			alertDialogBuilder.setTitle(R.string.wayfinding_no_gps_title);
			alertDialogBuilder.setMessage(R.string.wayfinding_no_gps_message);
			alertDialogBuilder.setPositiveButton(R.string.wayfinding_no_gps_positive_button, this);
			alertDialogBuilder.setNegativeButton(R.string.wayfinding_no_gps_negative_button, this);

			Dialog dialog = alertDialogBuilder.create();
			dialog.setCanceledOnTouchOutside(false);
			return dialog;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Activity parentActivity = getActivity();

			switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// Open location settings
					if (parentActivity != null) {
						try {
							parentActivity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
						}
						catch (Exception e) {
							if (BuildConfig.DEBUG) {
								Log.e(TAG, "onClick: unable to open location settings", e);
							}

							// Log the exception to crittercism
							Crittercism.logHandledException(e);
						}
						finally {
							parentActivity.finish();
						}
					}
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					if (parentActivity != null) {
						parentActivity.finish();
					}
					break;
			}
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			super.onCancel(dialog);

			// Simulate an cancel click
			onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
		}
	}

	public static class NoLocationDialogFragment extends DismissableDialogFragment implements DialogInterface.OnClickListener {
		private static final String TAG = FailedGettingRouteDialogFragment.class.getSimpleName();

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			alertDialogBuilder.setTitle(R.string.wayfinding_no_location_services_title);
			alertDialogBuilder.setMessage(R.string.wayfinding_no_location_services_message);
			alertDialogBuilder.setPositiveButton(R.string.wayfinding_no_location_services_positive_button, this);

			Dialog dialog = alertDialogBuilder.create();
			dialog.setCanceledOnTouchOutside(false);
			return dialog;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Activity parentActivity = getActivity();
					if (parentActivity != null) {
						parentActivity.finish();
					}
					break;
			}
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			super.onCancel(dialog);

			// Simulate an ok click
			onClick(dialog, DialogInterface.BUTTON_POSITIVE);
		}
	}
}
