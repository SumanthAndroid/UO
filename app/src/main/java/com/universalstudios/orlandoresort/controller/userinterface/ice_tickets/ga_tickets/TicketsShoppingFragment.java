package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.ga_tickets;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.FullScreenLoadingView;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.CommerceGroupListener;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.CommerceUiBuilder;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.OnCartCountChangeListener;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.OnCartSubTotalChangeListener;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.OnFilterAppliedListener;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.ShoppingActivity;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.ShoppingActivity.ShopItemType;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.ShoppingFilterFragment;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.ShoppingFragmentInteractionListener;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.controller.userinterface.web.WebViewActivity;
import com.universalstudios.orlandoresort.databinding.CommerceTicketShoppingFragmentBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.request.AddItemRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.response.AddItemErrorResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.response.AddItemResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.CartTicketGroupingRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.Offer;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupOrder;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupingResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.PersonalizationExtrasProduct;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCard;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceGroup;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetTridionSpecsRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses.GetTridionSpecsResponse;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpecManager;
import com.universalstudios.orlandoresort.view.custom_calendar.TicketType;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetCardsRequest.DEFAULT_TICKET_BMG_PATH;
import static com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetCardsRequest.DEFAULT_TICKET_UEP_PATH;
import static com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetCardsRequest.DEFAULT_UEP_PATH;
import static com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetCardsRequest.ONE_DAY_VISIT_STR_IDENTIFIER;
import static com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetCardsRequest.UEP_IDENTIFIER;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: TicketsShoppingFragment
 * Class Description: Fragment for users to purchase admission tickets
 */
public class TicketsShoppingFragment extends NetworkFragment implements OnFilterAppliedListener, View.OnClickListener, CommerceGroupListener {
    public static final String TAG = TicketsShoppingFragment.class.getSimpleName();

    private static final String KEY_ARG_SHOP_ITEM_TYPE = "KEY_ARG_SHOP_ITEM_TYPE";
    private static final String KEY_STATE_LATEST_CARDS = "KEY_STATE_LATEST_CARDS";
    private static final String KEY_STATE_ANNUAL_PASS = "KEY_STATE_ANNUAL_PASS";
    private static final String KEY_STATE_ANNUAL_PASS_FL_RESIDENT = "KEY_STATE_ANNUAL_PASS_FL_RESIDENT";

    private LinearLayout mCardsContainer;
    private TextView mFilterText;
    private TextView mChangeFilterText;
    private TextView mOnlineOfferText;
    private Button mContinueButton;
    private Button mBackButton;
    private FullScreenLoadingView mLoadingView;
    private OnCartSubTotalChangeListener mParentOnCartSubTotalChangeListener;
    private OnCartCountChangeListener mParentOnCartCountChangeListener;
    private int mShopItemType;
    private TridionConfig mTridionConfig;
    private ShoppingFragmentInteractionListener mShoppingFragmentInteractionListener;
    private List<CommerceCard> mLatestCards;
    private TextView mOnlineOfferHeaderText;
    private TextView mOnlineOfferHeaderLink;
    private ViewGroup mBestPriceContainer;
    private ViewGroup mHeaderMoreInfoContainer;
    private TextView mBestPriceHeaderText;
    private TextView mBestPriceDescText;
    private TextView mFooterText;
    private boolean mIsAnnualPass;
    private boolean mIsAnnualPassFloridaResident;

