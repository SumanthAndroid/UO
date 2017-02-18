/**
 * 
 */
package com.universalstudios.orlandoresort.view.map;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * 
 * 
 * @author Steven Byle
 */
public class MapWrapperLayout extends FrameLayout {
	/**
	 * Reference to a GoogleMap object
	 */
	private GoogleMap mGoogleMap;

	/**
	 * Vertical offset in pixels between the bottom edge of our InfoWindow
	 * and the marker position (by default it's bottom edge too).
	 * It's a good idea to use custom markers and also the InfoWindow frame,
	 * because we probably can't rely on the sizes of the default marker and frame.
	 */
	private int mBottomOffsetInPx;

	/**
	 * A currently selected marker
	 */
	private Marker mMarker;

	/**
	 * Our custom view which is returned from either the InfoWindowAdapter.getInfoContents
	 * or InfoWindowAdapter.getInfoWindow
	 */
	private View mInfoWindow;

	public MapWrapperLayout(Context context) {
		super(context);
	}

	public MapWrapperLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MapWrapperLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Must be called before we can route the touch events
	 */
	public void init(GoogleMap map, int bottomOffsetInPx) {
		mGoogleMap = map;
		mBottomOffsetInPx = bottomOffsetInPx;
	}

	/**
	 * Best to be called from either the InfoWindowAdapter.getInfoContents
	 * or InfoWindowAdapter.getInfoWindow.
	 */
	public void setMarkerWithInfoWindow(Marker marker, View infoWindow) {
		mMarker = marker;
		mInfoWindow = infoWindow;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean ret = false;
		// Make sure that the infoWindow is shown and we have all the needed references
		if (mMarker != null && mMarker.isInfoWindowShown() && mGoogleMap != null && mInfoWindow != null) {
			// Get a marker position on the screen
			Point point = mGoogleMap.getProjection().toScreenLocation(mMarker.getPosition());

			// Make a copy of the MotionEvent and adjust it's location
			// so it is relative to the infoWindow left top corner
			MotionEvent copyEv = MotionEvent.obtain(ev);
			copyEv.offsetLocation(
					-point.x + (mInfoWindow.getWidth() / 2),
					-point.y + mInfoWindow.getHeight() + mBottomOffsetInPx);

			// Dispatch the adjusted MotionEvent to the infoWindow
			ret = mInfoWindow.dispatchTouchEvent(copyEv);
			copyEv.recycle();
		}
		// If the infoWindow consumed the touch event, then just return true.
		// Otherwise pass this event to the super class and return its result
		return ret || super.dispatchTouchEvent(ev);
	}

	public int getPinAndInfoWindowOffsetCenterInPx() {
		int infoWindowOffsetPx = (mInfoWindow != null) ? (int) (Math.round(mInfoWindow.getHeight() * 0.4)) : 0;

		return infoWindowOffsetPx + getPinOffsetCenterInPx();
	}

	public int getPinOffsetCenterInPx() {
		int pinOffsetPx = (int) Math.round(mBottomOffsetInPx * 0.4);

		return pinOffsetPx;
	}
}
