package com.universalstudios.orlandoresort.model.persistence;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;

/**
 * Content provider to the app database storing all persisted data locally. This uses the {@link
 * UniversalOrlandoContentUris} to access the database tables.
 * directly.
 *
 * @author Steven Byle
 */
public class UniversalOrlandoContentProvider extends ContentProvider {
	private static final String TAG = UniversalOrlandoContentProvider.class.getSimpleName();

	/**
	 * The unique authority for this provider.
	 */
	public static final String AUTHORITY = BuildConfig.CONTENT_PROVIDER_AUTHORITY;
	private static final String MIME_TYPE_DATA = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + BuildConfig.CONTENT_PROVIDER_MIME_TYPE_DATA;

	private UniversalOrlandoDatabaseHelper mUniversalOrlandoDatabaseHelper;

	@Override
	public boolean onCreate() {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate");
		}
		mUniversalOrlandoDatabaseHelper = new UniversalOrlandoDatabaseHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		int uriCode = UniversalOrlandoContentUris.matchUri(uri);
		if (uriCode == -1) {
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		else {
			return MIME_TYPE_DATA;
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "query: " + uri.toString());
		}

		String dbTable = null;
		Uri uriToNotify = null;
		int uriCode = UniversalOrlandoContentUris.matchUri(uri);

		if (uriCode == UriMatcher.NO_MATCH) {
			throw new IllegalArgumentException("Unknown URI = " + uri);
		}
		else if (uriCode == UniversalOrlandoContentUris.URI_CODE_CUSTOM_TABLE) {
			// Use the end portion of the URI as the table statement
			dbTable = uri.getLastPathSegment();

			// Get the initial DB table, and use its URI to notify of changes
			uriToNotify = UniversalOrlandoContentUris.getContentUriByDbTableName(dbTable.split("\n")[0]);
		}
		else {
			dbTable = UniversalOrlandoContentUris.getDbTableNameByContentUri(uri);
			if (dbTable == null) {
				throw new IllegalArgumentException("No valid database table for URI = " + uri);
			}
			uriToNotify = uri;
		}

		// Using SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// Set the table to query from
		queryBuilder.setTables(dbTable);

		// Get database and query it
		SQLiteDatabase sqlDB = mUniversalOrlandoDatabaseHelper.getReadableDatabase();
		Cursor cursor = queryBuilder.query(sqlDB, projection, selection,
				selectionArgs, null, null, sortOrder);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "query: uri to notify of changes = " + uriToNotify.toString());
		}

		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uriToNotify);

		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "insert: " + uri.toString());
		}

		String dbTable = null;
		int uriCode = UniversalOrlandoContentUris.matchUri(uri);

		if (uriCode == UriMatcher.NO_MATCH) {
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		else {
			dbTable = UniversalOrlandoContentUris.getDbTableNameByContentUri(uri);
			if (dbTable == null) {
				throw new IllegalArgumentException("No valid database table for URI: " + uri);
			}
		}

		SQLiteDatabase sqlDB = mUniversalOrlandoDatabaseHelper.getWritableDatabase();
		long id = sqlDB.insert(dbTable, null, values);
		Uri newlyInsertedUri = uri.buildUpon().appendPath("" + id).build();

		// Make sure that potential listeners are getting notified
		getContext().getContentResolver().notifyChange(uri, null);

		return newlyInsertedUri;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "bulkInsert: " + uri.toString());
		}

		String dbTable = null;
		int uriCode = UniversalOrlandoContentUris.matchUri(uri);

		if (uriCode == UriMatcher.NO_MATCH) {
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		else {
			dbTable = UniversalOrlandoContentUris.getDbTableNameByContentUri(uri);
			if (dbTable == null) {
				throw new IllegalArgumentException("No valid database table for URI: " + uri);
			}
		}

		SQLiteDatabase sqlDB = mUniversalOrlandoDatabaseHelper.getWritableDatabase();
		int rowsInserted = 0;
		try {
			sqlDB.beginTransaction();
			for (ContentValues v : values) {
				if (v != null) {
					sqlDB.insert(dbTable, null, v);
					rowsInserted++;
				}
			}
			sqlDB.setTransactionSuccessful();
		}
		catch (SQLException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "bulkInsert: exception inserting rows", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
		finally {
			sqlDB.endTransaction();
		}

		// Make sure that potential listeners are getting notified
		getContext().getContentResolver().notifyChange(uri, null);

		return rowsInserted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "update: " + uri.toString());
		}

		String dbTable = null;
		int uriCode = UniversalOrlandoContentUris.matchUri(uri);

		if (uriCode == UriMatcher.NO_MATCH) {
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		else {
			dbTable = UniversalOrlandoContentUris.getDbTableNameByContentUri(uri);
			if (dbTable == null) {
				throw new IllegalArgumentException("No valid database table for URI: " + uri);
			}
		}

		SQLiteDatabase sqlDB = mUniversalOrlandoDatabaseHelper.getWritableDatabase();
		int rowsUpdated = sqlDB.update(dbTable, values, selection, selectionArgs);

		// Make sure that potential listeners are getting notified
		getContext().getContentResolver().notifyChange(uri, null);

		return rowsUpdated;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "delete: " + uri.toString());
		}

		SQLiteDatabase sqlDB = mUniversalOrlandoDatabaseHelper.getWritableDatabase();
		String dbTable = null;
		int uriCode = UniversalOrlandoContentUris.matchUri(uri);

		if (uriCode == UriMatcher.NO_MATCH) {
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		else {
			dbTable = UniversalOrlandoContentUris.getDbTableNameByContentUri(uri);
			if (dbTable == null) {
				throw new IllegalArgumentException("No valid database table for URI: " + uri);
			}
		}

		int rowsDeleted = sqlDB.delete(dbTable, selection, selectionArgs);

		// Make sure that potential listeners are getting notified
		getContext().getContentResolver().notifyChange(uri, null);

		return rowsDeleted;
	}

}
