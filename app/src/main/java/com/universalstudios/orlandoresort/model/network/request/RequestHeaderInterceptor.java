package com.universalstudios.orlandoresort.model.network.request;

import retrofit.RequestInterceptor;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;

import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkService;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

/**
 * Request interceptor that attaches global headers for all network requests
 * sent with {@link NetworkService}.
 * 
 * @author Steven Byle
 */
public class RequestHeaderInterceptor implements RequestInterceptor {
	private static final String TAG = RequestHeaderInterceptor.class.getSimpleName();

	private final Context mAppContext;

	/**
	 * @param context
	 * 
	 */
	public RequestHeaderInterceptor(Context context) {
		super();
		mAppContext = context.getApplicationContext();
	}

	@Override
	public void intercept(RequestFacade request) {

		// Add static headers
		request.addHeader(RequestHeaders.Keys.ACCEPT, RequestHeaders.Values.ACCEPT_APPLICATION_JSON);
		request.addHeader(RequestHeaders.Keys.ACCEPT_LANGUAGE, RequestHeaders.Values.ACCEPT_LANGUAGE_EN_US);

		// Add app and device headers
		PackageInfo pInfo = UniversalOrlandoApplication.getPackageInfo();
		if (pInfo != null) {
			request.addHeader(RequestHeaders.Keys.X_UNIWEBSERVICE_APP_VERSION, pInfo.versionName);
		}
		request.addHeader(RequestHeaders.Keys.X_UNIWEBSERVICE_PLATFORM, RequestHeaders.Values.X_UNIWEBSERVICE_PLATFORM_ANDROID);
		request.addHeader(RequestHeaders.Keys.X_UNIWEBSERVICE_PLATFORM_VERSION, Build.VERSION.RELEASE);
		request.addHeader(RequestHeaders.Keys.X_UNIWEBSERVICE_DEVICE, Build.MANUFACTURER + " " + Build.MODEL);

		// Add auth token headers, if they are set
		UniversalAppState universalAppState = UniversalAppStateManager.getInstance();
		String token = universalAppState.getToken();
		if (token != null) {
			request.addHeader(RequestHeaders.Keys.X_UNIWEBSERVICE_API_KEY, RequestHeaders.Values.X_UNIWEBSERVICE_API_KEY_ANDROID);
			request.addHeader(RequestHeaders.Keys.X_UNIWEBSERVICE_TOKEN, token);
		}
	}
}