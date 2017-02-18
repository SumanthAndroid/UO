package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.CatalogSubcategoryResult;

/**
 * @author tjudkins
 * @since 11/1/16
 */

public class AddOnsSubcategoryPagerAdapter extends FragmentStatePagerAdapter {
    List<CatalogSubcategoryResult> subcategoryResults = new ArrayList<>();

    public AddOnsSubcategoryPagerAdapter(FragmentManager fm, List<CatalogSubcategoryResult> subcategoryResults) {
        super(fm);
        if (null != subcategoryResults && subcategoryResults.size() > 0) {
            this.subcategoryResults.addAll(subcategoryResults);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return AddOnsShoppingSubcategoryFragment.newInstance(subcategoryResults.get(position));
    }


    @Override
    public int getCount() {
        return subcategoryResults.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return subcategoryResults.get(position).getName();
    }
}
