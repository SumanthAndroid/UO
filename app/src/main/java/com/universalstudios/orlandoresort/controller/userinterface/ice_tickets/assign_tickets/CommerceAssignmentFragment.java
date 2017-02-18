package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.account.AccountUtils;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets.AssignNamesAssignmentAdapter.AssignNamesAssignmentActionCallback;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.CommerceAssignmentFragmentBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.TravelPartyMember;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.CreateTravelPartyRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.GetTravelPartyRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.CreateTravelPartyResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetTravelPartyResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.TravelPartyMembers;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.UpdateItem.request.UpdateItemRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.UpdateItem.response.UpdateItemResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.AddOnTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.CartTicketGroupingRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ExpressPassTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ParkTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.Ticket;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupingResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.analytics.CrashAnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;
import com.universalstudios.orlandoresort.model.state.account.SessionCache;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/10/16.
 * Class: CommerceAssignmentFragment
 * Class Description: Fragment to enter names for assigning tickets
 */
public class CommerceAssignmentFragment extends NetworkFragment implements OnDateSetListener, AssignNamesAssignmentActionCallback {
    private static final String TAG = CommerceAssignmentFragment.class.getSimpleName();

    private static final String ARG_GUEST = "ARG_GUEST";
    private static final String KEY_STATE_CALL_IN_PROGRESS = "KEY_STATE_CALL_IN_PROGRESS";
    private static final String DATE_FORMAT_DOB_DISPLAY = "MM/dd/yyyy";
    private static final String DATE_FORMAT_DOB_ATTRIBUTE = "MM-dd-yyyy";

    private AssignNamesAssignmentAdapter mAdadpter;
    private CommerceAssignmentFragmentBinding binding;
    private CommerceAssignmentViewModel mViewModel;

    private OnTicketAssignedListener mListener;
    private CommerceAssignmentChangeListener assignmentChangeListener;

    private List<AssignableTicketItem> displayItems = new ArrayList<>();
    private PartyMember mGuest;

    private List<TravelPartyMember> mPartyMembers;
    private TravelPartyMember mSelectedMember;

    private boolean mIsCallInProgress;
    private TridionConfig mTridionConfig;
    private AssignableTicketItem mCurrentAssignableTicketItem;

    public static CommerceAssignmentFragment newInstance(PartyMember guest) {
        CommerceAssignmentFragment fragment = new CommerceAssignmentFragment();
        Bundle b = new Bundle();
        b.putSerializable(ARG_GUEST, guest);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAttach");
        }
        Fragment parentFragment = getParentFragment();
        // Check if parent fragment (if there is one) implements the interface
        if (parentFragment != null && parentFragment instanceof OnTicketAssignedListener) {
            mListener = (OnTicketAssignedListener) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (activity != null && activity instanceof OnTicketAssignedListener) {
            mListener = (OnTicketAssignedListener) activity;
        }
        // If neither implements the interface, log a warning
        else if (mListener == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement CommerceAssignmentChangeListener");
            }
        }


        if (parentFragment != null && parentFragment instanceof CommerceAssignmentChangeListener) {
            assignmentChangeListener = (CommerceAssignmentChangeListener) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (activity != null && activity instanceof CommerceAssignmentChangeListener) {
            assignmentChangeListener = (CommerceAssignmentChangeListener) activity;
        }
        // If neither implements the interface, log a warning
        else if (assignmentChangeListener == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement CommerceAssignmentChangeListener");
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mGuest = (PartyMember) bundle.getSerializable(ARG_GUEST);
        }

        if (savedInstanceState != null) {
            mIsCallInProgress = savedInstanceState.getBoolean(KEY_STATE_CALL_IN_PROGRESS, false);
        } else {
            mIsCallInProgress = false;
            sendPageViewAnalytics();
        }
        mTridionConfig = IceTicketUtils.getTridionConfig();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CommerceAssignmentFragmentBinding.inflate(inflater, container, false);

        mAdadpter = new AssignNamesAssignmentAdapter(mTridionConfig, this);
        binding.fragmentAssignNameRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.fragmentAssignNameRecyclerview.setAdapter(mAdadpter);
        mAdadpter.setGuestInfoItem(mGuest);
        mViewModel = new CommerceAssignmentViewModel();
        binding.setData(mViewModel);

        if (mGuest.ageType == PartyMember.AGE_TYPE_ADULT) {
            if (!TicketAssignmentUtils.primaryAlreadySelected()) {
                //if the user has not selected a primary, show checkbox as unchecked
                mAdadpter.setGuestInfoItemChecked(false);
            } else {
                if (TicketAssignmentUtils.primaryMemberSelected(mGuest)) {
                    //if the selected primary is the same as mGuest, show the checkbox as checked
                    mAdadpter.setGuestInfoItemChecked(true);
                } else {
                    //if the selected primary is not mGuest do not show the checkbox
                    mAdadpter.setGuestInfoItemChecked(null);
                }
            }
        } else {
            //do no show checkbox for non-adult
            mAdadpter.setGuestInfoItemChecked(null);
        }

        if (null != assignmentChangeListener) {
            assignmentChangeListener.onShowAddGuestChanged(false);
        }

        populateUnassignedTickets();
        enableDisableButton();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewCreated");
        }

