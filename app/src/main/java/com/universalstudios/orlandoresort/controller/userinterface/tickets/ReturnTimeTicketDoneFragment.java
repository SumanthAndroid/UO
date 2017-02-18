package com.universalstudios.orlandoresort.controller.userinterface.tickets;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.wayfinding.WayfindingUtils;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.appointments.AppointmentTimes;
import com.universalstudios.orlandoresort.model.network.domain.appointments.CreateAppointmentTimeResponse;
import com.universalstudios.orlandoresort.model.network.domain.appointments.DeleteCreatedAppointmentTicketRequest;
import com.universalstudios.orlandoresort.model.network.domain.appointments.DeleteCreatedAppointmentTicketResponse;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.image.ImageUtils;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.request.RequestQueryParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;
import com.universalstudios.orlandoresort.view.fonts.Button;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * shows Appointment Ticket detail page. Can be reached through MyTickets feature of the drawer or
 * featuredetail fragment where we create appointment ticket
 * Created by GOKHAN on 7/27/2016.
 */
public class ReturnTimeTicketDoneFragment extends NetworkFragment implements ActionBarTitleProvider, View.OnClickListener{

    private static final String TAG = ReturnTimeTicketDoneFragment.class.getSimpleName();
    private static final String KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR = "KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR";
    private static final String KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR = "KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR";
    private static final String KEY_ARG_IS_HAS_BEEN_READ = "KEY_ARG_IS_HAS_BEEN_READ";

    private static final String KEY_ARG_CHANGE_TIME_BUNDLE = "KEY_ARG_CHANGE_TIME_BUNDLE";
    private static final String KEY_ARG_IS_CHANGE_TIME = "KEY_ARG_IS_CHANGE_TIME";
    private static final String KEY_ARG_SELECTED_POI_OBJECT_JSON = "KEY_ARG_SELECTED_POI_OBJECT_JSON";
    private static final String KEY_ARG_SELECTED_POI_TYPE_ID = "KEY_ARG_SELECTED_POI_TYPE_ID";

    private static final String TIME_FORMAT = "yyyy-MM-dd'T'hh:mm:sszzzz";

    public static final int CHANGE_TIME_REQUEST = 10011;

    private AppointmentTimes createdAppointmentTimeObj;
    private CreateAppointmentTimeResponse createdTicketAppointmentObj;
    private String queueDisplayName;
    private TextView ticketTitle, ticketDateTime,admitQuantityText;
    private String createdAppointmentTimeStr, createdTicketAppointmentStr;
    private Button deleteBtn;
    private ImageView backgroundImageView;
    private ImageView backgroundImageViewDefault;
    private TextView changeReturnTimeBtn;
    private TextView guideMeButton;
    private TextView ampmEndTimeTextView;
    private TextView ticketTime;
    private LinearLayout backgroundImageDefaultContainer;
    private String mActionBarTitleStr;
    private List<AppointmentTimes> appointmentTicketList;
    private CreateAppointmentTimeResponse appointmentTimeTicket;
    private  String mImageSizeParam;
    private  Picasso mPicasso;
    private  UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
    private Context context;
    // private SearchRowViewHolder holder;
    private static String barcode;
    private static Boolean hasBeenReadArg;
    ImageView thumbnailImage,thumbnailImageNoImage ;
    ImageView thumbnailBarcodeImage,thumbnailBarcodeImageNoImage;
    private LinearLayout appointmentTicketLayout;
    private TextView barcodeTextView;
    ProgressDialog progressDialog;
    private Bundle changeTimeBundle = null;
    private String poiJson;
    private int poiTypeId;
    private PointOfInterest poi;

    private static Boolean hasBeenRead;


