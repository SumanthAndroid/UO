package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.CommerceUiBuilder;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets.TicketAssignmentUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkActivity;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.request.AddItemRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.response.AddItemErrorResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.response.AddItemResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.CartTicketGroupingRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupingResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.PersonalizationExtrasProduct;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetTridionSpecsRequest;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses.GetTridionSpecsResponse;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.ConcurrencyType;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddOnsWizardActivity extends NetworkActivity implements AddOnProgressFragment.OnNextClickListener, IAddOnsCallbacks {
    private static final String TAG = AddOnsWizardActivity.class.getSimpleName();

    private static final String KEY_ARG_PRODUCT_ID = "KEY_ARG_PRODUCT_ID";
    private static final String KEY_ARG_EXTRA_PRODUCT = "KEY_ARG_EXTRA_PRODUCT";
    private static final String KEY_ARG_QUANTITY_ADULT = "KEY_ARG_QUANTITY_ADULT";
    private static final String KEY_ARG_QUANTITY_CHILD = "KEY_ARG_QUANTITY_CHILD";

    private static final String KEY_STATE_STATE = "key_state_state";
    private static final String KEY_STATE_CURRENT_STEP = "key_state_current_step";
    private static final String KEY_STATE_CALL_IN_PROGRESS = "key_state_call_in_progress";

    private AddOnsState mState;
    private boolean mIsCallInProgress;

    private List<String> mSelectionSteps;
    private int mCurrentStep;
    private AddOnProgressFragment mProgressFragment;
    private ViewGroup mContainer;
    private ProgressBar mLoading;

    public static Intent newInstanceIntent(@NonNull Context context, @NonNull PersonalizationExtrasProduct product) {
        Intent intent = new Intent(context, AddOnsWizardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ARG_PRODUCT_ID, product.getUniqueId());
        bundle.putParcelable(KEY_ARG_EXTRA_PRODUCT, Parcels.wrap(product));
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceIntent(@NonNull Context context, @NonNull PersonalizationExtrasProduct product, int adultQuantity, int childQuantity) {
        Intent intent = new Intent(context, AddOnsWizardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ARG_PRODUCT_ID, product.getUniqueId());
        bundle.putParcelable(KEY_ARG_EXTRA_PRODUCT, Parcels.wrap(product));
        bundle.putInt(KEY_ARG_QUANTITY_ADULT, adultQuantity);
        bundle.putInt(KEY_ARG_QUANTITY_CHILD, childQuantity);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        setContentView(R.layout.activity_add_on_wizard);
        setTitle("");

        // If the action bar is null, then we have a problem
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            UserInterfaceUtils.enableActionBarHomeButton(actionBar);
        }

        mLoading = (ProgressBar) findViewById(R.id.activity_add_ons_wizard_loading);
        mContainer = (ViewGroup) findViewById(R.id.activity_add_ons_wizard_container);

        PersonalizationExtrasProduct personalizationExtrasProduct = null;
        int adultQuantity = -1;
        int childQuantity = -1;
        // Default parameters
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {

        }
        // Otherwise, set incoming parameters
        else {
            personalizationExtrasProduct = Parcels.unwrap(bundle.getParcelable(KEY_ARG_EXTRA_PRODUCT));
            if (bundle.containsKey(KEY_ARG_QUANTITY_ADULT) && bundle.containsKey(KEY_ARG_QUANTITY_CHILD)) {
                adultQuantity = bundle.getInt(KEY_ARG_QUANTITY_ADULT);
                childQuantity = bundle.getInt(KEY_ARG_QUANTITY_CHILD);
            }
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            mProgressFragment = AddOnProgressFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_add_ons_progress_container, mProgressFragment, AddOnProgressFragment.TAG)
                    .commit();
            getAddOnState().setPersonalizationExtrasProduct(personalizationExtrasProduct);
            mSelectionSteps = getAddOnState().getUiControls();
            if (adultQuantity != -1 && childQuantity != -1) {
                // Check if the extraProduct is strictly for all ages, if so set the all age quantity to adult + child
                if (personalizationExtrasProduct != null && personalizationExtrasProduct.isAllAgesOnly()) {
                    getAddOnState().setAllAgesQuantity(adultQuantity + childQuantity);
                } else {
                    getAddOnState().setAdultQuantity(adultQuantity);
                    getAddOnState().setChildQuantity(childQuantity);
                }
            }
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {
            mProgressFragment = (AddOnProgressFragment) getSupportFragmentManager().findFragmentByTag(AddOnProgressFragment.TAG);
            mCurrentStep = savedInstanceState.getInt(KEY_STATE_CURRENT_STEP, 0);
            mState = Parcels.unwrap(savedInstanceState.getParcelable(KEY_STATE_STATE));
            mSelectionSteps = mState.getUiControls();
            mIsCallInProgress = savedInstanceState.getBoolean(KEY_STATE_CALL_IN_PROGRESS, false);
            mCurrentStep = 0;
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStart");
        }
        getTridionLabelSpecs();
        showViewBasedOnState();
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
        outState.putInt(KEY_STATE_CURRENT_STEP, mCurrentStep);
        outState.putBoolean(KEY_STATE_CALL_IN_PROGRESS, mIsCallInProgress);
        outState.putParcelable(KEY_STATE_STATE, Parcels.wrap(mState));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStop");
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onOptionsItemSelected");
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return (super.onOptionsItemSelected(item));
        }
    }

    /**
     * if we are on the first step, use super to finish activity, otherwise,
     * decrement the current step, and show that step
     */
    @Override
    public void onBackPressed() {
        mCurrentStep--;
        if (mCurrentStep < 0) {
            super.onBackPressed();
        } else {
            showCurrentStep();
        }
    }

    private void getTridionLabelSpecs() {
        mIsCallInProgress = true;

        GetTridionSpecsRequest request = new GetTridionSpecsRequest.Builder(this)
                .setIds(mState.getAllTcmIds())
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    private void showViewBasedOnState() {
        if (mIsCallInProgress) {
            showLoadingView();
        } else {
            hideLoadingView();
        }
    }


    private void showLoadingView() {
        mLoading.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.GONE);
    }

    private void hideLoadingView() {
        mLoading.setVisibility(View.GONE);
        mContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNextClicked() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onNextClicked() called");
        }
        mCurrentStep++;
        showCurrentStep();
    }

    @Override
    public void onAddToCartClicked() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onAddToCartClicked() called");
        }
        // Disable add to cart button
        buttonEnabled(false);
        List<OrderItem> orderItems = getAddOnState().getOrderItems();
        AddItemRequest addItemRequest = new AddItemRequest.Builder(this)
                .setConcurrencyType(ConcurrencyType.ASYNCHRONOUS)
                .setOrderItem(orderItems)
                .build();
        NetworkUtils.queueNetworkRequest(addItemRequest);
        NetworkUtils.startNetworkService();
        sendAddonAddToCartAnalytics();
        mIsCallInProgress = true;
        showViewBasedOnState();
    }

    private void updateCart() {
        mIsCallInProgress = true;

        CartTicketGroupingRequest request = new CartTicketGroupingRequest.Builder(this)
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        mIsCallInProgress = false;

        if (networkResponse instanceof AddItemResponse) {
            AddItemResponse response = (AddItemResponse) networkResponse;
            if (networkResponse.isHttpStatusCodeSuccess()) {
                updateCart();
            } else {
                AddItemErrorResponse errorResponse = response.getNetworkErrorResponse();
                String errorMsg = IceTicketUtils.getUserErrorMessage(errorResponse, this);
                if (!TextUtils.isEmpty(errorMsg)) {
                    //return to step 1 if insufficient inventory error
                    if (errorResponse.hasInsufficientInventoryError() || errorResponse.isInventoryUnavailableError()) {
                        mCurrentStep = 0;
                        showCurrentStep();
                    }
                    Toast.makeText(this, errorMsg, Toast.LENGTH_LONG)
                            .setIconColor(Color.RED)
                            .show();
                }
            }
        } else if (networkResponse instanceof TicketGroupingResponse) {
            if (networkResponse.isHttpStatusCodeSuccess()) {
                TicketGroupingResponse response = (TicketGroupingResponse) networkResponse;
                // FIXME This UTILS class should not be making instances
                TicketAssignmentUtils.instance().setTicketGrouping(response.getOrder());
                CommerceUiBuilder.setCartData(response);
                setResult(RESULT_OK);
                finish();
            } else {
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "Response was null or returned with a failure code");
                }
            }
        } else if (networkResponse instanceof GetTridionSpecsResponse) {
            showCurrentStep();
        }

        showViewBasedOnState();
    }

    /**
     *
     */
    private void showCurrentStep() {
        Fragment fragment = null;
        String step = null;
        if (mSelectionSteps != null && mSelectionSteps.size() > mCurrentStep) {
            step = mSelectionSteps.get(mCurrentStep);
        }

        if (!TextUtils.isEmpty(step)) {
            if (PersonalizationExtrasProduct.UI_CONTROL_QUANTITY.equals(step)) {
                fragment = AddOnSelectQuantityFragment.newInstance();
            } else if (PersonalizationExtrasProduct.UI_CONTROL_DATE.equals(step)) {
                mState.setSelectedTime(null);
                mState.setSelectedTier(null);
                fragment = AddOnSelectDateFragment.newInstance();
            } else if (PersonalizationExtrasProduct.UI_CONTROL_TIME.equals(step)) {
                mState.setSelectedTime(null);
                mState.setSelectedTier(null);
                fragment = AddOnSelectTimeFragment.newInstance();
            } else if (PersonalizationExtrasProduct.UI_CONTROL_TIER_BMG.equals(step)) {
                mState.setSelectedTier(null);
                fragment = AddOnSelectTierFragment.newInstance();
            } else if (PersonalizationExtrasProduct.UI_CONTROL_CUSTOM_SKU_QUANTITY.equals(step)) {
                mState.setSelectedDate(null);
                mState.setSelectedTime(null);
                mState.setSelectedTier(null);
                fragment = AddOnSelectQuantityFragment.newInstance();
            }

            updateProgress();
            // The last step will show the add to cart button instead of next
            if (mCurrentStep == mSelectionSteps.size() - 1) {
                mProgressFragment.showAddToCartButton(false);
            } else {
                mProgressFragment.showNextButton(false);
            }
            mProgressFragment.updateSubtotal();

        }

        if (fragment != null) {
            final Fragment finalFragment = fragment;

            //this is may be call from onLoadFinished, need to run on UI thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.activity_add_ons_wizard_container, finalFragment)
                            .commitAllowingStateLoss();
                }
            });
        }
    }

    /**
     * set the progress
     */
    private void updateProgress() {
        if (mSelectionSteps != null && mProgressFragment != null) {
            // Hide progress bar if there is only one step
            mProgressFragment.toggleProgressBar(mSelectionSteps.size() > 1);
            double step = mCurrentStep + 1;
            double progress = step / mSelectionSteps.size();
            mProgressFragment.setProgress((int) Math.round(progress * 100));
        }
    }

    /**
     * @param enabled
     */
    @Override
    public void buttonEnabled(boolean enabled) {
        if (mProgressFragment != null && mSelectionSteps != null) {
            // update next button, not the last step
            if (mCurrentStep < mSelectionSteps.size() - 1) {
                mProgressFragment.showNextButton(enabled);
            } else {
                // update add to cart button, last step
                mProgressFragment.showAddToCartButton(enabled);
            }
        }
    }

    /**
     *
     */
    @Override
    public void updateSubtotal() {
        if (mProgressFragment != null) {
            mProgressFragment.updateSubtotal();
        }
    }

    @NonNull
    @Override
    public AddOnsState getAddOnState() {
        if (mState == null) {
            mState = new AddOnsState();
        }
        return mState;
    }

    private void sendAddonAddToCartAnalytics() {
        Map<String, Object> extraData = new HashMap<String, Object>();

        String offerCode = getAddOnState().getOffer();

        if(offerCode == null) {
            offerCode = "";
        }

        extraData.put(AnalyticsUtils.KEY_SESSION_ID, AccountStateManager.getGuestId());
        extraData.put(AnalyticsUtils.KEY_OFFER_CODE, offerCode);
        extraData.put(AnalyticsUtils.KEY_EVENT_NAME,  "EXTRAS PAGE");
        AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_SITE_TICKETS,
                        null,
                        null,
                        AnalyticsUtils.CONTENT_SUB_2_TICKET_ADD_TO_CART,
                        AnalyticsUtils.PROPERTY_NAME_CHECKOUT_PROCESS,
                        null,
                        extraData);
    }
}
