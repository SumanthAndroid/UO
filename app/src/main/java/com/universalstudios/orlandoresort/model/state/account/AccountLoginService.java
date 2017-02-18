package com.universalstudios.orlandoresort.model.state.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkSenderService;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfileResult;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.GetGuestProfileRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.LoginUserRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetGuestProfileResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.LoginUserErrorResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.LoginUserResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.GuestIdentity.GetAuthNFailureResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.GuestIdentity.GetAuthNRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.GuestIdentity.GetAuthNResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.GuestIdentity.GetAuthNResult;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.RegistrationData.UnregisteredGuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.RegistrationData.UnregisteredGuestResult;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.UnregisteredUser.GetUnregisteredGuestRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.UnregisteredUser.GetUnregisteredGuestResponse;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.commerce.CommerceStateManager;

/**
 * This service manages the sequence of network requests required for login and logout. To use this
 * service and listen for login results, you will need to register a {@link LoginBroadcastReceiver}
 * with a {@link LocalBroadcastManager} in your activity or fragment before starting the service.
 *
 * Here's an example of setting up and registering a broadcast receiver.
 *
 * <pre>
 * {@code
 *   private LoginBroadcastReceiver mLoginBroadcastReceiver;
 *
 *      private LoginBroadcastReceiver.LoginResultCallback mLoginResultCallback = new LoginBroadcastReceiver.LoginResultCallback() {
 *         @literal @Override
 *          public void onLoginResult(@LoginBroadcastReceiver.LoginResult int result) {
 *              hideLoading();
 *              switch (result) {
 *              case LoginBroadcastReceiver.SUCCESS_REGISTERED:
 *                  // Handle success for registered login
 *                  break;
 *              case LoginBroadcastReceiver.SUCCESS_UNREGISTERED:
 *                  // Handle success for unregistered login (guest)
 *                  break;
 *              case LoginBroadcastReceiver.SUCCESS_LOGOUT:
 *                  // Handle success for logout
 *                  break;
 *              case LoginBroadcastReceiver.ERROR_ACCOUNT_LOCKED:
 *                  showErrorMessage(mTridionConfig.getEr28());
 *                  break;
 *              case LoginBroadcastReceiver.ERROR_LOGIN_FAILED:
 *                  showErrorMessage(mTridionConfig.getEr25());
 *                  break;
 *              case LoginBroadcastReceiver.ERROR_UNKNOWN:
 *                  showErrorMessage(mTridionConfig.getEr24());
 *                  break;
 *              default:
 *              }
 *          }
 *      };
 *
 *
 *     @literal @Override
 *      public void onResume() {
 *          super.onResume();
 *          mLoginBroadcastReceiver = new LoginBroadcastReceiver(mLoginResultCallback);
 *          LocalBroadcastManager.getInstance(getContext()).registerReceiver(
 *              mLoginBroadcastReceiver, new LoginResultIntentFilter()
 *          );
 *      }
 *
 *     @literal @Override
 *      public void onPause() {
 *          if (null != mLoginBroadcastReceiver) {
 *              LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mLoginBroadcastReceiver);
 *              mLoginBroadcastReceiver = null;
 *          }
 *          super.onPause();
 *      }
 * }
 * </pre>
 *
 */
public class AccountLoginService extends NetworkSenderService {
    private static final String TAG = AccountLoginService.class.getSimpleName();

    private static final String ACTION_LOGIN_UNREGISTERED = "com.universalstudios.orlandoresort.controller.userinterface.network.action.LOGIN_UNREGISTERED";
    private static final String ACTION_LOGIN_REGISTERED = "com.universalstudios.orlandoresort.controller.userinterface.network.action.LOGIN_REGISTERED";
    private static final String ACTION_LOGOUT = "com.universalstudios.orlandoresort.controller.userinterface.network.action.LOGOUT";
    private static final String ACTION_GUEST_ID_ANALYTICS = "com.universalstudios.orlandoresort.controller.userinterface.network.action.GUEST_ID_ANALYTICS";

