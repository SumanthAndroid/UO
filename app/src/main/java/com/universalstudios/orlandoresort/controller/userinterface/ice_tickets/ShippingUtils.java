package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets;

import android.support.annotation.NonNull;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.DialogUtils;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ShippingModeData.DeliveryOption;

import java.util.List;

/**
 * Utility methods for getting and setting shipping options
 * @author tjudkins
 * @since 10/4/16
 */

public class ShippingUtils {
    public static final String TAG = ShippingUtils.class.getSimpleName();

    public static boolean isShippingRequired(@NonNull String shippingModeId, List<DeliveryOption> deliveryOptions) {
        boolean required = false;
        for (DeliveryOption deliveryOption : deliveryOptions) {
            if (null != deliveryOption) {
                if (shippingModeId.equalsIgnoreCase(deliveryOption.getId()) && deliveryOption.isShippingAddressRequired()) {
                    required = true;
                    break;
                }
            }
        }
        return required;
    }

    public static boolean isShippingInternational(@NonNull String shippingModeId, List<DeliveryOption> deliveryOptions) {
        boolean required = false;
        for (DeliveryOption deliveryOption : deliveryOptions) {
            if (null != deliveryOption) {
                if (shippingModeId.equalsIgnoreCase(deliveryOption.getId()) && deliveryOption.isInternational()) {
                    required = true;
                    break;
                }
            }
        }
        return required;
    }
}
