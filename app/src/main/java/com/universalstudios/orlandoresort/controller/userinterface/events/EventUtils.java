/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.events;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.explore.PoiViewHolder;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.domain.events.EventSeries;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.EventDate;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.EventDate.EventDateAscendingComparator;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

/**
 * Utility method for Events and Event Series
 * 
 * @author acampbell
 *
 */
public class EventUtils {

    private static final String TAG = EventUtils.class.getSimpleName();

    // Case in-sensitive search for 'harry potter'
    private static final String EVENT_SERIES_HARRY_POTTER_REGEX = "(?i).*(harry potter).*";

    private EventUtils() {}

    /**
     * Determine if the passed eventDates span multiple days
     * 
     * @param eventDates
     * @return
     */
    public static boolean eventDatesSpanMultipleDays(List<EventDate> eventDates) {
        if (eventDates != null && eventDates.size() > 0) {
            Collections.sort(eventDates, new EventDateAscendingComparator());
            EventDate startDate = eventDates.get(0);
            EventDate endDate = eventDates.get(eventDates.size() - 1);
            if (startDate.getStartDateUnix() != null && endDate.getEndDateUnix() != null) {
                Calendar calendarStart = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
                calendarStart.setTime(new Date(startDate.getStartDateUnix() * 1000));
                Calendar calendarEnd = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
                calendarEnd.setTime(new Date(endDate.getEndDateUnix() * 1000));
                return calendarStart.get(Calendar.DAY_OF_YEAR) != calendarEnd.get(Calendar.DAY_OF_YEAR);
            } else if (startDate.getStartDateUnix() != null && endDate.getStartDateUnix() != null) {
                Calendar calendarStart = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
                calendarStart.setTime(new Date(startDate.getStartDateUnix() * 1000));
                Calendar calendarEnd = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
                calendarEnd.setTime(new Date(endDate.getStartDateUnix() * 1000));
                return calendarStart.get(Calendar.DAY_OF_YEAR) != calendarEnd.get(Calendar.DAY_OF_YEAR);
            }
        }

        return false;
    }

    public static List<EventSeries> getEventsForNavigation() {
        ContentResolver resolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
        if (null != resolver) {
            StringBuilder queryString = new StringBuilder();
            queryString.append(" (").append(UniversalOrlandoDatabaseTables.EventSeriesTable.COL_FLAGS).append(" & ")
                    .append(EventSeries.FLAG_SHOW_IN_NAV).append(" = ").append(EventSeries.FLAG_SHOW_IN_NAV).append(") ");

            Cursor cursor = resolver.query(UniversalOrlandoContentUris.EVENT_SERIES, null, queryString.toString(), null, null, null);
            if (null != cursor && cursor.moveToFirst()) {
                try {
                    List<EventSeries> eventSeries = new ArrayList<>();
                    do {
                        String eventJson = cursor.getString(cursor.getColumnIndexOrThrow(UniversalOrlandoDatabaseTables.EventSeriesTable.COL_EVENT_SERIES_OBJECT_JSON));
                        EventSeries series = EventSeries.fromJson(eventJson, EventSeries.class);
                        if (null != series) {
                            eventSeries.add(series);
                        }
                    } while (cursor.moveToNext());
                    return eventSeries;
                } finally {
                    if (null != cursor) {
                        cursor.close();
                    }
                }
            }
        }
        return null;
    }

    public static String getEventDateStartSpanLines(List<EventDate> eventDates,
            String placeholder) {
        if (eventDates == null || eventDates.isEmpty()) {
            return placeholder;
        }

        int dateFormatFlags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_MONTH
                | DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_WEEKDAY;
        StringBuilder timeString = new StringBuilder();
        int count = 0;
        for (int i = 0; i < eventDates.size(); i++) {
            EventDate eventDate = eventDates.get(i);
            if (count > 0) {
                timeString.append("\n");
            }
            if (eventDate.getStartDateUnix() != null) {
                Formatter f = new Formatter(new StringBuilder(50), Locale.US);
                long startDateUnix = eventDate.getStartDateUnix() * 1000;
                String timeZone = DateTimeUtils.getParkTimezoneString();
                Formatter dateRange = DateUtils.formatDateRange(
                        UniversalOrlandoApplication.getAppContext(),
                        f,
                        startDateUnix,
                        startDateUnix,
                        dateFormatFlags,
                        timeZone);
                timeString.append(dateRange);
                count++;
            }
        }

        return timeString.toString();
    }

