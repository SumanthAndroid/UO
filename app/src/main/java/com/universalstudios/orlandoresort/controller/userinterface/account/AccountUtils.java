package com.universalstudios.orlandoresort.controller.userinterface.account;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.account.address.AddressInfo;
import com.universalstudios.orlandoresort.controller.userinterface.checkout.CountryStateArrays;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Address;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Contact;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfileResult;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetGuestProfileResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.offers.GetPersonalizationOffersRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.country_state.CountryStateProvinceStateManager;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.PreferenceUtils;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import java.util.Calendar;
import java.util.regex.Pattern;

import static com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.offers.GetPersonalizationOffersRequest.GEOLOCATION_TYPE_OUTER_US;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/15/16.
 * Class: AccountUtils
 * Class Description: Utils class for Account
 */
public class AccountUtils {
    public static final String TAG = AccountUtils.class.getSimpleName();

    public enum PasswordValidationError {
        LENGTH,
        REPEATING_CHARACTERS,
        SAME_AS_EMAIL,
        EMPTY,
        ENDS_WITH_SPACE,
        NONE
    }

    /**
     * Determines if a password is valid
     *
     * @param password User provided password
     * @param email    User's email
     * @return true if password is valid
     */
    public static PasswordValidationError isValidPassword(String password, String email) {
        if (TextUtils.isEmpty(password)) {
            return PasswordValidationError.EMPTY;
        }

        if (password.length() < 8) {
            return PasswordValidationError.LENGTH;
        }

        if (password.equals(email)) {
            return PasswordValidationError.SAME_AS_EMAIL;
        }

        boolean containsUnique = false;
        for (char c : password.toCharArray()) {
            if (password.indexOf(c) == password.lastIndexOf(c)) {
                containsUnique = true;
                break;
            } else {
                containsUnique = false;
            }
        }
        if (!containsUnique) {
            return PasswordValidationError.REPEATING_CHARACTERS;
        }

        if (password.substring(password.length() - 1).equals(" ")) {
            return PasswordValidationError.ENDS_WITH_SPACE;
        }

        return PasswordValidationError.NONE;
    }

