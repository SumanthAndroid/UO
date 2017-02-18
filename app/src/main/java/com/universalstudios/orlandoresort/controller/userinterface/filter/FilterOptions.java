/**
 *
 */
package com.universalstudios.orlandoresort.controller.userinterface.filter;

import android.location.Location;

import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.explore.FilterTabOption;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;

import java.util.ArrayList;
import java.util.List;

import static com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType.RIDES;
import static com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType.SHOWS;

/**
 * @author acampbell
 *
 */
public class FilterOptions extends GsonObject {

    public enum FilterSort {
        TYPE, ALPHABETICAL, DISTANCE, WAIT_TIMES, SHOW_TIMES, EVENT_DATE
    }

    private List<FilterTabOption> filterTabOptions;
    private FilterSort filterSort;
    private ExploreType exploreType;
    private Location userLocation;
    private boolean upperLot;
    private boolean lowerLot;
    private boolean expressPassLine;
    private boolean kidFriendly;
    private List<String> accessibilityOptions;
    private List<String> rideFilterVenues;
    private int rideFilterMaxWaitTime;
    private int riderHeight;
    private boolean singleRiderLine;
    private boolean childSwap;
    private List<String> rideSubTypes;
    private List<String> showFilterVenues;
    private List<String> showSubTypes;
    private int nextShowTime;
    private List<String> diningFilterVenues;
    private List<String> diningSubTypes;
    private int diningMinPrice;
    private List<String> diningPlans;
    private boolean characterDining;
    private boolean vegetarianHealthy;
    private List<String> shoppingFilterVenues;
    private List<String> shoppingSubTypes;
    private List<String> entertainmentSubTypes;
    private List<String> rentalServiceFilterVenues;
    private boolean expressPassSold;
    private int entertainmentMinPrice;
    private boolean parentalSupervisionRequired;
    private boolean lifeJacketRequired;
    private boolean amexShopping;
    private boolean amexDining;
    private boolean shopPackagePickup;

    //Rental Services Types
    private boolean rentalTypeEcv;
    private boolean rentalTypeStroller;
    private boolean rentalTypeWheelChair;

    public boolean hasCocaCola;


    private static List<String> ACCESSIBILITY_FLAGS;
    private static List<String> RIDE_SUB_TYPE_FLAGS;
    private static List<String> SHOW_SUB_TYPE_FLAGS;
    private static List<String> DINING_SUB_TYPE_FLAGS;
    private static List<String> DINING_PLAN_FLAGS;
    private static List<String> SHOPPING_SUB_TYPE_FLAGS;
    private static List<String> ENTERTAINMENT_SUB_TYPE_FLAGS;

    public static List<String> getAccessibilityFlags() {
        if (ACCESSIBILITY_FLAGS == null) {
            ACCESSIBILITY_FLAGS = new ArrayList<String>();
            // Order and contents matches string-array
            // R.array.advanced_filter_accessibility_options_array
            ACCESSIBILITY_FLAGS.add(String
                    .valueOf(PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_CLOSED_CAPTION));
            ACCESSIBILITY_FLAGS.add(String
                    .valueOf(PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_ANY_WHEELCHAIR));
            ACCESSIBILITY_FLAGS.add(String
                    .valueOf(PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_STANDARD_WHEELCHAIR));
            ACCESSIBILITY_FLAGS.add(String
                    .valueOf(PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_STATIONARY_SEATING));
            ACCESSIBILITY_FLAGS.add(String
                    .valueOf(PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_ASSISTIVE_LISTENING));
            ACCESSIBILITY_FLAGS.add(String
                    .valueOf(PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_SIGN_LANGUAGE));
        }
        return ACCESSIBILITY_FLAGS;
    }
    
