package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletEntitlementModel;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletProductItemViewModel;
import com.universalstudios.orlandoresort.databinding.FragmentWalletProductBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class WalletProductFragment extends Fragment {
	private static final String TAG = WalletProductFragment.class.getSimpleName();

	private static final String KEY_ARGS_WALLET_ENTITLEMENT_MODELS = "KEY_ARGS_WALLET_ENTITLEMENT_MODELS";
	private static final String STATE_VAR_WALLET_ITEM_VIEW_MODELS = "STATE_VAR_WALLET_ITEM_VIEW_MODELS";
	private static final String CONTENT_GROUP = AnalyticsUtils.CONTENT_GROUP_SALES;
	private static final String CONTENT_FOCUS = AnalyticsUtils.CONTENT_FOCUS_TICKETS;
	private static final String CONTENT_SUB_2 = AnalyticsUtils.UO_PAGE_IDENTIFIER_WALLET_TICKET_CARDS;
	private static final Map<String,Object> EXTRA_DATA = new HashMap<>();

	MoreInfoClickListener mListener;

	private RecyclerView mProductList;
	private TextView mTicketPurchased;
	private TextView mPurchasedDate;
	private WalletItemDetailAdapter mAdapter;
	private TridionConfig mTridionConfig;

	private WalletProductItemViewModel mExpandedItem;

	private WalletProductItemViewModel.WalletProductItemCallback mItemCallback =
			new WalletProductItemViewModel.WalletProductItemCallback() {
		@Override
		public void onExpandItemClicked(WalletProductItemViewModel walletProductItemViewModel) {
			expandItem(walletProductItemViewModel);
		}

		@Override
		public void onMoreInfoClicked(WalletEntitlementModel walletEntitlementModel) {
			if (mListener != null) {
				mListener.onMoreInfoClicked(walletEntitlementModel);
			}
		}
	};
	private List<WalletProductItemViewModel> mWalletProductItemViewModels;

	public WalletProductFragment() {
	}

	public static WalletProductFragment newInstance(List<WalletEntitlementModel> walletEntitlementModels) {
		Bundle bundle = new Bundle();
		bundle.putParcelable(KEY_ARGS_WALLET_ENTITLEMENT_MODELS, Parcels.wrap(walletEntitlementModels));

		WalletProductFragment fragment = new WalletProductFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onAttach");
		}
		// Check if parent fragment (if there is one) implements the interface
		Fragment parentFragment = getParentFragment();
		if (parentFragment != null && parentFragment instanceof MoreInfoClickListener) {
			mListener = (MoreInfoClickListener) parentFragment;
		}
		// Otherwise, check if parent activity implements the interface
		else if (context != null && context instanceof MoreInfoClickListener) {
			mListener = (MoreInfoClickListener) context;
		}
		// If neither implements the interface, log a warning
		else if (mListener == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement MoreInfoClickListener");
			}
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		List<WalletEntitlementModel> walletEntitlementModels;
		if (args != null) {
			walletEntitlementModels = Parcels.unwrap(args.getParcelable(KEY_ARGS_WALLET_ENTITLEMENT_MODELS));
		} else {
			walletEntitlementModels = null;
		}

		mTridionConfig = IceTicketUtils.getTridionConfig();
		if (null != savedInstanceState) {
			if (savedInstanceState.containsKey(STATE_VAR_WALLET_ITEM_VIEW_MODELS)) {
				mWalletProductItemViewModels = Parcels.unwrap(savedInstanceState.getParcelable(STATE_VAR_WALLET_ITEM_VIEW_MODELS));
				if (null != mWalletProductItemViewModels) {
					for (WalletProductItemViewModel viewModel : mWalletProductItemViewModels) {
						if (null != viewModel && viewModel.isExpanded()) {
							mExpandedItem = viewModel;
						}
					}
				}
			}
		} else if (null != walletEntitlementModels) {
			mWalletProductItemViewModels = new ArrayList<>(walletEntitlementModels.size());
			for (WalletEntitlementModel model : walletEntitlementModels) {
				mWalletProductItemViewModels.add(new WalletProductItemViewModel(model));
			}
			// First item is expanded by default
			if (!mWalletProductItemViewModels.isEmpty()) {
				mExpandedItem = mWalletProductItemViewModels.get(0);
			}
		}

		sendOnPageLoadAnalytics();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (null != mWalletProductItemViewModels && !mWalletProductItemViewModels.isEmpty()) {
			outState.putParcelable(STATE_VAR_WALLET_ITEM_VIEW_MODELS, Parcels.wrap(mWalletProductItemViewModels));
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		FragmentWalletProductBinding binding = FragmentWalletProductBinding.inflate(inflater);
		binding.setTridion(mTridionConfig);
		View fragmentView = binding.getRoot();
		mProductList = binding.fragmentWalletProductList;
		mTicketPurchased = binding.fragmentWalletProductPurchaseCount;
		mPurchasedDate = binding.fragmentWalletProductPurchaseDate;
		if (null != mWalletProductItemViewModels && !mWalletProductItemViewModels.isEmpty()) {
			int entitlementCount = mWalletProductItemViewModels.size();
			String ticketCountText = entitlementCount == 1 ? mTridionConfig.getOneTicketCount() :
					String.format(Locale.US, "%d %s", entitlementCount, mTridionConfig.getSCTicketsLabel());
			mTicketPurchased.setText(ticketCountText);
		}
		expandItem(mExpandedItem);

		mAdapter = new WalletItemDetailAdapter(mTridionConfig, mItemCallback, mWalletProductItemViewModels.toArray(new WalletProductItemViewModel[]{}));

		mProductList.setAdapter(mAdapter);
		mProductList.setHasFixedSize(true);
		mProductList.setLayoutManager(new LinearLayoutManager(getContext()));
		mProductList.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getActivity(), R.drawable.shape_wallet_list_entitlement_item_divider)));

		return fragmentView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}


	private void expandItem(WalletProductItemViewModel walletProductItemViewModel) {
		if (null != mExpandedItem) {
			mExpandedItem.setExpanded(false);
			mExpandedItem = null;
		}
		if (null != walletProductItemViewModel) {
			walletProductItemViewModel.setExpanded(true);
			mExpandedItem = walletProductItemViewModel;
			WalletEntitlementModel walletEntitlement = mExpandedItem.getModel();
			if (walletEntitlement != null) {
				String purchaseDate = walletEntitlement.getPurchasedDate();
				if (!TextUtils.isEmpty(purchaseDate)) {
					mPurchasedDate.setText(purchaseDate);
				}
			}
		}
	}

	interface MoreInfoClickListener {
		void onMoreInfoClicked(WalletEntitlementModel walletEntitlementModel);
	}

	private void sendOnPageLoadAnalytics() {
		AnalyticsUtils.trackPageView(CONTENT_GROUP, CONTENT_FOCUS, null, CONTENT_SUB_2,
				null, null, EXTRA_DATA);
	}
}
