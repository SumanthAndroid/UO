/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.welcome;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreActivity;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

/**
 * 
 * 
 * @author Steven Byle
 */
public class WelcomeFragment extends Fragment implements OnClickListener {
	private static final String TAG = WelcomeFragment.class.getSimpleName();

	public static final long DEFAULT_EXPRESS_PASS_REMINDER_INTERVAL_IN_MILLIS = 120 * 60 * 1000;

	private View mCloseButton;
	private View mRemindLaterButton;
	private View mViewExpressPassLocationsButton;

	public static WelcomeFragment newInstance() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance");
		}

		// Create a new fragment instance
		WelcomeFragment fragment = new WelcomeFragment();

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
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Default parameters
		Bundle args = getArguments();
		if (args == null) {
		}
		// Otherwise, set incoming parameters
		else {
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_welcome, container, false);

		// Setup Views
		mCloseButton = fragmentView.findViewById(R.id.fragment_welcome_close_button);
		mRemindLaterButton = fragmentView.findViewById(R.id.fragment_welcome_remind_later_button);
		mViewExpressPassLocationsButton = fragmentView.findViewById(R.id.fragment_welcome_view_express_pass_locations_button);

		// Set text
		mCloseButton.setOnClickListener(this);
		mRemindLaterButton.setOnClickListener(this);
		mViewExpressPassLocationsButton.setOnClickListener(this);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise, restore state
		else {
		}

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
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onClick");
		}

		Activity parentActivity = getActivity();

		switch (v.getId()) {
			case R.id.fragment_welcome_close_button:
				if (parentActivity != null) {
					UniversalAppState state = UniversalAppStateManager.getInstance();
					state.setRemindExpressPassLater(false);
					state.setRemindExpressPassLaterDateInMillis(null);
					UniversalAppStateManager.saveInstance(false);
				}
				// Finish the activity
				finishActivity();
				break;
			case R.id.fragment_welcome_remind_later_button:
				// Tag the event
				AnalyticsUtils.trackEvent(
						null,
						AnalyticsUtils.EVENT_NAME_YOURE_HERE_REMIND_ME_LATER_EXPRESS_PASS,
						null, null);

				if (parentActivity != null) {
					UniversalAppState state = UniversalAppStateManager.getInstance();

					// Set the date to remind the user later of express passes
					Long expressPassNotifIntervalInMin = state.getExpressPassNotificationIntervalInMin();
					long remindExpressPassLaterIntervalInMillis = expressPassNotifIntervalInMin != null
							? expressPassNotifIntervalInMin * 60 * 1000 : DEFAULT_EXPRESS_PASS_REMINDER_INTERVAL_IN_MILLIS;

					state.setRemindExpressPassLaterDateInMillis(new Date().getTime() + remindExpressPassLaterIntervalInMillis);
					state.setRemindExpressPassLater(true);
					UniversalAppStateManager.saveInstance(false);
				}
				// Finish the activity
				finishActivity();
				break;
			case R.id.fragment_welcome_view_express_pass_locations_button:
				// Tag the event
				AnalyticsUtils.trackEvent(
						null,
						AnalyticsUtils.EVENT_NAME_YOURE_HERE_GET_EXPRESS_PASS,
						null, null);

				if (parentActivity != null) {
					UniversalAppState state = UniversalAppStateManager.getInstance();
					state.setRemindExpressPassLater(false);
					state.setRemindExpressPassLaterDateInMillis(null);
					UniversalAppStateManager.saveInstance(false);

					// Open the explore by shopping - sells express pass page
					Bundle bundle = ExploreActivity.newInstanceBundle(
							R.string.action_title_express_pass_locations,
							ExploreType.SHOPPING_EXPRESS_PASS);
					startActivity(new Intent(parentActivity, ExploreActivity.class).putExtras(bundle));
				}
				// Finish the activity
				finishActivity();
				break;
			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onClick: unknown view clicked");
				}
				break;
		}
	}

	private void finishActivity() {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			parentActivity.finish();
		}
	}
}
