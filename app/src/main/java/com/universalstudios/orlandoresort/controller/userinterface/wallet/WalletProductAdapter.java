package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletProductGroupViewModel;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletProductItemViewModel;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.WalletEntitlement;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.WalletEntitlementDetails;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpecManager;
import com.universalstudios.orlandoresort.view.binding.DataBoundViewHolder;
import com.universalstudios.orlandoresort.view.binding.LayoutBinding;
import com.universalstudios.orlandoresort.view.binding.MultiTypeDataBoundAdapter;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.util.List;
import java.util.Locale;


/**
 * adapter for viewing active entitlement items in wallet, grouped by ticket type
 */
class WalletProductAdapter extends MultiTypeDataBoundAdapter {
	private static final String TAG = WalletProductAdapter.class.getSimpleName();

	private TridionConfig mTridionConfig;
	private WalletProductGroupViewModel.WalletProductGroupCallback mCallback;

	public WalletProductAdapter(TridionConfig tridionConfig, WalletProductGroupViewModel.WalletProductGroupCallback callback) {
		this(tridionConfig, callback, new LayoutBinding[]{});
	}

	public WalletProductAdapter(TridionConfig tridionConfig, WalletProductGroupViewModel.WalletProductGroupCallback callback,
								   LayoutBinding... items) {
		super((Object[]) items);
		this.mCallback = callback;
		this.mTridionConfig = tridionConfig;
	}

	@Override
	protected void bindItem(DataBoundViewHolder holder, int position, List payloads) {
		super.bindItem(holder, position, payloads);
		// this will work even if the layout does not have a tridion parameter
		holder.binding.setVariable(BR.callback, mCallback);
		holder.binding.setVariable(BR.tridion, mTridionConfig);
	}

	@Override
	public @LayoutRes
	int getItemLayoutId(int position) {
		// use layout ids as types
		Object item = getItem(position);

		if (item instanceof LayoutBinding) {
			return ((LayoutBinding) item).getLayoutId();
		}
		throw new IllegalArgumentException("unknown item type " + item);

	}

}
