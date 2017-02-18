package com.universalstudios.orlandoresort.controller.userinterface.tickets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.account.AccountLoginActivity;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkActivity;
import com.universalstudios.orlandoresort.model.network.domain.appointments.AppointmentTimes;
import com.universalstudios.orlandoresort.model.network.domain.appointments.CreateAppointmentTimeResponse;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

/**
 * Created by GOKHAN on 7/27/2016.
 */
public class ReturnTimeTicketDoneActivity extends NetworkActivity {

    private static final String TAG = ReturnTimeTicketDoneActivity.class.getSimpleName();
    private static final String KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR = "KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR";
    private static final String KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR = "KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR";
    private static final String KEY_ARG_IS_HAS_BEEN_READ = "KEY_ARG_IS_HAS_BEEN_READ";

    private static final String KEY_ARG_CHANGE_TIME_BUNDLE = "KEY_ARG_CHANGE_TIME_BUNDLE";
    private static final String KEY_ARG_SELECTED_POI_OBJECT_JSON = "KEY_ARG_SELECTED_POI_OBJECT_JSON";
    private static final String KEY_ARG_SELECTED_POI_TYPE_ID = "KEY_ARG_SELECTED_POI_TYPE_ID";

    private static final int LOGIN_REQUEST_CODE = 12345;

    private AppointmentTimes createdAppointmentTimeObj;
    private CreateAppointmentTimeResponse createdTicketAppointmentObj;
    private String createdAppointmentTimeStr, createdTicketAppointmentStr;
    private int guestQuantity;
    private static Boolean hasBeenRead;

    /** Bundle to start the queue ticket activity from pressing the change return time button */
    private Bundle changeTimeBundle = null;

    private String poiJson;
    private int poiTypeId;


    public static Bundle newInstanceBundle(String createdAppointmentTimeStr, String createdTicketAppointmentStr, Boolean hasBeenRead){
        Bundle args = new Bundle();
        args.putString(KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR, createdAppointmentTimeStr);
        args.putString(KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR, createdTicketAppointmentStr);
        args.putBoolean(KEY_ARG_IS_HAS_BEEN_READ, hasBeenRead);
        return args;
    }

    public static Bundle newInstanceBundle(String createdAppointmentTimeStr, String createdTicketAppointmentStr, Boolean hasBeenRead, Bundle changeTimeBundle, String poiJson, int poiTypeId){
        Bundle args = new Bundle();
        args.putString(KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR, createdAppointmentTimeStr);
        args.putString(KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR, createdTicketAppointmentStr);
        args.putBoolean(KEY_ARG_IS_HAS_BEEN_READ, hasBeenRead);
        args.putBundle(KEY_ARG_CHANGE_TIME_BUNDLE, changeTimeBundle);
        args.putString(KEY_ARG_SELECTED_POI_OBJECT_JSON, poiJson);
        args.putInt(KEY_ARG_SELECTED_POI_TYPE_ID, poiTypeId);
        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }
        setContentView(R.layout.activity_return_time_ticket);

        Bundle args = getIntent().getExtras();
        if (args != null){
            createdAppointmentTimeStr = args.getString(KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR);
            createdTicketAppointmentStr = args.getString(KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR);

            hasBeenRead = args.getBoolean(KEY_ARG_IS_HAS_BEEN_READ);
            changeTimeBundle = args.getBundle(KEY_ARG_CHANGE_TIME_BUNDLE);
            poiJson = args.getString(KEY_ARG_SELECTED_POI_OBJECT_JSON);
            poiTypeId = args.getInt(KEY_ARG_SELECTED_POI_TYPE_ID);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            if(AccountStateManager.isUserLoggedIn()) {
                addReturnTimeTicketDoneFragment();
            } else {
                startActivityForResult(AccountLoginActivity.newInstanceIntent(this), LOGIN_REQUEST_CODE);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LOGIN_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                addReturnTimeTicketDoneFragment();
            } else {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getParent() == null) {
            finish();
            setResult(Activity.RESULT_OK);
        }
        else {
            finish();
            getParent().setResult(Activity.RESULT_OK);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onOptionsItemSelected");
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                if (hasBeenRead) {
                    finish();
                    this.setResult(Activity.RESULT_OK);
                }else{
                    finish();
                    this.setResult(Activity.RESULT_OK);
//                    Bundle homeActivityBundle = HomeActivity.newInstanceBundle(HomeActivity.START_PAGE_TYPE_HOME);
//                    startActivity(new Intent(this, HomeActivity.class).putExtras(homeActivityBundle));
                }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "handleNetworkResponse");
        }
    }

    private void addReturnTimeTicketDoneFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_return_time_ticket_fragment_container,
                        ReturnTimeTicketDoneFragment.newInstance(createdAppointmentTimeStr, createdTicketAppointmentStr, hasBeenRead, changeTimeBundle, poiJson, poiTypeId))
                .commit();
    }
}
