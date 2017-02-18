package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.addons.items.AddOnsShoppingProductItem;
import com.universalstudios.orlandoresort.controller.userinterface.general.BasicInfoDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.ShoppingFragmentInteractionListener;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.FragmentAddonShoppingSubcategoryBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.CatalogSubcategoryResult;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.GetPersonalizationExtrasRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.GetPersonalizationExtrasResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.PersonalizationExtrasProduct;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetTridionSpecsRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses.GetTridionSpecsResponse;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.ConcurrencyType;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpecManager;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Add Ons for a given subcategory.
 */
public class AddOnsShoppingSubcategoryFragment extends NetworkFragment {

    private static final String TAG = AddOnsShoppingSubcategoryFragment.class.getSimpleName();

    private static final String ARG_SUBCATEGORY_RESULT = "subcategory_result";
    public static final int NUMBER_EXTRAS_REQUESTED = 100;

    private Handler mHandler;
    private CatalogSubcategoryResult subcategoryResult;
    private ShoppingFragmentInteractionListener mShoppingFragmentInteractionListener;
    private AddOnsShoppingSubcategoryViewModel mViewModel;
    private FragmentAddonShoppingSubcategoryBinding mBinding;
    private AddOnsShoppingAdapter mAdapter;
    private TridionConfig mTridionConfig;
    private List<AddOnsShoppingProductItem> mAddOnsProductItems = new ArrayList<>();

    public static AddOnsShoppingSubcategoryFragment newInstance(CatalogSubcategoryResult subcategoryResult) {
        AddOnsShoppingSubcategoryFragment fragment = new AddOnsShoppingSubcategoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SUBCATEGORY_RESULT, Parcels.wrap(subcategoryResult));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTridionConfig = IceTicketUtils.getTridionConfig();
        mViewModel = new AddOnsShoppingSubcategoryViewModel();
        mAdapter = new AddOnsShoppingAdapter(mTridionConfig, mAddOnsShoppingActionCallback);

