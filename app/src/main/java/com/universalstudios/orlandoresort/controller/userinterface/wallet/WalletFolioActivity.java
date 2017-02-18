package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
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
import com.universalstudios.orlandoresort.databinding.ActivityWalletFolioBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;

/**
 * @author tjudkins
 * @since 1/24/17
 */
public class WalletFolioActivity extends AppCompatActivity implements WalletFragment.WalletFragmentListener {

    private static final String TAG = WalletFolioActivity.class.getSimpleName();


    public static Intent newInstanceIntent(Context context) {
        Intent intent = new Intent(context, WalletFolioActivity.class);
        Bundle args = new Bundle();
        intent.putExtras(args);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWalletFolioBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_folio);

        TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();
        setTitle(tridionConfig.getPageHeaderWPTitle());

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowHomeEnabled(true);
        }

        binding.setTridion(tridionConfig);

        FragmentManager fm = getSupportFragmentManager();
        WalletPagerAdapter adapter = new WalletPagerAdapter(fm);
        binding.walletFragmentPager.setAdapter(adapter);
        binding.walletTabLayout.setupWithViewPager(binding.walletFragmentPager);
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
