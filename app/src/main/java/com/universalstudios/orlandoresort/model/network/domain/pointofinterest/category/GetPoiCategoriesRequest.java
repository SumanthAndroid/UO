package com.universalstudios.orlandoresort.model.network.domain.pointofinterest.category;

import android.content.Context;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.service.ServiceEndpointUtils;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * @author Steven Byle
 */
public class GetPoiCategoriesRequest extends UniversalOrlandoServicesRequest implements Callback<GetPoiCategoriesResponse> {
	private static final String TAG = GetPoiCategoriesRequest.class.getSimpleName();

	private GetPoiCategoriesRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType) {
		super(senderTag, priority, concurrencyType, null);
	}

	public static class Builder extends BaseNetworkRequestBuilder<Builder> {

		public Builder(NetworkRequestSender sender) {
			super(sender);
		}

		@Override
		protected Builder getThis() {
			return this;
		}

		public GetPoiCategoriesRequest build() {
			return new GetPoiCategoriesRequest(senderTag, priority, concurrencyType);
		}
	}

	@Override
	public void makeNetworkRequest(RestAdapter restAdapter) {
		super.makeNetworkRequest(restAdapter);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "makeNetworkRequest");
		}

		UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

		switch (getConcurrencyType()) {
			case SYNCHRONOUS:
				try {
					GetPoiCategoriesResponse response = services.getPoiCategories(ServiceEndpointUtils.getCity());
					success(response, null);
				}
				catch (RetrofitError retrofitError) {
					failure(retrofitError);
				}
				break;
			case ASYNCHRONOUS:
				services.getPoiCategories(ServiceEndpointUtils.getCity(), this);
				break;
			default:
				throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
		}

	}

	@Override
	public void success(GetPoiCategoriesResponse getPoiCategoriesResponse, Response response) {
	    if (getPoiCategoriesResponse == null) {
	        getPoiCategoriesResponse = new GetPoiCategoriesResponse();
	    }
		super.handleSuccess(getPoiCategoriesResponse, response);
	}

	@Override
	public void failure(RetrofitError retrofitError) {
		super.handleFailure(new GetPoiCategoriesResponse(), retrofitError);
	}

}