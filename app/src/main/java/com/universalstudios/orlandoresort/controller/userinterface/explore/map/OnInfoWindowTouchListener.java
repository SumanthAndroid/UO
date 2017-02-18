/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.explore.map;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CompoundButton;

import com.google.android.gms.maps.model.Marker;
import com.universalstudios.orlandoresort.BuildConfig;

/**
 * 
 * 
 * @author Steven Byle
 */
public class OnInfoWindowTouchListener implements OnTouchListener {
	private static final String TAG = OnInfoWindowTouchListener.class.getSimpleName();

	private final View mView;
	private final Handler mHandler;

	private Marker mMarker;
	private boolean mPressed;
	private final OnInfoWindowChildViewClickListener mOnInfoWindowChildViewClickListener;

	public OnInfoWindowTouchListener(View view, OnInfoWindowChildViewClickListener onInfoWindowChildViewClickListener) {
		mView = view;
		mHandler = new Handler();
		mPressed = false;
		mOnInfoWindowChildViewClickListener = onInfoWindowChildViewClickListener;
	}

	public void setMarker(Marker marker) {
		mMarker = marker;
	}

	@Override
	public boolean onTouch(View vv, MotionEvent event) {
		if (0 <= event.getX() && event.getX() <= mView.getWidth() &&
				0 <= event.getY() && event.getY() <= mView.getHeight())
		{
			switch (event.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					return startPress();

				case MotionEvent.ACTION_UP:
					// Delay showing the release of the press, so there is time to redraw the pressed state
					if (mView instanceof CompoundButton) {
						mHandler.postDelayed(confirmClickRunnable, 100);
					}
					else {
						mHandler.postDelayed(confirmClickRunnable, 75);
					}
					break;

				case MotionEvent.ACTION_CANCEL:
				default:
					break;
			}
		}
		else {
			// If the touch goes outside of the view's area
			// (like when moving finger out of the pressed button)
			// just release the press
			return endPress();
		}
		return false;
	}

	private boolean startPress() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "startPress: mPressed = " + mPressed);
		}

		if (!mPressed) {
			mPressed = true;
			mHandler.removeCallbacks(confirmClickRunnable);

			// Show the info window to trigger a redraw showing the pressed state
			mView.setPressed(true);
			if (mMarker != null) {
				mMarker.showInfoWindow();
			}
			return true;
		}
		return false;
	}

	private boolean endPress() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "endPress: mPressed = " + mPressed);
		}

		if (mPressed) {
			mPressed = false;
			mHandler.removeCallbacks(confirmClickRunnable);

			// Show the info window to trigger a redraw showing the unpressed state
			mView.setPressed(false);
			if (mMarker != null) {
				mMarker.showInfoWindow();
			}
			return true;
		}
		else {
			return false;
		}
	}

	private final Runnable confirmClickRunnable = new Runnable() {
		@Override
		public void run() {
			if (endPress() && mOnInfoWindowChildViewClickListener != null) {
				mOnInfoWindowChildViewClickListener.onInfoWindowChildViewClicked(mView, mMarker);
			}
		}
	};

}
