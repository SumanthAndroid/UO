/**
 *
 */
package com.universalstudios.orlandoresort.model.network.analytics;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.adobe.mobile.Analytics;
import com.adobe.mobile.Config;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ExpressPassTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ParkTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupOrder;
import com.universalstudios.orlandoresort.model.network.domain.venue.Venue;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Steven Byle
 */
public class AnalyticsUtils {
    private static final String TAG = AnalyticsUtils.class.getSimpleName();

    public static String CHANNEL_APP = "s.channel";
    public static final String CHANNEL_APP_UO = "App.UO";
    public static final String CHANNEL_APP_USH = "App.USH";
    public static final String PAGE_NAME_APP_TICKETS_UO = "AppTickets.UO";

    static {
        // Hollywood specific keys
        if (BuildConfigUtils.isLocationFlavorHollywood()) {
            CHANNEL_APP = CHANNEL_APP_USH;
        }
        // Orlando specific keys
        else {
            CHANNEL_APP = CHANNEL_APP_UO;
        }
    }

    public static final String USER_GROUP_B2C = "B2C";

    public static final String CONTENT_GROUP_SITE_NAV = "Site Navigation";
    public static final String CONTENT_GROUP_PLANNING = "Planning";
    public static final String CONTENT_GROUP_PARKS = "Parks";
    public static final String CONTENT_GROUP_TICKETS = "Tickets";
    public static final String CONTENT_GROUP_PROMOTIONS = "Promotions";
    public static final String CONTENT_GROUP_SITE_TICKETS = "AppTickets.UO";
    public static final String CONTENT_GROUP_SALES = "Sales";
    public static final String CONTENT_GROUP_CRM = "CRM";
    public static final String CONTENT_GROUP_ADDON = "Add-on";

    public static final String CONTENT_FOCUS_USH = "USH";
    public static final String CONTENT_FOCUS_CWH = "CW";
    public static final String CONTENT_FOCUS_GUEST_FEEDBACK = "Guest Feedback";
    public static final String CONTENT_FOCUS_NEWSLETTER_SIGN_UP = "Newsletter Sign Up";
    public static final String CONTENT_FOCUS_IOA = "IOA";
    public static final String CONTENT_FOCUS_USF = "USF";
    public static final String CONTENT_FOCUS_CW = "CityWalk";
    public static final String CONTENT_FOCUS_HOTELS = "Hotels";
    public static final String CONTENT_FOCUS_UOR = "UOR";
    public static final String CONTENT_FOCUS_GUIDE_ME = "Guide Me";
    public static final String CONTENT_FOCUS_GUEST_SERVICES = "Guest Services";
    public static final String CONTENT_FOCUS_EXPRESS_PASS = "Express Pass";
    public static final String CONTENT_FOCUS_RESTROOMS = "Restrooms";
    public static final String CONTENT_FOCUS_LOCKERS = "Lockers";
    public static final String CONTENT_FOCUS_WNW = "WnW";
    public static final String CONTENT_FOCUS_EVENTS = "Events";
    public static final String CONTENT_FOCUS_TICKETS = "Tickets";
    public static final String CONTENT_FOCUS_ALERTS = "Alerts";
    public static final String CONTENT_FOCUS_CHECKOUT = "Checkout";
    public static final String CONTENT_FOCUS_ERROR = "Error";
    public static final String CONTENT_FOCUS_ACCOUNT = "Account";

    public static final String CONTENT_SUB_1_RIDES = "Rides";
    public static final String CONTENT_SUB_1_SHOWS = "Shows";
    public static final String CONTENT_SUB_1_DINING = "Dining";
    public static final String CONTENT_SUB_1_SHOPS = "Shops";
    public static final String CONTENT_SUB_1_FAQ = "FAQ";
    public static final String CONTENT_SUB_1_GUEST_SERVICES_BOOTHS = "Guest Services Booths";
    public static final String CONTENT_SUB_1_ATMS = "ATMs";
    public static final String CONTENT_SUB_1_LOST_AND_FOUND = "Lost and Found";
    public static final String CONTENT_SUB_1_FIRST_AID = "First Aid";
    public static final String CONTENT_SUB_1_SERVICE_ANIMAL_REST_AREAS = "Service Animal Rest Areas";
    public static final String CONTENT_SUB_1_SMOKING_AREAS = "Smoking Areas";
    public static final String CONTENT_SUB_1_EVENT_SERIES_CAROUSEL = "Carousel";
    public static final String CONTENT_SUB_1_EVENTS_TODAY = "Today";
    public static final String CONTENT_SUB_1_EVENTS_ACTIVITIES_FORMAT = "Activities %s";
    public static final String CONTENT_SUB_1_TICKETS = "App Tickets Store";

