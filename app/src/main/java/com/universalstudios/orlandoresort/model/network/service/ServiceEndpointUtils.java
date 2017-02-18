package com.universalstudios.orlandoresort.model.network.service;

import android.net.Uri;

import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

/**
 * Created by kbojarski on 8/8/16.
 */

public class ServiceEndpointUtils {

	private static final String CITY_KEY = "city";
	private static final String CITY_ORLANDO = "orlando";
	private static final String CITY_HOLLYWOOD = "hollywood";

	public static final String URL_PATH_MAP_TILES = "api/MapTiles";
	public static final String URL_PATH_PRIVACY_POLICY = "MobileApp/Privacy";
	public static final String URL_PATH_PRIVACY_POLICY_NBC = "MobileApp/PrivacyNBC";
	public static final String URL_PATH_PRIVACY_FAQ_ORLANDO = "MobileApp/PrivacyFAQ/";
	public static final String URL_PATH_PRIVACY_FAQ_HOLLYWOOD = "MobileApp/PrivacyUSHFAQ";
	public static final String URL_PATH_TERMS_OF_SERVICE = "MobileApp/TermsOfService";
	public static final String URL_PATH_COPYRIGHT = "MobileApp/Copyright";
	public static final String URL_PATH_NEWS = "MobileApp/News";

	public static final String URL_PRIVACY_INFORMATION_CENTER = "https://www.universalorlando.com/uo-privacy-center.aspx";


	public static String getCity() {
		if (BuildConfigUtils.isLocationFlavorHollywood()) {
			return CITY_HOLLYWOOD;
		} else {
			return CITY_ORLANDO;
		}
	}

	public static String getPrivacyFaqEndPoint() {
		if(BuildConfigUtils.isLocationFlavorHollywood()) {
			return URL_PATH_PRIVACY_FAQ_HOLLYWOOD;
		} else {
			return URL_PATH_PRIVACY_FAQ_ORLANDO;
		}
	}

	public static Uri buildUri(String urlPath) {
		UniversalAppState universalAppState = UniversalAppStateManager
				.getInstance();

		return Uri.parse(universalAppState.getServicesBaseUrl()).
				buildUpon()
				.appendEncodedPath(urlPath)
				.appendQueryParameter(CITY_KEY, getCity())
				.build();

	}
}
