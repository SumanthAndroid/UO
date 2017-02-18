package com.universalstudios.orlandoresort.controller.userinterface.tickets;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.DialogUtils;
import com.universalstudios.orlandoresort.controller.userinterface.global.LocationUtils;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.permissions.PermissionsManager;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.permissions.PermissionsRequestsListener;
import com.universalstudios.orlandoresort.model.network.domain.appointments.AppointmentTimes;
import com.universalstudios.orlandoresort.model.network.domain.appointments.CreateAppointmentTimeRequest;
import com.universalstudios.orlandoresort.model.network.domain.appointments.CreateAppointmentTimeResponse;
import com.universalstudios.orlandoresort.model.network.domain.appointments.DeleteCreatedAppointmentTicketRequest;
import com.universalstudios.orlandoresort.model.network.domain.appointments.GetAppointmentTimesRequest;
import com.universalstudios.orlandoresort.model.network.domain.appointments.GetAppointmentTimesResponse;
import com.universalstudios.orlandoresort.model.network.domain.appointments.queues.QueuesResult;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;
import com.universalstudios.orlandoresort.view.fonts.Button;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * Created by GOKHAN on 7/19/2016.
 */
public class QueueTicketingFragment extends NetworkFragment implements ActionBarTitleProvider,View.OnClickListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemClickListener {


    private static final String TAG = QueueTicketingFragment.class.getSimpleName();
    public static final int MAX_APPOINTMENT_SIZE = 10;
    private long queueId;
    private long queueEntityId2;
    private static final String KEY_ARG_QUEUE_ID = "KEY_ARG_QUEUE_ID";
    private static final String KEY_ARG_QUEUE_DISPLAY_NAME = "KEY_ARG_QUEUE_DISPLAY_NAME";
    private static final String KEY_ARG_QUEUE_RESULT_JSON = "KEY_ARG_QUEUE_RESULT_JSON";
    private static final String KEY_ARG_IMAGE_URL_STR = "KEY_ARG_IMAGE_URL_STR";
    private static final String KEY_ARG_PARK_NAME = "KEY_ARG_PARK_NAME";
    private static final String KEY_ARG_IS_CHANGE_TIME = "KEY_ARG_IS_CHANGE_TIME";

    private static final String KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR = "KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR";
    private static final String KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR = "KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR";

    private static final int LOCATION_REQUEST_CODE = 10101;

    private String queueDisplayName;
    private LinearLayout appointmentTimeParentLayout;
    private TextView appointmentTimeView, progressBarTrackText;