    public static final String CONTENT_SUB_2_POPOVER = "Pop-Over";
    public static final String CONTENT_SUB_2_MAP = "Map";
    public static final String CONTENT_SUB_2_LIST = "List";
    public static final String CONTENT_SUB_2_HOME = "Home Screen";
    public static final String CONTENT_SUB_2_GUIDE_ME_DEPARTURE = "Departure";
    public static final String CONTENT_SUB_2_GUIDE_ME_ARRIVAL = "Arrival";
    public static final String CONTENT_SUB_2_PARK_NOTIFICATIONS = "Park Notifications";
    public static final String CONTENT_SUB_2_HOURS_AND_DIRECTIONS = "Hours and Directions";
    public static final String CONTENT_SUB_2_VACATION_PLANNER = "Vacation Planner";
    public static final String CONTENT_SUB_2_SEARCH = "Search";
    public static final String CONTENT_SUB_2_DETAIL = "Detail";
    public static final String CONTENT_SUB_2_CHILD_SWAP = "Child Swap Info";
    public static final String CONTENT_SUB_2_SINGLE_RIDER = "Single Rider Info";
    public static final String CONTENT_SUB_2_SETTINGS = "Settings";
    public static final String CONTENT_SUB_2_PRIVACY_AND_LEGAL = "Privacy and Legal";
    public static final String CONTENT_SUB_2_PARKING_REMINDER = "Parking Reminder";
    public static final String CONTENT_SUB_2_WALKTHROUGH = "Walkthrough";
    public static final String CONTENT_SUB_2_VERSION_INFO = "Version Info";
    public static final String CONTENT_SUB_2_PRIVACY_POLICY = "Privacy Policy";
    public static final String CONTENT_SUB_2_TERMS_OF_SERVICE = "Terms of Service";
    public static final String CONTENT_SUB_2_FAVORITES = "Favorites";
    public static final String CONTENT_SUB_2_MY_ALERTS = "My Alerts";
    public static final String CONTENT_SUB_2_SET_WAIT_TIME_ALERT = "Set Wait Time Alert";
    public static final String CONTENT_SUB_2_SET_SHOW_TIME_ALERT = "Set Show Time Alert";
    public static final String CONTENT_SUB_2_DINING_PLANS_INFO = "Dining Plans Info";
    public static final String CONTENT_SUB_2_APPLY_FILTER = "Apply Filter";
    public static final String CONTENT_SUB_2_EXPRESS_PASS_INFO = "Express Pass Info";
    public static final String CONTENT_SUB_2_PACKAGE_PICKUP_INFO = "Package Pickup Info";
    public static final String CONTENT_SUB_2_OFFER_SERIES_DETAIL_FORMAT = "%s Landing Page";
    public static final String CONTENT_SUB_2_OFFER_DETAIL_FORMAT = "%s %s Detail Page";
    public static final String CONTENT_SUB_2_OFFER_COUPON_SUCESS = "%s %s Coupon Page Success";
    public static final String CONTENT_SUB_2_OFFER_COUPON_FAIL = "%s %s Coupon Page Fail";
    public static final String CONTENT_SUB_2_OFFER_SERIES_APPLY_CLICK = "%s Apply Click";
    public static final String CONTENT_SUB_2_EVENTS_TODAY_FORMAT = "%s No Events";
    public static final String CONTENT_SUB_2_EVENT_SERIES_SCHEDULE_FORMAT = "%s Schedule";
    public static final String CONTENT_SUB_2_TICKETS = "Browse Tickets";
    public static final String CONTENT_SUB_2_TICKET_DETAILS = "Ticket Details";
    public static final String CONTENT_SUB_2_TICKET_ADD_TO_CART = "Add To Cart";
    public static final String CONTENT_SUB_2_TICKET_SHOPPING_CART = "Shopping Cart";
    public static final String CONTENT_SUB_2_BILLING_INFO = "Billing Information";
    public static final String CONTENT_SUB_2_ASSIGN_TICKETS = "Assign Tickets";
    public static final String CONTENT_SUB_2_TICKET_CONFORMATION = "Confirmation";
    public static final String CONTENT_SUB_2_VIEW_ASSIGN_TICKETS = "View Assign Tickets";
    public static final String CONTENT_SUB_2_FULL_PAGE = "Full Page";
    public static final String PROPERTY_NAME_RESORT_WIDE = "Resort Wide";
    public static final String PROPERTY_NAME_PARKS = "Parks";
    public static final String PROPERTY_NAME_IOA = "IOA";
    public static final String PROPERTY_NAME_USF = "USF";
    public static final String PROPERTY_NAME_CW = "CityWalk";
    public static final String PROPERTY_NAME_HOTELS = "Hotels";
    public static final String PROPERTY_NAME_WNW = "WnW";
    public static final String PROPERTY_NAME_USH = "USH";
    public static final String PROPERTY_NAME_CWH = "CW";
    public static final String PROPERTY_NAME_ANDROID = "Android Verified";
    public static final String PROPERTY_NAME_PARKS_RESORT = "Parks/Resorts";
    public static final String PROPERTY_NAME_SALES = "Sales";
    public static final String PROPERTY_NAME_CHECKOUT = "Checkout";
    public static final String PROPERTY_NAME_GENERAL = "General";
    public static final String PROPERTY_NAME_BEST_PRICE_GUARANTEE = "Best Price Guarantee";
    public static final String PROPERTY_NAME_UO = "Universal Orlando";
    public static final String PROPERTY_NAME_PARK_ADMISSION = "Park Admission";
    public static final String PROPERTY_NAME_SELL_PRODUCTS = "Sell Products";
    public static final String PROPERTY_NAME_PRODUCT_SELECTION = "Product Selection";
    public static final String PROPERTY_NAME_CHECKOUT_PROCESS = "Checkout Process";
    public static final String PROPERTY_NAME_NEW_OR_RETURN_VISITOR = "s.getNewRepeat()";
    public static final String PROPERTY_NAME_PAGE_BEST_PRICE_GUARANTEE = "Best Price Guarantee";
    public static final String PROPERTY_NAME_ASSIGN_TICKETS = "Assign Tickets";
    public static final String PROPERTY_NAME_GUEST_ASSIGN_TICKETS = "Guest Assigns Tickets";
    public static final String PROPERTY_NAME_VIEW_ASSIGN_TICKETS = "View Assign Tickets";
    public static final String PROPERTY_NAME_GUEST_VIEW_ASSIGN_TICKETS = "Guest View Assigns Tickets";
    public static final String PROPERTY_NAME_TICKETS = "Tickets";
    public static final String PROPERTY_NAME_BILLING_INFO = "Billing Information";
    public static final String PROPERTY_NAME_PAYMENT_INFO = "Payment Information";
    public static final String PROPERTY_NAME_TICKET_CONFORMATION = "Confirmation";
    public static final String PROPERTY_NAME_TICKET_PURCHASE_ID = "%web store - purchaseID%";
    public static final String PROPERTY_NAME_NO_TICKETS = "No Tickets";
    public static final String PROPERTY_NAME_SHOPPING = "Shopping";
    public static final String PROPERTY_NAME_DAY_SINCE_LAST_VISIT = "s.getDaysSinceLastVisit()";
    public static final String PROPERTY_NAME_TC = "Terms and Conditions";
    public static final String PROPERTY_NAME_TICKET_SET = "Tickets Set";
    public static final String PROPERTY_NAME_TICKET_DETAILS = "Ticket Details";
    public static final String PROPERTY_NAME_ADDITIONAL_TICKET_DETAILS = "Additional Ticket Details";
    public static final String PROPERTY_NAME_PARK_TICKET_INFO = "Park Ticket Information";
    public static final String PROPERTY_NAME_BILLING_ZIP = "Billing Zip";

