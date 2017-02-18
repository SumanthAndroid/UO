/**
 * 
 */
package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;


/**
 * 
 * 
 * @author Steven Byle
 */
public interface AccessibilityOptionsProvider {

	// Accessibility option values
	public static final String ACCESSIBILITY_OPTION_ASSISTIVE_LISTENING = "AssistiveListening";
	public static final String ACCESSIBILITY_OPTION_STANDARD_WHEELCHAIR = "StandardWheelchair";
	public static final String ACCESSIBILITY_OPTION_CLOSED_CAPTION = "ClosedCaption";
	public static final String ACCESSIBILITY_OPTION_SIGN_LANGUAGE = "SignLanguage";
	public static final String ACCESSIBILITY_OPTION_PARENTAL_DISCRETION_ADVISED = "ParentalDiscretionAdvised";
	public static final String ACCESSIBILITY_OPTION_WHEELCHAIR_MUST_TRANSFER = "WheelchairMustTransfer";
	public static final String ACCESSIBILITY_OPTION_ANY_WHEELCHAIR = "AnyWheelchair";
	public static final String ACCESSIBILITY_OPTION_STATIONARY_SEATING = "StationarySeating";
	public static final String ACCESSIBILITY_OPTION_EXTRA_INFO = "ExtraInfo";
	public static final String ACCESSIBILITY_OPTION_NO_UNATTENDED_CHILDREN = "NoUnattendedChildren";
	public static final String ACCESSIBILITY_OPTION_LIFE_VEST_REQUIRED = "LifeJacketRequired";

	public boolean hasAccessibilityOption(String accessibilityOption);
}
