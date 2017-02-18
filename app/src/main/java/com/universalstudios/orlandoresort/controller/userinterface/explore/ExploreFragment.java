
package com.universalstudios.orlandoresort.controller.userinterface.explore;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.crittercism.app.Crittercism;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.accessibility.AccessibilityUtils;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryMapFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryProvider;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.OnDatabaseQueryChangeListener;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.explore.map.ExploreMapFragment;
import com.universalstudios.orlandoresort.controller.userinterface.explore.map.PoiSelector;
import com.universalstudios.orlandoresort.controller.userinterface.explore.results.ExploreSearchResultsFragment;
import com.universalstudios.orlandoresort.controller.userinterface.filter.AdvancedFilterActivity;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions;
import com.universalstudios.orlandoresort.controller.userinterface.managers.AppPreferenceManager;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.model.state.OnUniversalAppStateChangeListener;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;
import com.universalstudios.orlandoresort.view.TintUtils;
import com.universalstudios.orlandoresort.view.buttons.FilterTabButton;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager.TransitionEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
public class ExploreFragment extends Fragment implements OnPageChangeListener, OnClickListener, DatabaseQueryProvider,
ActionBarTitleProvider, OnCheckedChangeListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
		OnUniversalAppStateChangeListener, PoiSelector {
	private static final String TAG = ExploreFragment.class.getSimpleName();

	private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";
	private static final String KEY_ARG_EXPLORE_TYPE = "KEY_ARG_EXPLORE_TYPE";
	private static final String KEY_ARG_SELECTED_POI_OBJECT_JSON = "KEY_ARG_SELECTED_POI_OBJECT_JSON";
	private static final String KEY_ARG_FILTER_OPTIONS = "KEY_ARG_FILTER_OPTIONS";
	private static final String KEY_STATE_SCROLLVIEW = "KEY_STATE_SCROLLVIEW";
	private static final String KEY_STATE_CUR_VIEWPAGER_TAB = "KEY_STATE_CUR_VIEWPAGER_TAB";
	private static final String KEY_STATE_IS_RESTROOM_TOGGLE_ON = "KEY_STATE_IS_RESTROOM_TOGGLE_ON";
	
	private static final int REQUEST_CODE_FILTER = 5001;

	private int mActionBarTitleResId;
	private int mCurrentViewPagerTab;
	private boolean mIsRestroomToggleOn;
	private String mSelectedPoiObjectJson;
	private ExploreType mExploreType;
	private List<FilterTabOption> mFilterTabOptions;
	private FilterOptions mFilterOptions;
	private DatabaseQuery mDatabaseQueryList;
	private DatabaseQuery mDatabaseQueryMap;
	private JazzyViewPager mViewPager;
	private ExploreFragmentPagerAdapter mExploreFragmentPagerAdapter;
	private LinearLayout mTabBarLinearLayout, mTabBarRootLinearLayout;
	private HorizontalScrollView mTabBarHorizontalScrollView;
	private DrawerStateProvider mParentDrawerStateProvider;
	private GoogleApiClient mGoogleApiClient;
	private LinearLayout mFilterRootLinearLayout;
	private FilterTabButton mClearFilterTabButton;
	private FilterTabButton mEditFilterTabButton;
    private ExploreSearchResultsFragment mSearchResultsFragment;
    private static boolean shouldShowCount;

	public static ExploreFragment newInstance(int actionBarTitleResId, ExploreType exploreType) {
		return newInstance(actionBarTitleResId, exploreType, null, null);
	}

    public static ExploreFragment newInstance(int actionBarTitleResId, ExploreType exploreType,
            FilterOptions filterOptions) {
		return newInstance(actionBarTitleResId, exploreType, null, filterOptions);
	}

	public static ExploreFragment newInstance(int actionBarTitleResId, ExploreType exploreType,
			String selectedPoiObjectJson) {
		return newInstance(actionBarTitleResId, exploreType, selectedPoiObjectJson, null);
	}

    public static ExploreFragment newInstance(int actionBarTitleResId, ExploreType exploreType,
			String selectedPoiObjectJson, FilterOptions filterOptions) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: exploreType = " + exploreType.getValue()
					   + " actionBarTitleResId = " + actionBarTitleResId
					   + " selectedPoiObjectJson = " + selectedPoiObjectJson);
		}

		// Create a new fragment instance
		ExploreFragment fragment = new ExploreFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		// Add parameters to the argument bundle
		args.putInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID, actionBarTitleResId);
		args.putSerializable(KEY_ARG_EXPLORE_TYPE, exploreType);

		// Add filter options if present
		if(filterOptions != null) {
			args.putString(KEY_ARG_FILTER_OPTIONS, filterOptions.toJson());
		}

		// Add selected POI if present
		if (selectedPoiObjectJson != null) {
			args.putString(KEY_ARG_SELECTED_POI_OBJECT_JSON, selectedPoiObjectJson);
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
			mExploreType = null;
			mActionBarTitleResId = -1;
			mSelectedPoiObjectJson = null;
		}
		// Otherwise, set incoming parameters
		else {
			mExploreType = (ExploreType) args.getSerializable(KEY_ARG_EXPLORE_TYPE);
			mActionBarTitleResId = args.getInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID);
			mSelectedPoiObjectJson = args.getString(KEY_ARG_SELECTED_POI_OBJECT_JSON);
            mFilterOptions = GsonObject.fromJson(args.getString(KEY_ARG_FILTER_OPTIONS), FilterOptions.class);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			mIsRestroomToggleOn = false;
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
			mIsRestroomToggleOn = savedInstanceState.getBoolean(KEY_STATE_IS_RESTROOM_TOGGLE_ON);
		}

        shouldShowCount = false;

		mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
				.addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
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
		View fragmentView = inflater.inflate(R.layout.fragment_explore, container, false);
		
		// Setup hidden filter tab buttons
		mFilterRootLinearLayout = (LinearLayout) fragmentView.findViewById(R.id.fragment_explore_filter_root_linear_layout);
		mEditFilterTabButton = (FilterTabButton) fragmentView.findViewById(R.id.fragment_explore_filter_tab_button_edit_filter);
		mEditFilterTabButton.setOnClickListener(this);
		mClearFilterTabButton = (FilterTabButton) fragmentView.findViewById(R.id.fragment_explore_filter_tab_button_clear_filter);
		mClearFilterTabButton.setOnClickListener(this);

		// Setup Views
		mViewPager = (JazzyViewPager) fragmentView.findViewById(R.id.fragment_explore_viewpager);
		mTabBarHorizontalScrollView = (HorizontalScrollView) fragmentView.findViewById(R.id.fragment_explore_filter_tab_bar_scroll);
		mTabBarLinearLayout = (LinearLayout) fragmentView.findViewById(R.id.fragment_explore_filter_tab_bar);
		mTabBarRootLinearLayout = (LinearLayout) fragmentView.findViewById(R.id.fragment_explore_filter_tab_bar_root);
        mSearchResultsFragment = (ExploreSearchResultsFragment) getChildFragmentManager().findFragmentById(R.id.fragment_explore_search_results_count_fragment);

		// Listen for page changes and setup pager animations
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setTransitionEffect(TransitionEffect.Tablet);
		mViewPager.setFadeEnabled(false);

		// Get the proper filter tab options
		mFilterTabOptions = createFilterTabOptions(mExploreType);
        // If filter options not passed create, otherwise just set filter tabs
        if (mFilterOptions == null) {
            mFilterOptions = createFilterOptions(mFilterTabOptions, mExploreType);
        } else {
            mFilterOptions.setFilterTabOptions(mFilterTabOptions);
            mFilterOptions.setExploreType(mExploreType);
        }

		if (mFilterTabOptions != null) {
			// Inflate the filter tab button into the tab bar
			for (int i = 0; i < mFilterTabOptions.size(); i++) {

				// Add a divider in front if this is not the first tab
				if (i > 0) {
					View filterTabDivider = inflater.inflate(R.layout.button_filter_tab_divider, mTabBarLinearLayout, false);
					mTabBarLinearLayout.addView(filterTabDivider);
				}

				// Create the tabs one by one
				FilterTabOption filterTabOption = mFilterTabOptions.get(i);

				FilterTabButton filterTabButton = (FilterTabButton) inflater.inflate(R.layout.button_filter_tab, mTabBarLinearLayout, false);
				filterTabButton.setText(filterTabOption.getText());
				filterTabButton.setImageDrawableResId(filterTabOption.getImageDrawableResId());
				filterTabButton.setId(filterTabOption.getId());
				filterTabButton.setChecked(filterTabOption.getDefaultCheckedState());
				filterTabButton.setOnClickListener(this);
				mTabBarLinearLayout.addView(filterTabButton);

				// Hold a ref to the button for later
				filterTabOption.setFilterTabButton(filterTabButton);
			}

			// After adding all tabs, go back and check for line wrapping
			for (int i = 0; i < mFilterTabOptions.size(); i++) {
				// Create the tabs one by one
				FilterTabOption filterTabOption = mFilterTabOptions.get(i);
				final FilterTabButton filterTabButton = filterTabOption.getFilterTabButton();

				filterTabButton.post(new Runnable() {
					@Override
					public void run() {
						// Allow any tabs that have line wraps to wrap content instead of using an equal gravity
						if (filterTabButton.getTextLineCount() > 1) {
							LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
									ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 0);
							filterTabButton.setLayoutParams(lp);
						}
					}
				});
			}

			mTabBarRootLinearLayout.setVisibility(View.VISIBLE);
		}
		else {
			mTabBarRootLinearLayout.setVisibility(View.GONE);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState != null) {
			final int[] position = savedInstanceState.getIntArray(KEY_STATE_SCROLLVIEW);
			if (position != null) {
				mTabBarHorizontalScrollView.post(new Runnable() {
					@Override
					public void run() {
						mTabBarHorizontalScrollView.scrollTo(position[0], position[1]);
					}
				});
			}
		}

		return fragmentView;
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewStateRestored: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Get latest location for sorting, if inside the geofence
		Location lastLocation = getUserLocationIfInResortGeofence();

		// Set pager adapter based on filter tab options (after the buttons have restored their state)
		mFilterOptions.setUserLocation(lastLocation);
		mDatabaseQueryList = createDatabaseQuery(mFilterOptions, false, mExploreType, lastLocation);
        mDatabaseQueryMap = createDatabaseQuery(mFilterOptions, mIsRestroomToggleOn, mExploreType, lastLocation);
		mExploreFragmentPagerAdapter = new ExploreFragmentPagerAdapter(mViewPager, getChildFragmentManager(), mDatabaseQueryList, mDatabaseQueryMap, mExploreType, mSelectedPoiObjectJson);
		mViewPager.setAdapter(mExploreFragmentPagerAdapter);

		// Enable action bar items after creating the pager adapter, because the
		// menu options rely on the state of the adapter
		setHasOptionsMenu(true);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			mCurrentViewPagerTab = mExploreFragmentPagerAdapter.getPagePosition(getDefaultPage(mExploreType));
			if (mCurrentViewPagerTab >= 0) {
				mViewPager.setCurrentItem(mCurrentViewPagerTab);
			}
		}
		// Otherwise, restore state
		else {
			mCurrentViewPagerTab = savedInstanceState.getInt(KEY_STATE_CUR_VIEWPAGER_TAB);
		}

        shouldShowCount = false;
        mSearchResultsFragment.resetView();
    }

	@Override
	public void onStart() {
		super.onStart();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStart");
		}

		// Get a connection to user location
		mGoogleApiClient.connect();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onResume");
		}

        shouldShowCount = false;
        mSearchResultsFragment.resetView();

		// Listen for state changes
		UniversalAppStateManager.registerStateChangeListener(this);
		
		// Toggle filters
		toggleFilters();
	}

	@Override
	public void onPause() {
		super.onPause();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onPause");
		}

		// Stop listening for state changes
		UniversalAppStateManager.unregisterStateChangeListener(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onSaveInstanceState");
		}

		if (mTabBarHorizontalScrollView != null) {
			int[] scrollViewState = new int[]{ mTabBarHorizontalScrollView.getScrollX(), mTabBarHorizontalScrollView.getScrollY()};
			outState.putIntArray("KEY_STATE_SCROLLVIEW", scrollViewState);
		}
		outState.putInt(KEY_STATE_CUR_VIEWPAGER_TAB, mCurrentViewPagerTab);
		outState.putBoolean(KEY_STATE_IS_RESTROOM_TOGGLE_ON, mIsRestroomToggleOn);
	}

	@Override
	public void onStop() {
		super.onStop();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStop");
		}

		// Stop listening to user location
		mGoogleApiClient.disconnect();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroyView");
		}

		// Release memory references to prevent context leaks
		if (mExploreFragmentPagerAdapter != null) {
			mExploreFragmentPagerAdapter.destroy();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreateOptionsMenu");
		}

		// Adds items to the action bar, if those pages exist
		if (mExploreFragmentPagerAdapter != null) {
			if (mCurrentViewPagerTab == mExploreFragmentPagerAdapter.getPagePosition(ExploreFragmentPagerAdapter.PAGE_MAP)
					&& mExploreFragmentPagerAdapter.getPagePosition(ExploreFragmentPagerAdapter.PAGE_LIST) >= 0) {
				inflater.inflate(R.menu.action_list, menu);
			}
			else if (mCurrentViewPagerTab == mExploreFragmentPagerAdapter.getPagePosition(ExploreFragmentPagerAdapter.PAGE_LIST)
					&& mExploreFragmentPagerAdapter.getPagePosition(ExploreFragmentPagerAdapter.PAGE_MAP) >= 0) {
				inflater.inflate(R.menu.action_map, menu);
			}
		}

		TintUtils.tintAllMenuItems(menu, getContext());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (BuildConfig.DEBUG) {
            Log.d(TAG, "onActivityResult: requestCode: " + requestCode + " resultCode: " + resultCode);
        }
	    
	    if(Activity.RESULT_OK == resultCode && REQUEST_CODE_FILTER == requestCode) {
	        Bundle extras = data.getExtras();
	        if(extras != null) {
                mFilterOptions = GsonObject.fromJson(
                        extras.getString(AdvancedFilterActivity.KEY_ARG_RESULT_FILTER_OPTIONS),
                        FilterOptions.class);
	            // Re-attach filter tab buttons
                if(mFilterOptions.getFilterTabOptions() != null) {
                    for (FilterTabOption filterTabOption : mFilterOptions.getFilterTabOptions()) {
                        filterTabOption.setFilterTabButton((FilterTabButton) mTabBarLinearLayout
                                .findViewById(filterTabOption.getId()));
                        if (filterTabOption.getFilterTabButton() != null) {
                            filterTabOption.getFilterTabButton().setChecked(filterTabOption.isChecked());
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG,
                                        "onActivityResult: Setting filter tab state: "
                                                + filterTabOption.getText() + " = " + filterTabOption.isChecked());
                            }
                        }
                    }
                }
                mFilterTabOptions = mFilterOptions.getFilterTabOptions();
                // Force a re-query with new options
                Location lastLocation = mFilterOptions.getUserLocation();
                mDatabaseQueryList = createDatabaseQuery(mFilterOptions, false, mExploreType, lastLocation);
                mDatabaseQueryMap = createDatabaseQuery(mFilterOptions, mIsRestroomToggleOn, mExploreType,
                        lastLocation);
                for (int position = 0; position < mExploreFragmentPagerAdapter.getCount(); position++) {
                    Fragment fragment = mExploreFragmentPagerAdapter.getPagerFragment(position);
                    if (fragment != null && fragment instanceof OnDatabaseQueryChangeListener) {
						if (fragment instanceof ExploreMapFragment) {
                            ((OnDatabaseQueryChangeListener) fragment)
                                    .onDatabaseQueryChange(mDatabaseQueryMap);

                        } else {
                            ((OnDatabaseQueryChangeListener) fragment)
                                    .onDatabaseQueryChange(mDatabaseQueryList);
                        }
                    }
                }

                if(shouldShowCount) {
                    shouldShowCount = false;
                    if (mCurrentViewPagerTab == ExploreFragmentPagerAdapter.PAGE_LIST) {
                        mSearchResultsFragment.onDatabaseQueryChange(mDatabaseQueryList);
                    } else {
                        mSearchResultsFragment.onDatabaseQueryChange(mDatabaseQueryMap);
                    }
                }

            }
	    } else {
	        if(BuildConfig.DEBUG) {
	            Log.w(TAG, "onActivityResult: Unknown result code");
	        }
	    }
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onPrepareOptionsMenu");
		}

		// If the menu drawer is open, hide action items related to the main
		// content view, and show action items specific to the menu
		if (mParentDrawerStateProvider != null) {
			boolean isDrawerOpenAtAll = mParentDrawerStateProvider.isDrawerOpenAtAll();

			MenuItem menuItem;
			menuItem = menu.findItem(R.id.action_map);
			if (menuItem != null) {
				menuItem.setVisible(!isDrawerOpenAtAll).setEnabled(!isDrawerOpenAtAll);
			}
			menuItem = menu.findItem(R.id.action_list);
			if (menuItem != null) {
				menuItem.setVisible(!isDrawerOpenAtAll).setEnabled(!isDrawerOpenAtAll);
			}
			// Only show advanced filter if it's enabled
			menuItem = menu.findItem(R.id.action_advanced_filter);
			if (menuItem != null) {
				boolean showAdvancedFilter = !isDrawerOpenAtAll && FilterOptions.isAdvancedFilterAvailable(mExploreType);
				menuItem.setVisible(showAdvancedFilter).setEnabled(showAdvancedFilter);
			}
		}

		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onOptionsItemSelected");
		}

		switch (item.getItemId()) {
			case R.id.action_map:
				mViewPager.setCurrentItem(mExploreFragmentPagerAdapter.getPagePosition(ExploreFragmentPagerAdapter.PAGE_MAP), true);
				return true;
			case R.id.action_list:
				mViewPager.setCurrentItem(mExploreFragmentPagerAdapter.getPagePosition(ExploreFragmentPagerAdapter.PAGE_LIST), true);
				return true;
			case R.id.action_advanced_filter:
			    launchAdvancedFilter();
			    return true;
			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onOptionsItemSelected: unknown menu item selected");
				}
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onClick");
		}
		
		// Handle clicks for hidden filter buttons
		switch(v.getId()) {
		    case R.id.fragment_explore_filter_tab_button_edit_filter:
		        launchAdvancedFilter();
		        // Keep button checked
		        ((FilterTabButton)v).setChecked(true);
		        // Return since there is no need to re-query
		        return;
		    case R.id.fragment_explore_filter_tab_button_clear_filter:
		        resetFilters();
		        // Keep button checked
		        ((FilterTabButton)v).setChecked(true);
		        break;
		}

		if (mExploreFragmentPagerAdapter != null) {
			// Get latest location for sorting, if inside the geofence
			Location lastLocation = getUserLocationIfInResortGeofence();
			
			// Update the filter tab option state for clicked tab
			if(v instanceof FilterTabButton) {
			    if(mFilterTabOptions != null) {
        			for(FilterTabOption filterTabOption : mFilterTabOptions) {
        			    if(v.getId() == filterTabOption.getId()) {
        			        filterTabOption.setChecked(((FilterTabButton)v).isChecked());
        			        break;
        			    }
        			}
			    }
			}

			// The filter options have changed, so update the database queries
			mFilterOptions.setFilterTabOptions(mFilterTabOptions);
			mFilterOptions.setUserLocation(lastLocation);
			mDatabaseQueryList = createDatabaseQuery(mFilterOptions, false, mExploreType, lastLocation);
            mDatabaseQueryMap = createDatabaseQuery(mFilterOptions, mIsRestroomToggleOn, mExploreType, lastLocation);

			// Update the current fragments to use the new filter options
			mExploreFragmentPagerAdapter.setDatabaseQueryList(mDatabaseQueryList);
			mExploreFragmentPagerAdapter.setDatabaseQueryMap(mDatabaseQueryMap);
			for (int position = 0; position < mExploreFragmentPagerAdapter.getCount(); position++) {
				Fragment fragment = mExploreFragmentPagerAdapter.getPagerFragment(position);
				if (fragment != null && fragment instanceof OnDatabaseQueryChangeListener) {
					if (fragment instanceof ExploreMapFragment) {
						((OnDatabaseQueryChangeListener) fragment).onDatabaseQueryChange(mDatabaseQueryMap);
					}
					else {
						((OnDatabaseQueryChangeListener) fragment).onDatabaseQueryChange(mDatabaseQueryList);
					}
				}
			}
        }
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCheckedChanged: isChecked = " + isChecked);
		}

		switch (buttonView.getId()) {
			case R.id.view_floating_action_toggle_restroom_map:
				toggleRestrooms(isChecked);
				break;
			default:
				break;
		}
	}

	private void toggleRestrooms(boolean showRestrooms) {

		if (mExploreFragmentPagerAdapter != null) {
			// Get latest location for sorting, if inside the geofence
			Location lastLocation = getUserLocationIfInResortGeofence();
			// The restroom toggle has changed, so update the map database query
			mIsRestroomToggleOn = showRestrooms;
			AppPreferenceManager.setRestroomFiltersOn(mIsRestroomToggleOn);
			mFilterOptions.setUserLocation(lastLocation);
			mDatabaseQueryMap = createDatabaseQuery(mFilterOptions, mIsRestroomToggleOn, mExploreType, lastLocation);

			// Update the current map fragments to use the new restroom toggle state
			mExploreFragmentPagerAdapter.setDatabaseQueryMap(mDatabaseQueryMap);
			for (int position = 0; position < mExploreFragmentPagerAdapter.getCount(); position++) {
				Fragment fragment = mExploreFragmentPagerAdapter.getPagerFragment(position);
				if (fragment != null && fragment instanceof OnDatabaseQueryChangeListener) {
					if (fragment instanceof ExploreMapFragment) {
						((ExploreMapFragment) fragment).mQueryReturned = false;
						((OnDatabaseQueryChangeListener) fragment).onDatabaseQueryChange(mDatabaseQueryMap);
					}
				}
			}
		}
	}

	@Override
	public DatabaseQuery provideDatabaseQuery(Object requester) {
		if (requester != null && requester instanceof DatabaseQueryMapFragment) {
			return mDatabaseQueryMap;
		}
		else {
			return mDatabaseQueryList;
		}
	}

	@Override
	public String provideTitle() {
		return getString(mActionBarTitleResId);
	}

	@Override
	public void onPageScrollStateChanged(int state) {}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

	@Override
	public void onPageSelected(int position) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onPageSelected: " + position);
		}
		mCurrentViewPagerTab = position;

		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			parentActivity.invalidateOptionsMenu();
		}
	}

	@Override
	public void onUniversalAppStateChange(UniversalAppState universalAppState) {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			parentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Update list sorting based on geofence
                        updateDatabaseQueriesBasedOnGeofence();
                    } catch (Exception e) {
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG, "onUniversalAppStateChange: exception trying to refresh UI", e);
                        }

                        // Log the exception to crittercism
                        Crittercism.logHandledException(e);
                    }
                }
            });
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onConnected");
		}

		// Update list sorting based on geofence
		updateDatabaseQueriesBasedOnGeofence();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (BuildConfig.DEBUG) {
			Log.e(TAG, "onConnectionFailed: connectionResult.getErrorCode() = " + connectionResult.getErrorCode());
		}
	}

	@Override
	public void onConnectionSuspended(int i) {}

	@Override
	public void selectPoi(int poiHashCode) {

		if (mExploreFragmentPagerAdapter != null) {
			// Switch over to the map
			int mapPagePos = mExploreFragmentPagerAdapter.getPagePosition(ExploreFragmentPagerAdapter.PAGE_MAP);
			mViewPager.setCurrentItem(mapPagePos);

			// Click on the POI
			Fragment mapFragment = mExploreFragmentPagerAdapter.getPagerFragment(mapPagePos);
			if (mapFragment != null && mapFragment instanceof PoiSelector) {
				((PoiSelector) mapFragment).selectPoi(poiHashCode);
			}
		}
	}

	public static void trackPageView(ExploreType exploreType, String contentSub2) {
		String activityTypeSub1 = null;
		String guestServicesSub1 = null;

		if (exploreType != null) {
			switch (exploreType) {
				case ISLANDS_OF_ADVENTURE:
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_PLANNING,
							AnalyticsUtils.CONTENT_FOCUS_IOA,
							null,
							contentSub2,
							AnalyticsUtils.PROPERTY_NAME_IOA,
							null, null);
					break;
				case UNIVERSAL_STUDIOS_FLORIDA:
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_PLANNING,
							AnalyticsUtils.CONTENT_FOCUS_USF,
							null,
							contentSub2,
							AnalyticsUtils.PROPERTY_NAME_USF,
							null, null);
					break;
				case CITY_WALK_ORLANDO:
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_PLANNING,
							AnalyticsUtils.CONTENT_FOCUS_CW,
							null,
							contentSub2,
							AnalyticsUtils.PROPERTY_NAME_CW,
							null, null);
					break;
				case HOTELS:
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_PLANNING,
							AnalyticsUtils.CONTENT_FOCUS_HOTELS,
							null,
							contentSub2,
							AnalyticsUtils.PROPERTY_NAME_HOTELS,
							null, null);
					break;
				case UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS:
				case UNIVERSAL_STUDIOS_HOLLYWOOD_DINING:
				case UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING:
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_PLANNING,
							AnalyticsUtils.CONTENT_FOCUS_USH,
							null,
							contentSub2,
							AnalyticsUtils.PROPERTY_NAME_USH,
							null, null);
					break;
				case CITY_WALK_HOLLYWOOD:
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_PLANNING,
							AnalyticsUtils.CONTENT_FOCUS_CWH,
							null,
							contentSub2,
							AnalyticsUtils.PROPERTY_NAME_CWH,
							null, null);
					break;
				case RIDES:
				case SHOWS:
				case DINING:
				case SHOPPING:
					activityTypeSub1 = AnalyticsUtils.getActivityTypeName(exploreType);
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_PLANNING,
							AnalyticsUtils.CONTENT_FOCUS_UOR,
							activityTypeSub1,
							contentSub2,
							AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
							null, null);
					break;
				case RESTROOMS:
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_PLANNING,
							AnalyticsUtils.CONTENT_FOCUS_RESTROOMS,
							null,
							contentSub2,
							AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
							null, null);
					break;
				case LOCKERS:
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_PLANNING,
							AnalyticsUtils.CONTENT_FOCUS_LOCKERS,
							null,
							contentSub2,
							AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
							null, null);
					break;
				case GUEST_SERVICES_BOOTHS:
				case ATMS:
				case FIRST_AID:
				case LOST_AND_FOUND:
				case SERVICE_ANIMAL_REST_AREAS:
				case SMOKING_AREAS:
					guestServicesSub1 = AnalyticsUtils.getGuestServicesName(exploreType);
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_PLANNING,
							AnalyticsUtils.CONTENT_FOCUS_GUEST_SERVICES,
							guestServicesSub1,
							contentSub2,
							AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
							null, null);
					break;
				case SHOPPING_EXPRESS_PASS:
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_TICKETS,
							AnalyticsUtils.CONTENT_FOCUS_EXPRESS_PASS,
							contentSub2,
							contentSub2,
							AnalyticsUtils.PROPERTY_NAME_PARKS,
							null, null);
					break;
				case FAVORITES:
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_PLANNING,
							null,
							null,
							AnalyticsUtils.CONTENT_SUB_2_FAVORITES,
							AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
							null, null);
					break;
				default:
					break;
			}
		}
	}

	private void updateDatabaseQueriesBasedOnGeofence() {
		if (mExploreFragmentPagerAdapter != null) {
			// Get latest location for sorting, if inside the geofence
			Location userLocation = getUserLocationIfInResortGeofence();

			// The filter options have changed, so update the database queries
			mFilterOptions.setUserLocation(userLocation);
			mDatabaseQueryList = createDatabaseQuery(mFilterOptions, false, mExploreType, userLocation);
            mDatabaseQueryMap = createDatabaseQuery(mFilterOptions, mIsRestroomToggleOn, mExploreType, userLocation);

			// Update the current fragments to use the new filter options
			mExploreFragmentPagerAdapter.setDatabaseQueryList(mDatabaseQueryList);
			mExploreFragmentPagerAdapter.setDatabaseQueryMap(mDatabaseQueryMap);
			for (int position = 0; position < mExploreFragmentPagerAdapter.getCount(); position++) {
				Fragment fragment = mExploreFragmentPagerAdapter.getPagerFragment(position);
				if (fragment != null && fragment instanceof OnDatabaseQueryChangeListener) {
					if (fragment instanceof ExploreMapFragment) {
						((OnDatabaseQueryChangeListener) fragment).onDatabaseQueryChange(mDatabaseQueryMap);
					}
					else {
						((OnDatabaseQueryChangeListener) fragment).onDatabaseQueryChange(mDatabaseQueryList);
					}
				}
			}
		}
	}

	private Location getUserLocationIfInResortGeofence() {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			if (UniversalAppStateManager.getInstance().isInResortGeofence()) {
				if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
					try {
						Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
						if (lastLocation != null) {
							return lastLocation;
						}
					} catch (SecurityException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	private int getDefaultPage(ExploreType exploreType) {
		// If talkback is on, default to the list page since it is easier to navigate
		if (AccessibilityUtils.isTalkBackEnabled()) {
			return ExploreFragmentPagerAdapter.PAGE_LIST;
		}

		switch (exploreType) {
			case FAVORITES:
			case WAIT_TIMES:
				return ExploreFragmentPagerAdapter.PAGE_LIST;
			default:
				return ExploreFragmentPagerAdapter.PAGE_MAP;
		}
	}
	
	private FilterOptions createFilterOptions(List<FilterTabOption> filterTabOptions, ExploreType exploreType) {
	    FilterOptions filterOptions = new FilterOptions(filterTabOptions, exploreType);
	    
	    return filterOptions;
	}

	private List<FilterTabOption> createFilterTabOptions(ExploreType exploreType) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "createFilterTabOptions: exploreType = " + exploreType.getValue());
		}

		List<FilterTabOption> filterBarTabOptions;

		switch (exploreType) {
			case ISLANDS_OF_ADVENTURE:
				filterBarTabOptions = new ArrayList<FilterTabOption>();
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_rides),
						R.drawable.ic_tab_filter_rides, true,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_shows),
						R.drawable.ic_tab_filter_shows, false,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_dine),
						R.drawable.ic_tab_filter_dining, false,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_DINING));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_shop),
						R.drawable.ic_tab_filter_shop, false,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP));

				return filterBarTabOptions;

            case VOLCANO_BAY:
			case UNIVERSAL_STUDIOS_FLORIDA:
				filterBarTabOptions = new ArrayList<FilterTabOption>();
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_rides),
						R.drawable.ic_tab_filter_rides, true,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_shows),
						R.drawable.ic_tab_filter_shows, false,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_dine),
						R.drawable.ic_tab_filter_dining, false,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_DINING));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_shop),
						R.drawable.ic_tab_filter_shop, false,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP));
				return filterBarTabOptions;

			case CITY_WALK_ORLANDO:
				filterBarTabOptions = new ArrayList<FilterTabOption>();
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_entertainment),
						R.drawable.ic_tab_filter_entertainment, true,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_shows),
						R.drawable.ic_tab_filter_shows, true,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_dine),
						R.drawable.ic_tab_filter_dining, false,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_DINING));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_shop),
						R.drawable.ic_tab_filter_shop, false,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP));
				return filterBarTabOptions;

			case UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS:
				filterBarTabOptions = new ArrayList<FilterTabOption>();
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_attractions),
						R.drawable.ic_tab_filter_rides, true,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_dine),
						R.drawable.ic_tab_filter_dining, false,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_DINING));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_shop),
						R.drawable.ic_tab_filter_shop, false,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP));
				return filterBarTabOptions;

			case UNIVERSAL_STUDIOS_HOLLYWOOD_DINING:
				filterBarTabOptions = new ArrayList<FilterTabOption>();
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_attractions),
						R.drawable.ic_tab_filter_rides, false,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_dine),
						R.drawable.ic_tab_filter_dining, true,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_DINING));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_shop),
						R.drawable.ic_tab_filter_shop, false,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP));
				return filterBarTabOptions;

			case UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING:
				filterBarTabOptions = new ArrayList<FilterTabOption>();
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_attractions),
						R.drawable.ic_tab_filter_rides, false,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_dine),
						R.drawable.ic_tab_filter_dining, false,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_DINING));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_shop),
						R.drawable.ic_tab_filter_shop, true,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP));
				return filterBarTabOptions;

			case CITY_WALK_HOLLYWOOD:
				filterBarTabOptions = new ArrayList<FilterTabOption>();
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_dine),
						R.drawable.ic_tab_filter_dining, true,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_DINING));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_shop),
						R.drawable.ic_tab_filter_shop, false,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_entertainment),
						R.drawable.ic_tab_filter_entertainment, false,
						PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
						"" + PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT));
				return filterBarTabOptions;

			case RIDES:
			case WAIT_TIMES:
				filterBarTabOptions = new ArrayList<FilterTabOption>();
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_thrill),
						R.drawable.ic_tab_filter_rides, true,
						PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
						"" + PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_THRILL));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_3d_and_4d),
						R.drawable.ic_tab_filter_rides_3d, true,
						PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
						"" + PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_VIDEO_3D_4D));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_water),
						R.drawable.ic_tab_filter_rides_water, true,
						PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
						"" + PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_WATER));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_kids),
						R.drawable.ic_tab_filter_rides_kids, true,
						PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
						"" + PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_KID_FRIENDLY));
				return filterBarTabOptions;

			case SHOWS:
				filterBarTabOptions = new ArrayList<FilterTabOption>();
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_action),
						R.drawable.ic_tab_filter_shows_action, true,
						PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
						"" + PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_ACTION));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_comedy),
						R.drawable.ic_tab_filter_shows_comedy, true,
						PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
						"" + PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_COMEDY));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_music),
						R.drawable.ic_tab_filter_shows_music, true,
						PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
						"" + PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_MUSIC));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_parade),
						R.drawable.ic_tab_filter_shows_parade, true,
						PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
						"" + PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_PARADE));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_character),
						R.drawable.ic_tab_filter_shows_character, true,
						PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
						"" + PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_CHARACTER));
				return filterBarTabOptions;

			case DINING:
				filterBarTabOptions = new ArrayList<FilterTabOption>();
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_fine),
						R.drawable.ic_tab_filter_dining_fine, true,
						PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
						"" + PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_FINE_DINING));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_casual),
						R.drawable.ic_tab_filter_dining_casual, true,
						PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
						"" + PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_CASUAL_DINING));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_quick_service),
						R.drawable.ic_tab_filter_dining_quick, true,
						PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
						"" + PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_QUICK_SERVICE));
				return filterBarTabOptions;

			case RESTROOMS:
				filterBarTabOptions = new ArrayList<FilterTabOption>();
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_restrooms),
						R.drawable.ic_tab_filter_restroom, true,
						PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
						"" + PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RESTROOM_TYPE_STANDARD));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_family_restrooms),
						R.drawable.ic_tab_filter_restroom_family, true,
						PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
						"" + PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RESTROOM_TYPE_FAMILY));
				return filterBarTabOptions;

			case LOCKERS:
				filterBarTabOptions = new ArrayList<FilterTabOption>();
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_rental_lockers),
						R.drawable.ic_tab_filter_lockers_rental, true,
						PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
						"" + PointsOfInterestTable.VAL_SUB_TYPE_FLAG_LOCKER_TYPE_RENTAL));
				filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_ride_lockers),
						R.drawable.ic_tab_filter_lockers_ride, true,
						PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
						"" + PointsOfInterestTable.VAL_SUB_TYPE_FLAG_LOCKER_TYPE_RIDE));
				return filterBarTabOptions;
			case ATTRACTIONS_EXPRESS_PASS:
			    filterBarTabOptions = new ArrayList<FilterTabOption>();
			    filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_rides),
                        R.drawable.ic_tab_filter_rides, true,
                        PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
                        "" + PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE));
                filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_shows),
                        R.drawable.ic_tab_filter_shows, true,
                        PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
                        "" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW,
                        "" + PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE));
                return filterBarTabOptions;
			case WET_N_WILD:
			    filterBarTabOptions = new ArrayList<FilterTabOption>();
                filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_rides),
                        R.drawable.ic_tab_filter_rides, true,
                        PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
                        "" + PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE));
                filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_dine),
                        R.drawable.ic_tab_filter_dining, false,
                        PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
                        "" + PointsOfInterestTable.VAL_POI_TYPE_ID_DINING));
                filterBarTabOptions.add(new FilterTabOption(getString(R.string.explore_filter_tab_shop),
                        R.drawable.ic_tab_filter_shop, false,
                        PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
                        "" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP));
                return filterBarTabOptions;
			default:
				return null;
		}
	}
	
    private void launchAdvancedFilter() {
        shouldShowCount = true;
        mSearchResultsFragment.resetView();
        // Add latest location to filter options
        mFilterOptions.setUserLocation(getUserLocationIfInResortGeofence());
        Bundle extras = AdvancedFilterActivity.newInstanceBunde(mFilterOptions, mExploreType);
        Intent filterIntent = new Intent(getActivity(), AdvancedFilterActivity.class);
        filterIntent.putExtras(extras);
        startActivityForResult(filterIntent, REQUEST_CODE_FILTER);
    }
    
    private void resetFilters() {
        shouldShowCount = false;
        mSearchResultsFragment.resetView();
        mFilterOptions = FilterOptions.resetFilter(mFilterOptions);
        toggleFilters();
    }
    
    private void toggleFilters() {
        if (mFilterOptions.isFilterComplex()) {
            mTabBarRootLinearLayout.setVisibility(View.VISIBLE);
            mFilterRootLinearLayout.setVisibility(View.VISIBLE);
            mTabBarLinearLayout.setVisibility(View.GONE);
        } else {
            mFilterRootLinearLayout.setVisibility(View.GONE);
            mTabBarLinearLayout.setVisibility(View.VISIBLE);

            // If there are no filter tabs hide the tab bar
            if (mFilterTabOptions == null || mFilterTabOptions.isEmpty()) {
                mTabBarRootLinearLayout.setVisibility(View.GONE);
            }
        }
    }
	
	private static DatabaseQuery createDatabaseQuery(FilterOptions filterOptions, boolean isRestroomTabOn, ExploreType exploreType,
            Location userLocation) {
	    if (BuildConfig.DEBUG) {
            Log.d(TAG, "createDatabaseQuery: exploreType = " + exploreType.getValue());
        }
	    switch (exploreType) {
            case ISLANDS_OF_ADVENTURE:
                return DatabaseQueryUtils.getExploreByVenueDatabaseQuery(filterOptions,
                        isRestroomTabOn,
                        VenuesTable.VAL_VENUE_ID_ISLANDS_OF_ADVENTURE);
            case UNIVERSAL_STUDIOS_FLORIDA:
                return DatabaseQueryUtils.getExploreByVenueDatabaseQuery(filterOptions,
                        isRestroomTabOn,
                        VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_FLORIDA);
            case CITY_WALK_ORLANDO:
                return DatabaseQueryUtils.getExploreByVenueDatabaseQuery(filterOptions,
                        isRestroomTabOn,
                        VenuesTable.VAL_VENUE_ID_CITY_WALK_ORLANDO);
			case UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS:
			case UNIVERSAL_STUDIOS_HOLLYWOOD_DINING:
			case UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING:
				return DatabaseQueryUtils.getExploreByVenueDatabaseQuery(filterOptions,
						isRestroomTabOn,
						VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD);
			case CITY_WALK_HOLLYWOOD:
				return DatabaseQueryUtils.getExploreByVenueDatabaseQuery(filterOptions,
						isRestroomTabOn,
						VenuesTable.VAL_VENUE_ID_CITY_WALK_HOLLYWOOD);
            case RIDES:
                return DatabaseQueryUtils.getExploreByActivityDatabaseQuery(filterOptions,
                        false,
                        false,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE);
            case WAIT_TIMES:
                return DatabaseQueryUtils.getExploreByActivityDatabaseQuery(filterOptions,
                        false,
                        true,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE);
            case SHOWS:
                return DatabaseQueryUtils.getExploreByActivityDatabaseQuery(filterOptions,
                        false,
                        false,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE);
            case UNIVERSAL_DINING:
            case UNIVERSAL_DINING_QUICK_SERVICE:
            case DINING:
                return DatabaseQueryUtils.getExploreByActivityDatabaseQuery(filterOptions,
                        false,
                        false,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_DINING);
            case SHOPPING:
                return DatabaseQueryUtils.getExploreByActivityDatabaseQuery(filterOptions,
                        false,
                        false,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP);
            case SHOPPING_EXPRESS_PASS:
                return DatabaseQueryUtils.getExploreByActivityDatabaseQuery(filterOptions,
                        true,
                        false,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP);
            case RESTROOMS:
                return DatabaseQueryUtils.getExploreByActivityDatabaseQuery(filterOptions,
                        false,
                        false,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_RESTROOM);
            case LOCKERS:
                return DatabaseQueryUtils.getExploreByActivityDatabaseQuery(filterOptions,
                        false,
                        false,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_LOCKERS);
			case RENTAL_SERVICES:
				return DatabaseQueryUtils.getExploreByActivityDatabaseQuery(filterOptions,
						false,
						false,
						PointsOfInterestTable.VAL_POI_TYPE_ID_RENTALS);
            case GUEST_SERVICES_BOOTHS:
                return DatabaseQueryUtils.getExploreByActivityDatabaseQuery(filterOptions,
                        false,
                        false,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_GUEST_SERVICES_BOOTH);
            case ATMS:
                return DatabaseQueryUtils.getExploreByActivityDatabaseQuery(filterOptions,
                        false,
                        false,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_ATM);
            case CHARGING_STATIONS:
                return DatabaseQueryUtils.getExploreByActivityDatabaseQuery(filterOptions,
                        false,
                        false,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_CHARGING_STATION);
            case LOST_AND_FOUND:
                return DatabaseQueryUtils.getExploreByActivityDatabaseQuery(filterOptions,
                        false,
                        false,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_LOST_AND_FOUND_STATION);
            case FIRST_AID:
                return DatabaseQueryUtils.getExploreByActivityDatabaseQuery(filterOptions,
                        false,
                        false,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_FIRST_AID_STATION);
            case SERVICE_ANIMAL_REST_AREAS:
                return DatabaseQueryUtils.getExploreByActivityDatabaseQuery(filterOptions,
                        false,
                        false,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_SERVICE_ANIMAL_REST_AREA);
            case SMOKING_AREAS:
                return DatabaseQueryUtils.getExploreByActivityDatabaseQuery(filterOptions,
                        false,
                        false,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_SMOKING_AREA);
            case HOTELS:
                return DatabaseQueryUtils.getExploreByActivityDatabaseQuery(filterOptions,
                        false,
                        false,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL);
            case FAVORITES:
                return DatabaseQueryUtils.getExploreByFavoritesDatabaseQuery(userLocation);
            case ATTRACTIONS_EXPRESS_PASS:
                return DatabaseQueryUtils.getExploreByPoiTypeDatabaseQuery(filterOptions, false, false,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE,
                        PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW);
			case ATTRACTIONS_PACKAGE_PICKUP:
				return DatabaseQueryUtils.getExploreByPoiTypeDatabaseQuery(filterOptions, false, false,
						PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP);
            case WET_N_WILD:
                return DatabaseQueryUtils.getExploreByVenueDatabaseQuery(filterOptions, isRestroomTabOn,
                        VenuesTable.VAL_VENUE_ID_WET_N_WILD);
			case VOLCANO_BAY:
                return DatabaseQueryUtils.getExploreByVenueDatabaseQuery(filterOptions, isRestroomTabOn,
                        VenuesTable.VAL_VENUE_ID_VOLCANO_BAY);
            default:
                return null;
        }
	}
}
