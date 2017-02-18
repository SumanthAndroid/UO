package com.universalstudios.orlandoresort.model.state.account;

import com.universalstudios.orlandoresort.controller.userinterface.account.address.AddressInfo;

/**
 * Maintains a cache of the addresses in checkout so the user can navigate away from the payment
 * screen then have the address pre-filled based on the previously entered address. For
 * logged out users, this is the full shipping and billing address. For logged in users, this is
 * only the selected addressId for billing and shipping.
 *
 * @author tjudkins
 * @since 1/13/17
 */

public class AddressCache {

    private static AddressCache sInstance;

    private AddressInfo billingAddress;
    private AddressInfo shippingAddress;

    private static synchronized AddressCache getInstance() {
        if (null == sInstance) {
            sInstance = new AddressCache();
        }
        return sInstance;
    }

    public static void clear() {
        setBillingAddress(null);
        setShippingAddress(null);
    }

    public static AddressInfo getBillingAddress() {
        return getInstance().billingAddress;
    }

    public static void setBillingAddress(AddressInfo billingAddress) {
        getInstance().billingAddress = billingAddress;
    }

    public static AddressInfo getShippingAddress() {
        return getInstance().shippingAddress;
    }

    public static void setShippingAddress(AddressInfo shippingAddress) {
        getInstance().shippingAddress = shippingAddress;
    }
}
