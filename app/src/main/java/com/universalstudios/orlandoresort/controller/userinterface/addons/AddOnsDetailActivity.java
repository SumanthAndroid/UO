package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarActivity;
import com.universalstudios.orlandoresort.controller.userinterface.addons.items.AddOnsShoppingProductItem;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.misc.RoadblockActivity;
import com.universalstudios.orlandoresort.databinding.ActivityAddOnsDetailBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;

import org.parceler.Parcels;

public class AddOnsDetailActivity extends ActionBarActivity {

    private static final String ARG_ADDONS_PRODUCT_ITEM = "ADDONS_PRODUCT_ITEM";

    AddOnsDetailsActionCallback mActionCallback = new AddOnsDetailsActionCallback() {
        @Override
        public void onSelectClicked(AddOnsShoppingProductItem extrasProductItem) {
            startActivity(new Intent(AddOnsDetailActivity.this, RoadblockActivity.class));
        }
    };
    private TridionConfig mTridionConfig;
    private ActivityAddOnsDetailBinding mBinding;
    private AddOnsShoppingProductItem mAddOnsShoppingProductItem;

    public static Intent newInstanceIntent(Context context, AddOnsShoppingProductItem addOnsShoppingProductItem) {
        Intent intent = new Intent(context, AddOnsDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_ADDONS_PRODUCT_ITEM, Parcels.wrap(addOnsShoppingProductItem));
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("");
        getActionBar().setDisplayShowHomeEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            getActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }

        // Default parameters
        Bundle args = getIntent().getExtras();
        if (args == null) {
            mAddOnsShoppingProductItem = null;
        }
        // Otherwise, set incoming parameters
        else {
            mAddOnsShoppingProductItem = Parcels.unwrap(args.getParcelable(ARG_ADDONS_PRODUCT_ITEM));
        }

        mTridionConfig = IceTicketUtils.getTridionConfig();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_ons_detail);
        mBinding.setTridion(mTridionConfig);
        mBinding.setData(mAddOnsShoppingProductItem);
        mBinding.setCallback(mActionCallback);

    }

    /**
     * Callback interface that will automagically bind the callbacks to the activity to leverage
     * using data binding.
     */
    public interface AddOnsDetailsActionCallback {
        void onSelectClicked(AddOnsShoppingProductItem extrasProductItem);
    }


}
