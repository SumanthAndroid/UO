package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.crypto;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

/**
 * Created by jamesblack on 8/5/16.
 */
public class KeyStoreUtils {
    private static final String TAG = KeyStoreUtils.class.getSimpleName();
    private static Cipher getCipher() {
        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) { // below android m
                return Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL"); // error in android 6: InvalidKeyException: Need RSA private or public key
            }
            else { // android m and above
                return Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidKeyStoreBCWorkaround"); // error in android 5: NoSuchProviderException: Provider not available: AndroidKeyStoreBCWorkaround
            }
        } catch (NoSuchPaddingException e) {
            if(BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            if(BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        } catch (NoSuchProviderException e) {
            if(BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static String decryptString(KeyStore keyStore, String alias, String cipherText) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
            PrivateKey privateKey = ((KeyStore.PrivateKeyEntry) privateKeyEntry).getPrivateKey();

            Cipher output = getCipher();
            if(output == null) return null;
            output.init(Cipher.DECRYPT_MODE, privateKey);

            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte)nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for(int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            String finalText = new String(bytes, 0, bytes.length, "UTF-8");
            return finalText;
        } catch (UnrecoverableEntryException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (KeyStoreException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (InvalidKeyException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (IOException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String encryptString(KeyStore keyStore, String alias, String initialText) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

            // Encrypt the text
            Cipher input = getCipher();
            if(input == null) return null;
            input.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, input);
            cipherOutputStream.write(initialText.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte [] vals = outputStream.toByteArray();
            return Base64.encodeToString(vals, Base64.DEFAULT);
        } catch (UnrecoverableEntryException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (KeyStoreException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (InvalidKeyException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (IOException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        }
        return null;
    }

    public static KeyPair createRSAKey(KeyStore keyStore, String alias) {
        try {
            // Create new key if needed
            if (!keyStore.containsAlias(alias)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 1);
                KeyPairGeneratorSpec spec = null;
                Context context = UniversalOrlandoApplication.getAppContext();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    spec = new KeyPairGeneratorSpec.Builder(context)
                            .setAlias(alias)
                            .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
                            .setSerialNumber(BigInteger.ONE)
                            .setStartDate(start.getTime())
                            .setEndDate(end.getTime())
                            .build();
                    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                    generator.initialize(spec);

                    return generator.generateKeyPair();
                }
            }
        } catch (NoSuchAlgorithmException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (KeyStoreException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (NoSuchProviderException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (InvalidAlgorithmParameterException e) {
            if(BuildConfig.DEBUG) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        }
        return null;
    }
}