    public static final String PROPERTY_VALUE_AMEX = "AmEx";

    public static final String EVENT_NAME_FAVORITE = "Favorite";
    public static final String EVENT_NAME_UNFAVORITE = "Unfavorite";
    public static final String EVENT_NAME_GUIDE_ME = "Guide Me";
    public static final String EVENT_NAME_GUIDE_ME_EXIT = "Guide Me Exit";
    public static final String EVENT_NAME_SKIP_WALKTHROUGH = "Skip Walkthrough";
    public static final String EVENT_NAME_WALKTHROUGH_COMPLETE = "Walkthrough Complete";
    public static final String EVENT_NAME_GET_RESORT_DIRECTIONS = "Get Resort Directions";
    public static final String EVENT_NAME_SOCIAL_SHARE = "Social Share Android";
    public static final String EVENT_NAME_CALL = "Call";
    public static final String EVENT_NAME_YOURE_HERE_GET_EXPRESS_PASS = "You're Here | Get Express Pass";
    public static final String EVENT_NAME_YOURE_HERE_REMIND_ME_LATER_EXPRESS_PASS = "You're Here | Remind Me Later Express Pass";
    public static final String EVENT_NAME_SET_ALERT = "Set Alert";
    public static final String EVENT_NAME_RECEIVE_ALERT = "Receive Alert";
    public static final String EVENT_NAME_GET_WET_N_WILD_DIRECTIONS = "Get Wet 'n Wild Directions";
    public static final String EVENT_NAME_MAP = "Map";
    public static final String EVENT_NAME_MENU_PDF = "View Menu - PDF";
    public static final String EVENT_NAME_MENU_IMAGES = "View Menu - Image";
    public static final String EVENT_NAME_ACCESSIBILITY_OPTIONS = "Accessibility - Options";

