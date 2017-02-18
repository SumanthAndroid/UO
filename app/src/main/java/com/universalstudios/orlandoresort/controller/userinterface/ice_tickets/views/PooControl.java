package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.CommerceUiBuilder;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.AndroidUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.UIControlField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/30/16.
 * Class: PooControl
 * Class Description: Control for Fl Resident
 */
public class PooControl extends LinearLayout implements CompoundButton.OnCheckedChangeListener {
    public static final String TAG = "PooControl";

    private SwitchCompat switchCompat;
    public enum CheckedState {
        CHECKED,
        UNCHECKED,
        USE_DEFAULT
    }

    private CheckedState checkedState = CheckedState.USE_DEFAULT;

    private Map<Boolean, String> identifierMap = new HashMap<>();

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    public PooControl(Context context) {
        super(context);
    }

    public PooControl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PooControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    public void setChecked(CheckedState state) {
        this.checkedState = state;
    }

    public void setSwitchChecked(boolean isChecked){
        switchCompat.setChecked(isChecked);
    }

    public CheckedState getCheckedState() {
        return checkedState;
    }

    public void setUIControlFields(List<UIControlField> fields) {
        if (null == fields || fields.isEmpty()) {
            return;
        }
        this.setOrientation(VERTICAL);

        final TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();

        boolean defaultChecked = false;

        for (UIControlField field : fields) {
            identifierMap.put(field.getDisplayValue().equalsIgnoreCase("Yes"), field.getServiceIdentifier());
            if (field.getDisplayValue().equalsIgnoreCase("Yes") && field.isDefault()) {
                defaultChecked = true;
            }
        }

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(HORIZONTAL);
        LayoutParams linearLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(0, (int) AndroidUtils.convertDpToPixel(10, getContext()), 0, 0);
        linearLayout.setLayoutParams(linearLayoutParams);

        // Layout containing TextView and info icon
        LinearLayout numDaysLayout = (LinearLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.florida_resident_info_label, null);
        TextView tvFloridaLabel = (TextView) numDaysLayout.findViewById(R.id.filter_florida_switch_label);
        tvFloridaLabel.setText(tridionConfig.getFloridaResidentLabel());

        // Adjust the number of days header info icon within the map legend
        ImageView ivFloridaInfo = (ImageView) numDaysLayout.findViewById(R.id.filter_florida_info_popup);
        ivFloridaInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), tridionConfig.getBuyTicketsFilterFLPopUp(), Toast.LENGTH_LONG)
                        .show();
            }
        });

        switchCompat = new SwitchCompat(getContext());
        switchCompat.setOnCheckedChangeListener(this);

        if (checkedState == CheckedState.CHECKED) {
            switchCompat.setChecked(true);
        } else if (checkedState == CheckedState.UNCHECKED) {
            switchCompat.setChecked(false);
        } else {
            switchCompat.setChecked(defaultChecked);
            CommerceUiBuilder.getCurrentFilter().isFloridaResident = defaultChecked;
        }

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.END;
        layoutParams.weight = 1;
        switchCompat.setLayoutParams(layoutParams);

        //Add views
        linearLayout.addView(numDaysLayout);
        linearLayout.addView(switchCompat);

        TextView tvBlackoutText = new TextView(getContext());
        tvBlackoutText.setTextColor(getContext().getResources().getColor(R.color.text_gray_light));
        tvBlackoutText.setText(tridionConfig.getFloridaResidentBlockoutDates());

        this.addView(linearLayout);
        this.addView(tvBlackoutText);
    }

    public String getIdentifier() {
        return identifierMap.get(switchCompat.isChecked());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (null != onCheckedChangeListener) {
            onCheckedChangeListener.onCheckedChanged(buttonView, isChecked);
        }
    }
}