    private static final String EXTRA_USERNAME = "com.universalstudios.orlandoresort.controller.userinterface.network.extra.USERNAME";
    private static final String EXTRA_PASSWORD = "com.universalstudios.orlandoresort.controller.userinterface.network.extra.PASSWORD";
    private static final String EXTRA_GUEST_ID = "com.universalstudios.orlandoresort.controller.userinterface.network.extra.GUEST_ID";

    private static final String KEY_ARG_INTENT = "INTENT";
    private int mRetryAttempts = 0;

    /**
     * Starts this service to perform the login sequence for unregistered users. If
     * the service is already performing a task this action will be queued.
     *
     */
    public static void startActionLoginWithGuestId(Context context, String guestId) {
        Intent intent = new Intent(context, AccountLoginService.class);
        intent.setAction(ACTION_LOGIN_UNREGISTERED);
        intent.putExtra(EXTRA_GUEST_ID, guestId);
        context.startService(intent);
    }

    /**
     * Starts this service to perform the login sequence for unregistered users. If
     * the service is already performing a task this action will be queued.
     *
     */
    public static void startActionLoginWithUsernamePassword(Context context, String username, String password) {
        Intent intent = new Intent(context, AccountLoginService.class);
        intent.setAction(ACTION_LOGIN_REGISTERED);
        intent.putExtra(EXTRA_USERNAME, username);
        intent.putExtra(EXTRA_PASSWORD, password);
        context.startService(intent);
    }

    /**
     * Starts this service to perform a logout. If
     * the service is already performing a task this action will be queued.
     *
     */
    public static void startActionLogout(Context context) {
        Intent intent = new Intent(context, AccountLoginService.class);
        intent.setAction(ACTION_LOGOUT);
        context.startService(intent);
    }

    /**
     * Starts this service to send the guest ID via the /createUnregistered
     * call for the sake of analytics.
     *
     * @param context The context
     */
    public static void startActionSendGuestIdAnalytics(Context context) {
        Intent intent = new Intent(context, AccountLoginService.class);
        intent.setAction(ACTION_GUEST_ID_ANALYTICS);
        context.startService(intent);
    }

