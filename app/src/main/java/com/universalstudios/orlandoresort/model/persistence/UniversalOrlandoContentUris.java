/**
 * 
 */
package com.universalstudios.orlandoresort.model.persistence;

import android.content.UriMatcher;
import android.net.Uri;

import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.AlertsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventSeriesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventSeriesTimesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventTimesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.InteractiveExperiencesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.NewsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OfferSeriesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OffersTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PathsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.ResponseQueueTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.ShowTimesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenueHoursTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenueLandsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.WaypointsTable;

import java.util.HashMap;
import java.util.Map;

/**
 * List of all of the content URIs needed to interact with the
 * {@link UniversalOrlandoContentProvider}
 * 
 * @author Steven Byle
 */
public class UniversalOrlandoContentUris {
	private static final String CUSTOM_TABLE_STATEMENT = "custom_table_statement";
	/**
	 * Special content URI to allow for joins and advanced queries via a content
	 * provider. There must be a new line ({@code "\n"}) after the first
	 * database table name added after the custom statement, the
	 * {@link UniversalOrlandoContentProvider} uses this to set the proper
	 * content URI to notify of changes
	 */
	public static final Uri CUSTOM_TABLE = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + CUSTOM_TABLE_STATEMENT);

	// Content URI for each database table
	public static final Uri RESPONSE_QUEUE = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + ResponseQueueTable.TABLE_NAME);
	public static final Uri POINTS_OF_INTEREST = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + PointsOfInterestTable.TABLE_NAME);
	public static final Uri VENUES = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + VenuesTable.TABLE_NAME);
	public static final Uri VENUE_LANDS = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + VenueLandsTable.TABLE_NAME);
	public static final Uri WAYPOINTS = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + WaypointsTable.TABLE_NAME);
	public static final Uri PATHS = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + PathsTable.TABLE_NAME);
	public static final Uri ALERTS = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + AlertsTable.TABLE_NAME);
	public static final Uri NEWS = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + NewsTable.TABLE_NAME);
	public static final Uri VENUE_HOURS = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + VenueHoursTable.TABLE_NAME);
	public static final Uri SHOW_TIMES = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + ShowTimesTable.TABLE_NAME);
	public static final Uri EVENT_TIMES = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + EventTimesTable.TABLE_NAME);
	public static final Uri EVENT_SERIES = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + EventSeriesTable.TABLE_NAME);
	public static final Uri EVENT_SERIES_TIMES = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + EventSeriesTimesTable.TABLE_NAME);
	public static final Uri OFFER_SERIES = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + OfferSeriesTable.TABLE_NAME);
	public static final Uri OFFERS = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + OffersTable.TABLE_NAME);
	public static final Uri INTERACTIVE_EXPERIENCES = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + InteractiveExperiencesTable.TABLE_NAME);
	public static final Uri INTERACTIVE_TRIGGERS = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + UniversalOrlandoDatabaseTables.InteractiveTriggersTable.TABLE_NAME);
	public static final Uri INTERACTIVE_BEACONS = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.TABLE_NAME);
	public static final Uri PHOTOFRAME_EXPERIENCE = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.TABLE_NAME);
	public static final Uri QUEUES = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + UniversalOrlandoDatabaseTables.QueuesTable.TABLE_NAME);
	public static final Uri TICKET_APPOINTMENTS = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + UniversalOrlandoDatabaseTables.TicketsAppointmentTable.TABLE_NAME);
	public static final Uri MOBILE_PAGES = Uri.parse("content://" + UniversalOrlandoContentProvider.AUTHORITY + "/" + UniversalOrlandoDatabaseTables.MobilePagesTable.TABLE_NAME);

	// URI codes for each content URI
	public static final int URI_CODE_CUSTOM_TABLE = 0;
	public static final int URI_CODE_RESPONSE_QUEUE = 1;
	public static final int URI_CODE_POINTS_OF_INTEREST = 2;
	public static final int URI_CODE_VENUES = 3;
	public static final int URI_CODE_VENUE_LANDS = 4;
	public static final int URI_CODE_WAYPOINTS = 5;
	public static final int URI_CODE_PATHS = 6;
	public static final int URI_CODE_ALERTS = 7;
	public static final int URI_CODE_NEWS = 8;
	public static final int URI_CODE_VENUE_HOURS = 9;
	public static final int URI_CODE_SHOW_TIMES = 10;
	public static final int URI_CODE_EVENT_TIMES = 11;
	public static final int URI_CODE_EVENT_SERIES = 12;
	public static final int URI_CODE_EVENT_SERIES_TIMES = 13;
	public static final int URI_CODE_OFFER_SERIES = 14;
	public static final int URI_CODE_OFFERS = 15;
	public static final int URI_CODE_INTERACTIVE_EXPERIENCES = 16;
    public static final int URI_CODE_INTERACTIVE_BEACONS = 17;
	public static final int URI_CODE_PHOTOFRAME_EXPERIENCES = 18;
	public static final int URI_CODE_QUEUES = 19;
	public static final int URI_CODE_TICKET_APPOINTMENTS = 20;
	public static final int URI_CODE_MOBILE_PAGES = 21;

	/**
	 * Link all of the valid URI codes and their matching database table
	 */
	private static final HashMap<Uri, String> URI_TO_TABLE_NAME_MAP;
	static {
		URI_TO_TABLE_NAME_MAP = new HashMap<Uri, String>();
		URI_TO_TABLE_NAME_MAP.put(RESPONSE_QUEUE, ResponseQueueTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(POINTS_OF_INTEREST, PointsOfInterestTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(VENUES, VenuesTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(VENUE_LANDS, VenueLandsTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(WAYPOINTS, WaypointsTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(PATHS, PathsTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(ALERTS, AlertsTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(NEWS, NewsTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(VENUE_HOURS, VenueHoursTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(SHOW_TIMES, ShowTimesTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(EVENT_TIMES, EventTimesTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(EVENT_SERIES, EventSeriesTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(EVENT_SERIES_TIMES, EventSeriesTimesTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(OFFER_SERIES, OfferSeriesTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(OFFERS, OffersTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(INTERACTIVE_EXPERIENCES, InteractiveExperiencesTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(PHOTOFRAME_EXPERIENCE, UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.TABLE_NAME);
        URI_TO_TABLE_NAME_MAP.put(INTERACTIVE_BEACONS, UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(QUEUES, UniversalOrlandoDatabaseTables.QueuesTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(TICKET_APPOINTMENTS, UniversalOrlandoDatabaseTables.TicketsAppointmentTable.TABLE_NAME);
		URI_TO_TABLE_NAME_MAP.put(MOBILE_PAGES, UniversalOrlandoDatabaseTables.MobilePagesTable.TABLE_NAME);
	}

	/**
	 * URI matcher used to resolve valid URIs with the {@link UniversalOrlandoContentProvider}.
	 */
	private static final UriMatcher URI_MATCHER;
	static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

		// Add the special URI for custom table statements
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, CUSTOM_TABLE_STATEMENT + "/*", URI_CODE_CUSTOM_TABLE);

		// Add the URIs for all the tables that exist in the DB
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, ResponseQueueTable.TABLE_NAME, URI_CODE_RESPONSE_QUEUE);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, PointsOfInterestTable.TABLE_NAME, URI_CODE_POINTS_OF_INTEREST);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, VenuesTable.TABLE_NAME, URI_CODE_VENUES);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, VenueLandsTable.TABLE_NAME, URI_CODE_VENUE_LANDS);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, WaypointsTable.TABLE_NAME, URI_CODE_WAYPOINTS);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, PathsTable.TABLE_NAME, URI_CODE_PATHS);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, AlertsTable.TABLE_NAME, URI_CODE_ALERTS);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, NewsTable.TABLE_NAME, URI_CODE_NEWS);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, VenueHoursTable.TABLE_NAME, URI_CODE_VENUE_HOURS);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, ShowTimesTable.TABLE_NAME, URI_CODE_SHOW_TIMES);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, EventTimesTable.TABLE_NAME, URI_CODE_EVENT_TIMES);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, EventSeriesTable.TABLE_NAME, URI_CODE_EVENT_SERIES);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, EventSeriesTimesTable.TABLE_NAME, URI_CODE_EVENT_SERIES_TIMES);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, OfferSeriesTable.TABLE_NAME, URI_CODE_OFFER_SERIES);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, OffersTable.TABLE_NAME, URI_CODE_OFFERS);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, InteractiveExperiencesTable.TABLE_NAME, URI_CODE_INTERACTIVE_EXPERIENCES);
        URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.TABLE_NAME, URI_CODE_INTERACTIVE_BEACONS);
        URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.TABLE_NAME, URI_CODE_PHOTOFRAME_EXPERIENCES);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, UniversalOrlandoDatabaseTables.QueuesTable.TABLE_NAME, URI_CODE_QUEUES);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, UniversalOrlandoDatabaseTables.TicketsAppointmentTable.TABLE_NAME, URI_CODE_TICKET_APPOINTMENTS);
		URI_MATCHER.addURI(UniversalOrlandoContentProvider.AUTHORITY, UniversalOrlandoDatabaseTables.MobilePagesTable.TABLE_NAME, URI_CODE_MOBILE_PAGES);
	}

	/**
	 * Convenience method to match a content URI.
	 *
	 * @param uri
	 *         the content URI to match
	 *
	 * @return the URI code that was matched, or {@code -1} if the URI did not match
	 */
	public static int matchUri(Uri uri) {
		return URI_MATCHER.match(uri);
	}

	/**
	 * Lookup the database table name using its content URI.
	 *
	 * @param contentUri
	 *         the content URI to lookup
	 *
	 * @return the database table name for the content URI, or {@code null} if the content URI can't
	 * be found
	 */
	public static String getDbTableNameByContentUri(Uri contentUri) {
		return URI_TO_TABLE_NAME_MAP.get(contentUri);
	}

	/**
	 * Reverse lookup a content URI using a databases table name.
	 *
	 * @param dbTableName
	 *         the database table name
	 *
	 * @return the content URI for the database table, or {@code null} if the table can't be found
	 */
	public static Uri getContentUriByDbTableName(String dbTableName) {
		for (Map.Entry<Uri, String> entry : URI_TO_TABLE_NAME_MAP.entrySet()) {
			String contentUriTableName = entry.getValue();
			if (contentUriTableName.equals(dbTableName)) {
				return entry.getKey();
			}
		}
		return null;
	}
}