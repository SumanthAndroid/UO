package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.PersonalizationExtrasProduct;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.PersonalizationExtrasResultSku;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory.GetPriceInventoryResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory.PriceInventoryEventAvailabilityDate;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory.PriceInventoryEventAvailabilityInvEvent;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory.PriceInventoryEventAvailabilityPricing;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpecManager;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Parcel
public class AddOnsState {
    private static final String TAG = AddOnsState.class.getSimpleName();

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "h:mm a";
    private static final String AGE_ADULT = "Adult";
    private static final String AGE_CHILD = "Child";
    private static final String AGE_ALL_AGES = "ALL";

    GetPriceInventoryResponse mInventoryResponse;
    TridionLabelSpec mProductSpec;
    PersonalizationExtrasProduct mPersonalizationExtrasProduct;

    Map<String, Map<String, PriceInventoryEventAvailabilityDate>> mPartNumberAvailabilityMap;
    Map<String, Integer> mAgeQuantityMap;
    Date mSelectedDate;
    String mSelectedTime;
    String mSelectedTier;
    String mSelectedTcmid2;
    String mSkuAttribute;
    String mSelectedSku;

    protected AddOnsState() {
    }

    @NonNull
    public Map<String, Integer> getAgeQuantityMap() {
        if (mAgeQuantityMap == null) {
            mAgeQuantityMap = new HashMap<>();
        }
        return mAgeQuantityMap;
    }

    public void setAgeQuantityMap(Map<String, Integer> mAgeQuantityMap) {
        this.mAgeQuantityMap = mAgeQuantityMap;
    }

    public void setAdultQuantity(int quantity) {
        getAgeQuantityMap().put(AGE_ADULT, quantity);
    }

    public void setAllAgesQuantity(int quantity) {
        getAgeQuantityMap().put(AGE_ALL_AGES, quantity);
    }

    public void setChildQuantity(int quantity) {
        getAgeQuantityMap().put(AGE_CHILD, quantity);
    }

    public Date getSelectedDate() {
        return mSelectedDate;
    }

