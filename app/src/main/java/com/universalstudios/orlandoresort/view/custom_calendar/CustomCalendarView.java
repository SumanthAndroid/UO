package com.universalstudios.orlandoresort.view.custom_calendar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.CommerceUiBuilder;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views.OnDateChangedListener;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Entitlements.ByDate.response.CombinedSeasonalTicket;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetCardsRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.PreferenceUtils;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.universalstudios.orlandoresort.view.custom_calendar.CalendarCellSeasonState.PEAK;
import static com.universalstudios.orlandoresort.view.custom_calendar.CalendarCellSeasonState.REGULAR;
import static com.universalstudios.orlandoresort.view.custom_calendar.CalendarCellSeasonState.VALUE;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 7/21/16.
 * Class: CustomCalendarView
 * Class Description: View containing the custom calendar object
 */
public class CustomCalendarView extends LinearLayout implements View.OnClickListener, CustomCalendarAdapter.CalendarChangeListener, OnDateChangedListener {
    public static final String TAG = "CustomCalendarView";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private OnDateChangedListener onDateChangedListener;
    private CustomCalendarAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ImageView mPreviousMonth;
    private ImageView mNextMonth;
    private TextView mMonthText;

    private int currentMonth = 0;
    private int currentDay;
    private int currentYear;
    private List<Date> selectedDates;

    private Calendar calendar;
    private Calendar startinCalendar;

    private TridionConfig tridionConfig;

    private int daysInLastMonth;
    private int daysInCurrentMonth;
    private int dayOfWeekMonthStarts;
    private TicketType ticketType;
    private boolean showSeasonalDates = false;
    private String datePreferenceName;
    private int daysLimit;
    private static final int DEFAULT_DAY_LIMIT = 180;//defaults to roughly 6 months

