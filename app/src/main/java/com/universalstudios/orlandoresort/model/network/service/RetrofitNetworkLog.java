package com.universalstudios.orlandoresort.model.network.service;

import android.util.Log;

import retrofit.android.AndroidLog;

/**
 * A log implementation for Retrofit's networking API.
 * 
 * @author Steven Byle
 */
public class RetrofitNetworkLog extends AndroidLog {
	private static final String TAG = RetrofitNetworkLog.class.getSimpleName();

	public RetrofitNetworkLog() {
		super(TAG);
	}

	@Override
	public void logChunk(String chunk) {
		Log.i(getTag(), chunk);
	}
}