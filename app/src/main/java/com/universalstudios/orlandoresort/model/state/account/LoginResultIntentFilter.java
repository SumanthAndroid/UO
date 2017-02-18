package com.universalstudios.orlandoresort.model.state.account;

import android.content.Intent;
import android.content.IntentFilter;

/**
 * Intent filter to catch local broadcasts for changes to the login state
 */
public class LoginResultIntentFilter extends IntentFilter {
    private static String ACTION_LOGIN_RESULT = "ACTION_LOGIN_RESULT";
    static String KEY_ARG_LOGIN_RESULT = "KEY_ARG_LOGIN_RESULT";

    public LoginResultIntentFilter() {
        super(ACTION_LOGIN_RESULT);
    }

    public static Intent newLoginResultIntent(@LoginBroadcastReceiver.LoginResult int result) {
        Intent intent = new Intent(ACTION_LOGIN_RESULT);
        intent.putExtra(KEY_ARG_LOGIN_RESULT, result);
        return intent;
    }
}
