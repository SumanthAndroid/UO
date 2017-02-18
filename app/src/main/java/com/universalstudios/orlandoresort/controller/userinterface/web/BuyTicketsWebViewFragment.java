
package com.universalstudios.orlandoresort.controller.userinterface.web;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crittercism.app.Crittercism;
import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.view.TintUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.api_compatibility.CompatibilityManager;


/**
 * @author Steven Byle
 */
public class BuyTicketsWebViewFragment extends Fragment implements ActionBarTitleProvider, View.OnClickListener {
	private static final String TAG = BuyTicketsWebViewFragment.class.getSimpleName();

	private static final String KEY_ARG_WEB_PAGE_URL = "KEY_ARG_WEB_PAGE_URL";
	private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";

	private Integer mActionBarTitleResId;
	private String mWebPageUrl;
	private DrawerStateProvider mParentDrawerStateProvider;
	private WebView mWebView;
	private ViewGroup mNoConnectionLayout;
	private ViewGroup mLoadingLayout;
    private Button mRetryButton;
    private TextView mLoadingDetailText;
    private TextView mLoadingTitleText;
    private ImageView mLoadingImage;
	private View cartView;
	private ImageView cartImageView;
	private TextView cartCountTextView;
	private int cartCount;

	public static BuyTicketsWebViewFragment newInstance(String webPageUrl) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: webPageUrl = " + webPageUrl);
		}

		// Create a new fragment instance
		BuyTicketsWebViewFragment fragment = new BuyTicketsWebViewFragment();

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

	public static BuyTicketsWebViewFragment newInstance(int actionBarTitleResId, String webPageUrl) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: webPageUrl = " + webPageUrl);
		}

		// Create a new fragment instance
		BuyTicketsWebViewFragment fragment = new BuyTicketsWebViewFragment();

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
		}
		// Otherwise, set incoming parameters
		else {
			mWebPageUrl = args.getString(KEY_ARG_WEB_PAGE_URL);
			mActionBarTitleResId = args.getInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID);
		}

		cartCount = 0;

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
		View fragmentView = inflater.inflate(R.layout.fragment_tickets_webview, container, false);

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

			mWebView.setWebViewClient(new BuyTicketsWebViewClient());
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.addJavascriptInterface(new BuyTicketsJavascriptInterface(), "Android");
			mWebView.loadUrl(mWebPageUrl);
			if (CompatibilityManager.isAboveApi(19)) {
				mWebView.evaluateJavascript("setTimeout(function(){  Android.status(\"PageLoaded\"); }, 30000);", null);
			} else {
				mWebView.loadUrl("javascript:setTimeout(function(){  Android.status(\"PageLoaded\"); }, 30000);");
			}
			showLoadingView(true, false);
		}
		else {
			mWebView.setVisibility(View.GONE);
			mNoConnectionLayout.setVisibility(View.VISIBLE);
			showLoadingView(false, false);
		}

		return fragmentView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreateOptionsMenu");
		}

		inflater.inflate(R.menu.action_view_cart, menu);
		MenuItem menuItem = menu.findItem(R.id.action_buytickets);
		if (menuItem != null) {
			menuItem.setVisible(true).setEnabled(true);
			cartView = menuItem.getActionView();
			cartCountTextView = (TextView) menuItem.getActionView().findViewById(R.id.ticket_cart_textview);
			cartImageView = (ImageView) menuItem.getActionView().findViewById(R.id.ticket_cart_imageview);
			cartView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (CompatibilityManager.isAboveApi(19)) {
						mWebView.evaluateJavascript("if (bb.settings.$html.hasClass(bb.cart.cartInClass)) {bb.cart.closeCart();} else {bb.cart.openCart();}", null);
					} else {
						mWebView.loadUrl("javascript:if (bb.settings.$html.hasClass(bb.cart.cartInClass)) {bb.cart.closeCart();} else {bb.cart.openCart();}");
					}
				}
			});
		}
		TintUtils.tintAllMenuItems(menu, getContext());
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onPrepareOptionsMenu");
		}

		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onOptionsItemSelected");
		}

		switch (item.getItemId()) {
			case R.id.action_buytickets:
				return true;

			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onOptionsItemSelected: unknown menu item selected");
				}
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewStateRestored: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Enable action bar items after creating the pager adapter, because the
		// menu options rely on the state of the adapter
		setHasOptionsMenu(true);
	}

    @Override
    public void onClick(View v) {
        if (v.getId() == mRetryButton.getId()) {
            showLoadingView(true, true);
            String url = (String) v.getTag();
            mWebView.loadUrl(TextUtils.isEmpty(url) ? mWebPageUrl : url, null);
        }
    }

    private void setCartCount(final int count){
		if(getActivity() == null){
			return;
		}

		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				cartCount = count;
				cartCountTextView.setText(String.valueOf(cartCount));
				cartView.invalidate();
			}
		});
	}

	private void showLoadingView(boolean showView, boolean showOverrideError) {
        if (mRetryButton.getVisibility() == View.VISIBLE && !showOverrideError) {
            return;
        }
        showErrorView(false, null);
		mLoadingLayout.setVisibility(showView ? View.VISIBLE : View.GONE);
        mLoadingDetailText.setText(getString(R.string.splash_loading_content_description));
        mLoadingTitleText.setText(getString(R.string.webview_loading_content));
        mLoadingImage.setImageResource(R.drawable.no_alerts_shrek);
	}

	private void showErrorView(boolean show, String failingUrl) {
		mLoadingLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        mRetryButton.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            mRetryButton.setTag(failingUrl);
            mLoadingDetailText.setText(getString(R.string.webview_error_detail));
            mLoadingTitleText.setText(getString(R.string.webview_error_loading_page));
            mLoadingImage.setImageResource(R.drawable.no_results_homer);
        }
        mRetryButton.setOnClickListener(show ? this : null);
	}

	@Override
	public String provideTitle() {
		if (mActionBarTitleResId == null) {
			return "";
		}
		return getString(mActionBarTitleResId);
	}

	public class BuyTicketsJavascriptInterface {

			//should be s dictionary object
			@JavascriptInterface
			public void cartCountInterOp(String jsonObject){

				CartCount cartCount = GsonObject.fromJson(jsonObject, CartCount.class);

				if(cartCount != null){

					int count = cartCount.getCount();

					if(BuildConfig.DEBUG){
						Log.d(TAG, "Updating cart count " +count);
					}

					setCartCount(count);
				}
			}

			//should be an integer
			@JavascriptInterface
			public void cartDisplayInterOp(int number){


				final Boolean displayCart = number == 1;

				if(getActivity() == null){
					return;
				}

				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (displayCart && (cartView.getVisibility() == View.INVISIBLE)) {
							cartView.setVisibility(View.VISIBLE);
						} else if (!displayCart && (cartView.getVisibility() == View.VISIBLE)) {
							cartView.setVisibility(View.INVISIBLE);
						}

						cartView.invalidate();
					}
				});

			}

			//should be a string
			@JavascriptInterface
			public void status(String statusStr){
				if(statusStr.equals("PageLoaded")){

					if(getActivity() == null){
						return;
					}

					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showLoadingView(false, false);
							cartView.setVisibility(View.VISIBLE);
						}
					});
				}
			}

			@JavascriptInterface
			public void toggleCartt(int toggleNum){

					if(getActivity() == null){
						return;
					}

					final int toggleNumber = toggleNum;
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showLoadingView(false, false);
							cartView.setVisibility((toggleNumber == 0) ? View.INVISIBLE : View.VISIBLE);
						}
					});
			}
	}

	public class CartCount{

		@SerializedName("count")
		public int count;

		public int getCount() {
			return count;
		}
	}

    public class BuyTicketsWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            showErrorView(true, null);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            showErrorView(true, null);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            showErrorView(true, failingUrl);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (null == getActivity()) {
                return;
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLoadingView(false, false);
                }
            });
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            showErrorView(true, null);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "shouldOverrideUrlLoading: url = " + url);
            }

            String urlLowerCase = url.toLowerCase();

            if (urlLowerCase.startsWith("http:") || urlLowerCase.startsWith("https:")) {
                return false;
            }

            try {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "shouldOverrideUrlLoading: exception trying to send intent", e);
                }

                Crittercism.logHandledException(e);
            }

            return true;
        }
    }

    public boolean onBackPressed() {
        if (null == mWebView) {
            return false;
        }
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }
}
