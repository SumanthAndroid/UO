package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.response.AddItemErrorResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.AddOnTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ExpressPassTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.OrderItemGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ParkTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupingResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfigWrapper;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.PersonalizationExtrasProduct;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.PersonalizationExtrasResultAttribute;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.PersonalizationExtrasResultSku;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.PersonalizationExtrasResultSkuPrice;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCard;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCardItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceGroup;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceInventoryItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceMessage;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceOfferPricingAndInventory;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetCardsRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.network_config.TridionConfigStateManager;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.PreferenceUtils;
import com.universalstudios.orlandoresort.view.custom_calendar.TicketType;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: IceTicketUtils
 * Class Description: General Utils class for ICE Tickets
 */
public class IceTicketUtils {
    public static final String TAG = "IceTicketUtils";

    public static final String CAL_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * TODO this method needs to have the extras identifier added
     * Static method for getting the ticket type for the service identifier for filters
     * @param identifier
     * @return
     */
    public static TicketType getTicketTypeForIdentifier(String identifier) {
        if(identifier == null) {
            return TicketType.TYPE_TICKETS;
        } else if (identifier.toUpperCase().contains(GetCardsRequest.UEP_IDENTIFIER)) {
            return TicketType.TYPE_EXPRESS;
        } else {
            return TicketType.TYPE_TICKETS;
        }
    }

    public static boolean isInventoryAvailableForCardItem(CommerceCardItem item) {
        if( null == item.getPricingAndInventory() || null == item.getPricingAndInventory().getOfferPricingAndInventory()
                || item.getPricingAndInventory().getOfferPricingAndInventory().isEmpty()){
            return false;
        }

        CommerceOfferPricingAndInventory[] pricingInventoryItems = item.getPricingAndInventory().getOfferPricingAndInventory().values().toArray(new CommerceOfferPricingAndInventory[item.getPricingAndInventory().getOfferPricingAndInventory().values().size()]);

        //if the inventory is not controlled, then inventory is available by default
        if(!pricingInventoryItems[0].isInventoryControlled()) {
            return true;
        }

        CommerceInventoryItem inventoryItem = null;
        if(null != pricingInventoryItems[0].getInventoyItems() && !pricingInventoryItems[0].getInventoyItems().isEmpty()) {
            inventoryItem = pricingInventoryItems[0].getInventoyItems().get(0);
        }

        if(null == inventoryItem){
            return false;
        }

        boolean hasEventId = null != inventoryItem.getEventId() && inventoryItem.getEventId() > 0;
        boolean hasResourceId = null != inventoryItem.getResourceId() && inventoryItem.getResourceId() > 0;
        boolean hasQuantity = null != inventoryItem.getQuantitiy() && inventoryItem.getQuantitiy() > 0;

        return hasEventId && hasResourceId && hasQuantity;
    }

    public static double getOnlineOfferText(List<CommerceCard> cards) {
        if (null == cards || cards.isEmpty() || null == cards.get(0).getGroups() || cards.get(0).getGroups().isEmpty()) {
            return 0;
        }

        if (null == cards.get(0).getGroups().get(0).getAdultPricingAndInventory()) {
            return 0;
        }

        BigDecimal offerPrice = cards.get(0).getGroups().get(0).getAdultPricingAndInventory().getOfferPrice();
        BigDecimal fullPrice = cards.get(0).getGroups().get(0).getAdultPricingAndInventory().getListPrice();
        int offer = fullPrice.subtract(offerPrice).intValue();
        if (offer > 5) {
            return offer;
        }
        return 0;
    }

    public static List<String> getIdsForTridion(List<CommerceCard> cards) {
        List<String> ids = new ArrayList<>();

        for (CommerceCard card : cards) {
            if (null != card.getGroups() && !card.getGroups().isEmpty()) {
                for (CommerceGroup group : card.getGroups()) {
                    if (!group.getCardItems().isEmpty()) {
                        for (CommerceCardItem item : group.getCardItems()) {
                            String id = item.getIdForTridion();
                            if (!TextUtils.isEmpty(id)) {
                                ids.add(id);
                            }
                            String detailId = item.getDetailIdForTridion();
                            if (!TextUtils.isEmpty(detailId)) {
                                ids.add(detailId);
                            }
                        }
                    }
                }
            }
        }
        return ids;
    }

