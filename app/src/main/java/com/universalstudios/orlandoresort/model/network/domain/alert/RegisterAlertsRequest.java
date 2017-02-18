package com.universalstudios.orlandoresort.model.network.domain.alert;

import android.content.pm.PackageInfo;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.network.service.ServiceEndpointUtils;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 
 * 
 * @author Steven Byle
 */
public class RegisterAlertsRequest extends UniversalOrlandoServicesRequest implements Callback<RegisterAlertsResponse> {
	private static final String TAG = RegisterAlertsRequest.class.getSimpleName();

	private RegisterAlertsRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, RegisterAlertsParams registerAlertsParams) {
		super(senderTag, priority, concurrencyType, registerAlertsParams);
	}

	private static class RegisterAlertsParams extends NetworkParams {
		private final RegisterAlertsBodyParams bodyParams;

		public RegisterAlertsParams() {
			super();
			bodyParams = new RegisterAlertsBodyParams();
		}
	}

	public static class RegisterAlertsBodyParams extends GsonObject {

		@SerializedName("PreviousDeviceId")
		private String previousDeviceId;

		@SerializedName("DeviceId")
		private String deviceId;

		@SerializedName("InPark")
		private boolean inPark;

		@SerializedName("WaitTimeNotifications")
		private boolean waitTimeNotifications;

		@SerializedName("InformationalNotifications")
		private boolean informationalNotifications;

		@SerializedName("ParkNewsNotifications")
		private boolean parkNewsNotifications;

		@SerializedName("EmergencyNotifications")
		private boolean emergencyNotifications;

		@SerializedName("RideOpenNotifications")
		private boolean rideOpenNotifications;

		@SerializedName("RideClosedNotifications")
		private boolean rideClosedNotifications;
	}

	@SuppressWarnings("javadoc")
	public static class Builder extends BaseNetworkRequestBuilder<Builder> {
		private final RegisterAlertsParams registerAlertsParams;

		public Builder(NetworkRequestSender sender) {
			super(sender);
			registerAlertsParams = new RegisterAlertsParams();
		}

		@Override
		protected Builder getThis() {
			return this;
		}

		public Builder setPreviousDeviceId(String previousDeviceId) {
			registerAlertsParams.bodyParams.previousDeviceId = previousDeviceId;
			return getThis();
		}

		public Builder setDeviceId(String deviceId) {
			registerAlertsParams.bodyParams.deviceId = deviceId;
			return getThis();
		}

		public Builder setInPark(boolean inPark) {
			registerAlertsParams.bodyParams.inPark = inPark;
			return getThis();
		}

		public RegisterAlertsRequest build() {
			return new RegisterAlertsRequest(senderTag, priority, concurrencyType, registerAlertsParams);
		}
	}

	@Override
	public void makeNetworkRequest(RestAdapter restAdapter) {
		super.makeNetworkRequest(restAdapter);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "makeNetworkRequest");
		}

		UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

		// Hard code the push notifications subscriptions
		RegisterAlertsParams params = (RegisterAlertsParams) getNetworkParams();
		params.bodyParams.waitTimeNotifications = true;
		params.bodyParams.rideOpenNotifications = true;
		params.bodyParams.rideClosedNotifications = true;
		params.bodyParams.parkNewsNotifications = true;
		params.bodyParams.informationalNotifications = false;
		params.bodyParams.emergencyNotifications = false;

		switch (getConcurrencyType()) {
			case SYNCHRONOUS:
				try {
					RegisterAlertsResponse response = services.registerAlerts(
							ServiceEndpointUtils.getCity(), params.bodyParams);
					success(response, null);
				}
				catch (RetrofitError retrofitError) {
					failure(retrofitError);
				}
				break;
			case ASYNCHRONOUS:
				services.registerAlerts(ServiceEndpointUtils.getCity(), params.bodyParams, this);
				break;
			default:
				throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
		}
	}

	@Override
	public void success(RegisterAlertsResponse registerAlertsResponse, Response response) {
		if (registerAlertsResponse == null) {
			registerAlertsResponse = new RegisterAlertsResponse();
		}

		// Copy over the GCM state
		RegisterAlertsParams params = (RegisterAlertsParams) getNetworkParams();
		String gcmRegistrationId = params.bodyParams.deviceId;

		String versionName = null;
		PackageInfo pInfo = UniversalOrlandoApplication.getPackageInfo();
		if (pInfo != null) {
			versionName = pInfo.versionName;
		}

		// Update the state object
		UniversalAppState uoState = UniversalAppStateManager.getInstance();
		uoState.setGcmRegistrationId(gcmRegistrationId);
		uoState.setGcmRegistrationIdVersionName(versionName);
		uoState.setDateOfLastPushRegSyncInMillis(new Date().getTime());

		// Save the updated state
		UniversalAppStateManager.saveInstance();

		// Inform any listeners after saving the response
		super.handleSuccess(registerAlertsResponse, response);
	}

	@Override
	public void failure(RetrofitError retrofitError) {
		super.handleFailure(new RegisterAlertsResponse(), retrofitError);
	}

}