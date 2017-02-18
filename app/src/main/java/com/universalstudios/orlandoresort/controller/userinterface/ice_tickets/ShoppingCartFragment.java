package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.checkout.CheckoutSignInActivity;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.FullScreenLoadingView;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets.CommerceAssignTicketsActivity;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets.TicketAssignmentUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.ShoppingCartAdapter;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.FragmentTicketshoppingCartBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.request.AddItemRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.response.AddItemErrorResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.response.AddItemResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.UpdateItem.request.UpdateItemRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.UpdateItem.response.UpdateItemResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response.Adjustment;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ShippingModeData.DeliveryOption;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ShippingModeData.GetEligibleShipModesRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ShippingModeData.GetEligibleShipModesResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.AddOnTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.CartTicketGroupingRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ExpressPassTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.Offer;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ParkTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.Ticket;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupOrder;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupingResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetTridionSpecsRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses.GetTridionSpecsResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.SecurityUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.cart.promo.AddPromoCodeRequest;
import com.universalstudios.orlandoresort.model.network.domain.cart.promo.AddPromoCodeResponse;
import com.universalstudios.orlandoresort.model.network.domain.cart.promo.RemovePromoCodeErrorResponse;
import com.universalstudios.orlandoresort.model.network.domain.cart.promo.RemovePromoCodeRequest;
import com.universalstudios.orlandoresort.model.network.domain.cart.promo.RemovePromoCodeResponse;
import com.universalstudios.orlandoresort.model.network.domain.offers.OfferAcceptedRequest;
import com.universalstudios.orlandoresort.model.network.domain.offers.OfferAcceptedResponse;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.ConcurrencyType;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/1/16.
 * Class: ShoppingCartFragment
 * Class Description: Shows the guest's shopping cart
 */
public class ShoppingCartFragment extends NetworkFragment implements View.OnClickListener {
    public static final String TAG = "ShoppingCartFragment";

    private static final int REQUEST_CODE_LOGIN = 101;
    private static final String KEY_STATE_SEND_REMOVE_ANALYTICS = "KEY_STATE_SEND_REMOVE_ANALYTICS";
    private static final String KEY_STATE_TICKET_GROUPING = "KEY_STATE_TICKET_GROUPING";
    private static final String KEY_STATE_CURRENT_PROMO_CODE = "KEY_STATE_CURRENT_PROMO_CODE";

    private FullScreenLoadingView mLoadingView;
    private RecyclerView mRecyclerView;
    private TextView tvNoItems;
    private Button btnShopNow;

    private ShoppingCartAdapter mTicketAdapter;

    private TicketGroupOrder mTicketGrouping;
    private TridionConfig mTridionConfig;
    private ContinueShoppingListener mContinueShoppingListener;
    private TicketGroupingResponse mTicketGroupingResponse;
    private Handler mHandler;
    private String mCurrentPromoCode;

    private SendAnalytics mSendAnalyticsType;
    private boolean mIsPrimaryPageLoad = false;

    private enum SendAnalytics {
        REMOVE,
        NONE
    }

    private boolean mPerformFullCartRefresh;

    public static ShoppingCartFragment newInstance() {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAttach");
        }

