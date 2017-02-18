package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;

/**
 * @author tjudkins
 * @since 11/1/16
 */

class WalletPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_TABS = 2;

    private TridionConfig mTridionConfig;

    public WalletPagerAdapter(FragmentManager fm) {
        super(fm);
        mTridionConfig = IceTicketUtils.getTridionConfig();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return WalletFragment.newInstance(true);
            case 1:
                return WalletFolioPaymentFragment.newInstance();
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mTridionConfig.getPurchasesTabTitle();
            case 1:
                return mTridionConfig.getPaymentTabTitle();
            default:
                return "";

        }
    }

}
