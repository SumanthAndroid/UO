package com.universalstudios.orlandoresort.controller.userinterface.global;

import android.support.v4.app.FragmentActivity;

import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;

/**
 * Base activity class that tracks life cycle metrics for inheriting classes
 * Created by Nicholas Hanna on 12/1/2016.
 */
public class AnalyticsActivity extends FragmentActivity {

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsUtils.collectLifecycleData(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AnalyticsUtils.pauseCollectingLifecycleData();
    }
}
