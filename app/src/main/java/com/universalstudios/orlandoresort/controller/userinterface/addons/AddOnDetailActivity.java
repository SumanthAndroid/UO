package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarActivity;


public class AddOnDetailActivity extends ActionBarActivity {
    private static final String TAG = AddOnDetailActivity.class.getSimpleName();

    private static final String KEY_ARGS_TITLE = "KEY_ARGS_TITLE";
    private static final String KEY_ARGS_HEADER = "KEY_ARGS_HEADER";
    private static final String KEY_ARGS_HTML = "KEY_ARGS_HTML";

    private String mTitle;
    private String mHeader;
    private String mHtmlText;

    public static Intent newInstanceIntent(Context context, String title, String html) {
        Intent intent = new Intent(context, AddOnDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ARGS_TITLE, title);
        bundle.putString(KEY_ARGS_HTML, html);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceIntent(Context context, String title, String header, String html) {
        Intent intent = new Intent(context, AddOnDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ARGS_TITLE, title);
        bundle.putString(KEY_ARGS_HEADER, header);
        bundle.putString(KEY_ARGS_HTML, html);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_fragment_containing_activity);

        // Default parameters
        Bundle args = getIntent().getExtras();
        if (args == null) {
            setTitle(R.string.action_title_add_on_details);
        }
        // Otherwise, set incoming parameters
        else {
            mTitle = args.getString(KEY_ARGS_TITLE);
            mHeader = args.getString(KEY_ARGS_HEADER);
            mHtmlText = args.getString(KEY_ARGS_HTML);

            setTitle(mTitle);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            AddOnDetailFragment fragment = AddOnDetailFragment.newInstance(mHeader, mHtmlText);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
