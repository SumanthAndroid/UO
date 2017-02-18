/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.explore.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
public class MapUtils {
	private static final String TAG = MapUtils.class.getSimpleName();

	public static final float MIN_ZOOM_LEVEL;
	public static final float MAX_ZOOM_LEVEL;

	public static final int ILLUSTRATED_ZOOM;
	public static final int ILLUSTRATED_X_MIN_NORTHWEST;
	public static final int ILLUSTRATED_Y_MIN_NORTHWEST;
	public static final int ILLUSTRATED_X_MAX_SOUTHEAST;
	public static final int ILLUSTRATED_Y_MAX_SOUTHEAST;

	public static final LatLng ILLUSTRATED_LATLNG_NORTHWEST;
	public static final LatLng ILLUSTRATED_LATLNG_SOUTHEAST;
	public static final LatLng ILLUSTRATED_LATLNG_NORTHEAST;
	public static final LatLng ILLUSTRATED_LATLNG_SOUTHWEST;
	public static LatLngBounds ILLUSTRATED_LATLNG_BOUNDS;

	public static final float MAP_BEARING_DEGREES;

	static {
		// Hollywood map settings
		if (BuildConfigUtils.isLocationFlavorHollywood()) {
			MIN_ZOOM_LEVEL = 15.0f;
			MAX_ZOOM_LEVEL = 19.5f;

			ILLUSTRATED_ZOOM = 19;
			ILLUSTRATED_X_MIN_NORTHWEST = 89767;
			ILLUSTRATED_Y_MIN_NORTHWEST = 209184;
			ILLUSTRATED_X_MAX_SOUTHEAST = ILLUSTRATED_X_MIN_NORTHWEST + 26;
			ILLUSTRATED_Y_MAX_SOUTHEAST = ILLUSTRATED_Y_MIN_NORTHWEST + 18;

			// North is up
			MAP_BEARING_DEGREES = 0.0f;
		}
		// Orlando map settings
		else {
			MIN_ZOOM_LEVEL = 14.0f;
			MAX_ZOOM_LEVEL = 18.5f;

			ILLUSTRATED_ZOOM = 18;
			ILLUSTRATED_X_MIN_NORTHWEST = 71734;
			ILLUSTRATED_Y_MIN_NORTHWEST = 109412;
			ILLUSTRATED_X_MAX_SOUTHEAST = ILLUSTRATED_X_MIN_NORTHWEST + 28;
			ILLUSTRATED_Y_MAX_SOUTHEAST = ILLUSTRATED_Y_MIN_NORTHWEST + 29;

			// West is up
			MAP_BEARING_DEGREES = 270.0f;
		}

		ILLUSTRATED_LATLNG_NORTHWEST = coordsToLatLng(ILLUSTRATED_X_MIN_NORTHWEST, ILLUSTRATED_Y_MIN_NORTHWEST, ILLUSTRATED_ZOOM);
		ILLUSTRATED_LATLNG_SOUTHEAST = coordsToLatLng(ILLUSTRATED_X_MAX_SOUTHEAST, ILLUSTRATED_Y_MAX_SOUTHEAST, ILLUSTRATED_ZOOM);
		ILLUSTRATED_LATLNG_NORTHEAST = new LatLng(ILLUSTRATED_LATLNG_NORTHWEST.latitude, ILLUSTRATED_LATLNG_SOUTHEAST.longitude);
		ILLUSTRATED_LATLNG_SOUTHWEST = new LatLng(ILLUSTRATED_LATLNG_SOUTHEAST.latitude, ILLUSTRATED_LATLNG_NORTHWEST.longitude);

		ILLUSTRATED_LATLNG_BOUNDS = new LatLngBounds(
				new LatLng(MapUtils.ILLUSTRATED_LATLNG_SOUTHWEST.latitude, MapUtils.ILLUSTRATED_LATLNG_SOUTHWEST.longitude),
				new LatLng(MapUtils.ILLUSTRATED_LATLNG_NORTHEAST.latitude, MapUtils.ILLUSTRATED_LATLNG_NORTHEAST.longitude));
	}

