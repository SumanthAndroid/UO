package com.universalstudios.orlandoresort.controller.userinterface.account.address;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.checkout.CountryStateArrays;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.FullScreenLoadingView;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkActivity;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.ActivityProfileAddressListBinding;
import com.universalstudios.orlandoresort.databinding.AddressListContentBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Address;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.GetGuestProfileRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetGuestProfileResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.country_state.CountryStateProvinceStateManager;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.ConcurrencyType;
import com.universalstudios.orlandoresort.model.network.response.HttpResponseStatus;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;
import com.universalstudios.orlandoresort.view.binding.DataBoundAdapter;
import com.universalstudios.orlandoresort.view.binding.DataBoundViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ProfileAddressListActivity extends NetworkActivity {

    private static final String TAG = ProfileAddressListActivity.class.getSimpleName();
    private static final int ACTIVITY_REQUEST_CODE_ADD_UPDATE_ADDRESS = 1000;

    /**
     * Create a bundle to pass to {@link AddressInformationActivity}. {@link AddressInformationActivity}
     * will return the bundle back to the caller with a new KEY_ARG_SELECTED_ADDRESS_ID if the ID has
     * changed.
     * @return bundle of activity extras
     */
    public static Intent newInstanceIntent(Context context, @SourceId.SourceIdType String sourceId) {
        Intent intent = new Intent(context, ProfileAddressListActivity.class);
        Bundle args = new Bundle();
        args.putString(AddressInformationFragment.KEY_ARG_SOURCE_ID, sourceId);
        intent.putExtras(args);
        return intent;
    }

    private AddressActionCallback addressActionCallback = new AddressActionCallback() {
        @Override
        public void onClick(@NonNull AddressInfo addressInfo) {
            Bundle bundle = AddressInformationActivity.newInstanceBundle(mSourceId, addressInfo.getAddressId(), false, true);
            Intent intent = new Intent(ProfileAddressListActivity.this, AddressInformationActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, ACTIVITY_REQUEST_CODE_ADD_UPDATE_ADDRESS);
        }
    };

    private String mSourceId;
    TridionConfig mTridion;
    private FullScreenLoadingView mLoadingView;
    private GuestProfile mGuestProfile;
    private CountryStateArrays mCountryStateArrays;
    private ActivityProfileAddressListBinding mBinding;
    private int mResultCode = RESULT_CANCELED;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getIntent().getExtras();

        if (args != null) {
            mSourceId = args.getString(AddressInformationFragment.KEY_ARG_SOURCE_ID);
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile_address_list);
        mTridion = IceTicketUtils.getTridionConfig();
        mCountryStateArrays = new CountryStateArrays(
                CountryStateProvinceStateManager.getCountriesInstance().getCountries(),
                CountryStateProvinceStateManager.getStateProvincesInstance().getStateProvinces());

        mBinding.setTridion(mTridion);

        setUpActionBar();

        if(savedInstanceState == null) {
            sendOnPageLoadAnalytics();
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        showLoadingView();
        if (AccountStateManager.isUserLoggedIn()) {

            GetGuestProfileRequest request = new GetGuestProfileRequest.Builder(this)
                    .setConcurrencyType(ConcurrencyType.ASYNCHRONOUS)
                    .build();

            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        } else {
            // FIXME We need to handle when the user is not logged in properly
            Toast.makeText(this, mTridion.getEr71(), Toast.LENGTH_LONG)
                    .setIconColor(Color.RED)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateOptionsMenu");
        }

        // Adds items to the action bar
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_address, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.add_address:
                // Add address
                Intent addressInfoIntent = new Intent(this, AddressInformationActivity.class);
                Bundle args = AddressInformationActivity
                        .newInstanceBundle(mSourceId, null, false);
                startActivityForResult(addressInfoIntent.putExtras(args), ACTIVITY_REQUEST_CODE_ADD_UPDATE_ADDRESS);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_REQUEST_CODE_ADD_UPDATE_ADDRESS:
                if (resultCode != Activity.RESULT_CANCELED) {
                    mResultCode = RESULT_OK;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // Set the result even onBack since the user may have edited one or more addresses before
        // pressing back
        setResult(mResultCode);
        super.onBackPressed();
    }

    private void setUpActionBar() {
        // If the action bar is null, then we have a problem
        if (getActionBar() == null) {
            return;
        }
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(mTridion.getAddressListPageTitle());
        actionBar.setDisplayShowHomeEnabled(false);
        UserInterfaceUtils.enableActionBarHomeButton(actionBar);
    }

    private void showLoadingView() {
        if (null == mLoadingView) {
            mLoadingView = FullScreenLoadingView.show(getSupportFragmentManager());
        }
    }

    private void hideLoadingView() {
        if (null != mLoadingView) {
            mLoadingView.dismiss();
            mLoadingView = null;
        }
    }


    private void setupRecyclerView(List<AddressInfo> addressInfos) {
        mBinding.addressList.setAdapter(new AddressDataBoundAdapter(addressActionCallback, addressInfos));
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        hideLoadingView();
        if (networkResponse instanceof GetGuestProfileResponse) {
            GetGuestProfileResponse response = (GetGuestProfileResponse) networkResponse;
            if (networkResponse.getHttpStatusCode() == HttpResponseStatus.SUCCESS_OK &&
                    response.getResult() != null) {

                mGuestProfile = response.getResult().getGuestProfile();
                List<Address> addresses = mGuestProfile.getAddresses();
                List<AddressInfo> addressInfos = new ArrayList<>();
                for (Address address : addresses) {
                    if (null != address) {
                        addressInfos.add(new AddressInfo(address, mCountryStateArrays));
                    }
                }
                setupRecyclerView(addressInfos);
            } else {
                Toast.makeText(this, mTridion.getEr71(), Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
        }

    }

    private void sendOnPageLoadAnalytics() {
            AnalyticsUtils.trackPageView(
                            AnalyticsUtils.CONTENT_GROUP_CRM,
                            AnalyticsUtils.CONTENT_FOCUS_ACCOUNT,
                            null,
                            AnalyticsUtils.UO_PAGE_IDENTIFIER_ADDRESSES,
                            null,
                            null,
                            null,
                            null);
    }

    public interface AddressActionCallback {
        void onClick(@NonNull AddressInfo addressInfo);
    }

    private static class AddressDataBoundAdapter extends DataBoundAdapter<AddressListContentBinding> {
        List<AddressInfo> mAddressList = new ArrayList<>();
        private AddressActionCallback mActionCallback;

        public AddressDataBoundAdapter(AddressActionCallback actionCallback, List<AddressInfo> addressInfos) {
            super(R.layout.address_list_content);
            mActionCallback = actionCallback;
            mAddressList.addAll(addressInfos);
        }

        @Override
        protected void bindItem(DataBoundViewHolder<AddressListContentBinding> holder, int position,
                                List<Object> payloads) {
            holder.binding.setData(mAddressList.get(position));
            holder.binding.setCallback(mActionCallback);
        }

        @Override
        public int getItemCount() {
            return mAddressList.size();
        }
    }

}
