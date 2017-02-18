package com.universalstudios.orlandoresort.controller.userinterface.account;

import android.app.ActionBar;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.FullScreenLoadingView;
import com.universalstudios.orlandoresort.controller.userinterface.home.HomeActivity;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkActivity;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.ResetPasswordActivityBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.ResetPasswordRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.ResetPasswordResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.network.response.HttpResponseStatus;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;
import com.universalstudios.orlandoresort.model.state.commerce.CommerceStateManager;
import com.universalstudios.orlandoresort.view.password.PasswordStrengthView;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/15/16.
 * Class: ResetPasswordActivity
 * Class Description: Activity to reset a user's password
 */
public class ResetPasswordActivity extends NetworkActivity implements View.OnClickListener {
    public static final String TAG = "ResetPasswordActivity";
    EditText etPassword;
    private PasswordStrengthView mPasswordStrengthView;
    Button btnSubmit;

    private String mEmail;
    private String requestId;
    private FullScreenLoadingView mLoadingView;
    private TridionConfig mTridionConfig;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTridionConfig = IceTicketUtils.getTridionConfig();
        Intent intent = getIntent();
        if (null != intent) {
            Uri uri = intent.getData();
            if (null != uri) {
                mEmail = uri.getQueryParameter("email");
                requestId = uri.getQueryParameter("requestId");
            }
        }

        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            actionBar.setTitle(mTridionConfig.getResetPasswordPageTitle());
            actionBar.setDisplayShowHomeEnabled(false);
        }

        // If data is incomplete, or commerce is disabled, just finish the activity
        if (null == mEmail || null == requestId || CommerceStateManager.isAppValidForCommerce(true, false)) {
            finish();
        }

        ResetPasswordActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.reset_password_activity);
        binding.setTridion(mTridionConfig);
        etPassword = binding.etResetPassword;
        mPasswordStrengthView = binding.passwordStrengthMeter;
        mPasswordStrengthView.bindPasswordEditText(etPassword);
        btnSubmit = binding.btnResetSubmit;
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnSubmit.getId()) {
            String password = etPassword.getText().toString();

            switch (AccountUtils.isValidPassword(password, mEmail)) {
                case NONE:
                    showLoadingView();
                    ResetPasswordRequest request = new ResetPasswordRequest.Builder(this)
                            .setEmail(mEmail)
                            .setNewPassword(password)
                            .setRequestId(requestId)
                            .build();
                    NetworkUtils.queueNetworkRequest(request);
                    NetworkUtils.startNetworkService();
                    break;

                case REPEATING_CHARACTERS:
                    Toast.makeText(this, mTridionConfig.getEr51(), Toast.LENGTH_LONG)
                            .setIconColor(Color.RED)
                            .show();
                    break;
                case LENGTH:
                    Toast.makeText(this, mTridionConfig.getEr9(), Toast.LENGTH_LONG)
                            .setIconColor(Color.RED)
                            .show();
                    break;
                case SAME_AS_EMAIL:
                    Toast.makeText(this, mTridionConfig.getEr60(), Toast.LENGTH_LONG)
                            .setIconColor(Color.RED)
                            .show();
                    break;
                case ENDS_WITH_SPACE:
                    Toast.makeText(this, mTridionConfig.getEr86(), Toast.LENGTH_LONG)
                            .setIconColor(Color.RED)
                            .show();
                    break;
            }
        }
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        hideLoadingView();
        if (networkResponse instanceof ResetPasswordResponse) {
            //handle response somehow
            if (null != networkResponse.getHttpStatusCode() &&
                    networkResponse.getHttpStatusCode() == HttpResponseStatus.SUCCESS_OK) {
                AccountStateManager.saveUsernamePassword(mEmail, etPassword.getText().toString().trim());
                Toast.makeText(this, mTridionConfig.getSu4(), Toast.LENGTH_SHORT)
                        .show();
                gotoViewProfile();
            } else {
                Toast.makeText(this, mTridionConfig.getEr71(), Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
        }

    }

    private void showLoadingView() {
        if (null == mLoadingView) {
            mLoadingView = FullScreenLoadingView.show(getSupportFragmentManager());
        }
    }

    private void hideLoadingView() {
        if (null != mLoadingView) {
            mLoadingView.dismiss();
            mLoadingView = null;
        }
    }

    private void gotoViewProfile() {
        Intent i = new Intent(this, HomeActivity.class);
        Bundle homeActivityBundle = HomeActivity.newInstanceBundle(HomeActivity.START_PAGE_TYPE_VIEW_PROFILE);
        i.putExtras(homeActivityBundle);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

}
