/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.geofence;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.global.OrientationLockActivity;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
public abstract class GeofenceActivity extends OrientationLockActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {
	private static final String TAG = GeofenceActivity.class.getSimpleName();

	private List<Geofence> mCurrentGeofences;
	private PendingIntent mGeofencePendingIntent;
	private GoogleApiClient mGoogleApiClient;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate");
		}

		mGeofencePendingIntent = null;
		mGoogleApiClient = null;

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

			// Add the geofences
			if (!getLocationClient().isConnected() && !getLocationClient().isConnecting()) {

				// Hollywood geofences
				if(BuildConfigUtils.isLocationFlavorHollywood()) {
					mCurrentGeofences = GeofenceUtils.getGeofenceListForAllOfUniversalHollywood();
				}
				// Orlando geofences
				else {
					mCurrentGeofences = GeofenceUtils.getGeofenceListForAllOfUniversalOrlando();
				}
				getLocationClient().connect();
			}
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onConnected");
		}

        try {
            GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

            // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
            // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
            // is already inside that geofence.
            builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

            // Add the geofences to be monitored by geofencing service.
            builder.addGeofences(mCurrentGeofences);

            LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, builder.build(), getGeofencePendingIntent()).setResultCallback(this);;
        } catch (SecurityException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (BuildConfig.DEBUG) {
			Log.e(TAG, "onConnectionFailed: connectionResult.getErrorCode() = " + connectionResult.getErrorCode());
		}
	}

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient = null;
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onAddGeofencesResult");
        }

        // If adding the geocodes was successful
        if (status.isSuccess()) {
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "onAddGeofencesResult: success");
            }

            // Wipe out any old geofence state on new connections, the geofences connected will update after
            UniversalAppStateManager.wipeGeofenceState();
        }

        getLocationClient().disconnect();
    }

    private GoogleApiClient getLocationClient() {
		if (mGoogleApiClient == null) {
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addApi(LocationServices.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.build();
		}
		return mGoogleApiClient;
	}

	private PendingIntent getGeofencePendingIntent() {
		// Get a PendingIntent that Location Services issues when a geofence transition occurs
		if (mGeofencePendingIntent == null) {
			Intent intent = new Intent(GeofenceTransitionBroadcastReceiver.ACTION_GEOFENCE_TRANSITION_RECEIVED);
			mGeofencePendingIntent = PendingIntent.getBroadcast(
					this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		}
		return mGeofencePendingIntent;
	}
}
