package com.universalstudios.orlandoresort.controller.userinterface.account;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.account.address.AddressInformationActivity;
import com.universalstudios.orlandoresort.controller.userinterface.account.address.ProfileAddressListActivity;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.FullScreenLoadingView;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.list.DividerItemDecoration;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.FragmentViewProfileBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Address;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestPreference;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SortedInterest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.UserContactPermissions;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.GetGuestProfileRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetGuestProfileResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.response.HttpResponseStatus;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/17/16.
 * Class: ViewProfileFragment
 * Class Description: Fragment for view the user's profile
 */
public class ViewProfileFragment extends NetworkFragment implements ViewProfileItem.OnProfileActionItemClickedListener {
    public static final String TAG = ViewProfileFragment.class.getSimpleName();

    private static final String ARG_GUEST_PROFILE = "ARG_GUEST_PROFILE";

    private static final int ACTIVITY_REQUEST_CODE_ADDRESS_LIST = 1000;
    private static final int ACTIVITY_REQUEST_CODE_ADD_ADDRESS = 1001;
    private static final int ACTIVITY_REQUEST_CODE_UPDATE_CONTACT_INFO = 1002;
    private static final int ACTIVITY_REQUEST_CODE_UPDATE_PASSWORD = 1003;
    private static final int ACTIVITY_REQUEST_CODE_UPDATE_PERSONAL_INFO = 1004;
    private static final int ACTIVITY_REQUEST_CODE_UPDATE_COMMUNICATION_PREFERENCES = 1005;
    private static final int ACTIVITY_REQUEST_CODE_UPDATE_INTERESTS = 1006;

    //Personal Info Views
    private RecyclerView mList;

    private GuestProfile guestProfile;
    private TridionConfig mTridionConfig;

    private FullScreenLoadingView mLoadingView;
    private boolean mRefreshProfile = true;

    /**
     * Factory method to create a new instance of ViewProfileFragment
     *
     * @return new instance of ViewProfileFragment
     */
    public static ViewProfileFragment newInstance() {
        ViewProfileFragment fragment = new ViewProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != savedInstanceState) {
            guestProfile = GuestProfile.fromJson(savedInstanceState.getString(ARG_GUEST_PROFILE), GuestProfile.class);
        } else {
            sendOnPageLoadAnalytics();
        }
        mTridionConfig = IceTicketUtils.getTridionConfig();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAttach");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentViewProfileBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_view_profile, container, false);
        binding.setTridion(mTridionConfig);

