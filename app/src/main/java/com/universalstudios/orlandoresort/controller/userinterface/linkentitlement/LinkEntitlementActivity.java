package com.universalstudios.orlandoresort.controller.userinterface.linkentitlement;

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
import com.universalstudios.orlandoresort.controller.userinterface.linkentitlement.data.LinkEntitlementModel;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;


public class LinkEntitlementActivity extends AppCompatActivity implements LinkEntitlementListener {
	private static final String TAG = LinkEntitlementActivity.class.getSimpleName();

	public static Intent newInstanceIntent(Context context) {
		Bundle bundle = new Bundle();

		Intent intent = new Intent(context, LinkEntitlementActivity.class);
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
		setContentView(R.layout.activity_link_entitlement);
		setTitle("");
		ActionBar actionBar = getSupportActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(true);
		}

		// Default parameters
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
		}
		// Otherwise, set incoming parameters
		else {
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			LinkEntitlementStep1Fragment fragment = LinkEntitlementStep1Fragment.newInstance();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.activity_link_entitlement_container, fragment)
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
	public void onCancel() {
		finish();
	}

	@Override
	public void onFirstFactorValidated(LinkEntitlementModel linkEntitlementModel) {
		LinkEntitlementStep2Fragment fragment = LinkEntitlementStep2Fragment.newInstance(linkEntitlementModel);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.activity_link_entitlement_container, fragment)
				.addToBackStack(LinkEntitlementStep2Fragment.class.getSimpleName())
				.commit();
	}

	@Override
	public void onSecondFactorValidated(LinkEntitlementModel linkEntitlementModel) {
		// TODO Assign Names
		finish();
	}

	@Override
	public void onEntitlementAssigned() {

	}
}