    public static String getPropNameForAccessibilityFlag(String flagName) {
    	
    	long accessibilityType;
    	
    	try { 
	    	accessibilityType = Long.parseLong(flagName, 10);
    	} catch(NumberFormatException ex) {
    		accessibilityType = -1;
    	}
    	
    	if( accessibilityType == PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_CLOSED_CAPTION){
    		return "Closed caption";
    	} else if ( accessibilityType == PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_ANY_WHEELCHAIR) {
    		return "ECV wheelchair";
    	} else if ( accessibilityType == PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_STANDARD_WHEELCHAIR) {
    		return "Standard wheelchair";
    	} else if ( accessibilityType == PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_STATIONARY_SEATING) {
    		return "Stationary seating";
    	} else if ( accessibilityType == PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_ASSISTIVE_LISTENING) {
    		return "Assistive listening";
    	} else if ( accessibilityType == PointsOfInterestTable.VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_SIGN_LANGUAGE) {
    		return "Sign language";
    	} else {
    		return "";
    	}
    	
    }

    public static String getPropNameForVenueFlag(String flagName) {

        long accessibilityType;

        try {
            accessibilityType = Long.parseLong(flagName, 10);
        } catch(NumberFormatException ex) {
            accessibilityType = -1;
        }

        if( accessibilityType == VenuesTable.VAL_VENUE_ID_VOLCANO_BAY){
            return "Universal Orlando Resort";
        } else if ( accessibilityType == VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_FLORIDA) {
            return "Universal Studios Florida";
        } else if ( accessibilityType == VenuesTable.VAL_VENUE_ID_ISLANDS_OF_ADVENTURE) {
            return "Islands of Adventure";
        } else if ( accessibilityType == VenuesTable.VAL_VENUE_ID_CITY_WALK_ORLANDO
                || accessibilityType == VenuesTable.VAL_VENUE_ID_CITY_WALK_HOLLYWOOD) {
            return "City Walk";
        } else if ( accessibilityType == VenuesTable.VAL_VENUE_ID_WET_N_WILD) {
            return "Wet And Wild";
        } else {
            return "";
        }

    }

