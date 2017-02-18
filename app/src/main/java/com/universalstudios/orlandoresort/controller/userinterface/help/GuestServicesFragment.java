
package com.universalstudios.orlandoresort.controller.userinterface.help;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.detail.FeatureListUtils;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreActivity;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.feedback.GuestFeedbackActivity;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

/**
 *
 *
 * @author Steven Byle
 */
public class GuestServicesFragment extends Fragment implements OnClickListener, OnLongClickListener, ActionBarTitleProvider {
    private static final String TAG = GuestServicesFragment.class.getSimpleName();

    private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";
    private static final String VIEW_TAG_PHONE_NUMBER = "VIEW_TAG_PHONE_NUMBER";
    private static final String VIEW_TAG_EMAIL_ADDRESS = "VIEW_TAG_EMAIL_ADDRESS";
    private static final String VIEW_TAG_SHOW_LOCATIONS = "VIEW_TAG_SHOW_LOCATIONS";
    private static final String VIEW_TAG_TWITTER = "VIEW_TAG_TWITTER";
    private static final String VIEW_TAG_FEEDBACK = "VIEW_TAG_FEEDBACK";

    private int mCalculatedImageHeightDp;
    private int mActionBarTitleResId;
    private DrawerStateProvider mParentDrawerStateProvider;
    private View mHeroImage;
    private ViewGroup mFeatureListLayout;
    private TextView mDescriptionText;

