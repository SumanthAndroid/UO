package com.universalstudios.orlandoresort.model.persistence;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.AlertsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventSeriesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventSeriesTimesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventTimesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.InteractiveExperiencesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.NewsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OfferSeriesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OffersTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PathsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.QueuesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.ResponseQueueTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.ShowTimesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.TicketsAppointmentTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenueHoursTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenueLandsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.WaypointsTable;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

/**
 * This is a database helper for the app database. It provides access to the database and its
 * tables. It also defines the creation of all database tables and upgrade scenarios.
 *
 * @author Steven Byle
 */
@SuppressWarnings("javadoc")
public class UniversalOrlandoDatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = UniversalOrlandoDatabaseHelper.class.getSimpleName();

	// DB name
	public static final String DB_NAME = "universal_orlando.db";

	// Production version 1.0.1
	private static final int DB_VERSION_1_0_1 = 31;
	// Production version 1.2
	private static final int DB_VERSION_1_2 = 32;
	// Production version 1.3
	private static final int DB_VERSION_1_3 = 33;
	// Production version 1.3.1
	private static final int DB_VERSION_1_3_1 = 34;
	// Production version 1.4
	private static final int DB_VERSION_1_4 = 38;
	// Production version 1.4.3
	private static final int DB_VERSION_1_4_3 = 39;
	// Production version 1.4.5
	private static final int DB_VERSION_1_4_5 = 42;
	// Production version 1.5.1
	private static final int DB_VERSION_1_5_1 = 43;

	private static final int DB_VERSION_1_5_1_2 = 44;

	private static final int DB_VERSION_1_5_2 = 45;

    private static final int DB_VERSION_1_5_1_3 = 45;

	private static final int DB_VERSION_1_6_1 = 46;

	// ***The DB version must ONLY be incremented if the DB schema changes, and the upgrade scenarios MUST be handled***
	public static final int DB_VERSION_CURRENT = DB_VERSION_1_6_1;

	/**
	 * Default constructor.
	 */
	public UniversalOrlandoDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION_CURRENT);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreate: database version = " + db.getVersion());
		}

		// Create all the tables needed in the DB
		createResponseQueueTable(db);
		createPointsOfInterestTable(db);
		createVenuesTable(db);
		createVenueLandsTable(db);
		createWaypointsTable(db);
		createPathsTable(db);
		createAlertsTable(db);
		createNewsTable(db);
		createVenueHoursTable(db);
		createShowTimesTable(db);
		createEventTimesTable(db);
		createEventSeriesTable(db);
		createEventSeriesTimesTable(db);
		createOfferSeriesTable(db);
		createOffersTable(db);
		createInteractiveExperiencesTable(db);
		createInteractiveTriggersTable(db);
		createInteractiveBeaconsTable(db);
		createPhotoFrameExperienceTable(db);
		createQueuesTable(db);
		createAppointmentTicketsTable(db);
		db.execSQL(UniversalOrlandoDatabaseTables.MobilePagesTable.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "onUpgrade: upgrading database from version "
					+ oldVersion + " to " + newVersion);
		}

		// If upgrading from a version older than 1.2, handle creating
		// new tables and data migration
		if (oldVersion < DB_VERSION_1_2) {

			// Create the new tables needed in 1.2
			createAlertsTable(db);
			createNewsTable(db);
		}

		// If upgrading from a version older than 1.3, handle creating
		// new tables and data migration
		if (oldVersion < DB_VERSION_1_3) {

			// Create the new tables needed in 1.3
			createVenueHoursTable(db);
		}

		// If upgrading from a version older than 1.3.1, handle creating
		// new tables and data migration
		if (oldVersion < DB_VERSION_1_3_1) {

			// Drop and recreate the response queue table for 1.3.1
			// since a new column was added
			dropTable(db, ResponseQueueTable.TABLE_NAME);
			createResponseQueueTable(db);
		}
		// If upgrading from a version older than 1.4
		// the tags column must be added to table_points_of_interest,
		// add show times table
		if (oldVersion < DB_VERSION_1_4) {
			createEventTimesTable(db);
			createEventSeriesTable(db);
			createShowTimesTable(db);
			createEventSeriesTimesTable(db);
			createOfferSeriesTable(db);
			createOffersTable(db);
			boolean upgradeSuccessful = DatabaseUpgradeHelper.upgradeTo1_4(db);
			// If upgrade failed drop the affected tables and recreate them
			if (!upgradeSuccessful) {
				dropTable(db, PointsOfInterestTable.TABLE_NAME);
				createPointsOfInterestTable(db);
				dropTable(db, EventSeriesTable.TABLE_NAME);
				createEventSeriesTable(db);
				dropTable(db, EventSeriesTimesTable.TABLE_NAME);
				createEventSeriesTimesTable(db);
				// Wipe the last sync data, since the database is being cleared
				UniversalAppStateManager.wipeLastSyncData();
			}
		}

		// If upgrading from a version older than 1.4.3 add sort_order column
		if (oldVersion < DB_VERSION_1_4_3) {
			boolean upgradeSuccessful = DatabaseUpgradeHelper.upgradeTo1_4_3(db);
			// If upgrade failed drop and recreate affected tables
			if (!upgradeSuccessful) {
				dropTable(db, PointsOfInterestTable.TABLE_NAME);
				createPointsOfInterestTable(db);
				dropTable(db, EventSeriesTable.TABLE_NAME);
				createEventSeriesTable(db);
			}
		}

		if (oldVersion < DB_VERSION_1_4_5) {
			createInteractiveExperiencesTable(db);
		}

		if (oldVersion < DB_VERSION_1_5_1) {
			dropTable(db, InteractiveExperiencesTable.TABLE_NAME);
			createInteractiveExperiencesTable(db);
			createInteractiveTriggersTable(db);
			createInteractiveBeaconsTable(db);
		}

		if (oldVersion < DB_VERSION_1_5_1_2) {
			DatabaseUpgradeHelper.upgradeTo1_5_1_2(db);
			createPhotoFrameExperienceTable(db);
		}

        if (oldVersion < DB_VERSION_1_5_1_3) {
            db.execSQL(EventTimesTable.UPDATE_ADD_FLAGS);
        }

		if(oldVersion < DB_VERSION_1_5_2){
			createQueuesTable(db);
			createAppointmentTicketsTable(db);
		}

		if (oldVersion < DB_VERSION_1_6_1) {
			db.execSQL(UniversalOrlandoDatabaseTables.MobilePagesTable.CREATE_TABLE);
		}
		// If somehow the old DB version is >= the current version, and onUprade
		// is still being called for some reason, wipe the DB and start fresh
		else {
            // Wipe the last sync data, since the database is being cleared
			UniversalAppStateManager.wipeLastSyncData();

			// Drop all tables in the database and recreate them
			dropAllTablesAndRecreate(db);

            // Recreate the database with the new schema
            onCreate(db);
        }
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "onDowngrade: downgrading database from version "
					+ oldVersion + " to " + newVersion);
		}

		// Wipe the last sync data, since the database is being cleared
		UniversalAppStateManager.wipeLastSyncData();

		// Drop all tables in the database and recreate them
		dropAllTablesAndRecreate(db);
	}

	private void dropAllTablesAndRecreate(SQLiteDatabase db) {

		// Drop every table in the database
		dropTables(db, UniversalOrlandoDatabaseTables.DB_TABLE_NAMES);

		// Recreate all the tables with the new schema
		onCreate(db);
	}

	private static void dropTables(SQLiteDatabase db, String[] dbTableNames) {
		// Drop all tables in the list
		for (String dbTableName : dbTableNames) {
			dropTable(db, dbTableName);
		}
	}

	private static void dropTable(SQLiteDatabase db, String dbTableName) {
		try {
			db.execSQL("DROP TABLE IF EXISTS " + dbTableName);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "dropTable: exception trying to drop a table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createResponseQueueTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + ResponseQueueTable.TABLE_NAME + "("
				+ ResponseQueueTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ ResponseQueueTable.COL_SENDER_TAG + " text, "
				+ ResponseQueueTable.COL_REQUEST_TAG + " text, "
				+ ResponseQueueTable.COL_TIME_OF_REQUEST + " integer, "
				+ ResponseQueueTable.COL_RESPONSE_OBJECT_FULL_CLASS_NAME + " text, "
				+ ResponseQueueTable.COL_RESPONSE_OBJECT_JSON + " text)";
		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createResponseQueueTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createResponseQueueTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createPointsOfInterestTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + PointsOfInterestTable.TABLE_NAME + "("
				+ PointsOfInterestTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ PointsOfInterestTable.COL_POI_ID + " integer, "
				+ PointsOfInterestTable.COL_POI_TYPE_ID + " integer, "
				+ PointsOfInterestTable.COL_DISPLAY_NAME + " text, "
				+ PointsOfInterestTable.COL_WAIT_TIME + " integer, "
				+ PointsOfInterestTable.COL_SHOW_TIMES_JSON + " text,"
				+ PointsOfInterestTable.COL_SUB_TYPE_FLAGS + " integer,"
				+ PointsOfInterestTable.COL_LIST_IMAGE_URL + " text,"
				+ PointsOfInterestTable.COL_THUMBNAIL_IMAGE_URL + " text,"
				+ PointsOfInterestTable.COL_VENUE_ID + " text, "
				+ PointsOfInterestTable.COL_VENUE_LAND_ID + " text, "
				+ PointsOfInterestTable.COL_LATITUDE + " real, "
				+ PointsOfInterestTable.COL_LONGITUDE + " real, "
				+ PointsOfInterestTable.COL_IS_ROUTABLE + " integer, "
				+ PointsOfInterestTable.COL_IS_FAVORITE + " integer, "
				+ PointsOfInterestTable.COL_POI_HASH_CODE + " integer, "
				+ PointsOfInterestTable.COL_POI_OBJECT_JSON + " text, "
				+ PointsOfInterestTable.COL_TAGS + " text, "
				+ PointsOfInterestTable.COL_MIN_RIDE_HEIGHT + " integer, "
				+ PointsOfInterestTable.COL_MAX_RIDE_HEIGHT + " integer, "
				+ PointsOfInterestTable.COL_MIN_PRICE + " real, "
				+ PointsOfInterestTable.COL_MAX_PRICE + " real, "
				+ PointsOfInterestTable.COL_OPTION_FLAGS + " integer, "
				+ PointsOfInterestTable.COL_OFFER_IDS + " text,"
				+ PointsOfInterestTable.COL_SORT_ORDER + " integer)";

		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createPointsOfInterestTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createPointsOfInterestTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

    private static void createPhotoFrameExperienceTable(SQLiteDatabase db) {
        try {
            db.execSQL(UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.CREATE_TABLE);
        } catch (SQLiteException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            Crittercism.logHandledException(e);
        }
    }

	private static void createVenuesTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + VenuesTable.TABLE_NAME + "("
				+ VenuesTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ VenuesTable.COL_VENUE_ID + " integer, "
				+ VenuesTable.COL_DISPLAY_NAME + " text, "
				+ VenuesTable.COL_LONG_DESCRIPTION + " text, "
				+ VenuesTable.COL_HOURS_LIST_JSON + " text, "
				+ VenuesTable.COL_VENUE_OBJECT_JSON + " text)";

		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createVenuesTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createVenuesTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createVenueLandsTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + VenueLandsTable.TABLE_NAME + "("
				+ VenueLandsTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ VenueLandsTable.COL_VENUE_LAND_ID + " integer, "
				+ VenueLandsTable.COL_CONTAINING_VENUE_ID + " integer, "
				+ VenueLandsTable.COL_DISPLAY_NAME + " text, "
				+ VenueLandsTable.COL_LONG_DESCRIPTION + " text, "
				+ VenueLandsTable.COL_VENUE_LAND_OBJECT_JSON + " text)";

		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createVenueLandsTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createVenueLandsTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createWaypointsTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + WaypointsTable.TABLE_NAME + "("
				+ WaypointsTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ WaypointsTable.COL_WAYPOINT_ID + " integer, "
				+ WaypointsTable.COL_WAYPOINT_OBJECT_JSON + " text)";

		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createWaypointsTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createWaypointsTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createShowTimesTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + ShowTimesTable.TABLE_NAME + "("
				+ ShowTimesTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ ShowTimesTable.COL_POI_ID + " integer, "
				+ ShowTimesTable.COL_SHOW_TIME + " text, "
				+ ShowTimesTable.COL_TIME_TYPE  + " integer, "
				+ ShowTimesTable.COL_POI_TYPE_ID + " integer)";

		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createShowTimesTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createShowTimesTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createPathsTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + PathsTable.TABLE_NAME + "("
				+ PathsTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ PathsTable.COL_PATH_ID + " integer, "
				+ PathsTable.COL_STARTING_WAYPOINT_ID + " integer, "
				+ PathsTable.COL_ENDING_WAYPOINT_ID + " integer, "
				+ PathsTable.COL_DESTINATION_ID + " integer, "
				+ PathsTable.COL_INSTRUCTIONS + " text, "
				+ PathsTable.COL_COMMENT + " text, "
				+ PathsTable.COL_NAVIGATION_IMAGE_URL + " text, "
				+ PathsTable.COL_PATH_OBJECT_JSON + " text)";

		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createPathsTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createPathsTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createAlertsTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + AlertsTable.TABLE_NAME + "("
				+ AlertsTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ AlertsTable.COL_ALERT_ID_STRING + " text, "
				+ AlertsTable.COL_ALERT_TYPE_ID + " integer, "
				+ AlertsTable.COL_POI_ID + " integer, "
				+ AlertsTable.COL_POI_NAME + " text, "
				+ AlertsTable.COL_SHOW_TIME + " text, "
				+ AlertsTable.COL_NOTIFY_MIN_BEFORE + " integer, "
				+ AlertsTable.COL_NOTIFY_THRESHOLD_IN_MIN + " integer, "
				+ AlertsTable.COL_CREATED_DATE_IN_MILLIS + " integer, "
				+ AlertsTable.COL_ALERT_OBJECT_JSON + " text)";

		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createAlertsTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createAlertsTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createNewsTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + NewsTable.TABLE_NAME + "("
				+ NewsTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ NewsTable.COL_NEWS_ID + " integer, "
				+ NewsTable.COL_MESSAGE_HEADING + " text, "
				+ NewsTable.COL_MESSAGE_BODY + " text,"
				+ NewsTable.COL_START_DATE_IN_MILLIS + " integer,"
				+ NewsTable.COL_EXPIRATION_DATE_IN_MILLIS + " integer,"
				+ NewsTable.COL_HAS_BEEN_READ + " integer,"
				+ NewsTable.COL_NEWS_OBJECT_JSON + " text)";

		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createNewsTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createNewsTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createQueuesTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + QueuesTable.TABLE_NAME + "("
				+ QueuesTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ QueuesTable.COL_QUEUE_ID + " integer, "
				+ QueuesTable.COL_QUEUE_ENTITY_ID + " integer, "
				+ QueuesTable.COL_QUEUE_OBJECT_JSON + " text)";

		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createQueuesTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createQueuesTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createAppointmentTicketsTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + TicketsAppointmentTable.TABLE_NAME + "("
				+ TicketsAppointmentTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ TicketsAppointmentTable.COL_TICKET_APPOINTMENT_ID + " integer, "
				+ TicketsAppointmentTable.COL_APPOINTMENT_TIME_ID + " integer, "
				+ TicketsAppointmentTable.COL_QUEUE_ENTITY_ID + " integer, "
				+ TicketsAppointmentTable.COL_START_TIME + " text, "
				+ TicketsAppointmentTable.COL_END_TIME + " text, "
				+ TicketsAppointmentTable.COL_HAS_BEEN_READ + " text, "
				+ TicketsAppointmentTable.COL_APPOINTMENT_OBJECT_JSON + " text, "
				+ TicketsAppointmentTable.COL_APPOINTMENT_TICKET_OBJECT_JSON + " text)";



		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createAppointmentTicketsTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createAppointmentTicketsTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createVenueHoursTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + VenueHoursTable.TABLE_NAME + "("
				+ VenueHoursTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ VenueHoursTable.COL_VENUE_ID + " integer, "
				+ VenueHoursTable.COL_DATE_IN_MILLIS + " integer, "
				+ VenueHoursTable.COL_OPEN_DATE_IN_MILLIS + " integer, "
				+ VenueHoursTable.COL_CLOSE_DATE_IN_MILLIS + " integer)";

		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createVenueHoursTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createVenueHoursTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createEventTimesTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + EventTimesTable.TABLE_NAME + "("
				+ EventTimesTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ EventTimesTable.COL_POI_ID + " integer,"
				+ EventTimesTable.COL_POI_TYPE_ID + " integer,"
				+ EventTimesTable.COL_START_DATE + " integer,"
				+ EventTimesTable.COL_FLAGS + " integer,"
				+ EventTimesTable.COL_END_DATE + " integer)";

		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createEventTimesTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createEventTimesTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createEventSeriesTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + EventSeriesTable.TABLE_NAME + "("
				+ EventSeriesTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ EventSeriesTable.COL_EVENT_SERIES_ID + " integer, "
				+ EventSeriesTable.COL_DISPLAY_NAME + " text, "
				+ EventSeriesTable.COL_VENUE_ID + " integer, "
				+ EventSeriesTable.COL_LIST_IMAGE_URL + " text, "
				+ EventSeriesTable.COL_THUMBNAIL_IMAGE_URL + " text, "
				+ EventSeriesTable.COL_EVENT_IDS + " text, "
				+ EventSeriesTable.COL_ATTRACTION_IDS + " text, "
				+ EventSeriesTable.COL_EVENT_SERIES_OBJECT_JSON + " text, "
				+ EventSeriesTable.COL_FLAGS + " integer, "
				+ EventSeriesTable.COL_SORT_ORDER + " integer)";

		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createEventSeriesTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createEventSeriesTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createEventSeriesTimesTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + EventSeriesTimesTable.TABLE_NAME + "("
				+ EventSeriesTimesTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ EventSeriesTimesTable.COL_EVENT_SERIES_ID + " integer, "
				+ EventSeriesTimesTable.COL_START_DATE + " integer, "
				+ EventSeriesTimesTable.COL_END_DATE + " integer)";

		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createEventSeriesTimesTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createEventSeriesTimesTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createOfferSeriesTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + OfferSeriesTable.TABLE_NAME + "("
				+ OfferSeriesTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ OfferSeriesTable.COL_OFFER_SERIES_ID + " integer, "
				+ OfferSeriesTable.COL_VENDOR + " text, "
				+ OfferSeriesTable.COL_DISPLAY_NAME + " text, "
				+ OfferSeriesTable.COL_LATITUDE+ " real, "
				+ OfferSeriesTable.COL_LONGITUDE + " real, "
				+ OfferSeriesTable.COL_OFFER_SERIES_OBJECT_JSON + " text)";

		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createOfferSeriesTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createOfferSeriesTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createOffersTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + OffersTable.TABLE_NAME + "("
				+ OffersTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ OffersTable.COL_OFFER_ID + " integer, "
				+ OffersTable.COL_VENDOR + " text, "
				+ OffersTable.COL_VENUE_ID + " integer, "
				+ OffersTable.COL_OFFER_TYPE + " text, "
				+ OffersTable.COL_DISPLAY_NAME + " text, "
				+ OffersTable.COL_SHORT_DESCRIPTION + " text, "
				+ OffersTable.COL_THUMBNAIL_IMAGE_URL + " text, "
				+ OffersTable.COL_END_DATE + " integer, "
				+ OffersTable.COL_LATITUDE+ " real, "
				+ OffersTable.COL_LONGITUDE + " real, "
				+ OffersTable.COL_OFFER_OBJECT_JSON + " text)";

		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createOffersTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createOffersTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createInteractiveExperiencesTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + InteractiveExperiencesTable.TABLE_NAME + "("
				+ InteractiveExperiencesTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ InteractiveExperiencesTable.COL_INTERACTIVE_EXPERIENCE_ID + " integer, "
				+ InteractiveExperiencesTable.COL_DISPLAY_NAME + " text, "
				+ InteractiveExperiencesTable.COL_INTERACTIVE_EXPERIENCE_OBJECT_JSON + " text)";

		try {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createInteractiveExperiencesTable: sql = " + sql);
			}
			db.execSQL(sql);
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createInteractiveExperiencesTable: exception trying to create table", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void createInteractiveTriggersTable(SQLiteDatabase db) {
		try {
			db.execSQL(UniversalOrlandoDatabaseTables.InteractiveTriggersTable.CREATE_TABLE);
		} catch (SQLiteException e) {
			if (BuildConfig.DEBUG) {
				e.printStackTrace();
			}
		}
	}

	private static void createInteractiveBeaconsTable(SQLiteDatabase db) {
		try {
			db.execSQL(UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.CREATE_TABLE);
		} catch (SQLiteException e) {
			if (BuildConfig.DEBUG) {
				e.printStackTrace();
			}
		}
	}
}
