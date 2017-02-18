package com.universalstudios.orlandoresort.view.custom_calendar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views.OnDateChangedListener;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory.PriceInventoryEventAvailabilityDate;
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


public class EventCalendarView extends LinearLayout implements View.OnClickListener, EventCalendarAdapter.CalendarChangeListener, OnDateChangedListener {
    private static final String TAG = EventCalendarView.class.getSimpleName();


    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    protected static final int DEFAULT_DAY_LIMIT = 180;//defaults to roughly 6 months

    private OnDateChangedListener mOnDateChangedListener;
    private OnMonthChangeListener mOnMonthChangedListener;
    private EventCalendarAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ImageView mPreviousMonth;
    private ImageView mNextMonth;
    private TextView mMonthText;
    private TextView mUnavailableText;
    private TextView mAvailableText;
    private TextView mCalendarSeasonDescText;
    private ViewGroup mDescriptionContainer;

    private int mCurrentMonth = 0;
    private int mCurrentYear;
    private List<Date> mSelectedDates;

    private Calendar mCalendar;
    private Calendar mStartingCalendar;

    private TridionConfig mTridionConfig;

    private int mDaysInLastMonth;
    private int mDaysInCurrentMonth;
    private int mDayOfWeekMonthStarts;
    private int mDaysLimit;
    private String mDatePreferenceName;

    private List<Map<String, PriceInventoryEventAvailabilityDate>> mCalendarResultMap;

    public EventCalendarView(Context context) {
        super(context);
        mDatePreferenceName = PreferenceUtils.CALENDAR_SELECTED_ADD_ON_DATE_KEY;
    }

