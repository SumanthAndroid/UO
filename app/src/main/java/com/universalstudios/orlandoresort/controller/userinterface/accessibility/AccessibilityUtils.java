/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.accessibility;

import android.content.Context;
import android.view.accessibility.AccessibilityManager;

import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;

/**
 * 
 * 
 * @author Steven Byle
 */
public class AccessibilityUtils {

	private static AccessibilityManager sAccessibilityManager;

	public static AccessibilityManager getAccessibilityManagerInstance() {
		if (sAccessibilityManager == null) {
			sAccessibilityManager = (AccessibilityManager) UniversalOrlandoApplication.getAppContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
		}
		return sAccessibilityManager;
	}

	/**
	 * Util method to determine is accessibility is enabled and talkback
	 * (explore by touch) is on.
	 *
	 * @return if talkback is on
	 */
	public static boolean isTalkBackEnabled() {
		// In theory this should never be null
		if (UniversalOrlandoApplication.getAppContext() == null) {
			return false;
		}

		AccessibilityManager am = getAccessibilityManagerInstance();
		boolean isAccessibilityEnabled = am.isEnabled();
		boolean isExploreByTouchEnabled = am.isTouchExplorationEnabled();
		return (isAccessibilityEnabled && isExploreByTouchEnabled);
	}

}
