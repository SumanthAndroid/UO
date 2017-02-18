package com.universalstudios.orlandoresort.model.persistence;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Dining;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Entertainment;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Ride;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Show;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventSeriesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.ShowTimesTable;

/**
 * Helper methods to assist with more complex database upgrades when needed
 * 
 * @author acampbell
 *
 */
public class DatabaseUpgradeHelper {

    private static final String TAG = DatabaseUpgradeHelper.class.getSimpleName();

    public static boolean upgradeTo1_4_3(SQLiteDatabase db) {
        boolean upgradeSuccessful = true;

        String sqlPoiSortOrder = "ALTER TABLE " + PointsOfInterestTable.TABLE_NAME + " ADD COLUMN "
                + PointsOfInterestTable.COL_SORT_ORDER + " integer";
        String sqlEventSeriesSortOrder = "ALTER TABLE " + EventSeriesTable.TABLE_NAME + " ADD COLUMN "
                + EventSeriesTable.COL_SORT_ORDER + " integer";

        try {
            db.execSQL(sqlPoiSortOrder);
            db.execSQL(sqlEventSeriesSortOrder);
        } catch (SQLException e) {
            upgradeSuccessful = false;
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "upgradeTo1_4_3: exception trying to alter table", e);
            }
        }

        return upgradeSuccessful;
    }

    public static boolean upgradeTo1_5_1_2(SQLiteDatabase db) {
        String upgradeEventSeriesSql = "ALTER TABLE " + EventSeriesTable.TABLE_NAME + " ADD COLUMN "
                + EventSeriesTable.COL_FLAGS + " integer";
//        String upgradeEventsTimesTable = "ALTER TABLE " + UniversalOrlandoDatabaseTables.EventTimesTable.TABLE_NAME +
//                " ADD COLUMN " + UniversalOrlandoDatabaseTables.EventTimesTable.COL_FLAGS + " integer";

        boolean upgradeSuccessful = true;

        try {
            db.execSQL(upgradeEventSeriesSql);
//            db.execSQL(upgradeEventsTimesTable);
        } catch (SQLiteException e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "error upgrading event series table");
            }
            upgradeSuccessful = false;
            Crittercism.logHandledException(e);
        }

        return upgradeSuccessful;
    }

    /**
     * Upgrades the database to 1.4 <br/>
     * Adds tags column to {@link PointsOfInterestTable} and insert show times
     * 
     * @param db
     *            writable database object
     */
    public static boolean upgradeTo1_4(SQLiteDatabase db) {
        String sqlTags = "ALTER TABLE " + PointsOfInterestTable.TABLE_NAME + " ADD COLUMN "
                + PointsOfInterestTable.COL_TAGS + " text";
        String sqlMinHeight = "ALTER TABLE " + PointsOfInterestTable.TABLE_NAME + " ADD COLUMN "
                + PointsOfInterestTable.COL_MIN_RIDE_HEIGHT + " integer";
        String sqlMaxHeight = "ALTER TABLE " + PointsOfInterestTable.TABLE_NAME + " ADD COLUMN "
                + PointsOfInterestTable.COL_MAX_RIDE_HEIGHT + " integer";
        String sqlMinPrice = "ALTER TABLE " + PointsOfInterestTable.TABLE_NAME + " ADD COLUMN "
                + PointsOfInterestTable.COL_MIN_PRICE + " real";
        String sqlMaxPrice = "ALTER TABLE " + PointsOfInterestTable.TABLE_NAME + " ADD COLUMN "
                + PointsOfInterestTable.COL_MAX_PRICE + " real";
        String sqlOptionFlags = "ALTER TABLE " + PointsOfInterestTable.TABLE_NAME + " ADD COLUMN "
                + PointsOfInterestTable.COL_OPTION_FLAGS + " integer";
        String sqlOfferIds = "ALTER TABLE " + PointsOfInterestTable.TABLE_NAME + " ADD COLUMN "
                + PointsOfInterestTable.COL_OFFER_IDS + " text";

        boolean upgradeSuccessful = true;

        // Alter table by adding new tags, min/max rider height,
        // min/max starting price, option flag columns
        try {
            db.execSQL(sqlTags);
            db.execSQL(sqlMinHeight);
            db.execSQL(sqlMaxHeight);
            db.execSQL(sqlMinPrice);
            db.execSQL(sqlMaxPrice);
            db.execSQL(sqlOptionFlags);
            db.execSQL(sqlOfferIds);
        } catch (SQLException e) {
            upgradeSuccessful = false;
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "upgradeTo1_4: exception trying to alter table", e);
            }

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        }

        if (!upgradeSuccessful) {
            if (BuildConfig.DEBUG) {
                // Exit for failed upgrade
                Log.e(TAG, "upgradeTo1_4: Upgrade failed, exiting");
            }
            return upgradeSuccessful;
        }

        // Populate the show times table
        Cursor cursor = null;
        try {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(PointsOfInterestTable.TABLE_NAME);
            String selection = PointsOfInterestTable.COL_POI_TYPE_ID + " IN ('"
                    + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW + "', '"
                    + PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE + "')";
            String[] projection = { PointsOfInterestTable.COL_POI_OBJECT_JSON,
                    PointsOfInterestTable.COL_POI_TYPE_ID };
            cursor = builder.query(db, projection, selection, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                db.beginTransaction();
                do {
                    List<ContentValues> contentValuesList = new ArrayList<ContentValues>();
                    Show show = GsonObject.fromJson(
                            cursor.getString(
                                    cursor.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON)),
                            Show.class);
                    int poiTypeId = cursor
                            .getInt(cursor.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
                    if (show != null) {
                        // Create content values for start times
                        if (show.getStartTimes() != null && !show.getStartTimes().isEmpty()) {
                            for (String startTime : show.getStartTimes()) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(ShowTimesTable.COL_TIME_TYPE,
                                        ShowTimesTable.TIME_TYPE_START_TIME);
                                contentValues.put(ShowTimesTable.COL_POI_ID, show.getId());
                                contentValues.put(ShowTimesTable.COL_SHOW_TIME, startTime);
                                contentValues.put(ShowTimesTable.COL_POI_TYPE_ID, poiTypeId);
                                contentValuesList.add(contentValues);
                            }
                        }
                        // Create content values for end times
                        if (show.getEndTimes() != null && !show.getEndTimes().isEmpty()) {
                            for (String endTime : show.getStartTimes()) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(ShowTimesTable.COL_TIME_TYPE,
                                        ShowTimesTable.TIME_TYPE_END_TIME);
                                contentValues.put(ShowTimesTable.COL_POI_ID, show.getId());
                                contentValues.put(ShowTimesTable.COL_SHOW_TIME, endTime);
                                contentValues.put(ShowTimesTable.COL_POI_TYPE_ID, poiTypeId);
                                contentValuesList.add(contentValues);
                            }
                        }

                        // Insert show times if any
                        if (!contentValuesList.isEmpty()) {
                            int rowsAdded = 0;
                            for (ContentValues contentValues : contentValuesList) {
                                db.insert(ShowTimesTable.TABLE_NAME, null, contentValues);
                                rowsAdded++;
                            }
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, "upgradeTo1_4: show times added during upgrade = " + rowsAdded);
                            }
                        }
                    }
                } while (cursor.moveToNext());

                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "upgradeTo1_4: Failed upgrading database to 1.4 inserting show times", e);
            }
            upgradeSuccessful = false;

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        } finally {
            if (db.inTransaction()) {
                db.endTransaction();
            }
            if (cursor != null) {
                cursor.close();
            }
        }

        // Populate the min/max rider height columns
        // Populate the min/max price columns for dining and entertainment
        cursor = null;
        try {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(PointsOfInterestTable.TABLE_NAME);
            String selection = PointsOfInterestTable.COL_POI_TYPE_ID + " IN (?, ?, ?, ?, ?)";
            String[] selectionArgs = { "" + PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE,
                    "" + PointsOfInterestTable.VAL_POI_TYPE_ID_DINING,
                    "" + PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT,
                    "" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW,
                    "" + PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE };
            String[] projection = { PointsOfInterestTable.COL_POI_OBJECT_JSON,
                    PointsOfInterestTable.COL_POI_ID, PointsOfInterestTable.COL_POI_TYPE_ID };
            cursor = builder.query(db, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                db.beginTransaction();
                do {
                    ContentValues contentValues = new ContentValues();
                    // Get point of interest type
                    int poiTypeId = cursor
                            .getInt(cursor.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
                    // Rides
                    if (poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE) {
                        Ride ride = GsonObject.fromJson(
                                cursor.getString(
                                        cursor.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON)),
                                Ride.class);
                        if (ride.getMinHeightInInches() != null) {
                            contentValues.put(PointsOfInterestTable.COL_MIN_RIDE_HEIGHT,
                                    ride.getMinHeightInInches());
                        }
                        if (ride.getMaxHeightInInches() != null) {
                            contentValues.put(PointsOfInterestTable.COL_MAX_RIDE_HEIGHT,
                                    ride.getMaxHeightInInches());
                        }
                        contentValues.put(PointsOfInterestTable.COL_OPTION_FLAGS, ride.getOptionFlags());
                        String updateSelection = PointsOfInterestTable.COL_POI_ID + " = " + ride.getId();
                        db.update(PointsOfInterestTable.TABLE_NAME, contentValues, updateSelection, null);
                    }
                    // Dining
                    else if (poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_DINING) {
                        Dining dining = GsonObject.fromJson(
                                cursor.getString(
                                        cursor.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON)),
                                Dining.class);
                        if (dining.getMinPrice() != null) {
                            contentValues.put(PointsOfInterestTable.COL_MIN_PRICE, dining.getMinPrice());
                        }
                        if (dining.getMaxPrice() != null) {
                            contentValues.put(PointsOfInterestTable.COL_MAX_PRICE, dining.getMaxPrice());
                        }
                        contentValues.put(PointsOfInterestTable.COL_OPTION_FLAGS, dining.getOptionFlags());
                        String updateSelection = PointsOfInterestTable.COL_POI_ID + " = " + dining.getId();
                        db.update(PointsOfInterestTable.TABLE_NAME, contentValues, updateSelection, null);
                    }
                    // Entertainment
                    else if (poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT) {
                        Entertainment entertainment = GsonObject.fromJson(
                                cursor.getString(
                                        cursor.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON)),
                                Entertainment.class);
                        if (entertainment.getMinPrice() != null || entertainment.getMaxPrice() != null) {
                            if (entertainment.getMinPrice() != null) {
                                contentValues.put(PointsOfInterestTable.COL_MIN_PRICE,
                                        entertainment.getMinPrice());
                            }
                            if (entertainment.getMaxPrice() != null) {
                                contentValues.put(PointsOfInterestTable.COL_MAX_PRICE,
                                        entertainment.getMaxPrice());
                            }
                            String updateSelection = PointsOfInterestTable.COL_POI_ID + " = "
                                    + entertainment.getId();
                            db.update(PointsOfInterestTable.TABLE_NAME, contentValues, updateSelection, null);
                        }
                    }
                    // Show Parade
                    else if (poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW
                            || poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE) {
                        Show show = GsonObject.fromJson(
                                cursor.getString(
                                        cursor.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON)),
                                Show.class);
                        contentValues.put(PointsOfInterestTable.COL_OPTION_FLAGS, show.getOptionFlags());
                        String updateSelection = PointsOfInterestTable.COL_POI_ID + " = " + show.getId();
                        db.update(PointsOfInterestTable.TABLE_NAME, contentValues, updateSelection, null);
                    }
                } while (cursor.moveToNext());

                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "upgradeTo1_4: Failed upgrading database to 1.4 min/max height or min/max pirce",
                        e);
            }
            upgradeSuccessful = false;

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        } finally {
            if (db.inTransaction()) {
                db.endTransaction();
            }
            if (cursor != null) {
                cursor.close();
            }
        }

        return upgradeSuccessful;
    }
}
