package com.universalstudios.orlandoresort.controller.userinterface.events;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;

/**
 * Container activity events
 * 
 * @author acampbell
 *
 */
public class EventSeriesActivity extends NetworkRefreshActivity {

    private static final String TAG = EventSeriesActivity.class.getSimpleName();

    private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";

    public static Bundle newInstanceBundle(int actionBarTitleResId) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID, actionBarTitleResId);
        return bundle;
    }

    private ViewGroup mEventSeriesFragmentContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }
        setContentView(R.layout.activity_event_series);

        int actionBarTitleResId;

        // Default parameters
        Bundle args = getIntent().getExtras();
        if (args == null) {
            actionBarTitleResId = -1;
        }
        // Otherwise, set incoming parameters
        else {
            actionBarTitleResId = args.getInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {

            // Load the events fragment
            mEventSeriesFragmentContainer = (ViewGroup) findViewById(R.id.activity_event_series_fragment_container);
            if (mEventSeriesFragmentContainer != null) {
                EventSeriesFragment fragment = EventSeriesFragment.newInstance(actionBarTitleResId, false);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(mEventSeriesFragmentContainer.getId(), fragment, fragment
                        .getClass().getName());

                fragmentTransaction.commit();
            }
        }
        // Otherwise, restore state
        else {

        }

        ActionBar actionBar = getActionBar();
        actionBar.setTitle(actionBarTitleResId);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStart");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onResume");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onPause");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onSaveInstanceState");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStop");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onDestroy");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
            default:
                return (super.onOptionsItemSelected(item));
        }
    }

}
