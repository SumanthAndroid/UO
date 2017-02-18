package com.universalstudios.orlandoresort.controller.userinterface.detail;

import java.lang.reflect.Type;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.detail.DiningMenuDetailPagerAdapter.OnImageTouchedListener;
import com.universalstudios.orlandoresort.view.fonts.TextView;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager;

/**
 * @author acampbell
 *
 */
public class DiningMenuDetailFragment extends Fragment implements OnClickListener, OnPageChangeListener,
        OnImageTouchedListener {

    private static final String TAG = DiningMenuDetailFragment.class.getSimpleName();

    private static final String KEY_ARG_MENU_IMAGES = "KEY_ARG_MENU_IMAGES";
    private static final int PAGER_PAGE_MARGIN_SIZE = 10;

    private List<String> mMenuImages;
    private JazzyViewPager mViewPager;
    private TextView mPageTitleTextView;
    private DiningMenuDetailPagerAdapter mAdapter;
    private View mInstructionsContainer;

    public static DiningMenuDetailFragment newInstance(List<String> menuImages) {
        DiningMenuDetailFragment fragment = new DiningMenuDetailFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ARG_MENU_IMAGES, new Gson().toJson(menuImages));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        // Default parameters
        Bundle args = getArguments();
        if (args == null) {}
        // Otherwise, set incoming parameters
        else {
            Type type = new TypeToken<List<String>>() {}.getType();
            mMenuImages = new Gson().fromJson(args.getString(KEY_ARG_MENU_IMAGES), type);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {}
        // Otherwise restore state, overwriting any passed in parameters
        else {}
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }

        View view = inflater.inflate(R.layout.fragment_dining_menu, container, false);

        // Setup views
        view.findViewById(R.id.fragment_dining_menu_close_imageview).setOnClickListener(this);
        mPageTitleTextView = (TextView) view.findViewById(R.id.fragment_dining_menu_page_textview);
        mInstructionsContainer = view.findViewById(R.id.fragment_dining_menu_instructions_container);
        mViewPager = (JazzyViewPager) view.findViewById(R.id.fragment_dining_menu_view_pager);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int marginSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PAGER_PAGE_MARGIN_SIZE,
                displayMetrics);
        mViewPager.setPageMargin(marginSize);
        // Menu images
        mViewPager.setVisibility(View.VISIBLE);
        mPageTitleTextView.setVisibility(View.VISIBLE);

        mAdapter = new DiningMenuDetailPagerAdapter(this, getActivity(), mMenuImages);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setAdapter(mAdapter);
        onPageSelected(mViewPager.getCurrentItem());

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {}
        // Otherwise restore state, overwriting any passed in parameters
        else {}

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_dining_menu_close_imageview:
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {}

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {}

    @Override
    public void onPageSelected(int position) {
        mPageTitleTextView.setText(mAdapter.getPageTitle(position));
    }

    private void hideInstructions() {
        if (mInstructionsContainer != null) {
            mInstructionsContainer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onImageTouched() {
        hideInstructions();
    }
}
