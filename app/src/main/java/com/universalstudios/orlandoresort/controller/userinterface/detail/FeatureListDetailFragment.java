package com.universalstudios.orlandoresort.controller.userinterface.detail;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.alerts.AlertsUtils;
import com.universalstudios.orlandoresort.controller.userinterface.alerts.SetAlertActivity;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.LoaderUtils;
import com.universalstudios.orlandoresort.controller.userinterface.events.EventUtils;
import com.universalstudios.orlandoresort.controller.userinterface.general.BasicInfoDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.photoframe.PhotoFrameActivity;
import com.universalstudios.orlandoresort.controller.userinterface.pois.PoiUtils;
import com.universalstudios.orlandoresort.controller.userinterface.tickets.QueueTicketingAppointmentActivity;
import com.universalstudios.orlandoresort.controller.userinterface.tickets.ReturnTimeTicketDoneActivity;
import com.universalstudios.orlandoresort.controller.userinterface.web.WebViewActivity;
import com.universalstudios.orlandoresort.model.alerts.RideOpenAlert;
import com.universalstudios.orlandoresort.model.alerts.WaitTimeAlert;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.appointments.AppointmentTimes;
import com.universalstudios.orlandoresort.model.network.domain.appointments.GetAppointmentTimesResponse;
import com.universalstudios.orlandoresort.model.network.domain.appointments.queues.QueuesResult;
import com.universalstudios.orlandoresort.model.network.domain.events.EventSeries;
import com.universalstudios.orlandoresort.model.network.domain.offers.Offer;
import com.universalstudios.orlandoresort.model.network.domain.photoframes.PhotoFrameExperience;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Dimensions;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Dining;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Entertainment;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Event;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Gateway;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Hotel;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Lockers;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PricesProvider;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.RentalServices;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Ride;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Shop;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Show;
import com.universalstudios.orlandoresort.model.network.domain.venue.Venue;
import com.universalstudios.orlandoresort.model.network.domain.venue.VenueHours;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.AlertsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventSeriesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OffersTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.QueuesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.TicketsAppointmentTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.model.state.OnUniversalAppStateChangeListener;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;
import com.universalstudios.orlandoresort.view.TintUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 *
 * @author Steven Byle
 */