	// Collections of park bounds
	private static List<LatLng> LATLNG_BOUNDS_AREA_ENTIRE_PARK;
	public static List<LatLng> getLatLngBoundsAreaEntirePark() {
		if (LATLNG_BOUNDS_AREA_ENTIRE_PARK == null) {
			LATLNG_BOUNDS_AREA_ENTIRE_PARK = new ArrayList<LatLng>();

			// Hollywood venues
			if (BuildConfigUtils.isLocationFlavorHollywood()) {
				LATLNG_BOUNDS_AREA_ENTIRE_PARK.addAll(getLatLngBoundsAreaParks());
				LATLNG_BOUNDS_AREA_ENTIRE_PARK.addAll(getLatLngBoundsAreaHotels());
			}
			// Orlando venues
			else {
				LATLNG_BOUNDS_AREA_ENTIRE_PARK.addAll(getLatLngBoundsAreaParks());
				LATLNG_BOUNDS_AREA_ENTIRE_PARK.addAll(getLatLngBoundsAreaHotels());
			}
		}

		return LATLNG_BOUNDS_AREA_ENTIRE_PARK;
	}

	private static List<LatLng> LATLNG_BOUNDS_AREA_PARKS;
	public static List<LatLng> getLatLngBoundsAreaParks() {
		if (LATLNG_BOUNDS_AREA_PARKS == null) {
			LATLNG_BOUNDS_AREA_PARKS = new ArrayList<LatLng>();

			// Hollywood parks
			if (BuildConfigUtils.isLocationFlavorHollywood()) {
				LATLNG_BOUNDS_AREA_PARKS.addAll(getLatLngBoundsUniversalStudiosHollywood());
				LATLNG_BOUNDS_AREA_PARKS.addAll(getLatLngBoundsCityWalkHollywood());
			}
			// Orlando parks
			else {
				LATLNG_BOUNDS_AREA_PARKS.addAll(getLatLngBoundsIslandsOfAdventure());
				LATLNG_BOUNDS_AREA_PARKS.addAll(getLatLngBoundsUniversalStudiosFlorida());
				LATLNG_BOUNDS_AREA_PARKS.addAll(getLatLngBoundsCityWalkOrlando());
				// Wet N Wild bounds excluded so map does not show it's POIs unless
				// Wet N Wild explore type is chosen
			}
		}

		return LATLNG_BOUNDS_AREA_PARKS;
	}

	private static List<LatLng> LATLNG_BOUNDS_AREA_HOTELS;
	public static List<LatLng> getLatLngBoundsAreaHotels() {
		if (LATLNG_BOUNDS_AREA_HOTELS == null) {
			LATLNG_BOUNDS_AREA_HOTELS = new ArrayList<LatLng>();

			// Hollywood hotels
			if (BuildConfigUtils.isLocationFlavorHollywood()) {
				// Hollywood has no hotels
			}
			// Orlando hotels
			else {
				LATLNG_BOUNDS_AREA_HOTELS.addAll(getLatLngBoundsCabanaBay());
				LATLNG_BOUNDS_AREA_HOTELS.addAll(getLatLngBoundsRoyalPacific());
				LATLNG_BOUNDS_AREA_HOTELS.addAll(getLatLngBoundsHardRock());
				LATLNG_BOUNDS_AREA_HOTELS.addAll(getLatLngBoundsPortofinoBay());
			}
		}

		return LATLNG_BOUNDS_AREA_HOTELS;
	}

