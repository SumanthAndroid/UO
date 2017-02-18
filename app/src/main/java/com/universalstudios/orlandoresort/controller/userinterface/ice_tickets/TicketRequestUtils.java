package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets;

import android.support.annotation.NonNull;

import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Address;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/9/16.
 * Class: TicketRequestUtils
 * Class Description: Utils class to hold all ICE Server Requests
 */
public class TicketRequestUtils {
    public static final String TAG = TicketRequestUtils.class.getSimpleName();

    public static String createAddressNickname(@Address.AddressType String addressType, @NonNull String orderId) {
        StringBuilder sb = new StringBuilder();
        switch (addressType) {
            case Address.ADDRESS_TYPE_BILLING:
                sb.append("billing_");
                break;
            case Address.ADDRESS_TYPE_SHIPPING:
                sb.append("shipping_");
                break;
            default:
                sb.append("unknown_");
                break;
        }
        sb.append(orderId)
                .append(System.currentTimeMillis());
        return sb.toString();
    }

}
