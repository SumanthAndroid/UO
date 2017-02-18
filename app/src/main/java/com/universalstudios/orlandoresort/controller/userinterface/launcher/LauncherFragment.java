package com.universalstudios.orlandoresort.controller.userinterface.launcher;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.security.ProviderInstaller;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.accessibility.AccessibilityUtils;
import com.universalstudios.orlandoresort.controller.userinterface.alerts.AlertsUtils;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.home.HomeActivity;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Countries.GetCountriesRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Countries.GetCountriesResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.State.GetStateRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.State.GetStateResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.GetTridionConfigsRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.GetTridionConfigsResponse;
import com.universalstudios.orlandoresort.model.network.analytics.CrashAnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.alert.GetRegisteredAlertsRequest;
import com.universalstudios.orlandoresort.model.network.domain.alert.RegisterAlertsRequest;
import com.universalstudios.orlandoresort.model.network.domain.alert.RegisterAlertsResponse;
import com.universalstudios.orlandoresort.model.network.domain.appointments.queues.GetQueuesByPageRequest;
import com.universalstudios.orlandoresort.model.network.domain.control.GetCommerceEnabledRequest;
import com.universalstudios.orlandoresort.model.network.domain.control.GetControlPropertiesRequest;
import com.universalstudios.orlandoresort.model.network.domain.control.GetControlPropertiesResponse;
import com.universalstudios.orlandoresort.model.network.domain.events.GetEventSeriesRequest;
import com.universalstudios.orlandoresort.model.network.domain.interactiveexperience.GetInteractiveExperienceResponse;
import com.universalstudios.orlandoresort.model.network.domain.news.GetNewsRequest;
import com.universalstudios.orlandoresort.model.network.domain.news.GetNewsResponse;
import com.universalstudios.orlandoresort.model.network.domain.offers.GetOfferVendorsRequest;
import com.universalstudios.orlandoresort.model.network.domain.photoframes.GetPhotoFrameExperienceRequest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.GetPoisRequest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.GetPoisResponse;
import com.universalstudios.orlandoresort.model.network.domain.tridion.GetMobilePagesRequest;
import com.universalstudios.orlandoresort.model.network.domain.venue.GetVenuesRequest;
import com.universalstudios.orlandoresort.model.network.domain.venue.GetVenuesResponse;
import com.universalstudios.orlandoresort.model.network.push.PushUtils;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.ConcurrencyType;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.Priority;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * @author Steven Byle
 */
public class LauncherFragment extends NetworkFragment {
	private static final String TAG = LauncherFragment.class.getSimpleName();

	private static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;

	private static final String KEY_STATE_HAS_STARTED_NETWORK_REQUESTS = "KEY_STATE_HAS_STARTED_NETWORK_REQUESTS";
	private static final String KEY_STATE_HAS_STARTED_APP = "KEY_STATE_HAS_STARTED_APP";
	private static final String KEY_STATE_GET_TOKEN_SUCCESS = "KEY_STATE_GET_TOKEN_SUCCESS";
	private static final String KEY_STATE_GET_VENUES_SUCCESS = "KEY_STATE_GET_VENUES_SUCCESS";
	private static final String KEY_STATE_GET_POIS_SUCCESS = "KEY_STATE_GET_POIS_SUCCESS";
	private static final String KEY_STATE_REGISTER_ALERTS_SUCCESS = "KEY_STATE_REGISTER_ALERTS_SUCCESS";
	private static final String KEY_STATE_RECEIVED_TOKEN_RESPONSE = "KEY_STATE_RECEIVED_TOKEN_RESPONSE";
	private static final String KEY_STATE_RECEIVED_VENUES_RESPONSE = "KEY_STATE_RECEIVED_VENUES_RESPONSE";
	private static final String KEY_STATE_RECEIVED_POIS_RESPONSE = "KEY_STATE_RECEIVED_POIS_RESPONSE";
	private static final String KEY_STATE_RECEIVED_REGISTER_ALERTS_RESPONSE = "KEY_STATE_RECEIVED_REGISTER_ALERTS_RESPONSE";
	private static final String KEY_STATE_IS_SYNC_REQUIRED = "KEY_STATE_IS_SYNC_REQUIRED";
	private static final String KEY_STATE_IS_ALERT_UPDATE_REQUIRED = "KEY_STATE_IS_ALERT_UPDATE_REQUIRED";

	private static final long MAX_TIME_SINCE_LAST_SYNC_IN_SEC = 90l * 24 * 60 * 60; // 90 days
	private static final long LOAD_IN_ANIM_DURATION_IN_MS = 900;
	private static final long LOAD_IN_ANIM_WAIT_AFTER_DURATION_IN_MS = 1600;
	private static final long SPIN_FLARE_ANIM_DURATION_IN_MS = 16 * 1000;
	private static final long START_ANIM_DELAY_IN_MS = 300;

    public static final float SPLASH_GLOW_IMAGE_OPACITY = 0.5f;

    private boolean mIsSyncRequired, mIsAlertRegistrationRequired;
    private boolean mHasStartedNetworkRequests, mHasAnimationCompleted,
            mHasStartedApp, mHasSavedInstanceState;
    private boolean mGetTokenSuccess, mGetVenuesSuccess, mGetPoisSuccess,
            mRegisterAlertsSuccess;
    private boolean mReceivedVenuesResponse, mReceivedTokenResponse,
            mReceivedPoisResponse, mReceivedRegisterAlertsResponse;

