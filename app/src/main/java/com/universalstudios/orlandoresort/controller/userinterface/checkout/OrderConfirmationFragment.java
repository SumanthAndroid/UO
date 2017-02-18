package com.universalstudios.orlandoresort.controller.userinterface.checkout;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.account.AccountUtils;
import com.universalstudios.orlandoresort.controller.userinterface.checkout.binding.OrderConfirmationViewModel;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.DialogUtils;
import com.universalstudios.orlandoresort.controller.userinterface.home.HomeActivity;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets.TicketAssignmentUtils;
import com.universalstudios.orlandoresort.controller.userinterface.image.PicassoProvider;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.offers.PersonalizationOfferModel;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.WalletUtils;
import com.universalstudios.orlandoresort.databinding.FragmentOrderConfirmationBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.CreateLinkedAccountRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.CreateLinkedAccountResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupOrder;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.offers.GetPersonalizationOffersRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.offers.GetPersonalizationOffersResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.offers.PersonalizationOffer;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.offers.PersonalizationOffersResult;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetTridionSpecsRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses.GetTridionSpecsResponse;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.analytics.CrashAnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.wallet.GetWalletFolioRequest;
import com.universalstudios.orlandoresort.model.network.domain.wallet.GetWalletFolioResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.ModifyPinRequest;
import com.universalstudios.orlandoresort.model.network.domain.wallet.ModifyPinResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.WalletFolioCard;
import com.universalstudios.orlandoresort.model.network.domain.wallet.WalletFolioResult;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.ConcurrencyType;
import com.universalstudios.orlandoresort.model.network.response.HttpResponseStatus;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountLoginService;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;
import com.universalstudios.orlandoresort.model.state.account.SessionCache;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpecManager;
import com.universalstudios.orlandoresort.view.password.PasswordStrengthView;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/23/16.
 * Class: OrderConfirmationFragment
 * Class Description: Fragment for the order confirmation
 */
public class OrderConfirmationFragment extends NetworkFragment implements OrderConfirmationActionCallback {
    public static final String TAG = OrderConfirmationFragment.class.getSimpleName();
    private static final int LINKED_ACCOUNT_RETRIES = 5;
    private static final int LINKED_ACCOUNT_RETRY_WAIT_MS = 3000;
    static final String KEY_ARG_ORDER_SUMMARY = "KEY_ARG_ORDER_SUMMARY";

    private static final String DATE_FORMAT_DOB_DISPLAY = "MM/dd/yyyy";

    private FragmentOrderConfirmationBinding mBinding;
    private PicassoProvider mParentPicassoProvider;

    private TridionConfig mTridionConfig;
    private OrderConfirmationViewModel mOrderConfirmationViewModel;
    private Handler mHandler;
    private String offersTcmId;
    private String offerCode;

    private int mLinkedAccountRetryAttempts;
    private boolean mIsCallInProgress;

