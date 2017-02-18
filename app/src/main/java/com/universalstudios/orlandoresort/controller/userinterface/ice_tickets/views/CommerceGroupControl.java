package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.CommerceGroupListener;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.link.ClickListener;
import com.universalstudios.orlandoresort.controller.userinterface.link.ClickableTextHelper;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCardItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceGroup;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpecManager;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/30/16.
 * Class: CommerceGroupControl
 * Class Description: Control for a Group
 */
public class CommerceGroupControl extends RelativeLayout implements CompoundButton.OnCheckedChangeListener {
    public static final String TAG = "CommerceGroupControl";

    private boolean isEnabled = false;
    private RadioButton radioButton;
    private CommerceGroup group;

    private CompoundButton.OnCheckedChangeListener listener;

    private CommerceGroupListener commerceGroupListener;

    public CommerceGroupControl(Context context) {
        super(context);
    }

    public CommerceGroupControl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommerceGroupControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setGroup(final CommerceGroup group, boolean enabled, boolean usesGroup1Options) {
        this.group = group;
        isEnabled = enabled;
        radioButton = (RadioButton) this.findViewById(R.id.commerceGroupRadioButton);
        radioButton.setOnCheckedChangeListener(this);

        TextView groupTitle = (TextView) this.findViewById(R.id.commerceGroupTitle);
        TextView groupDescription = (TextView) this.findViewById(R.id.commerceGroupDescription);
        TextView primaryPriceLabel = (TextView) this.findViewById(R.id.commercePrimaryPriceLabel);
        TextView startingFromLabel = (TextView) this.findViewById(R.id.commerceStartingFromLabel);

        TextView adultPrice = (TextView) this.findViewById(R.id.commerceGroupAdultPrice);
        TextView childPrice = (TextView) this.findViewById(R.id.commerceGroupChildPrice);
        TextView upsellTeaserText = (TextView) this.findViewById(R.id.commerceUpsellText);
        ImageView upsellImageView = (ImageView) this.findViewById(R.id.commerceUpsellImage);
        ViewGroup upsellContainer = (ViewGroup) this.findViewById(R.id.commerceUpsellContainer);

        String upsellText = null;
        String imgLocation = null;
        String adultPriceText = null;
        String childPriceText = null;
        if (null != group && !group.getCardItems().isEmpty()) {
            CommerceCardItem cardItem = group.getAdultItem();
            TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(cardItem.getIdForTridion());
            upsellText = labelSpec.getUpsellTeaserText();
            imgLocation = labelSpec.getUpsellImageURL();
            if (null != group.getAdultPricingAndInventory()) {

                if (group.isAnnualPass() && group.isFlexPayGroup()) {
                    adultPriceText = IceTicketUtils.getTridionConfig().getFormattedPrice(group.getDownPaymentValue());
                } else {
                    adultPriceText = IceTicketUtils.getTridionConfig().getFormattedPrice(group.getAdultPricingAndInventory().getOfferPrice());
                }
            }

            String descriptionText;
            if (usesGroup1Options) {
                groupTitle.setText(labelSpec.getPurchaseOption1Title());
                descriptionText = labelSpec.getPurchaseOption1Teaser();
                primaryPriceLabel.setText(labelSpec.getPurchaseOption1PrimaryPriceLabel());
            } else {
                groupTitle.setText(labelSpec.getPurchaseOptionTitle());
                descriptionText = labelSpec.getPurchaseOptionTeaser(cardItem);
                primaryPriceLabel.setText(labelSpec.getPurchaseOptionPrimaryPriceLabel());
                childPriceText = labelSpec.getPurchaseOptionSecondaryPriceLabel(group);
            }

            // Show starting from label for combo tickets, TODO: PriceTextAbove not present in labelSpecs for combo tickets
            startingFromLabel.setText(IceTicketUtils.getTridionConfig().getStartingFromLabel());
            if (group.isComboGroup()) {
                startingFromLabel.setVisibility(View.VISIBLE);
            } else {
                startingFromLabel.setVisibility(View.GONE);
            }

            if (!group.isAnnualPass()) {
                descriptionText = descriptionText + " " + IceTicketUtils.getTridionConfig().getMoreLabel();
            }
            SpannableString spanString = ClickableTextHelper.createEmbeddedSpannable(descriptionText,
                    IceTicketUtils.getTridionConfig().getMoreLabel(), R.color.ticket_text_color, false, new ClickListener() {
                        @Override
                        public void onLinkClicked() {
                            commerceGroupListener.onMoreClicked(group);
                        }
                    });
            groupDescription.setText(spanString);
            groupDescription.setMovementMethod(LinkMovementMethod.getInstance());

        }

        if (TextUtils.isEmpty(upsellText)) {
            upsellTeaserText.setVisibility(View.GONE);
        } else {
            upsellTeaserText.setVisibility(VISIBLE);
            upsellTeaserText.setText(upsellText);
        }

        if (TextUtils.isEmpty(imgLocation)) {
            upsellImageView.setVisibility(View.GONE);
        } else {
            upsellImageView.setVisibility(VISIBLE);
            Picasso.with(getContext())
                    .load(imgLocation)
                    .into(upsellImageView);
        }

        if (TextUtils.isEmpty(upsellText) && TextUtils.isEmpty(imgLocation)) {
            upsellContainer.setVisibility(View.GONE);
        } else {
            upsellContainer.setVisibility(View.VISIBLE);
        }

        if(isEnabled) {
            adultPrice.setText(adultPriceText);
            childPrice.setText(childPriceText);
            primaryPriceLabel.setVisibility(View.VISIBLE);
            childPrice.setVisibility(View.VISIBLE);
        } else {
            adultPrice.setText(IceTicketUtils.getTridionConfig().getSoldOutLabel());
            primaryPriceLabel.setVisibility(View.GONE);
            childPrice.setVisibility(View.GONE);
        }

        setControlEnabled(isEnabled);
    }

    public void setChecked(boolean checked) {
        if (null == radioButton && isEnabled) {
            return;
        }

        radioButton.setChecked(checked);
    }

    public boolean isChecked() {
        return radioButton.isChecked();
    }

    public void setControlEnabled(boolean enabled) {
        isEnabled = enabled;
        radioButton.setEnabled(isEnabled);
        if(isEnabled) {
            this.setAlpha(1.0f);
        } else {
            this.setAlpha(0.5f);
        }
    }

    public RadioButton getRadioButton() {
        return radioButton;
    }

    public void setOnCheckChangedListener(CompoundButton.OnCheckedChangeListener listener, CommerceGroupListener commerceGroupListener) {
        this.listener = listener;
        this.commerceGroupListener = commerceGroupListener;
    }

    public CommerceGroup getGroup() {
        return group;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (null != listener && isEnabled) {
            listener.onCheckedChanged(buttonView, isChecked);
        }
    }

}
