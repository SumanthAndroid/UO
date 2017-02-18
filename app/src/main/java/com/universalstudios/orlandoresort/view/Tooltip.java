package com.universalstudios.orlandoresort.view;

import android.view.View;
import android.widget.PopupWindow;

import com.universalstudios.orlandoresort.R;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/9/16.
 * Class: Tooltip
 * Class Description: TODO: ALWAYS FILL OUT
 */
public class Tooltip extends PopupWindow {
    private boolean mAbove = false;
    private final View mView;

    public Tooltip(View v, int width, int height) {
        super(v, width, height);
        mView = v;
        // Make sure the TextView has a background set as it will be used the first time it is
        // shown and positioned. Initialized with below background, which should have
        // dimensions identical to the above version for this to work (and is more likely).
        mView.setBackgroundResource(R.drawable.shape_tooltip_background);
    }

    void fixDirection(boolean above) {
        mAbove = above;

        mView.setBackgroundResource(R.drawable.shape_tooltip_background);
    }

    private int getResourceId(int currentId, int index) {
        if (currentId == 0) {
            currentId = R.drawable.shape_tooltip_background;
        }
        return currentId;
    }

    public void show(String text) {

        final float scale = mView.getResources().getDisplayMetrics().density;
//            mErrorPopup = new ErrorPopup(err, (int)(200 * scale + 0.5f), (int)(50 * scale + 0.5f));
//            mErrorPopup.setFocusable(false);
        this.setFocusable(false);
        // The user is entering text, so the input method is needed.  We
        // don't want the popup to be displayed on top of it.
//            mErrorPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);


//        TextView tv = (TextView) mErrorPopup.getContentView();
//        chooseSize(mErrorPopup, mError, tv);
//        tv.setText(mError);

        this.showAsDropDown((View) mView.getParent(), getErrorX(), getErrorY());
        fixDirection(true);
    }

    @Override
    public void update(int x, int y, int w, int h, boolean force) {
        super.update(x, y, w, h, force);

        boolean above = isAboveAnchor();
        if (above != mAbove) {
            fixDirection(above);
        }
    }

    /**
     * Returns the X offset to make the pointy top of the error point
     * at the middle of the error icon.
     */
    private int getErrorX() {
        /*
         * The "25" is the distance between the point and the right edge
         * of the background
         */
        final float scale = mView.getResources().getDisplayMetrics().density;


        final int layoutDirection = mView.getLayoutDirection();
        int errorX;
        int offset;
        switch (layoutDirection) {
            default:
            case View.LAYOUT_DIRECTION_LTR:
                offset = - 0;
                errorX = mView.getWidth() - this.getWidth() -
                        mView.getPaddingRight() + offset;
                break;
            case View.LAYOUT_DIRECTION_RTL:
                offset = 0;
                errorX = mView.getPaddingLeft() + offset;
                break;
        }
        return errorX;
    }

    /**
     * Returns the Y offset to make the pointy top of the error point
     * at the bottom of the error icon.
     */
    private int getErrorY() {
        /*
         * Compound, not extended, because the icon is not clipped
         * if the text height is smaller.
         */
        final int compoundPaddingTop = mView.getPaddingTop();
        int vspace = mView.getBottom() - mView.getTop() -
                mView.getBottom() - compoundPaddingTop;


        final int layoutDirection = mView.getLayoutDirection();
        int height;
        switch (layoutDirection) {
            default:
            case View.LAYOUT_DIRECTION_LTR:
                height = 0;
                break;
            case View.LAYOUT_DIRECTION_RTL:
                height = 0;
                break;
        }

        int icontop = compoundPaddingTop + (vspace - height) / 2;

        /*
         * The "2" is the distance between the point and the top edge
         * of the background.
         */
        final float scale = mView.getResources().getDisplayMetrics().density;
        return icontop + height - mView.getHeight() - (int) (2 * scale + 0.5f);
    }
}

