package com.universalstudios.orlandoresort.controller.userinterface.feedback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.DialogUtils;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.feedback.GuestFeedbackErrorState;
import com.universalstudios.orlandoresort.model.network.domain.feedback.SendGuestFeedbackRequest;
import com.universalstudios.orlandoresort.model.network.domain.feedback.SendGuestFeedbackResponse;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.utils.SimpleSpinnerAdapter;
import com.universalstudios.orlandoresort.view.fonts.Button;
import com.universalstudios.orlandoresort.view.fonts.EditText;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class GuestFeedbackFragment extends NetworkFragment implements OnClickListener, OnItemSelectedListener {
    private static final String TAG = GuestFeedbackFragment.class.getSimpleName();

    private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";
    private static final String KEY_STATE_IS_MAKING_FEEDBACK_CALL = "KEY_STATE_IS_MAKING_FEEDBACK_CALL";

    private int mActionBarTitleResId;
    private EditText mSubjectEdit;
    private Spinner mRatingSpinner;
    private EditText mMessageEdit;
    private EditText mNameEdit;
    private EditText mEmailEdit;
    private EditText mPhoneEdit;
    private TextView mSubjectErrorText;
    private TextView mRatingErrorText;
    private TextView mEmailErrorText;
    private TextView mMessageErrorText;
    private TextView mNameErrorText;
    private TextView mPhoneErrorText;
    private Button mSubmitButton;
    private ViewGroup mSubmitProgressView;
    private ArrayList<String> mRatingOptionsList;
    private SimpleSpinnerAdapter<String> mRatingAdapter;
    private boolean mIsMakingFeedbackCall;

    public GuestFeedbackFragment() {
    }

    public static GuestFeedbackFragment newInstance(int actionBarTitleResId) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance: actionBarTitleResId = " + actionBarTitleResId);
        }

        // Create a new fragment instance
        GuestFeedbackFragment fragment = new GuestFeedbackFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        // Add parameters to the argument bundle
        args.putInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID, actionBarTitleResId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAttach");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        // Default parameters
        Bundle args = getArguments();
        if (args == null) {
            mActionBarTitleResId = -1;
        }
        // Otherwise, set incoming parameters
        else {
            mActionBarTitleResId = args.getInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            // Track the page view
            AnalyticsUtils.trackPageView(
                    null,
                    AnalyticsUtils.CONTENT_FOCUS_GUEST_FEEDBACK,
                    null, null,
                    AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
                    null, null);

            mIsMakingFeedbackCall = false;
        }
        // Otherwise, restore state
        else {
            mIsMakingFeedbackCall = savedInstanceState.getBoolean(KEY_STATE_IS_MAKING_FEEDBACK_CALL);
        }

        Activity parentActivity = getActivity();
        if (parentActivity != null) {
            parentActivity.getActionBar().setTitle(mActionBarTitleResId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        View fragmentView = inflater.inflate(R.layout.fragment_guest_feedback, container, false);

        mSubjectEdit = (EditText) fragmentView.findViewById(R.id.fragment_guest_feedback_subject);
        mRatingSpinner = (Spinner) fragmentView.findViewById(R.id.fragment_guest_feedback_rating);
        mMessageEdit = (EditText) fragmentView.findViewById(R.id.fragment_guest_feedback_message);
        mNameEdit = (EditText) fragmentView.findViewById(R.id.fragment_guest_feedback_name);
        mEmailEdit = (EditText) fragmentView.findViewById(R.id.fragment_guest_feedback_email);
        mPhoneEdit = (EditText) fragmentView.findViewById(R.id.fragment_guest_feedback_phone);
        mSubmitButton = (Button) fragmentView.findViewById(R.id.fragment_guest_feedback_submit);
        mSubmitProgressView = (FrameLayout) fragmentView.findViewById(R.id.fragment_guest_feedback_sending_view);
        mSubjectErrorText = (TextView) fragmentView.findViewById(R.id.fragment_guest_feedback_subject_error);
        mRatingErrorText = (TextView) fragmentView.findViewById(R.id.fragment_guest_feedback_rating_error);
        mMessageErrorText = (TextView) fragmentView.findViewById(R.id.fragment_guest_feedback_message_error);
        mEmailErrorText = (TextView) fragmentView.findViewById(R.id.fragment_guest_feedback_email_error);
        mNameErrorText = (TextView) fragmentView.findViewById(R.id.fragment_guest_feedback_name_error);
        mPhoneErrorText = (TextView) fragmentView.findViewById(R.id.fragment_guest_feedback_phone_error);

        // Get ratings options
        String[] ratingOptions = getContext().getResources().getStringArray(R.array.guest_feedback_form_rating_options);
        mRatingOptionsList = new ArrayList<>(Arrays.asList(ratingOptions));
        mRatingOptionsList.add(0, "");
        mRatingAdapter = new SimpleSpinnerAdapter<String>(getContext(), mRatingOptionsList) {
            @Override
            public String getItemText(String object) {
                if (object != null) {
                    return object;
                }
                return "";
            }
        };

        // Bind listeners to views
        mRatingSpinner.setAdapter(mRatingAdapter);
        mRatingSpinner.setOnItemSelectedListener(this);
        mSubmitButton.setOnClickListener(this);
        mSubjectEdit.addTextChangedListener(new ErrorTextWatcher(mSubjectErrorText));
        mMessageEdit.addTextChangedListener(new ErrorTextWatcher(mMessageErrorText));
        mEmailEdit.addTextChangedListener(new ErrorTextWatcher(mEmailErrorText));
        mNameEdit.addTextChangedListener(new ErrorTextWatcher(mNameErrorText));
        mPhoneEdit.addTextChangedListener(new ErrorTextWatcher(mPhoneErrorText));

        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onActivityCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewStateRestored: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onSaveInstanceState");
        }

        outState.putBoolean(KEY_STATE_IS_MAKING_FEEDBACK_CALL, mIsMakingFeedbackCall);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_guest_feedback_submit:
                if (!mIsMakingFeedbackCall) {
                    // Close the keyboard
                    UserInterfaceUtils.closeKeyboard(mSubjectEdit);

                    // Trim the fields to remove leading/trailing whitespace before validating
                    trimFields();

                    // Send the feedback to the service tier
                    sendFeedback();

                    // Update the loading UI
                    mIsMakingFeedbackCall = true;
                    updateLoadingState();
                }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedRating = mRatingAdapter.getItem(mRatingSpinner.getSelectedItemPosition());
        if (!TextUtils.isEmpty(selectedRating)) {
            mRatingErrorText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "handleNetworkResponse");
        }

        if (networkResponse instanceof SendGuestFeedbackResponse) {
            SendGuestFeedbackResponse response = (SendGuestFeedbackResponse) networkResponse;

            if (response.isHttpStatusCodeSuccess()) {
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "handleNetworkResponse: SendGuestFeedbackResponse = success");
                }
                // Show success dialog
                GuestFeedbackSuccessDialogFragment dialogFragment = new GuestFeedbackSuccessDialogFragment();
                DialogUtils.showDialogFragment(getChildFragmentManager(), dialogFragment);

                // Tag the event
                String rating = mRatingSpinner.getSelectedItem().toString().trim();
                Map<String, Object> extraData = new AnalyticsUtils.Builder()
                        .setEnvironmentVar(82, rating)
                        .setProperty(68, rating)
                        .build();
                AnalyticsUtils.trackEvent(null, null,
                        AnalyticsUtils.EVENT_NUM_GUEST_FEEDBACK_SUCCESS, extraData);
            } else {
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "handleNetworkResponse: SendGuestFeedbackResponse = failure");
                }

                // Show any validation errors from the services
                boolean inlineErrorsShown = showInlineErrors(response.getErrorState());
                inlineErrorsShown = false;

                // If they couldn't be parsed or there are none to show, show a generic error
                if (!inlineErrorsShown) {
                    // Show error dialog
                    GuestFeedbackErrorDialogFragment dialogFragment = new GuestFeedbackErrorDialogFragment();
                    DialogUtils.showDialogFragment(getChildFragmentManager(), dialogFragment);
                }
            }

            // Update the loading UI
            mIsMakingFeedbackCall = false;
            updateLoadingState();
        }
    }

    private void trimFields() {
        mSubjectEdit.setText(mSubjectEdit.getText().toString().trim());
        mMessageEdit.setText(mMessageEdit.getText().toString().trim());
        mNameEdit.setText(mNameEdit.getText().toString().trim());
        mEmailEdit.setText(mEmailEdit.getText().toString().trim());
        mPhoneEdit.setText(mPhoneEdit.getText().toString().trim());
    }

    private boolean showInlineErrors(GuestFeedbackErrorState errorState) {
        boolean errorShown = false;

        if (errorState != null) {
            String subjectErrors = convertErrorListToString(errorState.getSubjectErrors());
            if (!TextUtils.isEmpty(subjectErrors)) {
                mSubjectErrorText.setText(subjectErrors);
                mSubjectErrorText.setVisibility(View.VISIBLE);
                errorShown = true;
            }

            String ratingErrors = convertErrorListToString(errorState.getRatingErrors());
            if (!TextUtils.isEmpty(ratingErrors)) {
                mRatingErrorText.setText(ratingErrors);
                mRatingErrorText.setVisibility(View.VISIBLE);
                errorShown = true;
            }

            String messageErrors = convertErrorListToString(errorState.getMessageErrors());
            if (!TextUtils.isEmpty(messageErrors)) {
                mMessageErrorText.setText(messageErrors);
                mMessageErrorText.setVisibility(View.VISIBLE);
                errorShown = true;
            }

            String nameErrors = convertErrorListToString(errorState.getNameErrors());
            if (!TextUtils.isEmpty(nameErrors)) {
                mNameErrorText.setText(nameErrors);
                mNameErrorText.setVisibility(View.VISIBLE);
                errorShown = true;
            }

            String emailErrors = convertErrorListToString(errorState.getEmailErrors());
            if (!TextUtils.isEmpty(emailErrors)) {
                mEmailErrorText.setText(emailErrors);
                mEmailErrorText.setVisibility(View.VISIBLE);
                errorShown = true;
            }

            String phoneErrors = convertErrorListToString(errorState.getPhoneErrors());
            if (!TextUtils.isEmpty(phoneErrors)) {
                mPhoneErrorText.setText(phoneErrors);
                mPhoneErrorText.setVisibility(View.VISIBLE);
                errorShown = true;
            }
        }
        return errorShown;
    }

    private static String convertErrorListToString(List<String> errorList) {
        String errorString = "";
        if (errorList != null) {
            for (String error : errorList) {
                if (!TextUtils.isEmpty(error)) {
                    errorString += error + "\n";
                }
            }
            errorString = errorString.trim();
        }
        return errorString;
    }

    /**
     *
     */
    private void sendFeedback() {
        SendGuestFeedbackRequest sendGuestFeedbackRequest = new SendGuestFeedbackRequest.Builder(this)
                .setSubject(mSubjectEdit.getText().toString().trim())
                .setRating(mRatingSpinner.getSelectedItem().toString().trim())
                .setMessage(mMessageEdit.getText().toString().trim())
                .setName(mNameEdit.getText().toString().trim())
                .setEmail(mEmailEdit.getText().toString().trim())
                .setPhone(mPhoneEdit.getText().toString().trim())
                .build();
        NetworkUtils.queueNetworkRequest(sendGuestFeedbackRequest);
        NetworkUtils.startNetworkService();
    }

    private void onUserAcknowledgeSuccess() {
        Activity parentActivity = getActivity();
        if (parentActivity != null) {
            parentActivity.finish();
        }
    }

    private void onUserAcknowledgeError() {
        // Nothing to do, let the user decide if they want to resubmit
    }

    public void updateLoadingState() {
        mSubmitProgressView.setVisibility(mIsMakingFeedbackCall ? View.VISIBLE : View.GONE);
    }

    public static class GuestFeedbackSuccessDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
        private static final String TAG = GuestFeedbackSuccessDialogFragment.class.getSimpleName();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setMessage(R.string.guest_feedback_submit_dialog_success_message);
            alertDialogBuilder.setPositiveButton(R.string.guest_feedback_submit_dialog_positive_button, this);

            Dialog dialog = alertDialogBuilder.create();
            dialog.setCanceledOnTouchOutside(true);
            return dialog;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Fragment parentFragment = getParentFragment();
                    if (parentFragment != null && parentFragment instanceof GuestFeedbackFragment) {
                        GuestFeedbackFragment guestFeedbackFragment = (GuestFeedbackFragment) parentFragment;
                        guestFeedbackFragment.onUserAcknowledgeSuccess();
                    }
                    break;
            }
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);

            // Simulate a cancel click
            onClick(dialog, DialogInterface.BUTTON_POSITIVE);
        }
    }

    public static class GuestFeedbackErrorDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
        private static final String TAG = GuestFeedbackErrorDialogFragment.class.getSimpleName();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setMessage(R.string.guest_feedback_submit_dialog_error_message);
            alertDialogBuilder.setPositiveButton(R.string.guest_feedback_submit_dialog_positive_button, this);

            Dialog dialog = alertDialogBuilder.create();
            dialog.setCanceledOnTouchOutside(true);
            return dialog;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Fragment parentFragment = getParentFragment();
                    if (parentFragment != null && parentFragment instanceof GuestFeedbackFragment) {
                        GuestFeedbackFragment guestFeedbackFragment = (GuestFeedbackFragment) parentFragment;
                        guestFeedbackFragment.onUserAcknowledgeError();
                    }
                    break;
            }
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);

            // Simulate a cancel click
            onClick(dialog, DialogInterface.BUTTON_POSITIVE);
        }
    }

    private static class ErrorTextWatcher implements TextWatcher {
        private TextView errorText;

        public ErrorTextWatcher(TextView errorText) {
            this.errorText = errorText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            errorText.setVisibility(View.GONE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
