package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletEntitlementModel;
import com.universalstudios.orlandoresort.databinding.FragmentEntitlementDetailsBinding;

import org.parceler.Parcels;

@Deprecated
public class EntitlementDetailFragment extends Fragment {
	private static final String TAG = EntitlementDetailFragment.class.getSimpleName();

	private static final String ARG_ENTITLEMENT_DATA_ITEM = "ARG_ENTITLEMENT_DATA_ITEM";

	private WalletEntitlementModel walletEntitlementModel;

	public static EntitlementDetailFragment newInstance(WalletEntitlementModel walletEntitlementModel) {
		EntitlementDetailFragment fragment = new EntitlementDetailFragment();
		Bundle b = new Bundle();
		b.putParcelable(ARG_ENTITLEMENT_DATA_ITEM, Parcels.wrap(walletEntitlementModel));
		fragment.setArguments(b);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			walletEntitlementModel = Parcels.unwrap(getArguments().getParcelable(ARG_ENTITLEMENT_DATA_ITEM));
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		FragmentEntitlementDetailsBinding binding = FragmentEntitlementDetailsBinding.inflate(inflater);
		binding.setData(walletEntitlementModel);
		return binding.getRoot();
	}

}
