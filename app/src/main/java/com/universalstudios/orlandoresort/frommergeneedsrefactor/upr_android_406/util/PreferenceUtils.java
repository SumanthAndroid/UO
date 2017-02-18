package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.facebook.crypto.Crypto;
import com.facebook.crypto.Entity;
import com.facebook.crypto.exception.CryptoInitializationException;
import com.facebook.crypto.exception.KeyChainException;
import com.facebook.crypto.keychain.KeyChain;
import com.facebook.crypto.util.SystemNativeCryptoLibrary;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.crypto.AESEncryptionUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.crypto.KeyStoreUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.crypto.SharedPrefsBackedKeyChain;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.HashMap;

public class PreferenceUtils {
	public static final String MyPREFERENCES = "MyPrefs" ;

    private static final String PREF_USER_NAME = "pref_username";
    private static final String PREF_USERS_PWORD = "pref_pword";
	private static final String PREF_USER_SAVED = "pref_saved_login";
    public static final String PREF_USERS_NAME_FIRST = "pref_name_first";
    public static final String PREF_USERS_NAME_LAST = "pref_name_last";
    public static final String PREF_USERS_NAME_SUFFIX = "pref_name_suffix";
    public static final String PREF_GUEST_ID = "pref_guest_id_ice_version";
    public static final String PREF_GUEST_PROFILE = "pref_guest_profile";
    public static final String PREF_WC_TOKEN = "pref_wc_token";
    public static final String PREF_WC_TRUSTED_TOKEN = "pref_wc_trusted_token";
	public static final String PREF_UNREGISTERED_USER_EMAIL = "pref_unregistered_user_email";

	public static final String CALENDAR_SELECTED_TICKETS_DATE_KEY = "cal_ticket_selected_date";
	public static final String CALENDAR_SELECTED_UEP_DATE_KEY = "cal_uep_selected_date";
	public static final String CALENDAR_SELECTED_ADD_ON_DATE_KEY = "cal_add_on_selected_date";

	public static final String PREF_TRIDION_CONFIG = "pref_tridion_config";

	public static String QA_SOFTLAYER_SWITCH = "QaSwitch";
    public static String UEP_ADDON = "UEPAddon";
    public static String NON_FLORIDA_TICKET = "NonFlorida";
    public static String FLORIDA_TICKET = "Florida";
	public static String EXPRESS_PASS_TICKETS = "Express_pass";
    public static String USER_ID = "user_id";
	public static String USER_DETAILS = "UserDetails";
	public static String REGISTER_DETAILS = "RegisterDetails";
	public static String CART_DETAILS = "CartDetails";
	public static String USER_PROFILE = "UpdateProfile";
	public static String UPDATE_PASSWORD = "update_password";
	public static String UNREGISTERED_DEVICE_ID = "UnRegisterDeviceId";

	public static String GUEST_IDENTITY = "GuestIdentity";
	public static String CONTRACT_ID = "ContractId";
	public static String CATEGORY_VIEW = "CategoryView";
	public static String CATEGORY_VIEW_BY_PARENT_CATEGORY = "CategoryViewByParentCategory";
	public static String PRODUCT_VIEW_BY_CATEGORY = "ProductViewByCategory";
	public static String COUNTRIES = "Countries";
	public static String STATES = "States";
	public static String EFOLIO_PIN = "EfolioPin";
	public static String ALL_POSSIBLE_PREFERENCES = "AllPossiblePreferences";
	public static String CUSTOMER_DEFAULT_PREFERENCES = "CustomerDefaultPreferences";
	public static String CUSTOMER_PREFERENCES = "CustomerPreferences";
	public static String BADGE_COUNT = "BadgeCount";
	/* Append the sku to TICKET_DETAILS to save/find it */
	public static String TICKET_DETAILS = "TicketDetails_";
	public static String VIEW_TICKETS = "ViewTickets";

	public static String KEYSTORE_ALIAS = "TiAStore";
	public static String ENCRYPTED_AES_KEY = "TiASymmetricKey";

