package com.universalstudios.orlandoresort.controller.userinterface.account;

import android.support.annotation.LayoutRes;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.view.binding.DataBoundViewHolder;
import com.universalstudios.orlandoresort.view.binding.LayoutBinding;
import com.universalstudios.orlandoresort.view.binding.MultiTypeDataBoundAdapter;

import java.util.List;

/**
 * Created by kbojarski on 9/30/16.
 * Heavily modified by tjudkins on 12/27/16.
 */

public class ViewProfileBindingAdapter extends MultiTypeDataBoundAdapter {
    private static final String TAG = ViewProfileBindingAdapter.class.getSimpleName();

    private ViewProfileItem.OnProfileActionItemClickedListener mCallback;

    public ViewProfileBindingAdapter(ViewProfileItem.OnProfileActionItemClickedListener callback,
                                     LayoutBinding... items) {
        super((Object[]) items);
        this.mCallback = callback;
    }

    @Override
    protected void bindItem(DataBoundViewHolder holder, int position, List payloads) {
        super.bindItem(holder, position, payloads);
        // this will work even if the layout does not have a tridion parameter
        holder.binding.setVariable(BR.callback, mCallback);
    }

    @Override
    public
    @LayoutRes
    int getItemLayoutId(int position) {
        // use layout ids as types
        Object item = getItem(position);

        if (item instanceof LayoutBinding) {
            return ((LayoutBinding) item).getLayoutId();
        }
        throw new IllegalArgumentException("unknown item type " + item);

    }

}
