package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets;

import android.databinding.Bindable;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.view.binding.BaseObservableWithLayoutItem;

/**
 * Assignment guest info item
 *
 * @author acampbell
 */
public class AssignNamesAssignmentGuestInfoItem extends BaseObservableWithLayoutItem {


    @Bindable
    private Boolean checked;
    @Bindable
    private String firstName;
    @Bindable
    private String lastName;
    @Bindable
    private String suffix;
    @Bindable
    private AssignNamesAdapter assignNamesAdapter;
    @Bindable
    private boolean showNameEntry;
    @Bindable
    private boolean showSpinner;
    @Bindable
    private boolean showEntryForm;
    @Bindable
    private boolean showSpinnerArrow;

    public AssignNamesAssignmentGuestInfoItem(PartyMember guest) {
        setGuest(guest);
    }

    @Override
    public int getLayoutId() {
        return R.layout.commerce_assignment_fragment_guest_info_item;
    }

    public void setGuest(PartyMember guest) {
        if (guest != null) {
            setFirstName(guest.firstname);
            setLastName(guest.lastname);
            setSuffix(guest.suffix);
        } else {
            setFirstName("");
            setLastName("");
            setSuffix("");
        }
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
        notifyPropertyChanged(BR.checked);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        notifyPropertyChanged(BR.suffix);
    }

    public AssignNamesAdapter getAssignNamesAdapter() {
        return assignNamesAdapter;
    }

    public void setAssignNamesAdapter(AssignNamesAdapter assignNamesAdapter) {
        this.assignNamesAdapter = assignNamesAdapter;
        notifyPropertyChanged(BR.assignNamesAdapter);
    }

    public boolean isShowNameEntry() {
        return showNameEntry;
    }

    public void setShowNameEntry(boolean showNameEntry) {
        this.showNameEntry = showNameEntry;
        notifyPropertyChanged(BR.showNameEntry);
    }

    public boolean isShowSpinner() {
        return showSpinner;
    }

    public void setShowSpinner(boolean showSpinner) {
        this.showSpinner = showSpinner;
        notifyPropertyChanged(BR.showSpinner);
    }

    public boolean isShowEntryForm() {
        return showEntryForm;
    }

    public void setShowEntryForm(boolean showEntryForm) {
        this.showEntryForm = showEntryForm;
        notifyPropertyChanged(BR.showEntryForm);
    }

    public boolean isShowSpinnerArrow() {
        return showSpinnerArrow;
    }

    public void setShowSpinnerArrow(boolean showSpinnerArrow) {
        this.showSpinnerArrow = showSpinnerArrow;
        notifyPropertyChanged(BR.showSpinnerArrow);
    }
}
