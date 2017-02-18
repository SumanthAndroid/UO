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
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;

import org.parceler.Parcels;

/**
 * {@link AccountUpdatePasswordActivity} which will initiate the {@link AccountUpdatePasswordActivity}.
 *
 * Created by Tyler Ritchie on 11/17/16.
 */

public class AccountUpdatePasswordActivity extends NetworkRefreshActivity implements AccountUpdateProvider {

    private static final String TAG = AccountUpdatePasswordActivity.class.getSimpleName();
    private static final String KEY_ARG_GUEST_PROFILE = "KEY_ARG_GUEST_PROFILE";

    /**
     * Create a bundle to pass to {@link AccountUpdatePasswordActivity}.
     *
     * @return bundle of activity extras
     */
    public static Intent newInstanceIntent(Context context, GuestProfile guestProfile) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstanceIntent");
        }

        Bundle args = new Bundle();
        if (guestProfile != null) {
            args.putParcelable(KEY_ARG_GUEST_PROFILE, Parcels.wrap(guestProfile));
        }
        Intent intent = new Intent(context, AccountUpdatePasswordActivity.class);
        intent.putExtras(args);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }
        setContentView(R.layout.activity_account_update_password);

        // Parameter declarations
        GuestProfile guestProfile;

        Bundle args = getIntent().getExtras();
        // Default parameters
        if (args == null) {
            guestProfile = null;
        }
        // Set incoming parameters from args
        else {
            guestProfile = Parcels.unwrap(args.getParcelable(KEY_ARG_GUEST_PROFILE));
        }

        // If this is the first creation, add fragments
        if (savedInstanceState == null) {
            ViewGroup fragmentContainer = (ViewGroup) findViewById(R.id.account_update_password_container);
            if (fragmentContainer != null) {
                AccountUpdatePasswordFragment fragment =
                        AccountUpdatePasswordFragment.newInstance(guestProfile);

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
        setActionbarTitle(tridionConfig.getUpdatePasswordPageTitle());
    }

    @Override
    public void updateAccount(boolean didUpdateAccount) {
        int resultCode = didUpdateAccount ? Activity.RESULT_OK : Activity.RESULT_CANCELED;
        setResult(resultCode, new Intent());
        finish();
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

}
