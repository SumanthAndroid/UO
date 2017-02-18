package com.universalstudios.orlandoresort.model.network.domain.news;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.network.service.ServiceEndpointUtils;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.NewsTable;
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
public class GetNewsRequest extends UniversalOrlandoServicesRequest implements Callback<GetNewsResponse> {
	private static final String TAG = GetNewsRequest.class.getSimpleName();

	public static final String PAGE_SIZE_ALL = "All";

	private GetNewsRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, GetNewsParams getNewsParams)  {
		super(senderTag, priority, concurrencyType, getNewsParams);
	}

	private static class GetNewsParams extends NetworkParams {
		private Integer page;
		private String pageSize;

		public GetNewsParams() {
			super();
		}
	}

	public static class Builder extends BaseNetworkRequestBuilder<Builder> {
		private final GetNewsParams getNewsParams;

		public Builder(NetworkRequestSender sender) {
			super(sender);
			getNewsParams = new GetNewsParams();
		}

		@Override
		protected Builder getThis() {
			return this;
		}

		public Builder setPage(Integer page) {
			getNewsParams.page = page;
			return getThis();
		}

		public Builder setPageSize(String pageSize) {
			getNewsParams.pageSize = pageSize;
			return getThis();
		}

		public GetNewsRequest build() {
			return new GetNewsRequest(senderTag, priority, concurrencyType, getNewsParams);
		}
	}

	@Override
	public void makeNetworkRequest(RestAdapter restAdapter) {
		super.makeNetworkRequest(restAdapter);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "makeNetworkRequest");
		}

		GetNewsParams params = (GetNewsParams) getNetworkParams();
		UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

		switch (getConcurrencyType()) {
			case SYNCHRONOUS:
				try {
					GetNewsResponse response = services.getNews(
							ServiceEndpointUtils.getCity(),
							params.pageSize,
							params.page);
					success(response, null);
				}
				catch (RetrofitError retrofitError) {
					failure(retrofitError);
				}
				break;
			case ASYNCHRONOUS:
				services.getNews(
						ServiceEndpointUtils.getCity(),
						params.pageSize,
						params.page,
						this);
				break;
			default:
				throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
		}
	}

	@Override
	public void success(GetNewsResponse getNewsResponse, Response response) {
		// Only sync if response is not null
		if (getNewsResponse != null) {
			// Sync the news items to the database
			syncNewsWithDatabase(getNewsResponse.getResults());

			// Store the last time park news was synced
			UniversalAppState universalAppState = UniversalAppStateManager.getInstance();
			universalAppState.setDateOfLastParkNewsSyncInMillis(new Date().getTime());
			UniversalAppStateManager.saveInstance();
		}
		else {
			getNewsResponse = new GetNewsResponse();
		}

		// Inform any listeners after saving the response
		super.handleSuccess(getNewsResponse, response);
	}

	@Override
	public void failure(RetrofitError retrofitError) {
		super.handleFailure(new GetNewsResponse(), retrofitError);
	}

	private void syncNewsWithDatabase(List<News> newsList) {
		// If null, treat the list as if it were empty
		if (newsList == null) {
			newsList = new ArrayList<News>();
		}

		// Query for all news items
		ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
		String projection[] = {
				NewsTable.COL_NEWS_ID,
				NewsTable.COL_NEWS_OBJECT_JSON
		};
		Cursor newsCursor = contentResolver.query(UniversalOrlandoContentUris.NEWS,
				projection, null, null, null);

		// List to track items that need to be inserted
		List<News> newNewsList = new ArrayList<News>();

		for (News newsFromResp : newsList) {
			if (newsFromResp == null) {
				continue;
			}
			boolean newsFoundInDb = false;

			// Parse the timestamps into dates
			newsFromResp.setDatesInMillis();

			// Start the cursor at the first row
			if (newsCursor != null && newsCursor.moveToFirst()) {
				// Go through every news item in the database
				do {
					int newsId = newsCursor.getInt(newsCursor.getColumnIndex(NewsTable.COL_NEWS_ID));

					// If the news item is found, check to see if it needs to be updated
					if (newsId == newsFromResp.getId()) {
						String newsObjectJson = newsCursor.getString(newsCursor.getColumnIndex(NewsTable.COL_NEWS_OBJECT_JSON));
						News newsFromDb = GsonObject.fromJson(newsObjectJson, News.class);

						// If the news item in the database is different than the new news item, update the database
						if (!newsFromResp.equals(newsFromDb, true)) {
							// Copy over any transient state that was set after syncing
							newsFromResp.setHasBeenRead(newsFromDb.getHasBeenRead());

							// Update the item in the DB
							updateNewsInDatabase(newsFromResp, contentResolver);
						}

						// Stop looping after finding the news item
						newsFoundInDb = true;
						break;
					}
				} while (newsCursor.moveToNext());
			}

			// If the news item wasn't found, add it to the list to be inserted
			if (!newsFoundInDb) {
				newNewsList.add(newsFromResp);
			}
		}

		// Insert any new news items
		insertNewsInDatabase(newNewsList, contentResolver);

		// Delete any news items that are not in the latest news set
		deleteOldNewsFromDatabase(newsList, contentResolver);

		// Close the cursor
		if (newsCursor != null && !newsCursor.isClosed()) {
			newsCursor.close();
		}
	}

	private static ContentValues createNewsContentValues(News news) {

		ContentValues contentValues = new ContentValues();
		contentValues.put(NewsTable.COL_NEWS_ID, news.getId());
		contentValues.put(NewsTable.COL_MESSAGE_HEADING, news.getMessageHeading());
		contentValues.put(NewsTable.COL_MESSAGE_BODY, news.getMessageBody());
		contentValues.put(NewsTable.COL_START_DATE_IN_MILLIS, news.getStartDateInMillis());
		contentValues.put(NewsTable.COL_EXPIRATION_DATE_IN_MILLIS, news.getExpirationDateInMillis());
		contentValues.put(NewsTable.COL_NEWS_OBJECT_JSON, news.toJson());

		return contentValues;
	}

	private static void insertNewsInDatabase(List<News> newNewsList, ContentResolver contentResolver) {
		if (newNewsList == null || newNewsList.size() == 0) {
			return;
		}

		if (BuildConfig.DEBUG) {
			Log.i(TAG, "insertNewsInDatabase: inserting new news items = " + newNewsList.size());
		}

		ContentValues contentValuesArray[] = new ContentValues[newNewsList.size()];
		for (int i = 0; i < newNewsList.size(); i++) {
			News newNews = newNewsList.get(i);

			ContentValues contentValues = createNewsContentValues(newNews);
			contentValuesArray[i] = contentValues;
		}

		try {
			contentResolver.bulkInsert(UniversalOrlandoContentUris.NEWS, contentValuesArray);
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "insertNewsInDatabase: exception inserting new items into the database", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void updateNewsInDatabase(News newsToUpdate, ContentResolver contentResolver) {
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "updateNewsInDatabase: updating news = " + newsToUpdate.getMessageHeading());
		}

		ContentValues contentValues = createNewsContentValues(newsToUpdate);
		String selection = new StringBuilder(NewsTable.COL_NEWS_ID)
		.append(" = '").append(newsToUpdate.getId()).append("'")
		.toString();

		try {
			contentResolver.update(UniversalOrlandoContentUris.NEWS, contentValues, selection, null);
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "updateNewsInDatabase: exception updating item in the database", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void deleteOldNewsFromDatabase(List<News> latestNewsList, ContentResolver contentResolver) {
		if (latestNewsList == null) {
			latestNewsList = new ArrayList<News>();
		}

		// Delete news that don't match the latest id set, if the latest set is empty, delete them all
		String selection = null;
		if (latestNewsList.size() > 0) {
			StringBuilder selectionBuilder = new StringBuilder(NewsTable.COL_NEWS_ID)
			.append(" NOT IN (");

			for (int i = 0; i < latestNewsList.size(); i++) {
				News news = latestNewsList.get(i);
				if (i > 0) {
					selectionBuilder.append(", ");
				}
				selectionBuilder.append("'").append(news.getId()).append("'");
			}
			selectionBuilder.append(")");
			selection = selectionBuilder.toString();
		}

		try {
			int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.NEWS, selection, null);
			if (BuildConfig.DEBUG) {
				Log.i(TAG, "deleteOldNewsFromDatabase: old news items removed = " + rowsDeleted);
			}
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "deleteOldNewsFromDatabase: exception deleting old items from the database", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

}