	// Specific park bounds
	private static List<LatLng> LATLNG_BOUNDS_PARK_ISLANDS_OF_ADVENTURE;
	public static List<LatLng> getLatLngBoundsIslandsOfAdventure() {
		if (LATLNG_BOUNDS_PARK_ISLANDS_OF_ADVENTURE == null) {
			LATLNG_BOUNDS_PARK_ISLANDS_OF_ADVENTURE = new ArrayList<LatLng>();

			LATLNG_BOUNDS_PARK_ISLANDS_OF_ADVENTURE.add(new LatLng(28.468471, -81.474812));
			LATLNG_BOUNDS_PARK_ISLANDS_OF_ADVENTURE.add(new LatLng(28.474465, -81.474608));
			LATLNG_BOUNDS_PARK_ISLANDS_OF_ADVENTURE.add(new LatLng(28.474639, -81.471432));
			LATLNG_BOUNDS_PARK_ISLANDS_OF_ADVENTURE.add(new LatLng(28.473852, -81.468514));
			LATLNG_BOUNDS_PARK_ISLANDS_OF_ADVENTURE.add(new LatLng(28.472569, -81.467206));
			LATLNG_BOUNDS_PARK_ISLANDS_OF_ADVENTURE.add(new LatLng(28.470391, -81.468321));
			LATLNG_BOUNDS_PARK_ISLANDS_OF_ADVENTURE.add(new LatLng(28.468976, -81.470907));
		}

		return LATLNG_BOUNDS_PARK_ISLANDS_OF_ADVENTURE;
	}

	private static List<LatLng> LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_FLORIDA;
	public static List<LatLng> getLatLngBoundsUniversalStudiosFlorida() {
		if (LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_FLORIDA == null) {
			LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_FLORIDA = new ArrayList<LatLng>();

			LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_FLORIDA.add(new LatLng(28.474170, -81.466444));
			LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_FLORIDA.add(new LatLng(28.475879, -81.471028));
			LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_FLORIDA.add(new LatLng(28.480377, -81.470818));
			LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_FLORIDA.add(new LatLng(28.481570, -81.467042));
			LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_FLORIDA.add(new LatLng(28.479477, -81.465139));
			LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_FLORIDA.add(new LatLng(28.478020, -81.465529));
		}

		return LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_FLORIDA;
	}

	private static List<LatLng> LATLNG_BOUNDS_PARK_CITY_WALK_ORLANDO;
	public static List<LatLng> getLatLngBoundsCityWalkOrlando() {
		if (LATLNG_BOUNDS_PARK_CITY_WALK_ORLANDO == null) {
			LATLNG_BOUNDS_PARK_CITY_WALK_ORLANDO = new ArrayList<LatLng>();

			LATLNG_BOUNDS_PARK_CITY_WALK_ORLANDO.add(new LatLng(28.471758, -81.465451));
			LATLNG_BOUNDS_PARK_CITY_WALK_ORLANDO.add(new LatLng(28.471678, -81.466534));
			LATLNG_BOUNDS_PARK_CITY_WALK_ORLANDO.add(new LatLng(28.472117, -81.467532));
			LATLNG_BOUNDS_PARK_CITY_WALK_ORLANDO.add(new LatLng(28.472834, -81.468095));
			LATLNG_BOUNDS_PARK_CITY_WALK_ORLANDO.add(new LatLng(28.474111, -81.468176));
			LATLNG_BOUNDS_PARK_CITY_WALK_ORLANDO.add(new LatLng(28.474624, -81.467617));
			LATLNG_BOUNDS_PARK_CITY_WALK_ORLANDO.add(new LatLng(28.475105, -81.466394));
			LATLNG_BOUNDS_PARK_CITY_WALK_ORLANDO.add(new LatLng(28.474666, -81.464930));
			LATLNG_BOUNDS_PARK_CITY_WALK_ORLANDO.add(new LatLng(28.473813, -81.464098));
			LATLNG_BOUNDS_PARK_CITY_WALK_ORLANDO.add(new LatLng(28.472639, -81.464672));
			LATLNG_BOUNDS_PARK_CITY_WALK_ORLANDO.add(new LatLng(28.472021, -81.465123));
		}

		return LATLNG_BOUNDS_PARK_CITY_WALK_ORLANDO;
	}

