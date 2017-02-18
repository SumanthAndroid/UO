package com.universalstudios.orlandoresort.model.state.account;

import android.content.Context;
import android.text.TextUtils;

import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Address;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.PreferenceUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Manages the login/logout state of the user. In the future, it could also manage any user
 * data such as profile that can be persisted locally.
 *
 * @author tjudkins
 * @since 11/8/16
 */
public class AccountStateManager {

    // TODO make this an object for state management that can be persisted
    private static boolean mIsUserLoggedIn;

    /******************************************************************************************
     * Public methods that other classes can use to retrieve data/state from Account Manager.
     ******************************************************************************************/

    public static synchronized void initialize() {
        saveStoredLogInState();
    }

    /**
     * Get if the user is logged in
     * @return true if logged in
     */
    public static boolean isUserLoggedIn() {
        return mIsUserLoggedIn;
    }

    /**
     * Save the username and password. This should only be called directly by {@link AccountLoginService}
     * and from screens that change the username or password ({@link com.universalstudios.orlandoresort.controller.userinterface.account.AccountUpdateContactInfoFragment},
     * {@link com.universalstudios.orlandoresort.controller.userinterface.account.AccountUpdatePasswordFragment}, and {@link com.universalstudios.orlandoresort.controller.userinterface.account.ResetPasswordActivity}).
     *
     * All others should use {@link AccountLoginService#startActionLoginWithUsernamePassword(Context, String, String)}.
     * @param username the username
     * @param password the password
     */
    public static synchronized void saveUsernamePassword(String username, String password) {
        setStoredUsername(username);
        setStoredPassword(password);
        saveStoredLogInState();
    }

    /**
     * Log the user out. This clears the username (if not remember me) and password, and sets
     * the logged in state.
     * This is intentionally package protected. Use {@link AccountLoginService#startActionLogout(Context)} to logout.
     */
    static synchronized void logout() {
        if (!getRememberMe()) {
            setStoredUsername("");
        }
        setStoredPassword("");
        setStoredGuestProfile(new GuestProfile());
        saveStoredLogInState();
    }

    /**
     * Save the "Remember Me" flag
     * @param rememberMe true if need to remember username
     */
    public static void setRememberMe(boolean rememberMe) {
        setStoredRememberMe(rememberMe);
    }

    /**
     * Get the username if the user is logged in or "Remember Me" was checked
     * @return the username
     */
    public static String getUsername() {
        String un;
        if (isUserLoggedIn() || getStoredRememberMe()) {
            un = getStoredUsername();
        } else {
            un = "";
        }
        return un;
    }

    /**
     * Get the password if the user is logged in
     * @return the password
     */
    public static String getPassword() {
        String pw;
        if (isUserLoggedIn()) {
            pw = getStoredPassword();
        } else {
            pw = "";
        }
        return pw;
    }

    /**
     * Get the "Remember Me" state
     * @return the remember me state
     */
    public static boolean getRememberMe() {
        return getStoredRememberMe();
    }

    /**
     * Set the authentication tokens used for account service calls
     * This is intentionally package protected. Use {@link AccountLoginService} to login.
     * @param wcToken the WCToken
     * @param wcTrustedToken the WCTrustedToken
     */
    static synchronized void setWcTokens(String wcToken, String wcTrustedToken) {
        setStoredWcToken(wcToken);
        setStoredWcTrustedToken(wcTrustedToken);
    }

    /**
     * Get the WCToken
     * @return WCToken
     */
    public static String getWcToken() {
        return getStoredWcToken();
    }

    /**
     * Get the WCTrustedToken
     * @return WCTrustedToken
     */
    public static String getWcTrustedToken() {
        return getStoredWcTrustedToken();
    }

    public static void setGuest(GuestProfile profile) {
        synchronized (profile) {
            setStoredGuestProfile(profile);
        }
    }

    public static synchronized void setGuestId(String guestId) {
        setStoredGuestId(guestId);
    }

    public static String getGuestId() {
        return getStoredGuestId();
    }

    /**
     * Retrieves a created basic authentication string, using the guest's stored
     * username and password in preferences.  Will return null if the username or password
     * is null or empty at this time.
     *
     * @return The basic authentication string
     */
    public static String getBasicAuthString() {
        return NetworkUtils.createBasicAuthString(getUsername(), getPassword());
    }

    /******************************************************************************************
     * Private methods used by Account Manager for saving/getting state. These should not be
     * used by anyone except Account Manager.
     ******************************************************************************************/

