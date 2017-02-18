package com.universalstudios.orlandoresort.controller.userinterface.account;


import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.account.address.AddressInformationActivity;
import com.universalstudios.orlandoresort.controller.userinterface.account.address.AddressViewModel;
import com.universalstudios.orlandoresort.controller.userinterface.account.address.ProfileAddressListActivity;
import com.universalstudios.orlandoresort.controller.userinterface.account.views.PhoneNumberFormattingEditText;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerClickHandler;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.FragmentCommunicationPrefsBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Address;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.EmailFrequencyType;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestPreference;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.UserContactPermissions;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.GetGuestProfileRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.UpdateCommunicationPrefsRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.UpdateContactInfoRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetGuestProfileResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.UpdateCommunicationPrefsResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.UpdateContactInfoResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.network.response.HttpResponseStatus;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.view.fonts.CheckBox;

import java.util.Arrays;
import java.util.List;


public class UpdateCommunicationPrefsFragment extends NetworkFragment implements CompoundButton.OnCheckedChangeListener, View.OnFocusChangeListener,
        View.OnClickListener {

    private static final String TAG = UpdateCommunicationPrefsFragment.class.getSimpleName();

    private static final String KEY_ARG_ACTION_BAR_TITLE_STRING = "key_arg_action_bar_title_string";
    private static final String KEY_STATE_IS_CALL_IN_PROGRESS = "key_state_is_call_in_progress";
    private static final String KEY_STATE_UPDATE_ADDRESS_ONLY = "KEY_STATE_UPDATE_ADDRESS_ONLY";

    private static final int ACTIVITY_REQUEST_CODE_ADD_ADDRESS = 10001;
    private static final int ACTIVITY_REQUEST_CODE_ADDRESS_LIST = 10002;

    private DrawerClickHandler mParentDrawerClickHandler;
    private String mActionBarTitleString;

    private TridionConfig mTridionConfig;
    private AddressViewModel mAddressViewModel;
    private GuestProfile mGuestProfile;

    private boolean mIsCallInProgress;
    private boolean mUpdateAllFields;

    private ViewGroup mContainer;
    private ProgressBar mLoading;
    private CheckBox mText;
    private CheckBox mEmail;
    private ViewGroup mEmailFrequencyLabelContainer;
    private SeekBar mEmailFrequency;
    private CheckBox mDirect;
    private CheckBox mTargeted;
    private PhoneNumberFormattingEditText mPhoneNumber;
    private TextView mPhoneError;
    private ViewGroup mEditAddressContainer;
    private ViewGroup mEditButtonContainer;
    private ViewGroup mAddAddressContainer;
    private TextView mTosAndPrivacy;
    private MenuItem mUpdate;

    private boolean mPhoneChanged;

    public UpdateCommunicationPrefsFragment() {
    }

    public static UpdateCommunicationPrefsFragment newInstance(String actionBarTitleString) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance: actionBarTitleResId = " + actionBarTitleString);
        }

        // Create a new fragment instance
        UpdateCommunicationPrefsFragment fragment = new UpdateCommunicationPrefsFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        // Add parameters to the argument bundle
        args.putString(KEY_ARG_ACTION_BAR_TITLE_STRING, actionBarTitleString);
        fragment.setArguments(args);

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
        if (parentFragment != null && parentFragment instanceof DrawerClickHandler) {
            mParentDrawerClickHandler = (DrawerClickHandler) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (activity != null && activity instanceof DrawerClickHandler) {
            mParentDrawerClickHandler = (DrawerClickHandler) activity;
        }
        // If neither implements the interface, log a warning
        else if (mParentDrawerClickHandler == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement DrawerClickHandler");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        if (args == null) {
            mActionBarTitleString = "";
        }
        // Otherwise, set incoming parameters
        else {
            mActionBarTitleString = args.getString(KEY_ARG_ACTION_BAR_TITLE_STRING);
        }

        if (savedInstanceState != null) {
            mIsCallInProgress = savedInstanceState.getBoolean(KEY_STATE_IS_CALL_IN_PROGRESS, false);
            mUpdateAllFields = savedInstanceState.getBoolean(KEY_STATE_UPDATE_ADDRESS_ONLY, true);
        } else {
            mIsCallInProgress = false;
            mUpdateAllFields = true;
        }

        mTridionConfig = IceTicketUtils.getTridionConfig();
        mAddressViewModel = new AddressViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView");
        }

        FragmentCommunicationPrefsBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_communication_prefs, container, false);
        binding.setTridion(mTridionConfig);
        binding.setAddress(mAddressViewModel);
        View view = binding.getRoot();

        mContainer = binding.fragmentCommunicationPrefsContainer;
        mEditAddressContainer = binding.fragmentCommunicationPrefsEditAddressContainer;
        mAddAddressContainer = binding.fragmentCommunicationPrefsAddAddressContainer;
        mEditButtonContainer = binding.fragmentCommunicationPrefsEditButtonContainer;
        mLoading = binding.fragmentCommunicationPrefsLoading;
        mEmailFrequencyLabelContainer = binding.fragmentCommunicationPrefsEmailFrequencyLabelContainer;
        mText = binding.fragmentCommunicationPrefsTexts;
        mPhoneNumber = binding.fragmentCommunicationPrefsPhoneNumber;
        mPhoneError = binding.fragmentCommunicationPrefsPhoneError;
        mEmail = binding.fragmentCommunicationPrefsEmails;
        mDirect = binding.fragmentCommunicationPrefsDirectMail;
        mTargeted = binding.fragmentCommunicationPrefsTargeted;
        mEmailFrequency = binding.fragmentCommunicationPrefsEmailFrequency;
        mTosAndPrivacy = binding.fragmentCommunicationPrefsTos;

        mEditButtonContainer.setOnClickListener(this);
        mAddAddressContainer.setOnClickListener(this);

        mText.setOnCheckedChangeListener(this);
        mEmail.setOnCheckedChangeListener(this);
        mDirect.setOnCheckedChangeListener(this);

        mPhoneNumber.setOnFocusChangeListener(this);
        mPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPhoneChanged = true;
                mPhoneError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTosAndPrivacy.setMovementMethod(LinkMovementMethod.getInstance());

        String emailFrequencyOptions = mTridionConfig.getCPEmailFrequencyOptions();
        List<String> emailFrequenciesList = Arrays.asList(emailFrequencyOptions.split(","));

        if (emailFrequenciesList.size() > 0) {
            //create params to evenly space view across parent
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f);

            mEmailFrequency.setMax(emailFrequenciesList.size() - 1);

            for (int i = 0; i < emailFrequenciesList.size(); i++) {
                String label = emailFrequenciesList.get(i);
                TextView textView = (TextView) inflater.inflate(R.layout.item_email_frequency_seek_bar_label, null);
                textView.setText(label);
                //when inflating width=0, weight=1 here, is not respected, so must be set again here
                textView.setLayoutParams(param);

                //first item is left-aligned
                //last item is right-aligned
                if (i == 0) {
                    textView.setGravity(Gravity.START);
                } else if (i == emailFrequenciesList.size() - 1) {
                    textView.setGravity(Gravity.END);
                }

                mEmailFrequencyLabelContainer.addView(textView);
            }
        }

        // Set the action bar title, if the drawer isn't open
        Activity parentActivity = getActivity();
        if (parentActivity != null && parentActivity.getActionBar() != null) {
            parentActivity.getActionBar().setTitle(mActionBarTitleString);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_STATE_IS_CALL_IN_PROGRESS, mIsCallInProgress);
        outState.putBoolean(KEY_STATE_UPDATE_ADDRESS_ONLY, mUpdateAllFields);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewCreated");
        }

        requestUserProfile(true);
        showViewBasedOnState();
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateOptionsMenu");
        }

        inflater.inflate(R.menu.action_update, menu);
        String itemText = mTridionConfig.getUpdateLabel();
        if (!TextUtils.isEmpty(itemText)) {
            mUpdate = menu.findItem(R.id.action_update);
            mUpdate.setTitle(mTridionConfig.getUpdateLabel());
        }
    }

    /**
     * when update is clicked update phone, then address, then preferences
     * if phone numnber has not changed, skip that, and update address
     * after address has been updated, {@link #handleNetworkResponse(NetworkResponse)} will call
     * {@link #updateCommunicationPrefs()}
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onOptionsItemSelected");
        }

        switch (item.getItemId()) {
            case R.id.action_update:
                if (validateFields()) {
                    if (mPhoneChanged) {
                        updatePhoneNumber();
                    } else {
                        updateCommunicationPrefs();
                    }
                }
                showViewBasedOnState();
                return true;
            default:
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "onOptionsItemSelected: unknown menu item selected");
                }
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_REQUEST_CODE_ADD_ADDRESS:
            case ACTIVITY_REQUEST_CODE_ADDRESS_LIST:
            default:
                requestUserProfile(false);
                showViewBasedOnState();
                break;
        }
    }

    private void showViewBasedOnState() {
        if (mIsCallInProgress) {
            showLoadingView();
        } else {
            showPreferencesView();
        }
    }

    private void showLoadingView() {
        mContainer.setVisibility(View.GONE);
        mLoading.setVisibility(View.VISIBLE);
    }

    private void showPreferencesView() {
        mContainer.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
    }

    private void updatePrefViewValues() {
        GuestPreference preference = mGuestProfile.getPreference();
        UserContactPermissions permissions = preference.getContactPermissions();

        mTargeted.setChecked(permissions.isTargetedSocialAdvertising());

        boolean emailPermission = permissions.isEmailPermission();
        mEmail.setChecked(emailPermission);
        mEmailFrequency.setProgress(EmailFrequencyType.fromValue(preference.getEmailFrequency()));
        if (emailPermission) {
            mEmailFrequencyLabelContainer.setVisibility(View.VISIBLE);
            mEmailFrequency.setVisibility(View.VISIBLE);
        } else {
            mEmailFrequencyLabelContainer.setVisibility(View.GONE);
            mEmailFrequency.setVisibility(View.GONE);
        }

        boolean textMessage = permissions.isTextMessage();
        mText.setChecked(textMessage);

        boolean directMail = permissions.isDirectMail();
        mDirect.setChecked(directMail);

        updateAddressViewValues();
        updatePhoneViewValue();
    }

    private void updatePhoneViewValue() {
        GuestPreference preference = mGuestProfile.getPreference();
        UserContactPermissions permissions = preference.getContactPermissions();
        boolean textMessage = permissions.isTextMessage();

        if (textMessage) {
            if (!TextUtils.isEmpty(mGuestProfile.getContact().getMobilePhone())) {
                mPhoneNumber.setVisibility(View.GONE);
            } else {
                mPhoneNumber.setVisibility(View.VISIBLE);
                mPhoneNumber.setText(AccountUtils.getFormattedPhoneNumber(mGuestProfile.getContact().getMobilePhone()));
            }
        } else {
            mPhoneNumber.setVisibility(View.GONE);
        }
    }

    private void updateAddressViewValues() {
        boolean hasAddress = mGuestProfile.hasAddress();

        //if direct not selected, hide both add and edit
        //use checkbox value instead of preference in case returning form address add/edit and refreshing data
        if (!mDirect.isChecked()) {
            mAddAddressContainer.setVisibility(View.GONE);
            mEditAddressContainer.setVisibility(View.GONE);
        } else {
            mAddAddressContainer.setVisibility(!hasAddress ? View.VISIBLE : View.GONE);
            mEditAddressContainer.setVisibility(hasAddress ? View.VISIBLE : View.GONE);
        }

        //if has address, update fields
        if (mGuestProfile.hasAddress()) {
            Address address = mGuestProfile.getPrimaryAddress();
            if (address == null) {
                List<Address> addresses = mGuestProfile.getAddresses();
                if (addresses != null) {
                    address = addresses.get(0);
                }
            }

            if (address != null) {
                mAddressViewModel.setAddress(address);
            }
        }
    }

    /**
     * get the guest profile, if returning from address add/edit, do not update
     * checkbox values because the user may have changed them, and would reset to
     * their unsaved values
     *
     * @param updateAllFields
     */
    public void requestUserProfile(boolean updateAllFields) {
        if (mIsCallInProgress) {
            return;
        }
        mUpdateAllFields = updateAllFields;
        mIsCallInProgress = true;

        GetGuestProfileRequest request = new GetGuestProfileRequest.Builder(this)
                .build();

        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    private void updatePhoneNumber() {
        if (mIsCallInProgress) {
            return;
        }

        final NetworkRequestSender sender = this;
        UpdateContactInfoRequest request = new UpdateContactInfoRequest.Builder(sender)
                .setMobilePhone(mPhoneNumber.getText().toString())
                .build();

        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();

        mIsCallInProgress = true;
    }

    private void updateCommunicationPrefs() {
        //do not make call if it is already in progress
        if (mIsCallInProgress) {
            return;
        }

        mIsCallInProgress = true;

        UpdateCommunicationPrefsRequest request = new UpdateCommunicationPrefsRequest.Builder(this)
                .setTextMessage(mText.isChecked())
                .setEmailPermission(mEmail.isChecked())
                .setDirectMail(mDirect.isChecked())
                .setTargetedSocialAdvertising(mTargeted.isChecked())
                .setEmailFrequency(EmailFrequencyType.fromValue(mEmailFrequency.getProgress()))
                .build();

        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    private boolean validateFields() {
        boolean validPhone = validatePhone();
        boolean validAddress = validateAddress();

        return validPhone && validAddress;
    }

    private boolean validateAddress() {
        boolean valid = true;
        if (mDirect.isChecked()) {
            valid = mGuestProfile.hasAddress();
        }

        if (!valid) {
            Toast.makeText(getContext(), mTridionConfig.getEr83(), Toast.LENGTH_SHORT).show();
        }

        return valid;
    }

    private boolean validatePhone() {
        if (!mPhoneChanged) {
            return true;
        }

        boolean valid = true;
        String text = mPhoneNumber.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            if (text.matches("[^\\d]")) {
                valid = false;
            }
        }

        if (!valid) {
            mPhoneError.setVisibility(View.VISIBLE);
            mUpdate.setEnabled(false);
        } else {
            mPhoneError.setVisibility(View.GONE);
            mUpdate.setEnabled(true);
        }

        return valid;
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        mIsCallInProgress = false;

        if (networkResponse instanceof GetGuestProfileResponse) {
            GetGuestProfileResponse response = (GetGuestProfileResponse) networkResponse;
            if (response.getResult() != null) {
                GuestProfile guestProfile = response.getResult().getGuestProfile();
                if (mUpdateAllFields) {
                    mGuestProfile = guestProfile;
                    updatePrefViewValues();
                } else {
                    Toast.makeText(getContext(), mTridionConfig.getEr82(), Toast.LENGTH_LONG).show();
                    mGuestProfile.setAddresses(guestProfile.getAddresses());
                    updateAddressViewValues();
                    updatePhoneViewValue();
                }
            } else {
                Toast.makeText(getContext(), mTridionConfig.getEr82(), Toast.LENGTH_SHORT).show();
            }
        } else if (networkResponse instanceof UpdateContactInfoResponse) {
            UpdateContactInfoResponse updateContactInfoResponse = (UpdateContactInfoResponse) networkResponse;

            // If updating contact info fails, just let the user know
            if (updateContactInfoResponse.getStatusCode() != HttpResponseStatus.SUCCESS_OK
                && updateContactInfoResponse.getStatusCode() != HttpResponseStatus.SUCCESS_UNKNOWN) {
                Toast.makeText(getContext(), mTridionConfig.getEr84(), android.widget.Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }

            // Always update communication prefs even if updating contact info fails
            updateCommunicationPrefs();
        } else if (networkResponse instanceof UpdateCommunicationPrefsResponse) {
            // If communication preferences were updated successfully, set the result and finish
            if (networkResponse.isHttpStatusCodeSuccess()) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
            // Otherwise, let the user know it failed
            else {
                Toast.makeText(getContext(), mTridionConfig.getEr85(), Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
        }

        showViewBasedOnState();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.fragment_communication_prefs_direct_mail:
                if (isChecked) {
                    mAddAddressContainer.setVisibility(!mGuestProfile.hasAddress() ? View.VISIBLE : View.GONE);
                    mEditAddressContainer.setVisibility(mGuestProfile.hasAddress() ? View.VISIBLE : View.GONE);
                } else {
                    mAddAddressContainer.setVisibility(View.GONE);
                    mEditAddressContainer.setVisibility(View.GONE);
                }
                break;
            case R.id.fragment_communication_prefs_texts:
                if (isChecked) {
                    if (!TextUtils.isEmpty(mGuestProfile.getContact().getMobilePhone())) {
                        mPhoneNumber.setVisibility(View.GONE);
                    } else {
                        mPhoneNumber.setVisibility(View.VISIBLE);
                    }
                } else {
                    mPhoneNumber.setVisibility(View.GONE);
                }
                break;
            case R.id.fragment_communication_prefs_emails:
                mEmailFrequencyLabelContainer.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                mEmailFrequency.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus && v.getId() == R.id.fragment_communication_prefs_phone_number) {
            String text = mPhoneNumber.getText().toString().trim().replaceAll("[^\\d.]", "");
            if (!TextUtils.isEmpty(text)) {
                mPhoneNumber.setText(AccountUtils.getFormattedPhoneNumber(text));
            }

            validatePhone();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_communication_prefs_add_address_container:
                // Add address
                Intent addressInfoIntent = new Intent(getActivity(), AddressInformationActivity.class);
                Bundle args = AddressInformationActivity.newInstanceBundle(SourceId.EDIT_COMMUNICATIONS, null, false);
                startActivityForResult(addressInfoIntent.putExtras(args), ACTIVITY_REQUEST_CODE_ADD_ADDRESS);
                break;
            case R.id.fragment_communication_prefs_edit_button_container:
                // Edit addresses
                Intent addressListIntent = ProfileAddressListActivity.newInstanceIntent(getContext(), SourceId.EDIT_COMMUNICATIONS);
                startActivityForResult(addressListIntent, ACTIVITY_REQUEST_CODE_ADDRESS_LIST);
                break;
        }
    }
}