    public static int getNumberOfTicketsInCart(TicketGroupingResponse response) {
        int numTickets = 0;

        if (null == response || null == response.getOrder() || null == response.getOrder().getOrderItemGroups()) {
            return 0;
        }

        for (ParkTicketGroups groups : response.getOrder().getOrderItemGroups().getParkTicketGroups()) {
            if (null != groups) {
                if (null != groups.getAdultTickets()) {
                    numTickets += groups.getAdultTickets().getQuantity();
                }
                if (null != groups.getChildTickets()) {
                    numTickets += groups.getChildTickets().getQuantity();
                }
            }
        }

        for (ExpressPassTicketGroups groups : response.getOrder().getOrderItemGroups().getExpressPassGroups()) {
            numTickets += groups.getQuantity();
        }

        for (AddOnTicketGroups groups : response.getOrder().getOrderItemGroups().getAddOnTicketGroups()) {
            if (null != groups) {
                if (null != groups.getAdultAddOns()) {
                    numTickets += groups.getAdultAddOns().getQuantity();
                }
                if (null != groups.getChildAddOns()) {
                    numTickets += groups.getChildAddOns().getQuantity();
                }
                if (null != groups.getAllAddOns()) {
                    numTickets += groups.getAllAddOns().getQuantity();
                }
            }
        }

        for (ParkTicketGroups groups : response.getOrder().getOrderItemGroups().getAnnualPassParkTicketGroups()) {
            if (null != groups) {
                if (null != groups.getAdultTickets()) {
                    numTickets += groups.getAdultTickets().getQuantity();
                }
                if (null != groups.getChildTickets()) {
                    numTickets += groups.getChildTickets().getQuantity();
                }
            }
        }

        return numTickets;
    }

    public static boolean isFloridaBillingAddressRequired(OrderItemGroups orderItemGroups) {
        boolean requiresFloridaBillingAddress = false;
        if (null != orderItemGroups && null != orderItemGroups.getParkTicketGroups()) {
            for (ParkTicketGroups parkTicketGroups : orderItemGroups.getParkTicketGroups()) {
                if (isFloridaResidentTicket(parkTicketGroups)) {
                    requiresFloridaBillingAddress = true;
                    break;
                }
            }
        }
        return requiresFloridaBillingAddress;
    }

    public static boolean isFloridaResidentTicket(ParkTicketGroups parkTicketGroups) {
        boolean isFloridaResidentTicket = false;
        if (null != parkTicketGroups.getAdultTickets() && null != parkTicketGroups.getAdultTickets().getItem()) {
            for (CommerceAttribute attribute : parkTicketGroups.getAdultTickets().getItem().getAttributes()) {
                if (null != attribute) {
                    if (attribute.isPoo() && attribute.isFloridaPoo()) {
                        isFloridaResidentTicket = true;
                        break;
                    }
                }
            }
        }
        if (!isFloridaResidentTicket && null != parkTicketGroups.getChildTickets()
                && null != parkTicketGroups.getAdultTickets().getItem()) {
            for (CommerceAttribute attribute : parkTicketGroups.getChildTickets().getItem().getAttributes()) {
                if (null != attribute) {
                    if (attribute.isPoo() && attribute.isFloridaPoo()) {
                        isFloridaResidentTicket = true;
                        break;
                    }
                }
            }
        }
        return isFloridaResidentTicket;
    }

    public static boolean isFloridaResidentTicket(List<CommerceAttribute> attributes) {
        boolean isFloridaResidentTicket = false;
        for (CommerceAttribute attribute : attributes) {
            if (null != attribute) {
                if (attribute.isPoo() && attribute.isFloridaPoo()) {
                    isFloridaResidentTicket = true;
                    break;
                }
            }
        }
        return isFloridaResidentTicket;
    }

