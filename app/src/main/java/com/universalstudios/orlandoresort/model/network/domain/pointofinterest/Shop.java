package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author Steven Byle
 */
@Parcel
public class Shop extends PointOfInterest {

	// Shop type values
	public static final String SHOP_TYPE_APPAREL_ACCESSORIES = "ApparelAccessories";
	public static final String SHOP_TYPE_COLLECTIBLES = "Collectibles";
	public static final String SHOP_TYPE_GAMES_NOVELTIES = "GamesNovelties";
	public static final String SHOP_TYPE_FOOD_SPECIALTIES = "Food";
	public static final String SHOP_TYPE_HEALTH_BEAUTY = "HealthBeauty";

	@SerializedName("SellsExpressPass")
	Boolean sellsExpressPass;
	
	@SerializedName("HasPackagePickup")
	Boolean hasPackagePickup;

	@SerializedName("ShopTypes")
	List<String> shopTypes;

	@Override
	public long getSubTypeFlags() {
		long subTypeFlags = 0l;

		if (sellsExpressPass != null && sellsExpressPass.booleanValue()) {
			subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_SELLS_EXPRESS_PASS;
		}
		if (hasPackagePickup != null && hasPackagePickup.booleanValue()) {
			subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_HAS_PACKAGE_PICKUP;
		}
		if (shopTypes != null) {
			for (String shopType : shopTypes) {
				if (shopType.equalsIgnoreCase(SHOP_TYPE_APPAREL_ACCESSORIES)) {
					subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_TYPE_APPAREL_ACCESSORIES;
				} else if (shopType.equalsIgnoreCase(SHOP_TYPE_COLLECTIBLES)) {
					subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_TYPE_COLLECTIBLES;
				} else if (shopType.equalsIgnoreCase(SHOP_TYPE_GAMES_NOVELTIES)) {
					subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_TYPE_GAMES_NOVELTIES;
				} else if (shopType.equalsIgnoreCase(SHOP_TYPE_FOOD_SPECIALTIES)) {
					subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_TYPE_FOOD_SPECIALTIES;
				} else if (shopType.equalsIgnoreCase(SHOP_TYPE_HEALTH_BEAUTY)) {
					subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOP_TYPE_HEALTH_BEAUTY;
				}
			}
		}
		return super.getSubTypeFlags() | subTypeFlags;
	}

	/**
	 * @return the sellsExpressPass
	 */
	public Boolean getSellsExpressPass() {
		return sellsExpressPass;
	}
	
	/**
	 * @return the hasPackagePickup
	 */
	public Boolean getHasPackagePickup() {
		return hasPackagePickup;
	}

	public List<String> getShopTypes() {
		return shopTypes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((sellsExpressPass == null) ? 0 : sellsExpressPass.hashCode());
		result = prime * result + ((hasPackagePickup == null) ? 0 : hasPackagePickup.hashCode());
		result = prime * result + ((shopTypes == null) ? 0 : shopTypes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Shop other = (Shop) obj;
		if (sellsExpressPass == null) {
			if (other.sellsExpressPass != null) {
				return false;
			}
		}
		else if (!sellsExpressPass.equals(other.sellsExpressPass)) {
			return false;
		}
		if (hasPackagePickup == null) {
			if (other.hasPackagePickup != null) {
				return false;
			}
		}
		else if (!hasPackagePickup.equals(other.hasPackagePickup)) {
			return false;
		}
		if (shopTypes == null) {
			if (other.shopTypes != null) {
				return false;
			}
		} else if (!shopTypes.equals(other.shopTypes)) {
			return false;
		}
		return true;
	}

}
