package com.universalstudios.orlandoresort.controller.userinterface.data;

import android.location.Location;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.explore.FilterTabOption;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions.FilterSort;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Event;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Ride;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.AlertsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventSeriesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventSeriesTimesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventTimesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.InteractiveExperiencesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.NewsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OfferSeriesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OffersTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.ShowTimesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenueHoursTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenueLandsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.QueuesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.TicketsAppointmentTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Utility class to create {@link DatabaseQuery} objects to be used for queries
 * with {@link CursorLoader} implementations.
 * 
 * @author Steven Byle
 */
public class DatabaseQueryUtils {
	private static final String TAG = DatabaseQueryUtils.class.getSimpleName();

	private static final String COL_TEMP_DISTANCE_APPROX_SQUARED_IN_METERS = "temp_distance_approx_squared_in_meters";
	private static final String SQLITE_WILDCARD = "%";
	private static final String SQLITE_NO_MATCH = "XXXXX NO MATCH XXXXX";
	private static final long DISTANCE_APPROX_FACTOR = 1000000;
	private static final String DATE_FORMAT_SHOW_TIME = "HH:mm:ss";
	private static final String SHOW_TIME_MAX_DATE = "99:99:99";
    public static final long LARGEST_INTEGER_VALUE = 9223372036854775806L;

	/**
	 *  ************************ GLOBAL ************************
	 */

	private static String getDistanceApproxCol(Location userLocation) {
		StringBuilder latDelta = new StringBuilder()
		.append("(").append(DISTANCE_APPROX_FACTOR).append("*")
		.append("(").append(userLocation.getLatitude()).append("-").append(PointsOfInterestTable.COL_FULL_LATITUDE).append(")")
		.append(")");

		StringBuilder lonDelta = new StringBuilder()
		.append("(").append(DISTANCE_APPROX_FACTOR).append("*")
		.append("(").append(userLocation.getLongitude()).append("-").append(PointsOfInterestTable.COL_FULL_LONGITUDE).append(")")
		.append(")");

		StringBuilder distanceApproxCol = new StringBuilder()
		.append("(")
		.append(latDelta).append("*").append(latDelta)
		.append("+")
		.append(lonDelta).append("*").append(lonDelta)
		.append(")");

		return distanceApproxCol.toString();
	}
	
	/**
     *  ************************ ADVANCED FILTERS ************************
     */
	
	private static void setAdvancedGlobalFilters(FilterOptions filterOptions, StringBuilder selection) {

	    // Advanced Filter - accessibility options
        if (filterOptions.getAccessibilityOptions() != null
                && !filterOptions.getAccessibilityOptions().isEmpty()) {
            selection.append(" AND (");
            for (int i = 0; i < filterOptions.getAccessibilityOptions().size(); i++) {
                String option = filterOptions.getAccessibilityOptions().get(i);
                if (i != 0) {
                    selection.append(" AND ");
                }
                // Include Any wheelchair accessibility option when Standard wheelchair is chosen
                if (option.equals(String.valueOf(PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_STANDARD_WHEELCHAIR))) {
                    selection.append("(").append(PointsOfInterestTable.COL_FULL_OPTION_FLAGS)
                    .append(" & ").append(option).append(" > 0 OR ")
                    .append(PointsOfInterestTable.COL_FULL_OPTION_FLAGS).append(" & ")
                    .append(PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_ANY_WHEELCHAIR)
                    .append(" > 0").append(")");
                } else {
                    selection.append(PointsOfInterestTable.COL_FULL_OPTION_FLAGS)
                    .append(" & ").append(option).append(" > 0");
                }
            }
            selection.append(")");
        }
        // Express pass line switch
        if (filterOptions.isExpressPassLine()) {
            selection.append(" AND ").append(PointsOfInterestTable.COL_FULL_OPTION_FLAGS).append(" & ")
            .append(PointsOfInterestTable.VAL_OPTION_FLAG_EXPRESS_PASS_ACCEPTED).append(" > 0");
        }
        // Kid-Friendly switch
        if(filterOptions.isKidFriendly()) {
            selection.append(" AND ").append(PointsOfInterestTable.COL_FULL_OPTION_FLAGS)
            .append(" & ")
            .append(PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_PARENTAL_DISCRETION_ADVISED)
            .append(" = 0");
        }
	}
	
    private static FilterTabOption getEnabledFilterTabOption(List<FilterTabOption> filterTabOptions,
            int poiTypeId, String columnName) {
        if (filterTabOptions.isEmpty()) {
            return null;
        }

        for (FilterTabOption filterTabOption : filterTabOptions) {
            List<String> columnValues = Arrays.asList(filterTabOption.getColumnValues());
            if (filterTabOption.isChecked() && columnName.equals(filterTabOption.getColumnName())
                    && columnValues.contains(String.valueOf(poiTypeId))) {
                return filterTabOption;
            }
        }

        return null;
    }
    
    private static void setFilterTabOptionSelection(StringBuilder selection, FilterTabOption filterTabOption) {
        selection.append(" AND ").append(filterTabOption.getColumnName()).append(" IN (");
        String[] columnValues = filterTabOption.getColumnValues();
        for (int j = 0; j < columnValues.length; j++) {
            String columnValue = columnValues[j];

            if (j > 0) {
                selection.append(", ");
            }
            selection.append("'").append(columnValue).append("'");
        }
        selection.append(")");
    }
    
    private static void setVenueAndLandSelection(FilterOptions filterOptions, StringBuilder selection, long venueId) {
        selection.append(PointsOfInterestTable.COL_FULL_VENUE_ID).append(" = ").append(venueId);

        // Upper/Lower lot switches
        if (filterOptions.isUpperLot() && filterOptions.isLowerLot()) {
            selection.append(" AND ").append("(").append(PointsOfInterestTable.COL_FULL_VENUE_LAND_ID).append(" = ")
                    .append(VenueLandsTable.VAL_LAND_ID_HOLLYWOOD_UPPER_LOT)
                    .append(" OR ").append(PointsOfInterestTable.COL_FULL_VENUE_LAND_ID).append(" = ")
                    .append(VenueLandsTable.VAL_LAND_ID_HOLLYWOOD_LOWER_LOT)
                    .append(")");
        } else if (filterOptions.isUpperLot() && !filterOptions.isLowerLot()) {
            selection.append(" AND ").append(PointsOfInterestTable.COL_FULL_VENUE_LAND_ID).append(" = ")
                    .append(VenueLandsTable.VAL_LAND_ID_HOLLYWOOD_UPPER_LOT);
        } else if (!filterOptions.isUpperLot() && filterOptions.isLowerLot()) {
            selection.append(" AND ").append(PointsOfInterestTable.COL_FULL_VENUE_LAND_ID).append(" = ")
                    .append(VenueLandsTable.VAL_LAND_ID_HOLLYWOOD_LOWER_LOT);
        }
    }
	
