package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.linkentitlement.LinkEntitlementActivity;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletEntitlementModel;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletProductGroupViewModel;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletViewModel;
import com.universalstudios.orlandoresort.databinding.FragmentWalletBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetTridionSpecsRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses.GetTridionSpecsResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.request.GetMyWalletRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.GetMyWalletResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.WalletEntitlementDetails;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.WalletEntitlements;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WalletFragment extends NetworkFragment {

	private static final String TAG = WalletFragment.class.getSimpleName();

	private static final String KEY_ARG_IS_CALL_IN_PROGRESS = "KEY_ARG_IS_CALL_IN_PROGRESS";
	private static final String KEY_ARG_IS_VIEWING_ACTIVE_ENTITLEMENTS = "KEY_ARG_IS_VIEWING_ACTIVE_ENTITLEMENTS";
	private static final String KEY_ARG_AUTO_SHOW_FOR_SINGLE_ENTITLEMENT = "KEY_ARG_AUTO_SHOW_FOR_SINGLE_ENTITLEMENT";

	private static final String KEY_EVENT_NAME = "event_name";

	private String contentGroup = AnalyticsUtils.CONTENT_GROUP_SALES;
	private String contentFocus = AnalyticsUtils.CONTENT_FOCUS_TICKETS;
	private String contentSub2 = AnalyticsUtils.UO_PAGE_IDENTIFIER_WALLET_LANDING;
	private Map<String,Object> extraData = new HashMap<>();

	private RecyclerView mEntitlementList;

	private WalletFragmentListener mListener;

	//show active or inactive entitlements
	private boolean mShowActiveEntitlements;
	private boolean mIsCallInProgress;
	private List<WalletProductGroupViewModel> mWalletProductGroupViewModelList;
	private TridionConfig mTridionConfig;
	private WalletViewModel mViewModel;
	private boolean mAutoShowSingleEntitlement;

	private WalletViewModel.WalletCallback mWalletCallback = new WalletViewModel.WalletCallback() {
		@Override
		public void onLinkTicketClicked() {
			startActivity(LinkEntitlementActivity.newInstanceIntent(getContext()));
		}

		@Override
		public void onViewExpiredClicked() {
			if (null != mListener) {
				mListener.onViewExpiredClicked();
			}
		}

		@Override
		public void onShopNowClicked() {
			sendShopNowClickedAnalytics();
			if (null != mListener) {
				mListener.onShopNowClicked();
			}
		}
	};

	private WalletProductGroupViewModel.WalletProductGroupCallback mGroupCallback = new WalletProductGroupViewModel.WalletProductGroupCallback() {
		@Override
		public void onGroupClicked(List<WalletEntitlementModel> walletEntitlementModels) {
			startActivity(WalletProductActivity.newInstance(getContext(), walletEntitlementModels));
		}
	};

	public WalletFragment() {
	}

	public static WalletFragment newInstance(boolean showActive) {
		return newInstance(showActive, true);
	}

	public static WalletFragment newInstance(boolean showActive, boolean autoShow) {
		// Create a new fragment instance
		WalletFragment fragment = new WalletFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		args.putBoolean(KEY_ARG_IS_VIEWING_ACTIVE_ENTITLEMENTS, showActive);
		args.putBoolean(KEY_ARG_AUTO_SHOW_FOR_SINGLE_ENTITLEMENT, autoShow);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onAttach");
		}
		Fragment parentFragment = getParentFragment();

		// Check if parent fragment (if there is one) implements the interface
		if (parentFragment != null && parentFragment instanceof WalletFragmentListener) {
			mListener = (WalletFragmentListener) parentFragment;
		}
		// Otherwise, check if parent activity implements the interface
		else if (activity != null && activity instanceof WalletFragmentListener) {
			mListener = (WalletFragmentListener) activity;
		}
		// If neither implements the interface, log a warning
		else if (mListener == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement WalletFragmentListener");
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		if (args == null) {
			mAutoShowSingleEntitlement = true;
		}
		// Otherwise, set incoming parameters
		else {
			mShowActiveEntitlements = args.getBoolean(KEY_ARG_IS_VIEWING_ACTIVE_ENTITLEMENTS);
			mAutoShowSingleEntitlement = args.getBoolean(KEY_ARG_AUTO_SHOW_FOR_SINGLE_ENTITLEMENT);
		}

		mViewModel = new WalletViewModel();

		if (savedInstanceState != null) {
			mIsCallInProgress = savedInstanceState.getBoolean(KEY_ARG_IS_CALL_IN_PROGRESS, false);
		} else {
			mIsCallInProgress = false;
		}

		mTridionConfig = IceTicketUtils.getTridionConfig();

		mViewModel.setShowLoading(mIsCallInProgress);
		sendOnPageLoadAnalytics();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView");
		}

		FragmentWalletBinding binding = FragmentWalletBinding.inflate(inflater);
		binding.setTridion(mTridionConfig);
		binding.setData(mViewModel);
		binding.setCallback(mWalletCallback);
		View view = binding.getRoot();

		mEntitlementList = binding.fragmentWalletList;

		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
		mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mEntitlementList.setLayoutManager(mLayoutManager);
		mEntitlementList.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getActivity(), R.drawable.shape_wallet_divider)));

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(KEY_ARG_IS_CALL_IN_PROGRESS, mIsCallInProgress);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewCreated");
		}
		requestMyWallet();
		showViewBasedOnState();
	}

	private void showViewBasedOnState() {
		if (mIsCallInProgress) {
			showLoadingView();
		} else if (null != mWalletProductGroupViewModelList && !mWalletProductGroupViewModelList.isEmpty()) {
			if (mWalletProductGroupViewModelList.size() == 1 && mAutoShowSingleEntitlement) {
				// Only one wallet group so just show the group
				startActivity(WalletProductActivity.newInstance(getContext(),
						mWalletProductGroupViewModelList.get(0).getWalletEntitlementModelList()));
				if (null != mListener) {
					mListener.onOneWalletItem();
				}
			} else {
				showWalletGroupList();
			}
		} else {
			showNoPurchasesView();
		}
		// 12-22-16: no tabs yet but naming method in anticipation of future added tabs;
		// current page view = 'purchases tab'
		sendPurchasesTabAnalytics();
	}

	private void showWalletGroupList() {
		if (null == mWalletProductGroupViewModelList) {
			mWalletProductGroupViewModelList = new ArrayList<>();
		}

		WalletProductAdapter adapter = new
				WalletProductAdapter(mTridionConfig, mGroupCallback,
				mWalletProductGroupViewModelList.toArray(new WalletProductGroupViewModel[]{}));
		mEntitlementList.setAdapter(adapter);

		mViewModel.setShowLoading(false);
		mViewModel.setShowEmptyView(false);
		mViewModel.setShowExpiredEntitlements(mShowActiveEntitlements);
		mViewModel.setShowEntitlements(true);
	}

	private void showNoPurchasesView() {
		mViewModel.setShowLoading(false);
		mViewModel.setShowEmptyView(true);
		mViewModel.setShowExpiredEntitlements(mShowActiveEntitlements);
		mViewModel.setShowEntitlements(false);
	}

	private void showLoadingView() {
		mViewModel.setShowLoading(true);
		mViewModel.setShowEntitlements(false);
		mViewModel.setShowEmptyView(false);
		mViewModel.setShowExpiredEntitlements(false);
	}

	private void requestMyWallet() {
		//do not make call if it is already in progress
		if (mIsCallInProgress) {
			return;
		}

		mIsCallInProgress = true;

		GetMyWalletRequest getMyWalletRequest = new GetMyWalletRequest.Builder(this)
				.setUserName(AccountStateManager.getUsername())
				.setUserPassword(AccountStateManager.getPassword())
				.build();

		NetworkUtils.queueNetworkRequest(getMyWalletRequest);
		NetworkUtils.startNetworkService();
	}

	private void requestTridionSpecs(WalletEntitlements walletEntitlements) {
		if (null != walletEntitlements) {
			List<String> tcmids = new ArrayList<>();
			if (null != walletEntitlements.getActiveEntitlements()) {
				for (WalletEntitlementDetails details : walletEntitlements.getActiveEntitlements()) {
					tcmids.addAll(details.getTcmIds());
				}
			}
			if (null != walletEntitlements.getInactiveEntitlements()) {
				for (WalletEntitlementDetails details : walletEntitlements.getInactiveEntitlements()) {
					tcmids.addAll(details.getTcmIds());
				}
			}

			final GetTridionSpecsRequest getTridionSpecsRequest = new GetTridionSpecsRequest.Builder(this)
					.setConcurrencyType(NetworkRequest.ConcurrencyType.ASYNCHRONOUS)
					.setIds(tcmids)
					.build();

			NetworkUtils.queueNetworkRequest(getTridionSpecsRequest);
			NetworkUtils.startNetworkService();
		} else {
			showViewBasedOnState();
		}
	}

	@Override
	public void handleNetworkResponse(NetworkResponse networkResponse) {
		mIsCallInProgress = false;

		if (networkResponse instanceof GetMyWalletResponse) {
			handleGetMyWalletResponse((GetMyWalletResponse) networkResponse);
			requestTridionSpecs(((GetMyWalletResponse) networkResponse).getWalletEntitlements());
		} else if (networkResponse instanceof GetTridionSpecsResponse) {
			// This request handles the response so just update the view
			showViewBasedOnState();
		}
	}

	private void handleGetMyWalletResponse(@NonNull GetMyWalletResponse response) {
		if (response.getWalletEntitlements() != null) {
			mWalletProductGroupViewModelList = new ArrayList<>();
			List<WalletEntitlementDetails> walletEntitlementDetailsList = mShowActiveEntitlements ?
					response.getWalletEntitlements().getActiveEntitlements() :
					response.getWalletEntitlements().getInactiveEntitlements();

			if (null != walletEntitlementDetailsList) {
				for (WalletEntitlementDetails details : walletEntitlementDetailsList) {
					mWalletProductGroupViewModelList.add(new WalletProductGroupViewModel(details));
				}
			}
		}

	}

	interface WalletFragmentListener{
		void onOneWalletItem();
		void onShopNowClicked();
		void onViewExpiredClicked();
	}

	private void sendOnPageLoadAnalytics() {
		AnalyticsUtils.trackPageView(contentGroup, contentFocus, null, contentSub2,
				null, null, extraData);
	}

	private void sendShopNowClickedAnalytics() {
		extraData.put("event38", "Form Submission");
		extraData.put(KEY_EVENT_NAME, "SHOP NOW Button");
		AnalyticsUtils.trackPageView(contentGroup, contentFocus, null, contentSub2,
				null,null,extraData);
	}

	private void sendPurchasesTabAnalytics() {
		extraData.put("event38", "Form Submission");
		extraData.put(KEY_EVENT_NAME, "PURCHASES Tab");
		AnalyticsUtils.trackPageView(contentGroup, contentFocus, null, contentSub2,
				null,null,extraData);
	}
}
