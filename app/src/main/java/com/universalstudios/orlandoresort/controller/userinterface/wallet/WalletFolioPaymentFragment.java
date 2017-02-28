package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.account.ViewProfileActivity;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.WalletFolioPaymentAdapter.WalletFolioPaymentActionCallback;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletFolioAlertItemViewModel;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletFolioCreatePinItemViewModel;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletFolioPaymentHeaderItem;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletFolioPaymentMethodItemViewModel;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletFolioPaymentViewModel;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletFolioSpendingLimitItemViewModel;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletFolioUpdatePinItemViewModel;
import com.universalstudios.orlandoresort.databinding.FragmentWalletFolioPaymentBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfigWrapper;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.network_config.TridionConfigStateManager;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.NumberUtils;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.wallet.GetWalletFolioRequest;
import com.universalstudios.orlandoresort.model.network.domain.wallet.GetWalletFolioResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.ModifyPinRequest;
import com.universalstudios.orlandoresort.model.network.domain.wallet.ModifyPinResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.SetSpendingLimitAlertRequest;
import com.universalstudios.orlandoresort.model.network.domain.wallet.SetSpendingLimitAlertResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.SetSpendingLimitRequest;
import com.universalstudios.orlandoresort.model.network.domain.wallet.WalletFolioCard;
import com.universalstudios.orlandoresort.model.network.domain.wallet.WalletFolioResult;
import com.universalstudios.orlandoresort.model.network.domain.wallet.WalletTravelPartyMember;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import java.math.BigInteger;

/**
 * @author acampbell
 */
public class WalletFolioPaymentFragment extends NetworkFragment implements WalletFolioPaymentActionCallback, OnClickListener {
    private static final String TAG = WalletFolioPaymentFragment.class.getSimpleName();

    private static final String KEY_STATE_IS_CREATING_PIN = "KEY_STATE_IS_CREATING_PIN";
    private static final String KEY_STATE_IS_UPDATING_PIN = "KEY_STATE_IS_UPDATING_PIN";

    private WalletFolioPaymentAdapter mAdapter;
    private static TridionConfig mTridionConfig;
    private FragmentWalletFolioPaymentBinding mBinding;
    private WalletFolioPaymentViewModel mViewModel;
    private boolean mIsCreatingPin, mIsUpdatingPin;
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 3;

    public static WalletFolioPaymentFragment newInstance() {
        return new WalletFolioPaymentFragment();
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
            mIsCreatingPin = false;
            mIsUpdatingPin = false;
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {
            mIsCreatingPin = savedInstanceState.getBoolean(KEY_STATE_IS_CREATING_PIN);
            mIsUpdatingPin = savedInstanceState.getBoolean(KEY_STATE_IS_UPDATING_PIN);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentWalletFolioPaymentBinding.inflate(inflater, container, false);
        mTridionConfig = IceTicketUtils.getTridionConfig();
        mBinding.setTridion(mTridionConfig);

        mAdapter = new WalletFolioPaymentAdapter(mTridionConfig, this);
        RecyclerView recyclerView = mBinding.fragmentWalletFolioPaymentRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(mAdapter);

        mViewModel = new WalletFolioPaymentViewModel();
        mBinding.setData(mViewModel);

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onStart:");
        }
        getFolio();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onSaveInstanceState");
        }

