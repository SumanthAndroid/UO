
package com.universalstudios.orlandoresort.controller.userinterface.help;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.general.BasicInfoDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.detail.DiningPlanDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.detail.ExpressPassDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.detail.PackagePickupDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.faq.VacationPlannerFaqActivity;
import com.universalstudios.orlandoresort.controller.userinterface.tutorial.TutorialActivity;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;

/**
 * @author Steven Byle
 */
public class HelpFragment extends Fragment implements OnClickListener, ActionBarTitleProvider {
	private static final String TAG = HelpFragment.class.getSimpleName();

	private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";
	private static final String KEY_ARG_FAQ_ONLY = "KEY_ARG_FAQ_ONLY";

	private int mActionBarTitleResId;
	private DrawerStateProvider mParentDrawerStateProvider;
	private LinearLayout mHelpLinearLayout;

	public static HelpFragment newInstance(int actionBarTitleResId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: actionBarTitleResId = " + actionBarTitleResId);
		}

		// Create a new fragment instance
		HelpFragment fragment = new HelpFragment();

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
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onAttach");
		}

		// Check if parent fragment (if there is one) implements the interface
		Fragment parentFragment = getParentFragment();
		if (parentFragment != null && parentFragment instanceof DrawerStateProvider) {
			mParentDrawerStateProvider = (DrawerStateProvider) parentFragment;
		}
		// Otherwise, check if parent activity implements the interface
		else if (activity != null && activity instanceof DrawerStateProvider) {
			mParentDrawerStateProvider = (DrawerStateProvider) activity;
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

		// Set the action bar title, if the drawer isn't open
		if (mParentDrawerStateProvider != null && !mParentDrawerStateProvider.isDrawerOpenAtAll()) {
			Activity parentActivity = getActivity();
			if (parentActivity != null) {
				parentActivity.getActionBar().setTitle(provideTitle());
			}
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_help, container, false);

		// Setup Views
		mHelpLinearLayout = (LinearLayout) fragmentView.findViewById(R.id.fragment_help_list_layout);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise, restore state
		else {
		}
		View itemView = null;

		// Add the help options
		itemView = createItemView(mHelpLinearLayout, R.string.help_option_ticket_assistance, this);
		mHelpLinearLayout.addView(itemView);

		itemView = createItemView(mHelpLinearLayout, R.string.help_option_tutorial, this);
		mHelpLinearLayout.addView(itemView);

		itemView = createItemView(mHelpLinearLayout, R.string.food_allergy_option_tutorial, this);
		mHelpLinearLayout.addView(itemView);

		itemView = createSectionHeaderView(mHelpLinearLayout, R.string.help_option_faq_section_header);
		mHelpLinearLayout.addView(itemView);

		itemView = createItemView(mHelpLinearLayout, R.string.help_option_wifi, this);
		mHelpLinearLayout.addView(itemView);

		itemView = createItemView(mHelpLinearLayout, R.string.help_option_uo_express_pass, this);
		mHelpLinearLayout.addView(itemView);

		itemView = createItemView(mHelpLinearLayout, R.string.help_option_uo_dining_plans, this);
		mHelpLinearLayout.addView(itemView);

		itemView = createItemView(mHelpLinearLayout, R.string.help_option_child_swap, this);
		mHelpLinearLayout.addView(itemView);

		itemView = createItemView(mHelpLinearLayout, R.string.help_option_single_rider_line, this);
		mHelpLinearLayout.addView(itemView);

		itemView = createItemView(mHelpLinearLayout, R.string.help_option_package_pickup, this);
		mHelpLinearLayout.addView(itemView);

		itemView = createItemView(mHelpLinearLayout, R.string.help_option_vacation_package_faq, this);
		mHelpLinearLayout.addView(itemView);

		itemView = createItemView(mHelpLinearLayout, R.string.help_option_coke_freestyle, this);
		mHelpLinearLayout.addView(itemView);

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

		Object tag = v.getTag();
		if (tag != null && tag instanceof Integer) {
			if (tag.equals(R.string.help_option_ticket_assistance)) {
				Activity parentActivity = getActivity();
				if (parentActivity != null) {
					parentActivity.startActivity(new Intent(parentActivity, TicketAssistanceActivity.class));
				}
			} else if (tag.equals(R.string.help_option_vacation_package_faq)) {
				Activity parentActivity = getActivity();
				if (parentActivity != null) {
					parentActivity.startActivity(new Intent(parentActivity, VacationPlannerFaqActivity.class));
				}
			} else if (tag.equals(R.string.help_option_wifi)) {
				Activity parentActivity = getActivity();
				if (parentActivity != null) {
					parentActivity.startActivity(new Intent(parentActivity, AboutWifiActivity.class));
				}
			} else if (tag.equals(R.string.help_option_tutorial)) {
				Activity parentActivity = getActivity();
				if (parentActivity != null) {
					// Track the page view
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_SITE_NAV,
							null, null,
							AnalyticsUtils.CONTENT_SUB_2_VERSION_INFO,
							AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
							null, null);

					parentActivity.startActivity(new Intent(parentActivity, TutorialActivity.class));
				}
			} else if (tag.equals(R.string.food_allergy_option_tutorial)) {
				Activity parentActivity = getActivity();
				if (parentActivity != null) {
					// Track the page view
					/*AnalyticsUtils.trackPageView(
							v.getContext(),
							AnalyticsUtils.CONTENT_GROUP_SITE_NAV,
							null, null,
							AnalyticsUtils.CONTENT_SUB_2_VERSION_INFO,
							AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
							null, null);/**/

					parentActivity.startActivity(new Intent(parentActivity, FoodAllergyActivity.class));
				}
			} else if (tag.equals(R.string.help_option_child_swap)) {
				Activity parentActivity = getActivity();
				if (parentActivity != null) {
					Bundle budle = BasicInfoDetailActivity.newInstanceBundle(R.string.action_title_child_swap,
							R.string.detail_basic_info_child_swap_title, R.string.detail_basic_info_child_swap_description);
					parentActivity.startActivity(new Intent(getActivity(), BasicInfoDetailActivity.class).putExtras(budle));
				}
			} else if (tag.equals(R.string.help_option_uo_dining_plans)) {
				Activity parentActivity = getActivity();
				if (parentActivity != null) {
					parentActivity.startActivity(new Intent(parentActivity, DiningPlanDetailActivity.class));
				}
			} else if (tag.equals(R.string.help_option_uo_express_pass)) {
				Activity parentActivity = getActivity();
				if (parentActivity != null) {
					parentActivity.startActivity(new Intent(parentActivity, ExpressPassDetailActivity.class));
				}
			} else if (tag.equals(R.string.help_option_single_rider_line)) {
				Activity parentActivity = getActivity();
				Bundle budle = BasicInfoDetailActivity.newInstanceBundle(R.string.action_title_single_rider,
						R.string.detail_basic_info_single_rider_title,
						R.string.detail_basic_info_single_rider_description);
				parentActivity.startActivity(new Intent(getActivity(), BasicInfoDetailActivity.class).putExtras(budle));
			} else if (tag.equals(R.string.help_option_package_pickup)) {
				// Open the basic info page
				Activity parentActivity = getActivity();
				parentActivity.startActivity(new Intent(getActivity(), PackagePickupDetailActivity.class));
			} else if (tag.equals(R.string.help_option_coke_freestyle)) {
				Activity parentActivity = getActivity();
				Bundle budle = BasicInfoDetailActivity.newInstanceBundle(R.string.action_title_coke_freestyle,
						R.string.detail_basic_info_coke_freestyle_title,
						R.string.detail_basic_info_coke_freestyle_description);
				parentActivity.startActivity(new Intent(getActivity(), BasicInfoDetailActivity.class).putExtras(budle));
			} else {
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onClick: unrecognized button press");
				}
			}
		}
	}

	@Override
	public String provideTitle() {
		return getString(mActionBarTitleResId);
	}

	private static View createItemView(ViewGroup parentLayout, Integer primaryTextStringResId, OnClickListener onClickListener) {
		LayoutInflater inflater = LayoutInflater.from(parentLayout.getContext());

		ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.list_settings_item, parentLayout, false);
		TextView primaryText = (TextView) itemView.findViewById(R.id.list_settings_item_primary_text);
		View divider = itemView.findViewById(R.id.list_settings_item_top_divider);
		Switch sideSwitch = (Switch) itemView.findViewById(R.id.list_settings_item_switch);
		CheckBox checkBox = (CheckBox) itemView.findViewById(R.id.list_settings_item_checkbox);

		if (primaryTextStringResId != null) {
			primaryText.setText(parentLayout.getContext().getString(primaryTextStringResId));
		}
		primaryText.setVisibility(primaryTextStringResId != null ? View.VISIBLE : View.GONE);

		boolean showDivider = parentLayout.getChildCount() > 0
				&& (parentLayout.getChildAt(parentLayout.getChildCount() - 1).getId() == R.id.list_settings_item_root_container);
		divider.setVisibility(showDivider ? View.VISIBLE : View.GONE);


		// Hide switches
		if (sideSwitch != null) {
			sideSwitch.setVisibility(View.GONE);
		}
		if (checkBox != null) {
			checkBox.setVisibility(View.GONE);
		}

		// Set the tag to reference back to clicks
		if (onClickListener != null) {
			itemView.setTag(primaryTextStringResId);
			itemView.setOnClickListener(onClickListener);
		}
		return itemView;
	}

	private static View createSectionHeaderView(ViewGroup parentLayout, Integer primaryTextStringResId) {
		LayoutInflater inflater = LayoutInflater.from(parentLayout.getContext());

		ViewGroup sectionHeaderView = (ViewGroup) inflater.inflate(R.layout.list_settings_section_header, parentLayout, false);
		TextView primaryText = (TextView) sectionHeaderView.findViewById(R.id.list_settings_section_header_title_text);

		if (primaryTextStringResId != null) {
			primaryText.setText(parentLayout.getContext().getString(primaryTextStringResId));
		}
		primaryText.setVisibility(primaryTextStringResId != null ? View.VISIBLE : View.GONE);
		return sectionHeaderView;
	}

}
