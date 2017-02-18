package com.universalstudios.orlandoresort.controller.userinterface.account;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.FullScreenLoadingView;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.AccountUpdateContactInfoBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Contact;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.UpdateContactInfoRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.ValidateEmailAddressRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.EmailValidationResult;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.UpdateContactInfoResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.ValidateEmailAddressResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.ConcurrencyType;
import com.universalstudios.orlandoresort.model.network.response.HttpResponseStatus;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import org.parceler.Parcels;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/24/16.
 * Class: AccountUpdateContactInfoFragment
 * Class Description: Fragment to update the user's contact info
 */
public class AccountUpdateContactInfoFragment extends NetworkFragment implements View.OnClickListener, View.OnFocusChangeListener {
    public static final String TAG = AccountUpdateContactInfoFragment.class.getSimpleName();
    private static final String KEY_ARG_GUEST_PROFILE = "ARG_GUEST_PROFILE";

    private EditText etEmail;
    private EditText etPhone;
    private Button btnUpdate;
    private Button btnCancel;
    private TextView tvErrorEmail;
    private TextView tvErrorPhoneNumber;
    private FullScreenLoadingView mLoadingView;
    private ImageView ivInfo;

    private TridionConfig mTridionConfig;
    private boolean mEmailLocalValid;
    private boolean mEmailChanged;
    private boolean mIsEmailValidRemote;
    private boolean mIsEmailValidationInProgress;
    private boolean mIsUpdateClicked;

    private GuestProfile guestProfile;
    private AccountUpdateProvider mAccountUpdateProvider;