	private static List<LatLng> LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_HOLLYWOOD;
	public static List<LatLng> getLatLngBoundsUniversalStudiosHollywood() {
		if (LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_HOLLYWOOD == null) {
			LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_HOLLYWOOD = new ArrayList<LatLng>();

			LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_HOLLYWOOD.add(new LatLng(34.139316, -118.351646));
			LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_HOLLYWOOD.add(new LatLng(34.140147, -118.353640));
			LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_HOLLYWOOD.add(new LatLng(34.142152, -118.356717));
			LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_HOLLYWOOD.add(new LatLng(34.140487, -118.358624));
			LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_HOLLYWOOD.add(new LatLng(34.138383, -118.357273));
			LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_HOLLYWOOD.add(new LatLng(34.136206, -118.356233));
			LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_HOLLYWOOD.add(new LatLng(34.136216, -118.354254));
			LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_HOLLYWOOD.add(new LatLng(34.137480, -118.352400));
		}

		return LATLNG_BOUNDS_PARK_UNIVERSAL_STUDIOS_HOLLYWOOD;
	}

	private static List<LatLng> LATLNG_BOUNDS_PARK_CITY_WALK_HOLLYWOOD;
	public static List<LatLng> getLatLngBoundsCityWalkHollywood() {
		if (LATLNG_BOUNDS_PARK_CITY_WALK_HOLLYWOOD == null) {
			LATLNG_BOUNDS_PARK_CITY_WALK_HOLLYWOOD = new ArrayList<LatLng>();

			LATLNG_BOUNDS_PARK_CITY_WALK_HOLLYWOOD.add(new LatLng(34.137489, -118.352301));
			LATLNG_BOUNDS_PARK_CITY_WALK_HOLLYWOOD.add(new LatLng(34.137040, -118.353513));
			LATLNG_BOUNDS_PARK_CITY_WALK_HOLLYWOOD.add(new LatLng(34.136129, -118.354310));
			LATLNG_BOUNDS_PARK_CITY_WALK_HOLLYWOOD.add(new LatLng(34.135487, -118.352105));
			LATLNG_BOUNDS_PARK_CITY_WALK_HOLLYWOOD.add(new LatLng(34.136114, -118.350380));
			LATLNG_BOUNDS_PARK_CITY_WALK_HOLLYWOOD.add(new LatLng(34.137251, -118.350519));
			LATLNG_BOUNDS_PARK_CITY_WALK_HOLLYWOOD.add(new LatLng(34.137489, -118.356800));
		}

		return LATLNG_BOUNDS_PARK_CITY_WALK_HOLLYWOOD;
	}
	
	private static List<LatLng> LATLNG_BOUNDS_PARK_WET_N_WILD;
	public static List<LatLng> getLatLngBoundsWetNWild() {
	    if (LATLNG_BOUNDS_PARK_WET_N_WILD == null) {
	        LATLNG_BOUNDS_PARK_WET_N_WILD = new ArrayList<LatLng>();
	        
	        LATLNG_BOUNDS_PARK_WET_N_WILD.add(new LatLng(28.458937, -81.466377));
	        LATLNG_BOUNDS_PARK_WET_N_WILD.add(new LatLng(28.461371, -81.466720));
	        LATLNG_BOUNDS_PARK_WET_N_WILD.add(new LatLng(28.462229, -81.464703));
	        LATLNG_BOUNDS_PARK_WET_N_WILD.add(new LatLng(28.461814, -81.464059));
	        LATLNG_BOUNDS_PARK_WET_N_WILD.add(new LatLng(28.459682, -81.463695));
	        LATLNG_BOUNDS_PARK_WET_N_WILD.add(new LatLng(28.458937, -81.466377));
	    }
	    
	    return LATLNG_BOUNDS_PARK_WET_N_WILD;
	}

