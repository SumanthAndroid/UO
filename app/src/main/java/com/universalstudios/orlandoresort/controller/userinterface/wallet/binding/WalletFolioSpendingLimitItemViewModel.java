package com.universalstudios.orlandoresort.controller.userinterface.wallet.binding;

import android.databinding.Bindable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.NumberUtils;
import com.universalstudios.orlandoresort.model.network.domain.wallet.WalletTravelPartyMember;
import com.universalstudios.orlandoresort.view.binding.BaseObservableWithLayoutItem;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

/**
 * @author acampbell
 */
public class WalletFolioSpendingLimitItemViewModel extends BaseObservableWithLayoutItem {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LIMIT_TYPE_NO_CHARGE, LIMIT_TYPE_NO_LIMIT, LIMIT_TYPE_LIMIT_PER_DAY})
    @interface LimitType {
    }

    public static final int LIMIT_TYPE_NO_CHARGE = R.id.wallet_folio_item_spending_limit_limit_1;
    public static final int LIMIT_TYPE_NO_LIMIT = R.id.wallet_folio_item_spending_limit_limit_2;
    public static final int LIMIT_TYPE_LIMIT_PER_DAY = R.id.wallet_folio_item_spending_limit_limit_3;

    private WalletTravelPartyMember walletTravelParty;
    @Bindable
    private String name;
    @Bindable
    private String limitMessage;
    @Bindable
    private String sequenceId;
    @Bindable
    private boolean nearLimit;
    @Bindable
    private BigInteger limitAmount;
    @Bindable
    private int progress;
    @Bindable
    private boolean expanded;
    @Bindable
    @LimitType
    private int limitType;

    public WalletFolioSpendingLimitItemViewModel(@NonNull WalletTravelPartyMember walletTravelParty) {
        setWalletTravelParty(walletTravelParty);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_wallet_folio_spending_limit;
    }

    public WalletTravelPartyMember getWalletTravelParty() {
        return walletTravelParty;
    }

    public void setWalletTravelParty(@NonNull WalletTravelPartyMember walletTravelParty) {
        this.walletTravelParty = walletTravelParty;
        TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();
        setName(String.format(Locale.US, "%s %s %s", walletTravelParty.getFirstName(),
                walletTravelParty.getLastName(), StringUtils.trimToEmpty(walletTravelParty.getSuffix())));
        setSequenceId(walletTravelParty.getSequenceId());
        setNearLimit(walletTravelParty.getNearLimit() == null ? false : walletTravelParty.getNearLimit());
        if (walletTravelParty.getUnlimited() != null && walletTravelParty.getUnlimited()) {
            setLimitMessage(tridionConfig.getDailySpendingLimitOption2Label());
            setProgress(0);
            setLimitType(LIMIT_TYPE_NO_LIMIT);
        } else if (walletTravelParty.getSpendingLimit() != null && BigInteger.ZERO.equals(walletTravelParty.getSpendingLimit())) {
            setLimitMessage(tridionConfig.getDailySpendingLimitOption1Label());
            setProgress(0);
            setLimitType(LIMIT_TYPE_NO_CHARGE);
        } else if (walletTravelParty.getSpendingLimit() != null && walletTravelParty.getSpentAmount() != null) {
            String price1 = tridionConfig.getFormattedPrice(walletTravelParty.getSpentAmount());
            BigDecimal spendingLimit = new BigDecimal(walletTravelParty.getSpendingLimit());
            String price2 = tridionConfig.getFormattedPrice(spendingLimit);
            setLimitMessage(limitMessage = String.format(Locale.US, "%s / %s", price1, price2));

            BigDecimal spendingProgress = walletTravelParty.getSpentAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
            spendingProgress = spendingProgress
                    .divide(new BigDecimal(walletTravelParty.getSpendingLimit().max(BigInteger.ONE)), BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100));
            setProgress(Math.max(0, Math.min(100, spendingProgress.intValue())));
            setLimitType(LIMIT_TYPE_LIMIT_PER_DAY);
        }
        if (walletTravelParty.getSpendingLimit() != null
                && !WalletTravelPartyMember.getMaxLimit().equals(walletTravelParty.getSpendingLimit())) {
            setLimitAmount(walletTravelParty.getSpendingLimit());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getLimitMessage() {
        return limitMessage;
    }

    public void setLimitMessage(String limitMessage) {
        this.limitMessage = limitMessage;
        notifyPropertyChanged(BR.limitMessage);
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
        notifyPropertyChanged(BR.sequenceId);
    }

    public boolean isNearLimit() {
        return nearLimit;
    }

    public void setNearLimit(boolean nearLimit) {
        this.nearLimit = nearLimit;
        notifyPropertyChanged(BR.nearLimit);
    }

    public String getLimitAmount() {
        if (limitAmount == null) {
            return null;
        } else {
            return limitAmount.toString();
        }
    }

    public void setLimitAmount(String limitAmount) {
        this.limitAmount = NumberUtils.toBigInteger(limitAmount);
        notifyPropertyChanged(BR.limitAmount);
    }

    public void setLimitAmount(BigInteger limitAmount) {
        this.limitAmount = limitAmount;
        notifyPropertyChanged(BR.limitAmount);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        notifyPropertyChanged(BR.progress);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
        notifyPropertyChanged(BR.expanded);
    }

    public @LimitType int getLimitType() {
        return limitType;
    }

    public void setLimitType(@LimitType int limitType) {
        this.limitType = limitType;
        notifyPropertyChanged(BR.limitType);
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String input = s.toString();
        BigInteger bigInteger = NumberUtils.toBigInteger(input);

        if (bigInteger != null && bigInteger.equals(BigInteger.ZERO)){
            setLimitType(LIMIT_TYPE_NO_CHARGE);
        }
    }
}
