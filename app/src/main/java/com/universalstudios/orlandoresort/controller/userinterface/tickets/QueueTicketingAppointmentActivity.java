package com.universalstudios.orlandoresort.controller.userinterface.tickets;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.account.AccountLoginActivity;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkActivity;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.network.domain.appointments.queues.QueuesResult;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;


/**
 *
 * Created by GOKHAN on 7/19/2016.
 */
public class QueueTicketingAppointmentActivity extends NetworkActivity{

    private static final String TAG = QueueTicketingAppointmentActivity.class.getSimpleName();
    private long queueId;
    private static final String KEY_ARG_QUEUE_ID = "KEY_ARG_QUEUE_ID";
    private static final String KEY_ARG_QUEUE_DISPLAY_NAME = "KEY_ARG_QUEUE_DISPLAY_NAME";
    private static final String KEY_ARG_QUEUE_RESULT_JSON_STR = "KEY_ARG_QUEUE_RESULT_JSON_STR";
    private static final String KEY_ARG_IMAGE_URL_STR = "KEY_ARG_IMAGE_URL_STR";
    private static final String KEY_ARG_PARK_NAME = "KEY_ARG_PARK_NAME";

    private static final String KEY_ARG_IS_CHANGE_TIME = "KEY_ARG_IS_CHANGE_TIME";
    private static final String KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR = "KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR";
    private static final String KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR = "KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR";

    private static final int LOGIN_REQUEST_CODE = 12345;

    private  MenuItem mReturnTicketMenuItem;
    private String queueDisplayName;
    private String queueResultJsonStr;
    private String appointmentTimesJsonStr;
    private QueuesResult queueResultJson;
    private String thumbnailImageUrl;
    private String parkName;
    private boolean isChangeTime;
    private String createdAppointmentTimeStr;
    private String createdTicketAppointmentStr;

    public static Bundle newInstanceBundle(String appointmentTimesJsonStr,long queueId, String queueResultStr,
                                           String queueDisplayName, String thumbnailImageUrl,String parkName) {
        Bundle args = new Bundle();
        args.putLong(KEY_ARG_QUEUE_ID, queueId);
        args.putString(KEY_ARG_QUEUE_DISPLAY_NAME,queueDisplayName);
        args.putString(KEY_ARG_QUEUE_RESULT_JSON_STR,queueResultStr);
        args.putString(KEY_ARG_IMAGE_URL_STR, thumbnailImageUrl);
        args.putString(KEY_ARG_PARK_NAME, parkName);
        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }
        setContentView(R.layout.activity_queue_ticketing);

        Bundle args = getIntent().getExtras();
        if (args != null){
            queueId = args.getLong(KEY_ARG_QUEUE_ID);
            queueDisplayName = args.getString(KEY_ARG_QUEUE_DISPLAY_NAME);
            queueResultJsonStr = args.getString(KEY_ARG_QUEUE_RESULT_JSON_STR);
            thumbnailImageUrl = args.getString(KEY_ARG_IMAGE_URL_STR);
            parkName = args.getString(KEY_ARG_PARK_NAME);
            isChangeTime = args.getBoolean(KEY_ARG_IS_CHANGE_TIME);
            if(isChangeTime) {
                createdAppointmentTimeStr = args.getString(KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR);
                createdTicketAppointmentStr = args.getString(KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR);
            }
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            if(AccountStateManager.isUserLoggedIn()) {
                addQueueTicketingAppointmentFragment();
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
                addQueueTicketingAppointmentFragment();
            } else {
                this.finish();
            }
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "handleNetworkResponse");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void addQueueTicketingAppointmentFragment() {
        QueueTicketingFragment frag;

        if(isChangeTime) {
            frag = QueueTicketingFragment.newInstance(queueId,queueResultJsonStr,queueDisplayName, thumbnailImageUrl,parkName, createdTicketAppointmentStr, createdAppointmentTimeStr);
        } else {
            frag = QueueTicketingFragment.newInstance(queueId,queueResultJsonStr,queueDisplayName, thumbnailImageUrl,parkName);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_queue_ticketing_fragment_container,
                        frag)
                .commit();
    }
}
