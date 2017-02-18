package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletEntitlementModel;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;

import org.parceler.Parcels;


public class EntitlementDetailActivity extends AppCompatActivity {
	private static final String TAG = EntitlementDetailActivity.class.getSimpleName();
	private static final String ARG_ENTITLEMENT_DATA_ITEM = "ARG_ENTITLEMENT_DATA_ITEM";

	WalletEntitlementModel walletEntitlementModel;

	public static Intent createIntent(Context context, WalletEntitlementModel entitlement) {
		Intent intent = new Intent(context, EntitlementDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable(ARG_ENTITLEMENT_DATA_ITEM, Parcels.wrap(entitlement));
		intent.putExtras(bundle);
		return intent;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.general_fragment_containing_activity);

		TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();
		setTitle(tridionConfig.getMyWalletDetailsLabel());
		ActionBar actionBar = getSupportActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(true);
			actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
		}

		// Default parameters
		Bundle args = getIntent().getExtras();
		if (args == null) {
			walletEntitlementModel = null;
		}
		// Otherwise, set incoming parameters
		else {
			walletEntitlementModel = Parcels.unwrap(args.getParcelable(ARG_ENTITLEMENT_DATA_ITEM));
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			EntitlementDetailFragment fragment = EntitlementDetailFragment.newInstance(walletEntitlementModel);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragmentContainer, fragment)
					.commit();
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
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

}
