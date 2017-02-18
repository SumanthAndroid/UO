package com.universalstudios.orlandoresort.model.network.domain.alert;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.model.alerts.Alert;
import com.universalstudios.orlandoresort.model.alerts.RideOpenAlert;
import com.universalstudios.orlandoresort.model.alerts.WaitTimeAlert;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.service.ServiceEndpointUtils;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.AlertsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 
 * 
 * @author Steven Byle
 */
public class GetRegisteredAlertsRequest extends UniversalOrlandoServicesRequest implements Callback<GetRegisteredAlertsResponse> {
	private static final String TAG = GetRegisteredAlertsRequest.class.getSimpleName();

	private GetRegisteredAlertsRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType) {
		super(senderTag, priority, concurrencyType, null);
	}

	@SuppressWarnings("javadoc")
	public static class Builder extends BaseNetworkRequestBuilder<Builder> {

		public Builder(NetworkRequestSender sender) {
			super(sender);
		}

		@Override
		protected Builder getThis() {
			return this;
		}

		public GetRegisteredAlertsRequest build() {
			return new GetRegisteredAlertsRequest(senderTag, priority, concurrencyType);
		}
	}

	@Override
	public void makeNetworkRequest(RestAdapter restAdapter) {
		super.makeNetworkRequest(restAdapter);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "makeNetworkRequest");
		}

		// Inject the device ID from the state
		String gcmRegId = UniversalAppStateManager.getInstance().getGcmRegistrationId();

		UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

		switch (getConcurrencyType()) {
			case SYNCHRONOUS:
				try {
					GetRegisteredAlertsResponse response = services.getRegisteredAlerts(
							ServiceEndpointUtils.getCity(),
							gcmRegId);
					success(response, null);
				}
				catch (RetrofitError retrofitError) {
					failure(retrofitError);
				}
				break;
			case ASYNCHRONOUS:
				services.getRegisteredAlerts(
						ServiceEndpointUtils.getCity(),
						gcmRegId,
						this);
				break;
			default:
				throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
		}
	}

	@Override
	public void success(GetRegisteredAlertsResponse getRegisteredAlertsResponse, Response response) {
		if (getRegisteredAlertsResponse != null) {
			// Sync alerts to match the server (assume server is source of truth)
			syncWaitTimeAlertsWithDatabase(getRegisteredAlertsResponse.getWaitTimeAlerts());
			syncRideOpenAlertsWithDatabase(getRegisteredAlertsResponse.getRideOpenAlertPoiIds());

			// Store the last time alerts were synced
			UniversalAppState uoState = UniversalAppStateManager.getInstance();
			uoState.setDateOfLastAlertSyncInMillis(new Date().getTime());
			UniversalAppStateManager.saveInstance();

			// If the server geofence status does not match the device, update the server
			Boolean inParkFromResp = getRegisteredAlertsResponse.getInPark();
			if (inParkFromResp == null || !inParkFromResp.equals(uoState.isInResortGeofence())) {

				RegisterAlertsRequest registerAlertsRequest = new RegisterAlertsRequest.Builder(null)
				.setPriority(Priority.NORMAL)
				.setDeviceId(uoState.getGcmRegistrationId())
				.setInPark(uoState.isInResortGeofence())
				.build();
				NetworkUtils.queueNetworkRequest(registerAlertsRequest);
				NetworkUtils.startNetworkService();
			}
		}
		else {
			getRegisteredAlertsResponse = new GetRegisteredAlertsResponse();
		}

		// Inform any listeners after saving the response
		super.handleSuccess(getRegisteredAlertsResponse, response);
	}

	@Override
	public void failure(RetrofitError retrofitError) {
		super.handleFailure(new GetRegisteredAlertsResponse(), retrofitError);
	}

	private void syncWaitTimeAlertsWithDatabase(List<RegisteredWaitTimeAlert> registeredWaitTimeAlerts) {
		if (registeredWaitTimeAlerts == null) {
			registeredWaitTimeAlerts = new ArrayList<RegisteredWaitTimeAlert>();
		}

		// Query for existing wait time alerts
		ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
		String[] projection = {
				AlertsTable.COL_FULL_ID,
				AlertsTable.COL_FULL_ALERT_ID_STRING,
				AlertsTable.COL_FULL_ALERT_TYPE_ID,
				AlertsTable.COL_FULL_NOTIFY_MIN_BEFORE,
				AlertsTable.COL_FULL_NOTIFY_THRESHOLD_IN_MIN,
				AlertsTable.COL_FULL_SHOW_TIME,
				AlertsTable.COL_FULL_CREATED_DATE_IN_MILLIS,
				AlertsTable.COL_FULL_ALERT_OBJECT_JSON,
				PointsOfInterestTable.COL_FULL_POI_ID,
				PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
				PointsOfInterestTable.COL_FULL_DISPLAY_NAME,
				PointsOfInterestTable.COL_FULL_POI_OBJECT_JSON,
		};
		StringBuilder selection = new StringBuilder(AlertsTable.COL_FULL_ALERT_TYPE_ID)
		.append(" = ").append(AlertsTable.VAL_ALERT_TYPE_ID_WAIT_TIME);

		// Set the query on the alerts table joining the POI for each alert
		String sqlTableStatement = new StringBuilder(UniversalOrlandoContentUris.CUSTOM_TABLE.toString()).append("/")
				.append(AlertsTable.TABLE_NAME)
				.append("\nLEFT OUTER JOIN ").append(PointsOfInterestTable.TABLE_NAME)
				.append(" ON (").append(AlertsTable.COL_FULL_POI_ID)
				.append(" = ").append(PointsOfInterestTable.COL_FULL_POI_ID).append(")")
				.toString();

		Uri contentUri = Uri.parse(sqlTableStatement);
		Cursor data = contentResolver.query(contentUri,
				projection, selection.toString(), null, null);

		// Go through all registered alerts and sync them with the local ones
		List<WaitTimeAlert> newWaitTimeAlertsList = new ArrayList<WaitTimeAlert>();
		for (RegisteredWaitTimeAlert registeredWaitTimeAlert : registeredWaitTimeAlerts) {
			// Skip any invalid alerts
			if (registeredWaitTimeAlert == null
					|| registeredWaitTimeAlert.getRideId() == null || registeredWaitTimeAlert.getRideId() == 0
					|| registeredWaitTimeAlert.getThresholdInMin() == null || registeredWaitTimeAlert.getThresholdInMin() == 0) {
				continue;
			}

			boolean alertFoundInDb = false;

			// Start the cursor at the first row
			if (data != null && data.moveToFirst()) {
				// Go through every alert in the database
				do {
					// If the wait time alert is found, check to see if it needs to be updated
					long alertPoiIdFromDb = data.getLong(data.getColumnIndex(PointsOfInterestTable.COL_POI_ID));
					long serverPoiId = registeredWaitTimeAlert.getRideId();
					if (alertPoiIdFromDb == serverPoiId) {

						int serverThresholdInMin = registeredWaitTimeAlert.getThresholdInMin();
						int alertThresholdInMinFromDb = data.getInt(data.getColumnIndex(AlertsTable.COL_NOTIFY_THRESHOLD_IN_MIN));

						// If the alert in the database is different than the server's alert, update the database version
						if (alertThresholdInMinFromDb != serverThresholdInMin) {
							String alertObjectJson = data.getString(data.getColumnIndex(AlertsTable.COL_ALERT_OBJECT_JSON));
							Integer alertTypeId = data.getInt(data.getColumnIndex(AlertsTable.COL_ALERT_TYPE_ID));
							WaitTimeAlert alertFromDb = (WaitTimeAlert) Alert.fromJson(alertObjectJson, alertTypeId);

							alertFromDb.setNotifyThresholdInMin(serverThresholdInMin);
							updateWaitTimeAlertInDatabase(alertFromDb, contentResolver);
						}

						// Stop looping after finding the alert
						alertFoundInDb = true;
						break;
					}
				} while (data.moveToNext());
			}

			// If the alert wasn't found, add it to the list to be inserted
			if (!alertFoundInDb) {
				long poiId = registeredWaitTimeAlert.getRideId();
				int notifyThresholdInMin = registeredWaitTimeAlert.getThresholdInMin();

				// Get the POI name from the DB
				String[] poiProjection = { PointsOfInterestTable.COL_FULL_DISPLAY_NAME };
				StringBuilder poiSelection = new StringBuilder(PointsOfInterestTable.COL_FULL_POI_ID)
				.append(" = ").append(poiId);

				Cursor poiData = contentResolver.query(UniversalOrlandoContentUris.POINTS_OF_INTEREST,
						poiProjection, poiSelection.toString(), null, null);

				if (poiData != null && poiData.moveToFirst()) {
					String poiName = poiData.getString(poiData.getColumnIndex(PointsOfInterestTable.COL_DISPLAY_NAME));
					WaitTimeAlert newWaitTimeAlert = new WaitTimeAlert(poiId, poiName, notifyThresholdInMin);
					newWaitTimeAlertsList.add(newWaitTimeAlert);
				}

				// Close the cursor
				if (poiData != null && !poiData.isClosed()) {
					poiData.close();
				}
			}
		}

		// Insert any new alerts
		insertWaitTimeAlertsInDatabase(newWaitTimeAlertsList, contentResolver);

		// Delete any alerts that are not in the server anymore
		deleteOldWaitTimeAlertsFromDatabase(registeredWaitTimeAlerts, contentResolver);

		// Close the cursor
		if (data != null && !data.isClosed()) {
			data.close();
		}
	}

	private static ContentValues createWaitTimeAlertContentValues(WaitTimeAlert waitTimeAlert) {

		final ContentValues contentValues = new ContentValues();
		contentValues.put(AlertsTable.COL_ALERT_ID_STRING, waitTimeAlert.getIdString());
		contentValues.put(AlertsTable.COL_ALERT_TYPE_ID, AlertsTable.VAL_ALERT_TYPE_ID_WAIT_TIME);
		contentValues.put(AlertsTable.COL_POI_ID, waitTimeAlert.getPoiId());
		contentValues.put(AlertsTable.COL_POI_NAME, waitTimeAlert.getPoiName());
		contentValues.put(AlertsTable.COL_NOTIFY_THRESHOLD_IN_MIN, waitTimeAlert.getNotifyThresholdInMin());
		contentValues.put(AlertsTable.COL_CREATED_DATE_IN_MILLIS, System.currentTimeMillis());
		contentValues.put(AlertsTable.COL_ALERT_OBJECT_JSON, waitTimeAlert.toJson());

		return contentValues;
	}

	private static void insertWaitTimeAlertsInDatabase(List<WaitTimeAlert> newWaitTimeAlertList, ContentResolver contentResolver) {
		if (newWaitTimeAlertList == null || newWaitTimeAlertList.size() == 0) {
			return;
		}

		if (BuildConfig.DEBUG) {
			Log.i(TAG, "insertWaitTimeAlertsInDatabase: inserting new items = " + newWaitTimeAlertList.size());
		}

		ContentValues contentValuesArray[] = new ContentValues[newWaitTimeAlertList.size()];
		for (int i = 0; i < newWaitTimeAlertList.size(); i++) {
			WaitTimeAlert newWaitTimeAlert = newWaitTimeAlertList.get(i);

			ContentValues contentValues = createWaitTimeAlertContentValues(newWaitTimeAlert);
			contentValuesArray[i] = contentValues;
		}

		try {
			contentResolver.bulkInsert(UniversalOrlandoContentUris.ALERTS, contentValuesArray);
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "insertWaitTimeAlertsInDatabase: exception inserting new items into the database", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void updateWaitTimeAlertInDatabase(WaitTimeAlert waitTimeAlertToUpdate, ContentResolver contentResolver) {
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "updateWaitTimeAlertInDatabase: updating item = " + waitTimeAlertToUpdate.getPoiName());
		}

		ContentValues contentValues = createWaitTimeAlertContentValues(waitTimeAlertToUpdate);
		String selection = new StringBuilder(AlertsTable.COL_ALERT_ID_STRING)
		.append(" = '").append(waitTimeAlertToUpdate.getIdString()).append("'")
		.append(" AND ").append(AlertsTable.COL_ALERT_TYPE_ID)
		.append(" = ").append(AlertsTable.VAL_ALERT_TYPE_ID_WAIT_TIME)
		.toString();

		try {
			contentResolver.update(UniversalOrlandoContentUris.ALERTS, contentValues, selection, null);
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "updateWaitTimeAlertInDatabase: exception updating item in the database", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void deleteOldWaitTimeAlertsFromDatabase(List<RegisteredWaitTimeAlert> registeredWaitTimeAlertList, ContentResolver contentResolver) {
		if (registeredWaitTimeAlertList == null) {
			registeredWaitTimeAlertList = new ArrayList<RegisteredWaitTimeAlert>();
		}

		StringBuilder selectionBuilder = new StringBuilder(AlertsTable.COL_ALERT_TYPE_ID)
		.append(" = ").append(AlertsTable.VAL_ALERT_TYPE_ID_WAIT_TIME);

		// Delete wait time alerts that don't match the latest id set, if the latest set is empty, delete them all
		if (registeredWaitTimeAlertList.size() > 0) {
			selectionBuilder.append(" AND ").append(AlertsTable.COL_ALERT_ID_STRING)
			.append(" NOT IN (");

			for (int i = 0; i < registeredWaitTimeAlertList.size(); i++) {
				long poiId = registeredWaitTimeAlertList.get(i).getRideId();
				String idString = WaitTimeAlert.computeIdString(poiId);

				if (i > 0) {
					selectionBuilder.append(", ");
				}
				selectionBuilder.append("'").append(idString).append("'");
			}
			selectionBuilder.append(")");
		}

		try {
			int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.ALERTS, selectionBuilder.toString(), null);
			if (BuildConfig.DEBUG) {
				Log.i(TAG, "deleteOldWaitTimeAlertsFromDatabase: old items removed = " + rowsDeleted);
			}
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "deleteOldWaitTimeAlertsFromDatabase: exception deleting old items from the database", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private void syncRideOpenAlertsWithDatabase(List<Long> rideOpenAlertPoiIdsFromServer) {
		if (rideOpenAlertPoiIdsFromServer == null) {
			rideOpenAlertPoiIdsFromServer = new ArrayList<Long>();
		}

		// Query for existing ride open alerts
		ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
		String[] projection = {
				AlertsTable.COL_FULL_ID,
				AlertsTable.COL_FULL_ALERT_ID_STRING,
				AlertsTable.COL_FULL_ALERT_TYPE_ID,
				AlertsTable.COL_FULL_NOTIFY_MIN_BEFORE,
				AlertsTable.COL_FULL_NOTIFY_THRESHOLD_IN_MIN,
				AlertsTable.COL_FULL_SHOW_TIME,
				AlertsTable.COL_FULL_CREATED_DATE_IN_MILLIS,
				AlertsTable.COL_FULL_ALERT_OBJECT_JSON,
				PointsOfInterestTable.COL_FULL_POI_ID,
				PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
				PointsOfInterestTable.COL_FULL_DISPLAY_NAME,
				PointsOfInterestTable.COL_FULL_POI_OBJECT_JSON,
		};
		StringBuilder selection = new StringBuilder(AlertsTable.COL_FULL_ALERT_TYPE_ID)
		.append(" = ").append(AlertsTable.VAL_ALERT_TYPE_ID_RIDE_OPEN);

		// Set the query on the alerts table joining the POI for each alert
		String sqlTableStatement = new StringBuilder(UniversalOrlandoContentUris.CUSTOM_TABLE.toString()).append("/")
				.append(AlertsTable.TABLE_NAME)
				.append("\nLEFT OUTER JOIN ").append(PointsOfInterestTable.TABLE_NAME)
				.append(" ON (").append(AlertsTable.COL_FULL_POI_ID)
				.append(" = ").append(PointsOfInterestTable.COL_FULL_POI_ID).append(")")
				.toString();

		Uri contentUri = Uri.parse(sqlTableStatement);
		Cursor data = contentResolver.query(contentUri,
				projection, selection.toString(), null, null);

		// Go through all registered alerts and sync them with the local ones
		List<RideOpenAlert> newRideOpenAlertList = new ArrayList<RideOpenAlert>();
		for (Long rideOpenAlertPoiIdFromServer : rideOpenAlertPoiIdsFromServer) {
			// Skip any invalid alerts
			if (rideOpenAlertPoiIdFromServer == null || rideOpenAlertPoiIdFromServer.equals(0)) {
				continue;
			}

			boolean alertFoundInDb = false;

			// Start the cursor at the first row
			if (data != null && data.moveToFirst()) {
				// Go through every ride open alert in the database
				do {
					// If the ride open alert is found, note that it exists
					long alertPoiIdFromDb = data.getLong(data.getColumnIndex(PointsOfInterestTable.COL_POI_ID));
					if (alertPoiIdFromDb == rideOpenAlertPoiIdFromServer) {
						// Stop looping after finding the alert
						alertFoundInDb = true;
						break;
					}
				} while (data.moveToNext());
			}

			// If the alert wasn't found, add it to the list to be inserted
			if (!alertFoundInDb) {

				// Get the POI name from the DB
				String[] poiProjection = { PointsOfInterestTable.COL_FULL_DISPLAY_NAME };
				StringBuilder poiSelection = new StringBuilder(PointsOfInterestTable.COL_FULL_POI_ID)
				.append(" = ").append(rideOpenAlertPoiIdFromServer);

				Cursor poiData = contentResolver.query(UniversalOrlandoContentUris.POINTS_OF_INTEREST,
						poiProjection, poiSelection.toString(), null, null);

				if (poiData != null && poiData.moveToFirst()) {
					String poiName = poiData.getString(poiData.getColumnIndex(PointsOfInterestTable.COL_DISPLAY_NAME));
					RideOpenAlert newRideOpenAlert = new RideOpenAlert(rideOpenAlertPoiIdFromServer, poiName);
					newRideOpenAlertList.add(newRideOpenAlert);
				}

				// Close the cursor
				if (poiData != null && !poiData.isClosed()) {
					poiData.close();
				}
			}
		}

		// Insert any new alerts
		insertRideOpenAlertsInDatabase(newRideOpenAlertList, contentResolver);

		// Delete any alerts that are not in the server anymore
		deleteOldRideOpenAlertsFromDatabase(rideOpenAlertPoiIdsFromServer, contentResolver);

		// Close the cursor
		if (data != null && !data.isClosed()) {
			data.close();
		}
	}

	private static ContentValues createRideOpenAlertContentValues(RideOpenAlert rideOpenAlert) {

		final ContentValues contentValues = new ContentValues();
		contentValues.put(AlertsTable.COL_ALERT_ID_STRING, rideOpenAlert.getIdString());
		contentValues.put(AlertsTable.COL_ALERT_TYPE_ID, AlertsTable.VAL_ALERT_TYPE_ID_RIDE_OPEN);
		contentValues.put(AlertsTable.COL_POI_ID, rideOpenAlert.getPoiId());
		contentValues.put(AlertsTable.COL_POI_NAME, rideOpenAlert.getPoiName());
		contentValues.put(AlertsTable.COL_CREATED_DATE_IN_MILLIS, System.currentTimeMillis());
		contentValues.put(AlertsTable.COL_ALERT_OBJECT_JSON, rideOpenAlert.toJson());

		return contentValues;
	}

	private static void insertRideOpenAlertsInDatabase(List<RideOpenAlert> newRideOpenAlertList, ContentResolver contentResolver) {
		if (newRideOpenAlertList == null || newRideOpenAlertList.size() == 0) {
			return;
		}

		if (BuildConfig.DEBUG) {
			Log.i(TAG, "insertRideOpenAlertsInDatabase: inserting new items = " + newRideOpenAlertList.size());
		}

		ContentValues contentValuesArray[] = new ContentValues[newRideOpenAlertList.size()];
		for (int i = 0; i < newRideOpenAlertList.size(); i++) {
			RideOpenAlert newRideOpenAlert = newRideOpenAlertList.get(i);

			ContentValues contentValues = createRideOpenAlertContentValues(newRideOpenAlert);
			contentValuesArray[i] = contentValues;
		}

		try {
			contentResolver.bulkInsert(UniversalOrlandoContentUris.ALERTS, contentValuesArray);
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "insertRideOpenAlertsInDatabase: exception inserting new items into the database", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void deleteOldRideOpenAlertsFromDatabase(List<Long> rideOpenAlertPoiIdsFromServer, ContentResolver contentResolver) {
		if (rideOpenAlertPoiIdsFromServer == null) {
			rideOpenAlertPoiIdsFromServer = new ArrayList<Long>();
		}

		StringBuilder selectionBuilder = new StringBuilder(AlertsTable.COL_ALERT_TYPE_ID)
		.append(" = ").append(AlertsTable.VAL_ALERT_TYPE_ID_RIDE_OPEN);

		// Delete ride open alerts that don't match the latest id set, if the latest set is empty, delete them all
		if (rideOpenAlertPoiIdsFromServer.size() > 0) {
			selectionBuilder.append(" AND ").append(AlertsTable.COL_ALERT_ID_STRING)
			.append(" NOT IN (");

			for (int i = 0; i < rideOpenAlertPoiIdsFromServer.size(); i++) {
				Long poiId = rideOpenAlertPoiIdsFromServer.get(i);

				// Skip any invalid alerts
				if (poiId == null || poiId.equals(0)) {
					continue;
				}

				String idString = RideOpenAlert.computeIdString(poiId);
				if (i > 0) {
					selectionBuilder.append(", ");
				}
				selectionBuilder.append("'").append(idString).append("'");
			}
			selectionBuilder.append(")");
		}

		try {
			int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.ALERTS, selectionBuilder.toString(), null);
			if (BuildConfig.DEBUG) {
				Log.i(TAG, "deleteOldRideOpenAlertsFromDatabase: old items removed = " + rowsDeleted);
			}
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "deleteOldRideOpenAlertsFromDatabase: exception deleting old items from the database", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

}