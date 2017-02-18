package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.general.BasicInfoDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets.TicketAssignmentUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views.CommerceCardControl;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views.CommerceDaysControl;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views.DaysButton;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views.OnCountsChangedListener;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views.OnDateChangedListener;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views.OnSelectionChangedListener;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views.PartyMembersControl;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views.PooControl;
import com.universalstudios.orlandoresort.controller.userinterface.utils.AndroidUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.CommerceCardControlBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Entitlements.ByDate.response.CombinedSeasonalTicket;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupOrder;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupingResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCard;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceUIControl;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.UIControlField;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetCardsRequest;
import com.universalstudios.orlandoresort.view.TicketCounterView;
import com.universalstudios.orlandoresort.view.custom_calendar.CalendarLegendAdapter;
import com.universalstudios.orlandoresort.view.custom_calendar.CalendarLegendSeason;
import com.universalstudios.orlandoresort.view.custom_calendar.CustomCalendarView;
import com.universalstudios.orlandoresort.view.custom_calendar.TicketType;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: CommerceUiBuilder
 * Class Description: Single class used for controlling the building and display of UI
 */
public class CommerceUiBuilder {
    public static final String TAG = "CommerceUiBuilder";

    //be sure to use all caps for your control types.
    public static final String CONTROL_TYPE_DAYS = "DAYS";
    public static final String CONTROL_TYPE_PARTYMEMBERS = "PARTYMEMBERS";
    public static final String CONTROL_TYPE_CALENDAR = "CALENDAR";
    public static final String CONTROL_TYPE_POO = "POO";

    //UEP Specific
    public static final String CONTROL_TYPE_PARKS = "PARKS";
    public static final int SEASONS_TOOLTIP_SECONDS = 30;
    public static final String IDENTIFIER_EXPRESS = "EXPRESS";

    private static CommerceUiBuilder sInstance = null;
    private ControlChangeListener listener;

    @NonNull
    private static synchronized CommerceUiBuilder getInstance() {
        if (null == sInstance) {
            sInstance = new CommerceUiBuilder();
        }
        return sInstance;
    }
    /**
     * Every reference to ticket filter info should point here
     * DO NOT pass around a reference. This class is a singleton and for
     * readability and to prevent bugs, we store the current filter information
     * here and ONLY here and reference the singleton anytime we need to access, modify, or reset the filters.
     * Only use the provided static methods for accessing this field. Add new static methods that reference the
     * singleton if you need to add custom controls in the future.
     */
    private static TicketFilterInfo sTicketFilterInfo; //Every reference to ticket filter info should point here.

    private static String currentIdentifier;

    private int ticketsInCart = 0;
    private BigDecimal cartSubTotal = BigDecimal.ZERO;

    private TridionConfig tridionConfig;

    /**
     * Constructor: Set to private to prevent manual instantiation
     */
    private CommerceUiBuilder(){}

    /**
     * Factory method to get the singleton instance of this class
     *
     * @param listener ControlChangeListener
     *                 Note: Do not pass in NULL to this. Create a static method to gain
     *                 access to fields if needed. EX: {@link this#getCurrentFilter()}
     *
     * @return Singleton instance of CommerceUiBuilder
     */
    public static void setControlChangeListener(@NonNull ControlChangeListener listener) {
        getInstance().listener = listener;
    }

    public static String getCurrentIdentifier() {
        return currentIdentifier;
    }

    public static void setCurrentIdentifier(String newIdentifier) {
         currentIdentifier = newIdentifier;
    }

    /**
     * Static method to obtain the reference to the current TicketFilterInfo
     *
     * @return Current TicketFilterInfo
     */
    @NonNull
    public static TicketFilterInfo getCurrentFilter() {
        if (null == sTicketFilterInfo) {
            return sTicketFilterInfo = new TicketFilterInfo();
        }
        return sTicketFilterInfo;
    }

    private static void setCartCount(int cartCount) {
        getInstance().ticketsInCart = cartCount;
    }

    public static int getCartCount() {
        return getInstance().ticketsInCart;
    }

    private static void setCartSubTotal(BigDecimal cartSubTotal) {
        getInstance().cartSubTotal = cartSubTotal;
    }

    public static String getCartSubTotalFormatted() {
        return IceTicketUtils.getTridionConfig().getFormattedPrice(getInstance().cartSubTotal);
    }

