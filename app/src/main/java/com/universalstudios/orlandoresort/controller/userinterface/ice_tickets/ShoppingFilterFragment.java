package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.account.AccountUtils;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.FullScreenLoadingView;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkDialogFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.databinding.CommerceShoppingFilterFragmentBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Entitlements.ByDate.request.GetTicketsByDateRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Entitlements.ByDate.response.CombinedSeasonalTicket;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Entitlements.ByDate.response.GetTicketsByDateResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Entitlements.ByDate.response.SeasonalTicketDetail;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCard;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCardsPage;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceUIControl;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.UIControlField;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetCardsRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses.GetCardsResponse;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.view.custom_calendar.TicketType;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetCardsRequest.DEFAULT_TICKETS_PATH;
import static com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetCardsRequest.DEFAULT_TICKET_BMG_PATH;
import static com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetCardsRequest.DEFAULT_TICKET_UEP_PATH;
import static com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetCardsRequest.DEFAULT_UEP_PATH;
import static com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetCardsRequest.ONE_DAY_VISIT_STR_IDENTIFIER;
import static com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetCardsRequest.UEP_IDENTIFIER;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: ShoppingFilterFragment
 * Class Description: Fragment to display the filters for shopping
 */
public class ShoppingFilterFragment extends NetworkDialogFragment implements ControlChangeListener,
        View.OnClickListener {
    public static final String TAG = ShoppingFilterFragment.class.getSimpleName();

    private static final String ARG_TICKET_TYPE = "ARG_TICKET_TYPE";
    private static final String ARG_SERVICE_IDENTIFIER = "ARG_SERV_ID";
    private static final String ARG_LOAD_DEFAULT = "ARG_LOAD_DEFAULT";
    public static final int CALENDAR_MONTHS_RANGE = 6;

    private LinearLayout layoutControlsView;
    private TextView mTitleView;
    private FullScreenLoadingView mLoadingView;
    private Button btnApply;
    private ImageButton btnCancel;

    private TicketType mTicketType;
    private List<CommerceCard> cards;
    private OnFilterAppliedListener listener;

    private String mCurrentServiceIdentifier;

    private Map<String, CombinedSeasonalTicket> mCalendarResultMap;

    private TridionConfig mTridionConfig;
    private boolean mLoadDefault;

    /**
     * Factory method for creating a new instance of ShoppingFilterFragment
     *
     * @return new instance of ShoppingFilterFragment
     */
    public static ShoppingFilterFragment newInstance(TicketType type, String serviceIdentifier, boolean loadDefault) {
        ShoppingFilterFragment filterFragment = new ShoppingFilterFragment();
        Bundle b = new Bundle();
        b.putSerializable(ARG_TICKET_TYPE, type);
        b.putString(ARG_SERVICE_IDENTIFIER, serviceIdentifier);
        b.putBoolean(ARG_LOAD_DEFAULT, loadDefault);
        filterFragment.setArguments(b);
        return filterFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null == savedInstanceState) {
            mTicketType = (TicketType) getArguments().getSerializable(ARG_TICKET_TYPE);
            mCurrentServiceIdentifier = getArguments().getString(ARG_SERVICE_IDENTIFIER);
            mLoadDefault = getArguments().getBoolean(ARG_LOAD_DEFAULT);

            mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());

            sendGetTicketByDateRequest();
        }
        mTridionConfig = IceTicketUtils.getTridionConfig();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CommerceShoppingFilterFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.commerce_shopping_filter_fragment, container, false);
        binding.setTridion(mTridionConfig);
        View layout = binding.getRoot();
        mTitleView = binding.titleTextView;
        layoutControlsView = binding.filterLayoutControls;
        btnCancel = binding.btnCancel;
        btnApply = binding.btnApply;

        mTitleView.setText(getTitleString());
        btnCancel.setOnClickListener(this);
        btnApply.setOnClickListener(this);

        return layout;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (null != getDialog()) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    public void setOnFilterAppliedListener(OnFilterAppliedListener listener) {
        this.listener = listener;
    }

    /**
     * Loads the default controls and gets the default tickets
     */
    private void loadDefaultsFortype() {
        switch (mTicketType) {
            case TYPE_TICKETS:
                CommerceUiBuilder.getCurrentFilter().clearTicketsSettings();
                getNewCardsRequest(DEFAULT_TICKETS_PATH, TicketType.TYPE_TICKETS);
                break;
            case TYPE_TICKET_BMG_BUNDLE:
                CommerceUiBuilder.getCurrentFilter().clearTicketsSettings();
                getNewCardsRequest(DEFAULT_TICKET_BMG_PATH, TicketType.TYPE_TICKET_BMG_BUNDLE);
                break;
            case TYPE_TICKET_UEP_BUNDLE:
                CommerceUiBuilder.getCurrentFilter().clearTicketsSettings();
                getNewCardsRequest(DEFAULT_TICKET_UEP_PATH, TicketType.TYPE_TICKET_UEP_BUNDLE);
                break;
            case TYPE_EXPRESS:
                CommerceUiBuilder.getCurrentFilter().clearExpressPassSettings();
                getNewCardsRequest(DEFAULT_UEP_PATH, TicketType.TYPE_EXPRESS);
                break;
        }
        mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
    }

    @Override
    public void onControlIdentifierChanged(String identifier, Date date) {
        if(identifier == null) {
            if(BuildConfig.DEBUG){
                Log.e(TAG, "The identifier variable for the onControlIdentifierChanged method should never be null");
            }
            return;
        }

        //this checks whether the control changed isn't selecting the same identifier only and not dates
        if (identifier.equals(mCurrentServiceIdentifier) && date == null) {
            return;
        }

        TicketType ticketType;
        if (identifier.toUpperCase().contains(UEP_IDENTIFIER)) {
            ticketType = TicketType.TYPE_EXPRESS;
            if(!CommerceUiBuilder.getCurrentFilter().hasExpressBeenChangedManually) {
                CommerceUiBuilder.getCurrentFilter().numExpressTickets = -1;
            }
        } else {
            ticketType = TicketType.TYPE_TICKETS;
        }


        Date originalDate = null;
        if(ticketType != null){
             originalDate = IceTicketUtils.retrieveSelectedCalendarDate(ticketType);
        }

        if (date == null) {
            // If this is the 1-day calendar tab or a UEP calendar, include the selected date
            // from SharedPreferences in the cards request
            if(identifier.toUpperCase().contains(UEP_IDENTIFIER) || identifier.toUpperCase().contains(ONE_DAY_VISIT_STR_IDENTIFIER)) {
                date = originalDate;
            }

        }

        mCurrentServiceIdentifier = identifier;
        if (null != getActivity()) {
            mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
        }

        // Include the selected date in the cards request
        if (date != null) {

            //if you changes your identifier (days or parks) then you shouldn't have a date based change occurring
            if(!identifier.equals(mCurrentServiceIdentifier) && date.compareTo(originalDate) == 0) {
                return;
            }

            // Include the seasons for the date in the request
            List<String> seasons;
            if (identifier.toUpperCase().contains(UEP_IDENTIFIER)) {
                seasons = null;
            } else {
                seasons  = getSeasonsForDate(date);
            }
            getNewCardsRequest(identifier, date, seasons);
        }
        // We set the ticket type to refer to the default date the respective ticket type has saved
        else {
            getNewCardsRequest(identifier, ticketType);
        }
    }

    /**
     * Returns a List of the applicable different seasons for a passed in Date.
     *
     * @param date The date to check for applicable seasons
     * @return The List of season Strings
     */
    private List<String> getSeasonsForDate(Date date) {
        List<String> seasons = new ArrayList<>();
        if (date == null) {
            return seasons;
        }
        SimpleDateFormat calDateFormat = new SimpleDateFormat(IceTicketUtils.CAL_DATE_FORMAT, Locale.US);
        String dateStr = calDateFormat.format(date);
        CombinedSeasonalTicket ticket = null;

        if (mCalendarResultMap != null) {
            ticket = mCalendarResultMap.get(dateStr);
        }
        if (ticket == null) {
            return seasons;
        }
        List<SeasonalTicketDetail> ticketDetails = ticket.getDetail();
        // Add a season string for every season available for this ticket
        // entry in the map (there may be duplicates so use a Set to enforce unique entries)
        Set<String> seasonsSet = new HashSet<>();
        for (SeasonalTicketDetail ticketDetail : ticketDetails) {
            if (ticketDetail.getSeason() == null) {
                continue;
            }
            seasonsSet.add(ticketDetail.getSeason());
        }
        seasons = new ArrayList<>(seasonsSet);
        return seasons;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnApply.getId()) {
            //Apply filter
            if (null != listener) {
                listener.onFilterApplied(cards, mCurrentServiceIdentifier);
                dismiss();
            }
        } else if (v.getId() == btnCancel.getId()) {
            dismiss();
        }
    }

    private void sendGetTicketByDateRequest() {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.MONTH, CALENDAR_MONTHS_RANGE);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date sixMonthsAhead = calendar.getTime();

        GetTicketsByDateRequest request = new GetTicketsByDateRequest.Builder(this)
                .setStartDate(today)
                .setEndDate(sixMonthsAhead)
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {

        // If the returned response is null or doesn't have a success code,
        // then the loading view should be dismissed (if it was shown in the first place)
        // and a warning logged
        if (networkResponse == null || !networkResponse.isHttpStatusCodeSuccess()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (mLoadingView != null) {
                        mLoadingView.dismiss();
                    }
                }
            });
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "Response was null or returned with a failure code");
            }
        }

        // This is here for NetworkFragment
        if (networkResponse instanceof GetTicketsByDateResponse) {

            GetTicketsByDateResponse ticketsByDateResponse = (GetTicketsByDateResponse) networkResponse;
            mCalendarResultMap = ticketsByDateResponse.getResult();

            // This is the first time we are opening this filter window in the app lifecycle
            if (null == mCurrentServiceIdentifier) {
                loadDefaultsFortype();
            }
            // We have opened the filter window previously, but are returning to it (mCurrentServiceIdentifier
            // is still set to its previous tab id)
            else {

                TicketType ticketType = IceTicketUtils.getTicketTypeForIdentifier(mCurrentServiceIdentifier);

                // If this is the 1-day calendar tab, include the selected date from SharedPreferences
                // in the cards request
                if (mCurrentServiceIdentifier.toUpperCase().contains(UEP_IDENTIFIER) || mCurrentServiceIdentifier.contains(ONE_DAY_VISIT_STR_IDENTIFIER)) {

                    Date date = IceTicketUtils.retrieveSelectedCalendarDate(ticketType);
                    // Include the seasons for the date in the request
                    List<String> seasons = getSeasonsForDate(date);

                    //uep will not come back with the correct values if we send seasons
                    if(mCurrentServiceIdentifier.toUpperCase().contains(UEP_IDENTIFIER)) {
                        if(!CommerceUiBuilder.getCurrentFilter().hasExpressBeenChangedManually) {
                            CommerceUiBuilder.getCurrentFilter().numExpressTickets = -1;
                        }
                        seasons = null;
                    }

                    getNewCardsRequest(mCurrentServiceIdentifier, date, seasons);

                }
                // This is a multi-day tab without a calendar, so create and fire the cards request without the dates
                else {
                    loadDefaultsFortype();
                }
            }
        } else if (networkResponse instanceof GetCardsResponse) {


            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (mLoadingView != null) {
                        mLoadingView.dismiss();
                    }
                }
            });

            GetCardsResponse response = (GetCardsResponse) networkResponse;

            // Loop through the different pieces of the filter screen as
            // instructed from the response
            CommerceCardsPage page = null;
            if (response.getResult() != null) {
                page = response.getResult().getPage();
            }
            if (page == null) {
                // Fail-fast if page is null
                return;
            }

            List<CommerceUIControl> controls = page.getControls();
            cards = page.getCards();

            layoutControlsView.removeAllViews();
            //TODO address with TA23460
            String titleLabel = mTicketType.equals(TicketType.TYPE_TICKETS) || mTicketType.equals(TicketType.TYPE_TICKET_BMG_BUNDLE) ? mTridionConfig.getBuyTicketsFilterSectionDaysHeader() : mTridionConfig.getPageHeaderEPUpsellText();
            CommerceUiBuilder.addTitleLabel(titleLabel, layoutControlsView, getContext());

            for (int i = 0; i < controls.size(); i++) {
                CommerceUIControl control = controls.get(i);
                String cardTitle = "";
                if (cards != null && !cards.isEmpty()) {
                    CommerceCard commerceCard = cards.get(0);
                    if (commerceCard != null &&
                        commerceCard.getGroups() != null && commerceCard.getGroups().size() > 0) {
                        int numberOfParks = commerceCard.getGroups().get(0).getNumParks();
                        cardTitle = returnCardTitleForParkNum(numberOfParks);
                    }
                }

                // Per-populate tickets on the first load only
                if (mLoadDefault) {
                    mLoadDefault = false;
                    listener.onFilterApplied(cards, mCurrentServiceIdentifier);
                }

                if(control.getControlType().toUpperCase().equals(CommerceUiBuilder.CONTROL_TYPE_CALENDAR)) {
                    List<UIControlField> controlFields = control.getFields();
                    if(controlFields == null){
                        controlFields = new ArrayList<>();
                    }
                    UIControlField calendarDate = getCalendarLimitFromControls(controls);
                    controlFields.add(calendarDate);
                }

                CommerceUiBuilder.setControlChangeListener(this);
                CommerceUiBuilder.createControlFromType(
                            control,
                            getActivity(),
                            layoutControlsView,
                            cardTitle,
                            mCalendarResultMap,
                            (i < controls.size() - 1));
            }
        }
    }

    private String getTitleString() {
        if (mTicketType == null) {
            return "";
        }

        TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();
        switch (mTicketType) {
            case TYPE_TICKETS:
                return tridionConfig.getAnnualPassHeaderLabel();
            case TYPE_TICKET_BMG_BUNDLE:    // TODO Set to proper label when it is determined
                return tridionConfig.getAnnualPassHeaderLabel();
            case TYPE_TICKET_UEP_BUNDLE:    // TODO Set to proper label when it is determined
                return tridionConfig.getAnnualPassHeaderLabel();
            case TYPE_EXPRESS:
                return tridionConfig.getSCExpressPassLabel();
            case TYPE_ADDONS:
            default:
                return tridionConfig.getSCExtrasLabel();
        }

    }

    private String returnCardTitleForParkNum(int numParks) {
        TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();

        switch(numParks) {
            case 1:
                return tridionConfig.getUEP1ParkDescription();
            case 2:
                return tridionConfig.getUEP2ParkDescription();
            default:
                //we should never hit this, but our default for express pass is 3 park
            case 3 :
                return tridionConfig.getUEP3ParkDescription();

        }
    }

    /**
     * Returns the date limiter for the calendar
     * @param controls
     * @return
     */
    private UIControlField getCalendarLimitFromControls(List<CommerceUIControl> controls) {
        if(null != controls) {
            for(CommerceUIControl control : controls) {
                if(null != control && control.isRollingAvailability()) {
                    List<UIControlField> controlFields = control.getFields();
                    if(controlFields != null && controlFields.size() > 0) {
                        return controlFields.get(0);
                    }
                }
            }
        }

        return null;
    }

    private void getNewCardsRequest(String identifier, TicketType ticketType) {
        Date date = IceTicketUtils.retrieveSelectedCalendarDate(ticketType);
        getNewCardsRequest(identifier, date, null);
    }

    private void getNewCardsRequest(String identifier, Date date, List<String> seasons) {
        TicketFilterInfo ticketFilterInfo = CommerceUiBuilder.getCurrentFilter();
        GetCardsRequest.Builder requestBuilder = new GetCardsRequest.Builder(this)
                .setCards(identifier)
                .setDates(date)
                .setSeasons(seasons)
                .setGeolocation(AccountUtils.getGeoLocationType(ticketFilterInfo.isFloridaResident()))
                .setTicketsPageNumberOfDays(ticketFilterInfo.getNumberOfDays())
                .setTicketsPageNumberOfAdults(ticketFilterInfo.getNumberOfAdultTickets())
                .setTicketsPageNumberOfChildren(ticketFilterInfo.getNumberOfChildTickets())
                .setTicketsPageFloridaResidentFlag(ticketFilterInfo.isFloridaResident())
                .setTicketsPageSelectedTicketDate(date);
        boolean skipInventory = false;
        // Skip inventory for bundles
        if (mTicketType == TicketType.TYPE_TICKET_BMG_BUNDLE || mTicketType == TicketType.TYPE_TICKET_UEP_BUNDLE) {
            skipInventory = true;
        }
        requestBuilder.setSkipInventory(Boolean.toString(skipInventory));
        GetCardsRequest request = requestBuilder.build();

        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }
}
