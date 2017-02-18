package com.universalstudios.orlandoresort.controller.userinterface.checkout;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.account.AccountUtils;
import com.universalstudios.orlandoresort.controller.userinterface.account.EmailErrorUtils;
import com.universalstudios.orlandoresort.controller.userinterface.global.LocationUtils;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.AndroidUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.FragmentCheckoutSignInBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.GetGuestProfileRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.ValidateEmailAddressRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.EmailValidationResult;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetGuestProfileResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.ValidateEmailAddressResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.network.domain.account.GetAccountStatusRequest;
import com.universalstudios.orlandoresort.model.network.domain.account.GetAccountStatusResponse;
import com.universalstudios.orlandoresort.model.network.response.HttpResponseStatus;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountLoginService;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;
import com.universalstudios.orlandoresort.model.state.account.LoginBroadcastReceiver;
import com.universalstudios.orlandoresort.model.state.account.LoginResultIntentFilter;
import com.universalstudios.orlandoresort.view.fonts.Button;
import com.universalstudios.orlandoresort.view.fonts.CheckBox;
import com.universalstudios.orlandoresort.view.fonts.EditText;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.util.Locale;


public class CheckoutSignInFragment extends NetworkFragment implements View.OnClickListener {
    private static final String TAG = CheckoutSignInFragment.class.getSimpleName();

    private static final String KEY_STATE_IS_CALL_IN_PROGRESS = "KEY_STATE_IS_CALL_IN_PROGRESS";
    private static final String KEY_STATE_REGISTER_OR_LOGIN_CALL = "KEY_STATE_REGISTER_OR_LOGIN_CALL";

    private static final int CALL_STATE_VALIDATE_EMAIL_REGISTER = 1;

    private int mCallValidateField;
    private boolean mIsCallInProgress;
    private CheckBox mMarketingCheckBox, mTermsCheckBox, mRememberMeCheckBox;
    private EditText mEmailEditText, mEmailLoginEditText, mPasswordLoginEditText;
    private Button mContinueButton, mLoginButton;
    private TextView mForgotPasswordText, mTermsMoreInfoText, mLoginEmailError, mLoginPasswordError,
            mEmailErrorText, mMarketingText, mTermsText;
    private RelativeLayout mLoading;
    private boolean mIgnoreEmailValidationError;

    private TridionConfig mTridionConfig;

    private CheckoutSignInFragmentListener mListener;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private LoginBroadcastReceiver mLoginBroadcastReceiver;

    private LoginBroadcastReceiver.LoginResultCallback mLoginResultCallback = new LoginBroadcastReceiver.LoginResultCallback() {
        @Override
        public void onLoginResult(@LoginBroadcastReceiver.LoginResult int result) {
            mIsCallInProgress = false;
            switch (result) {
                case LoginBroadcastReceiver.SUCCESS_REGISTERED:
                    GetGuestProfileRequest request = new GetGuestProfileRequest.Builder(CheckoutSignInFragment.this)
                            .build();

                    NetworkUtils.queueNetworkRequest(request);
                    NetworkUtils.startNetworkService();
                    break;
                case LoginBroadcastReceiver.ERROR_ACCOUNT_LOCKED:
                    showErrorMessage(mTridionConfig.getEr28());
                    break;
                case LoginBroadcastReceiver.ERROR_LOGIN_FAILED:
                case LoginBroadcastReceiver.ERROR_UNKNOWN:
                    showErrorMessage(mTridionConfig.getEr24());
                    break;
                case LoginBroadcastReceiver.SUCCESS_UNREGISTERED:
                case LoginBroadcastReceiver.SUCCESS_LOGOUT:
                default:
            }

            showViewBasedOnState();
        }
    };

