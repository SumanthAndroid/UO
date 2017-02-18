package com.universalstudios.orlandoresort.controller.userinterface.tickets;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.res.Resources;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;
import com.universalstudios.orlandoresort.model.network.domain.appointments.CreateAppointmentTimeResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by GOKHAN on 8/7/2016.
 */
public class AppointmentTicketUtils {

    private static final String TAG = AppointmentTicketUtils.class.getSimpleName();

    public static String getFormattedDate(Long timeInMillis, Resources r) {
        if (timeInMillis == null) {
            return null;
        }

        Date dateToFormat = new Date(timeInMillis);

        // Get a calendar set to the beginning of the current day, in the user's timezone
        Calendar calendarToday = Calendar.getInstance(Locale.US);
        calendarToday.set(Calendar.HOUR_OF_DAY, 0);
        calendarToday.set(Calendar.MINUTE, 0);
        calendarToday.set(Calendar.SECOND, 0);
        calendarToday.set(Calendar.MILLISECOND, 0);

        Calendar calendarYesterday = Calendar.getInstance(Locale.US);
        calendarYesterday.add(Calendar.DATE, -1);
        calendarYesterday.set(Calendar.HOUR_OF_DAY, 0);
        calendarYesterday.set(Calendar.MINUTE, 0);
        calendarYesterday.set(Calendar.SECOND, 0);
        calendarYesterday.set(Calendar.MILLISECOND, 0);

        String formattedDate;
        if (dateToFormat.getTime() > calendarToday.getTimeInMillis()) {
            SimpleDateFormat sdfOutTime = new SimpleDateFormat("h:mm a", Locale.US);
            formattedDate = sdfOutTime.format(dateToFormat);
        }
        else if (dateToFormat.getTime()  > calendarYesterday.getTimeInMillis()) {
            formattedDate = r.getString(R.string.news_date_yesterday);
        }
        else {
            SimpleDateFormat sdfOutTime = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
            formattedDate = sdfOutTime.format(dateToFormat);
        }

        return formattedDate;
    }


    public static void updateAppointmentHasBeenReadInDatabase(final List<CreateAppointmentTimeResponse> appointmentTicketList, boolean hasBeenRead,
                                                              final ContentResolver contentResolver, boolean async) {
        if (contentResolver == null || appointmentTicketList == null) {
            return;
        }
        Boolean hasBeenReadDB;
        for (CreateAppointmentTimeResponse appointmentTickets : appointmentTicketList) {
            hasBeenReadDB = appointmentTickets.isHasBeeenRead();
            if (hasBeenReadDB == null || !appointmentTickets.isHasBeeenRead()) {
                updateAppointmentHasBeenReadInDatabase(appointmentTickets, hasBeenRead, contentResolver, async);
            }
        }
    }

    public static void updateAppointmentHasBeenReadInDatabase(final CreateAppointmentTimeResponse appointmentTime, boolean hasBeenRead,
                                                       final ContentResolver contentResolver, boolean async) {
        if (contentResolver == null || appointmentTime == null) {
            return;
        }

        // Update the Appointment object
        appointmentTime.setHasBeeenRead(hasBeenRead);

        final ContentValues contentValues = new ContentValues();
        contentValues.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_HAS_BEEN_READ, appointmentTime.isHasBeeenRead());
        contentValues.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_APPOINTMENT_TICKET_OBJECT_JSON, appointmentTime.toJson());

        final String where = new StringBuilder(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_TICKET_APPOINTMENT_ID)
                .append(" = ").append(appointmentTime.getAppointmentTimeId())
                .toString();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // Try to update the item
                    int itemsUpdated = contentResolver.update(UniversalOrlandoContentUris.TICKET_APPOINTMENTS, contentValues, where, null);

                    // If the item doesn't exist, log it
                    if (itemsUpdated == 0) {
                        if (BuildConfig.DEBUG) {
                            Log.w(TAG, "updateAppointmentHasBeenReadInDatabase: Appointment item does no exist in the database");
                        }
                    }
                }
                catch (Exception e) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "updateAppointmentHasBeenReadInDatabase: exception saving to database", e);
                    }

                    // Log the exception to crittercism
                    Crittercism.logHandledException(e);
                }
            }
        };

        // If requested, run asynchronously
        if (async) {
            new Thread(runnable).start();
        }
        // Otherwise, run synchronously
        else {
            runnable.run();
        }
    }
}