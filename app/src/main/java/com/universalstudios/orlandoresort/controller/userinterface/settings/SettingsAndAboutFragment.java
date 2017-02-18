
package com.universalstudios.orlandoresort.controller.userinterface.settings;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.link.DeepLinkUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.state.settings.UniversalOrlandoSettings;
import com.universalstudios.orlandoresort.model.state.settings.UniversalOrlandoSettingsManager;


/**
 *
 *
 * @author Steven Byle
 */
public class SettingsAndAboutFragment extends Fragment implements OnClickListener, OnCheckedChangeListener, ActionBarTitleProvider {
	private static final String TAG = SettingsAndAboutFragment.class.getSimpleName();

	private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";

	private int mActionBarTitleResId;
	private DrawerStateProvider mParentDrawerStateProvider;
	private LinearLayout mSettingsLinearLayout;

	public static SettingsAndAboutFragment newInstance(int actionBarTitleResId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: actionBarTitleResId = " + actionBarTitleResId);
		}

		// Create a new fragment instance
		SettingsAndAboutFragment fragment = new SettingsAndAboutFragment();

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
			// Track the page view
			AnalyticsUtils.trackPageView(
					AnalyticsUtils.CONTENT_GROUP_SITE_NAV,
					null, null,
					AnalyticsUtils.CONTENT_SUB_2_SETTINGS,
					AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
					null, null);
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
		View fragmentView = inflater.inflate(R.layout.fragment_settings_and_about, container, false);

		// Setup Views
		mSettingsLinearLayout = (LinearLayout) fragmentView.findViewById(R.id.fragment_settings_and_about_list_layout);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise, restore state
		else {
		}

		// Remove any views in the layout
		mSettingsLinearLayout.removeAllViews();

		UniversalOrlandoSettings settings = UniversalOrlandoSettingsManager.getInstance();

		// Notifications
		View sectionHeaderView = createSectionHeaderView(mSettingsLinearLayout, R.string.settings_section_header_notifications);
		mSettingsLinearLayout.addView(sectionHeaderView);

		boolean isShowParkNewsNotificationsOn = settings.getIsShowParkNewsNotificationsOn();
		View itemView = createItemView(mSettingsLinearLayout, R.string.settings_option_notifications_park_news, null, isShowParkNewsNotificationsOn, null, this, this);
		mSettingsLinearLayout.addView(itemView);

		// Orlando only - Guide Me
		if (BuildConfigUtils.isLocationFlavorOrlando()) {
			sectionHeaderView = createSectionHeaderView(mSettingsLinearLayout, R.string.settings_section_header_guide_me);
			mSettingsLinearLayout.addView(sectionHeaderView);

			boolean isSoundOn = settings.getIsGuideMeSoundOn();
			itemView = createItemView(mSettingsLinearLayout, R.string.settings_option_guide_me_sounds, null, null, isSoundOn, this, this);
			mSettingsLinearLayout.addView(itemView);

			boolean isVibrateOn = settings.getIsGuideMeVibrateOn();
			itemView = createItemView(mSettingsLinearLayout, R.string.settings_option_guide_me_vibrate, null, null, isVibrateOn, this, this);
			mSettingsLinearLayout.addView(itemView);
		}

		// Hollywood only - Tutorial
		if (BuildConfigUtils.isLocationFlavorHollywood()) {
			sectionHeaderView = createSectionHeaderView(mSettingsLinearLayout, R.string.settings_section_header_about);
			mSettingsLinearLayout.addView(sectionHeaderView);

			itemView = createItemView(mSettingsLinearLayout, R.string.settings_option_tutorial, null, null, null, this, null);
			mSettingsLinearLayout.addView(itemView);

			itemView = createItemView(mSettingsLinearLayout, R.string.settings_option_privacy_policy, null, null, null, this, null);
			mSettingsLinearLayout.addView(itemView);

			itemView = createItemView(mSettingsLinearLayout, R.string.settings_option_privacy_policy_faq, null, null, null, this, null);
			mSettingsLinearLayout.addView(itemView);

			itemView = createItemView(mSettingsLinearLayout, R.string.settings_option_terms_and_conditions, null, null, null, this, null);
			mSettingsLinearLayout.addView(itemView);

			itemView = createItemView(mSettingsLinearLayout, R.string.settings_option_copyright_and_trademarks, null, null, null, this, null);
			mSettingsLinearLayout.addView(itemView);
		}


