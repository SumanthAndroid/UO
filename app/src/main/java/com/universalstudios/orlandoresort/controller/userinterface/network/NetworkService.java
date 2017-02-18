package com.universalstudios.orlandoresort.controller.userinterface.network;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.request.RequestHeaderInterceptor;
import com.universalstudios.orlandoresort.model.network.request.RequestQueue;
import com.universalstudios.orlandoresort.model.network.request.RequestQueueItem;
import com.universalstudios.orlandoresort.model.network.service.RetrofitNetworkLog;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.OkClient;

/**
 * Network service that processes the entire request queue by priority and makes the network calls
 * in the background. If killed by the OS, this service will restart itself and continue
 * processing.
 *
 * @author Steven Byle
 */
public class NetworkService extends IntentService {
	private static final String TAG = NetworkService.class.getSimpleName();

	private static final int CONNECT_TIMEOUT_MS = 15 * 1000; // 15 seconds
	private static final int READ_TIMEOUT_MS = 20 * 1000; // 20 seconds

	private HashMap<String, RestAdapter> mRestAdapters;
	private OkHttpClient mOkHttpClient;
	private OkClient mOkClient;
	private RequestHeaderInterceptor mRequestHeaderInterceptor;
	private RetrofitNetworkLog mNetworkLog;

	/**
	 * Constructor
	 */
	public NetworkService() {
		super(TAG);

		// Have the service restart if killed
		setIntentRedelivery(true);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate");
		}

		// **** ONLY TRUST ANY SSL CERT WHEN TESTING IN DEV ENVIRONMENTS ****
		if (!BuildConfig.SSL_CERT_VERIFICATION_ENABLED) {
			trustAllCerts();
		}

		// Instantiate map to hold rest adapters
		mRestAdapters = new HashMap<String, RestAdapter>();

		// Create a network client to reuse for the network calls
		mOkHttpClient = new OkHttpClient();
		mOkHttpClient.setConnectTimeout(CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS);
		mOkHttpClient.setReadTimeout(READ_TIMEOUT_MS, TimeUnit.MILLISECONDS);
		mOkHttpClient.setFollowSslRedirects(true);
		if (BuildConfig.NETWORK_TRACING_ENABLED) {
			mOkHttpClient.networkInterceptors().add(new StethoInterceptor());
		}
		mOkClient = new OkClient(mOkHttpClient);

		mRequestHeaderInterceptor = new RequestHeaderInterceptor(this);
		mNetworkLog = new RetrofitNetworkLog();
	}

	/**
	 * This method runs off the main UI thread, so it is OK to block and do long operations here.
	 * This method will process every network request from the queue in the order of their priority
	 * and make their corresponding network call, in a synchronous or asynchronous manner, depending
	 * on the request settings. After all requests in the request queue have been sent, the service
	 * destroys.
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onHandleIntent");
		}

		RequestQueueItem requestQueueItem;
		while ((requestQueueItem = RequestQueue.pollRequest()) != null) {

			NetworkRequest networkRequest = requestQueueItem.getNetworkRequest();
			String servicesBaseUrl = networkRequest.getServicesBaseUrl();

			// If control properties haven't synced yet, load a bad URL so the
			// subsequent requests will fail
			if (servicesBaseUrl == null || servicesBaseUrl.equals("")) {
				servicesBaseUrl = "http://notarealserver.notarealdomain";
			}

			// Use a unique key for each server URL
			String requestAdapterKey = servicesBaseUrl;
			RestAdapter restAdapter = mRestAdapters.get(requestAdapterKey);

			// If the rest adapter doesn't exist, create a new one
			if (restAdapter == null) {
				restAdapter = new RestAdapter.Builder()
						.setEndpoint(servicesBaseUrl)
						.setClient(mOkClient)
						.setRequestInterceptor(mRequestHeaderInterceptor)
						.setLogLevel(BuildConfig.LOGGING_ENABLED ? LogLevel.FULL : LogLevel.NONE)
						.setLog(mNetworkLog)
						.build();

				mRestAdapters.put(requestAdapterKey, restAdapter);
			}

			if (BuildConfig.DEBUG) {
				Log.i(TAG, "onHandleIntent: requestAdapterKey = " + requestAdapterKey);
				Log.i(TAG, "onHandleIntent: mRestAdapters = " + mRestAdapters.size());
			}

			// Make the request with the rest adapter
			networkRequest.makeNetworkRequest(restAdapter);
		}

		// Send queued analytics since the network radio is already being used
		AnalyticsUtils.sendQueuedAnalytics();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroy");
		}
	}

	/**
	 * This is a hack to trust all security certs, which should only be used for testing with self signed
	 * certs in dev environments
	 */
	private static void trustAllCerts() {
		if (BuildConfig.DEBUG) {
			Log.w(TAG, "trustAllCerts: TRUSTING ANY CERT, SHOULD ONLY BE USED IN TESTING!");
		}

		X509TrustManager easyTrustManager = new X509TrustManager() {

			@Override
			public void checkClientTrusted(
					X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(
					X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

		};

		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] {easyTrustManager};

		// Install the all-trusting trust manager
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		}
		catch (Exception e) {
			Log.e(TAG, "trustAllCerts: exception trusting all certs", e);

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	private static void trustAllHosts(OkHttpClient okHttpClient) {
		// Set a host name verifier on the client that does not validate the host name
		HostnameVerifier easyHostnameVerifier = new HostnameVerifier() {
			@Override
			public boolean verify(String s, SSLSession sslSession) {
				return true;
			}
		};
		okHttpClient.setHostnameVerifier(easyHostnameVerifier);
	}

}