    public static List<String> getRideSubTypeFlags() {
        if (RIDE_SUB_TYPE_FLAGS == null) {
            RIDE_SUB_TYPE_FLAGS = new ArrayList<String>();
            // Order and contents matches string-array
            // R.array.advanced_filter_ride_sub_types
            RIDE_SUB_TYPE_FLAGS.add(String.valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_THRILL));
            RIDE_SUB_TYPE_FLAGS.add(String
                    .valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_VIDEO_3D_4D));
            RIDE_SUB_TYPE_FLAGS.add(String.valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_WATER));
            RIDE_SUB_TYPE_FLAGS.add(String
                    .valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_KID_FRIENDLY));
        }
        return RIDE_SUB_TYPE_FLAGS;
    }

    public static List<String> getShowSubTypeFlags() {
        if (SHOW_SUB_TYPE_FLAGS == null) {
            SHOW_SUB_TYPE_FLAGS = new ArrayList<String>();
            // Order and contents matches string-array
            // R.array.advanced_filter_show_sub_types
            SHOW_SUB_TYPE_FLAGS.add(String.valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_ACTION));
            SHOW_SUB_TYPE_FLAGS.add(String.valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_COMEDY));
            SHOW_SUB_TYPE_FLAGS.add(String.valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_MUSIC));
            SHOW_SUB_TYPE_FLAGS.add(String.valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_PARADE));
            SHOW_SUB_TYPE_FLAGS.add(String
                    .valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_CHARACTER));
        }
        return SHOW_SUB_TYPE_FLAGS;
    }

    public static List<String> getDiningSubTypeFlags() {
        if (DINING_SUB_TYPE_FLAGS == null) {
            DINING_SUB_TYPE_FLAGS = new ArrayList<String>();
            // Order and contents matches string-array
            // R.array.advanced_filter_dining_sub_types
            // Hollywood dining types
            if (BuildConfigUtils.isLocationFlavorHollywood()) {
                DINING_SUB_TYPE_FLAGS.add(String
                        .valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_CASUAL_DINING));
                DINING_SUB_TYPE_FLAGS.add(String
                        .valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_QUICK_SERVICE));
                DINING_SUB_TYPE_FLAGS.add(String
                        .valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_SNACKS));
            }
            // Orlando dining types
            else {
                DINING_SUB_TYPE_FLAGS.add(String
                        .valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_FINE_DINING));
                DINING_SUB_TYPE_FLAGS.add(String
                        .valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_CASUAL_DINING));
                DINING_SUB_TYPE_FLAGS.add(String
                        .valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_QUICK_SERVICE));
            }

        }
        return DINING_SUB_TYPE_FLAGS;
    }

    public static List<String> getDiningPlanFlags() {
        if (DINING_PLAN_FLAGS == null) {
            DINING_PLAN_FLAGS = new ArrayList<String>();
            // Order and contents matches string-array
            // R.array.advanced_filter_dining_plans
            DINING_PLAN_FLAGS.add(String
                    .valueOf(PointsOfInterestTable.VAL_OPTION_FLAG_DINING_UNIVERSAL_DINING_PLAN));
            DINING_PLAN_FLAGS
                    .add(String
                            .valueOf(PointsOfInterestTable.VAL_OPTION_FLAG_DINING_UNIVERSAL_DINING_PLAN_QUICK_SERVICE));
        }
        return DINING_PLAN_FLAGS;
    }

    public static List<String> getShoppingSubTypeFlags() {
        if (SHOPPING_SUB_TYPE_FLAGS == null) {
            SHOPPING_SUB_TYPE_FLAGS = new ArrayList<String>();
            // Order and contents matches string-array
            // R.array.advanced_filter_shopping_sub_types
            // Hollywood shopping types
            if (BuildConfigUtils.isLocationFlavorHollywood()) {
                SHOPPING_SUB_TYPE_FLAGS.add(String
                        .valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_TYPE_APPAREL_ACCESSORIES));
                SHOPPING_SUB_TYPE_FLAGS.add(String
                        .valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_TYPE_COLLECTIBLES));
                SHOPPING_SUB_TYPE_FLAGS.add(String
                        .valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_TYPE_GAMES_NOVELTIES));
                SHOPPING_SUB_TYPE_FLAGS.add(String
                        .valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_TYPE_FOOD_SPECIALTIES));
                SHOPPING_SUB_TYPE_FLAGS.add(String
                        .valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_TYPE_HEALTH_BEAUTY));
            }
            // Orlando shopping types
            else {
                // No shopping sub types in Orlando
            }

        }
        return SHOPPING_SUB_TYPE_FLAGS;
    }

    public static List<String> getEntertainmentSubTypeFlags() {
        if (ENTERTAINMENT_SUB_TYPE_FLAGS == null) {
            ENTERTAINMENT_SUB_TYPE_FLAGS = new ArrayList<String>();
            // Order and contents matches string-array
            // R.array.advanced_filter_entertainment_sub_types
            // Hollywood entertainment types
            if (BuildConfigUtils.isLocationFlavorHollywood()) {
                ENTERTAINMENT_SUB_TYPE_FLAGS.add(String
                        .valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_ENTERTAINMENT_TYPE_NIGHT_SPOTS));
                ENTERTAINMENT_SUB_TYPE_FLAGS.add(String
                        .valueOf(PointsOfInterestTable.VAL_SUB_TYPE_FLAG_ENTERTAINMENT_TYPE_EXPERIENCES));
            }
            // Orlando entertainment types
            else {
                // No entertainment sub types in Orlando
            }

        }
        return ENTERTAINMENT_SUB_TYPE_FLAGS;
    }

    private FilterOptions() {
        this.filterTabOptions = new ArrayList<FilterTabOption>();
        this.accessibilityOptions = new ArrayList<String>();
        this.rideFilterVenues = new ArrayList<String>();
        this.rideSubTypes = new ArrayList<String>();
        this.showFilterVenues = new ArrayList<String>();
        this.showSubTypes = new ArrayList<String>();
        this.diningFilterVenues = new ArrayList<String>();
        this.diningSubTypes = new ArrayList<String>();
        this.diningPlans = new ArrayList<String>();
        this.shoppingFilterVenues = new ArrayList<String>();
        this.rentalServiceFilterVenues = new ArrayList<String>();
        this.shoppingSubTypes = new ArrayList<String>();
        this.entertainmentSubTypes = new ArrayList<String>();
    }

    public FilterOptions(List<FilterTabOption> filterTabOptions, ExploreType exploreType) {
        this();
        this.filterTabOptions = filterTabOptions;
        this.filterSort = getDefaultSort(exploreType);
        this.exploreType = exploreType;
    }

    /**
     * Determine the default sort option based on ExploreType
     * 
     * @param exploreType
     * @return
     */
    public static FilterSort getDefaultSort(ExploreType exploreType) {
        FilterSort defaultFilterSort;
        switch (exploreType) {
            case SHOPPING:
            case RENTAL_SERVICES:
            case SHOPPING_EXPRESS_PASS:
            case ATTRACTIONS_PACKAGE_PICKUP:
            case UNIVERSAL_DINING:
            case UNIVERSAL_DINING_QUICK_SERVICE:
                defaultFilterSort = FilterSort.ALPHABETICAL;
                break;
            case EVENTS:
            case EVENT_LIST:
            case UPCOMING_EVENTS:
                defaultFilterSort = FilterSort.EVENT_DATE;
                break;
            default:
                defaultFilterSort = FilterSort.TYPE;
                break;
        }
        return defaultFilterSort;
    }

    /**
     * Reset all filters
     *
     * @param filterOptions
     * @return
     */
    public static FilterOptions resetFilter(FilterOptions filterOptions) {
        FilterOptions newFilterOptions = new FilterOptions(filterOptions.getFilterTabOptions(),
                filterOptions.getExploreType());
        // Reset filter tabs to their original state
        if (newFilterOptions.getFilterTabOptions() != null) {
            for (FilterTabOption filterTabOption : newFilterOptions.getFilterTabOptions()) {
                filterTabOption.setChecked(filterTabOption.getDefaultCheckedState());
                // Reset filter tab button if present
                if (filterTabOption.getFilterTabButton() != null) {
                    filterTabOption.getFilterTabButton().setChecked(filterTabOption.getDefaultCheckedState());
                }
            }
        }

        return newFilterOptions;
    }

    /**
     * Determine if advanced filter menu option should be displayed based on ExplorType
     *
     * @param exploreType
     * @return
     */
    public static boolean isAdvancedFilterAvailable(ExploreType exploreType) {
        boolean isAvailable;
        switch (exploreType) {
            case CITY_WALK_ORLANDO:
            case DINING:
            case ISLANDS_OF_ADVENTURE:
            case RIDES:
            case SHOPPING:
            case RENTAL_SERVICES:
            case SHOWS:
            case UNIVERSAL_STUDIOS_FLORIDA:
            case WET_N_WILD:
            case WAIT_TIMES:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_DINING:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING:
            case CITY_WALK_HOLLYWOOD:
                isAvailable = true;
                break;
            default:
                isAvailable = false;
                break;
        }

        return isAvailable;
    }

    /**
     * Determine if any complex filter options are selected
     *
     * @return
     */
    public boolean isFilterComplex() {
        if (kidFriendly || childSwap || singleRiderLine || characterDining || vegetarianHealthy
                || expressPassSold || parentalSupervisionRequired || lifeJacketRequired || amexDining
                || amexShopping) {
            return true;
        }
        if (rideFilterMaxWaitTime > 0 || riderHeight > 0 || nextShowTime > 0 || diningMinPrice > 0
                || entertainmentMinPrice > 0) {
            return true;
        }
        if (!rideFilterVenues.isEmpty() || !accessibilityOptions.isEmpty() || !showFilterVenues.isEmpty()
                || !diningFilterVenues.isEmpty() || !shoppingFilterVenues.isEmpty() || !rentalServiceFilterVenues.isEmpty()) {
            return true;
        }
        if (exploreType != RIDES && exploreType != ExploreType.WAIT_TIMES
                && !rideSubTypes.isEmpty()) {
            return true;
        }
        if (exploreType != SHOWS && !showSubTypes.isEmpty()) {
            return true;
        }
        if (exploreType != ExploreType.DINING && !diningSubTypes.isEmpty()) {
            return true;
        }
        if (exploreType != ExploreType.SHOPPING && !shoppingSubTypes.isEmpty()) {
            return true;
        }
        if (!entertainmentSubTypes.isEmpty()) {
            return true;
        }
        // Filter is not complex when explore type is attractions and express
        // pass line is true, otherwise the filter is complex
        if (exploreType != ExploreType.ATTRACTIONS_EXPRESS_PASS && expressPassLine) {
            return true;
        }
        // Filter is not complex when explore type is a dining plan and there are dining plan filters
        if (exploreType != ExploreType.UNIVERSAL_DINING
                && exploreType != ExploreType.UNIVERSAL_DINING_QUICK_SERVICE && !diningPlans.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * @return the mFilterTabOptions
     */
    public List<FilterTabOption> getFilterTabOptions() {
        return filterTabOptions;
    }

    /**
     * @param mFilterTabOptions
     *            the mFilterTabOptions to set
     */
    public void setFilterTabOptions(List<FilterTabOption> mFilterTabOptions) {
        this.filterTabOptions = mFilterTabOptions;
    }

    /**
     * Returns current sort, returns default if set to distance and location is null
     *
     * @return the mFilterSort
     */
    public FilterSort getFilterSort() {
        if (filterSort == FilterSort.DISTANCE && userLocation == null) {
            return FilterOptions.getDefaultSort(exploreType);
        }
        return filterSort;
    }

    /**
     * @param mFilterSort
     *            the mFilterSort to set
     */
    public void setFilterSort(FilterSort mFilterSort) {
        this.filterSort = mFilterSort;
    }

    /**
     * @return the userLocation
     */
    public Location getUserLocation() {
        return userLocation;
    }

    /**
     * @param userLocation
     *            the userLocation to set
     */
    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    /**
     * @return the mExploreType
     */
    public ExploreType getExploreType() {
        return exploreType;
    }

    /**
     * @param mExploreType
     *            the mExploreType to set
     */
    public void setExploreType(ExploreType mExploreType) {
        this.exploreType = mExploreType;
    }

    /**
     * @return the expressPassLine
     */
    public boolean isExpressPassLine() {
        return expressPassLine;
    }

    /**
     * @param expressPassLine
     *            the expressPassLine to set
     */
    public void setExpressPassLine(boolean expressPassLine) {
        this.expressPassLine = expressPassLine;
    }

    /**
     * @return the kidFriendly
     */
    public boolean isKidFriendly() {
        return kidFriendly;
    }

    /**
     * @param kidFriendly
     *            the kidFriendly to set
     */
    public void setKidFriendly(boolean kidFriendly) {
        this.kidFriendly = kidFriendly;
    }

    /**
     * @return the accessibilityOptions
     */
    public List<String> getAccessibilityOptions() {
        return accessibilityOptions;
    }

    /**
     * @param accessibilityOptions
     *            the accessibilityOptions to set
     */
    public void setAccessibilityOptions(List<String> accessibilityOptions) {
        this.accessibilityOptions = accessibilityOptions;
    }

    /**
     * @return the rideFilterVenues
     */
    public List<String> getRideFilterVenues() {
        return rideFilterVenues;
    }

    /**
     * @param rideFilterVenues
     *            the rideFilterVenues to set
     */
    public void setRideFilterVenues(List<String> rideFilterVenues) {
        this.rideFilterVenues = rideFilterVenues;
    }

    /**
     * @return the rideFilterMaxWaitTime
     */
    public int getRideFilterMaxWaitTime() {
        return rideFilterMaxWaitTime;
    }

    /**
     * @param rideFilterMaxWaitTime
     *            the rideFilterMaxWaitTime to set
     */
    public void setRideFilterMaxWaitTime(int rideFilterMaxWaitTime) {
        this.rideFilterMaxWaitTime = rideFilterMaxWaitTime;
    }

    /**
     * @return the riderHeight
     */
    public int getRiderHeight() {
        return riderHeight;
    }

    /**
     * @param riderHeight
     *            the riderHeight to set
     */
    public void setRiderHeight(int riderHeight) {
        this.riderHeight = riderHeight;
    }

    /**
     * @return the singleRiderLine
     */
    public boolean isSingleRiderLine() {
        return singleRiderLine;
    }

    /**
     * @param singleRiderLine
     *            the singleRiderLine to set
     */
    public void setSingleRiderLine(boolean singleRiderLine) {
        this.singleRiderLine = singleRiderLine;
    }

    /**
     * @return the childSwap
     */
    public boolean isChildSwap() {
        return childSwap;
    }

    /**
     * @param childSwap
     *            the childSwap to set
     */
    public void setChildSwap(boolean childSwap) {
        this.childSwap = childSwap;
    }

    /**
     * @return the rideSubTypes
     */
    public List<String> getRideSubTypes() {
        return rideSubTypes;
    }

    /**
     * @param rideSubTypes
     *            the rideSubTypes to set
     */
    public void setRideSubTypes(List<String> rideSubTypes) {
        this.rideSubTypes = rideSubTypes;
    }

    /**
     * @return the showFilterVenues
     */
    public List<String> getShowFilterVenues() {
        return showFilterVenues;
    }

    /**
     * @param showFilterVenues
     *            the showFilterVenues to set
     */
    public void setShowFilterVenues(List<String> showFilterVenues) {
        this.showFilterVenues = showFilterVenues;
    }

    /**
     * @return the showSubTypes
     */
    public List<String> getShowSubTypes() {
        return showSubTypes;
    }

    /**
     * @param showSubTypes
     *            the showSubTypes to set
     */
    public void setShowSubTypes(List<String> showSubTypes) {
        this.showSubTypes = showSubTypes;
    }

    /**
     * @return the nextShowTime
     */
    public int getNextShowTime() {
        return nextShowTime;
    }

    /**
     * @param nextShowTime
     *            the nextShowTime to set
     */
    public void setNextShowTime(int nextShowTime) {
        this.nextShowTime = nextShowTime;
    }

    /**
     * @return the diningFilterVenues
     */
    public List<String> getDiningFilterVenues() {
        return diningFilterVenues;
    }

    /**
     * @param diningFilterVenues
     *            the diningFilterVenues to set
     */
    public void setDiningFilterVenues(List<String> diningFilterVenues) {
        this.diningFilterVenues = diningFilterVenues;
    }

    /**
     * @return the diningSubTypes
     */
    public List<String> getDiningSubTypes() {
        return diningSubTypes;
    }

    /**
     * @param diningSubTypes
     *            the diningSubTypes to set
     */
    public void setDiningSubTypes(List<String> diningSubTypes) {
        this.diningSubTypes = diningSubTypes;
    }

    /**
     * @return the diningStartingPrice
     */
    public int getDiningMinPrice() {
        return diningMinPrice;
    }

    /**
     * @param diningStartingPrice
     *            the diningStartingPrice to set
     */
    public void setDiningMinPrice(int diningStartingPrice) {
        this.diningMinPrice = diningStartingPrice;
    }

    /**
     * @return the diningPlans
     */
    public List<String> getDiningPlans() {
        return diningPlans;
    }

    /**
     * @param diningPlans
     *            the diningPlans to set
     */
    public void setDiningPlans(List<String> diningPlans) {
        this.diningPlans = diningPlans;
    }

    /**
     * @return the characterDining
     */
    public boolean isCharacterDining() {
        return characterDining;
    }

    /**
     * @param characterDining
     *            the characterDining to set
     */
    public void setCharacterDining(boolean characterDining) {
        this.characterDining = characterDining;
    }

    /**
     * @return the vegetarianHealthy
     */
    public boolean isVegetarianHealthy() {
        return vegetarianHealthy;
    }

    /**
     * @param vegetarianHealthy
     *            the vegetarianHealthy to set
     */
    public void setVegetarianHealthy(boolean vegetarianHealthy) {
        this.vegetarianHealthy = vegetarianHealthy;
    }

    /**
     * @return the shoppingFilterVenues
     */
    public List<String> getShoppingFilterVenues() {
        return shoppingFilterVenues;
    }

    /**
     * @param shoppingFilterVenues
     *            the shoppingFilterVenues to set
     */
    public void setShoppingFilterVenues(List<String> shoppingFilterVenues) {
        this.shoppingFilterVenues = shoppingFilterVenues;
    }

    public List<String> getRentalServicesFilterVenues() {return rentalServiceFilterVenues;}

    public void setRentalServicesFilterVenues(List<String> rentalServiceFilterVenues) {
        this.rentalServiceFilterVenues = rentalServiceFilterVenues;
    }

    public boolean isRentalTypeEcv() {return rentalTypeEcv;}
    public void setRentalTypeEcv(Boolean rentalTypeEcv) {this.rentalTypeEcv = rentalTypeEcv;}

    public boolean isRentalTypeStroller() {return rentalTypeStroller;}
    public void setRentalTypeStroller(Boolean rentalTypeStroller) {this.rentalTypeStroller = rentalTypeStroller;}

    public boolean isRentalTypeWheelChair() {return rentalTypeWheelChair;}
    public void setRentalTypeWheelChair(Boolean rentalTypeWheelChair) {this.rentalTypeWheelChair = rentalTypeWheelChair;}

    /**
     * @return the shopPackagePickup
     */
    public boolean isShopPackagePickup() {
        return shopPackagePickup;
    }

    /**
     * @param shopPackagePickup
     *            the shopPackagePickup to set
     */
    public void setShopPackagePickup(boolean shopPackagePickup) {
        this.shopPackagePickup = shopPackagePickup;
    }

    /**
     * @return the expressPassSold
     */
    public boolean isExpressPassSold() {
        return expressPassSold;
    }

    /**
     * @param expressPassSold
     *            the expressPassSold to set
     */
    public void setExpressPassSold(boolean expressPassSold) {
        this.expressPassSold = expressPassSold;
    }

    /**
     * @return the entertainmentMinPrice
     */
    public int getEntertainmentMinPrice() {
        return entertainmentMinPrice;
    }

    /**
     * @param entertainmentMinPrice
     *            the entertainmentMinPrice to set
     */
    public void setEntertainmentMinPrice(int entertainmentMinPrice) {
        this.entertainmentMinPrice = entertainmentMinPrice;
    }

    /**
     * @return the parentalSupervisionRequired
     */
    public boolean isParentalSupervisionRequired() {
        return parentalSupervisionRequired;
    }

    /**
     * @param parentalSupervisionRequired
     *            the parentalSupervisionRequired to set
     */
    public void setParentalSupervisionRequired(boolean parentalSupervisionRequired) {
        this.parentalSupervisionRequired = parentalSupervisionRequired;
    }

    /**
     * @return the lifeJacketRequired
     */
    public boolean isLifeJacketRequired() {
        return lifeJacketRequired;
    }

    /**
     * @param lifeJacketRequired
     *            the lifeJacketRequired to set
     */
    public void setLifeJacketRequired(boolean lifeJacketRequired) {
        this.lifeJacketRequired = lifeJacketRequired;
    }

    /**
     * @return the amexShopping
     */
    public boolean isAmexShopping() {
        return amexShopping;
    }

    /**
     * @param amexShopping
     *            the amexShopping to set
     */
    public void setAmexShopping(boolean amexShopping) {
        this.amexShopping = amexShopping;
    }

    /**
     * @return the amexDining
     */
    public boolean isAmexDining() {
        return amexDining;
    }

    /**
     * @param amexDining
     *            the amexDining to set
     */
    public void setAmexDining(boolean amexDining) {
        this.amexDining = amexDining;
    }

    public List<String> getShoppingSubTypes() {
        return shoppingSubTypes;
    }

    public void setShoppingSubTypes(List<String> shoppingSubTypes) {
        this.shoppingSubTypes = shoppingSubTypes;
    }

    public List<String> getEntertainmentSubTypes() {
        return entertainmentSubTypes;
    }

    public void setEntertainmentSubTypes(List<String> entertainmentSubTypes) {
        this.entertainmentSubTypes = entertainmentSubTypes;
    }

    public boolean isLowerLot() {
        return lowerLot;
    }

    public void setLowerLot(boolean lowerLot) {
        this.lowerLot = lowerLot;
    }

    public boolean isUpperLot() {
        return upperLot;
    }

    public void setUpperLot(boolean upperLot) {
        this.upperLot = upperLot;
    }
}
