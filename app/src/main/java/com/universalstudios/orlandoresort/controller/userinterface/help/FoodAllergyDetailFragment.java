/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.help;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.detail.FeatureListUtils;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreActivity;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.UniversalFloatingActionButton;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

/**
 * 
 * 
 * @author Steven Byle
 */
public class FoodAllergyDetailFragment extends Fragment implements OnClickListener, View.OnLongClickListener, ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = FoodAllergyDetailFragment.class.getSimpleName();
    private static final String VIEW_TAG_PHONE_NUMBER = "VIEW_TAG_PHONE_NUMBER";
    private static final String VIEW_TAG_WEB_ADDRESS = "VIEW_TAG_WEB_ADDRESS";
    private static final String VIEW_TAG_SHOW_LOCATIONS = "VIEW_TAG_SHOW_LOCATIONS";
    private UniversalFloatingActionButton mUfabWallet;
    private LinearLayout mButtonRootLinearLayout;
    private ScrollView mScrollContainer;
    private int mCalculatedImageHeightDp;
    private ImageView mHeroImage;

    public static FoodAllergyDetailFragment newInstance() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance");
        }

        // Create a new fragment instance
        FoodAllergyDetailFragment fragment = new FoodAllergyDetailFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }

        // Add parameters to the argument bundle
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAttach");
        }
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
        else {}

        //TODO  create a value to track food allergy page
        // If this is the first creation, default state variables
       /* if (savedInstanceState == null) {
            // Track the page view
            AnalyticsUtils.trackPageView(getActivity(), AnalyticsUtils.CONTENT_GROUP_PLANNING,
                    AnalyticsUtils.CONTENT_FOCUS_GUEST_SERVICES, AnalyticsUtils.CONTENT_SUB_1_FAQ,
                    AnalyticsUtils.CONTENT_SUB_2_EXPRESS_PASS_INFO, AnalyticsUtils.PROPERTY_NAME_PARKS, null,
                    null);
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {}
        /**/

        // Get the smallest (portrait) width in dp
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float widthDp = displayMetrics.widthPixels / displayMetrics.density;
        float heightDp = displayMetrics.heightPixels / displayMetrics.density;
        float smallestWidthDp = Math.min(widthDp, heightDp);

        // Compute the height based on image aspect ratio 1080x760 @ 480dpi
        mCalculatedImageHeightDp = (int) Math.round(smallestWidthDp * (760.0 / 1080.0));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }

        // Inflate the fragment layout into the container
        View fragmentView = inflater.inflate(R.layout.fragment_detail_food_allergy, container, false);

        // Setup Views
        mButtonRootLinearLayout = (LinearLayout) fragmentView.findViewById(R.id.fragment_detail_food_allergy_buton_root_linear_layout);
        mUfabWallet = (UniversalFloatingActionButton) fragmentView.findViewById(R.id.view_ufab_wallet_food_detail_allergy);
        mHeroImage = (ImageView) fragmentView.findViewById(R.id.fragment_detail_food_allergy_hero_image);
        mScrollContainer = (ScrollView) fragmentView.findViewById(R.id.fragment_scroll_container_food);

        // Set image height based on detail image aspect ratio
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int calculatedImageHeightPx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mCalculatedImageHeightDp, displayMetrics));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mHeroImage.getLayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = calculatedImageHeightPx;
        mHeroImage.setLayoutParams(layoutParams);

        // Add contact options
        UniversalAppState uoState = UniversalAppStateManager.getInstance();

        String phoneNumber = uoState.getGuestServicesPhoneNumber();
        FeatureListUtils.addPhoneFeature(getActivity(), mButtonRootLinearLayout,
                phoneNumber, null, VIEW_TAG_PHONE_NUMBER,
                this, this);

        View featureView = FeatureListUtils.createFeatureItemView(mButtonRootLinearLayout,
                R.drawable.ic_detail_feature_email, true,
                R.string.detail_basic_info_food_allergy_email_description, null, null,
                (mButtonRootLinearLayout.getChildCount() > 0),
                VIEW_TAG_WEB_ADDRESS, this);
        featureView.setOnClickListener(this);
        mButtonRootLinearLayout.addView(featureView);

        featureView = FeatureListUtils.createFeatureItemView(mButtonRootLinearLayout,
                R.drawable.ic_detail_feature_guest_services, true,
                R.string.contact_guest_services_show_locations, null, null,
                (mButtonRootLinearLayout.getChildCount() > 0),
                VIEW_TAG_SHOW_LOCATIONS, this);
        featureView.setOnClickListener(this);
        mButtonRootLinearLayout.addView(featureView);

        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }

        mUfabWallet.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onActivityCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewStateRestored: savedInstanceState "
                    + (savedInstanceState == null ? "==" : "!=") + " null");
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
            else if (tag.equals(VIEW_TAG_WEB_ADDRESS)) {

                String website = getResources().getString(R.string.detail_basic_info_food_allergy_email_description);
                // Send the user to the dialer with the phone number filled in
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                startActivity(myIntent);
            }
            else if (tag.equals(VIEW_TAG_SHOW_LOCATIONS)) {
                Bundle bundle = ExploreActivity.newInstanceBundle(R.string.drawer_item_guest_services_locations, ExploreType.GUEST_SERVICES_BOOTHS);
                Intent guestServicesLocationsIntent = new Intent(v.getContext(), ExploreActivity.class).putExtras(bundle);
                startActivity(guestServicesLocationsIntent);
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
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(getString(R.string.detail_phone_number_clipboard_label), phoneNumber);
                    clipboard.setPrimaryClip(clip);

                    UserInterfaceUtils.showToastFromForeground(
                            getString(R.string.detail_phone_number_copied_to_clipboard_toast_message), Toast.LENGTH_SHORT, getActivity());
                }

                return true;
            }
            else if (tag.equals(VIEW_TAG_WEB_ADDRESS)) {
                TextView primaryText = (TextView) v.findViewById(R.id.list_feature_item_primary_text);
                if (primaryText != null) {

                    String website = getResources().getString(R.string.detail_basic_info_food_allergy_email_description);
                    // Copy the phone number to the clip board
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(getString(R.string.detail_email_clipboard_label), website);
                    clipboard.setPrimaryClip(clip);

                    UserInterfaceUtils.showToastFromForeground(
                            getString(R.string.detail_email_copied_to_clipboard_toast_message), Toast.LENGTH_SHORT, getActivity());
                }

                return true;
            }
        }
        return false;
    }

    @Override
    public void onGlobalLayout() {
        mUfabWallet.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        int height = mUfabWallet.getHeight();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mUfabWallet.getLayoutParams();
        height += params.bottomMargin;
        mScrollContainer.setPadding(mScrollContainer.getPaddingLeft(), mScrollContainer.getPaddingTop(), mScrollContainer.getPaddingRight(), height);
    }
}
