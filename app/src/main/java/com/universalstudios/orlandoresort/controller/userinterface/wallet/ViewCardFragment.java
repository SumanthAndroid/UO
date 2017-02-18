package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.account.address.AddressInfo;
import com.universalstudios.orlandoresort.controller.userinterface.account.address.AddressInformationActivity;
import com.universalstudios.orlandoresort.controller.userinterface.checkout.CountryStateArrays;
import com.universalstudios.orlandoresort.controller.userinterface.checkout.CreditCardInfo;
import com.universalstudios.orlandoresort.controller.userinterface.checkout.ProfileAddressSpinnerAdapter;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.AlertDialogFragment;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.DialogUtils;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.ListDialogFragment;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.FragmentViewCardBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Address;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.GetGuestProfileRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetGuestProfileResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.country_state.CountryStateProvinceStateManager;
import com.universalstudios.orlandoresort.model.network.domain.wallet.AddCardRequest;
import com.universalstudios.orlandoresort.model.network.domain.wallet.AddCardResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.DeleteCardRequest;
import com.universalstudios.orlandoresort.model.network.domain.wallet.DeleteCardResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.EditCardRequest;
import com.universalstudios.orlandoresort.model.network.domain.wallet.EditCardResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.GetWalletCardsRequest;
import com.universalstudios.orlandoresort.model.network.domain.wallet.GetWalletCardsResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.SetPrimaryCardRequest;
import com.universalstudios.orlandoresort.model.network.domain.wallet.SetPrimaryCardResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.WalletFolioCard;
import com.universalstudios.orlandoresort.model.network.response.HttpResponseStatus;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;
import com.universalstudios.orlandoresort.model.state.account.AddressCache;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;


/**
 * ViewCardFragment provides the interface for users to select/enter their
 * credit card information, billing address
 * <p>
 * Use the {@link ViewCardFragment#newInstance(String)} factory method to
 * create an instance of this fragment.
 */