    public static TicketsShoppingFragment newInstance(@ShopItemType int shopItemType) {
        // Create a new fragment instance
        TicketsShoppingFragment fragment = new TicketsShoppingFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        // Add parameters to the argument bundle
        args.putInt(KEY_ARG_SHOP_ITEM_TYPE, shopItemType);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Default parameters
        Bundle args = getArguments();
        if (args == null) {
            mShopItemType = -1;
        }
        // Otherwise, set incoming parameters
        else {
            mShopItemType = args.getInt(KEY_ARG_SHOP_ITEM_TYPE);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            sendCreationAnalytics();
            mIsAnnualPass = false;
            mIsAnnualPassFloridaResident = false;
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {
            mLatestCards = Parcels.unwrap(savedInstanceState.getParcelable(KEY_STATE_LATEST_CARDS));
            mIsAnnualPass = savedInstanceState.getBoolean(KEY_STATE_ANNUAL_PASS);
            mIsAnnualPassFloridaResident = savedInstanceState.getBoolean(KEY_STATE_ANNUAL_PASS_FL_RESIDENT);
        }

        mTridionConfig = IceTicketUtils.getTridionConfig();

        Activity parentActivity = getActivity();
        if (parentActivity != null) {
            parentActivity.setTitle(getTitleText(mShopItemType));
            parentActivity.invalidateOptionsMenu();
        }

        updateCart();

        //if we already have cards to display, no need to show filter
        if (mLatestCards == null) {
            showFilter(true);
        } else{
            List<String> ids = IceTicketUtils.getIdsForTridion(mLatestCards);
            showLoadingView();

            GetTridionSpecsRequest tridionSpecsRequest = new GetTridionSpecsRequest.Builder(this)
                    .setIds(ids)
                    .build();
            NetworkUtils.queueNetworkRequest(tridionSpecsRequest);
            NetworkUtils.startNetworkService();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_STATE_LATEST_CARDS, Parcels.wrap(mLatestCards));
        outState.putBoolean(KEY_STATE_ANNUAL_PASS, mIsAnnualPass);
        outState.putBoolean(KEY_STATE_ANNUAL_PASS_FL_RESIDENT, mIsAnnualPassFloridaResident);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CommerceTicketShoppingFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.commerce_ticket_shopping_fragment, container, false);
        binding.setTridion(mTridionConfig);
        View layout = binding.getRoot();
        mCardsContainer = binding.commerceCardsContainer;
        mFilterText = binding.commerceFilterText;
        mChangeFilterText = binding.commerceFilterChangeFilter;
        mOnlineOfferHeaderText = binding.fragmentTicketShoppingProductSelectYourTicketText;
        mOnlineOfferHeaderLink = binding.commerceTicketShoppingHeaderLink;
        mOnlineOfferText = binding.commerceOnlineOffer;
        mBestPriceHeaderText = binding.fragmentTicketShoppingProductBestPriceGuaranteeText;
        mBestPriceDescText = binding.fragmentTicketShoppingProductBestPriceGuaranteeDetailText;
        mContinueButton = binding.shoppingContinue;
        mBackButton = binding.shoppingBack;
        mBestPriceContainer = binding.fragmentTicketshoppingBestPriceContainer;
        mHeaderMoreInfoContainer = binding.commerceTicketShoppingHeaderLinkContainer;
        mFooterText = binding.commerceTicketShoppingFragmentTicketFooter;

        mChangeFilterText.setOnClickListener(this);
        mContinueButton.setOnClickListener(this);

        setHeaderLayout();
        setFooterLayout();

        return layout;
    }

