package com.universalstudios.orlandoresort.view.password;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.network_config.TridionConfigStateManager;
import com.universalstudios.orlandoresort.view.TintUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 *
 */
public class PasswordStrengthView extends FrameLayout implements TextWatcher {
    private static final String KEY_STATE_SUPER = "KEY_STATE_SUPER";
    private static final String KEY_STATE_CURRENT_PASSWORD = "KEY_STATE_CURRENT_PASSWORD";

    private LinearLayout mStrengthMeterLayout;
    private TextView mStrengthText;
    private String mCurrentPassword;
    private int mNumStrengths, mUnfilledColor;
    private ArrayList<Integer> mStrengthColorList;
    private ArrayList<String> mStrengthTextList;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STRENGTH_SCORE_VERY_WEAK,
            STRENGTH_SCORE_WEAK,
            STRENGTH_SCORE_MEDIUM,
            STRENGTH_SCORE_MODERATE,
            STRENGTH_SCORE_STRONG})
    private @interface PasswordStrength {
    }

    // Declare the constants
    private static final int STRENGTH_SCORE_VERY_WEAK = 1;
    private static final int STRENGTH_SCORE_WEAK = 2;
    private static final int STRENGTH_SCORE_MEDIUM = 3;
    private static final int STRENGTH_SCORE_MODERATE = 4;
    private static final int STRENGTH_SCORE_STRONG = 5;
    private static final int MIN_STRENGTH_SCORE = STRENGTH_SCORE_VERY_WEAK;
    private static final int MAX_STRENGTH_SCORE = STRENGTH_SCORE_STRONG;

    public PasswordStrengthView(Context context) {
        super(context);

        // Initialize the view
        init(context);
    }

    public PasswordStrengthView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize the view
        init(context);
    }

    public PasswordStrengthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Initialize the view
        init(context);
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    public PasswordStrengthView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        // Initialize the view
        init(context);
    }

    private void init(Context context) {

        // Setup state for the tab button
        setSaveEnabled(true);
        setClickable(false);

        // Inflate the content containers
        LayoutInflater inflater = LayoutInflater.from(context);
        View content = inflater.inflate(R.layout.password_strength_view_content, this, false);
        addView(content);

        // Get references to the child views
        mStrengthMeterLayout = (LinearLayout) findViewById(R.id.password_strength_view_content_meter_layout);
        mStrengthText = (TextView) findViewById(R.id.password_strength_view_content_text);

        if (!isInEditMode()) {
            mStrengthMeterLayout.removeAllViews();

            // Get the remotely set strength colors and text
            TridionConfig config = TridionConfigStateManager.getInstance().getTridionConfig();
            mStrengthColorList = config.getPasswordStrengthColorList();
            mStrengthTextList = config.getPasswordStrengthTextList();
            mNumStrengths = Math.min(mStrengthColorList.size(), mStrengthTextList.size());
            mUnfilledColor = ContextCompat.getColor(context, R.color.password_strength_meter_unfilled);

            // Create each meter bar view and add them to the layout
            for (int i = 0; i < mNumStrengths; i++) {
                // Add each meter bar to the layout with equal weight
                View meterBarView = inflater.inflate(R.layout.password_strength_meter_item, mStrengthMeterLayout, false);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) meterBarView.getLayoutParams();
                params.weight = 1.0f;
                meterBarView.setLayoutParams(params);
                mStrengthMeterLayout.addView(meterBarView);
            }
        }

        // Update drawable state
        refreshDrawableState();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();

        // Save the super class state
        bundle.putParcelable(KEY_STATE_SUPER, super.onSaveInstanceState());

        // Save all state variables
        bundle.putString(KEY_STATE_CURRENT_PASSWORD, mCurrentPassword);

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            // Restore all state variables
            String currentPassword = bundle.getString(KEY_STATE_CURRENT_PASSWORD);

            // Update views
            updateCurrentPassword(currentPassword);

            // Restore the super class state
            state = bundle.getParcelable(KEY_STATE_SUPER);
        }
        super.onRestoreInstanceState(state);
    }

    public void bindPasswordEditText(EditText passwordEditText) {
        // Listen for any text changes
        passwordEditText.addTextChangedListener(this);

        // Set the password to what the input field has right now
        updateCurrentPassword(passwordEditText.getText().toString());
    }

    private void updateCurrentPassword(String newPassword) {
        mCurrentPassword = newPassword;

        if (TextUtils.isEmpty(mCurrentPassword)) {
            setVisibility(View.GONE);
        } else {
            // Rate the password
            int passwordRating = ratePasswordStrength(mCurrentPassword);

            // Make sure the rating is within the possible rating bounds
            if (passwordRating < MIN_STRENGTH_SCORE) {
                passwordRating = MIN_STRENGTH_SCORE;
            } else if (passwordRating > MAX_STRENGTH_SCORE) {
                passwordRating = MAX_STRENGTH_SCORE;
            }

            // Shift the rating so that the min score is 0
            int passwordRatingIndex = passwordRating - MIN_STRENGTH_SCORE;

            // Make sure rating index is between possible array of colors
            if (passwordRatingIndex < 0) {
                passwordRatingIndex = 0;
            } else if (passwordRatingIndex >= mNumStrengths) {
                passwordRatingIndex = mNumStrengths - 1;
            }

            // Set each meter bar to the right color
            for (int i = 0; i < mStrengthMeterLayout.getChildCount(); i++) {
                Drawable meterBarDrawable = mStrengthMeterLayout.getChildAt(i).getBackground().mutate();
                int colorToSet;

                // Set the meters up to the strength to their strength color
                if (i <= passwordRatingIndex) {
                    colorToSet = mStrengthColorList.get(passwordRatingIndex);
                }
                // Set meters above the strength to the unfilled color
                else {
                    colorToSet = mUnfilledColor;
                }
                TintUtils.tintDrawable(meterBarDrawable, colorToSet);
            }

            // Set the text to the proper rating
            String ratingText = mStrengthTextList.get(passwordRatingIndex);
            int filledColor = mStrengthColorList.get(passwordRatingIndex);
            mStrengthText.setText(ratingText);
            mStrengthText.setTextColor(filledColor);

            setVisibility(View.VISIBLE);
        }

        refreshDrawableState();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Do nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Do nothing
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Set the password to what the input field has right now
        updateCurrentPassword(s.toString());
    }

    public static @PasswordStrength int ratePasswordStrength(String password) {
        if (password != null) {
            int score = 0;
            int minLength = 8;
            int lengthInflectionPoint = 10;
            int magicFactor = 10;

            boolean hasLowercase = password.matches(".*[a-z].*");
            boolean hasUppercase = password.matches(".*[A-Z].*");
            boolean hasNumbers = password.matches(".*[0-9].*");
            boolean hasSymbols = password.matches(".*[-!$@#%^&*()_+|~=`{}\\[\\]:\";'<>?,.\\/].*");

            // Count how many complexities match
            int passedMatches = 0;
            passedMatches += hasLowercase ? 1 : 0;
            passedMatches += hasUppercase ? 1 : 0;
            passedMatches += hasNumbers ? 1 : 0;
            passedMatches += hasSymbols ? 1 : 0;

            // Add score for password length and complexities
            score += password.length() * 2 + ((password.length() >= lengthInflectionPoint) ? 1 : 0);
            score += passedMatches * magicFactor;

            // Adjust the score based on length
            if (password.length() < minLength) {
                score = Math.min(score, magicFactor);
            }

            // Adjust the score based on complexities
            if (passedMatches == 1) {
                score = Math.min(score, 1 * magicFactor);
            } else if (passedMatches == 2) {
                score = Math.min(score, 2 * magicFactor);
            } else if (passedMatches == 3) {
                score = Math.min(score, 4 * magicFactor);
            }

            int strengthScore;
            if (score <= 1 * magicFactor) {
                strengthScore = STRENGTH_SCORE_VERY_WEAK;
            } else if (score <= 2 * magicFactor) {
                strengthScore = STRENGTH_SCORE_WEAK;
            } else if (score <= 3 * magicFactor) {
                strengthScore = STRENGTH_SCORE_MEDIUM;
            } else if (score <= 4 * magicFactor) {
                strengthScore = STRENGTH_SCORE_MODERATE;
            } else {
                strengthScore = STRENGTH_SCORE_STRONG;
            }
            return strengthScore;
        }
        return STRENGTH_SCORE_VERY_WEAK;
    }
}
