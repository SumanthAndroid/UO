package com.universalstudios.orlandoresort.controller.userinterface.account;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.FullScreenLoadingView;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.FragmentAccountUpdatePersonalInformationBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.UpdatePersonalInfoRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.UpdatePersonalInfoResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/22/16.
 * Class: AccountUpdatePersonalInfoFragment
 * Class Description: Fragment for updating the user's personal information
 */
public class AccountUpdatePersonalInfoFragment extends NetworkFragment implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, View.OnFocusChangeListener {
    public static final String TAG = AccountUpdatePersonalInfoFragment.class.getSimpleName();
    private static final String KEY_ARG_GUEST_PROFILE = "KEY_ARG_GUEST_PROFILE";

    private TridionConfig mTridionConfig;
    private Spinner spinnerPrefix;
    private EditText etFirstname, etLastname, etSuffix, etBirthdate;
    private ImageView mInfo;
    private TextView tvErrorDob, tvErrorLastname, tvErrorFirstname, tvErrorSuffix;
    private Button btnUpdate, btnCancel;

    private GuestProfile guestProfile;
    private ArrayAdapter<String> prefixAdapter;
    private AccountUpdateProvider mAccountUpdateProvider;

    private FullScreenLoadingView mLoadingView;

    public static AccountUpdatePersonalInfoFragment newInstance(GuestProfile guestProfile) {
        AccountUpdatePersonalInfoFragment fragment = new AccountUpdatePersonalInfoFragment();
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
        ArrayList<String> prefixes = mTridionConfig.getPrefixValues();

        prefixAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, prefixes);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentAccountUpdatePersonalInformationBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_account_update_personal_information, container, false);
        binding.setTridion(mTridionConfig);
        View layout = binding.getRoot();

        etFirstname = binding.etFirstName;
        etLastname = binding.etLastName;
        etSuffix = binding.etSuffix;
        etBirthdate = binding.etDateOfBirth;

        tvErrorDob = binding.tvErrorDateofBirth;
        tvErrorFirstname = binding.tvFirstnameError;
        tvErrorLastname = binding.tvErrorLastname;
        tvErrorSuffix = binding.tvErrorSuffix;
        spinnerPrefix = binding.spinnerPrefix;

        btnUpdate = binding.btnUpdate;
        btnCancel = binding.btnCancel;
        btnCancel.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

        etFirstname.setOnFocusChangeListener(this);
        etBirthdate.setOnClickListener(this);
        etLastname.setOnFocusChangeListener(this);
        etSuffix.setOnFocusChangeListener(this);

        mInfo = binding.info;
        mInfo.setOnClickListener(this);
        spinnerPrefix.setAdapter(prefixAdapter);

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
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        hideLoadingView();
        if (networkResponse instanceof UpdatePersonalInfoResponse) {
            handleUpdatePersonalInfoResponse((UpdatePersonalInfoResponse) networkResponse);
        }
    }

    private void handleUpdatePersonalInfoResponse(@NonNull UpdatePersonalInfoResponse networkResponse) {
        if (networkResponse.isHttpStatusCodeSuccess()) {
            mAccountUpdateProvider.updateAccount(true);
            Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getSu9(), Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getContext(), mTridionConfig.getEr99(), Toast.LENGTH_LONG)
                    .setIconColor(Color.RED)
                    .show();
        }
    }

    private void populateFields() {
        etFirstname.setText(guestProfile.getContact().getFirstName());
        etLastname.setText(guestProfile.getContact().getLastName());
        etBirthdate.setText(guestProfile.getBirthDate());
        setSpinnerFromGuestProfile();
    }

    private void setSpinnerFromGuestProfile() {
        String prefix = guestProfile.getContact().getNamePrefix();
        if (TextUtils.isEmpty(prefix)) {
            prefix = mTridionConfig.getNoneLabel();
        }
        spinnerPrefix.setSelection(prefixAdapter.getPosition(prefix));
    }

    /**
     * Shows the Date of Birth date picker dialog
     */
    public void showDatePicker() {
        Calendar today = Calendar.getInstance();
        Calendar minCal = Calendar.getInstance();
        minCal.set(Calendar.YEAR, today.get(Calendar.YEAR) - 13);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.AlertDialogStyle_DatePicker, this, minCal.get(Calendar.YEAR),
                minCal.get(Calendar.MONTH), minCal.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(minCal.getTimeInMillis());

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (AccountUtils.isWithinDateRange(year, monthOfYear, dayOfMonth)) {
            String dateString = String.format("%02d/%02d/%04d", monthOfYear+1, dayOfMonth, year);
            etBirthdate.setText(dateString);
            tvErrorDob.setVisibility(View.GONE);
        } else {
            showError(tvErrorDob, mTridionConfig.getEr4());
        }
    }

    private boolean validateField(EditText et, boolean callToAction) {
        if (null == et) {
            return false;
        }

        boolean valid = true;
        hideAllErrors();
        String text;

        switch (et.getId()) {
            case R.id.etDateOfBirth:
                if (TextUtils.isEmpty(etBirthdate.getText().toString()) && callToAction) {
                    valid = false;
                    showError(tvErrorDob, mTridionConfig.getEr26());
                }
            case R.id.etSuffix:
                text = etSuffix.getText().toString().trim();
                if (!AccountUtils.isValidSuffix(text)) {
                    valid = false;
                    showError(tvErrorSuffix, mTridionConfig.getEr3());
                }
            case R.id.etLastName:
                text = etLastname.getText().toString();
                if (TextUtils.isEmpty(text) && callToAction) {
                    showError(tvErrorLastname, mTridionConfig.getEr26());
                    valid = false;
                } else if (!AccountUtils.isValidName(text)) {
                    showError(tvErrorLastname, mTridionConfig.getEr2());
                    valid = false;
                }
            case R.id.etFirstName:
                text = etFirstname.getText().toString();
                if (TextUtils.isEmpty(text) && callToAction) {
                    showError(tvErrorFirstname, mTridionConfig.getEr26());
                    valid = false;
                } else if (!AccountUtils.isValidName(text)) {
                    showError(tvErrorFirstname, mTridionConfig.getEr1());
                    valid = false;
                }
        }

        return valid;
    }

    private void showError(TextView v, String errorText) {
        v.setText(errorText);
        v.setVisibility(View.VISIBLE);
    }

    /**
     * hides all the error messages
     */
    private void hideAllErrors() {
        tvErrorSuffix.setVisibility(View.GONE);
        tvErrorFirstname.setVisibility(View.GONE);
        tvErrorLastname.setVisibility(View.GONE);
    }

    /**
     * Make the request to update the user's personal info
     */
    private void updatePersonalInfo() {
        showLoadingView();

        UpdatePersonalInfoRequest request = new UpdatePersonalInfoRequest.Builder(AccountUpdatePersonalInfoFragment.this)
                .setNamePrefix(spinnerPrefix.getSelectedItem().toString())
                .setFirstname(etFirstname.getText().toString())
                .setLastname(etLastname.getText().toString())
                .setNameSuffix(etSuffix.getText().toString())
                .setEmail(guestProfile.getContact().getEmail())
                .setPhone(guestProfile.getContact().getMobilePhone())
                .setBirthDate(etBirthdate.getText().toString())
                .setReceiveUpdates(guestProfile.getPreference().getReceiveUpdates())
                .setTermsOfServices(guestProfile.getPreference().getTermsAndServices())
                .build();

        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v instanceof EditText && !hasFocus) {
            validateField((EditText) v, false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == etBirthdate.getId()) {
            showDatePicker();
        } else if (v.getId() == btnUpdate.getId()) {
            if (validateField(etBirthdate, true)) {
                updatePersonalInfo();
            }
        } else if (v.getId() == btnCancel.getId()) {
            mAccountUpdateProvider.updateAccount(false);
        } else if (v.getId() == mInfo.getId()) {
            Toast.makeText(getContext(), mTridionConfig.getDOBToolTip(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void showLoadingView() {
        if (null == mLoadingView) {
            mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
        }
    }

    private void hideLoadingView() {
        if (null != mLoadingView) {
            mLoadingView.dismiss();
            mLoadingView = null;
        }
    }

}