	private static List<LatLng> LATLNG_BOUNDS_PARK_VOLCANO_BAY;
	public static List<LatLng> getLatlngBoundsParkVolcanoBay() {
		if (null == LATLNG_BOUNDS_PARK_VOLCANO_BAY) {
			LATLNG_BOUNDS_PARK_VOLCANO_BAY = new ArrayList<LatLng>();

			LATLNG_BOUNDS_PARK_VOLCANO_BAY.add(new LatLng(28.462299, -81.474655));
			LATLNG_BOUNDS_PARK_VOLCANO_BAY.add(new LatLng(28.462559, -81.47272));
			LATLNG_BOUNDS_PARK_VOLCANO_BAY.add(new LatLng(28.460448, -81.472749));
			LATLNG_BOUNDS_PARK_VOLCANO_BAY.add(new LatLng(28.460525, -81.474634));
		}
		return LATLNG_BOUNDS_PARK_VOLCANO_BAY;
	}

	private static List<LatLng> LATLNG_BOUNDS_HOTEL_CABANA_BAY;
	public static List<LatLng> getLatLngBoundsCabanaBay() {
		if (LATLNG_BOUNDS_HOTEL_CABANA_BAY == null) {
			LATLNG_BOUNDS_HOTEL_CABANA_BAY = new ArrayList<LatLng>();

			LATLNG_BOUNDS_HOTEL_CABANA_BAY.add(new LatLng(28.462748, -81.474618));
			LATLNG_BOUNDS_HOTEL_CABANA_BAY.add(new LatLng(28.462748, -81.470626));
			LATLNG_BOUNDS_HOTEL_CABANA_BAY.add(new LatLng(28.468124, -81.472601));
			LATLNG_BOUNDS_HOTEL_CABANA_BAY.add(new LatLng(28.468124, -81.472601));
		}

		return LATLNG_BOUNDS_HOTEL_CABANA_BAY;
	}

	private static List<LatLng> LATLNG_BOUNDS_HOTEL_ROYAL_PACIFIC;
	public static List<LatLng> getLatLngBoundsRoyalPacific() {
		if (LATLNG_BOUNDS_HOTEL_ROYAL_PACIFIC == null) {
			LATLNG_BOUNDS_HOTEL_ROYAL_PACIFIC = new ArrayList<LatLng>();

			LATLNG_BOUNDS_HOTEL_ROYAL_PACIFIC.add(new LatLng(28.467365, -81.467461));
			LATLNG_BOUNDS_HOTEL_ROYAL_PACIFIC.add(new LatLng(28.469591, -81.468127));
			LATLNG_BOUNDS_HOTEL_ROYAL_PACIFIC.add(new LatLng(28.469006, -81.465466));
		}

		return LATLNG_BOUNDS_HOTEL_ROYAL_PACIFIC;
	}

	private static List<LatLng> LATLNG_BOUNDS_HOTEL_HARD_ROCK;
	public static List<LatLng> getLatLngBoundsHardRock() {
		if (LATLNG_BOUNDS_HOTEL_HARD_ROCK == null) {
			LATLNG_BOUNDS_HOTEL_HARD_ROCK = new ArrayList<LatLng>();

			LATLNG_BOUNDS_HOTEL_HARD_ROCK.add(new LatLng(28.476390, -81.465756));
			LATLNG_BOUNDS_HOTEL_HARD_ROCK.add(new LatLng(28.475806, -81.463857));
			LATLNG_BOUNDS_HOTEL_HARD_ROCK.add(new LatLng(28.477513, -81.463739));
			LATLNG_BOUNDS_HOTEL_HARD_ROCK.add(new LatLng(28.477767, -81.465208));
		}

		return LATLNG_BOUNDS_HOTEL_HARD_ROCK;
	}

