package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.CommerceGroupListener;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.CommerceUiBuilder;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.databinding.CommerceCardHeaderAnnualPassBinding;
import com.universalstudios.orlandoresort.databinding.CommerceCardHeaderDayBinding;
import com.universalstudios.orlandoresort.databinding.TicketShoppingCardFooterBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCard;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCardItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceGroup;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceInventoryItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceOfferPricingAndInventory;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpecManager;
import com.universalstudios.orlandoresort.view.custom_calendar.TicketType;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/30/16.
 * Class: CommerceCardControl
 * Class Description: Control for an entire Card item
 */
public class CommerceCardControl extends LinearLayout implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    public static final String TAG = "CommerceCardControl";

    private String mCardId;
    private LinearLayout commerceCardGroupContainer;
    private CommerceGroupListener addToCartClickListener;
    private TicketType mTicketType;

    public CommerceCardControl(Context context) {
        super(context);
    }

    public CommerceCardControl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommerceCardControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Used to create the product cards after you have applied a filter. This is currently handling tickets and uep pass cards.
     * Needs to be updated for extras cards
     *
     * @param card
     * @param ticketType
     * @param listener
     */
    public void setCard(CommerceCard card, TicketType ticketType, CommerceGroupListener listener) {
        if (null == card || null == card.getGroups() || card.getGroups().isEmpty()) {
            return;
        }
        this.addToCartClickListener = listener;
        this.mTicketType = ticketType;
        this.mCardId = card.getId();

        TextView description = null; //can be updated by group later

        List<CommerceCardItem> cardItems = card.getGroups().get(0).getCardItems();
        if (!cardItems.isEmpty()) {
            CommerceCardItem cardItem = cardItems.get(0);
            TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(cardItem.getIdForTridion());

            ViewGroup header = (ViewGroup) this.findViewById(R.id.commerce_card_header);
            header.setBackgroundColor(labelSpec.getHeaderBackgroundColor());

            //if we are using the group 1 option and have a header image
            if (cardItems.size() > 0 && card.isCardGroupsUseSameSKU()
                    && !TextUtils.isEmpty(labelSpec.getTypeHeaderLogo())) {
                CommerceCardHeaderAnnualPassBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                        R.layout.commerce_card_header_annual_pass, header, true);
                binding.setTridion(labelSpec);
                binding.setData(cardItem);
                description = binding.commerceCardDescription;
            } else {
                CommerceCardHeaderDayBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                        R.layout.commerce_card_header_day, header, true);
                binding.setTridion(labelSpec);
                binding.setData(cardItem);
                description = binding.commerceCardDescription;
            }
        }

        commerceCardGroupContainer = (LinearLayout) this.findViewById(R.id.commerce_card_group_container);

        int numValidInventories = 0;
        for (int i = 0; i < card.getGroups().size(); i++) {
            CommerceGroup group = card.getGroups().get(i);

            boolean usesGroup1Options = i > 0 && cardItems.size() > 0 && card.isCardGroupsUseSameSKU();

            boolean hasValidInventory = false;
            if (ticketType == TicketType.TYPE_TICKET_BMG_BUNDLE || ticketType == TicketType.TYPE_TICKET_UEP_BUNDLE) {
                hasValidInventory = true;
            } else if (group.getCardItems().size() > 0 && group.getCardItems().get(0) != null) {
                hasValidInventory = IceTicketUtils.isInventoryAvailableForCardItem(group.getCardItems().get(0));
            }

            if (hasValidInventory) {
                numValidInventories++;
            }

            CommerceGroupControl groupControl = (CommerceGroupControl) LayoutInflater.from(getContext())
                    .inflate(R.layout.commerce_group_control, null);
            groupControl.setGroup(group, hasValidInventory, usesGroup1Options);
            groupControl.setOnCheckChangedListener(this, listener);

            if (numValidInventories == 1) {
                groupControl.setChecked(hasValidInventory);
                cardItems = group.getCardItems();
                if (!cardItems.isEmpty()) {
                    // Use the first CommerceCardItem to set the header information
                    CommerceCardItem cardItem = cardItems.get(0);
                    TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(cardItem.getIdForTridion());
                    if (description != null) {
                        description.setText(labelSpec.getTypeHeaderLine2(cardItem));
                    }
                }

                if (ticketType.equals(TicketType.TYPE_TICKETS)) {
                    setBestValueText(group);
                    setFloridaResidentValue(group);
                } else if (ticketType.equals(TicketType.TYPE_EXPRESS)) {
                    setDateValueText(group);
                }
                // Show special offer banner if card is an offer
                if (card.isInjected()) {
                    showSpecialOfferBanner(group);
                }
            }

            commerceCardGroupContainer.addView(groupControl);

            if (i < card.getGroups().size() - 1) {
                commerceCardGroupContainer.addView(CommerceUiBuilder.getDivider(getContext()));
            }
        }

        // This pains me to inflate and add a button in Java, but this entire screen needs to be
        // refactored to use a proper RecyclerView with all layouts defined in XML
        TicketShoppingCardFooterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.ticket_shopping_card_footer, this, true);
        binding.setTridion(IceTicketUtils.getTridionConfig());
        Button addToCart = binding.ticketShoppingAddToCartButton;
        Button selectButton = binding.ticketShoppingSelectButton;

        if (ticketType == TicketType.TYPE_TICKET_BMG_BUNDLE || ticketType == TicketType.TYPE_TICKET_UEP_BUNDLE) {
            addToCart.setVisibility(View.GONE);
            selectButton.setVisibility(View.VISIBLE);
            selectButton.setOnClickListener(this);
        } else {
            addToCart.setVisibility(View.VISIBLE);
            selectButton.setVisibility(View.GONE);
            if (numValidInventories > 0) {
                addToCart.setOnClickListener(this);
            } else {
                addToCart.setEnabled(false);
            }
        }

        Button seeDetails = binding.ticketShoppingSeeDetailsButton;

        seeDetails.setOnClickListener(this);
        if (card.isAnnualPass()) {
            seeDetails.setVisibility(VISIBLE);
        } else {
            seeDetails.setVisibility(GONE);
        }

    }

    /**
     * Used to calculate the best value banner text
     *
     * @param selectedGroup
     */
    private void setFloridaResidentValue(CommerceGroup selectedGroup) {
        boolean showFloridaResidentButton = false;
        if (selectedGroup != null && selectedGroup.getCardItems().size() > 0) {
            showFloridaResidentButton = IceTicketUtils.isFloridaResidentTicket(selectedGroup.getCardItems().get(0).getAttributes());
        }

        ViewGroup floridaBadge = (ViewGroup) this.findViewById(R.id.commerce_card_florida_badge);

        //Set Florida badge
        floridaBadge.setVisibility(showFloridaResidentButton ? View.VISIBLE : View.GONE);
    }

    private void showSpecialOfferBanner(CommerceGroup selectedGroup) {
        ViewGroup dateValueContainer = (ViewGroup) this.findViewById(R.id.commerce_card_date_value_container);
        ViewGroup bestValueContainer = (ViewGroup) this.findViewById(R.id.commerce_card_best_value_container);
        ViewGroup specialValueContainer = (ViewGroup) this.findViewById(R.id.commerce_card_special_value_container);
        dateValueContainer.setVisibility(View.GONE);
        bestValueContainer.setVisibility(View.GONE);
        specialValueContainer.setVisibility(View.VISIBLE);
    }

    /**
     * Used to calculate the best value banner text.
     * Best value banner text will either show a best value text or a price per day.
     * This is decided based on if the commerce card has the selected group as the best price banner.
     *
     * @param selectedGroup
     */
    private void setBestValueText(CommerceGroup selectedGroup) {
        ViewGroup bestValueContainer = (ViewGroup) this.findViewById(R.id.commerce_card_best_value_container);
        TextView bestValueText = (TextView) this.findViewById(R.id.commerce_card_best_value_banner_text);
        String bestValueString = null;
        if (selectedGroup.getCardItems().size() > 0) {
            CommerceCardItem cardItem = selectedGroup.getCardItems().get(0);
            TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(cardItem.getIdForTridion());
            bestValueString = labelSpec.getBanner(cardItem);
        }
		if (TextUtils.isEmpty(bestValueString)) {
			bestValueContainer.setVisibility(View.GONE);
		} else {
			bestValueContainer.setVisibility(VISIBLE);
			bestValueText.setText(bestValueString);
		}
	}

    /**
     * This swaps the best value banner with the date value banner and sets the text inside to the current
     * date
     *
     * @param selectedGroup
     */
    private void setDateValueText(CommerceGroup selectedGroup) {
        TextView dateValueText = (TextView) this.findViewById(R.id.commerce_card_date_value_banner_text);
        ViewGroup dateValueContainer = (ViewGroup) this.findViewById(R.id.commerce_card_date_value_container);
        ViewGroup bestValueContainer = (ViewGroup) this.findViewById(R.id.commerce_card_best_value_container);
        bestValueContainer.setVisibility(GONE);
        String dateValueString = null;
        if (selectedGroup.getCardItems().size() > 0) {
            CommerceCardItem cardItem = selectedGroup.getCardItems().get(0);
            TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(cardItem.getIdForTridion());
            dateValueString = labelSpec.getBanner(cardItem);
        }
        if (TextUtils.isEmpty(dateValueString)) {
            dateValueContainer.setVisibility(View.GONE);
        } else {
            dateValueContainer.setVisibility(VISIBLE);
            dateValueText.setText(dateValueString);
        }

    }

    public CommerceGroup getSelectedGroup() {
        for (int i = 0; i < commerceCardGroupContainer.getChildCount(); i++) {
            if (commerceCardGroupContainer.getChildAt(i) instanceof CommerceGroupControl) {
                CommerceGroupControl control = (CommerceGroupControl) commerceCardGroupContainer.getChildAt(i);
                if (control.isChecked()) {
                    return control.getGroup();
                }
            }
        }
        return null;
    }

	private String getAdultPartNumber(CommerceGroup group){
		if (group != null){
			for (CommerceCardItem cardItem : group.getCardItems()) {
				if (cardItem != null) {
					if (cardItem.isAdult()){
						return String.valueOf(cardItem.getPartNumber());
					}
				}
			}
		}
		return "";
	}

	private String getChildPartNumber(CommerceGroup group){
		if (group != null){
			for (CommerceCardItem cardItem : group.getCardItems()) {
				if (cardItem != null) {
					if (cardItem.isChild()){
						return String.valueOf(cardItem.getPartNumber());
					}
				}
			}
		}
		return "";
	}

    private String getExpressPartNumber(CommerceGroup group){
        if (group != null){
            for (CommerceCardItem cardItem : group.getCardItems()) {
                if (cardItem != null) {
                    return String.valueOf(cardItem.getPartNumber());
                }
            }
        }
        return "";
    }

    private String getGroupEventId(CommerceGroup group){
        if (group != null){
            for (CommerceCardItem cardItem : group.getCardItems()) {
                if (cardItem.getPricingAndInventory() != null && cardItem.getPricingAndInventory().getOfferPricingAndInventory() != null &&
                        !cardItem.getPricingAndInventory().getOfferPricingAndInventory().isEmpty()) {
                    CommerceOfferPricingAndInventory[] commerceOfferPricingAndInventorys = cardItem.getPricingAndInventory().getOfferPricingAndInventory().values().toArray(new CommerceOfferPricingAndInventory[cardItem.getPricingAndInventory().getOfferPricingAndInventory().size()]);
                    for(CommerceOfferPricingAndInventory commerceOfferPricingAndInventory : commerceOfferPricingAndInventorys) {
                        if(null != commerceOfferPricingAndInventory.getInventoyItems() && commerceOfferPricingAndInventory.getInventoyItems().size() > 0) {
                            for(CommerceInventoryItem inventoryItem : commerceOfferPricingAndInventory.getInventoyItems()) {
                                if(null != inventoryItem.getEventId() && !inventoryItem.getEventId().toString().isEmpty()) {
                                    return inventoryItem.getEventId().toString();
                                }
                            }
                        }
                    }
                }
            }
        }
        return "";
    }

    private String getGroupResourceId(CommerceGroup group){
        if (group != null){
            for (CommerceCardItem cardItem : group.getCardItems()) {
                if (cardItem.getPricingAndInventory() != null && cardItem.getPricingAndInventory().getOfferPricingAndInventory() != null &&
                        !cardItem.getPricingAndInventory().getOfferPricingAndInventory().isEmpty()) {
                    CommerceOfferPricingAndInventory[] commerceOfferPricingAndInventorys = cardItem.getPricingAndInventory().getOfferPricingAndInventory().values().toArray(new CommerceOfferPricingAndInventory[cardItem.getPricingAndInventory().getOfferPricingAndInventory().size()]);
                    for(CommerceOfferPricingAndInventory commerceOfferPricingAndInventory : commerceOfferPricingAndInventorys) {
                        if(null != commerceOfferPricingAndInventory.getInventoyItems() && commerceOfferPricingAndInventory.getInventoyItems().size() > 0) {
                            for(CommerceInventoryItem inventoryItem : commerceOfferPricingAndInventory.getInventoyItems()) {
                                if(null != inventoryItem.getResourceId() && !inventoryItem.getResourceId().toString().isEmpty()) {
                                    return inventoryItem.getResourceId().toString();
                                }
                            }
                        }
                    }
                }
            }
        }
        return "";
    }

	private List<OrderItem> getOrderItems() {
        if(null != CommerceUiBuilder.getCurrentIdentifier() && CommerceUiBuilder.getCurrentIdentifier().contains(CommerceUiBuilder.IDENTIFIER_EXPRESS)) {
            return getOrderItemForUEPPasses();
        } else {
            return getOrderItemForParkTickets();
        }
	}

    private List<OrderItem> getOrderItemForParkTickets() {
        List<OrderItem> orderItems = new ArrayList<>();
        int adultCount = CommerceUiBuilder.getCurrentFilter().getNumberOfAdultTickets();
        int childCount = CommerceUiBuilder.getCurrentFilter().getNumberOfChildTickets();

        List<CommerceAttribute> attributes = new ArrayList<>();
        if (!TextUtils.isEmpty(this.mCardId)) {
            attributes.add(CommerceAttribute.createCardIdAttribute(this.mCardId));
        }

        CommerceGroup group = getSelectedGroup();
        if (null != group && group.isFlexPayGroup()) {
            attributes.add(CommerceAttribute.createFlexPayAttribute());
        }

        if (adultCount > 0) {
            OrderItem adultTickets = new OrderItem();
            adultTickets.setPartNumber(getAdultPartNumber(group));
            adultTickets.setQuantity(adultCount);
            if (!attributes.isEmpty()) {
                adultTickets.setAttributes(attributes);
            }
            orderItems.add(adultTickets);
        }

        if (childCount > 0) {
            OrderItem childTickets = new OrderItem();
            childTickets.setPartNumber(getChildPartNumber(group));
            childTickets.setQuantity(childCount);
            if (!attributes.isEmpty()) {
                childTickets.setAttributes(attributes);
            }
            orderItems.add(childTickets);
        }

        return orderItems;
    }

    private List<OrderItem> getOrderItemForUEPPasses() {
        List<OrderItem> orderItems = new ArrayList<>();
        int expressCount = CommerceUiBuilder.getCurrentFilter().getNumExpressTickets();

        CommerceGroup group = getSelectedGroup();

        if (expressCount > 0) {
            OrderItem expressTickets = new OrderItem();
            expressTickets.setPartNumber(getExpressPartNumber(group));
            expressTickets.setQuantity(expressCount);

            List<CommerceAttribute> attributes = new ArrayList<>();
            attributes.add(CommerceAttribute.createInvEventIdAttribute(getGroupEventId(group)));
            attributes.add(CommerceAttribute.createInvResourceIdAttribute(getGroupResourceId(group)));
            if (!TextUtils.isEmpty(this.mCardId)) {
                attributes.add(CommerceAttribute.createCardIdAttribute(this.mCardId));
            }
            expressTickets.setAttributes(attributes);
            orderItems.add(expressTickets);
        }

        return orderItems;
    }

	@Override
	public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ticket_shopping_add_to_cart_button:
                if (null != addToCartClickListener) {
                    addToCartClickListener.onAddToCartClicked(getOrderItems());
                }
                break;
            case R.id.ticket_shopping_see_details_button:
                if (null != addToCartClickListener) {
                    addToCartClickListener.onMoreClicked(getSelectedGroup());
                }
                break;
            case R.id.ticket_shopping_select_button:
                    addToCartClickListener.onSelectClicked(getSelectedGroup());
                break;
        }
	}

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            for (int i = 0; i < commerceCardGroupContainer.getChildCount(); i++) {
                if (commerceCardGroupContainer.getChildAt(i) instanceof CommerceGroupControl) {
                    CommerceGroupControl control = (CommerceGroupControl) commerceCardGroupContainer.getChildAt(i);
                    if (control.getRadioButton() != buttonView) {
                        control.setChecked(false);
                    } else {
                        CommerceGroup selectedGroup = control.getGroup();
                        if (null != selectedGroup) {
                            List<CommerceCardItem> cardItems = selectedGroup.getCardItems();
                            if (!cardItems.isEmpty()) {
                                // Use the first CommerceCardItem from the first CommerceGroup to set the header information
                                CommerceCardItem cardItem = cardItems.get(0);
                                TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(cardItem.getIdForTridion());
                                String newTitleText = labelSpec.getTypeHeaderLine1(cardItem);
                                ((TextView) this.findViewById(R.id.commerce_card_title)).setText(newTitleText);
                                String newDescriptionText = labelSpec.getTypeHeaderLine2(cardItem);
                                ((TextView) this.findViewById(R.id.commerce_card_description)).setText(newDescriptionText);
                            }

                            if (this.mTicketType != null && this.mTicketType.equals(TicketType.TYPE_TICKETS)) {
                                setBestValueText(control.getGroup());
                                setFloridaResidentValue(control.getGroup());
                            }
                        }
                    }
                }
            }
        }
    }

}
