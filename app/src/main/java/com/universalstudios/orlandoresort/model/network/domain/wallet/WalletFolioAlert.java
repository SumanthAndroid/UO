package com.universalstudios.orlandoresort.model.network.domain.wallet;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by Nicholas Hanna on 1/18/2017.
 */
@Parcel
public class WalletFolioAlert extends GsonObject {
    @SerializedName("text")
    Boolean text;

    @SerializedName("email")
    Boolean email;

    public Boolean getText() {
        return text;
    }

    public Boolean getEmail() {
        return email;
    }
}