	public static String  SKU_DESCRIPTION =  "sku_description_";

	//the filename should obfuscated as much as possible
	private static final String PREFS_AUTOMATION_IDENTITY = "ghtjs_gje";
	private static final String TAG = "SEC_SHARED_PREFS_UTILS";

	public static final String SKU_DESCRIPTION_FORMAT = "sku_description_%s";
	public static final String PAGE_CONTENT_FORMAT = "page_content_%s";

    private static HashMap<String, Object> preferences = null;

	public static boolean canDecrypt = false;

	public PreferenceUtils()
	{
		if(preferences == null) {
			try {
				preferences = getPreferencesObject();
			} catch (FileNotFoundException e) {
				preferences = new HashMap<String, Object>();
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "PREFERENCES CLASS COULD NOT BE CREATED ON FILE!!");
				}
			}
		}
	}
	
	public void saveBoolean(String strKey,boolean value)
	{
		preferences.put(strKey, Boolean.valueOf(value));
	}

	public void removePreference(String strKey)
	{
		preferences.remove(strKey);
	}

	public void saveUsername(String username) {
		saveString(PREF_USER_NAME, username);
		commitPreference();
	}

	public String getUsername() {
		return getString(PREF_USER_NAME, "");
	}

	public void savePassword(String password) {
		saveString(PREF_USERS_PWORD, password);
		commitPreference();
	}

	public String getPassword() {
		return getString(PREF_USERS_PWORD, "");
	}

	public void saveRememberMe(boolean rememberMe) {
		saveBoolean(PREF_USER_SAVED, rememberMe);
		commitPreference();
	}

	public boolean getRememberMe() {
		return getBoolean(PREF_USER_SAVED, false);
	}

	public void saveString(String strKey, String value)
	{
		/*
		On Marshmallow or better just store the symmetric key.
		Before that store the RSA key in the keystore and then encrypt and store the symmetric key
		in SharedPreferences.
		 */
		if(strKey.equals(UNREGISTERED_DEVICE_ID)) {
			SecureRandom random = new SecureRandom();
			byte[] seed = new byte[20];
			random.nextBytes(seed);
			KeyStore keyStore = null;
			try {
				keyStore = KeyStore.getInstance("AndroidKeyStore");
				keyStore.load(null);
			} catch (KeyStoreException e) {
				if(BuildConfig.DEBUG) {
					e.printStackTrace();
				}
			} catch (CertificateException e) {
				if(BuildConfig.DEBUG) {
					e.printStackTrace();
				}
			} catch (NoSuchAlgorithmException e) {
				if(BuildConfig.DEBUG) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				if(BuildConfig.DEBUG) {
					e.printStackTrace();
				}
			}
			Context context = UniversalOrlandoApplication.getAppContext();
			SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES,
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedpreferences.edit();
			if(keyStore != null) {
				KeyStoreUtils.createRSAKey(keyStore, KEYSTORE_ALIAS);
				byte[] aesKey = AESEncryptionUtils.getRawKey(seed);
				byte[] encryptedValue = AESEncryptionUtils.encrypt(aesKey, value.getBytes());
				if (encryptedValue != null) {
					String encodedString = Base64.encodeToString(encryptedValue, Base64.DEFAULT);
					String encodedAesKey = Base64.encodeToString(aesKey, Base64.DEFAULT);
					String encryptedKey = KeyStoreUtils.encryptString(keyStore, KEYSTORE_ALIAS, encodedAesKey);
					if (encryptedKey != null) {
						editor.putString(ENCRYPTED_AES_KEY, encryptedKey);
						editor.putString(strKey, encodedString);
						canDecrypt = true;
					}
				}
			}
			editor.commit();
		}
		preferences.put(strKey, value);
	}

    public void saveInt(String strKey,int value)
    {
		preferences.put(strKey, Integer.valueOf(value));
    }

	public void saveLong(String strKey,long value)
	{
		preferences.put(strKey, Long.valueOf(value));
	}

	public void removeString(String strKey)
	{
		preferences.remove(strKey);
	}

	public void commitPreference()
	{
		savePreferences(preferences);
	}
	
	public boolean getBoolean(String strKey, boolean defaultValue)
	{
		if(isValidEntry(Boolean.class, strKey, defaultValue, preferences)){
			return (Boolean)preferences.get(strKey);
		}

		return defaultValue;
	}
	public String getString(String strKey, String defaultValue) {

		if(strKey != null && strKey.equals(UNREGISTERED_DEVICE_ID)) {
			Context context = UniversalOrlandoApplication.getAppContext();
			SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
			String encryptedGuestId = sharedpreferences.getString(strKey, defaultValue);
			if(encryptedGuestId.equals(defaultValue)) {
				return defaultValue;
			}
			String encryptedAESKey = sharedpreferences.getString(ENCRYPTED_AES_KEY, "");
			KeyStore keyStore = null;
			try {
				keyStore = KeyStore.getInstance("AndroidKeyStore");
				keyStore.load(null);
			} catch (KeyStoreException e) {
				if(BuildConfig.DEBUG) {
					Log.d(TAG, e.toString());
					e.printStackTrace();
				}
			} catch (CertificateException e) {
				if(BuildConfig.DEBUG) {
					Log.d(TAG, e.toString());
					e.printStackTrace();
				}
			} catch (NoSuchAlgorithmException e) {
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
			if(keyStore != null) {
				String aesKey = KeyStoreUtils.decryptString(keyStore, KEYSTORE_ALIAS, encryptedAESKey);
				if (aesKey != null) {
					byte[] decodedAesKey = Base64.decode(aesKey, Base64.DEFAULT);
					byte[] decodedEncryptedGuestId = Base64.decode(encryptedGuestId, Base64.DEFAULT);
					byte[] decryptedKey = AESEncryptionUtils.decrypt(decodedAesKey, decodedEncryptedGuestId);
					if (decryptedKey != null) {
						String decodedGuestId = new String(decryptedKey);
						canDecrypt = true;
						return decodedGuestId;
					} else {
						return null;
					}
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
		if (isValidEntry(String.class, strKey, defaultValue, preferences)) {
			return (String) preferences.get(strKey);
		}

		return defaultValue;
	}
    public int getInt(String strKey,int defaultValue)
    {
		if(isValidEntry(Double.class, strKey, defaultValue, preferences)){
			return ((Double) preferences.get(strKey)).intValue();
		}

		return defaultValue;
    }

	public long getLong(String strKey,long defaultValue)
	{
		if(isValidEntry(Double.class, strKey, defaultValue, preferences)){
			return ((Double) preferences.get(strKey)).longValue();
		}

		return defaultValue;
	}

	/**
	 * Method used for identifying whether the value saved matches the type we need to have as that value key
	 * If it is wrong, this method will match the key with the default value
	 * @param type
	 * @param key
	 * @param defaultValue
	 * @param preferences
     * @return
     */
	private boolean isValidEntry(Class type, String key, Object defaultValue, HashMap<String, Object> preferences){

		if(!preferences.containsKey(key)) {
			return false;
		}
		Object value = preferences.get(key);
		if(value != null && value.getClass().equals(type)){
			return true;
		} else {
			preferences.put(key, defaultValue);
			return false;
		}
	}

	private boolean savePreferences(HashMap<String, Object> map){
		try {
			saveEncryptedContent(map);
			return true;
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error saving preferences!");
			return false;
		}
	}

	private void saveEncryptedContent(HashMap<String, Object> map) throws FileNotFoundException {
		String entityId = PREFS_AUTOMATION_IDENTITY;
		String plainText = new GsonBuilder().create().toJson(map);
		// this constructor creates a key chain that produces 128-bit keys
		KeyChain keyChain = new SharedPrefsBackedKeyChain();
		// this constructor creates a crypto that uses  128-bit keys
		Crypto crypto = new Crypto(keyChain, new SystemNativeCryptoLibrary());
		Entity entity = new Entity(entityId);

		// Check for whether the crypto functionality is available
		// This might fail if Android does not load libaries correctly.
		if (!crypto.isAvailable()) {
			return;
		}

		Context context = UniversalOrlandoApplication.getAppContext();
		context.deleteFile(entityId);

		OutputStream fileStream = new BufferedOutputStream(
				context.openFileOutput(entityId, Context.MODE_PRIVATE));

		// Creates an output stream which encrypts the data as
		// it is written to it and writes it out to the file.
		OutputStream outputStream = null;
		try {
			outputStream = crypto.getCipherOutputStream(
                    fileStream,
					entity);

			// Write plaintext to it.
			outputStream.write(plainText.getBytes("UTF-8"));
		} catch (IOException e) {
			if(BuildConfig.DEBUG) {
				e.printStackTrace();
			}
		} catch (CryptoInitializationException e) {
			if(BuildConfig.DEBUG) {
				e.printStackTrace();
			}
		} catch (KeyChainException e) {
			if(BuildConfig.DEBUG) {
				e.printStackTrace();
			}
		} finally {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					if(BuildConfig.DEBUG) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	private HashMap<String, Object> getPreferencesObject() throws FileNotFoundException {

		String entityId = PREFS_AUTOMATION_IDENTITY;
		// this constructor creates a key chain that produces 128-bit keys
		KeyChain keyChain = new SharedPrefsBackedKeyChain();
		// this constructor creates a crypto that uses  128-bit keys
		Crypto crypto = new Crypto(keyChain, new SystemNativeCryptoLibrary());
		Entity entity = new Entity(entityId);

		// Get the file to which ciphertext has been written.

		InputStream inputStream = null;
		HashMap<String, Object> map = null;

		// Creates an input stream which decrypts the data as
		// it is read from it.
		String gsonString = "";
		int read;
		try {
			Context context = UniversalOrlandoApplication.getAppContext();
			FileInputStream fileInput = context.openFileInput(entityId);
			inputStream = crypto.getCipherInputStream(
					fileInput,
					entity);

			// Read into a byte array.
			int count = 0;
			//ArrayList<Byte> buffer = new ArrayList<>();
			byte[] buffer = new byte[1024];

			// You must read the entire stream to completion.
			// The verification is done at the end of the stream.
			// Thus not reading till the end of the stream will cause
			// a security bug. For safety, you should not
			// use any of the data until it's been fully read or throw
			// away the data if an exception occurs.
			gsonString = "";
			StringBuffer buf = new StringBuffer(5000);
			InputStreamReader inputreader = new InputStreamReader(inputStream);
			BufferedReader bufferedreader = new BufferedReader(inputreader);
			String line;
			while (( line = bufferedreader.readLine()) != null)
			{
				buf.append(line);
			}
			gsonString = buf.toString();
			map = new GsonBuilder().create().fromJson(gsonString, new HashMap<String, Object>().getClass());
		} catch(JsonSyntaxException e) {
			if(BuildConfig.DEBUG) {
				Log.d(TAG, e.toString() + "\n" + gsonString);
			}
		} catch (IOException e) {
			if(BuildConfig.DEBUG) {
				e.printStackTrace();
			}
		} catch (CryptoInitializationException e) {
			if(BuildConfig.DEBUG) {
				e.printStackTrace();
			}
		} catch (KeyChainException e) {
			if (BuildConfig.DEBUG) {
				e.printStackTrace();
			}
		} catch(Exception e) {
			if (BuildConfig.DEBUG) {
				e.printStackTrace();
			}
		} finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					if(BuildConfig.DEBUG) {
						e.printStackTrace();
					}
				}
			}
		}

		if(map == null) {
			map = new HashMap<String, Object>();
		}

		return map;
	}
}
