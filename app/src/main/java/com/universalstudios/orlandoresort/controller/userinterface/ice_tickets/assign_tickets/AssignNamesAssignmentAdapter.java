package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets;

import android.util.Log;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.view.binding.BaseObservableWithLayoutItem;
import com.universalstudios.orlandoresort.view.binding.DataBoundViewHolder;
import com.universalstudios.orlandoresort.view.binding.MultiTypeDataBoundAdapter;

import java.util.List;

/**
 * Assignment adapter
 *
 * @author acampbell
 */
public class AssignNamesAssignmentAdapter extends MultiTypeDataBoundAdapter {

    private static final String TAG = AssignNamesAssignmentAdapter.class.getSimpleName();

    private TridionConfig tridionConfig;
    private AssignNamesAssignmentGuestInfoItem guestInfoItem;
    private AssignNamesAssignmentFooterItem footerItem;
    private AssignNamesAssignmentActionCallback callback;

    public AssignNamesAssignmentAdapter(TridionConfig tridionConfig, AssignNamesAssignmentActionCallback callback) {
        this.tridionConfig = tridionConfig;
        this.callback = callback;
    }

    @Override
    public int getItemLayoutId(int position) {
        Object item = getItem(position);
        if (item instanceof BaseObservableWithLayoutItem) {
            return ((BaseObservableWithLayoutItem) item).getLayoutId();
        }
        throw new IllegalArgumentException("unknown item type " + item);
    }

    @Override
    protected void bindItem(DataBoundViewHolder holder, int position, List payloads) {
        super.bindItem(holder, position, payloads);
        holder.binding.setVariable(BR.tridion, tridionConfig);
        holder.binding.setVariable(BR.callback, callback);
    }

    public void setGuestInfoItem(PartyMember guest) {
        if (guestInfoItem == null) {
            guestInfoItem = new AssignNamesAssignmentGuestInfoItem(guest);
            addItem(guestInfoItem);
        } else {
            guestInfoItem.setGuest(guest);
        }
    }

    public void setGuestInfoItemChecked(Boolean state) {
        if (guestInfoItem != null) {
            guestInfoItem.setChecked(state);
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "setGuestInfoItemChecked: guestInfoItem is null");
            }
        }
    }

    public void setShowGuestInfoNameEntry(boolean state) {
        if (guestInfoItem != null) {
            guestInfoItem.setShowNameEntry(state);
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "setShowGuestInfoEntryForm: guestInfoItem is null");
            }
        }
    }

    public void setShowGuestInfoSpinner(boolean state) {
        if (guestInfoItem != null) {
            guestInfoItem.setShowSpinner(state);
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "setShowGuestInfoSpinner: guestInfoItem is null");
            }
        }
    }

    public void setShowGuestInfoEntryForm(boolean state) {
        if (guestInfoItem != null) {
            guestInfoItem.setShowEntryForm(state);
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "setShowGuestInfoEntryForm: guestInfoItem is null");
            }
        }
    }

    public String getFirstName() {
        if (guestInfoItem != null) {
            return guestInfoItem.getFirstName();
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "getFirstName: guestInfoItem is null");
            }
        }
        return null;
    }

    public String getLastName() {
        if (guestInfoItem != null) {
            return guestInfoItem.getLastName();
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "getLastName: guestInfoItem is null");
            }
        }
        return null;
    }

    public String getSuffix() {
        if (guestInfoItem != null) {
            return guestInfoItem.getSuffix();
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "getSuffix: guestInfoItem is null");
            }
        }
        return null;
    }

    public void setGuestInfoSpinnerAdapter(AssignNamesAdapter assignNamesAdapter) {
        if (guestInfoItem != null) {
            guestInfoItem.setAssignNamesAdapter(assignNamesAdapter);
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "setGuestInfoSpinnerAdapter: guestInfoItem is null");
            }
        }
    }

    public void setShowGuestInfoSpinnerArrow(boolean state) {
        if (guestInfoItem != null) {
            guestInfoItem.setShowSpinnerArrow(state);
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "setShowGuestInfoSpinnerArrow: guestInfoItem is null");
            }
        }
    }

    public void setFooterItem(boolean state) {
        if (footerItem == null) {
            footerItem = new AssignNamesAssignmentFooterItem(state);
            addItem(footerItem);
        } else {
            footerItem.setDoneEnabled(state);
        }
    }

    public void addAssignableItems(List<AssignableTicketItem> assignableTicketItems) {
        if (assignableTicketItems != null) {
            for (AssignableTicketItem assignableTicketItem : assignableTicketItems) {
                if (assignableTicketItem != null) {
                    addItem(assignableTicketItem);
                }
            }
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "addAssignableItems: assignableTicketItems is null");
            }
        }
    }

    /**
     * Callbacks that need to be implemented to use {@link AssignNamesAssignmentAdapter}.
     */
    public interface AssignNamesAssignmentActionCallback {
        void onDoneClicked();

        void onSpinnerItemSelected(int position);

        void onTextChanged();

        void onCheckedChanged(boolean isChecked);

        void onBirthDateInfoIconClicked();

        void onTicketSelected(AssignableTicketItem assignableTicketItem);

        void onTicketChecked(AssignableTicketItem assignableTicketItem, boolean isChecked);

        void onDatePickerClicked(AssignableTicketItem assignableTicketItem);
    }
}