    public static GuestServicesFragment newInstance(int actionBarTitleResId) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance: actionBarTitleResId = " + actionBarTitleResId);
        }

        // Create a new fragment instance
        GuestServicesFragment fragment = new GuestServicesFragment();

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        // Default parameters
        Bundle args = getArguments();
        if (args == null) {
            mActionBarTitleResId = -1;
        }
        // Otherwise, set incoming parameters
        else {
            mActionBarTitleResId = args.getInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID);
        }

        // Get the smallest (portrait) width in dp
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float widthDp = displayMetrics.widthPixels / displayMetrics.density;
        float heightDp = displayMetrics.heightPixels / displayMetrics.density;
        float smallestWidthDp = Math.min(widthDp, heightDp);

        // Compute the height based on image aspect ratio 1080x760 @ 480dpi
        mCalculatedImageHeightDp = (int) Math.round(smallestWidthDp * (760.0 / 1080.0));

        Activity parentActivity = getActivity();
        if (parentActivity != null) {
            parentActivity.getActionBar().setTitle(mActionBarTitleResId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        // Set the action bar title, if the drawer isn't open
        if (mParentDrawerStateProvider != null && !mParentDrawerStateProvider.isDrawerOpenAtAll()) {
            Activity parentActivity = getActivity();
            if (parentActivity != null) {
                parentActivity.getActionBar().setTitle(provideTitle());
            }
        }

        View fragmentView = inflater.inflate(R.layout.fragment_contact_guest_services, container, false);

        mHeroImage = fragmentView.findViewById(R.id.fragment_contact_guest_services_hero_image);
        mFeatureListLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_contact_guest_services_options_layout);
        mDescriptionText = (TextView) fragmentView.findViewById(R.id.guestServicesDescription);

        // Set image height based on detail image aspect ratio
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int calculatedImageHeightPx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mCalculatedImageHeightDp, displayMetrics));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, calculatedImageHeightPx);
        mHeroImage.setLayoutParams(layoutParams);

        UniversalAppState uoState = UniversalAppStateManager.getInstance();

        String description = uoState.getGuestServiceDescription();
        mDescriptionText.setText(description);

        FeatureListUtils.TintType tintType;

        if (BuildConfigUtils.isLocationFlavorHollywood()){
            tintType = FeatureListUtils.TintType.COLORED;
        }else{
            tintType = FeatureListUtils.TintType.NO_TINT;
        }

        String phoneNumber = uoState.getGuestServicesPhoneNumber();
        FeatureListUtils.addPhoneFeature(getActivity(), mFeatureListLayout,
                phoneNumber, null, VIEW_TAG_PHONE_NUMBER,
                this, this, tintType);

        String emailAddress = uoState.getGuestServicesEmailAddress();
        String emailSubject = getString(R.string.contact_guest_services_email_subject);

        FeatureListUtils.addEmailFeature(getActivity(), mFeatureListLayout,
                emailAddress, uoState.getGuestServicesDisplayEmail(), null, emailSubject,
                VIEW_TAG_EMAIL_ADDRESS, this, this,  tintType);

        View featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                R.drawable.ic_detail_feature_guest_services, true,
                R.string.contact_guest_services_show_locations, null, null,
                (mFeatureListLayout.getChildCount() > 0),
                VIEW_TAG_SHOW_LOCATIONS, this, tintType);
        mFeatureListLayout.addView(featureView);

        // Guest feedback is Hollywood only
        if (BuildConfigUtils.isLocationFlavorHollywood()) {
            FeatureListUtils.addFeedbackFeature(getActivity(), mFeatureListLayout,
                    getString(R.string.contact_guest_services_guest_feedback), null, VIEW_TAG_FEEDBACK, this, tintType);
        }

        // Twitter hashtags are Orlando only
        if (BuildConfigUtils.isLocationFlavorOrlando()) {
            String twitterUrl = uoState.getGuestServicesTwitterUrl();
            FeatureListUtils.addTwitterWithHashtagFeature(getActivity(), mFeatureListLayout,
                    twitterUrl, uoState.getGuestServicesDefaultShareText(), null, VIEW_TAG_TWITTER, this, this,tintType);
        }

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
            else if (tag.equals(VIEW_TAG_EMAIL_ADDRESS)) {
                // Send the user to the dialer with the phone number filled in
                Intent intent = (Intent) v.getTag(R.id.key_view_tag_detail_feature_email_address);
                if (intent != null && intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
            else if (tag.equals(VIEW_TAG_SHOW_LOCATIONS)) {
                Bundle bundle = ExploreActivity.newInstanceBundle(R.string.drawer_item_guest_services_locations, ExploreType.GUEST_SERVICES_BOOTHS);
                Intent guestServicesLocationsIntent = new Intent(v.getContext(), ExploreActivity.class).putExtras(bundle);
                startActivity(guestServicesLocationsIntent);
            } else if (tag.equals(VIEW_TAG_TWITTER)) {
                Intent twitterIntent = (Intent) v.getTag(R.id.key_view_tag_detail_feature_twitter_hashtag);
                if (twitterIntent != null && twitterIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(twitterIntent);
                }
			} else if (tag.equals(VIEW_TAG_FEEDBACK)) {
                Intent intent = new Intent(getContext(), GuestFeedbackActivity.class);
                startActivity(intent);

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
            else if (tag.equals(VIEW_TAG_EMAIL_ADDRESS)) {
                String emailAddress = UniversalAppStateManager.getInstance()
                        .getGuestServicesEmailAddress();

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getString(R.string.detail_email_clipboard_label), emailAddress);
                clipboard.setPrimaryClip(clip);

                UserInterfaceUtils.showToastFromForeground(
                        getString(R.string.detail_email_copied_to_clipboard_toast_message), Toast.LENGTH_SHORT, getActivity());

                return true;
            } else if (tag.equals(VIEW_TAG_TWITTER)) {
                String twitterUrl = UniversalAppStateManager.getInstance()
                        .getGuestServicesTwitterUrl();

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getString(R.string.detail_email_clipboard_label), twitterUrl);
                clipboard.setPrimaryClip(clip);

                UserInterfaceUtils.showToastFromForeground(
                        getString(R.string.detail_twitter_url_copied_to_clipboard_toast_message), Toast.LENGTH_SHORT, getActivity());
                return true;
            }else if (tag.equals(VIEW_TAG_TWITTER)) {
                String twitterUrl = UniversalAppStateManager.getInstance()
                        .getGuestServicesTwitterUrl();

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getString(R.string.detail_email_clipboard_label), twitterUrl);
                clipboard.setPrimaryClip(clip);

                UserInterfaceUtils.showToastFromForeground(
                        getString(R.string.detail_twitter_url_copied_to_clipboard_toast_message), Toast.LENGTH_SHORT, getActivity());
                return true;
            }
        }
        return false;
    }

    @Override
    public String provideTitle() {
        return getString(mActionBarTitleResId);
    }
}
