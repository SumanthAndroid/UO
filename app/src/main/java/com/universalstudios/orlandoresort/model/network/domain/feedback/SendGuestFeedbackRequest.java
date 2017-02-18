package com.universalstudios.orlandoresort.model.network.domain.feedback;

import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.network.service.ServiceEndpointUtils;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 
 * 
 * @author Steven Byle
 */
public class SendGuestFeedbackRequest extends UniversalOrlandoServicesRequest implements Callback<SendGuestFeedbackResponse> {
	private static final String TAG = SendGuestFeedbackRequest.class.getSimpleName();

	private SendGuestFeedbackRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, SendGuestFeedbackParams sendGuestFeedbackParams) {
		super(senderTag, priority, concurrencyType, sendGuestFeedbackParams);
	}

	private static class SendGuestFeedbackParams extends NetworkParams {
		private final SendGuestFeedbackBodyParams bodyParams;

		public SendGuestFeedbackParams() {
			super();
			bodyParams = new SendGuestFeedbackBodyParams();
		}
	}

	public static class SendGuestFeedbackBodyParams extends GsonObject {

		@SerializedName("Subject")
		private String subject;

		@SerializedName("Rating")
		private String rating;

		@SerializedName("Message")
		private String message;

		@SerializedName("Name")
		private String name;

		@SerializedName("Email")
		private String email;

		@SerializedName("Phone")
		private String phone;

	}

	@SuppressWarnings("javadoc")
	public static class Builder extends BaseNetworkRequestBuilder<Builder> {
		private final SendGuestFeedbackParams params;

		public Builder(NetworkRequestSender sender) {
			super(sender);
			params = new SendGuestFeedbackParams();
		}

		@Override
		protected Builder getThis() {
			return this;
		}

		public Builder setSubject(String subject) {
			params.bodyParams.subject = subject;
			return getThis();
		}

		public Builder setRating(String rating) {
			params.bodyParams.rating = rating;
			return getThis();
		}

		public Builder setMessage(String message) {
			params.bodyParams.message = message;
			return getThis();
		}

		public Builder setName(String name) {
			params.bodyParams.name = name;
			return getThis();
		}

		public Builder setEmail(String email) {
			params.bodyParams.email = email;
			return getThis();
		}

		public Builder setPhone(String phone) {
			params.bodyParams.phone = phone;
			return getThis();
		}

		public SendGuestFeedbackRequest build() {
			return new SendGuestFeedbackRequest(senderTag, priority, concurrencyType, params);
		}
	}

	@Override
	public void makeNetworkRequest(RestAdapter restAdapter) {
		super.makeNetworkRequest(restAdapter);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "makeNetworkRequest");
		}

		SendGuestFeedbackParams params = (SendGuestFeedbackParams) getNetworkParams();
		UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

		switch (getConcurrencyType()) {
			case SYNCHRONOUS:
				try {
					SendGuestFeedbackResponse response = services.sendGuestFeedback(
							ServiceEndpointUtils.getCity(), params.bodyParams);
					success(response, null);
				}
				catch (RetrofitError retrofitError) {
					failure(retrofitError);
				}
				break;
			case ASYNCHRONOUS:
				services.sendGuestFeedback(ServiceEndpointUtils.getCity(), params.bodyParams, this);
				break;
			default:
				throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
		}

	}

	@Override
	public void success(SendGuestFeedbackResponse sendGuestFeedbackResponse, Response response) {
		if (sendGuestFeedbackResponse == null) {
			sendGuestFeedbackResponse = new SendGuestFeedbackResponse();
		}
		super.handleSuccess(sendGuestFeedbackResponse, response);
	}

	@Override
	public void failure(RetrofitError retrofitError) {
		SendGuestFeedbackResponse sendGuestFeedbackResponse;

		// Read the response from the body if possible
		Object responseBody = retrofitError.getBody();
		if (responseBody != null && responseBody instanceof SendGuestFeedbackResponse) {
			sendGuestFeedbackResponse = (SendGuestFeedbackResponse) responseBody;
		} else {
			sendGuestFeedbackResponse = new SendGuestFeedbackResponse();
		}

		// Inform any listeners after handling the error
		super.handleFailure(sendGuestFeedbackResponse, retrofitError);
	}

}