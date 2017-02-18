package com.universalstudios.orlandoresort.controller.userinterface.account;


import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerClickHandler;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestPreference;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.GetGuestPreferencesRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.GetGuestProfileRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.UpdateCommunicationPrefsRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetGuestPreferencesResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetGuestProfileResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.UpdateCommunicationPrefsResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UpdateInterestsFragment extends NetworkFragment {

    private static final String TAG = UpdateInterestsFragment.class.getSimpleName();

    private static final String KEY_ARG_ACTION_BAR_TITLE_STRING = "key_arg_action_bar_title_string";
    private static final String KEY_STATE_IS_CALL_IN_PROGRESS = "key_state_is_call_in_progress";

    private DrawerClickHandler mParentDrawerClickHandler;
    private String mActionBarTitleString;

    private TridionConfig mTridionConfig;
    private GuestProfile mGuestProfile;

    private boolean mIsCallInProgress;

    private ViewGroup mContainer;
    private ProgressBar mLoading;
    private RecyclerView mInterests;
    private RecyclerView mSeasons;
    private GuestInterestsAdapter mInterestsAdapter;
    private GuestSeasonsAdapter mSeasonsAdapter;

    public UpdateInterestsFragment() {
    }

    public static UpdateInterestsFragment newInstance(String actionBarTitleString) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance: actionBarTitleResId = " + actionBarTitleString);
        }

        // Create a new fragment instance
        UpdateInterestsFragment fragment = new UpdateInterestsFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        // Add parameters to the argument bundle
        args.putString(KEY_ARG_ACTION_BAR_TITLE_STRING, actionBarTitleString);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAttach");
        }
        Fragment parentFragment = getParentFragment();
        // Check if parent fragment (if there is one) implements the interface
        if (parentFragment != null && parentFragment instanceof DrawerClickHandler) {
            mParentDrawerClickHandler = (DrawerClickHandler) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (activity != null && activity instanceof DrawerClickHandler) {
            mParentDrawerClickHandler = (DrawerClickHandler) activity;
        }
        // If neither implements the interface, log a warning
        else if (mParentDrawerClickHandler == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement DrawerClickHandler");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        if (args == null) {
            mActionBarTitleString = "";
        }
        // Otherwise, set incoming parameters
        else {
            mActionBarTitleString = args.getString(KEY_ARG_ACTION_BAR_TITLE_STRING);
        }

        if (savedInstanceState != null) {
            mIsCallInProgress = savedInstanceState.getBoolean(KEY_STATE_IS_CALL_IN_PROGRESS, false);
        } else {
            mIsCallInProgress = false;
        }

        mTridionConfig = IceTicketUtils.getTridionConfig();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView");
        }

        com.universalstudios.orlandoresort.databinding.FragmentUpdateInterestsBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_update_interests, container, false);
        binding.setTridion(mTridionConfig);
        View view = binding.getRoot();

        mContainer = binding.fragmentUpdatePreferencesContainer;
        mLoading = binding.fragmentUpdatePreferencesLoading;
        mInterests = binding.fragmentUpdateInterests;
        mSeasons = binding.fragmentUpdateSeasons;


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mInterests.setLayoutManager(layoutManager);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSeasons.setLayoutManager(gridLayoutManager);

        // Set the action bar title, if the drawer isn't open
        Activity parentActivity = getActivity();
        if (parentActivity != null && parentActivity.getActionBar() != null) {
            parentActivity.getActionBar().setTitle(mActionBarTitleString);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_STATE_IS_CALL_IN_PROGRESS, mIsCallInProgress);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewCreated");
        }

        requestUserProfile();
        showViewBasedOnState();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateOptionsMenu");
        }

        inflater.inflate(R.menu.action_update, menu);
        String itemText = mTridionConfig.getUpdateLabel();
        if (!TextUtils.isEmpty(itemText)) {
            MenuItem item = menu.findItem(R.id.action_update);
            item.setTitle(mTridionConfig.getUpdateLabel());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onOptionsItemSelected");
        }

        switch (item.getItemId()) {
            case R.id.action_update:
                updateCommunicationPrefs();
                showViewBasedOnState();
                return true;
            default:
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "onOptionsItemSelected: unknown menu item selected");
                }
                return super.onOptionsItemSelected(item);
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
    public void onDetach() {
        super.onDetach();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onDetach");
        }
    }

    private void showViewBasedOnState() {
        if (mIsCallInProgress) {
            showLoadingView();
        } else {
            showPreferencesView();
        }
    }

    private void showLoadingView() {
        mContainer.setVisibility(View.GONE);
        mLoading.setVisibility(View.VISIBLE);
    }

    private void showPreferencesView() {
        mContainer.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
    }

    public void requestUserProfile() {
        if (mIsCallInProgress) {
            return;
        }

        mIsCallInProgress = true;

        GetGuestProfileRequest request = new GetGuestProfileRequest.Builder(this)
                .build();

        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    private void requestMyPreferences() {
        //do not make call if it is already in progress
        if (mIsCallInProgress) {
            return;
        }

        mIsCallInProgress = true;

        GetGuestPreferencesRequest getGuestPreferencesRequest = new GetGuestPreferencesRequest.Builder(this)
                .build();

        NetworkUtils.queueNetworkRequest(getGuestPreferencesRequest);
        NetworkUtils.startNetworkService();
    }

    private void updateCommunicationPrefs() {
        //do not make call if it is already in progress
        if (mIsCallInProgress) {
            return;
        }

        mIsCallInProgress = true;

        UpdateCommunicationPrefsRequest request = new UpdateCommunicationPrefsRequest.Builder(this)
                .setInterests(getInterests())
                .setVacationSeasons(getVacationSeasons())
                .build();

        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    private Map<String, Boolean> getVacationSeasons() {
        return mSeasonsAdapter.getCheckboxValues();
    }

    private Map<String, String> getInterests() {
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> seekBars = mInterestsAdapter.getSeekBarValues();
        for (String key : seekBars.keySet()) {
            map.put(key, GuestPreference.getInterestStringValue(seekBars.get(key)));
        }
        return map;
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        mIsCallInProgress = false;

        if (networkResponse instanceof GetGuestProfileResponse) {
            GetGuestProfileResponse response = (GetGuestProfileResponse) networkResponse;
            if (response.getResult() != null) {
                mGuestProfile = response.getResult().getGuestProfile();
                requestMyPreferences();
            } else {
                Log.w(TAG, "handleNetworkResponse: GetGuestProfileResponse is null");
            }
        } else if (networkResponse instanceof GetGuestPreferencesResponse) {
            GetGuestPreferencesResponse response = (GetGuestPreferencesResponse) networkResponse;
            GuestPreference preference = mGuestProfile.getPreference();

            String keys = mTridionConfig.getManageInterestsOrder();
            List<String> keyList = Arrays.asList(keys.split(","));

            Map<String, Integer> profileValueMap = preference.getCommunicationInterestsValueMap();

            mInterestsAdapter = new GuestInterestsAdapter(keyList, profileValueMap);
            mInterests.setAdapter(mInterestsAdapter);

            mSeasonsAdapter = new GuestSeasonsAdapter(preference, response.getResult());
            mSeasons.setAdapter(mSeasonsAdapter);

        } else if (networkResponse instanceof UpdateCommunicationPrefsResponse) {
            // If communication preferences were updated successfully, set the result and finish
            if (networkResponse.isHttpStatusCodeSuccess()) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
            // Otherwise, let the user know it failed
            else {
                Toast.makeText(getContext(), mTridionConfig.getEr85(), Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
        }

        showViewBasedOnState();
    }
}