    private void setHeaderLayout() {
        if (mIsAnnualPass) {
            mOnlineOfferHeaderText.setText(mTridionConfig.getPageHeaderAPTitle());
            mOnlineOfferText.setText(mTridionConfig.getPageHeaderAPIntroText());
            mOnlineOfferHeaderLink.setText(mTridionConfig.getPageHeaderAPLinkLabel());

            mOnlineOfferText.setVisibility(View.VISIBLE);
            mHeaderMoreInfoContainer.setVisibility(View.VISIBLE);

            mHeaderMoreInfoContainer.setOnClickListener(this);
        } else if (mShopItemType == ShoppingActivity.SELECT_EXPRESS_PASS) {
            //this view isn't set as a variable as we will only set the text header once
            mFilterText.setText(CommerceUiBuilder.getCurrentFilter().getTicketFilterString());
            mBackButton.setVisibility(View.VISIBLE);
            mBackButton.setOnClickListener(this);
            mOnlineOfferHeaderText.setText(mTridionConfig.getPageHeaderEPTitle());
            mOnlineOfferText.setText(mTridionConfig.getPageHeaderEPIntroText());
            mOnlineOfferHeaderLink.setVisibility(View.GONE);
            mBestPriceContainer.setVisibility(View.INVISIBLE);

            mHeaderMoreInfoContainer.setVisibility(View.GONE);
            mHeaderMoreInfoContainer.setOnClickListener(null);

        } else if (mShopItemType == ShoppingActivity.SELECT_TICKET_BMG_BUNDLE) {
            mBestPriceContainer.setVisibility(View.VISIBLE);
            mOnlineOfferHeaderText.setText(mTridionConfig.getPageHeaderAOCTitle());
            mBestPriceHeaderText.setText(mTridionConfig.getPromoBPTitle());
            mBestPriceDescText.setText(mTridionConfig.getPromoBPTeaserText());

            mHeaderMoreInfoContainer.setVisibility(View.GONE);
            mHeaderMoreInfoContainer.setOnClickListener(null);

            mBestPriceContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendBestPriceGuaranteeAnalytics();
                    Bundle args = WebViewActivity.newInstanceBundleWithRawHtmlContent(
                            mTridionConfig.getPromoBPTitle(), mTridionConfig.getPromoBPDetails());
                    startActivity(new Intent(getActivity(), WebViewActivity.class).putExtras(args));
                }
            });
        } else if (mShopItemType == ShoppingActivity.SELECT_TICKET_UEP_BUNDLE) {
            //this view isn't set as a variable as we will only set the text header once
            mFilterText.setText(CommerceUiBuilder.getCurrentFilter().getTicketFilterString());
            mOnlineOfferHeaderText.setText(mTridionConfig.getPageHeaderUEPCTitle());
            mOnlineOfferText.setText(mTridionConfig.getPageHeaderEPIntroText());
            mOnlineOfferHeaderLink.setVisibility(View.GONE);
            mBestPriceContainer.setVisibility(View.INVISIBLE);

            mHeaderMoreInfoContainer.setVisibility(View.GONE);
            mHeaderMoreInfoContainer.setOnClickListener(null);

        } else {
            mBestPriceContainer.setVisibility(View.VISIBLE);
            mOnlineOfferHeaderText.setText(mTridionConfig.getPageHeaderPTTitle());
            mBestPriceHeaderText.setText(mTridionConfig.getPromoBPTitle());
            mBestPriceDescText.setText(mTridionConfig.getPromoBPTeaserText());

            mHeaderMoreInfoContainer.setVisibility(View.GONE);
            mHeaderMoreInfoContainer.setOnClickListener(null);

            mBestPriceContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendBestPriceGuaranteeAnalytics();
                    Bundle args = WebViewActivity.newInstanceBundleWithRawHtmlContent(
                            mTridionConfig.getPromoBPTitle(), mTridionConfig.getPromoBPDetails());
                    startActivity(new Intent(getActivity(), WebViewActivity.class).putExtras(args));
                }
            });
        }
    }

    private void setFooterLayout() {
        if (mIsAnnualPass) {
            List<String> footerItems = new ArrayList<>();
            for (CommerceCard card : mLatestCards) {
                footerItems.addAll(card.getAnnualPassFooterStrings());
            }


            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < footerItems.size(); i++) {
                if (i != 0) {
                    sb.append("\n");
                }
                sb.append(footerItems.get(i));
            }

            mFooterText.setText(sb.toString());
            mFooterText.setVisibility(View.VISIBLE);
        } else {
            mFooterText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Check if parent fragment (if there is one) implements the interface
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof OnCartSubTotalChangeListener) {
            mParentOnCartSubTotalChangeListener = (OnCartSubTotalChangeListener) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (context != null && context instanceof OnCartSubTotalChangeListener) {
            mParentOnCartSubTotalChangeListener = (OnCartSubTotalChangeListener) context;
        }
        // If neither implements the interface, log a warning
        else if (mParentOnCartSubTotalChangeListener == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement OnCartSubTotalChangeListener");
            }
        }

        // Check if parent fragment (if there is one) implements the interface
        if (parentFragment != null && parentFragment instanceof OnCartCountChangeListener) {
            mParentOnCartCountChangeListener = (OnCartCountChangeListener) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (context != null && context instanceof OnCartCountChangeListener) {
            mParentOnCartCountChangeListener = (OnCartCountChangeListener) context;
        }
        // If neither implements the interface, log a warning
        else if (mParentOnCartCountChangeListener == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement OnCartCountChangeListener");
            }
        }

        // Check if parent fragment (if there is one) implements the interface
        if (parentFragment != null && parentFragment instanceof ShoppingFragmentInteractionListener) {
            mShoppingFragmentInteractionListener = (ShoppingFragmentInteractionListener) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (context != null && context instanceof ShoppingFragmentInteractionListener) {
            mShoppingFragmentInteractionListener = (ShoppingFragmentInteractionListener) getActivity();
        }
        // If neither implements the interface, log a warning
        else if (mShoppingFragmentInteractionListener == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement ContinueShoppingListener");
            }
            throw new RuntimeException(context.toString()
                    + " must implement ShoppingFragmentInteractionListener");
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mLatestCards = Parcels.unwrap(savedInstanceState.getParcelable(KEY_STATE_LATEST_CARDS));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mParentOnCartSubTotalChangeListener = null;
        mParentOnCartCountChangeListener = null;
        mShoppingFragmentInteractionListener = null;
    }

    private void showFilter(boolean loadDefault) {
        // Ensure the proper identifier is set
        // TODO Revisit these cases when the CommerceUiBuilder and ShoppingFilterFragment are refactored
        // For now, each if statement is to prevent the default selection from being overwritten.
        switch (mShopItemType) {
            case ShoppingActivity.SELECT_TICKETS:
                if(CommerceUiBuilder.getCurrentIdentifier() != null && CommerceUiBuilder.getCurrentIdentifier().toUpperCase().contains(UEP_IDENTIFIER)) {
                    CommerceUiBuilder.setCurrentIdentifier(null);
                }
                break;
            case ShoppingActivity.SELECT_TICKET_BMG_BUNDLE:
                if(CommerceUiBuilder.getCurrentIdentifier() != null && CommerceUiBuilder.getCurrentIdentifier().toUpperCase().contains(UEP_IDENTIFIER)) {
                    CommerceUiBuilder.setCurrentIdentifier(DEFAULT_TICKET_BMG_PATH);
                }
                break;
            case ShoppingActivity.SELECT_TICKET_UEP_BUNDLE:
                if(CommerceUiBuilder.getCurrentIdentifier() != null && CommerceUiBuilder.getCurrentIdentifier().toUpperCase().contains(UEP_IDENTIFIER)) {
                    CommerceUiBuilder.setCurrentIdentifier(DEFAULT_TICKET_UEP_PATH);
                }
                break;
            case ShoppingActivity.SELECT_EXPRESS_PASS:
                if(CommerceUiBuilder.getCurrentIdentifier() == null || !CommerceUiBuilder.getCurrentIdentifier().toUpperCase().contains(UEP_IDENTIFIER)) {
                    CommerceUiBuilder.setCurrentIdentifier(DEFAULT_UEP_PATH);
                }
                break;
            case ShoppingActivity.SELECT_ADDONS:
                // TicketsShoppingFragment does not handle add ons
            default:
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "Invalid shop item type: " + mShopItemType);
                }
        }

        ShoppingFilterFragment filterFragment = ShoppingFilterFragment.newInstance(
                ShoppingActivity.convertShopItemTypeToTicketType(mShopItemType), CommerceUiBuilder.getCurrentIdentifier(), loadDefault);
        filterFragment.setOnFilterAppliedListener(this);
        filterFragment.show(getActivity().getSupportFragmentManager(), TAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commerceFilterChangeFilter:
                showFilter(false);
                break;
            case R.id.shoppingContinue:
                if (null != mShoppingFragmentInteractionListener) {
                    mShoppingFragmentInteractionListener.onContinueClicked();
                }
                break;
            case R.id.shoppingBack:
                if (null != mShoppingFragmentInteractionListener) {
                    mShoppingFragmentInteractionListener.onBackClicked();
                }
                break;
            case R.id.commerce_ticket_shopping_header_link_container:
                String url = mTridionConfig.getCompareAPUrl(mIsAnnualPassFloridaResident);
                if (!TextUtils.isEmpty(url)) {
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtras(WebViewActivity.newInstanceBundle(mTridionConfig.getPageHeaderAPLinkLabel(), url));
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onAddToCartClicked(List<OrderItem> orderItems) {
        //Add the selected ticket to the Guest's cart
        showLoadingView();
        if(CommerceUiBuilder.getCurrentIdentifier() != null) {
            if(CommerceUiBuilder.getCurrentIdentifier().toUpperCase().contains(UEP_IDENTIFIER) || CommerceUiBuilder.getCurrentIdentifier().toUpperCase().contains(ONE_DAY_VISIT_STR_IDENTIFIER)) {
                //this logic only works with the filter currently being limited to selecting tickets for one date. Once we support multiple dates, this
                //logic will need to be updated
                Date ticketDate = IceTicketUtils.retrieveSelectedCalendarDate(IceTicketUtils.getTicketTypeForIdentifier(CommerceUiBuilder.getCurrentIdentifier()));
                for(OrderItem orderItem : orderItems){
                    orderItem.setOrderDate(ticketDate);
                    if(BuildConfig.DEBUG){
                        Log.d(TAG, "Order item has a date added before adding it to the cart");
                    }
                }
            }
        }
        addItemsToCart(orderItems);
    }

    @Override
    public void onSelectClicked(CommerceGroup group) {
        if (group != null) {
            if (mShoppingFragmentInteractionListener != null) {
                PersonalizationExtrasProduct extrasProduct =
                        PersonalizationExtrasProduct.fromCommerceGroup(group);
                mShoppingFragmentInteractionListener.onSelectComboTicketClicked(extrasProduct);
            }
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "onSelectClicked: group is null");
            }
        }
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        dismissLoadingView();
        if (networkResponse instanceof TicketGroupingResponse) {
            if (networkResponse.isHttpStatusCodeSuccess()) {

                TicketGroupingResponse response = (TicketGroupingResponse) networkResponse;

                sendAddToCartAnalytics(response);
                CommerceUiBuilder.setCartData(response);
                refreshCartUi();

                if( response.getOrder() != null && response.getOrder().getMessages() != null && response.getOrder().getMessages().size() > 0) {
                    Toast.makeText(getContext(), response.getOrder().getMessages().get(0).getMessage(), Toast.LENGTH_LONG)
                            .setIconColor(Color.RED)
                            .show();
                }

            }
            else if (BuildConfig.DEBUG) {
                Log.w(TAG, "TicketGroupingResponse returned with an error code");
            }
        }
        else if (networkResponse instanceof AddItemResponse) {
            if (networkResponse.isHttpStatusCodeSuccess()) {
                AddItemResponse response = (AddItemResponse) networkResponse;
                AddItemErrorResponse errorResponse = response.getNetworkErrorResponse();
                if (errorResponse != null) {
                    String errorMsg = IceTicketUtils.getUserErrorMessage(errorResponse, getContext());
                    if (!TextUtils.isEmpty(errorMsg)) {
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG)
                                .setIconColor(Color.RED)
                                .show();
                    }
                }
                else {
                    Toast.makeText(getContext(), mTridionConfig.getTicketingConfirmationText(), android.widget.Toast.LENGTH_SHORT)
                            .setIcon(R.drawable.ic_cart)
                            .setIconColor(getResources().getColor(R.color.blue_color))
                            .setTextBold(true)
                            .show();

                    // Post to handler to make sure it is on the main thread
                    Handler mHandler = new Handler(getActivity().getMainLooper());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            showLoadingView();
                        }
                    });
                    updateCart();
                }
            }
            else if (BuildConfig.DEBUG) {
                Log.w(TAG, "AddItemResponse returned with an error code");
            }
        }
        else if (networkResponse instanceof GetTridionSpecsResponse) {
            if (networkResponse.isHttpStatusCodeSuccess()) {    // TODO This should come from manager?
                TicketType ticketType = ShoppingActivity.convertShopItemTypeToTicketType(mShopItemType);
                CommerceUiBuilder.createCards(mLatestCards, getActivity(), mCardsContainer, TicketsShoppingFragment.this, ticketType);
                setFooterLayout();
                if (mShopItemType == ShoppingActivity.SELECT_TICKETS || mShopItemType == ShoppingActivity.SELECT_TICKET_BMG_BUNDLE
                        || mShopItemType == ShoppingActivity.SELECT_TICKET_UEP_BUNDLE) {
                    mFilterText.setText(CommerceUiBuilder.getCurrentFilter().getTicketFilterString());
                } else if (mShopItemType == ShoppingActivity.SELECT_EXPRESS_PASS) {
                    String filterText = CommerceUiBuilder.getCurrentFilter().getUEPFilterString();
                    if(filterText == null || TextUtils.isEmpty(filterText.trim())){
                        filterText = CommerceUiBuilder.getCurrentFilter().getTicketFilterString();
                    }
                    mFilterText.setText(filterText);
                } else {
                    //TODO add logic for extras when we have it
                }

                if (!mIsAnnualPass) {
                    if ((mShopItemType == ShoppingActivity.SELECT_TICKETS)) {
                        TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();
                        CommerceGroup onlineSavingsGroup = IceTicketUtils.getBestOnlineSavingsGroup(mLatestCards);
                        mOnlineOfferText.setText(tridionConfig.getPageHeaderPTIntroText(onlineSavingsGroup));
                    }

                    double onlineOffer = IceTicketUtils.getOnlineOfferText(mLatestCards);
                    if (onlineOffer > 5 || mShopItemType == ShoppingActivity.SELECT_EXPRESS_PASS) {
                        mOnlineOfferText.setVisibility(View.VISIBLE);
                    } else {
                        mOnlineOfferText.setVisibility(View.GONE);
                    }
                }
            }
            else if (BuildConfig.DEBUG) {
                Log.w(TAG, "GetTridionSpecsResponse returned with an error code");
            }
        }
    }

    /**
     * Adds the given List of {@link OrderItem} products to the cart, provided the
     * List isn't empty and the user has an internet connection.
     *
     * @param orderItems The List of {@link OrderItem} products
     */
    private void addItemsToCart(List<OrderItem> orderItems) {

        if (NetworkUtils.isNetworkConnected() && !orderItems.isEmpty()) {

            AddItemRequest request = new AddItemRequest.Builder(this)
                    .setOrderItem(orderItems)
                    .build();
            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        }
    }

    private void updateCart() {

        CartTicketGroupingRequest request = new CartTicketGroupingRequest.Builder(this)
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    @Override
    public void onMoreClicked(CommerceGroup group) {
        String tcmId1 = "";
        if (null != group && !group.getCardItems().isEmpty()) {
            tcmId1 = group.getCardItems().get(0).getIdForTridion();
        }
        TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(tcmId1);
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtras(WebViewActivity.newInstanceBundleWithRawHtmlContent("", labelSpec.getCommerceCardDetailsHtmlString(group), false));
        startActivity(intent);
    }

    @Override
    public void onFilterApplied(final List<CommerceCard> cards, String serviceIdentifier) {
        if (null == cards || cards.isEmpty()) {
            return;
        }

        mIsAnnualPass = false;
        mIsAnnualPassFloridaResident = false;
        if (cards.get(0) != null) {
            CommerceCard commerceCard = cards.get(0);
            mIsAnnualPass = commerceCard.isAnnualPass();
            mIsAnnualPassFloridaResident = commerceCard.isFloridaPoo();
        }

        setHeaderLayout();

        mLatestCards = cards;
        List<String> ids = IceTicketUtils.getIdsForTridion(mLatestCards);
        showLoadingView();

        GetTridionSpecsRequest request = new GetTridionSpecsRequest.Builder(this)
                .setIds(ids)
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    private void refreshCartUi() {
        if (mParentOnCartSubTotalChangeListener != null) {
            mParentOnCartSubTotalChangeListener.onCartSubTotalChange();
        }
        if (mParentOnCartCountChangeListener != null) {
            mParentOnCartCountChangeListener.onCartCountChange();
        }
    }

    private String getTitleText(int shopItemType){
        TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();

        if (shopItemType == ShoppingActivity.SELECT_TICKETS) {
            return tridionConfig.getPageHeaderPTTitle();
        } else if (shopItemType == ShoppingActivity.SELECT_EXPRESS_PASS) {
            return tridionConfig.getPageHeaderEPAppTitle();
        } else if (shopItemType == ShoppingActivity.SELECT_ADDONS) {
            // TicketsShoppingFragment does not handle AddOns
            return "";
        } else if (shopItemType == ShoppingActivity.SELECT_TICKET_BMG_BUNDLE) {
            return mTridionConfig.getPageHeaderAOCTitle();
        } else if (shopItemType == ShoppingActivity.SELECT_TICKET_UEP_BUNDLE) {
            return mTridionConfig.getPageHeaderUEPCTitle();
        } else {
            return "";
        }
    }

    //TODO Analytics will be refactored for the february release
    private void sendCreationAnalytics() {
        AnalyticsUtils.trackTicketsPageView(
                AnalyticsUtils.CONTENT_SUB_2_TICKET_DETAILS,
                AnalyticsUtils.PROPERTY_NAME_TICKETS,
                AnalyticsUtils.CONTENT_SUB_2_TICKET_DETAILS,
                AnalyticsUtils.CONTENT_GROUP_SITE_TICKETS,
                AnalyticsUtils.PROPERTY_NAME_PRODUCT_SELECTION,
                AnalyticsUtils.PROPERTY_NAME_SHOPPING,
                null,
                null,
                "event2, event5, event38",
                false,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    //TODO Analytics will be refactored for the february release
    private void sendAddToCartAnalytics(@NonNull final TicketGroupingResponse response) {

        if(response.getOrder() == null || response.getOrder().getOrderItemGroups() == null){
            return;
        }

        TicketGroupOrder ticketGroupingResponse = response.getOrder();
        Offer offer = ticketGroupingResponse.getOffer();
        String offerCode = "";

        if(offer != null && offer.getCode() != null) {
            offerCode = offer.getCode();
        }

        Map<String, Object> extraData = new HashMap<String,Object>();
        extraData.put(AnalyticsUtils.KEY_SESSION_ID, AccountStateManager.getGuestId());
        extraData.put(AnalyticsUtils.KEY_OFFER_CODE, offerCode);
        extraData.put(AnalyticsUtils.KEY_EVENT_NAME, "ADD TO CART");
        extraData.put(AnalyticsUtils.KEY_EVENTS, "event2,event5,scAdd");

        AnalyticsUtils.trackTicketsPageView(
                AnalyticsUtils.CONTENT_SUB_2_TICKET_ADD_TO_CART,
                AnalyticsUtils.PROPERTY_NAME_TICKETS,
                AnalyticsUtils.CONTENT_SUB_2_TICKET_ADD_TO_CART,
                AnalyticsUtils.CONTENT_GROUP_SITE_TICKETS,
                AnalyticsUtils.PROPERTY_NAME_PRODUCT_SELECTION,
                AnalyticsUtils.PROPERTY_NAME_SHOPPING,
                null,
                null,
                "event2, event5, scAdd",
                true,
                null,
                mFilterText.getText().toString(),
                response.getOrder(),
                null,
                null,
                null);
    }

    //TODO Analytics will be refactored for the february release
    private void sendBestPriceGuaranteeAnalytics(){
        AnalyticsUtils.trackTicketsPageView(
                AnalyticsUtils.CONTENT_SUB_2_TICKETS,
                AnalyticsUtils.PROPERTY_NAME_TICKETS,
                AnalyticsUtils.PROPERTY_NAME_BEST_PRICE_GUARANTEE,
                AnalyticsUtils.CONTENT_GROUP_SITE_TICKETS,
                AnalyticsUtils.PROPERTY_NAME_PRODUCT_SELECTION,
                AnalyticsUtils.PROPERTY_NAME_BEST_PRICE_GUARANTEE,
                null,
                null,
                "event2, event5, event89",
                false,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    private void showLoadingView() {
        mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
    }

    private void dismissLoadingView() {
        if (mLoadingView != null) {
            mLoadingView.dismiss();
        }
    }
}
