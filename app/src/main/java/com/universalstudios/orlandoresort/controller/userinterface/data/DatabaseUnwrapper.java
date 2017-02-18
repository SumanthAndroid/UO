package com.universalstudios.orlandoresort.controller.userinterface.data;

import android.database.Cursor;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/21/16.
 * Class: DatabaseUnwrapper
 * Class Description: Interface for Objects that will be stored in the database
 */

public interface DatabaseUnwrapper<T> {
    T unwrap(Cursor cursor);
}
