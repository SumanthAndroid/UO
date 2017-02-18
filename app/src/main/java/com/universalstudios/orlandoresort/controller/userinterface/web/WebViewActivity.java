/**
 *
 */
package com.universalstudios.orlandoresort.controller.userinterface.web;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;

/**
 * @author Steven Byle
 */
public class WebViewActivity extends NetworkRefreshActivity {
	private static final String TAG = WebViewActivity.class.getSimpleName();

	public static final String MIME_TYPE_HTML = "text/html; charset=utf-8";
	public static final String ENCODING_UTF8_HTML = "UTF-8";

	private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";
	private static final String KEY_ARG_ACTION_BAR_TITLE_STRING = "KEY_ARG_ACTION_BAR_TITLE_STRING";
	private static final String KEY_ARG_WEB_PAGE_URL = "KEY_ARG_WEB_PAGE_URL";
	private static final String KEY_ARG_OVERRIDE_BACK_BUTTON = "KEY_ARG_OVERRIDE_BACK_BUTTON";
	private static final String KEY_ARG_WEB_PAGE_RAW_HTML_CONTENT = "KEY_ARG_WEB_PAGE_RAW_HTML_CONTENT";
	private static final String KEY_ARG_WEB_PAGE_USE_WIDE_VIEW_PORT = "KEY_ARG_WEB_PAGE_USE_WIDE_VIEW_PORT";

	private int mActionBarTitleResId;
	private String mActionBarTitleString;
	private ViewGroup mWebViewFragmentContainer;
	private WebViewFragment mWebViewFragment;
	private boolean mSystemBackIsBrowserBack;

	public static Bundle newInstanceBundleWithRawHtmlContent(String actionBarTitle, String rawHtmlContent) {
		return newInstanceBundleWithRawHtmlContent(actionBarTitle, rawHtmlContent, true);
	}

	public static Bundle newInstanceBundleWithRawHtmlContent(String actionBarTitle, String rawHtmlContent, boolean useWideViewPort) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = newInstanceBundle(actionBarTitle, null, false);

		// Add parameters to the argument bundle
		args.putString(KEY_ARG_WEB_PAGE_RAW_HTML_CONTENT, rawHtmlContent);
		args.putBoolean(KEY_ARG_WEB_PAGE_USE_WIDE_VIEW_PORT, useWideViewPort);
		return args;
	}

	public static Bundle newInstanceBundle(int actionBarTitleResId, String webPageUrl) {
		return newInstanceBundle(actionBarTitleResId, webPageUrl, false);
	}

	public static Bundle newInstanceBundle(int actionBarTitleResId, String webPageUrl, boolean systemBackIsBrowserBack) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = new Bundle();

		// Add parameters to the argument bundle
		args.putInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID, actionBarTitleResId);
		args.putString(KEY_ARG_WEB_PAGE_URL, webPageUrl);
		args.putBoolean(KEY_ARG_OVERRIDE_BACK_BUTTON, systemBackIsBrowserBack);

		return args;
	}

	public static Bundle newInstanceBundle(String actionBarTitle, String webPageUrl) {
		return newInstanceBundle(actionBarTitle, webPageUrl, false);
	}

	public static Bundle newInstanceBundle(String actionBarTitle, String webPageUrl, boolean systemBackIsBrowserBack) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = new Bundle();

		// Add parameters to the argument bundle
		args.putString(KEY_ARG_ACTION_BAR_TITLE_STRING, actionBarTitle);
		args.putString(KEY_ARG_WEB_PAGE_URL, webPageUrl);
		args.putBoolean(KEY_ARG_OVERRIDE_BACK_BUTTON, systemBackIsBrowserBack);

		return args;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
		setContentView(R.layout.activity_webview);

		String webPageUrl;
		String rawHtmlContent;
		boolean useWideViewPort = true;

		// Default parameters
		Bundle args = getIntent().getExtras();
		if (args == null) {
			mActionBarTitleResId = -1;
			webPageUrl = null;
			rawHtmlContent = null;
		}
		// Otherwise, set incoming parameters
		else {
			mActionBarTitleResId = args.getInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID, -1);
			mActionBarTitleString = args.getString(KEY_ARG_ACTION_BAR_TITLE_STRING, null);
			mSystemBackIsBrowserBack = args.getBoolean(KEY_ARG_OVERRIDE_BACK_BUTTON);
			rawHtmlContent = args.getString(KEY_ARG_WEB_PAGE_RAW_HTML_CONTENT);
			useWideViewPort = args.getBoolean(KEY_ARG_WEB_PAGE_USE_WIDE_VIEW_PORT);
			webPageUrl = args.getString(KEY_ARG_WEB_PAGE_URL);
		}

		mWebViewFragmentContainer = (ViewGroup) findViewById(R.id.activity_webview_container);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

			// Load the webivew mWebViewFragment
			if (mWebViewFragmentContainer != null) {

				if (rawHtmlContent != null) {
					mWebViewFragment = WebViewFragment.newInstanceWithRawHtmlContent(rawHtmlContent, useWideViewPort);
				} else if (mActionBarTitleString != null) {
					mWebViewFragment = WebViewFragment.newInstance(webPageUrl);
				} else {
					mWebViewFragment = WebViewFragment.newInstance(mActionBarTitleResId, webPageUrl);
				}
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mWebViewFragmentContainer.getId(), mWebViewFragment,
						mWebViewFragment.getClass().getName());

				fragmentTransaction.commit();
			}
		}
		// Otherwise, restore state
		else {

		}

		ActionBar actionBar = getActionBar();
		if (mActionBarTitleResId != -1) {
			actionBar.setTitle(mActionBarTitleResId);
		} else {
			actionBar.setTitle(mActionBarTitleString);
		}
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
	public void onDestroy() {
		super.onDestroy();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroy");
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onOptionsItemSelected");
		}

		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return (super.onOptionsItemSelected(item));
		}
	}

	@Override
	public void onBackPressed() {
		boolean letSystemHandleBack = true;

		// If desired, let the browser handle the back press
		if (mSystemBackIsBrowserBack && mWebViewFragment != null) {
			letSystemHandleBack = !mWebViewFragment.handleBrowserBack();
		}

		// If not handled by the browser, let back behave like normal
		if (letSystemHandleBack) {
			super.onBackPressed();
		}
	}
}
