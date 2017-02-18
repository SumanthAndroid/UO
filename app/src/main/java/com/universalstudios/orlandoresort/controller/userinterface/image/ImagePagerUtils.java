/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.image;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.R;

/**
 * 
 * 
 * @author Steven Byle
 */
public class ImagePagerUtils {

	public enum PagerDotColor {
		WHITE, BLUE
	}

	public static View createPagerDotView(ViewGroup parentViewGroup, boolean isOn, PagerDotColor pagerDotColor) {
		LayoutInflater inflater = LayoutInflater.from(parentViewGroup.getContext());

		View pagerDot = inflater.inflate(R.layout.pager_dot, parentViewGroup, false);
		pagerDot.setBackgroundResource(getPagerDotResId(isOn, pagerDotColor));

		return pagerDot;
	}

	public static int getPagerDotResId(boolean isOn, PagerDotColor pagerDotColor) {
		switch (pagerDotColor) {
			case WHITE:
				return (isOn ? R.drawable.shape_pager_dot_white_opaque : R.drawable.shape_pager_dot_white_translucent);
			case BLUE:
				return (isOn ? R.drawable.shape_pager_dot_blue : R.drawable.shape_pager_dot_gray);
			default:
				return (isOn ? R.drawable.shape_pager_dot_white_opaque : R.drawable.shape_pager_dot_white_translucent);
		}
	}
}
