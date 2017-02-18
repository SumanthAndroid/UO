package com.universalstudios.orlandoresort.view.progress;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import org.parceler.Parcels;

import java.util.List;

/**
 *
 */
public class StepProgressView extends FrameLayout {
    private static final String KEY_STATE_SUPER = "KEY_STATE_SUPER";
    private static final String KEY_STATE_STEPS = "KEY_STATE_STEPS";
    private static final String KEY_STATE_CURRENT_STEP_TAG = "KEY_STATE_CURRENT_STEP_TAG";

    private LinearLayout mPathsLayout, mStepsLayout, mStepsTextLayout;
    private List<Step> mSteps;
    private String mCurrentStepTag;

    public StepProgressView(Context context) {
        super(context);
        init();
    }

    public StepProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StepProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    public StepProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        // Setup state for the tab button
        setSaveEnabled(true);
        setClickable(true);

        // Inflate the content containers
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.step_progress_view_content, this, true);

        // Get references to the child views
        mPathsLayout = (LinearLayout) findViewById(R.id.step_progress_view_content_path_layout);
        mStepsLayout = (LinearLayout) findViewById(R.id.step_progress_view_content_step_layout);
        mStepsTextLayout = (LinearLayout) findViewById(R.id.step_progress_view_content_step_text_layout);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_STATE_SUPER, super.onSaveInstanceState());

        // Save all state variables
        bundle.putParcelable(KEY_STATE_STEPS, Parcels.wrap(mSteps));
        bundle.putString(KEY_STATE_CURRENT_STEP_TAG, mCurrentStepTag);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            // Restore all state variables
            mSteps = Parcels.unwrap(bundle.getParcelable(KEY_STATE_STEPS));
            mCurrentStepTag = bundle.getString(KEY_STATE_CURRENT_STEP_TAG);

            // Update views
            setSteps(mSteps, mCurrentStepTag);
            refreshDrawableState();

            state = bundle.getParcelable(KEY_STATE_SUPER);
        }
        super.onRestoreInstanceState(state);
    }

    public void setSteps(List<Step> steps, String currentStepTag) {
        mSteps = steps;
        mCurrentStepTag = currentStepTag;
        buildStepViews(mSteps, mCurrentStepTag);
    }

    public void setCurrentStepTag(String currentStepTag) {
        mCurrentStepTag = currentStepTag;
        buildStepViews(mSteps, mCurrentStepTag);
    }

    private void buildStepViews(List<Step> steps, String currentStepTag) {
        mPathsLayout.removeAllViews();
        mStepsLayout.removeAllViews();
        mStepsTextLayout.removeAllViews();

        if (steps != null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            boolean atOrBeforeCurrentStep = true;
            for (int i = 0; i < steps.size(); i++) {
                boolean isFirstStep = (i == 0);
                boolean isLastStep = (i == steps.size() - 1);

                Step step = steps.get(i);

                // Create the step view
                View stepViewParent = inflater.inflate(R.layout.step_progress_view_step, mStepsLayout, false);
                ImageView stepImageView = (ImageView) stepViewParent.findViewById(R.id.step_progress_view_step_image);

                // Setup views
                setWeight(stepViewParent, isFirstStep, isLastStep);
                setGravity(stepImageView, isFirstStep, isLastStep);
                setImageLevel(stepImageView, atOrBeforeCurrentStep);
                mStepsLayout.addView(stepViewParent);

                // Create the step text view
                View stepTextViewParent = inflater.inflate(R.layout.step_progress_view_step_text, mStepsTextLayout, false);
                TextView stepTextView = (TextView) stepTextViewParent.findViewById(R.id.step_progress_view_step_text);

                // Setup views
                setWeight(stepTextViewParent, isFirstStep, isLastStep);
                setGravity(stepTextView, isFirstStep, isLastStep);
                stepTextView.setText(step.getText());
                setTextColor(stepTextView, atOrBeforeCurrentStep);
                mStepsTextLayout.addView(stepTextViewParent);

                if (i > 0) {
                    // Create the path view
                    View pathViewParent = inflater.inflate(R.layout.step_progress_view_path, mPathsLayout, false);
                    ImageView pathImageView = (ImageView) pathViewParent.findViewById(R.id.step_progress_view_path_image);

                    // Setup views
                    setWeight(pathViewParent, 1.0f);
                    setImageLevel(pathImageView, atOrBeforeCurrentStep);
                    mPathsLayout.addView(pathViewParent);
                }

                // Check to see if we are at the current step
                if (currentStepTag.equals(step.getTag())) {
                    atOrBeforeCurrentStep = false;
                }
            }
        }
        refreshDrawableState();
    }

    private static void setWeight(View view, boolean isFirstStep, boolean isLastStep) {
        if (isFirstStep || isLastStep) {
            setWeight(view, 1.0f);
        } else {
            setWeight(view, 2.0f);
        }
    }

    private static void setWeight(View view, float weight) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.weight = weight;
        view.setLayoutParams(params);
    }

    private static void setGravity(View view, boolean isFirstStep, boolean isLastStep) {
        if (isFirstStep) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.LEFT;
            view.setLayoutParams(params);
        } else if (isLastStep) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.RIGHT;
            view.setLayoutParams(params);
        } else {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.CENTER_HORIZONTAL;
            view.setLayoutParams(params);
        }

        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            if (isFirstStep) {
                textView.setGravity(Gravity.LEFT);
            } else if (isLastStep) {
                textView.setGravity(Gravity.RIGHT);
            } else {
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }
    }

    private void setImageLevel(ImageView imageView, boolean atOrBeforeCurrentStep) {
        Resources res = getResources();
        int imageLevel = atOrBeforeCurrentStep ? res.getInteger(R.integer.step_progress_complete) :
                res.getInteger(R.integer.step_progress_incomplete);
        imageView.setImageLevel(imageLevel);
    }

    private void setTextColor(TextView textView, boolean atOrBeforeCurrentStep) {
        Resources res = getResources();
        int textColor = atOrBeforeCurrentStep ? res.getColor(R.color.text_gray_darkest) :
                res.getColor(R.color.text_gray_light);
        textView.setTextColor(textColor);
    }
}
