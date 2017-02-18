/**
 * 
 */
package com.universalstudios.orlandoresort.model.persistence;

import android.provider.BaseColumns;

import com.universalstudios.orlandoresort.BuildConfig;

/**
 * List of all of the database table and column names needed to interact with the app database.
 *
 * @author Steven Byle
 */
public class UniversalOrlandoDatabaseTables {
    
    // Static filter sort column name
    public static final String COL_FILTER_SORT = "filter_sort";
    public static final String COL_USER_LATITUDE = "user_latitude";
    public static final String COL_USER_LONGITUDE = "user_longitude";

	/**
	 * List of all table names in the database
	 */
	public static final String[] DB_TABLE_NAMES = {
		ResponseQueueTable.TABLE_NAME,
		PointsOfInterestTable.TABLE_NAME,
		VenuesTable.TABLE_NAME,
		VenueLandsTable.TABLE_NAME,
		WaypointsTable.TABLE_NAME,
		PathsTable.TABLE_NAME,
		AlertsTable.TABLE_NAME,
		NewsTable.TABLE_NAME,
		VenueHoursTable.TABLE_NAME,
		ShowTimesTable.TABLE_NAME,
		EventTimesTable.TABLE_NAME,
		EventSeriesTable.TABLE_NAME,
		EventSeriesTimesTable.TABLE_NAME,
		OfferSeriesTable.TABLE_NAME,
		OffersTable.TABLE_NAME
	};

	private static final String createFullyQualifiedColumnName(String tableName, String columnName) {
		return new StringBuilder(tableName).append(".").append(columnName).toString();
	}
	private static final String createAliasColumnName(String tableName, String columnName) {
		return new StringBuilder(tableName).append("_").append(columnName).toString();
	}

	// DB column names for response queue table
	public static class ResponseQueueTable {
		public static final String TABLE_NAME = "table_response_queue";

		public static final String COL_ID = BaseColumns._ID;
		public static final String COL_SENDER_TAG = "sender_tag";
		public static final String COL_REQUEST_TAG = "request_tag";
		public static final String COL_TIME_OF_REQUEST = "time_of_request";
		public static final String COL_RESPONSE_OBJECT_FULL_CLASS_NAME = "response_object_full_class_name";
		public static final String COL_RESPONSE_OBJECT_JSON = "response_object_json";

		public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
		public static final String COL_FULL_SENDER_TAG = createFullyQualifiedColumnName(TABLE_NAME, COL_SENDER_TAG);
		public static final String COL_FULL_REQUEST_TAG = createFullyQualifiedColumnName(TABLE_NAME, COL_REQUEST_TAG);
		public static final String COL_FULL_TIME_OF_REQUEST = createFullyQualifiedColumnName(TABLE_NAME, COL_TIME_OF_REQUEST);
		public static final String COL_FULL_RESPONSE_OBJECT_FULL_CLASS_NAME = createFullyQualifiedColumnName(TABLE_NAME, COL_RESPONSE_OBJECT_FULL_CLASS_NAME);
		public static final String COL_FULL_RESPONSE_OBJECT_JSON = createFullyQualifiedColumnName(TABLE_NAME, COL_RESPONSE_OBJECT_JSON);

		public static final String COL_ALIAS_ID = createAliasColumnName(TABLE_NAME, COL_ID);
		public static final String COL_ALIAS_SENDER_TAG = createAliasColumnName(TABLE_NAME, COL_SENDER_TAG);
		public static final String COL_ALIAS_REQUEST_TAG = createAliasColumnName(TABLE_NAME, COL_REQUEST_TAG);
		public static final String COL_ALIAS_TIME_OF_REQUEST = createAliasColumnName(TABLE_NAME, COL_TIME_OF_REQUEST);
		public static final String COL_ALIAS_RESPONSE_OBJECT_FULL_CLASS_NAME = createAliasColumnName(TABLE_NAME, COL_RESPONSE_OBJECT_FULL_CLASS_NAME);
		public static final String COL_ALIAS_RESPONSE_OBJECT_JSON = createAliasColumnName(TABLE_NAME, COL_RESPONSE_OBJECT_JSON);
	}

	// DB column names for points of interest table
	public static class PointsOfInterestTable {
		public static final String TABLE_NAME = "table_points_of_interest";

		public static final String COL_ID = BaseColumns._ID;
		public static final String COL_POI_ID = "poi_id";
		public static final String COL_POI_TYPE_ID = "poi_type_id";
		public static final String COL_DISPLAY_NAME = "display_name";
		public static final String COL_WAIT_TIME = "wait_time";
		public static final String COL_SHOW_TIMES_JSON = "show_times_json";
		public static final String COL_SUB_TYPE_FLAGS = "sub_type_flags";
		public static final String COL_LIST_IMAGE_URL = "list_image_url";
		public static final String COL_THUMBNAIL_IMAGE_URL = "thumbnail_image_url";
		public static final String COL_VENUE_ID = "venue_id";
		public static final String COL_VENUE_LAND_ID = "venue_land_id";
		public static final String COL_LATITUDE = "latitude";
		public static final String COL_LONGITUDE = "longitude";
		public static final String COL_IS_ROUTABLE = "is_routable";
		public static final String COL_IS_FAVORITE = "is_favorite";
		public static final String COL_POI_HASH_CODE = "poi_hash_code";
		public static final String COL_POI_OBJECT_JSON = "poi_object_json";
		public static final String COL_TAGS = "tags";
		public static final String COL_MIN_RIDE_HEIGHT = "min_ride_height";
		public static final String COL_MAX_RIDE_HEIGHT = "max_ride_height";
		public static final String COL_MIN_PRICE = "min_price";
		public static final String COL_MAX_PRICE = "max_price";
		public static final String COL_OPTION_FLAGS = "option_flags";
		public static final String COL_OFFER_IDS = "offer_ids";
		public static final String COL_SORT_ORDER = "sort_order";

