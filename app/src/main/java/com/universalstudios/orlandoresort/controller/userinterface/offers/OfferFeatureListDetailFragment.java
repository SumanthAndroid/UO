package com.universalstudios.orlandoresort.controller.userinterface.offers;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.detail.FeatureListUtils;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.domain.offers.Offer;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OffersTable;

import java.util.Formatter;
import java.util.Locale;

/**
 * @author acampbell
 *
 */
public class OfferFeatureListDetailFragment extends DatabaseQueryFragment implements OnClickListener {

    private static final String TAG = OfferFeatureListDetailFragment.class.getSimpleName();

    private static final String KEY_ARG_SECONDARY = "KEY_ARG_SECONDARY";

    private static final String VIEW_TAG_AMEX_CARD = "VIEW_TAG_AMEX_CARD";
    private static final String VIEW_TAG_END_DATE = "VIEW_TAG_END_DATE";
    private static final String VIEW_TAG_EXCLUSIONS = "VIEW_TAG_EXCLUSIONS";
    private static final String VIEW_TAG_TERMS = "VIEW_TAG_TERMS";
    private static final String VIEW_TAG_HOURS = "VIEW_TAG_HOURS";

    private LinearLayout mFeatureListLayout;
    private Offer mOffer;
    private boolean mIsSecondary;
    private View mFeatureListHeader;
    private TextView mFeatureListHeaderText;

    public static OfferFeatureListDetailFragment newInstance(DatabaseQuery databaseQuery, boolean isSecondary) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "newInstance: databaseQuery = " + databaseQuery);
        }

        // Create a new fragment instance
        OfferFeatureListDetailFragment fragment = new OfferFeatureListDetailFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
        args.putBoolean(KEY_ARG_SECONDARY, isSecondary);
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
        if (args == null) {

        }
        // Otherwise, set incoming parameters
        else {
            mIsSecondary = args.getBoolean(KEY_ARG_SECONDARY);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {

        }
        // Otherwise restore state, overwriting any passed in parameters
        else {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }

        // Inflate the fragment layout into the container
        View fragmentView = inflater.inflate(R.layout.fragment_offer_detail_feature_list, container, false);

        // Setup Views
        mFeatureListLayout = (LinearLayout) fragmentView
                .findViewById(R.id.fragment_offer_detail_feature_list_layout);
        mFeatureListHeader = fragmentView.findViewById(R.id.fragment_offer_detail_feature_list_header);
        mFeatureListHeaderText = (TextView) fragmentView
                .findViewById(R.id.fragment_offer_detail_feature_list_header_text);

        return fragmentView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android
     * .support.v4.content.Loader, java.lang.Object)
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onLoadFinished");
        }

        if (loader.getId() == LOADER_ID_DATABASE_QUERY) {
            // Clear the feature options and load new ones
            mFeatureListLayout.removeAllViews();

            if (data != null && data.moveToFirst()) {
                String offerObjectJson = data.getString(data
                        .getColumnIndex(OffersTable.COL_OFFER_OBJECT_JSON));
                mOffer = GsonObject.fromJson(offerObjectJson, Offer.class);
                addFeatures(mOffer);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android
     * .support.v4.content.Loader)
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public void onClick(View v) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onClick");
        }

        Object tag = v.getTag();
        if (tag != null && tag instanceof String) {
            if (tag.equals(VIEW_TAG_TERMS)) {
                Bundle args = TermsAndConditionsActivity.newInstanceBundle(mOffer.getTermsAndConditions());
                getActivity().startActivity(
                        new Intent(getActivity(), TermsAndConditionsActivity.class).putExtras(args));
            }
        }
    }

    private void addFeatures(Offer offer) {
        View featureView;
        // Secondary features, hours only
        if (mIsSecondary) {

            if (!TextUtils.isEmpty(offer.getLocationDays()) && !TextUtils.isEmpty(offer.getLocationHours())) {
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, null, false,
                        offer.getLocationDays(), offer.getLocationHours(), null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_HOURS, this);
                TextView primaryText = (TextView) featureView
                        .findViewById(R.id.list_feature_item_primary_text);
                TextView primarySubText = (TextView) featureView
                        .findViewById(R.id.list_feature_item_primary_sub_text);
                // Center text for hours
                ((LinearLayout.LayoutParams) primaryText.getLayoutParams()).gravity = Gravity.CENTER;
                ((LinearLayout.LayoutParams) primarySubText.getLayoutParams()).gravity = Gravity.CENTER;
                featureView.setClickable(false);
                mFeatureListLayout.addView(featureView);
            }

            if (mFeatureListLayout.getChildCount() > 0) {
                mFeatureListHeader.setVisibility(View.VISIBLE);
                mFeatureListHeaderText.setText(R.string.detail_header_hours);
            } else {
                mFeatureListHeader.setVisibility(View.GONE);
            }
        }
        // Primary features
        else {
            // Redeemable offer details
            if (!Offer.OFFER_TYPE_LOUNGE.equalsIgnoreCase(offer.getOfferType()) &&
                    !TextUtils.isEmpty((offer.getOfferAdditionalDetails()))) {
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, null, false,
                        offer.getOfferAdditionalDetails(), null, null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_AMEX_CARD, this);
                featureView.setClickable(false);
                mFeatureListLayout.addView(featureView);
            }
            // Exclusions
            if (!TextUtils.isEmpty(offer.getExclusions())) {
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, null, false,
                        offer.getExclusions(), null, null, (mFeatureListLayout.getChildCount() > 0),
                        VIEW_TAG_EXCLUSIONS, this);
                featureView.setClickable(false);
                mFeatureListLayout.addView(featureView);
            }
            // Expiration
            if (offer.getEndDateUnix() != null && offer.getEndDateUnix() != 0) {
                Formatter f = new Formatter(new StringBuilder(50), Locale.US);
                long endDateUnix = offer.getEndDateUnix() * 1000;
                String timeZone = DateTimeUtils.getParkTimezoneString();

                String date = DateUtils.formatDateRange(getActivity(), f, endDateUnix, endDateUnix,
                        DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE, timeZone).toString();

                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, null, false,
                        getString(R.string.offers_expires_format, date), null, null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_END_DATE, this);
                featureView.setClickable(false);
                mFeatureListLayout.addView(featureView);
            }
            // Terms and conditions
            if (!TextUtils.isEmpty(offer.getTermsAndConditions())) {
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, null, false,
                        R.string.offers_full_terms_and_conditions, null, null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_TERMS, this);
                featureView.setClickable(true);
                mFeatureListLayout.addView(featureView);
            }
        }
    }

}
