package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.crypto;

/**
 * @author jamestimberlake
 * @created 5/22/16.
 */

import java.security.SecureRandom;


public class FixedSecureRandom extends SecureRandom {

    @Override
    public synchronized void nextBytes(byte[] bytes) {
        SecureRandomFix.tryApplyFixes();
        super.nextBytes(bytes);
    }
}