package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.ConcurrencyType;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetTridionSpecsRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses.GetTridionSpecsResponse;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.util.ArrayList;
import java.util.List;

public class AddOnSelectTierFragment extends AddOnsFragment implements AddOnsSelectTierAdapter.ItemListener {
    private static final String TAG = AddOnSelectTierFragment.class.getSimpleName();

    private static final String KEY_STATE_IS_CALL_IN_PROGRESS = "KEY_STATE_IS_CALL_IN_PROGRESS";

    private TextView mTitle;
    private TextView mPromo;
    private TextView mDescription;
    private RecyclerView mList;
    private ViewGroup mContainer;
    private TextView mSelectedItemTitle;
    private TextView mSelectedItemDescription;
    private ImageView mSelectedItemImage;
    private ProgressBar mLoading;

    private Picasso mPicasso;

    private boolean mIsCallInProgress;

    public static AddOnSelectTierFragment newInstance() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance");
        }

        // Create a new fragment instance
        AddOnSelectTierFragment fragment = new AddOnSelectTierFragment();
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
        if (args == null) {
        }
        // Otherwise, set incoming parameters
        else {
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            mIsCallInProgress = false;
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {
            mIsCallInProgress = savedInstanceState.getBoolean(KEY_STATE_IS_CALL_IN_PROGRESS, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView");
        }

        View view = inflater.inflate(R.layout.fragment_add_on_select_tier, container, false);

        mContainer = (ViewGroup) view.findViewById(R.id.fragment_add_on_select_tier_container);
        mTitle = (TextView) view.findViewById(R.id.fragment_add_on_select_tier_title);
        mPromo = (TextView) view.findViewById(R.id.fragment_add_on_select_tier_summary);
        mDescription = (TextView) view.findViewById(R.id.fragment_add_on_select_tier_description);
        mList = (RecyclerView) view.findViewById(R.id.fragment_add_on_select_tier_list);
        mSelectedItemTitle = (TextView) view.findViewById(R.id.fragment_add_on_select_tier_selected_item_title);
        mSelectedItemDescription = (TextView) view.findViewById(R.id.fragment_add_on_select_tier_selected_item_description);
        mSelectedItemImage = (ImageView) view.findViewById(R.id.fragment_add_on_select_tier_selected_item_image);
        mLoading = (ProgressBar) view.findViewById(R.id.fragment_add_on_select_tier_loading);

        if (mCallbacks != null) {
            AddOnsState state = mCallbacks.getAddOnState();
            TridionLabelSpec productSpec = state.getProductTridionSpec();
            if (productSpec != null) {
                mTitle.setText(productSpec.getTitle());
                mDescription.setText(productSpec.getTypeSelectorHeading());
                mPromo.setText(productSpec.getTypeSectionSummary());
            }
        }

        mList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mList.setAdapter(new AddOnsSelectTierAdapter(getTiers(), this));

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
        showViewBasedOnState();
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
        outState.putBoolean(KEY_STATE_IS_CALL_IN_PROGRESS, mIsCallInProgress);
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
            showTierView();
        }
    }

    private void showLoadingView() {
        mLoading.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.GONE);
    }

    private void showTierView() {
        mLoading.setVisibility(View.GONE);
        mContainer.setVisibility(View.VISIBLE);
    }

    private List<SelectTierObject> getTiers() {
        if (mCallbacks != null) {
            AddOnsState state = mCallbacks.getAddOnState();
            return state.getTierList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void onItemClick(SelectTierObject item) {
        if (mCallbacks != null) {
            AddOnsState state = mCallbacks.getAddOnState();
            state.setSelectedTier(item.getTierName());

            mCallbacks.updateSubtotal();

            String partNumber = item.getAdultPartNumber();
            String tcmid2 = state.getTcmId2(partNumber);
            state.setTcmid2(tcmid2);
            state.getSelectedTierTridionSpec();

            TridionLabelSpec spec = state.getSelectedTierTridionSpec();
            if (spec == null) {
                getTierDetails(tcmid2);
            } else {
                setTierDetails(spec);
            }

            mCallbacks.buttonEnabled(true);
        }
    }

    private void getTierDetails(String tcmid) {
        if (!mIsCallInProgress) {
            mIsCallInProgress = true;
            List<String> ids = new ArrayList<>();
            ids.add(tcmid);
            GetTridionSpecsRequest request = new GetTridionSpecsRequest.Builder(this)
                    .setConcurrencyType(ConcurrencyType.ASYNCHRONOUS)
                    .setIds(ids)
                    .build();

            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        }

        showViewBasedOnState();
    }

    private Picasso getPicasso() {
        // Create the image downloader to get the images
        if (mPicasso == null) {
            mPicasso = new Picasso.Builder(getContext())
                    .build();
        }

        return mPicasso;
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        mIsCallInProgress = false;

        if (networkResponse instanceof GetTridionSpecsResponse) {
            // NO OP - The tridion specs are added to the map in the response handler.
        }

        showViewBasedOnState();
    }

    private void setTierDetails(TridionLabelSpec tridionLabelSpec) {
        String title = tridionLabelSpec.getTitle();
        String description = tridionLabelSpec.getDescription();
        String imageUrl = tridionLabelSpec.getImage();

        mSelectedItemTitle.setText(title);
        mSelectedItemDescription.setText(description);

        if (!TextUtils.isEmpty(imageUrl)) {
            getPicasso().load(imageUrl)
                    .into(mSelectedItemImage);
        } else {
            Log.w(TAG, "setTierDetails: imageUrl is empty");
        }
    }
}