    /**
     * This is a custom email address matcher that disallows + (per US18316). When this requirement
     * is removed, switch back to {@link Patterns}.EMAIL_ADDRESS that matches the international
     * standard.
     */
    public static final Pattern EMAIL_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9\\.\\_\\%\\-]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );


    /**
     * Checks to see if the email is valid per UO Standards
     *
     * @param email Email to validate
     * @return true if valid
     */
    public static boolean isValidEmail(String email) {
        if (!EMAIL_ADDRESS.matcher(email).matches() || email.matches(".*[!#`$%\\-&*\\[\\]/=?\\^\\{|}~].*")
                || email.startsWith(".") || email.endsWith(".") || email.contains("..")) {
            return false;
        } else if (email.charAt(email.indexOf("@") - 1) == '.') {
            return false;
        }
        return true;
    }

    /**
     * Determines if the given String matches Universal's name formatting rules
     *
     * @param text Text to test
     * @return true if valid
     */
    public static boolean isValidName(String text) {
        return !text.matches(".*[!#$%&*+\\[\\]/;=?\\^_\\{|}].*");
    }

    /**
     * Determines if the given String matches Universal's suffix formatting rules
     *
     * @param suffix Text to test
     * @return true if valid
     */
    public static boolean isValidSuffix(String suffix) {
        return !suffix.matches(".*[!#$%&*+\\[\\]/;=?\\^_\\{|}].*");
    }

    public static void setUserName(GetGuestProfileResponse guestProfileResponse, Context context) {
        if (guestProfileResponse != null && guestProfileResponse.getResult() != null) {
            GuestProfileResult guestProfileResult = guestProfileResponse.getResult();
            if (guestProfileResult.getGuestProfile() != null) {
                GuestProfile guestProfile = guestProfileResult.getGuestProfile();
                if (guestProfile.getContact() != null) {
                    Contact contact = guestProfile.getContact();

                    String firstName = contact.getFirstName();
                    String lastName = contact.getLastName();
                    String suffixName = contact.getNameSuffix();

                    AccountUtils.setFirstName(firstName);
                    AccountUtils.setLastName(lastName);
                    AccountUtils.setSuffixName(suffixName);
                }
            }
        }
    }

    /**
     * Creates the last line of an address. This may not be formatted for foreign addresses
     *
     * @param address Address object
     * @return Formatted third address line
     */
    public static String buildCityStateZipLine(Address address) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(address.getCity())) {
            sb.append(address.getCity());
        }
        if (!TextUtils.isEmpty(address.getState())) {
            sb.append(" ")
                    .append(address.getState());
        }
        if (!TextUtils.isEmpty(address.getZipCode())) {
            sb.append(", ").append(address.getZipCode());
        }
        return sb.toString();
    }

    /**
     * Builds the user's name with proper prefix and formatting
     *
     * @param contact Contact to format name of
     * @return Properly formatted name
     */
    public static String buildUsersName(Contact contact) {
        if (null == contact) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(contact.getNamePrefix())) {
            sb.append(contact.getNamePrefix()).append(" ");
        }
        if (!TextUtils.isEmpty(contact.getFirstName())) {
            sb.append(contact.getFirstName()).append(" ");
        }
        if (!TextUtils.isEmpty(contact.getLastName())) {
            sb.append(contact.getLastName());
        }
        if (!TextUtils.isEmpty(contact.getNameSuffix())) {
            sb.append(" ").append(contact.getNameSuffix());
        }

        return sb.toString();
    }

    /**
     * Builds the user's name with proper prefix and formatting
     *
     * @param address address containing name to format
     * @return Properly formatted name
     */
    public static String buildUsersName(Address address) {
        if (null == address) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(address.getFirstName())) {
            sb.append(address.getFirstName()).append(" ");
        }
        if (!TextUtils.isEmpty(address.getLastName())) {
            sb.append(address.getLastName());
        }

        return sb.toString();
    }

    /**
     * Determines if the dates are within range
     *
     * @param year  selected year
     * @param month selected month (Jan = 0)
     * @param day   selected day
     * @return true if within the approved range
     */
    public static boolean isWithinDateRange(int year, int month, int day) {
        Calendar today = Calendar.getInstance();

        Calendar maxCal = Calendar.getInstance();
        maxCal.set(Calendar.YEAR, today.get(Calendar.YEAR) - 130);

        Calendar minCal = Calendar.getInstance();
        minCal.set(Calendar.YEAR, today.get(Calendar.YEAR) - 13);
        minCal.add(Calendar.DAY_OF_MONTH, 1);

        Calendar selected = Calendar.getInstance();
        selected.set(Calendar.YEAR, year);
        selected.set(Calendar.MONTH, month);
        selected.set(Calendar.DAY_OF_MONTH, day);

        return selected.after(maxCal) && selected.before(minCal);
    }

    /**
     * Gets a phone number String properly formatted
     *
     * @param phone String phone number
     * @return properly formatted phone number String
     */
    public static String getFormattedPhoneNumber(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return "";
        }

        if (phone.length() >= 11) {
            return phone;
        }
        StringBuilder sb = new StringBuilder();
        if (phone.length() >= 3) {
            sb.append("(")
                    .append(phone.substring(0, 3))
                    .append(")");
            if (phone.length() > 3) {
                sb.append(" ");
            }
            if (phone.length() > 6) {
                sb.append(phone.substring(3, 6))
                        .append("-");
                sb.append(phone.substring(6, phone.length()));
            } else {
                sb.append(phone.substring(3, phone.length()));
            }
        } else {
            sb.append(phone);
        }

        return sb.toString();
    }

    public static String getUnregisteredUserEmail() {
        PreferenceUtils prefs = new PreferenceUtils();
        return prefs.getString(PreferenceUtils.PREF_UNREGISTERED_USER_EMAIL, "");
    }

    public static void setUnregisteredUserEmail(String unregisteredUserEmail) {
        PreferenceUtils prefs = new PreferenceUtils();
        prefs.saveString(PreferenceUtils.PREF_UNREGISTERED_USER_EMAIL, unregisteredUserEmail);
        prefs.commitPreference();
    }

    /**
     * Gets the securely stored first name for the user
     *
     * @return User's password
     */
    public static String getFirstName() {
        PreferenceUtils prefs = new PreferenceUtils();
        return prefs.getString(PreferenceUtils.PREF_USERS_NAME_FIRST, "");
    }

    /**
     * Sets the user's first name securely into the preferences
     *
     * @param first   password to set
     */
    public static void setFirstName(String first) {
        PreferenceUtils prefs = new PreferenceUtils();
        prefs.saveString(PreferenceUtils.PREF_USERS_NAME_FIRST, first == null ? "" : first);
        prefs.commitPreference();
    }

    /**
     * Gets the securely stored lsat name for the user
     *
     * @return User's password
     */
    public static String getLastName() {
        PreferenceUtils prefs = new PreferenceUtils();
        return prefs.getString(PreferenceUtils.PREF_USERS_NAME_LAST, "");
    }

    /**
     * Sets the user's last name suffix securely into the preferences
     *
     * @param last    password to set
     */
    public static void setLastName(String last) {
        PreferenceUtils prefs = new PreferenceUtils();
        prefs.saveString(PreferenceUtils.PREF_USERS_NAME_LAST, last == null ? "" : last);
        prefs.commitPreference();
    }

    /**
     * Gets the securely stored name suffix for the user
     *
     * @return User's password
     */
    public static String getSuffixName() {
        PreferenceUtils prefs = new PreferenceUtils();
        return prefs.getString(PreferenceUtils.PREF_USERS_NAME_SUFFIX, "");
    }

    /**
     * Sets the user's name suffix securely into the preferences
     *
     * @param suffix  password to set
     */
    public static void setSuffixName(String suffix) {
        PreferenceUtils prefs = new PreferenceUtils();
        prefs.saveString(PreferenceUtils.PREF_USERS_NAME_SUFFIX, suffix == null ? "" : suffix);
        prefs.commitPreference();
    }

    public static @GetPersonalizationOffersRequest.GeoLocationType String getGeoLocationType() {
        return getGeoLocationType(null);
    }


    public static @GetPersonalizationOffersRequest.GeoLocationType String getGeoLocationType(Boolean ticketFilterInFl) {

        //if florida is set to true, then that takes priority
        if(ticketFilterInFl != null && ticketFilterInFl) {
            return GetPersonalizationOffersRequest.GEOLOCATION_TYPE_FLORIDA;
        } else {

            //the next priority is setting the geolocation based on the primary address
            CountryStateArrays countryArrays = new CountryStateArrays(
                    CountryStateProvinceStateManager.getCountriesInstance().getCountries(),
                    CountryStateProvinceStateManager.getStateProvincesInstance().getStateProvinces());
            Address primaryAddress = AccountStateManager.getPrimaryAddress();


            AddressInfo primaryAddressInfo = null;

            if(primaryAddress != null) {
                primaryAddressInfo = new AddressInfo(primaryAddress, countryArrays);
            }

            if(primaryAddressInfo != null && primaryAddressInfo.getCountryCode() != null) {
                if(primaryAddressInfo.getDomesticAddress()) {
                    if(primaryAddressInfo.getFloridaState()) {
                        return GetPersonalizationOffersRequest.GEOLOCATION_TYPE_FLORIDA;
                    } else {
                        return GetPersonalizationOffersRequest.GEOLOCATION_TYPE_OUTER_US;
                    }
                } else {
                    return GetPersonalizationOffersRequest.GEOLOCATION_TYPE_INTERNATIONAL;
                }
            } else {

                //if we have nothing else available, then we try to figure out if the locale is within the us
                String locale = UniversalOrlandoApplication.getAppContext().getResources().getConfiguration().locale.getCountry();
                if(locale != null){
                    //FIXME add this to a class that makes sense to put this locale value
                    if(locale.equalsIgnoreCase("US")) {
                        return GetPersonalizationOffersRequest.GEOLOCATION_TYPE_OUTER_US;
                    } else {
                        return GetPersonalizationOffersRequest.GEOLOCATION_TYPE_INTERNATIONAL;
                    }
                }
            }
        }

        //default is OUS
        return GEOLOCATION_TYPE_OUTER_US;
    }
}
