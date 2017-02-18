package com.universalstudios.orlandoresort.controller.userinterface.linkentitlement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.linkentitlement.data.LinkEntitlementModel;
import com.universalstudios.orlandoresort.controller.userinterface.linkentitlement.data.LinkEntitlementStep1ViewModel;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.FragmentLinkEntitlementStep1Binding;
import com.universalstudios.orlandoresort.model.network.domain.linkentitlements.GetLinkEntitlementsMethodsRequest;
import com.universalstudios.orlandoresort.model.network.domain.linkentitlements.GetLinkEntitlementsMethodsResponse;
import com.universalstudios.orlandoresort.model.network.domain.linkentitlements.GetLinkEntitlementsMethodsResult;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.response.HttpResponseStatus;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcels;


public class LinkEntitlementStep1Fragment extends NetworkFragment implements NetworkRequestSender {

	private static final String TAG = LinkEntitlementStep1Fragment.class.getSimpleName();

	private static final String KEY_ARG_IS_CALL_IN_PROGRESS = "KEY_ARG_IS_CALL_IN_PROGRESS";
	private static final String KEY_ARG_LINK_ENTITLEMENT_MODEL = "KEY_ARG_LINK_ENTITLEMENT_MODEL";

	private LinkEntitlementListener mListener;

	//show active or inactive entitlements
	private boolean mIsCallInProgress;
	private LinkEntitlementStep1ViewModel mViewModel;
	private FragmentLinkEntitlementStep1Binding mBinding;

	public interface ActionCallback {
		void onTicketNumberInfoClicked();
		void onScanTicketClicked();
		void onNextClicked();
		void onCancelClicked();
	}

	private ActionCallback mActionCallback = new ActionCallback() {
		@Override
		public void onTicketNumberInfoClicked() {
			Toast.makeText(getContext(),
					IceTicketUtils.getTridionConfig().getWalletLinkPurchaseInfoToolTip(), Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void onScanTicketClicked() {
			IntentIntegrator.forSupportFragment(LinkEntitlementStep1Fragment.this)
					.setBeepEnabled(false)
					.initiateScan();
		}

		@Override
		public void onNextClicked() {
			requestLinkMethods();
		}

		@Override
		public void onCancelClicked() {
			if (null != mListener) {
				mListener.onCancel();
			}

		}
	};

	public LinkEntitlementStep1Fragment() {
	}

	public static LinkEntitlementStep1Fragment newInstance() {
		// Create a new fragment instance
		LinkEntitlementStep1Fragment fragment = new LinkEntitlementStep1Fragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		if (context instanceof LinkEntitlementListener) {
			mListener = (LinkEntitlementListener) context;
		}
		else if (null == mListener) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement LinkEntitleListener");
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinkEntitlementModel linkEntitlementModel = null;
		Bundle args = getArguments();
		if (args == null) {
		}
		// Otherwise, set incoming parameters
		else {
		}

		if (savedInstanceState != null) {
			mIsCallInProgress = savedInstanceState.getBoolean(KEY_ARG_IS_CALL_IN_PROGRESS, false);
			linkEntitlementModel = Parcels.unwrap(savedInstanceState.getParcelable(KEY_ARG_LINK_ENTITLEMENT_MODEL));
		} else {
			mIsCallInProgress = false;
		}

		mViewModel = new LinkEntitlementStep1ViewModel(linkEntitlementModel);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView");
		}

		mBinding = FragmentLinkEntitlementStep1Binding.inflate(inflater);
		mBinding.setTridion(IceTicketUtils.getTridionConfig());
		mBinding.setViewModel(mViewModel);
		mBinding.setCallback(mActionCallback);

		return mBinding.getRoot();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(KEY_ARG_IS_CALL_IN_PROGRESS, mIsCallInProgress);
		outState.putParcelable(KEY_ARG_LINK_ENTITLEMENT_MODEL, Parcels.wrap(mBinding.getViewModel().getLinkEntitlementModel()));
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewCreated");
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if(result != null) {
			if(result.getContents() == null) {
				// Cancelled
			} else {
				mViewModel.getLinkEntitlementModel().setOrderOrTicketNumber(result.getContents());
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void showViewBasedOnState() {
		if (mIsCallInProgress) {
			mViewModel.setShowLoading(true);
		} else {
			mViewModel.setShowLoading(false);
		}
	}

	@Override
	public void handleNetworkResponse(NetworkResponse networkResponse) {
		mIsCallInProgress = false;

		if (networkResponse instanceof GetLinkEntitlementsMethodsResponse) {
			handleGetLinkEntitlementsMethodsResponse((GetLinkEntitlementsMethodsResponse) networkResponse);
		}
	}

	private void requestLinkMethods() {
		//do not make call if it is already in progress
		if (mIsCallInProgress) {
			return;
		}

		mIsCallInProgress = true;
		showViewBasedOnState();

		// This needs to be asynchronous so the response code is parsed properly. Otherwise,
		// a 200 is returned regardless of the success code.
		GetLinkEntitlementsMethodsRequest request = new GetLinkEntitlementsMethodsRequest.Builder(this)
				.setConcurrencyType(NetworkRequest.ConcurrencyType.ASYNCHRONOUS)
				.setMediaId(mViewModel.getLinkEntitlementModel().getOrderOrTicketNumber())
				.build();

		NetworkUtils.queueNetworkRequest(request);
		NetworkUtils.startNetworkService();
	}

	private void handleGetLinkEntitlementsMethodsResponse(@NonNull GetLinkEntitlementsMethodsResponse response) {
		String errorMsg = "";
		if (response.getHttpStatusCode() == HttpResponseStatus.SUCCESS_OK
				&& null != response.getResult()) {
			GetLinkEntitlementsMethodsResult result = response.getResult();
			if (null != result.getMediaStatus()) {
				switch (result.getMediaStatus()) {
					case LinkEntitlementModel.MEDIA_STATUS_VALID:
						if (result.isName()) {
							mViewModel.getLinkEntitlementModel().setEntitleLinkType(LinkEntitlementModel.LINK_TYPE_NAME);
						} else if (result.isSalesProgramId()) {
							mViewModel.getLinkEntitlementModel().setEntitleLinkType(LinkEntitlementModel.LINK_TYPE_SALES_PROGRAM);
						}
						mListener.onFirstFactorValidated(mViewModel.getLinkEntitlementModel());
						break;
					case LinkEntitlementModel.MEDIA_STATUS_FORBIDDEN_TYPE:
						errorMsg = IceTicketUtils.getTridionConfig().getEr76();
						break;
					case LinkEntitlementModel.MEDIA_STATUS_LINKING_LOCKED:
						errorMsg = IceTicketUtils.getTridionConfig().getEr92();
						break;
					default:
						errorMsg = IceTicketUtils.getTridionConfig().getEr71();
				}
			} else {
				errorMsg = IceTicketUtils.getTridionConfig().getEr71();
			}
		} else {
			errorMsg = IceTicketUtils.getTridionConfig().getEr89();
		}
		if (!TextUtils.isEmpty(errorMsg)) {
			Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG)
					.setIconColor(Color.RED)
					.show();
		}
		showViewBasedOnState();
	}

}
