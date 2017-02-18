package com.universalstudios.orlandoresort.controller.userinterface.checkout;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.account.AccountUtils;
import com.universalstudios.orlandoresort.controller.userinterface.account.address.AddressInfo;
import com.universalstudios.orlandoresort.controller.userinterface.account.address.AddressInformationActivity;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.DialogUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.ShippingUtils;
import com.universalstudios.orlandoresort.controller.userinterface.link.ClickListener;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.controller.userinterface.web.WebViewActivity;
import com.universalstudios.orlandoresort.databinding.FragmentPaymentInformationBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.ErrorHandling.FLValidateZipDao;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Address;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.GetGuestProfileRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetGuestProfileResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.BillingAddress.AddBillingAddressRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.BillingAddress.AddBillingAddressResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.BillingAddress.Contact;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.shippinginfo.CartShippingInfoRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.shippinginfo.CartShippingInfoResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ShippingModeData.DeliveryOption;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ShippingModeData.GetEligibleShipModesRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ShippingModeData.GetEligibleShipModesResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.State.ProvState;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.CartTicketGroupingRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupOrder;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupingResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ValidateZip.ValidateZipRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.offers.GetPersonalizationOffersRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.SubmitPurchaseRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses.CardResult;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses.SubmitPurchaseResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.country_state.CountryStateProvinceStateManager;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.checkout.GetFlexpayContractRequest;
import com.universalstudios.orlandoresort.model.network.domain.checkout.GetFlexpayContractResponse;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.ConcurrencyType;
import com.universalstudios.orlandoresort.model.network.response.HttpResponseStatus;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.pricing.DisplayPricingModel;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;
import com.universalstudios.orlandoresort.model.state.account.AddressCache;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * PaymentInformationFragment provides the interface for users to select/enter their
 * credit card information, billing address, and shipping address when applicable.
 * Activities that contain this fragment must implement the
 * {@link PaymentInformationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaymentInformationFragment#newInstance()} factory method to
 * create an instance of this fragment.
 */
public class PaymentInformationFragment extends NetworkFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = PaymentInformationFragment.class.getSimpleName();

    private static final String KEY_ARG_SELECTED_ADDRESS_TYPE = "KEY_ARG_ADDRESS_INFO_ADDRESS_TYPE";
    private static final int ADDRESS_ACTIVITY_RESULT_CODE = 123;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CHECKOUT_STATE_START,
            CHECKOUT_CC_EXPIRATION_VALIDATION,
            CHECKOUT_STATE_FL_VALIDATION,
            CHECKOUT_STATE_CHECK_CART_ERRORS,
            CHECKOUT_STATE_BILLING_ADDRESS,
            CHECKOUT_STATE_SHIPPING_ADDRESS,
            CHECKOUT_STATE_CART_SHIPPING_INFO,
            CHECKOUT_STATE_PROCESS_PAYMENT})
    private @interface CheckoutState {}

    // Declare the constants
    private static final int CHECKOUT_STATE_START = 0;
    private static final int CHECKOUT_CC_EXPIRATION_VALIDATION = 1;
    private static final int CHECKOUT_STATE_FL_VALIDATION = 2;
    private static final int CHECKOUT_STATE_CHECK_CART_ERRORS = 3;
    private static final int CHECKOUT_STATE_BILLING_ADDRESS = 4;
    private static final int CHECKOUT_STATE_SHIPPING_ADDRESS = 5;
    private static final int CHECKOUT_STATE_CART_SHIPPING_INFO = 7;
    private static final int CHECKOUT_STATE_PROCESS_PAYMENT = 8;


    private PaymentInfoViewModel mPaymentInfoViewModel;

    private GuestProfile guestProfile;

    private CountryStateArrays mCountryStateArrays;
    // TODO Update fragment interactions
    private OnFragmentInteractionListener mListener;
    private ProfileAddressSpinnerAdapter mBillingAddressSpinnerAdapter;
    private ProfileAddressSpinnerAdapter mShippingAddressSpinnerAdapter;

    private FragmentPaymentInformationBinding mBinding;
    private String selectedBillingAddressId;
    private String selectedShippingAddressId;
    private TicketGroupOrder mOrder;
    private String mBillingAddressId;
    private String mShippingAddressId;

    private TridionConfig mTridionConfig;

    @CheckoutState private int mCheckoutState;

    private ClickListener flexPayTncLinkClickListener = new ClickListener() {
        @Override
        public void onLinkClicked() {
            requestFlexContract();
        }
    };

    public PaymentInformationFragment() {
        // Required empty public constructor
    }

    public static PaymentInformationFragment newInstance() {
        PaymentInformationFragment fragment = new PaymentInformationFragment();
        Bundle args = new Bundle();
        // add arguments here
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // retrieve arguments here
        }
        mTridionConfig = IceTicketUtils.getTridionConfig();

        mCountryStateArrays = new CountryStateArrays(
                CountryStateProvinceStateManager.getCountriesInstance().getCountries(),
                CountryStateProvinceStateManager.getStateProvincesInstance().getStateProvinces());
        mPaymentInfoViewModel = new PaymentInfoViewModel(AccountStateManager.isUserLoggedIn(),
                mCountryStateArrays);
        sendPaymentPageViewAnalytics();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Set the action bar title
        setToolbarTitle();

        requestCart();
    }

    @Override
    public void onPause() {
        AddressCache.setBillingAddress(mPaymentInfoViewModel.billingAddressInfo);
        if (mPaymentInfoViewModel.getShippingRequired()) {
            AddressCache.setShippingAddress(mPaymentInfoViewModel.shippingAddressInfo);
        }
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_payment_information, container, false);
        View view = mBinding.getRoot();
        TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();
        mBinding.setTridion(tridionConfig);
        mBinding.setCountryStateArrays(mCountryStateArrays);
        mBinding.setPaymentInfo(mPaymentInfoViewModel);

        mBinding.buttonCompletePaymentInformation.setOnClickListener(this);
        mBinding.savedBillingInfoPrimaryAddressEdit.setOnClickListener(this);
        mBinding.savedShippingInfoPrimaryAddressEdit.setOnClickListener(this);

        mBillingAddressSpinnerAdapter = new ProfileAddressSpinnerAdapter(getActivity(), mPaymentInfoViewModel.getSavedAddresses());
        mBinding.savedBillingInfoSpinner.setAdapter(mBillingAddressSpinnerAdapter);
        mShippingAddressSpinnerAdapter = new ProfileAddressSpinnerAdapter(getActivity(), mPaymentInfoViewModel.getSavedAddresses());
        mBinding.savedShippingInfoSpinner.setAdapter(mShippingAddressSpinnerAdapter);

        // If the only item is add new item, hide the arrow for the spinners
        if(mBinding.savedBillingInfoSpinner.getCount() == 1) {
            mBinding.savedBillingInfoSpinnerArrow.setVisibility(View.GONE);
        }
        if(mBinding.savedShippingInfoSpinner.getCount() == 1) {
            mBinding.savedShippingInfoSpinnerArrow.setVisibility(View.GONE);
        }

        mBinding.layoutAddressInfoBillingInclude.phoneNumberInfoPopup.setOnClickListener(this);
        mBinding.layoutAddressInfoShippingInclude.phoneNumberInfoPopup.setOnClickListener(this);
        mBinding.ccSecurityCodeInfoPopup.setOnClickListener(this);

        return view;
    }

    @Override
    public void handleNetworkResponse(NetworkResponse response) {

        Handler mHandler = new Handler(getActivity().getMainLooper());
        mHandler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.cancelLoadingView(getActivity().getSupportFragmentManager());
                    }
                });

        if (response instanceof CartShippingInfoResponse) {
            handleCartShippingInfoResponse((CartShippingInfoResponse) response);
        }
        else if (response instanceof TicketGroupingResponse) {
            if (response.isHttpStatusCodeSuccess()) {
                mOrder = ((TicketGroupingResponse) response).getOrder();
                if (mCheckoutState == CHECKOUT_STATE_CHECK_CART_ERRORS) {
                    if (null != mOrder && null != mOrder.getOrderItemGroups()) {
                        boolean isInventoryError = IceTicketUtils.hasInventoryError(mOrder.getOrderItemGroups());
                        if (isInventoryError) {
                            getActivity().finish();
                        }
                        else {
                            processNextPaymentState();
                        }
                    }
                    else {
                        showResponseErrorMessage();
                    }
                }
                else {
                    if (null != mOrder) {
                        boolean requiresFloridaBillingAddress = IceTicketUtils.isFloridaBillingAddressRequired(mOrder.getOrderItemGroups());
                        mPaymentInfoViewModel.setRequiresFloridaBillingAddress(requiresFloridaBillingAddress);
                        mPaymentInfoViewModel.setRequiresFlexPayTnc(mOrder.hasFlexPay());
                        mPaymentInfoViewModel.setTncLinkClickListener(flexPayTncLinkClickListener);
                        if (null != mOrder.getPricing()) {
                            if (null != mOrder.getPricing().getGrandTotal()) {
                                mPaymentInfoViewModel.setPricingModel(new DisplayPricingModel(mOrder.getDisplayPricing()));
                            }
                            requestShippingModes();
                        } else {
                            showResponseErrorMessage();
                        }
                    } else {
                        showResponseErrorMessage();
                    }
                }
            } else {
                showResponseErrorMessage();
            }
        }
        else if (response instanceof GetEligibleShipModesResponse) {
            if (response.isHttpStatusCodeSuccess()) {
                String shippingId = mOrder.getShipping().getShipModeId();
                List<DeliveryOption> deliveryOptions = ((GetEligibleShipModesResponse) response).getDeliveryOptions();
                boolean isShippingRequired = ShippingUtils.isShippingRequired(shippingId, deliveryOptions);
                boolean isDomesticShipping = !ShippingUtils.isShippingInternational(shippingId, deliveryOptions);
                mPaymentInfoViewModel.setShippingRequired(isShippingRequired);
                mPaymentInfoViewModel.setDomesticShipping(isDomesticShipping);
                requestProfile();
            } else {
                showResponseErrorMessage();
            }
        }
        else if (response instanceof GetGuestProfileResponse) {
            GetGuestProfileResponse guestProfileResponse = (GetGuestProfileResponse) response;
            if (guestProfileResponse.getResult() != null && guestProfileResponse.getStatusCode() == HttpResponseStatus.SUCCESS_OK) {

                AccountUtils.setUserName(guestProfileResponse, getContext());
                guestProfile = guestProfileResponse.getResult().getGuestProfile();
                if (null != guestProfile && null != guestProfile.getAddresses()) {
                    ArrayList<AddressInfo> savedAddresses = new ArrayList<>(guestProfile.getAddresses().size());
                    for (Address a : guestProfile.getAddresses()) {
                        savedAddresses.add(new AddressInfo(a, mCountryStateArrays));
                    }
                    updateSavedAddresses(savedAddresses);

                }

            } else {
                showResponseErrorMessage();
            }
        }
        else if(response instanceof FLValidateZipDao) {
            handleFlValidateZipResponse((FLValidateZipDao) response);
        } else if(response instanceof AddBillingAddressResponse) {
            handleAddBillingAddressResponse((AddBillingAddressResponse) response);
        } else if (response instanceof SubmitPurchaseResponse) {
            handlePurchaseResponse((SubmitPurchaseResponse) response);
        } else if (response instanceof GetFlexpayContractResponse) {
            handleGetFlexpayContractResponse((GetFlexpayContractResponse) response);
        }
    }

    private void updateSavedAddresses(ArrayList<AddressInfo> savedAddresses) {
        // clear first since adapters are pointing to same lists, and clearing after adding
        // will clear the data for the adapter.
        mBillingAddressSpinnerAdapter.clear();
        mShippingAddressSpinnerAdapter.clear();
        mPaymentInfoViewModel.setSavedAddresses(savedAddresses);
        mBillingAddressSpinnerAdapter.notifyDataSetChanged();
        mShippingAddressSpinnerAdapter.notifyDataSetChanged();
        mBinding.savedBillingInfoSpinner.setOnItemSelectedListener(this);
        mBinding.savedShippingInfoSpinner.setOnItemSelectedListener(this);
        if (mPaymentInfoViewModel.getSavedAddresses().size() > 0) {
            // if there is at least one saved address, set selection so it triggers
            // onItemSelectedListener for cascading effects
            mBinding.savedBillingInfoSpinner.setSelection(getSelectedSpinnerPos(R.id.saved_billing_info_spinner));
            mBinding.savedShippingInfoSpinner.setSelection(getSelectedSpinnerPos(R.id.saved_shipping_info_spinner));
            updateAddressSpinnerPositions(mPaymentInfoViewModel.billingAddressInfo);
            updateAddressSpinnerPositions(mPaymentInfoViewModel.shippingAddressInfo);
        }

    }

    private void updateAddressSpinnerPositions(AddressInfo addressInfo) {
        int countryPos = mCountryStateArrays.findCountryPosition(addressInfo.getCountryCode());
        int statePos = mCountryStateArrays.findStatePosition(addressInfo.getStateProvince());
        addressInfo.setCountryPosition(countryPos);
        addressInfo.setStatePosition(statePos);

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

    private void showResponseErrorMessage() {
        showMessage(mTridionConfig.getEr71());
    }

    private void showFloridaValidationError() {
        showMessage(mBinding.getTridion().getEr38());
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Context context = activity.getApplicationContext();
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            // FIXME REMOVING CHECK FOR TESTING
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        Intent addressInfoIntent = new Intent(getActivity().getBaseContext(), AddressInformationActivity.class);
        switch (view.getId()) {
            case R.id.button_complete_payment_information:
                handleCompletePaymentClicked();
                break;
            case R.id.saved_billing_info_primary_address_edit:
                // If selected address is passed as null, then the address will be added

                Bundle billingArgs = AddressInformationActivity
                        .newInstanceBundle(SourceId.GUEST_CHECKOUT, selectedBillingAddressId, mPaymentInfoViewModel.getRequiresFloridaBillingAddress());
                billingArgs.putString(KEY_ARG_SELECTED_ADDRESS_TYPE, Address.ADDRESS_TYPE_BILLING);
                startActivityForResult(addressInfoIntent.putExtras(billingArgs), ADDRESS_ACTIVITY_RESULT_CODE);
                break;
            case R.id.saved_shipping_info_primary_address_edit:
                // If selected address is passed as null, then the address will be added

                Bundle shippingArgs = AddressInformationActivity
                        .newInstanceBundle(SourceId.GUEST_CHECKOUT, selectedShippingAddressId, false);
                shippingArgs.putString(KEY_ARG_SELECTED_ADDRESS_TYPE, Address.ADDRESS_TYPE_SHIPPING);
                startActivityForResult(addressInfoIntent.putExtras(shippingArgs), ADDRESS_ACTIVITY_RESULT_CODE);
                break;
            case R.id.phone_number_info_popup:
                Toast.makeText(getContext(), mBinding.getTridion().getPhoneNumberToolTip(), Toast.LENGTH_LONG)
                        .show();
                break;
            case R.id.cc_security_code_info_popup:
                Toast.makeText(getContext(), mBinding.getTridion().getSecurityCodeToolTip(), Toast.LENGTH_LONG)
                        .show();
                break;
        }
    }

    /**
     * This is a callback method for the activity instigated by <code>startActivityForResult()</code>.
     *
     * @param requestCode The unique request code
     * @param resultCode The result code
     * @param intent The intent with any extras passed back
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        DialogUtils.cancelLoadingView(getActivity().getSupportFragmentManager());

        // Check which request we're responding to, make sure it was successful, and make sure the intent exists
        // If we are coming from the address information activity, then save the persisted address id for
        // updating our views.
        if (requestCode == ADDRESS_ACTIVITY_RESULT_CODE &&
                resultCode == AddressInformationActivity.RESULT_ADDED_OR_UPDATED && intent != null) {
            String addressId = intent.getStringExtra(AddressInformationActivity.KEY_ARG_SELECTED_ADDRESS_ID);
            @Address.AddressType String addressType = intent.getStringExtra(KEY_ARG_SELECTED_ADDRESS_TYPE);
            if (addressId != null && addressType != null) {
                switch (addressType) {
                    case Address.ADDRESS_TYPE_BILLING:
                        selectedBillingAddressId = addressId;
                        AddressCache.setBillingAddress(null);
                        sendBillingAddressAddedAnalytics();
                        break;
                    case Address.ADDRESS_TYPE_SHIPPING:
                        selectedShippingAddressId = addressId;
                        AddressCache.setShippingAddress(null);
                        break;
                }
            }
        }
    }

    /**
     * Returns the selected address spinner position, distinguishing between the spinners
     * via the addressType
     *
     * @param spinnerId The id of the spinner
     * @return The selected address spinner position
     */
    private int getSelectedSpinnerPos(@IdRes int spinnerId) {
        String matchAddressId = null;
        switch (spinnerId) {
            case R.id.saved_billing_info_spinner: {
                AddressInfo cachedAddress = AddressCache.getBillingAddress();
                if (null != cachedAddress && !TextUtils.isEmpty(cachedAddress.getAddressId())) {
                    matchAddressId = cachedAddress.getAddressId();
                } else {
                    matchAddressId = selectedBillingAddressId;
                }
                break;
            }
            case R.id.saved_shipping_info_spinner: {
                AddressInfo cachedAddress = AddressCache.getShippingAddress();
                if (null != cachedAddress && !TextUtils.isEmpty(cachedAddress.getAddressId())) {
                    matchAddressId = cachedAddress.getAddressId();
                } else {
                    matchAddressId = selectedShippingAddressId;
                }
                break;
            }
        }
        List<Address> guestProfileAddresses = guestProfile.getAddresses();
        if (guestProfileAddresses == null || guestProfileAddresses.isEmpty()) {
            return 0;
        }
        // If the matchAddressId was null, then there was no submitted billing/shipping
        // address ID, meaning that we have not added/edited any addresses yet (so just
        // show the primary address)
        if (null == matchAddressId) {
            for (int i = 0; i < guestProfileAddresses.size(); i++) {
                if (guestProfileAddresses.get(i) != null &&
                        guestProfileAddresses.get(i).isPrimary()) {
                    return i;
                }
            }
            // no primary found, so return 0 (first address position)
            return 0;
        }
        for (int i = 0; i < guestProfileAddresses.size(); i++) {
            if (guestProfileAddresses.get(i) != null &&
                TextUtils.equals(guestProfileAddresses.get(i).getAddressId(), matchAddressId)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Logic performed when one of the spinners (parent) has an item (address) that has been
     * selected.
     * @param parent The spinner whose item was selected
     * @param view The view
     * @param position The position of selected item
     * @param id The id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == parent.getCount() - 1) {
            mBinding.savedBillingInfoPrimaryAddressEdit.setVisibility(View.GONE);
            mBinding.savedShippingInfoPrimaryAddressEdit.setVisibility(View.GONE);
            switch (parent.getId()) {
                case R.id.saved_billing_info_spinner:
                    mBinding.savedBillingInfoSpinnerArrow.setVisibility(View.GONE);
                    break;
                case R.id.saved_shipping_info_spinner:
                    mBinding.savedShippingInfoSpinnerArrow.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            Intent addressInfoIntent = new Intent(getActivity().getBaseContext(), AddressInformationActivity.class);
            @Address.AddressType String addressType = parent.getId() ==
                    R.id.saved_billing_info_spinner ? Address.ADDRESS_TYPE_BILLING : Address.ADDRESS_TYPE_SHIPPING;
            Bundle args = AddressInformationActivity
                    .newInstanceBundle(SourceId.GUEST_CHECKOUT, null, mPaymentInfoViewModel.getRequiresFloridaBillingAddress());
            args.putString(KEY_ARG_SELECTED_ADDRESS_TYPE, addressType);
            startActivityForResult(addressInfoIntent.putExtras(args), ADDRESS_ACTIVITY_RESULT_CODE);

        } else {
            AddressInfo addressInfo;
            switch (parent.getId()) {
                case R.id.saved_billing_info_spinner:
                    addressInfo = mBillingAddressSpinnerAdapter.getItem(position);
                    if (addressInfo != null) {
                        selectedBillingAddressId = addressInfo.getAddressId();
                    }
                    mBinding.savedBillingInfoSpinnerArrow.setVisibility(View.VISIBLE);
                    mBinding.savedBillingInfoPrimaryAddressEdit.setVisibility(View.VISIBLE);
                    mBinding.savedBillingInfoPrimaryAddressText.setText(addressInfo.toFormattedString());
                    mBinding.savedBillingInfoPrimaryLabel.setVisibility(addressInfo.isPrimaryAddress()? View.VISIBLE: View.GONE);
                    mPaymentInfoViewModel.billingAddressInfo.copy(addressInfo);
                    break;
                case R.id.saved_shipping_info_spinner:
                    addressInfo = mShippingAddressSpinnerAdapter.getItem(position);
                    if (addressInfo != null) {
                        selectedShippingAddressId = addressInfo.getAddressId();
                    }
                    mBinding.savedShippingInfoSpinnerArrow.setVisibility(View.VISIBLE);
                    mBinding.savedShippingInfoPrimaryAddressEdit.setVisibility(View.VISIBLE);
                    mBinding.savedShippingInfoPrimaryAddressText.setText(addressInfo.toFormattedString());
                    mBinding.savedShippingInfoPrimaryLabel.setVisibility(addressInfo.isPrimaryAddress()? View.VISIBLE: View.GONE);
                    mPaymentInfoViewModel.shippingAddressInfo.copy(addressInfo);
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Gets the User's profile
     */
    private void requestProfile() {
        // If neither the username or password are null/empty, then we have populated login info
        // and can retrieve GuestProfile
        if (AccountStateManager.isUserLoggedIn()) {

            GetGuestProfileRequest request = new GetGuestProfileRequest.Builder(this)
                    .build();

            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();

            showLoadingView();
        } else {
            if (null != AddressCache.getBillingAddress()) {
                mPaymentInfoViewModel.billingAddressInfo.copy(AddressCache.getBillingAddress());
            }
            if (mPaymentInfoViewModel.getShippingRequired() && null != AddressCache.getShippingAddress()) {
                mPaymentInfoViewModel.shippingAddressInfo.copy(AddressCache.getShippingAddress());
            }
        }
    }

    /**
     * Gets the current cart
     */
    private void requestCart() {
        CartTicketGroupingRequest request = new CartTicketGroupingRequest.Builder(this)
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
        showLoadingView();
    }

    /**
     * Gets the current cart
     */
    private void requestShippingModes() {
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

      //  showLoadingView();
    }

    private void handleCompletePaymentClicked() {
        mCheckoutState = CHECKOUT_STATE_START;
        processNextPaymentState();
    }

    /**
     * State machine for processing a payment. Always call back to this instead of calling process
     * methods directly.
     */
    private void processNextPaymentState() {
        // Post to handler to make sure it is on the main thread since a network request can get
        // kicked off from a network response
        Handler mHandler = new Handler(getActivity().getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                switch (mCheckoutState) {
                    case CHECKOUT_STATE_START:
                        mCheckoutState = CHECKOUT_CC_EXPIRATION_VALIDATION;
                        checkCreditCardExpiration();
                        break;
                    case CHECKOUT_CC_EXPIRATION_VALIDATION:
                        mCheckoutState = CHECKOUT_STATE_FL_VALIDATION;
                        checkAddressValidations();
                        break;
                    case CHECKOUT_STATE_FL_VALIDATION:
                        mCheckoutState = CHECKOUT_STATE_CHECK_CART_ERRORS;
                        checkCartErrors();
                        break;
                    case CHECKOUT_STATE_CHECK_CART_ERRORS:
                        mCheckoutState = CHECKOUT_STATE_BILLING_ADDRESS;
                        processBillingAddress();
                        break;
                    case CHECKOUT_STATE_BILLING_ADDRESS:
                        mCheckoutState = CHECKOUT_STATE_SHIPPING_ADDRESS;
                        processShippingAddress();
                        break;
                    case CHECKOUT_STATE_SHIPPING_ADDRESS:
                        mCheckoutState = CHECKOUT_STATE_CART_SHIPPING_INFO;
                        processCartShippingInfo();
                        break;
                    case CHECKOUT_STATE_CART_SHIPPING_INFO:
                        mCheckoutState = CHECKOUT_STATE_PROCESS_PAYMENT;
                        processPayment();
                        break;
                    case CHECKOUT_STATE_PROCESS_PAYMENT:
                        mCheckoutState = CHECKOUT_STATE_START;
                        break;

                }
            }
        });
    }

    private void checkCreditCardExpiration() {
        boolean valid = false;
        if (mPaymentInfoViewModel.creditCardInfo.getExpirationMonth() >= 0
                && mPaymentInfoViewModel.creditCardInfo.getExpirationYear() >= 0) {
            Calendar cal = Calendar.getInstance();
            int currentMonth = cal.get(Calendar.MONTH);

            if (mPaymentInfoViewModel.creditCardInfo.getExpirationYear() == 0) {
                // if year index is 0, that is the current year, so check the month
                if (mPaymentInfoViewModel.creditCardInfo.getExpirationMonth() >= currentMonth) {
                    // current month/year or higher
                    valid = true;
                }
            } else {
                // later the current year
                valid = true;
            }
        }
        if (valid) {
            processNextPaymentState();
        } else {
            showMessage(mBinding.getTridion().getEr36());
        }
    }

    /**
     * Check all of the address validations needed for processing payment
     */
    private void checkAddressValidations() {
        // US11904/US15294 - Removing Domestic/International address check for 1.7
//        if ((mPaymentInfoViewModel.getDomesticShipping() &&
//                !mPaymentInfoViewModel.shippingAddressInfo.getDomesticAddress()) ||
//                (mPaymentInfoViewModel.getInternationalShipping() &&
//                        mPaymentInfoViewModel.shippingAddressInfo.getDomesticAddress())) {
//            showMessage(mBinding.getTridion().getEr56());
//        } else
        {
            if (mPaymentInfoViewModel.requiresFloridaBillingAddress) {
                // Check FL address
                AddressInfo billingAddress = mPaymentInfoViewModel.billingAddressInfo;
                if (billingAddress.getDomesticAddress() && mCountryStateArrays.getStates().size() > billingAddress.getStatePosition()
                        && billingAddress.getStatePosition() >= 0) {
                    if (!ProvState.STATE_CODE_FLORIDA.equals(mCountryStateArrays.getStates().get(billingAddress.getStatePosition()).getCode())) {
                        showFloridaValidationError();
                    } else {
                        showLoadingView();
                        ValidateZipRequest request = new ValidateZipRequest.Builder(this)
                                .setZip(billingAddress.getZip())
                                .build();
                        NetworkUtils.queueNetworkRequest(request);
                        NetworkUtils.startNetworkService();
                    }
                } else {
                    showFloridaValidationError();
                }
            } else {
                processNextPaymentState();
            }
        }
    }

    /**
     * Handle FL zip code network validation response.
     * @param response
     */
    private void handleFlValidateZipResponse(FLValidateZipDao response) {
        // Steps for checking the FL zip code validation response.
        // 1. Check if the response is not null. Show network error if null
        // 2. If the network returns that the zip code is not FL, show FL validation error
        // 3. If zip code is FL, then go to the next step in the payment process.
        if (response.isValid() != null) {
            if (!response.isValid()) {
                showFloridaValidationError();
            } else {
                // make call to commit payment
                processNextPaymentState();
            }
        } else {
            showResponseErrorMessage();
        }
    }

    private void checkCartErrors() {
        requestCart();
    }

    /**
     * Process the billing address for payments
     */
    private void processBillingAddress() {
        // Check the order. If it's null, something really bad has happened.
        if (null != mOrder && null != mOrder.getOrderId()) {
            showLoadingView();
            String email = getUsersEmail();
            Contact billingAddress = mBinding.getPaymentInfo().billingAddressInfo.toContact(mOrder.getOrderId(),
                    Address.ADDRESS_TYPE_BILLING, email);
            billingAddress.setPrimary(true);

            sendAddBillingAddressRequest(billingAddress);
        } else {
            showMessage(mTridionConfig.getEr71());
        }
    }

    private @NonNull String getUsersEmail() {
        String email = "";
        if (mPaymentInfoViewModel.isRegisteredUser) {
            if (null != guestProfile && null != guestProfile.getEmail()) {
                email = guestProfile.getEmail();
            }
        } else {
            email = AccountUtils.getUnregisteredUserEmail();
        }
        return email;
    }

    /**
     * Process the shipping address (if it is required) for payments
     */
    private void processShippingAddress() {
        if (!mPaymentInfoViewModel.getShippingRequired()) {
            // If the shipping address is not required, just go the next step
            processNextPaymentState();
        } else {
            // Check the order. If it's null, something really bad has happened.
            if (null != mOrder && null != mOrder.getOrderId()) {
                showLoadingView();
                String email = getUsersEmail();
                Contact shippingAddress = mBinding.getPaymentInfo().shippingAddressInfo.toContact(mOrder.getOrderId(),
                        Address.ADDRESS_TYPE_SHIPPING, email);
                sendAddBillingAddressRequest(shippingAddress);
            } else {
                showMessage(mTridionConfig.getEr71());
            }
        }
    }

    /**
     * Helper method for sending billing address for payment.
     * @param address
     */
    public void sendAddBillingAddressRequest(Contact address) {
        AddBillingAddressRequest request = new AddBillingAddressRequest.Builder(this)
                .addContact(address)
                .build();

        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    /**
     * Handle the billing address response
     * @param addressResponse
     */
    private void handleAddBillingAddressResponse(AddBillingAddressResponse addressResponse) {
        boolean success = false;
        if (addressResponse.isHttpStatusCodeSuccess()) {
            if (!TextUtils.isEmpty(addressResponse.getAddressId())) {
                if (mCheckoutState == CHECKOUT_STATE_BILLING_ADDRESS) {
                    mBillingAddressId = addressResponse.getAddressId();
                } else if (mCheckoutState == CHECKOUT_STATE_SHIPPING_ADDRESS) {
                    mShippingAddressId = addressResponse.getAddressId();
                }
                success = true;
            }
        }
        if (success) {
            processNextPaymentState();
        } else {
            showResponseErrorMessage();
        }
    }

    /**
     * Add the shipping address ID to the order items in the cart
     */
    private void processCartShippingInfo() {
        if (!mPaymentInfoViewModel.getShippingRequired()) {
            // If the shipping address is not required, just go the next step
            processNextPaymentState();
        } else {
            showLoadingView();
            CartShippingInfoRequest request = new CartShippingInfoRequest.Builder(this)
                    .setAddressId(mOrder, mShippingAddressId)
                    .build();

            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        }
    }

    /**
     * Handle the {@link CartShippingInfoResponse}
     * @param response
     */
    private void handleCartShippingInfoResponse(@NonNull CartShippingInfoResponse response) {
        if (response.isHttpStatusCodeSuccess()) {
            processNextPaymentState();
        } else {
            showResponseErrorMessage();
        }
    }

    /**
     * Time to actually process the payment
     */
    private void processPayment() {

        if (mOrder != null && mOrder.getOrderId() != null) {
            showLoadingView();

            SubmitPurchaseRequest request = new SubmitPurchaseRequest.Builder(this)
                    .setOrderId(mOrder.getOrderId())
                    .setAmount(mOrder.getPurchaseAmount())
                    .setBillingAddressId(mBillingAddressId)
                    .setAccountNumber(mPaymentInfoViewModel.creditCardInfo.getCreditCardNumber())
                    .setExpireMonth(mPaymentInfoViewModel.creditCardInfo.getExpirationMonth())
                    .setExpireYear(mPaymentInfoViewModel.creditCardInfo.getExpirationYear())
                    .setCardSecurityCode(mPaymentInfoViewModel.creditCardInfo.getSecurityCode())
                    .build();
            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        } else {
            showMessage(mTridionConfig.getEr71());
        }
    }

    /**
     * Handle the process payment response
     * @param response
     */
    private void handlePurchaseResponse(SubmitPurchaseResponse response) {
        mCheckoutState = CHECKOUT_STATE_START; // set just in case
        // response is not null by the time it gets here
        if (response.getResult() != null) {
            purchaseSuccessful(response.getResult());
        } else if (response.getHttpStatusCode() != null) {
            int statusCode = response.getHttpStatusCode();
            if (statusCode == SubmitPurchaseResponse.HTTP_STATUS_CC_PROCESSING_ERROR) {
                // Purchase failed due to a service issue, show error message
                showMessage(mBinding.getTridion().getEr42());
            } else if (statusCode >= HttpResponseStatus.ERROR_BAD_REQUEST && statusCode < HttpResponseStatus.ERROR_INTERNAL_SERVER_ERROR){
                // Purchase failed due to incorrect CC information
                showMessage(mBinding.getTridion().getEr35());
            } else {
                // Unknown error
                showMessage(mBinding.getTridion().getEr42());
            }
        } else {
            showMessage(mBinding.getTridion().getEr42());
        }
    }

    /**
     * The purchase was successful, so show the confirmation screen.
     * @param result the order result
     */
    private void purchaseSuccessful(@NonNull CardResult result) {
        getActivity().finish();
        Intent intent = new Intent(getActivity().getBaseContext(), OrderConfirmationActivity.class);
        PaymentSummaryInfo paymentSummaryInfo = new PaymentSummaryInfo(
                result.getAccountLastFour(),
                result.getPaymentType(),
                mTridionConfig.getFormattedPrice(result.getAmount()),
                mPaymentInfoViewModel.getPricingModel());
        OrderConfirmationInfo orderConfirmationInfo = new OrderConfirmationInfo();
        orderConfirmationInfo.confirmationNumber = result.getMasterOrderNumber();
        orderConfirmationInfo.firstName = mPaymentInfoViewModel.billingAddressInfo.getFirstName();
        orderConfirmationInfo.lastName = mPaymentInfoViewModel.billingAddressInfo.getLastName();
        orderConfirmationInfo.emailAddress = getUsersEmail();
        orderConfirmationInfo.paymentSummaryInfo = paymentSummaryInfo;
        if (mPaymentInfoViewModel.billingAddressInfo.getDomesticAddress()) {
            if (mPaymentInfoViewModel.billingAddressInfo.getStatePosition() ==
                    mPaymentInfoViewModel.billingAddressInfo.getCountryStateArrays().findStatePosition(ProvState.STATE_CODE_FLORIDA)) {
                orderConfirmationInfo.geoLocation = GetPersonalizationOffersRequest.GEOLOCATION_TYPE_FLORIDA;
            } else {
                orderConfirmationInfo.geoLocation = GetPersonalizationOffersRequest.GEOLOCATION_TYPE_OUTER_US;
            }
        } else {
            orderConfirmationInfo.geoLocation = GetPersonalizationOffersRequest.GEOLOCATION_TYPE_INTERNATIONAL;
        }
        sendConfirmedOrderInfoAnalytics(orderConfirmationInfo);
        Bundle billingArgs = OrderConfirmationActivity
                .newInstanceBundle(orderConfirmationInfo);
        startActivity(intent.putExtras(billingArgs));
    }

    private void requestFlexContract() {
        showLoadingView();
        GetFlexpayContractRequest request = new GetFlexpayContractRequest.Builder(this)
                .setOrderId(mOrder.getOrderId())
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();

    }

    private void handleGetFlexpayContractResponse(@NonNull GetFlexpayContractResponse response) {
        if (response.isHttpStatusCodeSuccess()) {
            if (!TextUtils.isEmpty(response.getResult())) {
                Bundle bundle = WebViewActivity.newInstanceBundleWithRawHtmlContent(
                        "", response.getResult(), false);
                startActivity(new Intent(getContext(), WebViewActivity.class).putExtras(bundle));
            } else {
                showResponseErrorMessage();
            }
        } else {
            showResponseErrorMessage();
        }
    }

    /**
     * Sets the action bar's title
     */
    private void setToolbarTitle() {
        // If either the activity or action bar are null, then we have a problem
        if (getActivity() == null || getActivity().getActionBar() == null) {
            return;
        }
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle(mTridionConfig.getPageHeaderPaymentTitle());
    }

    //TODO Analytics methods will be refactored
    private void sendConfirmedOrderInfoAnalytics(OrderConfirmationInfo orderConfirmationInfo){
        // constants
        String contentGroup = AnalyticsUtils.CONTENT_GROUP_SALES;
        String contentFocus = AnalyticsUtils.CONTENT_FOCUS_CHECKOUT;
        String contentSub2 = AnalyticsUtils.CONTENT_SUB_2_TICKET_CONFORMATION;
        String prop3 = AnalyticsUtils.PROPERTY_NAME_TICKETS;
        String prop5 = AnalyticsUtils.PROPERTY_NAME_TICKET_CONFORMATION;
        String prop8 = AnalyticsUtils.PROPERTY_NAME_PARKS_RESORT;
        String prop14 = AnalyticsUtils.PROPERTY_NAME_PRODUCT_SELECTION;
        String prop36 = AnalyticsUtils.PROPERTY_NAME_TICKET_CONFORMATION;
        String prop62 = orderConfirmationInfo.confirmationNumber;
        String eventDataMapValue = "purchase, event2, scCheckout, event41, event42=1, event43";
        boolean isPurchaseMade = true;
        String purchaseId = orderConfirmationInfo.confirmationNumber;
        TicketGroupOrder products = mOrder;

                AnalyticsUtils.trackTicketsPageView
                        (contentSub2,
                                prop3,
                                prop5,
                                prop8,
                                prop14,
                                prop36,
                                null,
                                prop62,
                                eventDataMapValue,
                                isPurchaseMade,
                                purchaseId,
                                null,
                                products,
                                contentGroup,
                                contentFocus,
                                null);
    }

    //TODO Analytics methods will be refactored
    private void sendBillingAddressAddedAnalytics(){
        // constants
        String contentGroup = AnalyticsUtils.CONTENT_GROUP_SALES;
        String contentFocus = AnalyticsUtils.CONTENT_FOCUS_CHECKOUT;
        String contentSub2 = AnalyticsUtils.CONTENT_SUB_2_BILLING_INFO;
        String prop3 = AnalyticsUtils.PROPERTY_NAME_TICKETS;
        String prop5 = AnalyticsUtils.PROPERTY_NAME_BILLING_INFO;
        String prop8 = AnalyticsUtils.PROPERTY_NAME_PARKS_RESORT;
        String prop14 = AnalyticsUtils.PROPERTY_NAME_PRODUCT_SELECTION;
        String prop36 = AnalyticsUtils.PROPERTY_NAME_BILLING_INFO;
        String eventDataMapValue = "event2, event3";
        boolean isPurchaseMade = false;

                AnalyticsUtils.trackTicketsPageView
                        (contentSub2,
                                prop3,
                                prop5,
                                prop8,
                                prop14,
                                prop36,
                                null,
                                null,
                                eventDataMapValue,
                                isPurchaseMade,
                                null,
                                null,
                                null,
                                contentGroup,
                                contentFocus,
                                null);
    }

    //TODO Analytics methods will be refactored
    private void sendPaymentPageViewAnalytics(){
        AnalyticsUtils.trackTicketsPageView(
                AnalyticsUtils.CONTENT_SUB_2_VIEW_ASSIGN_TICKETS,
                AnalyticsUtils.PROPERTY_NAME_TICKETS,
                AnalyticsUtils.PROPERTY_NAME_PAYMENT_INFO,
                AnalyticsUtils.PROPERTY_NAME_PARKS_RESORT,
                AnalyticsUtils.PROPERTY_NAME_PRODUCT_SELECTION,
                AnalyticsUtils.PROPERTY_NAME_PAYMENT_INFO,
                null,
                null,
                "event2, event3, scCheckout",
                false,
                null,
                null,
                null,
                AnalyticsUtils.CONTENT_GROUP_SALES,
                AnalyticsUtils.CONTENT_FOCUS_CHECKOUT,
                AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE);
    }

    private void sendUpdateShippingGroup() {
        AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_SALES,
                AnalyticsUtils.CONTENT_FOCUS_CHECKOUT,
                null,
                AnalyticsUtils.CONTENT_SUB_2_BILLING_INFO,
                null,
                null,
                null);
    }

    private void showLoadingView() {
        // Post to handler to make sure it is on the main thread
        Handler mHandler = new Handler(getActivity().getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DialogUtils.showLoadingView(getActivity().getSupportFragmentManager());
            }
        });
    }
}