public class FeatureListDetailFragment extends DatabaseQueryFragment implements OnClickListener,
        OnLongClickListener, OnUniversalAppStateChangeListener {

    private static final String TAG = FeatureListDetailFragment.class.getSimpleName();

    private static final String KEY_ARG_POI_ID = "KEY_ARG_POI_ID";
    private static final String KEY_ARG_IS_SECONDARY = "KEY_ARG_IS_SECONDARY";
    private static final String KEY_LOADER_ARG_DATABASE_QUERY_JSON = "KEY_LOADER_ARG_DATABASE_QUERY_JSON";

    private static final String VIEW_TAG_WAIT_TIME_ALERT = "VIEW_TAG_WAIT_TIME_ALERT";
    private static final String VIEW_TAG_RIDE_OPEN_ALERT = "VIEW_TAG_RIDE_OPEN_ALERT";
    private static final String VIEW_TAG_EXPRESS_PASS = "VIEW_TAG_EXPRESS_PASS";
    private static final String VIEW_TAG_SINGLE_RIDER = "VIEW_TAG_SINGLE_RIDER";
    private static final String VIEW_TAG_CHILD_SWAP = "VIEW_TAG_CHILD_SWAP";
    private static final String VIEW_TAG_GATEWAY_TYPE_DISPLAYED = "VIEW_TAG_GATEWAY_TYPE_DISPLAYED";
    private static final String VIEW_TAG_DINING_PLAN = "VIEW_TAG_DINING_PLAN";
    private static final String VIEW_TAG_PHONE_NUMBER = "VIEW_TAG_PHONE_NUMBER";
    private static final String VIEW_TAG_VIEW_MENU = "VIEW_TAG_VIEW_MENU";
    private static final String VIEW_TAG_PRICES = "VIEW_TAG_PRICES";
    private static final String VIEW_TAG_PARTY_PASS = "VIEW_TAG_PARTY_PASS";
    private static final String VIEW_TAG_SUB_TYPE = "VIEW_TAG_SUB_TYPE";
    private static final String VIEW_TAG_EXPRESS_EXPRESS_PASS_SOLD_HERE = "VIEW_TAG_EXPRESS_EXPRESS_PASS_SOLD_HERE";
    private static final String VIEW_TAG_NOMINAL_FEE = "VIEW_TAG_NOMINAL_FEE";
    private static final String VIEW_TAG_SUPER_THRILL = "VIEW_TAG_SUPER_THRILL";
    private static final String VIEW_TAG_GROUP_THRILL = "VIEW_TAG_GROUP_THRILL";
    private static final String VIEW_TAG_FAMILY_THRILL = "VIEW_TAG_FAMILY_THRILL";
    private static final String VIEW_TAG_EXTRAS = "VIEW_TAG_EXTRAS";
    private static final String VIEW_TAG_EVENT_DATES = "VIEW_TAG_EVENT_DATES";
    private static final String VIEW_TAG_EVENT_TIMES = "VIEW_TAG_EVENT_TIMES";
    private static final String VIEW_TAG_EVENT_LOCATION = "VIEW_TAG_EVENT_LOCATION";
    private static final String VIEW_TAG_EVENT_TICKET = "VIEW_TAG_EVENT_TICKET";
    private static final String VIEW_TAG_EVENT_EVENT_SERIES = "VIEW_TAG_EVENT_EVENT_SERIES";
    private static final String VIEW_TAG_EVENT_SERIES_DISCLAIMER = "VIEW_TAG_EVENT_SERIES_DISCLAIMER";
    private static final String VIEW_TAG_OFFER = "VIEW_TAG_OFFER";
    private static final String VIEW_TAG_PACKAGE_PICKUP = "VIEW_TAG_PACKAGE_PICKUP";
    private static final String VIEW_TAG_COKE_FREESTYLE = "VIEW_TAG_COKE_FREESTYLE";
    private static final String VIEW_TAG_EVC_RENTAL_TYPE = "VIEW_TAG_EVC_RENTAL_TYPE";
    private static final String VIEW_TAG_STROLLER_RENTAL_TYPE = "VIEW_TAG_STROLLER_RENTAL_TYPE";
    private static final String VIEW_TAG_WHEEL_CHAIR_RENTAL_TYPE = "VIEW_TAG_WHEELCHAIR_RENTAL_TYPE";
    private static final String VIEW_TAG_RENTAL_PRICES = "VIEW_TAG_RENTAL_PRICES";
    private static final String VIEW_TAG_PHOTO_FRAMES = "VIEW_TAG_PHOTO_FRAMES";
    private static final String VIEW_TAG_QUEUES = "VIEW_TAG_QUEUES";
    private static final String VIEW_TAG_APPOINTMENT_TICKET = "VIEW_TAG_APPOINTMENT_TICKET";
    private static final String KEY_REFRESH_FRAGMENT = "KEY_REFRESH_FRAGMENT";
    private static  boolean refreshFrag;


    // The loader's unique id. Loader ids are specific to the Activity or
    // Fragment in which they reside.
    private static final int LOADER_ID_ALERTS = LoaderUtils.LOADER_ID_SHOW_TIME_LIST_DETAIL_FRAGMENT;
    private static final int LOADER_ID_EVENT_SERIES = LoaderUtils.LOADER_ID_FEATURE_LIST_DETAIL_FRAGMENT_EVENT_SERIES;
    private static final int LOADER_ID_OFFERS = LoaderUtils.LOADER_ID_FEATURE_LIST_DETAIL_FRAGMENT_OFFERS;
    private static final int LOADER_ID_PHOTOFRAME_EXPERIENCE = LoaderUtils.LOADER_ID_PHOTOFRAME_EXPERIENCE;
    private static final int LOADER_ID_QUEUE = LoaderUtils.LOADER_ID_QUEUE;
    private static final int LOADER_ID_APPOINTMENT_TICKETS = LoaderUtils.LOADER_ID_APPOINTMENT_TICKETS;

    private static final int REQUEST_CODE_APPOINTMENTS = 10001;

    // Case-insensitive check for URL ending with '.pdf'
    private static final String MENU_PDF_REGEX = "(?i).+\\.pdf$";

    private LinearLayout mFeatureListLayout, mFeatureListTicketLayout;
    private View mFeatureListHeader, mFeatureTicketHeader;
    private TextView mFeatureListHeaderText, mFeatureTicketHeaderText;
    private View mSetWaitTimeAlertView;
    private View mSetRideOpenAlertView;
    private OnFeatureListStateChangeListener mParentOnFeatureListStateChangeListener;
    private Long mPoiId;
    private Boolean mIsSecondary;
    private String mPoiName;
    private Integer mWaitTime;
    private List<VenueHours> mVenueHours;
    private Integer mNotifyThresholdInMin;
    private boolean mIsRideOpenAlertSet;
    private PointOfInterest mPoi;
    private boolean mHasLoadedFeatures;
    private long queueEntityId;
    private long queueId;
    private String queueResultJson;

    private GetAppointmentTimesResponse getAppointmentTimesResponse;
    private List<AppointmentTimes> appointmentTimes;
    private boolean appointmentTimesReqCompleted;

    private String storedAppointmentTime,storedAppointmentTicketResponse;
    private static boolean isQueueCalled;
    private String venueDisplayName;
private boolean refreshFragment;
    private ViewGroup mFeatureListFragmentContainer;

    public static FeatureListDetailFragment newInstance(DatabaseQuery databaseQuery, long poiId,
                                                        boolean isSecondary) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance: databaseQuery = " + databaseQuery);
        }

        // Create a new fragment instance
        FeatureListDetailFragment fragment = new FeatureListDetailFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        // Add parameters to the argument bundle
        if (databaseQuery != null) {
            args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
        }
        args.putLong(KEY_ARG_POI_ID, poiId);
        args.putBoolean(KEY_ARG_IS_SECONDARY, isSecondary);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ARG_POI_ID, mPoiId);
        outState.putBoolean(KEY_ARG_IS_SECONDARY, mIsSecondary);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAttach");
        }

        // Check if parent fragment (if there is one) implements the interface
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof OnFeatureListStateChangeListener) {
            mParentOnFeatureListStateChangeListener = (OnFeatureListStateChangeListener) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (activity != null && activity instanceof OnFeatureListStateChangeListener) {
            mParentOnFeatureListStateChangeListener = (OnFeatureListStateChangeListener) activity;
        }
        // If neither implements the image selection callback, warn that
        // selections are being missed
        else if (mParentOnFeatureListStateChangeListener == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG,
                        "onAttach: neither the parent fragment or parent activity implement OnFeatureListStateChangeListener");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        // Default parameters
        Bundle args = getArguments();
        if (args == null) {
            mPoiId = null;
            mIsSecondary = null;
        }
        // Otherwise, set incoming parameters
        else {
            mPoiId = args.getLong(KEY_ARG_POI_ID);
            mIsSecondary = args.getBoolean(KEY_ARG_IS_SECONDARY);
        }
        mVenueHours = null;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }

        // Inflate the fragment layout into the container
        View fragmentView = inflater.inflate(R.layout.fragment_detail_feature_list, container, false);

        // Setup Views
        mFeatureListLayout = (LinearLayout) fragmentView
                .findViewById(R.id.fragment_detail_feature_list_layout);

        //setup views for tickets
        mFeatureListTicketLayout = (LinearLayout) getActivity().findViewById(R.id.fragment_detail_feature_list_ticket_layout);

        mFeatureListHeader = fragmentView.findViewById(R.id.fragment_detail_feature_list_header);
        mFeatureListHeaderText = (TextView) fragmentView
                .findViewById(R.id.fragment_detail_feature_list_header_text);

        mFeatureTicketHeader = getActivity().findViewById(R.id.fragment_detail_feature_ticket_header);
        mFeatureTicketHeaderText = (TextView) getActivity().findViewById(R.id.fragment_detail_feature_ticket_header_text);

        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onActivityResult: savedInstanceState ");
        }
        if ((requestCode == 10001)) {

            // recreate the fragment here
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onActivityCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }

        if(savedInstanceState != null){
            mPoiId = savedInstanceState.getLong(KEY_ARG_POI_ID);
            mIsSecondary = savedInstanceState.getBoolean(KEY_ARG_IS_SECONDARY);
        }

            Bundle queueArgs = new Bundle();
            DatabaseQuery queueDatabaseQuery = DatabaseQueryUtils.getQueuesByIdDatabaseQuery(mPoiId);
            queueArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, queueDatabaseQuery.toJson());
            getLoaderManager().initLoader(LOADER_ID_QUEUE, queueArgs, this);


        // Create loader that fetches both wait time and opening alerts set for this POI
        Bundle loaderArgs = new Bundle();
        int[] alertTypeIdList = { AlertsTable.VAL_ALERT_TYPE_ID_WAIT_TIME,
                AlertsTable.VAL_ALERT_TYPE_ID_RIDE_OPEN };
        DatabaseQuery databaseQuery = DatabaseQueryUtils
                .getAlertsForPoiDatabaseQuery(mPoiId, alertTypeIdList);
        loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
        getLoaderManager().initLoader(LOADER_ID_ALERTS, loaderArgs, this);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onResume");
        }

        // Update the views based on the geofence
        updateViewsBasedOnGeofenceAndWaitTime();

        // Listen for state changes
        UniversalAppStateManager.registerStateChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onPause");
        }

        // Stop listening for state changes
        UniversalAppStateManager.unregisterStateChangeListener(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateLoader: id = " + id);
        }


        switch (id) {
            case LOADER_ID_OFFERS:
            case LOADER_ID_EVENT_SERIES:
            case LOADER_ID_ALERTS:
            case LOADER_ID_PHOTOFRAME_EXPERIENCE:
            case LOADER_ID_QUEUE:
            case LOADER_ID_APPOINTMENT_TICKETS:
                String databaseQueryJson = args.getString(KEY_LOADER_ARG_DATABASE_QUERY_JSON);
                DatabaseQuery databaseQuery = GsonObject.fromJson(databaseQueryJson, DatabaseQuery.class);
                return LoaderUtils.createCursorLoader(databaseQuery);

            default:
                // Otherwise, let the parent class handle it
                return super.onCreateLoader(id, args);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onLoadFinished: loader.getId() = " + loader.getId());
        }

        switch (loader.getId()) {
            case LOADER_ID_OFFERS:
                if (data != null && data.moveToFirst()) {
                    List<Offer> offers = new ArrayList<Offer>();
                    do {
                        String offerObjectJson = data.getString(data
                                .getColumnIndex(OffersTable.COL_OFFER_OBJECT_JSON));
                        Offer offer = GsonObject.fromJson(offerObjectJson, Offer.class);
                        if (offer != null) {
                            offers.add(offer);
                        }
                    } while (data.moveToNext());
                    // Add offer features
                    if (mPoi != null) {
                        if (mPoi instanceof Shop) {
                            addShoppingOfferFeatures(offers);
                        } else if (mPoi instanceof Dining) {
                            addDiningOfferFeatures(offers);
                        }
                    }
                    loader.stopLoading();
                }
                break;
            case LOADER_ID_EVENT_SERIES:
                if (data != null && data.moveToFirst()) {
                    List<EventSeries> eventSeriesList = new ArrayList<EventSeries>();
                    do {
                        String eventSeriesObjectJson = data.getString(data
                                .getColumnIndex(EventSeriesTable.COL_EVENT_SERIES_OBJECT_JSON));
                        EventSeries eventSeries = GsonObject.fromJson(eventSeriesObjectJson,
                                EventSeries.class);
                        // Only add link if event series has more than 1 event
                        if (eventSeries != null && eventSeries.getEvents() != null
                                && eventSeries.getEvents().size() > 1) {
                            eventSeriesList.add(eventSeries);
                        }
                    } while (data.moveToNext());
                    addEventSeriesFeatures(eventSeriesList);
                    loader.stopLoading();
                }
                break;
            case LOADER_ID_DATABASE_QUERY:
                // Let the parent class check for double loads
                if (checkIfDoubleOnLoadFinished()) {
                    if (BuildConfig.DEBUG) {
                        Log.i(TAG, "onLoadFinished: ignoring update due to double load");
                    }
                    return;
                }

                if (!mHasLoadedFeatures) {

                    // Clear the feature options and load new ones
                    mFeatureListLayout.removeAllViews();

                    // Load in the POI data
                    if (data != null && data.moveToFirst()) {
                        String poiObjectJson = data.getString(data
                                .getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
                        Integer poiTypeId = data.getInt(data
                                .getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
                        mPoiName = data.getString(data
                                .getColumnIndex(PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME));
                        mWaitTime = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_WAIT_TIME));
                        String venueObjectJson = data.getString(data
                                .getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));

                        mPoi = PointOfInterest.fromJson(poiObjectJson, poiTypeId);
                        mPoi.setPoiTypeId(poiTypeId);

                        Venue venue = GsonObject.fromJson(venueObjectJson, Venue.class);
                        mVenueHours = venue.getHours();
                        venueDisplayName = venue.getDisplayName();

                        if (mPoi != null) {
                            // Add features based on the POI type
                            switch (poiTypeId) {
                                case PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE:
                                    addRideFeatures((Ride) mPoi);
                                    break;
                                case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW:
                                case PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE:
                                    addShowFeatures((Show) mPoi);
                                    break;
                                case PointsOfInterestTable.VAL_POI_TYPE_ID_DINING:
                                    addDiningFeatures((Dining) mPoi);
                                    // Load offers for dining
                                    loadOffers(mPoi);
                                    break;
                                case PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT:
                                    addEntertainmentFeatures((Entertainment) mPoi);
                                    break;
                                case PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL:
                                    addHotelFeatures((Hotel) mPoi, venue);
                                    break;
                                case PointsOfInterestTable.VAL_POI_TYPE_ID_WATERPARK:
                                    addParksFeatures(mPoi, venue);
                                    break;
                                case PointsOfInterestTable.VAL_POI_TYPE_ID_LOCKERS:
                                    addLockerFeatures((Lockers) mPoi);
                                    break;
                                case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP:
                                    addShoppingFeatures((Shop) mPoi);
                                    // Load offers for shopping
                                    loadOffers(mPoi);
                                    break;
                                case PointsOfInterestTable.VAL_POI_TYPE_ID_RENTALS:
                                    addRentalTypeFeatures((RentalServices) mPoi);
                                    // Load offers for rental types
                                    loadOffers(mPoi);
                                    break;
                                case PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT:
                                    Event event = (Event) mPoi;
                                    addEventFeatures(event);

                                    // Create loader to fetch the event series linked to this event
                                    Bundle loaderArgs = new Bundle();
                                    DatabaseQuery databaseQuery = DatabaseQueryUtils
                                            .getEventSeriesByIdDatabaseQuery(event.getEventSeriesIds());
                                    loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON,
                                            databaseQuery.toJson());
                                    getLoaderManager().initLoader(LOADER_ID_EVENT_SERIES, loaderArgs, this);
                                    break;
                                case PointsOfInterestTable.VAL_POI_TYPE_ID_GATEWAY:
                                    Gateway gateway = (Gateway) mPoi;
                                    addGatewayFeatures(gateway);
                                default:
                                    break;
                            }

                            Bundle photoFrameArgs = new Bundle();

                            List<Long> tempIds = mPoi.getPhotoFrameExperienceIds();
                            if (null != tempIds && !tempIds.isEmpty()) {
                                DatabaseQuery photoFrameExperienceDatabaseQuery = DatabaseQueryUtils.getPhotoFrameExperienceDatabaseQuery(tempIds);
                                photoFrameArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, photoFrameExperienceDatabaseQuery.toJson());

                                getLoaderManager().initLoader(LOADER_ID_PHOTOFRAME_EXPERIENCE, photoFrameArgs, this);
                            }
                            mHasLoadedFeatures = true;
                        }
                    }
                }

                // Update any views that rely on the geofence or wait time
                updateViewsBasedOnGeofenceAndWaitTime();

                // Update any other listeners of the latest state
                informFeatureListStateChangeListeners();

                break;
            case LOADER_ID_PHOTOFRAME_EXPERIENCE:
                List<PhotoFrameExperience> photoFrameExperiences = new ArrayList<>();
                if (null != data && data.moveToFirst()) {
                    do {
                        String expJson = data.getString(data.getColumnIndexOrThrow(UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.COL_PHOTOFRAME_EXPERIENCE_JSON));
                        if (!TextUtils.isEmpty(expJson)) {
                            PhotoFrameExperience experience = PhotoFrameExperience.fromJson(expJson, PhotoFrameExperience.class);
                            photoFrameExperiences.add(experience);
                        }
                    } while(data.moveToNext());
                }

                addPhotoFrameExperienceFeatures(photoFrameExperiences);
                break;
            case LOADER_ID_ALERTS:

                // Assume neither alert is set
                mNotifyThresholdInMin = null;
                mIsRideOpenAlertSet = false;

                // Load in the wait time alert for the POI
                if (data != null && data.moveToFirst()) {
                    do {
                        int alertTypeId = data.getInt(data.getColumnIndex(AlertsTable.COL_ALERT_TYPE_ID));
                        switch (alertTypeId) {
                            case AlertsTable.VAL_ALERT_TYPE_ID_WAIT_TIME:
                                mNotifyThresholdInMin = data.getInt(data
                                        .getColumnIndex(AlertsTable.COL_NOTIFY_THRESHOLD_IN_MIN));
                                break;
                            case AlertsTable.VAL_ALERT_TYPE_ID_RIDE_OPEN:
                                mIsRideOpenAlertSet = true;
                                break;
                        }
                    } while (data.moveToNext());
                }

                // Update the wait time alert view to show the current set alert
                updateSetAlertViews();

                break;

            case LOADER_ID_QUEUE:

                if(data != null && data.moveToFirst()){
                    do {
                        queueEntityId = data.getLong(data.getColumnIndex(QueuesTable.COL_QUEUE_ENTITY_ID));
                        queueId = data.getLong(data.getColumnIndex(QueuesTable.COL_QUEUE_ID));
                        queueResultJson = data.getString(data.getColumnIndex(QueuesTable.COL_QUEUE_OBJECT_JSON));

                       Log.e(TAG, "queue entity id: " + String.valueOf(queueEntityId));
                    } while (data.moveToNext());
                }

                Bundle appointmentTicketArgs = new Bundle();
                DatabaseQuery appointmentTicketDatabaseQuery = DatabaseQueryUtils.getAppointmentTicketsByIdDatabaseQuery(mPoiId);
                appointmentTicketArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, appointmentTicketDatabaseQuery.toJson());
                getLoaderManager().initLoader(LOADER_ID_APPOINTMENT_TICKETS, appointmentTicketArgs, this);

                break;

            case LOADER_ID_APPOINTMENT_TICKETS:

                if(data != null && data.moveToFirst()){
                    do {
                        storedAppointmentTime = data.getString(data.getColumnIndex(TicketsAppointmentTable.COL_APPOINTMENT_OBJECT_JSON));
                        storedAppointmentTicketResponse = data.getString(data.getColumnIndex(TicketsAppointmentTable.COL_APPOINTMENT_TICKET_OBJECT_JSON));

                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "StoredAppointmentTime Str: " + storedAppointmentTime);
                            Log.d(TAG, "StoredAppointmentTicketResponse Str: " + storedAppointmentTicketResponse);
                        }
                    } while (data.moveToNext());
                } else {
                    storedAppointmentTime = null;
                    storedAppointmentTicketResponse = null;
                }

                createAppointmetTimesFeatureView();

                break;
            default:
                break;
        }
    }


    private void createAppointmetTimesFeatureView() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "createAppointmetTimesFeatureView: ");
        }

        if(mFeatureTicketHeader != null && mFeatureListHeaderText != null) {
            mFeatureTicketHeader.setVisibility(View.GONE);
            if (queueEntityId == mPoiId) {
                View featureView;

                mFeatureListTicketLayout.removeAllViews();

                if (storedAppointmentTicketResponse == null || storedAppointmentTicketResponse.equals("")) {

                    featureView = FeatureListUtils.createFeatureItemView(mFeatureListTicketLayout,
                            R.drawable.ic_detail_feature_ticket_gray, false,
                            R.string.detail_feature_ticket_return_time, null, null,
                            (mFeatureListTicketLayout.getChildCount() > 0), VIEW_TAG_QUEUES, this);

                    TextView secondaryText = (TextView)featureView.findViewById(R.id.list_feature_item_secondary_text);
                    secondaryText.setVisibility(View.VISIBLE);
                    secondaryText.setText(R.string.virtual_line_reserve);

                    featureView.setClickable(true);

                    mFeatureListTicketLayout.setScrollBarSize(1);
                    if (mFeatureListTicketLayout.getChildCount() < 1) {
                        mFeatureListTicketLayout.addView(featureView);
                    }
                    // Show or hide the header based on if there is queue
                    mFeatureTicketHeader.setVisibility(View.VISIBLE);
                    mFeatureTicketHeaderText.setText(R.string.detail_feature_ticket_header);

                } else {

                    String ticketTitle = getResources().getString(R.string.detail_feature_ticket_return_time);
                    ticketTitle = ticketTitle + "   " + getAppointmentTimeText();

                    featureView = FeatureListUtils.createFeatureItemView(mFeatureListTicketLayout,
                            R.drawable.ic_detail_feature_ticket, false,
                            ticketTitle, null, null,
                            (mFeatureListTicketLayout.getChildCount() > 0), VIEW_TAG_APPOINTMENT_TICKET, this);
                    featureView.setClickable(true);

                    mFeatureListTicketLayout.setScrollBarSize(1);
                    mFeatureListTicketLayout.addView(featureView);
                    // Show or hide the header based on if there is queue
                    mFeatureTicketHeader.setVisibility(View.VISIBLE);
                    mFeatureTicketHeaderText.setText(R.string.detail_feature_ticket_header);
                }
            } else {
                mFeatureTicketHeader.setVisibility(View.GONE);
            }

        }
    }

    private String getAppointmentTimeText(){

        AppointmentTimes createdAppointmentTimeJson = GsonObject.fromJson(storedAppointmentTime, AppointmentTimes.class);
        String appointmentTimeItem = "";
        if (!createdAppointmentTimeJson.getStartTime().isEmpty()){
            String startTime = createdAppointmentTimeJson.getStartTime();
            if(!createdAppointmentTimeJson.getEndTime().isEmpty()){
                String endTime = createdAppointmentTimeJson.getEndTime();
                appointmentTimeItem = startTime.substring(0,startTime.length()-3) + "-" + endTime;
            }
        }
        return appointmentTimeItem;
    }

    private void loadOffers(PointOfInterest poi) {
        if (poi == null || poi.getOfferIds() == null || poi.getOfferIds().isEmpty()) {
            return;
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "loadOffers");
        }

        // Create loader to fetch the offer linked to this POI
        Bundle loaderArgs = new Bundle();
        DatabaseQuery databaseQuery = DatabaseQueryUtils.getOffersById(poi.getOfferIds());
        loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
        getLoaderManager().initLoader(LOADER_ID_OFFERS, loaderArgs, this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onLoaderReset");
        }

        switch (loader.getId()) {
            case LOADER_ID_DATABASE_QUERY:
                // Data is not available anymore, delete reference
                break;
            case LOADER_ID_ALERTS:
                // Data is not available anymore, delete reference
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onClick");
        }

        Object tag = v.getTag();
        if (tag != null && tag instanceof String) {
            if (tag.equals(VIEW_TAG_WAIT_TIME_ALERT)) {
                WaitTimeAlert waitTimeAlert = new WaitTimeAlert(mPoiId, mPoiName, 0);
                Bundle bundle = SetAlertActivity.newInstanceBundle(waitTimeAlert.toJson(),
                        AlertsTable.VAL_ALERT_TYPE_ID_WAIT_TIME);
                startActivity(new Intent(getActivity(), SetAlertActivity.class).putExtras(bundle));
            } else if (tag.equals(VIEW_TAG_RIDE_OPEN_ALERT)) {
                RideOpenAlert rideOpenAlert = new RideOpenAlert(mPoiId, mPoiName);
                Bundle bundle = SetAlertActivity.newInstanceBundle(rideOpenAlert.toJson(),
                        AlertsTable.VAL_ALERT_TYPE_ID_RIDE_OPEN);
                startActivity(new Intent(getActivity(), SetAlertActivity.class).putExtras(bundle));
            } else if (tag.equals(VIEW_TAG_EXPRESS_PASS)) {
                // Open the express pass detail page
                startActivity(new Intent(getActivity(), ExpressPassDetailActivity.class));
            } else if (tag.equals(VIEW_TAG_SINGLE_RIDER)) {
                // Open the basic info page
                Bundle bundle = BasicInfoDetailActivity.newInstanceBundle(R.string.action_title_single_rider,
                        R.string.detail_basic_info_single_rider_title,
                        R.string.detail_basic_info_single_rider_description);
                startActivity(new Intent(getActivity(), BasicInfoDetailActivity.class).putExtras(bundle));
            } else if (tag.equals(VIEW_TAG_CHILD_SWAP)) {
                // Open the basic info page
                Bundle bundle = BasicInfoDetailActivity.newInstanceBundle(R.string.action_title_child_swap,
                        R.string.detail_basic_info_child_swap_title,
                        R.string.detail_basic_info_child_swap_description);
                startActivity(new Intent(getActivity(), BasicInfoDetailActivity.class).putExtras(bundle));
            } else if (tag.equals(VIEW_TAG_PHONE_NUMBER)) {
                // Send the user to the dialer with the phone number filled in
                Intent intent = (Intent) v.getTag(R.id.key_view_tag_detail_feature_phone_number);
                if (intent != null && intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Tag the event
                    AnalyticsUtils.trackEvent(mPoiName, AnalyticsUtils.EVENT_NAME_CALL, null,
                            null);

                    startActivity(intent);
                }
            } else if (tag.equals(VIEW_TAG_DINING_PLAN)) {
                // Open the dining plan info page
                startActivity(new Intent(getActivity(), DiningPlanDetailActivity.class));
            } else if (tag.equals(VIEW_TAG_EVENT_TICKET) && mPoi != null) {
                if (mPoi instanceof Event) {
                    Event event = (Event) mPoi;
                    Bundle args = WebViewActivity.newInstanceBundle(
                            R.string.event_ticket_buy_now_web_view_title, event.getBuyNowUrl());
                    startActivity(new Intent(getActivity(), WebViewActivity.class).putExtras(args));
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.w(TAG, "onClick: Clicked poi is not an event tag = " + tag);
                    }
                }
            } else if (tag.equals(VIEW_TAG_EVENT_EVENT_SERIES) && mPoi != null) {
                if (mPoi instanceof Event) {
                    String eventSeriesObjectJson = (String) v
                            .getTag(R.id.key_view_tag_detail_feature_event_event_series_json);
                    EventSeries series = EventSeries.fromJson(eventSeriesObjectJson, EventSeries.class);
                    Long mapTileId = null;
                    if (null != series) {
                        mapTileId = series.getCustomMapTileId();
                    }
                    DetailUtils.openEventSeriesDetailPage(v.getContext(), null, eventSeriesObjectJson, mapTileId);
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.w(TAG, "onClick: Clicked poi is not an event tag = " + tag);
                    }
                }
            } else if (tag.equals(VIEW_TAG_VIEW_MENU)) {
                if (mPoi instanceof Dining) {
                    Dining dining = (Dining) mPoi;
                    String menuUrl = dining.getMenuImages().get(0);
                    if (Pattern.matches(MENU_PDF_REGEX, menuUrl)) {
                        // Track PDF menu event
                        AnalyticsUtils.trackEvent(dining.getDisplayName(),
                                AnalyticsUtils.EVENT_NAME_MENU_PDF, -1, null);

                        // Defer opening the PDF to the system
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(menuUrl));
                        startActivity(viewIntent);
                    } else {
                        // Track images menu event
                        AnalyticsUtils.trackEvent(dining.getDisplayName(),
                                AnalyticsUtils.EVENT_NAME_MENU_IMAGES, -1, null);

                        // Open the menu images in the menu activity
                        Bundle args = DiningMenuDetailActivity.newInstanceBundle(dining.getMenuImages());
                        startActivity(new Intent(getActivity(), DiningMenuDetailActivity.class)
                                .putExtras(args));
                    }
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.w(TAG, "onClick: Clicked poi is not dining tag = " + tag);
                    }
                }
            }
            else if(tag.equals(VIEW_TAG_QUEUES)){
                if(BuildConfig.DEBUG) {
                    Log.e(TAG, "feature tag: " + VIEW_TAG_QUEUES);
                }

                String parkName = getResources().getString(R.string.app_stat_sub_text);

                if(venueDisplayName != null || !venueDisplayName.equals("")){
                    parkName = venueDisplayName;
                }

                String thumbnailImageUrl = mPoi.getListImageUrl();
                refreshFrag= true;

                Bundle outState = new Bundle();
                outState.putBoolean(KEY_REFRESH_FRAGMENT, refreshFrag);
                Bundle args = QueueTicketingAppointmentActivity.newInstanceBundle("", queueId, queueResultJson, mPoiName,thumbnailImageUrl,parkName);
                startActivityForResult(new Intent(getActivity(), QueueTicketingAppointmentActivity.class)
                        .putExtras(args), REQUEST_CODE_APPOINTMENTS);

            }

            else if(tag.equals(VIEW_TAG_APPOINTMENT_TICKET)){
                if(BuildConfig.DEBUG) {
                    Log.e(TAG, "feature tag: " + VIEW_TAG_APPOINTMENT_TICKET);
                }

                String parkName = getResources().getString(R.string.app_stat_sub_text);

                if(venueDisplayName != null || !venueDisplayName.equals("")){
                    parkName = venueDisplayName;
                }

                String thumbnailImageUrl = mPoi.getListImageUrl();

                // Create a new instance bundle to start the Queue activity from the ReturnTimeDoneActivity
                // in order to allow users to change their appointment time from their current appointment time
                Bundle changeTimeBundle = QueueTicketingAppointmentActivity.newInstanceBundle("", queueId, queueResultJson, mPoiName, thumbnailImageUrl, parkName);
                Bundle args = ReturnTimeTicketDoneActivity.newInstanceBundle(storedAppointmentTime,storedAppointmentTicketResponse,false, changeTimeBundle, mPoi.toJson(), mPoi.getPoiTypeId());
                startActivityForResult(new Intent(getActivity(), ReturnTimeTicketDoneActivity.class).putExtras(args),
                        REQUEST_CODE_APPOINTMENTS);
            }
            else if (tag.equals(VIEW_TAG_OFFER)) {
                Long offerId = (Long) v.getTag(R.id.key_view_tag_detail_feature_offer_id);
                boolean pageLoaded = false;
                // Load offer detail page
                if (offerId != null) {
                    pageLoaded = DetailUtils.openOfferDetailPage(v.getContext(), offerId);
                }
                // Display error message if offer detail page did not load
                if (!pageLoaded) {
                    UserInterfaceUtils.showToastFromForeground(getString(R.string.event_unable_to_load_page),
                            Toast.LENGTH_SHORT, getActivity());
                }
            } else if(tag.equals(VIEW_TAG_PACKAGE_PICKUP)) {
                // Open the basic info page
                startActivity(new Intent(getActivity(), PackagePickupDetailActivity.class));
            } else if(tag.equals(VIEW_TAG_COKE_FREESTYLE)) {
                // Open the basic info page
                Bundle bundle = BasicInfoDetailActivity.newInstanceBundle(R.string.action_title_coke_freestyle,
                        null,
                        R.string.detail_basic_info_coke_freestyle_description);
                startActivity(new Intent(getActivity(), BasicInfoDetailActivity.class).putExtras(bundle));
            }
        } else if (tag instanceof PhotoFrameExperience) {
            PhotoFrameExperience experience = (PhotoFrameExperience) tag;
            startActivity(PhotoFrameActivity.createIntent(getActivity(), experience));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onLongClick");
        }

        Object tag = v.getTag();
        if (tag != null && tag instanceof String) {
            if (tag.equals(VIEW_TAG_PHONE_NUMBER)) {
                TextView primaryText = (TextView) v.findViewById(R.id.list_feature_item_primary_text);
                if (primaryText != null) {
                    String phoneNumber = (String) primaryText.getText();

                    // Copy the phone number to the clip board
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(
                            Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(
                            getString(R.string.detail_phone_number_clipboard_label), phoneNumber);
                    clipboard.setPrimaryClip(clip);

                    UserInterfaceUtils.showToastFromForeground(
                            getString(R.string.detail_phone_number_copied_to_clipboard_toast_message),
                            Toast.LENGTH_SHORT, getActivity());
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public void onUniversalAppStateChange(UniversalAppState universalAppState) {
        Activity parentActivity = getActivity();
        if (parentActivity != null) {
            parentActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        // Update the views based on the geofence
                        updateViewsBasedOnGeofenceAndWaitTime();
                    } catch (Exception e) {
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG, "onUniversalAppStateChange: exception trying to refresh UI", e);
                        }

                        // Log the exception to crittercism
                        Crittercism.logHandledException(e);
                    }
                }
            });
        }
    }

    private void updateSetAlertViews() {
        // Update the set wait time alert view text and colors based on if an alert is set
        if (mSetWaitTimeAlertView != null) {
            ImageView icon = (ImageView) mSetWaitTimeAlertView
                    .findViewById(R.id.list_feature_item_icon_image);
            TextView textView = (TextView) mSetWaitTimeAlertView
                    .findViewById(R.id.list_feature_item_secondary_text);

            boolean isWaitTimeAlertSet = mNotifyThresholdInMin != null;

            Drawable drawable = TintUtils.tintDrawable(ContextCompat.getDrawable(getContext(),
                    R.drawable.ic_detail_feature_wait_time_alert),
                    ContextCompat.getColor(getContext(),
                            isWaitTimeAlertSet ? R.color.feature_icon_colored : R.color.feature_icon));
            icon.setImageDrawable(drawable);
            textView.setTextColor(ContextCompat.getColor(getContext(),
                    isWaitTimeAlertSet ? R.color.text_primary : R.color.text_gray));
            String text = isWaitTimeAlertSet ? getString(R.string.detail_feature_wait_time_alert_set_min,
                    mNotifyThresholdInMin) : getString(R.string.detail_feature_wait_time_alert_set_alert);

            textView.setText(text);
        }
        // Update the set open alert view text and colors based on if an alert is set
        if (mSetRideOpenAlertView != null) {
            ImageView icon = (ImageView) mSetRideOpenAlertView
                    .findViewById(R.id.list_feature_item_icon_image);
            TextView textView = (TextView) mSetRideOpenAlertView
                    .findViewById(R.id.list_feature_item_secondary_text);

            Resources r = getResources();
            Drawable drawable = TintUtils.tintDrawable(ContextCompat.getDrawable(getContext(),
                    R.drawable.ic_detail_feature_wait_time_alert),
                    ContextCompat.getColor(getContext(),
                            mIsRideOpenAlertSet ? R.color.feature_icon_colored : R.color.feature_icon));
            icon.setImageDrawable(drawable);
            textView.setTextColor(ContextCompat.getColor(getContext(),
                    mIsRideOpenAlertSet ? R.color.text_primary : R.color.text_gray));
            String text = mIsRideOpenAlertSet ? getString(R.string.detail_feature_ride_open_alert_set_on)
                    : getString(R.string.detail_feature_ride_open_alert_set_alert);

            textView.setText(text);
        }
    }

    private void updateViewsBasedOnGeofenceAndWaitTime() {
        Activity parentActivity = getActivity();
        if (parentActivity != null && mFeatureListLayout != null && mSetWaitTimeAlertView != null
                && mSetRideOpenAlertView != null) {
            UniversalAppState uoState = UniversalAppStateManager.getInstance();
            boolean isWaitTimeFresh = UniversalAppStateManager.hasSyncedInTheLast(
                    uoState.getDateOfLastPoiSyncInMillis(), PoiUtils.WAIT_TIME_STALE_THRESHOLD_IN_SEC);
            boolean isDuringVenueHours = PoiUtils.isDuringVenueHours(System.currentTimeMillis(), mVenueHours);

            // Show or hide the set wait time alert
            boolean showSetWaitTimeAlertView;
            if (isWaitTimeFresh && mWaitTime != null && mWaitTime > 5) {
                showSetWaitTimeAlertView = true;
            } else {
                showSetWaitTimeAlertView = false;
            }
            mSetWaitTimeAlertView.setVisibility(showSetWaitTimeAlertView ? View.VISIBLE : View.GONE);

            // Show or hide the set ride open alert
            boolean showSetRideOpenAlertView;
            boolean rideIsInValidClosedState = AlertsUtils.showSetRideOpenAlert(mWaitTime);
            if ((isDuringVenueHours || mWaitTime.equals(Ride.RIDE_WAIT_TIME_STATUS_CLOSED_INSIDE_OF_OPERATING_HOURS)) && isWaitTimeFresh && rideIsInValidClosedState) {
                showSetRideOpenAlertView = true;
            } else {
                showSetRideOpenAlertView = false;
            }
            mSetRideOpenAlertView.setVisibility(showSetRideOpenAlertView ? View.VISIBLE : View.GONE);

            // Update the top divider of the third item based on if either set
            // alert view is shown. The first two items will always be the set
            // wait time alert and set ride open alert, and their visibility
            // will toggle, but they will never both be visible at once
            if (mFeatureListLayout.getChildCount() > 2) {
                View featureView = mFeatureListLayout.getChildAt(2);
                View topDivider = featureView.findViewById(R.id.list_feature_item_top_divider);
                boolean showTopDivider = mSetWaitTimeAlertView.getVisibility() == View.VISIBLE
                        || mSetRideOpenAlertView.getVisibility() == View.VISIBLE;
                topDivider.setVisibility(showTopDivider ? View.VISIBLE : View.GONE);
            }

            // Update any other listeners of the latest state
            informFeatureListStateChangeListeners();
        }
    }

    private void informFeatureListStateChangeListeners() {
        // If the feature list is the primary feature list (top section of detail page), and empty, inform the
        // parent listener
        if (mParentOnFeatureListStateChangeListener != null && mFeatureListLayout != null
                && (mIsSecondary == null || !mIsSecondary.booleanValue())) {

            int featureListItems = mFeatureListLayout.getChildCount();

            // Adjust the count if the set alerts views are hidden
            if (mSetWaitTimeAlertView != null && mSetRideOpenAlertView != null) {
                if (mSetWaitTimeAlertView.getVisibility() != View.VISIBLE) {
                    featureListItems -= 1;
                }
                if (mSetRideOpenAlertView.getVisibility() != View.VISIBLE) {
                    featureListItems -= 1;
                }
                boolean isFeatureListEmpty = (featureListItems <= 0);
                mParentOnFeatureListStateChangeListener.onFeatureListStateChange(isFeatureListEmpty);
            } else {
                boolean isFeatureListEmpty = (featureListItems == 0);
                mParentOnFeatureListStateChangeListener.onFeatureListStateChange(isFeatureListEmpty);
            }
        }
    }

    private void addRideFeatures(Ride ride) {

        // Set wait time text
        mSetWaitTimeAlertView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                R.drawable.ic_detail_feature_wait_time_alert, false, R.string.detail_feature_wait_time_alert,
                null, R.string.detail_feature_wait_time_alert_set_alert, false, // Never show a top divider,
                // will only ever appear as
                // top item
                VIEW_TAG_WAIT_TIME_ALERT, this);
        mSetWaitTimeAlertView.setClickable(true);

        // Set wait time text
        mSetRideOpenAlertView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                R.drawable.ic_detail_feature_wait_time_alert, false, R.string.detail_feature_ride_open_alert,
                null, R.string.detail_feature_ride_open_alert_set_alert, false, // Never show a top divider,
                // will only ever appear as
                // top item
                VIEW_TAG_RIDE_OPEN_ALERT, this);
        mSetRideOpenAlertView.setClickable(true);

        // Update the view to check if there is already an alert set
        updateSetAlertViews();

        // Add views to feature list after updating their state
        mFeatureListLayout.addView(mSetWaitTimeAlertView);
        mFeatureListLayout.addView(mSetRideOpenAlertView);

        // Set express pass text
        View featureView;
        Boolean expressPassAccepted = ride.getExpressPassAccepted();
        if (expressPassAccepted != null && expressPassAccepted.booleanValue()) {
            // Wet n Wild venue uses alternate express pass icon
            if (ride.getVenueId() == VenuesTable.VAL_VENUE_ID_WET_N_WILD) {
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                        R.drawable.ic_detail_feature_express_pass_wnw, false,
                        R.string.detail_feature_express_pass_line, null, null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_EXPRESS_PASS, this);
            } else {
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                        R.drawable.ic_detail_feature_express_pass, false,
                        R.string.detail_feature_express_pass_line, null, null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_EXPRESS_PASS, this);
            }
            featureView.setClickable(true);
            mFeatureListLayout.addView(featureView);
        }

        // Set nominal fee
        Boolean hasNominalFee = ride.getHasNominalFee();
        if (hasNominalFee != null && hasNominalFee.booleanValue()) {
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                    R.drawable.ic_detail_feature_nominal_fee, false,
                    R.string.detail_feature_ride_nominal_fee, null, null,
                    (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_NOMINAL_FEE, this);
            featureView.setClickable(false);
            mFeatureListLayout.addView(featureView);
        }

        // Set single rider text
        Boolean hasSingleRiderLine = ride.getHasSingleRiderLine();
        if (hasSingleRiderLine != null && hasSingleRiderLine.booleanValue()) {
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                    R.drawable.ic_detail_feature_single_rider, false,
                    R.string.detail_feature_single_rider_line, null, null,
                    (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_SINGLE_RIDER, this);
            featureView.setClickable(true);
            mFeatureListLayout.addView(featureView);
        }

        // Set child swap text
        Boolean hasChildSwap = ride.getHasChildSwap();
        if (hasChildSwap != null && hasChildSwap.booleanValue()) {
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                    R.drawable.ic_detail_feature_child_swap, false, R.string.detail_feature_child_swap, null,
                    null, (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_CHILD_SWAP, this);
            featureView.setClickable(true);
            mFeatureListLayout.addView(featureView);
        }

        // Set sub type text
        Integer poiSubTypeIconResId = getPoiSubTypeIconResId(ride);
        Integer poiSubTypeStringResId = getPoiSubTypeStringResId(ride);
        if (poiSubTypeIconResId != null && poiSubTypeStringResId != null) {
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, poiSubTypeIconResId,
                    false, poiSubTypeStringResId, null, null, (mFeatureListLayout.getChildCount() > 0),
                    VIEW_TAG_SUB_TYPE, this);
            featureView.setClickable(false);
            mFeatureListLayout.addView(featureView);
        }

        // Water super thrill secondary sub-type
        boolean waterSuperThrill = ride.hasSecondarySubType(Ride.RIDE_SECONDARY_TYPE_WATER_SUPER_THRILL);
        if (waterSuperThrill) {
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                    R.drawable.ic_detail_feature_super_thrill, false,
                    R.string.detail_feature_ride_super_thrill, null, null,
                    (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_SUPER_THRILL, this);
            featureView.setClickable(false);
            mFeatureListLayout.addView(featureView);
        }

        // Water group thrill secondary sub-type
        boolean waterGroupThrill = ride.hasSecondarySubType(Ride.RIDE_SECONDARY_TYPE_WATER_GROUP_THRILL);
        if (waterGroupThrill) {
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                    R.drawable.ic_detail_feature_group_thrill, false,
                    R.string.detail_feature_ride_group_thrill, null, null,
                    (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_GROUP_THRILL, this);
            featureView.setClickable(false);
            mFeatureListLayout.addView(featureView);
        }

        // Water family thrill secondary sub-type
        boolean waterFamilyThrill = ride.hasSecondarySubType(Ride.RIDE_SECONDARY_TYPE_WATER_FAMILY_THRILL);
        if (waterFamilyThrill) {
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                    R.drawable.ic_detail_feature_family_thrill, false,
                    R.string.detail_feature_ride_family_thrill, null, null,
                    (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_FAMILY_THRILL, this);
            featureView.setClickable(false);
            mFeatureListLayout.addView(featureView);
        }

        // Water extras secondary sub-type
        boolean extrasThrill = ride.hasSecondarySubType(Ride.RIDE_SECONDARY_TYPE_WATER_EXTRAS);
        if (extrasThrill) {
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                    R.drawable.ic_detail_feature_extras, false, R.string.detail_feature_ride_extras, null,
                    null, (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_EXTRAS, this);
            featureView.setClickable(false);
            mFeatureListLayout.addView(featureView);
        }
    }

    private void addShowFeatures(Show show) {
        // Add the primary feature list items
        if (mIsSecondary == null || !mIsSecondary.booleanValue()) {
            // Set express pass text
            Boolean expressPassAccepted = show.getExpressPassAccepted();
            if (expressPassAccepted != null && expressPassAccepted.booleanValue()) {
                View featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                        R.drawable.ic_detail_feature_express_pass, false,
                        R.string.detail_feature_express_pass_line, null, null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_EXPRESS_PASS, this);
                featureView.setClickable(true);
                mFeatureListLayout.addView(featureView);
            }

            // Set sub type text
            Integer poiSubTypeIconResId = getPoiSubTypeIconResId(show);
            Integer poiSubTypeStringResId = getPoiSubTypeStringResId(show);
            View featureView;
            if (poiSubTypeIconResId != null && poiSubTypeStringResId != null) {
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, poiSubTypeIconResId,
                        false, poiSubTypeStringResId, null, null, (mFeatureListLayout.getChildCount() > 0),
                        VIEW_TAG_SUB_TYPE, this);
                featureView.setClickable(false);
                mFeatureListLayout.addView(featureView);
            }
        }
        // Add the secondary feature list items
        else if (mIsSecondary.booleanValue()) {
            // Set reservations phone number
            String reservationsPhoneNumberString = show.getReservationsPhoneNumber();
            FeatureListUtils.addPhoneFeature(getActivity(), mFeatureListLayout,
                    reservationsPhoneNumberString, null, VIEW_TAG_PHONE_NUMBER, this, this);
        }
    }

    private void addDiningFeatures(Dining dining) {
        View featureView;

        // Primary feature items, actually appear below the map
        if (mIsSecondary == null || !mIsSecondary) {
            // Set sub type text
            Integer poiSubTypeIconResId = getPoiSubTypeIconResId(dining);
            Integer poiSubTypeStringResId = getPoiSubTypeStringResId(dining);
            if (poiSubTypeIconResId != null && poiSubTypeStringResId != null) {
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, poiSubTypeIconResId,
                        true, poiSubTypeStringResId, null, null, (mFeatureListLayout.getChildCount() > 0),
                        VIEW_TAG_SUB_TYPE, this, FeatureListUtils.TintType.COLORED);
                featureView.setClickable(false);
                mFeatureListLayout.addView(featureView);
            }
            // Set view menu
            if (dining.getMenuImages() != null && !dining.getMenuImages().isEmpty()) {
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                        R.drawable.ic_detail_feature_menu, true, R.string.detail_feature_view_menu, null,
                        null, (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_VIEW_MENU, this, FeatureListUtils.TintType.COLORED);
                featureView.setClickable(true);
                mFeatureListLayout.addView(featureView);
            }
            // Set price range
            addPriceFeature(dining, FeatureListUtils.TintType.COLORED);

            // Orlando only, set dining plan features for
            if (BuildConfigUtils.isLocationFlavorOrlando()) {
                Boolean diningPlanAccepted = dining.getDiningPlanAccepted();
                boolean isDiningPlanAccepted = diningPlanAccepted != null ? diningPlanAccepted.booleanValue()
                        : false;
                Boolean quickServeParticipant = dining.getQuickServeParticipant();
                boolean isQuickServeParticipant = quickServeParticipant != null ? quickServeParticipant
                        .booleanValue() : false;
                Integer primaryTextResId;
                if (isDiningPlanAccepted && isQuickServeParticipant) {
                    primaryTextResId = R.string.detail_feature_dining_plan_and_quick_service_accepted;
                } else if (isDiningPlanAccepted) {
                    primaryTextResId = R.string.detail_feature_dining_plan_accepted;
                } else if (isQuickServeParticipant) {
                    primaryTextResId = R.string.detail_feature_dining_plan_quick_service_accepted;
                } else {
                    primaryTextResId = R.string.detail_feature_dining_plans_not_accepted;
                }
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                        R.drawable.ic_detail_feature_dining_plan, true, primaryTextResId, null, null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_DINING_PLAN, this, FeatureListUtils.TintType.COLORED);
                featureView.setClickable(true);
                mFeatureListLayout.addView(featureView);
            }

            // Set phone number
            String phoneNumberString = dining.getPhoneNumber();
            Boolean reservationsSuggested = dining.getAreReservationsSuggested();
            boolean areReservationsSuggested = reservationsSuggested != null ? reservationsSuggested
                    .booleanValue() : false;
            String primarySubText = null;
            if (areReservationsSuggested) {
                primarySubText = getString(R.string.detail_feature_dining_reservations_suggested);
            }
            FeatureListUtils.addPhoneFeature(getActivity(), mFeatureListLayout, phoneNumberString,
                    primarySubText, VIEW_TAG_PHONE_NUMBER, this, this, FeatureListUtils.TintType.COLORED);

            Boolean hasCokeFreeStyle = dining.getHasCocaColaFreestyleMachine();
            if(hasCokeFreeStyle != null && hasCokeFreeStyle.booleanValue()){
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                        R.drawable.ic_detail_feature_coke, false,
                        R.string.detail_feature_coke_freestyle, null, null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_COKE_FREESTYLE,
                        this, FeatureListUtils.TintType.NO_TINT);
                mFeatureListLayout.addView(featureView);
            }
        }
    }

    private void addEntertainmentFeatures(Entertainment entertainment) {
        // Set price range
        addPriceFeature(entertainment, FeatureListUtils.TintType.DEFAULT);

        // Orlando only, set party pass
        if (BuildConfigUtils.isLocationFlavorOrlando()) {
            Boolean partyPassAccepted = entertainment.getPartyPassAccepted();
            int primaryTextResId = -1;
            if (partyPassAccepted != null && partyPassAccepted.booleanValue()) {
                primaryTextResId = R.string.detail_feature_party_pass_accepted;
            } else {
                primaryTextResId = R.string.detail_feature_party_pass_not_accepted;
            }

            View featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, null, true,
                    primaryTextResId, null, null, (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_PARTY_PASS,
                    this);
            featureView.setClickable(false);
            mFeatureListLayout.addView(featureView);


        } else if (BuildConfigUtils.isLocationFlavorHollywood()) {

            Integer poiSubTypeIconResId = getPoiSubTypeIconResId(entertainment);
            Integer poiSubTypeStringResId = getPoiSubTypeStringResId(entertainment);
            if (poiSubTypeIconResId != null && poiSubTypeStringResId != null) {
                View featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, poiSubTypeIconResId,
                        true, poiSubTypeStringResId, null, null, (mFeatureListLayout.getChildCount() > 0),
                        VIEW_TAG_SUB_TYPE, this, FeatureListUtils.TintType.COLORED);
                featureView.setClickable(false);
                mFeatureListLayout.addView(featureView);
            }
        }

    }

    private void addHotelFeatures(Hotel hotel, Venue venue) {

        // Add the primary feature list items
        if (mIsSecondary == null || !mIsSecondary.booleanValue()) {
            if (venue != null) {
                String phoneNumberString = venue.getPhoneNumber();
                String phoneNumberCopy = venue.getPhoneNumberCopy();
                FeatureListUtils
                        .addPhoneFeature(getActivity(), mFeatureListLayout, phoneNumberString,
                                phoneNumberCopy, VIEW_TAG_PHONE_NUMBER,
                                this, this);
            }
        }
        // Add the secondary feature list items
        else if (mIsSecondary.booleanValue()) {
            // Set reservations phone number
            String reservationsPhoneNumberString = hotel.getReservationsPhoneNumber();
            String reservationCopy = hotel.getReservationCopy();
            FeatureListUtils.addPhoneFeature(getActivity(), mFeatureListLayout,
                    reservationsPhoneNumberString,
                    reservationCopy, VIEW_TAG_PHONE_NUMBER,
                    this, this);

            // Show or hide the header based on if there is a phone number
            mFeatureListHeaderText.setText(R.string.detail_header_reservations);
            mFeatureListHeader.setVisibility(mFeatureListLayout.getChildCount() > 0 ? View.VISIBLE
                    : View.GONE);
        }
    }

    private void addParksFeatures(PointOfInterest poi, Venue venue) {

        // Set phone number
        if (venue != null) {
            String phoneNumberString = venue.getPhoneNumber();
            FeatureListUtils.addPhoneFeature(getActivity(), mFeatureListLayout, phoneNumberString, null,
                    VIEW_TAG_PHONE_NUMBER, this, this);
        }
    }

    private void addLockerFeatures(Lockers lockers) {
        // Set sub type text
        Integer poiSubTypeIconResId = getPoiSubTypeIconResId(lockers);
        Integer poiSubTypeStringResId = getPoiSubTypeStringResId(lockers);
        View featureView;
        if (poiSubTypeIconResId != null && poiSubTypeStringResId != null) {
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, poiSubTypeIconResId,
                    false, poiSubTypeStringResId, null, null, (mFeatureListLayout.getChildCount() > 0),
                    VIEW_TAG_SUB_TYPE, this);
            featureView.setClickable(false);
            mFeatureListLayout.addView(featureView);
        }

        // Add the locker dimensions
        StringBuilder lockerDimensions = new StringBuilder();
        List<Dimensions> lockerSizes = lockers.getLockerSizes();
        if (lockerSizes != null) {
            for (Dimensions dimensions : lockerSizes) {
                if (dimensions != null) {
                    Float width = dimensions.getWidthInInches();
                    Float height = dimensions.getHeightInInches();
                    Float depth = dimensions.getLengthInInches();
                    if (width != null && height != null && depth != null) {
                        String dimensionsRow = getString(R.string.detail_feature_available_sizes_format,
                                "" + width, "" + height, "" + depth);
                        lockerDimensions.append(dimensionsRow).append("\n");
                    }
                }
            }
        }
        if (lockerDimensions.length() > 0) {
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                    R.drawable.ic_detail_feature_locker_size, false,
                    getString(R.string.detail_feature_available_sizes), lockerDimensions.toString().trim(),
                    null, (mFeatureListLayout.getChildCount() > 0), null, this);
            featureView.setClickable(false);
            mFeatureListLayout.addView(featureView);
        }
    }

    private void addShoppingFeatures(Shop shop) {
        // Add the primary feature list items
        if (mIsSecondary == null || !mIsSecondary.booleanValue()) {
            View featureView;
            // Set express pass text
            Boolean sellsExpressPass = shop.getSellsExpressPass();
            if (sellsExpressPass != null && sellsExpressPass.booleanValue()) {
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                        R.drawable.ic_detail_feature_express_pass, false,
                        R.string.detail_feature_express_pass_sold_here, null, null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_EXPRESS_EXPRESS_PASS_SOLD_HERE,
                        this);
                featureView.setClickable(false);
                mFeatureListLayout.addView(featureView);
            }

            if (BuildConfigUtils.isLocationFlavorHollywood()) {
                Integer poiSubTypeIconResId = getPoiSubTypeIconResId(shop);
                Integer poiSubTypeStringResId = getPoiSubTypeStringResId(shop);
                if (poiSubTypeIconResId != null && poiSubTypeStringResId != null) {
					featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, poiSubTypeIconResId,
							true, poiSubTypeStringResId, null, null, (mFeatureListLayout.getChildCount() > 0),
							VIEW_TAG_SUB_TYPE, this, FeatureListUtils.TintType.DEFAULT);
					featureView.setClickable(false);
					mFeatureListLayout.addView(featureView);
				}
            }

            //should do some logic once this is in Tridion to for a location
            //is package pickup enabled
            Boolean hasPackagePickup = shop.getHasPackagePickup();
            if(hasPackagePickup != null && hasPackagePickup.booleanValue()){
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                        R.drawable.ic_detail_feature_package_pickup, false,
                        R.string.detail_feature_package_pickup, null, null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_PACKAGE_PICKUP,
                        this);
                mFeatureListLayout.addView(featureView);
            }

            // Show or hide the header based on if there are any feature items
            mFeatureListHeaderText.setText(R.string.detail_header_shop_detail);
            mFeatureListHeader.setVisibility(mFeatureListLayout.getChildCount() > 0 ? View.VISIBLE
                    : View.GONE);
        }
    }

    private void addQueueFeatures(QueuesResult queue){



    }

    private void addRentalTypeFeatures(RentalServices rentalServices) {
        // Add the primary feature list items
        if (mIsSecondary == null || !mIsSecondary.booleanValue()) {
            View featureView;

            List<String> rentalTypes = rentalServices.getRentalTypes();
            if (rentalTypes.contains(RentalServices.RENTAL_TYPE_ECVS)) {
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                        R.drawable.ic_detail_feature_rental_ecv, false,
                        R.string.detail_feature_rental_type_evc_available, null, null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_EVC_RENTAL_TYPE,
                        this);
                featureView.setClickable(false);
                mFeatureListLayout.addView(featureView);
            }
            if (rentalTypes.contains(RentalServices.RENTAL_TYPE_STROLLERS)) {
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                        R.drawable.ic_detail_feature_rental_stroller, false,
                        R.string.detail_feature_rental_type_stroller_available, null, null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_STROLLER_RENTAL_TYPE,
                        this);
                featureView.setClickable(false);
                mFeatureListLayout.addView(featureView);
            }
            if (rentalTypes.contains(RentalServices.RENTAL_TYPE_WHEELCHAIRS)) {
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                        R.drawable.ic_detail_feature_rental_wheelchair, false,
                        R.string.detail_feature_rental_type_wheel_chair_available, null, null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_WHEEL_CHAIR_RENTAL_TYPE,
                        this);
                featureView.setClickable(false);
                mFeatureListLayout.addView(featureView);
            }

            // Show or hide the header based on if there are any feature items
            mFeatureListHeaderText.setText(R.string.detail_header_rental_type_detail);
            mFeatureListHeader.setVisibility(mFeatureListLayout.getChildCount() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    private void addPriceFeature(PricesProvider pricesProvider, FeatureListUtils.TintType tintType) {
        Double minPriceDouble = pricesProvider.getMinimumPrice();
        Double maxPriceDouble = pricesProvider.getMaximumPrice();

        if (minPriceDouble != null || maxPriceDouble != null) {
            int minPrice = 0;
            int maxPrice = 0;
            if (minPriceDouble != null) {
                minPrice = (int) Math.ceil(minPriceDouble.doubleValue());
            }
            if (maxPriceDouble != null) {
                maxPrice = (int) Math.ceil(maxPriceDouble.doubleValue());
            }

            try {
                String formattedPrice = "";
                if (minPrice > 0 && maxPrice > 0) {
                    if (maxPrice > minPrice) {
                        String priceFormat = getString(R.string.detail_feature_price_range_two);
                        formattedPrice = String.format(priceFormat, minPrice, maxPrice);
                    } else if (minPrice == maxPrice) {
                        String priceFormat = getString(R.string.detail_feature_price_range_one);
                        formattedPrice = String.format(priceFormat, minPrice);
                    } else if (minPrice > maxPrice) {
                        String priceFormat = getString(R.string.detail_feature_price_min_starting_at);
                        formattedPrice = String.format(priceFormat, minPrice);
                    }
                } else if (minPrice > 0) {
                    String priceFormat = getString(R.string.detail_feature_price_min_starting_at);
                    formattedPrice = String.format(priceFormat, minPrice);
                } else if (maxPrice > 0) {
                    String priceFormat = getString(R.string.detail_feature_price_max_up_to);
                    formattedPrice = String.format(priceFormat, maxPrice);
                }

                if (!formattedPrice.isEmpty()) {
                    View featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                            R.drawable.ic_detail_feature_price, true, formattedPrice, null, null,
                            (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_PRICES, this, tintType);
                    featureView.setClickable(false);
                    mFeatureListLayout.addView(featureView);
                }
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "addPriceFeature: exception formatting price", e);
                }

                // Log the exception to crittercism
                Crittercism.logHandledException(e);
            }
        }
    }

    private void addEventFeatures(Event event) {
        View featureView;
        // Primary features above the map
        if (mIsSecondary == null || !mIsSecondary) {
            if (event.getEventDates() != null && !event.getEventDates().isEmpty()) {
                // Show dates and times for the event
                if (event.getEventDates().size() == 1) {
                    // Event dates
                    featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                            R.drawable.ic_detail_feature_calendar, false, EventUtils.getEventFullDay(
                                    event.getEventDates().get(0), false,
                                    getString(R.string.event_series_dates_tba)), null, null,
                            (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_EVENT_DATES, this);
                    featureView.setClickable(false);
                    mFeatureListLayout.addView(featureView);
                    // Event times
                    featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                            R.drawable.ic_detail_feature_clock, false, EventUtils.getEventTimeStartSpanLines(
                                    event.getEventDates(), getString(R.string.event_series_dates_tba)), null,
                            null, (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_EVENT_TIMES, this);
                    featureView.setClickable(false);
                    mFeatureListLayout.addView(featureView);
                } else {
                    // Event dates
                    boolean useShortStyle = EventUtils.eventDatesSpanMultipleDays(event.getEventDates());
                    featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                            R.drawable.ic_detail_feature_calendar, false, EventUtils.getEventDateSpan(
                                    event.getEventDates(), useShortStyle,
                                    getString(R.string.event_series_dates_tba), true), null, null,
                            (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_EVENT_DATES, this);
                    featureView.setClickable(false);
                    mFeatureListLayout.addView(featureView);
                    // Event times
                    featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                            R.drawable.ic_detail_feature_clock, false, EventUtils.getEventDateStartSpanLines(
                                    event.getEventDates(),
                                    getString(R.string.event_series_dates_tba)), null, EventUtils
                                    .getEventTimeStartSpanLines(event.getEventDates(),
                                            getString(R.string.event_series_dates_tba)), (mFeatureListLayout
                                    .getChildCount() > 0), VIEW_TAG_EVENT_TIMES, this);
                    featureView.setClickable(false);
                    mFeatureListLayout.addView(featureView);
                }
            }
            // No event date present
            else {
                // Event Dates
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                        R.drawable.ic_detail_feature_calendar, false, EventUtils.getEventDateSpan(
                                event.getEventDates(), true,
                                getString(R.string.event_series_dates_tba)), null, null, (mFeatureListLayout
                                .getChildCount() > 0), VIEW_TAG_EVENT_DATES, this);
                featureView.setClickable(false);
                mFeatureListLayout.addView(featureView);
            }

            if (null != event.getPhotoFrameExperiences() && !event.getPhotoFrameExperiences().isEmpty()) {
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                        R.drawable.ic_menu_settings, false, "Photo Frames(tbd)", null, null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_PHOTO_FRAMES, this);
                featureView.setClickable(true);
                mFeatureListLayout.addView(featureView);
            }
            // Event location
            if (!TextUtils.isEmpty(event.getLocation())) {
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                        R.drawable.ic_detail_feature_map_marker, false, event.getLocation(), null, null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_EVENT_LOCATION, this);
                featureView.setClickable(false);
                mFeatureListLayout.addView(featureView);
            }
        }
        // Secondary features below the map
        else {
            // Event ticket info
            String ticketPrimary = event.getTicketedEventDetails();
            String ticketSecondary = null;
            if (ticketPrimary != null) {
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                        R.drawable.ic_detail_feature_ticket, false, ticketPrimary, ticketSecondary, null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_EVENT_TICKET, this);
                featureView.setClickable(!TextUtils.isEmpty(ticketSecondary));
                mFeatureListLayout.addView(featureView);
            }
        }
    }

    private void addEventSeriesFeatures(List<EventSeries> eventSeriesList) {
        if (eventSeriesList != null && !eventSeriesList.isEmpty() && mIsSecondary != null && mIsSecondary) {
            View featureView;
            // Event series link
            for (EventSeries eventSeries : eventSeriesList) {
                String title = getString(R.string.detail_feature_event_event_series_format,
                        eventSeries.getDisplayName());
                boolean harryPotterSeries = EventUtils.isHarryPotterEvent(eventSeries);
                int linkIcon = harryPotterSeries ? R.drawable.ic_detail_feature_harry_potter_glasses
                        : R.drawable.ic_detail_feature_event_series;
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, linkIcon, false,
                        title, null, null, (mFeatureListLayout.getChildCount() > 0),
                        VIEW_TAG_EVENT_EVENT_SERIES, this);
                featureView.setClickable(true);
                // Add event series json to view tag for use in the onClick method
                featureView.setTag(R.id.key_view_tag_detail_feature_event_event_series_json,
                        eventSeries.toJson());
                mFeatureListLayout.addView(featureView);
            }
            // Event disclaimer from parent event series
            for (EventSeries eventSeries : eventSeriesList) {
                if (!TextUtils.isEmpty(eventSeries.getDisclaimer())) {
                    featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, null, false,
                            null, eventSeries.getDisclaimer(), null,
                            (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_EVENT_SERIES_DISCLAIMER, this);
                    featureView.setClickable(false);
                    mFeatureListLayout.addView(featureView);
                    // break out of loop since a non-empty disclaimer was found
                    break;
                }
            }
        }
    }

    private void addShoppingOfferFeatures(List<Offer> offers) {
        // Primary shopping features, above the map
        if (offers != null && !offers.isEmpty() && (mIsSecondary == null || mIsSecondary == false)) {
            // Offers
            for (Offer offer : offers) {
                addOffer(offer);
            }

            // Show or hide the header based on if there are any feature items
            mFeatureListHeader.setVisibility(mFeatureListLayout.getChildCount() > 0 ? View.VISIBLE
                    : View.GONE);
        }
    }

    private void addDiningOfferFeatures(List<Offer> offers) {
        // Secondary dining features, above the map
        if (offers != null && !offers.isEmpty() && mIsSecondary != null && mIsSecondary) {
            // Offers
            for (Offer offer : offers) {
                addOffer(offer);
            }

            // Show or hide the header based on if there are any feature items
            mFeatureListHeaderText.setText(R.string.detail_header_dining_detail);
            mFeatureListHeader.setVisibility(mFeatureListLayout.getChildCount() > 0 ? View.VISIBLE
                    : View.GONE);
        }
    }

    private  void addGatewayFeatures(Gateway gateway) {
        // Add the primary feature list items
        if (mIsSecondary == null || !mIsSecondary.booleanValue()) {
            View featureView;
            // Set express pass text
            String gatewayType = gateway.getGatewayType();
            if (gatewayType != null && !gatewayType.isEmpty()) {
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                        null, false,
                        gatewayType, null, null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_GATEWAY_TYPE_DISPLAYED,
                        this);
                featureView.setClickable(false);
                mFeatureListLayout.addView(featureView);
            }

            // Show or hide the header based on if there are any feature items
            mFeatureListHeaderText.setText(R.string.detail_header_gateway_detail);
            mFeatureListHeader.setVisibility(mFeatureListLayout.getChildCount() > 0 ? View.VISIBLE
                    : View.GONE);
        }
    }

    private void addPhotoFrameExperienceFeatures(List<PhotoFrameExperience> experiences) {
        View featureView;

        for (PhotoFrameExperience experience : experiences) {
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, R.drawable.ic_detail_photoframe, false,
                    experience.getShortDescription(), null, null,
                    (mFeatureListLayout.getChildCount() > 0), experience, this);
            featureView.setClickable(true);
            mFeatureListLayout.addView(featureView);
        }
    }

    /**
     * Add offer feature if it's food & beverage or merchandise
     *
     * @param offer
     */
    private void addOffer(Offer offer) {
        View featureView;
        if (Offer.OFFER_TYPE_FOOD_AND_BEVERAGE.equalsIgnoreCase(offer.getOfferType())) {
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                    R.drawable.ic_detail_feature_credit_card, false, R.string.offers_amex_food_and_beverage,
                    null, null, (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_OFFER, this);
            featureView.setClickable(true);
            featureView.setTag(R.id.key_view_tag_detail_feature_offer_id, offer.getId());
            mFeatureListLayout.addView(featureView);
        } else if (Offer.OFFER_TYPE_MERCHANDISE.equalsIgnoreCase(offer.getOfferType())) {
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                    R.drawable.ic_detail_feature_credit_card, false, R.string.offers_amex_merchandise, null,
                    null, (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_OFFER, this);
            featureView.setClickable(true);
            featureView.setTag(R.id.key_view_tag_detail_feature_offer_id, offer.getId());
            mFeatureListLayout.addView(featureView);
        }
    }

    private static Integer getPoiSubTypeIconResId(PointOfInterest poi) {
        if (poi == null) {
            return null;
        }

        if (poi instanceof Ride) {
            if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_THRILL)) {
                return R.drawable.ic_detail_feature_ride_thrill;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_WATER)) {
                return R.drawable.ic_detail_feature_ride_water;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_VIDEO_3D_4D)) {
                return R.drawable.ic_detail_feature_ride_3d;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_KID_FRIENDLY)) {
                return R.drawable.ic_detail_feature_ride_kids;
            } else {
                return R.drawable.ic_detail_feature_ride_thrill;
            }
        } else if (poi instanceof Show) {
            if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_ACTION)) {
                return R.drawable.ic_detail_feature_shows_action;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_COMEDY)) {
                return R.drawable.ic_detail_feature_shows_comedy;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_MUSIC)) {
                return R.drawable.ic_detail_feature_shows_music;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_PARADE)) {
                return R.drawable.ic_detail_feature_shows_parade;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_CHARACTER)) {
                return R.drawable.ic_detail_feature_shows_character;
            } else {
                return R.drawable.ic_detail_feature_shows;
            }
        } else if (poi instanceof Dining) {
            if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_FINE_DINING)) {
                return R.drawable.ic_detail_feature_dining_fine;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_CASUAL_DINING)) {
                return R.drawable.ic_detail_feature_dining_casual;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_QUICK_SERVICE)) {
                return R.drawable.ic_detail_feature_dining_quick;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_SNACKS)) {
                return R.drawable.ic_detail_feature_dining_snack;
            } else {
                return R.drawable.ic_detail_feature_dining;
            }
        } else if (poi instanceof Lockers) {
            if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_LOCKER_TYPE_RENTAL)) {
                return R.drawable.ic_detail_feature_lockers_rental;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_LOCKER_TYPE_RIDE)) {
                return R.drawable.ic_detail_feature_lockers_ride;
            } else {
                return R.drawable.ic_detail_feature_lockers_ride;
            }
        } else if (poi instanceof Shop) {
            return R.drawable.ic_menu_shopping;
        } else if (poi instanceof Entertainment) {
            return R.drawable.ic_detail_feature_entertainment;
        }
        return null;
    }

    private static Integer getPoiSubTypeStringResId(PointOfInterest poi) {
        if (poi == null) {
            return null;
        }

        if (poi instanceof Ride) {
            if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_THRILL)) {
                return R.string.detail_feature_ride_thrill;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_WATER)) {
                return R.string.detail_feature_ride_water;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_VIDEO_3D_4D)) {
                return R.string.detail_feature_ride_3d;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_KID_FRIENDLY)) {
                return R.string.detail_feature_ride_kids;
            } else {
                return R.string.detail_feature_ride;
            }
        } else if (poi instanceof Show) {
            if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_ACTION)) {
                return R.string.detail_feature_show_action;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_COMEDY)) {
                return R.string.detail_feature_show_comedy;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_MUSIC)) {
                return R.string.detail_feature_show_music;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_PARADE)) {
                return R.string.detail_feature_show_parade;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_CHARACTER)) {
                return R.string.detail_feature_show_character;
            } else {
                return R.string.detail_feature_show;
            }
        } else if (poi instanceof Dining) {
            if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_FINE_DINING)) {
                return R.string.detail_feature_dining_fine;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_CASUAL_DINING)) {
                return R.string.detail_feature_dining_casual;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_QUICK_SERVICE)) {
                return R.string.detail_feature_dining_quick;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_SNACKS)) {
                return R.string.detail_feature_dining_snack;
            } else {
                return R.string.detail_feature_dining;
            }
        } else if (poi instanceof Lockers) {
            if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_LOCKER_TYPE_RENTAL)) {
                return R.string.detail_feature_lockers_rental;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_LOCKER_TYPE_RIDE)) {
                return R.string.detail_feature_lockers_ride;
            } else {
                return R.string.detail_feature_lockers_ride;
            }
        } else if (poi instanceof Shop) {
            if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_TYPE_APPAREL_ACCESSORIES)) {
                return R.string.detail_feature_apparel_accessories;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_TYPE_COLLECTIBLES)) {
                return R.string.detail_feature_collectibles;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_TYPE_GAMES_NOVELTIES)) {
                return R.string.detail_feature_games_novelties;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_TYPE_FOOD_SPECIALTIES)) {
                return R.string.detail_feature_food_specialties;
            } else if (poi.isSubType(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_TYPE_HEALTH_BEAUTY)) {
                return R.string.detail_feature_health_beauty;
            } else {
                return R.string.detail_feature_shopping;
            }
        } else if (poi instanceof Entertainment) {
            return R.string.detail_feature_entertainment;
        } else {
            return null;
        }
    }
}