    private GridView gridView;
    private AppointmentTimeAdapter appointmentTimeAdapter;
    Context appContext;
    private List<AppointmentTimes> appointmentTimes;
    private int selectedGridViewItemPosition;
    private AppointmentTimes selectedAppointmentTime;
    private GetAppointmentTimesResponse getAppointmentTimesResponse;
    private int guestQuantity;
    private QueuesResult queueResultJson;
    private String queueResultJsonStr;
    private String thumbnailImageUrl;
    private SeekBar apptSeekBar;
    private String parkName;
    ProgressDialog progressDialog;
    private TextView noAppointmentTime;
    private TextView maxAppointmentCount;
    private View selectedItemView;
    private boolean isChangeTime;
    private AppointmentTimes createdAppointmentTimeObj;
    private CreateAppointmentTimeResponse createdTicketAppointmentObj;
    private String createdAppointmentTimeStr;
    private String createdTicketAppointmentStr;
    private int sliderQuantity = 0;
    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            getActivity().finish();
        }
    };
    CreateAppointmentTimeResponse createAppointmentTimeResponse;
    private Button reserveButton;

    public static QueueTicketingFragment newInstance(long queueId,String queueResultJsonStr,
                                                     String queueDisplayName,String thumbnailImageUrl,String parkName){
        QueueTicketingFragment fragment = new QueueTicketingFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_ARG_QUEUE_ID,queueId);
        args.putString(KEY_ARG_QUEUE_DISPLAY_NAME,queueDisplayName);
        args.putString(KEY_ARG_QUEUE_RESULT_JSON, queueResultJsonStr);
        args.putString(KEY_ARG_IMAGE_URL_STR,thumbnailImageUrl);
        args.putString(KEY_ARG_PARK_NAME,parkName);
        fragment.setArguments(args);
        return fragment;
    }

    public static QueueTicketingFragment newInstance(long queueId,String queueResultJsonStr,
                                                     String queueDisplayName,String thumbnailImageUrl,String parkName, String createdTicketAppointmentStr, String createdAppointmentTimeStr) {
        QueueTicketingFragment fragment = new QueueTicketingFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_ARG_QUEUE_ID,queueId);
        args.putString(KEY_ARG_QUEUE_DISPLAY_NAME,queueDisplayName);
        args.putString(KEY_ARG_QUEUE_RESULT_JSON, queueResultJsonStr);
        args.putString(KEY_ARG_IMAGE_URL_STR,thumbnailImageUrl);
        args.putString(KEY_ARG_PARK_NAME,parkName);
        args.putBoolean(KEY_ARG_IS_CHANGE_TIME, true);
        args.putString(KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR, createdTicketAppointmentStr);
        args.putString(KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR, createdAppointmentTimeStr);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        Bundle args = getArguments();
        if (args != null){
            queueId = args.getLong(KEY_ARG_QUEUE_ID);
            queueDisplayName = args.getString(KEY_ARG_QUEUE_DISPLAY_NAME);
            queueResultJsonStr = args.getString(KEY_ARG_QUEUE_RESULT_JSON);
            thumbnailImageUrl = args.getString(KEY_ARG_IMAGE_URL_STR);
            parkName = args.getString(KEY_ARG_PARK_NAME);
            isChangeTime = args.getBoolean(KEY_ARG_IS_CHANGE_TIME, false);

            if(isChangeTime) {
                createdAppointmentTimeStr = args.getString(KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR);
                createdTicketAppointmentStr = args.getString(KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR);

                createdAppointmentTimeObj = GsonObject.fromJson(createdAppointmentTimeStr, AppointmentTimes.class);
                createdTicketAppointmentObj = GsonObject.fromJson(createdTicketAppointmentStr, CreateAppointmentTimeResponse.class);

                if(createdTicketAppointmentObj != null) {
                    sliderQuantity = createdTicketAppointmentObj.getQuantity();
                }
            }
        }

        queueResultJson = GsonObject.fromJson(queueResultJsonStr, QueuesResult.class);

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {}
        // Otherwise restore state, overwriting any passed in parameters
        else {}

        setHasOptionsMenu(true);

        if(!PermissionsManager.isPermissionAllowed(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            PermissionsManager manager = new PermissionsManager();
            manager.handlePermissionRequest(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionsRequestsListener() {
                @Override
                public void onPermissionsUpdated(List<String> accepted, List<String> denied) {
                    if(accepted.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        checkPermissionRequisites();
                    } else {
                        showPermissionErrorDialog();
                    }
                }
            });
        } else {
            checkPermissionRequisites();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }

        Activity parentActivity = getActivity();
        if (parentActivity != null) {
            parentActivity.getActionBar().setTitle(provideTitle());
            appContext = getActivity().getApplicationContext();
        }

        View view = inflater.inflate(R.layout.fragment_detail_queue_ticket_app, container, false);

        maxAppointmentCount = (TextView)view.findViewById(R.id.appointment_time_slider_tv);
        int aptSize = queueResultJson.getMaxAppointmentSize();

        // Maximum displayed value is target value - 1; to show 9 the max value should be 8
        /* TODO: This is a hotfix for a server side limitation; this should be handled by the server. */
        if(aptSize > MAX_APPOINTMENT_SIZE) {
            aptSize = MAX_APPOINTMENT_SIZE;
        }

        apptSeekBar = (SeekBar)view.findViewById(R.id.appointment_time_slider);
        apptSeekBar.setMax(aptSize-1);
        apptSeekBar.setOnSeekBarChangeListener(this);
        sliderQuantity = 1;

        maxAppointmentCount.setText(String.valueOf(sliderQuantity));

        noAppointmentTime = (TextView)view.findViewById(R.id.no_appointment_time_available_tv);

        gridView = (GridView)view.findViewById(R.id.queue_ticket_appointment_time_layout);
        gridView.setChoiceMode(GridView.CHOICE_MODE_NONE);

        reserveButton = (Button)view.findViewById(R.id.ticket_appointment_confirmation);
        reserveButton.setOnClickListener(this);
        setReserveButtonVisible(false);
        
        if(isChangeTime) {
            reserveButton.setText(R.string.virtual_line_update_time);
        }

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /* If the time is unavailable for the party size, just return and don't process the click event */
        if(!appointmentTimeAdapter.isTimeGrayed((AppointmentTimes) appointmentTimeAdapter.getItem(position))) {
            if(selectedItemView != null) {
                selectedItemView.setActivated(false);
            }
            selectedItemView = view;
            selectedGridViewItemPosition = position;
            selectedAppointmentTime = appointmentTimes.get(position);
            selectedItemView.setActivated(true);
        }

        if(selectedAppointmentTime != null) {
            /* If this fragment is changing the time instead of creating a new one, check to see if quantity, start time and
             * end time match */
            if(isChangeTime) {
                /* If the all these items match, hide the button because the user is trying to reserve the same exact appointment they already have */
                if((sliderQuantity == createdTicketAppointmentObj.getQuantity() &&
                        selectedAppointmentTime.getStartTime().equals(createdAppointmentTimeObj.getStartTime()) &&
                        selectedAppointmentTime.getEndTime().equals(createdAppointmentTimeObj.getEndTime()))) {
                    setReserveButtonVisible(false);
                } else {
                    setReserveButtonVisible(true);
                }
            } else {
                /* If this fragment isn't changing the time, show the reserve button since the selected time isn't null */
                setReserveButtonVisible(true);
            }
        } else {
            setReserveButtonVisible(false);
        }
    }

    private void checkPermissionRequisites() {
        if(!LocationUtils.isLocationEnabled()) {
            showLocationEnabledDialog();
        } else if(!UniversalAppStateManager.getInstance().isInResortGeofence()) {
            showNotInParkDialog();
        } else {
            appointmentTimeWebServiceCall();
        }
    }


    private LinearLayout createLayoutAppointmentTimes(){

        LinearLayout appointmentTimeLayout = new LinearLayout(getActivity().getApplicationContext());
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearParams.setMargins(10,15,10,0);
        appointmentTimeLayout.setLayoutParams(linearParams);
        appointmentTimeLayout.setOrientation(LinearLayout.HORIZONTAL);

        return appointmentTimeLayout;
    }

    private void appointmentTimeWebServiceCall(){

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "appointmentTimeWebServiceCall");
        }

        Activity parentActivity = getActivity();
        if (parentActivity != null) {

            progressDialog = ProgressDialog.show(getActivity(), getString(R.string.please_wait_message),
                    "", true);

            GetAppointmentTimesRequest getAppointmentTimesRequest = new GetAppointmentTimesRequest.Builder(this)
                    .setPriority(NetworkRequest.Priority.HIGH)
                    .setConcurrencyType(NetworkRequest.ConcurrencyType.SYNCHRONOUS)
                    .setQueueId(queueId)
                    .build();
            NetworkUtils.queueNetworkRequest(getAppointmentTimesRequest);
            NetworkUtils.startNetworkService();

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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        progress += 1;
        if(appointmentTimeAdapter != null && !appointmentTimeAdapter.isEmpty()) {
            List<AppointmentTimes> times = new LinkedList<>();
            int size = appointmentTimeAdapter.getCount();
            for(int i = 0; i < size; i++) {
                AppointmentTimes time = (AppointmentTimes) appointmentTimeAdapter.getItem(i);
                /* Get the remaining capacity and compare it to the party size (i.e. val) */
                int remainingCapacity = time.getCapacity() - time.getUsedCapacity();
                if(progress > remainingCapacity) {
                    times.add(time);
                }
            }
            if(selectedAppointmentTime != null) {
                if(times.contains(selectedAppointmentTime) ||
                        ( createdTicketAppointmentObj != null &&
                            progress == createdTicketAppointmentObj.getQuantity() &&
                            createdAppointmentTimeObj.getStartTime() != null &&
                            createdAppointmentTimeObj.getEndTime() != null &&
                            selectedAppointmentTime.getStartTime().equals(createdTicketAppointmentObj.getAppointmentTime().getStartTime()) &&
                            selectedAppointmentTime.getEndTime().equals(createdTicketAppointmentObj.getAppointmentTime().getEndTime()))) {
                    setReserveButtonVisible(false);
                } else {
                    setReserveButtonVisible(true);
                }
            }
            sliderQuantity = progress;
            appointmentTimeAdapter.setGrayedAppointmentTimes(times);
            appointmentTimeAdapter.notifyDataSetChanged();
            maxAppointmentCount.setText(String.valueOf(progress));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        /* Unused */
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "handleNetworkResponse");
        }

        if (networkResponse instanceof GetAppointmentTimesResponse) {
            progressDialog.dismiss();

            getAppointmentTimesResponse = (GetAppointmentTimesResponse) networkResponse;
            appointmentTimes = getAppointmentTimesResponse.getAppointmentTimesResults();

            if(appointmentTimes.size()<1){
                noAppointmentTime.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
            }else{
                noAppointmentTime.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
            }

            appointmentTimeAdapter = new AppointmentTimeAdapter(appContext, R.layout.fragment_detail_queue_ticket_app_item, appointmentTimes);
            gridView.setAdapter(appointmentTimeAdapter);
            gridView.setOnItemClickListener(this);
        }

        if(networkResponse instanceof CreateAppointmentTimeResponse){
            progressDialog.dismiss();
            createAppointmentTimeResponse = (CreateAppointmentTimeResponse) networkResponse;
            createAppointmentTimeResponse.setTicketDisplayName(queueDisplayName);

            if(isChangeTime) {
                Bundle args = ReturnTimeTicketDoneActivity.newInstanceBundle(selectedAppointmentTime.toJson(),createAppointmentTimeResponse.toJson(),false);
                getActivity().setResult(Activity.RESULT_OK, new Intent().putExtras(args));
            }

            getActivity().finish();
            UserInterfaceUtils.showToastFromForeground("Ticket Created!", Toast.LENGTH_SHORT, getActivity());
            Bundle args = ReturnTimeTicketDoneActivity.newInstanceBundle(selectedAppointmentTime.toJson(),createAppointmentTimeResponse.toJson(),false);

            getActivity().finish();
        }
    }

    private TextView createAppointmentTimeTextView(){

        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tv=new TextView(getActivity());
        tv.setLayoutParams(lparams);

        return tv;
    }

    @Override
    public String provideTitle() {
        return getString(R.string.title_virtual_line_pass);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onClick(View v) {
        if(selectedAppointmentTime !=null) {
            makeCreateAppointmentTicketCall();
        }
    }

    private void setConfirmationDialog(){

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.virtual_line_create_return_time_title)
                .setMessage(R.string.virtual_line_delete_return_time_message)
                .setPositiveButton(getResources().getString(R.string.dialog_create_appointment_ticket_yes),
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with create
                        makeCreateAppointmentTicketCall();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void makeDeleteAppointmentTicketCall(){
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeDeleteAppointmentTicketCall");
        }

        DeleteCreatedAppointmentTicketRequest deleteCreatedAppointmentTicketRequest = new DeleteCreatedAppointmentTicketRequest.Builder(this)
                .setPriority(NetworkRequest.Priority.NORMAL)
                .setConcurrencyType(NetworkRequest.ConcurrencyType.SYNCHRONOUS)
                .setAppointmentTicketId(createdTicketAppointmentObj.getTicketAppointmentId())
                .setAppointmentTimeId(createdTicketAppointmentObj.getAppointmentTimeId())
                .setQueueId(createdAppointmentTimeObj.getQueueId())
                .build();
        NetworkUtils.queueNetworkRequest(deleteCreatedAppointmentTicketRequest);
        NetworkUtils.startNetworkService();
    }

    private void makeCreateAppointmentTicketCall(){

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "createTicketWebServiceCall");
        }

        if(!appointmentTimeAdapter.isTimeGrayed(selectedAppointmentTime)) {
            int quantity = apptSeekBar.getProgress()+1;

            Activity parentActivity = getActivity();
            if (parentActivity != null) {
                if(isChangeTime) {
                    makeDeleteAppointmentTicketCall();
                }
                progressDialog = ProgressDialog.show(parentActivity, getString(R.string.please_wait_message),
                        "", true);

                if(queueResultJson == null){
                    queueResultJson = GsonObject.fromJson(queueResultJsonStr, QueuesResult.class);
                }
                /* TODO: Create a virtual line manager class to handle requests for virtual lines */
                CreateAppointmentTimeRequest createAppointmentManager = new CreateAppointmentTimeRequest.Builder(this)
                        .setPriority(NetworkRequest.Priority.HIGH)
                        .setConcurrencyType(NetworkRequest.ConcurrencyType.SYNCHRONOUS)
                        .setAppointmentTimeId(selectedAppointmentTime.getId())
                        .setQuantity(quantity)
                        .setQueueEntityType(queueResultJson.getQueueEntityType())
                        .setImageUrl(thumbnailImageUrl)
                        .setParkName(parkName)
                        .setQueueResultJsonStr(queueResultJsonStr)
                        .setSelectedAppointmentTimeJson(selectedAppointmentTime.toJson())
                        .setQueueId(queueId)
                        .build();
                NetworkUtils.queueNetworkRequest(createAppointmentManager);
                NetworkUtils.startNetworkService();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOCATION_REQUEST_CODE) {
            if(!LocationUtils.isLocationEnabled()) {
                showLocationEnabledDialog();
            }
            else if(!UniversalAppStateManager.getInstance().isInResortGeofence()) {
                showNotInParkDialog();
            } else {
                appointmentTimeWebServiceCall();
            }
        }
    }

    private void showNotInParkDialog() {
        DialogUtils.showOneButtonMessageDialog(getActivity(),
                R.string.dpvq_location_out_of_range_title, R.string.dpvq_location_out_of_range_message,
                R.string.dpvq_location_ok, dialogClickListener);
    }

    private void showLocationEnabledDialog() {
        DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS), LOCATION_REQUEST_CODE);
            }
        };
        DialogUtils.showTwoButtonMessageDialog(getActivity(),
                R.string.dpvq_location_enable_location_title, R.string.dpvq_location_enable_location_message,
                R.string.dpvq_location_ok, okListener,
                R.string.dpvq_location_dont_allow, dialogClickListener);
    }

    private void showPermissionErrorDialog() {
        DialogUtils.showOneButtonMessageDialog(getActivity(),
                R.string.dpvq_location_permission_error_title, R.string.dpvq_location_permission_error_message,
                R.string.dpvq_location_ok, dialogClickListener);
    }

    private void setReserveButtonVisible(boolean visible) {
        reserveButton.setVisibility(((visible) ? View.VISIBLE : View.INVISIBLE));
    }
}