    public static final Integer EVENT_NUM_FAVORITE = 74;
    public static final Integer EVENT_NUM_UNFAVORITE = 75;
    public static final Integer EVENT_NUM_SOCIAL_SHARE = 76;
    public static final Integer EVENT_NUM_GUIDE_ME = 81;
    public static final Integer EVENT_NUM_SET_ALERT = 82;
    public static final Integer EVENT_NUM_RECEIVE_ALERT = 83;
    public static final Integer EVENT_NUM_GUEST_FEEDBACK_SUCCESS = 87;
    public static final Integer EVENT_NUM_NEWSLETTER_SIGNUP_SUCCESS = 88;

    public static final String LOCATION_STATE_IN_PARK = "In-Park";
    public static final String LOCATION_STATE_OUT_OF_PARK = "Out-of-Park";
    public static final String LOCATION_STATE_UNKOWN_LOCATION = "Unknown";

    public static final String UO_PAGE_IDENTIFIER_WALLET_TICKET_CARDS = "My Wallet Ticket Cards";
    public static final String UO_PAGE_IDENTIFIER_WALLET_CARD_DETAIL = "My Wallet Card Detail";
    public static final String UO_PAGE_IDENTIFIER_WALLET_LANDING = "My Wallet Landing";
    public static final String UO_PAGE_IDENTIFIER_FORGOT_PASSWORD = "Forgot Password";
    public static final String UO_PAGE_IDENTIFIER_SIGN_IN = "Sign In";
    public static final String UO_PAGE_IDENTIFIER_ADDRESSES = "Addresses";
    public static final String UO_PAGE_IDENTIFIER_UPDATE_ADDRESS = "Update Address";
    public static final String UO_PAGE_IDENTIFIER_PROFILE = "Profile";
    public static final String UO_PAGE_IDENTIFIER_ASSIGN_PURCHASES = "Assign Purchases";

    private static final String KEY_PROPERTY = "prop";
    private static final String KEY_ENVIRONMENT_VARIABLE = "eVar";
    private static final String KEY_CHANNEL = "channel";
    public static final String KEY_EVENTS = "events";
    public static final String KEY_USER_SHIPPING = "user_shippingamount";
    public static final String KEY_USER_TAXES = "user_taxesamount";
    public static final String KEY_USER_TOTAL = "user_totalamount";
    public static final String KEY_USER_DELIVERY = "user_deliverymethod";
    public static final String KEY_SESSION_ID = "session_id";
    public static final String KEY_OFFER_CODE = "Offer Code";
    public static final String KEY_USER_SUBTOTAL = "user_subtotalamount";

    public static final String KEY_LEADTYPE = "user_leadtype";
    public static final String KEY_EVENT_NAME = "event_name";
    private static final String VALUE_EVENT = "event";
    public static final String VALUE_CONFIRMATION_PAGE = "CONFIRMATION PAGE";


    public static final String KEY_PROP5_CONTENT_SUB_2 = "user_pageid";

    public static void initAdobeAnalytics() {
        Config.setContext(UniversalOrlandoApplication.getAppContext());
        Config.setDebugLogging(BuildConfig.LOGGING_ENABLED);
    }

    public static class Builder {
        private final Map<String, Object> mData;

        public Builder() {
            mData = new HashMap<>();
        }

        public Builder setProperty(int propNum, String value) {
            mData.put(KEY_PROPERTY + propNum, value);
            return this;
        }

        public Builder setEnvironmentVar(int eVarNum, String value) {
            mData.put(KEY_ENVIRONMENT_VARIABLE + eVarNum, value);
            return this;
        }

        public Builder setEventVar(int eventNum) {
            mData.put(KEY_EVENTS, VALUE_EVENT + eventNum);
            return this;
        }

        public Builder setKeyAndValue(String key, String value) {
            mData.put(key, value);
            return this;
        }

        public Builder addExtraData(Map<String, Object> extraData) {
            mData.putAll(extraData);
            return this;
        }

        public Map<String, Object> build() {
            return mData;
        }
    }

