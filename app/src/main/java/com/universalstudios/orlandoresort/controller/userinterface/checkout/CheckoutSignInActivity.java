package com.universalstudios.orlandoresort.controller.userinterface.checkout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.account.ForgotPasswordFragment;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;

public class CheckoutSignInActivity extends NetworkRefreshActivity implements CheckoutSignInFragment.CheckoutSignInFragmentListener {
    private static final String TAG = CheckoutSignInActivity.class.getSimpleName();
    public static final String KEY_ARG_SHOP_HHN_THEME = "KEY_ARG_SHOP_ITEM_HHN";


    public static Intent newInstanceIntent(Context context) {
        Bundle bundle = new Bundle();

        Intent intent = new Intent(context, CheckoutSignInActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceIntent(Context context, boolean isHollywoodHorrorNights) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_ARG_SHOP_HHN_THEME, isHollywoodHorrorNights);
        Intent intent = new Intent(context, CheckoutSignInActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }
        TridionConfig mTridionConfig = IceTicketUtils.getTridionConfig();
        Bundle args = getIntent().getExtras();
        if (args != null && args.getBoolean(KEY_ARG_SHOP_HHN_THEME, false)) {
            setTheme(R.style.HollywoodHorrorNightsTheme);
        } else {
            setTheme(R.style.Default);
        }
        setContentView(R.layout.activity_checkout_sign_in);

        // Default parameters
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
        }
        // Otherwise, set incoming parameters
        else {
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            CheckoutSignInFragment fragment = CheckoutSignInFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_checkout_sign_in_container, fragment)
                    .commit();
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {
        }

        // Set title from Tridion config
        if (null != getActionBar()) {
            getActionBar().setDisplayShowHomeEnabled(false);
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
    public void onLogin() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void onForgotPasswordClicked() {
        //TODO make forgot password activity
        ForgotPasswordFragment fragment = ForgotPasswordFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_checkout_sign_in_container, fragment)
                .addToBackStack(ForgotPasswordFragment.TAG)
                .commit();
    }
}
