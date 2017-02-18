/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;

import java.lang.ref.WeakReference;


/**
 * Utility class for interacting with {@link android.support.v4.content.CursorLoader}
 * implementations.
 *
 * @author Steven Byle
 */
public class LoaderUtils {
	private static final String TAG = LoaderUtils.class.getSimpleName();

	// Unique loader IDs, they must be unique per Activity/Fragment
	public static final int LOADER_ID_DATABASE_QUERY_ACTIVITY = 0;
	public static final int LOADER_ID_DATABASE_QUERY_FRAGMENT = 1;
	public static final int LOADER_ID_DATABASE_QUERY_MAP_FRAGMENT = 2;
	public static final int LOADER_ID_DATA_REFRESH_ACTIVITY = 3;
	@Deprecated
	public static final int LOADER_ID_NETWORK_ACTIVITY = 4;
	@Deprecated
	public static final int LOADER_ID_NETWORK_FRAGMENT = 5;
	public static final int LOADER_ID_SHOW_TIME_LIST_DETAIL_FRAGMENT = 6;
	public static final int LOADER_ID_SET_WAIT_TIME_ALERT_FRAGMENT = 7;
	public static final int LOADER_ID_FEATURE_LIST_DETAIL_FRAGMENT = 8;
	@Deprecated
	public static final int LOADER_ID_WAYFINDING_SERVICE = 9;
	@Deprecated
	public static final int LOADER_ID_WAIT_TIME_ALERT_CHECKER_SERVICE_NETWORK = 10;
	@Deprecated
	public static final int LOADER_ID_WAIT_TIME_ALERT_CHECKER_SERVICE_ALERTS = 11;
	public static final int LOADER_ID_HOME_ACTIVITY = 12;
	public static final int LOADER_ID_FEATURED_ITEM_FRAGMENT = 13;
	public static final int LOADER_ID_HOME_FRAGMENT = 14;
	public static final int LOADER_ID_HOME_FRAGMENT_2 = 15;
	public static final int LOADER_ID_SET_RIDE_OPEN_ALERT_FRAGMENT = 16;
	public static final int LOADER_ID_MULTI_CHOICE_FILTER_FRAGMENT = 17;
	public static final int LOADER_ID_NEWS_DETAIL_FRAGMENT = 18;
	public static final int LOADER_ID_FEATURE_LIST_DETAIL_FRAGMENT_EVENT_SERIES = 19;
	public static final int LOADER_ID_EXPLORE_LIST_FRAGMENT_EVENTS = 20;
	public static final int LOADER_ID_HOME_ACTIVITY_2 = 21;
	public static final int LOADER_ID_FEATURE_LIST_DETAIL_FRAGMENT_OFFERS = 22;
    public static final int LOADER_ID_HOME_FRAGMENT_3 = 23;
    public static final int LOADER_ID_FEATURED_ITEM_FRAGMENT_2 = 24;
	public static final int LOADER_ID_EXPLORE_RESULT_COUNT = 24;
	public static final int LOADER_ID_FEATURE_LIST_INTERACTIVE_EXPERIENCES = 25;
	public static final int LOADER_ID_HOME_FRAGMENT_4 = 26;
	public static final int LOADER_ID_HOME_FRAGMENT_5 = 27;
	public static final int LOADER_ID_HOME_FRAGMENT_6 = 28;
	public static final int LOADER_ID_HOME_FRAGMENT_7 = 29;
	public static final int LOADER_ID_HOME_FRAGMENT_8 = 30;
	public static final int LOADER_ID_FEATURED_ITEM_FRAGMENT_3 = 31;
	public static final int LOADER_ID_PHOTOFRAME_EXPERIENCE = 32;
	public static final int LOADER_ID_QUEUE = 33;
	public static final int LOADER_ID_APPOINTMENT_TICKETS = 34;
	public static final int LOADER_ID_APPOINMENT_TICKETS_UNREAD = 35;
	public static final int LOADER_ID_MOBILE_PAGES_FRAGMENT = 36;

	/**
	 * Threshold to detect when Android "double" loads the same content twice in a fragment. This
	 * usually happens when a fragment is using a loader within another fragment.
	 */
	public static final long DOUBLE_LOAD_THRESHOLD_MAX_IN_MS = 100;

	/**
	 * Create a {@link android.support.v4.content.CursorLoader} using a {@link DatabaseQuery}.
	 *
	 * @param databaseQuery
	 *         the database query to wrap
	 *
	 * @return the created loader
	 */
	public static CursorLoader createCursorLoader(DatabaseQuery databaseQuery) {

		Uri contentUri = databaseQuery.getContentUri();
		String[] projection = databaseQuery.getProjection();
		String selection = databaseQuery.getSelection();
		String[] selectionArgs = databaseQuery.getSelectionArgs();
		String orderBy = databaseQuery.getOrderBy();
		Context context = UniversalOrlandoApplication.getAppContext();

		CursorLoader cursorLoader = new CursorLoader(context, contentUri, projection, selection, selectionArgs, orderBy);
		return cursorLoader;
	}

	/**
	 * Android support library has a bug with {@link android.support.v4.view.ViewPager} not calling
	 * {@code onResume()} on the next fragment due to init loader being called on the first fragment
	 * without using a handler.
	 *
	 * @param fragment
	 *         the fragment to init the loader in
	 * @param savedInstanceState
	 *         the saved state of the fragment
	 * @param loaderId
	 *         the loader ID
	 * @param loaderArgs
	 *         the loader arguments
	 */
	public static <T extends Fragment & LoaderCallbacks<Cursor>> void initFragmentLoaderWithHandler(
			T fragment, Bundle savedInstanceState, int loaderId, Bundle loaderArgs) {

		if (savedInstanceState == null) {
			try {
				if (fragment.getActivity() != null
						&& fragment.getActivity().getWindow() != null
						&& fragment.getActivity().getWindow().getDecorView() != null
						&& fragment.getActivity().getWindow().getDecorView().getHandler() != null) {
					Handler handler = fragment.getActivity().getWindow().getDecorView().getHandler();
					handler.postDelayed(new InitLoaderRunnable<T>(fragment, loaderId, loaderArgs), 16);
				}
				else {
					// If the handler can't be used, load the normal way
					fragment.getLoaderManager().initLoader(loaderId, loaderArgs, fragment);
				}
			}
			catch (Exception e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "startFragmentLoaderWithHandler: exception trying to get handler to init loader", e);
				}

				// If the handler can't be used, load the normal way
				fragment.getLoaderManager().initLoader(loaderId, loaderArgs, fragment);
			}
		}
		else {
			fragment.getLoaderManager().initLoader(loaderId, loaderArgs, fragment);
		}
	}

	/**
	 * Class to initialize a loader in a runnable using weak references to prevent leaking a
	 * context.
	 */
	private static final class InitLoaderRunnable<T extends Fragment & LoaderCallbacks<Cursor>> implements Runnable {
		private final WeakReference<T> mFragment;
		private final Bundle mLoaderArgs;
		private final int mLoaderId;

		public InitLoaderRunnable(T fragment, int loaderId, Bundle loaderArgs) {
			mFragment = new WeakReference<T>(fragment);
			mLoaderId = loaderId;
			mLoaderArgs = loaderArgs;
		}

		@Override
		public void run() {
			final T fragment = mFragment.get();
			if (fragment != null && mLoaderArgs != null && fragment.isAdded()) {
				fragment.getLoaderManager().initLoader(mLoaderId, mLoaderArgs, fragment);
			}
		}
	}
}
