package com.universalstudios.orlandoresort.controller.userinterface.home;

import android.view.View;

import com.squareup.picasso.Callback;
import com.universalstudios.orlandoresort.controller.userinterface.home.HomeOffersRecyclerAdapter.OffersItemViewHolder;

import java.lang.ref.WeakReference;

/**
 * Class using weak references to prevent leaking a context
 */
public class HomeOfferImageCallback implements Callback {
    private final WeakReference<OffersItemViewHolder> mViewHolder;

    public HomeOfferImageCallback(OffersItemViewHolder viewHolder) {
        mViewHolder = new WeakReference<OffersItemViewHolder>(viewHolder);
    }

    @Override
    public void onSuccess() {
        OffersItemViewHolder holder = mViewHolder.get();
        if (holder != null) {
            if (holder.thumbnailImage != null) {
                holder.thumbnailImage.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onError() {
        OffersItemViewHolder holder = mViewHolder.get();
        if (holder != null) {
            if (holder.thumbnailImage != null) {
                holder.thumbnailImage.setVisibility(View.GONE);
            }
        }
    }
}
