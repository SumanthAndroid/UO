package com.universalstudios.orlandoresort.controller.userinterface.newsletter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.DialogUtils;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.newsletter.NewsletterRegistrationErrorState;
import com.universalstudios.orlandoresort.model.network.domain.newsletter.NewsletterRegistrationRequest;
import com.universalstudios.orlandoresort.model.network.domain.newsletter.NewsletterRegistrationResponse;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.network.service.ServiceEndpointUtils;
import com.universalstudios.orlandoresort.utils.SimpleSpinnerAdapter;
import com.universalstudios.orlandoresort.view.fonts.Button;
import com.universalstudios.orlandoresort.view.fonts.EditText;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class NewsletterFragment extends NetworkFragment implements ActionBarTitleProvider, View.OnClickListener {
	private static final String TAG = NewsletterFragment.class.getSimpleName();

	private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";
	private static final String KEY_STATE_IS_MAKING_REGISTRATION_CALL = "KEY_STATE_IS_MAKING_REGISTRATION_CALL";

	private static final int SPAN_ID_TERMS_OF_SERVICE = 1;
	private static final int SPAN_ID_PRIVACY_POLICY = 2;

	private DrawerStateProvider mParentDrawerStateProvider;
	private int mActionBarTitleResId;

	private EditText mEmailEdit;
	private TextView mEmailError;
	private Spinner mBirthMonthSpinner;
	private TextView mBirthMonthError;
	private Spinner mBirthYearSpinner;
	private TextView mBirthYearError;
	private EditText mZipEdit;
	private TextView mZipError;
	private TextView mTermsAndPolicy;
	private Button mSubmit;
	private ViewGroup mSubmitProgressModal;

	private ArrayAdapter<String> mMonthAdapter;
	private ArrayAdapter<String> mYearAdapter;

	private boolean mIsMakingRegistrationCall;

	public NewsletterFragment() {
	}

	public static NewsletterFragment newInstance(int actionBarTitleResId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: actionBarTitleResId = " + actionBarTitleResId);
		}

		// Create a new fragment instance
		NewsletterFragment fragment = new NewsletterFragment();

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

		// Check if parent fragment (if there is one) implements the interface
		Fragment parentFragment = getParentFragment();
		if (parentFragment != null && parentFragment instanceof DrawerStateProvider) {
			mParentDrawerStateProvider = (DrawerStateProvider) parentFragment;
		}
		// Otherwise, check if parent activity implements the interface
		else if (context != null && context instanceof DrawerStateProvider) {
			mParentDrawerStateProvider = (DrawerStateProvider) context;
		}
		// If neither implements the interface, log a warning
		else if (mParentDrawerStateProvider == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement DrawerStateProvider");
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		setHasOptionsMenu(true);

		// Default parameters
		Bundle args = getArguments();
		if (args == null) {
			mActionBarTitleResId = -1;
		}
		// Otherwise, set incoming parameters
		else {
			mActionBarTitleResId = args.getInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID);
		}

		if (savedInstanceState == null) {
			// Track the page view
			AnalyticsUtils.trackPageView(
					null,
					AnalyticsUtils.CONTENT_FOCUS_NEWSLETTER_SIGN_UP,
					null, null,
					AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
					null, null);

			// If this is the first creation, default state variables
			mIsMakingRegistrationCall = false;
		}
		// Otherwise, restore state
		else {
			mIsMakingRegistrationCall = savedInstanceState.getBoolean(KEY_STATE_IS_MAKING_REGISTRATION_CALL);
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

		// Set the action bar title, if the drawer isn't open
		if (mParentDrawerStateProvider != null && !mParentDrawerStateProvider.isDrawerOpenAtAll()) {
			Activity parentActivity = getActivity();
			if (parentActivity != null) {
				parentActivity.getActionBar().setTitle(provideTitle());
			}
		}

		View fragmentView = inflater.inflate(R.layout.fragment_newsletter, container, false);

		mEmailEdit = (EditText) fragmentView.findViewById(R.id.fragment_newsletter_email);
		mEmailError = (TextView) fragmentView.findViewById(R.id.fragment_newsletter_email_error);
		mBirthMonthSpinner = (Spinner) fragmentView.findViewById(R.id.fragment_newsletter_month);
		mBirthMonthError = (TextView) fragmentView.findViewById(R.id.fragment_newsletter_month_error);
		mBirthYearSpinner = (Spinner) fragmentView.findViewById(R.id.fragment_newsletter_year);
		mBirthYearError = (TextView) fragmentView.findViewById(R.id.fragment_newsletter_year_error);
		mZipEdit = (EditText) fragmentView.findViewById(R.id.fragment_newsletter_zip);
		mZipError = (TextView) fragmentView.findViewById(R.id.fragment_newsletter_zip_error);
		mTermsAndPolicy = (TextView) fragmentView.findViewById(R.id.fragment_newsletter_policy_and_terms);
		mSubmit = (Button) fragmentView.findViewById(R.id.fragment_newsletter_submit);
		mSubmitProgressModal = (ViewGroup) fragmentView.findViewById(R.id.fragment_newsletter_submitting_view);

		mTermsAndPolicy.setMovementMethod(LinkMovementMethod.getInstance());
		mTermsAndPolicy.setText(Html.fromHtml(getContext().getString(R.string.newsletter_footer_html_format,
				ServiceEndpointUtils.buildUri(ServiceEndpointUtils.URL_PATH_PRIVACY_POLICY_NBC),
				ServiceEndpointUtils.buildUri(ServiceEndpointUtils.URL_PATH_TERMS_OF_SERVICE))));

		//set spinner before adding listeners
		// Get month options
		List<String> monthOptions = getMonthList();
		monthOptions.add(0, getContext().getString(R.string.newsletter_select_month));
		mMonthAdapter = new SimpleSpinnerAdapter<String>(getContext(), monthOptions) {
			@Override
			public String getItemText(String object) {
				if (object != null) {
					return object;
				}
				return "";
			}
		};
		mBirthMonthSpinner.setAdapter(mMonthAdapter);

		// Get month options
		List<String> yearOptions = getYearList();
		yearOptions.add(0, getContext().getString(R.string.newsletter_select_year));
		mYearAdapter = new SimpleSpinnerAdapter<String>(getContext(), yearOptions) {
			@Override
			public String getItemText(String object) {
				if (object != null) {
					return object;
				}
				return "";
			}
		};
		mBirthYearSpinner.setAdapter(mYearAdapter);

		mSubmit.setOnClickListener(this);
		mEmailEdit.addTextChangedListener(new ErrorTextWatcher(mEmailError));
		mZipEdit.addTextChangedListener(new ErrorTextWatcher(mZipError));
		mBirthMonthSpinner.setOnItemSelectedListener(new ErrorItemSelectedListener(mBirthMonthError));
		mBirthYearSpinner.setOnItemSelectedListener(new ErrorItemSelectedListener(mBirthYearError));

		return fragmentView;
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

		outState.putBoolean(KEY_STATE_IS_MAKING_REGISTRATION_CALL, mIsMakingRegistrationCall);
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
	public String provideTitle() {
		return getString(mActionBarTitleResId);
	}


	/**
	 * get the list of ratings
	 *
	 * @return
	 */
	private List<String> getMonthList() {
		List<String> monthsList = new ArrayList<>();
		String[] months = new DateFormatSymbols(Locale.US).getMonths();
		for (String month : months) {
			monthsList.add(month);
		}

		return monthsList;
	}

	/**
	 * get the list of ratings
	 *
	 * @return
	 */
	private List<String> getYearList() {
		List<String> yearList = new ArrayList<>();
		//add this year to list
		Calendar prevYear = Calendar.getInstance();
		yearList.add(String.valueOf(prevYear.get(Calendar.YEAR)));

		//add last n years
		for (int i = 0; i < 100; i++) {
			prevYear.add(Calendar.YEAR, -1);
			yearList.add(String.valueOf(prevYear.get(Calendar.YEAR)));
		}
		return yearList;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.fragment_newsletter_submit:
				if (!mIsMakingRegistrationCall) {
					// Close the keyboard
					UserInterfaceUtils.closeKeyboard(mSubmit);

					// Trim the fields to remove leading/trailing whitespace before validating
					trimFields();

					// Send the feedback to the service tier
					register();

					// Update the loading UI
					mIsMakingRegistrationCall = true;
					updateLoadingState();
				}
		}
	}

	@Override
	public void handleNetworkResponse(NetworkResponse networkResponse) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "handleNetworkResponse");
		}
		if (networkResponse instanceof NewsletterRegistrationResponse) {
			NewsletterRegistrationResponse response = (NewsletterRegistrationResponse) networkResponse;

			if (response.isHttpStatusCodeSuccess()) {
				if (BuildConfig.DEBUG) {
					Log.i(TAG, "handleNetworkResponse: NewsletterRegistrationResponse = success");
				}
				// Show success dialog
				NewsletterRegistrationSuccessDialogFragment dialogFragment = new NewsletterRegistrationSuccessDialogFragment();
				DialogUtils.showDialogFragment(getChildFragmentManager(), dialogFragment);

				// Tag the event
				AnalyticsUtils.trackEvent(null, null,
						AnalyticsUtils.EVENT_NUM_NEWSLETTER_SIGNUP_SUCCESS, null);
			} else {
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "handleNetworkResponse: NewsletterRegistrationResponse = failure");
				}

				// Show any validation errors from the services
				boolean inlineErrorsShown = showInlineErrors(response.getErrorState());

				// If they couldn't be parsed or there are none to show, show a generic error
				if (!inlineErrorsShown) {
					// Show error dialog
					NewsletterRegistrationErrorDialogFragment dialogFragment = new NewsletterRegistrationErrorDialogFragment();
					DialogUtils.showDialogFragment(getChildFragmentManager(), dialogFragment);
				}
			}

			// Update the loading UI
			mIsMakingRegistrationCall = false;
			updateLoadingState();
		}
	}

	/**
	 *
	 */
	private void register() {
		String email = mEmailEdit.getText().toString().trim();
		Integer month = monthStringToInt(mBirthMonthSpinner.getSelectedItem().toString().trim());
		Integer year = yearStringToInt(mBirthYearSpinner.getSelectedItem().toString().trim());
		String zip = mZipEdit.getText().toString().trim();

		NewsletterRegistrationRequest newsletterRegistrationRequest = new NewsletterRegistrationRequest.Builder(this)
				.setEmail(email)
				.setBirthMonth(month)
				.setBirthYear(year)
				.setZipcode(zip)
				.build();
		NetworkUtils.queueNetworkRequest(newsletterRegistrationRequest);
		NetworkUtils.startNetworkService();
	}

	private Integer monthStringToInt(String month) {
		if (TextUtils.isEmpty(month)) {
			return null;
		}

		try {
			Date date = new SimpleDateFormat("MMM", Locale.US).parse(month);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return (cal.get(Calendar.MONTH) + 1);
		} catch (ParseException e) {
			return null;
		}
	}

	private Integer yearStringToInt(String year) {
		if (TextUtils.isEmpty(year)) {
			return null;
		}

		try {
			Date date = new SimpleDateFormat("yyyy", Locale.US).parse(year);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal.get(Calendar.YEAR);
		} catch (ParseException e) {
			return null;
		}

	}

	private void trimFields() {
		mEmailEdit.setText(mEmailEdit.getText().toString().trim());
		mZipEdit.setText(mZipEdit.getText().toString().trim());
	}

	private boolean showInlineErrors(NewsletterRegistrationErrorState errorState) {
		boolean errorShown = false;

		if (errorState != null) {

			String emailErrors = convertErrorListToString(errorState.getEmailErrors());
			if (!TextUtils.isEmpty(emailErrors)) {
				mEmailError.setText(emailErrors);
				mEmailError.setVisibility(View.VISIBLE);
				errorShown = true;
			}

			String monthErrors = convertErrorListToString(errorState.getBirthMonthErrors());
			if (!TextUtils.isEmpty(monthErrors)) {
				mBirthMonthError.setText(monthErrors);
				mBirthMonthError.setVisibility(View.VISIBLE);
				errorShown = true;
			}

			String yearErrors = convertErrorListToString(errorState.getBirthYearErrors());
			if (!TextUtils.isEmpty(yearErrors)) {
				mBirthYearError.setText(yearErrors);
				mBirthYearError.setVisibility(View.VISIBLE);
				errorShown = true;
			}

			String zipErrors = convertErrorListToString(errorState.getZipCodeErrors());
			if (!TextUtils.isEmpty(zipErrors)) {
				mZipError.setText(zipErrors);
				mZipError.setVisibility(View.VISIBLE);
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
		mSubmitProgressModal.setVisibility(mIsMakingRegistrationCall ? View.VISIBLE : View.GONE);
	}

	public static class NewsletterRegistrationSuccessDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
		private static final String TAG = NewsletterRegistrationSuccessDialogFragment.class.getSimpleName();

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			alertDialogBuilder.setTitle(R.string.newsletter_submit_dialog_success_title);
			alertDialogBuilder.setMessage(R.string.newsletter_submit_dialog_success_message);
			alertDialogBuilder.setPositiveButton(R.string.newsletter_submit_dialog_positive_button, this);

			Dialog dialog = alertDialogBuilder.create();
			dialog.setCanceledOnTouchOutside(true);
			return dialog;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Fragment parentFragment = getParentFragment();
					if (parentFragment != null && parentFragment instanceof NewsletterFragment) {
						NewsletterFragment newsletterFragment = (NewsletterFragment) parentFragment;
						newsletterFragment.onUserAcknowledgeSuccess();
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

	public static class NewsletterRegistrationErrorDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
		private static final String TAG = NewsletterRegistrationErrorDialogFragment.class.getSimpleName();

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			alertDialogBuilder.setMessage(R.string.newsletter_submit_dialog_error_message);
			alertDialogBuilder.setPositiveButton(R.string.newsletter_submit_dialog_positive_button, this);

			Dialog dialog = alertDialogBuilder.create();
			dialog.setCanceledOnTouchOutside(true);
			return dialog;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Fragment parentFragment = getParentFragment();
					if (parentFragment != null && parentFragment instanceof NewsletterFragment) {
						NewsletterFragment newsletterFragment = (NewsletterFragment) parentFragment;
						newsletterFragment.onUserAcknowledgeError();
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

	private static class ErrorItemSelectedListener implements AdapterView.OnItemSelectedListener {
		private TextView errorText;

		public ErrorItemSelectedListener(TextView errorText) {
			this.errorText = errorText;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			errorText.setVisibility(View.GONE);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

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
