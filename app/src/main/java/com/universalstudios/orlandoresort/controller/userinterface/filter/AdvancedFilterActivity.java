/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.filter;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.filter.MultiChoiceFilterDialogFragment.MultiChoiceOptionsType;
import com.universalstudios.orlandoresort.controller.userinterface.filter.MultiChoiceFilterDialogFragment.OnMultiChoiceFragmentCloseListener;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;

import java.util.List;

/**
 * @author acampbell
 *
 */
public class AdvancedFilterActivity extends NetworkRefreshActivity implements
        OnMultiChoiceFragmentCloseListener {

    private static final String TAG = AdvancedFilterActivity.class.getSimpleName();

    private static final String KEY_ARG_FILTER_OPTIONS = "KEY_ARG_FILTER_OPTIONS";
    public static final String KEY_ARG_RESULT_FILTER_OPTIONS = "KEY_ARG_RESULT_FILTER_OPTIONS";
    private static final String KEY_ARG_EXPLORE_TYPE = "KEY_EXPLORE_TYPE";

    private AdvancedFilterFragment mAdvancedFilterFragment;
    private ExploreType mExploreType;

    public static Bundle newInstanceBunde(FilterOptions filterOptions, ExploreType exploreType) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ARG_FILTER_OPTIONS, filterOptions.toJson());
        bundle.putSerializable(KEY_ARG_EXPLORE_TYPE, exploreType);

        return bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        setContentView(R.layout.activity_advanced_filter);

        // Set title
        setTitle(R.string.advanced_filter_title);

        Bundle bundle = getIntent().getExtras();
        FilterOptions filterOptions = new Gson().fromJson(bundle.getString(KEY_ARG_FILTER_OPTIONS),
                FilterOptions.class);
        mExploreType = (ExploreType) bundle.getSerializable(KEY_ARG_EXPLORE_TYPE);

        // If this is the first creation, add fragments
        if (savedInstanceState == null) {
            View container = findViewById(R.id.activity_advanced_filter_container);
            if (container != null) {
                mAdvancedFilterFragment = AdvancedFilterFragment.newInstance(filterOptions, mExploreType);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(container.getId(), mAdvancedFilterFragment, mAdvancedFilterFragment
                        .getClass().getName());
                transaction.commit();
            }
        }
        // Otherwise, restore state
        else {

        }

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

    @Override
    public void onMultiChoiceFragmentClose(MultiChoiceOptionsType multiChoiceOptionsType,
            List<String> selectedItems) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onMultiChoiceFragmentClose");
        }

        if (mAdvancedFilterFragment != null) {
            switch (multiChoiceOptionsType) {
                case ACCESSIBILITY:
                    mAdvancedFilterFragment.setAccessibiltyOptions(selectedItems);
                    break;
                case RIDE_VENUE:
                    mAdvancedFilterFragment.setRideFilterVenues(selectedItems);
                    break;
                case RIDE_TYPE:
                    mAdvancedFilterFragment.setRideSubTypes(selectedItems);
                    break;
                case SHOW_VENUE:
                    mAdvancedFilterFragment.setShowFilterVenues(selectedItems);
                    break;
                case SHOW_TYPE:
                    mAdvancedFilterFragment.setShowSubTypes(selectedItems);
                    break;
                case DINING_VENUE:
                    mAdvancedFilterFragment.setDiningFilterVenues(selectedItems);
                    break;
                case DINING_TYPE:
                    mAdvancedFilterFragment.setDiningSubTypes(selectedItems);
                    break;
                case DINING_PLAN:
                    mAdvancedFilterFragment.setDiningPlans(selectedItems);
                    break;
                case SHOPPING_TYPE:
                    mAdvancedFilterFragment.setShoppingSubTypes(selectedItems);
                    break;
                case SHOPPING_VENUE:
                    mAdvancedFilterFragment.setShoppingFilterVenues(selectedItems);
                    break;
                case ENTERTAINMENT_TYPE:
                    mAdvancedFilterFragment.setEntertainmentSubTypes(selectedItems);
                    break;
                case RENTAL_SERVICES_VENUE:
                    mAdvancedFilterFragment.setRentalServicesFilterVenues(selectedItems);
                    break;
                default:
                    if (BuildConfig.DEBUG) {
                        Log.w(TAG, "onMultiChoiceFragmentClose: Unknown multi choice type");
                    }
                    break;
            }
        }
    }

}