    /**
     * Factory method to create a new instance of AccountUpdateContactInfoFragment
     *
     * @param guestProfile
     *         The user's GuestProfile
     *
     * @return new instance of AccountUpdateContactInfoFragment
     */
    public static AccountUpdateContactInfoFragment newInstance(GuestProfile guestProfile) {
        AccountUpdateContactInfoFragment fragment = new AccountUpdateContactInfoFragment();
        Bundle b = new Bundle();
        if (guestProfile != null) {
            b.putParcelable(KEY_ARG_GUEST_PROFILE, Parcels.wrap(guestProfile));
        }
        fragment.setArguments(b);
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
        if (parentFragment != null && parentFragment instanceof AccountUpdateProvider) {
            mAccountUpdateProvider = (AccountUpdateProvider) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (activity != null && activity instanceof AccountUpdateProvider) {
            mAccountUpdateProvider = (AccountUpdateProvider) activity;
        }
        // If neither implements the interface, log a warning
        else if (mAccountUpdateProvider == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement AccountUpdateProvider");
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Default parameters
        Bundle args = getArguments();
        if (args == null) {
            guestProfile = null;
        }
        // Otherwise, set incoming parameters
        else {
            guestProfile = Parcels.unwrap(args.getParcelable(KEY_ARG_GUEST_PROFILE));
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {

        }
        // Otherwise restore state, overwriting any passed in parameters
        else {

        }
        mTridionConfig = IceTicketUtils.getTridionConfig();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AccountUpdateContactInfoBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.account_update_contact_info, container, false);
        binding.setTridion(mTridionConfig);
        View layout = binding.getRoot();
        etEmail = binding.etEmail;
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEmailChanged = true;
                mIsEmailValidRemote = false;
                tvErrorEmail.setVisibility(View.GONE);
                enableSubmitButton(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPhone = binding.etPhoneNumber;
        tvErrorEmail = binding.tvErrorEmail;
        tvErrorPhoneNumber = binding.tvErrorPhoneNumber;
        ivInfo = binding.ivInfo;

        etPhone.setOnFocusChangeListener(this);
        etEmail.setOnFocusChangeListener(this);

        btnCancel = binding.btnCancel;
        btnUpdate = binding.btnUpdate;

        btnCancel.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        ivInfo.setOnClickListener(this);

        populateFields();

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStart");
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus && v.getId() == etPhone.getId()) {
            String text = etPhone.getText().toString().trim().replaceAll("[^\\d.]", "");
            if (!TextUtils.isEmpty(text)) {
                etPhone.setText(AccountUtils.getFormattedPhoneNumber(text));
            }
        }

        if (!hasFocus && v instanceof EditText) {
            validateFields((EditText) v);
        }

        if (mEmailChanged && !mIsEmailValidationInProgress && !mIsUpdateClicked &&
                !hasFocus && v.getId() == R.id.etEmail) {
            if (mEmailLocalValid && !mIsEmailValidRemote) {
                validateEmailService();
            }
        }
    }

    private void validateEmailService() {
        // Only validate if the fragment is attached to the activity
        // This is why focus change listeners should not trigger network calls
        if (!isRemoving()) {
            String emailText = etEmail.getText().toString();
            mIsEmailValidationInProgress = true;
            FullScreenLoadingView.showDialog(getActivity().getSupportFragmentManager());

            // Fire the request with the guest ID as the marketing ID (getGuestId is null safe)
            ValidateEmailAddressRequest request = new ValidateEmailAddressRequest.Builder(this)
                    .setEmail(emailText)
                    .setMarketingId(AccountStateManager.getGuestId())
                    .build();
            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        }
    }

    /**
     * Pre populate the fields with the user's current contact information
     */
    private void populateFields() {
        etEmail.setText(guestProfile.getContact().getEmail());
        // Assume email is valid because it came from the profile
        mIsEmailValidRemote = true;
        etPhone.setText(AccountUtils.getFormattedPhoneNumber(guestProfile.getContact().getMobilePhone()));
    }

    /**
     * Validate that all fields above the current field are valid
     *
     * @param editText
     *         Current field (set to last field to validate all)
     *
     * @return true if valid
     */
    private boolean validateFields(EditText editText) {
        String text;
        boolean valid = true;
        hideAllErrors();

        switch (editText.getId()) {
            case R.id.etEmail:
                text = etEmail.getText().toString().trim();
                mEmailLocalValid = false;
                if (TextUtils.isEmpty(text)) {
                    tvErrorEmail.setText(mTridionConfig.getEr26());
                    showError(tvErrorEmail);
                    valid = false;
                }
                else if (!AccountUtils.isValidEmail(text)) {
                    tvErrorEmail.setText(mTridionConfig.getEr6());
                    showError(tvErrorEmail);
                    valid = false;
                }
                else {
                    mEmailLocalValid = true;
                }
            case R.id.etPhoneNumber:
                text = etPhone.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    if (text.matches("[^\\d]")) {
                        valid = false;
                        tvErrorPhoneNumber.setText(mTridionConfig.getEr5());
                        showError(tvErrorPhoneNumber);
                    }
                }
        }

        return valid;
    }

    /**
     * Hides all the error views
     */
    private void hideAllErrors() {
        tvErrorPhoneNumber.setVisibility(View.GONE);
    }

    /**
     * Shows the specific error view
     *
     * @param v
     *         View to show
     */
    private void showError(View v) {
        v.setVisibility(View.VISIBLE);
    }

    /**
     * Make request to update the user's contact info
     */
    private void updateContactInfo() {
        Contact contact = guestProfile.getContact();
        UpdateContactInfoRequest request = new UpdateContactInfoRequest.Builder(this)
                .setEmail(etEmail.getText().toString())
                .setFirstname(contact.getFirstName())
                .setLastname(contact.getLastName())
                .setNamePrefix(contact.getNamePrefix())
                .setNameSuffix(contact.getNameSuffix())
                .setMobilePhone(etPhone.getText().toString())
                .setConcurrencyType(ConcurrencyType.ASYNCHRONOUS)
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
        mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnCancel.getId()) {
            mAccountUpdateProvider.updateAccount(false);
        }
        else if (v.getId() == btnUpdate.getId()) {
            if (validateFields(etPhone)) {
                if (validateFields(etEmail)) {
                    if (!mIsEmailValidRemote && mEmailChanged && !mIsEmailValidationInProgress) {
                        mIsUpdateClicked = true;
                        validateEmailService();
                    } else {
                        updateContactInfo();
                    }
                }
            }
        }
        else if (v.getId() == ivInfo.getId()) {
            Toast.makeText(getContext(), mTridionConfig.getEmailAddressToolTip(), android.widget.Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        // Only handle a network response if the fragment is attached to the activity
        // This is why focus change listeners should not trigger network calls
        if (!isRemoving()) {
        /*
        This method handleNetworkResponse is in a call chain, ultimately being called from within
        NetworkFragment's onLoadFinished() method.  The NetworkDialog cannot be dismissed from this
        separate thread - it must be dismissed from the UI thread.
         */
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    FullScreenLoadingView.dismissDialog();
                }
            });

            // If the network response is null, then we have a problem
            if (networkResponse == null) {
                return;
            }
            // Perform email validation operations if this is a ValidateEmailAddressResponse
            if (networkResponse instanceof ValidateEmailAddressResponse) {
                handleValidationEmailAddressResponse((ValidateEmailAddressResponse) networkResponse);
            }
            else if (networkResponse instanceof UpdateContactInfoResponse) {
                UpdateContactInfoResponse response = (UpdateContactInfoResponse) networkResponse;

                if (response.getStatusCode() == HttpResponseStatus.SUCCESS_OK || response.getStatusCode() == HttpResponseStatus.SUCCESS_UNKNOWN) {
                    String username = "";
                    if (etEmail != null && etEmail.getText() != null) {
                        username = etEmail.getText().toString().trim();
                    }
                    AccountStateManager.saveUsernamePassword(username,
                            AccountStateManager.getPassword());
                    mAccountUpdateProvider.updateAccount(true);
                    Toast.makeText(getContext(), mTridionConfig.getSu7(), Toast.LENGTH_SHORT)
                            .show();
                }
                else {
                    Toast.makeText(getContext(), mTridionConfig.getEr84(), android.widget.Toast.LENGTH_LONG)
                            .setIconColor(Color.RED)
                            .show();
                }
            }
        }
    }

    private void handleValidationEmailAddressResponse(@NonNull ValidateEmailAddressResponse networkResponse) {
        mIsEmailValidationInProgress = false;
        ValidateEmailAddressResponse emailResponse = networkResponse;
        EmailValidationResult result = emailResponse.getResult();
        // Proceed if this marketing ID matches the one we originally sent, and if all of our fields are valid
        if (result != null && result.getMarketingId() != null && result.getCertainty() != null &&
            TextUtils.equals(result.getMarketingId(), AccountStateManager.getGuestId())) {

            // Get the error message and show it, if applicable
            String errorMessage = EmailErrorUtils.getErrorMessageIfExists(result, getContext());
            if (errorMessage != null) {
                showEmailErrorMessage(errorMessage, EmailErrorUtils.isEmailUndeliverable(result));
                enableSubmitButton(false);
                mIsEmailValidRemote = false;
            } else if (mEmailLocalValid) {
                tvErrorEmail.setVisibility(View.GONE);
                mIsEmailValidRemote = true;
                // If absolutely no errors at all, then show/activate the create account button
                enableSubmitButton(true);
                if (mIsUpdateClicked) {
                    mIsUpdateClicked = false;
                    updateContactInfo();
                }
            }
        }

    }

    private void showEmailErrorMessage(String errorMessage, boolean isUndeliverable) {
        if (tvErrorEmail == null) {
            return;
        }

        tvErrorEmail.setText(errorMessage);

        // Only present the ignore option if the email is deliverable
        if (!isUndeliverable) {
            OnClickListener ignoreOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // "Ignore" link clicked
                    tvErrorEmail.setVisibility(View.GONE);
                    // Override the remote email validation because the user clicked ignore
                    mIsEmailValidRemote = true;
                    enableSubmitButton(true);
                }
            };
            EmailErrorUtils.appendIgnoreLinkToText(tvErrorEmail, ignoreOnClickListener);
        }

        showError(tvErrorEmail);
    }
    private void enableSubmitButton(boolean enabled) {
        btnUpdate.setEnabled(enabled);

        int drawable = enabled ? R.drawable.shape_tooltip_background : R.drawable.tooltip_background_disabled;
        btnUpdate.setBackgroundResource(drawable);
    }
}