    public static void setCartData(TicketGroupingResponse ticketGroupingResponse) {
        int numTickets = IceTicketUtils.getNumberOfTicketsInCart(ticketGroupingResponse);
        setCartCount(numTickets);


        BigDecimal subTotal = BigDecimal.ZERO;
        if (null != ticketGroupingResponse
                && null != ticketGroupingResponse.getOrder()
                && null != ticketGroupingResponse.getOrder().getPricing()
                && null != ticketGroupingResponse.getOrder().getPricing().getTotalProductPrice()) {
            subTotal = ticketGroupingResponse.getOrder().getPricing().getTotalProductPrice();
        }

        setCartSubTotal(subTotal);
    }


    /**
     * This method is used to dynamically create a UIControl from services. This method is
     * not static because it requires an instance to be created or fetched with a ControlChangeListener
     *
     * @param control Control to create
     * @param context Calling Context
     * @param layout Layout to add the control to
     * @param calendarResultMap The given calendar season map
     * @param addDivider should this control be FOLLOWED by a divider
     */
    public static void createControlFromType(CommerceUIControl control, final Context context, LinearLayout layout, String cardTitle,
            Map<String, CombinedSeasonalTicket> calendarResultMap, boolean addDivider) {
        getInstance().createControlFromTypeNonStatic(control, context, layout, cardTitle, calendarResultMap, addDivider);
    }

    private void createControlFromTypeNonStatic(CommerceUIControl control, final Context context, LinearLayout layout, String cardTitle, Map<String, CombinedSeasonalTicket> calendarResultMap, boolean addDivider) {

        tridionConfig = IceTicketUtils.getTridionConfig();

        final String controlType = control.getControlType();
        final List<UIControlField> controlFields = control.getFields();

        //if the control type is unidentifiable, then we cancel out of the method
        if(controlType == null || controlType.isEmpty()){
            return;
        }

        if (controlType.toUpperCase().equals(CONTROL_TYPE_DAYS) || controlType.equals(CONTROL_TYPE_PARKS)) {
            final boolean isControlTypeDays = controlType.equals(CONTROL_TYPE_DAYS);

            if (isControlTypeDays) {

                // Inflate the number of days header layout (contains the number of days header as well) to be added
                LinearLayout numDaysLayout = (LinearLayout) LayoutInflater.from(context)
                        .inflate(R.layout.ticket_filter_info_label, null);
                TextView ticketsLabel = (TextView) numDaysLayout.findViewById(R.id.tickets_label);
                ticketsLabel.setText(tridionConfig.getNoOfDaysLabel());

                // Adjust the number of days header info icon within the map legend
                ImageView mInfo = (ImageView) numDaysLayout.findViewById(R.id.tickets_info_popup);
                mInfo.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, tridionConfig.getBuyTicketsFilterNoOfDaysPopUp(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
                layout.addView(numDaysLayout);
            } else {
                addHeaderLabel(tridionConfig.getNoOfParksLabel(), layout, context);
            }


            //Add controls
            CommerceDaysControl daysControl = new CommerceDaysControl(context);
            //control logic should changed based on which variable the controls modify
            //on the selection changed listener
            if(isControlTypeDays) {
                daysControl.setSelectedFromFilterInfo(getCurrentFilter().numberOfDays);
            } else {
                daysControl.setSelectedFromFilterInfo(getCurrentFilter().numberOfParks);
            }

            CommerceDaysControl.DaysControlOrderingType orderingType;

            //TODO this logic would need to be updated to support extras
            if(isControlTypeDays) {
                orderingType = CommerceDaysControl.DaysControlOrderingType.REVERSE;
            } else {
                orderingType = CommerceDaysControl.DaysControlOrderingType.NORMAL;
            }

            daysControl.setUIControlFields(orderingType, controlFields);
            daysControl.setOnSelectionChangedListener(new OnSelectionChangedListener() {
                @Override
                public void onSelectionChanged(DaysButton selectedButton) {
                    if (null != listener) {
                        setFilterInfoWithoutUIFromDaysControl(selectedButton);
                    }
                }
            });

            layout.addView(daysControl);

            if(!isControlTypeDays) {
                addDetailLabel(cardTitle, layout, context);
            }

        } else if (controlType.toUpperCase().equals(CONTROL_TYPE_CALENDAR)) {
            boolean isExpressPass = null != getCurrentIdentifier() && getCurrentIdentifier().toUpperCase().contains(GetCardsRequest.UEP_IDENTIFIER);

            // Florida residents shouldn't show for 1-day
            getCurrentFilter().isFloridaResident = false;

            // Add the calendar visiting header
            String calHeader;
            if(isExpressPass) {
                calHeader = tridionConfig.getSelectDatesLabel();
            } else {
                calHeader = tridionConfig.getSCSubHeader();
            }

            addHeaderLabel(calHeader, layout, context);

            if(!isExpressPass) {
                // Add ticket prices vary disclaimer
                String ticketDisclaimer = tridionConfig.getSCIntroText();
                addDetailLabel(ticketDisclaimer, layout, context);
            }


            // Add the calendar
            CustomCalendarView calendarView = (CustomCalendarView) LayoutInflater.from(context)
                    .inflate(R.layout.custom_calendar_view, null);

            Integer defaultDateRange = null;
            if(!isExpressPass) {
                if (controlFields != null && controlFields.size() > 0) {
                    try {
                        defaultDateRange = Integer.parseInt(controlFields.get(0).getDisplayValue());
                    } catch (NumberFormatException ex) {

                    }
                }
            }

            //TODO this logic would need to be updated for extras
            calendarView.init(calendarResultMap, defaultDateRange, !isExpressPass);
            layout.addView(calendarView);

            LinearLayout mapLegend = null;
            if(!isExpressPass) {
                // Inflate the map legend (contains the "Seasons" header as well) to be added
                mapLegend = (LinearLayout) LayoutInflater.from(context)
                        .inflate(R.layout.calendar_season_legend, null);
                TextView calendarSeasonsHeader = (TextView) mapLegend.findViewById(R.id.calendar_seasons_header);
                calendarSeasonsHeader.setText(tridionConfig.getSCSeasonsLabel());

                setMapLegendTableCells(mapLegend, context);
                // Adjust the seasons header info icon within the map legend
                ImageView mInfo = (ImageView) mapLegend.findViewById(R.id.calendar_seasons_info_popup);
                mInfo.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle args = BasicInfoDetailActivity.newInstanceBundle(tridionConfig.getSCSeasonsLabel(), null,
                                tridionConfig.getSCSeasonsTooltip());
                        Intent intent = new Intent(context, BasicInfoDetailActivity.class)
                                .putExtras(args);
                        context.startActivity(intent);
                    }
                });
            } else {
                calendarView.findViewById(R.id.calendar_season_description_view).setVisibility(View.GONE);
            }

