
package com.universalstudios.orlandoresort.controller.userinterface.web;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.analytics.CrashAnalyticsUtils;

import java.util.Locale;

import static com.universalstudios.orlandoresort.controller.userinterface.web.WebViewActivity.ENCODING_UTF8_HTML;
import static com.universalstudios.orlandoresort.controller.userinterface.web.WebViewActivity.MIME_TYPE_HTML;

/**
 * @author Steven Byle
 */
public class WebViewFragment extends Fragment implements ActionBarTitleProvider, View.OnClickListener {
	private static final String TAG = WebViewFragment.class.getSimpleName();

	private static final String KEY_ARG_WEB_PAGE_URL = "KEY_ARG_WEB_PAGE_URL";
	private static final String KEY_ARG_WEB_PAGE_RAW_CONTENT = "KEY_ARG_WEB_PAGE_RAW_CONTENT";
	private static final String KEY_ARG_USE_WIDE_VIEW_PORT = "KEY_ARG_USE_WIDE_VIEW_PORT";
	private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";

	private Integer mActionBarTitleResId;
	private String mWebPageUrl;
	private String mRawHtmlContent;
	private DrawerStateProvider mParentDrawerStateProvider;
	private WebView mWebView;
	private ViewGroup mNoConnectionLayout;

    private ViewGroup mLoadingLayout;
    private Button mRetryButton;
    private TextView mLoadingDetailText;
    private TextView mLoadingTitleText;
    private ImageView mLoadingImage;
	private boolean mUseWideViewPort;