		// Version number
		Context context = container.getContext();
		PackageInfo pInfo = UniversalOrlandoApplication.getPackageInfo();
		if (pInfo != null) {
			String versionName = context.getString(R.string.settings_option_version, pInfo.versionName);
			itemView = createItemView(mSettingsLinearLayout, null, versionName, null, null, null, null);
			mSettingsLinearLayout.addView(itemView);
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

		Object tag = v.getTag();
		if (tag != null && tag instanceof Integer) {
			if (tag.equals(R.string.settings_option_guide_me_sounds)) {
				CheckBox checkBox = (CheckBox) v.findViewById(R.id.list_settings_item_checkbox);
				checkBox.toggle();
			}
			else if (tag.equals(R.string.settings_option_guide_me_vibrate)) {
				CheckBox checkBox = (CheckBox) v.findViewById(R.id.list_settings_item_checkbox);
				checkBox.toggle();
			}
			else if (tag.equals(R.string.settings_option_notifications_park_news)) {
				Switch switchView = (Switch) v.findViewById(R.id.list_settings_item_switch);
				switchView.toggle();
			}
			else if (tag.equals(R.string.settings_option_tutorial)) {
				DeepLinkUtils.loadPage(DeepLinkUtils.DeepLinkType.WALKTHROUGH, getContext());
			}
			else if (tag.equals(R.string.settings_option_privacy_policy)) {
				DeepLinkUtils.loadPage(DeepLinkUtils.DeepLinkType.PRIVACY_POLICY, getContext());
			}
			else if (tag.equals(R.string.settings_option_terms_and_conditions)) {
				DeepLinkUtils.loadPage(DeepLinkUtils.DeepLinkType.TERMS_OF_SERVICE, getContext());
			}
			else if (tag.equals(R.string.settings_option_copyright_and_trademarks)) {
				DeepLinkUtils.loadPage(DeepLinkUtils.DeepLinkType.TRADEMARKS, getContext());
			} else if(tag.equals(R.string.settings_option_privacy_policy_faq)) {
				DeepLinkUtils.loadPage(DeepLinkUtils.DeepLinkType.PRIVACY_FAQ, getContext());
			}
			else {
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onClick: unrecognized button press");
				}
			}
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCheckedChanged: isChecked = " + isChecked);
		}

		Object tag = buttonView.getTag();
		if (tag != null && tag instanceof Integer) {
			if (tag.equals(R.string.settings_option_guide_me_sounds)) {
				UniversalOrlandoSettingsManager.getInstance().setIsGuideMeSoundOn(isChecked);
				UniversalOrlandoSettingsManager.saveInstance();
			}
			else if (tag.equals(R.string.settings_option_guide_me_vibrate)) {
				UniversalOrlandoSettingsManager.getInstance().setIsGuideMeVibrateOn(isChecked);
				UniversalOrlandoSettingsManager.saveInstance();
			}
			else if (tag.equals(R.string.settings_option_notifications_park_news)) {
				UniversalOrlandoSettingsManager.getInstance().setIsShowParkNewsNotificationsOn(isChecked);
				UniversalOrlandoSettingsManager.saveInstance();
			}
			else {
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onCheckedChanged: unrecognized switch changed");
				}
			}
		}
	}

	@Override
	public String provideTitle() {
		return getString(mActionBarTitleResId);
	}

	private static View createItemView(ViewGroup parentLayout, Integer primaryTextStringResId, String secondaryTextString,
			Boolean switchState, Boolean checkBoxState, OnClickListener onClickListener, OnCheckedChangeListener onCheckedChangeListener) {
		LayoutInflater inflater = LayoutInflater.from(parentLayout.getContext());

		ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.list_settings_item, parentLayout, false);
		TextView primaryText = (TextView) itemView.findViewById(R.id.list_settings_item_primary_text);
		TextView secondaryText = (TextView) itemView.findViewById(R.id.list_settings_item_secondary_text);
		View divider = itemView.findViewById(R.id.list_settings_item_top_divider);
		Switch sideSwitch = (Switch) itemView.findViewById(R.id.list_settings_item_switch);
		CheckBox checkBox = (CheckBox) itemView.findViewById(R.id.list_settings_item_checkbox);

		if (primaryTextStringResId != null) {
			primaryText.setText(parentLayout.getContext().getString(primaryTextStringResId));
		}
		primaryText.setVisibility(primaryTextStringResId != null ? View.VISIBLE : View.GONE);

		if (secondaryTextString != null) {
			secondaryText.setText(secondaryTextString);
		}
		secondaryText.setVisibility(secondaryTextString != null ? View.VISIBLE : View.GONE);

		boolean showDivider = parentLayout.getChildCount() > 0
				&& (parentLayout.getChildAt(parentLayout.getChildCount() - 1).getId() == R.id.list_settings_item_root_container);
		divider.setVisibility(showDivider ? View.VISIBLE : View.GONE);

		if (sideSwitch != null) {
			if (switchState != null) {
				sideSwitch.setChecked(switchState);
				sideSwitch.setTag(primaryTextStringResId);
				sideSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
			}
			sideSwitch.setVisibility(switchState != null ? View.VISIBLE : View.GONE);
		}

		if (checkBox != null) {
			if (checkBoxState != null) {
				checkBox.setChecked(checkBoxState);
				checkBox.setTag(primaryTextStringResId);
				checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
			}
			checkBox.setVisibility(checkBoxState != null ? View.VISIBLE : View.GONE);
		}

		// Set the tag to reference back to clicks
		if (onClickListener != null) {
			itemView.setTag(primaryTextStringResId);
			itemView.setOnClickListener(onClickListener);
		}
		itemView.setClickable(onClickListener != null ? true: false);

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
