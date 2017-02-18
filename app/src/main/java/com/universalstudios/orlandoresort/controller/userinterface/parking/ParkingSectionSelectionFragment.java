package com.universalstudios.orlandoresort.controller.userinterface.parking;

import android.content.Context;
import android.content.res.Resources;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;

/**
 * Created by Kevin Haines on 5/18/16.
 * Project: Universal Orlando
 * Class Name: ParkingSectionSelectionFragment
 * Class Description: This class just shows the parking Sections to conform with iOS's layout
 */
public class ParkingSectionSelectionFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "ParkingSectionSelectionFragment";

    private ViewGroup mContainer;

    public static ParkingSectionSelectionFragment newInstance() {
        ParkingSectionSelectionFragment fragment = new ParkingSectionSelectionFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = container;
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_parking_reminder_section_select, container, false);

        Resources res = getResources();

        // Hollywood parking sections
        if (BuildConfigUtils.isLocationFlavorHollywood()) {
            String[] garageSections = res.getStringArray(R.array.parking_reminder_section_garages);
            if (garageSections != null) {
                addSectionHeaderView(layout, getString(R.string.parking_reminder_section_garages));
				for (String section : garageSections) {
					addSectionItemView(layout, section);
				}
			}

            String[] lotSections = res.getStringArray(R.array.parking_reminder_section_lots);
            if (lotSections != null) {
                addSectionHeaderView(layout, getString(R.string.parking_reminder_section_lots));
                for (String section : lotSections) {
                    addSectionItemView(layout, section);
				}
			}
        }
        // Orlando parking sections
        else {
            String[] northSections = res.getStringArray(R.array.parking_reminder_section_north_garage);
            addSectionHeaderView(layout, getString(R.string.parking_reminder_section_north_garage));
            if (northSections != null) {
                for (String section : northSections) {
                    addSectionItemView(layout, section);
                }
            }

            String[] southSections = res.getStringArray(R.array.parking_reminder_section_south_garage);
            addSectionHeaderView(layout, getString(R.string.parking_reminder_section_south_garage));
            if (southSections != null) {
                for (String section : southSections) {
                    addSectionItemView(layout, section);
                }
            }
        }
        return layout;
    }

    /**
     * Adds the parking sections to the appropriate layout
     *
     * @param sectionName Parking section name
     * @param parentViewGroup Layout to add the section to
     */
    private void addSectionItemView(ViewGroup parentViewGroup, String sectionName) {
        if (TextUtils.isEmpty(sectionName)) {
            return;
        }
        Context context = parentViewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_item_parking_section, parentViewGroup, false);
        TextView section = (TextView) itemView.findViewById(R.id.parkingReminderSectionText);
        View topDivider = itemView.findViewById(R.id.parkingSectionListItemDivider);

        // Only show the top divider if there is another item before this section, and it is not a header
        boolean showTopDivider = false;
        if (parentViewGroup.getChildCount() > 0) {
            View lastView = parentViewGroup.getChildAt(parentViewGroup.getChildCount() - 1);
            if (lastView == null || lastView.getId() != R.id.list_item_parking_section_header_root) {
                showTopDivider = true;
            }
        }
        topDivider.setVisibility(showTopDivider ? View.VISIBLE : View.GONE);

        section.setText(sectionName);
        itemView.setClickable(true);
        itemView.setOnClickListener(this);
        itemView.setTag(sectionName);
        parentViewGroup.addView(itemView);
    }

    private static void addSectionHeaderView(ViewGroup parentViewGroup, String sectionHeaderName) {
        if (TextUtils.isEmpty(sectionHeaderName)) {
            return;
        }

        Context context = parentViewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_item_parking_section_header, parentViewGroup, false);
        TextView sectionHeaderText = (TextView) itemView.findViewById(R.id.list_item_parking_section_header_text);

        sectionHeaderText.setText(sectionHeaderName);
        parentViewGroup.addView(itemView);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null && v.getTag() instanceof String) {
            String tag = (String) v.getTag();

            Intent intent = new Intent();
            Bundle data = new Bundle();
            data.putString(ParkingReminderFragment.ARG_SECTION_TITLE, tag);
            intent.putExtras(data);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }
    }
}
