package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletFolioAlertItemViewModel;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletFolioCreatePinItemViewModel;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletFolioPaymentHeaderItem;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletFolioPaymentMethodItemViewModel;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletFolioSpendingLimitItemViewModel;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletFolioUpdatePinItemViewModel;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.view.binding.BaseObservableWithLayoutItem;
import com.universalstudios.orlandoresort.view.binding.DataBoundViewHolder;
import com.universalstudios.orlandoresort.view.binding.MultiTypeDataBoundAdapter;

import java.util.List;

/**
 * @author acampbell
 */
public class WalletFolioPaymentAdapter extends MultiTypeDataBoundAdapter {

    private TridionConfig tridionConfig;
    private WalletFolioPaymentActionCallback callback;

    public WalletFolioPaymentAdapter(TridionConfig tridionConfig, WalletFolioPaymentActionCallback callback) {
        this.tridionConfig = tridionConfig;
        this.callback = callback;
    }

    @Override
    public int getItemLayoutId(int position) {
        Object item = getItem(position);
        if (item instanceof BaseObservableWithLayoutItem) {
            return ((BaseObservableWithLayoutItem) item).getLayoutId();
        }
        throw new IllegalArgumentException("unknown item type " + item);
    }

    @Override
    protected void bindItem(DataBoundViewHolder holder, int position, List payloads) {
        super.bindItem(holder, position, payloads);
        holder.binding.setVariable(BR.tridion, tridionConfig);
        holder.binding.setVariable(BR.callback, callback);
    }

    public interface WalletFolioPaymentActionCallback {
        void onConfirmClicked(WalletFolioSpendingLimitItemViewModel viewModel);
        void onSaveClicked(WalletFolioAlertItemViewModel viewModel);
        void onUpdateProfileClicked();
        void onItemClicked(WalletFolioPaymentMethodItemViewModel viewModel);
        void onHeaderItemClicked(@WalletFolioPaymentHeaderItem.HeaderType int headerType);
        void onCreatePinClicked(WalletFolioCreatePinItemViewModel viewModel);
        void onCreatePinInfoClicked(WalletFolioCreatePinItemViewModel viewModel);
        void onUpdatePinClicked(WalletFolioUpdatePinItemViewModel viewModel);
        void onUpdatePinInfoClicked(WalletFolioUpdatePinItemViewModel viewModel);
    }
}
