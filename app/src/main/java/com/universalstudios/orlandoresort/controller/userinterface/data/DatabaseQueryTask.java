package com.universalstudios.orlandoresort.controller.userinterface.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/21/16.
 * Class: DatabaseQueryTask
 * Class Description: Task to run a database Query
 */
public class DatabaseQueryTask<T extends DatabaseUnwrapper> extends AsyncTask<Void, Void, List<T>> {
    public static final String TAG = "DatabaseQueryTask";

    private DatabaseQuery databaseQuery;
    private Class<? extends DatabaseUnwrapper> tClass;
    private DatabaseQueryListener listener;

    public DatabaseQueryTask(DatabaseQuery databaseQuery, Class<? extends DatabaseUnwrapper> tClass, DatabaseQueryListener listener) {
        this.databaseQuery = databaseQuery;
        this.tClass = tClass;
        this.listener = listener;
    }

    @Override
    protected List<T> doInBackground(Void... params) {
        List<T> list = new ArrayList<>();
        Context context = UniversalOrlandoApplication.getAppContext();
        if (null != context) {
            ContentResolver resolver = context.getContentResolver();
            Cursor cursor = resolver.query(databaseQuery.getContentUri(), databaseQuery.getProjection(),
                    databaseQuery.getSelection(), databaseQuery.getSelectionArgs(), databaseQuery.getOrderBy());

            if (null != cursor && cursor.moveToFirst()) {
                try {
                    do {
                        T object = (T) tClass.newInstance().unwrap(cursor);
                        list.add(object);
                    } while (cursor.moveToNext());
                } catch (IllegalAccessException e) {

                } catch (InstantiationException ex) {

                } catch (ClassCastException e) {

                } finally {
                    cursor.close();
                }
            }
        }

        return list;
    }

    @Override
    protected void onPostExecute(List<T> ts) {
        super.onPostExecute(ts);
        if (null != listener) {
            listener.onQueryComplete(ts);
        }

    }
}
