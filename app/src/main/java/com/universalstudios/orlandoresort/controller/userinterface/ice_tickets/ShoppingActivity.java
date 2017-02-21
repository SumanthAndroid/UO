package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.addons.AddOnDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.addons.AddOnsWizardActivity;
import com.universalstudios.orlandoresort.controller.userinterface.addons.items.AddOnsShoppingProductItem;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.PersonalizationExtrasProduct;
import com.universalstudios.orlandoresort.view.custom_calendar.TicketType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ShoppingActivity extends NetworkRefreshActivity implements OnCartCountChangeListener, ShoppingFragmentInteractionListener {
    private static final String TAG = ShoppingActivity.class.getSimpleName();

    private static final String KEY_ARG_SHOP_ITEM_TYPE = "KEY_ARG_SHOP_ITEM_TYPE";
    private static final String KEY_ARG_SHOP_HHN_THEME = "KEY_ARG_SHOP_ITEM_HHN";
    private static final int REQUEST_CODE_ADD_ONS = 101;

    public static TicketType convertShopItemTypeToTicketType(@ShopItemType int shopItemType) {
        switch (shopItemType) {
            case SELECT_TICKETS:
                return TicketType.TYPE_TICKETS;
            case SELECT_TICKET_BMG_BUNDLE:
                return TicketType.TYPE_TICKET_BMG_BUNDLE;
            case SELECT_TICKET_UEP_BUNDLE:
                return TicketType.TYPE_TICKET_UEP_BUNDLE;
            case SELECT_EXPRESS_PASS:
                return TicketType.TYPE_EXPRESS;
            case SELECT_ADDONS:
                return TicketType.TYPE_ADDONS;
        }
        return TicketType.TYPE_TICKETS;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SELECT_TICKETS, SELECT_EXPRESS_PASS, SELECT_ADDONS, SELECT_TICKET_BMG_BUNDLE, SELECT_TICKET_UEP_BUNDLE})
    public @interface ShopItemType {
    }

    public static final int SELECT_TICKETS = 0;
    public static final int SELECT_EXPRESS_PASS = 1;
    public static final int SELECT_ADDONS = 2;
    public static final int SELECT_TICKET_BMG_BUNDLE = 3;
    public static final int SELECT_TICKET_UEP_BUNDLE = 4;

    private MenuItem mCartMenuItem;
    private TextView mBadgeCountTextView;
    private int mShopItemType;
    private TridionConfig mTridionConfig;

    public static Intent newInstanceIntent(Context context, @ShopItemType int shopItemType) {
        // Create a new bundle and put in the args
        Bundle args = new Bundle();
        args.putInt(KEY_ARG_SHOP_ITEM_TYPE, shopItemType);

        // Attach the arguments to the intent
        Intent intent = new Intent(context, ShoppingActivity.class);
        intent.putExtras(args);
        return intent;
    }

    public static Intent newInstanceIntent(Context context, @ShopItemType int shopItemType, boolean isHollyHorrorNights) {
        // Create a new bundle and put in the args
        Bundle args = new Bundle();
        args.putInt(KEY_ARG_SHOP_ITEM_TYPE, shopItemType);
        args.putBoolean(KEY_ARG_SHOP_HHN_THEME, isHollyHorrorNights);

        // Attach the arguments to the intent
        Intent intent = new Intent(context, ShoppingActivity.class);
        intent.putExtras(args);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getIntent().getExtras();
        if (args != null && args.getBoolean(KEY_ARG_SHOP_HHN_THEME, false)) {
            setTheme(R.style.HollywoodHorrorNightsTheme);
        }else{
            setTheme(R.style.Default);
        }
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        setContentView(R.layout.activity_shopping);

        // Default parameters

        if (args == null) {
            mShopItemType = SELECT_TICKETS;
        } else {
            mShopItemType = args.getInt(KEY_ARG_SHOP_ITEM_TYPE);
            if (args.getBoolean(KEY_ARG_SHOP_HHN_THEME, false)) {
                if (getActionBar() != null) {
                    getActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                }
            }
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {

            //noinspection WrongConstant - This is using a proper shop item type value
            BuyTicketsContainerFragment fragment = BuyTicketsContainerFragment.newInstance(mShopItemType);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_shopping_container, fragment)
                    .commit();
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {
        }

        if (null != getActionBar()) {
            getActionBar().setDisplayShowHomeEnabled(false);

        }

        mTridionConfig = IceTicketUtils.getTridionConfig();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStart");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onResume");
        }

        // Refresh cart count in case it changed
        refreshCartCount();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onPause");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onSaveInstanceState");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStop");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onDestroy");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ADD_ONS:
                if (RESULT_OK == resultCode) {
                    Toast.makeText(this, mTridionConfig.getTicketingConfirmationText(), android.widget.Toast.LENGTH_SHORT)
                            .setIcon(R.drawable.ic_cart)
                            .setIconColor(getResources().getColor(R.color.blue_color))
                            .setTextBold(true)
                            .show();
                    refreshCartCount();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateOptionsMenu");
        }

        // Adds items to the action bar
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_shopping, menu);

        mCartMenuItem = menu.findItem(R.id.action_shopping);

        // Set the custom view on the cart icon and forward clicks
        MenuItemCompat.setActionView(mCartMenuItem, R.layout.cart_badge_layout);
        MenuItemCompat.getActionView(mCartMenuItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(mCartMenuItem);
            }
        });

        RelativeLayout notifyCount = (RelativeLayout) MenuItemCompat.getActionView(mCartMenuItem);
        mBadgeCountTextView = (TextView) notifyCount.findViewById(R.id.actionbar_notifcation_textview);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onPrepareOptionsMenu");
        }

        // Update the cart badge
        refreshCartCount();

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onOptionsItemSelected");
        }

        // Handle other action bar items
        switch (item.getItemId()) {
            case R.id.action_shopping:
                startActivity(ShoppingCartActivity.newInstanceIntent(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCartCountChange() {
        refreshCartCount();
        invalidateOptionsMenu();
    }

    private void refreshCartCount() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "refreshCartCount: " + CommerceUiBuilder.getCartCount());
        }

        // Refresh the cart badge
        if (mBadgeCountTextView != null) {
            mBadgeCountTextView.setText(Integer.toString(CommerceUiBuilder.getCartCount()));
        }

        //invalidateOptionsMenu();

    }

    @Override
    public void onCheckoutClicked() {
        startActivity(ShoppingCartActivity.newInstanceIntent(this));
    }

    @Override
    public void onContinueClicked() {
        switch (mShopItemType) {
            case SELECT_TICKETS:
                startActivity(ShoppingActivity.newInstanceIntent(this, SELECT_EXPRESS_PASS));
                break;
            case SELECT_EXPRESS_PASS:
                startActivity(ShoppingActivity.newInstanceIntent(this, SELECT_ADDONS));
                break;
            case SELECT_ADDONS:
                startActivity(ShoppingCartActivity.newInstanceIntent(this));
                break;
            case SELECT_TICKET_BMG_BUNDLE:
                startActivity(ShoppingCartActivity.newInstanceIntent(this));
                break;
            case SELECT_TICKET_UEP_BUNDLE:
                startActivity(ShoppingCartActivity.newInstanceIntent(this));
                break;
        }
    }

    @Override
    public void onBackClicked() {
        if (!isFinishing()) {
            finish();
        }
    }

    @Override
    public void onSelectAddOnClicked(AddOnsShoppingProductItem extrasProductItem) {
        startActivityForResult(AddOnsWizardActivity.newInstanceIntent(this, extrasProductItem.getExtrasProduct()), REQUEST_CODE_ADD_ONS);
    }

    @Override
    public void onSeeAddOnDetailsClicked(AddOnsShoppingProductItem extrasProductItem) {
        if (extrasProductItem.getLabelSpecTcmId1() != null) {
            startActivity(AddOnDetailActivity.newInstanceIntent(this, mTridionConfig.getAddOnDetailsLabel(),
                    extrasProductItem.getLabelSpecTcmId1().getDetails()));
        }
    }

    @Override
    public void onSelectComboTicketClicked(PersonalizationExtrasProduct extrasProduct) {
        if (extrasProduct != null) {
            TicketFilterInfo ticketFilterInfo = CommerceUiBuilder.getCurrentFilter();
            Intent intent = AddOnsWizardActivity.newInstanceIntent(this, extrasProduct,
                    ticketFilterInfo.getNumberOfAdultTickets(), ticketFilterInfo.getNumberOfChildTickets());
            startActivityForResult(intent, REQUEST_CODE_ADD_ONS);
        }
    }
}
