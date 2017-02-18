package com.universalstudios.orlandoresort.model.state.account;

import java.util.Date;

/**
 * Maintains a cache of the primary DOB in assign names so the user does not have to re-enter the info again
 * when registering a new account. This is only used for un-registered users
 */

public class SessionCache {

    private static SessionCache sInstance;

    private Date thisIsMeDob;

    private static synchronized SessionCache getInstance() {
        if (null == sInstance) {
            sInstance = new SessionCache();
        }
        return sInstance;
    }

    public static synchronized void clear() {
        setThisIsMeDob(null);
    }

    public static synchronized Date getThisIsMeDob() {
        return getInstance().thisIsMeDob;
    }

    public static synchronized void setThisIsMeDob(Date thisIsMeDob) {
        getInstance().thisIsMeDob = thisIsMeDob;
    }
}