    public static void trackPageView(
            String contentGroup,
            String contentFocus,
            String contentSub1,
            String contentSub2,
            String propertyName,
            String attractionName,
            Map<String, Object> extraData) {

        // Create the page name
        String pageName = CHANNEL_APP;
        if (contentFocus != null && !contentFocus.isEmpty()) {
            pageName += " | " + contentFocus;
        }
        if (contentSub1 != null && !contentSub1.isEmpty()) {
            pageName += " | " + contentSub1;
        }

        if (contentSub2 != null && !contentSub2.isEmpty()) {
            pageName += " | " + contentSub2;
        }


        // Set static fields
        Builder b = new Builder();
        b.setKeyAndValue(KEY_CHANNEL, CHANNEL_APP);
        b.setProperty(10, CHANNEL_APP);
        b.setEnvironmentVar(8, CHANNEL_APP);
        b.setProperty(1, USER_GROUP_B2C);
        b.setEnvironmentVar(1, USER_GROUP_B2C);
        b.setKeyAndValue(KEY_EVENTS, "");

        // Set props and evars
        boolean isInPark = UniversalAppStateManager.getInstance().isInResortGeofence();
        boolean isUnknown = UniversalAppStateManager.getInstance().isLocationUnkown();
        String location = isUnknown ? LOCATION_STATE_UNKOWN_LOCATION : isInPark ? LOCATION_STATE_IN_PARK : LOCATION_STATE_OUT_OF_PARK;
        b.setProperty(55, location);
        b.setEnvironmentVar(55, location);

        if (contentGroup != null && !contentGroup.isEmpty()) {
            b.setProperty(2, contentGroup);
        }
        if (contentFocus != null && !contentFocus.isEmpty()) {
            b.setProperty(3, contentFocus);
        }
        if (contentSub1 != null && !contentSub1.isEmpty()) {
            b.setProperty(4, contentSub1);
        }
        if (contentSub2 != null && !contentSub2.isEmpty()) {
            b.setProperty(5, contentSub2);
        }
        if (propertyName != null && !propertyName.isEmpty()) {
            b.setProperty(11, propertyName);
            b.setEnvironmentVar(17, propertyName);
        }
        if (attractionName != null && !attractionName.isEmpty()) {
            b.setEnvironmentVar(74, attractionName);
        }
        if (CONTENT_GROUP_PROMOTIONS.equals(contentGroup)) {
            b.setProperty(44, PROPERTY_VALUE_AMEX);
        }

        // Add any extra fields
        if (extraData != null && extraData.size() > 0) {
            b.addExtraData(extraData);
        }

        // Track the page view
        Analytics.trackState(pageName, b.build());
    }

    public static void trackPageView(
            String contentGroup,
            String contentFocus,
            String contentSub1,
            String contentSub2,
            String propertyName,
            String attractionName,
            String specificPageName,
            Map<String, Object> extraData) {

        String pageName = null;

        // Create the page name
        if(null != specificPageName && !specificPageName.isEmpty()){
            pageName = specificPageName;
        } else {
            pageName = CHANNEL_APP_UO;
        }//AppTickets.UO
        if (contentFocus != null && !contentFocus.isEmpty()) {//| Tickets
            pageName += " | " + contentFocus;
        }
        if (contentSub1 != null && !contentSub1.isEmpty()) {//| App Tickets Store
            pageName += " | " + contentSub1;
        }
        if (contentSub2 != null && !contentSub2.isEmpty()) {//| Confirmation
            pageName += " | " + contentSub2;
        }


        // Set static fields
        Builder b = new Builder();
        b.setKeyAndValue(KEY_CHANNEL, CHANNEL_APP_UO);
        b.setProperty(10, CHANNEL_APP_UO);
        b.setEnvironmentVar(8, CHANNEL_APP_UO);
        b.setProperty(1, USER_GROUP_B2C);
        b.setEnvironmentVar(1, USER_GROUP_B2C);
        b.setKeyAndValue(KEY_EVENTS, "");

        // Set props and evars
        boolean isInPark = UniversalAppStateManager.getInstance().isInUniversalOrlandoResortGeofence();
        boolean isUnkown = UniversalAppStateManager.getInstance().isLocationUnkown();
        String location =  isUnkown ? LOCATION_STATE_UNKOWN_LOCATION : isInPark ? LOCATION_STATE_IN_PARK : LOCATION_STATE_OUT_OF_PARK;
        b.setProperty(55, location);
        b.setEnvironmentVar(55, location);

        if (contentGroup != null && !contentGroup.isEmpty()) {
            b.setProperty(2, contentGroup);
        }
        if (contentFocus != null && !contentFocus.isEmpty()) {
            b.setProperty(3, contentFocus);
        }
        if (contentSub1 != null && !contentSub1.isEmpty()) {
            b.setProperty(4, contentSub1);
        }
        if (contentSub2 != null && !contentSub2.isEmpty()) {
            b.setProperty(5, contentSub2);
        }
        if (propertyName != null && !propertyName.isEmpty()) {
            b.setProperty(11, propertyName);
            b.setEnvironmentVar(17, propertyName);
        }
        if (attractionName != null && !attractionName.isEmpty()) {
            b.setEnvironmentVar(74, attractionName);
        }
        if (CONTENT_GROUP_PROMOTIONS.equals(contentGroup)) {
            b.setProperty(44, PROPERTY_VALUE_AMEX);
        }

        // Add any extra fields
        if (extraData != null && extraData.size() > 0) {
            b.addExtraData(extraData);
        }

        // Track the page view
        Analytics.trackState(pageName, b.build());
    }

