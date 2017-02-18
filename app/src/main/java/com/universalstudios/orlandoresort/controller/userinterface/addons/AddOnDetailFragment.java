package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.util.HashMap;
import java.util.Map;


public class AddOnDetailFragment extends Fragment {
    private static final String TAG = AddOnDetailFragment.class.getSimpleName();

    private static final String KEY_ARGS_HEADER = "KEY_ARGS_HEADER";
    private static final String KEY_ARGS_HTML = "KEY_ARGS_HTML";

    private TextView mHeader;
    private TextView mDetails;
    private String mHeaderText;
    private String mHtmlText;

    public static AddOnDetailFragment newInstance(String header, String html) {
        AddOnDetailFragment fragment = new AddOnDetailFragment();
        Bundle b = new Bundle();
        b.putString(KEY_ARGS_HEADER, header);
        b.putString(KEY_ARGS_HTML, html);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAttach");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate");
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            mHeaderText = bundle.getString(KEY_ARGS_HEADER);
            mHtmlText = bundle.getString(KEY_ARGS_HTML);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView");
        }

        View layout = inflater.inflate(R.layout.fragment_add_on_details, container, false);
        mHeader = (TextView) layout.findViewById(R.id.fragment_add_on_detail_header);
        mDetails = (TextView) layout.findViewById(R.id.fragment_add_on_detail_details);

        if (!TextUtils.isEmpty(mHeaderText)) {
            mHeader.setText(Html.fromHtml(mHeaderText));
        } else {
            mHeader.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mHtmlText)) {
            mDetails.setText(Html.fromHtml(mHtmlText));
        }

        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onActivityCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }
        sendOnPageLoadAnalytics();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewStateRestored: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStart");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onResume");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onPause");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onSaveInstanceState");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStop");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onDestroyView");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onDestroy");
        }
    }

    private void sendOnPageLoadAnalytics() {
        Map<String, Object> extraData = new HashMap<String, Object>();
        extraData.put(AnalyticsUtils.KEY_EVENTS, "event2,");
        AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_ADDON,
                mHtmlText,
                null,
                null,
                null,
                null,
                extraData);
    }
}
