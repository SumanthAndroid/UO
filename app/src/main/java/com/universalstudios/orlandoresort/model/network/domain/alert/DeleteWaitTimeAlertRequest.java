package com.universalstudios.orlandoresort.model.network.domain.alert;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
import com.universalstudios.orlandoresort.model.network.response.HttpResponseStatus;
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
public class DeleteWaitTimeAlertRequest extends UniversalOrlandoServicesRequest implements Callback<DeleteWaitTimeAlertResponse> {
	private static final String TAG = DeleteWaitTimeAlertRequest.class.getSimpleName();

	private DeleteWaitTimeAlertRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, DeleteWaitTimeAlertParams deleteWaitTimeAlertParams) {
		super(senderTag, priority, concurrencyType, deleteWaitTimeAlertParams);
	}

	private static class DeleteWaitTimeAlertParams extends NetworkParams {
		private Long rideId;
		private String alertIdString;

		public DeleteWaitTimeAlertParams() {
			super();
		}
	}

	@SuppressWarnings("javadoc")
	public static class Builder extends BaseNetworkRequestBuilder<Builder> {
		private final DeleteWaitTimeAlertParams deleteWaitTimeAlertParams;

		public Builder(NetworkRequestSender sender) {
			super(sender);
			deleteWaitTimeAlertParams = new DeleteWaitTimeAlertParams();
		}

		@Override
		protected Builder getThis() {
			return this;
		}

		public Builder setWaitTimeAlertToDelete(WaitTimeAlert waitTimeAlertToDelete) {
			deleteWaitTimeAlertParams.rideId = waitTimeAlertToDelete.getPoiId();
			deleteWaitTimeAlertParams.alertIdString = waitTimeAlertToDelete.getIdString();
			return getThis();
		}

		public DeleteWaitTimeAlertRequest build() {
			return new DeleteWaitTimeAlertRequest(senderTag, priority, concurrencyType, deleteWaitTimeAlertParams);
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

		DeleteWaitTimeAlertParams params = (DeleteWaitTimeAlertParams) getNetworkParams();
		UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

		switch (getConcurrencyType()) {
			case SYNCHRONOUS:
				try {
					DeleteWaitTimeAlertResponse response = services.deleteWaitTimeAlert(
							ServiceEndpointUtils.getCity(),
							deviceId,
							params.rideId);
					success(response, null);
				}
				catch (RetrofitError retrofitError) {
					failure(retrofitError);
				}
				break;
			case ASYNCHRONOUS:
				services.deleteWaitTimeAlert(
						ServiceEndpointUtils.getCity(),
						deviceId,
						params.rideId,
						this);
				break;
			default:
				throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
		}

	}

	@Override
	public void success(DeleteWaitTimeAlertResponse deleteWaitTimeAlertResponse, Response response) {
		if (deleteWaitTimeAlertResponse == null) {
			deleteWaitTimeAlertResponse = new DeleteWaitTimeAlertResponse();
		}

		// Delete the wait time alert from the database since it was successfully deleted on the server
		DeleteWaitTimeAlertParams params = (DeleteWaitTimeAlertParams) getNetworkParams();
		AlertsUtils.deleteAlertFromDatabase(params.alertIdString, false);

		// Inform any listeners after saving the response
		super.handleSuccess(deleteWaitTimeAlertResponse, response);
	}

	@Override
	public void failure(RetrofitError retrofitError) {
		boolean toastErrorMessage = true;

		// Check the error type
		Response response = retrofitError.getResponse();
		if (response != null) {
			int httpStatusCode = response.getStatus();
			if (httpStatusCode == HttpResponseStatus.ERROR_NOT_FOUND) {
				// Delete the wait time alert from the database since the server did not find it
				DeleteWaitTimeAlertParams params = (DeleteWaitTimeAlertParams) getNetworkParams();
				AlertsUtils.deleteAlertFromDatabase(params.alertIdString, false);
				toastErrorMessage = false;
			}
		}

		// Only toast the error message if it wasn't silently handled
		if (toastErrorMessage) {
			Context appContext = UniversalOrlandoApplication.getAppContext();
			UserInterfaceUtils.showToastFromBackground(
					appContext.getString(R.string.wait_time_alerts_toast_failed_to_delete),
					Toast.LENGTH_LONG, appContext);
		}

		// Inform any listeners after handling the error
		super.handleFailure(new DeleteWaitTimeAlertResponse(), retrofitError);
	}

}