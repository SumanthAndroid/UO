package com.universalstudios.orlandoresort.controller.userinterface.linkentitlement;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.linkentitlement.data.SalesProgram;
import com.universalstudios.orlandoresort.controller.userinterface.linkentitlement.data.SalesProgramRadioItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.view.binding.DataBoundViewHolder;
import com.universalstudios.orlandoresort.view.binding.LayoutBinding;
import com.universalstudios.orlandoresort.view.binding.MultiTypeDataBoundAdapter;

import java.util.List;

/**
 * @author tjudkins
 * @since 1/24/17
 */

public class SaleProgramsAdapter extends MultiTypeDataBoundAdapter {

    public interface ActionCallback {
        void onSalesProgramSelected(SalesProgramRadioItem salesProgram);
    }

    private TridionConfig mTridionConfig;
    private LinkEntitlementStep2Fragment.ActionCallback mCallback;
    private SalesProgramRadioItem mSelectedItem;

    private ActionCallback mAdapterActionCallback = new ActionCallback() {
        @Override
        public void onSalesProgramSelected(SalesProgramRadioItem selectedItem) {
            if (null != mSelectedItem) {
                mSelectedItem.setSelected(false);
            }
            mSelectedItem = selectedItem;
            if (null != mCallback) {
                mCallback.onSalesProgramSelected(selectedItem);
            }
        }
    };

    public SaleProgramsAdapter(LinkEntitlementStep2Fragment.ActionCallback callback) {
        super();
        this.mCallback = callback;
        this.mTridionConfig = IceTicketUtils.getTridionConfig();
    }

    public void setSalesPrograms(List<SalesProgram> salesPrograms) {
        if (null != salesPrograms) {
            for (SalesProgram salesProgram : salesPrograms) {
                SalesProgramRadioItem item = new SalesProgramRadioItem(salesProgram);
                addItem(item);
            }
        }
    }

    @Override
    protected void bindItem(DataBoundViewHolder holder, int position, List payloads) {
        super.bindItem(holder, position, payloads);
        // this will work even if the layout does not have a tridion parameter
        holder.binding.setVariable(BR.callback, mAdapterActionCallback);
        holder.binding.setVariable(BR.tridion, mTridionConfig);
    }

    @Override
    public @LayoutRes int getItemLayoutId(int position) {
        // use layout ids as types
        Object item = getItem(position);

        if (item instanceof LayoutBinding) {
            return ((LayoutBinding) item).getLayoutId();
        }
        throw new IllegalArgumentException("unknown item type " + item);
    }

}
