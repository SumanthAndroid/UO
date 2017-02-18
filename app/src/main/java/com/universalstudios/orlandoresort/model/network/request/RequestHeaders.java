/**
 * 
 */
package com.universalstudios.orlandoresort.model.network.request;

import com.universalstudios.orlandoresort.BuildConfig;

/**
 * Request header constants needed for network calls.
 *
 * @author Steven Byle
 */
public class RequestHeaders {

	public static final String HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1 =
			Keys.X_UNIWEBSERVICE_SERVICE_VERSION + ": " + Values.X_UNIWEBSERVICE_SERVICE_VERSION_1;
	public static final String HEADER_CONTENT_TYPE_APPLICATION_JSON =
			Keys.CONTENT_TYPE + ": " + Values.CONTENT_TYPE_APPLICATION_JSON;

	public static class Keys {
		public static final String ACCEPT = "Accept";
		public static final String ACCEPT_LANGUAGE = "Accept-Language";
		public static final String CONTENT_TYPE = "Content-Type";
		public static final String DATE = "Date";
		public static final String X_UNIWEBSERVICE_API_KEY = "X-UNIWebService-ApiKey";
		public static final String X_UNIWEBSERVICE_TOKEN = "X-UNIWebService-Token";
		public static final String X_UNIWEBSERVICE_SERVICE_VERSION = "X-UNIWebService-ServiceVersion";
		public static final String X_UNIWEBSERVICE_APP_VERSION = "X-UNIWebService-AppVersion";
		public static final String X_UNIWEBSERVICE_PLATFORM = "X-UNIWebService-Platform";
		public static final String X_UNIWEBSERVICE_PLATFORM_VERSION = "X-UNIWebService-PlatformVersion";
		public static final String X_UNIWEBSERVICE_DEVICE = "X-UNIWebService-Device";
	}

	public static class Values {
		public static final String ACCEPT_APPLICATION_JSON = "application/json";
		public static final String ACCEPT_LANGUAGE_EN_US = "en-US";
		public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
		public static final String X_UNIWEBSERVICE_API_KEY_ANDROID = BuildConfig.PARK_SERVICES_API_KEY;
		public static final String X_UNIWEBSERVICE_PLATFORM_ANDROID = "Android";
		public static final String X_UNIWEBSERVICE_SERVICE_VERSION_1 = "1";
	}
}