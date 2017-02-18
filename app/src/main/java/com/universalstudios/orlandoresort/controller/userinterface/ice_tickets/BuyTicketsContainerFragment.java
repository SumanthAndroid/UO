package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.addons.AddOnsShoppingFragment;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.ga_tickets.TicketsShoppingFragment;
import com.universalstudios.orlandoresort.databinding.BuyTicketsContainerBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.view.fonts.TextView;
import com.universalstudios.orlandoresort.view.progress.Step;
import com.universalstudios.orlandoresort.view.progress.StepProgressView;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 7/19/16.
 * Class: BuyTicketsContainerFragment
 * Class Description: Fragment that contains the entire ticket purchase flow
 */
public class BuyTicketsContainerFragment extends Fragment implements OnCartSubTotalChangeListener, View.OnClickListener {
    public static final String TAG = BuyTicketsContainerFragment.class.getSimpleName();

    private static final String KEY_ARG_SHOP_ITEM_TYPE = "KEY_ARG_SHOP_ITEM_TYPE";

    private FrameLayout mDetailContainer;
    private StepProgressView mStepProgressView;
    private TextView mSubtotalText;
    private View mCheckoutButton;
    private int mShopItemType;
    private TridionConfig mTridionConfig;

    private ShoppingFragmentInteractionListener mShoppingFragmentInteractionListener;

    public static BuyTicketsContainerFragment newInstance(@ShoppingActivity.ShopItemType int shopItemType) {
        // Create a new fragment instance
        BuyTicketsContainerFragment fragment = new BuyTicketsContainerFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        // Add parameters to the argument bundle
        args.putInt(KEY_ARG_SHOP_ITEM_TYPE, shopItemType);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Default parameters
        Bundle args = getArguments();
        if (args == null) {
            mShopItemType = -1;
        }
        // Otherwise, set incoming parameters
        else {
            mShopItemType = args.getInt(KEY_ARG_SHOP_ITEM_TYPE);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {
        }

        mTridionConfig = IceTicketUtils.getTridionConfig();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BuyTicketsContainerBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.buy_tickets_container, container, false);
        binding.setTridion(mTridionConfig);
        View layout = binding.getRoot();
        mDetailContainer = binding.ticketsDetailContainer;
        mSubtotalText = binding.tvSubtotal;
        mStepProgressView = binding.buyTicketsStepViewProgress;
        mCheckoutButton = binding.buyTicketsCheckoutButton;

        // Setup the progress step view, combo tickets will not show the progress view
        if (mShopItemType == ShoppingActivity.SELECT_TICKET_BMG_BUNDLE
                || mShopItemType == ShoppingActivity.SELECT_TICKET_UEP_BUNDLE) {
            mStepProgressView.setVisibility(View.GONE);
        } else {
            mStepProgressView.setVisibility(View.VISIBLE);
            mStepProgressView.setSteps(getTicketBuyingSteps(), Integer.toString(mShopItemType));
        }

        mCheckoutButton.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // If this is the first creation, add the child fragment
        if (savedInstanceState == null) {
            //noinspection WrongConstant this is the proper int type
            Fragment fragment;
            switch (mShopItemType) {
                case ShoppingActivity.SELECT_TICKETS:
                case ShoppingActivity.SELECT_EXPRESS_PASS:

                // TODO The following two cases should direct the user to the bundle fragment
                case ShoppingActivity.SELECT_TICKET_BMG_BUNDLE:
                case ShoppingActivity.SELECT_TICKET_UEP_BUNDLE:
                    fragment = TicketsShoppingFragment.newInstance(mShopItemType);
                    break;
                case ShoppingActivity.SELECT_ADDONS:
                    fragment = AddOnsShoppingFragment.newInstance();
                    break;
                default:
                    throw new RuntimeException("Invalid shopping type passed into ShoppingActivity");
            }
            getChildFragmentManager().beginTransaction()
                    .replace(mDetailContainer.getId(), fragment)
                    .commit();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Check if parent fragment (if there is one) implements the interface
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof ShoppingFragmentInteractionListener) {
            mShoppingFragmentInteractionListener = (ShoppingFragmentInteractionListener) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (context != null && context instanceof ShoppingFragmentInteractionListener) {
            mShoppingFragmentInteractionListener = (ShoppingFragmentInteractionListener) getActivity();
        }
        // If neither implements the interface, log a warning
        else if (mShoppingFragmentInteractionListener == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement ShoppingFragmentInteractionListener");
            }
            throw new RuntimeException(context.toString()
                    + " must implement ShoppingFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mShoppingFragmentInteractionListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh cart subtotal in case it changed
        refreshCartSubTotal();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == mCheckoutButton.getId()) {
            if (null != mShoppingFragmentInteractionListener) {
                mShoppingFragmentInteractionListener.onCheckoutClicked();
            }
        }
    }

    @Override
    public void onCartSubTotalChange() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshCartSubTotal();
            }
        });
    }

    private List<Step> getTicketBuyingSteps() {
        TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();
        ArrayList<Step> steps = new ArrayList<>();
        steps.add(new Step(tridionConfig.getTicketsLabel(), Integer.toString(ShoppingActivity.SELECT_TICKETS)));
        steps.add(new Step(tridionConfig.getExpressPassLabel(), Integer.toString(ShoppingActivity.SELECT_EXPRESS_PASS)));
        steps.add(new Step(tridionConfig.getExtrasLabel(), Integer.toString(ShoppingActivity.SELECT_ADDONS)));
        // Checkout can't actually be navigated to
        steps.add(new Step(tridionConfig.getCheckOutLabel(), Integer.toString(ShoppingActivity.SELECT_ADDONS + 1)));
        return steps;
    }

    private void refreshCartSubTotal() {
        // Refresh the cart badge
        mSubtotalText.setText(CommerceUiBuilder.getCartSubTotalFormatted());
    }

}