    /**
     * Retrieves the {@link TridionConfig} object using config string from SharedPreferences.
     * Will return a new {@link TridionConfig} if said string comes back empty, or the generated
     * {@link TridionConfig} from JSON is null.
     *
     * @return The {@link TridionConfig} object
     */
    public static TridionConfig getTridionConfig() {
        TridionConfigWrapper tridionConfigWrapper = TridionConfigStateManager.getInstance();
        if (tridionConfigWrapper == null || tridionConfigWrapper.getTridionConfig() == null) {
            return new TridionConfig();
        }
        return tridionConfigWrapper.getTridionConfig();
    }

    public static boolean hasInventoryError(List<OrderItem> orderItems) {
        boolean isInventoryError = false;
        ERRORS: for (OrderItem orderItem : orderItems) {
            for (CommerceMessage message : orderItem.getMessages()) {
                for (String errorKey : CommerceMessage.INVENTORY_ERROR_KEYS) {
                    if (errorKey.equals(message.getKey())) {
                        isInventoryError = true;
                        break ERRORS;
                    }
                }
            }
        }
        return isInventoryError;
    }

    public static boolean hasInventoryError(AddOnTicketGroups addOnTicketGroups) {
        List<OrderItem> orderItems = new ArrayList<>();
        if (null != addOnTicketGroups) {
            if (null != addOnTicketGroups.getAllAddOns() && null != addOnTicketGroups.getAllAddOns().getOrderItems()) {
                orderItems.addAll(addOnTicketGroups.getAllAddOns().getOrderItems());
            }
            if (null != addOnTicketGroups.getAdultAddOns() && null != addOnTicketGroups.getAdultAddOns().getOrderItems()) {
                orderItems.addAll(addOnTicketGroups.getAdultAddOns().getOrderItems());
            }
            if (null != addOnTicketGroups.getChildAddOns() && null != addOnTicketGroups.getChildAddOns().getOrderItems()) {
                orderItems.addAll(addOnTicketGroups.getChildAddOns().getOrderItems());
            }
        }

        return hasInventoryError(orderItems);
    }

    public static boolean hasInventoryError(OrderItemGroups orderItemGroups) {
        boolean isInventoryError = false;
        if (null != orderItemGroups) {
            for (ParkTicketGroups parkTicketGroups : orderItemGroups.getParkTicketGroups()) {
                if (null != parkTicketGroups) {
                    if (null != parkTicketGroups.getAdultTickets()) {
                        isInventoryError = hasInventoryError(parkTicketGroups.getAdultTickets().getOrderItems());
                        if (isInventoryError) break;
                    }
                    if (!isInventoryError && null != parkTicketGroups.getChildTickets()) {
                        isInventoryError = hasInventoryError(parkTicketGroups.getChildTickets().getOrderItems());
                        if (isInventoryError) break;
                    }
                }
            }
            if (!isInventoryError && null != orderItemGroups.getExpressPassGroups()) {
                for (ExpressPassTicketGroups epTicketGroups : orderItemGroups.getExpressPassGroups()) {
                    if (null != epTicketGroups) {
                        isInventoryError = hasInventoryError(epTicketGroups.getOrderItems());
                        if (isInventoryError) break;
                    }
                }
            }
            Map<String, List<AddOnTicketGroups>> addOnsMap = orderItemGroups.getAddOnsMap();
            if (null != addOnsMap) {
                for (String addOnKey : addOnsMap.keySet()) {
                    for (AddOnTicketGroups addOnTicketGroups : addOnsMap.get(addOnKey)) {
                        if (null != addOnTicketGroups) {
                            if (null != addOnTicketGroups.getAllAddOns()) {
                                isInventoryError = hasInventoryError(addOnTicketGroups.getAllAddOns().getOrderItems());
                                if (isInventoryError) break;
                            }
                            if (null != addOnTicketGroups.getAdultAddOns()) {
                                isInventoryError = hasInventoryError(addOnTicketGroups.getAdultAddOns().getOrderItems());
                                if (isInventoryError) break;
                            }
                            if (!isInventoryError && null != addOnTicketGroups.getChildAddOns()) {
                                isInventoryError = hasInventoryError(addOnTicketGroups.getChildAddOns().getOrderItems());
                                if (isInventoryError) break;
                            }
                        }
                    }
                }
            }
        }
        return isInventoryError;
    }

