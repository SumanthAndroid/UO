package com.universalstudios.orlandoresort.controller.userinterface.explore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarActivity;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.explore.list.ExploreListFragment;

/**
 * Created by kevin haines on 6/6/16.
 */
public class ClusteredExploreActivity extends ActionBarActivity {
    public static final String TAG = "clustrdexplractvty";

    private static final String ARG_QUERY = "ARG_QUERY";
    private static final String ARG_TYPE = "ARG_TYPE";

    public static Intent createIntent(Context context, String query, ExploreType exploreType) {
        Intent i = new Intent(context, ClusteredExploreActivity.class);
        i.putExtra(ARG_QUERY, query);
        i.putExtra(ARG_TYPE, exploreType);
        return i;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        String rawQuery = getIntent().getExtras().getString(ARG_QUERY);
        ExploreType type = (ExploreType) getIntent().getExtras().getSerializable(ARG_TYPE);
        if (null != rawQuery) {
            DatabaseQuery query = DatabaseQuery.fromJson(rawQuery, DatabaseQuery.class);
            ExploreListFragment exploreListFragment = ExploreListFragment.newInstance(query, type, (Long) null, true);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_explore_fragment_container, exploreListFragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
