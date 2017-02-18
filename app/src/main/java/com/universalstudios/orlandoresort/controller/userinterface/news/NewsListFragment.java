package com.universalstudios.orlandoresort.controller.userinterface.news;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.news.News;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.NewsTable;
import com.universalstudios.orlandoresort.view.TintUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Steven Byle
 */
public class NewsListFragment extends DatabaseQueryFragment implements OnItemClickListener, ActionBarTitleProvider {
	private static final String TAG = NewsListFragment.class.getSimpleName();

	private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";

	private Integer mActionBarTitleResId;
	private ListView mNewsListView;
	private NewsListCursorAdapter mNewsCursorAdapter;
	private ViewGroup mNoNewsLayout;
	private boolean mDoesAnyUnreadNewsExist;
	private List<News> mNewsList;
	private DrawerStateProvider mParentDrawerStateProvider;

	public static NewsListFragment newInstance(int actionBarTitleResId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance");
		}

		// Create a new fragment instance
		NewsListFragment fragment = new NewsListFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		DatabaseQuery databaseQuery = DatabaseQueryUtils.getAllActiveNewsDatabaseQuery();
		args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
		args.putInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID, actionBarTitleResId);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onAttach");
		}

		// Check if parent fragment (if there is one) implements the interface
		Fragment parentFragment = getParentFragment();
		if (parentFragment != null && parentFragment instanceof DrawerStateProvider) {
			mParentDrawerStateProvider = (DrawerStateProvider) parentFragment;
		}
		// Otherwise, check if parent activity implements the interface
		else if (activity != null && activity instanceof DrawerStateProvider) {
			mParentDrawerStateProvider = (DrawerStateProvider) activity;
		}
		// If neither implements the interface, log a warning
		else if (mParentDrawerStateProvider == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement DrawerStateProvider");
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Default parameters
		Bundle args = getArguments();
		if (args == null) {
			mActionBarTitleResId = null;
		}
		// Otherwise, set incoming parameters
		else {
			mActionBarTitleResId = args.getInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID);
		}

		mDoesAnyUnreadNewsExist = false;

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

			// Track the page view
			sendPageAnalytics();
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Set the action bar title, if the drawer isn't open
		if (mParentDrawerStateProvider != null && !mParentDrawerStateProvider.isDrawerOpenAtAll()) {
			Activity parentActivity = getActivity();
			if (parentActivity != null) {
				parentActivity.getActionBar().setTitle(provideTitle());
			}
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_news_list, container, false);

		// Setup Views
		mNewsListView = (ListView) fragmentView.findViewById(R.id.fragment_news_list_listview);
		mNoNewsLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_news_list_no_results_layout);

		mNewsCursorAdapter = new NewsListCursorAdapter(getActivity(), null, true);
		mNewsListView.setAdapter(mNewsCursorAdapter);
		mNewsListView.setOnItemClickListener(this);

		// If this is the first creation, default state
		if (savedInstanceState == null) {
			mNewsListView.setVisibility(View.VISIBLE);
			mNoNewsLayout.setVisibility(View.GONE);
		}
		else {

		}

		return fragmentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onActivityCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewStateRestored: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Enable action bar items
		setHasOptionsMenu(true);
	}

	@Override
	public void onStart() {
		super.onStart();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStart");
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onResume");
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onPause");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onSaveInstanceState");
		}
	}

	@Override
	public void onStop() {
		super.onStop();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStop");
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroyView");
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroy");
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreateOptionsMenu");
		}

		inflater.inflate(R.menu.action_mark_all_as_read, menu);
		TintUtils.tintAllMenuItems(menu, getContext());
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onPrepareOptionsMenu");
		}
		boolean isDrawerOpenAtAll = mParentDrawerStateProvider.isDrawerOpenAtAll();

		MenuItem menuItem = menu.findItem(R.id.action_mark_all_as_read);
		if (menuItem != null) {
			menuItem.setVisible(!isDrawerOpenAtAll && mDoesAnyUnreadNewsExist).setEnabled(!isDrawerOpenAtAll && mDoesAnyUnreadNewsExist);
		}

		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onOptionsItemSelected");
		}

		switch (item.getItemId()) {
			case R.id.action_mark_all_as_read:
				FragmentManager fragmentManager = getChildFragmentManager();
				boolean isDialogFragmentShowing =
						fragmentManager.findFragmentByTag(ConfirmMarkAllAsReadDialogFragment.class.getSimpleName()) != null;

				if (!isDialogFragmentShowing) {
					ConfirmMarkAllAsReadDialogFragment dialogFragment =
							ConfirmMarkAllAsReadDialogFragment.newInstance(mNewsList);
					dialogFragment.show(fragmentManager, dialogFragment.getClass().getSimpleName());
				}
				return true;
			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onOptionsItemSelected: unknown menu item selected");
				}
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onItemClick: position = " + position + " id = " + id);
		}

		// Open news detail page
		Cursor cursor = (Cursor) mNewsCursorAdapter.getItem(position);
		if (cursor != null && id != -1) {
			Long newsId = cursor.getLong(cursor.getColumnIndex(NewsTable.COL_NEWS_ID));

			Activity parentActivity = getActivity();
			if (parentActivity != null && newsId != null) {
				parentActivity.startActivity(new Intent(parentActivity, NewsDetailActivity.class).putExtras(NewsDetailActivity.newInstanceBundle(newsId)));
			}
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoadFinished");
		}

		switch (loader.getId()) {
			case LOADER_ID_DATABASE_QUERY:
				// Let the parent class check for double loads
				if (checkIfDoubleOnLoadFinished()) {
					if (BuildConfig.DEBUG) {
						Log.i(TAG, "onLoadFinished: ignoring update due to double load");
					}
					return;
				}

				// Swap current cursor with a new up to date cursor
				mNewsCursorAdapter.swapCursor(data);

				// Show/hide the no news layout
				boolean doesAnyNewsExist = (data != null && data.getCount() > 0);
				mNewsListView.setVisibility(doesAnyNewsExist ? View.VISIBLE : View.GONE);
				mNoNewsLayout.setVisibility(doesAnyNewsExist ? View.GONE : View.VISIBLE);

				// Only show the mark all as read icon if there are unread items
				mDoesAnyUnreadNewsExist = false;
				mNewsList = new ArrayList<News>();
				if (data != null && data.moveToFirst()) {
					do {
						String newsObjectJson = data.getString(data.getColumnIndex(NewsTable.COL_NEWS_OBJECT_JSON));
						News news = GsonObject.fromJson(newsObjectJson, News.class);
						mNewsList.add(news);

						boolean hasBeenRead = (data.getInt(data.getColumnIndex(NewsTable.COL_HAS_BEEN_READ)) != 0);
						if (!hasBeenRead) {
							mDoesAnyUnreadNewsExist = true;
						}
					} while (data.moveToNext());
				}

				// Refresh the action bar items
				invalidateOptionsMenu();
				break;
			default:
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoaderReset");
		}

		switch (loader.getId()) {
			case LOADER_ID_DATABASE_QUERY:
				// Data is not available anymore, delete reference
				mNewsCursorAdapter.swapCursor(null);
				break;
			default:
				break;
		}
	}

	@Override
	public String provideTitle() {
		if (mActionBarTitleResId == null) {
			return "";
		}
		return getString(mActionBarTitleResId);
	}

	private void invalidateOptionsMenu() {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			parentActivity.invalidateOptionsMenu();
		}
	}

	private void sendPageAnalytics() {
		AnalyticsUtils.trackPageView(
				AnalyticsUtils.CONTENT_GROUP_PLANNING,
				AnalyticsUtils.CONTENT_FOCUS_GUEST_SERVICES,
				null,
				AnalyticsUtils.CONTENT_SUB_2_PARK_NOTIFICATIONS,
				AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
				null,
				null);
	}

	public static class ConfirmMarkAllAsReadDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
		private static final String TAG = ConfirmMarkAllAsReadDialogFragment.class.getSimpleName();

		private static final String KEY_ARG_NEWS_LIST_JSON = "KEY_ARG_NEWS_LIST_JSON";
		private List<News> mNewsList;

		public static ConfirmMarkAllAsReadDialogFragment newInstance(List<News> newsList) {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "newInstance" );
			}

			// Create a new fragment instance
			ConfirmMarkAllAsReadDialogFragment fragment = new ConfirmMarkAllAsReadDialogFragment();

			// Get arguments passed in, if any
			Bundle args = fragment.getArguments();
			if (args == null) {
				args = new Bundle();
			}

			// Add parameters to the argument bundle
			if (newsList != null) {
				args.putString(KEY_ARG_NEWS_LIST_JSON, new Gson().toJson(newsList));
			}
			else {
				args.putString(KEY_ARG_NEWS_LIST_JSON, new Gson().toJson(new ArrayList<News>()));
			}

			fragment.setArguments(args);

			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			if (BuildConfig.DEBUG) {
				Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
			}

			// Default parameters
			Bundle args = getArguments();
			if (args == null) {
				mNewsList = null;
			}
			// Otherwise, set incoming parameters
			else {
				String newsListJson = args.getString(KEY_ARG_NEWS_LIST_JSON);
				mNewsList = new Gson().fromJson(newsListJson, new TypeToken<List<News>>() {}.getType());
			}
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			alertDialogBuilder.setTitle(R.string.news_confirm_mark_all_as_read_title);
			alertDialogBuilder.setMessage(R.string.news_confirm_mark_all_as_read_message);
			alertDialogBuilder.setPositiveButton(R.string.news_confirm_mark_all_as_read_positive_button, this);
			alertDialogBuilder.setNegativeButton(R.string.news_confirm_mark_all_as_read_negative_button, this);

			Dialog dialog = alertDialogBuilder.create();
			dialog.setCanceledOnTouchOutside(false);
			return dialog;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Activity parentActivity = getActivity();
					if (parentActivity != null) {
						NewsUtils.updateNewsHasBeenReadInDatabase(mNewsList, true, parentActivity.getContentResolver(), true);
					}
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					// Do nothing, just dismiss the dialog
					break;
			}
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			super.onCancel(dialog);

			// Simulate a cancel click
			onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
		}
	}
}