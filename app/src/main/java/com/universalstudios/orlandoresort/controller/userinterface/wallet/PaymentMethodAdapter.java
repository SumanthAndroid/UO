package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.model.network.domain.wallet.WalletFolioCard;
import com.universalstudios.orlandoresort.view.binding.BaseObservableWithLayoutItem;
import com.universalstudios.orlandoresort.view.binding.DataBoundViewHolder;
import com.universalstudios.orlandoresort.view.binding.MultiTypeDataBoundAdapter;

import java.util.List;

/**
 * @author acampbell
 */
public class PaymentMethodAdapter extends MultiTypeDataBoundAdapter {

    private PaymentActionCallback callback;

    public PaymentMethodAdapter(PaymentActionCallback callback) {
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
        holder.binding.setVariable(BR.callback, callback);
    }

    public interface PaymentActionCallback {
        void onItemClicked(WalletFolioCard card);
    }
}
