package com.universalstudios.orlandoresort.model.network.domain.alert;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.alerts.AlertsUtils;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.alerts.RideOpenAlert;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.network.service.ServiceEndpointUtils;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 
 * 
 * @author Steven Byle
 */
public class SetRideOpenAlertRequest extends UniversalOrlandoServicesRequest implements Callback<SetRideOpenAlertResponse> {
	private static final String TAG = SetRideOpenAlertRequest.class.getSimpleName();

	private SetRideOpenAlertRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, SetRideOpenAlertParams setRideOpenAlertParams) {
		super(senderTag, priority, concurrencyType, setRideOpenAlertParams);
	}

	private static class SetRideOpenAlertParams extends NetworkParams {
		private final SetRideOpenAlertBodyParams bodyParams;
		private RideOpenAlert rideOpenAlert;

		public SetRideOpenAlertParams() {
			super();
			bodyParams = new SetRideOpenAlertBodyParams();
		}
	}

	public static class SetRideOpenAlertBodyParams extends GsonObject {

		@SerializedName("DeviceId")
		private String deviceId;

		@SerializedName("RideId")
		private long rideId;
	}

	@SuppressWarnings("javadoc")
	public static class Builder extends BaseNetworkRequestBuilder<Builder> {
		private final SetRideOpenAlertParams setRideOpenAlertParams;

		public Builder(NetworkRequestSender sender) {
			super(sender);
			setRideOpenAlertParams = new SetRideOpenAlertParams();
		}

		@Override
		protected Builder getThis() {
			return this;
		}

		public Builder setRideOpenAlert(RideOpenAlert rideOpenAlert) {
			setRideOpenAlertParams.rideOpenAlert = rideOpenAlert;
			setRideOpenAlertParams.bodyParams.rideId = rideOpenAlert.getPoiId();
			return getThis();
		}

		public SetRideOpenAlertRequest build() {
			return new SetRideOpenAlertRequest(senderTag, priority, concurrencyType, setRideOpenAlertParams);
		}
	}

	@Override
	public void makeNetworkRequest(RestAdapter restAdapter) {
		super.makeNetworkRequest(restAdapter);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "makeNetworkRequest");
		}

		// Inject the device ID from the state
		String deviceId = UniversalAppStateManager.getInstance().getGcmRegistrationId();

		SetRideOpenAlertParams params = (SetRideOpenAlertParams) getNetworkParams();
		params.bodyParams.deviceId = deviceId;

		UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

		switch (getConcurrencyType()) {
			case SYNCHRONOUS:
				try {
					SetRideOpenAlertResponse response = services.setRideOpenAlert(
							ServiceEndpointUtils.getCity(), params.bodyParams);
					success(response, null);
				}
				catch (RetrofitError retrofitError) {
					failure(retrofitError);
				}
				break;
			case ASYNCHRONOUS:
				services.setRideOpenAlert(ServiceEndpointUtils.getCity(), params.bodyParams, this);
				break;
			default:
				throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
		}

	}

	@Override
	public void success(SetRideOpenAlertResponse setRideOpenAlertResponse, Response response) {
		if (setRideOpenAlertResponse == null) {
			setRideOpenAlertResponse = new SetRideOpenAlertResponse();
		}

		// Save the alert to the database since it was successful
		SetRideOpenAlertParams params = (SetRideOpenAlertParams) getNetworkParams();
		AlertsUtils.saveRideOpenAlertToDatabase(params.rideOpenAlert, false);

		// Inform any listeners after saving the response
		super.handleSuccess(setRideOpenAlertResponse, response);
	}

	@Override
	public void failure(RetrofitError retrofitError) {

		Context appContext = UniversalOrlandoApplication.getAppContext();
		UserInterfaceUtils.showToastFromBackground(
				appContext.getString(R.string.ride_open_alerts_toast_failed_to_save),
				Toast.LENGTH_LONG, appContext);

		// Inform any listeners after handling the error
		super.handleFailure(new SetRideOpenAlertResponse(), retrofitError);
	}

}