	private ViewGroup mLogoLayout;
	private ImageView mLogo, mLogoFlare1, mLogoFlare2, mLogoGlow;
	private TextView mTagLine;
	private AnimatorSet mAnimatorSet, mLoadInAnimSet, mSpinFlareAnimSet;
	private ViewGroup mFragmentLayout;

    public static LauncherFragment newInstance() {
        // Create a new fragment instance
        LauncherFragment fragment = new LauncherFragment();
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

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            mHasStartedNetworkRequests = mGetTokenSuccess = mGetVenuesSuccess = mGetPoisSuccess = mRegisterAlertsSuccess
                    = mReceivedVenuesResponse = mReceivedTokenResponse = mReceivedPoisResponse = mReceivedRegisterAlertsResponse
                    = mIsSyncRequired = mIsAlertRegistrationRequired = mHasAnimationCompleted = mHasStartedApp = mHasSavedInstanceState
                    = false;

			// Require a sync if the main app data hasn't been synced since the threshold
			UniversalAppState uoState = UniversalAppStateManager.getInstance();
			mIsSyncRequired = !UniversalAppStateManager.hasSyncedInTheLast(uoState.getDateOfLastControlPropSyncInMillis(), MAX_TIME_SINCE_LAST_SYNC_IN_SEC)
							  || !UniversalAppStateManager.hasSyncedInTheLast(uoState.getDateOfLastPoiSyncInMillis(), MAX_TIME_SINCE_LAST_SYNC_IN_SEC)
							  || !UniversalAppStateManager.hasSyncedInTheLast(uoState.getDateOfLastVenueSyncInMillis(), MAX_TIME_SINCE_LAST_SYNC_IN_SEC);

			if (BuildConfig.DEBUG) {
                Log.i(TAG, "onCreate: clearing response queue table and removing old show time alerts");
            }

            // Clean up data
            ContentResolver contentResolver = getActivity().getContentResolver();
            AlertsUtils.deleteOldShowTimeAlertsFromDatabase(contentResolver, true);
        }
        // Otherwise, restore state
        else {
            mHasStartedNetworkRequests = savedInstanceState.getBoolean(KEY_STATE_HAS_STARTED_NETWORK_REQUESTS);
            mGetTokenSuccess = savedInstanceState.getBoolean(KEY_STATE_GET_TOKEN_SUCCESS);
            mGetVenuesSuccess = savedInstanceState.getBoolean(KEY_STATE_GET_VENUES_SUCCESS);
            mGetPoisSuccess = savedInstanceState.getBoolean(KEY_STATE_GET_POIS_SUCCESS);
            mRegisterAlertsSuccess = savedInstanceState.getBoolean(KEY_STATE_REGISTER_ALERTS_SUCCESS);
            mReceivedTokenResponse = savedInstanceState.getBoolean(KEY_STATE_RECEIVED_TOKEN_RESPONSE);
            mReceivedVenuesResponse = savedInstanceState.getBoolean(KEY_STATE_RECEIVED_VENUES_RESPONSE);
            mReceivedPoisResponse = savedInstanceState.getBoolean(KEY_STATE_RECEIVED_POIS_RESPONSE);
            mReceivedRegisterAlertsResponse = savedInstanceState.getBoolean(KEY_STATE_RECEIVED_REGISTER_ALERTS_RESPONSE);
            mIsSyncRequired = savedInstanceState.getBoolean(KEY_STATE_IS_SYNC_REQUIRED);
            mIsAlertRegistrationRequired = savedInstanceState.getBoolean(KEY_STATE_IS_ALERT_UPDATE_REQUIRED);
            mHasStartedApp = savedInstanceState.getBoolean(KEY_STATE_HAS_STARTED_APP);
            mHasSavedInstanceState = false;
            mHasAnimationCompleted = false;
        }

    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_launcher, container, false);

		mFragmentLayout = (RelativeLayout)fragmentView.findViewById(R.id.fragment_launcher_layout);
		mLogoLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_launcher_logo_layout);
		mLogo = (ImageView) fragmentView.findViewById(R.id.fragment_launcher_logo);
		mLogoFlare1 = (ImageView) fragmentView.findViewById(R.id.fragment_launcher_logo_flare_1);
		mLogoFlare2 = (ImageView) fragmentView.findViewById(R.id.fragment_launcher_logo_flare_2);
		mLogoGlow = (ImageView) fragmentView.findViewById(R.id.fragment_launcher_logo_glow);
		mTagLine = (TextView) fragmentView.findViewById(R.id.fragment_launcher_tagline);

		mLogo.setVisibility(View.INVISIBLE);
		mLogoFlare1.setVisibility(View.INVISIBLE);
		mLogoFlare2.setVisibility(View.INVISIBLE);
		mLogoGlow.setVisibility(View.INVISIBLE);

		// If this is the Hollywood show the hollywood tagline
		mTagLine.setVisibility(BuildConfigUtils.isLocationFlavorHollywood() ? View.VISIBLE : View.INVISIBLE);

		// On first load
		if (savedInstanceState == null) {

			// Read out the loading state
			if (AccessibilityUtils.isTalkBackEnabled()) {
				mFragmentLayout.requestFocus();

                AccessibilityEvent event = AccessibilityEvent.obtain(
                        AccessibilityEvent.TYPE_VIEW_FOCUSED);
                event.setSource(mLogo);
                event.setClassName(mLogo.getClass().getName());
                event.setPackageName(mLogo.getContext().getPackageName());
                event.setEnabled(true);
                event.setContentDescription(getString(R.string.splash_loading_content_description));
                mLogo.sendAccessibilityEventUnchecked(event);
            }
        }

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

        mHasSavedInstanceState = false;

        // Google play services must be checked before starting the app
        if (checkGooglePlayServices()) {

            // Start by installing the security provider to enable TLS for all versions of Android
            // This then gets a GCM registration ID, and then starts the data sync calls
            installSecurityProvider();

            // Start the animation
            animateSplashLogo();
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

        outState.putBoolean(KEY_STATE_HAS_STARTED_NETWORK_REQUESTS, mHasStartedNetworkRequests);
        outState.putBoolean(KEY_STATE_GET_TOKEN_SUCCESS, mGetTokenSuccess);
        outState.putBoolean(KEY_STATE_GET_VENUES_SUCCESS, mGetVenuesSuccess);
        outState.putBoolean(KEY_STATE_GET_POIS_SUCCESS, mGetPoisSuccess);
        outState.putBoolean(KEY_STATE_REGISTER_ALERTS_SUCCESS, mRegisterAlertsSuccess);
        outState.putBoolean(KEY_STATE_RECEIVED_TOKEN_RESPONSE, mReceivedTokenResponse);
        outState.putBoolean(KEY_STATE_RECEIVED_VENUES_RESPONSE, mReceivedVenuesResponse);
        outState.putBoolean(KEY_STATE_RECEIVED_POIS_RESPONSE, mReceivedPoisResponse);
        outState.putBoolean(KEY_STATE_RECEIVED_REGISTER_ALERTS_RESPONSE, mReceivedRegisterAlertsResponse);
        outState.putBoolean(KEY_STATE_IS_SYNC_REQUIRED, mIsSyncRequired);
        outState.putBoolean(KEY_STATE_IS_ALERT_UPDATE_REQUIRED, mIsAlertRegistrationRequired);
        outState.putBoolean(KEY_STATE_HAS_STARTED_APP, mHasStartedApp);

        mHasSavedInstanceState = true;
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
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "handleNetworkResponse");
        }

        if (networkResponse instanceof GetControlPropertiesResponse) {
            GetControlPropertiesResponse response = (GetControlPropertiesResponse) networkResponse;

            mReceivedTokenResponse = true;
            mGetTokenSuccess = response.isHttpStatusCodeSuccess();
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "handleNetworkResponse: GetControlPropertiesResponse success = " + mGetTokenSuccess);
            }
        } else if (networkResponse instanceof GetVenuesResponse) {
            GetVenuesResponse response = (GetVenuesResponse) networkResponse;

            mReceivedVenuesResponse = true;
            mGetVenuesSuccess = response.isHttpStatusCodeSuccess();
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "handleNetworkResponse: GetVenuesResponse success = " + mGetVenuesSuccess);
            }
        } else if (networkResponse instanceof GetPoisResponse) {
            GetPoisResponse response = (GetPoisResponse) networkResponse;

            mReceivedPoisResponse = true;
            mGetPoisSuccess = response.isHttpStatusCodeSuccess();
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "handleNetworkResponse: GetPoisResponse success = " + mGetPoisSuccess);
            }
        } else if (networkResponse instanceof RegisterAlertsResponse) {
            RegisterAlertsResponse	response = (RegisterAlertsResponse) networkResponse;

            mReceivedRegisterAlertsResponse = true;
            mRegisterAlertsSuccess = response.isHttpStatusCodeSuccess();
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "handleNetworkResponse: RegisterAlertsResponse success = " + mRegisterAlertsSuccess);
            }
        } else if (networkResponse instanceof GetNewsResponse) {
            GetNewsResponse response = (GetNewsResponse) networkResponse;

            boolean getNewsSuccess = response.isHttpStatusCodeSuccess();
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "handleNetworkResponse: GetNewsResponse success = " + getNewsSuccess);
            }
        } else if (networkResponse instanceof GetInteractiveExperienceResponse) {
            GetInteractiveExperienceResponse response = (GetInteractiveExperienceResponse) networkResponse;

            boolean getExperiencesSuccess = response.isHttpStatusCodeSuccess();
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "handleNetworkResponse: GetInteractiveExperienceResponse success = " + getExperiencesSuccess);
            }
        } else if (networkResponse instanceof GetTridionConfigsResponse) {
            GetTridionConfigsResponse response = (GetTridionConfigsResponse) networkResponse;

            boolean getTridionConfigsSuccess = response.isHttpStatusCodeSuccess();
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "handleNetworkResponse: GetTridionConfigsResponse success = " + getTridionConfigsSuccess);
            }
        } else if (networkResponse instanceof GetCountriesResponse) {
            GetCountriesResponse response = (GetCountriesResponse) networkResponse;

            boolean getCountriesSuccess = response.isHttpStatusCodeSuccess();
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "handleNetworkResponse: GetCountriesResponse success = " + getCountriesSuccess);
            }
        } else if (networkResponse instanceof GetStateResponse) {
            GetStateResponse response = (GetStateResponse) networkResponse;

            boolean getStatesSuccess = response.isHttpStatusCodeSuccess();
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "handleNetworkResponse: GetStateResponse success = " + getStatesSuccess);
            }
        }

        // Check the app state and see if it can be started
        checkStateToStartApp();
    }

    private void animateSplashLogo() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "animateSplashLogo");
        }

        // Stop any current animation
        if (mAnimatorSet != null && mLoadInAnimSet != null && mSpinFlareAnimSet != null) {
            mAnimatorSet.removeAllListeners();
            mLoadInAnimSet.removeAllListeners();
            mSpinFlareAnimSet.removeAllListeners();
            mAnimatorSet.end();
        }

        // Create new animator sets
        mAnimatorSet = new AnimatorSet();
        mLoadInAnimSet = new AnimatorSet();
        mSpinFlareAnimSet = new AnimatorSet();

        // Create the load in animation
        PropertyValuesHolder pvScaleX = PropertyValuesHolder.ofFloat("scaleX", 0.0f, 1.0f);
        PropertyValuesHolder pvScaleY = PropertyValuesHolder.ofFloat("scaleY", 0.0f, 1.0f);
        ObjectAnimator logoScaleAnimator = ObjectAnimator.ofPropertyValuesHolder(mLogo, pvScaleX, pvScaleY);
        logoScaleAnimator.setDuration(LOAD_IN_ANIM_DURATION_IN_MS);

        OvershootInterpolator overshootInterpolator = new OvershootInterpolator(1.2f);
        logoScaleAnimator.setInterpolator(overshootInterpolator);

        PropertyValuesHolder pvAlpha = PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f);
        ObjectAnimator flare1AlphaAnimator = ObjectAnimator.ofPropertyValuesHolder(mLogoFlare1, pvAlpha);
        ObjectAnimator flare2AlphaAnimator = ObjectAnimator.ofPropertyValuesHolder(mLogoFlare2, pvAlpha);
        ObjectAnimator glowAlphaAnimator = ObjectAnimator.ofPropertyValuesHolder(mLogoGlow, pvAlpha);
        flare1AlphaAnimator.setDuration(LOAD_IN_ANIM_DURATION_IN_MS);
        flare2AlphaAnimator.setDuration(LOAD_IN_ANIM_DURATION_IN_MS);
        glowAlphaAnimator.setDuration(LOAD_IN_ANIM_DURATION_IN_MS);

        LinearInterpolator linearInterpolator = new LinearInterpolator();
        flare1AlphaAnimator.setInterpolator(linearInterpolator);
        flare2AlphaAnimator.setInterpolator(linearInterpolator);
        glowAlphaAnimator.setInterpolator(linearInterpolator);

        PropertyValuesHolder pvDelay = PropertyValuesHolder.ofFloat("rotation", 0.0f, 0.0f);
        ObjectAnimator logoDelayAnimator = ObjectAnimator.ofPropertyValuesHolder(mLogo, pvDelay);
        logoDelayAnimator.setDuration(LOAD_IN_ANIM_WAIT_AFTER_DURATION_IN_MS);

        // Play the load in animation
        mLoadInAnimSet.play(logoScaleAnimator)
                .with(flare1AlphaAnimator)
                .with(flare2AlphaAnimator)
                .with(glowAlphaAnimator);

        // Then start the delay after the logo has scaled in
        mLoadInAnimSet.play(logoDelayAnimator)
                .after(logoScaleAnimator);

        mLoadInAnimSet.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mHasAnimationCompleted = true;

                // Check the app state and see if it can be started
                checkStateToStartApp();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mHasAnimationCompleted = true;

                // Check the app state and see if it can be started
                checkStateToStartApp();
            }
        });

        // Create the flare spin animation
        PropertyValuesHolder pvRotationClockwise = PropertyValuesHolder.ofFloat("rotation", 0f, 360f);
        PropertyValuesHolder pvRotationCounterClockwise = PropertyValuesHolder.ofFloat("rotation", 0f, -180f);
        ObjectAnimator flare1RotationAnimator = ObjectAnimator.ofPropertyValuesHolder(mLogoFlare1, pvRotationClockwise);
        ObjectAnimator flare2RotationAnimator = ObjectAnimator.ofPropertyValuesHolder(mLogoFlare2, pvRotationCounterClockwise);

        flare1RotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        flare1RotationAnimator.setRepeatMode(ValueAnimator.REVERSE);
        flare1RotationAnimator.setInterpolator(new LinearInterpolator());
        flare2RotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        flare2RotationAnimator.setRepeatMode(ValueAnimator.REVERSE);
        flare2RotationAnimator.setInterpolator(new LinearInterpolator());

        mSpinFlareAnimSet.setDuration(SPIN_FLARE_ANIM_DURATION_IN_MS);
        mSpinFlareAnimSet.play(flare1RotationAnimator)
                .with(flare2RotationAnimator);

        // Load the animations together
        mAnimatorSet.play(mLoadInAnimSet).with(mSpinFlareAnimSet);
        mAnimatorSet.setStartDelay(START_ANIM_DELAY_IN_MS);
        mAnimatorSet.start();

        // Hide the views until the animation starts
        mLogo.setVisibility(View.INVISIBLE);
        mLogoFlare1.setVisibility(View.INVISIBLE);
        mLogoFlare2.setVisibility(View.INVISIBLE);
        mLogoGlow.setVisibility(View.INVISIBLE);

        // Once the animation starts, show the views
        mLogo.postDelayed(new ViewShowerRunnable(this), START_ANIM_DELAY_IN_MS);
    }

    private boolean checkGooglePlayServices() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "checkGooglePlayServices");
        }

        int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (statusCode == ConnectionResult.SUCCESS) {
            return true;
        } else {
            // If Google Play Services are not there, check for a way to fix it
            if (GooglePlayServicesUtil.isUserRecoverableError(statusCode)) {
                // Show the generated error dialog and then close the app after
                // the user makes a selection (ignore the activity result since
                // the app will just close)
                Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(statusCode, getActivity(), REQUEST_CODE_RECOVER_PLAY_SERVICES);
                errorDialog.setCanceledOnTouchOutside(false);
                errorDialog.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // Close the app after the user has made a selection, they will need to
                        // restart to retry Google Play Services
                        Activity parentActivity = getActivity();
                        if (parentActivity != null && !parentActivity.isFinishing()) {
                            parentActivity.finish();
                        }
                    }
                });
                errorDialog.show();
            } else {
                // If the error is unrecoverable, inform the user and close the app
                UserInterfaceUtils.showToastFromForeground(
                        GooglePlayServicesUtil.getErrorString(statusCode), Toast.LENGTH_LONG, getActivity());

                Activity parentActivity = getActivity();
                if (parentActivity != null && !parentActivity.isFinishing()) {
                    parentActivity.finish();
                }
            }
            return false;
        }
    }

    private void installSecurityProvider() {
        // Check the GCM registration ID, which will start the network
        // call sequence after it completes
        InstallSecurityProviderTask installSecurityProviderTask = new InstallSecurityProviderTask(this);
        installSecurityProviderTask.execute();
    }

    private void getGcmRegistrationId() {
        // Get a GCM registration ID, which will start the network
        // call sequence after it completes
        GetGcmRegistrationIdTask gcmRegistrationIdTask = new GetGcmRegistrationIdTask(this);
        gcmRegistrationIdTask.execute();
    }

    private void startNetworkRequests(String gcmRegistrationId, boolean isNewRegId) {
        // Make the networks requests just once
        if (!mHasStartedNetworkRequests) {
            Activity parentActivity = getActivity();
            if (parentActivity != null) {
                mHasStartedNetworkRequests = true;

                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "startNetworkRequests: gcmRegistrationId = " + gcmRegistrationId);
                    Log.i(TAG, "startNetworkRequests: isNewRegId = " + isNewRegId);
                }

                // Require an alert update if the registration ID is new or has changed
                mIsAlertRegistrationRequired = isNewRegId;

                // Start the data sync in the background
                runDataSync(gcmRegistrationId, ConcurrencyType.SYNCHRONOUS);
            }
            else {
                showFailedSyncDialog();
            }
        }
    }

    private void runDataSync(String latestGcmRegistrationId, ConcurrencyType concurrencyType) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "runDataSync: sending network requests");
        }

        Activity parentActivity = getActivity();
        if (parentActivity != null) {
            // Must be synchronous, since all requests need a token
            GetControlPropertiesRequest getControlPropertiesRequest = new GetControlPropertiesRequest.Builder(this)
                    .setPriority(Priority.VERY_HIGH)
                    .setConcurrencyType(ConcurrencyType.SYNCHRONOUS)
                    .build();
            NetworkUtils.queueNetworkRequest(getControlPropertiesRequest);

            GetVenuesRequest getVenuesRequest = new GetVenuesRequest.Builder(this)
                    .setPriority(Priority.HIGH)
                    .setConcurrencyType(concurrencyType)
                    .build();
            NetworkUtils.queueNetworkRequest(getVenuesRequest);

            GetPoisRequest getPoisRequest = new GetPoisRequest.Builder(this)
                    .setPriority(Priority.NORMAL)
                    .setConcurrencyType(concurrencyType)
                    .build();
            NetworkUtils.queueNetworkRequest(getPoisRequest);

            GetEventSeriesRequest getEventSeriesRequest = new GetEventSeriesRequest.Builder(this)
                    .setPriority(Priority.NORMAL)
                    .setConcurrencyType(concurrencyType)
                    .build();
            NetworkUtils.queueNetworkRequest(getEventSeriesRequest);

            GetOfferVendorsRequest getOfferVendorsRequest = new GetOfferVendorsRequest.Builder(this)
                    .setPriority(Priority.NORMAL)
                    .setConcurrencyType(concurrencyType)
                    .build();
            NetworkUtils.queueNetworkRequest(getOfferVendorsRequest);

            GetNewsRequest getNewsRequest = new GetNewsRequest.Builder(this)
                    .setPriority(Priority.LOW)
                    .setConcurrencyType(concurrencyType)
                    .setPageSize(GetNewsRequest.PAGE_SIZE_ALL)
                    .setPage(1)
                    .build();
            NetworkUtils.queueNetworkRequest(getNewsRequest);

            GetQueuesByPageRequest getQueuesByPageRequest = new GetQueuesByPageRequest.Builder(this)
                    .setPriority(Priority.NORMAL)
                    .setConcurrencyType(concurrencyType)
                    .setPageSize(GetQueuesByPageRequest.PAGE_SIZE_ALL)
                    .setPage(1)
                    .build();
            NetworkUtils.queueNetworkRequest(getQueuesByPageRequest);

            GetPhotoFrameExperienceRequest getPhotoFrameExperienceRequest = new GetPhotoFrameExperienceRequest.Builder(this)
                    .setPriority(Priority.NORMAL)
                    .setConcurrencyType(concurrencyType)
                    .build();
            NetworkUtils.queueNetworkRequest(getPhotoFrameExperienceRequest);

			GetMobilePagesRequest getMobilePagesRequest = new GetMobilePagesRequest.Builder(this)
					.setPriority(Priority.NORMAL)
					.setConcurrencyType(concurrencyType)
					.build();
			NetworkUtils.queueNetworkRequest(getMobilePagesRequest);

            // Push registration, only register if there is a GCM registration ID
            if (latestGcmRegistrationId != null && !latestGcmRegistrationId.isEmpty()) {
                // Only register if the registration ID is new or has changed
                if (mIsAlertRegistrationRequired) {
					UniversalAppState uoState = UniversalAppStateManager.getInstance();

                    RegisterAlertsRequest.Builder registerAlertsRequestBuilder = new RegisterAlertsRequest.Builder(this)
                            .setPriority(Priority.HIGH)
                            .setConcurrencyType(concurrencyType)
                            .setDeviceId(latestGcmRegistrationId)
                            .setInPark(uoState.isInResortGeofence());

                    // If there is an old registration ID, let the service tier know so it can migrate the alerts
                    String oldGcmRegistrationId = uoState.getGcmRegistrationId();
                    if (oldGcmRegistrationId != null && !oldGcmRegistrationId.isEmpty()) {
                        registerAlertsRequestBuilder.setPreviousDeviceId(oldGcmRegistrationId);
                    }

                    RegisterAlertsRequest registerAlertsRequest = registerAlertsRequestBuilder.build();
                    NetworkUtils.queueNetworkRequest(registerAlertsRequest);
                }
                // Otherwise, sync alerts since the device has been registered
                else {
                    GetRegisteredAlertsRequest getRegisteredAlertsRequest = new GetRegisteredAlertsRequest.Builder(this)
                            .setPriority(Priority.LOW)
                            .setConcurrencyType(concurrencyType)
                            .build();
                    NetworkUtils.queueNetworkRequest(getRegisteredAlertsRequest);
                }
            }
            else {
                // If there isn't a GCM registration ID, artificially fail the request
                mReceivedRegisterAlertsResponse = true;
                mRegisterAlertsSuccess = false;
                checkStateToStartApp();
            }

            // Orlando commerce calls
            if(BuildConfigUtils.isLocationFlavorOrlando()) {
                // Get remote text from Tridion
                GetTridionConfigsRequest getTridionConfigsRequest = new GetTridionConfigsRequest.Builder(this)
                        .setConcurrencyType(concurrencyType)
                        .build();
                NetworkUtils.queueNetworkRequest(getTridionConfigsRequest);

                // Get lists of counties
                GetCountriesRequest getCountriesRequest = new GetCountriesRequest.Builder(this)
                        .setConcurrencyType(concurrencyType)
                        .build();
                NetworkUtils.queueNetworkRequest(getCountriesRequest);

                // Get lists of states
                GetStateRequest getStateRequest = new GetStateRequest.Builder(this)
                        .setConcurrencyType(concurrencyType)
                        .build();
                NetworkUtils.queueNetworkRequest(getStateRequest);

                // Get remote config settings for commerce
                GetCommerceEnabledRequest getCommerceEnabledRequest = new GetCommerceEnabledRequest.Builder(this)
                        .setConcurrencyType(concurrencyType)
                        .build();
                NetworkUtils.queueNetworkRequest(getCommerceEnabledRequest);
            }

            // Start the service to start making the calls on the queue
            NetworkUtils.startNetworkService();
        }
    }

	private void startHomePageAndFinish() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "startHomePageAndFinish");
		}

		// Make sure the app only starts once
		Activity parentActivity = getActivity();
		if (!mHasStartedApp && parentActivity != null && !parentActivity.isFinishing()) {
			mHasStartedApp = true;
			Bundle homeActivityBundle = HomeActivity.newInstanceBundle(HomeActivity.START_PAGE_TYPE_HOME);
			startActivity(new Intent(parentActivity, HomeActivity.class).putExtras(homeActivityBundle));
			parentActivity.finish();
		}
	}

	@SuppressWarnings("ConstantConditions")
    private void checkStateToStartApp() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "checkStateToStartApp: mIsSyncRequired = " + mIsSyncRequired + " mIsAlertUpdateRequired = " + mIsAlertRegistrationRequired);
			Log.d(TAG, "checkStateToStartApp: mReceivedTokenResponse = " + mReceivedTokenResponse + " mGetTokenSuccess = " + mGetTokenSuccess);
			Log.d(TAG, "checkStateToStartApp: mReceivedVenuesResponse = " + mReceivedVenuesResponse + " mGetVenuesSuccess = " + mGetVenuesSuccess);
			Log.d(TAG, "checkStateToStartApp: mReceivedPoisResponse = " + mReceivedPoisResponse + " mGetPoisSuccess = " + mGetPoisSuccess);
			Log.d(TAG, "checkStateToStartApp: mReceivedRegisterAlertsResponse = " + mReceivedRegisterAlertsResponse + " mRegisterAlertsSuccess = " + mRegisterAlertsSuccess);
		}

		// Only start the app after the initial animation has ended
		if (mHasAnimationCompleted && mHasStartedNetworkRequests) {
			// If syncing and an alert update aren't required, start the app
			if (!mIsSyncRequired && !mIsAlertRegistrationRequired) {
				Log.i(TAG, "checkStateToStartApp: sync nor alert registration are required, starting app");
				startHomePageAndFinish();
			}
			// If only a sync is required, only start if the sync completes
			else if (mIsSyncRequired && !mIsAlertRegistrationRequired) {
				if (mGetTokenSuccess && mGetVenuesSuccess && mGetPoisSuccess) {
					Log.i(TAG, "checkStateToStartApp: sync only is required, and completed successfully, starting app");
					startHomePageAndFinish();
				}
				else if (mReceivedTokenResponse && mReceivedVenuesResponse && mReceivedPoisResponse) {
					Log.i(TAG, "checkStateToStartApp: sync only is required, but failed, showing failed sync dialog");
					showFailedSyncDialog();
				}
			}
			// If only an alert update is required, only start if the alert update completes
			else if (!mIsSyncRequired && mIsAlertRegistrationRequired) {
				if (mRegisterAlertsSuccess) {
					Log.i(TAG, "checkStateToStartApp: alert registration only is required, and completed successfully, starting app");
					startHomePageAndFinish();
				}
				else if (mReceivedRegisterAlertsResponse) {
					Log.i(TAG, "checkStateToStartApp: alert registration only is required, but failed, showing failed sync dialog");
					showFailedSyncDialog();
				}
			}
			// If a sync and alert update is required, only start if everything succeeds
			else if (mIsSyncRequired && mIsAlertRegistrationRequired) {
				if (mGetTokenSuccess && mGetVenuesSuccess && mGetPoisSuccess && mRegisterAlertsSuccess) {
					Log.i(TAG, "checkStateToStartApp: sync and alert registration is required, and completed successfully, starting app");
					startHomePageAndFinish();
				}
				else if (mReceivedTokenResponse && mReceivedVenuesResponse && mReceivedPoisResponse && mReceivedRegisterAlertsResponse) {
					Log.i(TAG, "checkStateToStartApp: sync and alert registration is required, but failed, showing failed sync dialog");
					showFailedSyncDialog();
				}
			}
		}
		else {
			Log.i(TAG, "checkStateToStartApp: animation has not completed");
		}
	}

    private void showFailedSyncDialog() {
        // Only show the dialog if it isn't already showing, and the state is valid
        Activity parentActivity = getActivity();
        if (parentActivity != null && !parentActivity.isFinishing() && !mHasSavedInstanceState) {

            FragmentManager fragmentManager = getChildFragmentManager();
            boolean isFailedSyncDialogShowing =
                    fragmentManager.findFragmentByTag(FailedSyncDialogFragment.class.getSimpleName()) != null;

            if (!isFailedSyncDialogShowing) {
                try {
                    FailedSyncDialogFragment dialogFragment = new FailedSyncDialogFragment();
                    dialogFragment.show(fragmentManager, dialogFragment.getClass().getSimpleName());
                } catch (Exception e) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "checkStateToStartApp: exception trying to show failed sync dialog", e);
                    }

                    // Log the exception to crittercism
                    CrashAnalyticsUtils.logHandledException(e);
                }
            }
        }
    }

    private static class InstallSecurityProviderTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<LauncherFragment> mLauncherFragment;

        public InstallSecurityProviderTask(LauncherFragment launcherFragment) {
            mLauncherFragment = new WeakReference<>(launcherFragment);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "doInBackground");
            }

            final LauncherFragment launcherFragment = mLauncherFragment.get();
            if (launcherFragment != null && launcherFragment.getActivity() != null) {
                Context appContext = launcherFragment.getActivity().getApplicationContext();

                // If needed, installs the latest security provider for network calls. This allows < 5.0 to use
                // the latest TLS protocols
                try {
                    ProviderInstaller.installIfNeeded(appContext);
                } catch (GooglePlayServicesRepairableException e) {
                    // Thrown when Google Play Services is not installed, up-to-date, or enabled
                    // Show dialog to allow users to install, update, or otherwise enable Google Play services.
                    GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), launcherFragment.getActivity(), 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, "Google Play Services not available.");
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void nothing) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onPostExecute");
            }

            final LauncherFragment launcherFragment = mLauncherFragment.get();
            if (launcherFragment != null) {
                // After completing get the GCM registration ID
                launcherFragment.getGcmRegistrationId();
            }
        }
    }

    private static class GetGcmRegistrationIdTask extends AsyncTask<Void, Void, String> {
        private final WeakReference<LauncherFragment> mLauncherFragment;
        private boolean mIsNewRegId;

        public GetGcmRegistrationIdTask(LauncherFragment launcherFragment) {
            mLauncherFragment = new WeakReference<>(launcherFragment);
            mIsNewRegId = false;
        }

        @Override
        protected String doInBackground(Void... params) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "doInBackground");
            }

            String curGcmRegId = null;
            String curVersionName = null;

            final LauncherFragment launcherFragment = mLauncherFragment.get();
            if (launcherFragment != null && launcherFragment.getActivity() != null) {
                Context appContext = launcherFragment.getActivity().getApplicationContext();

                // Get current GCM state
                UniversalAppState uoState = UniversalAppStateManager.getInstance();
                String storedGcmRegId = uoState.getGcmRegistrationId();
                String storedGcmRegIdVersionName = uoState.getGcmRegistrationIdVersionName();

                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "doInBackground: storedGcmRegId = " + storedGcmRegId);
                    Log.i(TAG, "doInBackground: storedGcmRegIdVersionName = " + storedGcmRegIdVersionName);
                }

                PackageInfo pInfo = UniversalOrlandoApplication.getPackageInfo();
                if (pInfo != null) {
                    curVersionName = pInfo.versionName;
                }

                // Get a new registration ID if it doesn't exist, or if the app version changes
                if (storedGcmRegId == null || storedGcmRegId.isEmpty()
                    || storedGcmRegIdVersionName == null || storedGcmRegIdVersionName.isEmpty()
                    || !storedGcmRegIdVersionName.equals(curVersionName)) {
                    mIsNewRegId = true;

                    try {
                        GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(appContext);
                        String newGcmRegistrationId = googleCloudMessaging.register(PushUtils.getSenderId());

                        if (BuildConfig.DEBUG) {
                            Log.i(TAG, "doInBackground: got a new GCM registration ID = " + newGcmRegistrationId);
                        }

                        curGcmRegId = newGcmRegistrationId;
                    } catch (IOException e) {
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG, "doInBackground: exception trying to register for GCM", e);
                        }
                    }
                }
                // Otherwise, just return the stored registration ID
                else {
                    curGcmRegId = storedGcmRegId;
                }
            }

            return curGcmRegId;
        }

        @Override
        protected void onPostExecute(String gcmRegistrationId) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onPostExecute");
            }

            final LauncherFragment launcherFragment = mLauncherFragment.get();
            if (launcherFragment != null) {
                // After getting GCM registration ID start the sync requests
                launcherFragment.startNetworkRequests(gcmRegistrationId, mIsNewRegId);
            }
        }
    }

    // Private static class using weak references to prevent leaking a context
    @SuppressLint("NewApi")
    private static class ViewShowerRunnable implements Runnable {
        private final WeakReference<LauncherFragment> mLauncherFragment;

        public ViewShowerRunnable(LauncherFragment launcherFragment) {
            mLauncherFragment = new WeakReference<>(launcherFragment);
        }

        @Override
        public void run() {

            final LauncherFragment launcherFragment = mLauncherFragment.get();
            if (launcherFragment != null) {
                if (launcherFragment.mLogo != null && launcherFragment.mLogoFlare1 != null
                        && launcherFragment.mLogoFlare2 != null && launcherFragment.mLogoGlow != null) {
                    launcherFragment.mLogo.setVisibility(View.VISIBLE);
                    launcherFragment.mLogoFlare1.setVisibility(View.VISIBLE);
                    launcherFragment.mLogoFlare2.setVisibility(View.VISIBLE);
                    launcherFragment.mLogoGlow.setVisibility(View.VISIBLE);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        launcherFragment.mLogoFlare1.setImageAlpha((int) (SPLASH_GLOW_IMAGE_OPACITY * 255));
                        launcherFragment.mLogoFlare2.setImageAlpha((int) (SPLASH_GLOW_IMAGE_OPACITY * 255));
                        launcherFragment.mLogoGlow.setImageAlpha((int) (SPLASH_GLOW_IMAGE_OPACITY * 255));
                    } else {
                        launcherFragment.mLogoFlare1.setAlpha(SPLASH_GLOW_IMAGE_OPACITY);
                        launcherFragment.mLogoFlare2.setAlpha(SPLASH_GLOW_IMAGE_OPACITY);
                        launcherFragment.mLogoGlow.setAlpha(SPLASH_GLOW_IMAGE_OPACITY);
                    }
                }
            }
        }
    }

    public static class FailedSyncDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
        public FailedSyncDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle(R.string.splash_failed_sync_title);
            alertDialogBuilder.setMessage(R.string.splash_failed_sync_message);
            alertDialogBuilder.setPositiveButton(R.string.splash_failed_sync_confirm_button, null);

            Dialog dialog = alertDialogBuilder.create();
            dialog.setCanceledOnTouchOutside(false);

            return dialog;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);

            // Close the app after the user has acknowledged the message, they
            // will need to restart to retry a sync
            Activity parentActivity = getActivity();
            if (parentActivity != null) {
                parentActivity.finish();
            }
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // Close the app after the user has acknowledged the message, they
                    // will need to restart to retry a sync
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
