package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request;

import android.content.Context;
import android.util.Log;

import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetTravelPartyResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;

import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.BuildConfig;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Retrofit request to get travel party.
 * <p/>
 */
public class GetTravelPartyRequest extends IBMOrlandoServicesRequest implements Callback<GetTravelPartyResponse> {

	// Logging Tag
	private static final String TAG = GetTravelPartyRequest.class.getSimpleName();

	protected GetTravelPartyRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
		super(senderTag, priority, concurrencyType, networkParams);
	}

	/**
	 * Private internal Network Parameter class used by the {@link Builder}.
	 */
	private static class GetTravelPartyParams extends NetworkParams {
		private String userName;
		private String userPassword;

		public GetTravelPartyParams() {
			super();
		}
	}

	/**
	 * A Builder class for building a new {@link GetTravelPartyRequest}.
	 */
	public static class Builder extends BaseNetworkRequestBuilder<Builder> {
		private final GetTravelPartyParams getTravelPartyParams;

		public Builder(NetworkRequestSender sender) {
			super(sender);
			this.getTravelPartyParams = new GetTravelPartyParams();
		}

		/**
		 * Method required by {@link BaseNetworkRequestBuilder} to allow the proper builder pattern
		 * with child classes.
		 *
		 * @return the {@link Builder}
		 */
		@Override
		protected Builder getThis() {
			return this;
		}

		/**
		 * [REQUIRED] Sets the guest's external Id for getting their party.
		 *
		 * @param userName the String of the guest's email/password
		 * @return the {@link Builder}
		 */
		public Builder setUserName(String userName) {
			this.getTravelPartyParams.userName = userName;
			return getThis();
		}

		/**
		 * [REQUIRED] Sets the guest's external Id for getting their party.
		 *
		 * @param password the String of the guest's password
		 * @return the {@link Builder}
		 */
		public Builder setUserPassword(String password) {
			this.getTravelPartyParams.userPassword = password;
			return getThis();
		}

		public GetTravelPartyRequest build() {
			return new GetTravelPartyRequest(senderTag, priority, concurrencyType, getTravelPartyParams);
		}

	}

	@Override
	public void makeNetworkRequest(RestAdapter restAdapter) {
		super.makeNetworkRequest(restAdapter);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "makeNetworkRequest");
		}

		// Set the params when the request is sent
		GetTravelPartyParams params = (GetTravelPartyParams) getNetworkParams();

		IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);
		String basicAuth = NetworkUtils.createBasicAuthString(params.userName, params.userPassword);

		switch (getConcurrencyType()) {
			case SYNCHRONOUS:
				try {
					GetTravelPartyResponse response = services.getTravelPartyMembers(
							basicAuth);
					success(response, null);
				} catch (RetrofitError retrofitError) {
					failure(retrofitError);
				}
				break;
			case ASYNCHRONOUS:
				services.getTravelPartyMembers(
						basicAuth,
						this);
				break;
			default:
				throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
		}

	}

	@Override
	public void success(GetTravelPartyResponse getTravelPartyResponse, Response response) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "success");
		}
		super.handleSuccess(getTravelPartyResponse, response);
	}

	@Override
	public void failure(RetrofitError error) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "failure");
		}
		super.handleFailure(new GetTravelPartyResponse(), error);
	}
}