    public static ReturnTimeTicketDoneFragment newInstance(String createdAppointmentTimeStr, String createdTicketAppointmentStr){
        ReturnTimeTicketDoneFragment fragment = new ReturnTimeTicketDoneFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR, createdAppointmentTimeStr);
        args.putString(KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR, createdTicketAppointmentStr);
        fragment.setArguments(args);
        return fragment;
    }

    public static ReturnTimeTicketDoneFragment newInstance(String createdAppointmentTimeStr, String createdTicketAppointmentStr, Boolean hasBeenRead){
        ReturnTimeTicketDoneFragment fragment = new ReturnTimeTicketDoneFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR, createdAppointmentTimeStr);
        args.putString(KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR, createdTicketAppointmentStr);
        args.putBoolean(KEY_ARG_IS_HAS_BEEN_READ, hasBeenRead);
        fragment.setArguments(args);
        return fragment;
    }

    public static ReturnTimeTicketDoneFragment newInstance(String createdAppointmentTimeStr, String createdTicketAppointmentStr, Boolean hasBeenRead, Bundle changeTimeArgs, String poiJson, int poiTypeId){
        ReturnTimeTicketDoneFragment fragment = new ReturnTimeTicketDoneFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR, createdAppointmentTimeStr);
        args.putString(KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR, createdTicketAppointmentStr);
        args.putBoolean(KEY_ARG_IS_HAS_BEEN_READ, hasBeenRead);
        args.putBundle(KEY_ARG_CHANGE_TIME_BUNDLE, changeTimeArgs);
        args.putString(KEY_ARG_SELECTED_POI_OBJECT_JSON, poiJson);
        args.putInt(KEY_ARG_SELECTED_POI_TYPE_ID, poiTypeId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        context = getActivity().getApplicationContext();

        // Create the image downloader to get the images
        mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
                CacheUtils.POI_IMAGE_DISK_CACHE_NAME,
                CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
                CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);
        mImageSizeParam = ImageUtils.getPoiImageSizeString(context.getResources().getInteger(R.integer.poi_search_image_dpi_shift));

        mPicasso = new Picasso.Builder(context)
                .loggingEnabled(UniversalOrlandoImageDownloader.SHOW_DEBUG)
                .downloader(mUniversalOrlandoImageDownloader)
                .build();

        mActionBarTitleStr = getResources().getString(R.string.title_virtual_line_pass);
        Bundle args = getArguments();
        if (args != null) {
            createdAppointmentTimeStr = args.getString(KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR);
            createdTicketAppointmentStr = args.getString(KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR);
            hasBeenRead = args.getBoolean(KEY_ARG_IS_HAS_BEEN_READ);

            createdAppointmentTimeObj = GsonObject.fromJson(createdAppointmentTimeStr, AppointmentTimes.class);
            createdTicketAppointmentObj = GsonObject.fromJson(createdTicketAppointmentStr, CreateAppointmentTimeResponse.class);
            changeTimeBundle = new Bundle(args.getBundle(KEY_ARG_CHANGE_TIME_BUNDLE));
            if(args.getBundle(KEY_ARG_CHANGE_TIME_BUNDLE) != null) {
                changeTimeBundle = new Bundle(args.getBundle(KEY_ARG_CHANGE_TIME_BUNDLE));
            }
            if (hasBeenRead) {
                updateAppointmentHasBeenReadInDatabase(createdTicketAppointmentObj, true, getActivity().getContentResolver(), true);
            }
            poiJson = args.getString(KEY_ARG_SELECTED_POI_OBJECT_JSON);
            poiTypeId = args.getInt(KEY_ARG_SELECTED_POI_TYPE_ID);
            poi = PointOfInterest.fromJson(poiJson, poiTypeId);

            Log.d(TAG,"Created Time: " + createdAppointmentTimeObj.toJson() + "/n" +
                    "  Created Ticket:" + createdTicketAppointmentObj.toJson());
        }


        // If this is the first creation, default state variables
        if (savedInstanceState == null) {}
        // Otherwise restore state, overwriting any passed in parameters
        else {}


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onActivityCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
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
        }

        View view = inflater.inflate(R.layout.fragment_ticket_appointment_details_dpvq, container, false);
        ticketTitle = (TextView)view.findViewById(R.id.ticket_appointment_ticket_name);
        ticketDateTime = (TextView)view.findViewById(R.id.ticket_appointment_ticket_date);
        ampmEndTimeTextView = (TextView)view.findViewById(R.id.ticket_appointment_subtitle_time_am_pm);
        changeReturnTimeBtn = (TextView) view.findViewById(R.id.ticket_appointment_dpvq_change_return_time);
        guideMeButton = (TextView)view.findViewById(R.id.ticket_appointment_dpvq_guide_me);
        admitQuantityText = (TextView)view.findViewById(R.id.ticket_appointment_admission_quantity);
        barcodeTextView = (TextView)view.findViewById(R.id.ticket_appointment_ticket_barcode_code);
        ticketTime = (TextView)view.findViewById(R.id.ticket_appointment_ticket_time);

        thumbnailBarcodeImageNoImage = (ImageView)view.findViewById(R.id.list_appointment_ticket_barcode_no_image_logo);
        thumbnailBarcodeImage = (ImageView)view.findViewById(R.id.list_appointment_ticket_barcode_image);

        backgroundImageView = (ImageView)view.findViewById(R.id.ticket_appointment_background);
        backgroundImageViewDefault = (ImageView)view.findViewById(R.id.ticket_appointment_default_background);
        backgroundImageDefaultContainer = (LinearLayout)view.findViewById(R.id.ticket_appointment_default_background_container);
        downloadBarcodeImage();

        guideMeButton.setOnClickListener(this);
        changeReturnTimeBtn.setOnClickListener(this);

        if(createdTicketAppointmentObj.getTicketDisplayName() == null || createdTicketAppointmentObj.getTicketDisplayName().equals("")) {
            queueDisplayName = createdAppointmentTimeObj.getTicketDisplayName();
        }else{
            queueDisplayName = createdTicketAppointmentObj.getTicketDisplayName();
        }

        if(!poi.getIsRoutable()) {
            guideMeButton.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) changeReturnTimeBtn.getLayoutParams();
            params.weight = 2;
            changeReturnTimeBtn.setLayoutParams(params);
        }

        setHasOptionsMenu(true);

        String formatDateStr = formatDate(createdAppointmentTimeObj.getDate());
        ticketTitle.setText(queueDisplayName);
        ticketDateTime.setText( formatDateStr);
        barcodeTextView.setText(barcode);

        ticketTime.setText(getAppointmentTimeText());


        String quantity = String.valueOf(createdTicketAppointmentObj.getQuantity());
        admitQuantityText.setText(getString(R.string.return_time_ticket_done_admit_quantity_format, quantity));

        if(poi.getQueueImage() != null) {
            mPicasso.load(Uri.parse(poi.getQueueImage())).into(backgroundImageView);
            backgroundImageDefaultContainer.setVisibility(View.INVISIBLE);
        } else if(poi.getListImageUrl() != null) {
            mPicasso.load(Uri.parse(poi.getListImageUrl())).into(backgroundImageViewDefault);
            backgroundImageView.setVisibility(View.INVISIBLE);
        }

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* TODO: Make a virtual queue manager class to handle the queues */
        if(requestCode == CHANGE_TIME_REQUEST && resultCode == Activity.RESULT_OK) {
            Bundle args = data.getExtras();
            createdAppointmentTimeStr = args.getString(KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR);
            createdTicketAppointmentStr = args.getString(KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR);

            createdAppointmentTimeObj = GsonObject.fromJson(createdAppointmentTimeStr, AppointmentTimes.class);
            createdTicketAppointmentObj = GsonObject.fromJson(createdTicketAppointmentStr, CreateAppointmentTimeResponse.class);

            /* 'Refresh' the UI elements that have changed */
            downloadBarcodeImage();

            String formatDateStr = formatDate(createdAppointmentTimeObj.getDate());
            ticketTitle.setText(queueDisplayName);
            ticketDateTime.setText(formatDateStr);
            barcodeTextView.setText(barcode);

            ticketTime.setText(getAppointmentTimeText());

            String quantity = String.valueOf(createdTicketAppointmentObj.getQuantity());
            admitQuantityText.setText(getString(R.string.return_time_ticket_done_admit_quantity_format, quantity));
        }
    }

    public void downloadTicketImage(){


        String thumbnailImageUrl = createdTicketAppointmentObj.getImageUrl();

        // Load the search image
        if (thumbnailImageUrl != null && !thumbnailImageUrl.isEmpty()) {

            Uri thumbnailImageUri = null;
            try {
                thumbnailImageUri = Uri.parse(thumbnailImageUrl);
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "bindView: invalid image URL: " + thumbnailImageUri, e);
                }
                // Log the exception to crittercism
                Crittercism.logHandledException(e);
            }

            if (thumbnailImageUri != null) {
                Uri imageUriToLoad = thumbnailImageUri.buildUpon()
                        .appendQueryParameter(RequestQueryParams.Keys.IMAGE_SIZE, mImageSizeParam)
                        .build();
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "bindView: imageUriToLoad = " + imageUriToLoad);
                }
                mPicasso.load(imageUriToLoad).into(thumbnailImage, new BarcodeImageCallback(thumbnailImage, thumbnailImageNoImage));

            }
        }
        downloadBarcodeImage();
    }

    public void downloadBarcodeImage(){
        String barcodeUrl = "https://services-prod.ucdp.net/webdata/CodeGenerator/QRCodeGen/?factor=6&textToEncode=";
        barcode = createdTicketAppointmentObj.getBarcode();
        String thumbnailImageUrl = barcodeUrl + barcode;

        // Load the search image
        if (thumbnailImageUrl != null && !thumbnailImageUrl.isEmpty()) {

            Uri thumbnailImageUri = null;
            try {
                thumbnailImageUri = Uri.parse(thumbnailImageUrl);
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "bindView: invalid image URL: " + thumbnailImageUri, e);
                }
                // Log the exception to crittercism
                Crittercism.logHandledException(e);
            }

            if (thumbnailImageUri != null) {
                Uri imageUriToLoad = thumbnailImageUri.buildUpon()
                        .appendQueryParameter(RequestQueryParams.Keys.IMAGE_SIZE, mImageSizeParam)
                        .build();
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "bindView: imageUriToLoad = " + imageUriToLoad);
                }
                mPicasso.load(imageUriToLoad).into(thumbnailBarcodeImage, new BarcodeImageCallback(thumbnailBarcodeImage, thumbnailBarcodeImageNoImage));

            }
        }
    }


    // Private static class using weak references to prevent leaking a context
    private static final class BarcodeImageCallback implements com.squareup.picasso.Callback {
        private final WeakReference<ImageView> mImage;
        private final WeakReference<ImageView> mNoImage;

        public BarcodeImageCallback(ImageView image, ImageView noImage){
            mImage = new WeakReference<ImageView>(image);
            mNoImage = new WeakReference<ImageView>(noImage);
        }

        @Override
        public void onSuccess() {

            ImageView image = mImage.get();
            ImageView noImage = mNoImage.get();

            if (image != null) {
                image.setVisibility(View.VISIBLE);
            }
            if (noImage != null) {
                noImage.setVisibility(View.GONE);
            }
        }

        @Override
        public void onError() {
            ImageView image = mImage.get();
            ImageView noImage = mNoImage.get();

            if (image != null) {
                image.setVisibility(View.GONE);
            }
            if (noImage != null) {
                noImage.setVisibility(View.VISIBLE);
            }
        }
    }

    //format date to OCT 30, 2016
    private String formatDate(String dateStr) {

        String dateSubStr = dateStr.substring(0, dateStr.length()-3);
        String dateStrLast = dateStr.substring(dateStr.length()-2);

        dateStr = dateSubStr + dateStrLast;

        String subDatestr = dateStr.substring(dateStr.length()-4);
        dateStr = dateStr.substring(0, dateStr.length()-5) + ".000" + "-" + subDatestr;

        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        String outputPattern = "EEE, MMM d, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date;
        String str =null;

        try {
            date = inputFormat.parse(dateStr);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private String getAppointmentTimeText(){
        String appointmentTimeItem = "";
        if (!createdAppointmentTimeObj.getStartTime().isEmpty()){
            String startTime = createdAppointmentTimeObj.getStartTime();
            if(!createdAppointmentTimeObj.getEndTime().isEmpty()){
                String endTime = createdAppointmentTimeObj.getEndTime();
                if(endTime != null) {
                    ampmEndTimeTextView.setText(getAmPmText(endTime));
                }
                /* This sets the time range without the AM/PM text appended at the end */
                appointmentTimeItem = startTime.substring(0,startTime.length()-3) + "-" + endTime.substring(0,endTime.length()-3);
            }
        }
        return appointmentTimeItem;
    }


    @Override
    public String provideTitle() {
        if (mActionBarTitleStr == null || mActionBarTitleStr.isEmpty()) {
            return "";
        }
        return mActionBarTitleStr;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateOptionsMenu");
        }

        inflater.inflate(R.menu.action_return_time_ticket_detail, menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_return_time_ticket_done_id:
                setDeleteDialog();
                return true;
            default:
                return (super.onOptionsItemSelected(item));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ticket_appointment_dpvq_guide_me:
                WayfindingUtils.openWayfindingPage(v.getContext(), poiJson, poiTypeId);
                break;
            case R.id.ticket_appointment_dpvq_change_return_time:
                startChangeTimeActivity();
                break;
            default:
                break;
        }
    }

    private void setDeleteDialog(){

        AlertDialog deleteDialog =    new AlertDialog.Builder(getActivity())
                .setTitle(R.string.virtual_line_delete_return_time_title)
                .setMessage(R.string.virtual_line_delete_return_time_message)
                .setPositiveButton(getResources().getString(R.string.dialog_delete_appointment_ticket_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with create
                        makeDeleteAppointmentTicketCall();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        android.widget.Button positiveButton = deleteDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.dialog_button_red));
        deleteDialog.show();
    }

    private void makeDeleteAppointmentTicketCall(){
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeDeleteAppointmentTicketCall");
        }
        progressDialog = ProgressDialog.show(getActivity(), "Please Wait...",
                "", true);

        DeleteCreatedAppointmentTicketRequest deleteCreatedAppointmentTicketRequest = new DeleteCreatedAppointmentTicketRequest.Builder(this)
                .setPriority(NetworkRequest.Priority.NORMAL)
                .setConcurrencyType(NetworkRequest.ConcurrencyType.SYNCHRONOUS)
                .setAppointmentTicketId(createdTicketAppointmentObj.getTicketAppointmentId())
                .setAppointmentTimeId(createdTicketAppointmentObj.getAppointmentTimeId())
                .setQueueId(createdAppointmentTimeObj.getQueueId())
                .build();
        NetworkUtils.queueNetworkRequest(deleteCreatedAppointmentTicketRequest);
        NetworkUtils.startNetworkService();

        if (getActivity().getParent() == null) {
            getActivity().setResult(Activity.RESULT_OK);
        }
        else {
            getActivity().getParent().setResult(Activity.RESULT_OK);
        }
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "handleNetworkResponse");
        }

        if(networkResponse instanceof DeleteCreatedAppointmentTicketResponse){
            progressDialog.dismiss();
            if(hasBeenRead) {
                UserInterfaceUtils.showToastFromForeground("Ticket Deleted!", Toast.LENGTH_SHORT, getActivity());
                finishActivity();
            }else{
                UserInterfaceUtils.showToastFromForeground("Ticket Deleted!", Toast.LENGTH_SHORT, getActivity());
                finishActivity();
            }
        }
    }
    public void startChangeTimeActivity() {
        changeTimeBundle.putString(KEY_ARG_CREATED_APPOINTMENT_TIME_JSON_STR, createdAppointmentTimeStr);
        changeTimeBundle.putString(KEY_ARG_CREATED_TICKET_APPOINTMENT_JSON_STR, createdTicketAppointmentStr);
        changeTimeBundle.putBoolean(KEY_ARG_IS_CHANGE_TIME, true);

        startActivityForResult(new Intent(getActivity(), QueueTicketingAppointmentActivity.class)
                .putExtras(changeTimeBundle), CHANGE_TIME_REQUEST);
    }

    private void finishActivity() {
        Activity parentActivity = getActivity();
        if (parentActivity != null) {
            parentActivity.finish();
            parentActivity.setResult(Activity.RESULT_OK);
        }
    }

    private void invalidateOptionsMenu() {
        Activity parentActivity = getActivity();
        if (parentActivity != null) {
            parentActivity.invalidateOptionsMenu();
        }
    }


    public static void updateAppointmentHasBeenReadInDatabase(final CreateAppointmentTimeResponse appointmentTimeTicket,
                                                              boolean hasBeenRead, final ContentResolver contentResolver,
                                                              boolean async) {
        if (contentResolver == null || appointmentTimeTicket == null) {
            return;
        }

        // Update the appointment ticket object
        appointmentTimeTicket.setHasBeeenRead(hasBeenRead);

        final ContentValues contentValues = new ContentValues();
        contentValues.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_HAS_BEEN_READ, appointmentTimeTicket.isHasBeeenRead());
        contentValues.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_APPOINTMENT_TICKET_OBJECT_JSON, appointmentTimeTicket.toJson());

        final String where = new StringBuilder(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_TICKET_APPOINTMENT_ID)
                .append(" = ").append(appointmentTimeTicket.getTicketAppointmentId())
                .toString();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // Try to update the item
                    int itemsUpdated = contentResolver.update(UniversalOrlandoContentUris.TICKET_APPOINTMENTS, contentValues, where, null);

                    // If the item doesn't exist, log it
                    if (itemsUpdated == 0) {
                        if (BuildConfig.DEBUG) {
                            Log.w(TAG, "updateAppointmentHasBeenReadInDatabase: Appointment item does no exist in the database");
                        }
                    }
                }
                catch (Exception e) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "updateAppointmentHasBeenReadInDatabase: exception saving to database", e);
                    }

                    // Log the exception to crittercism
                    Crittercism.logHandledException(e);
                }
            }
        };

        // If requested, run asynchronously
        if (async) {
            new Thread(runnable).start();
        }
        // Otherwise, run synchronously
        else {
            runnable.run();
        }
    }

    private String getAmPmText(String time) {
        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
            int amPm = c.get(Calendar.AM_PM);
            if(amPm == Calendar.AM) {
                return getString(R.string.am);
            } else {
                return getString(R.string.pm);
            }
        } catch(ParseException e) {
            if(BuildConfig.DEBUG) {
                Log.e(TAG, "parse exception", e);
            }
        }
        return "";
    }
}