package com.universalstudios.orlandoresort.controller.userinterface.linkentitlement;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.linkentitlement.data.LinkEntitlementModel;
import com.universalstudios.orlandoresort.controller.userinterface.linkentitlement.data.LinkEntitlementStep2ViewModel;
import com.universalstudios.orlandoresort.controller.userinterface.linkentitlement.data.SalesProgram;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.FragmentLinkEntitlementStep2Binding;
import com.universalstudios.orlandoresort.model.network.domain.linkentitlements.GetSalesProgramsRequest;
import com.universalstudios.orlandoresort.model.network.domain.linkentitlements.GetSalesProgramsResponse;
import com.universalstudios.orlandoresort.model.network.domain.linkentitlements.LinkEntitlementsRequest;
import com.universalstudios.orlandoresort.model.network.domain.linkentitlements.LinkEntitlementsResponse;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.response.HttpResponseStatus;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Map;


public class LinkEntitlementStep2Fragment extends NetworkFragment implements NetworkRequestSender {

	private static final String TAG = LinkEntitlementStep2Fragment.class.getSimpleName();

	private static final String KEY_ARG_IS_CALL_IN_PROGRESS = "KEY_ARG_IS_CALL_IN_PROGRESS";
	private static final String KEY_ARG_LINK_ENTITLEMENT_MODEL = "KEY_ARG_LINK_ENTITLEMENT_MODEL";

	private LinkEntitlementListener mListener;

	//show active or inactive entitlements
	private boolean mIsCallInProgress;
	private LinkEntitlementStep2ViewModel mViewModel;
	private FragmentLinkEntitlementStep2Binding mBinding;
	private SaleProgramsAdapter mSalesProgramsAdapter;

	public interface ActionCallback {
		void onSalesProgramSelected(SalesProgram salesProgram);
		void onSubmitClicked();
		void onCancelClicked();
	}

	private ActionCallback mActionCallback = new ActionCallback() {
		@Override
		public void onSalesProgramSelected(SalesProgram salesProgram) {
			mViewModel.getLinkEntitlementModel().setSalesProgram(salesProgram);
		}

		@Override
		public void onSubmitClicked() {
			requestLinkEntitlement();
		}

		@Override
		public void onCancelClicked() {
			if (null != mListener) {
				mListener.onCancel();
			}
		}
	};

	public LinkEntitlementStep2Fragment() {
	}

	public static LinkEntitlementStep2Fragment newInstance(LinkEntitlementModel linkEntitlementModel) {
		// Create a new fragment instance
		LinkEntitlementStep2Fragment fragment = new LinkEntitlementStep2Fragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
			args.putParcelable(KEY_ARG_LINK_ENTITLEMENT_MODEL, Parcels.wrap(linkEntitlementModel));
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
			linkEntitlementModel = Parcels.unwrap(args.getParcelable(KEY_ARG_LINK_ENTITLEMENT_MODEL));
		}

		if (savedInstanceState != null) {
			linkEntitlementModel = Parcels.unwrap(savedInstanceState.getParcelable(KEY_ARG_LINK_ENTITLEMENT_MODEL));
			mIsCallInProgress = savedInstanceState.getBoolean(KEY_ARG_IS_CALL_IN_PROGRESS, false);
		} else {
			mIsCallInProgress = false;
		}

		if (null == linkEntitlementModel) {
			if (BuildConfig.DEBUG) {
				throw new IllegalArgumentException("LinkEntitlementModel is required. Use newInstance(LinkEntitlementModel) to instantiate fragment");
			} else {
				mViewModel = new LinkEntitlementStep2ViewModel(new LinkEntitlementModel());
			}
		} else {
			mViewModel = new LinkEntitlementStep2ViewModel(linkEntitlementModel);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView");
		}

		mBinding = FragmentLinkEntitlementStep2Binding.inflate(inflater);
		mBinding.setTridion(IceTicketUtils.getTridionConfig());
		mBinding.setViewModel(mViewModel);
		mBinding.setCallback(mActionCallback);

		mBinding.selectChannelRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
		mSalesProgramsAdapter = new SaleProgramsAdapter(mActionCallback);

		mBinding.selectChannelRecyclerView.setAdapter(mSalesProgramsAdapter);

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