    private String getSelectedDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(getSelectedDate());
    }

    public void setSelectedDate(Date mSelectedDate) {
        this.mSelectedDate = mSelectedDate;
    }

    public String getSelectedTime() {
        return mSelectedTime;
    }

    public void setSelectedTime(String mSelectedTime) {
        this.mSelectedTime = mSelectedTime;
    }

    public String getSelectedTier() {
        return mSelectedTier;
    }

    public void setSelectedTier(String mSelectedTier) {
        this.mSelectedTier = mSelectedTier;
    }

    public GetPriceInventoryResponse getInventoryResponse() {
        return mInventoryResponse;
    }

    public void setInventoryResponse(GetPriceInventoryResponse mInventoryResponse) {
        this.mInventoryResponse = mInventoryResponse;
    }

    public PersonalizationExtrasProduct getPersonalizationExtrasProduct() {
        return mPersonalizationExtrasProduct;
    }

    public void setPersonalizationExtrasProduct(PersonalizationExtrasProduct personalizationExtrasProduct) {
        this.mPersonalizationExtrasProduct = personalizationExtrasProduct;
    }

    public String getSkuAttribute() {
        return mSkuAttribute;
    }

    public void setSkuAttribute(String mSkuAttribute) {
        this.mSkuAttribute = mSkuAttribute;
    }

    public String getSelectedSku() {
        return mSelectedSku;
    }

    public void setSelectedSku(String mSelectedSku) {
        this.mSelectedSku = mSelectedSku;
    }


    public Map<String, Map<String, PriceInventoryEventAvailabilityDate>> getPartNumberAvailabilityMap() {
        if (mPartNumberAvailabilityMap == null) {
            mPartNumberAvailabilityMap = mInventoryResponse.getPartNumberInventoryMap();
        }
        return mPartNumberAvailabilityMap;
    }

    /**
     * get the inventory fot the date that has been selected
     *
     * @return
     */
    private List<PriceInventoryEventAvailabilityDate> getInventoryForDate(Date selectedDate) {
        if (selectedDate == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(GetPriceInventoryResponse.MAP_DATE_FORMAT, Locale.US);
        String key = sdf.format(selectedDate);

        return getInventoryForDate(key);
    }

    /**
     * get the inventory fot the date that has been selected
     *
     * @return
     */
    private List<PriceInventoryEventAvailabilityDate> getInventoryForDate(String selectedDate) {
        if (selectedDate == null || mInventoryResponse == null) {
            return null;
        }
        return mInventoryResponse.getInventoryForDate(selectedDate);
    }

    /**
     * get a list of unique show times available for the selected date
     *
     * @return
     */
    public List<String> getShowTimesForSelectedDate() {
        if (getSelectedDate() != null) {
            return getShowTimesForDate(getSelectedDate());
        } else {
            return null;
        }
    }

    /**
     * get a list of unique show times available for the given date
     *
     * @return
     */
    public List<String> getShowTimesForDate(Date selectedDate) {
        if (mInventoryResponse != null) {
            return mInventoryResponse.getShowTimesForDate(selectedDate);
        } else {
            return null;
        }
    }

    @NonNull
    public BigDecimal getMinPriceByAge(String age) {
        BigDecimal result = null;

        //if we have selected a date, use that to get price
        if (mInventoryResponse != null && getSelectedDate() != null) {
            //get part numbers for age
            List<String> partNumbers;
            if (mSelectedTier == null) {
                partNumbers = getAgePartNumberMap().get(age);
            } else {
                partNumbers = new ArrayList<>();
                partNumbers.addAll(getPartNumbersForTier(mSelectedTier));
            }
            if (partNumbers != null) {
                //loop through each inventory by part number key
                for (String partNumber : partNumbers) {
                    //get inventory for selected date
                    Map<String, PriceInventoryEventAvailabilityDate> map = getPartNumberAvailabilityMap().get(partNumber);
                    if (map != null) {
                        PriceInventoryEventAvailabilityDate date = map.get(getSelectedDateString());
                        if (date != null) {
                            List<PriceInventoryEventAvailabilityPricing> pricing = date.getPriceInventoryEventAvailabilityPricings();
                            if (pricing != null) {
                                for (PriceInventoryEventAvailabilityPricing price : pricing) {
                                    //compare this low to previous low
                                    if (result == null) {
                                        result = price.getAmount();
                                    } else {
                                        result = result.compareTo(price.getAmount()) == 1 ? price.getAmount() : result;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (mPersonalizationExtrasProduct != null) {
            if (TextUtils.isEmpty(mSkuAttribute) || TextUtils.isEmpty(mSelectedSku)) {
                result = mPersonalizationExtrasProduct.getMinPriceByAge(age);
            } else {
                result = mPersonalizationExtrasProduct.getMinPriceByAgeAndSku(age, mSkuAttribute, mSelectedSku);
            }
        }

        return result != null ? result : BigDecimal.ZERO;
    }

    public List<String> getUiControls() {
        if (mPersonalizationExtrasProduct != null) {
            return mPersonalizationExtrasProduct.getUiControls();
        } else {
            return null;
        }
    }

    public HashMap<String, List<String>> getAgePartNumberMap() {
        if (mPersonalizationExtrasProduct != null) {
            return mPersonalizationExtrasProduct.getQuantitySelectors();
        } else {
            return new HashMap<>();
        }
    }

    private BigDecimal getPriceForSelectedDateAndTime(String partNumber) {
        if (getPartNumberAvailabilityMap() != null) {
            Map<String, PriceInventoryEventAvailabilityDate> map = getPartNumberAvailabilityMap().get(partNumber);
            if (map != null) {
                PriceInventoryEventAvailabilityDate availabilityDate = map.get(getSelectedDateString());
                if (availabilityDate != null) {
                    return availabilityDate.getPriceForTime(getSelectedTime());
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public String getPartNumberForTier(String tierName, String age) {
        if (mPersonalizationExtrasProduct != null) {
            return mPersonalizationExtrasProduct.getPartNumberForTier(tierName, age);
        }
        return null;
    }

    public String getAdultPartNumberForTier(String tierName) {
        return getPartNumberForTier(tierName, AGE_ADULT);
    }

    public String getChildPartNumberForTier(String tierName) {
        return getPartNumberForTier(tierName, AGE_CHILD);
    }

    private boolean isAvailableForSelectedDateAndTime(List<String> partNumbers) {
        if (getPartNumberAvailabilityMap() != null) {
            Map<String, Map<String, PriceInventoryEventAvailabilityDate>> availabilityMap = getPartNumberAvailabilityMap();
            if (availabilityMap != null) {
                for (String partNumber : partNumbers) {
                    Map<String, PriceInventoryEventAvailabilityDate> map = availabilityMap.get(partNumber);
                    if (map != null) {
                        PriceInventoryEventAvailabilityDate availabilityDate = map.get(getSelectedDateString());
                        if (availabilityDate != null) {
                            List<PriceInventoryEventAvailabilityInvEvent> events = availabilityDate.getPriceInventoryEventAvailabilityInvEvents();
                            if (events != null) {
                                for (PriceInventoryEventAvailabilityInvEvent event : events) {
                                    if (getSelectedTime().contentEquals(event.getShowTime())) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param tierName
     * @param age
     * @return
     */
    public BigDecimal getPriceForTierAndAge(String tierName, String age) {
        if (getAgePartNumberMap() != null) {
            List<String> partNumbers = getAgePartNumberMap().get(age);
            if (partNumbers != null) {
                if (mPersonalizationExtrasProduct != null) {
                    for (String partNumber : partNumbers) {
                        if (mPersonalizationExtrasProduct.isPartNumberAge(partNumber, age)
                                && mPersonalizationExtrasProduct.isPartNumberTier(partNumber, tierName)) {
                            return getPriceForSelectedDateAndTime(partNumber);
                        }
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getAdultPriceForTier(String tierName) {
        return getPriceForTierAndAge(tierName, AGE_ADULT);
    }

    public BigDecimal getChildPriceForTier(String tierName) {
        return getPriceForTierAndAge(tierName, AGE_CHILD);
    }

    /**
     * @return
     */
    public List<String> getPartNumbersForSelectedTier() {
        if (mSelectedTier != null) {
            return getPartNumbersForTier(mSelectedTier);
        } else {
            return null;
        }
    }

    /**
     * @param tierName
     * @return
     */
    public List<String> getPartNumbersForTier(String tierName) {
        List<String> partNumbers = new ArrayList<>();
        String adultPartNumber = getAdultPartNumberForTier(tierName);
        partNumbers.add(adultPartNumber);
        String childPartNumber = getChildPartNumberForTier(tierName);
        partNumbers.add(childPartNumber);
        return partNumbers;
    }

    public List<String> getAvailableTierNames() {
        if (mPersonalizationExtrasProduct == null) {
            return null;
        }

        List<String> tierList = mPersonalizationExtrasProduct.getTierList();
        List<String> filteredTierList = new ArrayList<>();
        if (tierList != null) {
            for (String tierName : tierList) {
                if (isAvailableForSelectedDateAndTime(getPartNumbersForTier(tierName))) {
                    filteredTierList.add(tierName);
                }
            }
        }
        return filteredTierList;
    }

    public List<SelectTierObject> getTierList() {
        List<String> tiers = getAvailableTierNames();

        List<SelectTierObject> list = new ArrayList<>();
        for (String tierName : tiers) {
            TridionLabelSpec spec = TridionLabelSpecManager.getSpecForId(getTcmId2(getAdultPartNumberForTier(tierName)));
            SelectTierObject selectTierObject = new SelectTierObject();
            selectTierObject.setTierName(tierName);

            selectTierObject.setDisplayName(spec.getTitle());

            Map<String, Integer> aqeQuantityMap = getAgeQuantityMap();
            Integer adultCount = aqeQuantityMap.get(AddOnsState.AGE_ADULT);
            Integer childCount = aqeQuantityMap.get(AddOnsState.AGE_CHILD);

            if (adultCount != null && adultCount > 0) {
                selectTierObject.setAdultPartNumber(getAdultPartNumberForTier(tierName));
                selectTierObject.setAdultPrice(getAdultPriceForTier(tierName));
            }

            if (childCount != null && childCount > 0) {
                selectTierObject.setChildPartNumber(getChildPartNumberForTier(tierName));
                selectTierObject.setChildPrice(getChildPriceForTier(tierName));
            }

            list.add(selectTierObject);
        }
        return list;
    }

    public String getTcmId2(String partNumber) {
        if (mPersonalizationExtrasProduct != null && partNumber != null) {
            return mPersonalizationExtrasProduct.getTcmId2(partNumber);
        } else {
            return null;
        }
    }

    public String getTcmId1(String partNumber) {
        if (mPersonalizationExtrasProduct != null && partNumber != null) {
            return mPersonalizationExtrasProduct.getTcmId1(partNumber);
        } else {
            return null;
        }
    }

    /**
     * @return
     */
    public Date getSelectedDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT + " " + TIME_FORMAT);
        Date date = null;
        try {
            date = sdf.parse(getSelectedDateString() + " " + getSelectedTime());
        } catch (ParseException e) {
            Crittercism.logHandledException(e);
        }
        return date;
    }

    /**
     * @return
     */
    public String getSelectedTierDescription() {
        return getSelectedTierTridionSpec().getTitle();
    }

    public void setTcmid2(String tcmid2) {
        this.mSelectedTcmid2 = tcmid2;
    }

    public String getTcmid2() {
        return this.mSelectedTcmid2;
    }

    public List<String> getAllTcmIds() {
        List<String> tcmids;
        if (mPersonalizationExtrasProduct != null) {
            tcmids = mPersonalizationExtrasProduct.getAllTcmIds();
        } else {
            tcmids = new ArrayList<>();
        }

        return tcmids;
    }

    public TridionLabelSpec getProductTridionSpec() {
        return TridionLabelSpecManager.getSpecForId(getTcmid1());
    }

    public TridionLabelSpec getSelectedTierTridionSpec() {
        return TridionLabelSpecManager.getSpecForId(getTcmid2());
    }

    public String getTcmid1() {
        if (mPersonalizationExtrasProduct == null) {
            return null;
        } else {
            return mPersonalizationExtrasProduct.getTcmId1();
        }
    }

    public String getOffer() {
        if (mPersonalizationExtrasProduct != null &&
                mPersonalizationExtrasProduct.getPersonalizationExtrasResultOffer() != null &&
                mPersonalizationExtrasProduct.getPersonalizationExtrasResultOffer().getCode() != null) {
               return mPersonalizationExtrasProduct.getPersonalizationExtrasResultOffer().getCode();
        }
        return null;
    }


    /**
     * Gather order items to be added to cart
     */
    @NonNull
    public List<OrderItem> getOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();
        if (mPersonalizationExtrasProduct != null) {
            Map<String, Integer> ageQuantityMap = getAgeQuantityMap();
            if (!TextUtils.isEmpty(mSkuAttribute) && !TextUtils.isEmpty(mSelectedSku)) {
                for(String age : ageQuantityMap.keySet()) {
                    Integer quantity = ageQuantityMap.get(age);
                    if (age != null && quantity != null && quantity > 0) {
                        List<PersonalizationExtrasResultSku> personalizationExtrasResultSkus =
                                mPersonalizationExtrasProduct.getPersonalizationExtrasResultSkus();
                        if (personalizationExtrasResultSkus != null) {
                            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                                if (personalizationExtrasResultSku != null
                                        && (personalizationExtrasResultSku.isAge(age) || personalizationExtrasResultSku.isAllAges())
                                        && personalizationExtrasResultSku.hasAttributeValue(mSkuAttribute, mSelectedSku)) {
                                    OrderItem orderItem = new OrderItem();
                                    orderItem.setPartNumber(personalizationExtrasResultSku.getPartNumber());
                                    orderItem.setQuantity(quantity);
                                    if (mInventoryResponse != null) {
                                        PriceInventoryEventAvailabilityInvEvent event =
                                                mInventoryResponse.getEventForDateTimeAndPartNumber(getSelectedDateTime(),
                                                        personalizationExtrasResultSku.getPartNumber());
                                        if (event != null) {
                                            // Set attributes
                                            List<CommerceAttribute> attributes = new ArrayList<>();
                                            attributes.add(CommerceAttribute.createDateAttribute(getSelectedDateTime()));
                                            attributes.add(CommerceAttribute.createInvEventIdAttribute(event.getEventId()));
                                            attributes.add(CommerceAttribute.createInvResourceIdAttribute(event.getResourceId()));
                                            orderItem.setAttributes(attributes);
                                        }
                                    }
                                    orderItems.add(orderItem);
                                }
                            }
                        }
                    }
                }
            } else if (mInventoryResponse != null) { // BMG // Dining Experience
                for(String age : ageQuantityMap.keySet()) {
                    Integer quantity = ageQuantityMap.get(age);
                    if (age != null && quantity != null && quantity > 0) {
                        List<PersonalizationExtrasResultSku> personalizationExtrasResultSkus =
                                mPersonalizationExtrasProduct.getPersonalizationExtrasResultSkus();
                        if (personalizationExtrasResultSkus != null) {
                            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                                if (personalizationExtrasResultSku != null
                                        && (personalizationExtrasResultSku.isAge(age) || personalizationExtrasResultSku.isAllAges())) {
                                    if (TextUtils.isEmpty(mSelectedTier) || personalizationExtrasResultSku.isTier(mSelectedTier)) {
                                        PriceInventoryEventAvailabilityInvEvent event =
                                                mInventoryResponse.getEventForDateTimeAndPartNumber(getSelectedDateTime(),
                                                        personalizationExtrasResultSku.getPartNumber());
                                        if (event != null) {
                                            OrderItem orderItem = new OrderItem();
                                            // Use combo part number as top-level partNumber
                                            if (personalizationExtrasResultSku.isComboSku()) {
                                                orderItem.setPartNumber(personalizationExtrasResultSku.getComboPartNumber());
                                            } else {
                                                orderItem.setPartNumber(personalizationExtrasResultSku.getPartNumber());
                                            }
                                            orderItem.setQuantity(quantity);
                                            // Set attributes
                                            List<CommerceAttribute> attributes = new ArrayList<>();
                                            attributes.add(CommerceAttribute.createDateAttribute(getSelectedDateTime(), event.getPartNumber()));
                                            attributes.add(CommerceAttribute.createInvEventIdAttribute(event.getEventId()));
                                            attributes.add(CommerceAttribute.createInvResourceIdAttribute(event.getResourceId()));
                                            orderItem.setAttributes(attributes);
                                            orderItems.add(orderItem);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return orderItems;
    }
}