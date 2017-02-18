
package com.universalstudios.orlandoresort.controller.userinterface.faq;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
public class VacationPlannerFaqFragment extends Fragment implements ActionBarTitleProvider {
	private static final String TAG = VacationPlannerFaqFragment.class.getSimpleName();

	private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";

	private int mActionBarTitleResId;
	private DrawerStateProvider mParentDrawerStateProvider;
	private LinearLayout mFaqLinearLayout;
	private List<FaqItem> mFaqItemList;

	public static VacationPlannerFaqFragment newInstance(int actionBarTitleResId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: actionBarTitleResId = " + actionBarTitleResId);
		}

		// Create a new fragment instance
		VacationPlannerFaqFragment fragment = new VacationPlannerFaqFragment();

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
					AnalyticsUtils.CONTENT_GROUP_PLANNING,
					AnalyticsUtils.CONTENT_FOCUS_GUEST_SERVICES,
					AnalyticsUtils.CONTENT_SUB_1_FAQ,
					AnalyticsUtils.CONTENT_SUB_2_HOURS_AND_DIRECTIONS,
					AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
					null, null);
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
		}

		mFaqItemList = new ArrayList<FaqItem>();
		mFaqItemList.add(new FaqItem(R.string.vacation_planner_faq_item_1_title, R.string.vacation_planner_faq_item_1_message));
		mFaqItemList.add(new FaqItem(R.string.vacation_planner_faq_item_2_title, R.string.vacation_planner_faq_item_2_message));
		mFaqItemList.add(new FaqItem(R.string.vacation_planner_faq_item_3_title, R.string.vacation_planner_faq_item_3_message));
		mFaqItemList.add(new FaqItem(R.string.vacation_planner_faq_item_4_title, R.string.vacation_planner_faq_item_4_message));
		mFaqItemList.add(new FaqItem(R.string.vacation_planner_faq_item_5_title, R.string.vacation_planner_faq_item_5_message));
		mFaqItemList.add(new FaqItem(R.string.vacation_planner_faq_item_6_title, R.string.vacation_planner_faq_item_6_message));
		mFaqItemList.add(new FaqItem(R.string.vacation_planner_faq_item_7_title, R.string.vacation_planner_faq_item_7_message));
		mFaqItemList.add(new FaqItem(R.string.vacation_planner_faq_item_8_title, R.string.vacation_planner_faq_item_8_message));

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

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_vacation_planner_faq, container, false);

		// Setup Views
		mFaqLinearLayout = (LinearLayout) fragmentView.findViewById(R.id.fragment_vacation_planner_faq_list_layout);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise, restore state
		else {
		}

		// Remove any views in the layout
		mFaqLinearLayout.removeAllViews();

		View itemView;
		for (int i = 0; i < mFaqItemList.size(); i++) {
			FaqItem faqItem = mFaqItemList.get(i);
			itemView = createItemView(mFaqLinearLayout, i+1, faqItem.getTitleTextResId(), faqItem.getMessageTextResId());
			mFaqLinearLayout.addView(itemView);
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
	public String provideTitle() {
		return getString(mActionBarTitleResId);
	}

	private static View createItemView(ViewGroup parentLayout, int number, Integer primaryTextStringResId, Integer secondaryTextStringResId) {
		LayoutInflater inflater = LayoutInflater.from(parentLayout.getContext());

		ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.list_faq_item, parentLayout, false);
		TextView primaryText = (TextView) itemView.findViewById(R.id.list_faq_item_primary_text);
		TextView secondaryText = (TextView) itemView.findViewById(R.id.list_faq_item_secondary_text);
		TextView numberText = (TextView) itemView.findViewById(R.id.list_faq_item_number_text);

		if (primaryTextStringResId != null) {
			primaryText.setText(parentLayout.getContext().getString(primaryTextStringResId));
		}
		primaryText.setVisibility(primaryTextStringResId != null ? View.VISIBLE : View.GONE);

		if (secondaryTextStringResId != null) {
			secondaryText.setText(parentLayout.getContext().getString(secondaryTextStringResId));
		}
		secondaryText.setVisibility(secondaryTextStringResId != null ? View.VISIBLE : View.GONE);

		if (number > 0) {
			numberText.setText("" + number);
		}
		numberText.setVisibility(number > 0 ? View.VISIBLE : View.GONE);

		return itemView;
	}

	private static class FaqItem {
		private final int titleTextResId;
		private final int messageTextResId;

		/**
		 * @param titleTextResId
		 * @param messageTextResId
		 */
		public FaqItem(int titleTextResId, int messageTextResId) {
			super();
			this.titleTextResId = titleTextResId;
			this.messageTextResId = messageTextResId;
		}

		/**
		 * @return the titleTextResId
		 */
		public int getTitleTextResId() {
			return titleTextResId;
		}

		/**
		 * @return the messageTextResId
		 */
		public int getMessageTextResId() {
			return messageTextResId;
		}
	}

}