    /**
     * Used for tracking analytics for any tickets related pages.
     * Populates the global fields needed for each call as well
     * @param contentSub2
     * @param prop3
     * @param prop5
     * @param prop8
     * @param prop14
     * @param prop36
     * @param prop61
     * @param prop62
     * @param eventDataMapValue
     * @param isPurchaseMade
     * @param purchaseId
     * @param productName
     * @param products
     */
    public static void trackTicketsPageView(
            String contentSub2,
            String prop3,
            String prop5,
            String prop8,
            String prop14,
            String prop36,
            String prop61,
            String prop62,
            String eventDataMapValue,
            boolean isPurchaseMade,
            String purchaseId,
            String productName,
            TicketGroupOrder products,
            String contentGroup,
            String contentFocus,
            String propertyName) {

        Map<String, Object> extraDataMap = new HashMap<>();
        extraDataMap.put("&&events", eventDataMapValue);

        if(isPurchaseMade){
            extraDataMap.put("m.purchase", "1");

            if(purchaseId != null) {
                extraDataMap.put("m.purchaseid", purchaseId);
            }

            if(productName != null){
                extraDataMap.put("m.productname", productName);
            }
            extraDataMap.put("&&products", getExtrasStringForProducts(products));
        }

        AnalyticsUtils.Builder ticketsDatabuilder = new AnalyticsUtils.Builder().setProperty(1, AnalyticsUtils.USER_GROUP_B2C)
                .setEnvironmentVar(1, AnalyticsUtils.USER_GROUP_B2C)
                .setProperty(100, AnalyticsUtils.PROPERTY_NAME_ANDROID)
                .setProperty(2, AnalyticsUtils.PROPERTY_NAME_SALES)
                .setProperty(3, prop3)
                .setEnvironmentVar(3, AnalyticsUtils.PROPERTY_NAME_GENERAL)
                .setProperty(4, AnalyticsUtils.CONTENT_SUB_1_TICKETS)
                .setProperty(5, prop5)
                .setProperty(8, prop8)
                .setEnvironmentVar(8, AnalyticsUtils.CONTENT_GROUP_SITE_TICKETS)
                .setProperty(9, AnalyticsUtils.PROPERTY_NAME_UO)
                .setEnvironmentVar(11, AnalyticsUtils.PROPERTY_NAME_PARK_ADMISSION)
                .setProperty(13, AnalyticsUtils.PROPERTY_NAME_SELL_PRODUCTS)
                .setProperty(14, prop14)
                .setProperty(16, AnalyticsUtils.PROPERTY_NAME_GENERAL)
                .setEnvironmentVar(17, AnalyticsUtils.PROPERTY_NAME_PARKS)
                .setEnvironmentVar(18, AnalyticsUtils.PROPERTY_NAME_NEW_OR_RETURN_VISITOR)
                .setEnvironmentVar(19, AnalyticsUtils.PROPERTY_NAME_NEW_OR_RETURN_VISITOR)
                .setProperty(21, AnalyticsUtils.PROPERTY_NAME_PARK_ADMISSION)
                .setProperty(36, prop36)
                .setProperty(37, prop36)
                .setProperty(39, AnalyticsUtils.PROPERTY_NAME_NEW_OR_RETURN_VISITOR)
                .addExtraData(extraDataMap);


        if(null != prop61) {
            ticketsDatabuilder.setProperty(61, prop61);
        }

        if(null != prop62) {
            //these are set as the same value on purpose. analytics has a lot of values like this
            ticketsDatabuilder.setProperty(62, prop62).setProperty(64, prop62);
        }

        if(contentGroup == null){
            contentGroup = AnalyticsUtils.CONTENT_GROUP_SITE_TICKETS;
        }

        if(propertyName == null){
            propertyName = AnalyticsUtils.PROPERTY_NAME_PARKS_RESORT;
        }

        trackPageView(
                contentGroup,
                contentFocus,
                AnalyticsUtils.CONTENT_SUB_1_TICKETS,
                contentSub2,
                propertyName,
                null,
                AnalyticsUtils.CHANNEL_APP,
                ticketsDatabuilder.build());
    }