    public static String getEventTimeStartSpanLines(List<EventDate> eventDates, String placeholder) {
        if (eventDates == null || eventDates.isEmpty()) {
            return placeholder;
        }

        SimpleDateFormat dateFormat;
        if (DateTimeUtils.is24HourFormat()) {
            dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        } else {
            dateFormat = new SimpleDateFormat("h:mm a", Locale.US);
        }
        dateFormat.setTimeZone(DateTimeUtils.getParkTimeZone());
        StringBuilder timeString = new StringBuilder();
        int count = 0;
        for (int i = 0; i < eventDates.size(); i++) {
            EventDate eventDate = eventDates.get(i);
            if (count > 0) {
                timeString.append("\n");
            }
            if (eventDate.getStartDateUnix() != null && eventDate.getEndDateUnix() != null) {
                if (eventDate.getStartDateUnix().equals(eventDate.getEndDateUnix())) {
                    timeString.append(dateFormat.format(new Date(eventDate.getStartDateUnix() * 1000)));
                } else {
                    timeString.append(dateFormat.format(new Date(eventDate.getStartDateUnix() * 1000)))
                            .append(" - ").append(dateFormat.format(new Date(eventDate.getEndDateUnix() * 1000)));
                }
                count++;
            } else if (eventDate.getStartDateUnix() != null) {
                timeString.append(dateFormat.format(new Date(eventDate.getStartDateUnix() * 1000)));
                count++;
            }
        }

        return timeString.toString();
    }

    public static String getEventFullDay(EventDate eventDate, boolean shortStyle,
            String placeholder) {
        Context context = UniversalOrlandoApplication.getAppContext();
        if (eventDate == null || eventDate.getStartDateUnix() == null) {
            return placeholder == null ? context.getString(R.string.event_series_dates_tba) : placeholder;
        }

        // Use abbreviated format
        int dateFormatFlags = 0;
        if (shortStyle) {
            dateFormatFlags = DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_DATE
                    | DateUtils.FORMAT_SHOW_YEAR;
        } else {
            dateFormatFlags = DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_WEEKDAY
                    | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR;
        }

        Formatter f = new Formatter(new StringBuilder(50), Locale.US);
        long startDateUnix = eventDate.getStartDateUnix() * 1000;
        String timeZone = DateTimeUtils.getParkTimezoneString();

        return DateUtils.formatDateRange(context, f, startDateUnix, startDateUnix, dateFormatFlags, timeZone).toString();
    }

