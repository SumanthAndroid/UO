package com.universalstudios.orlandoresort.controller.userinterface.explore.results;

import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.LoaderUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.OnDatabaseQueryChangeListener;

/**
 * @author jamestimberlake
 * @created 10/19/2015
 *
 * This class holds the classes needed to perform a search results
 * animation that happens when you filter the results of a park while on the explore page
 */
public class ExploreSearchResultsFragment extends DatabaseQueryFragment implements  LoaderManager.LoaderCallbacks<Cursor>, OnDatabaseQueryChangeListener {
    private static final String TAG = ExploreSearchResultsFragment.class.getSimpleName();

    private static final int LOAD_RESULT_COUNT = LoaderUtils.LOADER_ID_EXPLORE_RESULT_COUNT;

    private boolean isSearchAnimationFinished = true;
    private DatabaseQuery currentDatabaseQuery;
    private RelativeLayout mSearchResultsView;
    private TextView mSearchCountTextView;
    private TextView mSearchResultDescTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_explore_search_results, container, false);

        mSearchResultsView = (RelativeLayout) fragmentView.findViewById(R.id.fragment_explore_search_results_count_image);
        mSearchCountTextView = (TextView) fragmentView.findViewById(R.id.fragment_explore_search_results_count_text_view_result_header);
        mSearchResultDescTextView = (TextView) fragmentView.findViewById(R.id.fragment_explore_search_results_count_text_view_result_desc);

        Resources r = getActivity().getResources();
        //Typeface customTypeface = FontManager.getInstance().getTypeface(getActivity(), R.string.font_gotham_bold);
       // mSearchCountTextView.setTypeface(customTypeface);
        mSearchCountTextView.setTextColor(r.getColor(R.color.text_white));
       // mSearchResultDescTextView.setTypeface(customTypeface);
        mSearchResultDescTextView.setTextColor(r.getColor(R.color.text_white));
        return fragmentView;
    }

    /**
     * Searches the database query to get the result count
     * @param databaseQuery
     */
    public void onDatabaseQueryChange(DatabaseQuery databaseQuery){
        if(databaseQuery != null){
            currentDatabaseQuery = databaseQuery;
            if(isSearchAnimationFinished) {
                if (getLoaderManager().getLoader(LOAD_RESULT_COUNT) == null) {
                    getLoaderManager().initLoader(LOAD_RESULT_COUNT, null, this);
                } else {
                    getLoaderManager().restartLoader(LOAD_RESULT_COUNT, null, this);
                }
            }
        }
    }

    public void resetView(){
        if(mSearchResultsView.getAnimation() != null) {
            mSearchResultsView.getAnimation().cancel();
            isSearchAnimationFinished = true;
        }
        mSearchResultsView.setVisibility(View.GONE);
    }

    /**
     * Displays the animation for the search results. If ths resultsCount is zero, then the animation is not shown
     * @param resultsCount
     */
    private void showSearchResults(int resultsCount){
        if(isSearchAnimationFinished) {
            isSearchAnimationFinished = false;

            if(resultsCount > 0) {
                mSearchCountTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.explore_search_results_count_text_size_large));
                mSearchResultDescTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.explore_search_results_desc_text_size_large));
                mSearchCountTextView.setText(String.valueOf(resultsCount));
                mSearchResultDescTextView.setText(R.string.explore_search_results_desc_search_count);

            } else {
                mSearchCountTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.explore_search_results_count_text_size));
                mSearchResultDescTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.explore_search_results_desc_text_size));
                mSearchCountTextView.setText(R.string.explore_search_results_header_search_count_none);
                mSearchResultDescTextView.setText(R.string.list_no_results_message);
            }

            mSearchResultsView.setVisibility(View.VISIBLE);
            Animation animationOut = AnimationUtils.loadAnimation(this.getActivity(), R.anim.scale_up_fade_out);
            animationOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mSearchResultsView.setVisibility(View.GONE);
                    isSearchAnimationFinished = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mSearchResultsView.startAnimation(animationOut);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateLoader");
        }

        switch (id) {
            case LOAD_RESULT_COUNT:
                return LoaderUtils.createCursorLoader(currentDatabaseQuery);
            default:
                return super.onCreateLoader(id, bundle);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onLoadFinished");
        }

        if (currentDatabaseQuery != null) {
            int id = loader.getId();

            switch (id) {
                case LOAD_RESULT_COUNT:
                    int count = 0;

                    while (data.moveToNext()) {
                        count++;
                    }

                    showSearchResults(count);
                    break;
                default:
                    break;
            }
        }
        currentDatabaseQuery = null;
        loader.stopLoading();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
