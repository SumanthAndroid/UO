package com.universalstudios.orlandoresort.controller.userinterface.offers;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;

/**
 * @author acampbell
 *
 */
public class TermsAndConditionsActivity extends NetworkRefreshActivity {

    private static final String TAG = TermsAndConditionsActivity.class.getSimpleName();

    private static final String KEY_ARG_DESC = "KEY_ARG_DESC";
    private static final String KEY_STATE_DESC = "KEY_STATE_DESC";

    String mDesc;
    TextView mDescTextView;

    public static Bundle newInstanceBundle(String desc) {
        Bundle args = new Bundle();
        args.putString(KEY_ARG_DESC, desc);

        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        setContentView(R.layout.activity_terms_and_conditions);

        mDescTextView = (TextView) findViewById(R.id.activity_terms_and_conditions_desc_textview);

        // Default parameters
        Bundle args = getIntent().getExtras();
        if (args == null) {}
        // Otherwise, set incoming parameters
        else {
            mDesc = args.getString(KEY_ARG_DESC);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {

        } else {
            mDesc = savedInstanceState.getString(KEY_STATE_DESC);
        }

        mDescTextView.setText(mDesc);
        getActionBar().setTitle(R.string.offers_terms_and_conditions);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onSaveInstanceState");
        }

        outState.putString(KEY_STATE_DESC, mDesc);
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
