package com.universalstudios.orlandoresort.model.network.domain.wayfinding;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PathsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.WaypointsTable;

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
public class GetWayfindingRouteRequest extends UniversalOrlandoServicesRequest implements Callback<GetWayfindingRouteResponse> {
	private static final String TAG = GetWayfindingRouteRequest.class.getSimpleName();

	public static final String WAYPOINT_OPTION_ALL = "All";
	public static final String WAYPOINT_OPTION_NONE = "None";
	public static final String WAYPOINT_OPTION_IN_ROUTE = "InRoute";

	private GetWayfindingRouteRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, GetWayfindingRouteParams getWayfindingRouteParams) {
		super(senderTag, priority, concurrencyType, getWayfindingRouteParams);
	}

	private static class GetWayfindingRouteParams extends NetworkParams {
		private double startLatitude;
		private double startLongitude;
		private long poiId;
		private String waypointOption;

		public GetWayfindingRouteParams() {
			super();
		}
	}

	public static class Builder extends BaseNetworkRequestBuilder<Builder> {
		private final GetWayfindingRouteParams getWayfindingRouteParams;

		public Builder(NetworkRequestSender sender) {
			super(sender);
			getWayfindingRouteParams = new GetWayfindingRouteParams();
		}

		@Override
		protected Builder getThis() {
			return this;
		}

		public Builder setStartLatitude(double startLatitude) {
			getWayfindingRouteParams.startLatitude = startLatitude;
			return getThis();
		}

		public Builder setStartLongitude(double startLongitude) {
			getWayfindingRouteParams.startLongitude = startLongitude;
			return getThis();
		}

		public Builder setPoiId(long poiId) {
			getWayfindingRouteParams.poiId = poiId;
			return getThis();
		}

		public Builder setWaypointOption(String waypointOption) {
			getWayfindingRouteParams.waypointOption = waypointOption;
			return getThis();
		}

		public GetWayfindingRouteRequest build() {
			return new GetWayfindingRouteRequest(senderTag, priority, concurrencyType, getWayfindingRouteParams);
		}
	}

	@Override
	public void makeNetworkRequest(RestAdapter restAdapter) {
		super.makeNetworkRequest(restAdapter);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "makeNetworkRequest");
		}

		GetWayfindingRouteParams params = (GetWayfindingRouteParams) getNetworkParams();
		UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

		switch (getConcurrencyType()) {
			case SYNCHRONOUS:
				try {
					GetWayfindingRouteResponse response = services.getWayfindingRoute(
							params.startLatitude,
							params.startLongitude,
							params.poiId,
							params.waypointOption);
					success(response, null);
				}
				catch (RetrofitError retrofitError) {
					failure(retrofitError);
				}
				break;
			case ASYNCHRONOUS:
				services.getWayfindingRoute(
						params.startLatitude,
						params.startLongitude,
						params.poiId,
						params.waypointOption,
						this);
				break;
			default:
				throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
		}
	}

	@Override
	public void success(GetWayfindingRouteResponse getWayfindingRouteResponse, Response response) {
		if (getWayfindingRouteResponse == null) {
			getWayfindingRouteResponse = new GetWayfindingRouteResponse();
		}

		/* All wayfinding data is managed in memory, and a database table is not needed
		 * in the current implementation.

		// Add the waypoints and paths to the proper tables
		List<Waypoint> waypointList = getWayfindingRouteResponse.getWaypoints();
		List<Path> pathList = getWayfindingRouteResponse.getPaths();

		addWaypointsToDatabase(waypointList);
		addPathsToDatabase(pathList);
		 */

		// Inform any listeners after saving the response
		super.handleSuccess(getWayfindingRouteResponse, response);
	}

	@Override
	public void failure(RetrofitError retrofitError) {
		super.handleFailure(new GetWayfindingRouteResponse(), retrofitError);
	}

	private void addWaypointsToDatabase(List<Waypoint> waypointList) {
		ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();

		// Wipe the table
		deleteAllWaypointsFromDatabase(contentResolver);

		// Insert new waypoints
		insertWaypointsIntoDatabase(waypointList, contentResolver);
	}

	private void addPathsToDatabase(List<Path> pathList) {
		ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();

		// Wipe the table
		deleteAllPathsFromDatabase(contentResolver);

		// Insert new paths
		insertPathsIntoDatabase(pathList, contentResolver);
	}


	private static ContentValues createWaypointContentValues(Waypoint waypoint) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(WaypointsTable.COL_WAYPOINT_ID, waypoint.getId());
		contentValues.put(WaypointsTable.COL_WAYPOINT_OBJECT_JSON, waypoint.toJson());
		return contentValues;
	}

	private static ContentValues createPathContentValues(Path path) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(PathsTable.COL_PATH_ID, path.getId());
		contentValues.put(PathsTable.COL_STARTING_WAYPOINT_ID, path.getStartingWaypointId());
		contentValues.put(PathsTable.COL_ENDING_WAYPOINT_ID, path.getEndingWaypointId());
		contentValues.put(PathsTable.COL_DESTINATION_ID, path.getDestinationId());
		contentValues.put(PathsTable.COL_INSTRUCTIONS, path.getInstructions());
		contentValues.put(PathsTable.COL_COMMENT, path.getComment());
		contentValues.put(PathsTable.COL_NAVIGATION_IMAGE_URL, path.getNavigationImageUrl());
		contentValues.put(PathsTable.COL_PATH_OBJECT_JSON, path.toJson());
		return contentValues;
	}

	private static void insertWaypointsIntoDatabase(List<Waypoint> waypointList, ContentResolver contentResolver) {
		if (waypointList == null || waypointList.size() == 0) {
			return;
		}

		if (BuildConfig.DEBUG) {
			Log.i(TAG, "insertWaypointsIntoDatabase: inserting new waypoints = " + waypointList.size());
		}

		ContentValues contentValuesArray[] = new ContentValues[waypointList.size()];
		for (int i = 0; i < waypointList.size(); i++) {
			Waypoint newWaypoint = waypointList.get(i);

			ContentValues contentValues = createWaypointContentValues(newWaypoint);
			contentValuesArray[i] = contentValues;
		}

		try {
			contentResolver.bulkInsert(UniversalOrlandoContentUris.WAYPOINTS, contentValuesArray);
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "insertWaypointsIntoDatabase: exception inserting waypoint into the database", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void insertPathsIntoDatabase(List<Path> pathList, ContentResolver contentResolver) {
		if (pathList == null || pathList.size() == 0) {
			return;
		}

		if (BuildConfig.DEBUG) {
			Log.i(TAG, "insertPathsIntoDatabase: inserting new paths = " + pathList.size());
		}

		ContentValues contentValuesArray[] = new ContentValues[pathList.size()];
		for (int i = 0; i < pathList.size(); i++) {
			Path newPath = pathList.get(i);

			ContentValues contentValues = createPathContentValues(newPath);
			contentValuesArray[i] = contentValues;
		}

		try {
			contentResolver.bulkInsert(UniversalOrlandoContentUris.PATHS, contentValuesArray);
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "insertPathsIntoDatabase: exception inserting path into the database", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void deleteAllWaypointsFromDatabase(ContentResolver contentResolver) {

		try {
			int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.WAYPOINTS, null, null);
			if (BuildConfig.DEBUG) {
				Log.i(TAG, "deleteAllWaypointsFromDatabase: waypoints removed = " + rowsDeleted);
			}
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "deleteAllWaypointsFromDatabase: exception deleting waypoints in the database", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void deleteAllPathsFromDatabase(ContentResolver contentResolver) {

		try {
			int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.PATHS, null, null);
			if (BuildConfig.DEBUG) {
				Log.i(TAG, "deleteAllPathsFromDatabase: paths removed = " + rowsDeleted);
			}
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "deleteAllPathsFromDatabase: exception deleting paths in the database", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

}