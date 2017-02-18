/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.events;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * Used to limit the perceived amount of event returned from the cursor
 * 
 * @author acampbell
 *
 */
public class EventCursorWrapper extends CursorWrapper {

    private int mCount;

    public EventCursorWrapper(Cursor cursor, int count) {
        super(cursor);
        mCount = count;
    }

    @Override
    public int getCount() {
        return mCount;
    }

}
