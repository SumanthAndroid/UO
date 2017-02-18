package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.databinding.FragmentAddOnProgressBinding;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.view.fonts.Button;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;


public class AddOnProgressFragment extends AddOnsFragment implements View.OnClickListener {
    public static final String TAG = AddOnProgressFragment.class.getSimpleName();

    private ProgressBar mProgressBar;
    private TextView mSubTotal;
    private Button mNextButton;
    private Button mAddToCartButton;

    private OnNextClickListener mListener;


    public static AddOnProgressFragment newInstance() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance");
        }

        // Create a new fragment instance
        AddOnProgressFragment fragment = new AddOnProgressFragment();
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
        Fragment parentFragment = getParentFragment();
        // Check if parent fragment (if there is one) implements the interface
        if (parentFragment != null && parentFragment instanceof OnNextClickListener) {
            mListener = (OnNextClickListener) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (context != null && context instanceof OnNextClickListener) {
            mListener = (OnNextClickListener) context;
        }
        // If neither implements the interface, log a warning
        else if (mListener == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement OnNextClickListener");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView");
        }

        FragmentAddOnProgressBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_add_on_progress, container, false);
        binding.setTridion(IceTicketUtils.getTridionConfig());
        View view = binding.getRoot();
        mSubTotal = binding.fragmentAddOnProgressTotal;
        mProgressBar = binding.fragmentAddOnProgressProgress;
        mNextButton = binding.fragmentAddOnProgressNext;
        mAddToCartButton = binding.fragmentAddOnProgressAddToCart;

        mNextButton.setOnClickListener(this);
        mAddToCartButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewCreated");
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_add_on_progress_next:
                if (mListener != null) {
                    mListener.onNextClicked();
                }
                break;
            case R.id.fragment_add_on_progress_add_to_cart:
                if (mListener != null) {
                    mListener.onAddToCartClicked();
                }
                sendAddToCartAnalytics();
                break;
        }
    }

    public void setProgress(int progress) {
        mProgressBar.setProgress(progress);
    }

    public void setSubTotal(String subTotal) {
        mSubTotal.setText(subTotal);
    }

    public void updateSubtotal() {
        if (mCallbacks != null) {
            AddOnsState state = mCallbacks.getAddOnState();
            Map<String, Integer> map = state.getAgeQuantityMap();

            BigDecimal total = BigDecimal.ZERO;
            for (String key : map.keySet()) {
                Integer count = map.get(key);
                if (count != null) {
                    BigDecimal bdCount = BigDecimal.valueOf(count);
                    BigDecimal temp = state.getMinPriceByAge(key).multiply(bdCount);
                    total = total.add(temp);
                }
            }

            setSubTotal(NumberFormat.getCurrencyInstance().format(total));
        }
    }

    public void showNextButton(boolean enabled) {
        mNextButton.setVisibility(View.VISIBLE);
        mNextButton.setEnabled(enabled);
        mAddToCartButton.setVisibility(View.GONE);
    }

    public void showAddToCartButton(boolean enabled) {
        mNextButton.setVisibility(View.GONE);
        mAddToCartButton.setVisibility(View.VISIBLE);
        mAddToCartButton.setEnabled(enabled);
    }

    public void toggleProgressBar(boolean visible) {
        if (mProgressBar != null) {
            if (visible) {
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    interface OnNextClickListener {
        void onNextClicked();

        void onAddToCartClicked();
    }

    private void sendAddToCartAnalytics() {
            Map<String, Object> extraData = new HashMap<String, Object>();
            extraData.put(AnalyticsUtils.KEY_EVENTS, "scAdd,");
            extraData.put(AnalyticsUtils.KEY_EVENT_NAME, "ADD TO CART button");
            AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_ADDON,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    extraData);
    }
}
