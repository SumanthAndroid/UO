package com.universalstudios.orlandoresort.model.network.domain.venue;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenueHoursTable;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Steven Byle
 */
public class GetVenueHoursRequest extends UniversalOrlandoServicesRequest implements Callback<List<VenueHours>> {
	private static final String TAG = GetVenueHoursRequest.class.getSimpleName();

	private GetVenueHoursRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, GetVenueHoursParams getVenueHoursParams) {
		super(senderTag, priority, concurrencyType, getVenueHoursParams);
	}

	private static class GetVenueHoursParams extends NetworkParams {
		private long venueId;
		private String endDateString;

		public GetVenueHoursParams() {
			super();
		}

		public static String generateEndDateString(Date endDate) {
			if (endDate != null) {
				// Format the end date to a string ("05/31/2015")
				SimpleDateFormat endDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
				endDateFormat.setTimeZone(DateTimeUtils.getParkTimeZone());
				return endDateFormat.format(endDate);
			}

			return "";
		}
	}

	public static class Builder extends BaseNetworkRequestBuilder<Builder> {
		private final GetVenueHoursParams getVenueHoursParams;

		public Builder(NetworkRequestSender sender) {
			super(sender);
			getVenueHoursParams = new GetVenueHoursParams();
		}

		@Override
		protected Builder getThis() {
			return this;
		}

		public Builder setEndDate(Date endDate) {
			getVenueHoursParams.endDateString = GetVenueHoursParams.generateEndDateString(endDate);
			return getThis();
		}

		public Builder setVenueId(long venueId) {
			getVenueHoursParams.venueId = venueId;
			return getThis();
		}

		public GetVenueHoursRequest build() {
			return new GetVenueHoursRequest(senderTag, priority, concurrencyType, getVenueHoursParams);
		}
	}

	@Override
	public void makeNetworkRequest(RestAdapter restAdapter) {
		super.makeNetworkRequest(restAdapter);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "makeNetworkRequest");
		}

		GetVenueHoursParams params = (GetVenueHoursParams) getNetworkParams();
		UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

		switch (getConcurrencyType()) {
			case SYNCHRONOUS:
				try {
					List<VenueHours> venueHours = services.getVenueHours(
							params.venueId,
							params.endDateString);
					success(venueHours, null);
				}
				catch (RetrofitError retrofitError) {
					failure(retrofitError);
				}
				break;
			case ASYNCHRONOUS:
				services.getVenueHours(
						params.venueId,
						params.endDateString,
						this);
				break;
			default:
				throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
		}

	}

	@Override
	public void success(List<VenueHours> venueHours, Response response) {
		if (venueHours != null) {
			// Sync the venue hours with the database
			syncVenueHoursWithDatabase(venueHours);

			// Store the last time venue hours were synced
			UniversalAppState universalAppState = UniversalAppStateManager.getInstance();
			universalAppState.setDateOfLastVenueHoursSyncInMillis(new Date().getTime());
			UniversalAppStateManager.saveInstance();
		}
		else {
			venueHours = new ArrayList<VenueHours>();
		}

		// Wrap the raw response into a response object
		GetVenueHoursResponse getVenueHoursResponse = new GetVenueHoursResponse(venueHours);

		// Inform any listeners after saving the response
		super.handleSuccess(getVenueHoursResponse, response);
	}

	@Override
	public void failure(RetrofitError retrofitError) {
		super.handleFailure(new GetVenueHoursResponse(), retrofitError);
	}

	public static long getStartOfDayInMillis(long dateInMillis) {
		// Set the day to 12:00:00 am, right at the start of the day
		Calendar calendar = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
		calendar.setTime(new Date(dateInMillis));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTimeInMillis();
	}

	private void syncVenueHoursWithDatabase(List<VenueHours> venueHoursList) {
		if (venueHoursList == null) {
			return;
		}

		GetVenueHoursParams getVenueHoursParams = (GetVenueHoursParams) getNetworkParams();
		long venueHoursVenueId = getVenueHoursParams.venueId;

		// Query for all hours for that venue
		ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
		Cursor venueHoursCursor = contentResolver.query(UniversalOrlandoContentUris.VENUE_HOURS, null, VenueHoursTable.COL_VENUE_ID + " = " + getVenueHoursParams.venueId, null, null);

		// List to track venue hours that need to be inserted
		List<VenueHours> newVenueHoursList = new ArrayList<VenueHours>();

		for (VenueHours venueHours : venueHoursList) {
			if (venueHours == null) {
				continue;
			}
			long venueHoursDateInMillis = getStartOfDayInMillis(venueHours.getOpenTimeUnix() * 1000);
			boolean venueHoursFoundInDb = false;

			// Start the cursor at the first row
			if (venueHoursCursor != null && venueHoursCursor.moveToFirst()) {
				// Go through every venue hour in the database
				do {
					long venueFromDbVenueId = venueHoursCursor.getLong(venueHoursCursor.getColumnIndex(VenueHoursTable.COL_VENUE_ID));
					long venueFromDbDateInMillis = venueHoursCursor.getLong(venueHoursCursor.getColumnIndex(VenueHoursTable.COL_DATE_IN_MILLIS));

					// If the venue hours are found, check to see if it needs to be updated
					if (venueHoursVenueId == venueFromDbVenueId && venueHoursDateInMillis == venueFromDbDateInMillis) {
						long openDateInMillis = venueHoursCursor.getLong(venueHoursCursor.getColumnIndex(VenueHoursTable.COL_OPEN_DATE_IN_MILLIS));
						long closeDateInMillis = venueHoursCursor.getLong(venueHoursCursor.getColumnIndex(VenueHoursTable.COL_CLOSE_DATE_IN_MILLIS));

						// If the venue in the database is different than the new venue, update the database
						if ((venueHours.getOpenTimeUnix() * 1000) != openDateInMillis
								|| (venueHours.getCloseTimeUnix() * 1000) != closeDateInMillis) {
							updateVenueHoursInDatabase(venueHours, venueHoursDateInMillis, venueHoursVenueId, contentResolver);
						}

						// Stop looping after finding the venue
						venueHoursFoundInDb = true;
						break;
					}
				} while (venueHoursCursor.moveToNext());
			}

			// If the venue wasn't found, add it to the list to be inserted
			if (!venueHoursFoundInDb) {
				newVenueHoursList.add(venueHours);
			}
		}

		// Insert any new venues
		insertVenueHoursInDatabase(newVenueHoursList, venueHoursVenueId, contentResolver);

		// Delete any venues that are not in the latest venue set
		deleteOldVenueHoursFromDatabase(venueHoursList, venueHoursVenueId, contentResolver);

		// Close the cursor
		if (venueHoursCursor != null && !venueHoursCursor.isClosed()) {
			venueHoursCursor.close();
		}
	}

	private static ContentValues createVenueHoursContentValues(VenueHours venueHours, long dateInMillis, long venueId) {

		ContentValues contentValues = new ContentValues();
		contentValues.put(VenueHoursTable.COL_VENUE_ID, venueId);
		contentValues.put(VenueHoursTable.COL_DATE_IN_MILLIS, dateInMillis);
		contentValues.put(VenueHoursTable.COL_OPEN_DATE_IN_MILLIS, venueHours.getOpenTimeUnix() * 1000);
		contentValues.put(VenueHoursTable.COL_CLOSE_DATE_IN_MILLIS, venueHours.getCloseTimeUnix() * 1000);

		return contentValues;
	}

	private static void updateVenueHoursInDatabase(VenueHours venueHoursToUpdate, long dateInMillis, long venueId, ContentResolver contentResolver) {
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "updateVenueHoursInDatabase");
		}

		ContentValues contentValues = createVenueHoursContentValues(venueHoursToUpdate, dateInMillis, venueId);
		String selection = new StringBuilder(VenueHoursTable.COL_VENUE_ID).append(" = ").append(venueId)
				.append(" AND ")
				.append(VenueHoursTable.COL_DATE_IN_MILLIS).append(" = ").append(dateInMillis)
				.toString();

		try {
			contentResolver.update(UniversalOrlandoContentUris.VENUE_HOURS, contentValues, selection, null);
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "updateVenueHoursInDatabase: exception updating venue hours in the database", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void insertVenueHoursInDatabase(List<VenueHours> newVenueHoursList, long venueId, ContentResolver contentResolver) {
		if (newVenueHoursList == null || newVenueHoursList.size() == 0) {
			return;
		}

		if (BuildConfig.DEBUG) {
			Log.i(TAG, "insertVenueHoursInDatabase: inserting new venue hours = " + newVenueHoursList.size());
		}

		ContentValues contentValuesArray[] = new ContentValues[newVenueHoursList.size()];
		for (int i = 0; i < newVenueHoursList.size(); i++) {
			VenueHours newVenueHours = newVenueHoursList.get(i);

			ContentValues contentValues = createVenueHoursContentValues(newVenueHours, getStartOfDayInMillis(newVenueHours.getOpenTimeUnix() * 1000), venueId);
			contentValuesArray[i] = contentValues;
		}

		try {
			contentResolver.bulkInsert(UniversalOrlandoContentUris.VENUE_HOURS, contentValuesArray);
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "insertVenueHoursInDatabase: exception inserting venue hours into the database", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void deleteOldVenueHoursFromDatabase(List<VenueHours> latestVenueHoursList, long venueId, ContentResolver contentResolver) {
		if (latestVenueHoursList == null) {
			return;
		}

		// Delete venue hours that don't match the latest id set, if the latest is empty, delete them all
		StringBuilder selectionBuilder = new StringBuilder(VenueHoursTable.COL_VENUE_ID).append(" = ").append(venueId);
		if (latestVenueHoursList.size() > 0) {
			selectionBuilder.append(" AND ").append(VenueHoursTable.COL_DATE_IN_MILLIS)
			.append(" NOT IN (");

			for (int i = 0; i < latestVenueHoursList.size(); i++) {
				VenueHours venueHours = latestVenueHoursList.get(i);
				long dateInMillis = getStartOfDayInMillis(venueHours.getOpenTimeUnix() * 1000);

				if (i > 0) {
					selectionBuilder.append(", ");
				}
				selectionBuilder.append("'").append(dateInMillis).append("'");
			}
			selectionBuilder.append(")");
		}

		try {
			int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.VENUE_HOURS, selectionBuilder.toString(), null);
			if (BuildConfig.DEBUG) {
				Log.i(TAG, "deleteOldVenueHoursFromDatabase: old venue hours removed = " + rowsDeleted);
			}
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "deleteOldVenueHoursFromDatabase: exception deleting old venue hours in the database", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

}