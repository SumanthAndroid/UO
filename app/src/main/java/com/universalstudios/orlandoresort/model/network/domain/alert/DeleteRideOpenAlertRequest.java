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
import com.universalstudios.orlandoresort.model.alerts.RideOpenAlert;
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
public class DeleteRideOpenAlertRequest extends UniversalOrlandoServicesRequest implements Callback<DeleteRideOpenAlertResponse> {
	private static final String TAG = DeleteRideOpenAlertRequest.class.getSimpleName();

	private DeleteRideOpenAlertRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, DeleteRideOpenAlertParams deleteRideOpenAlertParams) {
		super(senderTag, priority, concurrencyType, deleteRideOpenAlertParams);
	}

	private static class DeleteRideOpenAlertParams extends NetworkParams {
		private Long rideId;
		private String alertIdString;

		public DeleteRideOpenAlertParams() {
			super();
		}
	}

	@SuppressWarnings("javadoc")
	public static class Builder extends BaseNetworkRequestBuilder<Builder> {
		private final DeleteRideOpenAlertParams deleteRideOpenAlertParams;

		public Builder(NetworkRequestSender sender) {
			super(sender);
			deleteRideOpenAlertParams = new DeleteRideOpenAlertParams();
		}

		@Override
		protected Builder getThis() {
			return this;
		}

		public Builder setRideOpenAlertToDelete(RideOpenAlert rideOpenAlertToDelete) {
			deleteRideOpenAlertParams.rideId = rideOpenAlertToDelete.getPoiId();
			deleteRideOpenAlertParams.alertIdString = rideOpenAlertToDelete.getIdString();
			return getThis();
		}

		public DeleteRideOpenAlertRequest build() {
			return new DeleteRideOpenAlertRequest(senderTag, priority, concurrencyType, deleteRideOpenAlertParams);
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

		DeleteRideOpenAlertParams params = (DeleteRideOpenAlertParams) getNetworkParams();
		UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

		switch (getConcurrencyType()) {
			case SYNCHRONOUS:
				try {
					DeleteRideOpenAlertResponse response = services.deleteRideOpenAlert(
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
				services.deleteRideOpenAlert(
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
	public void success(DeleteRideOpenAlertResponse deleteRideOpenAlertResponse, Response response) {
		if (deleteRideOpenAlertResponse == null) {
			deleteRideOpenAlertResponse = new DeleteRideOpenAlertResponse();
		}

		// Delete the alert from the database since it was successfully deleted on the server
		DeleteRideOpenAlertParams params = (DeleteRideOpenAlertParams) getNetworkParams();
		AlertsUtils.deleteAlertFromDatabase(params.alertIdString, false);

		// Inform any listeners after saving the response
		super.handleSuccess(deleteRideOpenAlertResponse, response);
	}

	@Override
	public void failure(RetrofitError retrofitError) {
		boolean toastErrorMessage = true;

		// Check the error type
		Response response = retrofitError.getResponse();
		if (response != null) {
			int httpStatusCode = response.getStatus();
			if (httpStatusCode == HttpResponseStatus.ERROR_NOT_FOUND) {
				// Delete the alert from the database since the server did not find it
				DeleteRideOpenAlertParams params = (DeleteRideOpenAlertParams) getNetworkParams();
				AlertsUtils.deleteAlertFromDatabase(params.alertIdString, false);
				toastErrorMessage = false;
			}
		}

		// Only toast the error message if it wasn't silently handled
		if (toastErrorMessage) {
			Context appContext = UniversalOrlandoApplication.getAppContext();
			UserInterfaceUtils.showToastFromBackground(
					appContext.getString(R.string.ride_open_alerts_toast_failed_to_delete),
					Toast.LENGTH_LONG, appContext);
		}

		// Inform any listeners after handling the error
		super.handleFailure(new DeleteRideOpenAlertResponse(), retrofitError);

	}
}