    private PersonalizationOfferModel.OfferActionCallback offerCtaCallback = new PersonalizationOfferModel.OfferActionCallback() {
        @Override
        public void onOfferCtaClicked(String url) {
            if (!TextUtils.isEmpty(url)) {
                Uri uri = Uri.parse(url);
                if (TextUtils.isEmpty(uri.getScheme())) {
                    uri.buildUpon().scheme("http://");
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                sendOfferClickedAnalytics();
                startActivity(intent);
            }
        }
    };

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            if (AccountUtils.isWithinDateRange(year, month, dayOfMonth)) {
                String dateString = String.format("%02d/%02d/%04d", month+1, dayOfMonth, year);
                mOrderConfirmationViewModel.setDob(dateString);
                mBinding.layoutOrderConfirmationUnregisteredInclude.orderConfirmationDobEditText.setError(null);
            } else {
                mBinding.layoutOrderConfirmationUnregisteredInclude.orderConfirmationDobEditText.setError(mBinding.getTridion().getEr26());
            }
        }
    };

    private void requestLinkedAccount() {
        mIsCallInProgress = true;
        showViewBasedOnState();
        String guestId = AccountStateManager.getGuestId();
        CreateLinkedAccountRequest request = new CreateLinkedAccountRequest.Builder(OrderConfirmationFragment.this)
                .setBirthDate(mOrderConfirmationViewModel.getDob())
                .setCustomerOrderId(mOrderConfirmationViewModel.orderConfirmationInfo.confirmationNumber)
                .setGuestId(guestId)
                .setPassword(mOrderConfirmationViewModel.getPassword())
                // TODO This shouldn't be set from the controller, internalize when this request is refactored
                .setSourceId(SourceId.PURCHASE_CONFIRMATION)
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    public static OrderConfirmationFragment newInstance(OrderConfirmationInfo orderConfirmationInfo) {
        OrderConfirmationFragment fragment = new OrderConfirmationFragment();
        Bundle args = new Bundle();
        // add arguments here
        args.putParcelable(KEY_ARG_ORDER_SUMMARY, Parcels.wrap(orderConfirmationInfo));
        fragment.setArguments(args);
        return fragment;
    }

    public OrderConfirmationFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTridionConfig = IceTicketUtils.getTridionConfig();
        mOrderConfirmationViewModel = new OrderConfirmationViewModel(AccountStateManager.isUserLoggedIn(),
                mTridionConfig.getPageHeaderOCTitle());

        //if the use already set their dob in the assign names page, use that
        Date dob = SessionCache.getThisIsMeDob();
        if (dob != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DOB_DISPLAY);
            String dateString = sdf.format(dob);
            mOrderConfirmationViewModel.setDob(dateString);
        }

        if (getArguments() != null) {
            // retrieve arguments here
            mOrderConfirmationViewModel.orderConfirmationInfo = Parcels.unwrap(getArguments().getParcelable(KEY_ARG_ORDER_SUMMARY));
        }

        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Check if parent fragment (if there is one) implements the interface
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof PicassoProvider) {
            mParentPicassoProvider = (PicassoProvider) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (activity != null && activity instanceof PicassoProvider) {
            mParentPicassoProvider = (PicassoProvider) activity;
        }
        // If neither implements the image selection callback, warn that
        // selections are being missed
        else if (mParentPicassoProvider == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement PicassoProvider");
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Set the action bar title
        setToolbarTitle();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentOrderConfirmationBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        mBinding.setViewModel(mOrderConfirmationViewModel);
        mBinding.setTridion(mTridionConfig);
        mBinding.setCallback(this);

        mBinding.layoutOrderConfirmationUnregisteredInclude.orderConfirmationTermsText
                .setText(mTridionConfig.getTermsAndPolicy());
        mBinding.layoutOrderConfirmationUnregisteredInclude.orderConfirmationTermsText
                .setMovementMethod(LinkMovementMethod.getInstance());

        mBinding.layoutOrderConfirmationUnregisteredInclude.orderConfirmationPasswordEditText
                .setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            checkPasswordError();
                        }
                    }
                });

        requestFolio();

        // Bind the new password view to the strength meter
        PasswordStrengthView passwordStrengthView = mBinding.layoutOrderConfirmationUnregisteredInclude.passwordStrengthMeter;
        passwordStrengthView.bindPasswordEditText(mBinding.layoutOrderConfirmationUnregisteredInclude.orderConfirmationPasswordEditText);

        String detailImageUrl = mTridionConfig.getPageHeaderOCHeroImage();
        loadImageUrlIntoImageView(mBinding.orderConfirmationHeroImageView, detailImageUrl);

        if (mOrderConfirmationViewModel.getRegisteredUser()) {
            // only get offers if the user is signed in
            requestPersonalizationOffers();
        }

        return view;
    }

    private void loadImageUrlIntoImageView(ImageView imageView, String imageUrl) {
        Uri imageUriToLoad = null;
        if (imageUrl != null) {
            try {
                imageUriToLoad = Uri.parse(imageUrl);
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "onCreate: exception trying to parse detail image URL", e);
                }

                // Log the exception to crittercism
                CrashAnalyticsUtils.logHandledException(e);
            }
        }
        if (imageUriToLoad != null && mParentPicassoProvider != null) {

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onLoadFinished: imageUriToLoad = " + imageUriToLoad);
            }

            Picasso picasso = mParentPicassoProvider.providePicasso();
            if (picasso != null) {
                picasso.load(imageUriToLoad).into(imageView, null);
            }
        }
    }

    @Override
    public void onDobClicked() {
        DialogUtils.showDatePicker(getContext(), dateSetListener);
    }

    @Override
    public void onDobInfoPopupClicked() {
        showMessage(mBinding.getTridion().getDOBToolTip());
    }

    @Override
    public void onCreateAccountClicked() {
        boolean dobError = TextUtils.isEmpty(mOrderConfirmationViewModel.getDob());
        mOrderConfirmationViewModel.setShowDobError(dobError);
        boolean passwordError = checkPasswordError();

        if (!dobError && !passwordError) {
            mLinkedAccountRetryAttempts = 0;
            requestLinkedAccount();
        }

    }

    @Override
    public void onManageAccountClicked() {
        gotoViewProfile();
    }

    @Override
    public void onCreatePinClicked(OrderConfirmationViewModel viewModel) {
        String enteredPin = viewModel.getEnteredPin();
        boolean isPinValid = WalletUtils.isWalletFolioPinCodeValid(viewModel.getEnteredPin());

        // If the PIN is valid, send it to services
        if (isPinValid) {
            mIsCallInProgress = true;
            showViewBasedOnState();

            ModifyPinRequest modifyPinRequest = new ModifyPinRequest.Builder(this)
                    .setPin(enteredPin)
                    .build();

            NetworkUtils.queueNetworkRequest(modifyPinRequest);
            NetworkUtils.startNetworkService();
        }
        // Otherwise, inform the user it doesn't pass the rules
        else  {
            Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getEr69(), Toast.LENGTH_LONG)
                    .setIconColor(Color.RED)
                    .show();
        }
    }

    @Override
    public void onCreatePinInfoClicked(OrderConfirmationViewModel viewModel) {
        // Show the create PIN rules
        Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getW8(), Toast.LENGTH_LONG)
                .show();
    }

    /**
     * Sets the action bar's title
     */
    private void setToolbarTitle() {
        // If either the activity or action bar are null, then we have a problem
        if (getActivity() == null || getActivity().getActionBar() == null) {
            return;
        }
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle(mTridionConfig.getPageHeaderCLTitle());
        actionBar.setDisplayShowHomeEnabled(false);
    }

    private void showViewBasedOnState() {
        if (mIsCallInProgress) {
            showLoadingView();
        } else {
            hideLoadingView();
        }
    }

    private void showLoadingView() {
        mBinding.loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoadingView() {
        mBinding.loadingView.setVisibility(View.GONE);
    }

    @Override
    public void handleNetworkResponse(NetworkResponse response) {
        mIsCallInProgress = false;
        if (response == null) {
            return;
        }

        if (response instanceof CreateLinkedAccountResponse) {
            handleCreateLinkedAccountResponse((CreateLinkedAccountResponse) response);
        } else if (response instanceof GetPersonalizationOffersResponse) {
            handlePersonalizationOffersResponse(((GetPersonalizationOffersResponse) response));
        } else if (response instanceof GetTridionSpecsResponse) {
            handleTridionSpecsResponse();
        } else if (response instanceof GetWalletFolioResponse) {
            handleWalletFolioResponse((GetWalletFolioResponse) response);
        } else if (response instanceof ModifyPinResponse) {
            handleModifyPinResponse((ModifyPinResponse) response);
        }
    }

    private void handleCreateLinkedAccountResponse(CreateLinkedAccountResponse response) {
        if (response.isHttpStatusCodeSuccess()) {
            // Request personalization offers now that the user has created an account
            mOrderConfirmationViewModel.setPageTitle(mTridionConfig.getSu2());
            mOrderConfirmationViewModel.setShowOrderConfirmation(false);
            mOrderConfirmationViewModel.setShowEmailConfirmation(false);
            AccountLoginService.startActionLoginWithUsernamePassword(getContext(),
                    mOrderConfirmationViewModel.orderConfirmationInfo.emailAddress.trim(),
                    mOrderConfirmationViewModel.getPassword());
            mOrderConfirmationViewModel.setRegisteredUser(true);
            Toast.makeText(getContext(), mTridionConfig.getSu2(), Toast.LENGTH_SHORT)
                    .show();
            requestPersonalizationOffers();
        } else if (null != response.getHttpStatusCode() &&
                response.getHttpStatusCode() == HttpResponseStatus.ERROR_CONNECTION_CLOSED) {
            mLinkedAccountRetryAttempts++;
            if (mLinkedAccountRetryAttempts < LINKED_ACCOUNT_RETRIES) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestLinkedAccount();
                    }
                }, LINKED_ACCOUNT_RETRY_WAIT_MS);
            } else {
                showMessage(mBinding.getTridion().getEr59());
                showViewBasedOnState();
            }
        } else if (null != response.getHttpStatusCode() &&
                response.getHttpStatusCode() == HttpResponseStatus.ERROR_ACCOUNT_REGISTERED) {
            showMessage(mBinding.getTridion().getEr61());
            showViewBasedOnState();
        } else {
            showMessage(mTridionConfig.getEr59());
            showViewBasedOnState();
        }
    }

    private void requestPersonalizationOffers() {
        mIsCallInProgress = true;
        showViewBasedOnState();
        String guestId = AccountStateManager.getGuestId();
        TicketGroupOrder ticketGroupOrder = TicketAssignmentUtils.instance().getTicketGroupOrder();

        GetPersonalizationOffersRequest request = new GetPersonalizationOffersRequest.Builder(this)
                .setExternalGuestId(guestId)
                .setNumberRequested(1)
                .setPartNumbers(ticketGroupOrder)
                .setGeoLocation(mOrderConfirmationViewModel.orderConfirmationInfo.geoLocation)
                .build();

        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();

    }

    private void handlePersonalizationOffersResponse(GetPersonalizationOffersResponse response) {
        PersonalizationOffersResult result = response.getPersonalizationOffersResult();
        if (null != result && null != result.getOffers() && result.getOffers().size() > 0) {
            PersonalizationOffer offer = result.getOffers().get(0);
            offersTcmId = offer.getContentId();
            int index = offersTcmId.indexOf("-");
            offersTcmId = offersTcmId.substring(index+1);
            offerCode = offer.getCode();


            requestTridionSpecs(offersTcmId);

        } else {
            showViewBasedOnState();
        }
    }

    private void handleWalletFolioResponse(GetWalletFolioResponse response) {

        boolean showCreatePin = false;

        if (response.isHttpStatusCodeSuccess()) {
            boolean paymentMethodExists = false;
            WalletFolioResult walletFolioResult = response.getWalletFolioResult();
            if (walletFolioResult != null && walletFolioResult.getCards() != null) {
                // Check if there's a payment method
                for (WalletFolioCard card : walletFolioResult.getCards()) {
                    if (card != null) {
                        paymentMethodExists = true;
                        break;
                    }
                }

                // Only show PIN creation if there's a payment method but there is no existing PIN
                if (paymentMethodExists) {
                    Boolean hasPin = walletFolioResult.getHasPin();
                    if (hasPin == null || !hasPin) {
                        showCreatePin = true;
                        loadImageUrlIntoImageView(mBinding.orderConfirmationPinMobileImageView,
                                mTridionConfig.getOrderConfirmationPinMobileImageView());
                        loadImageUrlIntoImageView(mBinding.orderConfirmationPinCardImageView,
                                mTridionConfig.getOrderConfirmationPinCardImageView());
                        loadImageUrlIntoImageView(mBinding.orderConfirmationPinLetterImageView,
                                mTridionConfig.getOrderConfirmationPinLetterImageView());
                    }
                }

            } else {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Wallet folio response had incomplete data; pin creation will not be shown");
                }
            }
        } else {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Wallet folio response returned unsuccessful; pin creation will not be shown");
            }
        }
        mOrderConfirmationViewModel.setShowPinCreation(showCreatePin);
    }

    private void handleModifyPinResponse(ModifyPinResponse response) {
        showViewBasedOnState();
        if (response.isHttpStatusCodeSuccess()) {
            // Inform the user it was successful
            Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getSu14(), Toast.LENGTH_LONG)
                    .show();
            mOrderConfirmationViewModel.setShowPinCreation(false);
        } else {
            Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getEr71(), Toast.LENGTH_LONG)
                    .setIconColor(Color.RED)
                    .show();
        }
    }

    private void requestFolio() {
        if (!mIsCallInProgress) {
            mIsCallInProgress = true;
            showViewBasedOnState();

            GetWalletFolioRequest request = new GetWalletFolioRequest.Builder(this).build();
            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        }
    }

    private void requestTridionSpecs(String id) {
        mIsCallInProgress = true;
        showViewBasedOnState();
        List<String> ids = new ArrayList<>(1);
        ids.add(id);
        GetTridionSpecsRequest request = new GetTridionSpecsRequest.Builder(this)
                .setConcurrencyType(ConcurrencyType.ASYNCHRONOUS)
                .setIds(ids)
                .build();

        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    private void handleTridionSpecsResponse() {
        if (null != offersTcmId) {
            TridionLabelSpec spec = TridionLabelSpecManager.getSpecForId(offersTcmId);

            PersonalizationOfferModel offerModel = new PersonalizationOfferModel(
                    spec.getImage(), spec.getTitle(), spec.getTeaserText(), spec.getButtonLabel(),
                    spec.getButtonCtaUrl(), spec.getPriceTextAbove(), spec.getPriceTextBelowPrimary(),
                    offerCtaCallback
            );
            mOrderConfirmationViewModel.setPersonalizationOffer(offerModel);
        }
        showViewBasedOnState();
    }

    private void showMessage(final String message) {
        // Post to handler to make sure it is on the main thread
        Handler mHandler = new Handler(getContext().getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
        });
    }

    private void gotoViewProfile() {
        Intent i = new Intent(getContext(), HomeActivity.class);
        Bundle homeActivityBundle = HomeActivity.newInstanceBundle(HomeActivity.START_PAGE_TYPE_VIEW_PROFILE);
        i.putExtras(homeActivityBundle);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        getActivity().finish();
    }

    private boolean checkPasswordError() {
        boolean passwordError = false;
        switch (AccountUtils.isValidPassword(mOrderConfirmationViewModel.getPassword(),
                mOrderConfirmationViewModel.orderConfirmationInfo.emailAddress)) {
            case REPEATING_CHARACTERS:
                mOrderConfirmationViewModel.setPasswordError(mBinding.getTridion().getEr51());
                passwordError = true;
                break;
            case LENGTH:
                mOrderConfirmationViewModel.setPasswordError(mBinding.getTridion().getEr9());
                passwordError = true;
                break;
            case SAME_AS_EMAIL:
                mOrderConfirmationViewModel.setPasswordError(mBinding.getTridion().getEr60());
                passwordError = true;
                break;
            case EMPTY:
                mOrderConfirmationViewModel.setPasswordError(mBinding.getTridion().getEr26());
                passwordError = true;
                break;
            case ENDS_WITH_SPACE:
                mOrderConfirmationViewModel.setPasswordError(mBinding.getTridion().getEr86());
                passwordError = true;
                break;
            case NONE:
                mOrderConfirmationViewModel.setPasswordError(null);
                passwordError = false;
                break;
        }
        return passwordError;
    }

    private void sendOfferClickedAnalytics() {
        Map<String, Object> extraData = new HashMap<String, Object>();
        if(offerCode == null) {
            offerCode = "";
        }

        extraData.put(AnalyticsUtils.KEY_SESSION_ID, AccountStateManager.getGuestId());
        extraData.put(AnalyticsUtils.KEY_OFFER_CODE, offerCode);
        extraData.put(AnalyticsUtils.KEY_EVENT_NAME,  AnalyticsUtils.VALUE_CONFIRMATION_PAGE);
        AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_SITE_TICKETS,
                null,
                null,
                AnalyticsUtils.CONTENT_SUB_2_TICKET_CONFORMATION,
                AnalyticsUtils.PROPERTY_NAME_CHECKOUT_PROCESS,
                null,
                extraData);
    }


}