    /**
     * Handles advanced filtering for venues, the filtering becomes a bit more
     * complex when there are multiple poi types. The filtering of each poi type
     * must only affect itself which causes for the types to be handled
     * individually
     * 
     * @param filterOptions
     * @param selection
     */
    private static void setVenueAdvancedSelection(FilterOptions filterOptions, StringBuilder selection,
            long venueId, boolean isRestroomToggleOn) {
        boolean isFirstFilterTab = true;
        List<FilterTabOption> filterTabOptions = filterOptions.getFilterTabOptions();

        // Restroom filter
        if (isRestroomToggleOn) {
            if (isFirstFilterTab) {
                isFirstFilterTab = false;
                selection.append(" AND (");
            } else {
                selection.append(" OR (");
            }
            selection.append(PointsOfInterestTable.COL_FULL_POI_TYPE_ID).append(" = ")
                    .append(PointsOfInterestTable.VAL_POI_TYPE_ID_RESTROOM).append(" AND ");
            setVenueAndLandSelection(filterOptions, selection, venueId);
            selection.append(")");
        }
        // Ride filters
        FilterTabOption filterTabOption = getEnabledFilterTabOption(filterTabOptions,
                PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE, PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
        if (filterTabOption != null) {
            if (isFirstFilterTab) {
                isFirstFilterTab = false;
                selection.append(" AND (");
            } else {
                selection.append(" OR (");
            }
            setVenueAndLandSelection(filterOptions, selection, venueId);
            setFilterTabOptionSelection(selection, filterTabOption);
            setAdvancedRideFilters(filterOptions, selection);
            setAdvancedGlobalFilters(filterOptions, selection);
            selection.append(")");
        }
        // Show filters
        filterTabOption = getEnabledFilterTabOption(filterTabOptions,
                PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW, PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
        if (filterTabOption != null) {
            if (isFirstFilterTab) {
                isFirstFilterTab = false;
                selection.append(" AND (");
            } else {
                selection.append(" OR (");
            }
            setVenueAndLandSelection(filterOptions, selection, venueId);
            setFilterTabOptionSelection(selection, filterTabOption);
            setAdvancedShowFilter(filterOptions, selection);
            setAdvancedGlobalFilters(filterOptions, selection);
            selection.append(")");
        }
        // Dining filters
        filterTabOption = getEnabledFilterTabOption(filterTabOptions,
                PointsOfInterestTable.VAL_POI_TYPE_ID_DINING, PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
        if (filterTabOption != null) {
            if (isFirstFilterTab) {
                isFirstFilterTab = false;
                selection.append(" AND (");
            } else {
                selection.append(" OR (");
            }
            setVenueAndLandSelection(filterOptions, selection, venueId);
            setFilterTabOptionSelection(selection, filterTabOption);
            setAdvancedDiningFilter(filterOptions, selection);
            selection.append(")");
        }
        // Shopping filters
        filterTabOption = getEnabledFilterTabOption(filterTabOptions,
                PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP, PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
        if (filterTabOption != null) {
            if (isFirstFilterTab) {
                isFirstFilterTab = false;
                selection.append(" AND (");
            } else {
                selection.append(" OR (");
            }
            setVenueAndLandSelection(filterOptions, selection, venueId);
            setFilterTabOptionSelection(selection, filterTabOption);
            setAdvancedShoppingFilter(filterOptions, selection);
            selection.append(")");
        }

        // Rental Services filters
        filterTabOption = getEnabledFilterTabOption(filterTabOptions,
                PointsOfInterestTable.VAL_POI_TYPE_ID_RENTALS, PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
        if (filterTabOption != null) {
            if (isFirstFilterTab) {
                isFirstFilterTab = false;
                selection.append(" AND (");
            } else {
                selection.append(" OR (");
            }
            setVenueAndLandSelection(filterOptions, selection, venueId);
            setFilterTabOptionSelection(selection, filterTabOption);
            setAdvancedRentalServicesFilter(filterOptions, selection);
            selection.append(")");
        }

        // Entertainment filters
        filterTabOption = getEnabledFilterTabOption(filterTabOptions,
                PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT,
                PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
        if (filterTabOption != null) {
            if (isFirstFilterTab) {
                isFirstFilterTab = false;
                selection.append(" AND (");
            } else {
                selection.append(" OR (");
            }
            setVenueAndLandSelection(filterOptions, selection, venueId);
            setFilterTabOptionSelection(selection, filterTabOption);
            setAdvancedEntertainmentFilter(filterOptions, selection);
            selection.append(")");
        }
    }
	
	private static void setActivityAdvancedSelection(FilterOptions filterOptions, StringBuilder selection) {
	    setAdvancedGlobalFilters(filterOptions, selection);
	    setAdvancedRideFilters(filterOptions, selection);
	    setAdvancedShowFilter(filterOptions, selection);
	    setAdvancedDiningFilter(filterOptions, selection);
	    setAdvancedShoppingFilter(filterOptions, selection);
        setAdvancedRentalServicesFilter(filterOptions, selection);
	    setAdvancedEntertainmentFilter(filterOptions, selection);

	}
	
	private static void setAdvancedEntertainmentFilter(FilterOptions filterOptions, StringBuilder selection) {
	    // Starting price
	    if(filterOptions.getEntertainmentMinPrice() > 0) {
            selection.append(" AND  (").append(PointsOfInterestTable.COL_FULL_MIN_PRICE)
            .append(" <= ").append(filterOptions.getEntertainmentMinPrice());
            selection.append(")");
        }

        // Entertainment sub-types
        if (filterOptions.getEntertainmentSubTypes() != null && !filterOptions.getEntertainmentSubTypes().isEmpty()) {
            switch (filterOptions.getExploreType()) {
                default:
                    selection.append(" AND (");
                    String columnName = PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS;
                    for (int i = 0; i < filterOptions.getEntertainmentSubTypes().size(); i++) {
                        String columnValue = filterOptions.getEntertainmentSubTypes().get(i);
                        if (i > 0) {
                            selection.append(" OR ");
                        }
                        selection.append(columnName).append(" & ").append("'").append(columnValue)
                                .append("'").append(" > 0");
                    }
                    selection.append(")");
                    break;
            }
        }
	}
	
	private static void setAdvancedShoppingFilter(FilterOptions filterOptions, StringBuilder selection) {
	    // Shopping venues
	    if (filterOptions.getShoppingFilterVenues() != null && !filterOptions.getShoppingFilterVenues().isEmpty()) {
            selection.append(" AND ").append(PointsOfInterestTable.COL_FULL_VENUE_ID).append(" IN (");
            for (int i = 0; i < filterOptions.getShoppingFilterVenues().size(); i++) {
                if (i > 0) {
                    selection.append(", ");
                }
                selection.append("'").append(filterOptions.getShoppingFilterVenues().get(i)).append("'");
            }
            selection.append(")");
        }

        // Shopping sub-types
        if (filterOptions.getShoppingSubTypes() != null && !filterOptions.getShoppingSubTypes().isEmpty()) {
            switch (filterOptions.getExploreType()) {
                default:
                    selection.append(" AND (");
                    String columnName = PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS;
                    for (int i = 0; i < filterOptions.getShoppingSubTypes().size(); i++) {
                        String columnValue = filterOptions.getShoppingSubTypes().get(i);
                        if (i > 0) {
                            selection.append(" OR ");
                        }
                        selection.append(columnName).append(" & ").append("'").append(columnValue)
                                .append("'").append(" > 0");
                    }
                    selection.append(")");
                    break;
            }
        }

	    // Express pass sold
	    if(filterOptions.isExpressPassSold()) {
	        selection.append(" AND (" + PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS).append(" & ")
            .append("'").append(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_SELLS_EXPRESS_PASS).append("' > 0)");
	    }

        // Package Pickup Offered
        if(filterOptions.isShopPackagePickup()) {
            selection.append(" AND (" + PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS).append(" & ")
                    .append("'").append(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_HAS_PACKAGE_PICKUP).append("' > 0)");
        }
	    // Amex merchandise
        if(filterOptions.isAmexShopping()) {
            selection.append(" AND (LENGTH(").append(PointsOfInterestTable.COL_FULL_OFFER_IDS)
            .append(") > 3)");
        }
	}

    //search advanced filter for Rental Services
    private static void setAdvancedRentalServicesFilter(FilterOptions filterOptions, StringBuilder selection) {
        // Rental Services venues
        if (filterOptions.getRentalServicesFilterVenues() != null && !filterOptions.getRentalServicesFilterVenues().isEmpty()) {
            selection.append(" AND ").append(PointsOfInterestTable.COL_FULL_VENUE_ID).append(" IN (");
            for (int i = 0; i < filterOptions.getRentalServicesFilterVenues().size(); i++) {
                if (i > 0) {
                    selection.append(", ");
                }
                selection.append("'").append(filterOptions.getRentalServicesFilterVenues().get(i)).append("'");
            }
            selection.append(")");
        }
        // Testing Rental Type offered
        if(filterOptions.isRentalTypeEcv()) {
            selection.append(" AND (" + PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS).append(" & ")
                    .append("'").append(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RENTAL_TYPE_ECV).append("' > 0)");
        }

        // Stroller Rental Type offered
        if(filterOptions.isRentalTypeStroller()) {
            selection.append(" AND (" + PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS).append(" & ")
                    .append("'").append(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RENTAL_TYPE_STROLLER).append("' > 0)");
        }
        // WheelChair Rental Type Offered
        if(filterOptions.isRentalTypeWheelChair()) {
            selection.append(" AND (" + PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS).append(" & ")
                    .append("'").append(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RENTAL_TYPE_WHEELCHAIR).append("' > 0)");
        }
    }


    private static void setAdvancedDiningFilter(FilterOptions filterOptions, StringBuilder selection) {
        // Dining venues
        if (filterOptions.getDiningFilterVenues() != null && !filterOptions.getDiningFilterVenues().isEmpty()) {
            selection.append(" AND ").append(PointsOfInterestTable.COL_FULL_VENUE_ID).append(" IN (");
            for (int i = 0; i < filterOptions.getDiningFilterVenues().size(); i++) {
                if (i > 0) {
                    selection.append(", ");
                }
                selection.append("'").append(filterOptions.getDiningFilterVenues().get(i)).append("'");
            }
            selection.append(")");
        }
        // Dining sub-types, sub-types are handled by filter tabs for dining so skip them
        if (filterOptions.getDiningSubTypes() != null && !filterOptions.getDiningSubTypes().isEmpty()) {
            switch (filterOptions.getExploreType()) {
                case DINING:
                    break;
                default:
                    selection.append(" AND (");
                    String columnName = PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS;
                    for (int i = 0; i < filterOptions.getDiningSubTypes().size(); i++) {
                        String columnValue = filterOptions.getDiningSubTypes().get(i);
                        if (i > 0) {
                            selection.append(" OR ");
                        }
                        selection.append(columnName).append(" & ").append("'").append(columnValue)
                                .append("'").append(" > 0");
                    }
                    selection.append(")");
                    break;
            }
        }
        // Starting price
        if(filterOptions.getDiningMinPrice() > 0) {
            selection.append(" AND  (").append(PointsOfInterestTable.COL_FULL_MIN_PRICE)
            .append(" <= ").append(filterOptions.getDiningMinPrice());
            selection.append(")");
        }
        // Dining plans
        if(filterOptions.getDiningPlans() != null && !filterOptions.getDiningPlans().isEmpty()) {
            selection.append(" AND (");
            for (int i = 0; i < filterOptions.getDiningPlans().size(); i++) {
                if (i > 0) {
                    selection.append(" AND ");
                }
                selection.append(PointsOfInterestTable.COL_FULL_OPTION_FLAGS)
                .append(" & ").append(filterOptions.getDiningPlans().get(i)).append(" > 0");
            }
            selection.append(")");
        }
        // Character dining
        if(filterOptions.isCharacterDining()) {
            selection.append(" AND (").append(PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS)
            .append(" & ").append(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_SERVICE_CHARACTER_DINING)
            .append(" > 0)");
        }
        // Vegetarian/healthy options
        if(filterOptions.isVegetarianHealthy()) {
            selection.append(" AND (").append(PointsOfInterestTable.COL_FULL_OPTION_FLAGS)
            .append(" & ").append(PointsOfInterestTable.VAL_OPTION_FLAG_DINING_VEGETARIAN_HEALTHY_OPTIONS)
            .append(" > 0)");
        }
        // Amex food and beverage
        if(filterOptions.isAmexDining()) {
            selection.append(" AND (LENGTH(").append(PointsOfInterestTable.COL_FULL_OFFER_IDS)
            .append(") > 3)");
        }

        if (filterOptions.hasCocaCola) {
            selection.append(" AND (").append(PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS)
                    .append(" & ").append(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINNING_COCA_COLA_FREESTYLE)
                    .append(" > 0)");
        }
    }
	
    private static void setAdvancedShowFilter(FilterOptions filterOptions, StringBuilder selection) {
        // Show venues
        if (filterOptions.getShowFilterVenues() != null && !filterOptions.getShowFilterVenues().isEmpty()) {
            selection.append(" AND ").append(PointsOfInterestTable.COL_FULL_VENUE_ID).append(" IN (");
            for (int i = 0; i < filterOptions.getShowFilterVenues().size(); i++) {
                if (i > 0) {
                    selection.append(", ");
                }
                selection.append("'").append(filterOptions.getShowFilterVenues().get(i)).append("'");
            }
            selection.append(")");
        }
        // Show sub-types, sub-types are handled by filter tabs for shows so skip them
        if (filterOptions.getShowSubTypes() != null && !filterOptions.getShowSubTypes().isEmpty()) {
            switch (filterOptions.getExploreType()) {
                case SHOWS:
                    break;
                default:
                    selection.append(" AND (");
                    String columnName = PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS;
                    for (int i = 0; i < filterOptions.getShowSubTypes().size(); i++) {
                        String columnValue = filterOptions.getShowSubTypes().get(i);
                        if (i > 0) {
                            selection.append(" OR ");
                        }
                        selection.append(columnName).append(" & ").append("'").append(columnValue)
                                .append("'").append(" > 0");
                    }
                    selection.append(")");
                    break;
            }
        }
        // Next show time
        if (filterOptions.getNextShowTime() > 0) {
            Calendar now = new GregorianCalendar(DateTimeUtils.getParkTimeZone(), Locale.US);
            now.add(Calendar.MINUTE, filterOptions.getNextShowTime());
            SimpleDateFormat showTimeSdf = new SimpleDateFormat(DATE_FORMAT_SHOW_TIME, Locale.US);
            showTimeSdf.setTimeZone(DateTimeUtils.getParkTimeZone());
            String showTime = showTimeSdf.format(now.getTime());
            selection.append(" AND (")
            .append(ShowTimesTable.COL_SHOW_TIME).append(" <= '").append(showTime).append("'")
            .append(")");
        }
    }
	
	private static void setAdvancedRideFilters(FilterOptions filterOptions, StringBuilder selection) {
        // Max ride wait time
        if(filterOptions.getRideFilterMaxWaitTime() > 0) {
            selection.append(" AND (").append(PointsOfInterestTable.COL_FULL_WAIT_TIME)
            .append(" <= ").append(filterOptions.getRideFilterMaxWaitTime())
            .append(" AND ").append(PointsOfInterestTable.COL_FULL_WAIT_TIME)
            .append(" >= 0)");
        }
        // Rider height
        if(filterOptions.getRiderHeight() > 0) {
            selection.append(" AND  (IFNULL(").append(PointsOfInterestTable.COL_FULL_MIN_RIDE_HEIGHT)
            .append(", 0) <= ").append(filterOptions.getRiderHeight()).append(" AND IFNULL(")
            .append(PointsOfInterestTable.COL_FULL_MAX_RIDE_HEIGHT).append(", 999) >= ")
            .append(filterOptions.getRiderHeight()).append(")");
        }
        // Single rider line
        if(filterOptions.isSingleRiderLine()) {
            selection.append(" AND ").append(PointsOfInterestTable.COL_FULL_OPTION_FLAGS)
            .append(" & ").append(PointsOfInterestTable.VAL_OPTION_FLAG_RIDE_SINGLE_RIDER_LINE)
            .append(" > 0");
        }
        // Child swap
        if(filterOptions.isChildSwap()) {
            selection.append(" AND ").append(PointsOfInterestTable.COL_FULL_OPTION_FLAGS)
            .append(" & ").append(PointsOfInterestTable.VAL_OPTION_FLAG_RIDE_CHILD_SWAP)
            .append(" > 0");
        }
        // Ride venues
        if (filterOptions.getRideFilterVenues() != null && !filterOptions.getRideFilterVenues().isEmpty()) {
            selection.append(" AND ").append(PointsOfInterestTable.COL_FULL_VENUE_ID).append(" IN (");
            for (int i = 0; i < filterOptions.getRideFilterVenues().size(); i++) {
                if (i > 0) {
                    selection.append(", ");
                }
                selection.append("'").append(filterOptions.getRideFilterVenues().get(i)).append("'");
            }
            selection.append(")");
        }
        // Ride sub-types, sub-types are handled by filter tabs for rides and wait times so skip them
        if (filterOptions.getRideSubTypes() != null && !filterOptions.getRideSubTypes().isEmpty()) {
            switch (filterOptions.getExploreType()) {
                case RIDES:
                case WAIT_TIMES:
                    break;
                default:
                    selection.append(" AND (");
                    String columnName = PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS;
                    for (int i = 0; i < filterOptions.getRideSubTypes().size(); i++) {
                        String columnValue = filterOptions.getRideSubTypes().get(i);
                        if (i > 0) {
                            selection.append(" OR ");
                        }
                        selection.append(columnName).append(" & ").append("'").append(columnValue)
                                .append("'").append(" > 0");
                    }
                    selection.append(")");
                    break;
            }
        }
        // Parental supervision required
        if (filterOptions.isParentalSupervisionRequired()) {
            selection.append(" AND ").append(PointsOfInterestTable.COL_FULL_OPTION_FLAGS)
            .append(" & ").append(PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_NO_UNATTENDED_CHILDREN)
            .append(" > 0");
        }
        // Life jacket required
        if (filterOptions.isLifeJacketRequired()) {
            selection.append(" AND ").append(PointsOfInterestTable.COL_FULL_OPTION_FLAGS)
            .append(" & ").append(PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_LIFE_VEST_REQUIRED)
            .append(" > 0");
        }
	}

	/**
	 *  ************************ EXPLORE CONTENT ************************
	 */

	private static Uri getExploreContentUri() {
	    // Current time formatted as show time in park's timezone
        SimpleDateFormat showTimeSdf = new SimpleDateFormat(DATE_FORMAT_SHOW_TIME, Locale.US);
        showTimeSdf.setTimeZone(DateTimeUtils.getParkTimeZone());
	    String timeNow = showTimeSdf.format(new Date());
	    
		// Set the query on the POI table joining the venue table
		String sqlTableStatement = new StringBuilder(UniversalOrlandoContentUris.CUSTOM_TABLE.toString()).append("/")
				.append(PointsOfInterestTable.TABLE_NAME)
				.append("\nLEFT OUTER JOIN ").append(VenuesTable.TABLE_NAME)
				.append(" ON (").append(PointsOfInterestTable.COL_FULL_VENUE_ID)
				.append(" = ").append(VenuesTable.COL_FULL_VENUE_ID).append(")")
				.append("\nLEFT JOIN (SELECT ").append(ShowTimesTable.COL_POI_ID).append(" , min(").append(ShowTimesTable.COL_SHOW_TIME).append(" ) AS ").append(ShowTimesTable.COL_SHOW_TIME)
				.append("\nFROM ").append(ShowTimesTable.TABLE_NAME)
				.append("\nWHERE ").append(ShowTimesTable.COL_SHOW_TIME).append(" >= '").append(timeNow).append("'")
				.append(" AND ").append(ShowTimesTable.COL_TIME_TYPE).append("='").append(ShowTimesTable.TIME_TYPE_START_TIME).append("'")
				.append("\nGROUP BY ").append(ShowTimesTable.COL_POI_ID)
				.append("\nORDER BY ").append(ShowTimesTable.COL_SHOW_TIME).append(" ) AS ").append(ShowTimesTable.TABLE_NAME)
				.append("\nON (").append(PointsOfInterestTable.COL_FULL_POI_ID)
				.append(" = ").append(ShowTimesTable.COL_FULL_POI_ID).append(")")
                .append("\nLEFT OUTER JOIN ").append(VenueLandsTable.TABLE_NAME)
                .append(" ON (").append(PointsOfInterestTable.COL_FULL_VENUE_LAND_ID)
                .append(" = ").append(VenueLandsTable.COL_FULL_VENUE_LAND_ID).append(")")
				.toString();

		Uri contentUri = Uri.parse(sqlTableStatement);
		return contentUri;
	}

	private static String[] getExploreProjection(Location userLocation, FilterSort filterSort) {
		// Get columns needed by the explore UI
	    String sortValue = filterSort == null ? "" : String.valueOf(filterSort.ordinal());
		if (userLocation != null) {
			String[] projection = {
					PointsOfInterestTable.COL_FULL_ID,
					PointsOfInterestTable.COL_FULL_POI_ID,
					PointsOfInterestTable.COL_FULL_DISPLAY_NAME + " AS " + PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME,
					PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
					PointsOfInterestTable.COL_FULL_WAIT_TIME,
					PointsOfInterestTable.COL_FULL_SHOW_TIMES_JSON,
					PointsOfInterestTable.COL_FULL_VENUE_ID,
					PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
					PointsOfInterestTable.COL_FULL_LIST_IMAGE_URL,
					PointsOfInterestTable.COL_FULL_LATITUDE,
					PointsOfInterestTable.COL_FULL_LONGITUDE,
					PointsOfInterestTable.COL_FULL_IS_ROUTABLE,
					PointsOfInterestTable.COL_FULL_IS_FAVORITE,
					PointsOfInterestTable.COL_FULL_POI_HASH_CODE,
					PointsOfInterestTable.COL_FULL_POI_OBJECT_JSON,
					VenuesTable.COL_FULL_DISPLAY_NAME + " AS " + VenuesTable.COL_ALIAS_DISPLAY_NAME,
					VenuesTable.COL_FULL_HOURS_LIST_JSON,
					VenuesTable.COL_FULL_VENUE_OBJECT_JSON,
                    VenueLandsTable.COL_FULL_DISPLAY_NAME + " AS " + VenueLandsTable.COL_ALIAS_DISPLAY_NAME,
                    VenueLandsTable.COL_FULL_VENUE_LAND_OBJECT_JSON,
                    getDistanceApproxCol(userLocation) + " AS " + COL_TEMP_DISTANCE_APPROX_SQUARED_IN_METERS,
					ShowTimesTable.COL_FULL_SHOW_TIME,
					"'" + sortValue + "' AS " + UniversalOrlandoDatabaseTables.COL_FILTER_SORT,
					"'" + userLocation.getLatitude() + "' AS " + UniversalOrlandoDatabaseTables.COL_USER_LATITUDE,
					"'" + userLocation.getLongitude() + "' AS " + UniversalOrlandoDatabaseTables.COL_USER_LONGITUDE

			};
			return projection;
		}
		else {
			String[] projection = {
					PointsOfInterestTable.COL_FULL_ID,
					PointsOfInterestTable.COL_FULL_POI_ID,
					PointsOfInterestTable.COL_FULL_DISPLAY_NAME + " AS " + PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME,
					PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
					PointsOfInterestTable.COL_FULL_WAIT_TIME,
					PointsOfInterestTable.COL_FULL_SHOW_TIMES_JSON,
					PointsOfInterestTable.COL_FULL_VENUE_ID,
					PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
					PointsOfInterestTable.COL_FULL_LIST_IMAGE_URL,
					PointsOfInterestTable.COL_FULL_LATITUDE,
					PointsOfInterestTable.COL_FULL_LONGITUDE,
					PointsOfInterestTable.COL_FULL_IS_ROUTABLE,
					PointsOfInterestTable.COL_FULL_IS_FAVORITE,
					PointsOfInterestTable.COL_FULL_POI_HASH_CODE,
					PointsOfInterestTable.COL_FULL_POI_OBJECT_JSON,
					VenuesTable.COL_FULL_DISPLAY_NAME + " AS " + VenuesTable.COL_ALIAS_DISPLAY_NAME,
					VenuesTable.COL_FULL_HOURS_LIST_JSON,
					VenuesTable.COL_FULL_VENUE_OBJECT_JSON,
                    VenueLandsTable.COL_FULL_DISPLAY_NAME + " AS " + VenueLandsTable.COL_ALIAS_DISPLAY_NAME,
                    VenueLandsTable.COL_FULL_VENUE_LAND_OBJECT_JSON,
                    ShowTimesTable.COL_FULL_SHOW_TIME,
                    "'" + sortValue + "' AS " + UniversalOrlandoDatabaseTables.COL_FILTER_SORT
			};
			return projection;
		}

	}

	/**
	 *  ************************ EXPLORE BY VENUE ************************
	 */
	
	private static String getExploreByVenueOrderBy(FilterOptions filterOptions, 
            String columnName, Location userLocation) {
        StringBuilder orderBy = new StringBuilder();
        // Ignore filter options if null
        if(filterOptions != null && columnName != null) {
            List<FilterTabOption> filterBarTabOptions = filterOptions.getFilterTabOptions();
            
            switch(filterOptions.getFilterSort()) {
                case ALPHABETICAL:
                    break;
                case DISTANCE:
                {
                    orderBy.append("\n").append(COL_TEMP_DISTANCE_APPROX_SQUARED_IN_METERS).append(" ASC, ");
                    break;
                }
                case WAIT_TIMES:
                {
                    orderBy.append("\nCASE ");
                    orderBy.append("\n WHEN (").append(PointsOfInterestTable.COL_FULL_WAIT_TIME)
                    .append(" >= ").append("'").append(1).append("')")
                    .append(" THEN ").append(PointsOfInterestTable.COL_FULL_WAIT_TIME);
    
                    int order = 1000;
    
                    orderBy.append("\n WHEN (").append(PointsOfInterestTable.COL_FULL_WAIT_TIME)
                    .append(" = ").append("'").append(Ride.RIDE_WAIT_TIME_STATUS_CLOSED_INCLEMENT_WEATHER).append("')")
                    .append(" THEN ").append(order++);
    
                    orderBy.append("\n WHEN (").append(PointsOfInterestTable.COL_FULL_WAIT_TIME)
                    .append(" = ").append("'").append(Ride.RIDE_WAIT_TIME_STATUS_CLOSED_TEMP).append("')")
                    .append(" THEN ").append(order++);
    
                    orderBy.append("\n WHEN (").append(PointsOfInterestTable.COL_FULL_WAIT_TIME)
                    .append(" = ").append("'").append(Ride.RIDE_WAIT_TIME_STATUS_OUT_OF_OPERATING_HOURS).append("')")
                    .append(" THEN ").append(order++);
    
                    orderBy.append("\n WHEN (").append(PointsOfInterestTable.COL_FULL_WAIT_TIME)
                    .append(" = ").append("'").append(Ride.RIDE_WAIT_TIME_STATUS_CLOSED_LONG_TERM).append("')")
                    .append(" THEN ").append(order++);
                    
                    orderBy.append("\n WHEN (").append(PointsOfInterestTable.COL_FULL_WAIT_TIME)
                    .append(" = ").append("'").append(0).append("')")
                    .append(" THEN ").append(order++);
    
                    orderBy.append("\n WHEN (").append(PointsOfInterestTable.COL_FULL_WAIT_TIME)
                    .append(" = ").append("'").append(Ride.RIDE_WAIT_TIME_STATUS_NOT_AVAILABLE).append("')")
                    .append(" THEN ").append(order++);
    
                    orderBy.append("\n ELSE ").append(order++).append(" END ASC, ");
                    break;
                }
                case TYPE:
                {
                    if (filterBarTabOptions != null && filterBarTabOptions.size() > 0) {
                        orderBy.append("CASE ").append(columnName);

                        for (int i = 0; i < filterBarTabOptions.size(); i++) {
                            FilterTabOption filterTabOptionOn = filterBarTabOptions.get(i);

                            String[] columnValues = filterTabOptionOn.getColumnValues();
                            for (int j = 0; j < columnValues.length; j++) {
                                String columnValue = columnValues[j];

                                orderBy.append("\n WHEN '").append(columnValue).append("' THEN ").append(i);
                            }
                        }
                        orderBy.append("\n ELSE ").append(filterBarTabOptions.size()).append(" END, ");
                    }
                    break;
                }
                case SHOW_TIMES:
                    orderBy.append("\n IFNULL(").append(ShowTimesTable.COL_SHOW_TIME).append(", '")
                    .append(SHOW_TIME_MAX_DATE).append("') ASC,")
                    .append("\n ").append(PointsOfInterestTable.COL_SHOW_TIMES_JSON).append(",");
                    break;
                case EVENT_DATE:
                    
                    break;
            }
        }
        
        orderBy.append("\n").append(PointsOfInterestTable.COL_FULL_DISPLAY_NAME).append(" COLLATE NOCASE ASC");
        return orderBy.toString();
    }
	
	public static DatabaseQuery getExploreByVenueDatabaseQuery(FilterOptions filterOptions, boolean isRestroomToggleOn, long venueId) {
	    List<FilterTabOption> filterBarTabOptions = filterOptions.getFilterTabOptions();
	    
        Uri contentUri = getExploreContentUri();
        String[] projection = getExploreProjection(filterOptions.getUserLocation(), filterOptions.getFilterSort());

        // Find all the options that are on
        String columnName = "";
        List<FilterTabOption> filterBarTabOptionsOn = new ArrayList<FilterTabOption>();
        for (FilterTabOption filterTabOption : filterBarTabOptions) {
            if (filterTabOption.isChecked()) {
                filterBarTabOptionsOn.add(filterTabOption);
            }
            columnName = filterTabOption.getColumnName();
        }

        // Match rows that are in the selected venue
        StringBuilder selection = new StringBuilder(PointsOfInterestTable.COL_FULL_VENUE_ID)
        .append(" IN (").append(venueId).append(")");

        // Match rows that are in one of the selected filters
        selection.append(" AND ").append(columnName).append(" IN (");
        for (int i = 0; i < filterBarTabOptionsOn.size(); i++) {
            FilterTabOption filterTabOptionOn = filterBarTabOptionsOn.get(i);

            String[] columnValues = filterTabOptionOn.getColumnValues();
            for (int j = 0; j < columnValues.length; j++) {
                String columnValue = columnValues[j];

                if (i > 0 || j > 0) {
                    selection.append(", ");
                }
                selection.append("'").append(columnValue).append("'");
            }
        }
        // If the restroom toggle is on, add it to the list
        if (isRestroomToggleOn) {
            if (filterBarTabOptionsOn.size() > 0) {
                selection.append(", ");
            }
            selection.append("'").append(PointsOfInterestTable.VAL_POI_TYPE_ID_RESTROOM).append("'");
        }
        selection.append(")");

        selection.append(" AND " + PointsOfInterestTable.COL_FULL_LATITUDE + " != '0'");
        selection.append(" AND " + PointsOfInterestTable.COL_FULL_LONGITUDE + " != '0'");

        // Advanced Filter selection
        setVenueAdvancedSelection(filterOptions, selection, venueId, isRestroomToggleOn);

        // Not using selection args
        String[] selectionArgs = null;

        // Order the rows grouped in the order of the category tabs
        String orderBy = getExploreByVenueOrderBy(filterOptions, columnName, filterOptions.getUserLocation());

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getExploreByVenueDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
                    + "\n projection = " + (projection == null ? "null" : projection)
                    + "\n selection = " + (selection == null ? "null" : selection.toString())
                    + "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
                    + "\n orderBy = " + (orderBy == null ? "null" : orderBy)
                    );
        }
        return (new DatabaseQuery(contentUri.toString(), projection, selection.toString(), selectionArgs, orderBy));
    }

	public static DatabaseQuery getExploreByVenueDatabaseQuery(long venueId, Location userLocation, long... poiTypeIds) {
		Uri contentUri = getExploreContentUri();
		String[] projection = getExploreProjection(userLocation, null);

		// Match rows that are in the selected venue
		StringBuilder selection = new StringBuilder(PointsOfInterestTable.COL_FULL_VENUE_ID)
		.append(" IN (").append(venueId).append(")");

		// Filter by the POI type
		selection.append(" AND ").append(PointsOfInterestTable.COL_FULL_POI_TYPE_ID)
		.append(" IN (");

		for (int i = 0; i < poiTypeIds.length; i++) {
			long poiTypeId = poiTypeIds[i];

			if (i > 0) {
				selection.append(", ");
			}
			selection.append("'").append(poiTypeId).append("'");
		}
		selection.append(")");

		// Not using selection args
		String[] selectionArgs = null;

		// Order the rows grouped in the order of the category tabs
		String orderBy = getExploreByVenueOrderBy(null, null, userLocation);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getExploreByVenueDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
					+ "\n projection = " + (projection == null ? "null" : projection)
					+ "\n selection = " + (selection == null ? "null" : selection.toString())
					+ "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
					+ "\n orderBy = " + (orderBy == null ? "null" : orderBy)
					);
		}
		return (new DatabaseQuery(contentUri.toString(), projection, selection.toString(), selectionArgs, orderBy));
	}

	/**
	 *  ************************ EXPLORE BY ACTIVITY ************************
	 */
	
    private static String getExploreByActivityOrderBy(FilterOptions filterOptions, 
            String columnName, Location userLocation) {
        StringBuilder orderBy = new StringBuilder();
        // Ignore filter options if null
        if (filterOptions != null && columnName != null) {
            List<FilterTabOption> filterBarTabOptions = filterOptions.getFilterTabOptions();

            switch (filterOptions.getFilterSort()) {
                case ALPHABETICAL:
                    break;
                case DISTANCE:
                {
                    if (userLocation != null) {
                        orderBy.append("\n").append(COL_TEMP_DISTANCE_APPROX_SQUARED_IN_METERS).append(" ASC, ");
                        break;
                    }
                }
                case WAIT_TIMES:
                {
                    orderBy.append("\nCASE ");
                    orderBy.append("\n WHEN (").append(PointsOfInterestTable.COL_FULL_WAIT_TIME)
                    .append(" >= ").append("'").append(1).append("')")
                    .append(" THEN ").append(PointsOfInterestTable.COL_FULL_WAIT_TIME);
    
                    int order = 1000;
    
                    orderBy.append("\n WHEN (").append(PointsOfInterestTable.COL_FULL_WAIT_TIME)
                    .append(" = ").append("'").append(Ride.RIDE_WAIT_TIME_STATUS_CLOSED_INCLEMENT_WEATHER).append("')")
                    .append(" THEN ").append(order++);
    
                    orderBy.append("\n WHEN (").append(PointsOfInterestTable.COL_FULL_WAIT_TIME)
                    .append(" = ").append("'").append(Ride.RIDE_WAIT_TIME_STATUS_CLOSED_TEMP).append("')")
                    .append(" THEN ").append(order++);
    
                    orderBy.append("\n WHEN (").append(PointsOfInterestTable.COL_FULL_WAIT_TIME)
                    .append(" = ").append("'").append(Ride.RIDE_WAIT_TIME_STATUS_OUT_OF_OPERATING_HOURS).append("')")
                    .append(" THEN ").append(order++);
    
                    orderBy.append("\n WHEN (").append(PointsOfInterestTable.COL_FULL_WAIT_TIME)
                    .append(" = ").append("'").append(Ride.RIDE_WAIT_TIME_STATUS_CLOSED_LONG_TERM).append("')")
                    .append(" THEN ").append(order++);
                    
                    orderBy.append("\n WHEN (").append(PointsOfInterestTable.COL_FULL_WAIT_TIME)
                    .append(" = ").append("'").append(0).append("')")
                    .append(" THEN ").append(order++);
    
                    orderBy.append("\n WHEN (").append(PointsOfInterestTable.COL_FULL_WAIT_TIME)
                    .append(" = ").append("'").append(Ride.RIDE_WAIT_TIME_STATUS_NOT_AVAILABLE).append("')")
                    .append(" THEN ").append(order++);
    
                    orderBy.append("\n ELSE ").append(order++).append(" END ASC, ");
                    break;
                }
                case TYPE:
                {
                    if (filterBarTabOptions != null && filterBarTabOptions.size() > 0) {
                        orderBy.append("CASE ");
    
                        for (int i = 0; i < filterBarTabOptions.size() ; i++) {
                            FilterTabOption filterTabOption = filterBarTabOptions.get(i);
    
                            String[] columnValues = filterTabOption.getColumnValues();
                            for (int j = 0; j < columnValues.length; j++) {
                                String columnValue = columnValues[j];
    
                                orderBy.append("\n WHEN (").append(columnName)
                                .append(" & ")
                                .append("'").append(columnValue).append("'")
                                .append(" > 0) THEN ")
                                .append(i);
                            }
                        }
                        orderBy.append("\n ELSE ").append(filterBarTabOptions.size()).append(" END, ");
                    }
                    break;
                }
                case SHOW_TIMES:
                    orderBy.append("\n IFNULL(").append(ShowTimesTable.COL_SHOW_TIME).append(", '")
                    .append(SHOW_TIME_MAX_DATE).append("') ASC,")
                    .append("\n ").append(PointsOfInterestTable.COL_SHOW_TIMES_JSON).append(",");
                    break;
                case EVENT_DATE:
                    orderBy.append("\n").append(EventTimesTable.COL_START_DATE).append(" IS NULL ASC, ");
                    break;
            }
        }
        
        orderBy.append("\n").append(PointsOfInterestTable.COL_FULL_DISPLAY_NAME).append(" COLLATE NOCASE ASC");
        return orderBy.toString();
    }

    /**
     * Filter tabs are POI types rather than sub-types
     * 
     * @param filterOptions
     * @param isExpressPassSoldHere
     * @param sortByWaitTimes
     * @param poiTypeIds
     * @return
     */
    public static DatabaseQuery getExploreByPoiTypeDatabaseQuery(FilterOptions filterOptions, boolean isExpressPassSoldHere,
            boolean sortByWaitTimes, long... poiTypeIds) {
        List<FilterTabOption> filterBarTabOptions = filterOptions.getFilterTabOptions();
        
        Uri contentUri = getExploreContentUri();
        String[] projection = getExploreProjection(filterOptions.getUserLocation(), filterOptions.getFilterSort());

        // Filter by the POI type
        StringBuilder selection = new StringBuilder(PointsOfInterestTable.COL_FULL_POI_TYPE_ID)
        .append(" IN (");

        for (int i = 0; i < poiTypeIds.length; i++) {
            long poiTypeId = poiTypeIds[i];

            if (i > 0) {
                selection.append(", ");
            }
            selection.append("'").append(poiTypeId).append("'");
        }
        selection.append(")");

        // If the is a filter tab bar, filter by the tabs that are on
        List<FilterTabOption> filterBarTabOptionsOn = new ArrayList<FilterTabOption>();
        String columnName = "";
        if (filterBarTabOptions != null && filterBarTabOptions.size() > 0) {
            // Find all the options that are on
            for (FilterTabOption filterTabOption : filterBarTabOptions) {
                if (filterTabOption.isChecked()) {
                    filterBarTabOptionsOn.add(filterTabOption);
                }
                columnName = filterTabOption.getColumnName();
            }

            if (filterBarTabOptionsOn.size() > 0) {
                // Match rows that are in one of the selected filters
                selection.append(" AND ").append(columnName).append(" IN (");
                for (int i = 0; i < filterBarTabOptionsOn.size(); i++) {
                    FilterTabOption filterTabOptionOn = filterBarTabOptionsOn.get(i);

                    String[] columnValues = filterTabOptionOn.getColumnValues();
                    for (int j = 0; j < columnValues.length; j++) {
                        String columnValue = columnValues[j];

                        if (i > 0 || j > 0) {
                            selection.append(", ");
                        }
                        selection.append("'").append(columnValue).append("'");
                    }
                }
                selection.append(")");
            }
            // If no filter tabs are on, select nothing
            else {
                selection = new StringBuilder(PointsOfInterestTable.COL_FULL_POI_TYPE_ID)
                .append(" IN ()");
            }
        }

        // If the express pass filter is on, add it
        if (isExpressPassSoldHere) {
            selection.append(" AND (" + PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS).append(" & ")
            .append("'").append(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_SELLS_EXPRESS_PASS).append("' > 0)");
        }

        // Advanced Filter selection
        setActivityAdvancedSelection(filterOptions, selection);

        // Not using selection args
        String[] selectionArgs = null;

        // Order the rows grouped in the order of the category tabs
        // Venue order by needed as the filter tabs contain poi types
        String orderBy = getExploreByVenueOrderBy(filterOptions, columnName, filterOptions.getUserLocation());

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getExploreByActivityDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
                    + "\n projection = " + (projection == null ? "null" : projection)
                    + "\n selection = " + (selection == null ? "null" : selection.toString())
                    + "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
                    + "\n orderBy = " + (orderBy == null ? "null" : orderBy)
                    );
        }
        return (new DatabaseQuery(contentUri.toString(), projection, selection.toString(), selectionArgs, orderBy));
    }
	
