package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletEntitlementModel;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletProductDetailsViewModel;
import com.universalstudios.orlandoresort.databinding.FragmentWalletProductDetailsBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

public class WalletProductDetailsFragment extends Fragment {
	private static final String TAG = WalletProductDetailsFragment.class.getSimpleName();

	private static final String KEY_ARGS_WALLET_ENTITLEMENT_DATA_ITEM = "KEY_ARGS_WALLET_ENTITLEMENT_DATA_ITEM";

	private static final String CONTENT_GROUP = AnalyticsUtils.CONTENT_GROUP_SALES;
	private static final String CONTENT_FOCUS = AnalyticsUtils.CONTENT_FOCUS_TICKETS;
	private static final String CONTENT_SUB_2 = AnalyticsUtils.UO_PAGE_IDENTIFIER_WALLET_TICKET_CARDS;
	private static final Map<String,Object> EXTRA_DATA = new HashMap<>();

	WalletEntitlementModel mWalletEntitlementModel;
	WalletProductDetailsViewModel mViewModel;
	WalletProductDetailsViewModel.WalletProductDetailsCallback mCallback = new WalletProductDetailsViewModel.WalletProductDetailsCallback() {
		@Override
		public void onDetailsClicked(WalletEntitlementModel walletEntitlementModel) {
			startActivity(EntitlementDetailActivity.createIntent(getActivity(), walletEntitlementModel));

		}
	};

	public WalletProductDetailsFragment() {
	}

	public static WalletProductDetailsFragment newInstance(WalletEntitlementModel walletEntitlementModel) {
		Bundle bundle = new Bundle();
		bundle.putParcelable(KEY_ARGS_WALLET_ENTITLEMENT_DATA_ITEM, Parcels.wrap(walletEntitlementModel));

		WalletProductDetailsFragment fragment = new WalletProductDetailsFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mWalletEntitlementModel = Parcels.unwrap(getArguments().getParcelable(KEY_ARGS_WALLET_ENTITLEMENT_DATA_ITEM));
			sendPageTrackViewAnalytics();
		}
		mViewModel = new WalletProductDetailsViewModel(mWalletEntitlementModel);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		FragmentWalletProductDetailsBinding binding = FragmentWalletProductDetailsBinding.inflate(inflater);
		View fragmentView = binding.getRoot();
		TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();
		binding.setTridion(tridionConfig);
		binding.setData(mViewModel);
		binding.setCallback(mCallback);

		return fragmentView;
	}

	//TODO Analytics will be refactored for the february release
	private void sendPageTrackViewAnalytics(){
		AnalyticsUtils.trackTicketsPageView(
				AnalyticsUtils.CONTENT_SUB_2_TICKETS,
				AnalyticsUtils.PROPERTY_NAME_TICKETS,
				AnalyticsUtils.UO_PAGE_IDENTIFIER_WALLET_CARD_DETAIL,
				AnalyticsUtils.PROPERTY_NAME_PARKS_RESORT,
				AnalyticsUtils.PROPERTY_NAME_PRODUCT_SELECTION,
				AnalyticsUtils.PROPERTY_NAME_PARK_ADMISSION,
				null,
				null,
				"event27",
				false,
				null,
				null,
				null,
				null,
				null,
				null);
	}

	private void sendOnPageLoadAnalytics() {
		AnalyticsUtils.trackPageView(CONTENT_GROUP, CONTENT_FOCUS, null, CONTENT_SUB_2,
				null, null, EXTRA_DATA);
	}

}
