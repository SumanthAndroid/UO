package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.crypto;

import android.util.Log;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.universalstudios.orlandoresort.BuildConfig;

/**
 * Created by jamesblack on 8/5/16.
 *
 */
public class AESEncryptionUtils {
    private static final String TAG = AESEncryptionUtils.class.getSimpleName();

    public static byte[] getRawKey(byte[] seed) {
        KeyGenerator kgen = null;
        SecureRandom sr = null;
        try {
            kgen = KeyGenerator.getInstance("AES");
            sr = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    public static byte[] encrypt(byte[] raw, byte[] clear) {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
            if(cipher == null) return null;
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(clear);
            return encrypted;
        } catch (NoSuchAlgorithmException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (NoSuchPaddingException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (BadPaddingException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (IllegalBlockSizeException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (InvalidKeyException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] decrypt(byte[] raw, byte[] encrypted) {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
            if(cipher == null) return null;
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decrypted = cipher.doFinal(encrypted);
            return decrypted;
        } catch (NoSuchAlgorithmException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (NoSuchPaddingException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (BadPaddingException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (IllegalBlockSizeException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (InvalidKeyException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        }
        return null;
    }
}
