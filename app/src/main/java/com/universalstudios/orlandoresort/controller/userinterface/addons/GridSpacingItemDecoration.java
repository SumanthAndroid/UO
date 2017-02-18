package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int columnCount;
    private int spacingPx;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int columnCount, int spacingPx, boolean includeEdge) {
        this.columnCount = columnCount;
        this.spacingPx = spacingPx;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % columnCount; // item column

        if (includeEdge) {
            outRect.left = spacingPx - column * spacingPx / columnCount; // spacing - column * ((1f / columnCount) * spacing)
            outRect.right = (column + 1) * spacingPx / columnCount; // (column + 1) * ((1f / columnCount) * spacing)

            if (position < columnCount) { // top edge
                outRect.top = spacingPx;
            }
            outRect.bottom = spacingPx; // item bottom
        } else {
            outRect.left = column * spacingPx / columnCount; // column * ((1f / columnCount) * spacing)
            outRect.right = spacingPx - (column + 1) * spacingPx / columnCount; // spacing - (column + 1) * ((1f /    columnCount) * spacing)
            if (position >= columnCount) {
                outRect.top = spacingPx; // item top
            }
        }
    }
}