package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletEntitlementModel;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.WalletEntitlement;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.WalletEntitlementAttributes;

import org.parceler.Parcels;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class WalletProductActivity extends AppCompatActivity implements WalletProductFragment.MoreInfoClickListener {
	private static final String TAG = WalletProductActivity.class.getSimpleName();

	private static final String KEY_ARGS_WALLET_ENTITLEMENT_MODELS = "KEY_ARGS_WALLET_ENTITLEMENT_MODELS";

	private ViewGroup mFragmentContainer;


	public static Intent newInstance(Context context, List<WalletEntitlementModel> walletEntitlementModels) {
		Parcelable wrapped = Parcels.wrap(walletEntitlementModels);
		Bundle bundle = new Bundle();
		bundle.putParcelable(KEY_ARGS_WALLET_ENTITLEMENT_MODELS, wrapped);

		Intent intent = new Intent(context, WalletProductActivity.class);
		intent.putExtras(bundle);
		return intent;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();
		setContentView(R.layout.activity_wallet_product);
		setTitle(tridionConfig.getMyWalletDetailsLabel());
		ActionBar actionBar = getSupportActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(true);
		}

		mFragmentContainer = (ViewGroup) findViewById(R.id.activity_wallet_product_container);
		// Load the wallet product fragment
		if (mFragmentContainer != null) {
			Bundle bundle = getIntent().getExtras();

			if (bundle != null) {
				List<WalletEntitlementModel> walletEntitlementModels = Parcels.unwrap(bundle.getParcelable(KEY_ARGS_WALLET_ENTITLEMENT_MODELS));
				WalletProductFragment fragment = WalletProductFragment.newInstance(walletEntitlementModels);
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mFragmentContainer.getId(), fragment, fragment.getClass().getName());

				fragmentTransaction.commit();
			} else {
				Log.d(TAG, "onCreate: null bundle");
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onOptionsItemSelected");
		}

		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return (super.onOptionsItemSelected(item));
		}
	}

	@Override
	public void onMoreInfoClicked(WalletEntitlementModel walletEntitlementModel) {
		getSupportFragmentManager().beginTransaction()
				.setCustomAnimations(R.anim.grow_from_middle,
						R.anim.shrink_to_middle,
						R.anim.grow_from_middle,
						R.anim.shrink_to_middle)
				.replace(mFragmentContainer.getId(), WalletProductDetailsFragment.newInstance(walletEntitlementModel))
				// Add this transaction to the back stack, allowing users to press
				// Back to get to the front of the card.
				.addToBackStack(null)
				.commit();
	}

}
