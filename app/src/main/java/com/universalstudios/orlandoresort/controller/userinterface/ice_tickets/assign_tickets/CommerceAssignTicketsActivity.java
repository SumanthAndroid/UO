package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarActivity;

import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/10/16.
 * Class: CommerceAssignTicketsActivity
 * Class Description: Activity to hold the Fragment for Assigning names to tickets
 */
public class CommerceAssignTicketsActivity extends ActionBarActivity implements CommerceAssignmentChangeListener, CommerceAssignmentFragment.OnTicketAssignedListener {
    private static final String TAG = CommerceAssignTicketsActivity.class.getSimpleName();

    public MenuItem mAddGuestMenuItem;

    public boolean showAddGuestItem = true;

    public static Intent createIntent(Context context) {
        return new Intent(context, CommerceAssignTicketsActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //will bypass snapshot to hide CC number
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_assign_ticket_name);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
        }
        // Otherwise, set incoming parameters
        else {

        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            Fragment fragment = CommerceAssignTicketsFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.assign_ticket_name_container, fragment)
                    .commit();
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {
        }
    }

    public void showNextFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.assign_ticket_name_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.assign_ticket_name_container);
                if (null != currentFragment && currentFragment instanceof CommerceAssignTicketsFragment) {
                    finish();
                } else {
                    super.onBackPressed();
                }
                return true;
            case R.id.add_guest:
                List<AssignableTicketItem> adultOrderItems = TicketAssignmentUtils.instance().getUnassignedAdultTickets();
                int ageType = PartyMember.AGE_TYPE_ADULT;
                if (null != adultOrderItems && !adultOrderItems.isEmpty()) {
                    ageType = PartyMember.AGE_TYPE_ADULT;
                } else {
                    List<AssignableTicketItem> childOrderItems = TicketAssignmentUtils.instance().getUnassignedChildTickets();
                    if (null != childOrderItems && !childOrderItems.isEmpty()) {
                        ageType = PartyMember.AGE_TYPE_CHILD;
                    }
                }
                showNextFragment(CommerceAssignmentFragment.newInstance(new PartyMember(ageType)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateOptionsMenu");
        }

        // Adds items to the action bar
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_guest, menu);
        mAddGuestMenuItem = menu.findItem(R.id.add_guest);
        if (null != mAddGuestMenuItem) {
            if (showAddGuestItem) {
                List<AssignableTicketItem> items = TicketAssignmentUtils.instance().getUnassignedAdultTickets();
                items.addAll(TicketAssignmentUtils.instance().getUnassignedChildTickets());
                items.addAll(TicketAssignmentUtils.instance().getUnassignedUEPTickets());
                showAddGuestItem = !items.isEmpty();
            }
            mAddGuestMenuItem.setVisible(showAddGuestItem);
        }
        return true;
    }

    @Override
    public void onShowAddGuestChanged(boolean show) {
        showAddGuestItem = show;
        invalidateOptionsMenu();
    }

    @Override
    public void onTicketAssigned() {
        Log.d(TAG, "onTicketAssigned() called");

        //this is called from onLoadFinished, so needs to be wrapped in a handler/runnable
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager().popBackStack();
            }
        });
    }

}
