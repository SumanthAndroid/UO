package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;

import com.universalstudios.orlandoresort.R;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.WalletEntitlementDetails;

public abstract class WalletAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

	private static final String ENTITLEMENT_TYPE_EXPRESS_PASS = "UEP";
	private static final String ENTITLEMENT_TYPE_PARK_TICKET = "TICKET";
	private static final String ENTITLEMENT_TYPE_DINING_PLAN = "DINING_PLAN";
	private static final String ENTITLEMENT_TYPE_PHOTO = "PHOTO";
	private static final String ENTITLEMENT_TYPE_STROLLER = "STROLLER";
	private static final String ENTITLEMENT_TYPE_CW_PASS = "CW_PASS";
	private static final String ENTITLEMENT_TYPE_CUP = "CUP";
	private static final String ENTITLEMENT_TYPE_CW_MEAL = "CW_MEAL";
	private static final String ENTITLEMENT_TYPE_VIP = "VIP";
	private static final String ENTITLEMENT_TYPE_BMG = "BMG";
	private static final String ENTITLEMENT_ANNUAL_PASS = "AnnualPass";

	/**
	 * get the icon to display in list item based on entitlement type
	 *
	 * @return resource ID of drawable to display
	 */
	@DrawableRes
	int getIconDrawableResId(WalletEntitlementDetails item) {
		switch (item.getItemType()) {
			case ENTITLEMENT_TYPE_VIP:
				return R.drawable.ic_wallet_vip;
			case ENTITLEMENT_TYPE_BMG:
				return R.drawable.ic_wallet_bmg;
			case ENTITLEMENT_TYPE_STROLLER:
				return R.drawable.ic_wallet_stroller;
			case ENTITLEMENT_TYPE_DINING_PLAN:
				return R.drawable.ic_wallet_dining;
			case ENTITLEMENT_ANNUAL_PASS:
				return R.drawable.ic_wallet_park_ticket;
			case ENTITLEMENT_TYPE_PARK_TICKET:
				return R.drawable.ic_wallet_park_ticket;
			case ENTITLEMENT_TYPE_EXPRESS_PASS:
				return R.drawable.ic_wallet_uep;
			case ENTITLEMENT_TYPE_CW_MEAL:
			case ENTITLEMENT_TYPE_CUP:
			case ENTITLEMENT_TYPE_CW_PASS:
			case ENTITLEMENT_TYPE_PHOTO:
			default:
				return R.drawable.ic_wallet_extras;
		}
	}
}
