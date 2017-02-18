package com.universalstudios.orlandoresort.model.network.domain.newsletter;

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


public class NewsletterRegistrationRequest extends UniversalOrlandoServicesRequest implements Callback<NewsletterRegistrationResponse> {
	private static final String TAG = NewsletterRegistrationRequest.class.getSimpleName();

	private NewsletterRegistrationRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NewsletterRegistrationParams NewsletterRegistrationParams) {
		super(senderTag, priority, concurrencyType, NewsletterRegistrationParams);
	}

	private static class NewsletterRegistrationParams extends NetworkParams {
		private final NewsletterRegistrationBodyParams bodyParams;

		public NewsletterRegistrationParams() {
			super();
			bodyParams = new NewsletterRegistrationBodyParams();
		}
	}

	public static class NewsletterRegistrationBodyParams extends GsonObject {

		@SerializedName("Email")
		private String email;

		@SerializedName("BirthDateMonth")
		private Integer birthMonth;

		@SerializedName("BirthDateYear")
		private Integer birthYear;

		@SerializedName("ZipCode")
		private String zipCode;

	}

	@SuppressWarnings("javadoc")
	public static class Builder extends BaseNetworkRequestBuilder<Builder> {
		private final NewsletterRegistrationParams params;

		public Builder(NetworkRequestSender sender) {
			super(sender);
			params = new NewsletterRegistrationParams();
		}

		@Override
		protected Builder getThis() {
			return this;
		}

		public Builder setEmail(String email) {
			params.bodyParams.email = email;
			return getThis();
		}

		public Builder setBirthMonth(Integer month) {
			params.bodyParams.birthMonth = month;
			return getThis();
		}

		public Builder setBirthYear(Integer year) {
			params.bodyParams.birthYear = year;
			return getThis();
		}

		public Builder setZipcode(String zipCode) {
			params.bodyParams.zipCode = zipCode;
			return getThis();
		}

		public NewsletterRegistrationRequest build() {
			return new NewsletterRegistrationRequest(senderTag, priority, concurrencyType, params);
		}
	}

	@Override
	public void makeNetworkRequest(RestAdapter restAdapter) {
		super.makeNetworkRequest(restAdapter);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "makeNetworkRequest");
		}

		NewsletterRegistrationParams params = (NewsletterRegistrationParams) getNetworkParams();
		UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

		switch (getConcurrencyType()) {
			case SYNCHRONOUS:
				try {
					NewsletterRegistrationResponse response = services.sendNewsletterRegistration(
							ServiceEndpointUtils.getCity(), params.bodyParams);
					success(response, null);
				} catch (RetrofitError retrofitError) {
					failure(retrofitError);
				}
				break;
			case ASYNCHRONOUS:
				services.sendNewsletterRegistration(ServiceEndpointUtils.getCity(), params.bodyParams, this);
				break;
			default:
				throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
		}

	}

	@Override
	public void success(NewsletterRegistrationResponse NewsletterRegistrationResponse, Response response) {
		if (NewsletterRegistrationResponse == null) {
			NewsletterRegistrationResponse = new NewsletterRegistrationResponse();
		}
		super.handleSuccess(NewsletterRegistrationResponse, response);
	}

	@Override
	public void failure(RetrofitError retrofitError) {
		NewsletterRegistrationResponse NewsletterRegistrationResponse;

		// Read the response from the body if possible
		Object responseBody = retrofitError.getBody();
		if (responseBody != null && responseBody instanceof NewsletterRegistrationResponse) {
			NewsletterRegistrationResponse = (NewsletterRegistrationResponse) responseBody;
		} else {
			NewsletterRegistrationResponse = new NewsletterRegistrationResponse();
		}

		// Inform any listeners after handling the error
		super.handleFailure(NewsletterRegistrationResponse, retrofitError);
	}

}