    public static String getUserErrorMessage(AddItemErrorResponse addItemErrorResponse, Context context) {
        String errorMsg = null;
        TridionConfig tridionConfig = getTridionConfig();
        if (null != addItemErrorResponse) {
            if (addItemErrorResponse.hasInventoryError()) {
                if (addItemErrorResponse.hasInsufficientInventoryError() ||
                        addItemErrorResponse.isInventoryUnavailableError()) {
                    errorMsg = tridionConfig.getEr57();
                } else {
                    errorMsg = tridionConfig.getEr50();
                }
            } else {
                errorMsg = addItemErrorResponse.getItemTypeMaxQuantityExceededError();
            }
            if (null == errorMsg) {
                // FIXME An error occurred, but don't know how to handle it
                errorMsg = tridionConfig.getEr71();
            }
        }
        return errorMsg;
    }

    /**
     * Retrieves the formerly selected date from SharedPreferences.  If there is no previously
     * stored date, today's date is used.
     *
     * @return The selected date
     */
    public static Date retrieveDefaultDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * Retrieves the formerly selected date from SharedPreferences.  If there is no previously
     * stored date, today's date is used.
     *
     * @return The selected date
     */
    public static Date retrieveSelectedCalendarDate(TicketType calendarType) {
        Date selectedDate = new Date();
        Context context = UniversalOrlandoApplication.getAppContext();
        // If the context is null, that's an immediate problem
        if (context == null) {
            return selectedDate;
        }
        PreferenceUtils preferenceUtils = new PreferenceUtils();

        String prefDateKey = "";
        switch (calendarType) {
            case TYPE_EXPRESS:
                prefDateKey = PreferenceUtils.CALENDAR_SELECTED_UEP_DATE_KEY;
                break;
            case TYPE_TICKETS:
                prefDateKey = PreferenceUtils.CALENDAR_SELECTED_TICKETS_DATE_KEY;
                break;
        }
        String storedDate = preferenceUtils.getString(prefDateKey, null);
        // Just use today's date if there is no stored date
        if (storedDate == null) {
            Calendar calendar = Calendar.getInstance();
            selectedDate = calendar.getTime();
        } else {
            try {
                SimpleDateFormat calDateFormat = new SimpleDateFormat(CAL_DATE_FORMAT, Locale.US);
                selectedDate = calDateFormat.parse(storedDate);
            }
            catch (ParseException ex) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "Parsing exception attempting to parse Date from stored date String ", ex);
                }
            }
        }
        return selectedDate;
    }

    /**
     * This will set the correct group as the item with the best online savings
     * @param cards
     * @return CommerceGroup that has the best online savings value
     */
    public static CommerceGroup getBestOnlineSavingsGroup(@NonNull List<CommerceCard> cards){
        if(null == cards || cards.isEmpty()){
            return null;
        }

        CommerceGroup bestValueGroup = null;
        int commerceCardBestValueIndex = -1;
        int commerceGroupBestValueIndex = -1;

        //here we will compare between all the groups in each card to see which has the
        //largest best savings amount
        for(int cardIndex = 0; cardIndex<cards.size(); cardIndex++){
            if(cards.get(cardIndex) != null && cards.get(cardIndex).getGroups() != null) {
                for (int groupIndex = 0; groupIndex < cards.get(cardIndex).getGroups().size(); groupIndex++) {
                    CommerceGroup group = cards.get(cardIndex).getGroups().get(groupIndex);
                    if(group !=  null) {
                        if (bestValueGroup == null) {
                            bestValueGroup = group;
                            commerceCardBestValueIndex = cardIndex;
                            commerceGroupBestValueIndex = groupIndex;
                        } else {
                            BigDecimal groupPrice = group.getOnlineSavingsPrice();
                            BigDecimal bestValueGroupPrice = bestValueGroup.getOnlineSavingsPrice();
                            if (groupPrice != null && bestValueGroupPrice != null) {
                                if (groupPrice.doubleValue() > bestValueGroupPrice.doubleValue()) {
                                    bestValueGroup = group;
                                    commerceCardBestValueIndex = cardIndex;
                                    commerceGroupBestValueIndex = groupIndex;
                                }
                            }
                        }
                    }
                }
            }
        }

        if(commerceCardBestValueIndex > -1 && commerceGroupBestValueIndex > -1) {
            return cards.get(commerceCardBestValueIndex).getGroups().get(commerceGroupBestValueIndex);
        }

        return null;
    }

    /**
     * Find the lowest add-ons price regardless of age (for general products without an adult/child attribute)
     * @param product the add-on product
     * @return price
     */
    public static float findAddOnsLowestPrice(@NonNull PersonalizationExtrasProduct product) {
        float lowestPrice = Float.POSITIVE_INFINITY;
        for (PersonalizationExtrasResultSku sku : product.getPersonalizationExtrasResultSkus()) {
            if (null != sku) {
                for (PersonalizationExtrasResultSkuPrice price : sku.getPersonalizationExtrasResultSkuPrices()) {
                    if (null != price && !TextUtils.isEmpty(price.getValue()) && PersonalizationExtrasResultSkuPrice.USAGE_DISPLAY.equalsIgnoreCase(price.getUsage())) {
                        float priceValue = Float.parseFloat(price.getValue());
                        if (priceValue < lowestPrice) {
                            lowestPrice = priceValue;
                        }
                    }
                }
            }
        }
        return lowestPrice;
    }

    /**
     * Find the lowest add-ons price for adult SKUs
     * @param product the add-on product
     * @return price
     */
    public static float findAddOnsLowestAdultPrice(@NonNull PersonalizationExtrasProduct product) {
        float lowestPrice = Float.POSITIVE_INFINITY;
        for (PersonalizationExtrasResultSku sku : product.getPersonalizationExtrasResultSkus()) {
            if (null != sku) {
                for (PersonalizationExtrasResultAttribute attribute : sku.getPersonalizationExtrasResultSkuAttributes()) {
                    if (attribute.isAge() && attribute.isAdult()) {
                        for (PersonalizationExtrasResultSkuPrice price : sku.getPersonalizationExtrasResultSkuPrices()) {
                            if (null != price && PersonalizationExtrasResultSkuPrice.USAGE_DISPLAY.equalsIgnoreCase(price.getUsage())) {
                                float priceValue = Float.parseFloat(price.getValue());
                                if (priceValue < lowestPrice) {
                                    lowestPrice = priceValue;
                                }
                            }
                        }
                    }
                }
            }
        }
        return lowestPrice;
    }

    /**
     * Find the lowest add-ons price for child SKUs
     * @param product the add-on product
     * @return price
     */
    public static float findAddOnsLowestChildPrice(@NonNull PersonalizationExtrasProduct product) {
        float lowestPrice = Float.POSITIVE_INFINITY;
        for (PersonalizationExtrasResultSku sku : product.getPersonalizationExtrasResultSkus()) {
            if (null != sku) {
                for (PersonalizationExtrasResultAttribute attribute : sku.getPersonalizationExtrasResultSkuAttributes()) {
                    if (attribute.isAge() && attribute.isChild()) {
                        for (PersonalizationExtrasResultSkuPrice price : sku.getPersonalizationExtrasResultSkuPrices()) {
                            if (null != price && PersonalizationExtrasResultSkuPrice.USAGE_DISPLAY.equalsIgnoreCase(price.getUsage())) {
                                float priceValue = Float.parseFloat(price.getValue());
                                if (priceValue < lowestPrice) {
                                    lowestPrice = priceValue;
                                }
                            }
                        }
                    }
                }
            }
        }
        return lowestPrice;
    }

}
