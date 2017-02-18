package com.universalstudios.orlandoresort.model.state.wayfinding;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;

/**
 * 
 * 
 * @author Steven Byle
 */
public class WayfindingStateManager {
	private static final String TAG = WayfindingStateManager.class.getSimpleName();

	private static WayfindingState sInstance;
	private static List<OnWayfindingStateChangeListener> sStateChangeListeners;

	private WayfindingStateManager() {
	}

	public static synchronized WayfindingState getInstance() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getInstance");
		}

		if (sInstance == null) {
			sInstance = new WayfindingState();
		}

		return sInstance;
	}

	public static synchronized void resetWayfindingState() {
		boolean wasWayfindingScreenShowing = getInstance().isWayfindingScreenShowing();
		sInstance = null;
		sInstance = new WayfindingState();

		// Copy over the state if the wayfinding screen was showing
		sInstance.setWayfindingScreenShowing(wasWayfindingScreenShowing);

		notifyStateChangeListeners();
	}

	public static synchronized void notifyStateChangeListeners() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "notifyStateChangeListeners");
		}

		// Inform all listeners of the latest state changes
		for (OnWayfindingStateChangeListener listener : getStateChangeListenersInstance()) {
			if (listener != null) {
				listener.onWayfindingStateChange(getInstance());
			}
		}
	}

	public static synchronized boolean registerStateChangeListener(OnWayfindingStateChangeListener listener) {
		return getStateChangeListenersInstance().add(listener);
	}

	public static synchronized boolean unregisterStateChangeListener(OnWayfindingStateChangeListener listener) {
		return getStateChangeListenersInstance().remove(listener);
	}

	private synchronized static List<OnWayfindingStateChangeListener> getStateChangeListenersInstance() {
		if (sStateChangeListeners == null) {
			sStateChangeListeners = new ArrayList<OnWayfindingStateChangeListener>();
		}
		return sStateChangeListeners;
	}

}
