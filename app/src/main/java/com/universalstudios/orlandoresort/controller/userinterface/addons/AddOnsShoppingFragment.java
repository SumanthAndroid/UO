package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.app.ActionBar;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.FullScreenLoadingView;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.ShoppingFragmentInteractionListener;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.FragmentAddOnsShoppingBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.CatalogCategoryResult;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.CatalogSubcategoryResult;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.GetCatalogCategoryRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.GetCatalogCategoryResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.GetCatalogSubcategoryRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.GetCatalogSubcategoryResponse;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import java.util.List;

/**
 * Fragment for Add Ons shopping list
 */
public class AddOnsShoppingFragment extends NetworkFragment {
    public static final String CATEGORY_NAME_EXTRAS = "Extras";

    private Handler mHandler;
    private ShoppingFragmentInteractionListener mShoppingFragmentInteractionListener;
    private FullScreenLoadingView mLoadingView;
    private FragmentAddOnsShoppingBinding mBinding;
    private TridionConfig mTridionConfig;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddOnsShoppingFragment.
     */
    public static AddOnsShoppingFragment newInstance() {
        AddOnsShoppingFragment fragment = new AddOnsShoppingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_add_ons_shopping, container, false);
        View view = mBinding.getRoot();
        mTridionConfig = IceTicketUtils.getTridionConfig();
        mBinding.setTridion(mTridionConfig);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHandler = new Handler(getActivity().getMainLooper());
        if (context instanceof ShoppingFragmentInteractionListener) {
            mShoppingFragmentInteractionListener = (ShoppingFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ShoppingFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showLoadingView();
        GetCatalogCategoryRequest request = new GetCatalogCategoryRequest.Builder(this)
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    @Override
    public void onStart() {
        super.onStart();
        setToolbarTitle();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mShoppingFragmentInteractionListener = null;
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        if (networkResponse instanceof GetCatalogCategoryResponse) {
            handleCatalogCategoryResponse((GetCatalogCategoryResponse) networkResponse);
        }
        if (networkResponse instanceof GetCatalogSubcategoryResponse) {
            handleCatalogSubcategoryResponse((GetCatalogSubcategoryResponse) networkResponse);
        }
    }

    private void handleCatalogCategoryResponse(@NonNull GetCatalogCategoryResponse response) {
        if (response.isHttpStatusCodeSuccess()) {
            for (CatalogCategoryResult ccResult : response.getCatalogCategoryResult()) {
                if (null != ccResult && CATEGORY_NAME_EXTRAS.equalsIgnoreCase(ccResult.getName())) {
                    final String extrasId = ccResult.getId();
                    GetCatalogSubcategoryRequest request = new GetCatalogSubcategoryRequest.Builder(this)
                            .setCategoryId(extrasId)
                            .setConcurrencyType(NetworkRequest.ConcurrencyType.ASYNCHRONOUS)
                            .build();
                    NetworkUtils.queueNetworkRequest(request);
                    NetworkUtils.startNetworkService();
                }
            }
        } else {
            hideLoadingView();
            showMessage(mTridionConfig.getEr71());
        }
    }

    private void handleCatalogSubcategoryResponse(@NonNull GetCatalogSubcategoryResponse response) {
        hideLoadingView();
        if (response.isHttpStatusCodeSuccess()) {
            final List<CatalogSubcategoryResult> subcategoryResults = response.getCatalogCategoryResult();

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    FragmentManager fm = getChildFragmentManager();
                    AddOnsSubcategoryPagerAdapter pagerAdapter = new AddOnsSubcategoryPagerAdapter(fm, subcategoryResults);
                    mBinding.addonsShoppingPager.setAdapter(pagerAdapter);

                }
            });
        } else {
            showMessage(mTridionConfig.getEr71());
        }
    }

    /**
     * Sets the action bar's title
     */
    private void setToolbarTitle() {
        // If either the activity or action bar are null, then we have a problem
        if (getActivity() == null || getActivity().getActionBar() == null) {
            return;
        }
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle(mTridionConfig.getExtrasLabel());
    }

    private void showLoadingView() {
        if (null == mLoadingView) {
            mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
        }
    }

    private void hideLoadingView(){
        if (null != mLoadingView) {
            mLoadingView.dismiss();
            mLoadingView = null;
        }
    }

    private void showMessage(final String message) {
        // Post to handler to make sure it is on the main thread
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
        });
    }

}
