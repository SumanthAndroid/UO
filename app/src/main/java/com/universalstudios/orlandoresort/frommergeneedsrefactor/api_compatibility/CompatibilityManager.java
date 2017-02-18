package com.universalstudios.orlandoresort.frommergeneedsrefactor.api_compatibility;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Kevin Haines on 5/5/16.
 * Project: Universal Orlando
 * Class Name: CompatibilityManager
 * Class Description: This class can be used to make checks to see if the user's
 * API level is high enough for a feature.
 */
public class CompatibilityManager extends AppCompatActivity {
    public static final String TAG = "CompatibilityManager";

    public static boolean isAboveApi(int api) {
        return Build.VERSION.SDK_INT >= api;
    }
}
