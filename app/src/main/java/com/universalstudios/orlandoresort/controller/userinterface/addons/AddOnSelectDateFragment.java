package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views.OnDateChangedListener;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory.GetPriceInventoryRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory.GetPriceInventoryRequest.Builder;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory.GetPriceInventoryResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory.PriceInventoryEvent;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;
import com.universalstudios.orlandoresort.view.custom_calendar.EventCalendarView;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;



public class AddOnSelectDateFragment extends AddOnsFragment implements OnDateChangedListener, EventCalendarView.OnMonthChangeListener {
    private static final String TAG = AddOnSelectDateFragment.class.getSimpleName();

    private static final String KEY_STATE_IS_CALL_IN_PROGRESS = "key_state_is_call_in_progress";

    private EventCalendarView mCalendarContainer;
    private ViewGroup mContainer;
    private ProgressBar mLoading;

    private boolean mIsCallInProgress;
    private TridionConfig mTridionConfig;

    public static AddOnSelectDateFragment newInstance() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance");
        }

        // Create a new fragment instance
        AddOnSelectDateFragment fragment = new AddOnSelectDateFragment();
        // Get arguments passed in, if any
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAttach");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate");
        }

        Bundle args = getArguments();
        if (args == null) {
        }
        // Otherwise, set incoming parameters
        else {
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            mIsCallInProgress = false;
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {
            mIsCallInProgress = savedInstanceState.getBoolean(KEY_STATE_IS_CALL_IN_PROGRESS, false);
        }
        mTridionConfig = IceTicketUtils.getTridionConfig();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView");
        }

        View view = inflater.inflate(R.layout.fragment_add_on_select_date, container, false);
        TextView mTitle = (TextView) view.findViewById(R.id.fragment_add_on_select_date_title);
        TextView mDescription = (TextView) view.findViewById(R.id.fragment_add_on_select_date_description);
        TextView mSelectDate = (TextView) view.findViewById(R.id.fragment_add_on_select_date_select_date);
        mCalendarContainer = (EventCalendarView) view.findViewById(R.id.fragment_add_on_select_date_calendar);
        mContainer = (ViewGroup) view.findViewById(R.id.fragment_add_on_select_date_container);
        mLoading = (ProgressBar) view.findViewById(R.id.fragment_add_on_select_date_loading);

        mTridionConfig = IceTicketUtils.getTridionConfig();
        mSelectDate.setText(mTridionConfig.getSelectDateLabel());

        if (mCallbacks != null) {
            AddOnsState state = mCallbacks.getAddOnState();
            TridionLabelSpec productSpec = state.getProductTridionSpec();
            if (productSpec != null) {
                mTitle.setText(productSpec.getTitle());
                mSelectDate.setText(productSpec.getDateSelectorHeading());
                mDescription.setText(Html.fromHtml(productSpec.getDateSectionSummary()));
            }

            if (state.getPersonalizationExtrasProduct() != null) {
                EventCalendarView calendarView = (EventCalendarView) LayoutInflater.from(getContext())
                        .inflate(R.layout.event_calendar_view, mCalendarContainer);
                Date endDate = state.getPersonalizationExtrasProduct().getEndDate();
                Integer rollingAvailability = state.getPersonalizationExtrasProduct().getRollingAvailability();
                Integer dayLimit = getDayLimit(endDate, rollingAvailability);
                calendarView.init(null, dayLimit);
                calendarView.setOnDateChangedListener(this);
                calendarView.setOnMonthChangedListener(this);
                calendarView.setInitialDate();
            }
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewCreated");
        }

        mCallbacks.buttonEnabled(false);
        getEventInventory();
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
        outState.putBoolean(KEY_STATE_IS_CALL_IN_PROGRESS, mIsCallInProgress);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStop");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onDestroyView");
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
    public void onDetach() {
        super.onDetach();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onDetach");
        }
    }

    private void showViewBasedOnState() {
        if (mIsCallInProgress) {
            showLoadingView();
        } else {
            showCalendarView();
        }
    }

    private void showLoadingView() {
        mLoading.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.GONE);
    }

    private void showCalendarView() {
        mLoading.setVisibility(View.GONE);
        mContainer.setVisibility(View.VISIBLE);
    }

    private PriceInventoryEvent createEvent(Date start, Date end, String partNumber, Integer quantity) {
        PriceInventoryEvent event = new PriceInventoryEvent();
        event.setStartDate(start);
        event.setEndDate(end);
        event.setPartNumber(partNumber);
        event.setQuantity(quantity);
        return event;
    }

    private void getEventInventory() {
        if (mCalendarContainer != null) {
            Date startDate = mCalendarContainer.getFirstDayInCurrentMonth();
            Date today = getToday();
            //if the starting date is before today, use today's date
            startDate = startDate.after(today) ? startDate : today;
            Date endDate = mCalendarContainer.getLastDayInCurrentMonth();
            getEventInventory(startDate, endDate);
        }
    }

    private Date getToday() {
        Calendar first = Calendar.getInstance();
        first.set(Calendar.HOUR_OF_DAY, 0);
        first.set(Calendar.MINUTE, 0);
        first.set(Calendar.SECOND, 0);
        first.set(Calendar.MILLISECOND, 0);
        return first.getTime();
    }

    private void getEventInventory(Date startDate, Date endDate) {
        if (!mIsCallInProgress) {
            mIsCallInProgress = true;

            Builder requestBuilder = new GetPriceInventoryRequest.Builder(this);
            if (mCallbacks != null) {
                AddOnsState state = mCallbacks.getAddOnState();
                if (state.getPersonalizationExtrasProduct() != null
                        && state.getPersonalizationExtrasProduct().isComboProduct()) {
                    Map<String, List<String>> agePartNumberMap = state.getPersonalizationExtrasProduct().getComboQuantitySelectors();
                    for (String key : state.getAgeQuantityMap().keySet()) {
                        Integer quantity = state.getAgeQuantityMap().get(key);
                        List<String> partNumbers = agePartNumberMap.get(key);
                        if (quantity != null && quantity > 0 && partNumbers != null) {
                            for (String partNumber : partNumbers) {
                                requestBuilder.addPriceInventoryEvent(createEvent(startDate, endDate, partNumber, quantity));
                            }
                        }
                    }
                } else {
                    if (state.getAgeQuantityMap() != null && state.getAgePartNumberMap() != null) {
                        for (String key : state.getAgeQuantityMap().keySet()) {
                            Integer quantity = state.getAgeQuantityMap().get(key);
                            List<String> partNumbers = state.getAgePartNumberMap().get(key);
                            if (quantity != null && quantity > 0 && partNumbers != null) {
                                for (String partNumber : partNumbers) {
                                    requestBuilder.addPriceInventoryEvent(createEvent(startDate, endDate, partNumber, quantity));
                                }
                            }
                        }
                    } else {
                        if (BuildConfig.DEBUG) {
                            Log.w(TAG, "getEventInventory: ticket maps null");
                        }
                    }
                }
            }

            GetPriceInventoryRequest request = requestBuilder.build();
            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();

        }
        showViewBasedOnState();
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        mIsCallInProgress = false;

        if (networkResponse instanceof GetPriceInventoryResponse) {
            if (mCallbacks != null) {
                AddOnsState state = mCallbacks.getAddOnState();
                state.setInventoryResponse((GetPriceInventoryResponse) networkResponse);
                boolean enableButton = mCalendarContainer.setDates(state.getInventoryResponse().getPriceInventoryEventAvailabilities(), state.getSelectedDate());
                mCallbacks.buttonEnabled(enableButton);
                if (!enableButton){
                    Toast.makeText(getActivity(),mTridionConfig.getEr65(),Toast.LENGTH_SHORT).show();

                }

            }
            else {
                Toast.makeText(getContext(), mTridionConfig.getEr65(), Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
        }
        showViewBasedOnState();
    }

    @Override
    public void onDateChanged(Date selectedDate) {
        if (selectedDate == null) {
            mCallbacks.buttonEnabled(false);
            return;
        }

        if (mCallbacks != null) {
            AddOnsState state = mCallbacks.getAddOnState();
            state.setSelectedDate(selectedDate);

            mCallbacks.buttonEnabled(true);
            mCallbacks.updateSubtotal();
        }
    }

    @Override
    public void onMonthChange() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onMonthChange() called");
        }
        getEventInventory();
    }

    /**
     * Determine the dayLimit for the calendar based on the products endDate and rollingAvailability
     * if available
     */
    @Nullable
    private Integer getDayLimit(@Nullable Date endDate, @Nullable Integer rollingAvailability) {
        Integer dayLimit = null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date current = calendar.getTime();

        if (endDate != null && endDate.getTime() > current.getTime()) {
            dayLimit = (int) TimeUnit.MILLISECONDS.toDays(endDate.getTime() - current.getTime());
            if (rollingAvailability != null) {
                dayLimit = dayLimit < rollingAvailability ? dayLimit : rollingAvailability;
            }
        } else if (rollingAvailability != null) {
            dayLimit = rollingAvailability;
        }

        return dayLimit;
    }
}
