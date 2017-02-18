package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.response;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceMessage;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import org.parceler.Parcel;

/**
 * Error object that gets return for a login failure
 * @author tjudkins
 * @since 10/26//16
 */
@Parcel
public class AddItemError extends GsonObject {
    public static final String TAG = AddItemError.class.getSimpleName();

    @SerializedName("errorMessage")
    String message;

    @SerializedName("errorKey")
    String key;

    public String getMessage() {
        return message;
    }

    public String getKey() {
        return key;
    }

    public boolean isInventoryError() {
        boolean isInventoryError = false;
        if(key == null) {
            return isInventoryError;
        }

        for (String invErrorKey : CommerceMessage.INVENTORY_ERROR_KEYS) {
            if (invErrorKey.equalsIgnoreCase(key)) {
                isInventoryError = true;
                break;
            }
        }
        return isInventoryError;
    }

    public boolean isInsufficientInventoryError() {
        return CommerceMessage.KEY_ERR_INV_ALLOCATION.equalsIgnoreCase(key)
                || CommerceMessage.KEY_ERR_INVENTORY_HOLD_NOT_SUCCESSFULL.equalsIgnoreCase(key);
    }

    public boolean isInventoryUnavailableError() {
        return CommerceMessage.KEY_ERR_INVENTORY_HOLD_NOT_SUCCESSFULL.equalsIgnoreCase(key);
    }
}
