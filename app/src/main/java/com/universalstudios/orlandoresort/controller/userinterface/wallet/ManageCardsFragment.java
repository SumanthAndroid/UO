package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.PaymentMethodItemViewModel;
import com.universalstudios.orlandoresort.databinding.FragmentManageCardsBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.network.domain.wallet.GetWalletCardsRequest;
import com.universalstudios.orlandoresort.model.network.domain.wallet.GetWalletCardsResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.WalletFolioCard;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.view.TintUtils;

import java.util.List;

public class ManageCardsFragment extends NetworkFragment implements PaymentMethodAdapter.PaymentActionCallback {
    private static final String TAG = ManageCardsFragment.class.getSimpleName();

    private static final int REQUEST_CODE_ADD_CARD = 101;
    private static final int REQUEST_CODE_VIEW_CARD = 102;

    private FragmentManageCardsBinding mBinding;
    private RecyclerView mRecyclerView;

    private List<WalletFolioCard> mCards;
    private boolean mIsCallInProgress;
    private PaymentMethodAdapter mAdapter;
    private TridionConfig mTridionConfig;

    public static ManageCardsFragment newInstance() {
        ManageCardsFragment fragment = new ManageCardsFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public ManageCardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mTridionConfig = IceTicketUtils.getTridionConfig();

        if (getArguments() == null) {

        } else {

        }

        if (savedInstanceState == null) {

        } else {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_cards, container, false);
        View view = mBinding.getRoot();
        mRecyclerView = mBinding.fragmentManageCardsList;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new PaymentMethodAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCards();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateOptionsMenu");
        }

        // Adds items to the action bar
        inflater.inflate(R.menu.menu_manage_cards, menu);
        TintUtils.tintAllMenuItems(menu, getContext());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_VIEW_CARD) {
            if (resultCode == Activity.RESULT_OK) {
                boolean lastCardDeleted = false;
                if (data != null) {
                    lastCardDeleted = data.getBooleanExtra(ViewCardActivity.KEY_ARGS_LAST_CARD_DELETED, false);
                }

                if (lastCardDeleted) {
                    Toast.makeText(getContext(), mTridionConfig.getSu17(), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
                    getCards();
                }
            }
        } else if (requestCode == REQUEST_CODE_ADD_CARD) {
            if (resultCode == Activity.RESULT_OK) {
                getCards();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.action_add_card:
                startActivityForResult(ViewCardActivity.newInstance(getContext()), REQUEST_CODE_ADD_CARD);
                return true;
            default:
                return (super.onOptionsItemSelected(item));
        }
    }

    public void getCards() {
        if (!mIsCallInProgress) {
            mIsCallInProgress = true;

            GetWalletCardsRequest request = new GetWalletCardsRequest.Builder(this)
                    .build();

            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        }
    }

    private void updateViewsBasedOnState() {
        if (mIsCallInProgress) {
            showLoadingView();
        } else {
            if (mCards != null && mCards.size() > 0) {
                showListView();
            } else {
                showEmptyView();
            }
        }
    }

    private void showEmptyView() {
        mBinding.fragmentManageCardsEmpty.setVisibility(View.VISIBLE);
        mBinding.fragmentManageCardsLoading.setVisibility(View.GONE);
        mBinding.fragmentManageCardsList.setVisibility(View.GONE);

    }

    private void showListView() {
        mBinding.fragmentManageCardsEmpty.setVisibility(View.GONE);
        mBinding.fragmentManageCardsLoading.setVisibility(View.GONE);
        mBinding.fragmentManageCardsList.setVisibility(View.VISIBLE);

    }

    private void showLoadingView() {
        mBinding.fragmentManageCardsEmpty.setVisibility(View.GONE);
        mBinding.fragmentManageCardsLoading.setVisibility(View.VISIBLE);
        mBinding.fragmentManageCardsList.setVisibility(View.GONE);

    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        mIsCallInProgress = false;

        if (networkResponse instanceof GetWalletCardsResponse) {
            GetWalletCardsResponse response = (GetWalletCardsResponse) networkResponse;
            if (response.getResult() != null) {
                mCards = response.getResult();
                mAdapter.clear();
                for (WalletFolioCard card : mCards) {
                    if (card != null) {
                        PaymentMethodItemViewModel viewModel = new PaymentMethodItemViewModel(card);
                        mAdapter.addItem(viewModel);
                    }
                }
            }
        }
        updateViewsBasedOnState();
    }

    @Override
    public void onItemClicked(WalletFolioCard card) {
        startActivityForResult(ViewCardActivity.newInstance(getContext(), card.getCardId()), REQUEST_CODE_VIEW_CARD);
    }
}
