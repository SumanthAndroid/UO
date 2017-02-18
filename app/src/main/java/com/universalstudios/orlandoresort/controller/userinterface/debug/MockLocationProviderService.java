package com.universalstudios.orlandoresort.controller.userinterface.debug;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.notification.NotificationUtils;

import java.util.ArrayList;

/**
 * @author Steven Byle
 */
public class MockLocationProviderService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	private static final String TAG = MockLocationProviderService.class.getSimpleName();

	public static final String MOCK_LOCATION_INSIDE_UO = "Inside UO";
	public static final String MOCK_LOCATION_INSIDE_IOA = "Inside IOA";
	public static final String MOCK_LOCATION_INSIDE_USF = "Inside USF";
	public static final String MOCK_LOCATION_INSIDE_CW = "Inside CW";
	public static final String MOCK_LOCATION_OUTSIDE_UO = "Outside UO";
	public static final String MOCK_LOCATION_INSIDE_WNW = "Inside WNW";
	public static final String MOCK_LOCATION_ROUTE_PARKING_LOT_TO_ET = "Route - Parking lot to ET";
	public static final String MOCK_LOCATION_ROUTE_HARRY_POTTER_TO_SPIDERMAN = "Route - HP to Spiderman";
	public static final String MOCK_LOCATION_ROUTE_LOST_CONTINENT_TO_JURASSIC = "Route - LC to Jurassic";

	private static final String KEY_ARG_STOP_SERVICE = "KEY_ARG_STOP_SERVICE";
	private static final String KEY_ARG_MOCK_LOCATION = "KEY_ARG_MOCK_LOCATION";

	private static final String TOAST_MOCK_LOCATIONS_DISABLED = "\"Allow Mock Locations\" must be enabled in Settings > Developer Options";
	private static final long UPDATE_PAUSE_IN_MS = 1 * 1000;
	private static final LatLng sLatLngInsideUo = new LatLng(28.472160, -81.462174);
	private static final LatLng sLatLngInsideIoa = new LatLng(28.472744, -81.473136);
	private static final LatLng sLatLngInsideUsf = new LatLng(28.476596, -81.468014);
	private static final LatLng sLatLngInsideCw = new LatLng(28.472643, -81.466529);
	private static final LatLng sLatLngOutsideUo = new LatLng(28.487454, -81.472533);
	private static final LatLng sLatLngInsideWnw = new LatLng(28.461565, -81.465523);

	private static ArrayList<String> sMockLocations;
	private boolean mServiceRunning;
	private NotificationManager mNotificationManager;
	private ProvideMockLocationsTask mProvideMockLocationsTask;
	private GoogleApiClient mGoogleApiClient;
	private String mMockLocation;

	private static LatLng[] sLatLngArrayParkingLotToET = {
		new LatLng(28.47216, -81.462174),
		new LatLng(28.4738675, -81.4637365),
		new LatLng(28.47354043, -81.4646821),
		new LatLng(28.47320641, -81.46535894),
		new LatLng(28.47315237, -81.46559581),
		new LatLng(28.47352766, -81.46585155),
		new LatLng(28.47394334, -81.46587342),
		new LatLng(28.47431928, -81.46604123),
		new LatLng(28.47487028, -81.46628967),
		new LatLng(28.47496483, -81.466663),
		new LatLng(28.47515514, -81.46693666),
		new LatLng(28.47521829, -81.46718301),
		new LatLng(28.47530316, -81.46700313),
		new LatLng(28.47554376, -81.46737579),
		new LatLng(28.47578332, -81.46716918),
		new LatLng(28.47607987, -81.46713297),
		new LatLng(28.47651845, -81.4674317),
		new LatLng(28.47679124, -81.46758006),
		new LatLng(28.47718222, -81.46773378),
		new LatLng(28.47765194, -81.46787795),
		new LatLng(28.47807028, -81.46770378),
		new LatLng(28.47803591, -81.46732357),
		new LatLng(28.47800729, -81.46701017)
	};

	private static LatLng[] sLatLngArrayHarryPotterToSpiderman = {
		new LatLng(28.472595, -81.47342),
		new LatLng( 28.472686, -81.473299),
		new LatLng(28.472966, -81.472781),
		new LatLng(28.4731921, -81.4724794),
		new LatLng(28.47316697, -81.47207218),
		new LatLng(28.4730761, -81.4718693),
		new LatLng(28.4728756, -81.4717494),
		new LatLng(28.47265206, -81.4715026),
		new LatLng(28.47264106, -81.47121469),
		new LatLng(28.472766, -81.470893),
		new LatLng(28.472745, -81.470559),
		new LatLng(28.47249104, -81.47039536),
		new LatLng(28.472491, -81.470128),
		new LatLng(28.47255401, -81.46955116),
		new LatLng(28.47223999, -81.46957791),
		new LatLng(28.47191006, -81.46950743),
		new LatLng(28.471241, -81.469516),
		new LatLng(28.47105239, -81.4696582),
		new LatLng(28.47084646, -81.46979248),
		new LatLng(28.47067799, -81.4700252)
	};

	private static LatLng[] sLatLngArrayLostContinentToJurassic = {
		new LatLng(28.47316697, -81.47207218),
		new LatLng(28.47316697, -81.47207218), // delay 12 seconds
		new LatLng(28.4731921, -81.4724794),
		new LatLng(28.472966, -81.472781),
		new LatLng(28.472686, -81.473299),
		new LatLng(28.472326, -81.473097),
		new LatLng(28.471992, -81.473101),
		new LatLng(28.47174447, -81.4729851),
		new LatLng(28.471374, -81.472903),
		new LatLng(28.471255, -81.473272),
		new LatLng(28.470687, -81.473085)
	};

	public static ArrayList<String> getMockLocations() {
		if (sMockLocations == null) {
			sMockLocations = new ArrayList<String>();
			sMockLocations.add(MOCK_LOCATION_INSIDE_UO);
			sMockLocations.add(MOCK_LOCATION_INSIDE_IOA);
			sMockLocations.add(MOCK_LOCATION_INSIDE_USF);
			sMockLocations.add(MOCK_LOCATION_INSIDE_CW);
			sMockLocations.add(MOCK_LOCATION_OUTSIDE_UO);
			sMockLocations.add(MOCK_LOCATION_INSIDE_WNW);
			sMockLocations.add(MOCK_LOCATION_ROUTE_PARKING_LOT_TO_ET);
			sMockLocations.add(MOCK_LOCATION_ROUTE_HARRY_POTTER_TO_SPIDERMAN);
			sMockLocations.add(MOCK_LOCATION_ROUTE_LOST_CONTINENT_TO_JURASSIC);
		}
		return sMockLocations;
	}

	public static Bundle newInstanceBundle(String mockLocation) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = new Bundle();

		if (mockLocation != null) {
			args.putString(KEY_ARG_MOCK_LOCATION, mockLocation);
		}
		return args;
	}

	public static Bundle newInstanceBundle(boolean stopService) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = new Bundle();

		args.putBoolean(KEY_ARG_STOP_SERVICE, stopService);
		return args;
	}

	public MockLocationProviderService() {
		super();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate");
		}

		// Init variables
		mServiceRunning = false;
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
		mProvideMockLocationsTask = new ProvideMockLocationsTask();

		// Get the notification manager to update notifications
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// If the stop button is pressed, stop the service
		Intent stopServiceIntent = new Intent(this, MockLocationProviderService.class);
		stopServiceIntent.putExtras(newInstanceBundle(true));
		PendingIntent selectStopServicePendingIntent = PendingIntent.getService(this, 0, stopServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Build the notification to show
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
		.setContentTitle("Universal Mock Location Provider")
		.setContentText("Click to stop...")
		.setTicker("Starting mock location provider...")
		.setSmallIcon(R.drawable.ic_stat_mock_location)
		.setContentIntent(selectStopServicePendingIntent)
		.setOngoing(true)
		.setOnlyAlertOnce(true)
		.setUsesChronometer(true);

		// API 16 (4.1) and up
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
		}

		// Start service in foreground
		startForeground(NotificationUtils.NOTIFICATION_ID_MOCK_LOCATION_PROVIDER, notificationBuilder.build());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStartCommand");
		}

		// Get incoming arguments
		Bundle args = intent.getExtras();
		mMockLocation = args.getString(KEY_ARG_MOCK_LOCATION);
		boolean stopService = args.getBoolean(KEY_ARG_STOP_SERVICE, false);

		// If the stop message was sent, stop the service
		if (stopService) {
			stopSelf();
		}
		// Otherwise, if the service isn't running, start the mock location provider
		else if (!mServiceRunning) {
			mServiceRunning = true;

			if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}

		// The OS will restart the service if it is stopped, automatically
		return Service.START_STICKY | Service.START_REDELIVER_INTENT;
	}

	@Override
	public IBinder onBind(Intent intent) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onBind");
		}

		// This is a started service, don't bind it
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroy");
		}

		// Stop the mock location provider
		mServiceRunning = false;
		mProvideMockLocationsTask.cancel(true);

		// Disconnect the current location client
		if (areMockLocationsEnabled() && mGoogleApiClient.isConnected()) {
			try {
				LocationServices.FusedLocationApi.setMockMode(mGoogleApiClient, false);
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		if (mGoogleApiClient.isConnected()) {
			try {
				mGoogleApiClient.disconnect();
			}
			catch (SecurityException e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "onDestroy: SecurityException trying to disconnect location client", e);
				}

				// Log the exception to crittercism
				Crittercism.logHandledException(e);
			}
		}

		// Remove the mock location notification
		mNotificationManager.cancel(NotificationUtils.NOTIFICATION_ID_MOCK_LOCATION_PROVIDER);
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onConnected");
		}

		if (mProvideMockLocationsTask.getStatus() != AsyncTask.Status.RUNNING) {
			mProvideMockLocationsTask.execute(UPDATE_PAUSE_IN_MS);
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (BuildConfig.DEBUG) {
			Log.e(TAG, "onConnectionFailed: connectionResult.getErrorCode() = " + connectionResult.getErrorCode());
		}

		// Shut down, can't test without working location services
		stopSelf();
	}

    @Override
    public void onConnectionSuspended(int i) {
        if (mServiceRunning) {
            // Shut down, can't test without working location services
            stopSelf();
        }
    }

    private boolean areMockLocationsEnabled() {
		return !(Settings.Secure.getString(getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION)
				.equals("0"));
	}

	private class ProvideMockLocationsTask extends AsyncTask<Long, String, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
		@Override
		protected Void doInBackground(Long... params) {
			long intervalWaiting = params[0];

			// If location services is not connected, stop
			if (!mGoogleApiClient.isConnected()) {
				stopSelf();
				return null;
			}

			// Turn on mock mode
			try {
				if (areMockLocationsEnabled() && mGoogleApiClient.isConnected()) {
                    try {
                        LocationServices.FusedLocationApi.setMockMode(mGoogleApiClient, true);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
				}
				else {
					publishProgress(TOAST_MOCK_LOCATIONS_DISABLED);
					stopSelf();
					return null;
				}
			}
			catch (SecurityException e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "doInBackground: SecurityException trying to set mock mode", e);
				}

				// Log the exception to crittercism
				Crittercism.logHandledException(e);

				publishProgress(TOAST_MOCK_LOCATIONS_DISABLED);
				stopSelf();
				return null;
			}

			Location mockLocation = new Location("flp");
			LatLng mockLocationLatLng;

			long locationUpdateNum = 0;
			while (true) {
				// Check if the task has been cancelled
				if (isCancelled()) {
					return null;
				}

				// If location services is not connected, stop
				if (!mGoogleApiClient.isConnected()) {
					stopSelf();
					return null;
				}

				// Get the current mock location and send it to the location client
				mockLocationLatLng = getMockLocationLatLng(mMockLocation, locationUpdateNum);

				// API 17 (4.2) and up
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
					mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
				}
				mockLocation.setTime(System.currentTimeMillis());
				mockLocation.setLatitude(mockLocationLatLng.latitude);
				mockLocation.setLongitude(mockLocationLatLng.longitude);
				mockLocation.setAccuracy((float) (Math.random() * 10 + 5));

				// Make sure mock location are still enabled before setting the mock location
				if (areMockLocationsEnabled()) {
                    try {
                        LocationServices.FusedLocationApi.setMockLocation(mGoogleApiClient, mockLocation);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
				}
				else {
					publishProgress(TOAST_MOCK_LOCATIONS_DISABLED);
					stopSelf();
					return null;
				}

				// Wait before sending the next location
				try {
					if (BuildConfig.DEBUG) {
						//Log.d(TAG, "doInBackground: waiting for " + intervalWaiting + " ms");
					}
					Thread.sleep(intervalWaiting);
				}
				catch (InterruptedException e) {
					if (BuildConfig.DEBUG) {
						//Log.e(TAG, "doInBackground InterruptedException", e);
					}

					return null;
				}

				locationUpdateNum++;
			}
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			UserInterfaceUtils.showToastFromForeground(progress[0], Toast.LENGTH_LONG, getApplicationContext());
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onCancelled(Void result) {
			super.onCancelled(result);
		}
	}

	private static LatLng getMockLocationLatLng(String mockLocation, long locationUpdateNum) {
		if (mockLocation.equals(MOCK_LOCATION_INSIDE_UO)) {
			return sLatLngInsideUo;
		}
		else if (mockLocation.equals(MOCK_LOCATION_INSIDE_IOA)) {
			return sLatLngInsideIoa;
		}
		else if (mockLocation.equals(MOCK_LOCATION_INSIDE_USF)) {
			return sLatLngInsideUsf;
		}
		else if (mockLocation.equals(MOCK_LOCATION_INSIDE_CW)) {
			return sLatLngInsideCw;
		}
		else if (mockLocation.equals(MOCK_LOCATION_OUTSIDE_UO)) {
			return sLatLngOutsideUo;
		}
		else if (mockLocation.equals(MOCK_LOCATION_INSIDE_WNW)) {
		    return sLatLngInsideWnw;
		}
 		else if (mockLocation.equals(MOCK_LOCATION_ROUTE_PARKING_LOT_TO_ET)) {
			return getMockLocationLatLngOnRoute(sLatLngArrayParkingLotToET, 10, locationUpdateNum);
		}
		else if (mockLocation.equals(MOCK_LOCATION_ROUTE_HARRY_POTTER_TO_SPIDERMAN)) {
			return getMockLocationLatLngOnRoute(sLatLngArrayHarryPotterToSpiderman, 10, locationUpdateNum);
		}
		else if (mockLocation.equals(MOCK_LOCATION_ROUTE_LOST_CONTINENT_TO_JURASSIC)) {
			return getMockLocationLatLngOnRoute(sLatLngArrayLostContinentToJurassic, 12, locationUpdateNum);
		}
		else {
			return null;
		}
	}

	private static LatLng getMockLocationLatLngOnRoute(LatLng[] latLngRoute, int totalStepsInBetween, long locationUpdateNum) {
		int totalSteps = (latLngRoute.length - 1) * totalStepsInBetween + 1;

		int step = (int) (locationUpdateNum % totalSteps);
		int stepInBetween = step % totalStepsInBetween;

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getMockLocationLatLngOnRoute: totalStepsInBetween = " + totalStepsInBetween
					+ " totalSteps = " + totalSteps
					+ " step = " + step
					+ " stepInBetween = " + stepInBetween);
		}

		if (stepInBetween == 0) {
			return latLngRoute[step / totalStepsInBetween];
		}
		else {
			int startStep = step / totalStepsInBetween;
			LatLng startLatLng = latLngRoute[startStep];
			LatLng endLatLng = latLngRoute[startStep + 1];
			double stepPercent = ((double) stepInBetween / totalStepsInBetween);
			return new LatLng(
					startLatLng.latitude + (endLatLng.latitude - startLatLng.latitude) * stepPercent,
					startLatLng.longitude + (endLatLng.longitude - startLatLng.longitude) * stepPercent);
		}
	}
}