    private ServiceHandler mServiceHandler;
    private boolean mRequestInProcess;
    private Intent mIntent;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (null != bundle) {
                processIntent((Intent) bundle.getParcelable(KEY_ARG_INTENT));
            } else {
                broadcastError(LoginBroadcastReceiver.ERROR_UNKNOWN);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceHandler = new ServiceHandler(thread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_ARG_INTENT, intent);
        msg.setData(bundle);
        mServiceHandler.sendMessage(msg);

        // Do not restart the service if it is killed
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onDestroy");
        }
    }

    private void processIntent(@Nullable Intent intent) {
        // We're only handling one login/logout request at a time. The result of the initial
        // request will be broadcast to anyone listening.
        if (!mRequestInProcess && intent != null) {
            mRequestInProcess = true;
            mIntent = intent;
            final String action = intent.getAction();
            if (ACTION_LOGIN_UNREGISTERED.equals(action)) {
                final String guestId = intent.getStringExtra(EXTRA_GUEST_ID);
                handleActionLoginUnregistered(guestId);
            } else if (ACTION_LOGIN_REGISTERED.equals(action)) {
                final String username = intent.getStringExtra(EXTRA_USERNAME);
                final String password = intent.getStringExtra(EXTRA_PASSWORD);
                handleActionLoginRegistered(username, password);
            } else if (ACTION_LOGOUT.equals(action)) {
                handleActionLogout();
            } else if (ACTION_GUEST_ID_ANALYTICS.equals(action)) {
                handleActionSendGuestIdAnalytics();
            } else {
                broadcastError(LoginBroadcastReceiver.ERROR_UNKNOWN);
            }
        }
    }

    /**
     * Handle action LOGIN_UNREGISTERED in the provided background thread with the provided
     * parameters.
     * @param guestId the guestId to use for login
     */
    private void handleActionLoginUnregistered(String guestId) {
        if (TextUtils.isEmpty(guestId)) {
            requestGetUnregisteredGuest(true);
        } else {
            requestAuthN();
        }
    }

    /**
     * Handle action LOGIN_REGISTERED in the provided background thread with the provided
     * parameters.
     * @param username the username
     * @param password the password
     */
    private void handleActionLoginRegistered(String username, String password) {
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            requestLoginUser(username, password);
        } else {
            broadcastError(LoginBroadcastReceiver.ERROR_LOGIN_FAILED);
        }
    }

    /**
     * Handle action LOGOUT in the provided background thread with the provided
     * parameters.
     */
    private void handleActionLogout() {
        if (CommerceStateManager.isAppValidForCommerce()) {
            requestGetUnregisteredGuest(true);
            AccountStateManager.logout();
            requestAuthN();
        }
        // If app isn't valid for commerce, just remove the users credentials
        else {
            AccountStateManager.logout();
        }
    }

    /**
     * Handle action ACTION_GUEST_ID_ANALYTICS in the provided background thread.
     */
    private void handleActionSendGuestIdAnalytics() {
        requestGetUnregisteredGuest(false);
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        if (networkResponse instanceof GetUnregisteredGuestResponse) {
            handleGetUnregisteredGuestResponse((GetUnregisteredGuestResponse) networkResponse);
        } else if (networkResponse instanceof LoginUserResponse) {
            handleLoginUserResponse((LoginUserResponse) networkResponse);
        } else if (networkResponse instanceof GetGuestProfileResponse) {
            handleGuestProfileResponse((GetGuestProfileResponse) networkResponse);
        } else if (networkResponse instanceof GetAuthNResponse) {
            handleGetAuthNResponse((GetAuthNResponse) networkResponse);
        } else if (networkResponse instanceof GetAuthNFailureResponse) {
            handleGetAuthNErrorResponse((GetAuthNFailureResponse) networkResponse);
        } else {
            broadcastError(LoginBroadcastReceiver.ERROR_UNKNOWN);
        }
    }

    private void requestGetUnregisteredGuest(boolean getNewGuestId) {
        NetworkRequestSender sender;

        // If getting a new guest ID, reset the tokens, handle the response, and replace the tokens
        if (getNewGuestId) {
            AccountStateManager.setWcTokens("", "");
            sender = this;
        }
        // Otherwise, don't handle the response, the request is just so the services can track the user
        else {
            sender = null;
        }

        GetUnregisteredGuestRequest request = new GetUnregisteredGuestRequest.Builder(sender)
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();

    }

    private void handleGetUnregisteredGuestResponse(@NonNull GetUnregisteredGuestResponse response) {
        if (response.isHttpStatusCodeSuccess()) {
            UnregisteredGuestResult result = response.getUnregisteredGuestResult();

            if (result.getUnregisteredGuestProfile() != null) {
                UnregisteredGuestProfile unregisteredGuestProfile = result.getUnregisteredGuestProfile();

                if (unregisteredGuestProfile.getGuestId() != null) {
                    AccountStateManager.setGuestId(unregisteredGuestProfile.getGuestId());

                    if (ACTION_LOGIN_REGISTERED.equals(mIntent.getAction())) {
                        final String username = mIntent.getStringExtra(EXTRA_USERNAME);
                        final String password = mIntent.getStringExtra(EXTRA_PASSWORD);
                        requestLoginUser(username, password);
                    } else {
                        requestAuthN();
                    }

                } else {
                    broadcastError(LoginBroadcastReceiver.ERROR_UNKNOWN);
                }
            } else {
                broadcastError(LoginBroadcastReceiver.ERROR_UNKNOWN);
            }

        } else {
            broadcastError(LoginBroadcastReceiver.ERROR_UNKNOWN);
        }
    }

    private void requestLoginUser(String username, String password) {
        // Reset the tokens since we're doing a new login
        AccountStateManager.setWcTokens("","");

        LoginUserRequest request = new LoginUserRequest.Builder(this)
                .setUsername(username)
                .setPassword(password)
                .build();

        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    private void handleLoginUserResponse(@NonNull LoginUserResponse response) {
        if (response.isHttpStatusCodeSuccess()) {
            if (null != mIntent) {
                final String username = mIntent.getStringExtra(EXTRA_USERNAME);
                final String password = mIntent.getStringExtra(EXTRA_PASSWORD);
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    AccountStateManager.saveUsernamePassword(username, password);
                    requestGuestProfile();
                } else {
                    // We should not get here without the intent containing the username and password
                    // so there is a bad error
                    broadcastError(LoginBroadcastReceiver.ERROR_UNKNOWN);
                }
            } else {
                // The intent should not be null, but if it is there is a serious error
                broadcastError(LoginBroadcastReceiver.ERROR_UNKNOWN);
            }
        } else {
            LoginUserErrorResponse errorResponse = response.getNetworkErrorResponse();
            if (errorResponse != null && errorResponse.isAccountLocked()) {
                broadcastError(LoginBroadcastReceiver.ERROR_ACCOUNT_LOCKED);
            } else {
                broadcastError(LoginBroadcastReceiver.ERROR_LOGIN_FAILED);
            }
        }
    }

    private void requestGuestProfile() {
        GetGuestProfileRequest request = new GetGuestProfileRequest.Builder(this)
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    private void handleGuestProfileResponse(@NonNull GetGuestProfileResponse response) {
        if (response.isHttpStatusCodeSuccess()) {
            GuestProfileResult result = response.getResult();
            if (null != result) {
                GuestProfile guestProfile = result.getGuestProfile();
                if (null != guestProfile) {
                    AccountStateManager.setGuestId(guestProfile.getGuestId());
                    requestAuthN();
                } else {
                    broadcastError(LoginBroadcastReceiver.ERROR_LOGIN_FAILED);
                }
            } else {
                broadcastError(LoginBroadcastReceiver.ERROR_LOGIN_FAILED);
            }
        } else {
            broadcastError(LoginBroadcastReceiver.ERROR_LOGIN_FAILED);
        }
    }

    private void requestAuthN() {
        GetAuthNRequest request = new GetAuthNRequest.Builder(this)
                .setConcurrencyType(NetworkRequest.ConcurrencyType.ASYNCHRONOUS)
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();
    }

    private void handleGetAuthNResponse(@NonNull GetAuthNResponse response) {
        if (response.isHttpStatusCodeSuccess()) {
            GetAuthNResult result = response.getResult();
            if (null != result && !TextUtils.isEmpty(result.getWCToken())
                    && !TextUtils.isEmpty(result.getWCTrustedToken())) {
                AccountStateManager.setWcTokens(
                        result.getWCToken(), result.getWCTrustedToken());
                broadcastSuccess();
            } else {
                retryOrFail();
            }
        } else {
            retryOrFail();
        }
    }

    private void handleGetAuthNErrorResponse(@NonNull GetAuthNFailureResponse response) {
        retryOrFail();
    }

    private void retryOrFail() {
        // If AuthN fails, get a new guestId and try again
        if (mRetryAttempts > 0) {
            broadcastError(LoginBroadcastReceiver.ERROR_UNKNOWN);
        } else {
            mRetryAttempts++;
            requestGetUnregisteredGuest(true);
        }
    }

    private void broadcastSuccess() {
        @LoginBroadcastReceiver.LoginResult int result;
        if (AccountStateManager.isUserLoggedIn()) {
            result = LoginBroadcastReceiver.SUCCESS_REGISTERED;
        } else if (ACTION_LOGOUT.equals(mIntent.getAction())) {
            result = LoginBroadcastReceiver.SUCCESS_LOGOUT;
        } else {
            result = LoginBroadcastReceiver.SUCCESS_UNREGISTERED;
        }
        if (ACTION_LOGIN_REGISTERED.equals(mIntent.getAction())
                || ACTION_LOGOUT.equals(mIntent.getAction())) {
            AddressCache.clear();
            SessionCache.clear();
        }
        broadcastResult(result);
    }

    private void broadcastError(@LoginBroadcastReceiver.LoginResult int errorCode) {
        broadcastResult(errorCode);
    }

    private void broadcastResult(@LoginBroadcastReceiver.LoginResult int result) {
        Intent intent = LoginResultIntentFilter.newLoginResultIntent(result);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        stopSelf();
    }
}

