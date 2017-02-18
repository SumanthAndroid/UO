package com.universalstudios.orlandoresort.model.state.account;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

public class LoginBroadcastReceiver extends BroadcastReceiver {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            SUCCESS_LOGOUT,
            SUCCESS_REGISTERED,
            SUCCESS_UNREGISTERED,
            ERROR_UNKNOWN,
            ERROR_LOGIN_FAILED,
            ERROR_ACCOUNT_LOCKED
    })
    public @interface LoginResult {}
    public static final int SUCCESS_LOGOUT = 3;
    public static final int SUCCESS_UNREGISTERED = 2;
    public static final int SUCCESS_REGISTERED = 1;
    public static final int ERROR_UNKNOWN = -1;
    public static final int ERROR_LOGIN_FAILED = -100;
    public static final int ERROR_ACCOUNT_LOCKED = -101;

    WeakReference<LoginResultCallback> mCallback;

    public LoginBroadcastReceiver(LoginResultCallback callback) {
        mCallback = new WeakReference<>(callback);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != mCallback && null != mCallback.get()) {
            @LoginResult int result = intent.getIntExtra(LoginResultIntentFilter.KEY_ARG_LOGIN_RESULT, ERROR_UNKNOWN);
            mCallback.get().onLoginResult(result);
        }
    }

    public interface LoginResultCallback {
        void onLoginResult(@LoginResult int result);
    }
}
