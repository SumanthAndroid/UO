package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;

/**
 * Created by kbojarski on 10/26/16.
 */

public abstract class AddOnsFragment extends NetworkFragment {
    private static final String TAG = AddOnsFragment.class.getSimpleName();

    protected IAddOnsCallbacks mCallbacks;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAttach");
        }
        Fragment parentFragment = getParentFragment();
        // Check if parent fragment (if there is one) implements the interface
        if (parentFragment != null && parentFragment instanceof IAddOnsCallbacks) {
            mCallbacks = (IAddOnsCallbacks) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (activity != null && activity instanceof IAddOnsCallbacks) {
            mCallbacks = (IAddOnsCallbacks) activity;
        }
        // If neither implements the interface, log a warning
        else if (mCallbacks == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement IAddOnsCallbacks");
            }
        }
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        //empty
    }
}
