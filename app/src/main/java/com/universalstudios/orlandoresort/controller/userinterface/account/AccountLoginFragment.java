package com.universalstudios.orlandoresort.controller.userinterface.account;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.link.ClickListener;
import com.universalstudios.orlandoresort.controller.userinterface.link.ClickableTextHelper;
import com.universalstudios.orlandoresort.controller.userinterface.utils.AndroidUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.FragmentAccountSignInBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.state.account.AccountLoginService;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;
import com.universalstudios.orlandoresort.model.state.account.LoginBroadcastReceiver;
import com.universalstudios.orlandoresort.model.state.account.LoginResultIntentFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/8/16.
 * Class: AccountLoginFragment
 * Class Description: Fragment that allows the user to login to their UO Account
 */
public class AccountLoginFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {
    public static final String TAG = AccountLoginFragment.class.getSimpleName();

    private static final String KEY_ARG_LOGIN_DESTINATION_TYPE = "KEY_ARG_LOGIN_DESTINATION_TYPE";
    private static final int ACTIVITY_REGISTER_PROFILE = 1000;

    private TextView tvForgotPassword;
    private TextView tvCreateAccount;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvErrorEmail;
    private TextView tvErrorPassword;
    private Button btnSignIn;
    private CheckBox cbRememberMe;
    private LoginDestinationType mLoginDestinationType;
    private TridionConfig mTridionConfig;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private AccountLoginResultHandler mAccountLoginResultHandler;
    private FragmentAccountSignInBinding mBinding;

    public enum LoginDestinationType {
        LOGIN_DESTINATION_USER_PROFILE,
        LOGIN_DESTINATION_MY_WALLET
    }

    private LoginBroadcastReceiver mLoginBroadcastReceiver;

    private LoginBroadcastReceiver.LoginResultCallback mLoginResultCallback = new LoginBroadcastReceiver.LoginResultCallback() {
        @Override
        public void onLoginResult(@LoginBroadcastReceiver.LoginResult int result) {
            hideLoading();
            switch (result) {
                case LoginBroadcastReceiver.SUCCESS_REGISTERED:
                    AccountStateManager.setRememberMe(cbRememberMe.isChecked());
                    mAccountLoginResultHandler.onLoginResult(true);
                    break;
                case LoginBroadcastReceiver.ERROR_ACCOUNT_LOCKED:
                    showErrorMessage(mTridionConfig.getEr28());
                    break;
                case LoginBroadcastReceiver.ERROR_LOGIN_FAILED:
                    showErrorMessage(mTridionConfig.getEr25());
                    break;
                case LoginBroadcastReceiver.ERROR_UNKNOWN:
                    showErrorMessage(mTridionConfig.getEr24());
                    break;
                case LoginBroadcastReceiver.SUCCESS_UNREGISTERED:
                case LoginBroadcastReceiver.SUCCESS_LOGOUT:
                default:
            }
        }
    };