        outState.putBoolean(KEY_STATE_IS_CREATING_PIN, mIsCreatingPin);
        outState.putBoolean(KEY_STATE_IS_UPDATING_PIN, mIsUpdatingPin);
    }

    private void getFolio() {
        GetWalletFolioRequest request = new GetWalletFolioRequest.Builder(this).build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
        showLoading();
    }

    private void showEmpty(boolean canChowDialog) {
        mViewModel.setShowLoading(false);
        mViewModel.setShowEmpty(true);

        UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(CacheUtils.COMMERCE_DISK_CACHE_NAME,
                CacheUtils.COMMERCE_DISK_CACHE_MIN_SIZE_BYTES,
                CacheUtils.COMMERCE_DISK_CACHE_MAX_SIZE_BYTES);
        if (canChowDialog) {
            Picasso mPicasso = new Picasso.Builder(getActivity())
                    .downloader(mUniversalOrlandoImageDownloader)
                    .build();
            mPicasso.load(mTridionConfig.getPaymentPartMembersImage()).into(mBinding.walletFolioNoPaymentImage, new Callback() {
                @Override
                public void onSuccess() {
                    mBinding.walletFolioNoPaymentImage.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {
                    mBinding.walletFolioNoPaymentImage.setVisibility(View.VISIBLE);
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false).setMessage(getTridionConfig().getEr92()).setPositiveButton(android.R.string.ok, null).create().show();
        }
        mBinding.walletFolioAddCard.setOnClickListener(this);
    }

    /**
     * Retrieves the {@link TridionConfig} object using config string from SharedPreferences.
     * Will return a new {@link TridionConfig} if said string comes back empty, or the generated
     * {@link TridionConfig} from JSON is null.
     *
     * @return The {@link TridionConfig} object
     */
    public static TridionConfig getTridionConfig() {
        TridionConfigWrapper tridionConfigWrapper = TridionConfigStateManager.getInstance();
        if (tridionConfigWrapper == null || tridionConfigWrapper.getTridionConfig() == null) {
            return new TridionConfig();
        }
        return tridionConfigWrapper.getTridionConfig();
    }

    private void showLoading() {
        mViewModel.setShowLoading(true);
        mViewModel.setShowEmpty(false);
    }

    private void hideLoading() {
        mViewModel.setShowLoading(false);
        mViewModel.setShowEmpty(false);
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {

        if (networkResponse instanceof GetWalletFolioResponse) {
            if (networkResponse.isHttpStatusCodeSuccess()) {
                retryCount = 0;
                GetWalletFolioResponse folioResponse = (GetWalletFolioResponse) networkResponse;
                updateFolioData(folioResponse.getWalletFolioResult());
            } else {
                retryCount++;
                if (retryCount < MAX_RETRY_COUNT) {
                    getFolio();
                } else {
                    retryCount = 0;
                    showEmpty(false);

                    /*Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getEr71(), Toast.LENGTH_LONG)
                            .setIconColor(Color.RED)
                            .show();*/
                }
            }
        } else if (networkResponse instanceof SetSpendingLimitAlertResponse) {
            if (networkResponse.isHttpStatusCodeSuccess()) {
                // Refresh the folio
                Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getSu18(), android.widget.Toast.LENGTH_LONG)
                        .show();
                getFolio();
            } else {
                hideLoading();
                Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getEr92(), Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
        } else if (networkResponse instanceof ModifyPinResponse) {
            if (networkResponse.isHttpStatusCodeSuccess()) {
                // Inform the user it was successful
                if (mIsCreatingPin) {
                    Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getSu14(), Toast.LENGTH_LONG)
                            .show();
                } else if (mIsUpdatingPin) {
                    Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getSu15(), Toast.LENGTH_LONG)
                            .show();
                }

                // Refresh the folio
                getFolio();
            } else {
                hideLoading();
                Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getEr71(), Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }

            mIsCreatingPin = false;
            mIsUpdatingPin = false;
        }
    }

    private void updateFolioData(WalletFolioResult walletFolioResult) {
        if (walletFolioResult != null && walletFolioResult.getCards() != null && !walletFolioResult.getCards().isEmpty()) {
            mAdapter.clear();
            populatePaymentMethods(mAdapter, walletFolioResult);
            populateTravelParty(mAdapter, walletFolioResult);
            populateCreateUpdatePin(mAdapter, walletFolioResult);
            populateTransactionHistory(mAdapter, walletFolioResult);
            hideLoading();
        } else {
            showEmpty(true);
        }
    }

    private static void populatePaymentMethods(WalletFolioPaymentAdapter adapter, WalletFolioResult walletFolioResult) {
        if (walletFolioResult != null && walletFolioResult.getCards() != null) {
            adapter.addItem(new WalletFolioPaymentHeaderItem(WalletFolioPaymentHeaderItem.HEADER_TYPE_PAYMENTS,
                    mTridionConfig.getFolioPaymentMethodsLabel(), mTridionConfig.getManageAllLabel()));

            for (WalletFolioCard card : walletFolioResult.getCards()) {
                if (card != null) {
                    if (card.isPrimary()) {
                        WalletFolioPaymentMethodItemViewModel viewModel = new WalletFolioPaymentMethodItemViewModel(card);
                        adapter.addItem(viewModel);
                    }
                }
            }
        }
    }

    private static void populateCreateUpdatePin(WalletFolioPaymentAdapter adapter, WalletFolioResult walletFolioResult) {
        boolean paymentMethodExists = false;
        if (walletFolioResult != null && walletFolioResult.getCards() != null) {
            // Check if there's a payment method
            for (WalletFolioCard card : walletFolioResult.getCards()) {
                if (card != null) {
                    paymentMethodExists = true;
                    break;
                }
            }

            // Only show PIN set/update if there's a payment method
            if (paymentMethodExists) {
                Boolean hasPin = walletFolioResult.getHasPin();
                boolean hasPinBeenSet = hasPin != null && hasPin;

                if (hasPinBeenSet) {
                    WalletFolioCreatePinItemViewModel viewModel = new WalletFolioUpdatePinItemViewModel();
                    adapter.addItem(viewModel);
                } else {
                    WalletFolioCreatePinItemViewModel viewModel = new WalletFolioCreatePinItemViewModel();
                    adapter.addItem(viewModel);
                }
            }
        }
    }

    private static void populateTravelParty(WalletFolioPaymentAdapter adapter, WalletFolioResult walletFolioResult) {
        if (walletFolioResult != null && walletFolioResult.getTravelParty() != null) {
            adapter.addItem(new WalletFolioPaymentHeaderItem(WalletFolioPaymentHeaderItem.HEADER_TYPE_SPENDING_LIMITS,
                    mTridionConfig.getDailySpendingLimitsLabel(), null));

            for (WalletTravelPartyMember walletTravelParty : walletFolioResult.getTravelParty()) {
                if (walletTravelParty != null) {
                    WalletFolioSpendingLimitItemViewModel viewModel = new WalletFolioSpendingLimitItemViewModel(walletTravelParty);
                    adapter.addItem(viewModel);
                }
            }
            adapter.addItem(new WalletFolioAlertItemViewModel(walletFolioResult.getAlert()));
        }
    }

    private static void populateTransactionHistory(WalletFolioPaymentAdapter adapter, WalletFolioResult walletFolioResult) {
        adapter.addItem(new WalletFolioPaymentHeaderItem(WalletFolioPaymentHeaderItem.HEADER_TYPE_TRANSACTIONS,
                mTridionConfig.getPageHeaderPHTitle(), mTridionConfig.getTransactionHistorySeeAllLabel()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wallet_folio_add_card:
                startActivity(ViewCardActivity.newInstance(getContext()));
                break;
            default:
                break;
        }
    }

    @Override
    public void onConfirmClicked(WalletFolioSpendingLimitItemViewModel viewModel) {
        SetSpendingLimitRequest request = null;
        switch (viewModel.getLimitType()) {
            case WalletFolioSpendingLimitItemViewModel.LIMIT_TYPE_LIMIT_PER_DAY:
                BigInteger limitAmount = NumberUtils.toBigInteger(viewModel.getLimitAmount());
                if (limitAmount != null) {
                    request = new SetSpendingLimitRequest.Builder(this)
                            .setSpendingLimit(limitAmount)
                            .setSequenceId(viewModel.getWalletTravelParty().getSequenceId())
                            .setIsUnlimited(false)
                            .build();
                }
                break;
            case WalletFolioSpendingLimitItemViewModel.LIMIT_TYPE_NO_CHARGE:
                request = new SetSpendingLimitRequest.Builder(this)
                        .setSpendingLimit(BigInteger.ZERO)
                        .setSequenceId(viewModel.getWalletTravelParty().getSequenceId())
                        .setIsUnlimited(false)
                        .build();
                break;
            case WalletFolioSpendingLimitItemViewModel.LIMIT_TYPE_NO_LIMIT:
                request = new SetSpendingLimitRequest.Builder(this)
                        .setSpendingLimit(WalletTravelPartyMember.getMaxLimit())
                        .setSequenceId(viewModel.getWalletTravelParty().getSequenceId())
                        .setIsUnlimited(true)
                        .build();
                break;
        }

        if (request != null) {
            showLoading();
            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        }
    }

    @Override
    public void onItemClicked(WalletFolioPaymentMethodItemViewModel viewModel) {
        startActivity(ViewCardActivity.newInstance(getContext(), viewModel.getWalletFolioCard().getCardId()));
    }

    @Override
    public void onHeaderItemClicked(@WalletFolioPaymentHeaderItem.HeaderType int headerType) {
        switch (headerType) {
            case WalletFolioPaymentHeaderItem.HEADER_TYPE_PAYMENTS:
                startActivity(ManageCardsActivity.newInstance(getContext()));
                break;
            case WalletFolioPaymentHeaderItem.HEADER_TYPE_SPENDING_LIMITS:
                break;
            case WalletFolioPaymentHeaderItem.HEADER_TYPE_TRANSACTIONS:
                break;

        }
    }

    @Override
    public void onCreatePinClicked(WalletFolioCreatePinItemViewModel viewModel) {
        createOrUpdatePin(viewModel.getEnteredPin(), true, false);

    }

    @Override
    public void onUpdatePinClicked(WalletFolioUpdatePinItemViewModel viewModel) {
        createOrUpdatePin(viewModel.getEnteredPin(), false, true);
    }

    private void createOrUpdatePin(String enteredPin, boolean createPin, boolean updatePin) {
        boolean isPinValid = WalletUtils.isWalletFolioPinCodeValid(enteredPin);

        // If the PIN is valid, send it to services
        if (isPinValid) {
            ModifyPinRequest modifyPinRequest = new ModifyPinRequest.Builder(this)
                    .setPin(enteredPin)
                    .build();

            NetworkUtils.queueNetworkRequest(modifyPinRequest);
            NetworkUtils.startNetworkService();
            showLoading();

            // Keep track of the state
            mIsCreatingPin = createPin;
            mIsUpdatingPin = updatePin;
        }
        // Otherwise, inform the user it doesn't pass the rules
        else {
            if (createPin) {
                Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getEr69(), Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            } else if (updatePin) {
                Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getEr70(), Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
        }
    }

    @Override
    public void onCreatePinInfoClicked(WalletFolioCreatePinItemViewModel viewModel) {
        // Show the create PIN rules
        Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getW8(), Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onUpdatePinInfoClicked(WalletFolioUpdatePinItemViewModel viewModel) {
        // Show the update PIN rules
        Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getW9(), Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onSaveClicked(WalletFolioAlertItemViewModel viewModel) {
        SetSpendingLimitAlertRequest request = new SetSpendingLimitAlertRequest.Builder(this)
                .setEmail(viewModel.isEmailChecked())
                .setText(viewModel.isPhoneChecked())
                .build();
        showLoading();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    @Override
    public void onUpdateProfileClicked() {
        startActivity(ViewProfileActivity.newInstanceIntent(getContext()));
    }
}