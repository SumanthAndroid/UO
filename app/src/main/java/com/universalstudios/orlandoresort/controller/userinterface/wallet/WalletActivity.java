package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.home.HomeActivity;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.CommerceUiBuilder;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;

import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.ShoppingActivity;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;

/**
 * WalletActivity wraps {@link WalletFragment} to show expired/used entitlements.
 */
public class WalletActivity extends AppCompatActivity implements WalletFragment.WalletFragmentListener {
	private static final String TAG = WalletActivity.class.getSimpleName();

	public static final String KEY_ARGS_SHOW_ACTIVE_ENTITLEMENTS = "KEY_ARGS_SHOW_ACTIVE_ENTITLEMENTS";

	boolean mShowActiveEntitlements;

	public static Intent newInstance(Context context, boolean showActiveEntitlements) {
		Bundle bundle = new Bundle();
		bundle.putBoolean(KEY_ARGS_SHOW_ACTIVE_ENTITLEMENTS, showActiveEntitlements);

		Intent intent = new Intent(context, WalletActivity.class);
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
		setContentView(R.layout.activity_wallet);
		setTitle(tridionConfig.getPageHeaderWPTitle());
		ActionBar actionBar = getSupportActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(true);
		}

		// Default parameters
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			mShowActiveEntitlements = true;
		}
		// Otherwise, set incoming parameters
		else {
			mShowActiveEntitlements = bundle.getBoolean(KEY_ARGS_SHOW_ACTIVE_ENTITLEMENTS, true);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			WalletFragment fragment = WalletFragment.newInstance(mShowActiveEntitlements);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.activity_wallet_root_container, fragment)
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

	@Override
	public void onOneWalletItem() {
		finish();
	}

	@Override
	public void onShopNowClicked() {
		CommerceUiBuilder.getCurrentFilter().resetAllFilters();
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this)
				.addNextIntent(HomeActivity.newInstanceIntent(this, HomeActivity.START_PAGE_TYPE_HOME))
				.addNextIntent(ShoppingActivity.newInstanceIntent(this, ShoppingActivity.SELECT_TICKETS));
		stackBuilder.startActivities();
	}

	@Override
	public void onViewExpiredClicked() {
		startActivity(WalletActivity.newInstance(this, false));
	}
}
