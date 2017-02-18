package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;


import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.TravelPartyMember;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class TravelPartyMembers extends GsonObject {

	@SerializedName("partyMembers")
	List<TravelPartyMember> partyMembers;

	public List<TravelPartyMember> getPartyMembers() {
		return partyMembers;
	}
}
