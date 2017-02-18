package com.universalstudios.orlandoresort.model.network.domain.tridion;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/19/16.
 * Class: GetMobilePagesRequest
 * Class Description: Request to get Mobile Pages
 */
public class GetMobilePagesRequest extends UniversalOrlandoServicesRequest implements Callback<GetMobilePagesResponse> {
    public static final String TAG = "GetMobilePagesRequest";

    private GetMobilePagesRequest(String senderTag, Priority priority) {
        super(senderTag, priority, ConcurrencyType.ASYNCHRONOUS, null);
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
            case ASYNCHRONOUS:
                services.getMobilePages("1", "10", this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {

        public Builder(NetworkRequestSender sender) {
            super(sender);
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public GetMobilePagesRequest build() {
            return new GetMobilePagesRequest(senderTag, priority);
        }
    }

    @Override
    public void success(GetMobilePagesResponse getMobilePagesResponse, retrofit.client.Response response) {
        if (null != getMobilePagesResponse) {
            syncWithDatabase(getMobilePagesResponse);
        }
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            retrofitError.printStackTrace();
        }
    }

    private void syncWithDatabase(GetMobilePagesResponse response) {
        if (null == response || null == response.getPages() || response.getPages().isEmpty()) {
            return;
        }

        ContentResolver resolver = UniversalOrlandoApplication.getAppContext().getContentResolver();

        if (null != resolver) {
            resolver.delete(UniversalOrlandoContentUris.MOBILE_PAGES, null, null);

            for (MobilePage page : response.getPages()) {
                ContentValues contentValues = getContentValues(page);
                resolver.insert(UniversalOrlandoContentUris.MOBILE_PAGES, contentValues);
            }
        }
    }

    private ContentValues getContentValues(MobilePage page) {
        ContentValues values = new ContentValues();
        values.put(UniversalOrlandoDatabaseTables.MobilePagesTable.COL_IDENTIFIER, page.getPageIdentifier());
        values.put(UniversalOrlandoDatabaseTables.MobilePagesTable.COL_JSON, page.toJson());
        return values;
    }
}


