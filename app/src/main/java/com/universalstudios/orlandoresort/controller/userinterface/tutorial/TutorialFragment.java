/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.tutorial;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.image.ImagePagerUtils;
import com.universalstudios.orlandoresort.controller.userinterface.image.ImagePagerUtils.PagerDotColor;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.view.fonts.TextView;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager.TransitionEffect;

/**
 * 
 * 
 * @author Steven Byle
 */
public class TutorialFragment extends Fragment implements OnPageChangeListener, OnClickListener {
	private static final String TAG = TutorialFragment.class.getSimpleName();

	private static final String KEY_STATE_CUR_VIEWPAGER_TAB = "KEY_STATE_CUR_VIEWPAGER_TAB";

	private int mCurrentViewPagerTab;
	private JazzyViewPager mViewPager;
	private LinearLayout mPagerDotContainer;
	private TextView mSkipButtonText;
	private View mSkipButton;
	private TutorialPageFragmentPagerAdapter mTutorialPageFragmentPagerAdapter;
	private static List<TutorialPage> sTutorialPages;

	public static TutorialFragment newInstance() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance");
		}

		// Create a new fragment instance
		TutorialFragment fragment = new TutorialFragment();

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
			// Track the page view
			AnalyticsUtils.trackPageView(
					AnalyticsUtils.CONTENT_GROUP_SITE_NAV,
					null, null,
					AnalyticsUtils.CONTENT_SUB_2_WALKTHROUGH,
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

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_tutorial, container, false);

		// Setup Views
		mViewPager = (JazzyViewPager) fragmentView.findViewById(R.id.fragment_tutorial_viewpager);
		mSkipButtonText = (TextView) fragmentView.findViewById(R.id.fragment_tutorial_skip_button_text);
		mSkipButton = fragmentView.findViewById(R.id.fragment_tutorial_skip_button);
		mPagerDotContainer = (LinearLayout) fragmentView.findViewById(R.id.fragment_tutorial_dot_layout);

		// Set text
		mSkipButton.setOnClickListener(this);

		// Setup view pager
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setTransitionEffect(TransitionEffect.Standard);
		mViewPager.setFadeEnabled(false);

		mTutorialPageFragmentPagerAdapter = new TutorialPageFragmentPagerAdapter(mViewPager,
				getChildFragmentManager(), getTutorialPages());
		mViewPager.setAdapter(mTutorialPageFragmentPagerAdapter);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			mCurrentViewPagerTab = mViewPager.getCurrentItem();
		}
		// Otherwise, restore state
		else {
			mCurrentViewPagerTab = savedInstanceState.getInt(KEY_STATE_CUR_VIEWPAGER_TAB);
		}

		// Clear out the dot pager indicator and add new ones
		mPagerDotContainer.removeAllViews();
		int pageCount = mTutorialPageFragmentPagerAdapter.getCount();
		if (pageCount > 1) {
			for (int i = 0; i < pageCount; i++) {
				View pagerDot = ImagePagerUtils.createPagerDotView(mPagerDotContainer, i == mCurrentViewPagerTab, PagerDotColor.BLUE);
				mPagerDotContainer.addView(pagerDot);
			}
			mPagerDotContainer.setVisibility(View.VISIBLE);
		}
		// Only show pager dots if there is more than one page
		else {
			mPagerDotContainer.setVisibility(View.GONE);
		}

		// Update the button text
		boolean isAtEndOfPager = mCurrentViewPagerTab == (mTutorialPageFragmentPagerAdapter.getCount() -1);
		mSkipButtonText.setText(getButtonTextResId(isAtEndOfPager));

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
		outState.putInt(KEY_STATE_CUR_VIEWPAGER_TAB, mCurrentViewPagerTab);
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

		if (mTutorialPageFragmentPagerAdapter != null) {
			mTutorialPageFragmentPagerAdapter.destroy();
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

		switch (v.getId()) {
			case R.id.fragment_tutorial_skip_button:
				// Finish the activity
				Activity parenActivity = getActivity();
				if (parenActivity != null) {
					boolean isAtEndOfPager = mCurrentViewPagerTab == (mTutorialPageFragmentPagerAdapter.getCount() - 1);
					String eventName = isAtEndOfPager ?
							AnalyticsUtils.EVENT_NAME_WALKTHROUGH_COMPLETE : AnalyticsUtils.EVENT_NAME_SKIP_WALKTHROUGH;

					// Track the event
					AnalyticsUtils.trackEvent(
							null,
							eventName,
							null,
							null);

					parenActivity.finish();
				}
				break;
			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onClick: unknown view clicked");
				}
				break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onPageSelected: " + position);
		}

		mCurrentViewPagerTab = position;

		// Set the proper dot on, and the others off
		int pageCount = mPagerDotContainer.getChildCount();
		if (pageCount > 1) {
			for (int i = 0; i < pageCount; i++) {
				View pagerDot = mPagerDotContainer.getChildAt(i);
				if (pagerDot != null) {
					pagerDot.setBackgroundResource(
							ImagePagerUtils.getPagerDotResId(i == mCurrentViewPagerTab, PagerDotColor.BLUE));
				}
			}
		}

		// Update the button text
		boolean isAtEndOfPager = mCurrentViewPagerTab == (mTutorialPageFragmentPagerAdapter.getCount() - 1);
		mSkipButtonText.setText(getButtonTextResId(isAtEndOfPager));
	}

	private static List<TutorialPage> getTutorialPages() {
		if (sTutorialPages == null) {
			sTutorialPages = new ArrayList<TutorialPage>();

			sTutorialPages.add(new TutorialPage(
					R.drawable.tutorial_screen_navigating, R.string.tutorial_navigating_title, R.string.tutorial_navigating_message));
			sTutorialPages.add(new TutorialPage(
					R.drawable.tutorial_screen_map, R.string.tutorial_map_title, R.string.tutorial_map_message));
			sTutorialPages.add(new TutorialPage(
					R.drawable.tutorial_screen_detail, R.string.tutorial_detail_title, R.string.tutorial_detail_message));
			sTutorialPages.add(new TutorialPage(
					R.drawable.tutorial_screen_wait_times, R.string.tutorial_wait_times_title, R.string.tutorial_wait_times_message));
			sTutorialPages.add(new TutorialPage(
					R.drawable.tutorial_screen_alerts, R.string.tutorial_alerts_title, R.string.tutorial_alerts_message));
			if (BuildConfigUtils.isLocationFlavorOrlando()) {
				sTutorialPages.add(new TutorialPage(
						R.drawable.tutorial_screen_wayfinding, R.string.tutorial_wayfinding_title, R.string.tutorial_wayfinding_message));
			}
		}
		return sTutorialPages;
	}

	private static int getButtonTextResId(boolean isAtEndOfPager) {
		return (isAtEndOfPager ? R.string.tutorial_button_get_started : R.string.tutorial_button_skip);
	}
}
