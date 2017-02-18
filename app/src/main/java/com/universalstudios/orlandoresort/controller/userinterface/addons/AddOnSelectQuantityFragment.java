package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets.TicketAssignmentUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views.AddOnQuantityControl;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views.OnCountsChangedListener;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupOrder;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;
import com.universalstudios.orlandoresort.view.TicketCounterView;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;


public class AddOnSelectQuantityFragment extends AddOnsFragment implements View.OnClickListener, OnCountsChangedListener, AddOnSelectSkuAdapter.ItemListener {
    private static final String TAG = AddOnSelectQuantityFragment.class.getSimpleName();

    private static final String TICKET_TYPE_ADULT = "Adult";
    private static final String TICKET_TYPE_CHILD = "Child";
    private static final int QUANTITY_DEFAULT_ADULT = 2;
    private static final int QUANTITY_DEFAULT_CHILD = 0;
    private static final int QUANTITY_DEFAULT_ALL_AGES = 2;
    private static final int MIN_QUANTITY_DEFAULT = 1;
    private static final int COLUMNS_SKU_SELECTOR = 2;

    private AddOnQuantityControl mSelectorContainer;
    private RecyclerView mRecyclerView;
    private TridionConfig mTridionConfig;
    private TextView mSelectSku;

    public static AddOnSelectQuantityFragment newInstance() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance");
        }

        // Create a new fragment instance
        AddOnSelectQuantityFragment fragment = new AddOnSelectQuantityFragment();
        // Get arguments passed in, if any
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAttach");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate");
        }

        Bundle args = getArguments();
        if (args == null) {
        }
        // Otherwise, set incoming parameters
        else {
        }

        // If this is the first creation, default state variables
        if (savedInstanceState != null) {
        } else {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView");
        }

        View view = inflater.inflate(R.layout.fragment_add_on_select_quantity, container, false);
        TextView mTitle = (TextView) view.findViewById(R.id.fragment_add_on_select_quantity_title);
        TextView mDescription = (TextView) view.findViewById(R.id.fragment_add_on_select_quantity_description);
        TextView mSeeDetails = (TextView) view.findViewById(R.id.fragment_add_on_select_quantity_see_details);
        TextView mSelectQuantity = (TextView) view.findViewById(R.id.fragment_add_on_select_quantity_select_quantity);
        mSelectSku = (TextView) view.findViewById(R.id.fragment_add_on_select_quantity_select_sku);
        mSelectorContainer = (AddOnQuantityControl) view.findViewById(R.id.fragment_add_on_select_quantity_list);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_add_on_select_quantity_sku_list);
        mRecyclerView.setItemAnimator(null);

        mTridionConfig = IceTicketUtils.getTridionConfig();

        mSeeDetails.setText(mTridionConfig.getSeeDetailsLabel());
        if (mCallbacks != null) {
            AddOnsState state = mCallbacks.getAddOnState();
            TridionLabelSpec productSpec = state.getProductTridionSpec();
            if (productSpec != null) {
                mTitle.setText(productSpec.getTitle());
                mSelectQuantity.setText(productSpec.getQuantitySelectorHeading());
                mDescription.setText(Html.fromHtml(productSpec.getAboutSectionSummary()));
                mSelectSku.setText(productSpec.getSkuSelectorHeading());
            }
        }

        mSeeDetails.setOnClickListener(this);

        addSkuSelectors();
        addQuantitySelectors();
        //update subtotal after creating selectors
        mCallbacks.updateSubtotal();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewCreated");
        }

        mCallbacks.buttonEnabled(isValidState());
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
    public void onDestroyView() {
        super.onDestroyView();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onDestroyView");
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
    public void onDetach() {
        super.onDetach();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onDetach");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_add_on_select_quantity_see_details:
                if (mCallbacks != null) {
                    AddOnsState state = mCallbacks.getAddOnState();
                    TridionLabelSpec productSpec = state.getProductTridionSpec();
                    if (productSpec != null) {
                        startActivity(AddOnDetailActivity.newInstanceIntent(getContext(), getString(R.string.action_title_add_on_details),
                                productSpec.getTitle(), productSpec.getDetails()));
                    }
                }
                break;
            default:
                break;
        }
    }

    private void addSkuSelectors() {
        if (mCallbacks != null) {
            AddOnsState state = mCallbacks.getAddOnState();

            if (state.getPersonalizationExtrasProduct() != null) {
                List<String> items = state.getPersonalizationExtrasProduct().getSkuSelectors();
                String definingAttribute = state.getPersonalizationExtrasProduct().getSkuDefiningAttribute();
                if (!items.isEmpty() && definingAttribute != null) {
                    state.setSkuAttribute(definingAttribute);
                    mSelectSku.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), COLUMNS_SKU_SELECTOR));
                    int spacing = getResources().getDimensionPixelSize(R.dimen.extras_select_sku_grid_spacing);
                    mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(COLUMNS_SKU_SELECTOR, spacing, false));
                    String selectedDisplayValue = null;
                    if (state.getSelectedSku() != null) {
                        selectedDisplayValue = state.getPersonalizationExtrasProduct().getSkuDisplayForAttributeValue(definingAttribute, state.getSelectedSku());
                    }
                    AddOnSelectSkuAdapter adapter = new AddOnSelectSkuAdapter(items, this, selectedDisplayValue);
                    mRecyclerView.setAdapter(adapter);
                }
            }
        }
    }

    private void addQuantitySelectors() {
        AddOnsState state = null;
        if (mCallbacks != null) {
            state = mCallbacks.getAddOnState();
        }

        if (state == null || state.getPersonalizationExtrasProduct() == null) {
            return;
        }

        for (String key : state.getAgePartNumberMap().keySet()) {
            int currentValue;
            int minValue;
            String label;
            String unit;

            TicketGroupOrder order = TicketAssignmentUtils.instance().getTicketGroupOrder();
            if (TICKET_TYPE_ADULT.equalsIgnoreCase(key)) {
                label = state.getProductTridionSpec().getQuantitySelectorBelowLabel1();
                unit = state.getProductTridionSpec().getPriceTextBelowPrimary();
                setStartingQuantity(key, state, order);
                minValue = getMinQuanitity(key, state);
            } else if (TICKET_TYPE_CHILD.equalsIgnoreCase(key)) {
                label = state.getProductTridionSpec().getQuantitySelectorBelowLabel2();
                unit = state.getProductTridionSpec().getPriceTextBelowSecondary();
                setStartingQuantity(key, state, order);
                minValue = getMinQuanitity(key, state);
            } else {
                label = state.getProductTridionSpec().getQuantitySelectorBelowLabel1();
                unit = state.getProductTridionSpec().getPriceTextBelowPrimary();
                setStartingQuantity(key, state, order);
                minValue = getMinQuanitity(key, state);
            }

            currentValue = state.getAgeQuantityMap().get(key);
            String abovePrice = state.getProductTridionSpec().getPriceTextAbove();
            BigDecimal minPriceByAge = state.getMinPriceByAge(key);
            int maxQuantity = getMaxQuantity(state);
            addSelectorContainer(key, label, currentValue, minValue, maxQuantity, minPriceByAge, unit, abovePrice);
        }

        mSelectorContainer.setOnCountChangedListener(this);
    }

    protected void addSelectorContainer(final String key, final String displayName, final int currentValue, final int min, final int max, final BigDecimal price, final String unit, final String abovePrice) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSelectorContainer.addField(key, displayName, unit, abovePrice, currentValue, price, min, max);
            }
        });
    }

    private void updateQuanityContainersPrice() {
        if (mCallbacks != null && mSelectorContainer != null) {
            AddOnsState state = mCallbacks.getAddOnState();
            for (String key : state.getAgeQuantityMap().keySet()) {
                mSelectorContainer.updatePrice(key, state.getMinPriceByAge(key));
                // Hide starting from label when price changes
                mSelectorContainer.toggleStartingFromLabel(key, false);
            }
        }
    }


    @Override
    public void onCountChanged(List<TicketCounterView> counterViews, TicketCounterView viewChanged, int oldValue, int newValue, int totalOfAllViews) {
        HashMap<String, Integer> ageQuantityMap = new HashMap<>();
        for (TicketCounterView counterView : counterViews) {
            String tag = (String) counterView.getTag();
            ageQuantityMap.put(tag, counterView.getCurrentValue());
        }

        if (mCallbacks != null) {
            mCallbacks.buttonEnabled(isValidState());

            AddOnsState state = mCallbacks.getAddOnState();
            state.setAgeQuantityMap(ageQuantityMap);
            state.setSelectedDate(null);

            mCallbacks.updateSubtotal();
            // Update ticket counter minimum quantities
            for(TicketCounterView counterView : counterViews) {
                String tag = (String) counterView.getTag();
                counterView.setMinimumValue(getMinQuanitity(tag, mCallbacks.getAddOnState()));
            }
        }
    }

    @Override
    public void onItemClick(String item) {
        if (mCallbacks != null) {
            AddOnsState state = mCallbacks.getAddOnState();
            if (state.getPersonalizationExtrasProduct() != null && !TextUtils.isEmpty(state.getSkuAttribute())
                    && item != null) {
                // Set selected sku option
                String attributeValue = state.getPersonalizationExtrasProduct().getAttributeValueForSkuDisplay(state.getSkuAttribute(), item);
                state.setSelectedSku(attributeValue);
                updateQuanityContainersPrice();
                mCallbacks.buttonEnabled(isValidState());
                mCallbacks.updateSubtotal();
            }
        }
    }

    /**
     * Check whether state is valid based on current SkuState
     */
    private boolean isValidState() {
        if (mCallbacks != null) {
            AddOnsState state = mCallbacks.getAddOnState();
            String skuAttribute = state.getSkuAttribute();
            if (TextUtils.isEmpty(skuAttribute) && mSelectorContainer != null) {
                return mSelectorContainer.getTotalOfAllCounters() > 0;
            } else if (!TextUtils.isEmpty(skuAttribute) && mSelectorContainer != null) {
                return mSelectorContainer.getTotalOfAllCounters() > 0
                        && !TextUtils.isEmpty(state.getSelectedSku());
            }
        }

        return false;
    }

    private void setStartingQuantity(@NonNull String age, @NonNull AddOnsState addOnsState, @Nullable TicketGroupOrder order) {
        int adult = 0;
        int child = 0;
        int allAges = 0;
        int maxQuantity = getMaxQuantity(addOnsState);
        if (addOnsState.getAgeQuantityMap().get(age) != null) {
            return;
        }
        // Attempt to pull starting quantity based on order
        if (order != null) {
            adult = order.getMaxNumberOfAdultTickets();
            child = order.getMaxNumberOfChildTickets();
        }
        // Attempt to pull starting quantity from extras response if order had 0 items for age
        if(addOnsState.getPersonalizationExtrasProduct() != null
                && (order == null || adult == 0 || child == 0)) {
            Integer quantity = addOnsState.getPersonalizationExtrasProduct().getStartingQuantityByAge(age);
            if (quantity != null) {
                if (TICKET_TYPE_ADULT.equalsIgnoreCase(age)) {
                    adult = quantity;
                } else if (TICKET_TYPE_CHILD.equalsIgnoreCase(age)) {
                    child = quantity;
                } else {
                    allAges = quantity;
                }
            }
        }
        // If values were unable to be pulled from the order or extras response, use defaults
        adult = adult != 0 ? adult : QUANTITY_DEFAULT_ADULT;
        child = child != 0 ? child : QUANTITY_DEFAULT_CHILD;
        allAges = allAges != 0 ? allAges : QUANTITY_DEFAULT_ALL_AGES;
        // Have to check  min quantity since an adult/allAges item may be required
        // and ensure the max quantity is not exceeded
        adult = Math.min(maxQuantity, Math.max(getMinQuanitity(age, addOnsState), adult));
        child = Math.min(maxQuantity, child);
        allAges = Math.min(maxQuantity, Math.max(getMinQuanitity(age, addOnsState), allAges));
        if (TICKET_TYPE_ADULT.equalsIgnoreCase(age)) {
            addOnsState.getAgeQuantityMap().put(age, adult);
        } else if (TICKET_TYPE_CHILD.equalsIgnoreCase(age)) {
            addOnsState.getAgeQuantityMap().put(age, child);
        } else {
            if (adult + child > QUANTITY_DEFAULT_ADULT) {
                addOnsState.getAgeQuantityMap().put(age, Math.min(maxQuantity, adult + child));
            } else {
                addOnsState.getAgeQuantityMap().put(age, allAges);
            }
        }
    }

    private int getMinQuanitity(@NonNull String age, @NonNull AddOnsState addOnsState) {
        int value;
        Integer adultQuantity = null;
        if (TICKET_TYPE_ADULT.equalsIgnoreCase(age)) {
            // Check if adult item has a min quantity if so use it as the min quantity
            if (addOnsState.getPersonalizationExtrasProduct() != null) {
                adultQuantity = addOnsState.getPersonalizationExtrasProduct().getMinQuantityByAge(age);
            }
            if (adultQuantity != null) {
                value = adultQuantity;
            } else {
                if (addOnsState.getAgeQuantityMap().containsKey(TICKET_TYPE_CHILD)
                        && addOnsState.getAgeQuantityMap().get(TICKET_TYPE_CHILD) != null
                        && addOnsState.getAgeQuantityMap().get(TICKET_TYPE_CHILD) > 0) {
                    value = 0;
                } else {
                    value = MIN_QUANTITY_DEFAULT;
                }
            }
        } else if (TICKET_TYPE_CHILD.equalsIgnoreCase(age)) {
            if (addOnsState.getAgeQuantityMap().containsKey(TICKET_TYPE_ADULT)
                    && addOnsState.getAgeQuantityMap().get(TICKET_TYPE_ADULT) != null
                    && addOnsState.getAgeQuantityMap().get(TICKET_TYPE_ADULT) <= 0) {
                value = MIN_QUANTITY_DEFAULT;
            } else {
                value = 0;
            }
        } else {
            value = MIN_QUANTITY_DEFAULT;
        }

        return value;
    }

    private int getMaxQuantity(@NonNull AddOnsState addOnsState) {
        int value = Integer.MAX_VALUE;
        if (addOnsState.getPersonalizationExtrasProduct() != null) {
            Integer maxQuantity = addOnsState.getPersonalizationExtrasProduct().getMaxQuantity();
            if (maxQuantity != null) {
                value = maxQuantity;
            }
        }
        return value;
    }
}