public class ViewCardFragment extends NetworkFragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, CardViewModel.OnPrimaryCheckChangeListener,
        AlertDialogFragment.OnAlertDialogFragmentClickListener {
    private static final String TAG = ViewCardFragment.class.getSimpleName();

    private static final String KEY_ARG_CARD = "KEY_ARG_CARD";
    private static final String KEY_STATE_CALL_IN_PROGRESS = "KEY_STATE_CALL_IN_PROGRESS";
    private static final String KEY_STATE_SET_PRIMARY_AFTER_DELETE = "KEY_STATE_SET_PRIMARY_AFTER_DELETE";

    private static final int REQUEST_CODE_SCAN_CREDIT_CARD = 101;
    private static final int REQUEST_CODE_ADDRESS = 102;
    private static final String CARD_LIST_DIALOG_FRAGMENT = "CARD_LIST_DIALOG_FRAGMENT";

    private CardViewModel mCardViewModel;

    private CountryStateArrays mCountryStateArrays;
    private ProfileAddressSpinnerAdapter mBillingAddressSpinnerAdapter;

    private FragmentViewCardBinding mBinding;

    private boolean mIsCallInProgress;
    private boolean mSetPrimaryAfterDelete;
    private ViewCardListener mListener;
    private TridionConfig mTridionConfig;
    private GuestProfile mGuestProfile;
    private String mSelectedBillingAddressId;
    private String mCardId;
    private List<WalletFolioCard> mCards;
    private WalletFolioCard mSelectedCard;
    private WalletFolioCard mNewPrimaryCard;

    public ViewCardFragment() {
        // Required empty public constructor
    }

    public static ViewCardFragment newInstance(String cardId) {
        ViewCardFragment fragment = new ViewCardFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ARG_CARD, cardId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAttach");
        }
        Fragment parentFragment = getParentFragment();

        // Check if parent fragment (if there is one) implements the interface
        if (parentFragment != null && parentFragment instanceof ViewCardListener) {
            mListener = (ViewCardListener) parentFragment;
        }
        // Otherwise, check if parent context implements the interface
        else if (context != null && context instanceof ViewCardListener) {
            mListener = (ViewCardListener) context;
        }
        // If neither implements the interface, log a warning
        else if (mListener == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent context implement ViewCardListener");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CreditCardInfo creditCardInfo = new CreditCardInfo();
        if (getArguments() != null) {
            mCardId = getArguments().getString(KEY_ARG_CARD);
        } else {
            mCardId = null;
        }

        if (savedInstanceState == null) {
            mIsCallInProgress = false;
            mSetPrimaryAfterDelete = false;
        } else {
            mIsCallInProgress = savedInstanceState.getBoolean(KEY_STATE_CALL_IN_PROGRESS, false);
            mSetPrimaryAfterDelete = savedInstanceState.getBoolean(KEY_STATE_SET_PRIMARY_AFTER_DELETE, false);
        }

        mTridionConfig = IceTicketUtils.getTridionConfig();

        mCountryStateArrays = new CountryStateArrays(
                CountryStateProvinceStateManager.getCountriesInstance().getCountries(),
                CountryStateProvinceStateManager.getStateProvincesInstance().getStateProvinces());
        mCardViewModel = new CardViewModel(this, mCountryStateArrays, creditCardInfo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_view_card, container, false);
        View view = mBinding.getRoot();
        TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();
        mBinding.setTridion(tridionConfig);
        mBinding.setCountryStateArrays(mCountryStateArrays);
        mBinding.setViewCard(mCardViewModel);

        mBinding.fragmentViewCardDelete.setOnClickListener(this);
        mBinding.fragmentViewCardAddCamera.setOnClickListener(this);
        mBinding.fragmentViewCardAdd.setOnClickListener(this);
        mBinding.fragmentViewCardUpdate.setOnClickListener(this);
        mBinding.fragmentViewCardSavedBillingInfoPrimaryAddressEdit.setOnClickListener(this);

        mBillingAddressSpinnerAdapter = new ProfileAddressSpinnerAdapter(getActivity(), mCardViewModel.getSavedAddresses());
        mBinding.fragmentViewCardSavedBillingInfoSpinner.setAdapter(mBillingAddressSpinnerAdapter);

        // If the only item is add new item, hide the arrow for the spinners
        if (mBinding.fragmentViewCardSavedBillingInfoSpinner.getCount() == 1) {
            mBinding.fragmentViewCardSavedBillingInfoSpinnerArrow.setVisibility(View.GONE);
        }

        //show/hide camera/delete buttons
        if (mCardId != null) {
            mBinding.fragmentViewCardDelete.setVisibility(View.VISIBLE);
            mBinding.fragmentViewCardAddCamera.setVisibility(View.GONE);
            mBinding.fragmentViewCardAdd.setVisibility(View.GONE);
        } else {
            //show/hide camera/delete buttons
            mBinding.fragmentViewCardDelete.setVisibility(View.GONE);
            mBinding.fragmentViewCardAddCamera.setVisibility(View.VISIBLE);
            mBinding.fragmentViewCardAdd.setVisibility(View.VISIBLE);
        }

        mBinding.fragmentViewCardSecurityCodeInfoPopup.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestGetProfile();
        showViewBasedOnState();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_STATE_CALL_IN_PROGRESS, mIsCallInProgress);
        outState.putBoolean(KEY_STATE_SET_PRIMARY_AFTER_DELETE, mSetPrimaryAfterDelete);
    }

    /**
     * This is a callback method for the activity instigated by <code>startActivityForResult()</code>.
     *
     * @param requestCode The unique request code
     * @param resultCode  The result code
     * @param intent      The intent with any extras passed back
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        DialogUtils.cancelLoadingView(getActivity().getSupportFragmentManager());

        // Check which request we're responding to, make sure it was successful, and make sure the intent exists
        // If we are coming from the address information activity, then save the persisted address id for
        // updating our views.
        switch (requestCode) {
            case REQUEST_CODE_ADDRESS:
                if (resultCode == AddressInformationActivity.RESULT_ADDED_OR_UPDATED && intent != null) {
                    String addressId = intent.getStringExtra(AddressInformationActivity.KEY_ARG_SELECTED_ADDRESS_ID);
                    if (addressId != null) {
                        mSelectedBillingAddressId = addressId;
                        requestGetProfile();
                        showViewBasedOnState();
                    }
                }
                break;
            case REQUEST_CODE_SCAN_CREDIT_CARD:
                if (intent != null && intent.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                    CreditCard scanResult = intent.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                    CreditCardInfo cardInfo = new CreditCardInfo();
                    cardInfo.setCreditCardNumber(scanResult.cardNumber);
                    //subtract for 0-index offset of spinner
                    cardInfo.setExpirationMonth(scanResult.expiryMonth - 1);
                    //subtract different from current year for 0-index offset of spinner
                    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                    cardInfo.setExpirationYear(scanResult.expiryYear - currentYear);
                    mCardViewModel.setCreditCardInfo(cardInfo);
                }
                break;
        }
    }

    /**
     * @return true if the cancel is handled by the fragment
     */
    public boolean onCancelClicked() {
        if (mCardId == null) {
            showCancelConfirmDialog();
            return true;
        } else {
            return false;
        }
    }

    private void showCancelConfirmDialog() {
        DialogUtils.showTwoButtonAlertDialogFragment(getChildFragmentManager(),
                null,
                mTridionConfig.getW10(),
                mTridionConfig.getYesLabel(),
                mTridionConfig.getNoLabel());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_view_card_update:
                requestModifyCard();
                showViewBasedOnState();
                break;
            case R.id.fragment_view_card_delete:
                handleDeleteClicked();
                break;
            case R.id.fragment_view_card_add_camera:
                Intent scanIntent = new Intent(getActivity(), CardIOActivity.class);
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
                startActivityForResult(scanIntent, REQUEST_CODE_SCAN_CREDIT_CARD);
                break;
            case R.id.fragment_view_card_add:
                requestSaveCard();
                showViewBasedOnState();
                break;
            case R.id.fragment_view_card_saved_billing_info_primary_address_edit:
                Intent addressInfoIntent = new Intent(getActivity().getBaseContext(), AddressInformationActivity.class);
                Bundle billingArgs = AddressInformationActivity
                        .newInstanceBundle(SourceId.GUEST_CHECKOUT, mSelectedBillingAddressId, false);
                startActivityForResult(addressInfoIntent.putExtras(billingArgs), REQUEST_CODE_ADDRESS);
                break;
            case R.id.phone_number_info_popup:
                Toast.makeText(getContext(), mBinding.getTridion().getPhoneNumberToolTip(), Toast.LENGTH_LONG)
                        .show();
                break;
            case R.id.fragment_view_card_security_code_info_popup:
                Toast.makeText(getContext(), mBinding.getTridion().getSecurityCodeToolTip(), Toast.LENGTH_LONG)
                        .show();
                break;
        }
    }

    private void handleDeleteClicked() {
        int numCardsInWallet = mCards.size();
        if (numCardsInWallet == 1) {
            // "Deleting your primary card will prevent you from using Tap-To-Pay. Are you sure you want to continue?"
            DialogUtils.showTwoButtonMessageDialog(getContext(),
                    null,
                    mTridionConfig.getWalletCardDeletePrimaryMsg(),
                    mTridionConfig.getDeleteFolioCardLabel(),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestDeleteCard(mCardId);
                            showViewBasedOnState();
                        }
                    },
                    mTridionConfig.getCancelLabel(),
                    null);
        } else if (numCardsInWallet == 2) {
            //"Are you sure you want to delete this credit card from your account?"
            DialogUtils.showTwoButtonMessageDialog(getContext(),
                    null,
                    mTridionConfig.getWalletCardDeleteMsg(),
                    mTridionConfig.getDeleteFolioCardLabel(),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (WalletFolioCard card : mCards) {
                                if (card != null && !mCardId.equals(card.getCardId())) {
                                    mNewPrimaryCard = card;
                                    mSetPrimaryAfterDelete = true;
                                    break;
                                }
                            }
                            requestDeleteCard(mCardId);
                            showViewBasedOnState();
                        }
                    },
                    mTridionConfig.getCancelLabel(),
                    null);
            //if primary deleted, set other as primary
        } else {
            if (mSelectedCard.isPrimary()) {
                //Please choose another card to be your Primary Card before you delete this credit card from your account."

                final List<WalletFolioCard> cards = new ArrayList<>(mCards.size());
                cards.addAll(mCards);
                // Find and remove the address to be deleted
                for (int i = 0; i < cards.size(); i++) {
                    if (cards.get(i).getCardId().equals(mCardId)) {
                        cards.remove(i);
                        break;
                    }
                }
                //
                List<String> cardStrings = new ArrayList<>(cards.size());
                for (WalletFolioCard card : cards) {
                    cardStrings.add(StringUtils.leftPad(card.getLastFour(), 16, "•"));
                }
                ListDialogFragment listDialogFragment = ListDialogFragment.getInstance(cardStrings,
                        mTridionConfig.getW11());
                listDialogFragment.setOnClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mNewPrimaryCard = cards.get(which);
                        mSetPrimaryAfterDelete = true;
                        requestDeleteCard(mCardId);
                        showViewBasedOnState();
                    }
                });
                listDialogFragment.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                        showViewBasedOnState();
                    }
                });

                FragmentManager fm = getChildFragmentManager();
                listDialogFragment.show(fm, CARD_LIST_DIALOG_FRAGMENT);
            } else {
                DialogUtils.showTwoButtonMessageDialog(getContext(),
                        null,
                        mTridionConfig.getWalletCardDeleteMsg(),
                        mTridionConfig.getDeleteFolioCardLabel(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestDeleteCard(mCardId);
                                showViewBasedOnState();
                            }
                        },
                        mTridionConfig.getCancelLabel(),
                        null);
            }
        }
    }

    private void requestDeleteCard(String cardId) {
        if (!mIsCallInProgress) {
            if (mCardId != null) {
                mIsCallInProgress = true;

                DeleteCardRequest request = new DeleteCardRequest.Builder(this)
                        .setCardId(cardId)
                        .build();

                NetworkUtils.queueNetworkRequest(request);
                NetworkUtils.startNetworkService();
            }
        }
    }

    public void requestGetCards() {
        if (!mIsCallInProgress) {
            mIsCallInProgress = true;

            GetWalletCardsRequest request = new GetWalletCardsRequest.Builder(this)
                    .build();

            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        }
    }

    public void requestSetPrimaryCard(String cardId) {
        if (!mIsCallInProgress) {
            mIsCallInProgress = true;

            SetPrimaryCardRequest request = new SetPrimaryCardRequest.Builder(this)
                    .setCardId(cardId)
                    .build();

            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        }
    }

    private void requestModifyCard() {
        if (!mIsCallInProgress) {
            mIsCallInProgress = true;

            String expireMonth = CreditCardInfo.EXPIRATION_MONTHS[mCardViewModel.creditCardInfo.getExpirationMonth()];
            String expireYear = CreditCardInfo.EXPIRATION_YEARS[mCardViewModel.creditCardInfo.getExpirationYear()];

            EditCardRequest request = new EditCardRequest.Builder(this)
                    .setExternalGuestId(AccountStateManager.getGuestId())
                    .setPaymentAccountNumber(mCardViewModel.creditCardInfo.getCreditCardNumber())
                    .setDeleteCardId(mSelectedCard.getCardId())
                    .setPrimary(mCardViewModel.creditCardInfo.isPrimary())
                    .setExpireMonth(expireMonth)
                    .setExpireYear(expireYear)
                    .setCardSecurityCode(mCardViewModel.creditCardInfo.getSecurityCode())
                    .setAddressId(mSelectedBillingAddressId)
                    .build();

            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        }
    }

    private void requestSaveCard() {
        if (!mIsCallInProgress) {
            mIsCallInProgress = true;

            String expireMonth = CreditCardInfo.EXPIRATION_MONTHS[mCardViewModel.creditCardInfo.getExpirationMonth()];
            String expireYear = CreditCardInfo.EXPIRATION_YEARS[mCardViewModel.creditCardInfo.getExpirationYear()];

            //set this card as primary if clicked or if no cards in folio
            boolean isPrimary = mCardViewModel.creditCardInfo.isPrimary() || mCards == null || mCards.size() == 1;

            AddCardRequest request = new AddCardRequest.Builder(this)
                    .setExternalGuestId(AccountStateManager.getGuestId())
                    .setPrimary(isPrimary)
                    .setPaymentAccountNumber(mCardViewModel.creditCardInfo.getCreditCardNumber())
                    .setExpireMonth(expireMonth)
                    .setExpireYear(expireYear)
                    .setCardSecurityCode(mCardViewModel.creditCardInfo.getSecurityCode())
                    .setAddressId(mSelectedBillingAddressId)
                    .build();

            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        }
    }

    /**
     * Gets the User's profile
     */
    private void requestGetProfile() {
        if (!mIsCallInProgress) {
            mIsCallInProgress = true;
            GetGuestProfileRequest request = new GetGuestProfileRequest.Builder(this)
                    .build();

            NetworkUtils.queueNetworkRequest(request);
            NetworkUtils.startNetworkService();
        }
    }

    @Override
    public void handleNetworkResponse(NetworkResponse response) {
        mIsCallInProgress = false;
        if (response instanceof GetWalletCardsResponse) {
            GetWalletCardsResponse getWalletCardsResponse = (GetWalletCardsResponse) response;
            if (getWalletCardsResponse.getResult() != null) {
                mCards = getWalletCardsResponse.getResult();
                for (WalletFolioCard card : mCards) {
                    if (mCardId != null && mCardId.equalsIgnoreCase(card.getCardId())) {
                        mSelectedCard = card;
                        CreditCardInfo creditCardInfo = new CreditCardInfo();
                        creditCardInfo.setPrimary(mSelectedCard.isPrimary());

                        Integer expireMonth = mSelectedCard.getExpiration().getMonthInt();
                        if (expireMonth != null) {
                            creditCardInfo.setExpirationMonth(expireMonth - 1);
                        }

                        Integer expireYear = mSelectedCard.getExpiration().getYearInt();
                        if (expireYear != null) {
                            Calendar calendar = Calendar.getInstance();
                            int currentYear = calendar.get(Calendar.YEAR);
                            creditCardInfo.setExpirationYear(expireYear - currentYear);
                        }

                        mCardViewModel.setCreditCardInfo(creditCardInfo);
                        mCardViewModel.setNewCard(false);

                        mSelectedBillingAddressId = mSelectedCard.getAddressId();

                        ArrayList<AddressInfo> savedAddresses = mCardViewModel.getSavedAddresses();
                        Integer selectedAddressPosition = null;
                        for (int i = 0; i < savedAddresses.size(); i++) {
                            AddressInfo addressInfo = savedAddresses.get(i);
                            if (addressInfo.getAddressId().equalsIgnoreCase(mSelectedBillingAddressId)) {
                                selectedAddressPosition = i;
                                break;
                            }
                        }
                        if (selectedAddressPosition != null) {
                            mBinding.fragmentViewCardSavedBillingInfoSpinner.setSelection(selectedAddressPosition);
                        } else {
                            Log.w(TAG, "handleNetworkResponse: Card address not found in profile");
                        }

                        //set hints
                        mBinding.fragmentViewCardCardNumber.setHint(StringUtils.leftPad(mSelectedCard.getLastFour(), 16, "•"));
                        mBinding.fragmentViewCardSecurityCode.setHint("•••");
                        break;
                    }
                }
            }
        } else if (response instanceof GetGuestProfileResponse) {
            GetGuestProfileResponse guestProfileResponse = (GetGuestProfileResponse) response;
            if (guestProfileResponse.getResult() != null && guestProfileResponse.getStatusCode() == HttpResponseStatus.SUCCESS_OK) {
                mGuestProfile = guestProfileResponse.getResult().getGuestProfile();
                if (null != mGuestProfile && null != mGuestProfile.getAddresses()) {
                    ArrayList<AddressInfo> savedAddresses = new ArrayList<>(mGuestProfile.getAddresses().size());
                    for (Address a : mGuestProfile.getAddresses()) {
                        savedAddresses.add(new AddressInfo(a, mCountryStateArrays));
                    }
                    updateSavedAddresses(savedAddresses);
                    requestGetCards();
                }
            } else {
                showResponseErrorMessage();
            }
        } else if (response instanceof AddCardResponse) {
            AddCardResponse addCardResponse = (AddCardResponse) response;
            if (addCardResponse.getResult() != null) {
                Toast.makeText(getContext(), mTridionConfig.getSu13(), Toast.LENGTH_SHORT).show();
                if (mListener != null) {
                    mListener.onCardAdded();
                }
            } else {
                showResponseErrorMessage();
            }
        } else if (response instanceof DeleteCardResponse) {
            DeleteCardResponse deleteCardResponse = (DeleteCardResponse) response;
            if (deleteCardResponse.getStatusCode() == HttpResponseStatus.SUCCESS_OK) {
                if (mSetPrimaryAfterDelete) {
                    requestSetPrimaryCard(mNewPrimaryCard.getCardId());
                } else {
                    Toast.makeText(getContext(), mTridionConfig.getSu21(), Toast.LENGTH_SHORT).show();
                    boolean lastCardDeleted = mCards.size() == 1;

                    if (!lastCardDeleted) {
                        Toast.makeText(getContext(), mTridionConfig.getSu21(), Toast.LENGTH_SHORT).show();
                    }

                    if (mListener != null) {
                        mListener.onCardDeleted(lastCardDeleted);
                    }
                }
            } else {
                Toast.makeText(getContext(), mTridionConfig.getEr79(), Toast.LENGTH_SHORT).show();
            }
        } else if (response instanceof SetPrimaryCardResponse) {
            SetPrimaryCardResponse setPrimaryCardResponse = (SetPrimaryCardResponse) response;
            if (HttpResponseStatus.isHttpStatusCodeSuccess(setPrimaryCardResponse.getStatusCode())) {
                if (mSetPrimaryAfterDelete) {
                    Toast.makeText(getContext(), mTridionConfig.getSu21(), Toast.LENGTH_SHORT).show();
                    if (mListener != null) {
                        mListener.onCardDeleted(false);
                    }
                } else {
                    if (mNewPrimaryCard != null) {
                        Toast.makeText(getContext(), mTridionConfig.getSu20(mNewPrimaryCard.getLastFour()), Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
                Toast.makeText(getContext(), mTridionConfig.getEr42(), Toast.LENGTH_SHORT).show();
            }
        } else if (response instanceof EditCardResponse) {
            EditCardResponse editCardResponse = (EditCardResponse) response;
            if (editCardResponse.getStatusCode() == HttpResponseStatus.SUCCESS_OK) {
                Toast.makeText(getContext(), mTridionConfig.getSu13(), Toast.LENGTH_SHORT).show();
                if (mListener != null) {
                    mListener.onCardUpdated();
                }
            } else {
                Toast.makeText(getContext(), mTridionConfig.getEr42(), Toast.LENGTH_SHORT).show();
            }
        }

        showViewBasedOnState();
    }

    private void showResponseErrorMessage() {
        showMessage(mTridionConfig.getEr71());
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG)
                .setIconColor(Color.RED)
                .show();
    }


    private void updateSavedAddresses(ArrayList<AddressInfo> savedAddresses) {
        // clear first since adapters are pointing to same lists, and clearing after adding
        // will clear the data for the adapter.
        mBillingAddressSpinnerAdapter.clear();
        mCardViewModel.setSavedAddresses(savedAddresses);
        mBillingAddressSpinnerAdapter.notifyDataSetChanged();
        mBinding.fragmentViewCardSavedBillingInfoSpinner.setOnItemSelectedListener(this);
        if (mCardViewModel.getSavedAddresses().size() > 0) {
            // if there is at least one saved address, set selection so it triggers
            // onItemSelectedListener for cascading effects
            mBinding.fragmentViewCardSavedBillingInfoSpinner.setSelection(getSelectedSpinnerPos(R.id.saved_billing_info_spinner));
            updateAddressSpinnerPositions(mCardViewModel.getBillingAddressInfo());
        }
    }

    private void updateAddressSpinnerPositions(AddressInfo addressInfo) {
        int countryPos = mCountryStateArrays.findCountryPosition(addressInfo.getCountryCode());
        int statePos = mCountryStateArrays.findStatePosition(addressInfo.getStateProvince());
        addressInfo.setCountryPosition(countryPos);
        addressInfo.setStatePosition(statePos);
    }

    /**
     * Returns the selected address spinner position, distinguishing between the spinners
     * via the addressType
     *
     * @param spinnerId The id of the spinner
     * @return The selected address spinner position
     */
    private int getSelectedSpinnerPos(@IdRes int spinnerId) {
        String matchAddressId = null;
        switch (spinnerId) {
            case R.id.saved_billing_info_spinner: {
                AddressInfo cachedAddress = AddressCache.getBillingAddress();
                if (null != cachedAddress && !TextUtils.isEmpty(cachedAddress.getAddressId())) {
                    matchAddressId = cachedAddress.getAddressId();
                } else {
                    matchAddressId = mSelectedBillingAddressId;
                }
                break;
            }
        }
        List<Address> guestProfileAddresses = mGuestProfile.getAddresses();
        if (guestProfileAddresses == null || guestProfileAddresses.isEmpty()) {
            return 0;
        }
        // If the matchAddressId was null, then there was no submitted billing/shipping
        // address ID, meaning that we have not added/edited any addresses yet (so just
        // show the primary address)
        if (null == matchAddressId) {
            for (int i = 0; i < guestProfileAddresses.size(); i++) {
                if (guestProfileAddresses.get(i) != null &&
                        guestProfileAddresses.get(i).isPrimary()) {
                    return i;
                }
            }
            // no primary found, so return 0 (first address position)
            return 0;
        }
        for (int i = 0; i < guestProfileAddresses.size(); i++) {
            if (guestProfileAddresses.get(i) != null &&
                    TextUtils.equals(guestProfileAddresses.get(i).getAddressId(), matchAddressId)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Logic performed when one of the spinners (parent) has an item (address) that has been
     * selected.
     *
     * @param parent   The spinner whose item was selected
     * @param view     The view
     * @param position The position of selected item
     * @param id       The id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == parent.getCount() - 1) {
            mBinding.fragmentViewCardSavedBillingInfoPrimaryAddressEdit.setVisibility(View.GONE);
            switch (parent.getId()) {
                case R.id.saved_billing_info_spinner:
                    mBinding.fragmentViewCardSavedBillingInfoSpinnerArrow.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            Intent addressInfoIntent = new Intent(getActivity().getBaseContext(), AddressInformationActivity.class);
            Bundle args = AddressInformationActivity.newInstanceBundle(SourceId.GUEST_CHECKOUT, null, false);
            startActivityForResult(addressInfoIntent.putExtras(args), REQUEST_CODE_ADDRESS);

        } else {
            AddressInfo addressInfo;
            switch (parent.getId()) {
                case R.id.fragment_view_card_saved_billing_info_spinner:
                    //if add address item selected,
                    if (position < mBillingAddressSpinnerAdapter.getCount()) {
                        addressInfo = mBillingAddressSpinnerAdapter.getItem(position);
                        if (addressInfo != null) {
                            mSelectedBillingAddressId = addressInfo.getAddressId();
                        }
                        addressInfo = mBillingAddressSpinnerAdapter.getItem(position);
                        mBinding.fragmentViewCardSavedBillingInfoSpinnerArrow.setVisibility(View.VISIBLE);
                        mBinding.fragmentViewCardSavedBillingInfoPrimaryAddressEdit.setVisibility(View.VISIBLE);
                        mBinding.fragmentViewCardSavedBillingInfoPrimaryAddressText.setText(addressInfo.toFormattedString());
                        mBinding.fragmentViewCardSavedBillingInfoPrimaryLabel.setVisibility(addressInfo.isPrimaryAddress() ? View.VISIBLE : View.GONE);
                        mCardViewModel.getBillingAddressInfo().copy(addressInfo);
                        break;
                    }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showViewBasedOnState() {
        if (mIsCallInProgress) {
            showLoadingView();
        } else {
            hideLoadingView();
        }
    }

    private void hideLoadingView() {
        Handler mHandler = new Handler(getActivity().getMainLooper());
        mHandler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.cancelLoadingView(getActivity().getSupportFragmentManager());
                    }
                });
    }

    private void showLoadingView() {
        // Post to handler to make sure it is on the main thread
        Handler mHandler = new Handler(getActivity().getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DialogUtils.showLoadingView(getActivity().getSupportFragmentManager());
            }
        });
    }

    /**
     * @param newCard
     * @param checked
     */
    @Override
    public void onPrimaryCheckChange(boolean newCard, boolean checked) {
        if (!newCard && !TextUtils.isEmpty(mCardId)) {
            String primaryCardId = null;
            if (checked) {
                primaryCardId = mCardId;
            } else {
                if (mCards != null && mCards.size() > 1) {
                    for (WalletFolioCard card : mCards) {
                        if (card != null && !mCardId.equals(card.getCardId())) {
                            mNewPrimaryCard = card;
                            primaryCardId = card.getCardId();
                            break;
                        }
                    }
                }
            }

            if (!TextUtils.isEmpty(primaryCardId)) {
                mSetPrimaryAfterDelete = false;
                requestSetPrimaryCard(primaryCardId);
                showViewBasedOnState();
            } else {
                Log.d(TAG, "onPrimaryCheckChange: no card to set as primary");
            }
        }
    }

    @Override
    public void onPositiveClick(DialogInterface dialog) {
        getActivity().finish();
    }

    @Override
    public void onNegativeClick(DialogInterface dialog) {
        dialog.dismiss();
    }

    @Override
    public void onNeutralTextClick(DialogInterface dialog) {
        //do nothing
    }


    interface ViewCardListener {
        void onCardAdded();

        void onCardDeleted(boolean lastCardDeleted);

        void onCardUpdated();
    }
}
