/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.filter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.LoaderUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author acampbell
 *
 */
public class MultiChoiceFilterDialogFragment extends DialogFragment implements LoaderCallbacks<Cursor>,
        OnClickListener {

    private static final String TAG = MultiChoiceFilterDialogFragment.class.getSimpleName();
    private static final String KEY_ARG_FILTER_OPTIONS = "KEY_ARG_FILTER_OPTIONS";
    private static final String KEY_ARG_ITEMS = "KEY_ARG_ITEMS";
    private static final String KEY_ARG_MULTI_SELECT_TYPE = "KEY_ARG_MULTI_SELECT_TYPE";
    private static final String KEY_LOADER_ARGS_DATABASE_QUERY = "KEY_LOADER_ARGS_DATABASE_QUERY";
    private static final String KEY_STATE_FILTER_OPTIONS = "KEY_STATE_FILTER_OPTIONS";
    private static final String KEY_STATE_ITEMS = "KEY_STATE_ITEMS";
    private static final String KEY_STATE_MULTI_SELECT_TYPE = "KEY_STATE_MULTI_SELECT_TYPE";
    private static final int LOADER_ID = LoaderUtils.LOADER_ID_MULTI_CHOICE_FILTER_FRAGMENT;

    private FilterOptions mFilterOptions;
    private BaseAdapter mAdapter;
    private ArrayList<String> mItems;
    private MultiChoiceOptionsType mMultiChoiceOptionsType;
    private ListView mListView;
    private TextView mTitleTextView;
    private OnMultiChoiceFragmentCloseListener mListener;

    private boolean mDataLoaded = false;

    /**
     * Interface used to pass selected options back to the
     * AdvancedFilterActivity and ultimately the AdvancedFilterFragment
     * 
     * @author acampbell
     *
     */
    public interface OnMultiChoiceFragmentCloseListener {

        void onMultiChoiceFragmentClose(MultiChoiceOptionsType multiSelectOptionsType,
                List<String> selectedItems);
    }

    public enum MultiChoiceOptionsType {
        ACCESSIBILITY,
        RIDE_VENUE,
        RIDE_TYPE,
        SHOW_VENUE,
        SHOW_TYPE,
        DINING_VENUE,
        DINING_TYPE,
        DINING_PLAN,
        SHOPPING_VENUE,
        SHOPPING_TYPE,
        ENTERTAINMENT_TYPE,
        RENTAL_SERVICES_VENUE
    }

    public static MultiChoiceFilterDialogFragment newInstance(FilterOptions filterOptions,
            MultiChoiceOptionsType multiSelectOptionsType) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance");
        }

        MultiChoiceFilterDialogFragment fragment = new MultiChoiceFilterDialogFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ARG_FILTER_OPTIONS, filterOptions.toJson());
        args.putSerializable(KEY_ARG_MULTI_SELECT_TYPE, multiSelectOptionsType);
        fragment.setArguments(args);

        return fragment;
    }

    public static MultiChoiceFilterDialogFragment newInstance(FilterOptions filterOptions, String[] items,
            MultiChoiceOptionsType multiSelectOptionsType) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance");
        }

        MultiChoiceFilterDialogFragment fragment = new MultiChoiceFilterDialogFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ARG_FILTER_OPTIONS, filterOptions.toJson());
        ArrayList<String> tempItems = new ArrayList<String>();
        Collections.addAll(tempItems, items);
        args.putStringArrayList(KEY_ARG_ITEMS, tempItems);
        args.putSerializable(KEY_ARG_MULTI_SELECT_TYPE, multiSelectOptionsType);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onAttach");
        }

        // Attach activity as listener
        if (activity instanceof OnMultiChoiceFragmentCloseListener) {
            mListener = (OnMultiChoiceFragmentCloseListener) activity;
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "onAttach: Activity must implement OnAccessibilityFragmentCloseListener");
            }
            IllegalArgumentException e = new IllegalArgumentException(
                    "Activity must implement OnAccessibilityFragmentCloseListener");
            throw e;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate");
        }

        Bundle args = getArguments();
        if (args != null) {
            mFilterOptions = GsonObject.fromJson(args.getString(KEY_ARG_FILTER_OPTIONS), FilterOptions.class);
            mItems = args.getStringArrayList(KEY_ARG_ITEMS);
            mMultiChoiceOptionsType = (MultiChoiceOptionsType) args
                    .getSerializable(KEY_ARG_MULTI_SELECT_TYPE);
        }
        if (savedInstanceState != null) {
            mFilterOptions = GsonObject.fromJson(savedInstanceState.getString(KEY_STATE_FILTER_OPTIONS),
                    FilterOptions.class);
            mItems = savedInstanceState.getStringArrayList(KEY_STATE_ITEMS);
            mMultiChoiceOptionsType = (MultiChoiceOptionsType) savedInstanceState
                    .getSerializable(KEY_STATE_MULTI_SELECT_TYPE);
        }

        setStyle(STYLE_NO_TITLE, getTheme());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateView");
        }

        View view = inflater
                .inflate(R.layout.fragment_advanced_filter_multi_choice_options, container, false);
        mListView = (ListView) view.findViewById(R.id.advanced_filter_multi_choice_listview);
        mTitleTextView = (TextView) view.findViewById(R.id.advanced_filter_multi_choice_title_textview);
        view.findViewById(R.id.advanced_filter_multi_choice_cancel_button).setOnClickListener(this);
        view.findViewById(R.id.advanced_filter_multi_choice_ok_button).setOnClickListener(this);

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateDialog");
        }
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onResume");
        }

        String title = null;
        // Set listview choice mode
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        switch (mMultiChoiceOptionsType) {
            case ACCESSIBILITY: {
                title = getString(R.string.advanced_filter_other_accessibility);
                mAdapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.advanced_filter_multi_choice_checked_item, mItems);
                mListView.setAdapter(mAdapter);
                List<String> accessibilityFlags = FilterOptions.getAccessibilityFlags();
                // Set selected items as checked
                for (int i = 0; i < accessibilityFlags.size(); i++) {
                    if (mFilterOptions.getAccessibilityOptions().contains(accessibilityFlags.get(i))) {
                        mListView.setItemChecked(i, true);
                    }
                }
                break;
            }
            case RIDE_VENUE: {
                title = getString(R.string.advanced_filter_venues);
                mAdapter = new SimpleCursorAdapter(getActivity(),
                        R.layout.advanced_filter_multi_choice_checked_item, null,
                        new String[] { VenuesTable.COL_DISPLAY_NAME }, new int[] { android.R.id.text1 }, 0);
                mListView.setAdapter(mAdapter);
                DatabaseQuery databaseQuery = DatabaseQueryUtils.getVenuesForTypeDatabaseQuery(Arrays
                        .asList("" + PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE));
                Bundle args = new Bundle();
                args.putString(KEY_LOADER_ARGS_DATABASE_QUERY, databaseQuery.toJson());
                getLoaderManager().initLoader(LOADER_ID, args, this);
                break;
            }
            case SHOW_VENUE: {
                title = getString(R.string.advanced_filter_venues);
                mAdapter = new SimpleCursorAdapter(getActivity(),
                        R.layout.advanced_filter_multi_choice_checked_item, null,
                        new String[] { VenuesTable.COL_DISPLAY_NAME }, new int[] { android.R.id.text1 }, 0);
                mListView.setAdapter(mAdapter);
                DatabaseQuery databaseQuery = DatabaseQueryUtils.getVenuesForTypeDatabaseQuery(Arrays.asList(
                        "" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW, ""
                                + PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE));
                Bundle args = new Bundle();
                args.putString(KEY_LOADER_ARGS_DATABASE_QUERY, databaseQuery.toJson());
                getLoaderManager().initLoader(LOADER_ID, args, this);
                break;
            }
            case RIDE_TYPE: {
                title = getString(R.string.advanced_filter_ride_type);
                mAdapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.advanced_filter_multi_choice_checked_item, mItems);
                mListView.setAdapter(mAdapter);
                List<String> rideSubTypeFlags = FilterOptions.getRideSubTypeFlags();

                // No ride sub-types selected so select them all
                if (mFilterOptions.getRideSubTypes().size() == 0) {
                    for (int i = 0; i < rideSubTypeFlags.size(); i++) {
                        mListView.setItemChecked(i, true);
                    }
                } else {
                    // Set selected items as checked
                    for (int i = 0; i < rideSubTypeFlags.size(); i++) {
                        if (mFilterOptions.getRideSubTypes().contains(rideSubTypeFlags.get(i))) {
                            mListView.setItemChecked(i, true);
                        }
                    }
                }
                break;
            }
            case SHOW_TYPE: {
                title = getString(R.string.advanced_filter_show_type);
                mAdapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.advanced_filter_multi_choice_checked_item, mItems);
                mListView.setAdapter(mAdapter);
                List<String> showSubTypeFlags = FilterOptions.getShowSubTypeFlags();

                // No ride sub-types selected so select them all
                if (mFilterOptions.getShowSubTypes().size() == 0) {
                    for (int i = 0; i < showSubTypeFlags.size(); i++) {
                        mListView.setItemChecked(i, true);
                    }
                } else {
                    // Set selected items as checked
                    for (int i = 0; i < showSubTypeFlags.size(); i++) {
                        if (mFilterOptions.getShowSubTypes().contains(showSubTypeFlags.get(i))) {
                            mListView.setItemChecked(i, true);
                        }
                    }
                }
                break;
            }
            case DINING_VENUE: {
                title = getString(R.string.advanced_filter_venues);
                mAdapter = new SimpleCursorAdapter(getActivity(),
                        R.layout.advanced_filter_multi_choice_checked_item, null,
                        new String[] { VenuesTable.COL_DISPLAY_NAME }, new int[] { android.R.id.text1 }, 0);
                mListView.setAdapter(mAdapter);
                DatabaseQuery databaseQuery = DatabaseQueryUtils.getVenuesForTypeDatabaseQuery(Arrays
                        .asList("" + PointsOfInterestTable.VAL_POI_TYPE_ID_DINING));
                Bundle args = new Bundle();
                args.putString(KEY_LOADER_ARGS_DATABASE_QUERY, databaseQuery.toJson());
                getLoaderManager().initLoader(LOADER_ID, args, this);
                break;
            }
            case DINING_TYPE: {
                title = getString(R.string.advanced_filter_dining_type);
                mAdapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.advanced_filter_multi_choice_checked_item, mItems);
                mListView.setAdapter(mAdapter);
                List<String> diningSubTypeFlags = FilterOptions.getDiningSubTypeFlags();

                // No dining sub-types selected so select them all
                if (mFilterOptions.getDiningSubTypes().size() == 0) {
                    for (int i = 0; i < diningSubTypeFlags.size(); i++) {
                        mListView.setItemChecked(i, true);
                    }
                } else {
                    // Set selected items as checked
                    for (int i = 0; i < diningSubTypeFlags.size(); i++) {
                        if (mFilterOptions.getDiningSubTypes().contains(diningSubTypeFlags.get(i))) {
                            mListView.setItemChecked(i, true);
                        }
                    }
                }
                break;
            }
            case DINING_PLAN: {
                title = getString(R.string.advanced_filter_dining_universal_dining_plan);
                mAdapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.advanced_filter_multi_choice_checked_item, mItems);
                mListView.setAdapter(mAdapter);
                List<String> diningPlanFlags = FilterOptions.getDiningPlanFlags();
                // Set selected items as checked
                for (int i = 0; i < diningPlanFlags.size(); i++) {
                    if (mFilterOptions.getDiningPlans().contains(diningPlanFlags.get(i))) {
                        mListView.setItemChecked(i, true);
                    }
                }
                break;
            }
            case SHOPPING_VENUE: {
                title = getString(R.string.advanced_filter_venues);
                mAdapter = new SimpleCursorAdapter(getActivity(),
                        R.layout.advanced_filter_multi_choice_checked_item, null,
                        new String[] { VenuesTable.COL_DISPLAY_NAME }, new int[] { android.R.id.text1 }, 0);
                mListView.setAdapter(mAdapter);
                DatabaseQuery databaseQuery = DatabaseQueryUtils.getVenuesForTypeDatabaseQuery(Arrays
                        .asList("" + PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP));
                Bundle args = new Bundle();
                args.putString(KEY_LOADER_ARGS_DATABASE_QUERY, databaseQuery.toJson());
                getLoaderManager().initLoader(LOADER_ID, args, this);
                break;
            }
            case SHOPPING_TYPE: {
                title = getString(R.string.advanced_filter_shopping_type);
                mAdapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.advanced_filter_multi_choice_checked_item, mItems);
                mListView.setAdapter(mAdapter);
                List<String> shoppingSubTypeFlags = FilterOptions.getShoppingSubTypeFlags();

                // No shopping sub-types selected so select them all
                if (mFilterOptions.getShoppingSubTypes().size() == 0) {
                    for (int i = 0; i < shoppingSubTypeFlags.size(); i++) {
                        mListView.setItemChecked(i, true);
                    }
                } else {
                    // Set selected items as checked
                    for (int i = 0; i < shoppingSubTypeFlags.size(); i++) {
                        if (mFilterOptions.getShoppingSubTypes().contains(shoppingSubTypeFlags.get(i))) {
                            mListView.setItemChecked(i, true);
                        }
                    }
                }
                break;
            }
            case ENTERTAINMENT_TYPE: {
                title = getString(R.string.advanced_filter_entertainment_type);
                mAdapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.advanced_filter_multi_choice_checked_item, mItems);
                mListView.setAdapter(mAdapter);
                List<String> entertainmentSubTypeFlags = FilterOptions.getEntertainmentSubTypeFlags();

                // No entertainment sub-types selected so select them all
                if (mFilterOptions.getEntertainmentSubTypes().size() == 0) {
                    for (int i = 0; i < entertainmentSubTypeFlags.size(); i++) {
                        mListView.setItemChecked(i, true);
                    }
                } else {
                    // Set selected items as checked
                    for (int i = 0; i < entertainmentSubTypeFlags.size(); i++) {
                        if (mFilterOptions.getEntertainmentSubTypes().contains(entertainmentSubTypeFlags.get(i))) {
                            mListView.setItemChecked(i, true);
                        }
                    }
                }
                break;
            }
            case RENTAL_SERVICES_VENUE: {
                title = getString(R.string.advanced_filter_venues);
                mAdapter = new SimpleCursorAdapter(getActivity(),
                        R.layout.advanced_filter_multi_choice_checked_item, null,
                        new String[] { VenuesTable.COL_DISPLAY_NAME }, new int[] { android.R.id.text1 }, 0);
                mListView.setAdapter(mAdapter);
                DatabaseQuery databaseQuery = DatabaseQueryUtils.getVenuesForTypeDatabaseQuery(Arrays
                        .asList("" + PointsOfInterestTable.VAL_POI_TYPE_ID_RENTALS));
                Bundle args = new Bundle();
                args.putString(KEY_LOADER_ARGS_DATABASE_QUERY, databaseQuery.toJson());
                getLoaderManager().initLoader(LOADER_ID, args, this);
                break;
            }

            default:
                break;
        }
        // dialog title
        mTitleTextView.setText(title);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onSaveInstanceState");
        }

        outState.putString(KEY_STATE_FILTER_OPTIONS, mFilterOptions.toJson());
        outState.putStringArrayList(KEY_STATE_ITEMS, mItems);
        outState.putSerializable(KEY_STATE_MULTI_SELECT_TYPE, mMultiChoiceOptionsType);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "show");
        }
        // Prevent two dialogs from appearing if view double tapped
        if (manager.findFragmentByTag(MultiChoiceFilterDialogFragment.class.getSimpleName()) == null) {
            super.show(manager, tag);
        }
    }

    @Override
    public void onClick(View v) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onClick");
        }
        switch (v.getId()) {
            case R.id.advanced_filter_multi_choice_cancel_button:
                getDialog().cancel();
                break;
            case R.id.advanced_filter_multi_choice_ok_button:
                setChoices();
                getDialog().dismiss();
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onDismiss");
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCancel");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateLoader: id = " + id);
        }
        switch (id) {
            case LOADER_ID:
                DatabaseQuery databaseQuery = GsonObject.fromJson(
                        args.getString(KEY_LOADER_ARGS_DATABASE_QUERY), DatabaseQuery.class);
                return LoaderUtils.createCursorLoader(databaseQuery);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onLoadFinished");
        }
        if (loader.getId() == LOADER_ID) {
            // Handle cursor depending on multi-choice type
            switch (mMultiChoiceOptionsType) {
                case SHOW_VENUE: {
                    if (mAdapter instanceof CursorAdapter) {
                        CursorAdapter cursorAdapter = (CursorAdapter) mAdapter;
                        cursorAdapter.swapCursor(data);
                        // Select all items if none are selected
                        if (mFilterOptions.getShowFilterVenues() == null
                                || mFilterOptions.getShowFilterVenues().isEmpty()) {
                            for (int i = 0; i < cursorAdapter.getCount(); i++) {
                                mListView.setItemChecked(i, true);
                            }
                        } else {
                            // Set selected items as checked
                            if (data != null && data.moveToFirst()) {
                                do {
                                    String venueId = data.getString(data
                                            .getColumnIndex(VenuesTable.COL_VENUE_ID));
                                    if (mFilterOptions.getShowFilterVenues() != null
                                            && mFilterOptions.getShowFilterVenues().contains(venueId)) {
                                        mListView.setItemChecked(data.getPosition(), true);
                                    }
                                } while (data.moveToNext());
                            }
                        }
                    }
                    break;
                }
                case RIDE_VENUE: {
                    if (mAdapter instanceof CursorAdapter) {
                        CursorAdapter cursorAdapter = (CursorAdapter) mAdapter;
                        cursorAdapter.swapCursor(data);
                        //Allow the cursor to swap but don't reinitialize the ui
                        //on every cursor load (loads many times therefore re-checking unchecked items)
                        if (mDataLoaded) {
                            return;
                        } else {
                            mDataLoaded = true;
                        }
                        if (mFilterOptions.getRideFilterVenues() == null
                                || mFilterOptions.getRideFilterVenues().isEmpty()) {
                            for (int i = 0; i < cursorAdapter.getCount(); i++) {
                                mListView.setItemChecked(i, true);
                            }
                        } else {
                            // Set selected items as checked
                            if (data != null && data.moveToFirst()) {
                                do {
                                    String venueId = data.getString(data
                                            .getColumnIndex(VenuesTable.COL_VENUE_ID));
                                    if (mFilterOptions.getRideFilterVenues() != null
                                            && mFilterOptions.getRideFilterVenues().contains(venueId)) {
                                        mListView.setItemChecked(data.getPosition(), true);
                                    }
                                } while (data.moveToNext());
                            }
                        }
                    }
                    break;
                }
                case DINING_VENUE: {
                    if (mAdapter instanceof CursorAdapter) {
                        CursorAdapter cursorAdapter = (CursorAdapter) mAdapter;
                        cursorAdapter.swapCursor(data);
                        // Select all items if none are selected
                        if (mFilterOptions.getDiningFilterVenues() == null
                                || mFilterOptions.getDiningFilterVenues().isEmpty()) {
                            for (int i = 0; i < cursorAdapter.getCount(); i++) {
                                mListView.setItemChecked(i, true);
                            }
                        } else {
                            // Set selected items as checked
                            if (data != null && data.moveToFirst()) {
                                do {
                                    String venueId = data.getString(data
                                            .getColumnIndex(VenuesTable.COL_VENUE_ID));
                                    if (mFilterOptions.getDiningFilterVenues() != null
                                            && mFilterOptions.getDiningFilterVenues().contains(venueId)) {
                                        mListView.setItemChecked(data.getPosition(), true);
                                    }
                                } while (data.moveToNext());
                            }
                        }
                    }
                    break;
                }
                case SHOPPING_VENUE: {
                    if (mAdapter instanceof CursorAdapter) {
                        CursorAdapter cursorAdapter = (CursorAdapter) mAdapter;
                        cursorAdapter.swapCursor(data);
                        // Select all items if none are selected
                        if (mFilterOptions.getShoppingFilterVenues() == null
                                || mFilterOptions.getShoppingFilterVenues().isEmpty()) {
                            for (int i = 0; i < cursorAdapter.getCount(); i++) {
                                mListView.setItemChecked(i, true);
                            }
                        } else {
                            // Set selected items as checked
                            if (data != null && data.moveToFirst()) {
                                do {
                                    String venueId = data.getString(data
                                            .getColumnIndex(VenuesTable.COL_VENUE_ID));
                                    if (mFilterOptions.getShoppingFilterVenues() != null
                                            && mFilterOptions.getShoppingFilterVenues().contains(venueId)) {
                                        mListView.setItemChecked(data.getPosition(), true);
                                    }
                                } while (data.moveToNext());
                            }
                        }
                    }
                    break;
                }

                case RENTAL_SERVICES_VENUE: {
                    if (mAdapter instanceof CursorAdapter) {
                        CursorAdapter cursorAdapter = (CursorAdapter) mAdapter;
                        cursorAdapter.swapCursor(data);
                        // Select all items if none are selected
                        if (mFilterOptions.getRentalServicesFilterVenues() == null
                                || mFilterOptions.getRentalServicesFilterVenues().isEmpty()) {
                            for (int i = 0; i < cursorAdapter.getCount(); i++) {
                                mListView.setItemChecked(i, true);
                            }
                        } else {
                            // Set selected items as checked
                            if (data != null && data.moveToFirst()) {
                                do {
                                    String venueId = data.getString(data
                                            .getColumnIndex(VenuesTable.COL_VENUE_ID));
                                    if (mFilterOptions.getRentalServicesFilterVenues() != null
                                            && mFilterOptions.getRentalServicesFilterVenues().contains(venueId)) {
                                        mListView.setItemChecked(data.getPosition(), true);
                                    }
                                } while (data.moveToNext());
                            }
                        }
                    }
                    break;
                }

                default:
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onLoaderReset");
        }
        switch (loader.getId()) {
            case LOADER_ID:
                if (mAdapter instanceof CursorAdapter) {
                    CursorAdapter cursorAdapter = (CursorAdapter) mAdapter;
                    cursorAdapter.swapCursor(null);
                }
                break;
            default:
                break;
        }
    }

    private void setChoices() {
        if (mListener != null) {
            List<String> selectedItems = new ArrayList<String>();
            SparseBooleanArray checkedPositions = mListView.getCheckedItemPositions();

            switch (mMultiChoiceOptionsType) {
                case ACCESSIBILITY: {
					// Track the event
					AnalyticsUtils.Builder extraData = new AnalyticsUtils.Builder();

                    String analyticValueStr = "";

                    List<String> accessibilityflags = FilterOptions.getAccessibilityFlags();
                    for (int i = 0; i < accessibilityflags.size(); i++) {
                        if (checkedPositions.get(i)) {
                            selectedItems.add(accessibilityflags.get(i));
                            String accessibilityName = FilterOptions.getPropNameForAccessibilityFlag(accessibilityflags.get(i));
                            if(!analyticValueStr.isEmpty()){
                                analyticValueStr = analyticValueStr.concat(",");
                            }

                           analyticValueStr = analyticValueStr.concat(accessibilityName);
                        }
                    }

                    extraData.setProperty(30, analyticValueStr); 

                    AnalyticsUtils.trackEvent(
                            AnalyticsUtils.EVENT_NAME_ACCESSIBILITY_OPTIONS,
                            null,
                            null,
                            extraData.build());
                    break;
                }
                case SHOPPING_VENUE:
                case RENTAL_SERVICES_VENUE:
                case DINING_VENUE:
                case SHOW_VENUE:
                case RIDE_VENUE: {
                    if (mAdapter instanceof CursorAdapter) {
                        CursorAdapter cursorAdapter = (CursorAdapter) mAdapter;
                        // Since this is a query related list, there's no way of
                        // knowing how many options were available in the
                        // AdvancedFilterFragment so if all choices were chosen
                        // return none
                        if (mAdapter.getCount() == mListView.getCheckedItemCount()) {
                            break;
                        }
                        for (int i = 0; i < mAdapter.getCount(); i++) {
                            if (checkedPositions.get(i)) {
                                Cursor cursor = (Cursor) cursorAdapter.getItem(i);
                                String venueId = cursor.getString(cursor
                                        .getColumnIndex(VenuesTable.COL_VENUE_ID));
                                selectedItems.add(venueId);
                            }
                        }
                    }
                    break;
                }
                case RIDE_TYPE: {
                    List<String> rideSubTypeFlags = FilterOptions.getRideSubTypeFlags();
                    for (int i = 0; i < rideSubTypeFlags.size(); i++) {
                        if (checkedPositions.get(i)) {
                            selectedItems.add(rideSubTypeFlags.get(i));
                        }
                    }
                    break;
                }
                case SHOW_TYPE: {
                    List<String> showSubTypeFlags = FilterOptions.getShowSubTypeFlags();
                    for (int i = 0; i < showSubTypeFlags.size(); i++) {
                        if (checkedPositions.get(i)) {
                            selectedItems.add(showSubTypeFlags.get(i));
                        }
                    }
                    break;
                }
                case DINING_TYPE: {
                    List<String> diningSubTypeFlags = FilterOptions.getDiningSubTypeFlags();
                    for (int i = 0; i < diningSubTypeFlags.size(); i++) {
                        if (checkedPositions.get(i)) {
                            selectedItems.add(diningSubTypeFlags.get(i));
                        }
                    }
                    break;
                }
                case DINING_PLAN: {
                    List<String> diningPlanFlags = FilterOptions.getDiningPlanFlags();
                    for (int i = 0; i < diningPlanFlags.size(); i++) {
                        if (checkedPositions.get(i)) {
                            selectedItems.add(diningPlanFlags.get(i));
                        }
                    }
                    break;
                }
                case SHOPPING_TYPE: {
                    List<String> shoppingSubTypeFlags = FilterOptions.getShoppingSubTypeFlags();
                    for (int i = 0; i < shoppingSubTypeFlags.size(); i++) {
                        if (checkedPositions.get(i)) {
                            selectedItems.add(shoppingSubTypeFlags.get(i));
                        }
                    }
                    break;
                }
                case ENTERTAINMENT_TYPE: {
                    List<String> entertainmentSubTypeFlags = FilterOptions.getEntertainmentSubTypeFlags();
                    for (int i = 0; i < entertainmentSubTypeFlags.size(); i++) {
                        if (checkedPositions.get(i)) {
                            selectedItems.add(entertainmentSubTypeFlags.get(i));
                        }
                    }
                    break;
                }
                default:
                    break;
            }
            mListener.onMultiChoiceFragmentClose(mMultiChoiceOptionsType, selectedItems);
        }
    }

}