    private static String getExtrasStringForProducts(TicketGroupOrder orderTickets){
        StringBuffer productsBuf = new StringBuffer();
        if(orderTickets != null && orderTickets.getOrderItemGroups() != null) {
            if(orderTickets.getOrderItemGroups().getParkTicketGroups() != null) {

                for (ParkTicketGroups group : orderTickets.getOrderItemGroups().getParkTicketGroups()) {

                    if (productsBuf.length() > 0) {
                        productsBuf.append(",");
                    }

                    if(group.getAdultTickets() != null && group.getAdultTickets().getProduct() != null){
                        productsBuf.append(group.getAdultTickets().getProduct().getId())
                                .append(";")
                                .append(group.getAdultTickets().getQuantity())
                                .append(";")
                                .append(group.getAdultTickets().getTotalPrice());
                    }

                    if(group.getChildTickets() != null && group.getChildTickets().getProduct() != null){
                        productsBuf.append(group.getChildTickets().getProduct().getId())
                                .append(";")
                                .append(group.getChildTickets().getQuantity())
                                .append(";")
                                .append(group.getChildTickets().getTotalPrice());
                    }


                }
            }

            if(orderTickets.getOrderItemGroups().getExpressPassGroups() != null) {

                for (ExpressPassTicketGroups group : orderTickets.getOrderItemGroups().getExpressPassGroups()) {

                    if (productsBuf.length() > 0) {
                        productsBuf.append(",");
                    }

                    if(group.getProduct() != null){
                        productsBuf.append(group.getProduct().getId())
                                .append(";")
                                .append(group.getQuantity())
                                .append(";")
                                .append(group.getTotalPrice());
                    }
                }
            }
            return "ticket;" + productsBuf.toString();
        }

        return "";
    }

    public static void trackEvent(
            String attractionName,
            String eventName,
            Integer eventNum,
            Map<String, Object> extraData) {

        // Create the page name
        String actionName = CHANNEL_APP_UO + " -";
        if (attractionName != null && !attractionName.isEmpty()) {
            actionName += " " + attractionName;
        }
        if (eventName != null && !eventName.isEmpty()) {
            actionName += " " + eventName;
        }

        // Set props and evars
        Builder b = new Builder();

        boolean isInPark = UniversalAppStateManager.getInstance().isInResortGeofence();
        boolean isUnknown = UniversalAppStateManager.getInstance().isLocationUnkown();
        String location = isUnknown ? LOCATION_STATE_UNKOWN_LOCATION : isInPark ? LOCATION_STATE_IN_PARK : LOCATION_STATE_OUT_OF_PARK;
        b.setProperty(55, location);
        b.setEnvironmentVar(55, location);

        if (eventNum != null) {
            b.setEventVar(eventNum);
        }
        if (attractionName != null && !attractionName.isEmpty()) {
            b.setEnvironmentVar(74, attractionName);
        }

        // Add any extra fields
        if (extraData != null && extraData.size() > 0) {
            b.addExtraData(extraData);
        }

        // Track the event
        Analytics.trackAction(actionName, b.build());
    }

