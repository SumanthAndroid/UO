package com.universalstudios.orlandoresort.controller.userinterface.debug;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.model.network.analytics.CrashAnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.alert.GetRegisteredAlertsRequest;
import com.universalstudios.orlandoresort.model.network.domain.alert.RegisterAlertsRequest;
import com.universalstudios.orlandoresort.model.network.domain.control.GetControlPropertiesRequest;
import com.universalstudios.orlandoresort.model.network.domain.control.GetControlPropertiesResponse;
import com.universalstudios.orlandoresort.model.network.domain.events.GetEventSeriesRequest;
import com.universalstudios.orlandoresort.model.network.domain.news.GetNewsRequest;
import com.universalstudios.orlandoresort.model.network.domain.news.GetNewsResponse;
import com.universalstudios.orlandoresort.model.network.domain.offers.GetOfferVendorsRequest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.GetPoisRequest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.GetPoisResponse;
import com.universalstudios.orlandoresort.model.network.domain.venue.GetVenuesRequest;
import com.universalstudios.orlandoresort.model.network.domain.venue.GetVenuesResponse;
import com.universalstudios.orlandoresort.model.network.push.PushUtils;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.Priority;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.OnUniversalAppStateChangeListener;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;
import com.universalstudios.orlandoresort.model.state.debug.UniversalOrlandoDebugState;
import com.universalstudios.orlandoresort.model.state.debug.UniversalOrlandoDebugStateManager;
import com.universalstudios.orlandoresort.view.TintUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * @author Steven Byle
 */
public class DebugOptionsActivity extends NetworkRefreshActivity implements OnClickListener, OnCheckedChangeListener, OnUniversalAppStateChangeListener {
	private static final String TAG = DebugOptionsActivity.class.getSimpleName();

	public static final String TITLE = "Debug Options";
	public static final String SYNC_CONTROL_PROPERTIES = "Sync Control Properties";
	public static final String SYNC_VENUES = "Sync Venues";
	public static final String SYNC_POIS = "Sync Points of Interest";
	public static final String SYNC_NEWS = "Sync Park News";
	public static final String SYNC_ALL_PARK_DATA = "Sync All Park Data";
	public static final String SYNC_ALERTS = "Sync Alerts";
	public static final String SYNC_PUSH_REG = "Sync Push Reg";
	public static final String SYNC_EVENT_SERIES = "Sync Event Series";
	public static final String SYNC_OFFERS = "Sync Offers";
	public static final String SHOW_GEOFENCES = "Show Geofences";
	public static final String START_MOCK_LOCATION_PROVIDER = "Start Mock Location Provider";
	public static final String MOCK_LOCATION = "Location";
	public static final String VIEW_APP_STATE = "View App State";

	private LinearLayout mDebugOptionsLinearLayout;
	private Switch sideSwitchShowGeofences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		setContentView(R.layout.activity_debug_options);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle(TITLE);