        if (AccountStateManager.isUserLoggedIn() && !mIsCallInProgress) {
            getTravelParty();
        }

        showViewBasedOnState();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_STATE_CALL_IN_PROGRESS, mIsCallInProgress);

    }

    @Override
    public void onDoneClicked() {
        UserInterfaceUtils.closeKeyboard(binding.getRoot());
        if (!mIsCallInProgress) {
            if (TicketAssignmentUtils.primarySelected()) {
                savePrimarySelectedName();
            }

            if (AccountStateManager.isUserLoggedIn()) {
                handleRegisteredGuestAssignTicket();
            } else {
                handleUnregisteredGuestAssignTicket();
            }
        }
        sendDoneClickedAnalytics();
    }

    @Override
    public void onTextChanged() {
        enableDisableButton();
    }

    @Override
    public void onCheckedChanged(boolean isChecked) {
        TicketAssignmentUtils.setPrimarySelected(isChecked);
        if (isChecked) {
            for (AssignableTicketItem displayItem : displayItems) {
                if (displayItem.isAnnualPass()) {
                    String dateText = displayItem.getDobText();
                    if (!TextUtils.isEmpty(dateText)) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DOB_DISPLAY);
                            Date primaryDob = sdf.parse(dateText);
                            SessionCache.setThisIsMeDob(primaryDob);
                            break;
                        } catch (ParseException e) {
                            CrashAnalyticsUtils.logHandledException(e);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onSpinnerItemSelected(int position) {
        //Last item is "Add New Name"
        if (position <= mPartyMembers.size() - 1) {
            mSelectedMember = mPartyMembers.get(position);
            mAdadpter.setShowGuestInfoSpinnerArrow(true);
            hideGuestNameEntry();
        } else {
            mSelectedMember = null;
            mAdadpter.setShowGuestInfoSpinnerArrow(false);
            showGuestNameEntry();
        }
        enableDisableButton();
    }

    @Override
    public void onTicketSelected(AssignableTicketItem assignableTicketItem) {
        assignableTicketItem.setSelected(!assignableTicketItem.isSelected());
        enableDisableButton();
    }

    @Override
    public void onTicketChecked(AssignableTicketItem assignableTicketItem, boolean isChecked) {
        assignableTicketItem.setSelected(isChecked);
        enableDisableButton();
    }

    @Override
    public void onBirthDateInfoIconClicked() {
        Toast.makeText(getContext(), mTridionConfig.getDOBToolTip(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDatePickerClicked(AssignableTicketItem assignableTicketItem) {
        showDatePicker(assignableTicketItem);
    }

    private void showViewBasedOnState() {
        if (mIsCallInProgress) {
            showLoadingView();
        } else {
            dismissLoadingView();
            if ((mPartyMembers != null && mPartyMembers.size() > 0) || AccountStateManager.isUserLoggedIn()) {
                showSelectPartyMember();
            } else {
                showEnterPartyMember();
            }
        }
    }

    private void showLoadingView() {
        //show progressbar
        mViewModel.setShowLoading(true);
        mAdadpter.setShowGuestInfoEntryForm(false);
    }

    private void dismissLoadingView() {
        mViewModel.setShowLoading(false);
    }

    private void showSelectPartyMember() {
        mAdadpter.setShowGuestInfoEntryForm(true);
        mAdadpter.setShowGuestInfoNameEntry(false);
        mAdadpter.setShowGuestInfoSpinner(true);
    }

    private void showEnterPartyMember() {
        mAdadpter.setShowGuestInfoEntryForm(true);
        mAdadpter.setShowGuestInfoNameEntry(true);
        mAdadpter.setShowGuestInfoSpinner(false);
    }

    private void hideGuestNameEntry() {
        mAdadpter.setShowGuestInfoNameEntry(false);
    }

    private void showGuestNameEntry() {
        mAdadpter.setShowGuestInfoNameEntry(true);
    }

    private void populateUnassignedTickets() {
        if (mGuest.ageType == PartyMember.AGE_TYPE_ADULT) {
            displayItems = TicketAssignmentUtils.instance().getAdultTicketOptionsForGuest(mGuest);
        } else {
            displayItems = TicketAssignmentUtils.instance().getChildTicketOptionsForGuest(mGuest);
        }
        //ueps will show regardless of an adult or child
        displayItems.addAll(TicketAssignmentUtils.instance().getUEPTicketOptionsForGuest(mGuest));
        // addons for guest
        displayItems.addAll(TicketAssignmentUtils.instance().getAddOnOptionsForGuest(mGuest));

        mAdadpter.addAssignableItems(displayItems);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dateString = String.format(Locale.US, "%02d/%02d/%04d", month + 1, dayOfMonth, year);
        if (TicketAssignmentUtils.primarySelected()) {
            if (!TextUtils.isEmpty(dateString)) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DOB_DISPLAY);
                    Date primaryDob = sdf.parse(dateString);
                    SessionCache.setThisIsMeDob(primaryDob);
                } catch (ParseException e) {
                    CrashAnalyticsUtils.logHandledException(e);
                }
            }
        }

        if (mCurrentAssignableTicketItem != null) {
            mCurrentAssignableTicketItem.setDobText(dateString);
            mCurrentAssignableTicketItem = null;
            enableDisableButton();
        }
    }

    /**
     * Shows the Date of Birth date picker dialog
     */
    public void showDatePicker(AssignableTicketItem assignableTicketItem) {
        mCurrentAssignableTicketItem = assignableTicketItem;
        Calendar today = Calendar.getInstance();
        Calendar minCal = Calendar.getInstance();
        minCal.set(Calendar.YEAR, today.get(Calendar.YEAR) - 13);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.AlertDialogStyle_DatePicker, this, minCal.get(Calendar.YEAR),
                minCal.get(Calendar.MONTH), minCal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(today.getTimeInMillis());
        datePickerDialog.show();
    }

    private List<OrderItem> getSelectedOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();
        for (AssignableTicketItem displayItem : displayItems) {
            if (displayItem.isSelected()) {
                //This will not returned orderitems for the ParkTicketgroup/ExpressPassgroups if that group is already assigned to the user
                OrderItem orderItem = null;
                if (null != displayItem.getTicketGroup()) {
                    orderItem = TicketAssignmentUtils.getFirstUnassignedOrderItem(displayItem.getTicketGroup(), mGuest);
                    // Set dob on orderItem if annual pass
                    if (displayItem.isAnnualPass() && !TextUtils.isEmpty(displayItem.getDobText())
                            && orderItem != null) {
                        DateFormat inDateFormat = new SimpleDateFormat(DATE_FORMAT_DOB_DISPLAY, Locale.US);
                        DateFormat outDateFormat = new SimpleDateFormat(DATE_FORMAT_DOB_ATTRIBUTE, Locale.US);
                        try {
                            Date date = inDateFormat.parse(displayItem.getDobText());
                            String formattedDate = outDateFormat.format(date);
                            if (orderItem.getAttributes() == null) {
                                orderItem.setAttributes(new ArrayList<CommerceAttribute>());
                            }
                            // Remove existing birthdate attribute if present
                            Iterator<CommerceAttribute> iterator = orderItem.getAttributes().iterator();
                            while (iterator.hasNext()) {
                                CommerceAttribute attribute = iterator.next();
                                if (attribute != null && attribute.isPartyMemberBirthDate()) {
                                    iterator.remove();
                                }
                            }
                            orderItem.getAttributes().add(CommerceAttribute.createPartyMemberBirthdateAttribute(formattedDate));
                        } catch (ParseException e) {
                            CrashAnalyticsUtils.logHandledException(e);
                        }
                    }
                } else if (null != displayItem.getExpressPassTicketGroup()) {
                    orderItem = TicketAssignmentUtils.getFirstUnassignedOrderItem(displayItem.getExpressPassTicketGroup(), mGuest);
                } else if (displayItem.getAddOnTicketGroup() != null) {
                    orderItem = TicketAssignmentUtils.getFirstUnassignedOrderItem(displayItem.getAddOnTicketGroup(), mGuest);
                }

                if (null != orderItem) {
                    orderItems.add(orderItem);
                }
            }
        }
        return orderItems;
    }

    private boolean isTicketSelected() {
        for (AssignableTicketItem displayItem : displayItems) {
            if (displayItem.isSelected()) {
                return true;
            }
        }
        return false;
    }

    private boolean isSelectedAnnualPassesValid() {
        for (AssignableTicketItem displayItem : displayItems) {
            if (displayItem.isSelected() && displayItem.isAnnualPass()
                    && TextUtils.isEmpty(displayItem.getDobText())) {
                return false;
            }
        }
        return true;
    }

    private void enableDisableButton() {
        if (mSelectedMember == null) {
            String firstName = mAdadpter.getFirstName();
            String lastName = mAdadpter.getLastName();
            String suffix = StringUtils.trimToEmpty(mAdadpter.getSuffix());
            boolean ticketSelected = isTicketSelected() && isSelectedAnnualPassesValid();

            if (ticketSelected && !TextUtils.isEmpty(firstName) && AccountUtils.isValidName(firstName)
                    && !TextUtils.isEmpty(lastName) && AccountUtils.isValidName(lastName) && AccountUtils.isValidSuffix(suffix)) {
                enableButton();
            } else {
                disableButton();
            }
        } else {
            if (isTicketSelected() && isSelectedAnnualPassesValid()) {
                enableButton();
            } else {
                disableButton();
            }
        }
    }

    private void disableButton() {
        mAdadpter.setFooterItem(false);
    }

    private void enableButton() {
        mAdadpter.setFooterItem(true);
    }

    private void assignEnteredNameToTicket(OrderItem orderItem) {
        String first = StringUtils.trimToEmpty(mAdadpter.getFirstName());
        String last = StringUtils.trimToEmpty(mAdadpter.getLastName());
        String suffix = StringUtils.trimToEmpty(mAdadpter.getSuffix());
        assignNameToTicket(orderItem, first, last, suffix, null);
    }

    private void assignSelectedNameToTicket(OrderItem orderItem) {
        String first = mSelectedMember.getFirstName();
        String last = mSelectedMember.getLastName();
        String suffix = mSelectedMember.getSuffix();
        Integer partyMemberId = mSelectedMember.getSequenceId();
        assignNameToTicket(orderItem, first, last, suffix, partyMemberId);
    }

    private void assignNameToTicket(OrderItem orderItem, String first, String last, String suffix, Integer partyMemberId) {
        List<CommerceAttribute> attributes = orderItem.getAttributes();
        if (null == attributes) {
            orderItem.setAttributes(new ArrayList<CommerceAttribute>());
        }

        //remove current assignment
        if (attributes != null && !attributes.isEmpty()) {
            //start from last item to not cause index issues when removing items
            for (int i = attributes.size() - 1; i >= 0; i--) {
                CommerceAttribute attribute = attributes.get(i);
                if (null != attribute) {
                    if (attribute.isAssignmentFirstName() ||
                            attribute.isAssignmentLastName() ||
                            attribute.isAssignmentSuffix() ||
                            attribute.isAssignmentMemberId()) {
                        attributes.remove(i);
                    }
                }
            }
        }

        CommerceAttribute attribute = CommerceAttribute.createAssignmentFirstNameAttribute(first);
        orderItem.getAttributes().add(attribute);



        CommerceAttribute lastnameAttribute = CommerceAttribute.createAssignmentLastNameAttribute(last);
        orderItem.getAttributes().add(lastnameAttribute);

        CommerceAttribute suffixAttribute = CommerceAttribute.createAssignmentSuffixAttribute(suffix);
        orderItem.getAttributes().add(suffixAttribute);

        if (partyMemberId != null) {
            CommerceAttribute partyMemberAttribute = CommerceAttribute.createAssignmentMemberIdAttribute(partyMemberId.toString());
            orderItem.getAttributes().add(partyMemberAttribute);
        }
    }

    /**
     * Saves the ticket assignment primary name values
     */
    private void savePrimarySelectedName() {
        TicketAssignmentUtils.setPrimaryName(getEnteredDataAsPartyMember());
    }

    private List<OrderItem> getDeselectedOrderItems() {
        List<OrderItem> deselectedItems = new ArrayList<>();
        for (AssignableTicketItem displayItem : displayItems) {
            if (!displayItem.isSelected()) {
                OrderItem orderItem = null;

                //logic to determine whether we are deselecting a part ticket or an express pass
                if (displayItem.getTicketGroup() != null &&
                        (!TicketAssignmentUtils.isGroupAssignedToGuest(displayItem.getTicketGroup().getAdultTickets(), mGuest)
                                || !TicketAssignmentUtils.isGroupAssignedToGuest(displayItem.getTicketGroup().getChildTickets(), mGuest))) {
                    orderItem = getOrderItemUnAssignedToUser(displayItem.getTicketGroup());
                } else if (displayItem.getExpressPassTicketGroup() != null &&
                        !TicketAssignmentUtils.isGroupAssignedToGuest(displayItem.getExpressPassTicketGroup(), mGuest)) {
                    orderItem = getOrderItemUnAssignedToUser(displayItem.getExpressPassTicketGroup());
                } else if (displayItem.getAddOnTicketGroup() != null
                        && (!TicketAssignmentUtils.isGroupAssignedToGuest(displayItem.getAddOnTicketGroup().getAdultAddOns(), mGuest)
                        || !TicketAssignmentUtils.isGroupAssignedToGuest(displayItem.getAddOnTicketGroup().getChildAddOns(), mGuest)
                        || !TicketAssignmentUtils.isGroupAssignedToGuest(displayItem.getAddOnTicketGroup().getAllAddOns(), mGuest))) {
                    orderItem = getOrderItemUnAssignedToUser(displayItem.getAddOnTicketGroup());
                }

                if (null != orderItem) {
                    deselectedItems.add(orderItem);
                }
            }
        }

        for (OrderItem orderItem : deselectedItems) {
            for (CommerceAttribute attribute : orderItem.getAttributes()) {
                if (null != attribute) {
                    if (attribute.isAssignmentFirstName() ||
                            attribute.isAssignmentLastName() ||
                            attribute.isAssignmentSuffix() ||
                            attribute.isAssignmentMemberId() ||
                            attribute.isPartyMemberBirthDate()) {
                        attribute.setValue("");
                    }
                }
            }
        }
        return deselectedItems;
    }

    private OrderItem getOrderItemUnAssignedToUser(ParkTicketGroups group) {
        Ticket t;
        if (mGuest.ageType == PartyMember.AGE_TYPE_ADULT) {
            t = group.getAdultTickets();
        } else {
            t = group.getChildTickets();
        }

        for (OrderItem orderItem : t.getOrderItems()) {
            if (!TicketAssignmentUtils.isOrderItemAssignedToGuest(orderItem, mGuest)) {
                return orderItem;
            }
        }
        return null;
    }

    private OrderItem getOrderItemUnAssignedToUser(ExpressPassTicketGroups group) {
        if (group != null && group.getOrderItems() != null) {
            for (OrderItem orderItem : group.getOrderItems()) {
                if (!TicketAssignmentUtils.isOrderItemAssignedToGuest(orderItem, mGuest)) {
                    return orderItem;
                }
            }
        }
        return null;
    }

    private OrderItem getOrderItemUnAssignedToUser(AddOnTicketGroups group) {
        if (group != null) {
            // Adult addons
            if (group.getAdultAddOns() != null && group.getAdultAddOns().getOrderItems() != null) {
                for (OrderItem orderItem : group.getAdultAddOns().getOrderItems()) {
                    if (!TicketAssignmentUtils.isOrderItemAssignedToGuest(orderItem, mGuest)) {
                        return orderItem;
                    }
                }
            }
            // Child addons
            if (group.getChildAddOns() != null && group.getChildAddOns().getOrderItems() != null) {
                for (OrderItem orderItem : group.getChildAddOns().getOrderItems()) {
                    if (!TicketAssignmentUtils.isOrderItemAssignedToGuest(orderItem, mGuest)) {
                        return orderItem;
                    }
                }
            }
            // All-age addons
            if (group.getAllAddOns() != null && group.getAllAddOns().getOrderItems() != null) {
                for (OrderItem orderItem : group.getAllAddOns().getOrderItems()) {
                    if (!TicketAssignmentUtils.isOrderItemAssignedToGuest(orderItem, mGuest)) {
                        return orderItem;
                    }
                }
            }
        }
        return null;
    }

    private void handleUnregisteredGuestAssignTicket() {
        if(mSelectedMember == null) {
            saveInputTravelPartyMember();
        } else {
            addUnregisteredSelectedMemberToOrder();
        }
    }

    private void handleRegisteredGuestAssignTicket() {
        if(mSelectedMember == null) {
            saveInputTravelPartyMember();
        } else {
            addSelectedMemberToOrder();
        }
    }

    private void addUnregisteredSelectedMemberToOrder() {
        List<OrderItem> orderItems = getSelectedOrderItems();

        //update assigned tickets
        orderItems.addAll(getAlreadyAssignedItems());
        //assign newly selected tickets
        for (OrderItem item : orderItems) {
            assignEnteredNameToTicket(item);
        }

        //update guest for deselecting tickets that were just updated
        mGuest = getEnteredDataAsPartyMember();

        orderItems.addAll(getDeselectedOrderItems());
        if (orderItems.isEmpty()) {
            if (mListener != null) {
                mListener.onTicketAssigned();
            }
            return;
        }

        updateCartItems(orderItems);
    }

    private void addSelectedMemberToOrder() {
        List<OrderItem> orderItems = getSelectedOrderItems();

        //update assigned tickets
        orderItems.addAll(getAlreadyAssignedItems());
        for (OrderItem item : orderItems) {
            assignSelectedNameToTicket(item);
        }

        orderItems.addAll(getDeselectedOrderItems());
        if (orderItems.isEmpty()) {
            if (mListener != null) {
                mListener.onTicketAssigned();
            }
            return;
        }

        mIsCallInProgress = true;

        updateCartItems(orderItems);

        showViewBasedOnState();
    }

    private void updateCartItems(List<OrderItem> orderItems) {
        UpdateItemRequest.Builder requestBuilder = new UpdateItemRequest.Builder(this);

        for (OrderItem orderItem : orderItems) {
            String orderItemId = orderItem.getOrderItemId();
            String firstName = orderItem.getAssignedFirstName();
            String lastName = orderItem.getAssignedLastName();
            String memberSuffix = orderItem.getSuffix();
            Integer memberId = orderItem.getPartyMemberId();
            String dob = orderItem.getPartyMemberBirthDate();

            requestBuilder.addOrderItemToUpdate(orderItemId, firstName, lastName, memberSuffix, memberId, dob);
        }

        UpdateItemRequest request = requestBuilder.build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    private PartyMember getEnteredDataAsPartyMember() {
        String first = StringUtils.trimToEmpty(mAdadpter.getFirstName());
        String last = StringUtils.trimToEmpty(mAdadpter.getLastName());
        String suffix = StringUtils.trimToEmpty(mAdadpter.getSuffix());

        PartyMember partyMember = new PartyMember(mGuest.ageType);
        partyMember.firstname = first;
        partyMember.lastname = last;
        partyMember.suffix = suffix;

        return partyMember;
    }

    private List<OrderItem> getAlreadyAssignedItems() {
        List<OrderItem> result = new ArrayList<>();

        //get tickets assigned to guest
        List<OrderItem> assignedOrderItems = TicketAssignmentUtils.instance().getOrderItemsAssignedToGuest(mGuest);
        if (assignedOrderItems != null && assignedOrderItems.size() > 0) {
            for (OrderItem assignedTicket : assignedOrderItems) {
                result.add(assignedTicket);
            }
        }

        return result;
    }

    private void showMessage(final String message) {
        // Post to handler to make sure it is on the main thread
        Handler mHandler = new Handler(getActivity().getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
        });
    }

    /**
     * sort travel party members by first name, then last name
     */
    private void sortTravelPartyMembers() {
        if (mPartyMembers != null && mPartyMembers.size() > 1) {
            Collections.sort(mPartyMembers, new Comparator<TravelPartyMember>() {
                @Override
                public int compare(TravelPartyMember left, TravelPartyMember right) {
                    if (left != null && left.getFirstName() != null) {
                        int result = left.getFirstName() != null ? left.getFirstName().compareToIgnoreCase(right.getFirstName()) : 0;
                        if (result == 0) {
                            result = left.getLastName() != null ? left.getLastName().compareToIgnoreCase(right.getLastName()) : 0;
                            if (result == 0) {
                                result = left.getSuffix() != null ? left.getSuffix().compareToIgnoreCase(right.getSuffix()) : 0;
                            }
                        }
                        return result;
                    } else {
                        if (right != null && right.getFirstName() != null) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                }
            });
        }
    }

    /**
     * remove members of travel party that have already been assigned
     */
    private void removeAssignedTravelPartyMembers() {
        List<TravelPartyMember> assignedTravelParty = new ArrayList<>();

        //add assign adults to list of assigned people
        Map<String, List<OrderItem>> assignedAdults = TicketAssignmentUtils.instance().getAssignedTicketAdults();
        for (Map.Entry<String, List<OrderItem>> entry : assignedAdults.entrySet()) {
            if (entry != null && entry.getValue() != null
                    && entry.getValue().size() > 0 && entry.getValue().get(0) != null) {
                OrderItem orderItem = entry.getValue().get(0);
                String firstName = orderItem.getAssignedFirstName();
                String lastName = orderItem.getAssignedLastName();
                String suffix = orderItem.getSuffix();
                Integer sequenceId = orderItem.getPartyMemberId();
                TravelPartyMember travelPartyMember = new TravelPartyMember(firstName, lastName, suffix, sequenceId);
                assignedTravelParty.add(travelPartyMember);
            }
        }

        //add assign children to list of assigned people
        Map<String, List<OrderItem>> assignedChildren = TicketAssignmentUtils.instance().getAssignedTicketsChild();
        for (Map.Entry<String, List<OrderItem>> entry : assignedChildren.entrySet()) {
            if (entry != null && entry.getValue() != null
                    && entry.getValue().size() > 0 && entry.getValue().get(0) != null) {
                OrderItem orderItem = entry.getValue().get(0);
                String firstName = orderItem.getAssignedFirstName();
                String lastName = orderItem.getAssignedLastName();
                String suffix = orderItem.getSuffix();
                Integer sequenceId = orderItem.getPartyMemberId();
                TravelPartyMember travelPartyMember = new TravelPartyMember(firstName, lastName, suffix, sequenceId);
                assignedTravelParty.add(travelPartyMember);
            }
        }

        if (mSelectedMember != null) {
            assignedTravelParty.add(mSelectedMember);
        }

        //check if party members have are in the list of people that have already been assigned
        if (mPartyMembers != null) {
            mPartyMembers.removeAll(assignedTravelParty);
        }
    }

    public void getTravelParty() {
        mIsCallInProgress = true;

        GetTravelPartyRequest request = new GetTravelPartyRequest.Builder(this)
                .setUserName(AccountStateManager.getUsername())
                .setUserPassword(AccountStateManager.getPassword())
                .build();

        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();

        showViewBasedOnState();
    }

    private void saveInputTravelPartyMember() {
        String first = StringUtils.trimToEmpty(mAdadpter.getFirstName());
        String last = StringUtils.trimToEmpty(mAdadpter.getLastName());
        String suffix = StringUtils.trimToEmpty(mAdadpter.getSuffix());

        mIsCallInProgress = true;

        CreateTravelPartyRequest request = new CreateTravelPartyRequest.Builder(this)
                .setPartyMemberFirstName(first)
                .setPartyMemberLastName(last)
                .setPartyMemberSuffix(suffix)
                .build();

        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();

        showViewBasedOnState();
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        mIsCallInProgress = false;

        if (networkResponse instanceof GetTravelPartyResponse) {
            if (networkResponse.isHttpStatusCodeSuccess()) {
                GetTravelPartyResponse travelPartyResponse = (GetTravelPartyResponse) networkResponse;
                TravelPartyMembers travelPartyMembers = travelPartyResponse.getTravelPartyMembers();
                if (travelPartyMembers != null && travelPartyMembers.getPartyMembers() != null) {
                    mPartyMembers = travelPartyMembers.getPartyMembers();
                }

                if (mPartyMembers == null) {
                    mPartyMembers = new ArrayList<>();
                }

                mSelectedMember = getPreSelectedMember(mPartyMembers);

                //remove travel party members that have already been assigned to tickets
                removeAssignedTravelPartyMembers();

                //sort travel party members by first name
                sortTravelPartyMembers();

                //if there is a selected member, then it should be added first to the spinner
                if (mSelectedMember != null) {
                    mPartyMembers.add(0, mSelectedMember);
                }
            } else {
                mPartyMembers = Collections.emptyList();
                logAndShowResponseErrorMessage();
            }
            mAdadpter.setGuestInfoSpinnerAdapter(new AssignNamesAdapter(getContext(), R.layout.item_assign_name, mPartyMembers));
        } else if (networkResponse instanceof CreateTravelPartyResponse) {
            CreateTravelPartyResponse createTravelPartyResponse = (CreateTravelPartyResponse) networkResponse;
            if (networkResponse.isHttpStatusCodeSuccess()) {
                if (createTravelPartyResponse.getCreatedTravelPartyMember() != null) {
                    String first = StringUtils.trimToEmpty(mAdadpter.getFirstName());
                    String last = StringUtils.trimToEmpty(mAdadpter.getLastName());
                    String suffix = StringUtils.trimToEmpty(mAdadpter.getSuffix());
                    mSelectedMember = new TravelPartyMember(first, last, suffix, createTravelPartyResponse.getCreatedTravelPartyMember().getSequenceId());

                    if (AccountStateManager.isUserLoggedIn()) {
                        handleRegisteredGuestAssignTicket();
                    } else {
                        handleUnregisteredGuestAssignTicket();
                    }
                }
            } else {
                logAndShowResponseErrorMessage();
            }
        } else if (networkResponse instanceof UpdateItemResponse) {
            if (networkResponse.isHttpStatusCodeSuccess()) {
                mIsCallInProgress = true;

                CartTicketGroupingRequest request = new CartTicketGroupingRequest.Builder(this)
                        .build();
                NetworkUtils.queueNetworkRequest(request);
                NetworkUtils.startNetworkService();
            } else {
                logAndShowResponseErrorMessage();
            }
        } else if (networkResponse instanceof TicketGroupingResponse) {
            if (networkResponse.isHttpStatusCodeSuccess()) {
                TicketGroupingResponse ticketGroupingResponse = (TicketGroupingResponse) networkResponse;
                // FIXME This UTILS class really shouldn't be creating instances of itself
                TicketAssignmentUtils.instance().setTicketGrouping(ticketGroupingResponse.getOrder());
                if (ticketGroupingResponse.getOrder() != null &&
                        ticketGroupingResponse.getOrder().getOrderItemGroups() != null &&
                        IceTicketUtils.hasInventoryError(ticketGroupingResponse.getOrder().getOrderItemGroups())) {
                    // There is an inventory error so go back to the cart (just finish since cart opened this activity)
                    getActivity().finish();
                } else {
                    //only save party member if the user is logged in and the member was not selected from the drop down
                    if (AccountStateManager.isUserLoggedIn() && mSelectedMember == null) {
                        mIsCallInProgress = true;
                        saveInputTravelPartyMember();
                    } else {
                        if (mListener != null) {
                            mListener.onTicketAssigned();
                        }
                    }
                }
            } else {
                logAndShowResponseErrorMessage();
            }
        } else {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "Unexpected response returned to handler method");
            }
            showMessage(mTridionConfig.getEr71());
        }

        showViewBasedOnState();
    }

    private void logAndShowResponseErrorMessage() {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, "Response was null or returned with a failure code");
        }
        showMessage(mTridionConfig.getEr71());
    }

    /**
     * Used to get the party memeber that is currently selected
     *
     * @param travelPartyMembers
     * @return
     */
    private TravelPartyMember getPreSelectedMember(List<TravelPartyMember> travelPartyMembers) {
        if (mGuest != null) {
            for (TravelPartyMember travelPartyMember : travelPartyMembers) {
                if (travelPartyMember.getFirstName().equals(mGuest.firstname) &&
                        travelPartyMember.getLastName().equals(mGuest.lastname) &&
                        travelPartyMember.getSuffix().equals(mGuest.suffix)) {
                    return travelPartyMember;
                }
            }
        }
        return null;
    }

    interface OnTicketAssignedListener {
        void onTicketAssigned();
    }

    //TODO Analytics will be refactored for the february release
    private void sendPageViewAnalytics() {
        AnalyticsUtils.trackTicketsPageView(
                AnalyticsUtils.CONTENT_SUB_2_ASSIGN_TICKETS,
                AnalyticsUtils.PROPERTY_NAME_CHECKOUT,
                AnalyticsUtils.UO_PAGE_IDENTIFIER_ASSIGN_PURCHASES,
                AnalyticsUtils.PROPERTY_NAME_PARKS_RESORT,
                AnalyticsUtils.PROPERTY_NAME_CHECKOUT_PROCESS,
                AnalyticsUtils.PROPERTY_NAME_GUEST_ASSIGN_TICKETS,
                null,
                null,
                "event2, event13",
                false,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    private void sendDoneClickedAnalytics() {
        Map<String, Object> extraData = new HashMap<String, Object>();
        extraData.put(AnalyticsUtils.KEY_EVENTS, "event38,");
        extraData.put(AnalyticsUtils.KEY_EVENT_NAME, "DONE button");
        AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_SALES,
                AnalyticsUtils.CONTENT_FOCUS_TICKETS,
                null,
                AnalyticsUtils.UO_PAGE_IDENTIFIER_ASSIGN_PURCHASES,
                null,
                null,
                extraData);
    }
}
