package com.universalstudios.orlandoresort.controller.userinterface.detail;

import java.lang.reflect.Type;
import java.util.List;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;

/**
 * @author acampbell
 *
 */
public class DiningMenuDetailActivity extends NetworkRefreshActivity {

    private static final String TAG = DiningMenuDetailActivity.class.getSimpleName();

    private static final String KEY_ARG_MENU_IMAGES = "KEY_ARG_MENU_IMAGES";

    private List<String> mMenuImages;

    public static Bundle newInstanceBundle(List<String> menuImages) {
        Bundle args = new Bundle();
        args.putString(KEY_ARG_MENU_IMAGES, new Gson().toJson(menuImages));

        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }
        // Allow activity to be rotated
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        setContentView(R.layout.activity_dining_menu);

        // Default parameters
        Bundle args = getIntent().getExtras();
        if (args == null) {

        }
        // Otherwise, set incoming parameters
        else {
            Type type = new TypeToken<List<String>>() {}.getType();
            mMenuImages = new Gson().fromJson(args.getString(KEY_ARG_MENU_IMAGES), type);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_dining_menus_fragment_container,
                            DiningMenuDetailFragment.newInstance(mMenuImages)).commit();
        }
        // Otherwise, restore state
        else {}

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onOptionsItemSelected");
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
