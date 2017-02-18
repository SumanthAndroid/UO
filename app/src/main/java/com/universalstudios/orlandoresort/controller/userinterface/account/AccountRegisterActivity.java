package com.universalstudios.orlandoresort.controller.userinterface.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;

/**
 * {@link AccountRegisterActivity} which will initiate the {@link AccountRegisterFragment}.
 *
 * Created by Tyler Ritchie on 11/17/16.
 */

public class AccountRegisterActivity extends NetworkRefreshActivity implements AccountRegisterResultHandler {

    private static final String TAG = AccountRegisterActivity.class.getSimpleName();

    /**
     * Create a bundle to pass to {@link AccountRegisterActivity}.
     *
     * @param context The context
     *
     * @return bundle of activity extras
     */
    public static Intent newInstanceIntent(Context context) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstanceIntent");
        }
        return new Intent(context, AccountRegisterActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }
        setContentView(R.layout.activity_registration);

        // If this is the first creation, add fragments
        if (savedInstanceState == null) {
            ViewGroup fragmentContainer = (ViewGroup) findViewById(R.id.account_registration_container);
            if (fragmentContainer != null) {
                AccountRegisterFragment fragment =
                        AccountRegisterFragment.newInstance();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(fragmentContainer.getId(), fragment,
                        fragment.getClass().getName());
                fragmentTransaction.commit();
            }
        }
        // Otherwise, restore state
        else {

        }

        // Set up remaining misc. components
        TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();
        setActionbarTitle(tridionConfig.getCreateAccountPageTitle());
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
    public void onRegisterResult(boolean didRegister) {
        int resultCode = didRegister ? Activity.RESULT_OK : Activity.RESULT_CANCELED;
        setResult(resultCode, new Intent());
        finish();
    }

}
