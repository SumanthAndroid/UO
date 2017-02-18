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
import com.universalstudios.orlandoresort.model.alerts.WaitTimeAlert;
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
public class SetWaitTimeAlertRequest extends UniversalOrlandoServicesRequest implements Callback<SetWaitTimeAlertResponse> {
	private static final String TAG = SetWaitTimeAlertRequest.class.getSimpleName();

	private SetWaitTimeAlertRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, SetWaitTimeAlertParams setWaitTimeAlertParams) {
		super(senderTag, priority, concurrencyType, setWaitTimeAlertParams);
	}

	private static class SetWaitTimeAlertParams extends NetworkParams {
		private final SetWaitTimeAlertBodyParams bodyParams;
		private WaitTimeAlert waitTimeAlert;

		public SetWaitTimeAlertParams() {
			super();
			bodyParams = new SetWaitTimeAlertBodyParams();
		}
	}

	public static class SetWaitTimeAlertBodyParams extends GsonObject {

		@SerializedName("DeviceId")
		private String deviceId;

		@SerializedName("RideId")
		private long rideId;

		@SerializedName("Threshold")
		private int thresholdInMin;
	}

	@SuppressWarnings("javadoc")
	public static class Builder extends BaseNetworkRequestBuilder<Builder> {
		private final SetWaitTimeAlertParams setWaitTimeAlertParams;

		public Builder(NetworkRequestSender sender) {
			super(sender);
			setWaitTimeAlertParams = new SetWaitTimeAlertParams();
		}

		@Override
		protected Builder getThis() {
			return this;
		}

		public Builder setWaitTimeAlert(WaitTimeAlert waitTimeAlert) {
			setWaitTimeAlertParams.waitTimeAlert = waitTimeAlert;
			setWaitTimeAlertParams.bodyParams.rideId = waitTimeAlert.getPoiId();
			setWaitTimeAlertParams.bodyParams.thresholdInMin = waitTimeAlert.getNotifyThresholdInMin();
			return getThis();
		}

		public SetWaitTimeAlertRequest build() {
			return new SetWaitTimeAlertRequest(senderTag, priority, concurrencyType, setWaitTimeAlertParams);
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

		SetWaitTimeAlertParams params = (SetWaitTimeAlertParams) getNetworkParams();
		params.bodyParams.deviceId = deviceId;

		UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

		switch (getConcurrencyType()) {
			case SYNCHRONOUS:
				try {
					SetWaitTimeAlertResponse response = services.setWaitTimeAlert(
							ServiceEndpointUtils.getCity(), params.bodyParams);
					success(response, null);
				}
				catch (RetrofitError retrofitError) {
					failure(retrofitError);
				}
				break;
			case ASYNCHRONOUS:
				services.setWaitTimeAlert(ServiceEndpointUtils.getCity(), params.bodyParams, this);
				break;
			default:
				throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
		}

	}

	@Override
	public void success(SetWaitTimeAlertResponse setWaitTimeAlertResponse, Response response) {
		if (setWaitTimeAlertResponse == null) {
			setWaitTimeAlertResponse = new SetWaitTimeAlertResponse();
		}

		// Save the wait time to the database since it was successful
		SetWaitTimeAlertParams params = (SetWaitTimeAlertParams) getNetworkParams();
		AlertsUtils.saveWaitTimeAlertToDatabase(params.waitTimeAlert, false);

		// Inform any listeners after saving the response
		super.handleSuccess(setWaitTimeAlertResponse, response);
	}

	@Override
	public void failure(RetrofitError retrofitError) {

		Context appContext = UniversalOrlandoApplication.getAppContext();
		UserInterfaceUtils.showToastFromBackground(
				appContext.getString(R.string.wait_time_alerts_toast_failed_to_save),
				Toast.LENGTH_LONG, appContext);

		// Inform any listeners after handling the error
		super.handleFailure(new SetWaitTimeAlertResponse(), retrofitError);
	}

}