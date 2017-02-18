package com.universalstudios.orlandoresort.controller.userinterface.account;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.DialogUtils;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.FullScreenLoadingView;
import com.universalstudios.orlandoresort.controller.userinterface.global.LocationUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.link.ClickListener;
import com.universalstudios.orlandoresort.controller.userinterface.link.ClickableTextHelper;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.FragmentRegistrationBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.CreateAccountRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.ValidateEmailAddressRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.CreateAccountResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.EmailValidationResult;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.ValidateEmailAddressResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountLoginService;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;
import com.universalstudios.orlandoresort.view.password.PasswordStrengthView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/4/16.
 * Class: AccountRegisterFragment
 * Class Description: Fragment that allows the user to register for a UO Account
 */
public class AccountRegisterFragment extends NetworkFragment implements View.OnFocusChangeListener, View.OnClickListener,
        DatePickerDialog.OnDateSetListener {
    private static final String TAG = AccountRegisterFragment.class.getSimpleName();

    private EditText mEtFirstName;
    private EditText mEtLastName;
    private EditText mEtDob;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private PasswordStrengthView mPasswordStrengthView;
    private ImageView mInfo;
    private CheckBox mCbReceiveUpdates;
    private CheckBox mCbTermsAndConditions;
    private Button mBtnCreateAccount;
    private TextView mAccountHoldingLabel;
    private TextView mSignIn;
    private TextView mTvTermsAndCondition;
    private Dialog termsErrorPop;
    private Handler mHandler = new Handler();

    private TridionConfig mTridionConfig;
    private View previousFocus;
    private boolean mEmailLocalValid;

    private String mEmailAddress;
    private String mPassword;

    private AccountRegisterResultHandler mAccountRegisterResultHandler;

    //Error views
    private TextView tvErrorFirstname, tvErrorLastname, tvErrorEmail, tvErrorPassword, tvErrorDob;

    /**
     * Factory method to create a new instance of AccountRegisterFragment
     *
     * @return new instance of AccountRegisterFragment
     */
    public static AccountRegisterFragment newInstance() {

        // Create a new fragment instance
        AccountRegisterFragment fragment = new AccountRegisterFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        // Add parameters to the argument
        // bundle here, if any

        // Set the fragment's arguments
        fragment.setArguments(args);

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
        if (parentFragment != null && parentFragment instanceof AccountRegisterResultHandler) {
            mAccountRegisterResultHandler = (AccountRegisterResultHandler) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (activity != null && activity instanceof AccountRegisterResultHandler) {
            mAccountRegisterResultHandler = (AccountRegisterResultHandler) activity;
        }
        // If neither implements the interface, log a warning
        else if (mAccountRegisterResultHandler == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement AccountRegisterResultHandler");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentRegistrationBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false);
        mTridionConfig = IceTicketUtils.getTridionConfig();
        binding.setTridion(mTridionConfig);
        View layout = binding.getRoot();
        mEtFirstName = binding.etFirstName;
        mEtLastName = binding.etLastName;
        mEtDob = binding.etDateOfBirth;
        mEtEmail = binding.etEmail;
        mEtPassword = binding.etPassword;
        mPasswordStrengthView = binding.passwordStrengthMeter;
        mCbReceiveUpdates = binding.cbUpdates;
        mCbTermsAndConditions = binding.cbTermsAndConditions;
        mBtnCreateAccount = binding.btnCreateAccount;
        mInfo = binding.info;
        tvErrorFirstname = binding.tvFirstnameError;
        tvErrorLastname = binding.tvErrorLastname;
        tvErrorEmail = binding.tvErrorEmail;
        tvErrorPassword = binding.tvErrorPassword;
        tvErrorDob = binding.tvErrorDateofBirth;
        mAccountHoldingLabel = binding.tvAccountHoldingLabel;
        mSignIn = binding.signIn;
        mTvTermsAndCondition = binding.tvTermsAndConditions;

        mEtFirstName.setOnFocusChangeListener(this);
        mEtLastName.setOnFocusChangeListener(this);
        mEtEmail.setOnFocusChangeListener(this);
        mEtPassword.setOnFocusChangeListener(this);
        mPasswordStrengthView.bindPasswordEditText(mEtPassword);
        mBtnCreateAccount.setOnClickListener(this);
        mEtDob.setOnClickListener(this);
        mInfo.setOnClickListener(this);
        mSignIn.setOnClickListener(this);

        mTvTermsAndCondition.setMovementMethod(LinkMovementMethod.getInstance());
        setSignInText();

        boolean isUS = LocationUtils.isLocaleCountrySet(getResources(), Locale.US);
        if (isUS) {
            mCbReceiveUpdates.setChecked(true);
        }


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
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        if(savedInstance == null) {
            sendOnPageLoadAnalytics();
        }
    }

    private void createAccount() {
        if (validateAllFields()) {
            // Kick off create account request
            mPassword = mEtPassword.getText().toString();
            String dob = mEtDob.getText().toString();
            mEmailAddress = mEtEmail.getText().toString();
            String firstName = mEtFirstName.getText().toString();
            String lastName = mEtLastName.getText().toString();
            boolean flagReceiveUpdates = mCbReceiveUpdates.isChecked();
            boolean flagTOS = true;

            CreateAccountRequest request = new CreateAccountRequest.Builder(this)
                    .setPassword(mPassword)
                    .setBirthDate(dob)
                    .setEmail(mEmailAddress)
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setReceiveUpdates(flagReceiveUpdates)
                    .setTermsOfService(flagTOS)
                    .build();
            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
            sendCreateAccountSuccessAnalytics();
            if(flagReceiveUpdates) {
                sendOptInCheckedAnalytics();
            }
        }
    }

    /**
     * Called when trying to submit email. Validates all the fields and shows errors in the sections.
     *
     * @return false if ANY of the fields are not ready
     */
    private boolean validateAllFields() {
        if (!mCbTermsAndConditions.isChecked()) {
            showErrorMessage(mTridionConfig.getEr22(), R.drawable.info_blue_bigger, Color.RED);
        }

        return validateText(mEtDob, true) && mCbTermsAndConditions.isChecked();
    }

    private boolean validateText(View v, boolean callToAction) {
        hideAllErrors();
        boolean valid = true;
        if (v instanceof EditText) {
            String text;
            switch (v.getId()) {
                case R.id.etDateOfBirth:
                    if (null == mEtDob.getTag()) {
                        showError(tvErrorDob);
                    }
                case R.id.etPassword:
                    switch (AccountUtils.isValidPassword(mEtPassword.getText().toString(), mEtEmail.getText().toString())) {
                        case REPEATING_CHARACTERS:
                            tvErrorPassword.setText(mTridionConfig.getEr51());
                            showError(tvErrorPassword);
                            valid = false;
                            break;
                        case LENGTH:
                            tvErrorPassword.setText(mTridionConfig.getEr9());
                            showError(tvErrorPassword);
                            valid = false;
                            break;
                        case SAME_AS_EMAIL:
                            tvErrorPassword.setText(mTridionConfig.getEr60());
                            showError(tvErrorPassword);
                            valid = false;
                            break;
                        case EMPTY:
                            if (callToAction) {
                                tvErrorPassword.setText(mTridionConfig.getEr26());
                                showError(tvErrorPassword);
                                valid = false;
                            }
                            break;
                        case ENDS_WITH_SPACE:
                            tvErrorPassword.setText(mTridionConfig.getEr86());
                            showError(tvErrorPassword);
                            valid = false;
                            break;
                    }

                case R.id.etEmail:
                    text = mEtEmail.getText().toString();
                    mEmailLocalValid = false;

                    if (TextUtils.isEmpty(text) && callToAction) {
                        tvErrorEmail.setText(mTridionConfig.getEr26());
                        valid = false;
                        showError(tvErrorEmail);
                    }
                    else if (!AccountUtils.isValidEmail(text) && !TextUtils.isEmpty(text)) {
                        tvErrorEmail.setText(mTridionConfig.getEr48());
                        valid = false;
                        showError(tvErrorEmail);
                    }
                    else {
                        mEmailLocalValid = true;
                    }
                case R.id.etLastName:
                    text = mEtLastName.getText().toString();
                    if (TextUtils.isEmpty(text) && callToAction) {
                        tvErrorLastname.setText(mTridionConfig.getEr26());
                        showError(tvErrorLastname);
                        valid = false;
                    } else if (!AccountUtils.isValidName(text)) {
                        tvErrorLastname.setText(mTridionConfig.getEr2());
                        showError(tvErrorLastname);
                        valid = false;
                    }
                case R.id.etFirstName:
                    text = mEtFirstName.getText().toString();
                    if (TextUtils.isEmpty(text) && callToAction) {
                        tvErrorFirstname.setText(mTridionConfig.getEr26());
                        showError(tvErrorFirstname);
                        valid = false;
                    } else if (!AccountUtils.isValidName(text)) {
                        tvErrorFirstname.setText(mTridionConfig.getEr1());
                        showError(tvErrorFirstname);
                        valid = false;
                    }
            }
        }
        return valid;
    }

    public void setSignInText() {
        String accountHolding = mTridionConfig.getAccountHoldingOptionLabel();
        int start = accountHolding.toLowerCase().indexOf(mTridionConfig.getSignInLabel().toLowerCase());
        String signIn = accountHolding.substring(start);
        if (start >= 0) {
            accountHolding = accountHolding.substring(0, start);
        }
        mAccountHoldingLabel.setText(accountHolding);
        SpannableString spannableString = ClickableTextHelper.createEmbeddedSpannable(signIn,
                signIn,
                R.color.ticket_text_color,
                true,
                new ClickListener() {
                    @Override
                    public void onLinkClicked() {
                        onClick(mSignIn);
                    }
                });

        mSignIn.setText(spannableString);
        mSignIn.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * hides all the error messages
     */
    private void hideAllErrors() {
        tvErrorPassword.setVisibility(View.GONE);
        tvErrorFirstname.setVisibility(View.GONE);
        tvErrorLastname.setVisibility(View.GONE);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (AccountUtils.isWithinDateRange(year, monthOfYear, dayOfMonth)) {
            String dateString = String.format("%02d/%02d/%04d", monthOfYear+1, dayOfMonth, year);
            mEtDob.setText(dateString);
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, monthOfYear);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mEtDob.setTag(selectedDate);
            tvErrorDob.setVisibility(View.GONE);
        } else {
            showError(tvErrorDob);
            mEtDob.setTag(null);
        }
    }

    @Override
    public void onFocusChange(View currentFocus, boolean hasFocus) {

        if (!hasFocus) {
            validateText(currentFocus, false);
        }
        if (previousFocus != null && previousFocus.getId() == R.id.etEmail &&
            currentFocus.getId() != R.id.etEmail && hasFocus) {
            previousFocus = null;
            if (currentFocus instanceof EditText && mEmailLocalValid) {
                validateEmailService();
            }
        } else {
            previousFocus = currentFocus;
        }
    }

    private void validateEmailService() {
        String emailText = mEtEmail.getText().toString();
        FullScreenLoadingView.showDialog(getActivity().getSupportFragmentManager());

        // Fire the request with the guest ID as the marketing ID (getGuestId is null safe)
        ValidateEmailAddressRequest request = new ValidateEmailAddressRequest.Builder(this)
                .setEmail(emailText)
                .setMarketingId(AccountStateManager.getGuestId())
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    private void showErrorMessage(String message, int resIcon, int colorTint) {
        View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_with_two_text_view, null);
        TextView tvMessage = (TextView) popupView.findViewById(R.id.popup_message_text_view);
        TextView tvTitle = (TextView) popupView.findViewById(R.id.popup_title_text_view);
        ImageView ivPopup = (ImageView) popupView.findViewById(R.id.popImage);
        tvTitle.setText(message);
        tvMessage.setText("");
        ivPopup.setImageResource(resIcon);
        if (colorTint != -1) {
            ivPopup.setColorFilter(colorTint);
        }
        if (termsErrorPop != null && termsErrorPop.isShowing()) {
            termsErrorPop.dismiss();
        } else {
            termsErrorPop = new Dialog(getActivity());
            termsErrorPop.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            termsErrorPop.setCancelable(true);
            termsErrorPop.setContentView(popupView);
            termsErrorPop.show();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (termsErrorPop != null && termsErrorPop.isShowing()) {
                    termsErrorPop.dismiss();
                }
            }
        }, 3000);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mBtnCreateAccount.getId()) {
            createAccount();
            sendCreateAccountSuccessAnalytics();
        } else if (v.getId() == mEtDob.getId()) {
            DialogUtils.showDatePicker(getActivity(), this);
        } else if (v.getId() == mInfo.getId()) {
            showErrorMessage(mTridionConfig.getDOBToolTip(), R.drawable.info_blue_bigger, -1);
        } else if (v.getId() == mSignIn.getId()) {
            mAccountRegisterResultHandler.onRegisterResult(false);
        }
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                FullScreenLoadingView.dismissDialog();
            }
        });

        // Perform email validation operations if this is a ValidateEmailAddressResponse
        if (networkResponse instanceof ValidateEmailAddressResponse) {
            if (networkResponse.isHttpStatusCodeSuccess()) {
                ValidateEmailAddressResponse emailResponse = (ValidateEmailAddressResponse) networkResponse;
                EmailValidationResult result = emailResponse.getResult();
                // Proceed if this marketing ID matches the one we originally sent, and if all of our fields are valid
                if (result != null && result.getMarketingId() != null && result.getCertainty() != null &&
                    TextUtils.equals(result.getMarketingId(), AccountStateManager.getGuestId())) {

                    // Get the error message and show it, if applicable
                    String errorMessage = EmailErrorUtils.getErrorMessageIfExists(result, getContext());
                    if (errorMessage != null) {
                        showEmailErrorMessage(errorMessage, EmailErrorUtils.isEmailUndeliverable(result));
                        enableSubmitButton(false);
                    }
                    else if (mEmailLocalValid) {
                        tvErrorEmail.setVisibility(View.GONE);
                        // If absolutely no errors at all, then show/activate the create account button
                        enableSubmitButton(true);
                    }
                }
            } else {
                showResponseErrorMessage();
            }
        }
        else if (networkResponse instanceof CreateAccountResponse) {
            if (networkResponse.isHttpStatusCodeSuccess()) {
                Toast.makeText(getContext(), mTridionConfig.getSu2(), Toast.LENGTH_SHORT)
                        .show();
                AccountLoginService.startActionLoginWithUsernamePassword(getContext(),
                        mEmailAddress, mPassword);
                mAccountRegisterResultHandler.onRegisterResult(true);
            }
            else {
                showResponseErrorMessage();
            }
        }
    }

    private void showResponseErrorMessage() {
        Toast.makeText(getContext(), mTridionConfig.getEr20(), android.widget.Toast.LENGTH_LONG)
                .setIconColor(Color.RED)
                .show();
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
                    enableSubmitButton(true);
                }
            };
            EmailErrorUtils.appendIgnoreLinkToText(tvErrorEmail, ignoreOnClickListener);
        }

        showError(tvErrorEmail);
    }

    private void enableSubmitButton(boolean enabled) {
        mBtnCreateAccount.setEnabled(enabled);

        int drawable = enabled ? R.drawable.shape_tooltip_background : R.drawable.tooltip_background_disabled;
        mBtnCreateAccount.setBackgroundResource(drawable);
    }

    private void showError(View v) {
        v.setVisibility(View.VISIBLE);
    }


    private void sendOnPageLoadAnalytics() {
        Map<String, Object> extraData = new HashMap<String, Object>();
        extraData.put(AnalyticsUtils.KEY_LEADTYPE, "Account Creation");
        AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_CRM,
                        AnalyticsUtils.CONTENT_FOCUS_ACCOUNT,
                        null,
                        null,
                        null,
                        null,
                        extraData);
    }

    private void sendOptInCheckedAnalytics() {
        Map<String, Object> extraData = new HashMap<String, Object>();
        extraData.put(AnalyticsUtils.KEY_EVENTS, "event122,event32,event20");
        AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_CRM,
                        AnalyticsUtils.CONTENT_FOCUS_ACCOUNT,
                        null,
                        null,
                        null,
                        null,
                        extraData);
    }

    private void sendCreateAccountSuccessAnalytics() {
        Map<String, Object> extraData = new HashMap<String, Object>();
        extraData.put(AnalyticsUtils.KEY_EVENTS, "event122,event32,event20");
        extraData.put(AnalyticsUtils.KEY_EVENT_NAME, "Form Submission");
        AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_CRM,
                        AnalyticsUtils.CONTENT_FOCUS_ACCOUNT,
                        null,
                        null,
                        null,
                        null,
                        extraData);
    }


}
