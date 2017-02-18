
package com.universalstudios.orlandoresort.controller.userinterface.global;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.universalstudios.orlandoresort.BuildConfig;

/**
 * @author Steven Byle
 */
public abstract class OrientationLockActivity extends AnalyticsActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check to see if the Activity explicitly requests for landscape or portrait
        int requestedOrientation = getRequestedOrientation();
        if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            && requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            && requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
            && requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
            && requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            && requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            && requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            && requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT) {

            // If the Activity does not request an explicit orientation, enable rotation
            // for dev testing of state saving and proper layout responsiveness
            int newOrientation = BuildConfig.SCREEN_ROTATION_ENABLED ?
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            setRequestedOrientation(newOrientation);
        }
    }
}