            // When a calendar date is selected, notify the fragment to send a new card request
            calendarView.setOnDateChangedListener(new OnDateChangedListener() {
                @Override
                public void onDateChanged(Date selectedDate) {
                    listener.onControlIdentifierChanged(currentIdentifier, selectedDate);
                }
            });

            calendarView.setInitialDate();

            if(null != mapLegend) {
                layout.addView(mapLegend);
            }

        } else if (controlType.toUpperCase().equals(CONTROL_TYPE_PARTYMEMBERS)) {
            final boolean isControlTypeExpress = controlFields.size() == 1;
            String headerLabel = isControlTypeExpress ? tridionConfig.getNoOfGuestsLabel() : tridionConfig.getNoOfGuestsTravelingLabel();
            addHeaderLabel(headerLabel, layout, context);

            final PartyMembersControl partyMembersControl = new PartyMembersControl(context);
            partyMembersControl.setOnCountChangedListener(new OnCountsChangedListener() {
                @Override
                public void onCountChanged(List<TicketCounterView> counterViews, TicketCounterView viewChanged,
                                           int oldValue, int newValue, int totalOfAllViews) {

                     if (!counterViews.isEmpty() && counterViews.size() == 2) {
                         TicketCounterView adultCounterView = counterViews.get(0);
                         TicketCounterView childCounterView = counterViews.get(1);
                         getCurrentFilter().numberOfAdultTickets = adultCounterView.getCurrentValue();
                         getCurrentFilter().numberOfChildTickets = childCounterView.getCurrentValue();

                         if(adultCounterView.getCurrentValue() > 0) {
                             childCounterView.setMinimumValue(0);
                         }

                         if(childCounterView.getCurrentValue() > 0) {
                             adultCounterView.setMinimumValue(0);
                         }

                         if(childCounterView.getCurrentValue() == 0){
                             adultCounterView.setMinimumValue(1);
                         }

                         if(adultCounterView.getCurrentValue() == 0){
                             childCounterView.setMinimumValue(1);
                         }

                     } else if (counterViews.size() == 1) {
                         getCurrentFilter().numExpressTickets = counterViews.get(0).getCurrentValue();
                         counterViews.get(0).setMinimumValue(1);
                         CommerceUiBuilder.getCurrentFilter().hasExpressBeenChangedManually = true;
                     } else if (counterViews.size() > 2 && BuildConfig.DEBUG) {
                         //We only force a crash in debug
                         throw new IllegalStateException("We have three ticketcounter views being returned but should never" +
                                 "have more than 2 total");
                     }
                }
            });

            int adults = 2;
            int childs = 0;
            int numExpressTickets = getCurrentFilter().numExpressTickets;
            if (getCurrentFilter().numberOfAdultTickets != -1) {
                adults = getCurrentFilter().numberOfAdultTickets;
            }
            if (getCurrentFilter().numberOfChildTickets != -1) {
                childs = getCurrentFilter().numberOfChildTickets;
            }


            //when there is only one ticket group for park tickets and we are initially loading this control
            //for uep, we show the total number of tickets instead of the default
            if(isControlTypeExpress) {
                TicketGroupOrder groupOrder = TicketAssignmentUtils.instance().getTicketGroupOrder();
                if(numExpressTickets == -1) {
                    int ticketTotal;
                    if (groupOrder != null &&
                            groupOrder.getOrderItemGroups() != null &&
                            groupOrder.getOrderItemGroups().getParkTicketGroups() != null &&
                            groupOrder.getOrderItemGroups().getParkTicketGroups().size() == 1) {
                        ticketTotal = 0;
                        if (groupOrder.getOrderItemGroups().getParkTicketGroups().get(0).getAdultTickets() != null) {
                            ticketTotal += groupOrder.getOrderItemGroups().getParkTicketGroups().get(0).getAdultTickets().getQuantity();
                        }

                        if (groupOrder.getOrderItemGroups().getParkTicketGroups().get(0).getChildTickets() != null) {
                            ticketTotal += groupOrder.getOrderItemGroups().getParkTicketGroups().get(0).getChildTickets().getQuantity();
                        }
                    } else {
                        ticketTotal = adults + childs;
                    }
                    numExpressTickets = ticketTotal;

                    //if adults and child tickets are not set, then we set a default value
                    if(numExpressTickets <= -1){
                        numExpressTickets = 2;
                    }
                }

                //sets the correct display name for the number of guests tridion control field value
                if(controlFields.size() > 0){
                    controlFields.get(0).setDisplayValue(tridionConfig.getAllAgesLabel());
                }

                partyMembersControl.setUIControlFields(controlFields, numExpressTickets );
                getCurrentFilter().numExpressTickets = numExpressTickets;
            } else {
                partyMembersControl.setUIControlFields(controlFields, adults, childs);
                getCurrentFilter().numberOfAdultTickets = adults;
                getCurrentFilter().numberOfChildTickets = childs;
            }

            layout.addView(partyMembersControl);
        } else if (controlType.toUpperCase().equals(CONTROL_TYPE_POO)) {
            final PooControl pooControl = new PooControl(context);
            pooControl.setChecked(getCurrentFilter().checkedState);
            pooControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (null != listener) {
                        getCurrentFilter().isFloridaResident = isChecked;
                        if (isChecked) {
                            getCurrentFilter().checkedState = PooControl.CheckedState.CHECKED;
                        } else {
                            getCurrentFilter().checkedState = PooControl.CheckedState.UNCHECKED;
                        }
                        currentIdentifier = pooControl.getIdentifier();
                        listener.onControlIdentifierChanged(currentIdentifier, null);
                    }
                }
            });
            pooControl.setUIControlFields(controlFields);

            if (getCurrentFilter().isFloridaResident) {
                getCurrentFilter().checkedState = PooControl.CheckedState.CHECKED;
            } else {
                getCurrentFilter().checkedState = PooControl.CheckedState.UNCHECKED;
            }

            if(pooControl.getCheckedState() != getCurrentFilter().checkedState) {
                pooControl.setSwitchChecked(getCurrentFilter().isFloridaResident);
            }
            layout.addView(pooControl);
        }

        if (addDivider) {
            layout.addView(getDivider(context));
        }
    }

    public static void setFilterInfoWithoutUIFromDaysControl(DaysButton selectedButton){
        if(getCurrentIdentifier() != null && getCurrentIdentifier().toUpperCase().contains(GetCardsRequest.UEP_IDENTIFIER)) {
            setNumParksFilterInfoFromParksControl(selectedButton);
        } else {
            setDaysFilterInfoFromDaysControl(selectedButton);
        }
    }

    public static void setDaysFilterInfoFromDaysControl(DaysButton selectedButton) {
        String text = selectedButton.getText();
        try {
            getCurrentFilter().numberOfDays = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            getCurrentFilter().numberOfDays = TicketFilterInfo.FLAG_ANNUAL_PASS;
        }

        if (null != getInstance().listener) {
            currentIdentifier = ((UIControlField) selectedButton.getTag()).getServiceIdentifier();
            getInstance().listener.onControlIdentifierChanged(currentIdentifier, null);
        }
    }

    //TODO address with TA23460
    public static void setNumParksFilterInfoFromParksControl(DaysButton selectedButton) {
        String text = selectedButton.getText();
        try {
            getCurrentFilter().numberOfParks = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            getCurrentFilter().numberOfParks = 3;
        }

        if (null != getInstance().listener) {
            currentIdentifier = ((UIControlField) selectedButton.getTag()).getServiceIdentifier();
            getInstance().listener.onControlIdentifierChanged(currentIdentifier, null);
        }
    }

    /**
     * Gets a divider View
     * TODO: Use a proper recyclerview with layouts and view holders
     *
     * @param context Calling Context
     * @return A divider View
     */
    public static View getDivider(Context context) {
        View divider = new View(context);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) AndroidUtils.convertDpToPixel(1, context));
        dividerParams.setMargins((int) AndroidUtils.convertDpToPixel(16, context),
                (int) AndroidUtils.convertDpToPixel(10, context),
                (int) AndroidUtils.convertDpToPixel(16, context),
                (int) AndroidUtils.convertDpToPixel(10, context));
        divider.setLayoutParams(dividerParams);
        divider.setBackgroundColor(context.getResources().getColor(R.color.detail_divider_gray));
        return divider;
    }

    /**
     * Creates a page title (not ActionBar title) and adds it to the layout provided
     * TODO: Use a proper recyclerview with layouts and view holders
     *
     * @param text Text to display in title
     * @param layout Layout to add this View to
     * @param context Calling Context
     */
    public static void addTitleLabel(String text, LinearLayout layout, Context context) {
        //Set label
        TextView label = new TextView(context);
        label.setGravity(Gravity.START);
        label.setTextSize(21);
        label.setFont(R.string.font_gotham_bold);
        label.setTextColor(Color.BLACK);
        label.setTypeface(Typeface.DEFAULT_BOLD);
        label.setTextAppearance(context, R.style.TextViewStyle_TicketCalendarHeader);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, (int) AndroidUtils.convertDpToPixel(10, context), 0, (int) AndroidUtils.convertDpToPixel(10, context));
        label.setLayoutParams(layoutParams);
        label.setText(text);
        layout.addView(label);
    }

    /**
     * Creates a header label to be added to a linear layout (Horizontal or Vertical)
     * TODO: Use a proper recyclerview with layouts and view holders
     *
     * @param text Text for label
     * @param layout LinearLayout to add the View to
     * @param context Calling Context
     */
    public static void addHeaderLabel(String text, LinearLayout layout, Context context) {
        //Set label
        TextView label = new TextView(context);
        label.setTextAppearance(context, R.style.TextViewStyle_TicketCalendarHeader);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, (int) AndroidUtils.convertDpToPixel(10, context), 0, (int) AndroidUtils.convertDpToPixel(5, context));
        label.setLayoutParams(layoutParams);
        label.setText(text);
        layout.addView(label);
    }

    /**
     * Creates a light gray detail label to be added to a linear layout (Horizontal or Vertical)
     * TODO: Use a proper recyclerview with layouts and view holders
     *
     * @param text Text for label
     * @param layout LinearLayout to add the View to
     * @param context Calling Context
     */
    public static void addDetailLabel(String text, LinearLayout layout, Context context) {
        //Set label
        TextView label = new TextView(context);
        label.setTextAppearance(context, R.style.TextViewStyle_TicketCalendarDetails);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, (int) AndroidUtils.convertDpToPixel(5, context), 0, (int) AndroidUtils.convertDpToPixel(10, context));
        label.setLayoutParams(layoutParams);
        label.setText(text);
        layout.addView(label);
    }

    /**
     * Creates the cards to be displayed for tickets
     * TODO: Use a proper recyclerview with layouts and view holders
     *  @param cards Cards to be displayed
     * @param context Calling Context
     * @param layout Layout to add the cards to
     * @param ticketType
     */
    public static void createCards(List<CommerceCard> cards, Context context, LinearLayout layout, CommerceGroupListener addToCartClickListener, TicketType ticketType) {
        layout.removeAllViews();
        if (cards != null) {
            boolean isExpressPass = (getCurrentIdentifier() != null && getCurrentIdentifier().toUpperCase().contains(IDENTIFIER_EXPRESS));
            // TODO this should be changed to a proper recycler view implementation where each type is responsible for it's own view holder and layout

            for (CommerceCard card : cards) {
				if (card != null) {
                    CommerceCardControlBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                            R.layout.commerce_card_control, null, false);
                    binding.setTridion(IceTicketUtils.getTridionConfig());
                    CommerceCardControl cardControl = (CommerceCardControl) binding.getRoot();

					cardControl.setCard(card, ticketType, addToCartClickListener);
					layout.addView(cardControl);
				}
			}
        }
    }

    public static View getWhiteDivider(Context context) {
        View divider = new View(context);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) AndroidUtils.convertDpToPixel(1, context));
        dividerParams.setMargins(0, 0, 0, 0);
        divider.setLayoutParams(dividerParams);
        divider.setBackgroundColor(Color.WHITE);
        return divider;
    }

    public static void addAssignNameItem(Context context, String title, ViewGroup layout, Object tag,
                                         boolean showDivider, View.OnClickListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.commerce_assign_name_item, null);
        android.widget.TextView tvTitle = (android.widget.TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        view.setTag(tag);
        view.setOnClickListener(listener);
        layout.addView(view);
        if (showDivider) {
            layout.addView(getWhiteDivider(context));
        }
    }

    public static void addCurrentlyAssignedNameItem(Context context, String title, String subtitle, ViewGroup layout, Object tag,
                                                    boolean showDivider, View.OnClickListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.commerce_assign_name_item, null);
        android.widget.TextView tvTitle = (android.widget.TextView) view.findViewById(R.id.tvTitle);
        LinearLayout assignedContainer = (LinearLayout) view.findViewById(R.id.assignedContainer);
        assignedContainer.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);

        android.widget.TextView tvName = (android.widget.TextView) view.findViewById(R.id.tvName);
        tvName.setText(title);

        android.widget.TextView tvSubtitle = (android.widget.TextView) view.findViewById(R.id.tvNumberOfAssignments);
        tvSubtitle.setText(subtitle);

        view.setTag(tag);
        view.setOnClickListener(listener);
        layout.addView(view);
        if (showDivider) {
            layout.addView(getWhiteDivider(context));
        }
    }

    /**
     * Sets each cell in the GridView of map table cells (value, regular, peak, and mixed labels).
     *
     * @param mapLegendTableCells The parent LinearLayout view containing the GridView
     * @param context The context
     */
    private void setMapLegendTableCells(LinearLayout mapLegendTableCells, final Context context) {
        CalendarLegendSeason[] valueSeasons = new CalendarLegendSeason[4];

        valueSeasons[0] = new CalendarLegendSeason(tridionConfig.getSCValueLabel(), tridionConfig.getSCValueDescriptionLabel(),
                tridionConfig.getSCValueColor());
        valueSeasons[1] = new CalendarLegendSeason(tridionConfig.getSCRegularLabel(), tridionConfig.getSCRegularDescriptionLabel(),
                tridionConfig.getSCRegularColor());
        valueSeasons[2] = new CalendarLegendSeason(tridionConfig.getSCPeakLabel(), tridionConfig.getSCPeakDescriptionLabel(),
                tridionConfig.getSCPeakColor());
        valueSeasons[3] = new CalendarLegendSeason(tridionConfig.getSCMixedLabel(), tridionConfig.getSCMixedDescriptionLabel(),
                tridionConfig.getSCMixedColor());

        GridView gridView = (GridView) mapLegendTableCells.findViewById(R.id.calendar_seasons_grid);
        CalendarLegendAdapter gridAdapter = new CalendarLegendAdapter(context, valueSeasons);
        gridView.setAdapter(gridAdapter);
    }
}