        View layout = binding.getRoot();
        mList = binding.fragmentViewProfileList;

        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.shape_divider_white_28dp));
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (null != getActivity() && null != getActivity().getActionBar()) {
            getActivity().getActionBar().setTitle(mTridionConfig.getMyProfilePageTitle());
            getActivity().invalidateOptionsMenu();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AccountStateManager.isUserLoggedIn()) {
            if (mRefreshProfile) {
                requestProfile();
            }
        }
    }

    private void showLoadingView() {
        if (null == mLoadingView) {
            mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
        }
    }

    private void hideLoadingView() {
        if (null != mLoadingView) {
            mLoadingView.dismiss();
            mLoadingView = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (null != guestProfile) {
            outState.putString(ARG_GUEST_PROFILE, guestProfile.toJson());
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Gets the User's profile
     */
    private void requestProfile() {
        showLoadingView();

        if (AccountStateManager.isUserLoggedIn()) {

            GetGuestProfileRequest request = new GetGuestProfileRequest.Builder(this)
                    .build();

            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        }
    }

    /**
     * Set the fields in the profile based on the request
     *
     * @param guestProfile UnregisteredGuestResult from the request
     */
    private void setGuestProfile(GuestProfile guestProfile) {
        this.guestProfile = guestProfile;
        if (null == guestProfile || guestProfile.isContactNull()) {
            if (BuildConfig.DEBUG) {
                Toast.makeText(getContext(), "DEBUG: There is an issue with this account. Please try a different account", Toast.LENGTH_LONG)
                        .show();
            }
            return;
        }


        List<ViewProfileItem> list = new ArrayList<>();
        //personal
        ViewProfileItem personal = new ViewProfileItem(ViewProfileItem.PROFILE_ITEM_PERSONAL, R.drawable.ic_account_personal, mTridionConfig.getPersonalCardLabel(), mTridionConfig.getEditLabel());
        personal.setDetail1(AccountUtils.buildUsersName(guestProfile.getContact()));
        personal.setDetail2(guestProfile.getBirthDate());
        list.add(personal);

        //contact info
        ViewProfileItem contactInfo = new ViewProfileItem(ViewProfileItem.PROFILE_ITEM_CONTACT, R.drawable.ic_account_contact, mTridionConfig.getContactInformationTitle(), mTridionConfig.getEditLabel());
        contactInfo.setDetail1(guestProfile.getContact().getEmail());
        contactInfo.setDetail2(AccountUtils.getFormattedPhoneNumber(guestProfile.getContact().getMobilePhone()));
        list.add(contactInfo);

        //password
        list.add(new ViewProfileItem(ViewProfileItem.PROFILE_ITEM_PASSWORD, R.drawable.ic_lock, mTridionConfig.getPasswordLabel(), mTridionConfig.getEditLabel()));

        //communication preferences
        ViewProfileItem communicationPrefs = new ViewProfileItem(ViewProfileItem.PROFILE_ITEM_COMMUNICATION_PREFS, R.drawable.ic_communication, mTridionConfig.getCPHeadingLabel(), mTridionConfig.getEditLabel());
        GuestPreference preferences = guestProfile.getPreference();
        if (preferences.getContactPermissions() != null) {
            UserContactPermissions contactPermissions = preferences.getContactPermissions();
            StringBuilder commStringBuilder = new StringBuilder();
            commStringBuilder.append(contactPermissions.isEmailPermission() ? mTridionConfig.getCPEmailOkOptionLabel() : mTridionConfig.getCPEmailNotOkOptionLabel());
            commStringBuilder.append("\n");
            commStringBuilder.append(contactPermissions.isTextMessage() ? mTridionConfig.getCPTextMessageOkOptionLabel() : mTridionConfig.getCPTextMessageNotOkOptionLabel());
            commStringBuilder.append("\n");
            commStringBuilder.append(contactPermissions.isDirectMail() ? mTridionConfig.getCPSendMailOkOptionLabel() : mTridionConfig.getCPSendMailNotOkOptionLabel());
            communicationPrefs.setDetail1(commStringBuilder.toString());
        }
        list.add(communicationPrefs);

        //interests
        ViewProfileItem interestsItem = new ViewProfileItem(ViewProfileItem.PROFILE_ITEM_INTERESTS, R.drawable.ic_interests, mTridionConfig.getMyInterestsPageTitle(), mTridionConfig.getEditLabel());
        //set interests
        int minDisplay = mTridionConfig.getMinimumInterestLevel();
        Map<String, Integer> interestsMap = preferences.getCommunicationInterestsValueMap();

        //create list of class to be sorted
        List<SortedInterest> interests = new ArrayList<>();
        Set<String> keySet = interestsMap.keySet();
        for (String key : keySet) {
            interests.add(new SortedInterest(key, interestsMap.get(key)));
        }
        Collections.sort(interests);

        //get list of display values
        List<String> interestsKeysToDisplay = new ArrayList<>();
        for (SortedInterest interest : interests) {
            //add one to offset 0-indexed value
            if (interest.getValue() != null && interest.getValue() + 1 >= minDisplay) {
                interestsKeysToDisplay.add(interest.getKey());
            }
        }

        //create string to display
        if (interestsKeysToDisplay.size() > 0) {
            StringBuilder interestsStringBuilder = new StringBuilder();
            for (int i = 0; i < interestsKeysToDisplay.size(); i++) {
                interestsStringBuilder.append(interestsKeysToDisplay.get(i));
                if (i < interestsKeysToDisplay.size() - 1) {
                    interestsStringBuilder.append("\n");
                }
            }
            interestsItem.setHeader1(mTridionConfig.getProfileInterestParksLabel());
            interestsItem.setDetail1(interestsStringBuilder.toString());
        }

        //set seasons
        Map<String, String> seasonsMap = preferences.getVacationSeasonsResponse();
        List<String> seasonsKeys = mTridionConfig.getSeasonKeyValues();
        List<String> seasonsDisplayValues = mTridionConfig.getSeasonDisplayValues();

        //get list of display values
        List<String> seasonsToDisplay = new ArrayList<>();
        if (seasonsMap != null && seasonsMap.size() >= 1 && seasonsKeys != null) {
            for (int i = 0; i < seasonsKeys.size(); i++) {
                String key = seasonsKeys.get(i);
                String seasonValue = seasonsMap.get(key);
                if (GuestPreference.isSeasonSelected(seasonValue)) {
                    seasonsToDisplay.add(seasonsDisplayValues.get(i));
                }
            }
        }

        //create string to display
        if (seasonsToDisplay.size() > 0) {
            StringBuilder interestsStringBuilder = new StringBuilder();
            for (int i = 0; i < seasonsToDisplay.size(); i++) {
                interestsStringBuilder.append(seasonsToDisplay.get(i));
                if (i < seasonsToDisplay.size() - 1) {
                    interestsStringBuilder.append("\n");
                }
            }
            interestsItem.setHeader2(mTridionConfig.getProfileInterestSeasonsLabel());
            interestsItem.setDetail3(interestsStringBuilder.toString());
        }
        list.add(interestsItem);

        //addresses
        ViewProfileItem addresses = new ViewProfileItem(ViewProfileItem.PROFILE_ITEM_ADDRESSES, R.drawable.ic_account_shipping, mTridionConfig.getAddressCardLabel(), mTridionConfig.getEditLabel());
        //set addresses
        if (null == guestProfile.getAddresses() || guestProfile.getAddresses().isEmpty()) {
            addresses.setActionText(mTridionConfig.getAddAddressLabel());
        } else {
            Address address = guestProfile.getAddresses().get(0);
            if (null != address) {
                addresses.setDetail1(AccountUtils.buildUsersName(address));
                addresses.setDetail2(address.getAddressLine1());
                addresses.setDetail3(address.getAddressLine2());
                addresses.setDetail4(AccountUtils.buildCityStateZipLine(address));
            } else {
                addresses.setActionText(mTridionConfig.getAddAddressLabel());
            }
        }
        list.add(addresses);

//        ViewProfileAdapter mAdapter = new ViewProfileAdapter(list, this);
        ViewProfileBindingAdapter mAdapter = new ViewProfileBindingAdapter(this, list.toArray(new ViewProfileItem[]{}));
        mList.setAdapter(mAdapter);
    }


    @Override
    public void onProfileActionItemClicked(@ViewProfileItem.ProfileItemType int itemType) {
        switch (itemType) {
            case ViewProfileItem.PROFILE_ITEM_PERSONAL:
                startActivityForResult(AccountUpdatePersonalInfoActivity.newInstanceIntent(
                        getContext(), guestProfile), ACTIVITY_REQUEST_CODE_UPDATE_PERSONAL_INFO);
                break;
            case ViewProfileItem.PROFILE_ITEM_CONTACT:
                startActivityForResult(AccountUpdateContactInfoActivity.newInstanceIntent(
                        getContext(), guestProfile), ACTIVITY_REQUEST_CODE_UPDATE_CONTACT_INFO);
                break;
            case ViewProfileItem.PROFILE_ITEM_PASSWORD:
                startActivityForResult(AccountUpdatePasswordActivity.newInstanceIntent(
                        getContext(), guestProfile), ACTIVITY_REQUEST_CODE_UPDATE_PASSWORD);
                break;
            case ViewProfileItem.PROFILE_ITEM_COMMUNICATION_PREFS:
                startActivityForResult(UpdatePreferencesActivity.newInstance(getContext()), ACTIVITY_REQUEST_CODE_UPDATE_COMMUNICATION_PREFERENCES);
                break;
            case ViewProfileItem.PROFILE_ITEM_INTERESTS:
                startActivityForResult(UpdateInterestsActivity.newInstance(getContext()), ACTIVITY_REQUEST_CODE_UPDATE_INTERESTS);
                break;
            case ViewProfileItem.PROFILE_ITEM_ADDRESSES:
                if (null == guestProfile) {
                    Toast.makeText(getContext(), mTridionConfig.getEr71(), Toast.LENGTH_SHORT)
                            .setIconColor(Color.RED)
                            .show();
                } else {
                    mRefreshProfile = false;
                    if (null == guestProfile.getAddresses() || guestProfile.getAddresses().isEmpty()) {
                        // Add address
                        Intent addressInfoIntent = new Intent(getActivity().getBaseContext(), AddressInformationActivity.class);
                        Bundle args = AddressInformationActivity
                                .newInstanceBundle(SourceId.EDIT_ADDRESS, null, false);
                        startActivityForResult(addressInfoIntent.putExtras(args), ACTIVITY_REQUEST_CODE_ADD_ADDRESS);

                    } else {
                        // Edit addresses
                        Intent addressListIntent = ProfileAddressListActivity.newInstanceIntent(getContext(), SourceId.EDIT_ADDRESS);
                        startActivityForResult(addressListIntent, ACTIVITY_REQUEST_CODE_ADDRESS_LIST);
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mRefreshProfile = false;
        switch (requestCode) {
            case ACTIVITY_REQUEST_CODE_ADD_ADDRESS:
            case ACTIVITY_REQUEST_CODE_ADDRESS_LIST:
            case ACTIVITY_REQUEST_CODE_UPDATE_CONTACT_INFO:
            case ACTIVITY_REQUEST_CODE_UPDATE_PASSWORD:
            case ACTIVITY_REQUEST_CODE_UPDATE_PERSONAL_INFO:
                if (resultCode != Activity.RESULT_CANCELED) {
                    mRefreshProfile = true;
                }
                break;
            case ACTIVITY_REQUEST_CODE_UPDATE_COMMUNICATION_PREFERENCES:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getContext(), mTridionConfig.getSu6(), Toast.LENGTH_SHORT)
                            .show();
                    mRefreshProfile = true;
                }
                break;
            case ACTIVITY_REQUEST_CODE_UPDATE_INTERESTS:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getContext(), mTridionConfig.getSu5(), Toast.LENGTH_SHORT)
                            .show();
                    mRefreshProfile = true;
                }
                break;

        }
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        hideLoadingView();

        if (networkResponse instanceof GetGuestProfileResponse) {
            GetGuestProfileResponse response = (GetGuestProfileResponse) networkResponse;
            if (response.getStatusCode() == HttpResponseStatus.SUCCESS_OK) {
                if (response.getResult() != null) {
                    setGuestProfile(response.getResult().getGuestProfile());
                }
                AccountUtils.setUserName(response, getContext());
            } else {
                Toast.makeText(getContext(), mTridionConfig.getEr71(), Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }

    private void sendOnPageLoadAnalytics() {
            Map<String,Object> extraData = new HashMap<String, Object>();
            AnalyticsUtils.trackPageView(
                            AnalyticsUtils.CONTENT_GROUP_CRM,
                            AnalyticsUtils.CONTENT_FOCUS_ACCOUNT,
                            null,
                            AnalyticsUtils.UO_PAGE_IDENTIFIER_PROFILE,
                            null,
                            null,
                            null,
                            extraData);
    }
}