    public EventCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mDatePreferenceName = PreferenceUtils.CALENDAR_SELECTED_ADD_ON_DATE_KEY;
    }

    public EventCalendarView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDatePreferenceName = PreferenceUtils.CALENDAR_SELECTED_ADD_ON_DATE_KEY;
    }

    public void setOnDateChangedListener(OnDateChangedListener onDateChangedListener) {
        this.mOnDateChangedListener = onDateChangedListener;
    }

    public void setOnMonthChangedListener(OnMonthChangeListener onMonthChangedListener) {
        this.mOnMonthChangedListener = onMonthChangedListener;
    }

    /**
     * Set the initial date of the mCalendar, determined by the date stored in SharedPreferences
     * (or today's date, if there is no date in SharedPreferences)
     */
    public void setInitialDate() {
        PreferenceUtils preferenceUtils = new PreferenceUtils();
        String storedDate = preferenceUtils.getString(mDatePreferenceName, null);
        // If there is no stored date in preferences, set the selected day to today's date
        if (storedDate == null) {
            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();
            setDefaultDate(today);
        } else {
            // There is a previously selected day in preferences, set the selected day to it
            try {
                Date startDate = DATE_FORMAT.parse(storedDate);
                setDefaultDate(startDate);
            } catch (ParseException e) {
                Crittercism.logHandledException(e);
            }
        }
    }

    public void init(List<Map<String, PriceInventoryEventAvailabilityDate>> priceInventoryEventAvailabilities) {
        init(priceInventoryEventAvailabilities, DEFAULT_DAY_LIMIT);
    }

    public void init(List<Map<String, PriceInventoryEventAvailabilityDate>> priceInventoryEventAvailabilities, Integer calendarLimit) {
        if (null == mAdapter) {
            mAdapter = new EventCalendarAdapter(getContext(), this);
        }

        mTridionConfig = IceTicketUtils.getTridionConfig();
        mCalendarResultMap = priceInventoryEventAvailabilities;

        mRecyclerView = (RecyclerView) this.findViewById(R.id.event_calendar_list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));

        mNextMonth = (ImageView) this.findViewById(R.id.event_calendar_next_month);
        mPreviousMonth = (ImageView) this.findViewById(R.id.event_calendar_previous_month);
        mNextMonth.setOnClickListener(this);
        mPreviousMonth.setOnClickListener(this);
        mMonthText = (TextView) this.findViewById(R.id.event_calendar_month_text);
        mUnavailableText = (TextView) this.findViewById(R.id.event_calendar_legend_unavailable_text);
        mAvailableText = (TextView) this.findViewById(R.id.event_calendar_legend_available_text);

        mUnavailableText.setText(mTridionConfig.getUnavailableLabel());
        mAvailableText.setText(mTridionConfig.getAvailableTimesLabel());

        mStartingCalendar = Calendar.getInstance();

        if (calendarLimit != null) {
            mDaysLimit = calendarLimit;
        } else {
            mDaysLimit = DEFAULT_DAY_LIMIT;
        }
        changeMonth(mStartingCalendar.get(Calendar.MONTH), mStartingCalendar.get(Calendar.YEAR));
        mAdapter.setDaysList(getPopulatedDaysList());

        mCalendarSeasonDescText = (TextView) this.findViewById(R.id.event_calendar_description_text);
        mDescriptionContainer = (ViewGroup) this.findViewById(R.id.event_calendar_description_container);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mNextMonth.getId()) {
            setNextMonth();
            mAdapter.setSelectedItem(-1);
        } else if (v.getId() == mPreviousMonth.getId()) {
            setPreviousMonth();
            mAdapter.setSelectedItem(-1);
        }

        if (mOnMonthChangedListener != null) {
            mOnMonthChangedListener.onMonthChange();
        }
    }

    /**
     * returns true if the selected date is found in the list of date cells created
     *
     * @param priceInventoryEventAvailabilities
     * @param selectedDate
     * @return
     */
    public boolean setDates(List<Map<String, PriceInventoryEventAvailabilityDate>> priceInventoryEventAvailabilities, Date selectedDate) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "setDates() called with: priceInventoryEventAvailabilities = [" + priceInventoryEventAvailabilities + "]");
        }
        mCalendarResultMap = priceInventoryEventAvailabilities;
        List<EventCalendarCell> populatedDaysList = getPopulatedDaysList();
        mAdapter.setDaysList(populatedDaysList);

        //if we already have selected a date, get that date's position in in the list of dates
        //and set it as selected in the list
        if (selectedDate != null) {
            for (int i = 0, populatedDaysListSize = populatedDaysList.size(); i < populatedDaysListSize; i++) {
                EventCalendarCell eventCalendarCell = populatedDaysList.get(i);
                if (selectedDate.equals(eventCalendarCell.getDate())) {
                    mAdapter.setSelectedItem(i);
                    setDescriptionText(eventCalendarCell);
                    return true;
                }
            }
        }
        return false;
    }

    public EventCalendarCell getCalendarCell(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mCurrentYear);
        calendar.set(Calendar.MONTH, mCurrentMonth);
        calendar.set(Calendar.DAY_OF_MONTH, i);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        EventCalendarCell eventCalendarCell = new EventCalendarCell(calendar.getTime(), false);
        // Set object's season state enum
        if (mCalendarResultMap != null) {
            for (Map<String, PriceInventoryEventAvailabilityDate> stringPriceInventoryEventAvailabilityDateMap : mCalendarResultMap) {
                if (mCalendarResultMap != null) {
                    // Month is zero-based, need to match exact date (regular dates are not zero-based)
                    String dateKey = getDateKey(calendar.getTime());
                    PriceInventoryEventAvailabilityDate priceInventoryEventAvailabilityDate = stringPriceInventoryEventAvailabilityDateMap.get(dateKey);
                    if (priceInventoryEventAvailabilityDate != null) {
                        eventCalendarCell.addPriceInventoryEvents(priceInventoryEventAvailabilityDate);
                    }
                }
            }
        }
        return eventCalendarCell;
    }

    private String getDateKey(Date date) {
        return DATE_FORMAT.format(date);
    }

    /**
     * Sets the date to be selected
     *
     * @param date
     */
    public void setDefaultDate(Date date) {
        if (mSelectedDates == null) {
            mSelectedDates = new ArrayList<>();
        }

        mSelectedDates.clear();
        mSelectedDates.add(date);
        //Show the selected date
        if (null != date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            changeMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
        }
    }

    /**
     * Performs operations to change the mCalendar's month when the next/previous
     * arrows are touched.
     *
     * @param month The new month
     * @param year  The new year
     */
    protected void changeMonth(int month, int year) {
        mCurrentMonth = month;
        mCurrentYear = year;
        mCalendar = new GregorianCalendar(year, month, 1);
        mDaysInCurrentMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        mDayOfWeekMonthStarts = mCalendar.get(Calendar.DAY_OF_WEEK);
        mCalendar = new GregorianCalendar(year, --month, 1);
        mDaysInLastMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        mAdapter.setDaysList(getPopulatedDaysList());

        setMonthText(mCurrentMonth);
    }

    /**
     * Retrieves the List<CalendarCellObject> which correspond to the mCalendar cells
     *
     * @return List<CalendarCellObject>
     */
    protected List<EventCalendarCell> getPopulatedDaysList() {
        List<EventCalendarCell> days = new ArrayList<>();
        int totalCount = 0;
        //Add the last days of the previous month
        int iterationCount = mDayOfWeekMonthStarts;
        if (mDayOfWeekMonthStarts != 1) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, mCurrentYear);
            calendar.set(Calendar.MONTH, mCurrentMonth);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            for (int i = 0; i < iterationCount - 1; i++) {
                calendar.set(Calendar.DAY_OF_MONTH, mDaysInLastMonth - iterationCount + 2 + i);
                days.add(new EventCalendarCell(calendar.getTime(), true));
                totalCount++;
            }
        }

        Calendar calendar = Calendar.getInstance();
        //Add days for current month
        boolean current = false;
        //noinspection WrongConstant
        if (mCurrentMonth == calendar.get(Calendar.MONTH) && mCurrentYear == calendar.get(Calendar.YEAR)) {
            current = true;
        }


        calendar.set(Calendar.YEAR, mCurrentYear);
        calendar.set(Calendar.MONTH, mCurrentMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        for (int i = 1; i <= mDaysInCurrentMonth; i++) {
            // Day is out of month
            if (current && i < calendar.get(Calendar.DAY_OF_MONTH)) {
                calendar.set(Calendar.DAY_OF_MONTH, i);
                EventCalendarCell object = new EventCalendarCell(calendar.getTime(), true);
                days.add(object);
            }
            // Day is not out of month
            else {
                EventCalendarCell object = getCalendarCell(i);
                days.add(object);
            }
            totalCount++;
        }


        calendar.set(Calendar.YEAR, mCurrentYear);
        calendar.set(Calendar.MONTH, mCurrentMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        int remainder = totalCount % 7;
        if (remainder > 0) {
            int count = 7 - remainder;
            for (int i = 0; i < count; i++) {
                calendar.set(Calendar.DAY_OF_MONTH, i + 1);
                days.add(new EventCalendarCell(calendar.getTime(), true));
                totalCount++;
            }
        }
        return days;
    }

    /**
     * Sets the text based on the current month
     */
    private void setMonthText(int month) {
        String monthText = "";

        switch (month) {
            case Calendar.JANUARY:
                monthText = mTridionConfig.getMonthJanuaryLabel();
                break;
            case Calendar.FEBRUARY:
                monthText = mTridionConfig.getMonthFebruaryLabel();
                break;
            case Calendar.MARCH:
                monthText = mTridionConfig.getMonthMarchLabel();
                break;
            case Calendar.APRIL:
                monthText = mTridionConfig.getMonthAprilLabel();
                break;
            case Calendar.MAY:
                monthText = mTridionConfig.getMonthMayLabel();
                break;
            case Calendar.JUNE:
                monthText = mTridionConfig.getMonthJuneLabel();
                break;
            case Calendar.JULY:
                monthText = mTridionConfig.getMonthJulyLabel();
                break;
            case Calendar.AUGUST:
                monthText = mTridionConfig.getMonthAugustLabel();
                break;
            case Calendar.SEPTEMBER:
                monthText = mTridionConfig.getMonthSeptemberLabel();
                break;
            case Calendar.OCTOBER:
                monthText = mTridionConfig.getMonthOctoberLabel();
                break;
            case Calendar.NOVEMBER:
                monthText = mTridionConfig.getMonthNovemberLabel();
                break;
            case Calendar.DECEMBER:
                monthText = mTridionConfig.getMonthDecemberLabel();
                break;
        }

        mMonthText.setText(monthText + " " + mCurrentYear);
    }

    /**
     * Shows the next month's mCalendar
     */
    protected void setNextMonth() {
        int startingMonth = mStartingCalendar.get(Calendar.MONTH);
        int startingYear = mStartingCalendar.get(Calendar.YEAR);

        int monthToChangeTo = mCurrentMonth;
        int yearToChangeTo = mCurrentYear;

        if (mCurrentMonth == 11) {
            monthToChangeTo = 0;
            yearToChangeTo++;
        } else {
            monthToChangeTo++;
        }

        if (yearToChangeTo == startingYear && monthToChangeTo < startingMonth) {
            return;
        }


        // This range ensures that the user cannot go back to a previous month, and that if
        // the month they are in is no further than the mDaysLimit available
        int monthAdjustedForYear = (12 * (yearToChangeTo - startingYear) + monthToChangeTo);

        //this logic works only if the limit doesn't go past a year's time
        //at that point, we would need to update this logic
        if (monthAdjustedForYear - startingMonth > getMonthLimitForCalendar()) {
            return;
        }

        changeMonth(monthToChangeTo, yearToChangeTo);

        mDescriptionContainer.setVisibility(GONE);
        onDateChanged(null);
    }

    /**
     * Shows the previous month's mCalendar
     */
    protected void setPreviousMonth() {
        Calendar nowCalendar = Calendar.getInstance();
        //noinspection WrongConstant
        if (mCurrentYear == nowCalendar.get(Calendar.YEAR) && mCurrentMonth == nowCalendar.get(Calendar.MONTH)) {
            return;
        }

        if (mCurrentMonth == 0) {
            mCurrentMonth = 11;
            mCurrentYear--;
        } else {
            mCurrentMonth--;
        }
        changeMonth(mCurrentMonth, mCurrentYear);

        mDescriptionContainer.setVisibility(GONE);
        onDateChanged(null);
    }

    @Override
    public void onDateSelected(EventCalendarCell calendarCellObject) {
        if (null != mSelectedDates && mSelectedDates.size() >= 5) {
            mAdapter.notifyDataSetChanged();
            return;//Can't add more than 5 dates
        }

        if (null == mSelectedDates) {
            mSelectedDates = new ArrayList<>();
        } else if (mSelectedDates.size() == 1) {
            mSelectedDates.clear();
        }

        mSelectedDates.add(calendarCellObject.getDate());

        // Save the selected date in SharedPreferences.  This will be the
        // selected date when the user re-opens the filter page
        String formattedDate = DATE_FORMAT.format(calendarCellObject.getDate());
        PreferenceUtils preferenceUtils = new PreferenceUtils();
        preferenceUtils.saveString(mDatePreferenceName, formattedDate);
        preferenceUtils.commitPreference();

        // Refresh the adapter
        onDateChanged(calendarCellObject.getDate());

        setDescriptionText(calendarCellObject);
        mDescriptionContainer.setVisibility(VISIBLE);
    }

    private void setDescriptionText(EventCalendarCell calendarCellObject) {
        mDescriptionContainer.setVisibility(VISIBLE);
        mCalendarSeasonDescText.setText(TextUtils.join(", ", calendarCellObject.getAvailableTimes()));
    }

    @Override
    public void onDateChanged(Date selectedDate) {
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(selectedDate);
        }
    }

    private int getMonthLimitForCalendar() {
        return (mDaysLimit / 30);
    }

    public Date getFirstDayInCurrentMonth() {
        Calendar first = Calendar.getInstance();
        first.set(Calendar.YEAR, mCurrentYear);
        first.set(Calendar.MONTH, mCurrentMonth);
        first.set(Calendar.DAY_OF_MONTH, 1);
        first.set(Calendar.HOUR_OF_DAY, 0);
        first.set(Calendar.MINUTE, 0);
        first.set(Calendar.SECOND, 0);
        first.set(Calendar.MILLISECOND, 0);
        return first.getTime();
    }

    public Date getLastDayInCurrentMonth() {
        Calendar last = Calendar.getInstance();
        last.setTime(getFirstDayInCurrentMonth());
        last.set(Calendar.MONTH, last.get(Calendar.MONTH) + 1);
        last.set(Calendar.DATE, last.get(Calendar.DATE) - 1);
        return last.getTime();
    }

    public interface OnMonthChangeListener {
        void onMonthChange();
    }
}
