package com.universalstudios.orlandoresort.controller.userinterface.account;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.FragmentAccountUpdatePasswordBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.UpdatePasswordRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.UpdatePasswordResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.network.response.HttpResponseStatus;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;
import com.universalstudios.orlandoresort.view.password.PasswordStrengthView;

import org.parceler.Parcels;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/22/16.
 * Class: AccountUpdatePasswordFragment
 * Class Description: Fragment that allows the user to update their password
 */
public class AccountUpdatePasswordFragment extends NetworkFragment implements View.OnClickListener, View.OnFocusChangeListener {
    public static final String TAG = AccountUpdatePasswordFragment.class.getSimpleName();
    private static final String KEY_ARG_GUEST_PROFILE = "KEY_ARG_GUEST_PROFILE";

    private EditText etCurrentPassword;
    private EditText etNewPassword;
    private PasswordStrengthView mPasswordStrengthView;
    private Button btnCancel;
    private Button btnUpdate;
    private TextView tvErrorCurrentPassword;
    private TextView tvErrorNewPassword;

    private GuestProfile guestProfile;
    private TridionConfig mTridionConfig;
    private AccountUpdateProvider mAccountUpdateProvider;
    private FragmentAccountUpdatePasswordBinding mBinding;

    /**
     * Factory method to create a new instance of AccountUpdatePasswordFragment
     *
     * @return new instance of AccountUpdatePasswordFragment
     */
    public static AccountUpdatePasswordFragment newInstance(GuestProfile guestProfile) {
        AccountUpdatePasswordFragment fragment = new AccountUpdatePasswordFragment();
        Bundle b = new Bundle();
        b.putParcelable(KEY_ARG_GUEST_PROFILE, Parcels.wrap(guestProfile));
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
        mBinding = FragmentAccountUpdatePasswordBinding.inflate(inflater, container, false);
        mBinding.setTridion(mTridionConfig);
        View layout = mBinding.getRoot();
        btnCancel = mBinding.btnCancel;
        btnUpdate = mBinding.btnUpdate;

        etCurrentPassword = mBinding.etCurrentPassword;
        etNewPassword = mBinding.etNewPassword;
        mPasswordStrengthView = mBinding.passwordStrengthMeter;
        tvErrorCurrentPassword = mBinding.tvErrorCurrentPassword;
        tvErrorNewPassword = mBinding.tvErrorNewPassword;

        etCurrentPassword.setOnFocusChangeListener(this);
        etNewPassword.setOnFocusChangeListener(this);
        mPasswordStrengthView.bindPasswordEditText(etNewPassword);

        btnUpdate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStart");
        }
    }

    private boolean validateFields(EditText editText, boolean callToAction) {
        boolean valid = true;
        hideAllErrors();
        String text;
        switch (editText.getId()) {
            case R.id.etNewPassword:
                text = etNewPassword.getText().toString();
                switch (AccountUtils.isValidPassword(text, guestProfile.getContact().getEmail())) {
                    case REPEATING_CHARACTERS:
                        tvErrorNewPassword.setText(mTridionConfig.getEr51());
                        showError(tvErrorNewPassword);
                        valid = false;
                        break;
                    case LENGTH:
                        tvErrorNewPassword.setText(mTridionConfig.getEr9());
                        showError(tvErrorNewPassword);
                        valid = false;
                        break;
                    case SAME_AS_EMAIL:
                        tvErrorNewPassword.setText(mTridionConfig.getEr60());
                        showError(tvErrorNewPassword);
                        valid = false;
                        break;
                    case EMPTY:
                        if (callToAction) {
                            tvErrorNewPassword.setText(mTridionConfig.getEr26());
                            showError(tvErrorNewPassword);
                            valid = false;
                        }
                        break;
                    case ENDS_WITH_SPACE:
                        tvErrorNewPassword.setText(mTridionConfig.getEr86());
                        showError(tvErrorNewPassword);
                        valid = false;
                        break;
                }
            case R.id.etCurrentPassword:
                text = etCurrentPassword.getText().toString();
                switch (AccountUtils.isValidPassword(text, guestProfile.getContact().getEmail())) {
                    case REPEATING_CHARACTERS:
                        tvErrorCurrentPassword.setText(mTridionConfig.getEr51());
                        showError(tvErrorCurrentPassword);
                        valid = false;
                        break;
                    case LENGTH:
                        tvErrorCurrentPassword.setText(mTridionConfig.getEr9());
                        showError(tvErrorCurrentPassword);
                        valid = false;
                        break;
                    case SAME_AS_EMAIL:
                        tvErrorCurrentPassword.setText(mTridionConfig.getEr60());
                        showError(tvErrorCurrentPassword);
                        valid = false;
                        break;
                    case EMPTY:
                        if (callToAction) {
                            tvErrorCurrentPassword.setText(mTridionConfig.getEr26());
                            showError(tvErrorCurrentPassword);
                            valid = false;
                        }
                        break;
                    case ENDS_WITH_SPACE:
                        tvErrorCurrentPassword.setText(mTridionConfig.getEr86());
                        showError(tvErrorCurrentPassword);
                        valid = false;
                        break;
                }
        }
        return valid;
    }

    private void hideAllErrors() {
        tvErrorCurrentPassword.setVisibility(View.GONE);
        tvErrorNewPassword.setVisibility(View.GONE);
    }

    private void showError(View v) {
        v.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus && v instanceof EditText) {
            validateFields((EditText) v, false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnCancel.getId()) {
            mAccountUpdateProvider.updateAccount(false);
        } else if (v.getId() == btnUpdate.getId()) {
            if (validateFields(etNewPassword, true)) {
                showLoading();
                String oldPassword = etCurrentPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();

                UpdatePasswordRequest request = new UpdatePasswordRequest.Builder(this)
                        .setOldPassword(oldPassword)
                        .setNewPassword(newPassword)
                        .build();
                NetworkUtils.queueNetworkRequest(request);
                NetworkUtils.startNetworkService();
            }
        }
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        hideLoading();
        if (networkResponse == null) {
            return;
        }

        if (networkResponse instanceof UpdatePasswordResponse) {
            UpdatePasswordResponse response = (UpdatePasswordResponse) networkResponse;
            if (response.getStatusCode() == HttpResponseStatus.SUCCESS_OK) {
                if (AccountStateManager.isUserLoggedIn()) {
                    AccountStateManager.saveUsernamePassword(
                            AccountStateManager.getUsername(),
                            etNewPassword.getText().toString().trim());
                }
                mAccountUpdateProvider.updateAccount(true);
                Toast.makeText(getContext(), mTridionConfig.getSu4(), Toast.LENGTH_SHORT)
                        .show();
            }
            else if (response.getStatusCode() == HttpResponseStatus.ERROR_FORBIDDEN) {
                Toast.makeText(getContext(), mTridionConfig.getEr8(), Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
            else {
                Toast.makeText(getContext(), mTridionConfig.getEr71(), Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
        }
    }

    private void showLoading() {
        mBinding.loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        mBinding.loadingView.setVisibility(View.GONE);
    }
}
