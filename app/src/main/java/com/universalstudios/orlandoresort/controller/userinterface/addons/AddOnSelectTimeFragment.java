package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;


public class AddOnSelectTimeFragment extends AddOnsFragment implements AddOnsSelectTimeAdapter.ItemListener {
    private static final String TAG = AddOnSelectTimeFragment.class.getSimpleName();

    private TextView mTitle;
    private TextView mSelectTime;
    private TextView mDescription;
    private RecyclerView mList;

    public static AddOnSelectTimeFragment newInstance() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance");
        }

        // Create a new fragment instance
        AddOnSelectTimeFragment fragment = new AddOnSelectTimeFragment();
        // Get arguments passed in, if any
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

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
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate");
        }

        Bundle args = getArguments();
        if (args != null) {
        } else {
            //no bundle args
        }
        // Otherwise, set incoming parameters


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
            Log.v(TAG, "onCreateView");
        }

        View view = inflater.inflate(R.layout.fragment_add_on_select_time, container, false);

        mTitle = (TextView) view.findViewById(R.id.fragment_add_on_select_time_title);
        mSelectTime = (TextView) view.findViewById(R.id.fragment_add_on_select_time_select_time);
        mDescription = (TextView) view.findViewById(R.id.fragment_add_on_select_time_description);
        mList = (RecyclerView) view.findViewById(R.id.fragment_add_on_select_time_list);

        if (mCallbacks != null) {
            AddOnsState state = mCallbacks.getAddOnState();
            TridionLabelSpec productSpec = state.getProductTridionSpec();
            if (productSpec != null) {
                mTitle.setText(productSpec.getTitle());
                mSelectTime.setText(productSpec.getTimeSelectorHeading());
                mDescription.setText(productSpec.getTimeSectionSummary());
            }

            mList.setLayoutManager(new GridLayoutManager(getContext(), 2));
            int spacing = getResources().getDimensionPixelSize(R.dimen.extras_select_time_grid_spacing);
            mList.addItemDecoration(new GridSpacingItemDecoration(2, spacing, false));

            mList.setAdapter(new AddOnsSelectTimeAdapter(state.getShowTimesForSelectedDate(), this));
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewCreated");
        }

        if (mCallbacks != null) {
            mCallbacks.buttonEnabled(false);
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
    public void onDetach() {
        super.onDetach();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onDetach");
        }
    }

    @Override
    public void onItemClick(String item) {
        if (mCallbacks != null) {
            mCallbacks.buttonEnabled(true);
        }
        if (mCallbacks != null) {
            AddOnsState state = mCallbacks.getAddOnState();
            state.setSelectedTime(item);
        }
    }
}
