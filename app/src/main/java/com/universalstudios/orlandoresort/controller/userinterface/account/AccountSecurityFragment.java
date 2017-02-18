package com.universalstudios.orlandoresort.controller.userinterface.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.universalstudios.orlandoresort.controller.userinterface.home.HomeActivity;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/19/16.
 * Class: AccountSecurityFragment
 * Class Description: Fragment that designates its subclasses as Fragments that should not be accessible
 * unless the user is logged into their account.
 */
public class AccountSecurityFragment extends Fragment {
    public static final String TAG = "AccountSecurityFragment";

    public void lockOut() {
        Intent i = new Intent(getActivity(), HomeActivity.class);
        Bundle homeActivityBundle = HomeActivity.newInstanceBundle(HomeActivity.START_PAGE_TYPE_HOME);
        i.putExtras(homeActivityBundle);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
