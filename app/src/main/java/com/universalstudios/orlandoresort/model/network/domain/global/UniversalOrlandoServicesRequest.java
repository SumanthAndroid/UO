package com.universalstudios.orlandoresort.model.network.domain.global;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

/**
 * 
 * 
 * @author Steven Byle
 */
public abstract class UniversalOrlandoServicesRequest extends NetworkRequest {
	private static final String TAG = UniversalOrlandoServicesRequest.class.getSimpleName();

	protected UniversalOrlandoServicesRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
		super(senderTag, priority, concurrencyType, networkParams);
	}

	@Override
	public String getServicesBaseUrl() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getServicesBaseUrl");
		}

		UniversalAppState universalAppState = UniversalAppStateManager.getInstance();
		return universalAppState.getServicesBaseUrl();
	}

}