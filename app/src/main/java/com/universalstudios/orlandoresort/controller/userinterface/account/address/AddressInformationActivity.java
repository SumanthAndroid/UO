package com.universalstudios.orlandoresort.controller.userinterface.account.address;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.account.AccountUtils;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.FullScreenLoadingView;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkActivity;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.GetGuestProfileRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetGuestProfileResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

/**
 * AddressInformationActivity which will initiate the AddressInformationFragment.
 *
 * Created by Tyler Ritchie on 10/3/16.
 */

public class AddressInformationActivity extends NetworkActivity {

    private static final String TAG = AddressInformationActivity.class.getSimpleName();
    public static final String KEY_ARG_SELECTED_ADDRESS_ID = "KEY_ARG_ADDRESS_INFO_ADDRESS_ID";
    static final String KEY_ARG_REQUIRES_FLORIDA_BILLING_ADDRESS = "KEY_ARG_REQUIRES_FLORIDA_BILLING_ADDRESS";
    static final String KEY_ARG_ALLOW_DELETE = "KEY_ARG_ALLOW_DELETE";
    public static final int RESULT_ADDED_OR_UPDATED = RESULT_FIRST_USER;
    public static final int RESULT_DELETED = RESULT_FIRST_USER + 1;
    private static final String ADDRESS_INFO_FRAGMENT_TAG = "new_address_info_fragment";
    private FullScreenLoadingView mLoadingView;
    private TridionConfig mTridionConfig;
    private String mSourceId;

    /**
     * Create a bundle to pass to {@link AddressInformationActivity}. {@link AddressInformationActivity}
     * will return the bundle back to the caller with a new KEY_ARG_SELECTED_ADDRESS_ID if the ID has
     * changed.
     * @param addressId the address ID
     * @param requiresFloridaBillingAddress does the address need to be a FL address
     * @param allowDelete allow the address to be delete
     * @return bundle of activity extras
     */
    public static Bundle newInstanceBundle(@SourceId.SourceIdType String sourceId, String addressId, boolean requiresFloridaBillingAddress,
                                           boolean allowDelete) {
        Bundle args = newInstanceBundle(sourceId, addressId, requiresFloridaBillingAddress);
        args.putBoolean(KEY_ARG_ALLOW_DELETE, allowDelete);
        return args;
    }

    /**
     * Create a bundle to pass to {@link AddressInformationActivity}. {@link AddressInformationActivity}
     * will return the bundle back to the caller with a new KEY_ARG_SELECTED_ADDRESS_ID if the ID has
     * changed.
     * @param addressId the address ID
     * @param requiresFloridaBillingAddress does the address need to be a FL address
     * @return bundle of activity extras
     */
    public static Bundle newInstanceBundle(@SourceId.SourceIdType String sourceId, String addressId, boolean requiresFloridaBillingAddress) {
        Bundle args = new Bundle();
        args.putString(AddressInformationFragment.KEY_ARG_SOURCE_ID, sourceId);
        args.putString(KEY_ARG_SELECTED_ADDRESS_ID, addressId);
        args.putBoolean(KEY_ARG_REQUIRES_FLORIDA_BILLING_ADDRESS, requiresFloridaBillingAddress);
        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_information);

        Bundle args = getIntent().getExtras();

        boolean isNewAddress = true;
        if (args != null) {
            mSourceId = args.getString(AddressInformationFragment.KEY_ARG_SOURCE_ID);
            String addressId = args.getString(KEY_ARG_SELECTED_ADDRESS_ID);
            isNewAddress = TextUtils.isEmpty(addressId);
        }

        mTridionConfig = IceTicketUtils.getTridionConfig();

        // If the address ID is null, then use the add header text; otherwise, it's an update
        setUpActionBar(isNewAddress);
    }

    @Override
    public void onStart() {
        super.onStart();

        // If neither the username or password are null/empty, then we have populated login info
        // and can retrieve GuestProfile
        requestProfile();
    }

    /**
     * Gets the User's profile
     */
    private void requestProfile() {
        if (AccountStateManager.isUserLoggedIn()) {

            GetGuestProfileRequest request = new GetGuestProfileRequest.Builder(this)
                    .build();

            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();

            mLoadingView = FullScreenLoadingView.show(this.getSupportFragmentManager());
        }
    }

    public void addFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.address_information_container, fragment, tag)
                .commit();
    }

    private void setUpActionBar(boolean newAddress) {
        // If the action bar is null, then we have a problem
        if (getActionBar() == null) {
            return;
        }
        ActionBar actionBar = getActionBar();
        // If the address ID is null, then this is an add
        if (newAddress) {
            actionBar.setTitle(mTridionConfig.getAddAddressLabel());
        }
        // This is an edit
        else {
            actionBar.setTitle(mTridionConfig.getUpdateAddressLabel());
        }
        actionBar.setDisplayShowHomeEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {

        if (mLoadingView != null) {
            mLoadingView.dismiss();
        }
        if (networkResponse instanceof GetGuestProfileResponse) {
            GetGuestProfileResponse response = (GetGuestProfileResponse) networkResponse;
            if (networkResponse.isHttpStatusCodeSuccess() && response.getResult() != null) {
                final GuestProfile guestProfile = response.getResult().getGuestProfile();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        addFragment(AddressInformationFragment.newInstance(mSourceId, guestProfile, getIntent().getExtras()),
                                ADDRESS_INFO_FRAGMENT_TAG);
                    }
                });
                AccountUtils.setUserName(response, this);
            }
            else {
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "Response was null or returned with a failure code");
                }
                finish();
            }
        }
    }
}
