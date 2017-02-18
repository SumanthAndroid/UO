package com.universalstudios.orlandoresort.view.horizontalscrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by ibm_admin on 5/20/2016.
 */
public class CenteringHorizontalScrollView extends HorizontalScrollView implements View.OnTouchListener {

    private static final int SWIPE_PAGE_ON_FACTOR = 10;
    private int mPositionOfCenterItem;
    private float mPrevScrollX;
    private boolean mStart;
    private int mItemWidth;
    private OnItemSelectListener mOnItemSelectListener;
    private boolean mIsSelectFirstPosition = false;

    public CenteringHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mItemWidth = 100; // or whatever your item width is.
        setOnTouchListener(this);
    }

    public void setItemWidth(int itemWidth) {
        mItemWidth = itemWidth;
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener)
    {
        this.mOnItemSelectListener = onItemSelectListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getRawX();

        boolean handled = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (mStart) {
                    mPrevScrollX = x;
                    mStart = false;
                }

                break;
            case MotionEvent.ACTION_UP:
                mStart = true;
                int minFactor = mItemWidth / SWIPE_PAGE_ON_FACTOR;

                if ((mPrevScrollX - (float) x) > minFactor) {
                    if (mPositionOfCenterItem < getMaxItemCount() - 1) {
                        mPositionOfCenterItem = mPositionOfCenterItem + 1;
                    }
                }
                else if (((float) x - mPrevScrollX) > minFactor) {
                    if (mPositionOfCenterItem > 0) {
                        mPositionOfCenterItem = mPositionOfCenterItem - 1;
                    }
                }

                scrollToActiveItem();

                handled = true;
                break;
        }

        return handled;
    }

    private int getMaxItemCount() {
        return getChildLayout().getChildCount();
    }

    private LinearLayout getChildLayout() {
        return (LinearLayout) getChildAt(0);
    }

    /**
     * Centers the current view the best it can.
     */
    public void centerCurrentItem() {
        if (getMaxItemCount() == 0) {
            return;
        }

        int currentX = getScrollX();
        View targetChild;
        int currentChild = -1;

        do {
            currentChild++;
            targetChild = getChildLayout().getChildAt(currentChild);
        } while (currentChild < getMaxItemCount() && targetChild.getLeft() < currentX);

        if (mPositionOfCenterItem != currentChild) {
            mPositionOfCenterItem = currentChild;
            scrollToActiveItem();
        }
    }

    /**
     * Scrolls the list view to the currently active child.
     */
    private void scrollToActiveItem() {
        int maxItemCount = getMaxItemCount();
        if (maxItemCount == 0) {
            return;
        }

        int targetItem = Math.min(maxItemCount - 1, mPositionOfCenterItem);
        targetItem = Math.max(0, targetItem);

        mPositionOfCenterItem = targetItem;

        // Scroll so that the target child is centered
        View targetView = getChildLayout().getChildAt(targetItem);

        int targetLeft = targetView.getLeft();
        int childWidth = targetView.getRight() - targetLeft;

        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int targetScroll = targetLeft - ((width - childWidth) / 2);

        super.smoothScrollTo(targetScroll, 0);

        if(mOnItemSelectListener != null)
            mOnItemSelectListener.onItemSelect(targetView, mPositionOfCenterItem);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(!mIsSelectFirstPosition) {
            mIsSelectFirstPosition = true;
            setCurrentItemAndCenter(mPositionOfCenterItem);
        }
    }

    /**
     * Sets the current item and centers it.
     * @param currentItem The new current item.
     */
    public void setCurrentItemAndCenter(int currentItem) {
        mPositionOfCenterItem = currentItem;
        scrollToActiveItem();
    }

    public interface OnItemSelectListener
    {
        public void onItemSelect(View v, int position);
    }
}
