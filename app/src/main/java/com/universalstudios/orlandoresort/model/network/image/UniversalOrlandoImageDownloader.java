/**
 * 
 */
package com.universalstudios.orlandoresort.model.network.image;

import android.net.Uri;
import android.util.Log;

import com.squareup.picasso.OkHttpDownloader;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.request.RequestHeaders;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * 
 * 
 * @author Steven Byle
 */
public class UniversalOrlandoImageDownloader extends OkHttpDownloader {
	private static final String TAG = UniversalOrlandoImageDownloader.class.getSimpleName();

	public static final boolean SHOW_DEBUG = false;
	private static final int CONNECT_TIMEOUT_MS = 15 * 1000;
	private static final int READ_TIMEOUT_MS = 20 * 1000;

	public UniversalOrlandoImageDownloader(String cacheDirName, long minSizeBytes, long maxSizeBytes) {
		super(CacheUtils.getOrCreateCacheDir(cacheDirName),
				CacheUtils.calculateDiskCacheSize(CacheUtils.getOrCreateCacheDir(cacheDirName), minSizeBytes, maxSizeBytes));
	}

	@Override
	protected HttpURLConnection openConnection(Uri uri) throws IOException {
		HttpURLConnection connection = super.openConnection(uri);

		// Set the timeouts on the request
		connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
		connection.setReadTimeout(READ_TIMEOUT_MS);

		// Set the headers on the request
		connection.setRequestProperty(RequestHeaders.Keys.X_UNIWEBSERVICE_API_KEY, RequestHeaders.Values.X_UNIWEBSERVICE_API_KEY_ANDROID);
		UniversalAppState universalAppState = UniversalAppStateManager.getInstance();
		String token = universalAppState.getToken();
		if (token != null) {
			connection.setRequestProperty(RequestHeaders.Keys.X_UNIWEBSERVICE_TOKEN, token);
		}

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "openConnection: uri = " + uri.toString()
					+ "\n" + "connection.getRequestProperties() = " + connection.getRequestProperties());
		}

		return connection;
	}

	public void destroy() {

	}
}