    private Map<String, CombinedSeasonalTicket> calendarResultMap;

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnDateChangedListener(OnDateChangedListener onDateChangedListener) {
        this.onDateChangedListener = onDateChangedListener;

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void init(Map<String, CombinedSeasonalTicket> calendarResultMapParam, Integer calendarLimit, boolean showSeasonalDates) {
        if (null == mAdapter) {
            mAdapter = new CustomCalendarAdapter(getContext(), this);
        }

        if(CommerceUiBuilder.getCurrentIdentifier() != null) {
            if(CommerceUiBuilder.getCurrentIdentifier().contains(GetCardsRequest.UEP_IDENTIFIER)) {
                ticketType = TicketType.TYPE_EXPRESS;
                datePreferenceName = PreferenceUtils.CALENDAR_SELECTED_UEP_DATE_KEY;
            } else {
                ticketType = TicketType.TYPE_TICKETS;
                datePreferenceName = PreferenceUtils.CALENDAR_SELECTED_TICKETS_DATE_KEY;
            }
        } else {
            //for now we default to the park tickets view
            ticketType = TicketType.TYPE_TICKETS;
            datePreferenceName = PreferenceUtils.CALENDAR_SELECTED_TICKETS_DATE_KEY;
        }

        tridionConfig = IceTicketUtils.getTridionConfig();
        calendarResultMap = calendarResultMapParam;
        this.showSeasonalDates = showSeasonalDates;

        mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerViewCustomCalender);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));

        mNextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        mPreviousMonth = (ImageView) this.findViewById(R.id.previousMonth);
        mNextMonth.setOnClickListener(this);
        mPreviousMonth.setOnClickListener(this);
        mMonthText = (TextView) this.findViewById(R.id.monthText);

        startinCalendar = Calendar.getInstance();
        currentDay = startinCalendar.get(Calendar.DAY_OF_MONTH);
        if(calendarLimit != null) {
            daysLimit = calendarLimit;
        } else {
            daysLimit = DEFAULT_DAY_LIMIT;
        }
        changeMonth(startinCalendar.get(Calendar.MONTH), startinCalendar.get(Calendar.YEAR));
        mAdapter.setDaysList(getPopulatedDaysList());

    }

    /**
     * Set the initial date of the calendar, determined by the date stored in SharedPreferences
     * (or today's date, if there is no date in SharedPreferences)
     */
    public void setInitialDate() {

        PreferenceUtils preferenceUtils = new PreferenceUtils();
        String storedDate = preferenceUtils.getString(datePreferenceName, null);
        // If there is no stored date in preferences, set the selected day to today's date
        if (storedDate == null || isDateBeforeToday(storedDate)) {
            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();
            setDefaultDate(today);
        } else {
            // There is a previously selected day in preferences, set the selected day to it
            try {
                Date startDate = DATE_FORMAT.parse(storedDate);
                setDefaultDate(startDate);
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the date to be selected
     * @param date
     */
    public void setDefaultDate(Date date) {
        if (selectedDates == null) {
            selectedDates = new ArrayList<>();
        }

        selectedDates.clear();
        selectedDates.add(date);
        //Show the selected date
        if (null != date) {
            changeMonth(date.getMonth(), date.getYear() + 1900);
        }
    }

    /**
     * Performs operations to change the calendar's month when the next/previous
     * arrows are touched.
     * @param month The new month
     * @param year The new year
     */
    private void changeMonth(int month, int year) {
        currentMonth = month;
        currentYear = year;
        calendar = new GregorianCalendar(year, month, 1);
        daysInCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        dayOfWeekMonthStarts = calendar.get(Calendar.DAY_OF_WEEK);
        calendar = new GregorianCalendar(year, --month, 1);
        daysInLastMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        mAdapter.setDaysList(getPopulatedDaysList());

        setMonthText(currentMonth);
    }

    /**
     * Retrieves the List<CalendarCellObject> which correspond to the calendar cells
     * @return List<CalendarCellObject>
     */
    private List<CalendarCellObject> getPopulatedDaysList() {
        List<CalendarCellObject> days = new ArrayList<>();
        int totalCount = 0;
        //Add the last days of the previous month
        int iterationCount = dayOfWeekMonthStarts;
        if (dayOfWeekMonthStarts != 1) {
            for (int i = 0; i < iterationCount - 1; i++) {
                days.add(new CalendarCellObject((daysInLastMonth - iterationCount + 2 + i), true));
                totalCount++;
            }
        }

        Calendar calendar = Calendar.getInstance();
        //Add days for current month
        boolean current = false;
        if (currentMonth == calendar.get(Calendar.MONTH) && currentYear == calendar.get(Calendar.YEAR)) {
            current = true;
        }
        for (int i = 1; i <= daysInCurrentMonth; i++) {
            // Day is out of month
            if (current && i < calendar.get(Calendar.DAY_OF_MONTH)) {
                CalendarCellObject object = new CalendarCellObject(i, true);
                object.isSelected = false;
                days.add(object);
            }
            // Day is not out of month
            else {
                CalendarCellObject object = new CalendarCellObject(i, false);
                // Set object's season state enum
                if (calendarResultMap != null && showSeasonalDates) {
                    // Month is zero-based, need to match exact date (regular dates are not zero-based)
                    StringBuilder sb = extractSeasonalTicketString(currentYear, currentMonth + 1, i);
                    CombinedSeasonalTicket seasonalTicket = calendarResultMap.get(sb.toString());
                    if (seasonalTicket != null) {
                        String computedSeasonEnumKey = seasonalTicket.getComputedSeason();
                        String uvbSeasonKey = seasonalTicket.getUvbSeason();
                        String twoParkSeasonKey = seasonalTicket.getTwoParkSeason();

                        object.computedSeasonState = CalendarCellSeasonState.fromSeasonValue(computedSeasonEnumKey);
                        object.uvbSeasonState = CalendarCellSeasonState.fromSeasonValue(uvbSeasonKey);
                        object.twoParkSeasonState = CalendarCellSeasonState.fromSeasonValue(twoParkSeasonKey);
                    }
                    else {
                        object.computedSeasonState = null;
                    }
                } else {
                    //setting the computedSeasonState to null treat's the object like a regular date
                    object.computedSeasonState = null;
                }

                object.isSelected = isDateSelected(currentMonth, currentYear, object.day);
                if (object.isSelected) {
                    // Set the calendar season description text based off of the season
                    setCalendarSeasonDescText(object.uvbSeasonState, object.twoParkSeasonState);
                }
                days.add(object);
            }
            totalCount++;
        }

        int remainder = totalCount % 7;
        if (remainder > 0) {
            int count = 7 - remainder;
            for (int i = 0; i < count; i++) {
                days.add(new CalendarCellObject(i + 1, true));
                totalCount++;
            }
        }
        return days;
    }

    /**
     * Build's a seasonal ticket string given the year, month, and day; in the format of YYYY-MM-DD
     *
     * @param currentYear The year
     * @param currentMonth The month
     * @param currentDay The day
     * @return The date string, in the format of YYYY-MM-DD
     */
    private StringBuilder extractSeasonalTicketString(int currentYear, int currentMonth, int currentDay) {
        StringBuilder sb = new StringBuilder();
        sb.append(currentYear);
        sb.append("-");
        sb.append(String.format(Locale.US, "%02d", currentMonth));
        sb.append("-");
        sb.append(String.format(Locale.US, "%02d", currentDay));
        return sb;
    }

    /**
     * Determines if the current date is selected
     *
     * @param month month to check against
     * @param year year to check against
     * @param day day of month to check against
     * @return true if date should be selected
     */
    private boolean isDateSelected(int month, int year, int day) {
        if (null == selectedDates) {
            return false;
        }
        for (Date date : selectedDates) {
            if (date.getDate() == day && (date.getYear() + 1900) == year && date.getMonth() == month) {
                return true;
            }
        }
        return false;
    }

    /**
     * This is used to tell is a formatted date string is dated before today
     * @param dateString
     * @return
     */
    private boolean isDateBeforeToday(String dateString){
        try {
            Date date = DATE_FORMAT.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();
            return date.compareTo(today) < 0;
        } catch (ParseException ex){
            //if the date is malformed, then we return true
            return true;
        }
    }

    /**
     * Sets the text based on the current month
     */
    private void setMonthText(int month) {
        String monthText = "";

        switch (month) {
            case Calendar.JANUARY:
                monthText = tridionConfig.getMonthJanuaryLabel();
                break;
            case Calendar.FEBRUARY:
                monthText = tridionConfig.getMonthFebruaryLabel();
                break;
            case Calendar.MARCH:
                monthText = tridionConfig.getMonthMarchLabel();
                break;
            case Calendar.APRIL:
                monthText = tridionConfig.getMonthAprilLabel();
                break;
            case Calendar.MAY:
                monthText = tridionConfig.getMonthMayLabel();
                break;
            case Calendar.JUNE:
                monthText = tridionConfig.getMonthJuneLabel();
                break;
            case Calendar.JULY:
                monthText = tridionConfig.getMonthJulyLabel();
                break;
            case Calendar.AUGUST:
                monthText = tridionConfig.getMonthAugustLabel();
                break;
            case Calendar.SEPTEMBER:
                monthText = tridionConfig.getMonthSeptemberLabel();
                break;
            case Calendar.OCTOBER:
                monthText = tridionConfig.getMonthOctoberLabel();
                break;
            case Calendar.NOVEMBER:
                monthText = tridionConfig.getMonthNovemberLabel();
                break;
            case Calendar.DECEMBER:
                monthText = tridionConfig.getMonthDecemberLabel();
                break;
        }

        mMonthText.setText(monthText + " " + currentYear);
    }

    /**
     * Shows the next month's calendar
     */
    private void setNextMonth() {
        int startingMonth = startinCalendar.get(Calendar.MONTH);
        int startingYear = startinCalendar.get(Calendar.YEAR);

        int monthToChangeTo = currentMonth;
        int yearToChangeTo = currentYear;

        if (currentMonth == 11) {
            monthToChangeTo = 0;
            yearToChangeTo++;
        } else {
            monthToChangeTo++;
        }

        if(yearToChangeTo == startingYear && monthToChangeTo < startingMonth) {
            return;
        }


        // This range ensures that the user cannot go back to a previous month, and that if
        // the month they are in is no further than the daysLimit available
        int monthAdjustedForYear = (12*(yearToChangeTo - startingYear) + monthToChangeTo);

        //this logic works only if the limit doesn't go past a year's time
        //at that point, we would need to update this logic
        if ( monthAdjustedForYear - startingMonth > getMonthLimitForCalendar()) {
            return;
        }

        changeMonth(monthToChangeTo, yearToChangeTo);
    }

    /**
     * Shows the previous month's calendar
     */
    private void setPreviousMonth() {
        Calendar nowCalendar = Calendar.getInstance();
        if (currentYear == nowCalendar.get(Calendar.YEAR) && currentMonth == nowCalendar.get(Calendar.MONTH)) {
            return;
        }

        if (currentMonth == 0) {
            currentMonth = 11;
            currentYear--;
        } else {
            currentMonth--;
        }
        changeMonth(currentMonth, currentYear);
    }

    @Override
    public void onDateSelected(CalendarCellObject calendarCellObject) {
        if (null != selectedDates && selectedDates.size() >= 5) {
            calendarCellObject.isSelected = false;
            mAdapter.notifyDataSetChanged();
            return;//Can't add more than 5 dates
        }

        if (null == selectedDates) {
            selectedDates = new ArrayList<>();
        }
        else if (selectedDates.size() == 1) {
            selectedDates.clear();
        }
        Date selectedDate = new Date();
        selectedDate.setYear(currentYear - 1900);
        selectedDate.setMonth(currentMonth);
        selectedDate.setDate(calendarCellObject.day);
        selectedDates.add(selectedDate);

        // Save the selected date in SharedPreferences.  This will be the
        // selected date when the user re-opens the filter page
        String formattedDate = DATE_FORMAT.format(selectedDate);
        PreferenceUtils preferenceUtils = new PreferenceUtils();
        preferenceUtils.saveString(datePreferenceName, formattedDate);
        preferenceUtils.commitPreference();

        // Refresh the adapter
        mAdapter.setDaysList(getPopulatedDaysList());
        // Fire the on selected date changed event
        onDateChanged(selectedDate);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mNextMonth.getId()) {
            setNextMonth();
        } else if (v.getId() == mPreviousMonth.getId()) {
            setPreviousMonth();
        }
    }

    @Override
    public void onDateChanged(Date selectedDate) {
        if (onDateChangedListener != null) {
            onDateChangedListener.onDateChanged(selectedDate);
        }
    }

    public void setCalendarSeasonDescText(CalendarCellSeasonState uvbSeasonState, CalendarCellSeasonState twoParkSeasonState) {
        if (uvbSeasonState == null || twoParkSeasonState == null) {
            return;
        }

        TextView mCalendarSeasonDescText = (TextView) this.findViewById(R.id.calendar_season_description_text);

        // There is a 3x3 grid of possibilities for what this season description string should display,
        // chosen depending on both the UVB season and the 2P season
        if (uvbSeasonState == VALUE) {
            if (twoParkSeasonState == REGULAR) {
                mCalendarSeasonDescText.setText(tridionConfig.getValueRegularDescription());
            }
            else if (twoParkSeasonState == PEAK) {
                mCalendarSeasonDescText.setText(tridionConfig.getValuePeakDescription());
            }
            else if (twoParkSeasonState == VALUE) {
                mCalendarSeasonDescText.setText(tridionConfig.getValueValueDescription());
            }
        }
        else if (uvbSeasonState == REGULAR) {
            if (twoParkSeasonState == REGULAR) {
                mCalendarSeasonDescText.setText(tridionConfig.getRegularRegularDescription());
            }
            else if (twoParkSeasonState == PEAK) {
                mCalendarSeasonDescText.setText(tridionConfig.getRegularPeakDescription());
            }
            else if (twoParkSeasonState == VALUE) {
                mCalendarSeasonDescText.setText(tridionConfig.getRegularValueDescription());
            }
        }
        else if (uvbSeasonState == PEAK) {
            if (twoParkSeasonState == REGULAR) {
                mCalendarSeasonDescText.setText(tridionConfig.getPeakRegularDescription());
            }
            else if (twoParkSeasonState == PEAK) {
                mCalendarSeasonDescText.setText(tridionConfig.getPeakPeakDescription());
            }
            else if (twoParkSeasonState == VALUE) {
                mCalendarSeasonDescText.setText(tridionConfig.getPeakValueDescription());
            }
        }
    }

    private int getMonthLimitForCalendar() {
        return (daysLimit / 30);
    }
}
