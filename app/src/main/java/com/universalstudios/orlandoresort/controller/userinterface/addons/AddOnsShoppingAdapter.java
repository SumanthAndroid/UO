package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.support.annotation.LayoutRes;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.controller.userinterface.addons.items.AddOnsShoppingFooterItem;
import com.universalstudios.orlandoresort.controller.userinterface.addons.items.AddOnsShoppingProductItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.view.binding.BaseObservableWithLayoutItem;
import com.universalstudios.orlandoresort.view.binding.DataBoundViewHolder;
import com.universalstudios.orlandoresort.view.binding.MultiTypeDataBoundAdapter;

import java.util.List;

/**
 * New and improved {@link AddOnsShoppingAdapter} that leverages data binding to simplify and
 * modularize the add ons shopping. Each  item is now a distinct class and layout that contain
 * their own view model for updating their own recycler view.
 * @author tjudkins
 * @since 11/1/16
 */

public final class AddOnsShoppingAdapter extends MultiTypeDataBoundAdapter {

    private TridionConfig mTridion;
    private AddOnsShoppingActionCallback mActionCallback;

    private AddOnsShoppingFooterItem mFooterItem;


    public AddOnsShoppingAdapter(TridionConfig tridionConfig, AddOnsShoppingActionCallback actionCallback) {
        this(tridionConfig, actionCallback, (Object[]) new BaseObservableWithLayoutItem[]{});
    }

    public AddOnsShoppingAdapter(TridionConfig tridionConfig, AddOnsShoppingActionCallback actionCallback, Object... items) {
        super(items);
        mTridion = tridionConfig;
        mActionCallback = actionCallback;
    }

    @Override
    protected void bindItem(DataBoundViewHolder holder, int position, List payloads) {
        super.bindItem(holder, position, payloads);
        // this will work even if the layout does not have a callback parameter
        holder.binding.setVariable(BR.callback, mActionCallback);
        holder.binding.setVariable(BR.tridion, mTridion);
    }

    @Override
    public @LayoutRes int getItemLayoutId(int position) {
        // use layout ids as types
        Object item = getItem(position);

        if (item instanceof BaseObservableWithLayoutItem) {
            return ((BaseObservableWithLayoutItem) item).getLayoutId();
        }
        throw new IllegalArgumentException("unknown item type " + item);

    }

    @Override
    public void clear() {
        super.clear();
        mFooterItem = null;
    }

    public void setFooter() {
        if (null == mFooterItem) {
            mFooterItem = new AddOnsShoppingFooterItem();
            addItem(mFooterItem);
        }
    }

    /**
     * Callbacks that need to be implemented the fragment that leverages the {@link AddOnsShoppingAdapter}.
     * The adapter will automagically bind the callbacks to the recyclerviews to leverage the callbacks
     * using data binding.
     */
    public interface AddOnsShoppingActionCallback {
        void onContinueShoppingClicked();
        void onBackClicked();
        void onBestPriceGuaranteeClicked();
        void onSelectClicked(AddOnsShoppingProductItem extrasProductItem);
        void onSeeDetailsClicked(AddOnsShoppingProductItem extrasProductItem);

    }

}
