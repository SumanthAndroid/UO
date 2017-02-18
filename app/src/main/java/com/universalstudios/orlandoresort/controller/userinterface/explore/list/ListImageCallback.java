package com.universalstudios.orlandoresort.controller.userinterface.explore.list;

import android.view.View;

import com.squareup.picasso.Callback;
import com.universalstudios.orlandoresort.controller.userinterface.explore.PoiViewHolder;

import java.lang.ref.WeakReference;

/**
 * Class using weak references to prevent leaking a context
 */
public class ListImageCallback implements Callback {
    private final WeakReference<PoiViewHolder> mPoiViewHolder;

    public ListImageCallback(PoiViewHolder poiViewHolder) {
        mPoiViewHolder = new WeakReference<PoiViewHolder>(poiViewHolder);
    }

    @Override
    public void onSuccess() {
        PoiViewHolder holder = mPoiViewHolder.get();
        if (holder != null) {
            if (holder.listImage != null) {
                holder.listImage.setVisibility(View.VISIBLE);
            }

            if (holder.listImageNoImage != null) {
                holder.listImageNoImage.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onError() {
        PoiViewHolder holder = mPoiViewHolder.get();
        if (holder != null) {
            if (holder.listImage != null) {
                holder.listImage.setVisibility(View.GONE);
            }
            if (holder.listImageNoImage != null) {
                holder.listImageNoImage.setVisibility(View.VISIBLE);
            }
        }
    }
}
