package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.home.HomeActivity;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;

public class ShoppingCartActivity extends NetworkRefreshActivity implements ContinueShoppingListener {
	private static final String TAG = ShoppingCartActivity.class.getSimpleName();

	public static Intent newInstanceIntent(Context context) {
		Bundle bundle = new Bundle();

		Intent intent = new Intent(context, ShoppingCartActivity.class);
		intent.putExtras(bundle);
		return intent;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		setContentView(R.layout.activity_shopping_cart);

		// Default parameters
		Bundle args = getIntent().getExtras();
		if (args == null) {
		}
		// Otherwise, set incoming parameters
		else {
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			ShoppingCartFragment fragment = ShoppingCartFragment.newInstance();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.activity_shopping_cart_container, fragment)
					.commit();
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
		}

		// Set the title
		setTitle(IceTicketUtils.getTridionConfig().getPageHeaderSCTitle());
		if (null != getActionBar()) {
			getActionBar().setDisplayShowHomeEnabled(false);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStart");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onResume");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onPause");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onSaveInstanceState");
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStop");
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroy");
		}
	}

	@Override
	public void onContinueShopping(@ShoppingActivity.ShopItemType int cartDestination) {

		//clear the filter
		CommerceUiBuilder.getCurrentFilter().resetAllFilters();

		//clear the filter
		CommerceUiBuilder.getCurrentFilter().resetAllFilters();

        // Create a synthetic stack since this is a deep link into the shopping process
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this)
                .addNextIntent(HomeActivity.newInstanceIntent(this, HomeActivity.START_PAGE_TYPE_HOME))
                .addNextIntent(ShoppingActivity.newInstanceIntent(this, ShoppingActivity.SELECT_TICKETS));

        if (cartDestination != ShoppingActivity.SELECT_TICKETS) {
            stackBuilder.addNextIntent(ShoppingActivity.newInstanceIntent(this, ShoppingActivity.SELECT_EXPRESS_PASS));
            if (cartDestination != ShoppingActivity.SELECT_EXPRESS_PASS) {
                // This is a future-proofing step so we don't have to fix Extras later
                stackBuilder.addNextIntent(ShoppingActivity.newInstanceIntent(this, ShoppingActivity.SELECT_ADDONS));
            }

        }

        stackBuilder.startActivities();
        if (!isFinishing()) {
            // probably not necessary, but safe
            finish();
        }

    }
}
