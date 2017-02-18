package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.GuestIdentity;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * @author tjudkins
 * @since 12/29/16
 */

@Parcel
public class GetAuthNResult extends GsonObject {
    @SerializedName("userId")
    String userId;

    @SerializedName("WCToken")
    String WCToken;

    @SerializedName("WCTrustedToken")
    String WCTrustedToken;

    @SerializedName("personalizationID")
    String personalizationID;

    /**
     *
     * @return
     * The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @return
     * The WCToken
     */
    public String getWCToken() {
        return WCToken;
    }

    /**
     *
     * @return
     * The WCTrustedToken
     */
    public String getWCTrustedToken() {
        return WCTrustedToken;
    }

    /**
     *
     * @return
     * The personalizationID
     */
    public String getPersonalizationID() {
        return personalizationID;
    }
}