    public static String getEventDateSpan(List<EventDate> eventDates, boolean shortStyle,
            String placeholder, boolean... showYearArray) {
        Context context = UniversalOrlandoApplication.getAppContext();
        if (eventDates == null || eventDates.isEmpty()) {
            return placeholder == null ? context.getString(R.string.event_series_dates_tba) : placeholder;
        }
        boolean showYear = showYearArray.length > 0 ? showYearArray[0] : false;

        // Use abbreviated format
        int dateFormatFlags = 0;
        if (shortStyle) {
            dateFormatFlags = DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_DATE;
        } else {
            dateFormatFlags = DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_WEEKDAY
                    | DateUtils.FORMAT_SHOW_DATE;
        }
        if (showYear) {
            dateFormatFlags |= DateUtils.FORMAT_SHOW_YEAR;
        }

        // Sort dates in ascending order earliest to latest date
        Collections.sort(eventDates, new EventDateAscendingComparator());
        // Set dateString to a default value
        String dateString = placeholder;
        EventDate startDate = eventDates.get(0);
        EventDate endDate = eventDates.get(eventDates.size() - 1);
        if (startDate.getStartDateUnix() != null && endDate.getEndDateUnix() != null) {
            dateString = DateUtils.formatDateRange(context, new Formatter(new StringBuilder(50), Locale.US),
                    startDate.getStartDateUnix() * 1000, endDate.getEndDateUnix() * 1000, dateFormatFlags,
                    DateTimeUtils.getParkTimezoneString()).toString();
        } else if (startDate.getStartDateUnix() != null && endDate.getStartDateUnix() != null) {
            dateString = DateUtils.formatDateRange(context, new Formatter(new StringBuilder(50), Locale.US),
                    startDate.getStartDateUnix() * 1000, endDate.getStartDateUnix() * 1000, dateFormatFlags,
                    DateTimeUtils.getParkTimezoneString()).toString();
        } else if (startDate.getStartDateUnix() != null) {
            Formatter f = new Formatter(new StringBuilder(50), Locale.US);
            long startDateUnix = startDate.getStartDateUnix() * 1000;
            String timeZone = DateTimeUtils.getParkTimezoneString();

            dateString = DateUtils.formatDateRange(context, f, startDateUnix, startDateUnix, dateFormatFlags, timeZone).toString();
        } else {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "getEventDateSpan: event span startdate or enddate is null");
            }
        }

        return dateString;
    }

    public static String getEventHeaderDateSpan(EventDate eventDate, boolean shortStyle,
            String placeholder) {
        if (eventDate == null || eventDate.getStartDateUnix() == null || eventDate.getStartDateUnix() == 0) {
            return placeholder;
        }
        String dateSpan = getEventDateSpan(Arrays.asList(eventDate), shortStyle, placeholder);
        Calendar calendar = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
        Calendar eventCalendar = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
        eventCalendar.setTimeInMillis(eventDate.getStartDateUnix() * 1000);

        Context context = UniversalOrlandoApplication.getAppContext();
        // Check if event is today
        if (calendar.get(Calendar.YEAR) == eventCalendar.get(Calendar.YEAR)
                && calendar.get(Calendar.DAY_OF_YEAR) == eventCalendar.get(Calendar.DAY_OF_YEAR)) {
            dateSpan = context.getString(R.string.event_timeline_header_today_format, dateSpan);
        }
        // Check if event is tomorrow
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        if (calendar.get(Calendar.YEAR) == eventCalendar.get(Calendar.YEAR)
                && calendar.get(Calendar.DAY_OF_YEAR) == eventCalendar.get(Calendar.DAY_OF_YEAR)) {
            dateSpan = context.getString(R.string.event_timeline_header_tomorrow_format, dateSpan);
        }

        return dateSpan;
    }

    public static void updatePoiCircleBadgeForEvent(List<EventDate> eventDates, PoiViewHolder eventViewHolder,
            boolean favorites) {
        if (favorites && eventViewHolder != null) {
            eventViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);
            return;
        }

        // There must be valid event dates, and they must have been synced today
        UniversalAppState uoState = UniversalAppStateManager
                .getInstance();
        boolean hasSyncedPoisToday = UniversalAppStateManager
                .hasSyncedToday(uoState.getDateOfLastPoiSyncInMillis());
        if (eventDates == null || eventDates.size() == 0 || !hasSyncedPoisToday) {
            eventViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);
            return;
        }

        // If single date is empty hide the badge
        EventDate singleEventDate = eventDates.get(0);
        if (singleEventDate == null || singleEventDate.getStartDateUnix() == null
                || singleEventDate.getStartDateUnix() == 0) {
            eventViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);
            return;
        }

        Context context = UniversalOrlandoApplication.getAppContext();

        // Sort dates in ascending order earliest to latest date
        Collections.sort(eventDates, new EventDateAscendingComparator());
        if (eventDates != null && !eventDates.isEmpty()) {

            Date now = new Date();
            for (EventDate eventDate : eventDates) {
                Date date = new Date(eventDate.getStartDateUnix() * 1000);

                if (now.before(date)) {
                    String formattedShowTime;
                    String formattedShowTimeAmPm;
                    if (DateTimeUtils.is24HourFormat()) {
                        // Format the show time to 24-hour, park time
                        SimpleDateFormat sdfOutTime = new SimpleDateFormat("HH:mm", Locale.US);
                        sdfOutTime.setTimeZone(DateTimeUtils.getParkTimeZone());
                        formattedShowTime = sdfOutTime.format(date);
                        formattedShowTimeAmPm = "";
                    } else {
                        // Format the show time to 12-hour, park time
                        SimpleDateFormat sdfOutTime = new SimpleDateFormat("h:mm", Locale.US);
                        sdfOutTime.setTimeZone(DateTimeUtils.getParkTimeZone());
                        formattedShowTime = sdfOutTime.format(date);

                        // Last, format the AM/PM portion, park time
                        SimpleDateFormat sdfOutTimeAmPm = new SimpleDateFormat("a", Locale.US);
                        sdfOutTimeAmPm.setTimeZone(DateTimeUtils.getParkTimeZone());
                        formattedShowTimeAmPm = sdfOutTimeAmPm.format(date);
                    }

                    SimpleDateFormat sdfOutDate = new SimpleDateFormat("MMM d", Locale.US);
                    String formattedShowDate = sdfOutDate.format(date).toUpperCase(Locale.US);

                    // Apply the day/time to the badge
                    eventViewHolder.showTimeStartsText.setText(formattedShowDate);
                    eventViewHolder.showTimeStartsTimeText.setText(formattedShowTime);
                    eventViewHolder.showTimeAmPmText.setText(formattedShowTimeAmPm);
                    eventViewHolder.showTimeAmPmText.setVisibility(!TextUtils.isEmpty(formattedShowTimeAmPm) ? View.VISIBLE : View.GONE);

                    // Toggle badge visibility
                    eventViewHolder.waitTimeLayout.setVisibility(View.GONE);
                    eventViewHolder.closedLayout.setVisibility(View.GONE);
                    eventViewHolder.showTimeOpensText.setVisibility(View.GONE);
                    eventViewHolder.showTimeStartsText.setVisibility(View.VISIBLE);
                    eventViewHolder.showTimeBackgroundGray.setVisibility(View.GONE);
                    eventViewHolder.showTimeBackgroundBlue.setVisibility(View.VISIBLE);
                    eventViewHolder.showTimeLayout.setVisibility(View.VISIBLE);
                    eventViewHolder.circleBadgeRootLayout.setVisibility(View.VISIBLE);
                    return;
                }
            }

            // Show last event date with a gray background
            EventDate eventDate = eventDates.get(eventDates.size() - 1);
            Date eventStartDate = new Date(eventDate.getStartDateUnix() * 1000);

            String formattedShowTime;
            String formattedShowTimeAmPm;
            if (DateTimeUtils.is24HourFormat()) {
                // Format the show time to 24-hour, park time
                SimpleDateFormat sdfOutTime = new SimpleDateFormat("HH:mm", Locale.US);
                sdfOutTime.setTimeZone(DateTimeUtils.getParkTimeZone());
                formattedShowTime = sdfOutTime.format(eventStartDate);
                formattedShowTimeAmPm = "";
            } else {
                // Format the show time to 12-hour, park time
                SimpleDateFormat sdfOutTime = new SimpleDateFormat("h:mm", Locale.US);
                sdfOutTime.setTimeZone(DateTimeUtils.getParkTimeZone());
                formattedShowTime = sdfOutTime.format(eventStartDate);

                // Last, format the AM/PM portion, park time
                SimpleDateFormat sdfOutTimeAmPm = new SimpleDateFormat("a", Locale.US);
                sdfOutTimeAmPm.setTimeZone(DateTimeUtils.getParkTimeZone());
                formattedShowTimeAmPm = sdfOutTimeAmPm.format(eventStartDate);
            }

            SimpleDateFormat sdfOutDate = new SimpleDateFormat("MMM d", Locale.US);
            String formattedShowDate = sdfOutDate.format(eventStartDate).toUpperCase(Locale.US);

            // Apply the day/time to the badge
            eventViewHolder.showTimeStartsText.setText(formattedShowDate);
            eventViewHolder.showTimeStartsTimeText.setText(formattedShowTime);
            eventViewHolder.showTimeAmPmText.setText(formattedShowTimeAmPm);
            eventViewHolder.showTimeAmPmText.setVisibility(!TextUtils.isEmpty(formattedShowTimeAmPm) ? View.VISIBLE : View.GONE);

            // Toggle badge visibility
            eventViewHolder.waitTimeLayout.setVisibility(View.GONE);
            eventViewHolder.closedLayout.setVisibility(View.GONE);
            eventViewHolder.showTimeOpensText.setVisibility(View.GONE);
            eventViewHolder.showTimeStartsText.setVisibility(View.VISIBLE);
            eventViewHolder.showTimeBackgroundGray.setVisibility(View.VISIBLE);
            eventViewHolder.showTimeBackgroundBlue.setVisibility(View.GONE);
            eventViewHolder.showTimeLayout.setVisibility(View.VISIBLE);
            eventViewHolder.circleBadgeRootLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Determines if date passed in is covered by the passed EventDates. Returns time range is covered,
     * placeholder otherwise
     * 
     * @param eventDates
     * @param date
     * @param placeholder
     * @return
     */
    public static String getEventStatusForDay(Calendar calendar, List<EventDate> eventDates, Date date,
            String placeholder) {
        if (date == null || eventDates == null || eventDates.isEmpty()) {
            return placeholder;
        }

        calendar.setTime(date);
        // Reset calendar time
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long day = calendar.getTimeInMillis();
        StringBuilder dateString = new StringBuilder();
        boolean additionalDate = false;
        // Get the day of the year for the passed date
        for (EventDate eventDate : eventDates) {
            if (eventDate.getStartDateUnix() != null) {
                // Start time
                calendar.setTimeInMillis(eventDate.getStartDateUnix() * 1000);
                // Reset calendar time
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                long startDay = calendar.getTimeInMillis();

                // Check if the event occurs on the passed date
                if (day == startDay) {
                    if (additionalDate) {
                        dateString.append(", ")
                                .append(getEventTimeStartSpanLines(Arrays.asList(eventDate), placeholder));
                    } else {
                        dateString.append(getEventTimeStartSpanLines(Arrays.asList(eventDate), placeholder));
                        additionalDate = true;
                    }
                }
            }
        }
        if (dateString.length() > 0) {
            return dateString.toString();
        }

        return placeholder;
    }

    /**
     * Determine the appropriate primary ticketing message
     *
     * @param venueId
     * @param separateTicket
     * @param annualPass
     * @param buyNowUrl
     * @return
     */
    public static String getEventTicketPrimaryMessage(Long venueId, boolean separateTicket,
            boolean annualPass, String buyNowUrl) {
        Context context = UniversalOrlandoApplication.getAppContext();
        if (annualPass) {
            return context.getString(R.string.event_ticket_annual_pass);
        }

        if (separateTicket) {
            return context.getString(R.string.event_ticket_separate_ticket);
        }

        if (!separateTicket && !annualPass && venueId != null) {
            if (VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_FLORIDA == venueId) {
                return context.getString(R.string.event_ticket_free_usf);
            } else if (venueId != null && VenuesTable.VAL_VENUE_ID_ISLANDS_OF_ADVENTURE == venueId) {
                return context.getString(R.string.event_ticket_free_ioa);
            } else {
                return context.getString(R.string.event_ticket_no_ticket_required);
            }
        }

        return context.getString(R.string.event_ticket_free_uso);
    }

    /**
     * Determine the appropriate secondary ticketing message. Secondary text no longer displayed, logic remain
     * to determine if BuyNow feature is clickable
     * 
     * @param separateTicket
     * @param buyNowUrl
     * @return
     */
    public static String getEventTicketSecondaryMessage(boolean separateTicket,
            String buyNowUrl) {
        if (separateTicket && !TextUtils.isEmpty(buyNowUrl)) {
            return UniversalOrlandoApplication.getAppContext()
                    .getString(R.string.event_ticket_separate_ticket_buy_now);
        }

        return null;
    }

    public static EventDate getEndDate(List<EventDate> eventDates) {
        EventDate endDate = null;
        if (eventDates != null && !eventDates.isEmpty()) {
            Collections.sort(eventDates, new EventDateAscendingComparator());
            return eventDates.get(eventDates.size() - 1);
        }

        return endDate;
    }

    /**
     * Determine if the passed event series is harry potter related
     * 
     * @param eventSeries
     * @return
     */
    public static boolean isHarryPotterEvent(EventSeries eventSeries) {
        if (eventSeries != null) {
            if (!TextUtils.isEmpty(eventSeries.getDisplayName())
                    && eventSeries.getDisplayName().matches(EVENT_SERIES_HARRY_POTTER_REGEX)) {
                return true;
            }
            if (!TextUtils.isEmpty(eventSeries.getShortDescription())
                    && eventSeries.getShortDescription().matches(EVENT_SERIES_HARRY_POTTER_REGEX)) {
                return true;
            }
        }

        return false;
    }

}
