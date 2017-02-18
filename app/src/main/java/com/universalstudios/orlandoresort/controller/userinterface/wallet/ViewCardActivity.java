package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.debug.DebugOptionsActivity;

public class ViewCardActivity extends AppCompatActivity implements ViewCardFragment.ViewCardListener {
    private static final String TAG = DebugOptionsActivity.class.getSimpleName();

    private static final String KEY_ARG_CARD = "KEY_ARG_CARD";
    public static final String KEY_ARGS_LAST_CARD_DELETED = "KEY_ARGS_LAST_CARD_DELETED";

    private String mCardId;
    private ViewCardFragment mFragment;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, ViewCardActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstance(Context context, String cardId) {
        Intent intent = new Intent(context, ViewCardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ARG_CARD, cardId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        //hide cc details from the multitasking snapshot functionality
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_view_card);
        setTitle(R.string.wallet_folio_manage_payment_method);

        // Default parameters
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            mCardId = null;
        }
        // Otherwise, set incoming parameters
        else {
            mCardId = bundle.getString(KEY_ARG_CARD);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            mFragment = ViewCardFragment.newInstance(mCardId);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_view_card_root_container, mFragment)
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
    protected void onStart() {
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
    protected void onStop() {
        super.onStop();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStop");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onDestroy");
        }
    }

    @Override
    public void onBackPressed() {
        boolean fragmentHandledBack = mFragment.onCancelClicked();
        if (!fragmentHandledBack) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //if adding card show error
                boolean fragmentHandledBack = mFragment.onCancelClicked();
                if (!fragmentHandledBack) {
                    finish();
                }
                return true;
            default:
                return (super.onOptionsItemSelected(item));
        }
    }

    @Override
    public void onCardAdded() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void onCardDeleted(boolean lastCardDeleted) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_ARGS_LAST_CARD_DELETED, lastCardDeleted);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onCardUpdated() {
        setResult(Activity.RESULT_OK);
        finish();
    }
}