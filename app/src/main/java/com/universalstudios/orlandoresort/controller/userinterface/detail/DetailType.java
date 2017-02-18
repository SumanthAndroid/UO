/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

/**
 * 
 * 
 * @author Steven Byle
 */
public enum DetailType {
	RIDE(1),
	SHOW(2),
	DINING(3),
	HOTEL(4),
	ENTERTAINMENT(5),
	PARK(6),
	LOCKER(7),
	SHOPPING(8),
	EVENT(9),
	GATEWAY(10),
	GENERAL_LOCATION(11),
	RENTAL_SERVICES(12);

	private final int value;
	private DetailType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
