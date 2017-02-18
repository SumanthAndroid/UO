package com.universalstudios.orlandoresort.model.network.domain.events;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.domain.preloadingdb.SyncEventSeriesWithDatabase;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
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
 * @author acampbell
 *
 */
public class GetEventSeriesRequest extends UniversalOrlandoServicesRequest
        implements Callback<GetEventSeriesResponse> {

    private static final String TAG = GetEventSeriesRequest.class.getSimpleName();

    private GetEventSeriesRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType) {
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

        public GetEventSeriesRequest build() {
            return new GetEventSeriesRequest(senderTag, priority, concurrencyType);
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
                    GetEventSeriesResponse response = services.getEventSeries(ServiceEndpointUtils.getCity());
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getEventSeries(ServiceEndpointUtils.getCity(), this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        super.handleFailure(new GetEventSeriesResponse(), retrofitError);
    }

    @Override
    public void success(GetEventSeriesResponse getEventSeriesResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "success");
        }

        if (getEventSeriesResponse != null) {

            // Store the last time event series were synced
            UniversalAppState universalAppState = UniversalAppStateManager
                    .getInstance();
            universalAppState.setDateOfLastEventSeriesSyncInMillis(new Date().getTime());
            UniversalAppStateManager.saveInstance();
            SyncEventSeriesWithDatabase syncEventSeriesWithDatabase = new SyncEventSeriesWithDatabase(getEventSeriesResponse);
            syncEventSeriesWithDatabase.successCaseEventSeries();

        } else {
            getEventSeriesResponse = new GetEventSeriesResponse();
        }

        // Inform any listeners after saving the response
        super.handleSuccess(getEventSeriesResponse, response);
    }
}
