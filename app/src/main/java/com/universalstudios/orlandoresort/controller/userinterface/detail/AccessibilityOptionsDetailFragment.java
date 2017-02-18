/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import java.util.Locale;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.AccessibilityOptionsProvider;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Ride;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

/**
 * 
 * 
 * @author Steven Byle
 */
public class AccessibilityOptionsDetailFragment extends DatabaseQueryFragment implements OnClickListener, OnFeatureListStateChangeListener {
	private static final String TAG = AccessibilityOptionsDetailFragment.class.getSimpleName();

	private static final String KEY_STATE_ARE_ACCESSIBILITY_OPTIONS_EXPANDED = "KEY_STATE_ARE_ACCESSIBILITY_OPTIONS_EXPANDED";
	private static final String KEY_STATE_IS_FEATURE_LIST_EMPTY = "KEY_STATE_IS_FEATURE_LIST_EMPTY";
	private static final int DP_WIDTH_PER_COLLAPSED_ICON = 80;

	private boolean mAreAccessibilityOptionsExpanded;
	private LinearLayout mAccessibilityIconHorizLayout;
	private LinearLayout mAccessibilityIconVertLayout;
	private ViewGroup mExpandCollapseButtonLayout;
	private ViewGroup mExpandCollapseButton;
	private ImageView mExpandCollapseButtonIconDown;
	private ImageView mExpandCollapseButtonIconUp;
	private View mTopDivider;
	private boolean mIsFeatureListEmpty;