    /**
     * Check and save the login state of the user
     */
    private static synchronized void saveStoredLogInState() {
        String user = getStoredUsername();
        String pass = getStoredPassword();

        // TODO Base this on a check against the services rather than just un/pw saved
        AccountStateManager.mIsUserLoggedIn = !TextUtils.isEmpty(user) && !TextUtils.isEmpty(pass);
    }

    /**
     * Sets the user's username securely into preferences
     *
     * @param username User's username/email
     */
    private static synchronized void setStoredUsername(String username) {
        PreferenceUtils prefs = new PreferenceUtils();
        prefs.saveUsername(username);
    }

    /**
     * Gets the securely stored username for the user
     *
     * @return User's username
     */
    private static String getStoredUsername() {
        PreferenceUtils prefs = new PreferenceUtils();
        return prefs.getUsername();
    }

    /**
     * Sets the user's password securely into the preferences
     *
     * @param password password to set
     */
    private static synchronized void setStoredPassword(String password) {
        PreferenceUtils prefs = new PreferenceUtils();
        prefs.savePassword(password);
    }

    /**
     * Gets the securely stored password for the user
     *
     * @return User's password
     */
    private static String getStoredPassword() {
        PreferenceUtils prefs = new PreferenceUtils();
        return prefs.getPassword();
    }

    /**
     * Set if "Remember Me" is checked
     *
     * @param rememberMe state to set
     */
    private static synchronized void setStoredRememberMe(boolean rememberMe) {
        PreferenceUtils prefs = new PreferenceUtils();
        prefs.saveRememberMe(rememberMe);
    }

    /**
     * Gets the securely stored "remember me" setting
     */
    private static boolean getStoredRememberMe() {
        PreferenceUtils prefs = new PreferenceUtils();
        return prefs.getRememberMe();
    }

    private static synchronized void setStoredWcToken(String wcToken) {
        PreferenceUtils prefs = new PreferenceUtils();
        prefs.saveString(PreferenceUtils.PREF_WC_TOKEN, wcToken);
        prefs.commitPreference();
    }

    private static String getStoredWcToken() {
        PreferenceUtils prefs = new PreferenceUtils();
        return prefs.getString(PreferenceUtils.PREF_WC_TOKEN, "");
    }

    private static synchronized void setStoredWcTrustedToken(String wcTrustedToken) {
        PreferenceUtils prefs = new PreferenceUtils();
        prefs.saveString(PreferenceUtils.PREF_WC_TRUSTED_TOKEN, wcTrustedToken);
        prefs.commitPreference();
    }

    private static String getStoredWcTrustedToken() {
        PreferenceUtils prefs = new PreferenceUtils();
        return prefs.getString(PreferenceUtils.PREF_WC_TRUSTED_TOKEN, "");
    }

    private static synchronized void setStoredGuestId(String guestId) {
        PreferenceUtils prefs = new PreferenceUtils();
        prefs.saveString(PreferenceUtils.PREF_GUEST_ID, guestId);
        prefs.commitPreference();
    }

    private static String getStoredGuestId() {
        PreferenceUtils prefs = new PreferenceUtils();
        return prefs.getString(PreferenceUtils.PREF_GUEST_ID, "");
    }

    //FIXME we should not be allowing the manager to return nested objects that are mutable
    public static Address getPrimaryAddress() {
        if (!AccountStateManager.isUserLoggedIn()) {
            return null;
        }

        GuestProfile guestProfile = getStoredGuestProfile();
        if(guestProfile != null &&
                guestProfile.getAddresses() != null &&
                !guestProfile.getAddresses().isEmpty()) {
            return guestProfile.getAddresses().get(0);
        }

        return null;
    }

    private synchronized static GuestProfile getStoredGuestProfile() {
        if (!AccountStateManager.isUserLoggedIn()) {
            return null;
        }

        PreferenceUtils prefs = new PreferenceUtils();
        String guestProfileStr =  prefs.getString(PreferenceUtils.PREF_GUEST_PROFILE, "");
        return GsonObject.fromJson(guestProfileStr, GuestProfile.class);
    }

    private static synchronized void setStoredGuestProfile(GuestProfile guestProfile) {
        if(guestProfile == null) {
            guestProfile = new GuestProfile();
        }

        PreferenceUtils prefs = new PreferenceUtils();
        prefs.saveString(PreferenceUtils.PREF_GUEST_PROFILE, guestProfile.toString());
        prefs.commitPreference();
    }

}