    public static CheckoutSignInFragment newInstance() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance");
        }

        // Create a new fragment instance
        CheckoutSignInFragment fragment = new CheckoutSignInFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        // Add parameters to the argument bundle
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
        if (parentFragment != null && parentFragment instanceof CheckoutSignInFragmentListener) {
            mListener = (CheckoutSignInFragmentListener) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (activity != null && activity instanceof CheckoutSignInFragmentListener) {
            mListener = (CheckoutSignInFragmentListener) activity;
        }
        // If neither implements the interface, log a warning
        else if (mListener == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement CheckoutSignInFragmentListener");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate");
        }

        Bundle args = getArguments();
        if (args == null) {
        }
        // Otherwise, set incoming parameters
        else {
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            mIsCallInProgress = false;
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {
            mIsCallInProgress = savedInstanceState.getBoolean(KEY_STATE_IS_CALL_IN_PROGRESS, false);
            mCallValidateField = savedInstanceState.getInt(KEY_STATE_REGISTER_OR_LOGIN_CALL, -1);
        }

        mTridionConfig = IceTicketUtils.getTridionConfig();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView");
        }// GuestLoginOptIn

        FragmentCheckoutSignInBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_checkout_sign_in, container, false);
        binding.setTridion(mTridionConfig);
        View view = binding.getRoot();
        TextView mGuestContinueText = binding.fragmentCheckoutGuestContinueText;
        mEmailEditText = binding.fragmentCheckoutSignInEmailEditText;
        mEmailErrorText = binding.fragmentCheckoutSignInEmailErrorText;
        mMarketingCheckBox = binding.fragmentCheckoutSignInMarketingCheckbox;
        mTermsCheckBox = binding.fragmentCheckoutSignInTermsCheckbox;
        mMarketingText = binding.fragmentCheckoutSignInMarketingText;
        mTermsText = binding.fragmentCheckoutSignInTermsText;
        mTermsMoreInfoText = binding.fragmentCheckoutSignInTermsMoreInfoText;
        mContinueButton = binding.fragmentCheckoutSignInContinueButton;
        mEmailLoginEditText = binding.fragmentCheckoutSignInLoginEmail;
        mPasswordLoginEditText = binding.fragmentCheckoutSignInLoginPassword;
        mForgotPasswordText = binding.fragmentCheckoutSignInForgotPassword;
        mRememberMeCheckBox = binding.fragmentCheckoutSignInRememberMe;
        mLoginButton = binding.fragmentCheckoutSignInLogin;
        mLoginEmailError = binding.fragmentCheckoutSignInLoginEmailError;
        mLoginPasswordError = binding.fragmentCheckoutSignInLoginPasswordError;
        mLoading = binding.fragmentCheckoutSignInLoading;

        if (savedInstanceState == null) {
            // Default the marketing opted in if the locale is set to US, opted out otherwise
            boolean defaultOptInToMarketing = LocationUtils.isLocaleSet(getResources(), Locale.US);
            mMarketingCheckBox.setChecked(defaultOptInToMarketing);
        }

        mGuestContinueText.setText(mTridionConfig.getGuestLoginContinueLabel());
        mMarketingText.setText(mTridionConfig.getGuestLoginOptIn());

        if (AccountStateManager.getRememberMe()) {
            mEmailLoginEditText.setText(AccountStateManager.getUsername());
            mRememberMeCheckBox.setChecked(true);
        }

        // Terms and Conditions/Privacy Policy links/text
        mTermsText.setText(mTridionConfig.getTermsAndPolicy());
        mTermsText.setMovementMethod(LinkMovementMethod.getInstance());

        mTermsMoreInfoText.setText(mTridionConfig.getPrivacyInformation());
        mTermsMoreInfoText.setMovementMethod(LinkMovementMethod.getInstance());

        mForgotPasswordText.setOnClickListener(this);

        mContinueButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);

        mEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mIgnoreEmailValidationError = false;
                validateUnregisteredUserInfo(mIgnoreEmailValidationError);
            }
        });

        mTermsCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                validateUnregisteredUserInfo(mIgnoreEmailValidationError);
            }
        });

        mEmailLoginEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateLoginUserInfo();
            }
        });
        mEmailLoginEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String enteredEmail = mEmailLoginEditText.getText().toString();
                    if (TextUtils.isEmpty(enteredEmail)) {
                        mLoginEmailError.setText(mTridionConfig.getEr26());
                        mLoginEmailError.setVisibility(View.VISIBLE);
                    } else if (!AccountUtils.isValidEmail(enteredEmail)) {
                        mLoginEmailError.setText(mTridionConfig.getEr6());
                        mLoginEmailError.setVisibility(View.VISIBLE);
                    } else {
                        mLoginEmailError.setVisibility(View.GONE);
                    }
                }
            }
        });

        mPasswordLoginEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateLoginUserInfo();
            }
        });
        mPasswordLoginEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(mPasswordLoginEditText.getText().toString())) {
                        mLoginPasswordError.setText(mTridionConfig.getEr26());
                        mLoginPasswordError.setVisibility(View.VISIBLE);
                    } else {
                        mLoginPasswordError.setVisibility(View.GONE);
                    }
                }
            }
        });


        mLoginButton.setEnabled(false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewCreated");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStart");
        }
        Activity parentActivity = getActivity();
        if (parentActivity != null && null != parentActivity.getActionBar()) {
            parentActivity.getActionBar().setTitle(mTridionConfig.getPageHeaderCLTitle());
        }

        // Validate the unregistered info
        validateUnregisteredUserInfo(mIgnoreEmailValidationError);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onResume");
        }
        mLoginBroadcastReceiver = new LoginBroadcastReceiver(mLoginResultCallback);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                mLoginBroadcastReceiver, new LoginResultIntentFilter()
        );

    }

    @Override
    public void onPause() {
        if (null != mLoginBroadcastReceiver) {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mLoginBroadcastReceiver);
            mLoginBroadcastReceiver = null;
        }
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
        outState.putBoolean(KEY_STATE_IS_CALL_IN_PROGRESS, mIsCallInProgress);
        outState.putInt(KEY_STATE_REGISTER_OR_LOGIN_CALL, mCallValidateField);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_checkout_sign_in_forgot_password:
                if (mListener != null) {
                    mListener.onForgotPasswordClicked();
                }
                break;
            case R.id.fragment_checkout_sign_in_continue_button:
                UserInterfaceUtils.closeKeyboard(v);
                // Fire off the create unregistered call for tracking the external guest ID
                AccountLoginService.startActionSendGuestIdAnalytics(getContext());
                getAccountStatus();
                break;
            case R.id.fragment_checkout_sign_in_login:
                UserInterfaceUtils.closeKeyboard(v);
                login();
                break;
        }
    }

    private void showViewBasedOnState() {
        if (mIsCallInProgress) {
            showLoading();
        } else {
            hideLoading();
        }
    }

    private void showLoading() {
        mLoading.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        mLoading.setVisibility(View.GONE);
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        mIsCallInProgress = false;

        if (networkResponse instanceof GetAccountStatusResponse) {
            GetAccountStatusResponse accountStatusResponse = (GetAccountStatusResponse) networkResponse;
            //201 means this email is already in use by a registered guest, force them to log in
            if (accountStatusResponse.getStatusCode() == HttpResponseStatus.SUCCESS_CREATED) {
                Toast.makeText(getContext(), mTridionConfig.getEr67(), Toast.LENGTH_SHORT)
                        .setIconColor(Color.RED)
                        .show();
                mContinueButton.setEnabled(false);
            } else {
                if (mIgnoreEmailValidationError) {
                    continueAsUnregisteredUser();
                } else {
                    validateEmail();
                }
            }
        }
        if (networkResponse instanceof ValidateEmailAddressResponse) {
            ValidateEmailAddressResponse emailResponse = (ValidateEmailAddressResponse) networkResponse;
            EmailValidationResult result = emailResponse.getResult();
            // Proceed if this marketing ID matches the one we originally sent, and if all of our fields are valid
            if (result != null && result.getMarketingId() != null && result.getCertainty() != null &&
                    TextUtils.equals(result.getMarketingId(), AccountStateManager.getGuestId())) {

                // Get the error message and show it, if applicable
                String errorMessage = EmailErrorUtils.getErrorMessageIfExists(result, getContext());

                if (mCallValidateField == CALL_STATE_VALIDATE_EMAIL_REGISTER) {
                    if (errorMessage != null) {
                        showEmailErrorMessage(errorMessage, EmailErrorUtils.isEmailUndeliverable(result));
                        mContinueButton.setEnabled(false);
                    } else {
                        continueAsUnregisteredUser();
                    }
                }
            }
        } else if (networkResponse instanceof GetGuestProfileResponse) {

            if (networkResponse.isHttpStatusCodeSuccess()) {
                GetGuestProfileResponse response = (GetGuestProfileResponse) networkResponse;
                if (mListener != null) {
                    mListener.onLogin();
                }
                AccountUtils.setUserName(response, getContext());
            } else {
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "Retrieving guest profile call resulted in failure code");
                }
            }
        }

        showViewBasedOnState();
    }

    private void continueAsUnregisteredUser() {
        // Remove error text and activate the Continue button
        mEmailErrorText.setVisibility(View.GONE);
        mContinueButton.setEnabled(true);

        // Store the unregistered user's email for later
        String unregisteredUserEmail = mEmailEditText.getText().toString().trim();
        AccountUtils.setUnregisteredUserEmail(unregisteredUserEmail);

        if (mListener != null) {
            mListener.onLogin();
        }
    }

    private void showEmailErrorMessage(String errorMessage, boolean isUndeliverable) {
        mEmailErrorText.setText(errorMessage);
        mEmailErrorText.setVisibility(View.VISIBLE);

        // Only present the ignore option if the email is deliverable
        if (!isUndeliverable) {
            OnClickListener ignoreOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // "Ignore" link clicked
                    mIgnoreEmailValidationError = true;
                    validateUnregisteredUserInfo(mIgnoreEmailValidationError);
                }
            };
            EmailErrorUtils.appendIgnoreLinkToText(mEmailErrorText, ignoreOnClickListener);
        }
    }

    private void showErrorMessage(final String errorMessage) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG)
                        .setIcon(R.drawable.error_blue_bigger)
                        .setIconColor(Color.RED)
                        .show();
            }
        });
    }

    private void login() {
        AndroidUtils.forceHideKeyboard(getActivity(), getActivity().getCurrentFocus());

        final String username = mEmailLoginEditText.getText().toString().trim();
        final String password = mPasswordLoginEditText.getText().toString().trim();

        mIsCallInProgress = true;
        AccountLoginService.startActionLoginWithUsernamePassword(getContext(), username, password);
        showViewBasedOnState();
    }

    private void getAccountStatus() {
        mIsCallInProgress = true;
        String emailAddress = mEmailEditText.getText().toString().trim();

        // Fire the request with the guest ID as the marketing ID (getGuestId is null safe)
        GetAccountStatusRequest request = new GetAccountStatusRequest.Builder(this)
                .setEmail(emailAddress)
                .build();

        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();

        showViewBasedOnState();
    }

    private void validateEmail() {
        mIsCallInProgress = true;
        mCallValidateField = CALL_STATE_VALIDATE_EMAIL_REGISTER;

        String emailAddress = mEmailEditText.getText().toString().trim();

        // Fire the request with the guest ID as the marketing ID (getGuestId is null safe)
        ValidateEmailAddressRequest request = new ValidateEmailAddressRequest.Builder(this)
                .setEmail(emailAddress)
                .setMarketingId(AccountStateManager.getGuestId())
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();

        showViewBasedOnState();
    }

    private boolean validateUnregisteredUserInfo(boolean ignoreEmailValidationError) {
        boolean valid = true;

        // Check terms
        if (!mTermsCheckBox.isChecked()) {
            valid = false;
        }

        // Check unregistered email
        String emailEntered = mEmailEditText.getText().toString().trim();
        if (TextUtils.isEmpty(emailEntered) || !AccountUtils.isValidEmail(emailEntered)) {
            valid = false;
        }

        // Show an email error if the email is entered, but not valid (otherwise the user will not know why they can't click continue)
        // Do not show the email error if it has been ignored by the user (because it was undeliverable)
        if (!TextUtils.isEmpty(emailEntered) && !AccountUtils.isValidEmail(emailEntered) && !ignoreEmailValidationError) {
            showEmailErrorMessage(mTridionConfig.getEr6(), true);
        } else {
            mEmailErrorText.setVisibility(View.GONE);
        }

        // Update continue button
        mContinueButton.setEnabled(valid);

        return valid;
    }

    private boolean validateLoginUserInfo() {
        boolean valid = true;

        // Check password email
        String passwordEntered = mPasswordLoginEditText.getText().toString();
        if (TextUtils.isEmpty(passwordEntered)) {
            valid = false;
        }

        // Check email
        String emailEntered = mEmailLoginEditText.getText().toString();
        if (TextUtils.isEmpty(emailEntered) || !AccountUtils.isValidEmail(emailEntered)) {
            valid = false;
        }

        // Update login button
        mLoginButton.setEnabled(valid);

        return valid;
    }

    public interface CheckoutSignInFragmentListener {
        void onLogin();

        void onForgotPasswordClicked();
    }

}
