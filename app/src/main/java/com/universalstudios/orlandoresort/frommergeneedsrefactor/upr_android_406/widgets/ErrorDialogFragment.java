package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.widgets;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.universalstudios.orlandoresort.R;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.uo_network_library.view.fonts.Button;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.uo_network_library.view.fonts.TextView;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IBM_ADMIN on 6/20/2016.
 */
public class ErrorDialogFragment extends DialogFragment implements View.OnClickListener {

    private String title;
    private String primaryMessage;
    private String secondaryMessage;
    private int resIcon;
    private View mRootView;
    private View mTitleLayout;
    private ImageView title_icon_View;
    private TextView title_textView;
    private ImageButton cancel_btn;
    private String actionButtonText, extraButtonText;
    private boolean titleBarVisible;
    private Bitmap imageBitmap;
    private ImageView errorImageView;

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    /**
     * This is the primary button for submit or reset or similar actions.
     */
    private Button actionButton;
    /**
     * This is an extra button in case the error requires it.
     */
    private Button extraButton;
    private OnButtonActionListener cancelListener, actionButtonListener, extraButtonListener;
    private TextView primaryMsgTextView;
    private TextView secondaryMsgTextView;
    private boolean isFullScreen;
    private static final String KEY_EVENT_61 = "event61";
    private static final String KEY_EVENT_NAME = "event_name";

