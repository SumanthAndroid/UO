package com.universalstudios.orlandoresort.controller.userinterface.help;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.MobilePagesFragment;
import com.universalstudios.orlandoresort.controller.userinterface.detail.FeatureListUtils;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.model.network.image.ImageUtils;
import com.universalstudios.orlandoresort.model.network.request.RequestQueryParams;
import com.universalstudios.orlandoresort.utils.ClipBoardUtils;

/**
 * Ticket assistance fragment
 * Created by Nicholas Hanna on 11/9/2016.
 */
public class TicketAssistanceFragment extends MobilePagesFragment implements View.OnClickListener, View.OnLongClickListener {

    public static final String TAG = TicketAssistanceFragment.class.getSimpleName();

    private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";
    private static final String VIEW_TAG_PHONE_NUMBER = "VIEW_TAG_PHONE_NUMBER";

    private static final String PAGE_IDENTIFIER = "TICKET_ASSISTANCE";

    private int mActionBarTitleResId;
    private int mCalculatedImageHeightDp;

    private DrawerStateProvider mParentDrawerStateProvider;
    private ImageView mHeroImage;
    private ViewGroup mFeatureListLayout;
    private TextView mDescriptionText;
    private TextView mDescriptionTitleText;

    public static TicketAssistanceFragment newInstance(int actionBarTitleResId) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance: actionBarTitleResId = " + actionBarTitleResId);
        }
        TicketAssistanceFragment fragment = new TicketAssistanceFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        // Add parameters to the argument bundle
        args.putInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID, actionBarTitleResId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAttach");
        }

        // Check if parent fragment (if there is one) implements the interface
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof DrawerStateProvider) {
            mParentDrawerStateProvider = (DrawerStateProvider) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (activity != null && activity instanceof DrawerStateProvider) {
            mParentDrawerStateProvider = (DrawerStateProvider) activity;
        }
        // If neither implements the interface, log a warning
        else if (mParentDrawerStateProvider == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement DrawerStateProvider");
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        // Default parameters
        Bundle args = getArguments();
        if (args == null) {
            mActionBarTitleResId = -1;
        }
        
        else {
            mActionBarTitleResId = args.getInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID);
        }

        Activity parentActivity = getActivity();
        if (parentActivity != null) {
            parentActivity.getActionBar().setTitle(mActionBarTitleResId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }
        View fragmentView = inflater.inflate(R.layout.fragment_ticket_assistance, container, false);

        mHeroImage = (ImageView)fragmentView.findViewById(R.id.fragment_ticket_assistance_hero_image);
        mFeatureListLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_ticket_assistance_options_layout);
        mDescriptionText = (TextView) fragmentView.findViewById(R.id.fragment_ticket_assistance_description);
        mDescriptionTitleText = (TextView)fragmentView.findViewById(R.id.fragment_ticket_assistance_description_title);

        return fragmentView;
    }

    @Override
    public void onClick(View v) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onClick");
        }

        Object tag = v.getTag();
        if (tag != null && tag instanceof String) {
            if (tag.equals(VIEW_TAG_PHONE_NUMBER)) {
                // Send the user to the dialer with the phone number filled in
                Intent intent = (Intent) v.getTag(R.id.key_view_tag_detail_feature_phone_number);
                if (intent != null && intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onLongClick");
        }

        Object tag = v.getTag();
        if (tag != null && tag instanceof String) {
            if (tag.equals(VIEW_TAG_PHONE_NUMBER)) {
                TextView primaryText = (TextView) v.findViewById(R.id.list_feature_item_primary_text);
                if (primaryText != null) {
                    String phoneNumber = (String) primaryText.getText();

                    // Copy the phone number to the clip board
                    ClipBoardUtils.copyToClipboard(R.string.detail_phone_number_clipboard_label, phoneNumber);
                    UserInterfaceUtils.showToastFromForeground(
                            getString(R.string.detail_phone_number_copied_to_clipboard_toast_message), Toast.LENGTH_SHORT, getActivity());
                }

                return true;
            }
        }
        return false;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, data);
        String phoneNumber = mMobilePage.getPhoneNumber();
        FeatureListUtils.addPhoneFeature(getActivity(), mFeatureListLayout,
                phoneNumber, null, VIEW_TAG_PHONE_NUMBER,
                TicketAssistanceFragment.this, TicketAssistanceFragment.this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public String getPageId() {
        return PAGE_IDENTIFIER;
    }

    @Override
    public void onMobilePageLoaded() {
        getActivity().getActionBar().setTitle(mMobilePage.getTitleName());
        mDescriptionText.setText(mMobilePage.getShortDescription());
        mDescriptionTitleText.setText(mMobilePage.getMobileDisplayName());

        /* 11/29/2016 - tridion response doesn't contain image URI */
        if(mMobilePage.getThumbnailImageUrl() != null) {
            mHeroImage.setVisibility(View.VISIBLE);
            String mImageSizeParam = ImageUtils.getPoiImageSizeString(
                    getResources().getInteger(R.integer.poi_detail_image_dpi_shift));

            Uri imageUriToLoad = Uri.parse(mMobilePage.thumbnailImageUrl).buildUpon()
                    .appendQueryParameter(RequestQueryParams.Keys.IMAGE_SIZE, mImageSizeParam)
                    .build();

            getPicasso().load(imageUriToLoad)
                    .placeholder(R.drawable.ic_no_image_logo_detail)
                    .fit()
                    .into(mHeroImage);
        }
    }
}