    public static void sendQueuedAnalytics() {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "sendQueuedAnalytics: Analytics.getQueueSize() = " + Analytics.getQueueSize());
        }

        if (Analytics.getQueueSize() > 0) {
            Analytics.sendQueuedHits();
        }
    }

    /**
     * Start collecting lifecycle data; called in onResume of the app's main activity as per
     * Adobe's site
     * https://marketing.adobe.com/resources/help/en_US/mobile/android/dev_qs.html
     * @param activity main activity of the application
     */
    public static void collectLifecycleData(FragmentActivity activity) {
        Config.collectLifecycleData(activity);
    }

    /**
     * Pause collecting lifecycle data
     */
    public static void pauseCollectingLifecycleData() {
        Config.pauseCollectingLifecycleData();
    }

    public static String getPropertyName(Venue venue) {

        long venueId = venue != null ? venue.getId() : -1l;

        if (venueId == VenuesTable.VAL_VENUE_ID_ISLANDS_OF_ADVENTURE) {
            return AnalyticsUtils.PROPERTY_NAME_IOA;
        } else if (venueId == VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_FLORIDA) {
            return AnalyticsUtils.PROPERTY_NAME_USF;
        } else if (venueId == VenuesTable.VAL_VENUE_ID_CITY_WALK_ORLANDO) {
            return AnalyticsUtils.PROPERTY_NAME_CW;
        } else if (venueId == VenuesTable.VAL_VENUE_ID_CITY_WALK_HOLLYWOOD) {
            return AnalyticsUtils.PROPERTY_NAME_CWH;
        } else if (venueId == VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD) {
            return AnalyticsUtils.PROPERTY_NAME_USH;
        } else {
            return (venue != null ? venue.getDisplayName() : null);
        }
    }

    public static String getPoiTypeIdName(int poiTypeId) {

        switch (poiTypeId) {
            case PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE:
                return CONTENT_SUB_1_RIDES;
            case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW:
                return CONTENT_SUB_1_SHOWS;
            case PointsOfInterestTable.VAL_POI_TYPE_ID_DINING:
                return CONTENT_SUB_1_DINING;
            case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP:
                return CONTENT_SUB_1_SHOPS;
            case PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL:
                return CONTENT_FOCUS_HOTELS;
            default:
                return null;
        }
    }

    public static String getExploreTypeName(ExploreType exploreType) {
        if (exploreType == null) {
            return null;
        }
        switch (exploreType) {
            case ISLANDS_OF_ADVENTURE:
                return AnalyticsUtils.CONTENT_FOCUS_IOA;
            case UNIVERSAL_STUDIOS_FLORIDA:
                return AnalyticsUtils.CONTENT_FOCUS_USF;
            case CITY_WALK_ORLANDO:
                return AnalyticsUtils.CONTENT_FOCUS_CW;
            case UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_DINING:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING:
                return AnalyticsUtils.CONTENT_FOCUS_USH;
            case CITY_WALK_HOLLYWOOD:
                return AnalyticsUtils.CONTENT_FOCUS_CWH;
            case HOTELS:
                return AnalyticsUtils.CONTENT_FOCUS_HOTELS;
            case RIDES:
                return AnalyticsUtils.CONTENT_SUB_1_RIDES;
            case SHOWS:
                return AnalyticsUtils.CONTENT_SUB_1_SHOWS;
            case DINING:
                return AnalyticsUtils.CONTENT_SUB_1_DINING;
            case SHOPPING:
                return AnalyticsUtils.CONTENT_SUB_1_SHOPS;
            case RESTROOMS:
                return AnalyticsUtils.CONTENT_FOCUS_RESTROOMS;
            case LOCKERS:
                return AnalyticsUtils.CONTENT_FOCUS_LOCKERS;
            case GUEST_SERVICES_BOOTHS:
            case ATMS:
            case FIRST_AID:
            case LOST_AND_FOUND:
            case SERVICE_ANIMAL_REST_AREAS:
            case SMOKING_AREAS:
                return AnalyticsUtils.CONTENT_FOCUS_GUEST_SERVICES;
            case SHOPPING_EXPRESS_PASS:
                return AnalyticsUtils.CONTENT_FOCUS_EXPRESS_PASS;
            default:
                return null;
        }
    }

    public static String getGuestServicesName(ExploreType exploreType) {

        if (exploreType != null) {
            switch (exploreType) {
                case GUEST_SERVICES_BOOTHS:
                    return AnalyticsUtils.CONTENT_SUB_1_GUEST_SERVICES_BOOTHS;
                case ATMS:
                    return AnalyticsUtils.CONTENT_SUB_1_ATMS;
                case FIRST_AID:
                    return AnalyticsUtils.CONTENT_SUB_1_FIRST_AID;
                case LOST_AND_FOUND:
                    return AnalyticsUtils.CONTENT_SUB_1_LOST_AND_FOUND;
                case SERVICE_ANIMAL_REST_AREAS:
                    return AnalyticsUtils.CONTENT_SUB_1_SERVICE_ANIMAL_REST_AREAS;
                case SMOKING_AREAS:
                    return AnalyticsUtils.CONTENT_SUB_1_SMOKING_AREAS;
                default:
                    break;
            }
        }
        return null;
    }

    public static String getActivityTypeName(ExploreType exploreType) {

        if (exploreType != null) {
            switch (exploreType) {
                case RIDES:
                    return AnalyticsUtils.CONTENT_SUB_1_RIDES;
                case SHOWS:
                    return AnalyticsUtils.CONTENT_SUB_1_SHOWS;
                case DINING:
                    return AnalyticsUtils.CONTENT_SUB_1_DINING;
                case SHOPPING:
                    return AnalyticsUtils.CONTENT_SUB_1_SHOPS;
                default:
                    break;
            }
        }
        return null;
    }

}