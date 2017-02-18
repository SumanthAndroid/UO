package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.checkout.PaymentInformationFragment;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.CommerceUiBuilder;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.databinding.CommerceAssignNamesToTicketsBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupOrder;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;

import java.util.List;
import java.util.Map;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/10/16.
 * Class: CommerceAssignTicketsFragment
 * Class Description: Fragment for assigning names to tickets
 */
public class CommerceAssignTicketsFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "CommerceAsgnTcktsFrgmnt";

    private TicketGroupOrder mOrder;

    private LinearLayout mAssignmentsContainer;
    private TextView tvUnassignedCount;
    private ImageView ivChevron;
    private LinearLayout mTicketsLeftContainer;
    private TextView tvUnassigned;
    private Button btnContinue;

    private CommerceAssignmentChangeListener assignmentChangeListener;
    private TridionConfig mTridionConfig;

    public static CommerceAssignTicketsFragment newInstance() {
        CommerceAssignTicketsFragment fragment = new CommerceAssignTicketsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            // retrieve arguments here
            sendPageViewAnalytics();
            TicketAssignmentUtils.setPrimarySelected(false);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CommerceAssignmentChangeListener) {
            assignmentChangeListener = (CommerceAssignmentChangeListener) activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mTridionConfig = IceTicketUtils.getTridionConfig();
        mOrder = TicketAssignmentUtils.instance().getTicketGroupOrder();
        CommerceAssignNamesToTicketsBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.commerce_assign_names_to_tickets, container, false);
        binding.setTridion(mTridionConfig);
        View layout = binding.getRoot();
        mAssignmentsContainer = binding.commerceAssignTicketsContainer;
        tvUnassignedCount = binding.tvUnassignedCount;
        ivChevron = binding.chevron;
        mTicketsLeftContainer = binding.ticketsLeftContainer;
        tvUnassigned = binding.tvUnassigned;
        btnContinue = binding.btnContinue;
        btnContinue.setOnClickListener(this);

        mTridionConfig = IceTicketUtils.getTridionConfig();
        addAssignNamesItems();

        ivChevron.setOnClickListener(this);
        tvUnassignedCount.setOnClickListener(this);

        if (null != getActivity() && null != getActivity().getActionBar()) {
            getActivity().getActionBar().setTitle(mTridionConfig.getPageHeaderAEAppTitle());
        }
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != assignmentChangeListener) {
            assignmentChangeListener.onShowAddGuestChanged(true);
        }
    }

    private void addAssignNamesItems() {
        // If order is null, no need to add party members to assign
        if (mOrder == null) {
            Log.w(TAG, "The order was null");
            disableButton();
            return;
        }

        int adults = mOrder.getMaxNumberOfAdultTickets();
        int maxAdult = adults;
        int childs = mOrder.getMaxNumberOfChildTickets();
        int maxChild = childs;
        int ueps = mOrder.getMaxNumberOfUEPTickets();
        int allAgeAddons = mOrder.getMaxNumberOfAllAgeAddOnTickets();

        Map<String, List<OrderItem>> assignedAdults = mOrder.getAssignedTicketAdults();
        Map<String, List<OrderItem>> assignedUEPs = mOrder.getAssignedUEPTickets();
        Map<String, List<OrderItem>> assignedChilds = mOrder.getAssignedTicketsChild();
        Map<String, List<OrderItem>> assignedAllAgeAddons = mOrder.getAssignedAllAgeAddOnTickets();

        // add assigned adult views
        for (Map.Entry<String, List<OrderItem>> entry : assignedAdults.entrySet()) {
            if (entry != null && entry.getValue() != null) {
                List<OrderItem> items = entry.getValue();
                List<OrderItem> uep = assignedUEPs.get(entry.getKey());
                if (uep != null && uep.size() > 0) {
                    items.addAll(uep);
                    assignedUEPs.remove(entry.getKey());
                    ueps--;
                }
                List<OrderItem> addOn = assignedAllAgeAddons.get(entry.getKey());
                if (addOn != null && addOn.size() > 0) {
                    items.addAll(addOn);
                    assignedAllAgeAddons.remove(entry.getKey());
                    allAgeAddons--;
                }
                String numAssignments = mTridionConfig.getPartyMemberAssignSecondaryText(items.size());

                OrderItem orderItem = items.get(0);
                PartyMember guest = new PartyMember(PartyMember.AGE_TYPE_ADULT);
                if (orderItem != null) {
                    guest.firstname = orderItem.getAssignedFirstName();
                    guest.lastname = orderItem.getAssignedLastName();
                    guest.suffix = orderItem.getSuffix();
                    guest.orderItems = items;
                }
                checkIfPrimaryMember(guest);
                CommerceUiBuilder.addCurrentlyAssignedNameItem(getActivity(), entry.getKey(), numAssignments, mAssignmentsContainer, guest, true, this);
                adults--;
            }

        }

        // add assigned child views
        for (Map.Entry<String, List<OrderItem>> entry : assignedChilds.entrySet()) {
            if (entry != null && entry.getValue() != null) {
                List<OrderItem> items = entry.getValue();
                List<OrderItem> uep = assignedUEPs.get(entry.getKey());
                if (uep != null && uep.size() > 0) {
                    items.addAll(uep);
                    assignedUEPs.remove(entry.getKey());
                    ueps--;
                }
                List<OrderItem> addOn = assignedAllAgeAddons.get(entry.getKey());
                if (addOn != null && addOn.size() > 0) {
                    items.addAll(addOn);
                    assignedAllAgeAddons.remove(entry.getKey());
                    allAgeAddons--;
                }
                String numAssignments = mTridionConfig.getPartyMemberAssignSecondaryText(items.size());

                OrderItem orderItem = items.get(0);
                PartyMember guest = new PartyMember(PartyMember.AGE_TYPE_CHILD);
                if (orderItem != null) {
                    guest.firstname = orderItem.getAssignedFirstName();
                    guest.lastname = orderItem.getAssignedLastName();
                    guest.suffix = orderItem.getSuffix();
                    guest.orderItems = items;
                }

                checkIfPrimaryMember(guest);
                CommerceUiBuilder.addCurrentlyAssignedNameItem(getActivity(), entry.getKey(), numAssignments, mAssignmentsContainer, guest, true, this);
                childs--;
            }
        }

        // add assigned uep views
        for (Map.Entry<String, List<OrderItem>> entry : assignedUEPs.entrySet()) {
            if (entry != null && entry.getValue() != null) {
                List<OrderItem> items = entry.getValue();
                String numAssignments = mTridionConfig.getPartyMemberAssignSecondaryText(items.size());

                OrderItem orderItem = items.get(0);
                PartyMember guest = new PartyMember(PartyMember.AGE_TYPE_ADULT);
                if (orderItem != null) {
                    guest.firstname = orderItem.getAssignedFirstName();
                    guest.lastname = orderItem.getAssignedLastName();
                    guest.suffix = orderItem.getSuffix();
                    guest.orderItems = items;
                }
                checkIfPrimaryMember(guest);
                CommerceUiBuilder.addCurrentlyAssignedNameItem(getActivity(), entry.getKey(), numAssignments, mAssignmentsContainer, guest, true, this);
                ueps--;
                adults--;
            }
        }

        //add assigned allAgeAddon views
        for (Map.Entry<String, List<OrderItem>> entry : assignedAllAgeAddons.entrySet()) {
            if (entry != null && entry.getValue() != null) {
                List<OrderItem> items = entry.getValue();
                String numAssignments = mTridionConfig.getPartyMemberAssignSecondaryText(items.size());

                OrderItem orderItem = items.get(0);
                PartyMember guest = new PartyMember(PartyMember.AGE_TYPE_ADULT);
                if (orderItem != null) {
                    guest.firstname = orderItem.getAssignedFirstName();
                    guest.lastname = orderItem.getAssignedLastName();
                    guest.suffix = orderItem.getSuffix();
                    guest.orderItems = items;
                }
                checkIfPrimaryMember(guest);
                CommerceUiBuilder.addCurrentlyAssignedNameItem(getActivity(), entry.getKey(), numAssignments, mAssignmentsContainer, guest, true, this);
                allAgeAddons--;
                adults--;
            }
        }

        // Determine the max number of remaining adults that are needed
        ueps = ueps - (maxAdult + maxChild);
        allAgeAddons = allAgeAddons - (maxAdult + maxChild);
        adults = Math.max(adults, Math.max(ueps, allAgeAddons));
        //add unassigned adult views
        for (int i = 0; i < adults; i++) {
            CommerceUiBuilder.addAssignNameItem(getActivity(), mTridionConfig.getUnassignedAdultLabel() + " " + (assignedAdults.size() + i + 1), mAssignmentsContainer, new PartyMember(PartyMember.AGE_TYPE_ADULT), true, this);
        }
        //add unassigned child views
        for (int i = 0; i < childs; i++) {
            CommerceUiBuilder.addAssignNameItem(getActivity(), mTridionConfig.getUnassignedChildLabel() + " " + (assignedChilds.size() + i + 1), mAssignmentsContainer, new PartyMember(PartyMember.AGE_TYPE_CHILD), true, this);
        }

        //if the number of adult + child items are 0, then add one adult
        if(mAssignmentsContainer.getChildCount() == 0){
            CommerceUiBuilder.addAssignNameItem(getActivity(), mTridionConfig.getAdultLabel() + (1), mAssignmentsContainer, new PartyMember(PartyMember.AGE_TYPE_ADULT), true, this);
        }

        int total = addUnassignedTicketViews();

        tvUnassignedCount.setText(Integer.toString(total));

        if (total > 0) {
            disableButton();
        } else {
            enableButton();
        }
    }

    /**
     * This will look to see if there is a party member that is a
     * primary member for the assigned tickets. Sets the primary selected
     * to true if the member is already a primary member
     * @param member
     */
    private void checkIfPrimaryMember(PartyMember member){

        if(TicketAssignmentUtils.instance().primarySelected()){
            return;
        }

        if(TicketAssignmentUtils.instance().primaryMemberSelected(member)){
            TicketAssignmentUtils.instance().setPrimarySelected(true);
        }
    }



    private int addUnassignedTicketViews() {
        List<AssignableTicketItem> ticketItems = TicketAssignmentUtils.instance().getUnassignedAdultTickets();
        ticketItems.addAll(TicketAssignmentUtils.instance().getUnassignedChildTickets());
        ticketItems.addAll(TicketAssignmentUtils.instance().getUnassignedUEPTickets());
        ticketItems.addAll(TicketAssignmentUtils.instance().getUnassignedAddOnTickets());
        int total = 0;

        for (int i = 0; i < ticketItems.size(); i++) {
            AssignableTicketItem item = ticketItems.get(i);
            View ticketView = LayoutInflater.from(getActivity()).inflate(R.layout.commerce_assignable_ticket_view, null);
            TextView tvLine1 = (TextView) ticketView.findViewById(R.id.tv_line1);
            TextView tvLine2 = (TextView) ticketView.findViewById(R.id.tv_line2);
            TextView tvLine3 = (TextView) ticketView.findViewById(R.id.tv_line3);
            View divider = ticketView.findViewById(R.id.divider);
            ImageView tvAssignableImageView = (ImageView) ticketView.findViewById(R.id.tvAssignableImageView);

            if (item.isExpress()) {
                tvAssignableImageView.setImageResource(R.drawable.ic_detail_feature_express_pass);
            } else if (item.isTicket()) {
                tvAssignableImageView.setImageResource(R.drawable.icon_ticket);
            } else if (item.isExtra()) {
                tvAssignableImageView.setImageResource(R.drawable.icon_addon);
            } else if (item.isAnnualPass()) {
                tvAssignableImageView.setImageResource(R.drawable.ic_annual_pass);
            }

            tvLine1.setText(item.getAssignNamesAlternativeHeaderLine1() + " (" + item.getUnAssignedOrderItems().size() + ")");
            total += item.getUnAssignedOrderItems().size();
            tvLine2.setText(item.getAssignNamesAlternativeHeaderLine2());
            tvLine3.setText(item.getAssignNamesAlternativeHeaderLine3());
            tvLine3.setVisibility(!TextUtils.isEmpty(item.getAssignNamesAlternativeHeaderLine3()) ? View.VISIBLE : View.GONE);
            if (i < ticketItems.size() - 1) {
                divider.setVisibility(View.VISIBLE);
            } else {
                divider.setVisibility(View.GONE);
            }

            mTicketsLeftContainer.addView(ticketView);
        }
        return total;
    }

    private void disableButton() {
        btnContinue.setBackgroundResource(R.drawable.shape_button_blue_disabled_background);
        btnContinue.setEnabled(false);
    }

    private void enableButton() {
        btnContinue.setBackgroundResource(R.drawable.shape_button_blue_background);
        btnContinue.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() instanceof PartyMember) {
            PartyMember guest = (PartyMember) v.getTag();
            if (getActivity() instanceof CommerceAssignTicketsActivity) {
                CommerceAssignmentFragment fragment = CommerceAssignmentFragment.newInstance(guest);
                ((CommerceAssignTicketsActivity) getActivity()).showNextFragment(fragment);
            }
        } else if (v.getId() == ivChevron.getId() || v.getId() == tvUnassignedCount.getId()) {
            if (mTicketsLeftContainer.getVisibility() == View.GONE) {
                mTicketsLeftContainer.setVisibility(View.VISIBLE);
                tvUnassigned.setVisibility(View.VISIBLE);
                ivChevron.setImageResource(R.drawable.ic_chevron_up_white_24dp);
            } else {
                mTicketsLeftContainer.setVisibility(View.GONE);
                tvUnassigned.setVisibility(View.GONE);
                ivChevron.setImageResource(R.drawable.ic_chevron_down_white_24dp);
            }
        } else if (v.getId() == btnContinue.getId()) {
            if (getActivity() instanceof CommerceAssignTicketsActivity) {

                // TODO pass appropriate parameters to PaymentInformationFragment
                ((CommerceAssignTicketsActivity) getActivity()).showNextFragment(
                        PaymentInformationFragment.newInstance());
            }
        }
    }

    //TODO Analytics will be refactored for the february release
    private void sendPageViewAnalytics(){
        AnalyticsUtils.trackTicketsPageView(
                AnalyticsUtils.CONTENT_SUB_2_TICKETS,
                AnalyticsUtils.PROPERTY_NAME_CHECKOUT,
                AnalyticsUtils.PROPERTY_NAME_VIEW_ASSIGN_TICKETS,
                AnalyticsUtils.PROPERTY_NAME_PARKS_RESORT,
                AnalyticsUtils.PROPERTY_NAME_CHECKOUT_PROCESS,
                AnalyticsUtils.PROPERTY_NAME_GUEST_VIEW_ASSIGN_TICKETS,
                null,
                null,
                "event2, event13]",
                false,
                null,
                null,
                null,
                null,
                null,
                null);
    }
}