        // Check if parent fragment (if there is one) implements the interface
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof ContinueShoppingListener) {
            mContinueShoppingListener = (ContinueShoppingListener) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (activity != null && activity instanceof ContinueShoppingListener) {
            mContinueShoppingListener = (ContinueShoppingListener) activity;
        }
        // If neither implements the interface, log a warning
        else if (mContinueShoppingListener == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement ContinueShoppingListener");
            }
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Default parameters
        Bundle args = getArguments();
        if (args == null) {

        }
        // Otherwise, set incoming parameters
        else {

        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            mIsPrimaryPageLoad = true;
            mSendAnalyticsType = SendAnalytics.NONE;
            mPerformFullCartRefresh = true;
            mCurrentPromoCode = null;
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {
            mSendAnalyticsType = (SendAnalytics) savedInstanceState.getSerializable(KEY_STATE_SEND_REMOVE_ANALYTICS);
            mPerformFullCartRefresh = savedInstanceState.getBoolean(KEY_STATE_TICKET_GROUPING);
            mCurrentPromoCode = savedInstanceState.getString(KEY_STATE_CURRENT_PROMO_CODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mTridionConfig = IceTicketUtils.getTridionConfig();
        FragmentTicketshoppingCartBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ticketshopping_cart, container, false);
        binding.setTridion(mTridionConfig);
        mRecyclerView = binding.lvShoppingCart;
        tvNoItems = binding.fragmentTicketshoppingCartNoItemText;
        btnShopNow = binding.fragmentTickeshoppingCartShowNowButton;

        btnShopNow.setOnClickListener(this);

        mTicketAdapter = new ShoppingCartAdapter(mTridionConfig, mActionCallbacks);
        mRecyclerView.setAdapter(mTicketAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mHandler = new Handler(getActivity().getMainLooper());

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestCart(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onSaveInstanceState");
        }

        outState.putSerializable(KEY_STATE_SEND_REMOVE_ANALYTICS, mSendAnalyticsType);
        outState.putBoolean(KEY_STATE_TICKET_GROUPING, mPerformFullCartRefresh);
        outState.putString(KEY_STATE_CURRENT_PROMO_CODE, mCurrentPromoCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    startActivity(CommerceAssignTicketsActivity.createIntent(getContext()));
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private ShoppingCartAdapter.ShoppingCartActionCallback mActionCallbacks = new ShoppingCartAdapter.ShoppingCartActionCallback() {
        @Override
        public void onRemoveClicked(Ticket adultTicket, Ticket childTicket, String orderId) {
            List<OrderItem> orderItems = new ArrayList<>();

            if(adultTicket != null && adultTicket.getOrderItems() != null) {
                orderItems.addAll(adultTicket.getOrderItems());
            }

            if(childTicket != null && childTicket.getOrderItems() != null) {
                orderItems.addAll(childTicket.getOrderItems());
            }

            removeItemsFromCart(orderItems);

        }

        @Override
        public void onAddSameItem(ParkTicketGroups parkTicketGroups, @ShoppingCartAdapter.TicketAgeType int ticketAgeType) {
            mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
            if (null != parkTicketGroups && null != parkTicketGroups.getAdultTickets() && null != parkTicketGroups.getChildTickets()) {
                Ticket ticket;
                Ticket otherTicket;
                if (ticketAgeType == ShoppingCartAdapter.TICKET_AGE_ADULT) {
                    // Add an adult ticket, but get attributes from child if adult qty = 0
                    ticket = parkTicketGroups.getAdultTickets();
                    otherTicket = parkTicketGroups.getChildTickets();
                } else {
                    // Add a child ticket, but get attributes from adult if child qty = 0
                    ticket = parkTicketGroups.getChildTickets();
                    otherTicket = parkTicketGroups.getAdultTickets();
                }

                addSameTicketWithAlternativeForZeroQuantity(ticket, otherTicket);
            }
        }

        @Override
        public void onAddSameItem(AddOnTicketGroups addOnTicketGroups, @ShoppingCartAdapter.TicketAgeType int ticketAgeType) {
            mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
            if (null != addOnTicketGroups && (null != addOnTicketGroups.getAllAddOns() ||
                    (null != addOnTicketGroups.getChildAddOns() && null != addOnTicketGroups.getAdultAddOns()))) {
                Ticket ticket;
                Ticket otherTicket;
                if (ticketAgeType == ShoppingCartAdapter.TICKET_AGE_GENERAL) {
                    // There is no other ticket for general add-ons
                    ticket = addOnTicketGroups.getAllAddOns();
                    otherTicket = null;
                } else if (ticketAgeType == ShoppingCartAdapter.TICKET_AGE_ADULT) {
                    // Add an adult ticket, but get attributes from child if adult qty = 0
                    ticket = addOnTicketGroups.getAdultAddOns();
                    otherTicket = addOnTicketGroups.getChildAddOns();
                } else {
                    // Add a child ticket, but get attributes from adult if child qty = 0
                    ticket = addOnTicketGroups.getChildAddOns();
                    otherTicket = addOnTicketGroups.getAdultAddOns();
                }

                addSameTicketWithAlternativeForZeroQuantity(ticket, otherTicket);
            }
        }

        private void addSameTicketWithAlternativeForZeroQuantity(Ticket ticket, Ticket otherTicket) {
            List<CommerceAttribute> attributes = null;
            if (null != ticket && ticket.getQuantity() > 0) {
                attributes = ticket.getOrderItemAttributes();
            } else if (null != otherTicket) {
                attributes = otherTicket.getOrderItemAttributes();
            }

            addCopyOfSingleItem(ticket.getItem().getPartNumber(), attributes);
        }

        @Override
        public void onRemoveSameItem(Ticket ticket, String orderId) {
            OrderItem itemToRemove = TicketAssignmentUtils.getFirstUnassignedOrderItem(ticket.getOrderItems());
            if (null == itemToRemove) {
                itemToRemove = ticket.getOrderItems().get(0);
            }
            removeItemFromCart(itemToRemove);
        }

        @Override
        public void onRemoveClicked(ExpressPassTicketGroups expressTicket, String orderId) {
            if(expressTicket != null && expressTicket.getOrderItems() != null) {
                List<OrderItem> orderItems = expressTicket.getOrderItems();
                removeItemsFromCart(orderItems);
            }
        }

        @Override
        public void onRemoveSameItem(ExpressPassTicketGroups expressTicket, String orderId) {
            if(expressTicket != null && expressTicket.getOrderItems() != null ) {
                OrderItem itemToRemove = TicketAssignmentUtils.getFirstUnassignedOrderItem(expressTicket.getOrderItems());

                if(itemToRemove == null && expressTicket.getOrderItems().size() > 0){
                    itemToRemove = expressTicket.getOrderItems().get(0);
                }

                if(itemToRemove != null) {
                    removeItemFromCart(itemToRemove);
                }
            }
        }

        @Override
        public void onAddSameItem(ExpressPassTicketGroups expressTicket, String orderId) {
            mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
            List<CommerceAttribute> attributes = expressTicket.getOrderItemAttributes();

            addCopyOfSingleItem(expressTicket.getItem().getPartNumber(), attributes);
        }

        @Override
        public void onRemoveClicked(AddOnTicketGroups addOnTicket, String orderId) {
            if(addOnTicket != null) {
                List<OrderItem> orderItems = new ArrayList<>();
                if (addOnTicket.getAllAddOns() != null) {
                    orderItems.addAll(addOnTicket.getAllAddOns().getOrderItems());
                }
                if (addOnTicket.getAdultAddOns() != null) {
                    orderItems.addAll(addOnTicket.getAdultAddOns().getOrderItems());
                }
                if (addOnTicket.getChildAddOns() != null) {
                    orderItems.addAll(addOnTicket.getChildAddOns().getOrderItems());
                }
                if (orderItems.size() > 0) {
                    removeItemsFromCart(orderItems);
                }
            }

        }

        @Override
        public void onContinueShoppingClicked() {
            // No reason to do anything if no one is listening
            if (null == mContinueShoppingListener) return;

            /** CONTINUE Logic (US17856)
             * The guest has reached the cart page with or without items in their cart   Comp
             * If no ticket products are in the cart, clicking the continue shopping button takes the guest to the tickets shopping page
             * If tickets (and anything else) are in the cart but not UEP, continue shopping goes to the UEP page
             * If tickets and UEP are both in cart already, continue shopping goes to the add on page (all tab)
             * If tickets, UEP, and extras are all in cart already, continue shopping goes to the add on page (all tab)
             */
            @ShoppingActivity.ShopItemType int ticketTypePage = ShoppingActivity.SELECT_ADDONS;
            if (null != mTicketGrouping && null != mTicketGrouping.getOrderItemGroups()) {
                List<ParkTicketGroups> parkTicketGroups = mTicketGrouping.getOrderItemGroups().getParkTicketGroups();
                boolean hasParkTickets = null != parkTicketGroups && parkTicketGroups.size() > 0;
                List<ExpressPassTicketGroups> expressPassTicketGroups = mTicketGrouping.getOrderItemGroups().getExpressPassGroups();
                boolean hasExpressPasses = null != expressPassTicketGroups && expressPassTicketGroups.size() > 0;

                if (!hasParkTickets) {
                    ticketTypePage = ShoppingActivity.SELECT_TICKETS;
                } else if (hasParkTickets && !hasExpressPasses) {
                    ticketTypePage = ShoppingActivity.SELECT_EXPRESS_PASS;
                }
            }

            mContinueShoppingListener.onContinueShopping(ticketTypePage);
        }

        @Override
        public void onCheckoutClicked() {
            sendOnCheckoutClickedAnalytics(mTicketGrouping);
            if (AccountStateManager.isUserLoggedIn()) {
                startActivity(CommerceAssignTicketsActivity.createIntent(getActivity()));
            } else {
                Intent intent = CheckoutSignInActivity.newInstanceIntent(getContext());
                startActivityForResult(intent, REQUEST_CODE_LOGIN);
            }
        }

        @Override
        public void onDeliveryOptionSelected(DeliveryOption deliveryOption) {
            // set “deliveryMethod” when we update; delivery method is the shipModeId
            mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());

            // isAdded() to check if the fragment is currently alive, so the ShoppingCartFragment.this doesn't fail
            if (isAdded()) {
                mSendAnalyticsType = SendAnalytics.NONE;
                requestPricing(deliveryOption, false);
            }
        }

        @Override
        public void onAddPromoCodeToCart(String promoCode) {
            if (mTicketGrouping != null && mTicketGrouping.getPricing() != null) {
                Adjustment adjustment = mTicketGrouping.getPricing().getFirstAdjustment();
                String cartPromoCode = null;
                if (adjustment != null) {
                    cartPromoCode = adjustment.getCode();
                }

                // Add promo code to cart if no promo code is present in the cart
                // and the entered code is not empty
                if (!TextUtils.isEmpty(promoCode) && TextUtils.isEmpty(cartPromoCode)) {
                    mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
                    AddPromoCodeRequest request = new AddPromoCodeRequest.Builder(ShoppingCartFragment.this)
                            .setPromoCode(promoCode)
                            .build();
                    NetworkUtils.queueNetworkRequest(request);
                    NetworkUtils.startNetworkService();
                }
                // Remove promo code if entered promo code was empty or there was a
                // code already present in the cart set mCurrentPromoCode to
                // allow the add promo request to be called after the removal
                else {
                    // Remove promo code if present
                    if (!TextUtils.isEmpty(promoCode)) {
                        mCurrentPromoCode = promoCode;
                    } else {
                        mCurrentPromoCode = null;
                    }
                    // If there's no promo code in the cart nothing to do here
                    if (!TextUtils.isEmpty(cartPromoCode)) {
                        mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
                        RemovePromoCodeRequest request = new RemovePromoCodeRequest.Builder(ShoppingCartFragment.this)
                                .setPromoCode(cartPromoCode)
                                .build();
                        NetworkUtils.queueNetworkRequest(request);
                        NetworkUtils.startNetworkService();
                    }
                }
            }
        }

        @Override
        public void onAddPromoItemToCart(Offer offer) {
            // Add promo item to cart
            if (offer != null && offer.getSku() != null && !TextUtils.isEmpty(offer.getSku().getPartNumber())) {
                OrderItem orderItem = new OrderItem();
                orderItem.setPartNumber(offer.getSku().getPartNumber());
                orderItem.setQuantity(1);
                mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
                AddItemRequest request = new AddItemRequest.Builder(ShoppingCartFragment.this)
                        .setConcurrencyType(ConcurrencyType.ASYNCHRONOUS)
                        .setOrderItem(Arrays.asList(orderItem))
                        .build();
                NetworkUtils.queueNetworkRequest(request);

                // Track offer was accepted
                OfferAcceptedRequest offerAcceptedRequest = new OfferAcceptedRequest.Builder(ShoppingCartFragment.this)
                        .setConcurrencyType(ConcurrencyType.ASYNCHRONOUS)
                        .addOffer(offer.getCode(), offer.getTreatmentCode())
                        .build();
                NetworkUtils.queueNetworkRequest(offerAcceptedRequest);
                NetworkUtils.startNetworkService();
            }
        }
    };

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "handleNetworkResponse");
        }
        dismissLoadingView();

        if (networkResponse instanceof AddPromoCodeResponse) {
            handleAddPromoCodeResponse((AddPromoCodeResponse) networkResponse);
        }
        else if (networkResponse instanceof UpdateItemResponse) {
            handleUpdateItemResponse((UpdateItemResponse) networkResponse);
        }
        else if (networkResponse instanceof TicketGroupingResponse) {
            handleTicketGroupingResponse((TicketGroupingResponse) networkResponse);
        }
        else if (networkResponse instanceof AddItemResponse) {
            handleAddItemResponse((AddItemResponse) networkResponse);
        }
        else if (networkResponse instanceof GetTridionSpecsResponse) {
            handleGetTridionSpecsResponse((GetTridionSpecsResponse) networkResponse);
        }
        else if (networkResponse instanceof GetEligibleShipModesResponse) {
            handleGetEligibleShipModesResponse((GetEligibleShipModesResponse) networkResponse);
        } else if (networkResponse instanceof OfferAcceptedResponse) {
            // Nothing to do
        }
        else if (networkResponse instanceof RemovePromoCodeResponse) {
            handleRemovePromoCodeResponse((RemovePromoCodeResponse) networkResponse);
        }
    }

    private void handleGetEligibleShipModesResponse(@NonNull GetEligibleShipModesResponse response) {
        if (response.isHttpStatusCodeSuccess()) {
            addDeliveryOptions(response.getDeliveryOptions());
        }
        else if (BuildConfig.DEBUG) {
            Log.w(TAG, "GetEligibleShipModesResponse returned with an error code");
        }
    }

    private void handleGetTridionSpecsResponse(@NonNull GetTridionSpecsResponse response) {
        if (response.isHttpStatusCodeSuccess()) {
            CommerceUiBuilder.setCartData(ShoppingCartFragment.this.mTicketGroupingResponse);
        }
        else {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "GetTridionSpecsResponse returned with an error code");
            }
        }
        updateCart(ShoppingCartFragment.this.mTicketGroupingResponse);
    }

    private void handleTicketGroupingResponse(@NonNull TicketGroupingResponse response) {
        if (response.isHttpStatusCodeSuccess()) {
            CommerceUiBuilder.setCartData(response);
            mTicketGroupingResponse = response;
            if(mIsPrimaryPageLoad) {
                sendOnPageLoadAnalytics(response.getOrder());
                mIsPrimaryPageLoad = false;
            }

            if (mPerformFullCartRefresh) {
                if (response.getOrder() != null && response.getOrder().getOrderItemGroups() != null) {
                    getTridionItemsForCart(response);
                }
                else {
                    updateCart(response);
                }
            }
            else {
                refreshDeliveryAndPricing(response);
            }
        }
        else if (BuildConfig.DEBUG) {
            Log.w(TAG, "TicketGroupingResponse returned with an error code");
        }
    }

    private void handleUpdateItemResponse(@NonNull UpdateItemResponse response) {
        if (response.isHttpStatusCodeSuccess()) {
            if (mSendAnalyticsType == SendAnalytics.REMOVE) {
                sendRemoveCartAnalytics();

                CartTicketGroupingRequest request = new CartTicketGroupingRequest.Builder(this)
                        .setConcurrencyType(ConcurrencyType.ASYNCHRONOUS)
                        .build();
                NetworkUtils.queueNetworkRequest(request);
                NetworkUtils.startNetworkService();
            }
            else if (mSendAnalyticsType == SendAnalytics.NONE) {
                requestCart(false);
            }
        }
        else {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "UpdateItemResponse returned with an error code");
            }
            requestCart(true);
        }
    }

    private void handleAddPromoCodeResponse(@NonNull AddPromoCodeResponse response) {
        if (response.isHttpStatusCodeSuccess()) {
            getTicketAdapter().setPromoCodeValid(true);
            requestCart(false);
        } else if (BuildConfig.DEBUG) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "AddPromoCodeResponse returned with an error code");
            }
            getTicketAdapter().setPromoCodeValid(false);
        }
    }

    private void handleRemovePromoCodeResponse(@NonNull RemovePromoCodeResponse response) {
        if (response.isHttpStatusCodeSuccess()) {
            if (mCurrentPromoCode != null) {
                mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
                AddPromoCodeRequest request = new AddPromoCodeRequest.Builder(ShoppingCartFragment.this)
                        .setPromoCode(mCurrentPromoCode)
                        .build();
                NetworkUtils.queueNetworkRequest(request);
                NetworkUtils.startNetworkService();
                mCurrentPromoCode = null;
            } else {
                requestCart(false);
            }
        } else {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "handleRemovePromoCodeResponse: RemovePromoCodeResponse returned with an error code");
            }

            RemovePromoCodeErrorResponse errorResponse = response.getNetworkErrorResponse();
            Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getEr71(), Toast.LENGTH_LONG)
                    .setIconColor(Color.RED)
                    .show();
        }
    }

    /**
     * Request the guest's shopping cart
     * @param performFullCartRefresh
     */
    private void requestCart(boolean performFullCartRefresh) {
        mPerformFullCartRefresh = performFullCartRefresh;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
            }
        });

        CartTicketGroupingRequest request = new CartTicketGroupingRequest.Builder(this)
                .setConcurrencyType(ConcurrencyType.ASYNCHRONOUS)
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    private void getTridionItemsForCart(@NonNull TicketGroupingResponse response) {
        List<String> ids = null;
        if (response.getOrder() != null) {
            ids = response.getOrder().getIdsForTridion();
        }

        if (ids != null && !ids.isEmpty()) {
            GetTridionSpecsRequest request = new GetTridionSpecsRequest.Builder(this)
                    .setConcurrencyType(ConcurrencyType.ASYNCHRONOUS)
                    .setIds(ids)
                    .build();
            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        } else {
            updateCart(mTicketGroupingResponse);
        }
    }

    private void updateCart(TicketGroupingResponse resp) {
        if (isCartEmpty(resp)) {
            showEmptyCart();
        } else {
            getTicketAdapter().clear();

            mTicketGrouping = resp.getOrder();
            TicketAssignmentUtils.instance().setTicketGrouping(mTicketGrouping);
            showShoppingCartTickets(resp.getOrder(), resp.getOrder().getOrderId());
            //Please note this doesn't work for any Android versions that require you asking for permission
            SecurityUtils.startProfiling(mTicketGrouping.getOrderId());

            requestShippingMethods();
        }
    }

    /**
     * Update the cart without clearing so the adapter doesn't cause the screen to scroll to the top
     * @param resp
     */
    private void refreshDeliveryAndPricing(TicketGroupingResponse resp) {
        if (isCartEmpty(resp)) {
            showEmptyCart();
        } else {
            mTicketGrouping = resp.getOrder();
            TicketAssignmentUtils.instance().setTicketGrouping(mTicketGrouping);
            if (null != mTicketGrouping && null != mTicketGrouping.getShipping()) {
                getTicketAdapter().setSelectedDeliveryOption(mTicketGrouping.getShipping().getShipModeId());
                getTicketAdapter().setPromoCodeEntry(mTicketGrouping.getPricing());
                getTicketAdapter().setPricing(mTicketGrouping.getDisplayPricing());
            }
            if (null != mTicketGrouping && null != mTicketGrouping.getOrderItemGroups()) {
                boolean isInventoryError = IceTicketUtils.hasInventoryError(mTicketGrouping.getOrderItemGroups());
                getTicketAdapter().setFooter(!isInventoryError);
            }
        }
    }

    private boolean isCartEmpty(TicketGroupingResponse resp) {
        if (null == resp || null == resp.getOrder() || isOrderEmpty(resp)) {
            return true;
        } else {
            return false;
        }

    }

    private void requestShippingMethods() {

        Context context = getContext();
        if (NetworkUtils.isNetworkConnected()) {

            GetEligibleShipModesRequest request = new GetEligibleShipModesRequest.Builder(this)
                    .setConcurrencyType(ConcurrencyType.ASYNCHRONOUS)
                    .build();
            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        } else {
            Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getEr71(), Toast.LENGTH_LONG)
                    .setIconColor(Color.RED)
                    .show();
        }

    }

    private void addDeliveryOptions(List<DeliveryOption> shippingModeList) {
        if (null != mTicketGrouping) {
            boolean isInventoryError = IceTicketUtils.hasInventoryError(mTicketGrouping.getOrderItemGroups());

            if (null != mTicketGrouping.getShipping()) {
                String selectedShipModeId = mTicketGrouping.getShipping().getShipModeId();
                getTicketAdapter().setDeliveryOptions(shippingModeList, selectedShipModeId);
                getTicketAdapter().setPromoCodeEntry(mTicketGrouping.getPricing());
                getTicketAdapter().setPricing(mTicketGrouping.getDisplayPricing());

                //if the selected delivery option ID is not in the list select the first option and update
                if (!hasSelectedOption(shippingModeList, selectedShipModeId)) {
                    DeliveryOption deliveryOption = getFirstDeliveryOption(shippingModeList);
                    if (deliveryOption != null) {
                        if (!isInventoryError) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
                                }
                            });
                            mSendAnalyticsType = SendAnalytics.NONE;
                            requestPricing(deliveryOption, false);
                        }
                    }
                }
            }
            if (null != mTicketGrouping.getOrderItemGroups()) {
                getTicketAdapter().setFooter(!isInventoryError && (shippingModeList != null && shippingModeList.size() > 0));
                if (isInventoryError) {
                    showMessage(mTridionConfig.getEr55());
                }
            }
        }
    }

    private void requestPricing(DeliveryOption deliveryOption, boolean performFullCartRefresh) {
        mPerformFullCartRefresh = performFullCartRefresh;
        UpdateItemRequest request = new UpdateItemRequest.Builder(this)
                .setConcurrencyType(ConcurrencyType.ASYNCHRONOUS)
                .setDeliveryOption(mTicketGrouping, deliveryOption)
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    /**
     * get the first available delivery option ID
     *
     * @param deliveryOptions
     * @return
     */
    private DeliveryOption getFirstDeliveryOption(List<DeliveryOption> deliveryOptions) {
        if (deliveryOptions != null) {
            for (DeliveryOption deliveryOption : deliveryOptions) {
                if (deliveryOption != null) {
                    return deliveryOption;
                }
            }
        }
        return null;
    }


    /**
     * check if the selected ID is the ID of at least one item in the delivery options
     *
     * @param deliveryOptions
     * @param selectedId
     * @return
     */
    private boolean hasSelectedOption(List<DeliveryOption> deliveryOptions, String selectedId) {
        if (deliveryOptions != null) {
            for (DeliveryOption deliveryOption : deliveryOptions) {
                if (selectedId.equals(deliveryOption.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOrderEmpty(TicketGroupingResponse response) {
        return null == response || null == response.getOrder() || response.getOrder().isEmpty();
    }

    /**
     * Shows the Views for an empty shopping cart
     */
    private void showEmptyCart() {
        tvNoItems.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        btnShopNow.setVisibility(View.VISIBLE);
    }

    public void showShoppingCartTickets(TicketGroupOrder order, String orderId) {
        if (order != null && order.getOrderItemGroups() != null) {
            List<ParkTicketGroups> parkTicketGroups = new ArrayList<>();
            if (order.getOrderItemGroups().getParkTicketGroups() != null) {
                parkTicketGroups.addAll(order.getOrderItemGroups().getParkTicketGroups());
            }
            if (order.getOrderItemGroups().getAnnualPassParkTicketGroups() != null) {
                parkTicketGroups.addAll(order.getOrderItemGroups().getAnnualPassParkTicketGroups());
            }
            getTicketAdapter().addParkTickets(parkTicketGroups, orderId);
            getTicketAdapter().addExpressPassTickets(order.getOrderItemGroups() .getExpressPassGroups(), orderId);
            getTicketAdapter().addAddOnTickets(order.getOrderItemGroups() .getAddOnsMap(), orderId);
            getTicketAdapter().addPromoItem(order.getOffer());
        }
    }

    private ShoppingCartAdapter getTicketAdapter() {
        if (null == mTicketAdapter) {
            mTicketAdapter = new ShoppingCartAdapter(mTridionConfig, mActionCallbacks);
            mRecyclerView.setAdapter(mTicketAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        return mTicketAdapter;
    }

    private void removeItemFromCart(OrderItem orderItem) {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        removeItemsFromCart(orderItems);
    }

    private void removeItemsFromCart(List<OrderItem> orderItems) {
        if (orderItems != null && orderItems.size() > 0) {
            mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
            List<String> orderItemIds = new ArrayList<>(orderItems.size());
            for (OrderItem orderItem : orderItems) {
                orderItemIds.add(orderItem.getOrderItemId());
            }

            if (NetworkUtils.isNetworkConnected()) {
                if (orderItemIds.size() > 0) {
                    mSendAnalyticsType = SendAnalytics.REMOVE;

                    mPerformFullCartRefresh = true;
                    UpdateItemRequest request = new UpdateItemRequest.Builder(this)
                            .setConcurrencyType(ConcurrencyType.ASYNCHRONOUS)
                            .addOrderItemsToRemove(orderItemIds)
                            .build();
                    NetworkUtils.queueNetworkRequest(request);
                    NetworkUtils.startNetworkService();
                }
            } else {
                Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getEr71(), Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
        }
    }

    private void handleAddItemResponse(@NonNull AddItemResponse response) {
        if (response.isHttpStatusCodeSuccess()) {
            requestCart(true);
        } else {
            AddItemErrorResponse errorResponse = response.getNetworkErrorResponse();
            String errorString = IceTicketUtils.getUserErrorMessage(errorResponse, getContext());

            if (!TextUtils.isEmpty(errorString)) {
                Toast.makeText(getContext(), errorString, Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
            requestCart(true);
        }
    }

    private void addCopyOfSingleItem(@NonNull String partNumber, List<CommerceAttribute> attributes) {

        AddItemRequest request = new AddItemRequest.Builder(this)
                .setConcurrencyType(ConcurrencyType.ASYNCHRONOUS)
                .addOrderItemWithAttributes(partNumber, attributes)
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == btnShopNow.getId()) {
            // Nothing in the cart so go to Select Tickets
            if (mContinueShoppingListener != null) {
                mContinueShoppingListener.onContinueShopping(ShoppingActivity.SELECT_TICKETS);
            }
        }
    }

    private void showMessage(final String message) {
        // Post to handler to make sure it is on the main thread
        Handler handler = new Handler(getActivity().getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
        });
    }

    private void dismissLoadingView(){
        if (null != mLoadingView) {
            mLoadingView.dismiss();
        }
    }

//    //TODO Analytics will be refactored at a later date
    private void sendRemoveCartAnalytics(){
        AnalyticsUtils.trackTicketsPageView(
                AnalyticsUtils.CONTENT_SUB_2_TICKET_SHOPPING_CART,
                AnalyticsUtils.PROPERTY_NAME_CHECKOUT,
                AnalyticsUtils.CONTENT_SUB_2_TICKET_SHOPPING_CART,
                AnalyticsUtils.CONTENT_GROUP_SITE_TICKETS,
                AnalyticsUtils.PROPERTY_NAME_CHECKOUT_PROCESS,
                AnalyticsUtils.PROPERTY_NAME_SHOPPING,
                null,
                null,
                "event2, scRemove",
                false,
                null,
                null,
                null,
                null,
                null,
                null);

    }

    //TODO Analytics will be refactored at a later date
    private void sendAddToCartAnalytics(){
        AnalyticsUtils.trackTicketsPageView(
                AnalyticsUtils.CONTENT_SUB_2_TICKET_SHOPPING_CART,
                AnalyticsUtils.PROPERTY_NAME_CHECKOUT,
                AnalyticsUtils.CONTENT_SUB_2_TICKET_SHOPPING_CART,
                AnalyticsUtils.CONTENT_GROUP_SITE_TICKETS,
                AnalyticsUtils.PROPERTY_NAME_CHECKOUT_PROCESS,
                AnalyticsUtils.PROPERTY_NAME_SHOPPING,
                null,
                null,
                "event2, scAdd",
                false,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    private void sendOnCheckoutClickedAnalytics(TicketGroupOrder ticketGroupOrder) {
        // since these values are just passed as analytics data without further computation, they are typed as Strings
        // correct
        Map<String, Object> extraData = getExtraMapForCheckoutPage(ticketGroupOrder);
        extraData.put(AnalyticsUtils.KEY_EVENTS, "scCheckout");
        AnalyticsUtils.trackPageView(
                AnalyticsUtils.CONTENT_GROUP_SALES,
                AnalyticsUtils.CONTENT_FOCUS_CHECKOUT,
                null, null,
                null,
                null,
                extraData);
    }

    private void sendOnPageLoadAnalytics(TicketGroupOrder ticketGroupOrder) {
        // using member variable to properly capture value of 'products' parameter depending
        // on the state of the ticket grouping object when the page loads
        // since these values are just passed as analytics data without further computation, they are typed as Strings


        AnalyticsUtils.trackPageView(
                AnalyticsUtils.CONTENT_GROUP_SALES,
                AnalyticsUtils.CONTENT_FOCUS_CHECKOUT,
                null,
                null,
                null,
                null,
                getExtraMapForCheckoutPage(ticketGroupOrder));
    }

    public static Map<String, Object> getExtraMapForCheckoutPage(TicketGroupOrder ticketGroupOrder) {
        Map<String, Object> extraData = new HashMap<String, Object>();
        String userSubTotalAmount = "";
        String userShippingAmount = "";
        String userTaxesAmount = "";
        String userTotalAmount = "";
        String userDeliveryMethod = "";

        if(ticketGroupOrder != null) {
            if (ticketGroupOrder.getPricing() != null) {

                if (ticketGroupOrder.getPricing().getTotalProductPrice() != null) {
                    userSubTotalAmount = ticketGroupOrder.getPricing().getTotalProductPrice().toString();
                }

                BigDecimal shippingCharge = BigDecimal.ZERO;
                BigDecimal shippingTax = BigDecimal.ZERO;
                if (ticketGroupOrder.getPricing().getShippingCharge() != null) {
                    shippingCharge = ticketGroupOrder.getPricing().getShippingCharge();
                }

                if (ticketGroupOrder.getPricing().getShippingTax() != null) {
                    shippingTax = ticketGroupOrder.getPricing().getShippingTax();
                }

                userShippingAmount = shippingCharge.add(shippingTax).toString();


                BigDecimal salesTax = BigDecimal.ZERO;
                if (ticketGroupOrder.getPricing().getSalesTax() != null) {
                    salesTax = ticketGroupOrder.getPricing().getSalesTax();
                }

                userTaxesAmount = salesTax.add(shippingTax).toString();

                if (ticketGroupOrder.getPricing().getGrandTotal() != null) {
                    userTotalAmount = ticketGroupOrder.getPricing().getGrandTotal().toString();
                }
            }

            if (ticketGroupOrder.getShipping() != null) {
                userDeliveryMethod = ticketGroupOrder.getShipping().getShipModeId();
            }
        }

        extraData.put(AnalyticsUtils.KEY_USER_SUBTOTAL, userSubTotalAmount);
        extraData.put(AnalyticsUtils.KEY_USER_SHIPPING, userShippingAmount);
        extraData.put(AnalyticsUtils.KEY_USER_TAXES, userTaxesAmount);
        extraData.put(AnalyticsUtils.KEY_USER_TOTAL, userTotalAmount);
        extraData.put(AnalyticsUtils.KEY_USER_DELIVERY, userDeliveryMethod);

        return extraData;
    }

}
