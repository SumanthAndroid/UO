package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;

public abstract class IBMOrlandoServicesRequest extends NetworkRequest {

    // Logging Tag
    private static final String TAG = IBMOrlandoServicesRequest.class.getSimpleName();

    protected IBMOrlandoServicesRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    @Override
    public String getServicesBaseUrl() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getServicesBaseUrl");
        }

        return BuildConfig.COMMERCE_SERVICES_BASE_URL;

    }

}