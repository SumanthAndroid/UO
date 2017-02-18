package com.universalstudios.orlandoresort.controller.userinterface.list;

import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 */
public class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mHorizontalSpaceDimenRes;

    public HorizontalSpaceItemDecoration(@DimenRes int horizontalSpaceDimenRes) {
        this.mHorizontalSpaceDimenRes = horizontalSpaceDimenRes;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.right = view.getResources().getDimensionPixelSize(mHorizontalSpaceDimenRes);
        }
    }
}