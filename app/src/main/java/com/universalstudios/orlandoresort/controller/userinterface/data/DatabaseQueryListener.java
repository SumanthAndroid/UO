package com.universalstudios.orlandoresort.controller.userinterface.data;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/21/16.
 * Class: DatabaseQueryListener
 * Class Description: TODO: ALWAYS FILL OUT
 */
public interface DatabaseQueryListener<T> {
    public static final String TAG = "DatabaseQueryListener";

    void onQueryComplete(T result);
}
