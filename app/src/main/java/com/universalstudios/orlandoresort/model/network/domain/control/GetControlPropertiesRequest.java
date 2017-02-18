package com.universalstudios.orlandoresort.model.network.domain.control;

import android.util.Base64;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.request.RequestHeaders;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoControlServices;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Steven Byle
 */
public class GetControlPropertiesRequest extends NetworkRequest implements Callback<GetControlPropertiesResponse> {
	private static final String TAG = GetControlPropertiesRequest.class.getSimpleName();

	private GetControlPropertiesRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, GetControlPropertiesParams getControlPropertiesParams) {
		super(senderTag, priority, concurrencyType, getControlPropertiesParams);
	}

	private static class GetControlPropertiesParams extends NetworkParams {
		private final GetControlPropertiesBodyParams bodyParams;

		public GetControlPropertiesParams() {
			super();
			bodyParams = new GetControlPropertiesBodyParams();
		}
	}

	public static class GetControlPropertiesBodyParams extends GsonObject {

		@SerializedName("apikey")
		private String apiKey;

		@SerializedName("signature")
		private String signature;

	}

	public static class Builder extends BaseNetworkRequestBuilder<Builder> {
		private final GetControlPropertiesParams getControlPropertiesParams;

		public Builder(NetworkRequestSender sender) {
			super(sender);
			getControlPropertiesParams = new GetControlPropertiesParams();
		}

		@Override
		protected Builder getThis() {
			return this;
		}

		public GetControlPropertiesRequest build() {
			// Set special defaults if they are not explicitly set
			priority = (priority != null) ? priority : Priority.VERY_HIGH;
			concurrencyType = (concurrencyType != null) ? concurrencyType : ConcurrencyType.SYNCHRONOUS;
			return new GetControlPropertiesRequest(senderTag, priority, concurrencyType, getControlPropertiesParams);
		}
	}

	@Override
	public String getServicesBaseUrl() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getServicesBaseUrl");
		}

		// Use the currently set services environment
		return BuildConfig.PARK_SERVICES_BASE_URL;
	}

	@Override
	public void makeNetworkRequest(RestAdapter restAdapter) {
		super.makeNetworkRequest(restAdapter);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "makeNetworkRequest");
		}

		String formattedDateOfRequest = getStandardFormattedDate(new Date());
		String secret = getApiSecret();
		String signature = createSignature(RequestHeaders.Values.X_UNIWEBSERVICE_API_KEY_ANDROID, secret, formattedDateOfRequest);

		// Set the params when the request is sent
		GetControlPropertiesParams params = (GetControlPropertiesParams) getNetworkParams();
		params.bodyParams.apiKey = RequestHeaders.Values.X_UNIWEBSERVICE_API_KEY_ANDROID;
		params.bodyParams.signature = signature;

		UniversalOrlandoControlServices services = restAdapter.create(UniversalOrlandoControlServices.class);

		switch (getConcurrencyType()) {
			case SYNCHRONOUS:
				try {
					GetControlPropertiesResponse response = services.getControlProperties(
							formattedDateOfRequest,
							params.bodyParams);
					success(response, null);
				}
				catch (RetrofitError retrofitError) {
					failure(retrofitError);
				}
				break;
			case ASYNCHRONOUS:
				services.getControlProperties(
						formattedDateOfRequest,
						params.bodyParams,
						this);
				break;
			default:
				throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
		}

	}

	@Override
	public void success(GetControlPropertiesResponse getControlPropertiesResponse, Response response) {
		if (getControlPropertiesResponse != null) {
			// Copy the response into the latest state instance
			UniversalAppState appState = UniversalAppStateManager.getInstance();

			GetControlPropertiesResponse resp = getControlPropertiesResponse;

			// These are shared values between both Orlando and Hollywood
			appState.setToken(resp.getToken());
			appState.setTokenExpirationString(resp.getTokenExpirationString());
			appState.setTokenExpirationUnix(resp.getTokenExpirationUnix());
			appState.setServicesBaseUrl(resp.getServicesBaseUrl());

			// Handle park specific config values
			Long newMapTileSetId;

			// Hollywood config
			if (BuildConfigUtils.isLocationFlavorHollywood()) {
				newMapTileSetId = resp.getUshActiveMapTileSetId();
				appState.setActiveMapTileSetBaseColor(resp.getUshActiveMapTileSetBaseColor());
				appState.setParkGeoFence(resp.getUshGeoFence());
				appState.setWaitTimeUpdateIntervalInSec(resp.getUshWaitTimeUpdateIntervalInSec());
				appState.setExpressPassUrl(resp.getUshExpressPassUrl());
				appState.setFeaturedItems(resp.getUshFeaturedList());
				appState.setGuestServicesEmailAddress(resp.getUshGuestServicesEmailAddress());
				appState.setGuestServicesPhoneNumber(resp.getUshGuestServicesPhoneNumber());
				appState.setInParkWifiNetworkName(resp.getUshInParkWifiNetworkName());
				appState.setRiderGuidePdfUrl(resp.getUshRiderGuidePdfUrl());
				appState.setTicketWebStoreUrl(resp.getUshTicketWebstoreUrl());
				appState.setGuestServiceDescription(resp.getUshGuestServicesDescription());
				appState.setGuestServicesDefaultShareText(resp.getUshGuestServicesDefaultShareText());
				appState.setGuestServicesDisplayEmail(resp.getUshGuestServicesDisplayEmail());
				appState.setGuestServicesTwitterUrl(resp.getUshGuestServicesTwitterUrl());
				appState.setFaqPageUrl(resp.getUshFaqPageUrl());
				appState.setFeaturedOfferItems(resp.getUshFeaturedOffersList());
			}
			// Orlando config
			else {
				newMapTileSetId = resp.getOrlandoActiveMapTileSetId();
				appState.setActiveMapTileSetBaseColor(resp.getOrlandoActiveMapTileSetBaseColor());
				appState.setParkGeoFence(resp.getOrlandoGeoFence());
				appState.setWaitTimeUpdateIntervalInSec(resp.getWaitTimeUpdateIntervalInSec());
				appState.setExpressPassUrl(resp.getOrlandoExpressPassUrl());
				appState.setOrlandoFeaturedPointsOfInterest(resp.getOrlandoFeaturedPointsOfInterest());
				appState.setFeaturedItems(resp.getOrlandoFeaturedList());
				appState.setExpressPassNotificationIntervalInMin(resp.getExPassNotificationIntervalInMin());
				appState.setGuestServicesEmailAddress(resp.getGuestServicesEmailAddress());
				appState.setGuestServicesPhoneNumber(resp.getGuestServicesPhoneNumber());
				appState.setInParkWifiNetworkName(resp.getInParkWifiNetworkName());
				appState.setRiderGuidePdfUrl(resp.getRiderGuidePdfUrl());
				appState.setTicketWebStoreUrl(resp.getTicketWebstoreUrl());
				appState.setGuestServiceDescription(resp.getGuestServicesDescription());
				appState.setGuestServicesDefaultShareText(resp.getGuestServicesDefaultShareText());
				appState.setGuestServicesDisplayEmail(resp.getGuestServicesDisplayEmail());
				appState.setGuestServicesTwitterUrl(resp.getGuestServicesTwitterUrl());
				appState.setFeaturedOfferItems(null); // UO does not have featured offers yet
				appState.setDefaultEventsToEventSeries(resp.isDefaultEventsToEventSeries());
			}

			// If the map tile set changes, wipe the map tile disk cache before updating the tile set ID
			Long oldMapTileSetId = appState.getActiveMapTileSetId();
			if (oldMapTileSetId != null && newMapTileSetId != null && oldMapTileSetId.longValue() != newMapTileSetId.longValue()) {
				if (BuildConfig.DEBUG) {
					Log.i(TAG, "success: new map tile set, wiping map tile disk cache");
				}
				CacheUtils.deleteCacheDir(CacheUtils.MAP_TILE_DISK_CACHE_NAME);
			}
			appState.setActiveMapTileSetId(newMapTileSetId);


			// Store the last time control properties were synced
			appState.setDateOfLastControlPropSyncInMillis(new Date().getTime());

			// Save the updated state
			UniversalAppStateManager.saveInstance();
		}
		else {
			getControlPropertiesResponse = new GetControlPropertiesResponse();
		}

		// Inform any listeners after saving the response
		super.handleSuccess(getControlPropertiesResponse, response);
	}

	@Override
	public void failure(RetrofitError retrofitError) {
		super.handleFailure(new GetControlPropertiesResponse(), retrofitError);
	}

	private String getApiSecret() {
		return BuildConfigUtils.deobfuscateString(BuildConfig.PARK_SERVICES_API_SECRET_ENCODED);
	}

	private static String getStandardFormattedDate(Date dateOfRequest) {
		// Date format: Thu, 09 Jan 2014 19:00:52 GMT
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
		sdf.setTimeZone(DateTimeUtils.getGmtTimeZone());
		String formattedDate = sdf.format(dateOfRequest) + " GMT";

		return formattedDate;
	}

	private static synchronized String createSignature(String apiKey, String apiSecret, String formattedDateOfRequest) {
		String signature = null;

		try {
			String requestKey = apiKey + "\n" + formattedDateOfRequest + "\n";

			if (BuildConfig.DEBUG) {
				Log.d(TAG, "generateRequestKey: requestKey = " + requestKey);
			}

			byte key[] = Base64.decode(apiSecret, Base64.DEFAULT);
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(key, "HmacSHA256"));
			byte[] hash = mac.doFinal(requestKey.getBytes("UTF-8"));
			signature = new String(Base64.encode(hash, Base64.NO_WRAP), "UTF-8");

			if (BuildConfig.DEBUG) {
				Log.d(TAG, "createSignature: signature = " + signature);
			}

		}
		catch (NoSuchAlgorithmException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createSignature: exception trying to get encoding algorithm");
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
		catch (InvalidKeyException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createSignature: exception trying to create signature");
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
		catch (IllegalStateException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createSignature: exception trying to create signature");
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
		catch (UnsupportedEncodingException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "createSignature: exception trying to create signature");
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}

		return signature;
	}

}