	public static AccessibilityOptionsDetailFragment newInstance(DatabaseQuery databaseQuery) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: databaseQuery = " + databaseQuery);
		}

		// Create a new fragment instance
		AccessibilityOptionsDetailFragment fragment = new AccessibilityOptionsDetailFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		if (databaseQuery != null) {
			args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
		}
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
			mAreAccessibilityOptionsExpanded = false;
			mIsFeatureListEmpty = false;
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
			mAreAccessibilityOptionsExpanded = savedInstanceState.getBoolean(KEY_STATE_ARE_ACCESSIBILITY_OPTIONS_EXPANDED);
			mIsFeatureListEmpty = savedInstanceState.getBoolean(KEY_STATE_IS_FEATURE_LIST_EMPTY);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_detail_accessibility_options, container, false);

		// Setup Views
		mAccessibilityIconHorizLayout = (LinearLayout) fragmentView.findViewById(R.id.fragment_detail_accessibility_options_icon_horiz_layout);
		mAccessibilityIconVertLayout = (LinearLayout) fragmentView.findViewById(R.id.fragment_detail_accessibility_options_icon_vert_layout);
		mExpandCollapseButtonLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_detail_accessibility_options_expand_collapse_button_layout);
		mExpandCollapseButton = (ViewGroup) fragmentView.findViewById(R.id.fragment_detail_accessibility_options_expand_collapse_button);
		mExpandCollapseButtonIconDown = (ImageView) fragmentView.findViewById(R.id.fragment_detail_accessibility_options_expand_collapse_button_icon_down);
		mExpandCollapseButtonIconUp = (ImageView) fragmentView.findViewById(R.id.fragment_detail_accessibility_options_expand_collapse_button_icon_up);
		mTopDivider = fragmentView.findViewById(R.id.fragment_detail_accessibility_options_top_divider);

		mExpandCollapseButton.setOnClickListener(this);
		mAccessibilityIconVertLayout.setOnClickListener(this);

		// Show/hide the top divider depending on the feature list
		mTopDivider.setVisibility(mIsFeatureListEmpty ? View.GONE : View.VISIBLE);

		// Restore expand/collapse state
		mExpandCollapseButtonLayout.setVisibility(mAreAccessibilityOptionsExpanded ?
				View.VISIBLE : View.GONE);
		updateAccessibilityExpandCollapseState(mAreAccessibilityOptionsExpanded);

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

		outState.putBoolean(KEY_STATE_ARE_ACCESSIBILITY_OPTIONS_EXPANDED, mAreAccessibilityOptionsExpanded);
		outState.putBoolean(KEY_STATE_IS_FEATURE_LIST_EMPTY, mIsFeatureListEmpty);
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

		switch (v.getId()) {
			case R.id.fragment_detail_accessibility_options_expand_collapse_button:
				// Toggle the expand/collapse state
				updateAccessibilityExpandCollapseState(!mAreAccessibilityOptionsExpanded);
				break;

			default: {
				UniversalAppState uoState = UniversalAppStateManager.getInstance();
				String riderGuidePdfUrl = uoState.getRiderGuidePdfUrl();
				if (mAreAccessibilityOptionsExpanded && riderGuidePdfUrl != null && !riderGuidePdfUrl.isEmpty()) {
					Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(riderGuidePdfUrl));
					startActivity(myIntent);
				}
			}
				break;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoadFinished");
		}

		switch (loader.getId()) {
			case LOADER_ID_DATABASE_QUERY:
				// Let the parent class check for double loads
				if (checkIfDoubleOnLoadFinished()) {
					if (BuildConfig.DEBUG) {
						Log.i(TAG, "onLoadFinished: ignoring update due to double load");
					}
					return;
				}

				// Go through each new POI
				if (data != null && data.moveToFirst()) {
					String poiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
					Integer poiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));

					PointOfInterest poi = PointOfInterest.fromJson(poiObjectJson, poiTypeId);

					// Clear the accessibility options and load new ones
					mAccessibilityIconHorizLayout.removeAllViews();
					mAccessibilityIconVertLayout.removeAllViews();

					if (poi instanceof AccessibilityOptionsProvider) {
						AccessibilityOptionsProvider aoProvider = (AccessibilityOptionsProvider) poi;

						// Get the amount of width for the collapsed state, and calculate how many icons can fit
						DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
						float widthDp = displayMetrics.widthPixels / displayMetrics.density;
						float heightDp = displayMetrics.heightPixels / displayMetrics.density;
						float smallestWidthDp = Math.min(widthDp, heightDp);

						int maxNumIconsInCollapsedState = Math.round(widthDp) / DP_WIDTH_PER_COLLAPSED_ICON;
						int minNumIconsToEnableCollapsedState = Math.round(smallestWidthDp) / DP_WIDTH_PER_COLLAPSED_ICON;

						// Create accessibility options for collapsed/expanded states
						if (poi instanceof Ride) {
							Ride ride = (Ride) poi;

							Integer minHeightInches = ride.getMinHeightInInches();
							Integer maxHeightInches = ride.getMaxHeightInInches();

							String formattedHeight = null;
							if (minHeightInches != null && maxHeightInches != null) {
								formattedHeight = getString(R.string.detail_access_option_height_title_range_two,
										minHeightInches.intValue(), maxHeightInches.intValue());
							}
							else if (minHeightInches != null) {
								formattedHeight = getString(R.string.detail_access_option_height_title_min,
										minHeightInches.intValue());
							}
							else if (maxHeightInches != null) {
								formattedHeight = getString(R.string.detail_access_option_height_title_max,
										maxHeightInches.intValue());
							}

							if (formattedHeight != null) {
								if (mAccessibilityIconHorizLayout.getChildCount() < maxNumIconsInCollapsedState) {
									View accessibilityIcon = createAccessibilityItemHorizontal(R.drawable.ic_detail_accessibility_height);
									mAccessibilityIconHorizLayout.addView(accessibilityIcon);
								}

								View accessibilityListItem = createAccessibilityItemVertical(R.drawable.ic_detail_accessibility_height,
										formattedHeight,
										getString(R.string.detail_access_option_height_sub_title),
										getString(R.string.detail_access_option_height_description));
								mAccessibilityIconVertLayout.addView(accessibilityListItem);
							}

							// Special note if child needs a supervising companion
							final int supervisionThresholdInInches = 48;
							if (minHeightInches != null && minHeightInches > 0 && minHeightInches < supervisionThresholdInInches) {
								if (mAccessibilityIconHorizLayout.getChildCount() < maxNumIconsInCollapsedState) {
									View accessibilityIcon = createAccessibilityItemHorizontal(R.drawable.ic_detail_accessibility_supervision_required_height);
									mAccessibilityIconHorizLayout.addView(accessibilityIcon);
								}

								View accessibilityListItem = createAccessibilityItemVertical(R.drawable.ic_detail_accessibility_supervision_required_height,
										getString(R.string.detail_access_option_supervision_required_title, supervisionThresholdInInches),
										getString(R.string.detail_access_option_supervision_required_sub_title),
										getString(R.string.detail_access_option_supervision_required_description,
												minHeightInches.intValue(), supervisionThresholdInInches));
								mAccessibilityIconVertLayout.addView(accessibilityListItem);
							}
						}

						if (aoProvider.hasAccessibilityOption(AccessibilityOptionsProvider.ACCESSIBILITY_OPTION_PARENTAL_DISCRETION_ADVISED)) {
							if (mAccessibilityIconHorizLayout.getChildCount() < maxNumIconsInCollapsedState) {
								View accessibilityIcon = createAccessibilityItemHorizontal(R.drawable.ic_detail_accessibility_parental_discretion);
								mAccessibilityIconHorizLayout.addView(accessibilityIcon);
							}

							View accessibilityListItem = createAccessibilityItemVertical(R.drawable.ic_detail_accessibility_parental_discretion,
									R.string.detail_access_option_parental_discretion_title,
									R.string.detail_access_option_parental_discretion_sub_title,
									R.string.detail_access_option_parental_discretion_description);
							mAccessibilityIconVertLayout.addView(accessibilityListItem);
						}
						if (aoProvider.hasAccessibilityOption(AccessibilityOptionsProvider.ACCESSIBILITY_OPTION_STANDARD_WHEELCHAIR)) {
							if (mAccessibilityIconHorizLayout.getChildCount() < maxNumIconsInCollapsedState) {
								View accessibilityIcon = createAccessibilityItemHorizontal(R.drawable.ic_detail_accessibility_wheelchair);
								mAccessibilityIconHorizLayout.addView(accessibilityIcon);
							}

							View accessibilityListItem = createAccessibilityItemVertical(R.drawable.ic_detail_accessibility_wheelchair,
									R.string.detail_access_option_standard_wheelchair_title,
									null,
									R.string.detail_access_option_standard_wheelchair_description);
							mAccessibilityIconVertLayout.addView(accessibilityListItem);
						}
						if (aoProvider.hasAccessibilityOption(AccessibilityOptionsProvider.ACCESSIBILITY_OPTION_ANY_WHEELCHAIR)) {
							if (mAccessibilityIconHorizLayout.getChildCount() < maxNumIconsInCollapsedState) {
								View accessibilityIcon = createAccessibilityItemHorizontal(R.drawable.ic_detail_accessibility_wheelchair_ecv);
								mAccessibilityIconHorizLayout.addView(accessibilityIcon);
							}

							View accessibilityListItem = createAccessibilityItemVertical(R.drawable.ic_detail_accessibility_wheelchair_ecv,
									R.string.detail_access_option_any_wheelchair_title,
									null,
									R.string.detail_access_option_any_wheelchair_description);
							mAccessibilityIconVertLayout.addView(accessibilityListItem);
						}
						if (aoProvider.hasAccessibilityOption(AccessibilityOptionsProvider.ACCESSIBILITY_OPTION_WHEELCHAIR_MUST_TRANSFER)) {
							if (mAccessibilityIconHorizLayout.getChildCount() < maxNumIconsInCollapsedState) {
								View accessibilityIcon = createAccessibilityItemHorizontal(R.drawable.ic_detail_accessibility_wheelchair_must_transfer);
								mAccessibilityIconHorizLayout.addView(accessibilityIcon);
							}

							View accessibilityListItem = createAccessibilityItemVertical(R.drawable.ic_detail_accessibility_wheelchair_must_transfer,
									R.string.detail_access_option_wheelchair_must_transfer_title,
									null,
									R.string.detail_access_option_wheelchair_must_transfer_description);
							mAccessibilityIconVertLayout.addView(accessibilityListItem);
						}
						if (aoProvider.hasAccessibilityOption(AccessibilityOptionsProvider.ACCESSIBILITY_OPTION_STATIONARY_SEATING)) {
							if (mAccessibilityIconHorizLayout.getChildCount() < maxNumIconsInCollapsedState) {
								View accessibilityIcon = createAccessibilityItemHorizontal(R.drawable.ic_detail_accessibility_stationary_seating);
								mAccessibilityIconHorizLayout.addView(accessibilityIcon);
							}

							View accessibilityListItem = createAccessibilityItemVertical(R.drawable.ic_detail_accessibility_stationary_seating,
									R.string.detail_access_option_stationary_seating_title,
									null,
									R.string.detail_access_option_stationary_seating_description);
							mAccessibilityIconVertLayout.addView(accessibilityListItem);
						}
						if (aoProvider.hasAccessibilityOption(AccessibilityOptionsProvider.ACCESSIBILITY_OPTION_CLOSED_CAPTION)) {
							if (mAccessibilityIconHorizLayout.getChildCount() < maxNumIconsInCollapsedState) {
								View accessibilityIcon = createAccessibilityItemHorizontal(R.drawable.ic_detail_accessibility_closed_caption);
								mAccessibilityIconHorizLayout.addView(accessibilityIcon);
							}

							View accessibilityListItem = createAccessibilityItemVertical(R.drawable.ic_detail_accessibility_closed_caption,
									R.string.detail_access_option_closed_caption_title,
									R.string.detail_access_option_closed_caption_sub_title,
									R.string.detail_access_option_closed_caption_description);
							mAccessibilityIconVertLayout.addView(accessibilityListItem);
						}
						if (aoProvider.hasAccessibilityOption(AccessibilityOptionsProvider.ACCESSIBILITY_OPTION_SIGN_LANGUAGE)) {
							if (mAccessibilityIconHorizLayout.getChildCount() < maxNumIconsInCollapsedState) {
								View accessibilityIcon = createAccessibilityItemHorizontal(R.drawable.ic_detail_accessibility_sign_language);
								mAccessibilityIconHorizLayout.addView(accessibilityIcon);
							}

							View accessibilityListItem = createAccessibilityItemVertical(R.drawable.ic_detail_accessibility_sign_language,
									R.string.detail_access_option_sign_language_title,
									R.string.detail_access_option_sign_language_sub_title,
									R.string.detail_access_option_sign_language_description);
							mAccessibilityIconVertLayout.addView(accessibilityListItem);
						}
						if (aoProvider.hasAccessibilityOption(AccessibilityOptionsProvider.ACCESSIBILITY_OPTION_ASSISTIVE_LISTENING)) {
							if (mAccessibilityIconHorizLayout.getChildCount() < maxNumIconsInCollapsedState) {
								View accessibilityIcon = createAccessibilityItemHorizontal(R.drawable.ic_detail_accessibility_assistive_listening);
								mAccessibilityIconHorizLayout.addView(accessibilityIcon);
							}

							View accessibilityListItem = createAccessibilityItemVertical(R.drawable.ic_detail_accessibility_assistive_listening,
									R.string.detail_access_option_assistive_listening_title,
									R.string.detail_access_option_assistive_listening_sub_title,
									R.string.detail_access_option_assistive_listening_description);
							mAccessibilityIconVertLayout.addView(accessibilityListItem);
						}
						if (aoProvider.hasAccessibilityOption(AccessibilityOptionsProvider.ACCESSIBILITY_OPTION_NO_UNATTENDED_CHILDREN)) {
                            if (mAccessibilityIconHorizLayout.getChildCount() < maxNumIconsInCollapsedState) {
                                View accessibilityIcon = createAccessibilityItemHorizontal(R.drawable.ic_detail_accessibility_parental_discretion);
                                mAccessibilityIconHorizLayout.addView(accessibilityIcon);
                            }

                            View accessibilityListItem = createAccessibilityItemVertical(R.drawable.ic_detail_accessibility_parental_discretion,
                                    R.string.detail_access_option_no_unattended_children_title,
                                    R.string.detail_access_option_no_unattended_children_sub_title,
                                    R.string.detail_access_option_no_unattended_children_description);
                            mAccessibilityIconVertLayout.addView(accessibilityListItem);
                        }
                        if (aoProvider.hasAccessibilityOption(AccessibilityOptionsProvider.ACCESSIBILITY_OPTION_LIFE_VEST_REQUIRED)) {
                            if (mAccessibilityIconHorizLayout.getChildCount() < maxNumIconsInCollapsedState) {
                                View accessibilityIcon = createAccessibilityItemHorizontal(R.drawable.ic_detail_accessibility_life_vest_required);
                                mAccessibilityIconHorizLayout.addView(accessibilityIcon);
                            }

                            View accessibilityListItem = createAccessibilityItemVertical(R.drawable.ic_detail_accessibility_life_vest_required,
                                    R.string.detail_access_option_life_vest_required_title,
                                    R.string.detail_access_option_life_vest_required_sub_title,
                                    R.string.detail_access_option_life_vest_required_description);
                            mAccessibilityIconVertLayout.addView(accessibilityListItem);
                        }
						if (aoProvider.hasAccessibilityOption(AccessibilityOptionsProvider.ACCESSIBILITY_OPTION_EXTRA_INFO)) {
							if (mAccessibilityIconHorizLayout.getChildCount() < maxNumIconsInCollapsedState) {
								View accessibilityIcon = createAccessibilityItemHorizontal(R.drawable.ic_detail_accessibility_additional_restrictions);
								mAccessibilityIconHorizLayout.addView(accessibilityIcon);
							}

							int detailString = R.string.detail_access_option_extra_info_description;
							UniversalAppState uoState = UniversalAppStateManager.getInstance();
							String riderGuidePdfUrl = uoState.getRiderGuidePdfUrl();
							if(riderGuidePdfUrl != null && !riderGuidePdfUrl.isEmpty()) {
								detailString = R.string.detail_access_option_extra_info_riders_guide;
							}

							View accessibilityListItem = createAccessibilityItemVertical(R.drawable.ic_detail_accessibility_additional_restrictions,
									R.string.detail_access_option_extra_info_title,
									null,
									detailString);
							mAccessibilityIconVertLayout.addView(accessibilityListItem);
						}

						// Set the whole view's visibility based on if there are accessibility options
						getView().setVisibility(mAccessibilityIconVertLayout.getChildCount() > 0 ?
								View.VISIBLE : View.GONE);

						// Show/hide the expand collapse button depending on if
						// there are enough total icons to need it
						boolean expandCollapseEnabled = mAccessibilityIconVertLayout.getChildCount() >= minNumIconsToEnableCollapsedState;
						mExpandCollapseButtonLayout.setVisibility(expandCollapseEnabled ?
								View.VISIBLE : View.GONE);

						// If the expand/collapse is not enabled, default to expanded state
						if (!expandCollapseEnabled) {
							updateAccessibilityExpandCollapseState(true);
						}
					}
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoaderReset");
		}

		switch (loader.getId()) {
			case LOADER_ID_DATABASE_QUERY:
				// Data is not available anymore, delete reference
				break;
			default:
				break;
		}
	}

	@Override
	public void onFeatureListStateChange(boolean isEmpty) {
		mIsFeatureListEmpty = isEmpty;
		if (mTopDivider != null) {
			mTopDivider.setVisibility(mIsFeatureListEmpty ? View.GONE : View.VISIBLE);
		}
	}

	private View createAccessibilityItemHorizontal(Integer iconResId) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());

		ImageView iconImage = (ImageView) inflater.inflate(R.layout.accessibility_icon, mAccessibilityIconHorizLayout, false);

		if (iconResId != null) {
			iconImage.setImageResource(iconResId);
		}

		int accessibililtyIconSize = getResources().getDimensionPixelSize(R.dimen.detail_accessibility_icon_size);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				0, accessibililtyIconSize, 1.0f);
		iconImage.setLayoutParams(layoutParams);
		iconImage.setVisibility(iconResId != null ? View.VISIBLE : View.GONE);

		return iconImage;
	}

	private View createAccessibilityItemVertical(Integer iconResId, Integer titleTextResId, Integer subTitleTextResId, Integer descTextResId) {
		String titleText = titleTextResId != null ? getString(titleTextResId) : null;
		String subTitleText = subTitleTextResId != null ? getString(subTitleTextResId) : null;
		String descText = descTextResId != null ? getString(descTextResId) : null;

		return createAccessibilityItemVertical(iconResId, titleText, subTitleText, descText);
	}

	private View createAccessibilityItemVertical(Integer iconResId, String titleText, String subTitleText, String descText) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());

		View accessibilityListItem = inflater.inflate(R.layout.list_accessibility_item, mAccessibilityIconVertLayout, false);
		ImageView iconImage = (ImageView) accessibilityListItem.findViewById(R.id.list_accessibility_item_icon_image);
		TextView titleTextView = (TextView) accessibilityListItem.findViewById(R.id.list_accessibility_item_title_text);
		TextView subTitleTextView = (TextView) accessibilityListItem.findViewById(R.id.list_accessibility_item_subtitle_text);
		TextView descTextView = (TextView) accessibilityListItem.findViewById(R.id.list_accessibility_item_description_text);

		if (iconResId != null) {
			iconImage.setImageResource(iconResId);
		}
		iconImage.setVisibility(iconResId != null ? View.VISIBLE : View.GONE);

		if (titleText != null) {
			titleTextView.setText(titleText.toUpperCase(Locale.US));
		}
		titleTextView.setVisibility(titleText != null ? View.VISIBLE : View.GONE);

		if (subTitleText != null) {
			subTitleTextView.setText(subTitleText.toUpperCase(Locale.US));
		}
		subTitleTextView.setVisibility(subTitleText != null ? View.VISIBLE : View.GONE);

		if (descText != null) {
			descTextView.setText(descText);
		}
		descTextView.setVisibility(descText != null ? View.VISIBLE : View.GONE);

		return accessibilityListItem;
	}

	private void updateAccessibilityExpandCollapseState(boolean expandAccessibilityOptions) {
		mAreAccessibilityOptionsExpanded = expandAccessibilityOptions;

		if (mAreAccessibilityOptionsExpanded) {
			mAccessibilityIconHorizLayout.setVisibility(View.GONE);
			mAccessibilityIconVertLayout.setVisibility(View.VISIBLE);
			mExpandCollapseButtonIconDown.setVisibility(View.GONE);
			mExpandCollapseButtonIconUp.setVisibility(View.VISIBLE);
		}
		else {
			mAccessibilityIconVertLayout.setVisibility(View.GONE);
			mAccessibilityIconHorizLayout.setVisibility(View.VISIBLE);
			mExpandCollapseButtonIconUp.setVisibility(View.GONE);
			mExpandCollapseButtonIconDown.setVisibility(View.VISIBLE);
		}
	}

}