	private static List<LatLng> LATLNG_BOUNDS_HOTEL_PORTOFINO_BAY;
	public static List<LatLng> getLatLngBoundsPortofinoBay() {
		if (LATLNG_BOUNDS_HOTEL_PORTOFINO_BAY == null) {
			LATLNG_BOUNDS_HOTEL_PORTOFINO_BAY = new ArrayList<LatLng>();

			LATLNG_BOUNDS_HOTEL_PORTOFINO_BAY.add(new LatLng(28.478729, -81.460681));
			LATLNG_BOUNDS_HOTEL_PORTOFINO_BAY.add(new LatLng(28.480219, -81.462998));
			LATLNG_BOUNDS_HOTEL_PORTOFINO_BAY.add(new LatLng(28.482134, -81.462312));
			LATLNG_BOUNDS_HOTEL_PORTOFINO_BAY.add(new LatLng(28.481219, -81.459608));
		}

		return LATLNG_BOUNDS_HOTEL_PORTOFINO_BAY;
	}

	private static List<LatLng> LATLNG_BOUNDS_HOME_PAGE_STATIC_MAP;
	public static List<LatLng> getLatLngBoundsHomePageStaticMap() {
		if (LATLNG_BOUNDS_HOME_PAGE_STATIC_MAP == null) {
			LATLNG_BOUNDS_HOME_PAGE_STATIC_MAP = new ArrayList<LatLng>();

			LATLNG_BOUNDS_HOME_PAGE_STATIC_MAP.add(new LatLng(28.473072, -81.465607));
			LATLNG_BOUNDS_HOME_PAGE_STATIC_MAP.add(new LatLng(28.475639, -81.465579));
			LATLNG_BOUNDS_HOME_PAGE_STATIC_MAP.add(new LatLng(28.475251, -81.468097));
			LATLNG_BOUNDS_HOME_PAGE_STATIC_MAP.add(new LatLng(28.471552, -81.467965));
			LATLNG_BOUNDS_HOME_PAGE_STATIC_MAP.add(new LatLng(28.471507, -81.467103));
		}

		return LATLNG_BOUNDS_HOME_PAGE_STATIC_MAP;
	}
	
	public static LatLng coordsToLatLng(int x, int y, int zoom) {
		return coordsToLatLng((double) x, (double) y, (double) zoom);
	}

	public static LatLng coordsToLatLng(double x, double y, double zoom) {

		// Check coordinate bounds
		if (!(zoom >= 0 && zoom <= 21)
				|| !(x >= 0 && x <= (Math.pow(2, zoom) - 1))
				|| !(y >= 0 && y <= (Math.pow(2, zoom) - 1))) {
			throw new IllegalArgumentException("Invalid x, y, and/or zoom coordinates");
		}

		double n = Math.pow(2, zoom);
		double lonDegrees = x / n * 360 - 180;
		double latRadians = Math.atan(Math.sinh(Math.PI * (1 - 2 * y / n)));
		double latDegrees = Math.toDegrees(latRadians);

		LatLng latLng = new LatLng(latDegrees, lonDegrees);

		return latLng;
	}

	public static XYZoomCoords latLngToCoords(double latDegrees, double lonDegrees, int zoom) {

		// Check coordinate bounds
		if (!(zoom >= 0 && zoom <= 21)
				|| !(latDegrees >= -85.0511 && latDegrees <= 85.0511)
				|| !(lonDegrees >= -180 && lonDegrees <= 180)) {
			throw new IllegalArgumentException("Invalid x, y, and/or zoom coordinates");
		}

		double n = Math.pow(2, zoom);
		double latRadians = Math.toRadians(latDegrees);
		int x = (int) ((lonDegrees + 180.0) / 360.0 * n);
		int y = (int) (n * (1.0 - (Math.log(Math.tan(latRadians) + 1.0 / Math.cos(latRadians)) / Math.PI)) / 2.0);

		XYZoomCoords coords = new XYZoomCoords(x, y, zoom);

		return coords;
	}
}