    public static ErrorDialogFragment newInstance(String title, String primaryMessage, String secondaryMessage, int resIcon) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle args = getBundle(title, primaryMessage, secondaryMessage, resIcon, false);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    public static ErrorDialogFragment newInstance(String title, String primaryMessage, String secondaryMessage, int resIcon, boolean isFullScreen) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle args = getBundle(title, primaryMessage, secondaryMessage, resIcon, isFullScreen);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    public static ErrorDialogFragment newInstance(String title, String primaryMessage, String secondaryMessage, boolean isFullScreen) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle args = getBundle(title, primaryMessage, secondaryMessage, 0, isFullScreen);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }


    public static ErrorDialogFragment newInstance(String title, String primaryMessage, String secondaryMessage) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle args = getBundle(title, primaryMessage, secondaryMessage, 0, false);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    private static Bundle getBundle(String title, String primaryMessage, String secondaryMessage, int resIcon, boolean isFullScreen) {
        Bundle args = new Bundle();
        if (title != null) {
            args.putString("title", title);
        }
        if (primaryMessage != null) {
            args.putString("primaryMessage", primaryMessage);
        }
        if (secondaryMessage != null) {
            args.putString("secondaryMessage", secondaryMessage);
        }
        if (resIcon > 0) {
            args.putInt("resIcon", resIcon);
        }
        args.putBoolean("isFullScreen", isFullScreen);
        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        title = getArguments().getString("title");
        primaryMessage = getArguments().getString("primaryMessage");
        secondaryMessage = getArguments().getString("secondaryMessage");
        resIcon = getArguments().getInt("resIcon");
        isFullScreen = getArguments().getBoolean("isFullScreen");
        if (isFullScreen) {
            setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.dialog_fragment_error, container, false);
        return mRootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTitleLayout = mRootView.findViewById(R.id.titleLayout);
        title_icon_View = (ImageView) mRootView.findViewById(R.id.title_icon);
        title_textView = (TextView) mRootView.findViewById(R.id.title_textView);
        cancel_btn = (ImageButton) mRootView.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(this);
        actionButton = (Button) mRootView.findViewById(R.id.button_1);
        actionButton.setOnClickListener(this);
        extraButton = (Button) mRootView.findViewById(R.id.button_2);
        extraButton.setOnClickListener(this);
        errorImageView = (ImageView) mRootView.findViewById(R.id.error_image_view);

        if(imageBitmap != null) {
            errorImageView.setImageBitmap(imageBitmap);
        } else {
            errorImageView.setVisibility(View.GONE);
        }
        if (resIcon > 0) {
            title_icon_View.setVisibility(View.VISIBLE);
            title_icon_View.setImageResource(resIcon);
        } else {
            title_icon_View.setVisibility(View.GONE);
        }
        if (title != null) {
            title_textView.setText(title);
            title_textView.setVisibility(View.VISIBLE);
        } else {
            title_textView.setVisibility(View.GONE);
        }
        primaryMsgTextView = (TextView) mRootView.findViewById(R.id.primary_msg_textView);
        if (primaryMessage != null) {
            primaryMsgTextView.setText(primaryMessage);
            primaryMsgTextView.setVisibility(View.VISIBLE);
        } else {
            primaryMsgTextView.setVisibility(View.GONE);
        }
        secondaryMsgTextView = (TextView) mRootView.findViewById(R.id.secondary_msg_textView);
        if (secondaryMessage != null) {
            secondaryMsgTextView.setVisibility(View.VISIBLE);
            secondaryMsgTextView.setText(secondaryMessage);
        } else {
            secondaryMsgTextView.setVisibility(View.GONE);
        }
        setActionButtonText(actionButtonText);
        setExtraButtonText(extraButtonText);
        setTitleBarVisible(titleBarVisible);
        setTitleBarIcon(resIcon);
    }

    /**
     * This is to set the text for the action button.
     * @param button1Text Text for the button
     */
    public void setActionButtonText(String button1Text) {
        actionButtonText = button1Text;
        if (button1Text != null && actionButton != null) {
            actionButton.setVisibility(View.VISIBLE);
            actionButton.setText(button1Text);
        } else if (actionButton != null){
            actionButton.setVisibility(View.GONE);
        }
    }

    /**
     * This is to set the text for the extra button.
     * @param button2Text Text for the button
     */
    public void setExtraButtonText(String button2Text) {
        extraButtonText = button2Text;
        if (button2Text != null && extraButton != null) {
            extraButton.setVisibility(View.VISIBLE);
            extraButton.setText(button2Text);
        } else if( extraButton != null){
            extraButton.setVisibility(View.GONE);
        }
    }

    public void setTitleBarVisible(boolean visible) {
        titleBarVisible = visible;
        if (mTitleLayout != null) {
            if (visible) {
                mTitleLayout.setVisibility(View.VISIBLE);
            } else {
                mTitleLayout.setVisibility(View.GONE);
            }
        }
    }

    public void setTitleBarIcon(int icon_res_id) {
        resIcon = icon_res_id;
        if (title_icon_View != null) {
            if (resIcon > 0) {
                title_icon_View.setVisibility(View.VISIBLE);
                title_icon_View.setImageResource(icon_res_id);
            } else {
                title_icon_View.setVisibility(View.GONE);
            }
        }
    }

    public void setTitle(String title) {
        if (title_textView != null) {
            if (title != null) {
                title_textView.setVisibility(View.VISIBLE);
                title_textView.setText(title);
            } else {
                title_textView.setVisibility(View.GONE);
            }
        }
    }

    public void setOnCancelButtonActionListener(OnButtonActionListener onButtonActionListener) {
        cancelListener = onButtonActionListener;
    }

    public void setOnButton1ActionListener(OnButtonActionListener onButtonActionListener) {
        actionButtonListener = onButtonActionListener;
    }

    public void setOnButton2ActionListener(OnButtonActionListener onButtonActionListener) {
        extraButtonListener = onButtonActionListener;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cancel_btn) {
            if (cancelListener != null) {
                cancelListener.OnButtonClick();
            }
            this.dismiss();
        } else if (id == R.id.button_1) {
            if (actionButtonListener != null) {
                actionButtonListener.OnButtonClick();
            }
        } else if (id == R.id.button_2) {
            if (extraButtonListener != null) {
                extraButtonListener.OnButtonClick();
            }
        }
        sendOnClickAnalytics();
    }

    public interface OnButtonActionListener {
        public void OnButtonClick();
    }

    private void sendOnClickAnalytics () {
        Map<String, Object> extraData = new HashMap<String, Object>();
        extraData.put(KEY_EVENT_61, "event61 as counter");
        extraData.put(KEY_EVENT_NAME, "Error Full Page");
        AnalyticsUtils.trackPageView(
                AnalyticsUtils.CONTENT_GROUP_SITE_NAV,
                AnalyticsUtils.CONTENT_FOCUS_ERROR,
                null,
                AnalyticsUtils.CONTENT_SUB_2_FULL_PAGE,
                null,
                null,
                extraData);
    }
}