	/**
	 * 
	 * @param filterOptions
	 * @param isExpressPassSoldHere
	 * @param sortByWaitTimes
	 * @param poiTypeIds
	 * @return
	 */
	public static DatabaseQuery getExploreByActivityDatabaseQuery(FilterOptions filterOptions, boolean isExpressPassSoldHere,
            boolean sortByWaitTimes, long... poiTypeIds) {
	    List<FilterTabOption> filterBarTabOptions = filterOptions.getFilterTabOptions();
	    
	    Uri contentUri = getExploreContentUri();
        String[] projection = getExploreProjection(filterOptions.getUserLocation(), filterOptions.getFilterSort());

        // Filter by the POI type
        StringBuilder selection = new StringBuilder(PointsOfInterestTable.COL_FULL_POI_TYPE_ID)
        .append(" IN (");

        for (int i = 0; i < poiTypeIds.length; i++) {
            long poiTypeId = poiTypeIds[i];

            if (i > 0) {
                selection.append(", ");
            }
            selection.append("'").append(poiTypeId).append("'");
        }
        selection.append(")");
        selection.append(" AND " + PointsOfInterestTable.COL_FULL_LATITUDE + " != '0' ");

        // If the is a filter tab bar, filter by the tabs that are on
        List<FilterTabOption> filterBarTabOptionsOn = new ArrayList<FilterTabOption>();
        String columnName = "";
        if (filterBarTabOptions != null && filterBarTabOptions.size() > 0) {
            // Find all the options that are on
            for (FilterTabOption filterTabOption : filterBarTabOptions) {
                if (filterTabOption.isChecked()) {
                    filterBarTabOptionsOn.add(filterTabOption);
                }
                columnName = filterTabOption.getColumnName();
            }

            // Match rows that are one of the selected filters
            if (filterBarTabOptionsOn.size() > 0) {
                selection.append(" AND (");
                for (int i = 0; i < filterBarTabOptionsOn.size(); i++) {
                    FilterTabOption filterTabOptionOn = filterBarTabOptionsOn.get(i);

                    String[] columnValues = filterTabOptionOn.getColumnValues();
                    for (int j = 0; j < columnValues.length; j++) {
                        String columnValue = columnValues[j];

                        if (i > 0 || j > 0) {
                            selection.append(" OR ");
                        }
                        selection.append(columnName)
                        .append(" & ")
                        .append("'").append(columnValue).append("'")
                        .append(" > 0");
                    }
                }
                selection.append(")");
            }
            // If no filter tabs are on, select nothing
            else {
                selection = new StringBuilder(PointsOfInterestTable.COL_FULL_POI_TYPE_ID)
                .append(" IN ()");
            }
        }

        // If the express pass filter is on, add it
        if (isExpressPassSoldHere) {
            selection.append(" AND (" + PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS).append(" & ")
            .append("'").append(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_SELLS_EXPRESS_PASS).append("' > 0)");
        }

        if (filterOptions.hasCocaCola) {
            selection.append(" AND (").append(PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS)
                    .append(" & ").append(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINNING_COCA_COLA_FREESTYLE)
                    .append(" > 0)");
        }

        // Advanced Filter selection
        setActivityAdvancedSelection(filterOptions, selection);

        // Not using selection args
        String[] selectionArgs = null;

        // Order the rows grouped in the order of the sub-category tabs
        String orderBy = getExploreByActivityOrderBy(filterOptions, columnName, filterOptions.getUserLocation());

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getExploreByActivityDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
                    + "\n projection = " + (projection == null ? "null" : projection)
                    + "\n selection = " + (selection == null ? "null" : selection.toString())
                    + "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
                    + "\n orderBy = " + (orderBy == null ? "null" : orderBy)
                    );
        }
        return (new DatabaseQuery(contentUri.toString(), projection, selection.toString(), selectionArgs, orderBy));
	}

    public static DatabaseQuery getExploreByShortestWaitTimesDatabaseQuery(Integer rowLimit, long... poiTypeIds) {
        FilterOptions filterOptions = new FilterOptions(null, ExploreType.WAIT_TIMES);
        filterOptions.setFilterSort(FilterSort.WAIT_TIMES);

        Uri contentUri = getExploreContentUri();
        String[] projection = getExploreProjection(filterOptions.getUserLocation(), filterOptions.getFilterSort());

        // Filter by the POI type
        StringBuilder selection = new StringBuilder(PointsOfInterestTable.COL_FULL_POI_TYPE_ID)
                .append(" IN (");

        for (int i = 0; i < poiTypeIds.length; i++) {
            long poiTypeId = poiTypeIds[i];

            if (i > 0) {
                selection.append(", ");
            }
            selection.append("'").append(poiTypeId).append("'");
        }
        selection.append(")");

        // Only get POIs with valid wait times
        selection.append(" AND ").append(PointsOfInterestTable.COL_FULL_WAIT_TIME).append(" > 0");

        // Advanced Filter selection
        setActivityAdvancedSelection(filterOptions, selection);

        // Not using selection args
        String[] selectionArgs = null;

        // Order the rows by wait time
        String columnName = "";
        StringBuilder orderByBuilder = new StringBuilder(getExploreByActivityOrderBy(filterOptions, columnName, filterOptions.getUserLocation()));

        // If a limit is passed in, set it
        if (rowLimit != null && rowLimit >= 0) {
            orderByBuilder.append(" LIMIT ").append(rowLimit);
        }
        String orderBy = orderByBuilder.toString();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getExploreByActivityDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
                       + "\n projection = " + (projection == null ? "null" : projection)
                       + "\n selection = " + (selection == null ? "null" : selection.toString())
                       + "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
                       + "\n orderBy = " + (orderBy == null ? "null" : orderBy)
            );
        }
        return (new DatabaseQuery(contentUri.toString(), projection, selection.toString(), selectionArgs, orderBy));
    }


    /**
	 *  ************************ FAVORITES ************************
	 */

	public static DatabaseQuery getExploreByFavoritesDatabaseQuery(Location userLocation) {

		Uri contentUri = getExploreContentUri();
		String[] projection = getExploreProjection(userLocation, null);

		// Match rows that are favorites
		StringBuilder selection = new StringBuilder(PointsOfInterestTable.COL_FULL_IS_FAVORITE).append(" != 0");

		// Not using selection args
		String[] selectionArgs = null;

		// Use the same order as search, the rows grouped by categories, and then sort by name alphabetically
		String orderBy = getSearchOrderBy(userLocation);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getExploreByFavoritesDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
					+ "\n projection = " + (projection == null ? "null" : projection)
					+ "\n selection = " + (selection == null ? "null" : selection.toString())
					+ "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
					+ "\n orderBy = " + (orderBy == null ? "null" : orderBy)
					);
		}
		return (new DatabaseQuery(contentUri.toString(), projection, selection.toString(), selectionArgs, orderBy));
	}


	/**
	 *  ************************ SEARCH ************************
	 */

	private static Uri getSearchContentUri() {
		// Set the query on the POI table joining the venue table and land tables
		String sqlTableStatement = new StringBuilder(UniversalOrlandoContentUris.CUSTOM_TABLE.toString()).append("/")
				.append(PointsOfInterestTable.TABLE_NAME)
				.append("\nLEFT OUTER JOIN ").append(VenuesTable.TABLE_NAME)
				.append(" ON (").append(PointsOfInterestTable.COL_FULL_VENUE_ID)
				.append(" = ").append(VenuesTable.COL_FULL_VENUE_ID).append(")")
				.append("\nLEFT OUTER JOIN ").append(VenueLandsTable.TABLE_NAME)
				.append(" ON (").append(PointsOfInterestTable.COL_FULL_VENUE_LAND_ID)
				.append(" = ").append(VenueLandsTable.COL_FULL_VENUE_LAND_ID).append(")").toString();

		Uri contentUri = Uri.parse(sqlTableStatement);
		return contentUri;
	}

	private static String[] getSearchProjection(Location userLocation) {

		// Get columns needed by the search UI
		if (userLocation != null) {
			String[] projection = {
					PointsOfInterestTable.COL_FULL_ID,
					PointsOfInterestTable.COL_FULL_POI_ID,
					PointsOfInterestTable.COL_FULL_DISPLAY_NAME + " AS " + PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME,
					PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
					PointsOfInterestTable.COL_FULL_WAIT_TIME,
					PointsOfInterestTable.COL_FULL_SHOW_TIMES_JSON,
					PointsOfInterestTable.COL_FULL_VENUE_ID,
					PointsOfInterestTable.COL_FULL_POI_OBJECT_JSON,
					PointsOfInterestTable.COL_FULL_THUMBNAIL_IMAGE_URL,
					PointsOfInterestTable.COL_FULL_LATITUDE,
					PointsOfInterestTable.COL_FULL_LONGITUDE,
					PointsOfInterestTable.COL_FULL_POI_HASH_CODE,
					VenuesTable.COL_FULL_VENUE_OBJECT_JSON,
					VenuesTable.COL_FULL_DISPLAY_NAME + " AS " + VenuesTable.COL_ALIAS_DISPLAY_NAME,
					VenueLandsTable.COL_FULL_DISPLAY_NAME + " AS " + VenueLandsTable.COL_ALIAS_DISPLAY_NAME,
                    VenueLandsTable.COL_FULL_VENUE_LAND_OBJECT_JSON,
                    getDistanceApproxCol(userLocation) + " AS " + COL_TEMP_DISTANCE_APPROX_SQUARED_IN_METERS
			};
			return projection;
		}
		else {
			String[] projection = {
					PointsOfInterestTable.COL_FULL_ID,
					PointsOfInterestTable.COL_FULL_POI_ID,
					PointsOfInterestTable.COL_FULL_DISPLAY_NAME + " AS " + PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME,
					PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
					PointsOfInterestTable.COL_FULL_WAIT_TIME,
					PointsOfInterestTable.COL_FULL_SHOW_TIMES_JSON,
					PointsOfInterestTable.COL_FULL_VENUE_ID,
					PointsOfInterestTable.COL_FULL_POI_OBJECT_JSON,
					PointsOfInterestTable.COL_FULL_THUMBNAIL_IMAGE_URL,
					PointsOfInterestTable.COL_FULL_LATITUDE,
					PointsOfInterestTable.COL_FULL_LONGITUDE,
					PointsOfInterestTable.COL_FULL_POI_HASH_CODE,
					VenuesTable.COL_FULL_VENUE_OBJECT_JSON,
					VenuesTable.COL_FULL_DISPLAY_NAME + " AS " + VenuesTable.COL_ALIAS_DISPLAY_NAME,
					VenueLandsTable.COL_FULL_DISPLAY_NAME + " AS " + VenueLandsTable.COL_ALIAS_DISPLAY_NAME,
                    VenueLandsTable.COL_FULL_VENUE_LAND_OBJECT_JSON
            };
			return projection;
		}
	}

	private static String getSearchOrderBy(Location userLocation) {
		StringBuilder orderBy = new StringBuilder("CASE ").append(PointsOfInterestTable.COL_FULL_POI_TYPE_ID)
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE).append("' THEN 1")
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW).append("' THEN 2")
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE).append("' THEN 2") // parade == show
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_DINING).append("' THEN 3")
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT).append("' THEN 4")
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP).append("' THEN 5")
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT).append("' THEN 6")
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL).append("' THEN 7")
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_WATERPARK).append("' THEN 8")
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_RESTROOM).append("' THEN 9")
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_LOCKERS).append("' THEN 10")
                .append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_RENTALS).append("' THEN 11")
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_GUEST_SERVICES_BOOTH).append("' THEN 12")
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_ATM).append("' THEN 13")
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_CHARGING_STATION).append("' THEN 14")
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_LOST_AND_FOUND_STATION).append("' THEN 15")
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_SERVICE_ANIMAL_REST_AREA).append("' THEN 16")
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_FIRST_AID_STATION).append("' THEN 17")
				.append("\n WHEN '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_SMOKING_AREA).append("' THEN 18")
				.append("\n ELSE 999 END, ");

		if (userLocation != null) {
			orderBy.append(COL_TEMP_DISTANCE_APPROX_SQUARED_IN_METERS).append(" ASC");
		}
		else {
			orderBy.append(PointsOfInterestTable.COL_FULL_DISPLAY_NAME).append(" COLLATE NOCASE ASC");
		}


		return orderBy.toString();
	}

	public static DatabaseQuery getSearchDatabaseQuery(String searchQuery, Location userLocation) {

		Uri contentUri = getSearchContentUri();
		String[] projection = getSearchProjection(userLocation);

		// Only allow searching certain POI types
		StringBuilder selection = new StringBuilder(PointsOfInterestTable.COL_FULL_POI_TYPE_ID).append(" IN (");
		selection.append("'").append(PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE).append("'")
		.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_DINING).append("'")
		.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW).append("'")
		.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL).append("'")
		.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE).append("'")
		.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_RESTROOM).append("'")
		.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_ATM).append("'")
		//.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_PARKING).append("'")
		.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP).append("'")
		.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_LOCKERS).append("'")
        .append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_RENTALS).append("'")
		.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_SERVICE_ANIMAL_REST_AREA).append("'")
		.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_SMOKING_AREA).append("'")
		//.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_PACKAGE_PICKUP).append("'")
		.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_LOST_AND_FOUND_STATION).append("'")
		.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_GUEST_SERVICES_BOOTH).append("'")
		.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_FIRST_AID_STATION).append("'")
		//.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_CHARGING_STATION).append("'")
		//.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_PHONE_CARD_DISPENSER).append("'")
		.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT).append("'")
		.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_WATERPARK).append("'")
		.append(", '").append(PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT).append("'")
		.append(")");

		// Replace all characters that are not alphanumeric with wildcards
		searchQuery = searchQuery.replaceAll("[^a-zA-Z0-9]", SQLITE_WILDCARD);

		// Insert a single wildcard in between each valid query term
		String[] searchQueryTerms = searchQuery.split(SQLITE_WILDCARD);
		StringBuilder newSearchQuery = new StringBuilder();
		for (int i = 0; i < searchQueryTerms.length; i++) {

			String queryTerm = searchQueryTerms[i];
			if (queryTerm != null && !queryTerm.isEmpty()) {
				if (i > 0 && newSearchQuery.length() > 0) {
					newSearchQuery.append(SQLITE_WILDCARD);
				}
				newSearchQuery.append(queryTerm);
			}
		}

		// If the search has no valid characters, search for nothing
		if (newSearchQuery.toString().isEmpty()) {
			searchQuery = SQLITE_NO_MATCH;
		}
		// Otherwise, add wild cards to the front and back of the query to allow
		// partial searching
		else {
			searchQuery = newSearchQuery
					.insert(0, SQLITE_WILDCARD)
					.append(SQLITE_WILDCARD)
					.toString();
		}

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getSearchPoisDatabaseQuery: searchQuery = " + searchQuery);
		}

		// Match POI names
		selection.append(" AND (").append(PointsOfInterestTable.COL_FULL_DISPLAY_NAME)
		.append(" LIKE '").append(searchQuery).append("'");

		// Or match venue names
		selection.append(" OR ").append(VenuesTable.COL_FULL_DISPLAY_NAME)
		.append(" LIKE '").append(searchQuery).append("'");

		// Or match venue land names
		selection.append(" OR ").append(VenueLandsTable.COL_FULL_DISPLAY_NAME)
		.append(" LIKE '").append(searchQuery).append("'");
		
        // Or match POI tags
        for (String term : searchQueryTerms) {
            if (!TextUtils.isEmpty(term)) {
                selection.append(" OR ").append(PointsOfInterestTable.COL_FULL_TAGS).append(" LIKE '%|")
                        .append(term).append("|%'");
            }
        }
		selection.append(")");

		// Not using selection args
		String[] selectionArgs = null;

		// Order the rows grouped by categories, and then sort by name alphabetically
		String orderBy = getSearchOrderBy(userLocation);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getSearchPoisDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
					+ "\n projection = " + (projection == null ? "null" : projection)
					+ "\n selection = " + (selection == null ? "null" : selection.toString())
					+ "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
					+ "\n orderBy = " + (orderBy == null ? "null" : orderBy)
					);
		}
		return (new DatabaseQuery(contentUri.toString(), projection, selection.toString(), selectionArgs, orderBy));
	}


	/**
	 *  ************************ DETAIL ************************
	 */

	private static Uri getDetailContentUri() {
		// Set the query on the POI table joining the venue table and land tables
		String sqlTableStatement = new StringBuilder(UniversalOrlandoContentUris.CUSTOM_TABLE.toString()).append("/")
				.append(PointsOfInterestTable.TABLE_NAME)
				.append("\nLEFT OUTER JOIN ").append(VenuesTable.TABLE_NAME)
				.append(" ON (").append(PointsOfInterestTable.COL_FULL_VENUE_ID)
				.append(" = ").append(VenuesTable.COL_FULL_VENUE_ID).append(")")
				.append("\nLEFT OUTER JOIN ").append(VenueLandsTable.TABLE_NAME)
				.append(" ON (").append(PointsOfInterestTable.COL_FULL_VENUE_LAND_ID)
				.append(" = ").append(VenueLandsTable.COL_FULL_VENUE_LAND_ID).append(")").toString();

		Uri contentUri = Uri.parse(sqlTableStatement);
		return contentUri;
	}

	private static String[] getDetailProjection() {
		// Get columns needed by the detail UI
		String[] projection = {
				PointsOfInterestTable.COL_FULL_ID,
				PointsOfInterestTable.COL_FULL_POI_ID,
				PointsOfInterestTable.COL_FULL_DISPLAY_NAME + " AS " + PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME,
				PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
				PointsOfInterestTable.COL_FULL_WAIT_TIME,
				PointsOfInterestTable.COL_FULL_SHOW_TIMES_JSON,
				PointsOfInterestTable.COL_FULL_VENUE_ID,
				PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
				PointsOfInterestTable.COL_FULL_POI_OBJECT_JSON,
				PointsOfInterestTable.COL_FULL_LATITUDE,
				PointsOfInterestTable.COL_FULL_LONGITUDE,
				PointsOfInterestTable.COL_FULL_IS_FAVORITE,
				PointsOfInterestTable.COL_FULL_IS_ROUTABLE,
				PointsOfInterestTable.COL_FULL_POI_HASH_CODE,
				VenuesTable.COL_FULL_HOURS_LIST_JSON,
				VenuesTable.COL_FULL_VENUE_OBJECT_JSON,
				VenuesTable.COL_FULL_DISPLAY_NAME + " AS " + VenuesTable.COL_ALIAS_DISPLAY_NAME,
				VenueLandsTable.COL_FULL_DISPLAY_NAME + " AS " + VenueLandsTable.COL_ALIAS_DISPLAY_NAME,
                VenueLandsTable.COL_FULL_VENUE_LAND_OBJECT_JSON
		};
		return projection;
	}

	public static DatabaseQuery getDetailDatabaseQuery(long poiId) {
		List<Long> poiIdList = new ArrayList<Long>();
		poiIdList.add(poiId);
		return getDetailDatabaseQuery(poiIdList);
	}

	public static DatabaseQuery getDetailDatabaseQuery(List<Long> poiIdList) {

		Uri contentUri = getDetailContentUri();
		String[] projection = getDetailProjection();

		// Match the POI by its ID
		StringBuilder selectionBuilder = new StringBuilder(PointsOfInterestTable.COL_FULL_POI_ID)
		.append(" IN (");
		if (poiIdList != null) {
			for (int i = 0; i < poiIdList.size(); i++) {
				if (i > 0) {
					selectionBuilder.append(", ");
				}
				selectionBuilder.append(poiIdList.get(i));
			}
		}
		selectionBuilder.append(")");
		String selection = selectionBuilder.toString();

		// Not using selection args
		String[] selectionArgs = null;

		// Order the rows doesn't matter, because there should only be one
		String orderBy = "";

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getDetailDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
					+ "\n projection = " + (projection == null ? "null" : projection)
					+ "\n selection = " + (selection == null ? "null" : selection)
					+ "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
					+ "\n orderBy = " + (orderBy == null ? "null" : orderBy)
					);
		}
		return (new DatabaseQuery(contentUri.toString(), projection, selection, selectionArgs, orderBy));
	}

	/**
	 *  ************************ HOME ************************
	 */

	private static Uri getVenueContentUri() {
		// Set the query on the venue table
		String sqlTableStatement = UniversalOrlandoContentUris.VENUES.toString();

		Uri contentUri = Uri.parse(sqlTableStatement);
		return contentUri;
	}

	private static String[] getVenueProjection() {
		// Get columns needed by the home UI
		String[] projection = {
				VenuesTable.COL_FULL_VENUE_ID,
				VenuesTable.COL_FULL_VENUE_OBJECT_JSON
		};
		return projection;
	}

	private static String getVenueOrderBy(long... venueIds) {
		StringBuilder orderBy = new StringBuilder("CASE ").append(VenuesTable.COL_FULL_VENUE_ID);

		for (int i = 0; i < venueIds.length ; i++) {
			orderBy.append("\n WHEN ")
			.append("'").append(venueIds[i]).append("'")
			.append(" THEN ")
			.append(i);
		}
		orderBy.append("\n ELSE ").append(venueIds.length).append(" END");

		return orderBy.toString();
	}

	public static DatabaseQuery getVenueDatabaseQuery(long... venueIds) {
		Uri contentUri = getVenueContentUri();
		String[] projection = getVenueProjection();

		// Match the venues by their IDs
		StringBuilder selection = new StringBuilder(VenuesTable.COL_FULL_VENUE_ID)
		.append(" IN (");

		for (int i = 0; i < venueIds.length; i++) {
			long venueId = venueIds[i];

			if (i > 0) {
				selection.append(", ");
			}
			selection.append("'").append(venueId).append("'");
		}
		selection.append(")");

		// Not using selection args
		String[] selectionArgs = null;

		// Order the rows in the order of the venues requested
		String orderBy = getVenueOrderBy(venueIds);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getVenueDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
					+ "\n projection = " + (projection == null ? "null" : projection)
					+ "\n selection = " + (selection == null ? "null" : selection.toString())
					+ "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
					+ "\n orderBy = " + (orderBy == null ? "null" : orderBy)
					);
		}
		return (new DatabaseQuery(contentUri.toString(), projection, selection.toString(), selectionArgs, orderBy));
	}


	/**
	 *  ************************ ALERTS ************************
	 */

	private static Uri getAlertsContentUri() {
		// Set the query on the alerts table
		return UniversalOrlandoContentUris.ALERTS;
	}

	private static Uri getAlertsWithPoiAndVenueContentUri() {
		// Set the query on the alerts table joining the POI table for each alert
		String sqlTableStatement = new StringBuilder(UniversalOrlandoContentUris.CUSTOM_TABLE.toString()).append("/")
				.append(AlertsTable.TABLE_NAME)
				.append("\nLEFT OUTER JOIN ").append(PointsOfInterestTable.TABLE_NAME)
				.append(" ON (").append(AlertsTable.COL_FULL_POI_ID)
				.append(" = ").append(PointsOfInterestTable.COL_FULL_POI_ID).append(")")
				.append("\nLEFT OUTER JOIN ").append(VenuesTable.TABLE_NAME)
				.append(" ON (").append(PointsOfInterestTable.COL_FULL_VENUE_ID)
				.append(" = ").append(VenuesTable.COL_FULL_VENUE_ID).append(")").toString();

		Uri contentUri = Uri.parse(sqlTableStatement);
		return contentUri;
	}

	private static String[] getAlertsForPoiProjection() {
		// Get columns needed by the alerts on POI detail UI
		String[] projection = {
				AlertsTable.COL_FULL_NOTIFY_MIN_BEFORE,
				AlertsTable.COL_FULL_NOTIFY_THRESHOLD_IN_MIN,
				AlertsTable.COL_FULL_SHOW_TIME,
				AlertsTable.COL_FULL_CREATED_DATE_IN_MILLIS,
				AlertsTable.COL_FULL_ALERT_TYPE_ID,
				AlertsTable.COL_FULL_ALERT_OBJECT_JSON
		};
		return projection;
	}

	private static String[] getAlertsWithPoiAndVenueProjection() {
		// Get columns needed by the alerts list UI
		String[] projection = {
				AlertsTable.COL_FULL_ID,
				AlertsTable.COL_FULL_ALERT_ID_STRING,
				AlertsTable.COL_FULL_ALERT_TYPE_ID,
				AlertsTable.COL_FULL_NOTIFY_MIN_BEFORE,
				AlertsTable.COL_FULL_NOTIFY_THRESHOLD_IN_MIN,
				AlertsTable.COL_FULL_SHOW_TIME,
				AlertsTable.COL_FULL_CREATED_DATE_IN_MILLIS,
				AlertsTable.COL_FULL_ALERT_OBJECT_JSON,
				PointsOfInterestTable.COL_FULL_POI_ID,
				PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
				PointsOfInterestTable.COL_FULL_DISPLAY_NAME,
				PointsOfInterestTable.COL_FULL_POI_OBJECT_JSON,
				PointsOfInterestTable.COL_FULL_WAIT_TIME,
				PointsOfInterestTable.COL_FULL_THUMBNAIL_IMAGE_URL,
				VenuesTable.COL_FULL_HOURS_LIST_JSON,
				VenuesTable.COL_FULL_VENUE_OBJECT_JSON
		};
		return projection;
	}

	public static DatabaseQuery getAlertsForPoiDatabaseQuery(Long poiId, int alertTypeId) {
		int[] alertTypeIdList = { alertTypeId };
		return getAlertsForPoiDatabaseQuery(poiId, alertTypeIdList);
	}

	public static DatabaseQuery getAlertsForPoiDatabaseQuery(Long poiId, int[] alertTypeIdList) {

		Uri contentUri = getAlertsContentUri();
		String[] projection = getAlertsForPoiProjection();

		// Match all alerts by the POI id
		StringBuilder selection = new StringBuilder(AlertsTable.COL_FULL_POI_ID)
		.append(" IN (").append(poiId).append(")")
		.append(" AND ")
		.append(AlertsTable.COL_FULL_ALERT_TYPE_ID)
		.append(" IN (");

		for (int i = 0; i < alertTypeIdList.length; i++) {
			if (i > 0) {
				selection.append(", ");
			}
			selection.append(alertTypeIdList[i]);
		}
		selection.append(")");


		// Not using selection args
		String[] selectionArgs = null;

		// Order the rows doesn't matter, because there should only be one
		String orderBy = "";

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getAlertsForPoiDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
					+ "\n projection = " + (projection == null ? "null" : projection)
					+ "\n selection = " + (selection == null ? "null" : selection.toString())
					+ "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
					+ "\n orderBy = " + (orderBy == null ? "null" : orderBy)
					);
		}
		return (new DatabaseQuery(contentUri.toString(), projection, selection.toString(), selectionArgs, orderBy));
	}

	public static DatabaseQuery getAlertDetailDatabaseQuery(String alertIdString) {

		Uri contentUri = getAlertsWithPoiAndVenueContentUri();
		String[] projection = getAlertsWithPoiAndVenueProjection();

		// Match the alert by its ID, and make sure it was created today
		StringBuilder selection = new StringBuilder(AlertsTable.COL_FULL_ALERT_ID_STRING)
		.append(" = '").append(alertIdString).append("'");

		// Not using selection args
		String[] selectionArgs = null;

		// Order the rows doesn't matter, because there should only be one
		String orderBy = "";

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getAlertDetailDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
					+ "\n projection = " + (projection == null ? "null" : projection)
					+ "\n selection = " + (selection == null ? "null" : selection.toString())
					+ "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
					+ "\n orderBy = " + (orderBy == null ? "null" : orderBy)
					);
		}
		return (new DatabaseQuery(contentUri.toString(), projection, selection.toString(), selectionArgs, orderBy));
	}

	public static DatabaseQuery getAlertsDatabaseQuery() {

		Uri contentUri = getAlertsWithPoiAndVenueContentUri();
		String[] projection = getAlertsWithPoiAndVenueProjection();

		// Match all alerts that were created today
		String selection = null;

		// Not using selection args
		String[] selectionArgs = null;

		// Order the rows by type, then name, then show time
		StringBuilder orderBy = new StringBuilder("CASE ").append(AlertsTable.COL_FULL_ALERT_TYPE_ID)
				.append("\n WHEN '").append(AlertsTable.VAL_ALERT_TYPE_ID_WAIT_TIME).append("' THEN 1")
				.append("\n WHEN '").append(AlertsTable.VAL_ALERT_TYPE_ID_RIDE_OPEN).append("' THEN 2")
				.append("\n WHEN '").append(AlertsTable.VAL_ALERT_TYPE_ID_SHOW_TIME).append("' THEN 3")
				.append("\n ELSE 999 END, ")
				.append("\n").append(AlertsTable.COL_FULL_POI_NAME).append(" COLLATE NOCASE ASC, ")
				.append("\n").append(AlertsTable.COL_FULL_SHOW_TIME).append(" ASC");

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getAlertsDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
					+ "\n projection = " + (projection == null ? "null" : projection)
					+ "\n selection = " + (selection == null ? "null" : selection.toString())
					+ "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
					+ "\n orderBy = " + (orderBy == null ? "null" : orderBy.toString())
					);
		}
		return (new DatabaseQuery(contentUri.toString(), projection, selection, selectionArgs, orderBy.toString()));
	}

    private static Uri getInteractiveExperienceUri() {
        return UniversalOrlandoContentUris.INTERACTIVE_EXPERIENCES;
    }

    private static String[] getInteractiveExperienceProjection() {
        String[] projection = {
                InteractiveExperiencesTable.COL_FULL_ID,
                InteractiveExperiencesTable.COL_FULL_INTERACTIVE_EXPERIENCE_ID,
                InteractiveExperiencesTable.COL_FULL_DISPLAY_NAME,
                InteractiveExperiencesTable.COL_FULL_INTERACTIVE_EXPERIENCE_OBJECT_JSON
        };
        return projection;
    }


	/**
	 *  ************************ NEWS ************************
	 */

	private static Uri getNewsContentUri() {
		return UniversalOrlandoContentUris.NEWS;
	}

	private static String[] getNewsProjection() {
		String[] projection = {
				NewsTable.COL_FULL_ID,
				NewsTable.COL_FULL_NEWS_ID,
				NewsTable.COL_FULL_MESSAGE_HEADING,
				NewsTable.COL_FULL_MESSAGE_BODY,
				NewsTable.COL_FULL_START_DATE_IN_MILLIS,
				NewsTable.COL_FULL_EXPIRATION_DATE_IN_MILLIS,
				NewsTable.COL_FULL_HAS_BEEN_READ,
				NewsTable.COL_FULL_NEWS_OBJECT_JSON
		};
		return projection;
	}

    public static DatabaseQuery getAllActiveNewsDatabaseQuery() {
        return getAllActiveNewsDatabaseQuery(null);
    }

	public static DatabaseQuery getAllActiveNewsDatabaseQuery(Integer rowLimit) {

		Uri contentUri = getNewsContentUri();
		String[] projection = getNewsProjection();

		// Only return news items between their start and expiration dates
        long currentTimeInMillis = System.currentTimeMillis();
        String selection = new StringBuilder()
                .append(currentTimeInMillis).append(" >= ").append(NewsTable.COL_FULL_START_DATE_IN_MILLIS)
                .append(" AND ")
                .append(currentTimeInMillis).append(" < ").append(NewsTable.COL_FULL_EXPIRATION_DATE_IN_MILLIS)
                .toString();

		// Not using selection args
		String[] selectionArgs = null;

		// Order the rows by start date
        StringBuilder orderByBuilder = new StringBuilder(NewsTable.COL_FULL_START_DATE_IN_MILLIS).append(" DESC");

        // If a limit is passed in, set it
        if (rowLimit != null && rowLimit >= 0) {
            orderByBuilder.append(" LIMIT ").append(rowLimit);
        }
        String orderBy = orderByBuilder.toString();

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getAllActiveNewsDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
					+ "\n projection = " + (projection == null ? "null" : projection)
					+ "\n selection = " + (selection == null ? "null" : selection)
					+ "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
					+ "\n orderBy = " + (orderBy == null ? "null" : orderBy)
					);
		}

		return (new DatabaseQuery(contentUri.toString(), projection, selection, selectionArgs, orderBy));
	}

	public static DatabaseQuery getAllActiveUnreadNewsDatabaseQuery() {

		Uri contentUri = getNewsContentUri();
		String[] projection = getNewsProjection();

		// Only return news items between their start and expiration dates, and have not been read
		long currentTimeInMillis = System.currentTimeMillis();
		String selection = new StringBuilder()
		.append(currentTimeInMillis).append(" >= ").append(NewsTable.COL_FULL_START_DATE_IN_MILLIS)
		.append(" AND ")
		.append(currentTimeInMillis).append(" < ").append(NewsTable.COL_FULL_EXPIRATION_DATE_IN_MILLIS)
		.append(" AND (")
		.append(NewsTable.COL_FULL_HAS_BEEN_READ).append(" IS NULL")
		.append(" OR ")
		.append(NewsTable.COL_FULL_HAS_BEEN_READ).append(" = 0)")
		.toString();

		// Not using selection args
		String[] selectionArgs = null;

		// Order the rows by start date
		String orderBy = new StringBuilder(NewsTable.COL_FULL_START_DATE_IN_MILLIS).append(" DESC").toString();

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getAllActiveUnreadNewsDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
					+ "\n projection = " + (projection == null ? "null" : projection)
					+ "\n selection = " + (selection == null ? "null" : selection)
					+ "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
					+ "\n orderBy = " + (orderBy == null ? "null" : orderBy)
					);
		}

		return (new DatabaseQuery(contentUri.toString(), projection, selection, selectionArgs, orderBy));
	}

	public static DatabaseQuery getActiveNewsDatabaseQuery(List<Long> newsIds) {

		Uri contentUri = getNewsContentUri();
		String[] projection = getNewsProjection();

		// Only return news items between their start and expiration dates, and have not been read
		long currentTimeInMillis = System.currentTimeMillis();
		StringBuilder selectionBuilder = new StringBuilder(NewsTable.COL_FULL_NEWS_ID)
		.append(" IN (");
		if (newsIds != null) {
			for (int i = 0; i < newsIds.size(); i++) {
				if (i > 0) {
					selectionBuilder.append(", ");
				}
				selectionBuilder.append(newsIds.get(i));
			}
		}
		selectionBuilder.append(")");

		selectionBuilder.append(" AND ").append(currentTimeInMillis).append(" >= ").append(NewsTable.COL_FULL_START_DATE_IN_MILLIS)
		.append(" AND ")
		.append(currentTimeInMillis).append(" < ").append(NewsTable.COL_FULL_EXPIRATION_DATE_IN_MILLIS)
		.toString();
		String selection = selectionBuilder.toString();

		// Not using selection args
		String[] selectionArgs = null;

		// Order the rows by start date
		String orderBy = new StringBuilder(NewsTable.COL_FULL_START_DATE_IN_MILLIS).append(" DESC").toString();

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getActiveNewsDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
					+ "\n projection = " + (projection == null ? "null" : projection)
					+ "\n selection = " + (selectionBuilder == null ? "null" : selectionBuilder)
					+ "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
					+ "\n orderBy = " + (orderBy == null ? "null" : orderBy)
					);
		}

		return (new DatabaseQuery(contentUri.toString(), projection, selection, selectionArgs, orderBy));
	}

	public static DatabaseQuery getNewsDatabaseQuery(long newsId) {

		Uri contentUri = getNewsContentUri();
		String[] projection = getNewsProjection();

		// Match the news item based on its id
		String selection = new StringBuilder(NewsTable.COL_FULL_NEWS_ID)
		.append(" = ").append(newsId)
		.toString();

		// Not using selection args
		String[] selectionArgs = null;

		// Order the rows by start time
		String orderBy = null;

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getNewsDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
					+ "\n projection = " + (projection == null ? "null" : projection)
					+ "\n selection = " + (selection == null ? "null" : selection)
					+ "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
					+ "\n orderBy = " + (orderBy == null ? "null" : orderBy)
					);
		}
		return (new DatabaseQuery(contentUri.toString(), projection, selection, selectionArgs, orderBy));
	}


    /**
     *  ************************ QUEUES ************************
     */

    private static Uri getQueuesContentUri() {
        return UniversalOrlandoContentUris.QUEUES;
    }

    private static String[] getQueuesProjection() {
        String[] projection = {
                QueuesTable.COL_FULL_ID,
                QueuesTable.COL_FULL_QUEUE_ID,
                QueuesTable.COL_FULL_QUEUE_ENTITY_ID,
                QueuesTable.COL_FULL_QUEUE_OBJECT_JSON,
        };
        return projection;
    }

    public static DatabaseQuery getQueuesByIdDatabaseQuery(Long poiId) {

        Uri contentUri = getQueuesContentUri();
        String[] projection = getQueuesProjection();

        // Match the POI by its ID
        StringBuilder selection = new StringBuilder(QueuesTable.COL_FULL_QUEUE_ENTITY_ID)
                .append(" IN (");
        if (poiId != null) {
                selection.append(poiId);
        }
        selection.append(")");

        StringBuilder orderBy = new StringBuilder(QueuesTable.COL_FULL_QUEUE_ENTITY_ID);

        return new DatabaseQuery(contentUri.toString(), projection, selection.toString(), null, orderBy.toString());
    }



    /**
     *  ************************ TICKETS ************************
     */

    private static Uri getAppointmentTicketsContentUri() {
        return UniversalOrlandoContentUris.TICKET_APPOINTMENTS;
    }

    private static String[] getAppointmentTicketsProjection() {
        String[] projection = {
                TicketsAppointmentTable.COL_FULL_ID,
                TicketsAppointmentTable.COL_FULL_TICKET_APPOINTMENT_ID,
                TicketsAppointmentTable.COL_FULL_APPOINTMENT_TIME_ID,
                TicketsAppointmentTable.COL_FULL_QUEUE_ENTITY_ID,
                TicketsAppointmentTable.COL_FULL_START_TIME,
                TicketsAppointmentTable.COL_FULL_END_TIME,
                TicketsAppointmentTable.COL_FULL_HAS_BEEN_READ,
                TicketsAppointmentTable.COL_FULL_APPOINTMENT_OBJECT_JSON,
                TicketsAppointmentTable.COL_FULL_APPOINTMENT_TICKET_OBJECT_JSON
        };
        return projection;
    }

    public static DatabaseQuery getAppointmentTicketsByIdDatabaseQuery(Long poiId) {

        Uri contentUri = getAppointmentTicketsContentUri();
        String[] projection = getAppointmentTicketsProjection();


        // Match the POI by its ID
        StringBuilder selection = new StringBuilder(TicketsAppointmentTable.COL_FULL_QUEUE_ENTITY_ID)
                .append(" IN (");
        if (poiId != null) {
            selection.append(poiId);
        }
        selection.append(")");

        StringBuilder orderBy = new StringBuilder(TicketsAppointmentTable.COL_FULL_QUEUE_ENTITY_ID);

        return new DatabaseQuery(contentUri.toString(), projection, selection.toString(), null, orderBy.toString());
    }

    public static DatabaseQuery getUnReadAppointmentTicketsDatabaseQuery() {

        Uri contentUri = getAppointmentTicketsContentUri();
        String[] projection = getAppointmentTicketsProjection();

        String orderBy = new StringBuilder(TicketsAppointmentTable.COL_FULL_HAS_BEEN_READ).toString();

        String selection = new StringBuilder()
                .append(TicketsAppointmentTable.COL_FULL_HAS_BEEN_READ).append(" IS NULL")
                .append(" OR ")
                .append(TicketsAppointmentTable.COL_FULL_HAS_BEEN_READ).append(" = 0")
                .toString();

        return (new DatabaseQuery(contentUri.toString(), projection, selection, null, orderBy));
    }

    public static DatabaseQuery getAllAppointmentTicketsDatabaseQuery() {

        Uri contentUri = getAppointmentTicketsContentUri();
        String[] projection = getAppointmentTicketsProjection();

        String orderBy = new StringBuilder(TicketsAppointmentTable.COL_FULL_TICKET_APPOINTMENT_ID).toString();

        return (new DatabaseQuery(contentUri.toString(), projection, null, null, orderBy));
    }

    /**
     *  ****************************************************************
     */

	/**
	 *  ************************ VENUE HOURS ************************
	 */

	private static Uri getVenueHoursContentUri() {
		// Set the query on the alerts table
		return UniversalOrlandoContentUris.VENUE_HOURS;
	}

	private static String[] getVenueHoursProjection() {
		// Get columns needed by the home UI
		String[] projection = {
				VenueHoursTable.COL_FULL_ID,
				VenueHoursTable.COL_FULL_DATE_IN_MILLIS,
				VenueHoursTable.COL_FULL_OPEN_DATE_IN_MILLIS,
				VenueHoursTable.COL_FULL_CLOSE_DATE_IN_MILLIS,
				VenueHoursTable.COL_FULL_VENUE_ID
		};
		return projection;
	}

	public static DatabaseQuery getVenueHoursDatabaseQuery(long... venueIds) {
		Uri contentUri = getVenueHoursContentUri();
		String[] projection = getVenueHoursProjection();

		// Only get venue hours for specific venues
		StringBuilder selection = new StringBuilder(VenueHoursTable.COL_FULL_VENUE_ID)
		.append(" IN (");

		for (int i = 0; i < venueIds.length; i++) {
			long venueId = venueIds[i];

			if (i > 0) {
				selection.append(", ");
			}
			selection.append("'").append(venueId).append("'");
		}
		selection.append(")");

		// Not using selection args
		String[] selectionArgs = null;

		// Order the rows in the order of the venues requested
		String orderBy =  new StringBuilder(VenueHoursTable.COL_FULL_DATE_IN_MILLIS).append(" ASC").toString();

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getVenueHoursDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
					+ "\n projection = " + (projection == null ? "null" : projection)
					+ "\n selection = " + (selection == null ? "null" : selection.toString())
					+ "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
					+ "\n orderBy = " + (orderBy == null ? "null" : orderBy)
					);
		}
		return (new DatabaseQuery(contentUri.toString(), projection, selection.toString(), selectionArgs, orderBy));
	}
	
	/**
     *  ************************ Advanced Filter ************************
     */
	
    /**
     * Return query for distinct venues that contain pois of the passed in type<br/>
     * Projection: {venue_id,display_name}
     * @param poiTypes
     * @return
     */
    public static DatabaseQuery getVenuesForTypeDatabaseQuery(List<String> poiTypes) {
        StringBuilder contentUri = new StringBuilder(UniversalOrlandoContentUris.CUSTOM_TABLE.toString())
                .append("/").append(PointsOfInterestTable.TABLE_NAME)
                .append("\nJOIN ").append(VenuesTable.TABLE_NAME).append(" ON ")
                .append(PointsOfInterestTable.COL_FULL_VENUE_ID).append(" = ")
                .append(VenuesTable.COL_FULL_VENUE_ID);
        
        StringBuilder selection = new StringBuilder(PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
        selection.append(" IN (");
        if (poiTypes != null) {
            for (int i = 0; i < poiTypes.size(); i++) {
                if (i > 0) {
                    selection.append(", ");
                }
                selection.append("'").append(poiTypes.get(i)).append("'");
            }
        }
        selection.append(")");

        String[] projection = { "DISTINCT " + VenuesTable.COL_FULL_VENUE_ID,
                VenuesTable.COL_FULL_DISPLAY_NAME, VenuesTable.COL_FULL_ID };

        String[] selectionArgs = null;
        String orderBy = VenuesTable.COL_FULL_DISPLAY_NAME + " COLLATE NOCASE ASC";

        return (new DatabaseQuery(contentUri.toString(), projection, selection.toString(), selectionArgs,
                orderBy));
    }

    /**
     *  ************************ Events ************************
     */

    private static String[] getEventsProjection() {
        // Get columns needed by Events UI
        String[] projection = {
                PointsOfInterestTable.COL_FULL_ID,
                PointsOfInterestTable.COL_FULL_POI_ID,
                PointsOfInterestTable.COL_FULL_DISPLAY_NAME + " AS " + PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME,
                PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
                PointsOfInterestTable.COL_FULL_VENUE_ID,
                PointsOfInterestTable.COL_FULL_LIST_IMAGE_URL,
                PointsOfInterestTable.COL_FULL_IS_FAVORITE,
                PointsOfInterestTable.COL_FULL_POI_OBJECT_JSON,
                VenuesTable.COL_FULL_DISPLAY_NAME + " AS " + VenuesTable.COL_ALIAS_DISPLAY_NAME,
                VenuesTable.COL_FULL_VENUE_OBJECT_JSON,
                EventTimesTable.COL_FULL_START_DATE,
                EventTimesTable.COL_FULL_END_DATE,
                VenueLandsTable.COL_FULL_DISPLAY_NAME + " AS " + VenueLandsTable.COL_ALIAS_DISPLAY_NAME,
                VenueLandsTable.COL_FULL_VENUE_LAND_OBJECT_JSON
        };
        return projection;
    }

    private static Uri getEventsContentUri() {
        // Set the query on the POI table joining the venue table
        String sqlTableStatement = new StringBuilder(UniversalOrlandoContentUris.CUSTOM_TABLE.toString()).append("/")
                .append(PointsOfInterestTable.TABLE_NAME)
                .append("\nLEFT JOIN (SELECT min(")
                .append(EventTimesTable.COL_START_DATE).append(") AS ")
                .append(EventTimesTable.COL_START_DATE)
                .append(", max(").append(EventTimesTable.COL_END_DATE)
                .append(") AS ").append(EventTimesTable.COL_END_DATE)
                .append(", ").append(EventTimesTable.COL_POI_ID)
                .append("\nFROM ").append(EventTimesTable.TABLE_NAME)
                .append("\nGROUP BY ").append(EventTimesTable.COL_POI_ID)
                .append(") AS ").append(EventTimesTable.TABLE_NAME)
                .append(" ON ").append(PointsOfInterestTable.COL_FULL_POI_ID)
                .append(" = ").append(EventTimesTable.COL_FULL_POI_ID)
                .append("\nLEFT OUTER JOIN ").append(VenuesTable.TABLE_NAME)
                .append(" ON (").append(PointsOfInterestTable.COL_FULL_VENUE_ID)
                .append(" = ").append(VenuesTable.COL_FULL_VENUE_ID).append(")")
                .append("\nLEFT OUTER JOIN ").append(VenueLandsTable.TABLE_NAME)
                .append(" ON (").append(PointsOfInterestTable.COL_FULL_VENUE_LAND_ID)
                .append(" = ").append(VenueLandsTable.COL_FULL_VENUE_LAND_ID).append(")")
                .toString();

        Uri contentUri = Uri.parse(sqlTableStatement);
        return contentUri;
    }

    public static DatabaseQuery getEventsDatabaseQuery() {
        Uri contentUri = getEventsContentUri();
        String[] projection = getEventsProjection();
        StringBuilder selection = new StringBuilder(PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
        selection.append(" IN ('")
        .append(PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT)
        .append("')");

        StringBuilder orderBy = new StringBuilder(EventTimesTable.COL_START_DATE);
        orderBy.append(" IS NULL ASC");

        return new DatabaseQuery(contentUri.toString(), projection, selection.toString(), null, orderBy.toString());
    }

    /**
     *  ************************ Event Series ************************
     */

    private static String[] getEventSeriesProjection() {
        // Get columns needed by Events UI
        String[] projection = {
                EventSeriesTable.COL_FULL_ID,
                EventSeriesTable.COL_FULL_EVENT_SERIES_ID,
                EventSeriesTable.COL_FULL_DISPLAY_NAME + " AS " + EventSeriesTable.COL_ALIAS_DISPLAY_NAME,
                EventSeriesTable.COL_FULL_VENUE_ID,
                EventSeriesTable.COL_FULL_LIST_IMAGE_URL,
                EventSeriesTable.COL_FULL_EVENT_SERIES_OBJECT_JSON,
                VenuesTable.COL_FULL_DISPLAY_NAME + " AS " + VenuesTable.COL_ALIAS_DISPLAY_NAME,
                VenuesTable.COL_FULL_VENUE_OBJECT_JSON,
                EventSeriesTimesTable.COL_FULL_START_DATE,
                EventSeriesTimesTable.COL_FULL_END_DATE
        };
        return projection;
    }
    
    private static Uri getEventSeriesContentUri() {
        // Set the query on the POI table joining the venue table
        String sqlTableStatement = new StringBuilder(UniversalOrlandoContentUris.CUSTOM_TABLE.toString()).append("/")
                .append(EventSeriesTable.TABLE_NAME)
                .append("\nLEFT JOIN (SELECT min(")
                .append(EventSeriesTimesTable.COL_START_DATE).append(") AS ")
                .append(EventSeriesTimesTable.COL_START_DATE)
                .append(", max(").append(EventSeriesTimesTable.COL_END_DATE)
                .append(") AS ").append(EventSeriesTimesTable.COL_END_DATE)
                .append(", ").append(EventSeriesTimesTable.COL_EVENT_SERIES_ID)
                .append("\nFROM ").append(EventSeriesTimesTable.TABLE_NAME)
                .append("\nGROUP BY ").append(EventSeriesTimesTable.COL_EVENT_SERIES_ID)
                .append(") AS ").append(EventSeriesTimesTable.TABLE_NAME)
                .append(" ON ").append(EventSeriesTable.COL_FULL_EVENT_SERIES_ID)
                .append(" = ").append(EventSeriesTimesTable.COL_FULL_EVENT_SERIES_ID)
                .append("\nLEFT OUTER JOIN ").append(VenuesTable.TABLE_NAME)
                .append(" ON (").append(EventSeriesTable.COL_FULL_VENUE_ID)
                .append(" = ").append(VenuesTable.COL_FULL_VENUE_ID).append(")").toString();

        Uri contentUri = Uri.parse(sqlTableStatement);
        return contentUri;
    }
    
    public static DatabaseQuery getEventSeriesDatabaseQuery() {
        Uri contentUri = getEventSeriesContentUri();
        String[] projection = getEventSeriesProjection();
        
        StringBuilder orderBy = new StringBuilder(EventSeriesTable.COL_SORT_ORDER).append(" ASC");
        
        return new DatabaseQuery(contentUri.toString(), projection, null, null, orderBy.toString());
    }
    
    public static DatabaseQuery getEventSeriesDetailDatabaseQuery(long eventSeriesId) {
        Uri contentUri = getEventSeriesContentUri();
        String[] projection = getEventSeriesProjection();
        
        StringBuilder selection = new StringBuilder(EventSeriesTable.COL_FULL_EVENT_SERIES_ID)
        .append(" = '").append(eventSeriesId).append("'");
        
        return new DatabaseQuery(contentUri.toString(), projection, selection.toString(), null, null);
    }
    
    public static DatabaseQuery getEventSeriesByIdDatabaseQuery(List<Long> eventSeriesIds) {
        Uri contentUri = getEventSeriesContentUri();
        String[] projection = getEventSeriesProjection();
        
        StringBuilder selection = new StringBuilder(EventSeriesTable.COL_FULL_EVENT_SERIES_ID)
        .append(" IN (");
        if (eventSeriesIds != null) {
            for (int i = 0; i < eventSeriesIds.size(); i++) {
                if (i > 0) {
                    selection.append(", ");
                }
                selection.append("'").append(eventSeriesIds.get(i)).append("'");
            }
        }
        selection.append(")");
        
        return new DatabaseQuery(contentUri.toString(), projection, selection.toString(), null, null);
    }
    
    private static String[] getExploreEventProjection(FilterSort filterSort, boolean showRecurringEventsOnce) {
     // Get columns needed by the explore UI
        String sortValue = filterSort == null ? "" : String.valueOf(filterSort.ordinal());

        if (showRecurringEventsOnce) {
            String[] projection = {
                    PointsOfInterestTable.COL_FULL_ID,
                    PointsOfInterestTable.COL_FULL_POI_ID,
                    PointsOfInterestTable.COL_FULL_DISPLAY_NAME + " AS " + PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME,
                    PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
                    PointsOfInterestTable.COL_FULL_WAIT_TIME,
                    PointsOfInterestTable.COL_FULL_SHOW_TIMES_JSON,
                    PointsOfInterestTable.COL_FULL_VENUE_ID,
                    PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
                    PointsOfInterestTable.COL_FULL_LIST_IMAGE_URL,
                    PointsOfInterestTable.COL_FULL_LATITUDE,
                    PointsOfInterestTable.COL_FULL_LONGITUDE,
                    PointsOfInterestTable.COL_FULL_IS_ROUTABLE,
                    PointsOfInterestTable.COL_FULL_IS_FAVORITE,
                    PointsOfInterestTable.COL_FULL_POI_HASH_CODE,
                    PointsOfInterestTable.COL_FULL_POI_OBJECT_JSON,
                    VenuesTable.COL_FULL_DISPLAY_NAME + " AS " + VenuesTable.COL_ALIAS_DISPLAY_NAME,
                    VenuesTable.COL_FULL_HOURS_LIST_JSON,
                    VenuesTable.COL_FULL_VENUE_OBJECT_JSON,
                    "MIN(" + EventTimesTable.COL_FULL_POI_ID + ") AS " + EventTimesTable.COL_ALIAS_POI_ID,
                    EventTimesTable.COL_FULL_START_DATE,
                    EventTimesTable.COL_FULL_END_DATE,
                    VenueLandsTable.COL_FULL_DISPLAY_NAME + " AS " + VenueLandsTable.COL_ALIAS_DISPLAY_NAME,
                    VenueLandsTable.COL_FULL_VENUE_LAND_OBJECT_JSON,
                    "'" + sortValue + "' AS " + UniversalOrlandoDatabaseTables.COL_FILTER_SORT
            };
            return projection;
        } else {
            String[] projection = {
                    PointsOfInterestTable.COL_FULL_ID,
                    PointsOfInterestTable.COL_FULL_POI_ID,
                    PointsOfInterestTable.COL_FULL_DISPLAY_NAME + " AS " + PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME,
                    PointsOfInterestTable.COL_FULL_POI_TYPE_ID,
                    PointsOfInterestTable.COL_FULL_WAIT_TIME,
                    PointsOfInterestTable.COL_FULL_SHOW_TIMES_JSON,
                    PointsOfInterestTable.COL_FULL_VENUE_ID,
                    PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS,
                    PointsOfInterestTable.COL_FULL_LIST_IMAGE_URL,
                    PointsOfInterestTable.COL_FULL_LATITUDE,
                    PointsOfInterestTable.COL_FULL_LONGITUDE,
                    PointsOfInterestTable.COL_FULL_IS_ROUTABLE,
                    PointsOfInterestTable.COL_FULL_IS_FAVORITE,
                    PointsOfInterestTable.COL_FULL_POI_HASH_CODE,
                    PointsOfInterestTable.COL_FULL_POI_OBJECT_JSON,
                    VenuesTable.COL_FULL_DISPLAY_NAME + " AS " + VenuesTable.COL_ALIAS_DISPLAY_NAME,
                    VenuesTable.COL_FULL_HOURS_LIST_JSON,
                    VenuesTable.COL_FULL_VENUE_OBJECT_JSON,
                    EventTimesTable.COL_FULL_START_DATE,
                    EventTimesTable.COL_FULL_END_DATE,
                    VenueLandsTable.COL_FULL_DISPLAY_NAME + " AS " + VenueLandsTable.COL_ALIAS_DISPLAY_NAME,
                    VenueLandsTable.COL_FULL_VENUE_LAND_OBJECT_JSON,
                    "'" + sortValue + "' AS " + UniversalOrlandoDatabaseTables.COL_FILTER_SORT
            };
            return projection;
        }
    }
    
    public static Uri getExploreEventContentUri() {
        // Set the query on the POI table joining the venue table
        String sqlTableStatement = new StringBuilder(UniversalOrlandoContentUris.CUSTOM_TABLE.toString()).append("/")
                .append(PointsOfInterestTable.TABLE_NAME)
                .append("\nLEFT JOIN ").append(EventTimesTable.TABLE_NAME)
                .append(" ON ").append(PointsOfInterestTable.COL_FULL_POI_ID)
                .append(" = ").append(EventTimesTable.COL_FULL_POI_ID)
                .append("\nLEFT JOIN ").append(VenuesTable.TABLE_NAME)
                .append(" ON ").append(VenuesTable.COL_FULL_VENUE_ID)
                .append(" = ").append(PointsOfInterestTable.COL_FULL_VENUE_ID)
                .append("\nLEFT JOIN ").append(VenueLandsTable.TABLE_NAME)
                .append(" ON ").append(PointsOfInterestTable.COL_FULL_VENUE_LAND_ID)
                .append(" = ").append(VenueLandsTable.COL_FULL_VENUE_LAND_ID)
                .toString();

        Uri contentUri = Uri.parse(sqlTableStatement);
        return contentUri;
    }
    
    public static DatabaseQuery getTimelineEventsById(List<Long> poiIdList, FilterOptions filterOptions) {
        Uri contentUri = getExploreEventContentUri();
        String[] projection = getExploreEventProjection(filterOptions.getFilterSort(), false);

        // Match the POI by its ID
        StringBuilder selection = new StringBuilder(PointsOfInterestTable.COL_FULL_POI_ID)
        .append(" IN (");
        if (poiIdList != null) {
            for (int i = 0; i < poiIdList.size(); i++) {
                if (i > 0) {
                    selection.append(", ");
                }
                selection.append(poiIdList.get(i));
            }
        }
        selection.append(")");
        
        StringBuilder orderBy = getEventOrderByStringBuilder();
        
        return new DatabaseQuery(contentUri.toString(), projection, selection.toString(), null, orderBy.toString());
    }
    
    public static DatabaseQuery getPointsOfInterestById(List<Long> poiIdList, FilterOptions filterOptions) {
        Uri contentUri = getExploreContentUri();
        String[] projection = getExploreProjection(null, filterOptions.getFilterSort());
        
        // Match the POI by its ID
        StringBuilder selection = new StringBuilder(PointsOfInterestTable.COL_FULL_POI_ID)
        .append(" IN (");
        if (poiIdList != null) {
            for (int i = 0; i < poiIdList.size(); i++) {
                if (i > 0) {
                    selection.append(", ");
                }
                selection.append(poiIdList.get(i));
            }
        }
        selection.append(")");
        
        StringBuilder orderBy = new StringBuilder(PointsOfInterestTable.COL_FULL_POI_TYPE_ID)
        .append(", ").append(PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME).append(" COLLATE NOCASE ASC");
        
        return new DatabaseQuery(contentUri.toString(), projection, selection.toString(), null, orderBy.toString());
    }

    public static DatabaseQuery getPointsOfInterestByFilterOptons(FilterOptions filterOptions) {
        Uri contentUri = getExploreContentUri();
        String[] projection = getExploreProjection(null, filterOptions.getFilterSort());

        // Match the POI by its ID
        StringBuilder selection = new StringBuilder(PointsOfInterestTable.COL_FULL_POI_ID)
                .append(" IN (");
        int index = -1;
        for (FilterTabOption option : filterOptions.getFilterTabOptions()) {

            if (null == option.getFilterTabButton()) {
                continue;
            }

            if (option.isChecked()) {
                index++;
                if(index > 0){
                    selection.append(", ");
                }

                for (int  i = 0; i < option.getColumnValues().length; i++) {
                    if (i > 0) {
                        selection.append(", ");
                    }
                    String poiId = option.getColumnValues()[i];
                    selection.append(poiId);
                }
            }
        }
        selection.append(")");

        StringBuilder orderBy = new StringBuilder(PointsOfInterestTable.COL_FULL_POI_TYPE_ID)
                .append(", ").append(PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME).append(" COLLATE NOCASE ASC");

        return new DatabaseQuery(contentUri.toString(), projection, selection.toString(), null, orderBy.toString());
    }
    
    public static DatabaseQuery getUpcomingTimelineEvents(FilterOptions filterOptions) {
        Uri contentUri = getExploreEventContentUri();
        String[] projection = getExploreEventProjection(filterOptions.getFilterSort(), false);
        
        // Match the POI by its ID
        Calendar calendar = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
        // Reset calendar to 12:00am
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long timeTodayUnix = calendar.getTimeInMillis() / 1000;
        // Add three days to the date
        calendar.add(Calendar.DAY_OF_YEAR, 3);
        long dateUnix = calendar.getTimeInMillis() / 1000;
        
        StringBuilder selection = new StringBuilder(EventTimesTable.COL_FULL_START_DATE)
        .append(" < '").append(dateUnix).append("'");
        // Filter out past events
        selection.append(" AND ").append(EventTimesTable.COL_FULL_START_DATE)
        .append(" >= ").append("'").append(timeTodayUnix).append("'").append(" AND (").append(EventTimesTable.COL_FLAGS).append( " & ")
                .append(Event.FLAG_SHOW_IN_TODAY_VIEW).append(" = ").append(Event.FLAG_SHOW_IN_TODAY_VIEW).append(")");
        
        StringBuilder orderBy = getEventOrderByStringBuilder();
        
        return new DatabaseQuery(contentUri.toString(), projection, selection.toString(), null, orderBy.toString());
    }

    public static DatabaseQuery getAllUpcomingTimelineEvents(FilterOptions filterOptions) {
        return getAllUpcomingTimelineEvents(filterOptions, false, null);
    }

    public static DatabaseQuery getAllUpcomingTimelineEvents(FilterOptions filterOptions, boolean showRecurringEventsOnce, Integer rowLimit) {
        Uri contentUri = getExploreEventContentUri();
        String[] projection = getExploreEventProjection(filterOptions.getFilterSort(), showRecurringEventsOnce);

        // Get time at 12:00am current day park time
        Calendar calendar = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long timeTodayUnix = calendar.getTimeInMillis() / 1000;

        StringBuilder selection = new StringBuilder(EventTimesTable.COL_FULL_START_DATE)
                .append(" >= ").append("'").append(timeTodayUnix).append("'")
                .append(" AND ").append(EventTimesTable.COL_FLAGS).append(" & ")
                .append(Event.FLAG_SHOW_IN_TODAY_VIEW).append(" = ").append(Event.FLAG_SHOW_IN_TODAY_VIEW)
                .append(" AND ").append(EventTimesTable.COL_START_DATE).append(" != ").append(LARGEST_INTEGER_VALUE);

        if (showRecurringEventsOnce) {
            selection.append(") GROUP BY (").append(EventTimesTable.COL_FULL_POI_ID);
        }
        
        StringBuilder orderByBuilder = getEventOrderByStringBuilder();
        // If a limit is passed in, set it
        if (rowLimit != null && rowLimit >= 0) {
            orderByBuilder.append(" LIMIT ").append(rowLimit);
        }
        String orderBy = orderByBuilder.toString();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getAllUpcomingTimelineEvents: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
                       + "\n projection = " + (projection == null ? "null" : projection)
                       + "\n selection = " + (selection == null ? "null" : selection)
                       + "\n orderBy = " + (orderBy == null ? "null" : orderBy)
            );
        }

        return new DatabaseQuery(contentUri.toString(), projection, selection.toString(), null, orderBy);
    }
    
    /**
     *  ************************ Offers ************************
     */
    
    public static DatabaseQuery getOfferCount(String vendor) {
        Uri contentUri = UniversalOrlandoContentUris.OFFERS;
        String[] projection = { "COUNT(*) AS " + BaseColumns._COUNT };
        String selection = new StringBuilder(OffersTable.COL_FULL_VENDOR).append(" LIKE '")
                .append(vendor).append("'").toString();

        return new DatabaseQuery(contentUri.toString(), projection, selection, null, null);
    }
    
    private static Uri getOffersContentUri() {
        String sqlTableStatement = new StringBuilder(UniversalOrlandoContentUris.CUSTOM_TABLE.toString()).append("/")
                .append(OffersTable.TABLE_NAME)
                .append("\nLEFT JOIN ").append(OfferSeriesTable.TABLE_NAME).append(" ON ")
                .append(OfferSeriesTable.COL_FULL_VENDOR).append(" = ")
                .append(OffersTable.COL_FULL_VENDOR)
                .append("\nLEFT JOIN ").append(VenuesTable.TABLE_NAME).append(" ON ")
                .append(VenuesTable.COL_FULL_VENUE_ID).append(" = ")
                .append(OffersTable.COL_FULL_VENUE_ID).toString();
        
        Uri uri = Uri.parse(sqlTableStatement);
        return uri;
    }
    
    private static String[] getOffersProjection() {
        return new String[] { 
                OffersTable.COL_FULL_ID, 
                OffersTable.COL_FULL_OFFER_ID,
                OffersTable.COL_FULL_SHORT_DESCRIPTION + " AS " + OffersTable.COL_ALIAS_SHORT_DESCRIPTION,
                OffersTable.COL_FULL_THUMBNAIL_IMAGE_URL + " AS " + OffersTable.COL_ALIAS_THUMBNAIL_IMAGE_URL,
                OffersTable.COL_FULL_DISPLAY_NAME + " AS " + OffersTable.COL_ALIAS_DISPLAY_NAME,
                OffersTable.COL_FULL_DISPLAY_NAME + " AS " + PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME,
                OfferSeriesTable.COL_FULL_DISPLAY_NAME + " AS " + OfferSeriesTable.COL_ALIAS_DISPLAY_NAME,
                OffersTable.COL_FULL_LATITUDE,
                OffersTable.COL_FULL_LONGITUDE,
                VenuesTable.COL_FULL_DISPLAY_NAME + " AS " + VenuesTable.COL_ALIAS_DISPLAY_NAME,
                VenuesTable.COL_FULL_VENUE_OBJECT_JSON,
                OffersTable.COL_FULL_OFFER_OBJECT_JSON
        };
    }
    
    public static DatabaseQuery getOffersById(List<Long> offerIds) {
        Uri contentUri = getOffersContentUri();
        String projection[] = getOffersProjection();
        StringBuilder selection = new StringBuilder(OffersTable.COL_FULL_OFFER_ID)
        .append(" IN (");
        
        for (int i = 0; i < offerIds.size(); i++) {
            Long offerId = offerIds.get(i);
            if (i > 0) {
                selection.append(", ");
            }
            selection.append("'").append(offerId).append("'");
        }
        selection.append(")");
        
        String orderBy = OffersTable.COL_FULL_DISPLAY_NAME;

        return new DatabaseQuery(contentUri.toString(), projection, selection.toString(), null, orderBy);
    }
    
    public static DatabaseQuery getOfferSeries(String vendor) {
        Uri contentUri = UniversalOrlandoContentUris.OFFER_SERIES;
        String[] projection = { OfferSeriesTable.COL_FULL_ID, OfferSeriesTable.COL_FULL_OFFER_SERIES_ID,
                OfferSeriesTable.COL_FULL_DISPLAY_NAME + " AS " + OfferSeriesTable.COL_ALIAS_DISPLAY_NAME,
                "''" + " AS " + PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME,
                OfferSeriesTable.COL_LATITUDE, OfferSeriesTable.COL_LONGITUDE,
                OfferSeriesTable.COL_FULL_OFFER_SERIES_OBJECT_JSON };
        String selection = new StringBuilder(OfferSeriesTable.COL_FULL_VENDOR).append(" LIKE '")
                .append(vendor).append("'").toString();

        return new DatabaseQuery(contentUri.toString(), projection, selection.toString(), null, null);
    }

    public static DatabaseQuery getPhotoFrameExperienceDatabaseQuery(List<Long> experienceIds) {

        Uri contentUri = UniversalOrlandoContentUris.PHOTOFRAME_EXPERIENCE;
        String[] projection = {
                UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.COL_ID,
                UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.COL_EXPERIENCE_ID,
                UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.COL_PHOTOFRAME_EXPERIENCE_JSON
        };

        StringBuilder selectionBuilder = new StringBuilder(UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.COL_EXPERIENCE_ID)
                .append(" IN (");
        if (experienceIds != null) {
            for (int i = 0; i < experienceIds.size(); i++) {
                if (i > 0) {
                    selectionBuilder.append(", ");
                }
                selectionBuilder.append(experienceIds.get(i));
            }
        }
        selectionBuilder.append(")");

        String selection = selectionBuilder.toString();

        // Not using selection args
        String[] selectionArgs = null;

        // Order the rows by start date
        String orderBy = new StringBuilder(UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.COL_ID).append(" DESC").toString();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getInteractiveExperiencesDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
                    + "\n projection = " + (projection == null ? "null" : projection)
                    + "\n selection = " + (selectionBuilder == null ? "null" : selectionBuilder)
                    + "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
                    + "\n orderBy = " + (orderBy == null ? "null" : orderBy)
            );
        }

        return (new DatabaseQuery(contentUri.toString(), projection, selection, selectionArgs, orderBy));
    }


    public static DatabaseQuery getInteractiveExperiencesDatabaseQuery(List<Long> experienceIds) {

        Uri contentUri = getInteractiveExperienceUri();
        String[] projection = getInteractiveExperienceProjection();

        StringBuilder selectionBuilder = new StringBuilder(InteractiveExperiencesTable.COL_FULL_INTERACTIVE_EXPERIENCE_ID)
                .append(" IN (");
        if (experienceIds != null) {
            for (int i = 0; i < experienceIds.size(); i++) {
                if (i > 0) {
                    selectionBuilder.append(", ");
                }
                selectionBuilder.append(experienceIds.get(i));
            }
        }
        selectionBuilder.append(")");

        String selection = selectionBuilder.toString();

        // Not using selection args
        String[] selectionArgs = null;

        // Order the rows by start date
        String orderBy = new StringBuilder(InteractiveExperiencesTable.COL_FULL_ID).append(" DESC").toString();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getInteractiveExperiencesDatabaseQuery: " + " contentUri = " + (contentUri == null ? "null" : contentUri.toString())
                            + "\n projection = " + (projection == null ? "null" : projection)
                            + "\n selection = " + (selectionBuilder == null ? "null" : selectionBuilder)
                            + "\n selectionArgs = " + (selectionArgs == null ? "null" : selectionArgs)
                            + "\n orderBy = " + (orderBy == null ? "null" : orderBy)
            );
        }

        return (new DatabaseQuery(contentUri.toString(), projection, selection, selectionArgs, orderBy));
    }


    private static StringBuilder getEventOrderByStringBuilder(){
       return new StringBuilder("")
               .append(EventTimesTable.COL_START_DATE).append(", ").append(PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME)
                .append(" COLLATE NOCASE ASC");
    }

    public static DatabaseQuery getMobilePageDatabaseQuery(String pageId) {
        return new DatabaseQuery(UniversalOrlandoContentUris.MOBILE_PAGES.toString(),
                null, UniversalOrlandoDatabaseTables.MobilePagesTable.COL_IDENTIFIER + " = ?",
                new String[]{pageId}, "");
    }
}
