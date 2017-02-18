package com.universalstudios.orlandoresort.controller.userinterface.offers;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;

/**
 * @author acampbell
 *
 */
public class OfferCouponCodeActivity extends NetworkRefreshActivity {

    private static final String TAG = OfferCouponCodeActivity.class.getSimpleName();

    private static final String KEY_ARG_OFFER_ID = "KEY_ARG_OFFER_ID";

    private View mFragmentContainer;
    private long mOfferId;

    public static Bundle newInstanceBundle(long offerId) {
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_ARG_OFFER_ID, offerId);

        return bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }
        setContentView(R.layout.activity_offer_coupon_code);

        // Setup views
        mFragmentContainer = findViewById(R.id.activity_offer_coupon_code_container);

        // Default parameters
        Bundle args = getIntent().getExtras();
        if (args == null) {}
        // Otherwise, set incoming parameters
        else {
            mOfferId = args.getLong(KEY_ARG_OFFER_ID);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            // Offer coupon fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(mFragmentContainer.getId(), OfferCouponCodeFragment.newInstance(mOfferId))
                    .commit();
        }
        // Otherwise, restore state
        else {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onOptionsItemSelected");
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
