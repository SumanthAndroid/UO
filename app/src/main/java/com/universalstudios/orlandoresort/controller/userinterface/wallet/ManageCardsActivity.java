package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.debug.DebugOptionsActivity;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;

public class ManageCardsActivity extends AppCompatActivity {
    private static final String TAG = DebugOptionsActivity.class.getSimpleName();


    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, ManageCardsActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();
        setContentView(R.layout.activity_manage_cards);
        setTitle(tridionConfig.getFolioPaymentMethodsLabel());

        // Default parameters
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
        }
        // Otherwise, set incoming parameters
        else {
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            ManageCardsFragment manageCardsFragment = ManageCardsFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_manage_cards_root_container, manageCardsFragment)
                    .commit();
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {
        }

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onRestoreInstanceState");
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
}