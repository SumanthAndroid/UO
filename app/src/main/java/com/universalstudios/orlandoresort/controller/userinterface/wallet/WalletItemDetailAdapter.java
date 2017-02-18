package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.support.annotation.LayoutRes;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletProductItemViewModel;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.view.binding.DataBoundViewHolder;
import com.universalstudios.orlandoresort.view.binding.LayoutBinding;
import com.universalstudios.orlandoresort.view.binding.MultiTypeDataBoundAdapter;

import java.util.List;

/**
 * Created by kbojarski on 9/30/16.
 * Heavily modified by tjudkins on 12/27/16.
 */

public class WalletItemDetailAdapter extends MultiTypeDataBoundAdapter {
	private static final String TAG = WalletItemDetailAdapter.class.getSimpleName();

	private TridionConfig mTridionConfig;
	private WalletProductItemViewModel.WalletProductItemCallback mCallback;

	public WalletItemDetailAdapter(TridionConfig tridionConfig, WalletProductItemViewModel.WalletProductItemCallback callback) {
		this(tridionConfig, callback, new LayoutBinding[]{});
	}

	public WalletItemDetailAdapter(TridionConfig tridionConfig, WalletProductItemViewModel.WalletProductItemCallback callback,
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