		public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
		public static final String COL_FULL_POI_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_POI_ID);
		public static final String COL_FULL_POI_TYPE_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_POI_TYPE_ID);
		public static final String COL_FULL_DISPLAY_NAME = createFullyQualifiedColumnName(TABLE_NAME, COL_DISPLAY_NAME);
		public static final String COL_FULL_WAIT_TIME = createFullyQualifiedColumnName(TABLE_NAME, COL_WAIT_TIME);
		public static final String COL_FULL_SHOW_TIMES_JSON = createFullyQualifiedColumnName(TABLE_NAME, COL_SHOW_TIMES_JSON);
		public static final String COL_FULL_SUB_TYPE_FLAGS = createFullyQualifiedColumnName(TABLE_NAME, COL_SUB_TYPE_FLAGS);
		public static final String COL_FULL_LIST_IMAGE_URL = createFullyQualifiedColumnName(TABLE_NAME, COL_LIST_IMAGE_URL);
		public static final String COL_FULL_THUMBNAIL_IMAGE_URL = createFullyQualifiedColumnName(TABLE_NAME, COL_THUMBNAIL_IMAGE_URL);
		public static final String COL_FULL_VENUE_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_VENUE_ID);
		public static final String COL_FULL_VENUE_LAND_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_VENUE_LAND_ID);
		public static final String COL_FULL_LATITUDE = createFullyQualifiedColumnName(TABLE_NAME, COL_LATITUDE);
		public static final String COL_FULL_LONGITUDE = createFullyQualifiedColumnName(TABLE_NAME, COL_LONGITUDE);
		public static final String COL_FULL_IS_ROUTABLE = createFullyQualifiedColumnName(TABLE_NAME, COL_IS_ROUTABLE);
		public static final String COL_FULL_IS_FAVORITE = createFullyQualifiedColumnName(TABLE_NAME, COL_IS_FAVORITE);
		public static final String COL_FULL_POI_HASH_CODE = createFullyQualifiedColumnName(TABLE_NAME, COL_POI_HASH_CODE);
		public static final String COL_FULL_POI_OBJECT_JSON = createFullyQualifiedColumnName(TABLE_NAME, COL_POI_OBJECT_JSON);
		public static final String COL_FULL_TAGS = createFullyQualifiedColumnName(TABLE_NAME, COL_TAGS);
		public static final String COL_FULL_MIN_RIDE_HEIGHT = createFullyQualifiedColumnName(TABLE_NAME, COL_MIN_RIDE_HEIGHT);
        public static final String COL_FULL_MAX_RIDE_HEIGHT = createFullyQualifiedColumnName(TABLE_NAME, COL_MAX_RIDE_HEIGHT);
        public static final String COL_FULL_MIN_PRICE = createFullyQualifiedColumnName(TABLE_NAME, COL_MIN_PRICE);
        public static final String COL_FULL_MAX_PRICE = createFullyQualifiedColumnName(TABLE_NAME, COL_MAX_PRICE);
        public static final String COL_FULL_OPTION_FLAGS = createFullyQualifiedColumnName(TABLE_NAME, COL_OPTION_FLAGS);
        public static final String COL_FULL_OFFER_IDS = createFullyQualifiedColumnName(TABLE_NAME, COL_OFFER_IDS);
        public static final String COL_FULL_SORT_ORDER = createFullyQualifiedColumnName(TABLE_NAME, COL_SORT_ORDER);
		
		public static final String COL_ALIAS_ID = createAliasColumnName(TABLE_NAME, COL_ID);
		public static final String COL_ALIAS_POI_ID = createAliasColumnName(TABLE_NAME, COL_POI_ID);
		public static final String COL_ALIAS_POI_TYPE_ID = createAliasColumnName(TABLE_NAME, COL_POI_TYPE_ID);
		public static final String COL_ALIAS_DISPLAY_NAME = createAliasColumnName(TABLE_NAME, COL_DISPLAY_NAME);
		public static final String COL_ALIAS_WAIT_TIME = createAliasColumnName(TABLE_NAME, COL_WAIT_TIME);
		public static final String COL_ALIAS_SHOW_TIMES_JSON = createAliasColumnName(TABLE_NAME, COL_SHOW_TIMES_JSON);
		public static final String COL_ALIAS_SUB_TYPE_FLAGS = createAliasColumnName(TABLE_NAME, COL_SUB_TYPE_FLAGS);
		public static final String COL_ALIAS_LIST_IMAGE_URL = createAliasColumnName(TABLE_NAME, COL_LIST_IMAGE_URL);
		public static final String COL_ALIAS_THUMBNAIL_IMAGE_URL = createAliasColumnName(TABLE_NAME, COL_THUMBNAIL_IMAGE_URL);
		public static final String COL_ALIAS_VENUE_ID = createAliasColumnName(TABLE_NAME, COL_VENUE_ID);
		public static final String COL_ALIAS_VENUE_LAND_ID = createAliasColumnName(TABLE_NAME, COL_VENUE_LAND_ID);
		public static final String COL_ALIAS_LATITUDE = createAliasColumnName(TABLE_NAME, COL_LATITUDE);
		public static final String COL_ALIAS_LONGITUDE = createAliasColumnName(TABLE_NAME, COL_LONGITUDE);
		public static final String COL_ALIAS_IS_ROUTABLE = createAliasColumnName(TABLE_NAME, COL_IS_ROUTABLE);
		public static final String COL_ALIAS_IS_FAVORITE = createAliasColumnName(TABLE_NAME, COL_IS_FAVORITE);
		public static final String COL_ALIAS_POI_HASH_CODE = createAliasColumnName(TABLE_NAME, COL_POI_HASH_CODE);
		public static final String COL_ALIAS_POI_OBJECT_JSON = createAliasColumnName(TABLE_NAME, COL_POI_OBJECT_JSON);
		public static final String COL_ALIAS_TAGS = createAliasColumnName(TABLE_NAME, COL_TAGS);
		public static final String COL_ALIAS_MIN_RIDE_HEIGHT = createAliasColumnName(TABLE_NAME, COL_MIN_RIDE_HEIGHT);
		public static final String COL_ALIAS_MAX_RIDE_HEIGHT = createAliasColumnName(TABLE_NAME, COL_MAX_RIDE_HEIGHT);
		public static final String COL_ALIAS_MIN_PRICE = createAliasColumnName(TABLE_NAME, COL_MIN_PRICE);
        public static final String COL_ALIAS_MAX_PRICE = createAliasColumnName(TABLE_NAME, COL_MAX_PRICE);
        public static final String COL_ALIAS_OPTION_FLAGS = createAliasColumnName(TABLE_NAME, COL_OPTION_FLAGS);
        public static final String COL_ALIAS_OFFER_IDS = createAliasColumnName(TABLE_NAME, COL_OFFER_IDS);
        public static final String COL_ALIAS_SORT_ORDER = createAliasColumnName(TABLE_NAME, COL_SORT_ORDER);

		// These values CANNOT be changed without a DB update, but CAN be added to
		public static final int VAL_POI_TYPE_ID_RIDE = 1;
		public static final int VAL_POI_TYPE_ID_DINING = 2;
		public static final int VAL_POI_TYPE_ID_SHOW = 3;
		public static final int VAL_POI_TYPE_ID_HOTEL = 4;
		public static final int VAL_POI_TYPE_ID_PARADE = 5;
		public static final int VAL_POI_TYPE_ID_RESTROOM = 6;
		public static final int VAL_POI_TYPE_ID_ATM = 7;
		public static final int VAL_POI_TYPE_ID_PARKING = 8;
		public static final int VAL_POI_TYPE_ID_SHOP = 9;
		public static final int VAL_POI_TYPE_ID_LOCKERS = 10;
		public static final int VAL_POI_TYPE_ID_SERVICE_ANIMAL_REST_AREA = 11;
		public static final int VAL_POI_TYPE_ID_SMOKING_AREA = 12;
		public static final int VAL_POI_TYPE_ID_PACKAGE_PICKUP = 13;
		public static final int VAL_POI_TYPE_ID_LOST_AND_FOUND_STATION = 14;
		public static final int VAL_POI_TYPE_ID_GUEST_SERVICES_BOOTH = 15;
		public static final int VAL_POI_TYPE_ID_FIRST_AID_STATION = 16;
		public static final int VAL_POI_TYPE_ID_CHARGING_STATION = 17;
		public static final int VAL_POI_TYPE_ID_PHONE_CARD_DISPENSER = 18;
		public static final int VAL_POI_TYPE_ID_ENTERTAINMENT = 19;
		public static final int VAL_POI_TYPE_ID_WATERPARK = 20;
		public static final int VAL_POI_TYPE_ID_EVENT = 21;
		public static final int VAL_POI_TYPE_ID_OFFER = 22;
		public static final int VAL_POI_TYPE_ID_GEN_LOCATION = 23;
		public static final int VAL_POI_TYPE_ID_GATEWAY = 24;
		public static final int VAL_POI_TYPE_ID_RENTALS = 25;

		// Bitwise flags for the sub type options, which are unique to all POIs
		// These values CANNOT be changed without a DB update, but CAN be added to
		public static final long VAL_SUB_TYPE_FLAG_RIDE_TYPE_THRILL= 1l << 0;
		public static final long VAL_SUB_TYPE_FLAG_RIDE_TYPE_VIDEO_3D_4D = 1l << 1;
		public static final long VAL_SUB_TYPE_FLAG_RIDE_TYPE_WATER = 1l << 2;
		public static final long VAL_SUB_TYPE_FLAG_RIDE_TYPE_KID_FRIENDLY = 1l << 3;
		public static final long VAL_SUB_TYPE_FLAG_SHOW_TYPE_PARADE = 1l << 4;
		public static final long VAL_SUB_TYPE_FLAG_SHOW_TYPE_ACTION = 1l << 5;
		public static final long VAL_SUB_TYPE_FLAG_SHOW_TYPE_MUSIC = 1l << 6;
		public static final long VAL_SUB_TYPE_FLAG_SHOW_TYPE_COMEDY = 1l << 7;
		public static final long VAL_SUB_TYPE_FLAG_DINING_SERVICE_BREAKFAST = 1l << 8;
		public static final long VAL_SUB_TYPE_FLAG_DINING_SERVICE_LUNCH = 1l << 9;
		public static final long VAL_SUB_TYPE_FLAG_DINING_SERVICE_DINNER = 1l << 10;
		public static final long VAL_SUB_TYPE_FLAG_DINING_SERVICE_CHARACTER_DINING = 1l << 11;
		public static final long VAL_SUB_TYPE_FLAG_DINING_TYPE_FINE_DINING = 1l << 12;
		public static final long VAL_SUB_TYPE_FLAG_DINING_TYPE_CASUAL_DINING = 1l << 13;
		public static final long VAL_SUB_TYPE_FLAG_DINING_TYPE_QUICK_SERVICE = 1l << 14;
		public static final long VAL_SUB_TYPE_FLAG_RESTROOM_TYPE_STANDARD = 1l << 15;
		public static final long VAL_SUB_TYPE_FLAG_RESTROOM_TYPE_FAMILY = 1l << 16;
		public static final long VAL_SUB_TYPE_FLAG_LOCKER_TYPE_RIDE = 1l << 17;
		public static final long VAL_SUB_TYPE_FLAG_LOCKER_TYPE_RENTAL = 1l << 18;
		public static final long VAL_SUB_TYPE_FLAG_SHOP_SELLS_EXPRESS_PASS = 1l << 19;
		public static final long VAL_SUB_TYPE_FLAG_DINING_SERVICE_SNACK = 1l << 20;
		public static final long VAL_SUB_TYPE_FLAG_SHOW_TYPE_CHARACTER = 1l << 21;
		public static final long VAL_SUB_TYPE_FLAG_RIDE_WATER_SUPER_THRILL = 1l << 22;
		public static final long VAL_SUB_TYPE_FLAG_RIDE_WATER_GROUP_THRILL = 1l << 23;
		public static final long VAL_SUB_TYPE_FLAG_RIDE_WATER_FAMILY_THRILL = 1l << 24;
		public static final long VAL_SUB_TYPE_FLAG_RIDE_WATER_EXTRAS = 1l << 25;
		public static final long VAL_SUB_TYPE_FLAG_SHOP_HAS_PACKAGE_PICKUP = 1l << 26;
		public static final long VAL_SUB_TYPE_FLAG_RENTAL_TYPE_ECV = 1l << 27;
		public static final long VAL_SUB_TYPE_FLAG_RENTAL_TYPE_STROLLER = 1l << 28;
		public static final long VAL_SUB_TYPE_FLAG_RENTAL_TYPE_WHEELCHAIR = 1l << 29;
		public static final long VAL_SUB_TYPE_FLAG_RENTAL_TYPE_ECV_ONLY = 1l << 30;
		public static final long VAL_SUB_TYPE_FLAG_DINING_TYPE_SNACKS = 1l << 31;
		public static final long VAL_SUB_TYPE_FLAG_SHOP_TYPE_APPAREL_ACCESSORIES = 1l << 32;
		public static final long VAL_SUB_TYPE_FLAG_SHOP_TYPE_COLLECTIBLES = 1l << 33;
		public static final long VAL_SUB_TYPE_FLAG_SHOP_TYPE_GAMES_NOVELTIES = 1l << 34;
		public static final long VAL_SUB_TYPE_FLAG_SHOP_TYPE_FOOD_SPECIALTIES = 1l << 35;
		public static final long VAL_SUB_TYPE_FLAG_ENTERTAINMENT_TYPE_NIGHT_SPOTS = 1l << 36;
		public static final long VAL_SUB_TYPE_FLAG_ENTERTAINMENT_TYPE_EXPERIENCES = 1l << 37;
		public static final long VAL_SUB_TYPE_FLAG_SHOP_TYPE_HEALTH_BEAUTY = 1l << 38;
		public static final long VAL_SUB_TYPE_FLAG_DINNING_COCA_COLA_FREESTYLE = 1l << 39;

		// Bitwise flags for the sub type options, which are unique to all POIs
        // These values CANNOT be changed without a DB update, but CAN be added to
		public static final long VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_ASSISTIVE_LISTENING = 1l << 1;
		public static final long VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_STANDARD_WHEELCHAIR = 1l << 2;
		public static final long VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_CLOSED_CAPTION = 1l << 3;
		public static final long VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_SIGN_LANGUAGE = 1l << 4;
		public static final long VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_PARENTAL_DISCRETION_ADVISED = 1l << 5;
		public static final long VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_WHEELCHAIR_MUST_TRANSFER = 1l << 6;
		public static final long VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_ANY_WHEELCHAIR = 1l << 7;
		public static final long VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_STATIONARY_SEATING = 1l << 8;
		public static final long VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_EXTRA_INFO = 1l << 9;
		public static final long VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_NO_UNATTENDED_CHILDREN = 1l << 10;
		public static final long VAL_OPTION_FLAG_ACCESSIBILITY_OPTION_LIFE_VEST_REQUIRED = 1l << 11;
		public static final long VAL_OPTION_FLAG_EXPRESS_PASS_ACCEPTED = 1l << 12;
		public static final long VAL_OPTION_FLAG_RIDE_SINGLE_RIDER_LINE = 1l << 13;
		public static final long VAL_OPTION_FLAG_RIDE_CHILD_SWAP = 1l << 14;
		public static final long VAL_OPTION_FLAG_DINING_UNIVERSAL_DINING_PLAN = 1l << 15;
		public static final long VAL_OPTION_FLAG_DINING_UNIVERSAL_DINING_PLAN_QUICK_SERVICE = 1l << 16;
		public static final long VAL_OPTION_FLAG_DINING_CHARACTER_DINING = 1l << 17;
		public static final long VAL_OPTION_FLAG_DINING_VEGETARIAN_HEALTHY_OPTIONS = 1l << 18;
	}

	// DB column names for venues table
	public static class VenuesTable {
		public static final String TABLE_NAME = "table_venues";

		public static final String COL_ID = BaseColumns._ID;
		public static final String COL_VENUE_ID = "venue_id";
		public static final String COL_DISPLAY_NAME = "display_name";
		public static final String COL_LONG_DESCRIPTION = "long_description";
		public static final String COL_HOURS_LIST_JSON = "hours_list_json";
		public static final String COL_VENUE_OBJECT_JSON = "venue_object_json";

		public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
		public static final String COL_FULL_VENUE_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_VENUE_ID);
		public static final String COL_FULL_DISPLAY_NAME = createFullyQualifiedColumnName(TABLE_NAME, COL_DISPLAY_NAME);
		public static final String COL_FULL_LONG_DESCRIPTION = createFullyQualifiedColumnName(TABLE_NAME, COL_LONG_DESCRIPTION);
		public static final String COL_FULL_HOURS_LIST_JSON = createFullyQualifiedColumnName(TABLE_NAME, COL_HOURS_LIST_JSON);
		public static final String COL_FULL_VENUE_OBJECT_JSON = createFullyQualifiedColumnName(TABLE_NAME, COL_VENUE_OBJECT_JSON);

		public static final String COL_ALIAS_ID = createAliasColumnName(TABLE_NAME, COL_ID);
		public static final String COL_ALIAS_VENUE_ID = createAliasColumnName(TABLE_NAME, COL_VENUE_ID);
		public static final String COL_ALIAS_DISPLAY_NAME = createAliasColumnName(TABLE_NAME, COL_DISPLAY_NAME);
		public static final String COL_ALIAS_LONG_DESCRIPTION = createAliasColumnName(TABLE_NAME, COL_LONG_DESCRIPTION);
		public static final String COL_ALIAS_HOURS_LIST_JSON = createAliasColumnName(TABLE_NAME, COL_HOURS_LIST_JSON);
		public static final String COL_ALIAS_VENUE_OBJECT_JSON = createAliasColumnName(TABLE_NAME, COL_VENUE_OBJECT_JSON);

		public static final long VAL_VENUE_ID_ISLANDS_OF_ADVENTURE = 10000;
		public static final long VAL_VENUE_ID_UNIVERSAL_STUDIOS_FLORIDA = 10010;
		public static final long VAL_VENUE_ID_CITY_WALK_ORLANDO = 10011;
		public static final long VAL_VENUE_ID_WET_N_WILD = 11837;
		public static final long VAL_VENUE_ID_VOLCANO_BAY = BuildConfig.VENUE_ID_VOLCANO_BAY;
		public static final long VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD = BuildConfig.VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD;
		public static final long VAL_VENUE_ID_CITY_WALK_HOLLYWOOD = BuildConfig.VENUE_ID_CITY_WALK_HOLLYWOOD;
	}

	// DB column names for venue lands table
	public static class VenueLandsTable {
		public static final String TABLE_NAME = "table_venue_lands";

		public static final String COL_ID = BaseColumns._ID;
		public static final String COL_VENUE_LAND_ID = "venue_land_id";
		public static final String COL_CONTAINING_VENUE_ID = "containing_venue_id";
		public static final String COL_DISPLAY_NAME = "display_name";
		public static final String COL_LONG_DESCRIPTION = "long_description";
		public static final String COL_VENUE_LAND_OBJECT_JSON = "venue_land_object_json";

		public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
		public static final String COL_FULL_VENUE_LAND_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_VENUE_LAND_ID);
		public static final String COL_FULL_CONTAINING_VENUE_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_CONTAINING_VENUE_ID);
		public static final String COL_FULL_DISPLAY_NAME = createFullyQualifiedColumnName(TABLE_NAME, COL_DISPLAY_NAME);
		public static final String COL_FULL_LONG_DESCRIPTION = createFullyQualifiedColumnName(TABLE_NAME, COL_LONG_DESCRIPTION);
		public static final String COL_FULL_VENUE_LAND_OBJECT_JSON = createFullyQualifiedColumnName(TABLE_NAME, COL_VENUE_LAND_OBJECT_JSON);

		public static final String COL_ALIAS_ID = createAliasColumnName(TABLE_NAME, COL_ID);
		public static final String COL_ALIAS_VENUE_LAND_ID = createAliasColumnName(TABLE_NAME, COL_VENUE_LAND_ID);
		public static final String COL_ALIAS_CONTAINING_VENUE_ID = createAliasColumnName(TABLE_NAME, COL_CONTAINING_VENUE_ID);
		public static final String COL_ALIAS_DISPLAY_NAME = createAliasColumnName(TABLE_NAME, COL_DISPLAY_NAME);
		public static final String COL_ALIAS_LONG_DESCRIPTION = createAliasColumnName(TABLE_NAME, COL_LONG_DESCRIPTION);
		public static final String COL_ALIAS_VENUE_LAND_OBJECT_JSON = createAliasColumnName(TABLE_NAME, COL_VENUE_LAND_OBJECT_JSON);

		public static final long VAL_LAND_ID_HOLLYWOOD_UPPER_LOT = BuildConfig.LAND_ID_HOLLYWOOD_UPPER_LOT;
		public static final long VAL_LAND_ID_HOLLYWOOD_LOWER_LOT = BuildConfig.LAND_ID_HOLLYWOOD_LOWER_LOT;
	}

	// DB column names for waypoints table
	public static class WaypointsTable {
		public static final String TABLE_NAME = "table_waypoints";

		public static final String COL_ID = BaseColumns._ID;
		public static final String COL_WAYPOINT_ID = "waypoint_id";
		public static final String COL_WAYPOINT_OBJECT_JSON = "waypoint_object_json";

		public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
		public static final String COL_FULL_WAYPOINT_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_WAYPOINT_ID);
		public static final String COL_FULL_WAYPOINT_OBJECT_JSON = createFullyQualifiedColumnName(TABLE_NAME, COL_WAYPOINT_OBJECT_JSON);

		public static final String COL_ALIAS_ID = createAliasColumnName(TABLE_NAME, COL_ID);
		public static final String COL_ALIAS_WAYPOINT_ID = createAliasColumnName(TABLE_NAME, COL_WAYPOINT_ID);
		public static final String COL_ALIAS_WAYPOINT_OBJECT_JSON = createAliasColumnName(TABLE_NAME, COL_WAYPOINT_OBJECT_JSON);
	}

	// DB column names for paths table
	public static class PathsTable {
		public static final String TABLE_NAME = "table_paths";

		public static final String COL_ID = BaseColumns._ID;
		public static final String COL_PATH_ID = "path_id";
		public static final String COL_STARTING_WAYPOINT_ID = "starting_waypoint_id";
		public static final String COL_ENDING_WAYPOINT_ID = "ending_waypoint_id";
		public static final String COL_DESTINATION_ID = "destination_waypoint_id";
		public static final String COL_INSTRUCTIONS = "instructions";
		public static final String COL_COMMENT = "comment";
		public static final String COL_NAVIGATION_IMAGE_URL = "navigation_image_url";
		public static final String COL_PATH_OBJECT_JSON = "path_object_json";

		public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
		public static final String COL_FULL_PATH_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_PATH_ID);
		public static final String COL_FULL_STARTING_WAYPOINT_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_STARTING_WAYPOINT_ID);
		public static final String COL_FULL_ENDING_WAYPOINT_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ENDING_WAYPOINT_ID);
		public static final String COL_FULL_DESTINATION_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_DESTINATION_ID);
		public static final String COL_FULL_INSTRUCTIONS = createFullyQualifiedColumnName(TABLE_NAME, COL_INSTRUCTIONS);
		public static final String COL_FULL_COMMENT = createFullyQualifiedColumnName(TABLE_NAME, COL_COMMENT);
		public static final String COL_FULL_NAVIGATION_IMAGE_URL = createFullyQualifiedColumnName(TABLE_NAME, COL_NAVIGATION_IMAGE_URL);
		public static final String COL_FULL_PATH_OBJECT_JSON = createFullyQualifiedColumnName(TABLE_NAME, COL_PATH_OBJECT_JSON);

		public static final String COL_ALIAS_ID = createAliasColumnName(TABLE_NAME, COL_ID);
		public static final String COL_ALIAS_PATH_ID = createAliasColumnName(TABLE_NAME, COL_PATH_ID);
		public static final String COL_ALIAS_STARTING_WAYPOINT_ID = createAliasColumnName(TABLE_NAME, COL_STARTING_WAYPOINT_ID);
		public static final String COL_ALIAS_ENDING_WAYPOINT_ID = createAliasColumnName(TABLE_NAME, COL_ENDING_WAYPOINT_ID);
		public static final String COL_ALIAS_DESTINATION_ID = createAliasColumnName(TABLE_NAME, COL_DESTINATION_ID);
		public static final String COL_ALIAS_INSTRUCTIONS = createAliasColumnName(TABLE_NAME, COL_INSTRUCTIONS);
		public static final String COL_ALIAS_COMMENT = createAliasColumnName(TABLE_NAME, COL_COMMENT);
		public static final String COL_ALIAS_NAVIGATION_IMAGE_URL = createAliasColumnName(TABLE_NAME, COL_NAVIGATION_IMAGE_URL);
		public static final String COL_ALIAS_PATH_OBJECT_JSON = createAliasColumnName(TABLE_NAME, COL_PATH_OBJECT_JSON);
	}

	// DB column names for alerts table
	public static class AlertsTable {
		public static final String TABLE_NAME = "table_alerts";

		public static final String COL_ID = BaseColumns._ID;
		public static final String COL_ALERT_ID_STRING = "alert_id_string";
		public static final String COL_ALERT_TYPE_ID = "alert_type";
		public static final String COL_POI_ID = "poi_id";
		public static final String COL_POI_NAME = "poi_name";
		public static final String COL_SHOW_TIME = "show_time";
		public static final String COL_NOTIFY_MIN_BEFORE = "notify_min_before";
		public static final String COL_NOTIFY_THRESHOLD_IN_MIN = "notify_threshold_in_min";
		public static final String COL_CREATED_DATE_IN_MILLIS = "created_date_in_millis";
		public static final String COL_ALERT_OBJECT_JSON = "alert_object_json";

		public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
		public static final String COL_FULL_ALERT_ID_STRING = createFullyQualifiedColumnName(TABLE_NAME, COL_ALERT_ID_STRING);
		public static final String COL_FULL_ALERT_TYPE_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ALERT_TYPE_ID);
		public static final String COL_FULL_POI_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_POI_ID);
		public static final String COL_FULL_POI_NAME = createFullyQualifiedColumnName(TABLE_NAME, COL_POI_NAME);
		public static final String COL_FULL_SHOW_TIME = createFullyQualifiedColumnName(TABLE_NAME, COL_SHOW_TIME);
		public static final String COL_FULL_NOTIFY_MIN_BEFORE = createFullyQualifiedColumnName(TABLE_NAME, COL_NOTIFY_MIN_BEFORE);
		public static final String COL_FULL_NOTIFY_THRESHOLD_IN_MIN = createFullyQualifiedColumnName(TABLE_NAME, COL_NOTIFY_THRESHOLD_IN_MIN);
		public static final String COL_FULL_CREATED_DATE_IN_MILLIS = createFullyQualifiedColumnName(TABLE_NAME, COL_CREATED_DATE_IN_MILLIS);
		public static final String COL_FULL_ALERT_OBJECT_JSON = createFullyQualifiedColumnName(TABLE_NAME, COL_ALERT_OBJECT_JSON);

		public static final String COL_ALIAS_ID = createAliasColumnName(TABLE_NAME, COL_ID);
		public static final String COL_ALIAS_ALERT_ID_STRING = createAliasColumnName(TABLE_NAME, COL_ALERT_ID_STRING);
		public static final String COL_ALIAS_ALERT_TYPE_ID = createAliasColumnName(TABLE_NAME, COL_ALERT_TYPE_ID);
		public static final String COL_ALIAS_POI_ID = createAliasColumnName(TABLE_NAME, COL_POI_ID);
		public static final String COL_ALIAS_POI_NAME = createAliasColumnName(TABLE_NAME, COL_POI_NAME);
		public static final String COL_ALIAS_SHOW_TIME = createAliasColumnName(TABLE_NAME, COL_SHOW_TIME);
		public static final String COL_ALIAS_NOTIFY_MIN_BEFORE = createAliasColumnName(TABLE_NAME, COL_NOTIFY_MIN_BEFORE);
		public static final String COL_ALIAS_NOTIFY_THRESHOLD_IN_MIN = createAliasColumnName(TABLE_NAME, COL_NOTIFY_THRESHOLD_IN_MIN);
		public static final String COL_ALIAS_CREATED_DATE_IN_MILLIS = createAliasColumnName(TABLE_NAME, COL_CREATED_DATE_IN_MILLIS);
		public static final String COL_ALIAS_ALERT_OBJECT_JSON = createAliasColumnName(TABLE_NAME, COL_ALERT_OBJECT_JSON);

		// These values CANNOT be changed without a DB update, but CAN be added to
		public static final int VAL_ALERT_TYPE_ID_WAIT_TIME = 1;
		public static final int VAL_ALERT_TYPE_ID_SHOW_TIME = 2;
		public static final int VAL_ALERT_TYPE_ID_RIDE_OPEN = 3;
	}

	// DB column names for news table
	public static class NewsTable {
		public static final String TABLE_NAME = "table_news";

		public static final String COL_ID = BaseColumns._ID;
		public static final String COL_NEWS_ID = "news_id";
		public static final String COL_MESSAGE_HEADING = "message_heading";
		public static final String COL_MESSAGE_BODY = "message_body";
		public static final String COL_START_DATE_IN_MILLIS = "start_date_in_millis";
		public static final String COL_EXPIRATION_DATE_IN_MILLIS = "expiration_date_in_millis";
		public static final String COL_HAS_BEEN_READ = "has_been_read";
		public static final String COL_NEWS_OBJECT_JSON = "news_object_json";

		public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
		public static final String COL_FULL_NEWS_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_NEWS_ID);
		public static final String COL_FULL_MESSAGE_HEADING = createFullyQualifiedColumnName(TABLE_NAME, COL_MESSAGE_HEADING);
		public static final String COL_FULL_MESSAGE_BODY = createFullyQualifiedColumnName(TABLE_NAME, COL_MESSAGE_BODY);
		public static final String COL_FULL_START_DATE_IN_MILLIS = createFullyQualifiedColumnName(TABLE_NAME, COL_START_DATE_IN_MILLIS);
		public static final String COL_FULL_EXPIRATION_DATE_IN_MILLIS = createFullyQualifiedColumnName(TABLE_NAME, COL_EXPIRATION_DATE_IN_MILLIS);
		public static final String COL_FULL_HAS_BEEN_READ = createFullyQualifiedColumnName(TABLE_NAME, COL_HAS_BEEN_READ);
		public static final String COL_FULL_NEWS_OBJECT_JSON = createFullyQualifiedColumnName(TABLE_NAME, COL_NEWS_OBJECT_JSON);

		public static final String COL_ALIAS_ID = createAliasColumnName(TABLE_NAME, COL_ID);
		public static final String COL_ALIAS_NEWS_ID = createAliasColumnName(TABLE_NAME, COL_NEWS_ID);
		public static final String COL_ALIAS_MESSAGE_HEADING = createAliasColumnName(TABLE_NAME, COL_MESSAGE_HEADING);
		public static final String COL_ALIAS_MESSAGE_BODY = createAliasColumnName(TABLE_NAME, COL_MESSAGE_BODY);
		public static final String COL_ALIAS_START_DATE_IN_MILLIS = createAliasColumnName(TABLE_NAME, COL_START_DATE_IN_MILLIS);
		public static final String COL_ALIAS_EXPIRATION_DATE_IN_MILLIS = createAliasColumnName(TABLE_NAME, COL_EXPIRATION_DATE_IN_MILLIS);
		public static final String COL_ALIAS_HAS_BEEN_READ = createAliasColumnName(TABLE_NAME, COL_HAS_BEEN_READ);
		public static final String COL_ALIAS_NEWS_OBJECT_JSON = createAliasColumnName(TABLE_NAME, COL_NEWS_OBJECT_JSON);
	}

	// DB column names for venue hours table
	public static class VenueHoursTable {
		public static final String TABLE_NAME = "table_venue_hours";

		public static final String COL_ID = BaseColumns._ID;
		public static final String COL_VENUE_ID = "venue_id";
		public static final String COL_DATE_IN_MILLIS = "date_in_millis";
		public static final String COL_OPEN_DATE_IN_MILLIS = "open_date_in_millis";
		public static final String COL_CLOSE_DATE_IN_MILLIS = "close_date_in_millis";

		public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
		public static final String COL_FULL_VENUE_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_VENUE_ID);
		public static final String COL_FULL_DATE_IN_MILLIS = createFullyQualifiedColumnName(TABLE_NAME, COL_DATE_IN_MILLIS);
		public static final String COL_FULL_OPEN_DATE_IN_MILLIS = createFullyQualifiedColumnName(TABLE_NAME, COL_OPEN_DATE_IN_MILLIS);
		public static final String COL_FULL_CLOSE_DATE_IN_MILLIS = createFullyQualifiedColumnName(TABLE_NAME, COL_CLOSE_DATE_IN_MILLIS);

		public static final String COL_ALIAS_ID = createAliasColumnName(TABLE_NAME, COL_ID);
		public static final String COL_ALIAS_VENUE_ID = createAliasColumnName(TABLE_NAME, COL_VENUE_ID);
		public static final String COL_ALIAS_DATE_IN_MILLIS = createAliasColumnName(TABLE_NAME, COL_DATE_IN_MILLIS);
		public static final String COL_ALIAS_OPEN_DATE_IN_MILLIS = createAliasColumnName(TABLE_NAME, COL_OPEN_DATE_IN_MILLIS);
		public static final String COL_ALIAS_CLOSE_DATE_IN_MILLIS = createAliasColumnName(TABLE_NAME, COL_CLOSE_DATE_IN_MILLIS);
	}
	
	// DB column names for show times table
	public static class ShowTimesTable {
	    public static final String TABLE_NAME = "table_show_times";
	    
	    public static final String COL_ID = BaseColumns._ID;
	    public static final String COL_POI_ID = "poi_id";
	    public static final String COL_SHOW_TIME = "show_time";
	    public static final String COL_TIME_TYPE = "time_type";
	    public static final String COL_POI_TYPE_ID = "poi_type_id";
	    
	    public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
	    public static final String COL_FULL_POI_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_POI_ID);
	    public static final String COL_FULL_SHOW_TIME = createFullyQualifiedColumnName(TABLE_NAME, COL_SHOW_TIME);
	    public static final String COL_FULL_TIME_TYPE = createFullyQualifiedColumnName(TABLE_NAME, COL_TIME_TYPE);
	    public static final String COL_FULL_POI_TYPE_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_POI_TYPE_ID);
	    
	    public static final String COL_ALIAS_ID = createAliasColumnName(TABLE_NAME, COL_ID);
	    public static final String COL_ALIAS_POI_ID = createAliasColumnName(TABLE_NAME, COL_POI_ID);
	    public static final String COL_ALIAS_SHOW_TIME = createAliasColumnName(TABLE_NAME, COL_SHOW_TIME);
	    public static final String COL_ALIAS_TIME_TYPE = createAliasColumnName(TABLE_NAME, COL_TIME_TYPE);
	    public static final String COL_ALIAS_POI_TYPE_ID = createAliasColumnName(TABLE_NAME, COL_POI_TYPE_ID);
	    
	    public static final int TIME_TYPE_START_TIME = 0;
	    public static final int TIME_TYPE_END_TIME = 1;
	}
	
	// DB column names for event times table
	public static class EventTimesTable {
	    public static final String TABLE_NAME="table_event_times";
	    
	    public static final String COL_ID = BaseColumns._ID;
	    public static final String COL_POI_ID = "poi_id";
	    public static final String COL_POI_TYPE_ID = "poi_type_id";
	    public static final String COL_START_DATE = "start_date";
	    public static final String COL_END_DATE = "end_date";
		public static final String COL_FLAGS = "flags";

	    public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
        public static final String COL_FULL_POI_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_POI_ID);
        public static final String COL_FULL_POI_TYPE_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_POI_TYPE_ID);
        public static final String COL_FULL_START_DATE = createFullyQualifiedColumnName(TABLE_NAME, COL_START_DATE);
        public static final String COL_FULL_END_DATE = createFullyQualifiedColumnName(TABLE_NAME, COL_END_DATE);
		public static final String COL_FULL_FLAGS = createFullyQualifiedColumnName(TABLE_NAME, COL_FLAGS);

        public static final String COL_ALIAS_ID = createAliasColumnName(TABLE_NAME, COL_ID);
        public static final String COL_ALIAS_POI_ID = createAliasColumnName(TABLE_NAME, COL_POI_ID);
        public static final String COL_ALIAS_POI_TYPE_ID = createAliasColumnName(TABLE_NAME, COL_POI_TYPE_ID);
        public static final String COL_ALIAS_START_DATE = createAliasColumnName(TABLE_NAME, COL_START_DATE);
        public static final String COL_ALIAS_END_DATE = createAliasColumnName(TABLE_NAME, COL_END_DATE);
		public static final String COL_ALIAS_FLAGS = createAliasColumnName(TABLE_NAME, COL_FLAGS);

		public static final String UPDATE_ADD_FLAGS = "ALTER TABLE " + TABLE_NAME +
				" ADD COLUMN " + COL_FLAGS + " INTEGER";
	}
	
	// DB column names for event series table
	public static class EventSeriesTable {
	    public static final String TABLE_NAME = "table_event_series";
	    
	    public static final String COL_ID = BaseColumns._ID;
	    public static final String COL_EVENT_SERIES_ID = "event_series_id";
	    public static final String COL_DISPLAY_NAME = "display_name";
	    public static final String COL_VENUE_ID = "venue_id";
	    public static final String COL_LIST_IMAGE_URL = "list_image_url";
        public static final String COL_THUMBNAIL_IMAGE_URL = "thumbnail_image_url";
        public static final String COL_EVENT_IDS = "event_ids";
        public static final String COL_ATTRACTION_IDS = "attraction_ids";
		public static final String COL_FLAGS = "flags";
        public static final String COL_EVENT_SERIES_OBJECT_JSON = "event_series_object_json";
        public static final String COL_SORT_ORDER = "sort_order";
	    
	    public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
	    public static final String COL_FULL_EVENT_SERIES_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_EVENT_SERIES_ID);
	    public static final String COL_FULL_DISPLAY_NAME = createFullyQualifiedColumnName(TABLE_NAME, COL_DISPLAY_NAME);
	    public static final String COL_FULL_VENUE_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_VENUE_ID);
	    public static final String COL_FULL_LIST_IMAGE_URL = createFullyQualifiedColumnName(TABLE_NAME, COL_LIST_IMAGE_URL);
        public static final String COL_FULL_THUMBNAIL_IMAGE_URL = createFullyQualifiedColumnName(TABLE_NAME, COL_THUMBNAIL_IMAGE_URL);
        public static final String COL_FULL_EVENT_IDS = createFullyQualifiedColumnName(TABLE_NAME, COL_EVENT_IDS);
        public static final String COL_FULL_ATTRACTION_IDS = createFullyQualifiedColumnName(TABLE_NAME, COL_ATTRACTION_IDS);
        public static final String COL_FULL_EVENT_SERIES_OBJECT_JSON = createFullyQualifiedColumnName(TABLE_NAME, COL_EVENT_SERIES_OBJECT_JSON);
        public static final String COL_FULL_SORT_ORDER = createFullyQualifiedColumnName(TABLE_NAME, COL_SORT_ORDER);
		public static final String COL_FULL_FLAGS = createFullyQualifiedColumnName(TABLE_NAME, COL_FLAGS);

	    public static final String COL_ALIAS_ID = createAliasColumnName(TABLE_NAME, COL_ID);
	    public static final String COL_ALIAS_EVENT_SERIES_ID = createAliasColumnName(TABLE_NAME, COL_EVENT_SERIES_ID);
	    public static final String COL_ALIAS_DISPLAY_NAME = createAliasColumnName(TABLE_NAME, COL_DISPLAY_NAME);
	    public static final String COL_ALIAS_VENUE_ID = createAliasColumnName(TABLE_NAME, COL_VENUE_ID);
	    public static final String COL_ALIAS_LIST_IMAGE_URL = createAliasColumnName(TABLE_NAME, COL_LIST_IMAGE_URL);
        public static final String COL_ALIAS_THUMBNAIL_IMAGE_URL = createAliasColumnName(TABLE_NAME, COL_THUMBNAIL_IMAGE_URL);
        public static final String COL_ALIAS_EVENT_IDS = createAliasColumnName(TABLE_NAME, COL_EVENT_IDS);
        public static final String COL_ALIAS_EVENT_SERIES_OBJECT_JSON = createAliasColumnName(TABLE_NAME, COL_EVENT_SERIES_OBJECT_JSON);
        public static final String COL_ALIAS_ATTRACTION_IDS = createAliasColumnName(TABLE_NAME, COL_ATTRACTION_IDS);
        public static final String COL_ALIAS_SORT_ORDER = createAliasColumnName(TABLE_NAME, COL_SORT_ORDER);
		public static final String COL_ALIAS_FLAGS = createAliasColumnName(TABLE_NAME, COL_FLAGS);
	}
	
	// DB column names for event times table
    public static class EventSeriesTimesTable {
        public static final String TABLE_NAME= "table_event_series_times";
        
        public static final String COL_ID = BaseColumns._ID;
        public static final String COL_EVENT_SERIES_ID = "event_series_id";
        public static final String COL_START_DATE = "start_date";
        public static final String COL_END_DATE = "end_date";
        
        public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
        public static final String COL_FULL_EVENT_SERIES_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_EVENT_SERIES_ID);
        public static final String COL_FULL_START_DATE = createFullyQualifiedColumnName(TABLE_NAME, COL_START_DATE);
        public static final String COL_FULL_END_DATE = createFullyQualifiedColumnName(TABLE_NAME, COL_END_DATE);
        
        public static final String COL_ALIAS_ID = createAliasColumnName(TABLE_NAME, COL_ID);
        public static final String COL_ALIAS_EVENT_SERIES_ID = createAliasColumnName(TABLE_NAME, COL_EVENT_SERIES_ID);
        public static final String COL_ALIAS_START_DATE = createAliasColumnName(TABLE_NAME, COL_START_DATE);
        public static final String COL_ALIAS_END_DATE = createAliasColumnName(TABLE_NAME, COL_END_DATE);
    }
    
    public static class OfferSeriesTable {
        public static final String TABLE_NAME = "table_offer_series";
        
        public static final String COL_ID = BaseColumns._ID;
        public static final String COL_OFFER_SERIES_ID = "offer_series_id";
        public static final String COL_VENDOR = "vendor";
        public static final String COL_DISPLAY_NAME = "display_name";
        public static final String COL_LATITUDE = "latitude";
        public static final String COL_LONGITUDE = "longitude";
        public static final String COL_OFFER_SERIES_OBJECT_JSON = "offer_series_object_json";
        
        public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
        public static final String COL_FULL_OFFER_SERIES_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_OFFER_SERIES_ID);
        public static final String COL_FULL_VENDOR = createFullyQualifiedColumnName(TABLE_NAME, COL_VENDOR);
        public static final String COL_FULL_DISPLAY_NAME = createFullyQualifiedColumnName(TABLE_NAME, COL_DISPLAY_NAME);
        public static final String COL_FULL_LATITUDE = createFullyQualifiedColumnName(TABLE_NAME, COL_LATITUDE);
        public static final String COL_FULL_LONGITUDE = createFullyQualifiedColumnName(TABLE_NAME, COL_LONGITUDE);
        public static final String COL_FULL_OFFER_SERIES_OBJECT_JSON  = createFullyQualifiedColumnName(TABLE_NAME, COL_OFFER_SERIES_OBJECT_JSON);
        
        public static final String COL_ALIAS_ID = createAliasColumnName(TABLE_NAME, COL_ID);
        public static final String COL_ALIAS_OFFER_SERIES_ID = createAliasColumnName(TABLE_NAME, COL_OFFER_SERIES_ID);
        public static final String COL_ALIAS_VENDOR = createAliasColumnName(TABLE_NAME, COL_VENDOR);
        public static final String COL_ALIAS_DISPLAY_NAME = createAliasColumnName(TABLE_NAME, COL_DISPLAY_NAME);
        public static final String COL_ALIAS_LATITUDE = createAliasColumnName(TABLE_NAME, COL_LATITUDE);
        public static final String COL_ALIAS_LONGITUDE = createAliasColumnName(TABLE_NAME, COL_LONGITUDE);
        public static final String COL_ALIAS_OFFER_SERIES_OBJECT_JSON = createAliasColumnName(TABLE_NAME, COL_OFFER_SERIES_OBJECT_JSON);
    }
    
    public static class OffersTable {
        public static final String TABLE_NAME = "table_offers";
        
        public static final String COL_ID = BaseColumns._ID;
        public static final String COL_OFFER_ID = "offer_id";
        public static final String COL_VENDOR = "vendor";
        public static final String COL_VENUE_ID = "venue_id";
        public static final String COL_OFFER_TYPE = "offer_type";
        public static final String COL_DISPLAY_NAME = "display_name";
        public static final String COL_SHORT_DESCRIPTION = "short_description";
        public static final String COL_THUMBNAIL_IMAGE_URL = "thumbnail_image_url";
        public static final String COL_END_DATE = "end_date";
        public static final String COL_LATITUDE = "latitude";
        public static final String COL_LONGITUDE = "longitude";
        public static final String COL_OFFER_OBJECT_JSON = "offer_object_json";
        
        public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
        public static final String COL_FULL_OFFER_ID  = createFullyQualifiedColumnName(TABLE_NAME, COL_OFFER_ID);
        public static final String COL_FULL_VENDOR = createFullyQualifiedColumnName(TABLE_NAME, COL_VENDOR);
        public static final String COL_FULL_VENUE_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_VENUE_ID);
        public static final String COL_FULL_OFFER_TYPE = createFullyQualifiedColumnName(TABLE_NAME, COL_OFFER_TYPE);
        public static final String COL_FULL_DISPLAY_NAME = createFullyQualifiedColumnName(TABLE_NAME, COL_DISPLAY_NAME);
        public static final String COL_FULL_SHORT_DESCRIPTION = createFullyQualifiedColumnName(TABLE_NAME, COL_SHORT_DESCRIPTION);
        public static final String COL_FULL_THUMBNAIL_IMAGE_URL = createFullyQualifiedColumnName(TABLE_NAME, COL_THUMBNAIL_IMAGE_URL);
        public static final String COL_FULL_END_DATE = createFullyQualifiedColumnName(TABLE_NAME, COL_END_DATE);
        public static final String COL_FULL_LATITUDE = createFullyQualifiedColumnName(TABLE_NAME, COL_LATITUDE);
        public static final String COL_FULL_LONGITUDE = createFullyQualifiedColumnName(TABLE_NAME, COL_LONGITUDE);
        public static final String COL_FULL_OFFER_OBJECT_JSON = createFullyQualifiedColumnName(TABLE_NAME, COL_OFFER_OBJECT_JSON);
        
        public static final String COL_ALIAS_ID = createAliasColumnName(TABLE_NAME, COL_ID);
        public static final String COL_ALIAS_OFFER_ID  = createAliasColumnName(TABLE_NAME, COL_OFFER_ID);
        public static final String COL_ALIAS_VENDOR = createAliasColumnName(TABLE_NAME, COL_VENDOR);
        public static final String COL_ALIAS_VENUE_ID = createAliasColumnName(TABLE_NAME, COL_VENUE_ID);
        public static final String COL_ALIAS_OFFER_TYPE = createAliasColumnName(TABLE_NAME, COL_OFFER_TYPE);
        public static final String COL_ALIAS_DISPLAY_NAME = createAliasColumnName(TABLE_NAME, COL_DISPLAY_NAME);
        public static final String COL_ALIAS_SHORT_DESCRIPTION = createAliasColumnName(TABLE_NAME, COL_SHORT_DESCRIPTION);
        public static final String COL_ALIAS_THUMBNAIL_IMAGE_URL = createAliasColumnName(TABLE_NAME, COL_THUMBNAIL_IMAGE_URL);
        public static final String COL_ALIAS_END_DATE = createAliasColumnName(TABLE_NAME, COL_END_DATE);
        public static final String COL_ALIAS_LATITUDE = createAliasColumnName(TABLE_NAME, COL_LATITUDE);
        public static final String COL_ALIAS_LONGITUDE = createAliasColumnName(TABLE_NAME, COL_LONGITUDE);
        public static final String COL_ALIAS_OFFER_OBJECT_JSON = createAliasColumnName(TABLE_NAME, COL_OFFER_OBJECT_JSON);
        
        public static final String VAL_VENDOR_AMEX = "amex";
        public static final String VAL_VENDOR_USH = "ush";
    }

	public static class InteractiveExperiencesTable {
		public static final String TABLE_NAME = "table_interactive_experiences";

		public static final String COL_ID = BaseColumns._ID;
		public static final String COL_INTERACTIVE_EXPERIENCE_ID = "interactive_experience_id";
		public static final String COL_DISPLAY_NAME = "experience_name";
		public static final String COL_INTERACTIVE_EXPERIENCE_OBJECT_JSON = "interactive_experience_json";

		public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
		public static final String COL_FULL_INTERACTIVE_EXPERIENCE_ID  = createFullyQualifiedColumnName(TABLE_NAME, COL_INTERACTIVE_EXPERIENCE_ID);
		public static final String COL_FULL_DISPLAY_NAME = createFullyQualifiedColumnName(TABLE_NAME, COL_DISPLAY_NAME);
		public static final String COL_FULL_INTERACTIVE_EXPERIENCE_OBJECT_JSON = createFullyQualifiedColumnName(TABLE_NAME, COL_INTERACTIVE_EXPERIENCE_OBJECT_JSON);


		public static final String COL_ALIAS_ID = createAliasColumnName(TABLE_NAME, COL_ID);
		public static final String COL_ALIAS_INTERACTIVE_EXPERIENCE_ID  = createAliasColumnName(TABLE_NAME, COL_INTERACTIVE_EXPERIENCE_ID);
		public static final String COL_ALIAS_DISPLAY_NAME = createAliasColumnName(TABLE_NAME, COL_DISPLAY_NAME);
		public static final String COL_ALIAS_INTERACTIVE_EXPERIENCE_OBJECT_JSON = createAliasColumnName(TABLE_NAME, COL_INTERACTIVE_EXPERIENCE_OBJECT_JSON);

	}

	public static class InteractiveTriggersTable {
		public static final String TABLE_NAME = "interactive_triggers";

		public static final String COL_ID = BaseColumns._ID;
		public static final String COL_TRIGGER_ID = "trigger_id";
		public static final String COL_TRIGGER_JSON = "trigger_json";
		public static final String COL_ASSOC_EXP_ID = "assoc_exp_id";

		public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
				COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				COL_TRIGGER_ID + " INTEGER, " +
				COL_ASSOC_EXP_ID + " INTEGER, " +
				COL_TRIGGER_JSON + " TEXT) ";
	}

	public static class InteractiveBeaconsTable {
		public static final String TABLE_NAME = "interactive_beacons";

		public static final String COL_ID = BaseColumns._ID;
		public static final String COL_UUID = "beacon_uuid";
		public static final String COL_ASSOC_TRIGGER_ID = "assoc_trigger_id";
		public static final String COL_BEACON_JSON = "beacon_json";
		public static final String COL_MAJOR = "beacon_major";
		public static final String COL_MINOR = "beacon_minor";

		public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
				COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				COL_UUID + " TEXT, " +
				COL_ASSOC_TRIGGER_ID + " INTEGER, " +
				COL_MAJOR + " INTEGER, " +
				COL_MINOR + " INTEGER, " +
				COL_BEACON_JSON + " TEXT) ";
	}

	public static class PhotoFrameExperienceTable {
		public static final String TABLE_NAME = "table_photo_frame_experiences";

		public static final String COL_ID = BaseColumns._ID;
		public static final String COL_EXPERIENCE_ID = "photoframe_experience_id";
		public static final String COL_PHOTOFRAME_EXPERIENCE_JSON = "photoframe_experience_json";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_EXPERIENCE_ID + " INTEGER,"
                + COL_PHOTOFRAME_EXPERIENCE_JSON + " TEXT)";
	}


	// DB column names for Queues table
	public static class QueuesTable {
		public static final String TABLE_NAME = "table_queues";

		public static final String COL_ID = BaseColumns._ID;
		public static final String COL_QUEUE_ID = "queue_id";
		public static final String COL_QUEUE_ENTITY_ID = "queue_entity_id";
		public static final String COL_QUEUE_OBJECT_JSON = "queue_object_json";


		public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
		public static final String COL_FULL_QUEUE_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_QUEUE_ID);
		public static final String COL_FULL_QUEUE_ENTITY_ID= createFullyQualifiedColumnName(TABLE_NAME, COL_QUEUE_ENTITY_ID);
		public static final String COL_FULL_QUEUE_OBJECT_JSON = createFullyQualifiedColumnName(TABLE_NAME, COL_QUEUE_OBJECT_JSON);

	}

	public static class MobilePagesTable {
		public static final String TABLE_NAME = "table_mobile_pages";
		public static final String COL_ID = BaseColumns._ID;
		public static final String COL_IDENTIFIER = "identifier";
		public static final String COL_JSON = "object_json";

		public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
				+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_IDENTIFIER + " TEXT,"
				+ COL_JSON + " TEXT)";

	}


	// DB column names for Tickets table
	public static class TicketsAppointmentTable {
		public static final String TABLE_NAME = "table_appointment_tickets";

		public static final String COL_ID = BaseColumns._ID;
		public static final String COL_TICKET_APPOINTMENT_ID = "ticket_appointment_id";
		public static final String COL_APPOINTMENT_TIME_ID = "appointment_time_id";
		public static final String COL_QUEUE_ENTITY_ID = "queue_entity_id";
		public static final String COL_START_TIME = "start_time";
		public static final String COL_END_TIME = "end_time";
		public static final String COL_HAS_BEEN_READ = "has_been_read";
		public static final String COL_APPOINTMENT_OBJECT_JSON = "appointment_object_json";
		public static final String COL_APPOINTMENT_TICKET_OBJECT_JSON = "appointment_ticket_object_json";


		public static final String COL_FULL_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_ID);
		public static final String COL_FULL_TICKET_APPOINTMENT_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_TICKET_APPOINTMENT_ID);
		public static final String COL_FULL_APPOINTMENT_TIME_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_APPOINTMENT_TIME_ID);
		public static final String COL_FULL_QUEUE_ENTITY_ID = createFullyQualifiedColumnName(TABLE_NAME, COL_QUEUE_ENTITY_ID);
		public static final String COL_FULL_START_TIME = createFullyQualifiedColumnName(TABLE_NAME, COL_START_TIME);
		public static final String COL_FULL_END_TIME = createFullyQualifiedColumnName(TABLE_NAME, COL_END_TIME);
		public static final String COL_FULL_HAS_BEEN_READ = createFullyQualifiedColumnName(TABLE_NAME, COL_HAS_BEEN_READ);
		public static final String COL_FULL_APPOINTMENT_OBJECT_JSON = createFullyQualifiedColumnName(TABLE_NAME, COL_APPOINTMENT_OBJECT_JSON);
		public static final String COL_FULL_APPOINTMENT_TICKET_OBJECT_JSON = createFullyQualifiedColumnName(TABLE_NAME, COL_APPOINTMENT_TICKET_OBJECT_JSON);


		// These values CANNOT be changed without a DB update, but CAN be added to
		public static  int VAL_POI_TYPE_ID_RIDE = 1;
		public static  int VAL_POI_TYPE_ID_DINING = 2;
		public static  int VAL_POI_TYPE_ID_SHOW = 3;
		public static  int VAL_POI_TYPE_ID_HOTEL = 4;
		public static  int VAL_POI_TYPE_ID_PARADE = 5;
		public static  int VAL_POI_TYPE_ID_RESTROOM = 6;
		public static  int VAL_POI_TYPE_ID_ATM = 7;
		public static  int VAL_POI_TYPE_ID_PARKING = 8;
		public static  int VAL_POI_TYPE_ID_SHOP = 9;
		public static  int VAL_POI_TYPE_ID_LOCKERS = 10;
		public static  int VAL_POI_TYPE_ID_SERVICE_ANIMAL_REST_AREA = 11;
		public static  int VAL_POI_TYPE_ID_SMOKING_AREA = 12;
		public static  int VAL_POI_TYPE_ID_PACKAGE_PICKUP = 13;
		public static  int VAL_POI_TYPE_ID_LOST_AND_FOUND_STATION = 14;
		public static  int VAL_POI_TYPE_ID_GUEST_SERVICES_BOOTH = 15;
		public static  int VAL_POI_TYPE_ID_FIRST_AID_STATION = 16;
		public static  int VAL_POI_TYPE_ID_CHARGING_STATION = 17;
		public static  int VAL_POI_TYPE_ID_PHONE_CARD_DISPENSER = 18;
		public static  int VAL_POI_TYPE_ID_ENTERTAINMENT = 19;
		public static  int VAL_POI_TYPE_ID_WATERPARK = 20;
		public static  int VAL_POI_TYPE_ID_EVENT = 21;
		public static  int VAL_POI_TYPE_ID_OFFER = 22;
		public static  int VAL_POI_TYPE_ID_GEN_LOCATION = 23;
		public static  int VAL_POI_TYPE_ID_GATEWAY = 24;
		public static  int VAL_POI_TYPE_ID_RENTALS = 25;

	}

}