		if (null != mViewModel.getLinkEntitlementModel() &&
				mViewModel.getLinkEntitlementModel().getEntitleLinkType() == LinkEntitlementModel.LINK_TYPE_SALES_PROGRAM) {
			requestSalesPrograms();
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

		if (networkResponse instanceof LinkEntitlementsResponse) {
			handleLinkEntitlementsResponse((LinkEntitlementsResponse) networkResponse);
		} else if (networkResponse instanceof GetSalesProgramsResponse) {
			handleGetSalesProgramsResponse((GetSalesProgramsResponse) networkResponse);
		}
	}

	private void requestSalesPrograms() {
		//do not make call if it is already in progress
		if (mIsCallInProgress) {
			return;
		}

		mIsCallInProgress = true;
		showViewBasedOnState();

		GetSalesProgramsRequest request = new GetSalesProgramsRequest.Builder(this)
				.setConcurrencyType(NetworkRequest.ConcurrencyType.ASYNCHRONOUS)
				.build();
		NetworkUtils.queueNetworkRequest(request);
		NetworkUtils.startNetworkService();
	}

	private void handleGetSalesProgramsResponse(@NonNull GetSalesProgramsResponse response) {
		if (response.isHttpStatusCodeSuccess()) {
			ArrayList<SalesProgram> salesPrograms = new ArrayList<>();
			Map<String, String> salesProgramsMap = response.getResult();
			if (null != salesProgramsMap) {
				for (String key : salesProgramsMap.keySet()) {
					salesPrograms.add(new SalesProgram(key, salesProgramsMap.get(key)));
				}
			}
			mSalesProgramsAdapter.setSalesPrograms(salesPrograms);

			showViewBasedOnState();
		} else {
			Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getEr71(), Toast.LENGTH_LONG)
					.setIconColor(Color.RED)
					.show();
			showViewBasedOnState();
		}
	}

	private void requestLinkEntitlement() {
		//do not make call if it is already in progress
		if (mIsCallInProgress) {
			return;
		}

		mIsCallInProgress = true;
		showViewBasedOnState();

		// This needs to be asynchronous so the response code is parsed properly. Otherwise,
		// a 200 is returned regardless of the success code.
		LinkEntitlementModel linkEntitlementModel = mViewModel.getLinkEntitlementModel();
		LinkEntitlementsRequest request = null;
		LinkEntitlementsRequest.Builder builder = new LinkEntitlementsRequest.Builder(this)
				.setConcurrencyType(NetworkRequest.ConcurrencyType.ASYNCHRONOUS)
				.addMediaId(linkEntitlementModel.getOrderOrTicketNumber());
		if (linkEntitlementModel.getEntitleLinkType() == LinkEntitlementModel.LINK_TYPE_NAME) {
			builder.setFirstName(linkEntitlementModel.getFirstName())
					.setLastName(linkEntitlementModel.getLastName());
			request = builder.build();
		} else if (null != linkEntitlementModel.getSalesProgram()) {
			builder.setSalesProgramId(linkEntitlementModel.getSalesProgram().getProgramCode());
			request = builder.build();
		}

		if (null != request) {
			NetworkUtils.queueNetworkRequest(request);
			NetworkUtils.startNetworkService();
		} else {
			Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getEr71(), Toast.LENGTH_LONG)
					.setIconColor(Color.RED)
					.show();
		}
	}

	private void handleLinkEntitlementsResponse(@NonNull LinkEntitlementsResponse response) {
		String errorMsg = "";
		if (response.getHttpStatusCode() == HttpResponseStatus.SUCCESS_OK
				&& null != response.getResult()) {
			if (null != response.getResult().getMediaStatus()) {
				switch (response.getResult().getMediaStatus()) {
					case LinkEntitlementModel.MEDIA_STATUS_VALID:
						Toast.makeText(getContext(), IceTicketUtils.getTridionConfig().getSu16(), Toast.LENGTH_LONG)
								.show();
						mListener.onSecondFactorValidated(mViewModel.getLinkEntitlementModel());
						break;
					case LinkEntitlementModel.MEDIA_STATUS_INVALID_SECOND_FACTOR:
						if (mViewModel.getLinkEntitlementModel().getEntitleLinkType() == LinkEntitlementModel.LINK_TYPE_NAME) {
							errorMsg = IceTicketUtils.getTridionConfig().getEr90();
						} else {
							errorMsg = IceTicketUtils.getTridionConfig().getEr91();
						}
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
			errorMsg = IceTicketUtils.getTridionConfig().getEr71();
		}
		if (!TextUtils.isEmpty(errorMsg)) {
			Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG)
					.setIconColor(Color.RED)
					.show();
		}
		showViewBasedOnState();
	}

}
