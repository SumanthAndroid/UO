package com.universalstudios.orlandoresort.controller.userinterface.account.address;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.checkout.CountryStateArrays;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.ListDialogFragment;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.FragmentAddressInformationBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.ErrorHandling.FLValidateZipDao;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.ErrorHandling.GAValidateZipDao;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Address;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.AddUpdateGuestAddressesRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.DeleteGuestAddressRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.AddUpdateGuestAddressesResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.DeleteGuestAddressResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.State.ProvState;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ValidateZip.ValidateZipRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.country_state.CountryStateProvinceStateManager;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.response.HttpResponseStatus;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;
import com.universalstudios.orlandoresort.view.textview.ZipCodeEditText;

import org.parceler.Parcels;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AddressInformationFragment provides the interface for logged-in users to add/edit their
 * address information.
 * Activities that contain this fragment must implement the
 * {@link AddressInformationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddressInformationFragment#newInstance(GuestProfile guestProfile, String sourceId, Bundle args)} factory method to
 * create an instance of this fragment.
 */
public class AddressInformationFragment extends NetworkFragment implements View.OnClickListener {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ADDRESS_STATE_START,
            ADDRESS_STATE_FL_VALIDATION,
            ADDRESS_STATE_PROCESS_SAVE,
            ADDRESS_STATE_GA_VALIDATION})
    private @interface SaveState {}

    // Declare the constants
    private static final int ADDRESS_STATE_START = 0;
    private static final int ADDRESS_STATE_FL_VALIDATION = 1;
    private static final int ADDRESS_STATE_PROCESS_SAVE = 2;
    private static final int ADDRESS_STATE_GA_VALIDATION = 3;

    // The fragment initialization parameters, e.g. KEY_ARG_SELECTED_ADDRESS_ID
    static final String KEY_ARG_GUEST_PROFILE = "KEY_ARG_GUEST_PROFILE";
    static final String KEY_ARG_SOURCE_ID = "KEY_ARG_SOURCE_ID";
    public static final String ADDRESS_LIST_DIALOG_FRAGMENT = "ADDRESS_LIST_DIALOG_FRAGMENT";

    private static final String STATE_VAR_IS_CALL_IN_PROGRESS = "STATE_VAR_IS_CALL_IN_PROGRESS";


    private AddressInfoViewModel.OnAddressDeletedListener addressDeletedListener = new AddressInfoViewModel.OnAddressDeletedListener() {
        @Override
        public void onAddressDeleted(AddressInfo addressInfo) {
            handleAddressDeletedClicked(addressInfo);
        }
    };

    private String mSourceId;
    private AddressInfoViewModel mAddressInfoViewModel;
    private String mReceivedAddressId;
    private GuestProfile mGuestProfile;
    private TridionConfig mTridionConfig;
    private CountryStateArrays mCountryStateArrays;

    private boolean mIsCallInProgress;

    private OnFragmentInteractionListener mListener;
    private boolean mIsDeletedPrimary;
    private Address mNewPrimaryAddress;

    @SaveState
    private int mAddressState;

    public AddressInformationFragment() {
        // Required empty public constructor
    }

    public static AddressInformationFragment newInstance(@SourceId.SourceIdType String sourceId, GuestProfile guestProfile, Bundle args) {
        AddressInformationFragment fragment = new AddressInformationFragment();
        // Extras are passed through from AddressInformationActivity. If none are provided,
        // then it is treated like Add address.
        Bundle newArgs = args;
        if (null == newArgs) {
            newArgs = new Bundle();
        }
        newArgs.putParcelable(KEY_ARG_GUEST_PROFILE, Parcels.wrap(guestProfile));
        newArgs.putString(KEY_ARG_SOURCE_ID, sourceId);
        fragment.setArguments(newArgs);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            mReceivedAddressId = arguments.getString(AddressInformationActivity.KEY_ARG_SELECTED_ADDRESS_ID, null);
            mGuestProfile = Parcels.unwrap(arguments.getParcelable(KEY_ARG_GUEST_PROFILE));
            mSourceId = arguments.getString(KEY_ARG_SOURCE_ID);
            boolean requiresFloridaBillingAddress = arguments.getBoolean(AddressInformationActivity.KEY_ARG_REQUIRES_FLORIDA_BILLING_ADDRESS, false);
            boolean requiresGeorgiaBillingAddress = arguments.getBoolean(AddressInformationActivity.KEY_ARG_REQUIRES_FLORIDA_BILLING_ADDRESS, false);

            boolean allowDelete = arguments.getBoolean(AddressInformationActivity.KEY_ARG_ALLOW_DELETE, false);
            boolean newAddress = null == mReceivedAddressId;

            mCountryStateArrays = new CountryStateArrays(
                    CountryStateProvinceStateManager.getCountriesInstance().getCountries(),
                    CountryStateProvinceStateManager.getStateProvincesInstance().getStateProvinces());
            mAddressInfoViewModel = new AddressInfoViewModel(mCountryStateArrays, allowDelete, newAddress);

            // If the address ID is not null, then this is an edit
            if (mReceivedAddressId != null) {
                Address guestEditAddress = null;
                for (Address editAddress : mGuestProfile.getAddresses()) {
                    if (TextUtils.equals(mReceivedAddressId, editAddress.getAddressId())) {
                        guestEditAddress = editAddress;
                    }
                }
                if (guestEditAddress != null) {
                    AddressInfo newConvertedAddrInfo = new AddressInfo(guestEditAddress, mCountryStateArrays);
                    mAddressInfoViewModel.addressInfo.copy(newConvertedAddrInfo);
                }
                // If this is an edit, then the Florida zip code must have been valid when the address was added
                mAddressInfoViewModel.setValidFloridaZip(true);
            }

            // Set that florida zip is valid
            mAddressInfoViewModel.setFloridaZipRequired(requiresFloridaBillingAddress);
                mAddressInfoViewModel.setOnAddressDeletedListener(addressDeletedListener);
        }
        if (savedInstanceState != null) {
            mIsCallInProgress = savedInstanceState.getBoolean(STATE_VAR_IS_CALL_IN_PROGRESS, false);
        } else {
            mIsCallInProgress = false;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentAddressInformationBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_address_information, container, false);
        View view = binding.getRoot();

        mTridionConfig = IceTicketUtils.getTridionConfig();
        binding.setTridion(mTridionConfig);
        binding.setCountryStateArrays(mCountryStateArrays);
        mAddressInfoViewModel.setPrimaryAddressChecked(mAddressInfoViewModel.addressInfo.isPrimaryAddress());

        // When the zip code field loses focus, perform the Florida zipcode check if the user has florida-ticket based tickets
        mAddressInfoViewModel.addressInfo.setOnZipFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                // We are only concerned in the case where the focus is lost
                // If we are leaving the activity, we do not care to perform this check
                if (!focus && getActivity() != null && !getActivity().isFinishing()) {
                    ZipCodeEditText zipCodeEditText = (ZipCodeEditText) view;

                    // Validate the zip code locally
                    boolean valid = checkZipFormat(zipCodeEditText);
                    if (!valid) {
                        return;
                    }

                    //todo check the spinner for validating the zip code.
                    // Check florida validation, if the zip code has been valid up to this point
//                    checkFloridaZipValidations(false);


                    AddressInfo billingAddress = mAddressInfoViewModel.addressInfo;
                    if (billingAddress.getStatePosition() != -1) {


                    if(ProvState.STATE_CODE_FLORIDA.equals(mCountryStateArrays.getStates().get(billingAddress.getStatePosition()).getCode())){
                        checkFloridaZipValidations(false);
                    } else if (ProvState.STATE_CODE_GEORGIA.equals(mCountryStateArrays.getStates().get(billingAddress.getStatePosition()).getCode())){
                        checkgeorgiaZipValidations(false);
                    } else {
                        showGeorgiaValidationError();
                    }
                }}
            }
        });
        binding.setAddressInfo(mAddressInfoViewModel);

        binding.layoutAddressInfoInclude.phoneNumberInfoPopup.setOnClickListener(this);
        binding.buttonCompleteAddressInformation.setOnClickListener(this);

        int countryPos = mCountryStateArrays.findCountryPosition(mAddressInfoViewModel.getAddressInfo().getCountryCode());
        int statePos = mCountryStateArrays.findStatePosition(mAddressInfoViewModel.getAddressInfo().getStateProvince());
        mAddressInfoViewModel.getAddressInfo().setCountryPosition(countryPos);
        mAddressInfoViewModel.getAddressInfo().setStatePosition(statePos);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_VAR_IS_CALL_IN_PROGRESS, mIsCallInProgress);
    }

    /**
     * Checks the character length of the zip code field.  Returns true if valid, shows an error
     * and returns false if invalid.
     *
     * @param editText The {@link ZipCodeEditText} to check
     * @return True if valid, false if invalid
     */
    private boolean checkZipFormat(ZipCodeEditText editText) {
        String text = editText.getText().toString();
        if (text.length() > 0 && text.length() < ZipCodeEditText.MIN_CHARS_DOMESTIC) {
            editText.setError(mTridionConfig.getEr32());
            return false;
        }
        editText.setError(null);
        return true;
    }

    private void checkFloridaZipValidations(boolean submittingForm) {
        if (mAddressInfoViewModel.isFloridaZipRequired()) {

            // Check FL address
            AddressInfo billingAddress = mAddressInfoViewModel.addressInfo;

            // If we aren't submitting the form, just check the zip code
            if (!submittingForm) {
                fireZipValidationRequest(billingAddress);
            } else {
                // Check the state to ensure it is Florida
                if (billingAddress.getDomesticAddress() && mCountryStateArrays.getStates().size() > billingAddress.getStatePosition()
                    && billingAddress.getStatePosition() >= 0) {
                    if (!ProvState.STATE_CODE_FLORIDA.equals(mCountryStateArrays.getStates().get(billingAddress.getStatePosition()).getCode())) {
                        showFloridaValidationError();
                    }
                    else {
                        // State is fine, but still check zip code
                        fireZipValidationRequest(billingAddress);
                    }
                }
                else {
                    showFloridaValidationError();
                }
            }
        } else if (submittingForm) {
            processNextFieldState();
        }
    }

    private void checkgeorgiaZipValidations(boolean submittingForm){
        if(mAddressInfoViewModel.isGeorgiaZipRequired()){
            AddressInfo billingAddress = mAddressInfoViewModel.addressInfo;
            if(!submittingForm){
                fireZipValidationRequest(billingAddress);
            } else{
                // check to ensure it is Georgia
                if(billingAddress.getDomesticAddress() && mCountryStateArrays.getStates().size() > billingAddress.getStatePosition()
                        && billingAddress.getStatePosition() >= 0){
                    if(!ProvState.STATE_CODE_GEORGIA.equals(mCountryStateArrays.getStates().get(billingAddress.getStatePosition()).getCode())){
                        showGeorgiaValidationError();
                    } else {
                        //state is fine, but still check Zip code
                        fireZipValidationRequest(billingAddress);
                    }
                }else {
                    showGeorgiaValidationError();
                }
            }
        } else if (submittingForm){
            processNextFieldState();
        }
    }

    private void fireZipValidationRequest(AddressInfo billingAddress) {
        mIsCallInProgress = true;
        showViewBasedOnState();
        ValidateZipRequest request = new ValidateZipRequest.Builder(this)
                .setZip(billingAddress.getZip())
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
        sendValidationForZipAnalytics();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Context context = activity.getApplicationContext();
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void showViewBasedOnState() {
        if (mIsCallInProgress || mAddressState != ADDRESS_STATE_START) {
            mAddressInfoViewModel.setShowLoading(true);
        } else {
            mAddressInfoViewModel.setShowLoading(false);
        }
    }

    private void handleCompleteAddressClicked() {
        if (mAddressState == ADDRESS_STATE_START ) {
            processNextFieldState();
        }
    }

    /**
     * State machine for processing a payment. Always call back to this instead of calling process
     * methods directly.
     */
    private void processNextFieldState() {
        switch (mAddressState) {
            case ADDRESS_STATE_START:
                AddressInfo billingAddress = mAddressInfoViewModel.addressInfo;
                if(ProvState.STATE_CODE_FLORIDA.equals(mCountryStateArrays.getStates().get(billingAddress.getStatePosition()).getCode())){
                    mAddressState = ADDRESS_STATE_FL_VALIDATION;
                    showViewBasedOnState();
                    checkFloridaZipValidations(true);
                } else if (ProvState.STATE_CODE_GEORGIA.equals(mCountryStateArrays.getStates().get(billingAddress.getStatePosition()).getCode())){
                    mAddressState = ADDRESS_STATE_GA_VALIDATION;
                    showViewBasedOnState();
                    checkgeorgiaZipValidations(true);
                } else {
                    showGeorgiaValidationError();
                }
                break;
            case ADDRESS_STATE_FL_VALIDATION:
                mAddressState = ADDRESS_STATE_PROCESS_SAVE;
                showViewBasedOnState();
                processSubmission();
                break;
            case ADDRESS_STATE_GA_VALIDATION:
                mAddressState = ADDRESS_STATE_PROCESS_SAVE;
                showViewBasedOnState();
                processSubmission();
                break;
            case ADDRESS_STATE_PROCESS_SAVE:
                mAddressState = ADDRESS_STATE_START;
                showViewBasedOnState();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_complete_address_information:
                handleCompleteAddressClicked();
                break;

            case R.id.phone_number_info_popup:
                showMessage(mTridionConfig.getPhoneNumberToolTip(), -1);
                break;
        }
    }

    private void processSubmission() {
        boolean promptForNewPrimary = false;
        AddressInfo addressInfo = mAddressInfoViewModel.addressInfo;
        Address addressToAddEdit = addressInfo.toAddress(mSourceId);
        mReceivedAddressId = addressToAddEdit.getAddressId();

        final ArrayList<Address> addressList = new ArrayList<>();
        addressList.add(addressToAddEdit);

        if (null == mReceivedAddressId &&
                (null == mGuestProfile.getAddresses() || mGuestProfile.getAddresses().size() == 0)) {
            // If there is only one address, then it needs to be primary
            addressList.get(0).setPrimary(true);
        }
        if (addressToAddEdit.isPrimary()) {
            // If the user set this as a primary address, we must update any other
            // address that is primary to not be primary
            ArrayList<Address> updatePrimAddresses = new ArrayList<>();
            for (Address address : mGuestProfile.getAddresses()) {
                // Look at all addresses other than the one we are adding/editing
                // and set their primary flag to false if it's true
                if (!TextUtils.equals(address.getAddressId(), addressToAddEdit.getAddressId()) &&
                    address.isPrimary()) {
                    address.setPrimary(false);
                    updatePrimAddresses.add(address);
                }
            }
            addressList.addAll(updatePrimAddresses);
        } else {
            // If there is no other primary set, ask the user for the new primary address
            // If the profile happened to have no primary address to being with, this will also
            // prompt the user for a new primary address. This should never happen in prod, but
            // will help rectify any profiles with a missing primary address.
            boolean hasPrimaryAddress = false;
            final ArrayList<Address> updatePrimAddresses = new ArrayList<>(mGuestProfile.getAddresses().size());
            List<String> addressStrings = new ArrayList<>(mGuestProfile.getAddresses().size());
            for (Address address : mGuestProfile.getAddresses()) {
                if (!TextUtils.equals(address.getAddressId(), addressToAddEdit.getAddressId())) {
                    if (address.isPrimary()) {
                        hasPrimaryAddress = true;
                        break;
                    }
                    updatePrimAddresses.add(address);
                    addressStrings.add(address.getAddressLine1());
                }
            }
            if (!hasPrimaryAddress) {
                if (updatePrimAddresses.size() == 1) {
                    // If there is only one other address, make it primary
                    updatePrimAddresses.get(0).setPrimary(true);
                    addressList.addAll(updatePrimAddresses);
                } else if (updatePrimAddresses.size() > 1) {
                    // If there is more than one other address, prompt the user to select
                    // a new primary address from the remaining addresses.
                    promptForNewPrimary = true;
                    ListDialogFragment listDialogFragment = ListDialogFragment.getInstance(addressStrings,
                            mTridionConfig.getAddressDeletingMessage());
                    listDialogFragment.setOnClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Address newPrimaryAddress = updatePrimAddresses.get(which);
                            newPrimaryAddress.setPrimary(true);
                            addressList.add(newPrimaryAddress);
                            requestAddUpdateAddresses(addressList);
                        }
                    });
                    listDialogFragment.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    });

                    FragmentManager fm = getChildFragmentManager();
                    listDialogFragment.show(fm, ADDRESS_LIST_DIALOG_FRAGMENT);
                }
            }

        }

        if (!promptForNewPrimary) {
            // Create the request with the addresses (the one added/edited,
            // and any that had their primary flags flipped to false)
            requestAddUpdateAddresses(addressList);
        } else {
            showViewBasedOnState();
        }
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        mIsCallInProgress = false;

        if (networkResponse == null) {
            showViewBasedOnState();
            return;
        }
        if(networkResponse instanceof FLValidateZipDao) {
            handleFlValidateZipResponse((FLValidateZipDao) networkResponse);
        }
        if (networkResponse instanceof GAValidateZipDao){
            handleGAValidateZipResponse((GAValidateZipDao) networkResponse);
        } else if (networkResponse instanceof AddUpdateGuestAddressesResponse) {
            mAddressState = ADDRESS_STATE_START;

            AddUpdateGuestAddressesResponse addressResponse = (AddUpdateGuestAddressesResponse) networkResponse;
            if (addressResponse.getStatusCode() == 200 && addressResponse.getResponse() != null) {
                if (mIsDeletedPrimary) {
                    // if this is true, this response is the result of updating the primary address,
                    // not the user updating an address, so finish and return
                    showViewBasedOnState();
                    finishAndReturnResult(AddressInformationActivity.RESULT_DELETED, null);
                } else {
                    Address persistedAddress = null;
                    // If the address was added, the persisted address was the only item in the response list
                    // or a primary was added
                    if (mReceivedAddressId == null && addressResponse.getResponse().size() >= 1) {
                        // Only one address should have been added/edited, so the list should be of size 1.
                        persistedAddress = addressResponse.getResponse().get(0);
                    }
                    // If the address ID is not null, then we performed an edit (of possibly multiple addresses if we removed
                    // their primary flags).  Search the response addresses for the matching address ID
                    else if (mReceivedAddressId != null) {
                        for (Address address : addressResponse.getResponse()) {
                            if (TextUtils.equals(mReceivedAddressId, address.getAddressId())) {
                                persistedAddress = address;
                            }
                        }
                    }
                    final String updatedAddressId;
                    if (persistedAddress == null) {
                        updatedAddressId = "";
                    } else {
                        updatedAddressId = persistedAddress.getAddressId();
                    }

                    showMessage(mTridionConfig.getSu9(), -1);
                    finishAndReturnResult(AddressInformationActivity.RESULT_ADDED_OR_UPDATED, updatedAddressId);
                }

            } else {
                showMessage(mTridionConfig.getEr71(), Color.RED);
            }
        } else if (networkResponse instanceof DeleteGuestAddressResponse) {
            if (HttpResponseStatus.isHttpStatusCodeSuccess(networkResponse.getHttpStatusCode())) {
                onAddressDeleted();
            }
        }

    }

    private void handleAddressDeletedClicked(final AddressInfo addressInfo) {
        mIsDeletedPrimary = addressInfo.isPrimaryAddress();
        final List<Address> addresses = new ArrayList<>(mGuestProfile.getAddresses().size());
        addresses.addAll(mGuestProfile.getAddresses());
        if (mIsDeletedPrimary && addresses.size() > 1) {
            // Find and remove the address to be deleted
            for (int i = 0; i < addresses.size(); i++) {
                if (addresses.get(i).getAddressId().equals(addressInfo.getAddressId())) {
                    addresses.remove(i);
                    break;
                }
            }
            // If there is only one left, make it the primary
            if (addresses.size() == 1) {
                mNewPrimaryAddress = addresses.get(0);
                requestDeleteAddress(addressInfo.getAddressId());
            } else {
                // If there is more than one left, let the user choose
                List<String> addressStrings = new ArrayList<>(addresses.size());
                for (Address address : addresses) {
                    addressStrings.add(address.getAddressLine1());
                }
                ListDialogFragment listDialogFragment = ListDialogFragment.getInstance(addressStrings,
                        mTridionConfig.getAddressDeletingMessage());
                listDialogFragment.setOnClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mNewPrimaryAddress = addresses.get(which);
                        requestDeleteAddress(addressInfo.getAddressId());
                    }
                });
                listDialogFragment.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                        showViewBasedOnState();
                    }
                });

                FragmentManager fm = getChildFragmentManager();
                listDialogFragment.show(fm, ADDRESS_LIST_DIALOG_FRAGMENT);

            }

        } else {
            String deletedAddressId = addressInfo.getAddressId();
            requestDeleteAddress(deletedAddressId);
        }
    }

    private void requestDeleteAddress(String deletedAddressId) {
        String username = AccountStateManager.getUsername();
        String password = AccountStateManager.getPassword();
        String guestId = AccountStateManager.getGuestId();
        if (null == username || null == password || null == guestId) {
            finishAndReturnResult(Activity.RESULT_CANCELED, null);
        } else {
            mIsCallInProgress = true;
            showViewBasedOnState();
            DeleteGuestAddressRequest request = new DeleteGuestAddressRequest.Builder(this)
                    .setAddressId(deletedAddressId)
                    .setConcurrencyType(NetworkRequest.ConcurrencyType.ASYNCHRONOUS)
                    .build();
            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        }
    }

    private void onAddressDeleted() {
        final List<Address> addresses = mGuestProfile.getAddresses();
        if (!mIsDeletedPrimary || null == mNewPrimaryAddress) {
            // The deleted address was not the primary or it was the only address in the profile
            // so just return
            showMessage(mTridionConfig.getSu10(), -1);
            finishAndReturnResult(AddressInformationActivity.RESULT_DELETED, null);
        } else {
            // The deleted address was the primary, so set a new primary
            for (int i = 0; i < addresses.size(); i++) {
                // First, delete the deleted address from the list
                if (mReceivedAddressId.equals(addresses.get(i).getAddressId())) {
                    addresses.remove(i);
                    break;
                }
            }
            // Set the new primary
            for (Address address : addresses) {
                if (address.getAddressId().equals(mNewPrimaryAddress.getAddressId())) {
                    address.setPrimary(true);
                }
            }
            Toast.makeText(getContext(), mTridionConfig.getSu10(), Toast.LENGTH_SHORT)
                    .show();
            requestAddUpdateAddresses(addresses);
        }
    }

    private void requestAddUpdateAddresses(final List<Address> addresses) {
        // Only make the call if a call is not already in progress
        if (!mIsCallInProgress) {
            String username = AccountStateManager.getUsername();
            String password = AccountStateManager.getPassword();
            String guestId = AccountStateManager.getGuestId();

            if (null == username || null == password || null == guestId) {
                finishAndReturnResult(Activity.RESULT_CANCELED, null);
            }
            else {
                mIsCallInProgress = true;
                showViewBasedOnState();
                AddUpdateGuestAddressesRequest request = new AddUpdateGuestAddressesRequest.Builder(this)
                        .setAddresses(addresses)
                        .setConcurrencyType(NetworkRequest.ConcurrencyType.ASYNCHRONOUS)
                        .build();
                NetworkUtils.queueNetworkRequest(request);
                NetworkUtils.startNetworkService();
                sendUpdateShipAddressAnalytics();

            }
        }

    }

    private void finishAndReturnResult(int resultCode, String updatedAddressId) {
        // Return to the payment fragment passing the address type and the persisted
        // address ID
        Bundle args = getArguments(); // Return the arguments that were provided
        if (resultCode == AddressInformationActivity.RESULT_ADDED_OR_UPDATED) {
            // with the new Address ID
            args.putString(AddressInformationActivity.KEY_ARG_SELECTED_ADDRESS_ID, updatedAddressId);
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtras(args);
        getActivity().setResult(resultCode, returnIntent);
        getActivity().finish();

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

    private void showMessage(final String message, int iconColor) {
        showViewBasedOnState();
        // Post to handler to make sure it is on the main thread
        Handler mHandler = new Handler(getActivity().getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null && !getActivity().isDestroyed()) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG)
                            .setIconColor(Color.RED)
                            .show();
                }
            }
        });
    }

    private void showFloridaValidationError() {
        mAddressState = ADDRESS_STATE_START;
        mAddressInfoViewModel.setValidFloridaZip(false);
        showMessage(mTridionConfig.getEr38(), Color.RED);
    }

    private void showGeorgiaValidationError(){
        mAddressState = ADDRESS_STATE_START;
        mAddressInfoViewModel.setValidGeorgiaZip(false);
        showMessage(mTridionConfig.getEr58(), Color.RED);
    }

    /**
     * Handle FL zip code network validation response.
     * @param response
     */
    private void handleFlValidateZipResponse(FLValidateZipDao response) {
        if (response == null) {
            showViewBasedOnState();
            return;
        }
        // Steps for checking the FL zip code validation response.
        // 1. Check if the response is not null. Show network error if null
        // 2. If the network returns that the zip code is not FL, show FL validation error
        // 3. If zip code is FL, then go to the next step in the payment process.
        if (response.isValid() != null) {
            if (!response.isValid()) {
                showFloridaValidationError();
            } else {
                // Set that florida zip is valid
                mAddressInfoViewModel.setValidFloridaZip(true);
                if (mAddressState == ADDRESS_STATE_FL_VALIDATION) {
                    processNextFieldState();
                } else {
                    showViewBasedOnState();
                }
            }
        } else {
            showMessage(mTridionConfig.getEr71(), Color.RED);
        }
    }

    /**
     * Handle GA zip code network validation response.
     * @param response
     */
    private void handleGAValidateZipResponse(GAValidateZipDao response) {
        if (response == null) {
            showViewBasedOnState();
            return;
        }
        // Steps for checking the GA zip code validation response.
        // 1. Check if the response is not null. Show network error if null
        // 2. If the network returns that the zip code is not GA, show GA validation error
        // 3. If zip code is GA, then go to the next step in the payment process.
        if (response.isValid() != null) {
            if (!response.isValid()) {
                showGeorgiaValidationError();
            } else {
                // Set that Georgia zip is valid
                mAddressInfoViewModel.setValidGeorgiaZip(true);
                if (mAddressState == ADDRESS_STATE_GA_VALIDATION) {
                    processNextFieldState();
                } else {
                    showViewBasedOnState();
                }
            }
        } else {
            showMessage(mTridionConfig.getEr71(), Color.RED);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Address address);
    }

    //TODO Analytics will be refactored for the february release
    private void sendValidationForZipAnalytics(){
        AnalyticsUtils.trackTicketsPageView(
                AnalyticsUtils.CONTENT_SUB_2_PRIVACY_AND_LEGAL,
                AnalyticsUtils.PROPERTY_NAME_TICKETS,
                AnalyticsUtils.PROPERTY_NAME_TC,
                AnalyticsUtils.PROPERTY_NAME_PARKS_RESORT,
                AnalyticsUtils.PROPERTY_NAME_PRODUCT_SELECTION,
                AnalyticsUtils.PROPERTY_NAME_TC,
                AnalyticsUtils.PROPERTY_NAME_BILLING_ZIP,
                null,
                "event2, event13, event88",
                false,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    private void sendUpdateShipAddressAnalytics() {
            Map<String, Object> extraData = new HashMap<String, Object>();
            extraData.put(AnalyticsUtils.KEY_EVENTS, "event122");
            extraData.put(AnalyticsUtils.KEY_EVENT_NAME, "Form Submission");
            AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_CRM,
                            AnalyticsUtils.CONTENT_FOCUS_ACCOUNT,
                    null,
                    AnalyticsUtils.UO_PAGE_IDENTIFIER_UPDATE_ADDRESS,
                    null,
                    null,
                    extraData);

    }
}