    /**
     * Factory method to create a new instance of AccountLoginFragment
     *
     * @return new instance of AccountLoginFragment
     */
    public static AccountLoginFragment newInstance() {
        // Create a new fragment instance
        AccountLoginFragment fragment = new AccountLoginFragment();

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
        if (parentFragment != null && parentFragment instanceof AccountLoginResultHandler) {
            mAccountLoginResultHandler = (AccountLoginResultHandler) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (activity != null && activity instanceof AccountLoginResultHandler) {
            mAccountLoginResultHandler = (AccountLoginResultHandler) activity;
        }
        // If neither implements the interface, log a warning
        else if (mAccountLoginResultHandler == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement AccountLoginResultHandler");
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
            mLoginDestinationType = LoginDestinationType.LOGIN_DESTINATION_USER_PROFILE;
        }
        // Otherwise, set incoming parameters
        else {
            mLoginDestinationType = (LoginDestinationType) args.getSerializable(KEY_ARG_LOGIN_DESTINATION_TYPE);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            sendOnPageLoadAnalytics();
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {
        }

        mTridionConfig = IceTicketUtils.getTridionConfig();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAccountSignInBinding.inflate(inflater, container, false);
        mBinding.setTridion(mTridionConfig);
        View layout = mBinding.getRoot();
        tvForgotPassword = mBinding.tvForgotPassword;
        tvCreateAccount = mBinding.tvCreateAccount;
        etEmail = mBinding.etEmail;
        etPassword = mBinding.etPassword;
        tvErrorEmail = mBinding.tvErrorEmail;
        tvErrorPassword = mBinding.tvErrorPassword;
        btnSignIn = mBinding.btnSignIn;
        cbRememberMe = mBinding.cbRememberMe;

        btnSignIn.setOnClickListener(this);

        etEmail.setOnFocusChangeListener(this);
        etPassword.setOnClickListener(this);

        keepLoggedIn();

        setCreateAccountLink();
        setForgotPasswordLink();
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
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
    }

    private void showErrorMessage(final String errorMessage) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), errorMessage, android.widget.Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
        });
    }

    private void keepLoggedIn(){

        String userName = AccountStateManager.getUsername();
        String userNamePass = AccountStateManager.getPassword();
        Boolean rememberMeCheck = AccountStateManager.getRememberMe();

        if(userNamePass !=null && userName != null && rememberMeCheck){
            etEmail.setText(userName);
            etPassword.setText(userNamePass);
            cbRememberMe.setChecked(true);
        }
        else{
            etEmail.setText("");
            etPassword.setText("");
            cbRememberMe.setChecked(false);
        }

    }

    /**
     * Sets the Create an Account text as a link
     */
    private void setCreateAccountLink() {
        SpannableString text = ClickableTextHelper.createEmbeddedSpannable(mTridionConfig.getCreateAccountLinkLabel(),
                mTridionConfig.getCreateAccountLinkLabel(), R.color.ticket_text_color, true, new ClickListener() {
                    @Override
                    public void onLinkClicked() {
                        onClick(tvCreateAccount);
                        sendForgotPasswordAnalytics();
                    }
                });
        tvCreateAccount.setText(text);
        tvCreateAccount.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Sets the Forgot password text as a link
     */
    private void setForgotPasswordLink() {
        SpannableString text = ClickableTextHelper.createEmbeddedSpannable(mTridionConfig.getForgotPasswordLabel(),
                mTridionConfig.getForgotPasswordLabel(), R.color.ticket_text_color, true, new ClickListener() {
                    @Override
                    public void onLinkClicked() {
                        onClick(tvForgotPassword);
                    }
                });
        tvForgotPassword.setText(text);
        tvForgotPassword.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Log the user in to their account
     */
    private void logUserIn() {
        AndroidUtils.forceHideKeyboard(getActivity(), getActivity().getCurrentFocus());
        if (validateField(etPassword)) {
            showLoading();
            AccountLoginService.startActionLoginWithUsernamePassword(getContext(),
                    etEmail.getText().toString(),
                    etPassword.getText().toString());

        }
    }

    /**
     * Validates all the fields from the current field up and shows the error messages
     * as necessary
     *
     * @param editText View to validate (and all the views above it)
     * @return true if all fields from the designated one up are valid
     */
    private boolean validateField(EditText editText) {
        boolean valid = true;
        hideAllErrors();
        switch (editText.getId()) {
            case R.id.etPassword:
                switch (AccountUtils.isValidPassword(etPassword.getText().toString(), "")) {
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
                }
                //Intentional dropthrough
            case R.id.etEmail:
                String text = etEmail.getText().toString().trim();
                if (!AccountUtils.isValidEmail(text)) {
                    valid = false;
                    showError(tvErrorEmail);
                }
        }

        return valid;
    }

    /**
     * Sets the error View's visibility to Visible
     *
     * @param v View to show
     */
    private void showError(View v) {
        v.setVisibility(View.VISIBLE);
    }

    /**
     * hides all the error messages
     */
    private void hideAllErrors() {
        tvErrorPassword.setVisibility(View.GONE);
        tvErrorEmail.setVisibility(View.GONE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v instanceof EditText && !hasFocus) {
            validateField((EditText) v);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == tvForgotPassword.getId()) {
            // If this fragment ever needs to return back to this screen or another upon
            // completion then startActivityForResult(...) should be utilized
            startActivity(ForgotPasswordActivity.newInstanceIntent(getContext()));
        } else if (v.getId() == tvCreateAccount.getId()) {
            startActivityForResult(AccountRegisterActivity.newInstanceIntent(getContext()),
                    ACTIVITY_REGISTER_PROFILE);
        } else if (v.getId() == btnSignIn.getId()) {
            logUserIn();
            sendSignInClickedAnalytics();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_REGISTER_PROFILE:
                if (resultCode == Activity.RESULT_OK) {
                    // Registration was successful, finish
                    mAccountLoginResultHandler.onLoginResult(true);
                }
                break;
            default:
                Log.w(TAG, "Fragment received unexpected result " +
                           "from finished activity");
                break;
        }
    }

    private void showLoading() {
        mBinding.loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        mBinding.loadingView.setVisibility(View.GONE);
    }


    private void sendOnPageLoadAnalytics() {
            Map<String, Object> extraData = new HashMap<String,Object>();
            AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_CRM,
                            AnalyticsUtils.CONTENT_FOCUS_ACCOUNT,
                            null,
                            AnalyticsUtils.UO_PAGE_IDENTIFIER_SIGN_IN,
                            null,
                            null,
                            extraData);
    }

    private void sendSignInClickedAnalytics() {
            Map<String, Object> extraData = new HashMap<String,Object>();
            extraData.put(AnalyticsUtils.KEY_EVENT_NAME, "Form Submission,");
            extraData.put(AnalyticsUtils.KEY_EVENTS, "event122");
            AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_CRM,
                            AnalyticsUtils.CONTENT_FOCUS_ACCOUNT,
                            null,
                            AnalyticsUtils.UO_PAGE_IDENTIFIER_SIGN_IN,
                            null,
                            null,
                            extraData);
    }

    private void sendForgotPasswordAnalytics() {
            Map<String, Object> extraData = new HashMap<String,Object>();
            extraData.put(AnalyticsUtils.KEY_EVENT_NAME, "Form Submission,");
            extraData.put(AnalyticsUtils.KEY_EVENTS, "event147");
            AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_CRM,
                            AnalyticsUtils.CONTENT_FOCUS_ACCOUNT,
                            null,
                            AnalyticsUtils.UO_PAGE_IDENTIFIER_FORGOT_PASSWORD,
                            null,
                            null,
                            extraData);
    }

}