        if (getArguments() != null) {
            subcategoryResult = Parcels.unwrap(getArguments().getParcelable(ARG_SUBCATEGORY_RESULT));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mHandler = new Handler(getContext().getMainLooper());
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_addon_shopping_subcategory, container, false);
        mBinding.setViewModel(mViewModel);
        mBinding.list.setAdapter(mAdapter);
        View view = mBinding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (null != subcategoryResult) {
            mViewModel.setLoading(true);
            if (null != subcategoryResult && !TextUtils.isEmpty(AccountStateManager.getGuestId())) {
                GetPersonalizationExtrasRequest request = new GetPersonalizationExtrasRequest.Builder(this)
                        // this is needed because several fragments can load at once in a pager
                        .setConcurrencyType(NetworkRequest.ConcurrencyType.ASYNCHRONOUS)
                        .setExternalGuestId(AccountStateManager.getGuestId())
                        .setSubcategoryId(subcategoryResult.getId())
                        .setNumberRequested(NUMBER_EXTRAS_REQUESTED)
                        .build();
                NetworkUtils.queueNetworkRequest(request);
                NetworkUtils.startNetworkService();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ShoppingFragmentInteractionListener) {
            mShoppingFragmentInteractionListener = (ShoppingFragmentInteractionListener) getActivity();
        }
        // If neither implements the interface, log a warning
        else {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement ShoppingFragmentInteractionListener");
            }
            throw new RuntimeException(context.toString()
                    + " must implement ShoppingFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mShoppingFragmentInteractionListener = null;
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        mViewModel.setLoading(false);
        if (networkResponse instanceof GetPersonalizationExtrasResponse) {
            handlePersonalizationExtrasResponse((GetPersonalizationExtrasResponse) networkResponse);
        } else if (networkResponse instanceof GetTridionSpecsResponse) {
            handleTridionSpecsResponse((GetTridionSpecsResponse) networkResponse);
        }

    }

    private void handlePersonalizationExtrasResponse(@NonNull GetPersonalizationExtrasResponse response) {
        if (response.isHttpStatusCodeSuccess() && null != response.getPersonalizationExtrasResult()) {
            List<String> tcmIds = new ArrayList<>();
            for (PersonalizationExtrasProduct product : response.getPersonalizationExtrasResult().getPersonalizationExtrasProduct()) {
                if (null != product) {
                    AddOnsShoppingProductItem productItem = new AddOnsShoppingProductItem(mTridionConfig, product);

                    // The constructor for AddOnsShoppingProductItem extracts the TCMIDs
                    if (!TextUtils.isEmpty(productItem.getTcmId1())) {
                        tcmIds.add(productItem.getTcmId1());
                    }
                    if (!TextUtils.isEmpty(productItem.getTcmId2())) {
                        tcmIds.add(productItem.getTcmId2());
                    }

                    mAddOnsProductItems.add(productItem);
                }
            }

            requestLabelSpecs(tcmIds);
        } else {
            showMessage(mTridionConfig.getEr71());
        }
    }

    private void requestLabelSpecs(@NonNull List<String> tcmIds) {
        if (!tcmIds.isEmpty()) {

            GetTridionSpecsRequest request = new GetTridionSpecsRequest.Builder(this)
                    .setConcurrencyType(ConcurrencyType.ASYNCHRONOUS)
                    .setIds(tcmIds)
                    .build();
            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();

        } else {
            showMessage(mTridionConfig.getEr71());
        }
    }

    private void handleTridionSpecsResponse(@NonNull GetTridionSpecsResponse response) {
        if(response.isHttpStatusCodeSuccess()) {
            updateExtras();
        } else {
            showMessage(mTridionConfig.getEr71());
        }
    }

    private void updateExtras() {
        mAdapter.clear();
        for (AddOnsShoppingProductItem productItem : mAddOnsProductItems) {
            if (!TextUtils.isEmpty(productItem.getTcmId1())) {
                productItem.setLabelSpecTcmId1(TridionLabelSpecManager.getSpecForId(productItem.getTcmId1()));
            }
            if (!TextUtils.isEmpty(productItem.getTcmId2())) {
                productItem.setLabelSpecTcmId2(TridionLabelSpecManager.getSpecForId(productItem.getTcmId2()));
            }
            mAdapter.addItem(productItem);
        }
        mAdapter.setFooter();
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

    private AddOnsShoppingAdapter.AddOnsShoppingActionCallback mAddOnsShoppingActionCallback =
            new AddOnsShoppingAdapter.AddOnsShoppingActionCallback() {

                @Override
                public void onContinueShoppingClicked() {
                    if (null != mShoppingFragmentInteractionListener) {
                        mShoppingFragmentInteractionListener.onContinueClicked();
                    }
                }

                @Override
                public void onBackClicked() {
                    if (null != mShoppingFragmentInteractionListener) {
                        mShoppingFragmentInteractionListener.onBackClicked();
                    }
                }

                @Override
                public void onBestPriceGuaranteeClicked() {
                    Bundle args = BasicInfoDetailActivity.newInstanceBundle(mTridionConfig.getPromoBPTitle(), null,
                            mTridionConfig.getPromoBPDetails());
                    Intent intent = new Intent(getContext(), BasicInfoDetailActivity.class)
                            .putExtras(args);
                    getContext().startActivity(intent);
                }

                @Override
                public void onSelectClicked(AddOnsShoppingProductItem extrasProductItem) {
                    if (null != mShoppingFragmentInteractionListener) {
                        mShoppingFragmentInteractionListener.onSelectAddOnClicked(extrasProductItem);
                    }
                }

                @Override
                public void onSeeDetailsClicked(AddOnsShoppingProductItem extrasProductItem) {
                    if (null != mShoppingFragmentInteractionListener) {
                        mShoppingFragmentInteractionListener.onSeeAddOnDetailsClicked(extrasProductItem);
                    }
                }
            };

}