	public static WebViewFragment newInstance(String webPageUrl) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: webPageUrl = " + webPageUrl);
		}

		// Create a new fragment instance
		WebViewFragment fragment = new WebViewFragment();

		/// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		// Add parameters to the argument bundle
		args.putString(KEY_ARG_WEB_PAGE_URL, webPageUrl);
		fragment.setArguments(args);

		return fragment;
	}


	public static WebViewFragment newInstance(int actionBarTitleResId, String webPageUrl) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: webPageUrl = " + webPageUrl);
		}

		// Create a new fragment instance
		WebViewFragment fragment = new WebViewFragment();

		/// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		// Add parameters to the argument bundle
		args.putInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID, actionBarTitleResId);
		args.putString(KEY_ARG_WEB_PAGE_URL, webPageUrl);
		fragment.setArguments(args);

		return fragment;
	}

	public static WebViewFragment newInstanceWithRawHtmlContent(String rawHtmlContent, boolean useWideViewPort) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: rawHTMLContent = " + rawHtmlContent);
		}

		// Create a new fragment instance
		WebViewFragment fragment = new WebViewFragment();

		/// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		// Add parameters to the argument bundle
		args.putString(KEY_ARG_WEB_PAGE_RAW_CONTENT, rawHtmlContent);
		args.putBoolean(KEY_ARG_USE_WIDE_VIEW_PORT, useWideViewPort);
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
			mWebPageUrl = null;
			mActionBarTitleResId = null;
			mRawHtmlContent = null;
		}
		// Otherwise, set incoming parameters
		else {
			mWebPageUrl = args.getString(KEY_ARG_WEB_PAGE_URL);
			mActionBarTitleResId = args.getInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID);
			mRawHtmlContent = args.getString(KEY_ARG_WEB_PAGE_RAW_CONTENT);
			mUseWideViewPort = args.getBoolean(KEY_ARG_USE_WIDE_VIEW_PORT);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			if (mActionBarTitleResId != null) {
				switch (mActionBarTitleResId.intValue()) {
					case R.string.privacy_and_legal_option_privacy_policy:
						// Track the page view
						AnalyticsUtils.trackPageView(
								AnalyticsUtils.CONTENT_GROUP_SITE_NAV,
								null, null,
								AnalyticsUtils.CONTENT_SUB_2_PRIVACY_POLICY,
								AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
								null, null);
						break;
					case R.string.privacy_and_legal_option_terms_of_service:
						// Track the page view
						AnalyticsUtils.trackPageView(
								AnalyticsUtils.CONTENT_GROUP_SITE_NAV,
								null, null,
								AnalyticsUtils.CONTENT_SUB_2_TERMS_OF_SERVICE,
								AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
								null, null);
						break;
					default:
						break;
				}
			}
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
		View fragmentView = inflater.inflate(R.layout.fragment_webview, container, false);

		// Setup Views
		mWebView = (WebView) fragmentView.findViewById(R.id.fragment_webview_webview);
		mNoConnectionLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_webview_no_connection_layout);

        mLoadingLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_webview_loading_layout);
        mRetryButton = (Button) fragmentView.findViewById(R.id.webviewRetry);
        mLoadingDetailText = (TextView) fragmentView.findViewById(R.id.fragment_webview_loading_message_text);
        mLoadingTitleText = (TextView) fragmentView.findViewById(R.id.fragment_webview_loading_title_text);
        mLoadingImage = (ImageView) fragmentView.findViewById(R.id.fragment_webview_loading_image);

		// Load content into web view
		boolean isNetworkConnected = NetworkUtils.isNetworkConnected();
		if (isNetworkConnected) {
			mWebView.setVisibility(View.VISIBLE);
			mNoConnectionLayout.setVisibility(View.GONE);

			WebSettings webSettings = mWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			webSettings.setAllowContentAccess(false);
			webSettings.setAllowFileAccess(false);
			webSettings.setSupportMultipleWindows(false);
			webSettings.setSupportZoom(true);
			webSettings.setBuiltInZoomControls(true);
			webSettings.setDisplayZoomControls(false);
			webSettings.setUseWideViewPort(mUseWideViewPort);
			webSettings.setLoadWithOverviewMode(true);

			mWebView.setWebViewClient(new ControlledWebViewClient());

			if(null != mRawHtmlContent && !TextUtils.isEmpty(mRawHtmlContent)){
				mWebView.loadDataWithBaseURL(BuildConfig.COMMERCE_SERVICES_BASE_URL, mRawHtmlContent, MIME_TYPE_HTML, ENCODING_UTF8_HTML, null);
			} else if (null != mWebPageUrl && !TextUtils.isEmpty(mWebPageUrl)){
				mWebView.loadUrl(mWebPageUrl);
			} else {
				if(BuildConfig.DEBUG){
					//THIS SHOULD NEVER HAPPEN!
					Log.e(TAG, "The webview has not been given valid content to show");
				}
			}
		}
		else {
			mWebView.setVisibility(View.GONE);
			mNoConnectionLayout.setVisibility(View.VISIBLE);
		}

		return fragmentView;
	}

	@Override
	public String provideTitle() {
		if (mActionBarTitleResId == null) {
			return "";
		}
		return getString(mActionBarTitleResId);
	}

	private void showErrorView(boolean show) {
		mLoadingLayout.setVisibility(show ? View.VISIBLE : View.GONE);
		mRetryButton.setVisibility(show ? View.VISIBLE : View.GONE);
		if (show) {
			mLoadingDetailText.setText(getString(R.string.webview_error_detail));
			mLoadingTitleText.setText(getString(R.string.webview_error_loading_page));
			mLoadingImage.setImageResource(R.drawable.no_results_homer);
		}
		mRetryButton.setOnClickListener(show ? this : null);
	}

	public class ControlledWebViewClient extends WebViewClient {

		@Override
		public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
			super.onReceivedError(view, request, error);
			mWebView.destroy();
			showErrorView(true);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "shouldOverrideUrlLoading: url = " + url);
			}

			String urlLowerCase = url.toLowerCase(Locale.US);

			// If the URL is a web link, load it in the web view
			if (urlLowerCase.startsWith("http:") || urlLowerCase.startsWith("https:")) {
				return false;
			}

			// Otherwise, send an intent and let the system handle it
			try {
				view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			}
			catch (Exception e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "shouldOverrideUrlLoading: exception trying to send intent", e);
				}

				// Log the exception to crittercism
				CrashAnalyticsUtils.logHandledException(e);
			}
			return true;
		}
	}

    @Override
    public void onClick(View v) {
        if (v.getId() == mRetryButton.getId()) {
            mWebView.reload();
        }
    }

	public boolean handleBrowserBack() {
		if (mWebView != null && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return false;
	}
}
