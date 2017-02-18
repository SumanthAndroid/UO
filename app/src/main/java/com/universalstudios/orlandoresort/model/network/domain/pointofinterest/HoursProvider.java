
package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

/**
 * @author Steven Byle
 */
public interface HoursProvider {

	// Hours constants
	String HOURS_VARIES = "Varies";
	String HOURS_SEASONAL = "Seasonal";
	String HOURS_WITH_PARK = "WithPark";
	String HOURS_CLOSED = "Closed";

	String getOpenTime();
	String getCloseTime();
	DailyHours getDailyHours();
}