		mDebugOptionsLinearLayout = (LinearLayout) findViewById(R.id.debug_options_list_layout);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onRestoreInstanceState");
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onResume");
		}

		// Update the UI when resuming
		refreshUi();

		// Listen for state changes
		UniversalAppStateManager.registerStateChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onPause");
		}

		// Stop listening for state changes
		UniversalAppStateManager.unregisterStateChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreateOptionsMenu");
		}

		// Adds items to the action bar
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.action_refresh, menu);
		TintUtils.tintAllMenuItems(menu, this);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.action_refresh:
				refreshUi();
				return true;
			default:
				return(super.onOptionsItemSelected(item));
		}
	}

	@Override
	public void onClick(View v) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onClick");
		}

		Object tag = v.getTag();
		if (tag != null && tag instanceof String) {
			if (tag.equals(SYNC_CONTROL_PROPERTIES)) {
				GetControlPropertiesRequest getControlPropertiesRequest = new GetControlPropertiesRequest.Builder(this)
				.build();
				NetworkUtils.queueNetworkRequest(getControlPropertiesRequest);

				NetworkUtils.startNetworkService();
			}
			else if (tag.equals(SYNC_VENUES)) {
				GetVenuesRequest getVenuesRequest = new GetVenuesRequest.Builder(this)
				.build();
				NetworkUtils.queueNetworkRequest(getVenuesRequest);

				NetworkUtils.startNetworkService();
			}
			else if (tag.equals(SYNC_POIS)) {
				GetPoisRequest getPoisRequest = new GetPoisRequest.Builder(this)
				.build();
				NetworkUtils.queueNetworkRequest(getPoisRequest);

				NetworkUtils.startNetworkService();
			}
			else if (tag.equals(SYNC_NEWS)) {
				GetNewsRequest getNewsRequest = new GetNewsRequest.Builder(this)
				.setPageSize(GetNewsRequest.PAGE_SIZE_ALL)
				.setPage(1)
				.build();
				NetworkUtils.queueNetworkRequest(getNewsRequest);

				NetworkUtils.startNetworkService();
			}
			else if (tag.equals(SYNC_PUSH_REG)) {
				UniversalAppState state = UniversalAppStateManager.getInstance();

				RegisterAlertsRequest registerAlertsRequest = new RegisterAlertsRequest.Builder(this)
				.setDeviceId(state.getGcmRegistrationId())
				.setInPark(state.isInResortGeofence())
				.build();
				NetworkUtils.queueNetworkRequest(registerAlertsRequest);

				NetworkUtils.startNetworkService();
			}
			else if (tag.equals(SYNC_ALERTS)) {
				GetRegisteredAlertsRequest getRegisteredAlertsRequest = new GetRegisteredAlertsRequest.Builder(this)
				.setPriority(Priority.NORMAL)
				.setConcurrencyType(NetworkRequest.ConcurrencyType.SYNCHRONOUS)
				.build();
				NetworkUtils.queueNetworkRequest(getRegisteredAlertsRequest);

				NetworkUtils.startNetworkService();
			}
			else if (tag.equals(SYNC_EVENT_SERIES)) {
			    GetEventSeriesRequest getEventSeriesRequest = new GetEventSeriesRequest.Builder(this)
			    .setPriority(Priority.NORMAL)
                .setConcurrencyType(NetworkRequest.ConcurrencyType.SYNCHRONOUS)
                .build();
			    NetworkUtils.queueNetworkRequest(getEventSeriesRequest);
			    
			    NetworkUtils.startNetworkService();
			}
			else if (tag.equals(SYNC_OFFERS)) {
			    GetOfferVendorsRequest getOfferVendorsRequest = new GetOfferVendorsRequest.Builder(this)
			    .setPriority(Priority.NORMAL)
                .setConcurrencyType(NetworkRequest.ConcurrencyType.SYNCHRONOUS)
                .build();
                NetworkUtils.queueNetworkRequest(getOfferVendorsRequest);
                
                NetworkUtils.startNetworkService();
			}
			else if (tag.equals(SYNC_ALL_PARK_DATA)) {
				GetControlPropertiesRequest getControlPropertiesRequest = new GetControlPropertiesRequest.Builder(this)
				.setPriority(Priority.VERY_HIGH)
				.setConcurrencyType(NetworkRequest.ConcurrencyType.SYNCHRONOUS)
				.build();
				NetworkUtils.queueNetworkRequest(getControlPropertiesRequest);

				GetVenuesRequest getVenuesRequest = new GetVenuesRequest.Builder(this)
				.setPriority(Priority.HIGH)
				.setConcurrencyType(NetworkRequest.ConcurrencyType.SYNCHRONOUS)
				.build();
				NetworkUtils.queueNetworkRequest(getVenuesRequest);

				GetPoisRequest getPoisRequest = new GetPoisRequest.Builder(this)
				.setPriority(Priority.NORMAL)
				.setConcurrencyType(NetworkRequest.ConcurrencyType.SYNCHRONOUS)
				.build();
				NetworkUtils.queueNetworkRequest(getPoisRequest);

				GetNewsRequest getNewsRequest = new GetNewsRequest.Builder(this)
				.setPriority(Priority.LOW)
				.setConcurrencyType(NetworkRequest.ConcurrencyType.SYNCHRONOUS)
				.setPageSize(GetNewsRequest.PAGE_SIZE_ALL)
				.setPage(1)
				.build();
				NetworkUtils.queueNetworkRequest(getNewsRequest);

				UniversalAppState state = UniversalAppStateManager.getInstance();
				RegisterAlertsRequest registerAlertsRequest = new RegisterAlertsRequest.Builder(this)
				.setDeviceId(state.getGcmRegistrationId())
				.setInPark(state.isInResortGeofence())
				.setPriority(Priority.LOW)
				.setConcurrencyType(NetworkRequest.ConcurrencyType.SYNCHRONOUS)
				.build();
				NetworkUtils.queueNetworkRequest(registerAlertsRequest);

				GetRegisteredAlertsRequest getRegisteredAlertsRequest = new GetRegisteredAlertsRequest.Builder(this)
				.setPriority(Priority.VERY_LOW)
				.setConcurrencyType(NetworkRequest.ConcurrencyType.SYNCHRONOUS)
				.build();
				NetworkUtils.queueNetworkRequest(getRegisteredAlertsRequest);
				
				GetEventSeriesRequest getEventSeriesRequest = new GetEventSeriesRequest.Builder(this)
				.setPriority(Priority.VERY_LOW)
				.setConcurrencyType(NetworkRequest.ConcurrencyType.SYNCHRONOUS)
				.build();
				NetworkUtils.queueNetworkRequest(getEventSeriesRequest);
				
				GetOfferVendorsRequest getOfferVendorsRequest = new GetOfferVendorsRequest.Builder(this)
                .setPriority(Priority.NORMAL)
                .setConcurrencyType(NetworkRequest.ConcurrencyType.SYNCHRONOUS)
                .build();
                NetworkUtils.queueNetworkRequest(getOfferVendorsRequest);

				NetworkUtils.startNetworkService();
			}
			else if (tag.equals(START_MOCK_LOCATION_PROVIDER)) {
				startMockLocationProvider();
			}
			else if (tag.equals(SHOW_GEOFENCES)) {
				sideSwitchShowGeofences.toggle();
			}
			else if (tag.equals(MOCK_LOCATION)) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				boolean isDialogFragmentShowing =
						fragmentManager.findFragmentByTag(SetMockLocationDialogFragment.class.getSimpleName()) != null;

				if (!isDialogFragmentShowing) {
					SetMockLocationDialogFragment dialogFragment = new SetMockLocationDialogFragment();
					dialogFragment.show(fragmentManager, dialogFragment.getClass().getSimpleName());
				}
			}
			else if (tag.equals(VIEW_APP_STATE)) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				boolean isDialogFragmentShowing =
						fragmentManager.findFragmentByTag(ShowControlPropertiesDialogFragment.class.getSimpleName()) != null;

				if (!isDialogFragmentShowing) {
					ShowControlPropertiesDialogFragment dialogFragment = new ShowControlPropertiesDialogFragment();
					dialogFragment.show(fragmentManager, dialogFragment.getClass().getSimpleName());
				}
			} else {
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onClick: unrecognized button press");
				}
			}
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCheckedChanged: isChecked = " + isChecked);
		}

		Object tag = buttonView.getTag();
		if (tag != null && tag instanceof String) {
			if (tag.equals(SHOW_GEOFENCES)) {
				// Update the show geofence state
				UniversalOrlandoDebugStateManager.getInstance().setShowGeofencesOnMap(isChecked);
				UniversalOrlandoDebugStateManager.saveInstance();
			}
			else {
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onCheckedChanged: unrecognized switch changed");
				}
			}
		}
	}

	@Override
	public void onUniversalAppStateChange(UniversalAppState universalAppState) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					// Update the UI when the state changes
					refreshUi();
				}
				catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "onUniversalAppStateChange: exception trying to refresh UI", e);
					}

					// Log the exception to crittercism
					Crittercism.logHandledException(e);
				}
			}
		});
	}

	@Override
	public void handleNetworkResponse(NetworkResponse networkResponse) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "handleNetworkResponse");
		}

		if (networkResponse instanceof GetControlPropertiesResponse) {
			GetControlPropertiesResponse response = (GetControlPropertiesResponse) networkResponse;

			if (response.isHttpStatusCodeSuccess()) {
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "handleNetworkResponse: GetControlPropertiesResponse = success");
				}
			}
			else {
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "handleNetworkResponse: GetControlPropertiesResponse = failure");
				}
			}
		}
		else if (networkResponse instanceof GetVenuesResponse) {
			GetVenuesResponse response = (GetVenuesResponse) networkResponse;

			if (response.isHttpStatusCodeSuccess()) {
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "handleNetworkResponse: GetVenuesRequest = success");
				}
			}
			else {
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "handleNetworkResponse: GetVenuesRequest = failure");
				}
			}
		}
		else if (networkResponse instanceof GetPoisResponse) {
			GetPoisResponse	response = (GetPoisResponse) networkResponse;

			if (response.isHttpStatusCodeSuccess()) {
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "handleNetworkResponse: GetPoisResponse = success");
				}
			}
			else {
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "handleNetworkResponse: GetPoisResponse = failure");
				}
			}
		}
		else if (networkResponse instanceof GetNewsResponse) {
			GetNewsResponse	response = (GetNewsResponse) networkResponse;

			if (response.isHttpStatusCodeSuccess()) {
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "handleNetworkResponse: GetNewsResponse = success");
				}
			}
			else {
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "handleNetworkResponse: GetNewsResponse = failure");
				}
			}
		}
		else {
			super.handleNetworkResponse(networkResponse);
		}
	}

	private void startMockLocationProvider() {
		String curMockLocation = UniversalOrlandoDebugStateManager.getInstance().getMockLocationString();
		Intent serviceIntent = new Intent(this, MockLocationProviderService.class);
		serviceIntent.putExtras(MockLocationProviderService.newInstanceBundle(curMockLocation));
		startService(serviceIntent);
	}

	private void refreshUi() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "refreshUi");
		}

		UniversalAppState state = UniversalAppStateManager.getInstance();
		mDebugOptionsLinearLayout.removeAllViews();

		// Device Info
		View sectionHeaderView = createSectionHeaderView(mDebugOptionsLinearLayout, "Device Info");
		mDebugOptionsLinearLayout.addView(sectionHeaderView);

		View itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Device",  Build.MANUFACTURER + " " + Build.MODEL, null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"OS Version", Build.VERSION.RELEASE + " (API " + Build.VERSION.SDK_INT + ")", null);
		mDebugOptionsLinearLayout.addView(itemView);

		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int widthPx = displayMetrics.widthPixels;
		int heightPx = displayMetrics.heightPixels;
		int widthDp = Math.round(widthPx / displayMetrics.density);
		int heightDp = Math.round(heightPx / displayMetrics.density);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Density Bucket", displayMetrics.densityDpi + " dpi", null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Resolution (dp)", widthDp + " x " + heightDp + " dp", null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Resolution (px)", widthPx + " x " + heightPx + " px", null);
		mDebugOptionsLinearLayout.addView(itemView);

		// App Info
		sectionHeaderView = createSectionHeaderView(mDebugOptionsLinearLayout, "App Version Info");
		mDebugOptionsLinearLayout.addView(sectionHeaderView);

		PackageInfo pInfo = UniversalOrlandoApplication.getPackageInfo();
		if (pInfo != null) {
			itemView = createItemView(mDebugOptionsLinearLayout, R.mipmap.ic_launcher,
					"App Name", getString(getApplicationInfo().labelRes), null);
			mDebugOptionsLinearLayout.addView(itemView);

			itemView = createItemView(mDebugOptionsLinearLayout, null,
					"Application ID", BuildConfig.APPLICATION_ID, null);
			mDebugOptionsLinearLayout.addView(itemView);

			itemView = createItemView(mDebugOptionsLinearLayout, null,
					"Package Name", pInfo.packageName, null);
			mDebugOptionsLinearLayout.addView(itemView);

			itemView = createItemView(mDebugOptionsLinearLayout, null,
					"Version Name", "" + pInfo.versionName, null);
			mDebugOptionsLinearLayout.addView(itemView);

			itemView = createItemView(mDebugOptionsLinearLayout, null,
					"Version Code", "" + pInfo.versionCode, null);
			mDebugOptionsLinearLayout.addView(itemView);

			itemView = createItemView(mDebugOptionsLinearLayout, null,
					"Location Flavor", BuildConfig.FLAVOR_DIMENSION_LOCATION, null);
			mDebugOptionsLinearLayout.addView(itemView);

			itemView = createItemView(mDebugOptionsLinearLayout, null,
					"Service Environment Flavor", BuildConfig.FLAVOR_DIMENSION_SERVICE_ENVIRONMENT, null);
			mDebugOptionsLinearLayout.addView(itemView);

			itemView = createItemView(mDebugOptionsLinearLayout, null,
					"Build Type", BuildConfig.BUILD_TYPE, null);
			mDebugOptionsLinearLayout.addView(itemView);
		}

		// Build Configuration
		sectionHeaderView = createSectionHeaderView(mDebugOptionsLinearLayout, "Build Configuration");
		mDebugOptionsLinearLayout.addView(sectionHeaderView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Park Services URL", BuildConfig.PARK_SERVICES_BASE_URL, null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Config Provided Park Services URL", state.getServicesBaseUrl(), null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Commerce Services URL", BuildConfig.COMMERCE_SERVICES_BASE_URL, null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Commerce Services Store ID", BuildConfig.COMMERCE_STORE_ID, null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Commerce Services Catalog ID", BuildConfig.COMMERCE_SALES_CATALOG_ID, null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Commerce Services Shop Junction Path", BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION, null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"SSL Certificate Verification", BuildConfig.SSL_CERT_VERIFICATION_ENABLED ? "Enabled" : "Disabled", null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Network Debug Tracing", BuildConfig.NETWORK_TRACING_ENABLED ? "Enabled" : "Disabled", null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Push Sender ID", PushUtils.getSenderId(), null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Crash Analytics", BuildConfig.CRASH_ANALYTICS_ENABLED ? "Enabled" : "Disabled", null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Crash Analytics App ID", CrashAnalyticsUtils.getCrashAnalyticsAppId(), null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Logging", BuildConfig.LOGGING_ENABLED ? "Enabled" : "Disabled", null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Mock Locations", BuildConfig.MOCK_LOCATIONS_ENABLED ? "Enabled" : "Disabled", null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Screen Rotation", BuildConfig.SCREEN_ROTATION_ENABLED ? "Enabled" : "Disabled", null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Show Missing Remote Text", BuildConfig.SHOW_MISSING_REMOTE_TEXT ? "Enabled" : "Disabled", null);
		mDebugOptionsLinearLayout.addView(itemView);

		// Network info
		sectionHeaderView = createSectionHeaderView(mDebugOptionsLinearLayout, "Network Info");
		mDebugOptionsLinearLayout.addView(sectionHeaderView);

		// Format the sync times
		SimpleDateFormat sdfOutTime = new SimpleDateFormat("EEE, LLL d h:mm:ss a z", Locale.US);

		Long tokenExpiresInSec = state.getTokenExpirationUnix();
		String tokenExpiresInSecString = tokenExpiresInSec != null ? "" + sdfOutTime.format(new Date(tokenExpiresInSec * 1000)) : "Never";
		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Park Services Token Expires", tokenExpiresInSecString, null);
		mDebugOptionsLinearLayout.addView(itemView);

		Long lastControlPropSyncMs = state.getDateOfLastControlPropSyncInMillis();
		String lastControlPropSyncString = lastControlPropSyncMs != null ? "" + sdfOutTime.format(new Date(lastControlPropSyncMs)) : "Never";
		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Last Control Prop Sync", lastControlPropSyncString, null);
		mDebugOptionsLinearLayout.addView(itemView);

		Long lastVenueSyncMs = state.getDateOfLastVenueSyncInMillis();
		String lastVenueSyncString = lastVenueSyncMs != null ? "" + sdfOutTime.format(new Date(lastVenueSyncMs)) : "Never";
		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Last Venue Sync", lastVenueSyncString, null);
		mDebugOptionsLinearLayout.addView(itemView);

		Long lastPoiSyncMs = state.getDateOfLastPoiSyncInMillis();
		String lastPoiSyncString = lastPoiSyncMs != null ? "" + sdfOutTime.format(new Date(lastPoiSyncMs)) : "Never";
		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Last POI Sync", lastPoiSyncString, null);
		mDebugOptionsLinearLayout.addView(itemView);

		Long lastParkNewsSyncMs = state.getDateOfLastParkNewsSyncInMillis();
		String lastParkNewsSyncString = lastParkNewsSyncMs != null ? "" + sdfOutTime.format(new Date(lastParkNewsSyncMs)) : "Never";
		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Last Park News Sync", lastParkNewsSyncString, null);
		mDebugOptionsLinearLayout.addView(itemView);

		Long lastPushRegSyncMs = state.getDateOfLastPushRegSyncInMillis();
		String lastPushRegSyncString = lastPushRegSyncMs != null ? "" + sdfOutTime.format(new Date(lastPushRegSyncMs)) : "Never";
		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Last Push Reg Sync", lastPushRegSyncString, null);
		mDebugOptionsLinearLayout.addView(itemView);

		Long lastAlertSyncMs = state.getDateOfLastAlertSyncInMillis();
		String lastAlertSyncString = lastAlertSyncMs != null ? "" + sdfOutTime.format(new Date(lastAlertSyncMs)) : "Never";
		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Last Alert Sync", lastAlertSyncString, null);
		mDebugOptionsLinearLayout.addView(itemView);
		
		Long lastEventSeriesSyncMs = state.getDateOfLastEventSeriesSyncInMillis();
        String lastEventSeriesSyncString = lastEventSeriesSyncMs != null ? "" + sdfOutTime.format(new Date(lastEventSeriesSyncMs)) : "Never";
        itemView = createItemView(mDebugOptionsLinearLayout, null,
                "Last Event Series Sync", lastEventSeriesSyncString, null);
        mDebugOptionsLinearLayout.addView(itemView);
        
        Long lastOffersSyncMs = state.getDateOfLastOffersSyncInMillis();
        String lastOffersSyncString = lastOffersSyncMs != null ? "" + sdfOutTime.format(new Date(lastOffersSyncMs)) : "Never";
        itemView = createItemView(mDebugOptionsLinearLayout, null,
                "Last Offers Sync", lastOffersSyncString, null);
        mDebugOptionsLinearLayout.addView(itemView);

		Long lastQueuesSyncMs = state.getDateOfLastQueuesSyncInMillis();
		String lastQueuesSyncString = lastQueuesSyncMs != null ? "" + sdfOutTime.format(new Date(lastQueuesSyncMs)) : "Never";
		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Last Queues Sync", lastQueuesSyncString, null);
		mDebugOptionsLinearLayout.addView(itemView);

		itemView = createItemView(mDebugOptionsLinearLayout, R.drawable.ic_menu_settings,
				VIEW_APP_STATE, null, this);
		mDebugOptionsLinearLayout.addView(itemView);

		// Network controls
		sectionHeaderView = createSectionHeaderView(mDebugOptionsLinearLayout, "Network Controls");
		mDebugOptionsLinearLayout.addView(sectionHeaderView);

		itemView = createItemView(mDebugOptionsLinearLayout, R.drawable.ic_menu_settings, SYNC_ALL_PARK_DATA, null, this);
		mDebugOptionsLinearLayout.addView(itemView);
		itemView = createItemView(mDebugOptionsLinearLayout, R.drawable.ic_menu_settings, SYNC_CONTROL_PROPERTIES, null, this);
		mDebugOptionsLinearLayout.addView(itemView);
		itemView = createItemView(mDebugOptionsLinearLayout, R.drawable.ic_menu_settings, SYNC_VENUES, null, this);
		mDebugOptionsLinearLayout.addView(itemView);
		itemView = createItemView(mDebugOptionsLinearLayout, R.drawable.ic_menu_settings, SYNC_POIS, null, this);
		mDebugOptionsLinearLayout.addView(itemView);
		itemView = createItemView(mDebugOptionsLinearLayout, R.drawable.ic_menu_settings, SYNC_NEWS, null, this);
		mDebugOptionsLinearLayout.addView(itemView);
		itemView = createItemView(mDebugOptionsLinearLayout, R.drawable.ic_menu_settings, SYNC_PUSH_REG, null, this);
		mDebugOptionsLinearLayout.addView(itemView);
		itemView = createItemView(mDebugOptionsLinearLayout, R.drawable.ic_menu_settings, SYNC_ALERTS, null, this);
		mDebugOptionsLinearLayout.addView(itemView);
		itemView = createItemView(mDebugOptionsLinearLayout, R.drawable.ic_menu_settings, SYNC_EVENT_SERIES, null, this);
        mDebugOptionsLinearLayout.addView(itemView);
        itemView = createItemView(mDebugOptionsLinearLayout, R.drawable.ic_menu_settings, SYNC_OFFERS, null, this);
        mDebugOptionsLinearLayout.addView(itemView);

		// Geofence info
		sectionHeaderView = createSectionHeaderView(mDebugOptionsLinearLayout, "Geofence Info");
		mDebugOptionsLinearLayout.addView(sectionHeaderView);

		Long dateOfLastGeofenceCheckInMillis = state.getDateOfLastGeofenceCheckInMillis();
		String lastGeofenceCheckString = dateOfLastGeofenceCheckInMillis != null ? "" + sdfOutTime.format(new Date(dateOfLastGeofenceCheckInMillis)) : "Never";
		itemView = createItemView(mDebugOptionsLinearLayout, null,
				"Last Manual Check", lastGeofenceCheckString, null);
		mDebugOptionsLinearLayout.addView(itemView);

		if (BuildConfigUtils.isLocationFlavorHollywood()) {
			itemView = createItemView(mDebugOptionsLinearLayout, null,
					"In Universal Hollywood Resort", state.isInUniversalHollywoodResortGeofence() ? "Yes" : "No", null);
			mDebugOptionsLinearLayout.addView(itemView);
			itemView = createItemView(mDebugOptionsLinearLayout, null,
					"In Universal Studios Hollywood", state.isInUniversalStudiosHollywoodGeofence() ? "Yes" : "No", null);
			mDebugOptionsLinearLayout.addView(itemView);
			itemView = createItemView(mDebugOptionsLinearLayout, null,
					"In CityWalk Hollwood", state.isInCityWalkHollywoodGeofence() ? "Yes" : "No", null);
			mDebugOptionsLinearLayout.addView(itemView);
		} else {
			itemView = createItemView(mDebugOptionsLinearLayout, null,
					"In Universal Orlando Resort", state.isInUniversalOrlandoResortGeofence() ? "Yes" : "No", null);
			mDebugOptionsLinearLayout.addView(itemView);
			itemView = createItemView(mDebugOptionsLinearLayout, null,
					"In Islands of Adventure", state.isInIslandsOfAdventureGeofence() ? "Yes" : "No", null);
			mDebugOptionsLinearLayout.addView(itemView);
			itemView = createItemView(mDebugOptionsLinearLayout, null,
					"In Universal Studios Florida", state.isInUniversalStudiosFloridaGeofence() ? "Yes" : "No", null);
			mDebugOptionsLinearLayout.addView(itemView);
			itemView = createItemView(mDebugOptionsLinearLayout, null,
					"In CityWalk", state.isInCityWalkOrlandoGeofence() ? "Yes" : "No", null);
			mDebugOptionsLinearLayout.addView(itemView);
			itemView = createItemView(mDebugOptionsLinearLayout, null,
					"In Wet 'n Wild", state.isInWetNWildGeofence() ? "Yes" : "No", null);
			mDebugOptionsLinearLayout.addView(itemView);
		}

		// Geofence controls
		sectionHeaderView = createSectionHeaderView(mDebugOptionsLinearLayout, "Geofence Controls");
		mDebugOptionsLinearLayout.addView(sectionHeaderView);

		UniversalOrlandoDebugState debugState = UniversalOrlandoDebugStateManager.getInstance();
		boolean showGeofencesOnMap = debugState.isShowGeofencesOnMap();

		itemView = createItemView(mDebugOptionsLinearLayout, null,
				SHOW_GEOFENCES, null, this);
		sideSwitchShowGeofences = (Switch) itemView.findViewById(R.id.list_debug_item_switch);
		sideSwitchShowGeofences.setTag(SHOW_GEOFENCES);
		sideSwitchShowGeofences.setOnCheckedChangeListener(this);
		sideSwitchShowGeofences.setChecked(showGeofencesOnMap);
		sideSwitchShowGeofences.setVisibility(View.VISIBLE);
		mDebugOptionsLinearLayout.addView(itemView);

		// Mock Location controls
		if (BuildConfig.MOCK_LOCATIONS_ENABLED) {
			sectionHeaderView = createSectionHeaderView(mDebugOptionsLinearLayout, "Mock Location Controls");
			mDebugOptionsLinearLayout.addView(sectionHeaderView);

			itemView = createItemView(mDebugOptionsLinearLayout, R.drawable.ic_menu_settings, START_MOCK_LOCATION_PROVIDER, null, this);
			mDebugOptionsLinearLayout.addView(itemView);

			String curMockLocation = UniversalOrlandoDebugStateManager.getInstance().getMockLocationString();
			if (curMockLocation == null || !MockLocationProviderService.getMockLocations().contains(curMockLocation)) {
				curMockLocation = MockLocationProviderService.getMockLocations().get(0);
				UniversalOrlandoDebugStateManager.getInstance().setMockLocationString(curMockLocation);
				UniversalOrlandoDebugStateManager.saveInstance();
			}
			itemView = createItemView(mDebugOptionsLinearLayout, R.drawable.ic_menu_settings,
					MOCK_LOCATION, curMockLocation, this);
			mDebugOptionsLinearLayout.addView(itemView);
		}
	}

	private static View createItemView(ViewGroup parentLayout, Integer iconResId, String primaryTextString, String primarySubTextString, OnClickListener onClickListener) {
		LayoutInflater inflater = LayoutInflater.from(parentLayout.getContext());

		ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.list_debug_item, parentLayout, false);
		ImageView iconImage = (ImageView) itemView.findViewById(R.id.list_debug_item_icon_image);
		TextView primaryText = (TextView) itemView.findViewById(R.id.list_debug_item_primary_text);
		TextView primarySubText = (TextView) itemView.findViewById(R.id.list_debug_item_primary_sub_text);
		TextView secondaryText = (TextView) itemView.findViewById(R.id.list_debug_item_secondary_text);
		View divider = itemView.findViewById(R.id.list_debug_item_top_divider);

		if (iconResId != null) {
			iconImage.setImageResource(iconResId);
		}
		iconImage.setVisibility(iconResId != null ? View.VISIBLE : View.GONE);

		if (primaryTextString != null) {
			primaryText.setText(primaryTextString);
		}
		primaryText.setVisibility(primaryTextString != null ? View.VISIBLE : View.GONE);

		// Hide for now
		secondaryText.setVisibility(View.GONE);

		if (primarySubTextString != null) {
			primarySubText.setText(primarySubTextString);
		}
		primarySubText.setVisibility(primarySubTextString != null ? View.VISIBLE : View.GONE);

		boolean showDivider = parentLayout.getChildCount() > 0
				&& (parentLayout.getChildAt(parentLayout.getChildCount() - 1).getId() == R.id.list_debug_item_root_container);
		divider.setVisibility(showDivider ? View.VISIBLE : View.GONE);

		// Set the tag to reference back to clicks
		if (onClickListener != null) {
			itemView.setTag(primaryTextString);
			itemView.setOnClickListener(onClickListener);
		}
		return itemView;
	}

	private static View createSectionHeaderView(ViewGroup parentLayout, String primaryTextString) {
		LayoutInflater inflater = LayoutInflater.from(parentLayout.getContext());

		ViewGroup sectionHeaderView = (ViewGroup) inflater.inflate(R.layout.list_debug_section_header, parentLayout, false);
		TextView primaryText = (TextView) sectionHeaderView.findViewById(R.id.list_debug_section_header_title_text);

		if (primaryTextString != null) {
			primaryText.setText(primaryTextString);
		}
		primaryText.setVisibility(primaryTextString != null ? View.VISIBLE : View.GONE);
		return sectionHeaderView;
	}

	public static class SetMockLocationDialogFragment extends DialogFragment {
		public SetMockLocationDialogFragment() {
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			alertDialogBuilder.setTitle("Choose a Mock Location");
			alertDialogBuilder.setNegativeButton("Cancel", null);

			String curMockLocation = UniversalOrlandoDebugStateManager.getInstance().getMockLocationString();
			final List<String> mockLocations = MockLocationProviderService.getMockLocations();
			int curMockLocationIndex = mockLocations.indexOf(curMockLocation);

			alertDialogBuilder.setSingleChoiceItems(
					mockLocations.toArray(new CharSequence[mockLocations.size()]), curMockLocationIndex, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Activity parentActivity = getActivity();

							String chosenLocation = mockLocations.get(which);
							UniversalOrlandoDebugStateManager.getInstance().setMockLocationString(chosenLocation);
							UniversalOrlandoDebugStateManager.saveInstance();
							dialog.dismiss();

							// Refresh the UI and start/update the mock location service
							if (parentActivity instanceof DebugOptionsActivity) {
								((DebugOptionsActivity) parentActivity).refreshUi();
								((DebugOptionsActivity) parentActivity).startMockLocationProvider();
							}
						}
					});

			Dialog dialog = alertDialogBuilder.create();
			dialog.setCanceledOnTouchOutside(false);
			return dialog;
		}

		@Override
		public void onDismiss(DialogInterface dialog) {
			super.onDismiss(dialog);
		}
	}

	public static class ShowControlPropertiesDialogFragment extends DialogFragment {
		public ShowControlPropertiesDialogFragment() {
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			alertDialogBuilder.setTitle("Control Properties");

			UniversalAppState uoState = UniversalAppStateManager.getInstance();
			alertDialogBuilder.setMessage(uoState.toJsonString(true));
			alertDialogBuilder.setPositiveButton("OK", null);

			Dialog dialog = alertDialogBuilder.create();
			dialog.setCanceledOnTouchOutside(true);

			// Hack to change text size
			dialog.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(12);
			textView.setTextIsSelectable(true);
			return dialog;
		}

		@Override
		public void onDismiss(DialogInterface dialog) {
			super.onDismiss(dialog);
		}
	}

}