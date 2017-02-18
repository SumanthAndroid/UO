package com.universalstudios.orlandoresort.controller.userinterface.list;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Adds interior dividers to a RecyclerView with a LinearLayoutManager or its
 * subclass.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

	private Drawable mDivider;
	private int mOrientation;

	private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

	/**
	 *
	 */
	public DividerItemDecoration(Drawable divider) {
		mDivider = divider;
	}

	/**
	 *
	 */
	public DividerItemDecoration(Context context, @DrawableRes int drawableResId) {
		this(ContextCompat.getDrawable(context, drawableResId));
	}

	/**
	 * Default divider will be used
	 */
	public DividerItemDecoration(Context context) {
		final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
		mDivider = styledAttributes.getDrawable(0);
		styledAttributes.recycle();
	}

	/**
	 * Draws horizontal or vertical dividers onto the parent RecyclerView.
	 *
	 * @param canvas The {@link Canvas} onto which dividers will be drawn
	 * @param parent The RecyclerView onto which dividers are being added
	 * @param state  The current RecyclerView.State of the RecyclerView
	 */
	@Override
	public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
		if (mOrientation == LinearLayoutManager.HORIZONTAL) {
			drawHorizontalDividers(canvas, parent);
		} else if (mOrientation == LinearLayoutManager.VERTICAL) {
			drawVerticalDividers(canvas, parent);
		}
	}

	/**
	 * Determines the size and location of offsets between items in the parent
	 * RecyclerView.
	 *
	 * @param outRect The {@link Rect} of offsets to be added around the child
	 *                view
	 * @param view    The child view to be decorated with an offset
	 * @param parent  The RecyclerView onto which dividers are being added
	 * @param state   The current RecyclerView.State of the RecyclerView
	 */
	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		super.getItemOffsets(outRect, view, parent, state);

		if (parent.getChildAdapterPosition(view) == 0) {
			return;
		}

		mOrientation = ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
		if (mOrientation == LinearLayoutManager.HORIZONTAL) {
			outRect.left = mDivider.getIntrinsicWidth();
		} else if (mOrientation == LinearLayoutManager.VERTICAL) {
			outRect.top = mDivider.getIntrinsicHeight();
		}
	}

	/**
	 * Adds dividers to a RecyclerView with a LinearLayoutManager or its
	 * subclass oriented horizontally.
	 *
	 * @param canvas The {@link Canvas} onto which horizontal dividers will be
	 *               drawn
	 * @param parent The RecyclerView onto which horizontal dividers are being
	 *               added
	 */
	private void drawHorizontalDividers(Canvas canvas, RecyclerView parent) {
		int parentTop = parent.getPaddingTop();
		int parentBottom = parent.getHeight() - parent.getPaddingBottom();

		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount - 1; i++) {
			View child = parent.getChildAt(i);

			RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

			int parentLeft = child.getRight() + params.rightMargin;
			int parentRight = parentLeft + mDivider.getIntrinsicWidth();

			mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom);
			mDivider.draw(canvas);
		}
	}

	/**
	 * Adds dividers to a RecyclerView with a LinearLayoutManager or its
	 * subclass oriented vertically.
	 *
	 * @param canvas The {@link Canvas} onto which vertical dividers will be
	 *               drawn
	 * @param parent The RecyclerView onto which vertical dividers are being
	 *               added
	 */
	private void drawVerticalDividers(Canvas canvas, RecyclerView parent) {
		int parentLeft = parent.getPaddingLeft();
		int parentRight = parent.getWidth() - parent.getPaddingRight();

		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount - 1; i++) {
			View child = parent.getChildAt(i);

			RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

			int parentTop = child.getBottom() + params.bottomMargin;
			int parentBottom = parentTop + mDivider.getIntrinsicHeight();

			mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom);
			mDivider.draw(canvas);
		}
	}
}
